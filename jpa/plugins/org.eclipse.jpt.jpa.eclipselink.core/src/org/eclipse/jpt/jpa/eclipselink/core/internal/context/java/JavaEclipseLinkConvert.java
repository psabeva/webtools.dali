/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.java;

import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.resource.java.Annotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAttribute;
import org.eclipse.jpt.common.utility.Filter;
import org.eclipse.jpt.common.utility.internal.Association;
import org.eclipse.jpt.common.utility.internal.SimpleAssociation;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.iterables.ArrayIterable;
import org.eclipse.jpt.common.utility.internal.iterables.FilteringIterable;
import org.eclipse.jpt.jpa.core.JpaFactory;
import org.eclipse.jpt.jpa.core.context.Converter;
import org.eclipse.jpt.jpa.core.context.java.JavaAttributeMapping;
import org.eclipse.jpt.jpa.core.context.java.JavaConverter;
import org.eclipse.jpt.jpa.core.internal.context.JptValidator;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.ConverterTextRangeResolver;
import org.eclipse.jpt.jpa.core.internal.jpa1.context.java.AbstractJavaConverter;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConvert;
import org.eclipse.jpt.jpa.eclipselink.core.context.EclipseLinkConverter;
import org.eclipse.jpt.jpa.eclipselink.core.context.persistence.EclipseLinkPersistenceUnit;
import org.eclipse.jpt.jpa.eclipselink.core.internal.context.EclipseLinkConvertValidator;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConvertAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkNamedConverterAnnotation;

public class JavaEclipseLinkConvert
	extends AbstractJavaConverter
	implements EclipseLinkConvert
{
	private final EclipseLinkConvertAnnotation convertAnnotation;

	private String specifiedConverterName;
	private String defaultConverterName;

	private JavaEclipseLinkConverter<?> converter;


	protected static final JavaEclipseLinkConverter.Adapter[] CONVERTER_ADAPTER_ARRAY = new JavaEclipseLinkConverter.Adapter[] {
		JavaEclipseLinkCustomConverter.Adapter.instance(),
		JavaEclipseLinkTypeConverter.Adapter.instance(),
		JavaEclipseLinkObjectTypeConverter.Adapter.instance(),
		JavaEclipseLinkStructConverter.Adapter.instance()
	};
	protected static final Iterable<JavaEclipseLinkConverter.Adapter> CONVERTER_ADAPTERS = new ArrayIterable<JavaEclipseLinkConverter.Adapter>(CONVERTER_ADAPTER_ARRAY);
                                                                                                                      

	public JavaEclipseLinkConvert(JavaAttributeMapping parent, EclipseLinkConvertAnnotation convertAnnotation, JavaConverter.Owner owner) {
		super(parent, owner);
		this.convertAnnotation = convertAnnotation;
		this.specifiedConverterName = convertAnnotation.getValue();
		this.converter = this.buildConverter();
	}

	public EclipseLinkConvertAnnotation getConverterAnnotation() {
		return this.convertAnnotation;
	}

	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.setSpecifiedConverterName_(this.convertAnnotation.getValue());
		this.syncConverter();
	}

	@Override
	public void update() {
		super.update();
		this.setDefaultConverterName(this.buildDefaultConverterName());
		if (this.converter != null) {
			this.converter.update();
		}
	}


	// ********** converter name **********

	public String getConverterName() {
		return (this.specifiedConverterName != null) ? this.specifiedConverterName : this.defaultConverterName;
	}

	public String getSpecifiedConverterName() {
		return this.specifiedConverterName;
	}

	public void setSpecifiedConverterName(String name) {
		this.convertAnnotation.setValue(name);
		this.setSpecifiedConverterName_(name);
	}

	protected void setSpecifiedConverterName_(String name) {
		String old = this.specifiedConverterName;
		this.specifiedConverterName = name;
		this.firePropertyChanged(SPECIFIED_CONVERTER_NAME_PROPERTY, old, name);
	}

	public String getDefaultConverterName() {
		return this.defaultConverterName;
	}

	protected void setDefaultConverterName(String name) {
		String old = this.defaultConverterName;
		this.defaultConverterName = name;
		this.firePropertyChanged(DEFAULT_CONVERTER_NAME_PROPERTY, old, name);
	}

	protected String buildDefaultConverterName() {
		return DEFAULT_CONVERTER_NAME;
	}


	// ********** converter **********

	public JavaEclipseLinkConverter<?> getConverter() {
		return this.converter;
	}

	public void setConverter(Class<? extends EclipseLinkConverter> converterType) {
		if (converterType == null) {
			if (this.converter != null) {
				this.setConverter_(null);
				this.retainConverterAnnotation(null);
			}
		} else {
			if ((this.converter == null) || (this.converter.getType() != converterType)) {
				JavaEclipseLinkConverter.Adapter converterAdapter = this.getConverterAdapter(converterType);
				this.retainConverterAnnotation(converterAdapter);
				this.setConverter_(converterAdapter.buildNewConverter(this.getResourceAttribute(), this));
			}
		}
	}

	protected void setConverter_(JavaEclipseLinkConverter<?> converter) {
		JavaEclipseLinkConverter<?> old = this.converter;
		this.converter = converter;
		this.firePropertyChanged(CONVERTER_PROPERTY, old, converter);
	}

	protected JavaEclipseLinkConverter<?> buildConverter() {
		return this.isVirtual() ? null : this.buildConverter_();
	}

	protected JavaEclipseLinkConverter<?> buildConverter_() {
		JavaResourceAttribute resourceAttribute = this.getResourceAttribute();
		for (JavaEclipseLinkConverter.Adapter adapter : this.getConverterAdapters()) {
			JavaEclipseLinkConverter<?> javaConverter = adapter.buildConverter(resourceAttribute, this);
			if (javaConverter != null) {
				return javaConverter;
			}
		}
		return null;
	}

	/**
	 * Clear all the converter annotations <em>except</em> for the annotation
	 * corresponding to the specified adapter. If the specified adapter is
	 * <code>null</code>, remove <em>all</em> the converter annotations.
	 */
	protected void retainConverterAnnotation(JavaEclipseLinkConverter.Adapter converterAdapter) {
		JavaResourceAttribute resourceAttribute = this.getResourceAttribute();
		for (JavaEclipseLinkConverter.Adapter adapter : this.getConverterAdapters()) {
			if (adapter != converterAdapter) {
				adapter.removeConverterAnnotation(resourceAttribute);
			}
		}
	}

	protected void syncConverter() {
		if ( ! this.isVirtual()) {
			this.syncConverter_();
		}
	}

	protected void syncConverter_() {
		Association<JavaEclipseLinkConverter.Adapter, EclipseLinkNamedConverterAnnotation> assoc = this.getEclipseLinkConverterAnnotation();
		if (assoc == null) {
			if (this.converter != null) {
				this.setConverter_(null);
			}
		} else {
			JavaEclipseLinkConverter.Adapter adapter = assoc.getKey();
			EclipseLinkNamedConverterAnnotation annotation = assoc.getValue();
			if ((this.converter != null) &&
					(this.converter.getType() == adapter.getConverterType()) &&
					(this.converter.getConverterAnnotation() == annotation)) {
				this.converter.synchronizeWithResourceModel();
			} else {
				this.setConverter_(adapter.buildConverter(annotation, this));
			}
		}
	}

	/**
	 * Return the first EclipseLink converter annotation we find along with its
	 * corresponding adapter. Return <code>null</code> if there are no
	 * converter annotations.
	 */
	protected Association<JavaEclipseLinkConverter.Adapter, EclipseLinkNamedConverterAnnotation> getEclipseLinkConverterAnnotation() {
		JavaResourceAttribute resourceAttribute = this.getResourceAttribute();
		for (JavaEclipseLinkConverter.Adapter adapter : this.getConverterAdapters()) {
			EclipseLinkNamedConverterAnnotation annotation = adapter.getConverterAnnotation(resourceAttribute);
			if (annotation != null) {
				return new SimpleAssociation<JavaEclipseLinkConverter.Adapter, EclipseLinkNamedConverterAnnotation>(adapter, annotation);
			}
		}
		return null;
	}


	// ********** converter adapters **********

	/**
	 * Return the converter adapter for the specified converter type.
	 */
	protected JavaEclipseLinkConverter.Adapter getConverterAdapter(Class<? extends EclipseLinkConverter> converterType) {
		for (JavaEclipseLinkConverter.Adapter adapter : this.getConverterAdapters()) {
			if (adapter.getConverterType() == converterType) {
				return adapter;
			}
		}
		throw new IllegalArgumentException("unknown converter type: " + converterType.getName()); //$NON-NLS-1$
	}

	protected Iterable<JavaEclipseLinkConverter.Adapter> getConverterAdapters() {
		return CONVERTER_ADAPTERS;
	}


	// ********** misc **********

	public Class<? extends Converter> getType() {
		return EclipseLinkConvert.class;
	}

	/**
	 * Return whether the convert is <em>virtual</em> and, as a result, does
	 * not have a converter.
	 */
	protected boolean isVirtual() {
		return this.getAttributeMapping().getPersistentAttribute().isVirtual();
	}

	@Override
	public void dispose() {
		super.dispose();
		this.setConverter(null);
	}


	// ********** Java completion proposals **********

	@Override
	public Iterable<String> getJavaCompletionProposals(int pos, Filter<String> filter, CompilationUnit astRoot) {
		Iterable<String> result = super.getJavaCompletionProposals(pos, filter, astRoot);
		if (result != null) {
			return result;
		}
		if (this.convertValueTouches(pos, astRoot)) {
			result = this.getJavaCandidateConverterNames(filter);
			if (result != null) {
				return result;
			}
		}
		return null;
	}

	protected boolean convertValueTouches(int pos, CompilationUnit astRoot) {
		return this.convertAnnotation.valueTouches(pos, astRoot);
	}

	protected Iterable<String> getJavaCandidateConverterNames(Filter<String> filter) {
		return StringTools.convertToJavaStringLiterals(this.getCandidateConverterNames(filter));
	}

	protected Iterable<String> getCandidateConverterNames(Filter<String> filter) {
		return new FilteringIterable<String>(this.getConverterNames(), filter);
	}

	protected Iterable<String> getConverterNames() {
		return this.getPersistenceUnit().getUniqueConverterNames();
	}

	@Override
	public EclipseLinkPersistenceUnit getPersistenceUnit() {
		return (EclipseLinkPersistenceUnit) super.getPersistenceUnit();
	}


	// ********** adapter **********

	public static class Adapter
		extends JavaConverter.AbstractAdapter
	{
		private static final Adapter INSTANCE = new Adapter();
		public static Adapter instance() {
			return INSTANCE;
		}

		private Adapter() {
			super();
		}

		public Class<? extends Converter> getConverterType() {
			return EclipseLinkConvert.class;
		}

		@Override
		protected String getAnnotationName() {
			return EclipseLinkConvertAnnotation.ANNOTATION_NAME;
		}

		public JavaConverter buildConverter(Annotation converterAnnotation, JavaAttributeMapping parent, JpaFactory factory) {
			return new JavaEclipseLinkConvert(parent, (EclipseLinkConvertAnnotation) converterAnnotation, this.buildOwner());
		}
		
		@Override
		protected Owner buildOwner() {
			return new Owner() {
				public JptValidator buildValidator(Converter converter, ConverterTextRangeResolver textRangeResolver) {
					return new EclipseLinkConvertValidator((EclipseLinkConvert) converter, textRangeResolver);
				}
			};
		}
	}
}
