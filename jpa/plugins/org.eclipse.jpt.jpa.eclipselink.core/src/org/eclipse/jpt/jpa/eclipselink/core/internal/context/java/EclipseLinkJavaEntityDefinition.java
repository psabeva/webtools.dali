/*******************************************************************************
 * Copyright (c) 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.context.java;

import org.eclipse.jpt.common.utility.internal.iterables.ArrayIterable;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.jpa.core.context.java.JavaTypeMappingDefinition;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaEntityDefinition;
import org.eclipse.jpt.jpa.core.internal.context.java.JavaTypeMappingDefinitionWrapper;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkCacheAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkChangeTrackingAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkCustomizerAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkExistenceCheckingAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkObjectTypeConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkReadOnlyAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkStructConverterAnnotation;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkTypeConverterAnnotation;

public class EclipseLinkJavaEntityDefinition
	extends JavaTypeMappingDefinitionWrapper
{
	private static final JavaTypeMappingDefinition DELEGATE = JavaEntityDefinition.instance();

	// singleton
	private static final JavaTypeMappingDefinition INSTANCE = new EclipseLinkJavaEntityDefinition();

	/**
	 * Return the singleton.
	 */
	public static JavaTypeMappingDefinition instance() {
		return INSTANCE;
	}


	/**
	 * Enforce singleton usage
	 */
	private EclipseLinkJavaEntityDefinition() {
		super();
	}

	@Override
	protected JavaTypeMappingDefinition getDelegate() {
		return DELEGATE;
	}

	@Override
	public Iterable<String> getSupportingAnnotationNames() {
		return COMBINED_SUPPORTING_ANNOTATION_NAMES;
	}

	private static final String[] ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES_ARRAY = new String[] {
		EclipseLinkCacheAnnotation.ANNOTATION_NAME,
		EclipseLinkChangeTrackingAnnotation.ANNOTATION_NAME,
		EclipseLinkConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkCustomizerAnnotation.ANNOTATION_NAME,
		EclipseLinkExistenceCheckingAnnotation.ANNOTATION_NAME,
		EclipseLinkObjectTypeConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkReadOnlyAnnotation.ANNOTATION_NAME,
		EclipseLinkStructConverterAnnotation.ANNOTATION_NAME,
		EclipseLinkTypeConverterAnnotation.ANNOTATION_NAME,
	};
	// 'public' because EclipseLink MappedSuperclass also supports these annotations
	public static final Iterable<String> ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES = new ArrayIterable<String>(ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES_ARRAY);

	@SuppressWarnings("unchecked")
	private static final Iterable<String> COMBINED_SUPPORTING_ANNOTATION_NAMES = new CompositeIterable<String>(
		DELEGATE.getSupportingAnnotationNames(),
		ECLIPSE_LINK_SUPPORTING_ANNOTATION_NAMES
	);
}