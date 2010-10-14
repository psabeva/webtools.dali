/*******************************************************************************
 * Copyright (c) 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.core.internal;

import java.util.List;
import org.eclipse.jpt.core.JpaAnnotationDefinitionProvider;
import org.eclipse.jpt.core.internal.AbstractJpaAnnotationDefintionProvider;
import org.eclipse.jpt.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.jaxb.core.internal.resource.java.XmlTypeAnnotationDefinition;

/**
 * Support for JAXB annotations
 */
public class GenericJaxbJpaAnnotationDefinitionProvider
	extends AbstractJpaAnnotationDefintionProvider
{
	// singleton
	private static final JpaAnnotationDefinitionProvider INSTANCE = 
			new GenericJaxbJpaAnnotationDefinitionProvider();
	
	
	/**
	 * Return the singleton
	 */
	public static JpaAnnotationDefinitionProvider instance() {
		return INSTANCE;
	}
	
	
	/**
	 * Enforce singleton usage
	 */
	private GenericJaxbJpaAnnotationDefinitionProvider() {
		super();
	}
	
	
	@Override
	protected void addTypeAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
		definitions.add(XmlTypeAnnotationDefinition.instance());
	}
	
	@Override
	protected void addTypeMappingAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
		definitions.add(XmlTypeAnnotationDefinition.instance());
	}
	
	@Override
	protected void addAttributeAnnotationDefinitionsTo(List<AnnotationDefinition> definitions) {
	}
}