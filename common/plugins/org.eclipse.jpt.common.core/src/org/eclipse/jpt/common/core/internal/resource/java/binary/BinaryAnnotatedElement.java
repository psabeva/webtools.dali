/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.core.internal.resource.java.binary;

import java.util.Hashtable;
import java.util.Vector;
import org.eclipse.jdt.core.IAnnotation;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IMemberValuePair;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.CompilationUnit;
import org.eclipse.jpt.common.core.AnnotationProvider;
import org.eclipse.jpt.common.core.JptCommonCorePlugin;
import org.eclipse.jpt.common.core.resource.java.Annotation;
import org.eclipse.jpt.common.core.resource.java.JavaResourceAnnotatedElement;
import org.eclipse.jpt.common.core.resource.java.JavaResourceNode;
import org.eclipse.jpt.common.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.CollectionTools;
import org.eclipse.jpt.common.utility.internal.Transformer;
import org.eclipse.jpt.common.utility.internal.TransformerAdapter;
import org.eclipse.jpt.common.utility.internal.iterables.CompositeIterable;
import org.eclipse.jpt.common.utility.internal.iterables.EmptyListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.ListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneIterable;
import org.eclipse.jpt.common.utility.internal.iterables.LiveCloneListIterable;
import org.eclipse.jpt.common.utility.internal.iterables.TransformationIterable;

/**
 * Java binary annotated element
 */
abstract class BinaryAnnotatedElement
	extends BinaryNode
	implements JavaResourceAnnotatedElement
{
	/** JDT annotated element adapter */
	final Adapter adapter;

	/**
	 * Annotations keyed by annotation name;
	 * no duplicates (the Java compiler does not allow duplicate annotations).
	 */
	private final Hashtable<String, Annotation> annotations = new Hashtable<String, Annotation>();

	/**
	 * Annotation containers keyed by <em>nestable</em> annotation name.
	 * This is used to store annotations that can be both standalone and nested
	 * and are moved back and forth between the two.
	 */
	private final Hashtable<String, AnnotationContainer> annotationContainers = new Hashtable<String, AnnotationContainer>();

	/**
	 * These are built as needed.
	 */
	private final Hashtable<String, Annotation> nullAnnotationsCache = new Hashtable<String, Annotation>();


	// ********** construction/initialization **********

	BinaryAnnotatedElement(JavaResourceNode parent, Adapter adapter) {
		super(parent);
		this.adapter = adapter;
		this.initializeAnnotations();
	}

	private void initializeAnnotations() {
		for (IAnnotation annotation : this.getJdtAnnotations()) {
			this.addAnnotation(annotation);
		}
	}

	private void addAnnotation(IAnnotation jdtAnnotation) {
		String jdtAnnotationName = jdtAnnotation.getElementName();
		// check whether the annotation is a valid container annotation first
		// because container validations are also valid annotations
		// TODO remove container annotations from list of annotations???
		if (this.annotationIsValidContainer(jdtAnnotationName)) {
			String nestableAnnotationName = this.getAnnotationProvider().getNestableAnnotationName(jdtAnnotationName);
			AnnotationContainer container = new AnnotationContainer();
			container.initializeFromContainerAnnotation(jdtAnnotation);
			this.annotationContainers.put(nestableAnnotationName, container);
		}
		else if (this.annotationIsValid(jdtAnnotationName)) {
			this.annotations.put(jdtAnnotationName, this.buildAnnotation(jdtAnnotation));
		}
		else if (this.annotationIsValidNestable(jdtAnnotationName)) {
			// if we already have an annotation container (because there was a container annotation)
			// ignore the standalone nestable annotation
			if (this.annotationContainers.get(jdtAnnotationName) == null) {
				AnnotationContainer container = new AnnotationContainer();
				container.initializeFromStandaloneAnnotation(jdtAnnotation);
				this.annotationContainers.put(jdtAnnotationName, container);
			}
		}
	}

	private boolean annotationIsValid(String annotationName) {
		return CollectionTools.contains(this.getAnnotationProvider().getAnnotationNames(), annotationName);
	}

	private boolean annotationIsValidContainer(String annotationName) {
		return CollectionTools.contains(this.getAnnotationProvider().getContainerAnnotationNames(), annotationName);
	}

	private boolean annotationIsValidNestable(String annotationName) {
		return CollectionTools.contains(this.getAnnotationProvider().getNestableAnnotationNames(), annotationName);
	}


	// ********** updating **********

	@Override
	public void update() {
		super.update();
		this.updateAnnotations();
	}

	// TODO
	private void updateAnnotations() {
		throw new UnsupportedOperationException();
	}


	// ********** annotations **********

	public Iterable<Annotation> getAnnotations() {
		return new LiveCloneIterable<Annotation>(this.annotations.values());
	}

	public int getAnnotationsSize() {
		return this.annotations.size();
	}

	public Annotation getAnnotation(String annotationName) {
		return this.annotations.get(annotationName);
	}

	public Annotation getNonNullAnnotation(String annotationName) {
		Annotation annotation = this.getAnnotation(annotationName);
		return (annotation != null) ? annotation : this.getNullAnnotation(annotationName);
	}

	private Annotation getNullAnnotation(String annotationName) {
		synchronized (this.nullAnnotationsCache) {
			Annotation annotation = this.nullAnnotationsCache.get(annotationName);
			if (annotation == null) {
				annotation = this.buildNullAnnotation(annotationName);
				this.nullAnnotationsCache.put(annotationName, annotation);
			}
			return annotation;
		}
	}

	private Annotation buildNullAnnotation(String annotationName) {
		return this.getAnnotationProvider().buildNullAnnotation(this, annotationName);
	}

	/* CU private */ Annotation buildAnnotation(IAnnotation jdtAnnotation) {
		return this.getAnnotationProvider().buildAnnotation(this, jdtAnnotation);
	}


	// ********** combination annotations **********

	private Iterable<NestableAnnotation> getNestableAnnotations() {
		return new CompositeIterable<NestableAnnotation>(this.getNestableAnnotationLists());
	}

	private Iterable<Iterable<NestableAnnotation>> getNestableAnnotationLists() {
		return new TransformationIterable<AnnotationContainer, Iterable<NestableAnnotation>>(this.getAnnotationContainers(), ANNOTATION_CONTAINER_NESTED_ANNOTATIONS_TRANSFORMER);
	}

	private static final Transformer<AnnotationContainer, Iterable<NestableAnnotation>> ANNOTATION_CONTAINER_NESTED_ANNOTATIONS_TRANSFORMER = new AnnotationContainerNestedAnnotationsTransformer();
	static final class AnnotationContainerNestedAnnotationsTransformer
		extends TransformerAdapter<AnnotationContainer, Iterable<NestableAnnotation>>
	{
		@Override
		public Iterable<NestableAnnotation> transform(AnnotationContainer container) {
			return container.getNestedAnnotations();
		}
	}

	private Iterable<AnnotationContainer> getAnnotationContainers() {
		return new LiveCloneIterable<AnnotationContainer>(this.annotationContainers.values());
	}

	public ListIterable<NestableAnnotation> getAnnotations(String nestableAnnotationName) {
		AnnotationContainer container = this.annotationContainers.get(nestableAnnotationName);
		return (container != null) ? container.getNestedAnnotations() : EmptyListIterable.<NestableAnnotation> instance();
	}


	public int getAnnotationsSize(String nestableAnnotationName) {
		AnnotationContainer container = this.annotationContainers.get(nestableAnnotationName);
		return (container == null) ? 0 : container.getNestedAnnotationsSize();
	}

	public NestableAnnotation getAnnotation(int index, String nestableAnnotationName) {
		AnnotationContainer container = this.annotationContainers.get(nestableAnnotationName);
		return (container == null) ? null : container.getNestedAnnotation(index);
	}

	private Iterable<Annotation> getContainerOrStandaloneNestableAnnotations() {
		return new TransformationIterable<AnnotationContainer, Annotation>(this.getAnnotationContainers(), TOP_LEVEL_ANNOTATION_CONTAINER_TRANSFORMER);
	}

	private static final Transformer<AnnotationContainer, Annotation> TOP_LEVEL_ANNOTATION_CONTAINER_TRANSFORMER = new TopLevelAnnotationContainerTransformer();
	/* CU private */ static final class TopLevelAnnotationContainerTransformer
		extends TransformerAdapter<AnnotationContainer, Annotation>
	{
		@Override
		public Annotation transform(AnnotationContainer container) {
			Annotation containerAnnotation = container.getContainerAnnotation();
			return (containerAnnotation != null) ? containerAnnotation : container.getNestedAnnotation(0);
		}
	}

	/* CU private */ NestableAnnotation buildAnnotation(IAnnotation jdtAnnotation, int index) {
		return this.getAnnotationProvider().buildAnnotation(this, jdtAnnotation, index);
	}


	// ***** all annotations *****

	@SuppressWarnings("unchecked")
	public Iterable<Annotation> getTopLevelAnnotations() {
		return new CompositeIterable<Annotation>(
					this.getAnnotations(),
					this.getContainerOrStandaloneNestableAnnotations()
				);
	}

	public boolean isAnnotated() {
		return ! this.isUnannotated();
	}

	private boolean isUnannotated() {
		return this.annotations.isEmpty() && this.annotationContainers.isEmpty();
	}

	public boolean isAnnotatedWithAnyOf(Iterable<String> annotationNames) {
		for (Annotation annotation : this.getSignificantAnnotations()) {
			if (CollectionTools.contains(annotationNames, annotation.getAnnotationName())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Return the "significant" annotations;
	 * i.e. ignore the container annotations (they have no semantics).
	 */
	@SuppressWarnings("unchecked")
	private Iterable<Annotation> getSignificantAnnotations() {
		return new CompositeIterable<Annotation>(
					this.getAnnotations(),
					this.getNestableAnnotations()
				);
	}


	// ********** misc **********

	@Override
	protected AnnotationProvider getAnnotationProvider() {
		return super.getAnnotationProvider();
	}

	private IAnnotation[] getJdtAnnotations() {
		try {
			return this.adapter.getAnnotations();
		} catch (JavaModelException ex) {
			JptCommonCorePlugin.log(ex);
			return EMPTY_JDT_ANNOTATION_ARRAY;
		}
	}
	private static final IAnnotation[] EMPTY_JDT_ANNOTATION_ARRAY = new IAnnotation[0];


	// ********** IJavaElement adapter **********

	interface Adapter {
		/**
		 * Return the adapter's JDT element (IPackageFragment, IType, IField, IMethod).
		 */
		IJavaElement getElement();

		/**
		 * Return the adapter's element's JDT annotations.
		 */
		IAnnotation[] getAnnotations() throws JavaModelException;
	}


	// ********** unsupported JavaResourcePersistentMember implementation **********

	public Annotation addAnnotation(String annotationName) {
		throw new UnsupportedOperationException();
	}

	public NestableAnnotation addAnnotation(int index, String nestableAnnotationName) {
		throw new UnsupportedOperationException();
	}

	public void moveAnnotation(int targetIndex, int sourceIndex, String nestableAnnotationName) {
		throw new UnsupportedOperationException();
	}

	public void removeAnnotation(String annotationName) {
		throw new UnsupportedOperationException();
	}

	public void removeAnnotation(int index, String nestableAnnotationName) {
		throw new UnsupportedOperationException();
	}

	public TextRange getNameTextRange(CompilationUnit astRoot) {
		throw new UnsupportedOperationException();
	}

	public TextRange getTextRange(String nestableAnnotationName, CompilationUnit astRoot) {
		throw new UnsupportedOperationException();
	}

	/* CU private */ static final IMemberValuePair[] EMPTY_MEMBER_VALUE_PAIR_ARRAY = new IMemberValuePair[0];
	/* CU private */ static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];


	// ********** annotation container **********

	/* CU private */ class AnnotationContainer {

		private Annotation containerAnnotation;

		private final Vector<NestableAnnotation> nestedAnnotations = new Vector<NestableAnnotation>();

		AnnotationContainer() {
			super();
		}

		void initializeFromContainerAnnotation(IAnnotation jdtContainerAnnotation) {
			this.initializeNestedAnnotations(jdtContainerAnnotation);
			this.containerAnnotation = BinaryAnnotatedElement.this.buildAnnotation(jdtContainerAnnotation);
		}

		void initializeNestedAnnotations(IAnnotation jdtContainerAnnotation) {
			Object[] jdtNestedAnnotations = this.getJdtNestedAnnotations(jdtContainerAnnotation);
			int len = jdtNestedAnnotations.length;
			for (int i = 0; i < len; i++) {
				IAnnotation jdtNestedAnnotation = (IAnnotation) jdtNestedAnnotations[i];
				this.addAnnotation(jdtNestedAnnotation, i);
			}
		}

		private Object[] getJdtNestedAnnotations(IAnnotation jdtContainerAnnotation) {
			return BinaryAnnotatedElement.this.getJdtMemberValues(jdtContainerAnnotation, "value"); //$NON-NLS-1$
		}

		void initializeFromStandaloneAnnotation(IAnnotation jdtNestableAnnotation) {
			this.addAnnotation(jdtNestableAnnotation, 0);
		}

		private void addAnnotation(IAnnotation jdtNestableAnnotation, int index) {
			this.nestedAnnotations.add(BinaryAnnotatedElement.this.buildAnnotation(jdtNestableAnnotation, index));
		}

		Annotation getContainerAnnotation() {
			return this.containerAnnotation;
		}

		ListIterable<NestableAnnotation> getNestedAnnotations() {
			return new LiveCloneListIterable<NestableAnnotation>(this.nestedAnnotations);
		}

		int getNestedAnnotationsSize() {
			return this.nestedAnnotations.size();
		}

		NestableAnnotation getNestedAnnotation(int index) {
			return this.nestedAnnotations.get(index);
		}
	}
}
