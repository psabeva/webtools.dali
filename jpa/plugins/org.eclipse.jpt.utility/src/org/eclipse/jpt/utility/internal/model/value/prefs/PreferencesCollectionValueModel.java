/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.internal.model.value.prefs;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.prefs.BackingStoreException;
import java.util.prefs.PreferenceChangeEvent;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.Preferences;

import org.eclipse.jpt.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.utility.internal.iterators.TransformationIterator;
import org.eclipse.jpt.utility.internal.model.value.AspectAdapter;
import org.eclipse.jpt.utility.internal.model.value.CollectionValueModel;
import org.eclipse.jpt.utility.internal.model.value.ValueModel;

/**
 * This adapter wraps a Preferences node and converts its preferences into a
 * CollectionValueModel of PreferencePropertyValueModels. It listens for
 * "preference" changes and converts them into VALUE collection changes.
 */
public class PreferencesCollectionValueModel
	extends AspectAdapter
	implements CollectionValueModel
{

	/** Cache the current preferences, stored in models and keyed by name. */
	protected Map preferences;

	/** A listener that listens to the preferences node for added or removed preferences. */
	protected PreferenceChangeListener preferenceChangeListener;


	// ********** constructors **********

	/**
	 * Construct an adapter for the specified preferences node.
	 */
	public PreferencesCollectionValueModel(Preferences preferences) {
		super(preferences);
	}

	/**
	 * Construct an adapter for the specified preferences node.
	 */
	public PreferencesCollectionValueModel(ValueModel preferencesHolder) {
		super(preferencesHolder);
	}


	// ********** initialization **********

	@Override
	protected void initialize() {
		super.initialize();
		this.preferences = new HashMap();
		this.preferenceChangeListener = this.buildPreferenceChangeListener();
	}

	/**
	 * A preferences have changed, notify the listeners.
	 */
	protected PreferenceChangeListener buildPreferenceChangeListener() {
		// transform the preference change events into VALUE collection change events
		return new PreferenceChangeListener() {
			public void preferenceChange(PreferenceChangeEvent e) {
				PreferencesCollectionValueModel.this.preferenceChanged(e.getKey(), e.getNewValue());
			}
			@Override
			public String toString() {
				return "preference change listener";
			}
		};
	}


	// ********** ValueModel implementation **********

	/**
	 * Return an iterator on the preference models.
	 */
	public synchronized Object getValue() {
		return this.preferences.values().iterator();
	}


	// ********** CollectionValueModel implementation **********

	public void addItem(Object item) {
		throw new UnsupportedOperationException();
	}

	public void addItems(Collection items) {
		for (Iterator stream = items.iterator(); stream.hasNext(); ) {
			this.addItem(stream.next());
		}
	}

	public void removeItem(Object item) {
		throw new UnsupportedOperationException();
	}

	public void removeItems(Collection items) {
		for (Iterator stream = items.iterator(); stream.hasNext(); ) {
			this.removeItem(stream.next());
		}
	}

	public synchronized int size() {
		return this.preferences.size();
	}


	// ********** AspectAdapter implementation **********

    @Override
	protected boolean hasListeners() {
		return this.hasAnyCollectionChangeListeners(VALUE);
	}

    @Override
	protected void fireAspectChange(Object oldValue, Object newValue) {
		this.fireCollectionChanged(VALUE);
	}

    @Override
	protected void engageNonNullSubject() {
		((Preferences) this.subject).addPreferenceChangeListener(this.preferenceChangeListener);
		for (Iterator stream = this.preferenceModels(); stream.hasNext(); ) {
			PreferencePropertyValueModel preferenceModel = (PreferencePropertyValueModel) stream.next();
			this.preferences.put(preferenceModel.getKey(), preferenceModel);
		}
	}

    @Override
	protected void disengageNonNullSubject() {
		try {
			((Preferences) this.subject).removePreferenceChangeListener(this.preferenceChangeListener);
		} catch (IllegalStateException ex) {
			// for some odd reason, we are not allowed to remove a listener from a "dead"
			// preferences node; so handle the exception that gets thrown here
			if ( ! ex.getMessage().equals("Node has been removed.")) {
				// if it is not the expected exception, re-throw it
				throw ex;
			}
		}
		this.preferences.clear();
	}


	// ********** AbstractModel implementation **********

	@Override
	public void toString(StringBuilder sb) {
		sb.append(this.subject);
	}


	// ********** internal methods **********

	/**
	 * Return an iterator on the preference models.
	 * At this point we can be sure that the subject is not null.
	 */
	protected Iterator preferenceModels() {
		String[] keys;
		try {
			keys = ((Preferences) this.subject).keys();
		} catch (BackingStoreException ex) {
			throw new RuntimeException(ex);
		}
		return new TransformationIterator(new ArrayIterator(keys)) {
			protected Object transform(Object next) {
				return PreferencesCollectionValueModel.this.buildPreferenceModel((String) next);
			}
		};
	}

	/**
	 * Override this method to tweak the model used to wrap the
	 * specified preference (e.g. to customize the model's converter).
	 */
	protected PreferencePropertyValueModel buildPreferenceModel(String key) {
		return new PreferencePropertyValueModel(this.subjectHolder, key);
	}

	protected synchronized void preferenceChanged(String key, String newValue) {
		if (newValue == null) {
			// a preference was removed
			PreferencePropertyValueModel preferenceModel = (PreferencePropertyValueModel) this.preferences.remove(key);
			this.fireItemRemoved(VALUE, preferenceModel);
		} else if ( ! this.preferences.containsKey(key)) {
			// a preference was added
			PreferencePropertyValueModel preferenceModel = this.buildPreferenceModel(key);
			this.preferences.put(key, preferenceModel);
			this.fireItemAdded(VALUE, preferenceModel);
		} else {
			// a preference's value changed - do nothing
		}
	}

}
