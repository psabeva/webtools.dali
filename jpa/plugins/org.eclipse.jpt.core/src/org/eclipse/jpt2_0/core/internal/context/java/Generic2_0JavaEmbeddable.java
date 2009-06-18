/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt2_0.core.internal.context.java;

import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.internal.context.java.AbstractJavaEmbeddable;
import org.eclipse.jpt2_0.core.MappingKeys2_0;


public class Generic2_0JavaEmbeddable extends AbstractJavaEmbeddable
{
	public Generic2_0JavaEmbeddable(JavaPersistentType parent) {
		super(parent);
	}
	
	@Override
	public boolean attributeMappingKeyAllowed(String attributeMappingKey) {
		return attributeMappingKey == MappingKeys.BASIC_ATTRIBUTE_MAPPING_KEY 
			|| attributeMappingKey == MappingKeys.MANY_TO_ONE_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys.ONE_TO_MANY_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys.ONE_TO_ONE_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys.MANY_TO_MANY_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys2_0.ELEMENT_COLLECTION_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys.EMBEDDED_ATTRIBUTE_MAPPING_KEY
			|| attributeMappingKey == MappingKeys.TRANSIENT_ATTRIBUTE_MAPPING_KEY;
	}
}
