/*******************************************************************************
 *  Copyright (c) 2012  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.internal.resource.java;

import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jpt.common.core.resource.java.Annotation;
import org.eclipse.jpt.common.core.resource.java.AnnotationDefinition;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.utility.jdt.AnnotatedElement;
import org.eclipse.jpt.jpa.eclipselink.core.internal.resource.java.binary.BinaryEclipseLinkUuidGeneratorAnnotation2_4;
import org.eclipse.jpt.jpa.eclipselink.core.internal.resource.java.source.SourceEclipseLinkUuidGeneratorAnnotation2_4;
import org.eclipse.jpt.jpa.eclipselink.core.resource.java.EclipseLinkUuidGeneratorAnnotation2_4;

public class EclipseLinkUuidGenerator2_4AnnotationDefinition
	implements AnnotationDefinition
{
	// singleton
	private static final AnnotationDefinition INSTANCE = new EclipseLinkUuidGenerator2_4AnnotationDefinition();

	/**
	 * Return the singleton.
	 */
	public static AnnotationDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	protected EclipseLinkUuidGenerator2_4AnnotationDefinition() {
		super();
	}

	public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, AnnotatedElement annotatedElement) {
		return new SourceEclipseLinkUuidGeneratorAnnotation2_4(parent, annotatedElement);
	}

	public Annotation buildNullAnnotation(JavaResourceAnnotatedElement parent) {
		throw new UnsupportedOperationException();
	}

	public Annotation buildAnnotation(JavaResourceAnnotatedElement parent, IAnnotation jdtAnnotation) {
		return new BinaryEclipseLinkUuidGeneratorAnnotation2_4(parent, jdtAnnotation);
	}

	public String getAnnotationName() {
		return EclipseLinkUuidGeneratorAnnotation2_4.ANNOTATION_NAME;
	}
}
