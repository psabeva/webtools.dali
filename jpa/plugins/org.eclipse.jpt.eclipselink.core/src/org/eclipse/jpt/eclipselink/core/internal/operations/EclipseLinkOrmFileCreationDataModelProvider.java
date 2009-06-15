/*******************************************************************************
 *  Copyright (c) 2008, 2009 Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.operations;

import org.eclipse.core.runtime.Path;
import org.eclipse.jpt.core.internal.operations.OrmFileCreationDataModelProvider;
import org.eclipse.jpt.eclipselink.core.internal.EclipseLinkJpaPlatformProvider;
import org.eclipse.jpt.eclipselink.core.internal.JptEclipseLinkCorePlugin;
import org.eclipse.jpt.eclipselink1_1.core.internal.EclipseLink1_1JpaPlatformProvider;
import org.eclipse.wst.common.frameworks.datamodel.IDataModelOperation;

public class EclipseLinkOrmFileCreationDataModelProvider extends OrmFileCreationDataModelProvider
{
	/**
	 * required default constructor
	 */
	public EclipseLinkOrmFileCreationDataModelProvider() {
		super();
	}
	
	
	@Override
	public IDataModelOperation getDefaultOperation() {
		return new EclipseLinkOrmFileCreationOperation(getDataModel());
	}
	
	@Override
	public Object getDefaultProperty(String propertyName) {
		if (propertyName.equals(FILE_PATH)) {
			return new Path(JptEclipseLinkCorePlugin.DEFAULT_ECLIPSELINK_ORM_XML_FILE_PATH).toPortableString();
		}
		return super.getDefaultProperty(propertyName);
	}
	
	
	@Override
	protected boolean isSupportedPlatformId(String id) {
		return id.equals(EclipseLinkJpaPlatformProvider.ID) || id.equals(EclipseLink1_1JpaPlatformProvider.ID);
	}
	
}
