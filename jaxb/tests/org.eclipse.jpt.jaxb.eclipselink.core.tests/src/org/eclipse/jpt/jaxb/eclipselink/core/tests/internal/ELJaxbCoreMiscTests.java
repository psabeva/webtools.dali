/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jaxb.eclipselink.core.tests.internal;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.eclipse.jpt.common.core.tests.BundleActivatorTest;
import org.eclipse.jpt.jaxb.eclipselink.core.ELJaxbPlatform;

public class ELJaxbCoreMiscTests {

	public static Test suite() {
		TestSuite suite = new TestSuite(ELJaxbCoreMiscTests.class.getName());

		suite.addTest(new BundleActivatorTest(ELJaxbPlatform.class));

		return suite;
	}
	
	
	private ELJaxbCoreMiscTests() {
		super();
		throw new UnsupportedOperationException();
	}
}