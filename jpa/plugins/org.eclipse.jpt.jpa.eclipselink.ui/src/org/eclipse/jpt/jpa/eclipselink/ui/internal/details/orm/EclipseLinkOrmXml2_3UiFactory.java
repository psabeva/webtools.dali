/*******************************************************************************
 * Copyright (c) 2011, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.details.orm;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.orm.OrmEntity;
import org.eclipse.jpt.jpa.core.context.orm.OrmMappedSuperclass;
import org.eclipse.jpt.jpa.ui.details.JpaComposite;
import org.eclipse.swt.widgets.Composite;

public class EclipseLinkOrmXml2_3UiFactory 
	extends EclipseLinkOrmXml2_0UiFactory
{
	// ********** type mappings **********

	@Override
	public JpaComposite createOrmMappedSuperclassComposite(
			PropertyValueModel<OrmMappedSuperclass> mappedSuperclassModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		return new OrmEclipseLinkMappedSuperclass2_3Composite(mappedSuperclassModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	public JpaComposite createOrmEntityComposite(
			PropertyValueModel<OrmEntity> entityModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		return new OrmEclipseLinkEntity2_3Composite(entityModel, parentComposite, widgetFactory, resourceManager);
	}
}
