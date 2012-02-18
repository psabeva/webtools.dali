/*******************************************************************************
 * Copyright (c) 2008, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.customization;

import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Customization;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.widgets.Composite;

/**
 * WeavingInternalComposite
 */
public class WeavingInternalComposite extends Pane<Customization>
{
	/**
	 * Creates a new <code>WeavingInternalComposite</code>.
	 *
	 * @param parentController
	 *            The parent container of this one
	 * @param parent
	 *            The parent container
	 */
	public WeavingInternalComposite(
					Pane<? extends Customization> parentComposite,
					Composite parent) {

		super(parentComposite, parent);
	}

	@Override
	protected void initializeLayout(Composite container) {

		this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel,
			this.buildWeavingInternalHolder(),
			this.buildWeavingInternalStringHolder(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
	}
	
	private ModifiablePropertyValueModel<Boolean> buildWeavingInternalHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.WEAVING_INTERNAL_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getWeavingInternal();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setWeavingInternal(value);
			}
		};
	}

	private PropertyValueModel<String> buildWeavingInternalStringHolder() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultWeavingInternalHolder()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingInternalLabel;
			}
		};
	}
	
	
	private PropertyValueModel<Boolean> buildDefaultWeavingInternalHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.WEAVING_INTERNAL_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getWeavingInternal() != null) {
					return null;
				}
				return this.subject.getDefaultWeavingInternal();
			}
		};
	}
}
