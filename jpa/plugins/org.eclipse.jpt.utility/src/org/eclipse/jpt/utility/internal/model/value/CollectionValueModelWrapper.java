/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value;

import java.util.Collection;
import java.util.Iterator;

import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.ChangeSupport;
import org.eclipse.jpt.utility.internal.model.event.CollectionChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.CollectionChangeListener;

/**
 * This abstract class provides the infrastructure needed to wrap
 * another collection value model, "lazily" listen to it, and propagate
 * its change notifications.
 */
public abstract class CollectionValueModelWrapper
	extends AbstractModel
	implements CollectionValueModel
{


	/** The wrapped collection value model. */
	protected CollectionValueModel collectionHolder;

	/** A listener that allows us to synch with changes to the wrapped collection holder. */
	protected CollectionChangeListener collectionChangeListener;


	// ********** constructors **********

	/**
	 * Construct a collection value model with the specified wrapped
	 * collection value model.
	 */
	protected CollectionValueModelWrapper(CollectionValueModel collectionHolder) {
		super();
		this.collectionHolder = collectionHolder;
	}
	

	// ********** initialization **********

	@Override
	protected void initialize() {
		super.initialize();
		this.collectionChangeListener = this.buildCollectionChangeListener();
	}

	@Override
	protected ChangeSupport buildChangeSupport() {
		return new ValueModelChangeSupport(this);
	}

	protected CollectionChangeListener buildCollectionChangeListener() {
		return new CollectionChangeListener() {
			public void itemsAdded(CollectionChangeEvent e) {
				CollectionValueModelWrapper.this.itemsAdded(e);
			}		
			public void itemsRemoved(CollectionChangeEvent e) {
				CollectionValueModelWrapper.this.itemsRemoved(e);
			}
			public void collectionCleared(CollectionChangeEvent e) {
				CollectionValueModelWrapper.this.collectionCleared(e);
			}
			public void collectionChanged(CollectionChangeEvent e) {
				CollectionValueModelWrapper.this.collectionChanged(e);
			}
			@Override
			public String toString() {
				return "collection change listener";
			}
		};
	}


	// ********** extend change support **********

	/**
	 * Extend to start listening to the nested model if necessary.
	 */
	@Override
	public synchronized void addCollectionChangeListener(CollectionChangeListener listener) {
		if (this.hasNoCollectionChangeListeners(VALUE)) {
			this.engageModel();
		}
		super.addCollectionChangeListener(listener);
	}
	
	/**
	 * Extend to start listening to the nested model if necessary.
	 */
	@Override
	public synchronized void addCollectionChangeListener(String collectionName, CollectionChangeListener listener) {
		if (collectionName == VALUE && this.hasNoCollectionChangeListeners(VALUE)) {
			this.engageModel();
		}
		super.addCollectionChangeListener(collectionName, listener);
	}
	
	/**
	 * Extend to stop listening to the nested model if necessary.
	 */
	@Override
	public synchronized void removeCollectionChangeListener(CollectionChangeListener listener) {
		super.removeCollectionChangeListener(listener);
		if (this.hasNoCollectionChangeListeners(VALUE)) {
			this.disengageModel();
		}
	}
	
	/**
	 * Extend to stop listening to the nested model if necessary.
	 */
	@Override
	public synchronized void removeCollectionChangeListener(String collectionName, CollectionChangeListener listener) {
		super.removeCollectionChangeListener(collectionName, listener);
		if (collectionName == VALUE && this.hasNoCollectionChangeListeners(VALUE)) {
			this.disengageModel();
		}
	}


	// ********** CollectionValueModel implementation **********

	/**
	 * wrappers cannot be modified - the underlying model must be modified directly
	 */
	public void addItem(Object item) {
		throw new UnsupportedOperationException();
	}

	public void addItems(Collection items) {
		for (Iterator stream = items.iterator(); stream.hasNext(); ) {
			this.addItem(stream.next());
		}
	}

	/**
	 * wrappers cannot be modified - the underlying model must be modified directly
	 */
	public void removeItem(Object item) {
		throw new UnsupportedOperationException();
	}

	public void removeItems(Collection items) {
		for (Iterator stream = items.iterator(); stream.hasNext(); ) {
			this.removeItem(stream.next());
		}
	}


	// ********** behavior **********

	/**
	 * Start listening to the collection holder.
	 */
	protected void engageModel() {
		this.collectionHolder.addCollectionChangeListener(VALUE, this.collectionChangeListener);
	}

	/**
	 * Stop listening to the collection holder.
	 */
	protected void disengageModel() {
		this.collectionHolder.removeCollectionChangeListener(VALUE, this.collectionChangeListener);
	}

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.collectionHolder);
	}


	// ********** collection change support **********

	/**
	 * Items were added to the wrapped collection holder;
	 * propagate the change notification appropriately.
	 */
	protected abstract void itemsAdded(CollectionChangeEvent e);

	/**
	 * Items were removed from the wrapped collection holder;
	 * propagate the change notification appropriately.
	 */
	protected abstract void itemsRemoved(CollectionChangeEvent e);

	/**
	 * The wrapped collection holder was cleared;
	 * propagate the change notification appropriately.
	 */
	protected abstract void collectionCleared(CollectionChangeEvent e);

	/**
	 * The value of the wrapped collection holder has changed;
	 * propagate the change notification appropriately.
	 */
	protected abstract void collectionChanged(CollectionChangeEvent e);

}
