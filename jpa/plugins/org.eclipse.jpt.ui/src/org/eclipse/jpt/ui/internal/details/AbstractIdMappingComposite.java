/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.ui.internal.details;

import org.eclipse.jpt.core.context.Column;
import org.eclipse.jpt.core.context.Converter;
import org.eclipse.jpt.core.context.ConvertibleMapping;
import org.eclipse.jpt.core.context.IdMapping;
import org.eclipse.jpt.core.context.TemporalConverter;
import org.eclipse.jpt.ui.WidgetFactory;
import org.eclipse.jpt.ui.details.JpaComposite;
import org.eclipse.jpt.ui.internal.widgets.FormPane;
import org.eclipse.jpt.utility.internal.model.value.PropertyAspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.utility.model.value.WritablePropertyValueModel;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;

/**
 * Here the layout of this pane:
 * <pre>
 * -----------------------------------------------------------------------------
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | ColumnComposite                                                       | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | TemporalTypeComposite                                                 | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * | ------------------------------------------------------------------------- |
 * | |                                                                       | |
 * | | GenerationComposite                                                   | |
 * | |                                                                       | |
 * | ------------------------------------------------------------------------- |
 * -----------------------------------------------------------------------------</pre>
 *
 * @see IdMapping
 * @see ColumnComposite
 * @see TemporalTypeComposite
 * @see IdMappingGenerationComposite
 *
 * @version 2.2
 * @since 1.0
 */
public abstract class AbstractIdMappingComposite<T extends IdMapping>
	extends FormPane<T>
    implements JpaComposite
{
	/**
	 * Creates a new <code>IdMappingComposite</code>.
	 *
	 * @param subjectHolder The holder of the subject <code>IIdMapping</code>
	 * @param parent The parent container
	 * @param widgetFactory The factory used to create various common widgets
	 */
	public AbstractIdMappingComposite(PropertyValueModel<? extends T> subjectHolder,
	                          Composite parent,
	                          WidgetFactory widgetFactory) {

		super(subjectHolder, parent, widgetFactory);
	}

	protected PropertyValueModel<? extends Column> buildColumnHolder() {
		return new TransformationPropertyValueModel<T, Column>(getSubjectHolder())  {
			@Override
			protected Column transform_(T value) {
				return value.getColumn();
			}
		};
	}

	protected void initializeTypePane(Composite container) {

		container = addCollapsableSection(
			container,
			JptUiDetailsMessages.TypeSection_type
		);
		((GridLayout) container.getLayout()).numColumns = 2;

		// No converter
		Button noConverterButton = addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_default, 
			buildNoConverterHolder(), 
			null);
		((GridData) noConverterButton.getLayoutData()).horizontalSpan = 2;
				
		PropertyValueModel<Converter> specifiedConverterHolder = buildSpecifiedConverterHolder();
		// Temporal
		addRadioButton(
			container, 
			JptUiDetailsMessages.TypeSection_temporal, 
			buildTemporalBooleanHolder(), 
			null);
		registerSubPane(new TemporalTypeComposite(buildTemporalConverterHolder(specifiedConverterHolder), container, getWidgetFactory()));
	}

	protected WritablePropertyValueModel<Boolean> buildNoConverterHolder() {
		return new PropertyAspectAdapter<T, Boolean>(getSubjectHolder(), ConvertibleMapping.SPECIFIED_CONVERTER_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				return Boolean.valueOf(this.subject.getSpecifiedConverter() == null);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value.booleanValue()) {
					this.subject.setSpecifiedConverter(Converter.NO_CONVERTER);
				}
			}
		};
	}

	protected WritablePropertyValueModel<Boolean> buildTemporalBooleanHolder() {
		return new PropertyAspectAdapter<T, Boolean>(getSubjectHolder(), ConvertibleMapping.SPECIFIED_CONVERTER_PROPERTY) {
			@Override
			protected Boolean buildValue_() {
				Converter converter = this.subject.getSpecifiedConverter();
				if (converter == null) {
					return Boolean.FALSE;
				}
				return Boolean.valueOf(converter.getType() == Converter.TEMPORAL_CONVERTER);
			}

			@Override
			protected void setValue_(Boolean value) {
				if (value.booleanValue()) {
					this.subject.setSpecifiedConverter(Converter.TEMPORAL_CONVERTER);
				}
			}
		};
	}

	protected PropertyValueModel<Converter> buildSpecifiedConverterHolder() {
		return new PropertyAspectAdapter<T, Converter>(getSubjectHolder(), ConvertibleMapping.SPECIFIED_CONVERTER_PROPERTY) {
			@Override
			protected Converter buildValue_() {
				return this.subject.getSpecifiedConverter();
			}
		};
	}
	
	protected PropertyValueModel<TemporalConverter> buildTemporalConverterHolder(PropertyValueModel<Converter> converterHolder) {
		return new TransformationPropertyValueModel<Converter, TemporalConverter>(converterHolder) {
			@Override
			protected TemporalConverter transform_(Converter converter) {
				return (converter != null && converter.getType() == Converter.TEMPORAL_CONVERTER) ? (TemporalConverter) converter : null;
			}
		};
	}
}