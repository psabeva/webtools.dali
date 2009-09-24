/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.details.java;

import org.eclipse.jpt.core.context.java.JavaEmbeddable;
import org.eclipse.jpt.core.context.java.JavaEmbeddedMapping;
import org.eclipse.jpt.core.context.java.JavaEntity;
import org.eclipse.jpt.core.context.java.JavaIdMapping;
import org.eclipse.jpt.core.context.java.JavaManyToOneMapping;
import org.eclipse.jpt.core.context.java.JavaMappedSuperclass;
import org.eclipse.jpt.core.context.java.JavaOneToOneMapping;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.details.java.BaseJavaUiFactory;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaEmbeddable2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaEmbeddedMapping2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaEntity2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaIdMapping2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaManyToOneMapping2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaMappedSuperclass2_0Composite;
import org.eclipse.jpt.ui.internal.jpa2.details.java.JavaOneToOneMapping2_0Composite;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.swt.widgets.Composite;

/**
 * The default implementation of the UI factory required to show the information
 * related to a JPA mapping (type or attribute).
 *
 * @see GenericPersistenceXmlUiFactory
 *
 * @version 1.0
 * @since 1.0
 */
public class Generic2_0JavaUiFactory extends BaseJavaUiFactory
{
	
	@Override
	public JpaComposite createJavaMappedSuperclassComposite(
			PropertyValueModel<JavaMappedSuperclass> subjectHolder,
			Composite parent, WidgetFactory widgetFactory) {
		return new JavaMappedSuperclass2_0Composite(subjectHolder, parent, widgetFactory);
	}
	
	@Override
	public JpaComposite createJavaEntityComposite(
			PropertyValueModel<JavaEntity> subjectHolder,
			Composite parent, WidgetFactory widgetFactory) {
		return new JavaEntity2_0Composite(subjectHolder, parent, widgetFactory);
	}
	
	@Override
	public JpaComposite createJavaEmbeddableComposite(
			PropertyValueModel<JavaEmbeddable> subjectHolder,
			Composite parent, WidgetFactory widgetFactory) {
		return new JavaEmbeddable2_0Composite(subjectHolder, parent, widgetFactory);
	}

	@Override
	public JpaComposite createJavaIdMappingComposite(
			PropertyValueModel<JavaIdMapping> subjectHolder,
			Composite parent,
			WidgetFactory widgetFactory) {
		return new JavaIdMapping2_0Composite(subjectHolder, parent, widgetFactory);
	}

	@Override
	public JpaComposite createJavaEmbeddedMappingComposite(
			PropertyValueModel<JavaEmbeddedMapping> subjectHolder,
			Composite parent,
			WidgetFactory widgetFactory) {
		return new JavaEmbeddedMapping2_0Composite(subjectHolder, parent, widgetFactory);
	}
	
	@Override
	public JpaComposite createJavaManyToOneMappingComposite(
			PropertyValueModel<JavaManyToOneMapping> subjectHolder, 
			Composite parent, 
			WidgetFactory widgetFactory) {
		return new JavaManyToOneMapping2_0Composite(subjectHolder, parent, widgetFactory);
	}
	
	@Override
	public JpaComposite createJavaOneToOneMappingComposite(
			PropertyValueModel<JavaOneToOneMapping> subjectHolder, 
			Composite parent, 
			WidgetFactory widgetFactory) {
		return new JavaOneToOneMapping2_0Composite(subjectHolder, parent, widgetFactory);
	}
}
