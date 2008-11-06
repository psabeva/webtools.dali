package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.core.internal.context.AbstractXmlContextNode;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.jpt.eclipselink.core.context.Mutable;
import org.eclipse.jpt.eclipselink.core.internal.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlMutable;

public class EclipseLinkOrmMutable extends AbstractXmlContextNode 
	implements Mutable
{
	protected XmlMutable resource;
	
	protected boolean defaultMutable;
	
	protected Boolean specifiedMutable;
	
	
	public EclipseLinkOrmMutable(OrmAttributeMapping parent) {
		super(parent);
	}
		
	@Override
	public EclipseLinkPersistenceUnit getPersistenceUnit() {
		return (EclipseLinkPersistenceUnit) super.getPersistenceUnit();
	}
	
	protected OrmAttributeMapping getAttributeMapping() {
		return (OrmAttributeMapping) getParent();
	}
	
	public boolean isMutable() {
		return (this.specifiedMutable != null) ? this.specifiedMutable.booleanValue() : this.defaultMutable;
	}
	
	public boolean isDefaultMutable() {
		return this.defaultMutable;
	}
	
	protected void setDefaultMutable(boolean newDefaultMutable) {
		boolean oldDefaultMutable = this.defaultMutable;
		this.defaultMutable = newDefaultMutable;
		firePropertyChanged(DEFAULT_MUTABLE_PROPERTY, oldDefaultMutable, newDefaultMutable);
	}
	
	public Boolean getSpecifiedMutable() {
		return this.specifiedMutable;
	}
	
	public void setSpecifiedMutable(Boolean newSpecifiedMutable) {
		Boolean oldSpecifiedMutable = this.specifiedMutable;
		this.specifiedMutable = newSpecifiedMutable;
		this.resource.setMutable(newSpecifiedMutable);
		firePropertyChanged(SPECIFIED_MUTABLE_PROPERTY, oldSpecifiedMutable, newSpecifiedMutable);
	}
	
	protected void setSpecifiedMutable_(Boolean newSpecifiedMutable) {
		Boolean oldSpecifiedMutable = this.specifiedMutable;
		this.specifiedMutable = newSpecifiedMutable;
		firePropertyChanged(SPECIFIED_MUTABLE_PROPERTY, oldSpecifiedMutable, newSpecifiedMutable);
	}
	
	
	// **************** initialize/update **************************************
	
	protected void initialize(XmlMutable resource) {
		this.resource = resource;
		this.defaultMutable = calculateDefaultMutable();
		this.specifiedMutable = resource.getMutable();
	}
	
	protected void update() {
		setDefaultMutable(calculateDefaultMutable());
		setSpecifiedMutable_(this.resource.getMutable());
	}
	
	protected boolean calculateDefaultMutable() {
		OrmAttributeMapping attributeMapping = getAttributeMapping();
		JavaPersistentAttribute javaAttribute = attributeMapping.getJavaPersistentAttribute();
		if (javaAttribute == null) {
			return false;
		}
		JavaResourcePersistentAttribute javaResourceAttribute = javaAttribute.getResourcePersistentAttribute();
		if (javaResourceAttribute.typeIsDateOrCalendar()) {
			Boolean persistenceUnitDefaultMutable = getPersistenceUnit().getOptions().getTemporalMutable();
			return persistenceUnitDefaultMutable == null ? false : persistenceUnitDefaultMutable.booleanValue();
		}
		return javaResourceAttribute.typeIsSerializable();
	}
	
	
	// **************** validation **************************************
	
	public TextRange getValidationTextRange() {
		return this.resource.getMutableTextRange();
	}
}
