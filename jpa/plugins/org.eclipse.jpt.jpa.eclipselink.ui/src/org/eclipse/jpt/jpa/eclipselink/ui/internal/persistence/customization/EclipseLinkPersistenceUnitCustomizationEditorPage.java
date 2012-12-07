/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.ui.internal.persistence.customization;

import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jface.resource.ResourceManager;
import org.eclipse.jpt.common.ui.WidgetFactory;
import org.eclipse.jpt.common.ui.internal.JptCommonUiMessages;
import org.eclipse.jpt.common.ui.internal.widgets.ClassChooserPane;
import org.eclipse.jpt.common.ui.internal.widgets.Pane;
import org.eclipse.jpt.common.ui.internal.widgets.TriStateCheckBox;
import org.eclipse.jpt.common.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.Customization;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkHelpContextIds;
import org.eclipse.jpt.jpa.eclipselink.ui.internal.EclipseLinkUiMessages;
import org.eclipse.osgi.util.NLS;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

public class EclipseLinkPersistenceUnitCustomizationEditorPage<T extends Customization>
	extends Pane<T>
{
	public EclipseLinkPersistenceUnitCustomizationEditorPage(
			PropertyValueModel<T> subjectModel,
			Composite parentComposite,
            WidgetFactory widgetFactory,
            ResourceManager resourceManager) {
		super(subjectModel, parentComposite, widgetFactory, resourceManager);
	}

	@Override
	protected Composite addComposite(Composite container) {
		GridLayout layout = new GridLayout(2, true); //2 columns equal width
		layout.marginHeight = 0;
		layout.marginWidth  = 0;
		layout.marginTop    = 0;
		layout.marginLeft   = 0;
		layout.marginBottom = 0;
		layout.marginRight  = 0;

		return this.addPane(container, layout);
	}

	@Override
	protected void initializeLayout(Composite parent) {
		Section weavingSection = this.getWidgetFactory().createSection(parent, ExpandableComposite.TITLE_BAR);
		weavingSection.setText(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_weavingPropertiesGroupBox);
		weavingSection.setClient(this.initializeWeavingSection(weavingSection));
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = SWT.TOP;
		weavingSection.setLayoutData(gridData);

		Section customizersSection = this.getWidgetFactory().createSection(parent, ExpandableComposite.TITLE_BAR);
		customizersSection.setText(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_customizersSection);
		customizersSection.setClient(this.initializeCustomizersSection(customizersSection));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = SWT.TOP;
		gridData.verticalSpan = 2;
		customizersSection.setLayoutData(gridData);

		Section otherSection = this.getWidgetFactory().createSection(parent, ExpandableComposite.TITLE_BAR);
		otherSection.setText(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_otherSection);
		otherSection.setClient(this.initializeOtherSection(otherSection));
		gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.verticalAlignment = SWT.TOP;
		otherSection.setLayoutData(gridData);
	}

	protected Control initializeWeavingSection(Section weavingSection) {
		return new WeavingPropertiesComposite(this, weavingSection).getControl();
	}

	protected Control initializeOtherSection(Section otherSection) {
		Composite container = this.addSubPane(otherSection, 2, 0, 0, 0, 0);

		// Validation Only
		TriStateCheckBox validationOnlyCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validationOnlyLabel,
			this.buildValidationOnlyModel(),
			this.buildValidationOnlyStringModel(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		GridData gridData = new GridData();
		gridData.horizontalSpan = 2;
		validationOnlyCheckBox.getCheckBox().setLayoutData(gridData);

		// Mapping Files Validate Schema

		TriStateCheckBox validateSchemaCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validateSchemaLabel,
			this.buildValidateSchemaModel(),
			this.buildValidateSchemaStringModel(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		validateSchemaCheckBox.getCheckBox().setLayoutData(gridData);

		// Throw Exceptions
		TriStateCheckBox throwExceptionsCheckBox = this.addTriStateCheckBoxWithDefault(
			container,
			EclipseLinkUiMessages.PersistenceXmlCustomizationTab_throwExceptionsLabel,
			this.buildThrowExceptionsHolder(),
			this.buildThrowExceptionsStringModel(),
			EclipseLinkHelpContextIds.PERSISTENCE_CUSTOMIZATION
		);
		gridData = new GridData();
		gridData.horizontalSpan = 2;
		throwExceptionsCheckBox.getCheckBox().setLayoutData(gridData);

		// Exception Handler
		Hyperlink exceptionHandlerHyperlink = this.addHyperlink(container, EclipseLinkUiMessages.PersistenceXmlCustomizationTab_exceptionHandlerLabel);
		this.initializeExceptionHandlerClassChooser(container, exceptionHandlerHyperlink);

		// Profiler:
		Hyperlink profilerHyperlink = this.addHyperlink(container, EclipseLinkUiMessages.PersistenceXmlCustomizationTab_profilerLabel);
		new ProfilerClassChooser(this, container, profilerHyperlink);
		
		return container;
	}

	protected Control initializeCustomizersSection(Section customizersSection) {
		Composite container = this.addSubPane(customizersSection);

		// Session Customizer
		new SessionCustomizersComposite(this, container);

		this.buildEntityListComposite(container);

		return container;
	}

	protected void buildEntityListComposite(Composite parent) {
		new EntityListComposite(this, parent); 
	}


	//********* validation only ***********

	private ModifiablePropertyValueModel<Boolean> buildValidationOnlyModel() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.VALIDATION_ONLY_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getValidationOnly();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setValidationOnly(value);
			}
		};
	}

	private PropertyValueModel<String> buildValidationOnlyStringModel() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultValidationOnlyModel()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validationOnlyLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validationOnlyLabel;
			}
		};
	}

	private PropertyValueModel<Boolean> buildDefaultValidationOnlyModel() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.VALIDATION_ONLY_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getValidationOnly() != null) {
					return null;
				}
				return this.subject.getDefaultValidationOnly();
			}
		};
	}


	//********* validate schema ***********

	private ModifiablePropertyValueModel<Boolean> buildValidateSchemaModel() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.VALIDATE_SCHEMA_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getValidateSchema();
			}

			@Override
			protected void setValue_(Boolean value) {
				this.subject.setValidateSchema(value);
			}
		};
	}

	private PropertyValueModel<String> buildValidateSchemaStringModel() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultValidateSchemaModel()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validateSchemaLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_validateSchemaLabel;
			}
		};
	}

	private PropertyValueModel<Boolean> buildDefaultValidateSchemaModel() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.VALIDATE_SCHEMA_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getValidateSchema() != null) {
					return null;
				}
				return this.subject.getDefaultValidateSchema();
			}
		};
	}


	//********* throw exceptions ***********

	private ModifiablePropertyValueModel<Boolean> buildThrowExceptionsHolder() {
		return new PropertyAspectAdapter<Customization, Boolean>(getSubjectHolder(), Customization.THROW_EXCEPTIONS_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return this.subject.getThrowExceptions();
			}
			@Override
			protected void setValue_(Boolean value) {
				this.subject.setThrowExceptions(value);
			}
		};
	}

	private PropertyValueModel<String> buildThrowExceptionsStringModel() {
		return new TransformationPropertyValueModel<Boolean, String>(buildDefaultThrowExceptionsModel()) {
			@Override
			protected String transform(Boolean value) {
				if (value != null) {
					String defaultStringValue = value.booleanValue() ? JptCommonUiMessages.Boolean_True : JptCommonUiMessages.Boolean_False;
					return NLS.bind(EclipseLinkUiMessages.PersistenceXmlCustomizationTab_throwExceptionsLabelDefault, defaultStringValue);
				}
				return EclipseLinkUiMessages.PersistenceXmlCustomizationTab_throwExceptionsLabel;
			}
		};
	}

	private PropertyValueModel<Boolean> buildDefaultThrowExceptionsModel() {
		return new PropertyAspectAdapter<Customization, Boolean>(
			getSubjectHolder(),
			Customization.THROW_EXCEPTIONS_PROPERTY)
		{
			@Override
			protected Boolean buildValue_() {
				if (this.subject.getThrowExceptions() != null) {
					return null;
				}
				return this.subject.getDefaultThrowExceptions();
			}
		};
	}


	//********* exception handler ***********

	private ClassChooserPane<Customization> initializeExceptionHandlerClassChooser(Composite container, Hyperlink hyperlink) {

		return new ClassChooserPane<Customization>(this, container, hyperlink) {

			@Override
			protected ModifiablePropertyValueModel<String> buildTextHolder() {
				return new PropertyAspectAdapter<Customization, String>(
							this.getSubjectHolder(), Customization.EXCEPTION_HANDLER_PROPERTY) {
					@Override
					protected String buildValue_() {
						return this.subject.getExceptionHandler();
					}

					@Override
					protected void setValue_(String value) {

						if (value.length() == 0) {
							value = null;
						}
						this.subject.setExceptionHandler(value);
					}
				};
			}

			@Override
			protected String getClassName() {
				return this.getSubject().getExceptionHandler();
			}

			@Override
			protected IJavaProject getJavaProject() {
				return getSubject().getJpaProject().getJavaProject();
			}

			@Override
			protected void setClassName(String className) {
				this.getSubject().setExceptionHandler(className);
			}

			@Override
			protected String getSuperInterfaceName() {
				return Customization.ECLIPSELINK_EXCEPTION_HANDLER_CLASS_NAME;
			}
		};
	}
}
