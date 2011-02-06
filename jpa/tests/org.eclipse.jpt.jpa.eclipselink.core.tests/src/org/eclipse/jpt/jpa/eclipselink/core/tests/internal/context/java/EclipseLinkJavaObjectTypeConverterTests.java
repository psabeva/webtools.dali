/*******************************************************************************
 * Copyright (c) 2007, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context.java;

import java.util.Iterator;
import java.util.ListIterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.core.context.BasicMapping;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConversionValue;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkObjectTypeConverter;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLink;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConversionValueAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkObjectTypeConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context.EclipseLinkContextModelTestCase;

@SuppressWarnings("nls")
public class EclipseLinkJavaObjectTypeConverterTests extends EclipseLinkContextModelTestCase
{
	
	private ICompilationUnit createTestEntityWithConvertAndObjectTypeConverter() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLink.CONVERT, EclipseLink.OBJECT_TYPE_CONVERTER, EclipseLink.CONVERSION_VALUE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"foo\")").append(CR);
				sb.append("    @ObjectTypeConverter(name=\"foo\", defaultObjectValue=\"bar\")");
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithConvertAndDataType() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLink.CONVERT, EclipseLink.OBJECT_TYPE_CONVERTER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"foo\")").append(CR);
				sb.append("    @ObjectTypeConverter(dataType=Foo.class)");
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithConvertAndObjectType() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLink.CONVERT, EclipseLink.OBJECT_TYPE_CONVERTER);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"foo\")").append(CR);
				sb.append("    @ObjectTypeConverter(objectType=Foo.class)");
			}
		});
	}
	
	private ICompilationUnit createTestEntityWithConvertAndObjectTypeConverterConversionValue() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.ENTITY, EclipseLink.CONVERT, EclipseLink.OBJECT_TYPE_CONVERTER, EclipseLink.CONVERSION_VALUE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Entity");
			}
			
			@Override
			public void appendIdFieldAnnotationTo(StringBuilder sb) {
				sb.append("@Convert(\"foo\")").append(CR);
				sb.append("    @ObjectTypeConverter(name=\"foo\", defaultObjectValue=\"bar\", conversionValues = @ConversionValue(dataValue=\"f\", objectValue=\"female\"))");
			}
		});
	}

	public EclipseLinkJavaObjectTypeConverterTests(String name) {
		super(name);
	}


	public void testGetName() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		assertEquals("foo", converter.getName());
	}

	public void testSetName() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		assertEquals("foo", converter.getName());
		
		converter.setName("bar");
		assertEquals("bar", converter.getName());
			
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("bar", converterAnnotation.getName());

		
		converter.setName(null);
		assertEquals(null, converter.getName());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals(null, converterAnnotation.getName());


		converter.setName("bar");
		assertEquals("bar", converter.getName());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("bar", converterAnnotation.getName());
	}
	
	public void testGetNameUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
				
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();

		assertEquals("foo", converter.getName());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		converterAnnotation.setName("bar");
		getJpaProject().synchronizeContextModel();
		assertEquals("bar", converter.getName());
		
		attributeResource.removeAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(null, eclipseLinkConvert.getConverter());
		
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.addAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		getJpaProject().synchronizeContextModel();
		assertNotNull(eclipseLinkConvert.getConverter());
		
		converterAnnotation.setName("FOO");
		getJpaProject().synchronizeContextModel();
		assertEquals("FOO", eclipseLinkConvert.getConverter().getName());	
	}

	public void testGetDataType() throws Exception {
		createTestEntityWithConvertAndDataType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		assertEquals("Foo", converter.getDataType());
	}

	public void testSetDataType() throws Exception {
		createTestEntityWithConvertAndDataType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		assertEquals("Foo", converter.getDataType());
		
		converter.setDataType("Bar");
		assertEquals("Bar", converter.getDataType());
			
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", converterAnnotation.getDataType());

		
		converter.setDataType(null);
		assertEquals(null, converter.getDataType());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals(null, converterAnnotation.getDataType());


		converter.setDataType("Bar");
		assertEquals("Bar", converter.getDataType());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", converterAnnotation.getDataType());
	}
	
	public void testGetDataTypeUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithConvertAndDataType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
				
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();

		assertEquals("Foo", converter.getDataType());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		converterAnnotation.setDataType("Bar");
		getJpaProject().synchronizeContextModel();
		assertEquals("Bar", converter.getDataType());
		
		attributeResource.removeAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(null, eclipseLinkConvert.getConverter());
		
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.addAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		getJpaProject().synchronizeContextModel();
		assertNotNull(eclipseLinkConvert.getConverter());
		
		converterAnnotation.setDataType("FooBar");
		getJpaProject().synchronizeContextModel();
		assertEquals("FooBar", ((EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter()).getDataType());	
	}
	
	public void testGetObjectType() throws Exception {
		createTestEntityWithConvertAndObjectType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		assertEquals("Foo", converter.getObjectType());
	}

	public void testSetObjectType() throws Exception {
		createTestEntityWithConvertAndObjectType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		assertEquals("Foo", converter.getObjectType());
		
		converter.setObjectType("Bar");
		assertEquals("Bar", converter.getObjectType());
			
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", converterAnnotation.getObjectType());

		
		converter.setObjectType(null);
		assertEquals(null, converter.getObjectType());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals(null, converterAnnotation.getObjectType());


		converter.setObjectType("Bar");
		assertEquals("Bar", converter.getObjectType());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("Bar", converterAnnotation.getObjectType());
	}
	
	public void testGetObjectTypeUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithConvertAndObjectType();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
				
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();

		assertEquals("Foo", converter.getObjectType());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		converterAnnotation.setObjectType("Bar");
		getJpaProject().synchronizeContextModel();
		assertEquals("Bar", converter.getObjectType());
		
		attributeResource.removeAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(null, eclipseLinkConvert.getConverter());
		
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.addAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		getJpaProject().synchronizeContextModel();
		assertNotNull(eclipseLinkConvert.getConverter());
		
		converterAnnotation.setObjectType("FooBar");
		getJpaProject().synchronizeContextModel();
		assertEquals("FooBar", ((EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter()).getObjectType());	
	}
	
	
	public void testAddConversionValue() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		
		EclipseLinkConversionValue conversionValue = converter.addConversionValue(0);
		conversionValue.setDataValue("F");
		conversionValue.setObjectValue("female");
		
		ListIterator<EclipseLinkConversionValueAnnotation> resourceConversionValues = converterAnnotation.conversionValues();
		EclipseLinkConversionValueAnnotation resourceConversionValue = resourceConversionValues.next();
		assertEquals("F", resourceConversionValue.getDataValue());
		assertEquals("female", resourceConversionValue.getObjectValue());
		
		EclipseLinkConversionValue conversionValue2 = converter.addConversionValue(0);
		conversionValue2.setDataValue("M");
		conversionValue2.setObjectValue("male");
		
		resourceConversionValues = converterAnnotation.conversionValues();
		resourceConversionValue = resourceConversionValues.next();
		assertEquals("M", resourceConversionValue.getDataValue());
		assertEquals("male", resourceConversionValue.getObjectValue());
		resourceConversionValue = resourceConversionValues.next();
		assertEquals("F", resourceConversionValue.getDataValue());
		assertEquals("female", resourceConversionValue.getObjectValue());
		
		EclipseLinkConversionValue conversionValue3 = converter.addConversionValue(1);
		conversionValue3.setDataValue("O");
		conversionValue3.setObjectValue("male");
		
		resourceConversionValues = converterAnnotation.conversionValues();
		resourceConversionValue = resourceConversionValues.next();
		assertEquals("M", resourceConversionValue.getDataValue());
		assertEquals("male", resourceConversionValue.getObjectValue());
		resourceConversionValue = resourceConversionValues.next();
		assertEquals("O", resourceConversionValue.getDataValue());
		assertEquals("male", resourceConversionValue.getObjectValue());
		resourceConversionValue = resourceConversionValues.next();
		assertEquals("F", resourceConversionValue.getDataValue());
		assertEquals("female", resourceConversionValue.getObjectValue());
		
		ListIterator<EclipseLinkConversionValue> conversionValues = converter.conversionValues();
		assertEquals(conversionValue2, conversionValues.next());
		assertEquals(conversionValue3, conversionValues.next());
		assertEquals(conversionValue, conversionValues.next());
		
		conversionValues = converter.conversionValues();
		assertEquals("M", conversionValues.next().getDataValue());
		assertEquals("O", conversionValues.next().getDataValue());
		assertEquals("F", conversionValues.next().getDataValue());
	}
	
	public void testRemoveConversionValue() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		
		converter.addConversionValue(0).setDataValue("F");
		converter.addConversionValue(1).setDataValue("M");
		converter.addConversionValue(2).setDataValue("O");
		
		ListIterator<EclipseLinkConversionValueAnnotation> resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(3, CollectionTools.size(resourceConversionValues));
		
		converter.removeConversionValue(0);
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(2, CollectionTools.size(resourceConversionValues));
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals("M", resourceConversionValues.next().getDataValue());
		assertEquals("O", resourceConversionValues.next().getDataValue());

		converter.removeConversionValue(0);
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(1, CollectionTools.size(resourceConversionValues));
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals("O", resourceConversionValues.next().getDataValue());
		
		converter.removeConversionValue(0);
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(0, CollectionTools.size(resourceConversionValues));
	}

	public void testMoveConversionValue() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);

		converter.addConversionValue(0).setDataValue("F");
		converter.addConversionValue(1).setDataValue("M");
		converter.addConversionValue(2).setDataValue("O");
		
		ListIterator<EclipseLinkConversionValueAnnotation> resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(3, CollectionTools.size(resourceConversionValues));
		
		converter.moveConversionValue(2,0);
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(3, CollectionTools.size(resourceConversionValues));
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals("M", resourceConversionValues.next().getDataValue());
		assertEquals("O", resourceConversionValues.next().getDataValue());
		assertEquals("F", resourceConversionValues.next().getDataValue());
		
		converter.moveConversionValue(0,1);
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals(3, CollectionTools.size(resourceConversionValues));
		resourceConversionValues = converterAnnotation.conversionValues();
		assertEquals("O", resourceConversionValues.next().getDataValue());
		assertEquals("M", resourceConversionValues.next().getDataValue());
		assertEquals("F", resourceConversionValues.next().getDataValue());
	}
	
	public void testUpdateConversionValues() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);

		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		
		converterAnnotation.addConversionValue(0).setDataValue("F");
		converterAnnotation.addConversionValue(1).setDataValue("M");
		converterAnnotation.addConversionValue(2).setDataValue("O");
		getJpaProject().synchronizeContextModel();

		ListIterator<EclipseLinkConversionValue> conversionValues = converter.conversionValues();
		assertEquals("F", conversionValues.next().getDataValue());
		assertEquals("M", conversionValues.next().getDataValue());
		assertEquals("O", conversionValues.next().getDataValue());
		assertFalse(conversionValues.hasNext());
		
		converterAnnotation.moveConversionValue(2, 0);
		getJpaProject().synchronizeContextModel();
		conversionValues = converter.conversionValues();
		assertEquals("M", conversionValues.next().getDataValue());
		assertEquals("O", conversionValues.next().getDataValue());
		assertEquals("F", conversionValues.next().getDataValue());
		assertFalse(conversionValues.hasNext());
		
		converterAnnotation.moveConversionValue(0, 1);
		getJpaProject().synchronizeContextModel();
		conversionValues = converter.conversionValues();
		assertEquals("O", conversionValues.next().getDataValue());
		assertEquals("M", conversionValues.next().getDataValue());
		assertEquals("F", conversionValues.next().getDataValue());
		assertFalse(conversionValues.hasNext());
		
		converterAnnotation.removeConversionValue(1);
		getJpaProject().synchronizeContextModel();
		conversionValues = converter.conversionValues();
		assertEquals("O", conversionValues.next().getDataValue());
		assertEquals("F", conversionValues.next().getDataValue());
		assertFalse(conversionValues.hasNext());
		
		converterAnnotation.removeConversionValue(1);
		getJpaProject().synchronizeContextModel();
		conversionValues = converter.conversionValues();
		assertEquals("O", conversionValues.next().getDataValue());
		assertFalse(conversionValues.hasNext());
		
		converterAnnotation.removeConversionValue(0);
		getJpaProject().synchronizeContextModel();
		conversionValues = converter.conversionValues();
		assertFalse(conversionValues.hasNext());
	}
	
	public void testConversionValuesSize() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);


		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
	
		assertEquals(0, converter.conversionValuesSize());
		
		converterAnnotation.addConversionValue(0).setDataValue("F");
		converterAnnotation.addConversionValue(1).setDataValue("M");
		converterAnnotation.addConversionValue(2).setDataValue("O");
		
		this.getJpaProject().synchronizeContextModel();
		assertEquals(3, converter.conversionValuesSize());
	}


	public void testGetDefaultObjectValue() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		
		assertEquals("bar", converter.getDefaultObjectValue());
	}

	public void testSetDefaultObjectValue() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
		
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();
		assertEquals("bar", converter.getDefaultObjectValue());
		
		converter.setDefaultObjectValue("baz");
		assertEquals("baz", converter.getDefaultObjectValue());
			
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("baz", converterAnnotation.getDefaultObjectValue());

		
		converter.setDefaultObjectValue(null);
		assertEquals(null, converter.getDefaultObjectValue());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals(null, converterAnnotation.getDefaultObjectValue());


		converter.setDefaultObjectValue("bar");
		assertEquals("bar", converter.getDefaultObjectValue());
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		assertEquals("bar", converterAnnotation.getDefaultObjectValue());
	}
	
	public void testGetDefaultObjectValueUpdatesFromResourceModelChange() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverter();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
				
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();

		assertEquals("bar", converter.getDefaultObjectValue());
		
		JavaResourcePersistentType typeResource = getJpaProject().getJavaResourcePersistentType(FULLY_QUALIFIED_TYPE_NAME);
		JavaResourcePersistentAttribute attributeResource = typeResource.persistableAttributes().next();
		EclipseLinkObjectTypeConverterAnnotation converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.getAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		converterAnnotation.setDefaultObjectValue("baz");
		getJpaProject().synchronizeContextModel();
		assertEquals("baz", converter.getDefaultObjectValue());
		
		attributeResource.removeAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);
		getJpaProject().synchronizeContextModel();
		assertEquals(null, eclipseLinkConvert.getConverter());
		
		converterAnnotation = (EclipseLinkObjectTypeConverterAnnotation) attributeResource.addAnnotation(EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME);		
		getJpaProject().synchronizeContextModel();
		assertNotNull(eclipseLinkConvert.getConverter());
		
		converterAnnotation.setDefaultObjectValue("FOO");
		getJpaProject().synchronizeContextModel();
		assertEquals("FOO", ((EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter()).getDefaultObjectValue());	
	}
	
	public void testInitializeConversionValues() throws Exception {
		createTestEntityWithConvertAndObjectTypeConverterConversionValue();
		addXmlClassRef(FULLY_QUALIFIED_TYPE_NAME);
				
		PersistentAttribute persistentAttribute = getJavaPersistentType().attributes().next();
		BasicMapping basicMapping = (BasicMapping) persistentAttribute.getMapping();
		EclipseLinkConvert eclipseLinkConvert = (EclipseLinkConvert) basicMapping.getConverter();
		EclipseLinkObjectTypeConverter converter = (EclipseLinkObjectTypeConverter) eclipseLinkConvert.getConverter();

		assertEquals(1, converter.conversionValuesSize());
		assertEquals("f", converter.conversionValues().next().getDataValue());
		assertEquals("female", converter.conversionValues().next().getObjectValue());
	}
}
