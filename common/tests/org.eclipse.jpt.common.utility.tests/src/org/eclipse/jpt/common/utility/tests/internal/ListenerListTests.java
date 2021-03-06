/*******************************************************************************
 * Copyright (c) 2008, 2010 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.tests.internal;

import java.io.Serializable;
import java.util.EventListener;

import junit.framework.TestCase;

import org.eclipse.jpt.common.utility.internal.ListenerList;
import org.eclipse.jpt.common.utility.internal.SystemTools;
import org.eclipse.jpt.common.utility.internal.iterable.IterableTools;

@SuppressWarnings("nls")
public class ListenerListTests extends TestCase {

	public ListenerListTests(String name) {
		super(name);
	}

	public void testGetListeners() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		Iterable<Listener> listeners = listenerList.getListeners();
		assertEquals(0, IterableTools.size(listeners));

		listenerList.add(listener1);
		listenerList.add(listener2);
		listeners = listenerList.getListeners();
		assertEquals(2, IterableTools.size(listeners));
		assertTrue(IterableTools.contains(listeners, listener1));
		assertTrue(IterableTools.contains(listeners, listener2));
	}

	public void testSize() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		assertEquals(0, listenerList.size());

		listenerList.add(listener1);
		listenerList.add(listener2);
		assertEquals(2, listenerList.size());
	}

	public void testIsEmpty() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		assertTrue(listenerList.isEmpty());

		listenerList.add(listener1);
		listenerList.add(listener2);
		assertFalse(listenerList.isEmpty());
	}

	public void testAdd_null() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		boolean exCaught = false;
		try {
			listenerList.add(null);
			fail("invalid listener list: " + listenerList);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAdd_duplicate() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener = new LocalListener();
		listenerList.add(listener);

		boolean exCaught = false;
		try {
			listenerList.add(listener);
			fail("invalid listener list: " + listenerList);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemove() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		listenerList.add(listener1);
		listenerList.add(listener2);
		assertTrue(IterableTools.contains(listenerList.getListeners(), listener1));
		assertTrue(IterableTools.contains(listenerList.getListeners(), listener2));

		listenerList.remove(listener1);
		assertFalse(IterableTools.contains(listenerList.getListeners(), listener1));
		assertTrue(IterableTools.contains(listenerList.getListeners(), listener2));

		listenerList.remove(listener2);
		assertFalse(IterableTools.contains(listenerList.getListeners(), listener2));
	}

	public void testRemove_null() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		boolean exCaught = false;
		try {
			listenerList.remove(null);
			fail("invalid listener list: " + listenerList);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemove_unregistered() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener = new LocalListener();
		listenerList.add(listener);
		listenerList.remove(listener);

		boolean exCaught = false;
		try {
			listenerList.remove(listener);
			fail("invalid listener list: " + listenerList);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testClear() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		listenerList.add(listener1);
		listenerList.add(listener2);
		assertTrue(IterableTools.contains(listenerList.getListeners(), listener1));
		assertTrue(IterableTools.contains(listenerList.getListeners(), listener2));

		listenerList.clear();
		assertFalse(IterableTools.contains(listenerList.getListeners(), listener1));
		assertFalse(IterableTools.contains(listenerList.getListeners(), listener2));
	}

	public void testSerialization() throws Exception {
		// This test doesn't pass in the Eclipse build environment (Linux/IBM VM) for some reason
		if (SystemTools.jvmIsSun()) {
			this.verifySerialization();
		}
	}

	private void verifySerialization() throws Exception {
		ListenerList<Listener> listenerList = new ListenerList<Listener>(Listener.class);
		Listener listener1 = new LocalListener();
		Listener listener2 = new LocalListener();
		listenerList.add(listener1);
		listenerList.add(listener2);

		ListenerList<Listener> listenerList2 = TestTools.serialize(listenerList);
		assertNotSame(listenerList, listenerList2);
		assertEquals(2, listenerList2.size());

		Listener listener3 = new NonSerializableListener();
		listenerList.add(listener3);

		listenerList2 = TestTools.serialize(listenerList);
		assertNotSame(listenerList, listenerList2);
		assertEquals(2, listenerList2.size());

	}

	interface Listener extends EventListener {
		void somethingHappened();
	}
	
	static class LocalListener implements Listener, Serializable {
		private static final long serialVersionUID = 1L;
		public void somethingHappened() {
			// do nothing
		}
	}
	
	static class NonSerializableListener implements Listener {
		public void somethingHappened() {
			// do nothing
		}
	}
	
}
