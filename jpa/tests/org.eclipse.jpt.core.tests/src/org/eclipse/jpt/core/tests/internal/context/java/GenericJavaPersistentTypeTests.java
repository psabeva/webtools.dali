/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.tests.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.context.PersistentType;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.persistence.ClassRef;
import org.eclipse.jpt.core.resource.java.EmbeddableAnnotation;
import org.eclipse.jpt.core.resource.java.EntityAnnotation;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.core.resource.persistence.PersistenceFactory;
import org.eclipse.jpt.core.resource.persistence.XmlMappingFileRef;
import org.eclipse.jpt.core.tests.internal.context.ContextModelTestCase;
import org.eclipse.jpt.core.tests.internal.projects.TestJavaProject.SourceWriter;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;


@SuppressWarnings("nls")
public class GenericJavaPersistentTypeTests extends ContextModelTestCase
{
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		XmlMappingFileRef mappingFileRef = PersistenceFactory.eINSTANCE.createXmlMappingFileRef();
		mappingFileRef.setFileName(JptCorePlugin.DEFAULT_ORM_XML_FILE_PATH);
		getXmlPersistenceUnit().getMappingFiles().add(mappingFileRef);
		getPersistenceXmlResource().save(null);
	}
		
	private ICompilationUnit createTestEntity() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
		});
	}

	private ICompilationUnit createTestEntityAnnotatedField() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}

	private ICompilationUnit createTestEntityAnnotatedMethod() throws Exception {	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	private ICompilationUnit createTestEntityAnnotatedFieldAndMethod() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	
	private ICompilationUnit createTestSubType() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild.java", "AnnotationTestTypeChild", new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY);
			}
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends " + TYPE_NAME + " ");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

		});
	}

	private ICompilationUnit createTestSubTypeWithFieldAnnotation() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild.java", "AnnotationTestTypeChild", new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends " + TYPE_NAME + " ");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	
	private ICompilationUnit createTestSubTypeWithMethodAnnotation() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild.java", "AnnotationTestTypeChild", new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends " + TYPE_NAME + " ");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
	
	private ICompilationUnit createTestSubTypeNonPersistent() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild.java", "AnnotationTestTypeChild", new DefaultAnnotationWriter() {
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends " + TYPE_NAME + " ");
			}
		});
	}

	private ICompilationUnit createTestSubTypePersistentExtendsNonPersistent() throws Exception {
		return this.createTestType(PACKAGE_NAME, "AnnotationTestTypeChild2.java", "AnnotationTestTypeChild2", new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.ID);
			}
			@Override
			public void appendExtendsImplementsTo(StringBuilder sb) {
				sb.append("extends AnnotationTestTypeChild ");
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}

			@Override
			public void appendGetIdMethodAnnotationTo(StringBuilder sb) {
				sb.append("@Id");
			}
		});
	}
		
	public GenericJavaPersistentTypeTests(String name) {
		super(name);
	}
	
	public void testGetName() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(FULLY_QUALIFIED_TYPE_NAME, getJavaPersistentType().getName());
	}
	
	public void testGetAccessNothingAnnotated() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(AccessType.FIELD, getJavaPersistentType().getAccess());
	}

	public void testAccessField() throws Exception {
		createTestEntityAnnotatedField();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(AccessType.FIELD, getJavaPersistentType().getAccess());
	}
	
	public void testAccessProperty() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(AccessType.PROPERTY, getJavaPersistentType().getAccess());
	}
	
	public void testAccessFieldAndMethodAnnotated() throws Exception {
		createTestEntityAnnotatedFieldAndMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(AccessType.FIELD, getJavaPersistentType().getAccess());
	}

	public void testAccessInheritance() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubType();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		classRefs.next();
		ClassRef classRef = classRefs.next();
		
		JavaPersistentType javaPersistentType = classRef.getJavaPersistentType();
		assertEquals(PACKAGE_NAME + ".AnnotationTestTypeChild", javaPersistentType.getName());
		
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}
		
	public void testAccessInheritance2() throws Exception {
		createTestEntityAnnotatedField();
		createTestSubType();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		classRefs.next();
		ClassRef classRef = classRefs.next();
		
		JavaPersistentType javaPersistentType = classRef.getJavaPersistentType();
		assertEquals(PACKAGE_NAME + ".AnnotationTestTypeChild", javaPersistentType.getName());
		
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
	}	
		
	public void testAccessInheritance3() throws Exception {
		createTestEntityAnnotatedField();
		createTestSubTypeWithMethodAnnotation();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		classRefs.next();
		ClassRef classRef = classRefs.next();
		
		JavaPersistentType javaPersistentType = classRef.getJavaPersistentType();
		assertEquals(PACKAGE_NAME + ".AnnotationTestTypeChild", javaPersistentType.getName());
		
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}	
		
	public void testAccessInheritance4() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeWithFieldAnnotation();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		classRefs.next();
		ClassRef classRef = classRefs.next();
		JavaPersistentType javaPersistentType = classRef.getJavaPersistentType();
		
		assertEquals(PACKAGE_NAME + ".AnnotationTestTypeChild", javaPersistentType.getName());
		
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
	}
	
	//inherited class having annotations set wins over the default access set on persistence-unit-defaults
	public void testAccessInheritancePersistenceUnitDefaultAccess() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubType();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.FIELD);

		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		classRefs.next();
		ClassRef classRef = classRefs.next();
		JavaPersistentType javaPersistentType = classRef.getJavaPersistentType();
		
		assertEquals(PACKAGE_NAME + ".AnnotationTestTypeChild", javaPersistentType.getName());
		
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}

	public void testAccessXmlNoAccessNoAnnotations() throws Exception {
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntity();

		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
	}
	
	public void testAccessXmlEntityAccessNoAnnotations() throws Exception {
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntity();
		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 

		entityPersistentType.setSpecifiedAccess(AccessType.FIELD);
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());

		entityPersistentType.setSpecifiedAccess(AccessType.PROPERTY);
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}
	
	public void testAccessXmlPersistenceUnitDefaultsAccessNoAnnotations()  throws Exception {
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntity();
		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());

		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.FIELD);
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());

		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.PROPERTY);
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}
	
	public void testAccessXmlEntityPropertyAccessAndFieldAnnotations() throws Exception {
		//xml access set to property, field annotations, JavaPersistentType access is field
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntityAnnotatedField();
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType(); 

		ormPersistentType.setSpecifiedAccess(AccessType.PROPERTY);
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
	}
	
	public void testAccessXmlEntityFieldAccessAndPropertyAnnotations() throws Exception {
		//xml access set to field, property annotations, JavaPersistentType access is property
		OrmPersistentType ormPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntityAnnotatedMethod();
		JavaPersistentType javaPersistentType = ormPersistentType.getJavaPersistentType(); 

		ormPersistentType.setSpecifiedAccess(AccessType.FIELD);
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}
	
	public void testAccessXmlPersistenceUnitDefaultsAccessFieldAnnotations() throws Exception {
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntityAnnotatedField();
		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 

		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.PROPERTY);
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
	}

	//inheritance wins over entity-mappings specified access
	public void testAccessXmlEntityMappingsAccessWithInheritance() throws Exception {
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		OrmPersistentType childEntityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		createTestEntityAnnotatedMethod();
		createTestSubType();
		JavaPersistentType childJavaPersistentType = childEntityPersistentType.getJavaPersistentType(); 

		getEntityMappings().setSpecifiedAccess(AccessType.FIELD);
		assertEquals(AccessType.PROPERTY, entityPersistentType.getJavaPersistentType().getAccess());
		assertEquals(AccessType.PROPERTY, childJavaPersistentType.getAccess());
	}

	public void testAccessXmlMetadataCompleteFieldAnnotations() throws Exception {
		//xml access set to property, java has field annotations so the access should be field
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntityAnnotatedField();
		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 

		getEntityMappings().getPersistenceUnitMetadata().getPersistenceUnitDefaults().setAccess(AccessType.PROPERTY);
		getEntityMappings().getPersistenceUnitMetadata().setXmlMappingMetadataComplete(true);
		assertEquals(AccessType.FIELD, javaPersistentType.getAccess());
		
	}
	
	public void testAccessNoXmlAccessXmlMetdataCompletePropertyAnnotations() throws Exception {
		//xml access not set, metadata complete set.  JavaPersistentType access is property because properties are annotated
		OrmPersistentType entityPersistentType = getEntityMappings().addPersistentType(MappingKeys.ENTITY_TYPE_MAPPING_KEY, FULLY_QUALIFIED_TYPE_NAME);
		createTestEntityAnnotatedMethod();
		JavaPersistentType javaPersistentType = entityPersistentType.getJavaPersistentType(); 

		getEntityMappings().getPersistenceUnitMetadata().setXmlMappingMetadataComplete(true);
		assertEquals(AccessType.PROPERTY, javaPersistentType.getAccess());
	}
	
	public void testParentPersistentType() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeWithFieldAnnotation();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		ClassRef classRef = classRefs.next();
		JavaPersistentType rootJavaPersistentType = classRef.getJavaPersistentType();
		
		classRef = classRefs.next();
		JavaPersistentType childJavaPersistentType = classRef.getJavaPersistentType();
		
		assertEquals(rootJavaPersistentType, childJavaPersistentType.getParentPersistentType());
		assertNull(rootJavaPersistentType.getParentPersistentType());
	}
	
	public void testParentPersistentType2() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeWithFieldAnnotation();
		
		//parent is not added to the persistenceUnit, but it should still be found
		//as the parentPersistentType because of impliedClassRefs and changes for bug 190317
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		JavaPersistentType javaPersistentType = classRefs.next().getJavaPersistentType();
		
		assertNotNull(javaPersistentType.getParentPersistentType());
	}	
	
	//Entity extends Non-Entity extends Entity 
	public void testParentPersistentType3() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeNonPersistent();
		createTestSubTypePersistentExtendsNonPersistent();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild2");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		ClassRef classRef = classRefs.next();
		JavaPersistentType rootJavaPersistentType = classRef.getJavaPersistentType();
		
		classRef = classRefs.next();
		JavaPersistentType childJavaPersistentType = classRef.getJavaPersistentType();
		
		assertEquals(rootJavaPersistentType, childJavaPersistentType.getParentPersistentType());
		assertNull(rootJavaPersistentType.getParentPersistentType());
	}
	
	public void testInheritanceHierarchy() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeNonPersistent();
		createTestSubTypePersistentExtendsNonPersistent();
		
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild2");
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		JavaPersistentType rootJavaPersistentType = classRefs.next().getJavaPersistentType();
		JavaPersistentType childJavaPersistentType = classRefs.next().getJavaPersistentType();
		
		Iterator<PersistentType> inheritanceHierarchy = childJavaPersistentType.inheritanceHierarchy();	
		
		assertEquals(childJavaPersistentType, inheritanceHierarchy.next());
		assertEquals(rootJavaPersistentType, inheritanceHierarchy.next());
	}
	
	public void testInheritanceHierarchy2() throws Exception {
		createTestEntityAnnotatedMethod();
		createTestSubTypeNonPersistent();
		createTestSubTypePersistentExtendsNonPersistent();
		
		addXmlClassRef(PACKAGE_NAME + ".AnnotationTestTypeChild2");
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		ListIterator<ClassRef> classRefs = getPersistenceUnit().specifiedClassRefs();
		JavaPersistentType childJavaPersistentType = classRefs.next().getJavaPersistentType();
		JavaPersistentType rootJavaPersistentType = classRefs.next().getJavaPersistentType();
		
		Iterator<PersistentType> inheritanceHierarchy = childJavaPersistentType.inheritanceHierarchy();	
		
		assertEquals(childJavaPersistentType, inheritanceHierarchy.next());
		assertEquals(rootJavaPersistentType, inheritanceHierarchy.next());
	}
	
	public void testGetMapping() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMapping().getKey());
	}
	
	public void testGetMappingNull() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(MappingKeys.NULL_TYPE_MAPPING_KEY, getJavaPersistentType().getMapping().getKey());
	}
	
	public void testMappingKey() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}
	
	public void testMappingKeyNull() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(MappingKeys.NULL_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}
	
	public void testSetMappingKey() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(MappingKeys.NULL_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());

		getJavaPersistentType().setMappingKey(MappingKeys.ENTITY_TYPE_MAPPING_KEY);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		assertNotNull(typeResource.getMappingAnnotation());
		assertTrue(typeResource.getMappingAnnotation() instanceof EntityAnnotation);
		
		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}
	
	public void testSetMappingKey2() throws Exception {
		createTestEntityAnnotatedField();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());

		getJavaPersistentType().setMappingKey(MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		assertNotNull(typeResource.getMappingAnnotation());
		assertTrue(typeResource.getMappingAnnotation() instanceof EmbeddableAnnotation);
		
		assertEquals(MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}

	public void testSetMappingKeyNull() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());

		getJavaPersistentType().setMappingKey(MappingKeys.NULL_TYPE_MAPPING_KEY);
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		assertNull(typeResource.getMappingAnnotation());
		assertNull(typeResource.getMappingAnnotation(EntityAnnotation.ANNOTATION_NAME));
		
		assertEquals(MappingKeys.NULL_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}
	
	public void testGetMappingKeyMappingChangeInResourceModel() throws Exception {
		createTestEntityAnnotatedField();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.setMappingAnnotation(EmbeddableAnnotation.ANNOTATION_NAME);
				
		assertEquals(MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}
	
	public void testGetMappingKeyMappingChangeInResourceModel2() throws Exception {
		createTestType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		assertEquals(MappingKeys.NULL_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		typeResource.setMappingAnnotation(EntityAnnotation.ANNOTATION_NAME);
				
		assertEquals(MappingKeys.ENTITY_TYPE_MAPPING_KEY, getJavaPersistentType().getMappingKey());
	}

	public void testIsMapped() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertTrue(getJavaPersistentType().isMapped());
		
		getJavaPersistentType().setMappingKey(MappingKeys.NULL_TYPE_MAPPING_KEY);	
		assertFalse(getJavaPersistentType().isMapped());	
	}
	
	public void testAttributes() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		ListIterator<JavaPersistentAttribute> attributes = getJavaPersistentType().attributes();
		
		assertEquals("id", attributes.next().getName());
		assertFalse(attributes.hasNext());
	}
	
	public void testAttributes2() throws Exception {
		createTestEntityAnnotatedFieldAndMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		ListIterator<JavaPersistentAttribute> attributes = getJavaPersistentType().attributes();
		
		assertEquals("id", attributes.next().getName());
		assertEquals("name", attributes.next().getName());
		assertFalse(attributes.hasNext());
	}
	
	public void testAttributesSize() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(1, getJavaPersistentType().attributesSize());
	}
	
	public void testAttributesSize2() throws Exception {
		createTestEntityAnnotatedFieldAndMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		assertEquals(2, getJavaPersistentType().attributesSize());
	}
	
	public void testAttributeNamed() throws Exception {
		createTestEntityAnnotatedMethod();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute attribute = getJavaPersistentType().getAttributeNamed("id");
		
		assertEquals("id", attribute.getName());
		assertNull(getJavaPersistentType().getAttributeNamed("name"));
		assertNull(getJavaPersistentType().getAttributeNamed("foo"));
	}
	
	public void testAttributeNamed2() throws Exception {
		createTestEntityAnnotatedField();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute attribute = getJavaPersistentType().getAttributeNamed("name");
		
		assertEquals("name", attribute.getName());
		
		assertNull(getJavaPersistentType().getAttributeNamed("foo"));
	}
	
	public void testRenameAttribute() throws Exception {
		ICompilationUnit testType = createTestEntityAnnotatedField();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		ListIterator<JavaPersistentAttribute> attributes = getJavaPersistentType().attributes();
		JavaPersistentAttribute idAttribute = attributes.next();
		JavaPersistentAttribute nameAttribute = attributes.next();
		
		
		assertEquals("id", idAttribute.getName());
		assertEquals("name", nameAttribute.getName());
		
		IField idField = testType.findPrimaryType().getField("id");
		idField.rename("id2", false, null);
		
		attributes = getJavaPersistentType().attributes();
		JavaPersistentAttribute nameAttribute2 = attributes.next();
		JavaPersistentAttribute id2Attribute = attributes.next();

		assertNotSame(idAttribute, id2Attribute);
		assertEquals("id2", id2Attribute.getName());
		assertEquals(nameAttribute, nameAttribute2);
		assertEquals("name", nameAttribute2.getName());
		assertFalse(attributes.hasNext());
	}

	public void testParentPersistentTypeGeneric() throws Exception {
		createTestGenericEntity();
		createTestGenericMappedSuperclass();
		
		addXmlClassRef(PACKAGE_NAME + ".Entity1");
		addXmlClassRef(PACKAGE_NAME + ".Entity2");
		
		JavaPersistentType javaPersistentType = getJavaPersistentType();
		assertEquals("test.Entity1", javaPersistentType.getName());
		assertNotNull(javaPersistentType.getParentPersistentType());
		
		assertEquals("test.Entity2", javaPersistentType.getParentPersistentType().getName());
	}

	private void createTestGenericEntity() throws Exception {
		SourceWriter sourceWriter = new SourceWriter() {
			public void appendSourceTo(StringBuilder sb) {
				sb.append(CR);
					sb.append("import ");
					sb.append(JPA.ENTITY);
					sb.append(";");
					sb.append(CR);
				sb.append("@Entity");
				sb.append(CR);
				sb.append("public class Entity1 ");
				sb.append("extends Entity2<Integer> {}").append(CR);
			}
		};
		this.javaProject.createCompilationUnit(PACKAGE_NAME, "Entity1.java", sourceWriter);
	}
	
	private void createTestGenericMappedSuperclass() throws Exception {
		SourceWriter sourceWriter = new SourceWriter() {
			public void appendSourceTo(StringBuilder sb) {
				sb.append(CR);
					sb.append("import ");
					sb.append(JPA.MAPPED_SUPERCLASS);
					sb.append(";");
					sb.append(CR);
				sb.append("@MappedSuperclass");
				sb.append(CR);
				sb.append("public class Entity2<K> {}").append(CR);
			}
		};
		this.javaProject.createCompilationUnit(PACKAGE_NAME, "Entity2.java", sourceWriter);
	}
}
