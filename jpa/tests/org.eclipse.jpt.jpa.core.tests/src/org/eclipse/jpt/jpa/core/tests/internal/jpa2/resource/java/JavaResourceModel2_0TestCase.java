/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.internal.jpa2.resource.java;

import org.eclipse.jpt.jpa.core.JpaAnnotationDefinitionProvider;
import org.eclipse.jpt.jpa.core.internal.jpa2.GenericJpaAnnotationDefinitionProvider2_0;
import org.eclipse.jpt.jpa.core.tests.internal.resource.java.JpaJavaResourceModelTestCase;

public class JavaResourceModel2_0TestCase extends JpaJavaResourceModelTestCase
{	
	
	public JavaResourceModel2_0TestCase(String name) {
		super(name);
	}

	@Override
	protected JpaAnnotationDefinitionProvider annotationDefinitionProvider() {
		return GenericJpaAnnotationDefinitionProvider2_0.instance();
	}
}
