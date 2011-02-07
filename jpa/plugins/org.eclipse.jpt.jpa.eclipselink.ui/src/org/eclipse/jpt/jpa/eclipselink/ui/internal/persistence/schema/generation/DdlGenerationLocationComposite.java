/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.schema.generation;

import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.FolderChooserComboPane;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.model.value.WritablePropertyValueModel;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.schema.generation.SchemaGeneration;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 *  DdlGenerationLocationComposite
 */
public class DdlGenerationLocationComposite extends Pane<SchemaGeneration>
{
	public DdlGenerationLocationComposite(Pane<? extends SchemaGeneration> parentPane,
	                                      Composite parent) {

		super(parentPane, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {
		
		new FolderChooserComboPane<SchemaGeneration>(this, container) {

			@Override
			protected WritablePropertyValueModel<String> buildTextHolder() {
				return new PropertyAspectAdapter<SchemaGeneration, String>(
										getSubjectHolder(), SchemaGeneration.APPLICATION_LOCATION_PROPERTY) {
					@Override
					protected String buildValue_() {

						String name = subject.getApplicationLocation();
						if (name == null) {
							name = defaultValue(subject);
						}
						return name;
					}

					@Override
					protected void setValue_(String value) {

						if (defaultValue(subject).equals(value)) {
							value = null;
						}
						subject.setApplicationLocation(value);
					}
				};
			}

			private String defaultValue(SchemaGeneration subject) {
				String defaultValue = subject.getDefaultApplicationLocation();

				if (defaultValue != null) {
					return NLS.bind(
						JptCommonUiMessages.DefaultWithOneParam,
						defaultValue
					);
				}
				else {
					return this.getDefaultString();
				}
			}

			@Override
			protected String getDefaultString() {
				return EclipseLinkUiMessages.PersistenceXmlSchemaGenerationTab_defaultDot;
			}

			@Override
			protected String getDialogMessage() {
				return EclipseLinkUiMessages.DdlGenerationLocationComposite_dialogMessage;
			}

			@Override
			protected String getDialogTitle() {
				return EclipseLinkUiMessages.DdlGenerationLocationComposite_dialogTitle;
			}

			@Override
			protected String getLabelText() {
				return EclipseLinkUiMessages.PersistenceXmlSchemaGenerationTab_ddlGenerationLocationLabel;
			}
		};
	}
}