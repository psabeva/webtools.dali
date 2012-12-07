/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details.java;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.ReadOnlyPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.java.JavaManyToManyMapping;
import org.eclipse.jpt.jpa.ui.details.JpaComposite;
import org.eclipse.jpt.jpa.ui.details.java.JavaAttributeMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.details.java.JavaUiFactory;
import org.eclipse.jpt.jpa.ui.internal.details.AbstractManyToManyMappingUiDefinition;
import org.eclipse.swt.widgets.Composite;

public class JavaManyToManyMappingUiDefinition
	extends AbstractManyToManyMappingUiDefinition<ReadOnlyPersistentAttribute, JavaManyToManyMapping>
	implements JavaAttributeMappingUiDefinition<JavaManyToManyMapping>
{
	// singleton
	private static final JavaManyToManyMappingUiDefinition INSTANCE = 
			new JavaManyToManyMappingUiDefinition();
	
	
	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingUiDefinition<JavaManyToManyMapping> instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Ensure single instance.
	 */
	private JavaManyToManyMappingUiDefinition() {
		super();
	}
	
	public JpaComposite buildAttributeMappingComposite(JavaUiFactory factory, PropertyValueModel<JavaManyToManyMapping> mappingModel, PropertyValueModel<Boolean> enabledModel, Composite parentComposite, WidgetFactory widgetFactory, ResourceManager resourceManager) {
		return factory.createJavaManyToManyMappingComposite(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}
}
