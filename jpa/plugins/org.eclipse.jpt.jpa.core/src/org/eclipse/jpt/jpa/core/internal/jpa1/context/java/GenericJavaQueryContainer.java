/*******************************************************************************
 * Copyright (c) 2009, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.internal.jpa1.context.java;

import java.util.List;
import org.eclipse.jpt.common.core.resource.java.NestableAnnotation;
import org.eclipse.jpt.common.core.utility.TextRange;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;
import org.eclipse.jpt.common.utility.iterable.ListIterable;
import org.eclipse.jpt.jpa.core.context.NamedNativeQuery;
import org.eclipse.jpt.jpa.core.context.NamedQuery;
import org.eclipse.jpt.jpa.core.context.Query;
import org.eclipse.jpt.jpa.core.context.java.JavaNamedNativeQuery;
import org.eclipse.jpt.jpa.core.context.java.JavaNamedQuery;
import org.eclipse.jpt.jpa.core.context.java.JavaQueryContainer;
import org.eclipse.jpt.jpa.core.internal.context.java.AbstractJavaContextModel;
import org.eclipse.jpt.jpa.core.jpa2_1.context.NamedStoredProcedureQuery2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.context.java.JavaNamedStoredProcedureQuery2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.context.java.JavaQueryContainer2_1;
import org.eclipse.jpt.jpa.core.jpa2_1.resource.java.NamedStoredProcedureQueryAnnotation2_1;
import org.eclipse.jpt.jpa.core.resource.java.NamedNativeQueryAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.NamedQueryAnnotation;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;
import org.eclipse.wst.validation.internal.provisional.core.IReporter;

/**
 * Java query container
 */
public class GenericJavaQueryContainer
	extends AbstractJavaContextModel<JavaQueryContainer.Parent>
	implements JavaQueryContainer2_1
{
	protected final ContextListContainer<JavaNamedQuery, NamedQueryAnnotation> namedQueryContainer;
	protected final ContextListContainer<JavaNamedNativeQuery, NamedNativeQueryAnnotation> namedNativeQueryContainer;
	protected final ContextListContainer<JavaNamedStoredProcedureQuery2_1, NamedStoredProcedureQueryAnnotation2_1> namedStoredProcedureQueryContainer;


	public GenericJavaQueryContainer(JavaQueryContainer.Parent parent) {
		super(parent);
		this.namedQueryContainer = this.buildNamedQueryContainer();
		this.namedNativeQueryContainer = this.buildNamedNativeQueryContainer();
		this.namedStoredProcedureQueryContainer = this.buildNamedStoredProcedureQueryContainer();
	}


	// ********** synchronize/update **********

	@Override
	public void synchronizeWithResourceModel() {
		super.synchronizeWithResourceModel();
		this.syncNamedQueries();
		this.syncNamedNativeQueries();
		this.syncNamedStoredProcedureQueries();
	}

	@Override
	public void update() {
		super.update();
		this.updateModels(this.getNamedQueries());
		this.updateModels(this.getNamedNativeQueries());
		this.updateModels(this.getNamedStoredProcedureQueries());
	}


	// ********** queries **********

	@SuppressWarnings("unchecked")
	public Iterable<Query> getQueries() {
		return IterableTools.<Query>concatenate(
				this.getNamedQueries(),
				this.getNamedNativeQueries(),
				this.getNamedStoredProcedureQueries());
	}


	// ********** named queries **********


	public ListIterable<JavaNamedQuery> getNamedQueries() {
		return this.namedQueryContainer.getContextElements();
	}

	public int getNamedQueriesSize() {
		return this.namedQueryContainer.getContextElementsSize();
	}

	public JavaNamedQuery addNamedQuery() {
		return this.addNamedQuery(this.getNamedQueriesSize());
	}

	public JavaNamedQuery addNamedQuery(int index) {
		NamedQueryAnnotation annotation = this.addNamedQueryAnnotation(index);
		return this.namedQueryContainer.addContextElement(index, annotation);
	}

	protected NamedQueryAnnotation addNamedQueryAnnotation(int index) {
		return (NamedQueryAnnotation) this.parent.getResourceAnnotatedElement().addAnnotation(index, NamedQueryAnnotation.ANNOTATION_NAME);
	}

	public void removeNamedQuery(NamedQuery namedQuery) {
		this.removeNamedQuery(this.namedQueryContainer.indexOfContextElement((JavaNamedQuery) namedQuery));
	}

	public void removeNamedQuery(int index) {
		this.parent.getResourceAnnotatedElement().removeAnnotation(index, NamedQueryAnnotation.ANNOTATION_NAME);
		this.namedQueryContainer.removeContextElement(index);
	}

	public void moveNamedQuery(int targetIndex, int sourceIndex) {
		this.parent.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, NamedQueryAnnotation.ANNOTATION_NAME);
		this.namedQueryContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected JavaNamedQuery buildNamedQuery(NamedQueryAnnotation namedQueryAnnotation) {
		return this.getJpaFactory().buildJavaNamedQuery(this, namedQueryAnnotation);
	}

	protected void syncNamedQueries() {
		this.namedQueryContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<NamedQueryAnnotation> getNamedQueryAnnotations() {
		return IterableTools.downCast(this.getNestableNamedQueryAnnotations_());
	}

	protected ListIterable<NestableAnnotation> getNestableNamedQueryAnnotations_() {
		return this.parent.getResourceAnnotatedElement().getAnnotations(NamedQueryAnnotation.ANNOTATION_NAME);
	}

	protected ContextListContainer<JavaNamedQuery, NamedQueryAnnotation> buildNamedQueryContainer() {
		NamedQueryContainer container = new NamedQueryContainer();
		container.initialize();
		return container;
	}

	/**
	 * named query container
	 */
	protected class NamedQueryContainer
		extends ContextListContainer<JavaNamedQuery, NamedQueryAnnotation>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return NAMED_QUERIES_LIST;
		}
		@Override
		protected JavaNamedQuery buildContextElement(NamedQueryAnnotation resourceElement) {
			return GenericJavaQueryContainer.this.buildNamedQuery(resourceElement);
		}
		@Override
		protected ListIterable<NamedQueryAnnotation> getResourceElements() {
			return GenericJavaQueryContainer.this.getNamedQueryAnnotations();
		}
		@Override
		protected NamedQueryAnnotation getResourceElement(JavaNamedQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
	}


	// ********** named native queries **********

	public ListIterable<JavaNamedNativeQuery> getNamedNativeQueries() {
		return this.namedNativeQueryContainer.getContextElements();
	}

	public int getNamedNativeQueriesSize() {
		return this.namedNativeQueryContainer.getContextElementsSize();
	}

	public JavaNamedNativeQuery addNamedNativeQuery() {
		return this.addNamedNativeQuery(this.getNamedNativeQueriesSize());
	}

	public JavaNamedNativeQuery addNamedNativeQuery(int index) {
		NamedNativeQueryAnnotation annotation = this.addNamedNativeQueryAnnotation(index);
		return this.namedNativeQueryContainer.addContextElement(index, annotation);
	}

	protected NamedNativeQueryAnnotation addNamedNativeQueryAnnotation(int index) {
		return (NamedNativeQueryAnnotation) this.parent.getResourceAnnotatedElement().addAnnotation(index, NamedNativeQueryAnnotation.ANNOTATION_NAME);
	}

	public void removeNamedNativeQuery(NamedNativeQuery namedNativeQuery) {
		this.removeNamedNativeQuery(this.namedNativeQueryContainer.indexOfContextElement((JavaNamedNativeQuery) namedNativeQuery));
	}

	public void removeNamedNativeQuery(int index) {
		this.parent.getResourceAnnotatedElement().removeAnnotation(index, NamedNativeQueryAnnotation.ANNOTATION_NAME);
		this.namedNativeQueryContainer.removeContextElement(index);
	}

	public void moveNamedNativeQuery(int targetIndex, int sourceIndex) {
		this.parent.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, NamedNativeQueryAnnotation.ANNOTATION_NAME);
		this.namedNativeQueryContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected JavaNamedNativeQuery buildNamedNativeQuery(NamedNativeQueryAnnotation namedNativeQueryAnnotation) {
		return this.getJpaFactory().buildJavaNamedNativeQuery(this, namedNativeQueryAnnotation);
	}

	protected void syncNamedNativeQueries() {
		this.namedNativeQueryContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<NamedNativeQueryAnnotation> getNamedNativeQueryAnnotations() {
		return IterableTools.downCast(this.getNestableNamedNativeQueryAnnotations_());
	}

	protected ListIterable<NestableAnnotation> getNestableNamedNativeQueryAnnotations_() {
		return this.parent.getResourceAnnotatedElement().getAnnotations(NamedNativeQueryAnnotation.ANNOTATION_NAME);
	}

	protected ContextListContainer<JavaNamedNativeQuery, NamedNativeQueryAnnotation> buildNamedNativeQueryContainer() {
		NamedNativeQueryContainer container = new NamedNativeQueryContainer();
		container.initialize();
		return container;
	}

	/**
	 * named query container
	 */
	protected class NamedNativeQueryContainer
		extends ContextListContainer<JavaNamedNativeQuery, NamedNativeQueryAnnotation>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return NAMED_NATIVE_QUERIES_LIST;
		}
		@Override
		protected JavaNamedNativeQuery buildContextElement(NamedNativeQueryAnnotation resourceElement) {
			return GenericJavaQueryContainer.this.buildNamedNativeQuery(resourceElement);
		}
		@Override
		protected ListIterable<NamedNativeQueryAnnotation> getResourceElements() {
			return GenericJavaQueryContainer.this.getNamedNativeQueryAnnotations();
		}
		@Override
		protected NamedNativeQueryAnnotation getResourceElement(JavaNamedNativeQuery contextElement) {
			return contextElement.getQueryAnnotation();
		}
	}

	// ********** named stored procedure queries **********

	public ListIterable<JavaNamedStoredProcedureQuery2_1> getNamedStoredProcedureQueries() {
		return this.namedStoredProcedureQueryContainer.getContextElements();
	}

	public int getNamedStoredProcedureQueriesSize() {
		return this.namedStoredProcedureQueryContainer.getContextElementsSize();
	}

	public JavaNamedStoredProcedureQuery2_1 addNamedStoredProcedureQuery() {
		return this.addNamedStoredProcedureQuery(this.getNamedStoredProcedureQueriesSize());
	}

	public JavaNamedStoredProcedureQuery2_1 addNamedStoredProcedureQuery(int index) {
		NamedStoredProcedureQueryAnnotation2_1 annotation = this.addNamedStoredProcedureQueryAnnotation(index);
		return this.namedStoredProcedureQueryContainer.addContextElement(index, annotation);
	}

	protected NamedStoredProcedureQueryAnnotation2_1 addNamedStoredProcedureQueryAnnotation(int index) {
		return (NamedStoredProcedureQueryAnnotation2_1) this.parent.getResourceAnnotatedElement().addAnnotation(index, NamedStoredProcedureQueryAnnotation2_1.ANNOTATION_NAME);
	}

	public void removeNamedStoredProcedureQuery(NamedStoredProcedureQuery2_1 namedStoredProcedureQuery) {
		this.removeNamedStoredProcedureQuery(this.namedStoredProcedureQueryContainer.indexOfContextElement((JavaNamedStoredProcedureQuery2_1) namedStoredProcedureQuery));
	}

	public void removeNamedStoredProcedureQuery(int index) {
		this.parent.getResourceAnnotatedElement().removeAnnotation(index, NamedStoredProcedureQueryAnnotation2_1.ANNOTATION_NAME);
		this.namedStoredProcedureQueryContainer.removeContextElement(index);
	}

	public void moveNamedStoredProcedureQuery(int targetIndex, int sourceIndex) {
		this.parent.getResourceAnnotatedElement().moveAnnotation(targetIndex, sourceIndex, NamedStoredProcedureQueryAnnotation2_1.ANNOTATION_NAME);
		this.namedStoredProcedureQueryContainer.moveContextElement(targetIndex, sourceIndex);
	}

	protected JavaNamedStoredProcedureQuery2_1 buildNamedStoredProcedureQuery(NamedStoredProcedureQueryAnnotation2_1 namedStoredProcedureQueryAnnotation) {
		return this.getJpaFactory2_1().buildJavaNamedStoredProcedureQuery(this, namedStoredProcedureQueryAnnotation);
	}

	protected void syncNamedStoredProcedureQueries() {
		this.namedStoredProcedureQueryContainer.synchronizeWithResourceModel();
	}

	protected ListIterable<NamedStoredProcedureQueryAnnotation2_1> getNamedStoredProcedureQueryAnnotations() {
		return IterableTools.downCast(this.getNestableNamedStoredProcedureQueryAnnotations_());
	}

	protected ListIterable<NestableAnnotation> getNestableNamedStoredProcedureQueryAnnotations_() {
		return this.parent.getResourceAnnotatedElement().getAnnotations(NamedStoredProcedureQueryAnnotation2_1.ANNOTATION_NAME);
	}

	protected ContextListContainer<JavaNamedStoredProcedureQuery2_1, NamedStoredProcedureQueryAnnotation2_1> buildNamedStoredProcedureQueryContainer() {
		NamedStoredProcedureQueryContainer container = new NamedStoredProcedureQueryContainer();
		container.initialize();
		return container;
	}

	/**
	 * named query container
	 */
	protected class NamedStoredProcedureQueryContainer
		extends ContextListContainer<JavaNamedStoredProcedureQuery2_1, NamedStoredProcedureQueryAnnotation2_1>
	{
		@Override
		protected String getContextElementsPropertyName() {
			return NAMED_STORED_PROCEDURE_QUERIES_LIST;
		}
		@Override
		protected JavaNamedStoredProcedureQuery2_1 buildContextElement(NamedStoredProcedureQueryAnnotation2_1 resourceElement) {
			return GenericJavaQueryContainer.this.buildNamedStoredProcedureQuery(resourceElement);
		}
		@Override
		protected ListIterable<NamedStoredProcedureQueryAnnotation2_1> getResourceElements() {
			return GenericJavaQueryContainer.this.getNamedStoredProcedureQueryAnnotations();
		}
		@Override
		protected NamedStoredProcedureQueryAnnotation2_1 getResourceElement(JavaNamedStoredProcedureQuery2_1 contextElement) {
			return contextElement.getQueryAnnotation();
		}
	}


	// ********** validation **********

	/**
	 * The queries are validated in the persistence unit.
	 * @see org.eclipse.jpt.jpa.core.internal.context.persistence.AbstractPersistenceUnit#validateQueries(List, IReporter)
	 */
	@Override
	public void validate(List<IMessage> messages, IReporter reporter) {
		super.validate(messages, reporter);
		// queries are validated in the persistence unit
	}

	public TextRange getValidationTextRange() {
		TextRange textRange = this.parent.getResourceAnnotatedElement().getTextRange();
		return (textRange != null) ? textRange : this.parent.getValidationTextRange();
	}
}
