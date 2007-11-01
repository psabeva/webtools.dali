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

import java.util.Arrays;

import org.eclipse.jpt.utility.internal.model.Model;
import org.eclipse.jpt.utility.internal.model.event.ListChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.ListChangeListener;

/**
 * Extend ValueAspectPropertyValueModelAdapter to listen to one or more list
 * aspects of the value in the wrapped value model.
 */
public class ValueListPropertyValueModelAdapter
	extends ValueAspectPropertyValueModelAdapter
{

	/** The names of the value's lists that we listen to. */
	protected final String[] listNames;

	/** Listener that listens to the value. */
	protected ListChangeListener valueListListener;


	// ********** constructors **********

	/**
	 * Construct an adapter for the specified value list.
	 */
	public ValueListPropertyValueModelAdapter(PropertyValueModel valueHolder, String listName) {
		this(valueHolder, new String[] {listName});
	}

	/**
	 * Construct an adapter for the specified value lists.
	 */
	public ValueListPropertyValueModelAdapter(PropertyValueModel valueHolder, String listName1, String listName2) {
		this(valueHolder, new String[] {listName1, listName2});
	}

	/**
	 * Construct an adapter for the specified value lists.
	 */
	public ValueListPropertyValueModelAdapter(PropertyValueModel valueHolder, String listName1, String listName2, String listName3) {
		this(valueHolder, new String[] {listName1, listName2, listName3});
	}

	/**
	 * Construct an adapter for the specified value lists.
	 */
	public ValueListPropertyValueModelAdapter(PropertyValueModel valueHolder, String[] listNames) {
		super(valueHolder);
		this.listNames = listNames;
	}


	// ********** initialization **********

	@Override
	protected void initialize() {
		super.initialize();
		this.valueListListener = this.buildValueListListener();
	}

	/**
	 * All we really care about is the fact that a List aspect has 
	 * changed. Do the same thing no matter which event occurs.
	 */
	protected ListChangeListener buildValueListListener() {
		return new ListChangeListener() {
			public void itemsAdded(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			public void itemsRemoved(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			public void itemsReplaced(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			public void itemsMoved(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			public void listCleared(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			public void listChanged(ListChangeEvent e) {
				ValueListPropertyValueModelAdapter.this.valueAspectChanged();
			}
			@Override
			public String toString() {
				return "value list listener: " + Arrays.asList(ValueListPropertyValueModelAdapter.this.listNames);
			}
		};
	}

	@Override
	protected void startListeningToValue() {
		Model v = (Model) this.value;
		for (int i = this.listNames.length; i-- > 0; ) {
			v.addListChangeListener(this.listNames[i], this.valueListListener);
		}
	}

	@Override
	protected void stopListeningToValue() {
		Model v = (Model) this.value;
		for (int i = this.listNames.length; i-- > 0; ) {
			v.removeListChangeListener(this.listNames[i], this.valueListListener);
		}
	}

}
