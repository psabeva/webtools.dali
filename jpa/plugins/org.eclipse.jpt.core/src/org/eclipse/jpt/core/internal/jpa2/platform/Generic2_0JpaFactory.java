/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.internal.jpa2.platform;

import org.eclipse.jpt.core.context.MappingFile;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.context.PersistentType.Owner;
import org.eclipse.jpt.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.core.context.java.JavaBasicMapping;
import org.eclipse.jpt.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.core.context.java.JavaEmbeddedIdMapping;
import org.eclipse.jpt.core.context.java.JavaEmbeddedMapping;
import org.eclipse.jpt.core.context.java.JavaIdMapping;
import org.eclipse.jpt.core.context.java.JavaJpaContextNode;
import org.eclipse.jpt.core.context.java.JavaManyToManyMapping;
import org.eclipse.jpt.core.context.java.JavaManyToOneMapping;
import org.eclipse.jpt.core.context.java.JavaOneToManyMapping;
import org.eclipse.jpt.core.context.java.JavaOneToOneMapping;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaSequenceGenerator;
import org.eclipse.jpt.core.context.java.JavaTransientMapping;
import org.eclipse.jpt.core.context.java.JavaVersionMapping;
import org.eclipse.jpt.core.context.orm.EntityMappings;
import org.eclipse.jpt.core.context.orm.OrmAttributeMapping;
import org.eclipse.jpt.core.context.orm.OrmBasicMapping;
import org.eclipse.jpt.core.context.orm.OrmEmbeddable;
import org.eclipse.jpt.core.context.orm.OrmEmbeddedIdMapping;
import org.eclipse.jpt.core.context.orm.OrmEmbeddedMapping;
import org.eclipse.jpt.core.context.orm.OrmEntity;
import org.eclipse.jpt.core.context.orm.OrmGeneratorContainer;
import org.eclipse.jpt.core.context.orm.OrmIdMapping;
import org.eclipse.jpt.core.context.orm.OrmManyToManyMapping;
import org.eclipse.jpt.core.context.orm.OrmManyToOneMapping;
import org.eclipse.jpt.core.context.orm.OrmMappedSuperclass;
import org.eclipse.jpt.core.context.orm.OrmOneToManyMapping;
import org.eclipse.jpt.core.context.orm.OrmOneToOneMapping;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmTransientMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.context.orm.OrmVersionMapping;
import org.eclipse.jpt.core.context.orm.PersistenceUnitMetadata;
import org.eclipse.jpt.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.core.internal.jpa2.context.java.Generic2_0JavaEmbeddable;
import org.eclipse.jpt.core.internal.jpa2.context.java.Generic2_0JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.jpa2.context.java.Generic2_0JavaPersistentType;
import org.eclipse.jpt.core.internal.jpa2.context.java.GenericJavaSequenceGenerator2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericEntityMappings2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericOrmEmbeddable2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericOrmPersistentAttribute2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericOrmPersistentType2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericOrmSequenceGenerator2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericOrmXml2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.GenericPersistenceUnitMetadata2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlBasic2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlEmbedded2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlEmbeddedId2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlId2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlManyToMany2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlManyToOne2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlNullAttributeMapping2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlOneToMany2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlOneToOne2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlTransient2_0;
import org.eclipse.jpt.core.internal.jpa2.context.orm.VirtualXmlVersion2_0;
import org.eclipse.jpt.core.internal.platform.GenericJpaFactory;
import org.eclipse.jpt.core.jpa2.context.orm.OrmSequenceGenerator2_0;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlBasic;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlEmbeddable;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlEmbedded;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlEmbeddedId;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlEntity;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlId;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlManyToMany;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlManyToOne;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlMappedSuperclass;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlOneToMany;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlOneToOne;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlSequenceGenerator;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlTransient;
import org.eclipse.jpt.core.jpa2.resource.orm.XmlVersion;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.orm.XmlGeneratorContainer;
import org.eclipse.jpt.core.resource.orm.XmlNullAttributeMapping;
import org.eclipse.jpt.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt.core.resource.xml.JpaXmlResource;

/**
 * Central class that allows extenders to easily replace implementations of
 * various Dali interfaces.
 */
public class Generic2_0JpaFactory extends GenericJpaFactory
{
	public Generic2_0JpaFactory() {
		super();
	}
	
	@Override
	public JavaEmbeddable buildJavaEmbeddable(JavaPersistentType parent) {
		return new Generic2_0JavaEmbeddable(parent);
	}
	
	// ********** Context Nodes **********
	
	public MappingFile build2_0MappingFile(MappingFileRef parent, JpaXmlResource resource) {
		return build2_0OrmXml(parent, resource);
	}
	
	protected GenericOrmXml2_0 build2_0OrmXml(MappingFileRef parent, JpaXmlResource resource) {
		return new GenericOrmXml2_0(parent, resource);
	}

	// ********** Generic 2.0-specific ORM Context Model **********
	
	public EntityMappings build2_0EntityMappings(GenericOrmXml2_0 parent, XmlEntityMappings xmlEntityMappings) {
		return new GenericEntityMappings2_0(parent, xmlEntityMappings);
	}
	
	public PersistenceUnitMetadata build2_0PersistenceUnitMetadata(GenericEntityMappings2_0 parent, XmlEntityMappings xmlEntityMappings) {
		return new GenericPersistenceUnitMetadata2_0(parent, xmlEntityMappings);
	}
	
	public OrmPersistentType build2_0OrmPersistentType(EntityMappings parent, XmlTypeMapping resourceMapping) {
		return new GenericOrmPersistentType2_0(parent, resourceMapping);
	}

	public OrmEmbeddable build2_0OrmEmbeddable(OrmPersistentType parent, XmlEmbeddable resourceMapping) {
		return new GenericOrmEmbeddable2_0(parent, resourceMapping);
	}

	public OrmEntity build2_0OrmEntity(OrmPersistentType parent, XmlEntity resourceMapping) {
		return buildOrmEntity(parent, resourceMapping);
	}

	public OrmMappedSuperclass build2_0OrmMappedSuperclass(OrmPersistentType parent, XmlMappedSuperclass resourceMapping) {
		return buildOrmMappedSuperclass(parent, resourceMapping);
	}
	
	public OrmPersistentAttribute build2_0OrmPersistentAttribute(OrmPersistentType parent, OrmPersistentAttribute.Owner owner, XmlAttributeMapping resourceMapping) {
		return new GenericOrmPersistentAttribute2_0(parent, owner, resourceMapping);
	}
	
	public OrmBasicMapping build2_0OrmBasicMapping(OrmPersistentAttribute parent, XmlBasic resourceMapping) {
		return buildOrmBasicMapping(parent, resourceMapping);
	}
	
	public OrmEmbeddedMapping build2_0OrmEmbeddedMapping(OrmPersistentAttribute parent, XmlEmbedded resourceMapping) {
		return buildOrmEmbeddedMapping(parent, resourceMapping);
	}
	
	public OrmEmbeddedIdMapping build2_0OrmEmbeddedIdMapping(OrmPersistentAttribute parent, XmlEmbeddedId resourceMapping) {
		return buildOrmEmbeddedIdMapping(parent, resourceMapping);
	}
	
	public OrmIdMapping build2_0OrmIdMapping(OrmPersistentAttribute parent, XmlId resourceMapping) {
		return buildOrmIdMapping(parent, resourceMapping);
	}
	
	public OrmManyToManyMapping build2_0OrmManyToManyMapping(OrmPersistentAttribute parent, XmlManyToMany resourceMapping) {
		return buildOrmManyToManyMapping(parent, resourceMapping);
	}
	
	public OrmManyToOneMapping build2_0OrmManyToOneMapping(OrmPersistentAttribute parent, XmlManyToOne resourceMapping) {
		return buildOrmManyToOneMapping(parent, resourceMapping);
	}
	
	public OrmOneToManyMapping build2_0OrmOneToManyMapping(OrmPersistentAttribute parent, XmlOneToMany resourceMapping) {
		return buildOrmOneToManyMapping(parent, resourceMapping);
	}
	
	public OrmOneToOneMapping build2_0OrmOneToOneMapping(OrmPersistentAttribute parent, XmlOneToOne resourceMapping) {
		return buildOrmOneToOneMapping(parent, resourceMapping);
	}
	
	public OrmTransientMapping build2_0OrmTransientMapping(OrmPersistentAttribute parent, XmlTransient resourceMapping) {
		return buildOrmTransientMapping(parent, resourceMapping);
	}
	
	public OrmVersionMapping build2_0OrmVersionMapping(OrmPersistentAttribute parent, XmlVersion resourceMapping) {
		return buildOrmVersionMapping(parent, resourceMapping);
	}
	
	public OrmAttributeMapping build2_0OrmNullAttributeMapping(OrmPersistentAttribute parent, XmlNullAttributeMapping resourceMapping) {
		return buildOrmNullAttributeMapping(parent, resourceMapping);
	}
	
	public OrmGeneratorContainer build2_0OrmGeneratorContainer(XmlContextNode parent, XmlGeneratorContainer resourceGeneratorContainer) {
		return super.buildOrmGeneratorContainer(parent, resourceGeneratorContainer);
	}

	public OrmSequenceGenerator2_0 build2_0OrmSequenceGenerator(XmlContextNode parent, XmlSequenceGenerator resourceSequenceGenerator) {
		return new GenericOrmSequenceGenerator2_0(parent, resourceSequenceGenerator);
	}
	
	public XmlBasic build2_0VirtualXmlBasic(OrmTypeMapping ormTypeMapping, JavaBasicMapping javaBasicMapping) {
		return new VirtualXmlBasic2_0(ormTypeMapping, javaBasicMapping);
	}
	
	public XmlId build2_0VirtualXmlId(OrmTypeMapping ormTypeMapping, JavaIdMapping javaIdMapping) {
		return new VirtualXmlId2_0(ormTypeMapping, javaIdMapping);
	}
	
	public XmlEmbeddedId build2_0VirtualXmlEmbeddedId(OrmTypeMapping ormTypeMapping, JavaEmbeddedIdMapping javaEmbeddedIdMapping) {
		return new VirtualXmlEmbeddedId2_0(ormTypeMapping, javaEmbeddedIdMapping);
	}
	
	public XmlEmbedded build2_0VirtualXmlEmbedded(OrmTypeMapping ormTypeMapping, JavaEmbeddedMapping javaEmbeddedMapping) {
		return new VirtualXmlEmbedded2_0(ormTypeMapping, javaEmbeddedMapping);
	}
	
	public XmlManyToMany build2_0VirtualXmlManyToMany(OrmTypeMapping ormTypeMapping, JavaManyToManyMapping javaManyToManyMapping) {
		return new VirtualXmlManyToMany2_0(ormTypeMapping, javaManyToManyMapping);
	}
	
	public XmlManyToOne build2_0VirtualXmlManyToOne(OrmTypeMapping ormTypeMapping, JavaManyToOneMapping javaManyToOneMapping) {
		return new VirtualXmlManyToOne2_0(ormTypeMapping, javaManyToOneMapping);
	}
	
	public XmlOneToMany build2_0VirtualXmlOneToMany(OrmTypeMapping ormTypeMapping, JavaOneToManyMapping javaOneToManyMapping) {
		return new VirtualXmlOneToMany2_0(ormTypeMapping, javaOneToManyMapping);
	}
	
	public XmlOneToOne build2_0VirtualXmlOneToOne(OrmTypeMapping ormTypeMapping, JavaOneToOneMapping javaOneToOneMapping) {
		return new VirtualXmlOneToOne2_0(ormTypeMapping, javaOneToOneMapping);
	}
	
	public XmlTransient build2_0VirtualXmlTransient(OrmTypeMapping ormTypeMapping, JavaTransientMapping javaTransientMapping) {
		return new VirtualXmlTransient2_0(ormTypeMapping, javaTransientMapping);
	}
	
	public XmlVersion build2_0VirtualXmlVersion(OrmTypeMapping ormTypeMapping, JavaVersionMapping javaVersionMapping) {
		return new VirtualXmlVersion2_0(ormTypeMapping, javaVersionMapping);
	}
	
	public XmlNullAttributeMapping build2_0VirtualXmlNullAttributeMapping(OrmTypeMapping ormTypeMapping, JavaAttributeMapping javaAttributeMapping) {
		return new VirtualXmlNullAttributeMapping2_0(ormTypeMapping, javaAttributeMapping);
	}

	// ********** Java Context Model **********

	@Override
	public JavaPersistentAttribute buildJavaPersistentAttribute(PersistentType parent, JavaResourcePersistentAttribute jrpa) {
		return new Generic2_0JavaPersistentAttribute(parent, jrpa);
	}
	
	@Override
	public JavaPersistentType buildJavaPersistentType(Owner owner, JavaResourcePersistentType jrpt) {
		return new Generic2_0JavaPersistentType(owner, jrpt);
	}

	@Override
	public JavaSequenceGenerator buildJavaSequenceGenerator(JavaJpaContextNode parent) {
		return new GenericJavaSequenceGenerator2_0(parent);
	}

}
