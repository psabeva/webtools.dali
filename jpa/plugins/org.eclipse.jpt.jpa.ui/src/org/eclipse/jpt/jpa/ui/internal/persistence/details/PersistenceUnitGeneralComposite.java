/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.persistence.details;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.WritablePropertyValueModel;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.ui.details.JpaPageComposite;
import org.eclipse.jpt.jpa.ui.internal.JpaHelpContextIds;
import org.eclipse.jpt.jpa.ui.internal.persistence.JptUiPersistenceMessages;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * |                                                                           |
 * | - General --------------------------------------------------------------- |
 * |                         ------------------------------------------------- |
 * |   Name:                 | I                                             | |
 * |                         ------------------------------------------------- |
 * |                         ------------------------------------------------- |
 * |   Persistence Provider: |                                             |v| |
 * |                         ------------------------------------------------- |
 * |                                                                           |
 * |                                                                           |
 * | - Mapped Classes -------------------------------------------------------- |
 * |                                                                           |
 * |   Description                                                             |
 * |                                                                           |
 * |   ----------------------------------------------------------------------- |
 * |   |                                                                     | |
 * |   | PersistenceUnitMappedClassesComposite                               | |
 * |   |                                                                     | |
 * |   ----------------------------------------------------------------------- |
 * |                                                                           |
 * |                                                                           |
 * | - XML Mapping Files ----------------------------------------------------- |
 * |                                                                           |
 * |   Description                                                             |
 * |                                                                           |
 * |   ----------------------------------------------------------------------- |
 * |   |                                                                     | |
 * |   | PersistenceUnitMappingFilesComposite                                | |
 * |   |                                                                     | |
 * |   ----------------------------------------------------------------------- |
 * |                                                                           |
 * |                                                                           |
 * | - JAR Files ------------------------------------------------------------- |
 * |                                                                           |
 * |   Description                                                             |
 * |                                                                           |
 * |   ----------------------------------------------------------------------- |
 * |   |                                                                     | |
 * |   | PersistenceUnitJarFilesComposite                                    | |
 * |   |                                                                     | |
 * |   ----------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see PersistenceUnit
 * @see PersistenceUnitMappedClassesComposite
 * @see PersistenceUnitMappingFilesComposite
 * @see PersistenceUnitJarFilesComposite
 *
 * @version 2.0
 * @since 2.0
 */
public abstract class PersistenceUnitGeneralComposite extends Pane<PersistenceUnit>
                                             implements JpaPageComposite
{
	/**
	 * Creates a new <code>PersistenceUnitGeneralComposite</code>.
	 *
	 * @param subjectHolder The holder of this pane's subject
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public PersistenceUnitGeneralComposite(PropertyValueModel<? extends PersistenceUnit> subjectHolder,
	                                       Composite container,
	                                       WidgetFactory widgetFactory) {

		super(subjectHolder, container, widgetFactory);
	}
	

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected Composite addContainer(Composite parent) {
		Composite container = addSubPane(parent);
		updateGridData(container);

		return container;
	}

	private WritablePropertyValueModel<String> buildPersistenceProviderHolder() {
		return new PropertyAspectAdapter<PersistenceUnit, String>(getSubjectHolder(), PersistenceUnit.PROVIDER_PROPERTY) {
			@Override
			protected String buildValue_() {
				return subject.getProvider();
			}

			@Override
			protected void setValue_(String value) {
				if (value.length() == 0) {
					value = null;
				}
				subject.setProvider(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildPersistenceUnitNameHolder() {
		return new PropertyAspectAdapter<PersistenceUnit, String>(getSubjectHolder(), PersistenceUnit.NAME_PROPERTY) {
			@Override
			protected String buildValue_() {
				return subject.getName();
			}

			@Override
			protected void setValue_(String value) {
				subject.setName(value);
			}
		};
	}

	private WritablePropertyValueModel<String> buildPersistenceUnitDescriptionHolder() {
		return new PropertyAspectAdapter<PersistenceUnit, String>(getSubjectHolder(), PersistenceUnit.DESCRIPTION_PROPERTY) {
			@Override
			protected String buildValue_() {
				return subject.getDescription();
			}
			
			@Override
			protected void setValue_(String value) {
				if (value.length() == 0) {
					value = null;
				}
				subject.setDescription(value);
			}
		};
	}

	/*
	 * (non-Javadoc)
	 */
	public String getHelpID() {
		return JpaHelpContextIds.PERSISTENCE_XML_GENERAL;
	}

	public ImageDescriptor getPageImageDescriptor() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 */
	public String getPageText() {
		return JptUiPersistenceMessages.PersistenceUnitGeneralComposite_general;
	}

	protected void initializeGeneralPane(Composite container) {

		container = this.addSection(
			container,
			JptUiPersistenceMessages.PersistenceUnitGeneralComposite_general
		);

		// Name widgets
		this.addLabeledText(
			container,
			JptUiPersistenceMessages.PersistenceUnitGeneralComposite_name,
			this.buildPersistenceUnitNameHolder(),
			this.getHelpID()
		);

		// Persistence Provider widgets
		this.addLabeledText(
			container,
			JptUiPersistenceMessages.PersistenceUnitGeneralComposite_persistenceProvider,
			this.buildPersistenceProviderHolder(),
			this.getHelpID()
		);

		// Description widgets
		this.addLabeledText(
			container,
			JptUiPersistenceMessages.PersistenceUnitGeneralComposite_description,
			this.buildPersistenceUnitDescriptionHolder(),
			this.getHelpID()
		);
	}

	protected void initializeMappedClassesPane(Composite container) {

		container = addCollapsibleSection(
			container,
			JptUiPersistenceMessages.PersistenceUnitGeneralComposite_mappedClasses
		);

		updateGridData(container);
		updateGridData(container.getParent());

		new PersistenceUnitClassesComposite(this, container);
	}
	
	protected void updateGridData(Composite container) {

		GridData gridData = new GridData();
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace   = true;
		gridData.horizontalAlignment       = SWT.FILL;
		gridData.verticalAlignment         = SWT.FILL;
		container.setLayoutData(gridData);
	}
}
