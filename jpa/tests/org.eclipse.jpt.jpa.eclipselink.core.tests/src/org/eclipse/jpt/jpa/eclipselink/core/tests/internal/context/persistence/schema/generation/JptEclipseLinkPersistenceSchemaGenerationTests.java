/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.tests.internal.context.persistence.schema.generation;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * decentralize test creation code
 */
public class JptEclipseLinkPersistenceSchemaGenerationTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(JptEclipseLinkPersistenceSchemaGenerationTests.class.getPackage().getName());

		suite.addTestSuite(SchemaGenerationValueModelTests.class);
		suite.addTestSuite(SchemaGenerationBasicAdapterTests.class);
		suite.addTestSuite(SchemaGenerationAdapterTests.class);

		return suite;
	}

	private JptEclipseLinkPersistenceSchemaGenerationTests() {
		super();
		throw new UnsupportedOperationException();
	}

}
