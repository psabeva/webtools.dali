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

import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;

/**
 * Abstract property value model that provides behavior for wrapping a property
 * value model and listening for changes to aspects of the *value* contained
 * by the value model. Changes to the actual value are also monitored.
 * 
 * This is useful if you have a value that may change, but whose aspects can also
 * change in a fashion that might change the value's external appearance.
 * 
 * Subclasses need to override two methods:
 * 
 * listenToValue(Model)
 *     begin listening to the appropriate aspect of the specified value and call
 *     #valueAspectChanged(Object) whenever the aspect changes
 * 
 * stopListeningToValue(Model)
 *     stop listening to the appropriate aspect of the specified value
 */
public abstract class ValueAspectPropertyValueModelAdapter
	extends PropertyValueModelWrapper
{
	/** Cache the value so we can disengage. */
	protected Object value;


	// ********** constructor **********

	/**
	 * Constructor - the value holder is required.
	 */
	protected ValueAspectPropertyValueModelAdapter(PropertyValueModel valueHolder) {
		super(valueHolder);
	}


	// ********** initialization **********

	@Override
	protected void initialize() {
		super.initialize();
		this.value = null;
	}


	// ********** ValueModel implementation **********

	public Object getValue() {
		return this.value;
	}


	// ********** PropertyValueModel implementation **********

	public void setValue(Object value) {
		this.valueHolder.setValue(value);
	}


	// ********** PropertyValueModelWrapper implementation **********

	@Override
	protected void valueChanged(PropertyChangeEvent e) {
		this.disengageValue();
		this.engageValue();
		this.firePropertyChanged(e.propertyName(), e.oldValue(), e.newValue());
	}


	// ********** behavior **********

	/**
	 * Start listening to the value holder and the value.
	 */
	@Override
	protected void engageValueHolder() {
		super.engageValueHolder();
		this.engageValue();
	}

	protected void engageValue() {
		this.value = this.valueHolder.getValue();
		if (this.value != null) {
			this.startListeningToValue();
		}
	}

	/**
	 * Start listening to the current value.
	 * At this point we can be sure that the value is not null.
	 */
	protected abstract void startListeningToValue();

	/**
	 * Stop listening to the value holder and the value.
	 */
	@Override
	protected void disengageValueHolder() {
		this.disengageValue();
		super.disengageValueHolder();
	}

	protected void disengageValue() {
		if (this.value != null) {
			this.stopListeningToValue();
			this.value = null;
		}
	}

	/**
	 * Stop listening to the current value.
	 * At this point we can be sure that the value is not null.
	 */
	protected abstract void stopListeningToValue();

	protected void valueAspectChanged() {
		this.firePropertyChanged(VALUE, this.value);		// hmmm...
	}

}
