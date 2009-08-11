/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.java;

import java.util.ListIterator;
import org.eclipse.jpt.core.resource.java.AssociationOverrideAnnotation;

public interface JavaJoinColumnInAssociationOverrideJoiningStrategy 
	extends 
		JavaJpaContextNode,
		JavaJoinColumnJoiningStrategy
{

	void initialize(AssociationOverrideAnnotation associationOverride);
	
	void update(AssociationOverrideAnnotation associationOverride);
	
	ListIterator<JavaJoinColumn> joinColumns();

}
