/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.core.context.BasicMapping;
import org.eclipse.jpt.core.context.Converter;
import org.eclipse.jpt.core.context.PersistentAttribute;
import org.eclipse.jpt.core.resource.java.JPA;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkNamedConverter;
import org.eclipse.jpt.eclipselink.core.resource.java.ConvertAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.EclipseLinkJPA;
import org.eclipse.jpt.eclipselink.core.resource.java.StructConverterAnnotation;
import org.eclipse.jpt.eclipselink.core.resource.java.TypeConverterAnnotation;
import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;

public class EclipseLinkJavaConvertTests extends EclipseLinkJavaContextModelTestCase
{

	private void createConvertAnnotation() throws Exception{
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "Convert", "String value() default \"none\";");		
	}
	
	private void createTypeConverterAnnotation() throws Exception {
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "TypeConverter", "String name(); Class dataType() default void.class; Class objectType default void.class;");		
	}
	
	private void createStructConverterAnnotation() throws Exception {
		this.createAnnotationAndMembers(EclipseLinkJPA.PACKAGE, "StructConverter", "String name(); String converter();");		
	}

	private ICompilationUnit createTestEntityWithBasicMapping() throws Exception {
		createConvertAnnotation();
		
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, JPA.BASIC);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Basic").append(CR);
			}
		});
	}

	
	private ICompilationUnit createTestEntityWithConvert() throws Exception {
		createConvertAnnotation();
	
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLinkJPA.CONVERT);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity").append(CR);
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"class-instance\")").append(CR);
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithConvertAndTypeConverter() throws Exception {
		createConvertAnnotation();
		createTypeConverterAnnotation();
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLinkJPA.CONVERT, EclipseLinkJPA.TYPE_CONVERTER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"class-instance\")").append(CR);
				sb.append("    @TypeConverter");
			}
		});
	}
	
	public EclipseLinkJavaConvertTests(String name) {
		super(name);
	}


	public void testGetConverterName() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.attributes().next();
		attributeResource.addAnnotation(ConvertAnnotation.ANNOTATION_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getSpecifiedMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		
		assertEquals(EclipseLinkConvert.NO_CONVERTER, eclipseLinkConvert.getConverterName());
		assertEquals(EclipseLinkConvert.NO_CONVERTER, eclipseLinkConvert.getDefaultConverterName());
		assertEquals(null, eclipseLinkConvert.getSpecifiedConverterName());
	}
	
	public void testGetConvertName2() throws Exception {
		createTestEntityWithConvert();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();

		assertEquals(EclipseLinkConvert.CLASS_INSTANCE_CONVERTER, eclipseLinkConvert.getConverterName());
	}

	public void testSetSpecifiedConverterName() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getSpecifiedMapping();
		basicMapping.setSpecifiedConverter(EclipseLinkConvert.ECLIPSE_LINK_CONVERTER);
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		assertEquals(null, eclipseLinkConvert.getSpecifiedConverterName());
		
		eclipseLinkConvert.setSpecifiedConverterName("foo");
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.attributes().next();
		ConvertAnnotation convertAnnotation = (ConvertAnnotation) attributeResource.getAnnotation(ConvertAnnotation.ANNOTATION_NAME);
		
		assertEquals("foo", convertAnnotation.getValue());
		
		eclipseLinkConvert.setSpecifiedConverterName(null);
		convertAnnotation = (ConvertAnnotation) attributeResource.getAnnotation(ConvertAnnotation.ANNOTATION_NAME);
		assertNotNull(convertAnnotation);
		assertEquals(null, convertAnnotation.getValue());
	}
	
	public void testGetConverterNameUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithBasicMapping();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getSpecifiedMapping();

		assertEquals(Converter.NO_CONVERTER, basicMapping.getConverter().getType());
		
		
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.attributes().next();
		ConvertAnnotation convert = (ConvertAnnotation) attributeResource.addAnnotation(ConvertAnnotation.ANNOTATION_NAME);
		convert.setValue("foo");
		
		assertEquals(EclipseLinkConvert.ECLIPSE_LINK_CONVERTER, basicMapping.getConverter().getType());
		assertEquals("foo", ((EclipseLinkConvert) basicMapping.getConverter()).getConverterName());
		
		attributeResource.removeAnnotation(ConvertAnnotation.ANNOTATION_NAME);
		
		assertEquals(Converter.NO_CONVERTER, basicMapping.getConverter().getType());
		assertFalse(basicMapping.isDefault());
		assertSame(basicMapping, persistentAttribute.getSpecifiedMapping());
	}
	

	public void testGetConverter() throws Exception {
		createTestEntityWithConvertAndTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		
		assertEquals(EclipseLinkNamedConverter.TYPE_CONVERTER, eclipseLinkConvert.getConverter().getType());
	}
	
	public void testSetConverter() throws Exception {
		createTestEntityWithConvert();
		createStructConverterAnnotation();
		createTypeConverterAnnotation();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = javaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		
		assertEquals(null, eclipseLinkConvert.getConverter());
		
		eclipseLinkConvert.setConverter(EclipseLinkNamedConverter.TYPE_CONVERTER);	
		assertEquals(EclipseLinkNamedConverter.TYPE_CONVERTER, eclipseLinkConvert.getConverter().getType());
	
		JavaResourcePersistentType typeResource = jpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.attributes().next();
		assertNotNull(attributeResource.getAnnotation(TypeConverterAnnotation.ANNOTATION_NAME));
		
		eclipseLinkConvert.setConverter(EclipseLinkNamedConverter.STRUCT_CONVERTER);
		assertEquals(EclipseLinkNamedConverter.STRUCT_CONVERTER, eclipseLinkConvert.getConverter().getType());
		assertNotNull(attributeResource.getAnnotation(StructConverterAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TypeConverterAnnotation.ANNOTATION_NAME));

		eclipseLinkConvert.setConverter(EclipseLinkNamedConverter.NO_CONVERTER);
		assertEquals(null, eclipseLinkConvert.getConverter());
		assertNull(attributeResource.getAnnotation(StructConverterAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(TypeConverterAnnotation.ANNOTATION_NAME));
		
		eclipseLinkConvert.setConverter(EclipseLinkNamedConverter.STRUCT_CONVERTER);
		assertEquals(EclipseLinkNamedConverter.STRUCT_CONVERTER, eclipseLinkConvert.getConverter().getType());
		assertNotNull(attributeResource.getAnnotation(StructConverterAnnotation.ANNOTATION_NAME));

		
		basicMapping.setSpecifiedConverter(null);
		assertNull(attributeResource.getAnnotation(StructConverterAnnotation.ANNOTATION_NAME));
		assertNull(attributeResource.getAnnotation(ConvertAnnotation.ANNOTATION_NAME));
		
	}

}
