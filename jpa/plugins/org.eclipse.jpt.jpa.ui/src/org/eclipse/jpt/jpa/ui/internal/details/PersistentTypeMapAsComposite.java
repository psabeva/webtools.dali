/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import java.util.Collection;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.ui.details.DefaultMappingUiDefinition;
import org.eclipse.jpt.jpa.ui.details.MappingUiDefinition;
import org.eclipse.swt.widgets.Composite;

/**
 * This "Map As" composite is responsible for showing the mapping name and
 * mapping type for a type.
 */
public class PersistentTypeMapAsComposite
	extends MapAsComposite<PersistentType>
{
	public PersistentTypeMapAsComposite(Pane<? extends PersistentType> parentPane, Composite parent) {
		super(parentPane, parent);
	}
	
	protected String getMappingKey() {
		return getSubject().getMappingKey();
	}

	@Override
	protected MappingChangeHandler buildMappingChangeHandler() {
		return new TypeMappingChangeHandler();
	}

	protected class TypeMappingChangeHandler
		implements MappingChangeHandler
	{
		public String getLabelText() {
			String mappingKey = getMappingKey();
			return (mappingKey != null) ?
					JptUiDetailsMessages.MapAsComposite_mappedTypeText :
					JptUiDetailsMessages.MapAsComposite_unmappedTypeText;
		}

		public String getMappingText() {
			return getMappingUiDefinition().getLinkLabel();
		}

		public void morphMapping(MappingUiDefinition definition) {
			getSubject().setMappingKey(definition.getKey());
		}

		public String getName() {
			return getSubject().getSimpleName();
		}

		public Iterable<? extends MappingUiDefinition<? extends PersistentType, ?>> getMappingUiDefinitions() {
			return getTypeMappingUiDefinitions();
		}

		public MappingUiDefinition getMappingUiDefinition() {
			return getTypeMappingUiDefinition();
		}
	}

	/**
	 * Retrieves the list of definitions that are registered with the JPT plugin.
	 *
	 * @return The supported types of mapping
	 */
	protected Iterable<? extends MappingUiDefinition<? extends PersistentType, ?>> getTypeMappingUiDefinitions() {
		return getJpaPlatformUi().getTypeMappingUiDefinitions(getSubject().getResourceType());
	}
	
	protected MappingUiDefinition<? extends PersistentType, ?> getTypeMappingUiDefinition() {
		return getJpaPlatformUi().getTypeMappingUiDefinition(getSubject().getResourceType(), getMappingKey());
	}
	
	@Override
	protected DefaultMappingUiDefinition getDefaultDefinition() {
		return getJpaPlatformUi().getDefaultTypeMappingUiDefinition(getSubject().getResourceType());
	}
	
	@Override
	protected DefaultMappingUiDefinition getDefaultDefinition(String mappingKey) {
		return getDefaultDefinition();
	}
	
	@Override
	protected void addPropertyNames(Collection<String> propertyNames) {
		super.addPropertyNames(propertyNames);
		propertyNames.add(PersistentType.MAPPING_PROPERTY);
		propertyNames.add(PersistentType.NAME_PROPERTY);
	}

	@Override
	protected void propertyChanged(String propertyName) {
		super.propertyChanged(propertyName);

		if (propertyName == PersistentType.MAPPING_PROPERTY ||
		    propertyName == PersistentType.NAME_PROPERTY) {

			updateDescription();
		}
	}
}
