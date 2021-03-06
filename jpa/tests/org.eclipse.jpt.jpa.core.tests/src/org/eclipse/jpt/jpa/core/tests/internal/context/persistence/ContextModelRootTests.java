/*******************************************************************************
 * Copyright (c) 2007, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.internal.context.persistence;

import org.eclipse.jpt.common.core.internal.operations.JptFileCreationDataModelProperties;
import org.eclipse.jpt.common.core.resource.xml.JptXmlResource;
import org.eclipse.jpt.jpa.core.context.JpaContextModelRoot;
import org.eclipse.jpt.jpa.core.internal.operations.PersistenceFileCreationDataModelProvider;
import org.eclipse.jpt.jpa.core.tests.internal.context.ContextModelTestCase;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

@SuppressWarnings("nls")
public class ContextModelRootTests
	extends ContextModelTestCase
{
	public ContextModelRootTests(String name) {
		super(name);
	}
	
	public void testUpdateAddPersistenceXml() throws Exception {
		deleteResource(getPersistenceXmlResource());
		JpaContextModelRoot baseJpaContent = getJavaProject().getJpaProject().getContextModelRoot();
		
		assertFalse(getPersistenceXmlResource().fileExists());
		assertNull(baseJpaContent.getPersistenceXml());
		
		IDataModel config =
			DataModelFactory.createDataModel(new PersistenceFileCreationDataModelProvider());
		config.setProperty(JptFileCreationDataModelProperties.CONTAINER_PATH, 
				getJpaProject().getProject().getFolder("src/META-INF").getFullPath());
		config.getDefaultOperation().execute(null, null);
		
		assertNotNull(baseJpaContent.getPersistenceXml());
	}
	
	public void testUpdateRemovePersistenceXml() throws Exception {
		JptXmlResource pr = getPersistenceXmlResource();
		JpaContextModelRoot baseJpaContent = getJavaProject().getJpaProject().getContextModelRoot();
		
		assertNotNull(baseJpaContent.getPersistenceXml());
		
		deleteResource(pr);
		
		assertNull(baseJpaContent.getPersistenceXml());
	}
}
