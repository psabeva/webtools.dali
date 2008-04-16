/*******************************************************************************
 *  Copyright (c) 2007 Oracle. 
 *  All rights reserved.  This program and the accompanying materials 
 *  are made available under the terms of the Eclipse Public License v1.0 
 *  which accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.context.orm;

import java.util.List;
import org.eclipse.core.resources.IResource;
import org.eclipse.jpt.core.JpaStructureNode;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmXml;
import org.eclipse.jpt.core.context.orm.PersistenceUnitDefaults;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.resource.orm.OrmFactory;
import org.eclipse.jpt.core.resource.orm.OrmResource;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.core.utility.TextRange;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class OrmXmlImpl extends AbstractOrmJpaContextNode
	implements OrmXml
{
	protected OrmResource ormResource;
	
	protected EntityMappings entityMappings;
	
	
	public OrmXmlImpl(MappingFileRef parent, OrmResource ormResource) {
		super(parent);
		this.initialize(ormResource);
	}
	
	@Override
	public MappingFileRef getParent() {
		return (MappingFileRef) super.getParent();
	}
	
	public String getId() {
		// isn't actually displayed, so needs no details page
		return null;
	}
	
	public OrmPersistentType getPersistentType(String fullyQualifiedTypeName) {
		if (getEntityMappings() != null) {
			return getEntityMappings().getPersistentType(fullyQualifiedTypeName);
		}
		return null;
	}
	
	@Override
	public IResource getResource() {
		return ormResource.getFile();
	}
	
	// **************** persistence ********************************************
	
	@Override
	public EntityMappings getEntityMappings() {
		return this.entityMappings;
	}
	
	protected void setEntityMappings(EntityMappings newEntityMappings) {
		EntityMappings oldEntityMappings = this.entityMappings;
		this.entityMappings = newEntityMappings;
		firePropertyChanged(ENTITY_MAPPINGS_PROPERTY, oldEntityMappings, newEntityMappings);
	}

	public EntityMappings addEntityMappings() {
		if (this.entityMappings != null) {
			throw new IllegalStateException();
		}
		
		XmlEntityMappings xmlEntityMappings = OrmFactory.eINSTANCE.createXmlEntityMappings();
		this.entityMappings = buildEntityMappings(xmlEntityMappings);
		this.ormResource.getContents().add(xmlEntityMappings);
		firePropertyChanged(ENTITY_MAPPINGS_PROPERTY, null, this.entityMappings);
		return this.entityMappings;
	}
	
	public void removeEntityMappings() {
		if (this.entityMappings == null) {
			throw new IllegalStateException();
		}
		this.entityMappings.dispose();
		EntityMappings oldEntityMappings = this.entityMappings;
		
		this.entityMappings = null;
		XmlEntityMappings xmlEntityMappings = this.ormResource.getEntityMappings(); //TODO helper removeEntityMappings method on ormResource??
		this.ormResource.getContents().remove(xmlEntityMappings);
		firePropertyChanged(ENTITY_MAPPINGS_PROPERTY, oldEntityMappings, null);
	}
	
	public PersistenceUnitDefaults getPersistenceUnitDefaults() {
		if (getEntityMappings() != null) {
			return getEntityMappings().getPersistenceUnitDefaults();
		}
		return null;
	}
	// **************** updating ***********************************************
	
	protected void initialize(OrmResource ormResource) {
		this.ormResource = ormResource;
		if (ormResource.getEntityMappings() != null) {
			this.entityMappings = buildEntityMappings(ormResource.getEntityMappings());
		}
	}

	public void update(OrmResource ormResource) {
		this.ormResource = ormResource;
		if (ormResource.getEntityMappings() != null) {
			if (this.entityMappings != null) {
				this.entityMappings.update(ormResource.getEntityMappings());
			}
			else {
				setEntityMappings(buildEntityMappings(ormResource.getEntityMappings()));
			}
		}
		else {
			if (getEntityMappings() != null) {
				getEntityMappings().dispose();
			}
			setEntityMappings(null);
		}
	}
	
	protected EntityMappings buildEntityMappings(XmlEntityMappings xmlEntityMappings) {
		return getJpaFactory().buildEntityMappings(this, xmlEntityMappings);
	}
	
	
	// *************************************************************************
	
	public JpaStructureNode getStructureNode(int textOffset) {
		if (entityMappings.containsOffset(textOffset)) {
			return entityMappings.getStructureNode(textOffset);
		}
		return this;
	}
	
	// never actually selected
	public TextRange getSelectionTextRange() {
		return TextRange.Empty.instance();
	}
	
	public TextRange getValidationTextRange() {
		return TextRange.Empty.instance();
	}
	
	
	@Override
	public void addToMessages(List<IMessage> messages) {
		super.addToMessages(messages);
		if (getEntityMappings() != null) {
			getEntityMappings().addToMessages(messages);
		}
	}
	
	public void dispose() {
		if (getEntityMappings() != null) {
			getEntityMappings().dispose();
		}
	}
}
