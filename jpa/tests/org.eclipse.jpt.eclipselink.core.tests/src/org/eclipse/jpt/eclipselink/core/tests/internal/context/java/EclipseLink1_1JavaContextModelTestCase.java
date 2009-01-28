/*******************************************************************************
 * Copyright (c) 2007, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.tests.internal.context.java;

import org.eclipse.jpt.core.internal.facet.JpaFacetDataModelProperties;
import org.eclipse.jpt.core.internal.facet.JpaFacetDataModelProvider;
import org.eclipse.jpt.eclipselink.core.internal.EclipseLink1_1JpaPlatformProvider;
import org.eclipse.jpt.eclipselink.core.tests.internal.context.EclipseLinkContextModelTestCase;
import org.eclipse.wst.common.frameworks.datamodel.DataModelFactory;
import org.eclipse.wst.common.frameworks.datamodel.IDataModel;

public class EclipseLink1_1JavaContextModelTestCase extends EclipseLinkContextModelTestCase
{	
	public static final String JPA_ANNOTATIONS_PACKAGE_NAME = "javax.persistence"; //$NON-NLS-1$

	@Override
	protected IDataModel buildJpaConfigDataModel() {
		IDataModel dataModel = DataModelFactory.createDataModel(new JpaFacetDataModelProvider());		
		dataModel.setProperty(JpaFacetDataModelProperties.PLATFORM_ID, EclipseLink1_1JpaPlatformProvider.ID);
		dataModel.setProperty(JpaFacetDataModelProperties.CREATE_ORM_XML, Boolean.TRUE);
		return dataModel;
	}	
	
	public EclipseLink1_1JavaContextModelTestCase(String name) {
		super(name);
	}
}
