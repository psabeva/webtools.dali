/*******************************************************************************
 * Copyright (c) 2008, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.internal.swt.bind;

import org.eclipse.jpt.common.ui.internal.listeners.SWTPropertyChangeListenerWrapper;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.model.event.PropertyChangeEvent;
import org.eclipse.jpt.common.utility.model.listener.PropertyChangeListener;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.widgets.Text;

/**
 * This binding can be used to keep a text field
 * synchronized with a model text/string.
 * 
 * @see ModifiablePropertyValueModel
 * @see Text
 */
@SuppressWarnings("nls")
class TextFieldModelBinding {

	/**
	 * The text model we keep synchronized with the text field.
	 */
	private final ModifiablePropertyValueModel<String> textModel;

	/**
	 * A listener that allows us to synchronize the text field's contents with
	 * the text model.
	 */
	private final PropertyChangeListener textModelChangeListener;

	/**
	 * The text field we keep synchronized with the text model.
	 */
	private final Text textField;

	/**
	 * A listener that allows us to synchronize our text model
	 * with the text field's contents.
	 */
	private final ModifyListener textFieldModifyListener;

	/**
	 * A listener that allows us to stop listening to stuff when the text field
	 * is disposed.
	 */
	private final DisposeListener textFieldDisposeListener;

	/**
	 * Hmm...
	 */
	private boolean settingTextFieldText = false;


	// ********** constructor **********

	/**
	 * Constructor - the text model and text field are required.
	 */
	TextFieldModelBinding(ModifiablePropertyValueModel<String> textModel, Text textField) {
		super();
		if ((textModel == null) || (textField == null)) {
			throw new NullPointerException();
		}
		this.textModel = textModel;
		this.textField = textField;

		this.textModelChangeListener = this.buildTextModelChangeListener();
		this.textModel.addPropertyChangeListener(PropertyValueModel.VALUE, this.textModelChangeListener);

		this.textFieldModifyListener = this.buildTextFieldModifyListener();
		this.textField.addModifyListener(this.textFieldModifyListener);

		this.textFieldDisposeListener = this.buildTextFieldDisposeListener();
		this.textField.addDisposeListener(this.textFieldDisposeListener);

		this.setTextFieldText(textModel.getValue());
	}


	// ********** initialization **********

	private PropertyChangeListener buildTextModelChangeListener() {
		return new SWTPropertyChangeListenerWrapper(this.buildTextModelChangeListener_());
	}

	private PropertyChangeListener buildTextModelChangeListener_() {
		return new PropertyChangeListener() {
			public void propertyChanged(PropertyChangeEvent event) {
				TextFieldModelBinding.this.textModelChanged(event);
			}
			@Override
			public String toString() {
				return "text listener";
			}
		};
	}

	private ModifyListener buildTextFieldModifyListener() {
		return new ModifyListener() {
			public void modifyText(ModifyEvent event) {
				TextFieldModelBinding.this.textFieldModified();
			}
			@Override
			public String toString() {
				return "text field modify listener";
			}
		};
	}

	private DisposeListener buildTextFieldDisposeListener() {
		return new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				TextFieldModelBinding.this.textFieldDisposed();
			}
			@Override
			public String toString() {
				return "text field dispose listener";
			}
		};
	}


	// ********** text model events **********

	/* CU private */ void textModelChanged(PropertyChangeEvent event) {
		if ( ! this.textField.isDisposed()) {  // ???
			this.setTextFieldText((String) event.getNewValue());
		}
	}

	private void setTextFieldText(String text) {
		// the text model can be null, but the text field cannot
		this.setTextFieldText_((text == null) ? "" : text);
	}

	private void setTextFieldText_(String text) {
		if ( ! text.equals(this.textField.getText())) {  // ???
			this.setTextFieldText__(text);
		}
	}

	private void setTextFieldText__(String text) {
		this.settingTextFieldText = true;
		try {
			this.textField.setText(text);
		} finally {
			this.settingTextFieldText = false;
		}
	}


	// ********** text field events **********

	/* CU private */ void textFieldModified() {
		if ( ! this.settingTextFieldText) {
			this.setTextModelText(this.textField.getText());
		}
	}

	private void setTextModelText(String text) {
		if ( ! text.equals(this.textModel.getValue())) {  // ???
			this.textModel.setValue(text);
		}
	}

	/* CU private */ void textFieldDisposed() {
		// the text field is not yet "disposed" when we receive this event
		// so we can still remove our listeners
		this.textField.removeDisposeListener(this.textFieldDisposeListener);
		this.textField.removeModifyListener(this.textFieldModifyListener);
		this.textModel.removePropertyChangeListener(PropertyValueModel.VALUE, this.textModelChangeListener);
	}


	// ********** misc **********

	@Override
	public String toString() {
		return ObjectTools.toString(this, this.textModel);
	}
}
