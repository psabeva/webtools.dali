/*******************************************************************************
 * Copyright (c) 2012, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.orm;

import java.util.List;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.iterable.EmptyListIterable;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.internal.iterable.SingleElementListIterable;
import org.eclipse.jpt.common.utility.internal.iterable.SuperListIterableWrapper;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.jpa.core.context.DiscriminatorType;
import org.eclipse.jpt.jpa.core.context.Entity;
import org.eclipse.jpt.jpa.core.context.InheritanceType;
import org.eclipse.jpt.jpa.core.context.JpaContextModel;
import org.eclipse.jpt.jpa.core.context.NamedColumn;
import org.eclipse.jpt.jpa.core.context.NamedDiscriminatorColumn;
import org.eclipse.jpt.jpa.core.context.TypeMapping;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.context.orm.AbstractOrmXmlContextModel;
import org.eclipse.jpt.jpa.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt.jpa.db.Table;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkEntity;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkMappedSuperclass;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkMultitenantType2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkSpecifiedTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkVirtualTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.java.EclipseLinkJavaMultitenancy2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.java.EclipseLinkJavaNonEmbeddableTypeMapping;
import org.eclipse.jpt.jpa.eclipselink.core.context.java.EclipseLinkJavaSpecifiedTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.EclipseLinkEntityMappings;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.EclipseLinkOrmMultitenancy2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.OrmEclipseLinkNonEmbeddableTypeMapping;
import org.eclipse.jpt.jpa.eclipselink.core.context.orm.OrmSpecifiedTenantDiscriminatorColumn2_3;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.TargetDatabase;
import org.eclipse.jpt.jpa.eclipselink.core.internal.EclipseLink2_4JpaPlatformFactory;
import org.eclipse.jpt.jpa.eclipselink.core.internal.EclipseLinkJpaPlatformFactory.EclipseLinkJpaPlatformVersion;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.TenantDiscriminatorColumnValidator2_3;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlMultitenant;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlMultitenantHolder;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.XmlTenantDiscriminatorColumn;
import org.eclipse.jpt.jpa.eclipselink.core.resource.orm.v2_4.XmlMultitenant_2_4;
import org.eclipse.jpt.jpa.eclipselink.core.validation.JptJpaEclipseLinkCoreValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

public class OrmEclipseLinkMultitenancyImpl2_3
	extends AbstractOrmXmlContextModel<OrmEclipseLinkNonEmbeddableTypeMapping>
	implements EclipseLinkOrmMultitenancy2_3
{
	protected boolean specifiedMultitenant;
	protected boolean defaultMultitenant;

	protected EclipseLinkMultitenantType2_3 specifiedType;
	protected EclipseLinkMultitenantType2_3 defaultType;

	protected Boolean specifiedIncludeCriteria;
	protected boolean defaultIncludeCriteria = DEFAULT_INCLUDE_CRITERIA;

	protected final ContextListContainer<OrmSpecifiedTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn> specifiedTenantDiscriminatorColumnContainer;
	protected final EclipseLinkTenantDiscriminatorColumn2_3.ParentAdapter tenantDiscriminatorColumnParentAdapter;

	protected final ContextListContainer<EclipseLinkVirtualTenantDiscriminatorColumn2_3, EclipseLinkTenantDiscriminatorColumn2_3> defaultTenantDiscriminatorColumnContainer;

	protected final EclipseLinkTenantDiscriminatorColumn2_3 defaultTenantDiscriminatorColumn;

	protected boolean specifiedTenantDiscriminatorColumnsAllowed;

	public OrmEclipseLinkMultitenancyImpl2_3(OrmEclipseLinkNonEmbeddableTypeMapping parent) {
		super(parent);

		this.specifiedMultitenant = isMultitenantElementSpecified();
		this.specifiedType = this.buildSpecifiedType();
		this.specifiedIncludeCriteria = this.buildSpecifiedIncludeCriteria();
		this.tenantDiscriminatorColumnParentAdapter = this.buildTenantDiscriminatorColumnParentAdapter();
		this.specifiedTenantDiscriminatorColumnContainer = this.buildSpecifiedTenantDiscriminatorColumnContainer();
		this.defaultTenantDiscriminatorColumn = this.buildTenantDiscriminatorColumn(null);
		this.defaultTenantDiscriminatorColumnContainer = this.buildDefaultTenantDiscriminatorColumnContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedMultitenant_(isMultitenantElementSpecified());
		this.setSpecifiedType_(this.buildSpecifiedType());
		this.setSpecifiedIncludeCriteria_(this.buildSpecifiedIncludeCriteria());
		this.syncSpecifiedTenantDiscriminatorColumns();
	}

	@Override
	public void update() {
		super.update();
		this.updateModels(this.getSpecifiedTenantDiscriminatorColumns());

		boolean xmlMultitenantNotSpecified = (this.getXmlMultitenant() == null);
		EclipseLinkJavaMultitenancy2_3 javaMultitenantPolicy = this.getJavaMultitenancyPolicyForDefaults();
		boolean javaMultitenantPolicySpecified = javaMultitenantPolicy != null;
		boolean useJavaValue = (xmlMultitenantNotSpecified && javaMultitenantPolicySpecified);

		this.setDefaultMultitenant(useJavaValue ? javaMultitenantPolicy.isMultitenant() : this.buildDefaultMultitenant());
		this.setDefaultType(useJavaValue ? javaMultitenantPolicy.getType() : this.buildDefaultType());
		this.setDefaultIncludeCriteria(useJavaValue ? javaMultitenantPolicy.isIncludeCriteria() : this.buildDefaultIncludeCriteria());
		this.setSpecifiedTenantDiscriminatorColumnsAllowed(this.buildSpecifiedTenantDiscriminatorColumnsAllowed());
		this.updateDefaultTenantDiscriminatorColumns();
	}


	// ********** multitenant **********

	public boolean isMultitenant() {
		return isSpecifiedMultitenant() || isDefaultMultitenant();
	}

	public boolean isDefaultMultitenant() {
		return this.defaultMultitenant;
	}

	public void setDefaultMultitenant(boolean defaultMultitenant) {
		boolean old = this.defaultMultitenant;
		this.defaultMultitenant = defaultMultitenant;
		this.firePropertyChanged(DEFAULT_MULTITENANT_PROPERTY, old, defaultMultitenant);
	}

	protected boolean buildDefaultMultitenant() {
		return this.isMultitenantInheritanceHierarchy() || this.isSuperMappedSuperclassMultitenant();
	}

	public boolean isSpecifiedMultitenant() {
		return this.specifiedMultitenant;
	}

	public void setSpecifiedMultitenant(boolean isMultitenant) {
		if (isMultitenant) {
			if (getXmlMultitenant() != null) {
				throw new IllegalStateException("Multitenant element already specified"); //$NON-NLS-1$				
			}
			this.addXmlMultitenant();
		}
		else {
			if (getXmlMultitenant() == null) {
				throw new IllegalStateException("Multitenant element does not exist"); //$NON-NLS-1$				
			}
			this.removeXmlMultitenant();
			this.setSpecifiedType(null);
			for (int i = this.getSpecifiedTenantDiscriminatorColumnsSize(); i-- > 0; ) {
				this.removeSpecifiedTenantDiscriminatorColumn(i);
			}
		}
		this.setSpecifiedMultitenant_(isMultitenant);
	}

	protected void setSpecifiedMultitenant_(boolean isMultitenant) {
		boolean old = this.specifiedMultitenant;
		this.specifiedMultitenant = isMultitenant;
		this.firePropertyChanged(SPECIFIED_MULTITENANT_PROPERTY, old, isMultitenant);
	}


	// ********** type **********

	public EclipseLinkMultitenantType2_3 getType() {
		return (this.specifiedType != null) ? this.specifiedType : this.getDefaultType();
	}

	public EclipseLinkMultitenantType2_3 getSpecifiedType() {
		return this.specifiedType;
	}

	public void setSpecifiedType(EclipseLinkMultitenantType2_3 type) {
		if (this.valuesAreDifferent(this.specifiedType, type)) {
			XmlMultitenant xmlMultitenant = this.getXmlMultitenantForUpdate();
			this.setSpecifiedType_(type);
			xmlMultitenant.setType(EclipseLinkMultitenantType2_3.toOrmResourceModel(type));
		}
	}

	protected void setSpecifiedType_(EclipseLinkMultitenantType2_3 type) {
		EclipseLinkMultitenantType2_3 old = this.specifiedType;
		this.specifiedType = type;
		this.firePropertyChanged(SPECIFIED_TYPE_PROPERTY, old, type);
	}

	protected EclipseLinkMultitenantType2_3 buildSpecifiedType() {
		XmlMultitenant xmlMultitenant = this.getXmlMultitenant();
		return (xmlMultitenant == null) ? null : EclipseLinkMultitenantType2_3.fromOrmResourceModel(xmlMultitenant.getType());
	}

	public EclipseLinkMultitenantType2_3 getDefaultType() {
		return this.defaultType;
	}

	protected void setDefaultType(EclipseLinkMultitenantType2_3 type) {
		EclipseLinkMultitenantType2_3 old = this.defaultType;
		this.defaultType = type;
		this.firePropertyChanged(DEFAULT_TYPE_PROPERTY, old, type);
	}

	protected EclipseLinkMultitenantType2_3 buildDefaultType() {
		if (!isMultitenant()) {
			return null;
		}
		if (isSpecifiedMultitenant()) {
			return DEFAULT_TYPE;
		}
		if (this.isMultitenantInheritanceHierarchy()) {
			return getRootEntity().getMultitenancy().getType();
		}
		EclipseLinkMappedSuperclass superMappedSuperclass = getSuperMappedSuperclass();
		return superMappedSuperclass != null ? superMappedSuperclass.getMultitenancy().getType() : DEFAULT_TYPE;
	}


	// ********** include criteria **********

	public boolean isIncludeCriteria() {
		return (this.specifiedIncludeCriteria != null) ? this.specifiedIncludeCriteria.booleanValue() : this.defaultIncludeCriteria;
	}

	public Boolean getSpecifiedIncludeCriteria() {
		return this.specifiedIncludeCriteria;
	}

	public void setSpecifiedIncludeCriteria(Boolean includeCriteria) {
		if (this.valuesAreDifferent(this.specifiedIncludeCriteria, includeCriteria)) {
			XmlMultitenant xmlMultitenant = this.getXmlMultitenantForUpdate();
			this.setSpecifiedIncludeCriteria_(includeCriteria);
			xmlMultitenant.setIncludeCriteria(includeCriteria);
		}
	}

	protected void setSpecifiedIncludeCriteria_(Boolean includeCriteria) {
		Boolean old = this.specifiedIncludeCriteria;
		this.specifiedIncludeCriteria = includeCriteria;
		this.firePropertyChanged(SPECIFIED_INCLUDE_CRITERIA_PROPERTY, old, includeCriteria);
	}

	protected Boolean buildSpecifiedIncludeCriteria() {
		XmlMultitenant xmlMultitenant = this.getXmlMultitenant();
		return (xmlMultitenant == null) ? null : xmlMultitenant.getIncludeCriteria();
	}


	public boolean isDefaultIncludeCriteria() {
		return this.defaultIncludeCriteria;
	}

	protected void setDefaultIncludeCriteria(boolean includeCriteria) {
		boolean old = this.defaultIncludeCriteria;
		this.defaultIncludeCriteria = includeCriteria;
		this.firePropertyChanged(DEFAULT_INCLUDE_CRITERIA_PROPERTY, old, includeCriteria);
	}

	protected boolean buildDefaultIncludeCriteria() {
		if (getType() == EclipseLinkMultitenantType2_3.VPD) {
			return false;
		}
		return DEFAULT_INCLUDE_CRITERIA;
	}


	// ********** tenant discriminator columns **********

	public ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getTenantDiscriminatorColumns() {
		return this.hasSpecifiedTenantDiscriminatorColumns() ?
			this.getReadOnlySpecifiedTenantDiscriminatorColumns() : 
			this.getReadOnlyDefaultTenantDiscriminatorColumns();
	}

	public int getTenantDiscriminatorColumnsSize() {
		return this.hasSpecifiedTenantDiscriminatorColumns() ?
			this.getSpecifiedTenantDiscriminatorColumnsSize() : 
			this.getDefaultTenantDiscriminatorColumnsSize();
	}

	// ********** specified tenant discriminator columns **********

	public ListIterable<OrmSpecifiedTenantDiscriminatorColumn2_3> getSpecifiedTenantDiscriminatorColumns() {
		return this.specifiedTenantDiscriminatorColumnContainer.getContextElements();
	}

	protected ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getReadOnlySpecifiedTenantDiscriminatorColumns() {
		return new SuperListIterableWrapper<EclipseLinkTenantDiscriminatorColumn2_3>(this.getSpecifiedTenantDiscriminatorColumns());
	}

	public int getSpecifiedTenantDiscriminatorColumnsSize() {
		return this.specifiedTenantDiscriminatorColumnContainer.getContextElementsSize();
	}

	public boolean hasSpecifiedTenantDiscriminatorColumns() {
		return this.getSpecifiedTenantDiscriminatorColumnsSize() != 0;
	}

	public OrmSpecifiedTenantDiscriminatorColumn2_3 addSpecifiedTenantDiscriminatorColumn() {
		return this.addSpecifiedTenantDiscriminatorColumn(this.getSpecifiedTenantDiscriminatorColumnsSize());
	}

	public OrmSpecifiedTenantDiscriminatorColumn2_3 addSpecifiedTenantDiscriminatorColumn(int index) {
		XmlTenantDiscriminatorColumn xmlJoinColumn = this.buildXmlTenantDiscriminatorColumn();
		OrmSpecifiedTenantDiscriminatorColumn2_3 joinColumn = this.specifiedTenantDiscriminatorColumnContainer.addContextElement(index, xmlJoinColumn);
		this.getXmlMultitenantForUpdate().getTenantDiscriminatorColumns().add(index, xmlJoinColumn);
		return joinColumn;
	}

	protected XmlTenantDiscriminatorColumn buildXmlTenantDiscriminatorColumn() {
		return EclipseLinkOrmFactory.eINSTANCE.createXmlTenantDiscriminatorColumn();
	}

	public void removeSpecifiedTenantDiscriminatorColumn(EclipseLinkSpecifiedTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		this.removeSpecifiedTenantDiscriminatorColumn(this.specifiedTenantDiscriminatorColumnContainer.indexOfContextElement((OrmSpecifiedTenantDiscriminatorColumn2_3) tenantDiscriminatorColumn));
	}

	public void removeSpecifiedTenantDiscriminatorColumn(int index) {
		this.specifiedTenantDiscriminatorColumnContainer.removeContextElement(index);
		this.getXmlMultitenant().getTenantDiscriminatorColumns().remove(index);
	}

	public void moveSpecifiedTenantDiscriminatorColumn(int targetIndex, int sourceIndex) {
		this.specifiedTenantDiscriminatorColumnContainer.moveContextElement(targetIndex, sourceIndex);
		this.getXmlMultitenant().getTenantDiscriminatorColumns().move(targetIndex, sourceIndex);
	}


	protected void syncSpecifiedTenantDiscriminatorColumns() {
		this.specifiedTenantDiscriminatorColumnContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<XmlTenantDiscriminatorColumn> getXmlTenantDiscriminatorColumns() {
		if (getXmlMultitenant() == null) {
			return EmptyListIterable.instance();
		}
		// clone to reduce chance of concurrency problems
		return IterableTools.cloneLive(this.getXmlMultitenant().getTenantDiscriminatorColumns());
	}

	/**
	 *  specified tenant discriminator column container
	 */
	protected class SpecifiedTenantDiscriminatorColumnContainer
		extends ContextListContainer<OrmSpecifiedTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return SPECIFIED_TENANT_DISCRIMINATOR_COLUMNS_LIST;
		}
		@Override
		protected OrmSpecifiedTenantDiscriminatorColumn2_3 buildContextElement(XmlTenantDiscriminatorColumn resourceElement) {
			return OrmEclipseLinkMultitenancyImpl2_3.this.buildTenantDiscriminatorColumn(resourceElement);
		}
		@Override
		protected ListIterable<XmlTenantDiscriminatorColumn> getResourceElements() {
			return OrmEclipseLinkMultitenancyImpl2_3.this.getXmlTenantDiscriminatorColumns();
		}
		@Override
		protected XmlTenantDiscriminatorColumn getResourceElement(OrmSpecifiedTenantDiscriminatorColumn2_3 contextElement) {
			return contextElement.getXmlColumn();
		}
	}

	protected EclipseLinkTenantDiscriminatorColumn2_3.ParentAdapter buildTenantDiscriminatorColumnParentAdapter() {
		return new TenantDiscriminatorColumnParentAdapter();
	}

	protected ContextListContainer<OrmSpecifiedTenantDiscriminatorColumn2_3, XmlTenantDiscriminatorColumn> buildSpecifiedTenantDiscriminatorColumnContainer() {
		SpecifiedTenantDiscriminatorColumnContainer container = new SpecifiedTenantDiscriminatorColumnContainer();
		container.initialize();
		return container;
	}

	protected OrmSpecifiedTenantDiscriminatorColumn2_3 buildTenantDiscriminatorColumn(XmlTenantDiscriminatorColumn xmlTenantDiscriminatorColumn) {
		return new EclipseLinkOrmTenantDiscriminatorColumn2_3(this.tenantDiscriminatorColumnParentAdapter, xmlTenantDiscriminatorColumn);
	}


	// ********** default tenant discriminator columns **********

	public ListIterable<EclipseLinkVirtualTenantDiscriminatorColumn2_3> getDefaultTenantDiscriminatorColumns() {
		return this.defaultTenantDiscriminatorColumnContainer.getContextElements();
	}

	protected ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getReadOnlyDefaultTenantDiscriminatorColumns() {
		return new SuperListIterableWrapper<EclipseLinkTenantDiscriminatorColumn2_3>(this.getDefaultTenantDiscriminatorColumns());
	}

	public int getDefaultTenantDiscriminatorColumnsSize() {
		return this.defaultTenantDiscriminatorColumnContainer.getContextElementsSize();
	}

	protected void clearDefaultTenantDiscriminatorColumns() {
		this.defaultTenantDiscriminatorColumnContainer.clearContextList();
	}

	/**
	 * If there are any specified tenant discriminator columns, then there are no default
	 * tenant discriminator columns.
	 * If there are Java specified tenant discriminator columns, then those are the default
	 * tenant discriminator columns.
	 * @see #getTenantDiscriminatorColumnsForDefaults()
	 */
	protected void updateDefaultTenantDiscriminatorColumns() {
		this.defaultTenantDiscriminatorColumnContainer.update();
	}

	protected ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getTenantDiscriminatorColumnsForDefaults() {
		if (this.getType() == null || this.getType() == EclipseLinkMultitenantType2_3.TABLE_PER_TENANT) {
			return EmptyListIterable.instance();
		}
		EclipseLinkJavaMultitenancy2_3 javaMultitenancy = this.getJavaMultitenancyPolicyForDefaults();
		if (javaMultitenancy != null && !this.isSpecifiedMultitenant()) {
			if (javaMultitenancy.hasSpecifiedTenantDiscriminatorColumns()) {
				return new SuperListIterableWrapper<EclipseLinkTenantDiscriminatorColumn2_3>(javaMultitenancy.getSpecifiedTenantDiscriminatorColumns());
			}
		}
		if (this.isMultitenantInheritanceHierarchy()) {
			return new SuperListIterableWrapper<EclipseLinkTenantDiscriminatorColumn2_3>(this.getRootEntity().getMultitenancy().getTenantDiscriminatorColumns());
		}
		if (!isSpecifiedMultitenant()) {
			EclipseLinkMappedSuperclass superMappedSuperclass = this.getSuperMappedSuperclass();
			if (superMappedSuperclass != null && superMappedSuperclass.getMultitenancy().isMultitenant()) {
				return new SuperListIterableWrapper<EclipseLinkTenantDiscriminatorColumn2_3>(superMappedSuperclass.getMultitenancy().getTenantDiscriminatorColumns());
			}
		}
		if (this.getSpecifiedTenantDiscriminatorColumnsSize() == 0) {
			ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> contextColumns = this.getContextDefaultTenantDiscriminatorColumns();
			if (IterableTools.isEmpty(contextColumns)) {
				return new SingleElementListIterable<EclipseLinkTenantDiscriminatorColumn2_3>(this.defaultTenantDiscriminatorColumn);
			}
			return contextColumns;
		}
		return EmptyListIterable.instance();
	}

	protected ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getContextDefaultTenantDiscriminatorColumns() {
		EclipseLinkEntityMappings entityMappings = this.getEclipseLinkEntityMappings();
		return (entityMappings != null) ? entityMappings.getTenantDiscriminatorColumns() : this.getPersistenceUnit().getDefaultTenantDiscriminatorColumns();
	}


	protected void moveDefaultTenantDiscriminatorColumn(int index, EclipseLinkVirtualTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		this.defaultTenantDiscriminatorColumnContainer.moveContextElement(index, tenantDiscriminatorColumn);
	}

	protected EclipseLinkVirtualTenantDiscriminatorColumn2_3 addDefaultTenantDiscriminatorColumn(int index, EclipseLinkJavaSpecifiedTenantDiscriminatorColumn2_3 javaTenantDiscriminatorColumn) {
		return this.defaultTenantDiscriminatorColumnContainer.addContextElement(index, javaTenantDiscriminatorColumn);
	}

	protected EclipseLinkVirtualTenantDiscriminatorColumn2_3 buildVirtualTenantDiscriminatorColumn(EclipseLinkTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		return new EclipseLinkOrmVirtualTenantDiscriminatorColumn2_3(this.tenantDiscriminatorColumnParentAdapter, tenantDiscriminatorColumn);
	}

	protected void removeDefaultTenantDiscriminatorColumn(EclipseLinkVirtualTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn) {
		this.defaultTenantDiscriminatorColumnContainer.removeContextElement(tenantDiscriminatorColumn);
	}

	protected ContextListContainer<EclipseLinkVirtualTenantDiscriminatorColumn2_3, EclipseLinkTenantDiscriminatorColumn2_3> buildDefaultTenantDiscriminatorColumnContainer() {
		return new DefaultTenantDiscriminatorColumnContainer();
	}

	public boolean specifiedTenantDiscriminatorColumnsAllowed() {
		return this.specifiedTenantDiscriminatorColumnsAllowed;
	}

	public void setSpecifiedTenantDiscriminatorColumnsAllowed(boolean allowed) {
		boolean old = this.specifiedTenantDiscriminatorColumnsAllowed;
		this.specifiedTenantDiscriminatorColumnsAllowed = allowed;
		this.firePropertyChanged(SPECIFIED_TENANT_DISCRIMINATOR_COLUMNS_ALLOWED_PROPERTY, old, allowed);
	}

	protected boolean buildSpecifiedTenantDiscriminatorColumnsAllowed() {
		return this.parent.isMultitenantMetadataAllowed();
	}


	/**
	 * default tenant discriminator column container
	 */
	protected class DefaultTenantDiscriminatorColumnContainer
		extends ContextListContainer<EclipseLinkVirtualTenantDiscriminatorColumn2_3, EclipseLinkTenantDiscriminatorColumn2_3>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return DEFAULT_TENANT_DISCRIMINATOR_COLUMNS_LIST;
		}
		@Override
		protected EclipseLinkVirtualTenantDiscriminatorColumn2_3 buildContextElement(EclipseLinkTenantDiscriminatorColumn2_3 resourceElement) {
			return OrmEclipseLinkMultitenancyImpl2_3.this.buildVirtualTenantDiscriminatorColumn(resourceElement);
		}
		@Override
		protected ListIterable<EclipseLinkTenantDiscriminatorColumn2_3> getResourceElements() {
			return OrmEclipseLinkMultitenancyImpl2_3.this.getTenantDiscriminatorColumnsForDefaults();
		}
		@Override
		protected EclipseLinkTenantDiscriminatorColumn2_3 getResourceElement(EclipseLinkVirtualTenantDiscriminatorColumn2_3 contextElement) {
			return contextElement.getOverriddenColumn();
		}
	}


	// ********** tenant discriminator column parent adapter **********

	public class TenantDiscriminatorColumnParentAdapter
		implements EclipseLinkTenantDiscriminatorColumn2_3.ParentAdapter
	{
		public JpaContextModel getColumnParent() {
			return OrmEclipseLinkMultitenancyImpl2_3.this;
		}

		public String getDefaultContextPropertyName() {
			return EclipseLinkTenantDiscriminatorColumn2_3.DEFAULT_CONTEXT_PROPERTY;
		}

		public boolean getDefaultPrimaryKey() {
			return EclipseLinkTenantDiscriminatorColumn2_3.DEFAULT_PRIMARY_KEY;
		}

		public int getDefaultLength() {
			return NamedDiscriminatorColumn.DEFAULT_LENGTH;
		}

		public DiscriminatorType getDefaultDiscriminatorType() {
			return NamedDiscriminatorColumn.DEFAULT_DISCRIMINATOR_TYPE;
		}

		public String getDefaultTableName() {
			return getTypeMapping().getPrimaryTableName();
		}

		public String getDefaultColumnName(NamedColumn column) {
			return EclipseLinkTenantDiscriminatorColumn2_3.DEFAULT_NAME;
		}

		public Table resolveDbTable(String tableName) {
			return getTypeMapping().resolveDbTable(tableName);
		}

		public Iterable<String> getCandidateTableNames() {
			return getTypeMapping().getAllAssociatedTableNames();
		}

		public boolean tableNameIsInvalid(String tableName) {
			return getTypeMapping().tableNameIsInvalid(tableName);
		}

		public JptValidator buildColumnValidator(NamedColumn column) {
			return new TenantDiscriminatorColumnValidator2_3((EclipseLinkTenantDiscriminatorColumn2_3) column);
		}

		public TextRange getValidationTextRange() {
			return OrmEclipseLinkMultitenancyImpl2_3.this.getValidationTextRange();
		}
	}

	// ********** XML multitenant **********

	/**
	 * Return null if the XML multitenant does not exists.
	 */
	protected XmlMultitenant getXmlMultitenant() {
		return getXmlMultitenantHolder().getMultitenant();
	}

	protected XmlMultitenantHolder getXmlMultitenantHolder() {
		return (XmlMultitenantHolder) this.getXmlTypeMapping();
	}

	/**
	 * Build the XML multitenant if it does not exist.
	 */
	protected XmlMultitenant getXmlMultitenantForUpdate() {
		XmlMultitenant xmlMultitenant = this.getXmlMultitenant();
		return (xmlMultitenant != null) ? xmlMultitenant : this.buildXmlMultitenant();
	}

	protected XmlMultitenant buildXmlMultitenant() {
		XmlMultitenant xmlMultitenant = EclipseLinkOrmFactory.eINSTANCE.createXmlMultitenant();
		this.getXmlMultitenantHolder().setMultitenant(xmlMultitenant);
		return xmlMultitenant;
	}

	public boolean isMultitenantElementSpecified() {
		return this.getXmlMultitenant() != null;
	}

	protected XmlMultitenant addXmlMultitenant() {
		XmlMultitenant xmlMultitenant = this.buildXmlMultitenant();
		this.getXmlMultitenantHolder().setMultitenant(xmlMultitenant);
		return xmlMultitenant;
	}

	protected void removeXmlMultitenant() {
		this.getXmlMultitenantHolder().setMultitenant(null);
	}

	// ********** misc **********

	protected OrmEclipseLinkNonEmbeddableTypeMapping getTypeMapping() {
		return this.parent;
	}

	protected XmlTypeMapping getXmlTypeMapping() {
		return this.getTypeMapping().getXmlTypeMapping();
	}

	public EclipseLinkEntityMappings getEclipseLinkEntityMappings() {
		return (EclipseLinkEntityMappings) super.getMappingFileRoot();
	}

	@Override
	public EclipseLinkPersistenceUnit getPersistenceUnit() {
		return (EclipseLinkPersistenceUnit) super.getPersistenceUnit();
	}

	protected EclipseLinkJavaNonEmbeddableTypeMapping getJavaTypeMappingForDefaults() {
		return this.getTypeMapping().getJavaTypeMappingForDefaults();
	}

	protected EclipseLinkJavaMultitenancy2_3 getJavaMultitenancyPolicyForDefaults() {
		EclipseLinkJavaNonEmbeddableTypeMapping javaTypeMapping = this.getJavaTypeMappingForDefaults();
		return (javaTypeMapping == null) ? null : javaTypeMapping.getMultitenancy();
	}

	protected boolean isMultitenantInheritanceHierarchy() {
		if (this.isInheritanceStrategyTablePerClass()) {
			return false;
		}
		return this.isRootEntityMultitenant();
	}

	protected boolean isInheritanceStrategyTablePerClass() {
		return this.getTypeMapping().getInheritanceStrategy() == InheritanceType.TABLE_PER_CLASS;
	}

	protected boolean isRootEntityMultitenant() {
		EclipseLinkEntity rootEntity = this.getRootEntity();
		return (rootEntity != null) && (rootEntity != this.getTypeMapping()) && rootEntity.getMultitenancy().isMultitenant();
	}

	protected EclipseLinkEntity getRootEntity() {
		//instanceof check in case the rootEntity is in an orm.xml instead of an eclipselinkorm.xml file.
		Entity entity = this.getTypeMapping().getRootEntity();
		return (entity instanceof EclipseLinkEntity) ? (EclipseLinkEntity) entity : null;
	}

	protected boolean isSuperMappedSuperclassMultitenant() {
		EclipseLinkMappedSuperclass mappedSuperclass = this.getSuperMappedSuperclass(this.getTypeMapping());
		return mappedSuperclass != null && mappedSuperclass.getMultitenancy().isMultitenant();
	}

	protected EclipseLinkMappedSuperclass getSuperMappedSuperclass() {
		return this.getSuperMappedSuperclass(this.getTypeMapping());
	}

	protected EclipseLinkMappedSuperclass getSuperMappedSuperclass(TypeMapping typeMapping) {
		TypeMapping superTypeMapping = typeMapping.getSuperTypeMapping();
		if (superTypeMapping == null) {
			return null;
		}
		//instanceof check in case the mapped superclass is in an orm.xml instead of an eclipselinkorm.xml file.
		if (superTypeMapping instanceof EclipseLinkMappedSuperclass) {
			return (EclipseLinkMappedSuperclass) superTypeMapping;
		}
		return this.getSuperMappedSuperclass(superTypeMapping);
	}

	public boolean usesPrimaryKeyTenantDiscriminatorColumns() {
		for (EclipseLinkTenantDiscriminatorColumn2_3 column : getTenantDiscriminatorColumns()) {
			if (column.isPrimaryKey()) {
				return true;
			}
		}
		return false;
	}


	// ********** validation **********

	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		if (getType() == EclipseLinkMultitenantType2_3.TABLE_PER_TENANT && ! this.getJpaPlatformVersion().isCompatibleWithEclipseLinkVersion(EclipseLink2_4JpaPlatformFactory.VERSION)) {
			messages.add(
				this.buildValidationMessage(
					this.getValidationTextRange(),
					JptJpaEclipseLinkCoreValidationMessages.MULTITENANT_TABLE_PER_TENANT_NOT_SUPPORTED
				)
			);			
		}
		if (getType() == EclipseLinkMultitenantType2_3.VPD) {
			String targetDatabase = getPersistenceUnit().getEclipseLinkOptions().getTargetDatabase();
			if (targetDatabase == null) {
				messages.add(
					this.buildValidationMessage(
						this.getValidationTextRange(),
						JptJpaEclipseLinkCoreValidationMessages.MULTITENANT_VPD_MIGHT_NOT_BE_NOT_SUPPORTED
					)
				);
			}
			else if (!TargetDatabase.isOracleDatabase(targetDatabase)) {
				messages.add(
					this.buildValidationMessage(
						this.getValidationTextRange(),
						JptJpaEclipseLinkCoreValidationMessages.MULTITENANT_VPD_NOT_SUPPORTED_ON_NON_ORACLE_DATABASE_PLATFORM,
						targetDatabase
					)
				);
			}
		}
		if (getSpecifiedTenantDiscriminatorColumnsSize() > 0) {
			if (!this.specifiedTenantDiscriminatorColumnsAllowed()) {
				messages.add(
					this.buildValidationMessage(
						this.getXmlMultitenant().getValidationTextRange(),
						JptJpaEclipseLinkCoreValidationMessages.MULTITENANT_METADATA_CANNOT_BE_SPECIFIED_ON_NON_ROOT_ENTITY
					)
				);
			}
			else {
				for (OrmSpecifiedTenantDiscriminatorColumn2_3 column : this.getSpecifiedTenantDiscriminatorColumns()) {
					column.validate(messages, reporter);
				}
			}
		}
		if (this.specifiedTenantDiscriminatorColumnsAllowed()) {
			//bug 360731 no need to validate, they will be validated where they are specified 
			for (EclipseLinkVirtualTenantDiscriminatorColumn2_3 column : this.getDefaultTenantDiscriminatorColumns()) {
				column.validate(messages, reporter);
			}
		}
		if (this.getSpecifiedIncludeCriteria() == Boolean.TRUE && getType() == EclipseLinkMultitenantType2_3.VPD) {
			messages.add(
				this.buildValidationMessage(
					this.getXmlMultitenant().getIncludeCriteriaTextRange(),
					JptJpaEclipseLinkCoreValidationMessages.MULTITENANT_VPD_INCLUDE_CRITERIA_WILL_BE_IGNORED
				)
			);			
		}
	}

	@Override
	protected EclipseLinkJpaPlatformVersion getJpaPlatformVersion() {
		return (EclipseLinkJpaPlatformVersion) super.getJpaPlatformVersion();
	}

	public TextRange getValidationTextRange() {
		TextRange textRange = this.getXmlMultitenantValidationTextRange();
		return (textRange != null) ? textRange : this.getTypeMapping().getValidationTextRange();
	}

	protected TextRange getXmlMultitenantValidationTextRange() {
		XmlMultitenant_2_4 xmlMultitenant = this.getXmlMultitenant();
		return (xmlMultitenant == null) ? null : xmlMultitenant.getValidationTextRange();
	}

	// ********** completion proposals **********

	@Override
	public Iterable<String> getCompletionProposals(int pos) {
		Iterable<String> result = super.getCompletionProposals(pos);
		if (result != null) {
			return result;
		}
		for (OrmSpecifiedTenantDiscriminatorColumn2_3 tenantDiscriminatorColumn : this.getSpecifiedTenantDiscriminatorColumns()) {
			result = tenantDiscriminatorColumn.getCompletionProposals(pos);
			if (result != null) {
				return result;
			}
		}
		return null;
	}
}
