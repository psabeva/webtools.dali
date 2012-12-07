/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.details.java;

import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.core.context.java.JavaIdMapping;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.details.EclipseLinkIdMappingComposite;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.details.EclipseLinkMutableTriStateCheckBox;
import org.eclipse.jpt.jpa.ui.internal.details.ColumnComposite;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.IdMapping2_0MappedByRelationshipPane;
import org.eclipse.jpt.jpa.ui.internal.jpa2.details.IdMappingGeneration2_0Composite;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;

public class JavaEclipseLinkIdMapping2_0Composite
	extends EclipseLinkIdMappingComposite<JavaIdMapping>
{
	public JavaEclipseLinkIdMapping2_0Composite(
			PropertyValueModel<? extends JavaIdMapping> mappingModel,
			PropertyValueModel<Boolean> enabledModel,
			Composite parentComposite,
			WidgetFactory widgetFactory,
			ResourceManager resourceManager) {
		super(mappingModel, enabledModel, parentComposite, widgetFactory, resourceManager);
	}
	

	@Override
	protected void initializeLayout(Composite container) {
		initializeIdCollapsibleSection(container);
		initializeTypeCollapsibleSection(container);
		initializeConvertersCollapsibleSection(container);
		initializeGenerationCollapsibleSection(container);
	}
	
	@Override
	protected Control initializeIdSection(Composite container) {
		container = this.addSubPane(container);

		new IdMapping2_0MappedByRelationshipPane(this, getSubjectHolder(), container);
		new ColumnComposite(this, buildColumnHolder(), container);

		new EclipseLinkMutableTriStateCheckBox(this, buildMutableHolder(), container);

		return container;
	}	
	
	@Override
	protected void initializeGenerationCollapsibleSection(Composite container) {
		new IdMappingGeneration2_0Composite(this, container);
	}
}
