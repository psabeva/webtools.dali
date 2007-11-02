/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.model.value.prefs;

import java.util.List;
import java.util.prefs.AbstractPreferences;
import java.util.prefs.Preferences;

import org.eclipse.jpt.utility.internal.ClassTools;
import org.eclipse.jpt.utility.tests.internal.TestTools;

import junit.framework.TestCase;

/**
 * set up and tear down a test node for any subclass that
 * needs to test preferences-related stuff
 */
public abstract class PreferencesTestCase extends TestCase {
	protected Preferences classNode;
	public Preferences testNode;
	protected static final String TEST_NODE_NAME = "test node";

	public PreferencesTestCase(String name) {
		super(name);
	}

	@Override
	protected void setUp() throws Exception {
		super.setUp();
		Preferences packageNode = Preferences.userNodeForPackage(this.getClass());
		this.classNode = packageNode.node(ClassTools.shortClassNameForObject(this));
		// clean out any leftover crap...
		if ((this.classNode.keys().length > 0) || (this.classNode.childrenNames().length > 0)) {
			this.classNode.removeNode();
			// ...and re-create the node
			this.classNode = packageNode.node(ClassTools.shortClassNameForObject(this));
		}
		this.testNode = this.classNode.node(TEST_NODE_NAME);
	}

	@Override
	protected void tearDown() throws Exception {
		// wait for all the events to be delivered before tearing down
		this.waitForEventQueueToClear();
		Preferences node = this.classNode.parent();
		this.classNode.removeNode();
		while (this.nodeIsVestigial(node)) {
			Preferences parent = node.parent();
			node.removeNode();
			node = parent;
		}
		TestTools.clear(this);
		super.tearDown();
	}

	private boolean nodeIsVestigial(Preferences node) throws Exception {
		return (node != null)
			&& (node.keys().length == 0)
			&& (node.childrenNames().length == 0)
			&& (node != Preferences.userRoot());
	}

	protected void waitForEventQueueToClear() {
		try {
			while ( ! this.preferencesEventQueue().isEmpty()) {
				Thread.sleep(100);
			}
			Thread.sleep(100);
		} catch (InterruptedException ex) {
			throw new RuntimeException(ex);
		}
	}

	private List preferencesEventQueue() {
		return (List) ClassTools.getStaticFieldValue(AbstractPreferences.class, "eventQueue");
	}

}
