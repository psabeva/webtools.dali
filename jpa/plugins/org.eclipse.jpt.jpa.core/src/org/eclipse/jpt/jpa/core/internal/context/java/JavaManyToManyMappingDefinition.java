/*******************************************************************************
 * Copyright (c) 2006, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.context.java;

import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMappingDefinition;

public class JavaManyToManyMappingDefinition
	extends AbstractJavaManyToManyMappingDefinition
{
	// singleton
	private static final JavaAttributeMappingDefinition INSTANCE = new JavaManyToManyMappingDefinition();

	/**
	 * Return the singleton.
	 */
	public static JavaAttributeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private JavaManyToManyMappingDefinition() {
		super();
	}
}