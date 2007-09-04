/*******************************************************************************
 * Copyright (c) 2007 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.utility.tests.internal.model;

import java.io.IOException;
import java.io.Serializable;
import java.util.Arrays;

import org.eclipse.jpt.utility.internal.ClassTools;
import org.eclipse.jpt.utility.internal.model.AbstractModel;
import org.eclipse.jpt.utility.internal.model.ChangeSupport;
import org.eclipse.jpt.utility.internal.model.event.CollectionChangeEvent;
import org.eclipse.jpt.utility.internal.model.event.ListChangeEvent;
import org.eclipse.jpt.utility.internal.model.event.PropertyChangeEvent;
import org.eclipse.jpt.utility.internal.model.event.StateChangeEvent;
import org.eclipse.jpt.utility.internal.model.event.TreeChangeEvent;
import org.eclipse.jpt.utility.internal.model.listener.ChangeListener;
import org.eclipse.jpt.utility.internal.model.listener.CollectionChangeListener;
import org.eclipse.jpt.utility.internal.model.listener.ListChangeAdapter;
import org.eclipse.jpt.utility.internal.model.listener.ListChangeListener;
import org.eclipse.jpt.utility.internal.model.listener.PropertyChangeListener;
import org.eclipse.jpt.utility.internal.model.listener.StateChangeListener;
import org.eclipse.jpt.utility.internal.model.listener.TreeChangeListener;
import org.eclipse.jpt.utility.tests.internal.TestTools;

import junit.framework.TestCase;

public class AbstractModelTests
	extends TestCase
	implements StateChangeListener, PropertyChangeListener, CollectionChangeListener, ListChangeListener, TreeChangeListener
{
	private TestModel testModel;
	private static final String TEST_TO_STRING = "this is a test";

	private StateChangeEvent stateChangeEvent;
	private boolean stateChangedCalled = false;

	private PropertyChangeEvent propertyChangeEvent;
	private boolean propertyChangeCalled = false;
	private static final String PROPERTY_NAME = "propertyName";
	static final Object OLD_OBJECT_VALUE = new Object();
	static final Object NEW_OBJECT_VALUE = new Object();
	static final Integer OLD_INT_VALUE = new Integer(27);
	static final Integer NEW_INT_VALUE = new Integer(42);
	static final Boolean OLD_BOOLEAN_VALUE = Boolean.TRUE;
	static final Boolean NEW_BOOLEAN_VALUE = Boolean.FALSE;

	private CollectionChangeEvent collectionChangeEvent;
	private boolean itemsAddedCollectionCalled = false;
	private boolean itemsRemovedCollectionCalled = false;
	private boolean collectionChangedCalled = false;
	private boolean collectionClearedCalled = false;
	private static final String COLLECTION_NAME = "collectionName";
	static final Object ADDED_OBJECT_VALUE = new Object();
	static final Object REMOVED_OBJECT_VALUE = new Object();
	static final int TARGET_INDEX = 7;
	static final int SOURCE_INDEX = 22;

	private ListChangeEvent listChangeEvent;
	private boolean itemsAddedListCalled = false;
	private boolean itemsRemovedListCalled = false;
	private boolean itemsReplacedListCalled = false;
	private boolean itemsMovedListCalled = false;
	private boolean listChangedCalled = false;
	private boolean listClearedCalled = false;
	private static final String LIST_NAME = "listName";
	private static final int ADD_INDEX = 3;
	private static final int REMOVE_INDEX = 5;
	private static final int REPLACE_INDEX = 2;

	private TreeChangeEvent treeChangeEvent;
	private boolean nodeAddedCalled = false;
	private boolean nodeRemovedCalled = false;
	private boolean treeChangedCalled = false;
	private boolean treeClearedCalled = false;
	private static final String TREE_NAME = "treeName";
	static final Object[] OBJECT_ARRAY_PATH = {new Object(), new Object(), new String()};
	static final Object[] EMPTY_PATH = {};

	public AbstractModelTests(String name) {
		super(name);
	}
	
	@Override
	protected void setUp() throws Exception {
		super.setUp();
		this.testModel = new TestModel();
	}

	@Override
	protected void tearDown() throws Exception {
		TestTools.clear(this);
		super.tearDown();
	}

	public void testFireStateChange() {
		this.stateChangeEvent = null;
		this.stateChangedCalled = false;
		this.testModel.addStateChangeListener(this);
		this.testModel.testFireStateChange();
		assertNotNull(this.stateChangeEvent);
		assertEquals(this.testModel, this.stateChangeEvent.getSource());
		assertTrue(this.stateChangedCalled);
	}

	public void testFirePropertyChangeObjectObject() {
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(this);
		this.testModel.testFirePropertyChangeObjectObject();
		this.verifyPropertyChangeEvent(OLD_OBJECT_VALUE, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(this);
		this.testModel.testFirePropertyChangeObjectObject();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeObjectObject();
		this.verifyPropertyChangeEvent(OLD_OBJECT_VALUE, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeObjectObject();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);
	}

	public void testFirePropertyChangeObject() {
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(this);
		this.testModel.testFirePropertyChangeObject();
		this.verifyPropertyChangeEvent(null, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(this);
		this.testModel.testFirePropertyChangeObject();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeObject();
		this.verifyPropertyChangeEvent(null, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeObject();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);
	}

	public void testFirePropertyChangeIntInt() {
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(this);
		this.testModel.testFirePropertyChangeIntInt();
		this.verifyPropertyChangeEvent(OLD_INT_VALUE, NEW_INT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(this);
		this.testModel.testFirePropertyChangeIntInt();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeIntInt();
		this.verifyPropertyChangeEvent(OLD_INT_VALUE, NEW_INT_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeIntInt();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);
	}

	public void testFirePropertyChangeBooleanBoolean() {
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(this);
		this.testModel.testFirePropertyChangeBooleanBoolean();
		this.verifyPropertyChangeEvent(OLD_BOOLEAN_VALUE, NEW_BOOLEAN_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(this);
		this.testModel.testFirePropertyChangeBooleanBoolean();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.addPropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeBooleanBoolean();
		this.verifyPropertyChangeEvent(OLD_BOOLEAN_VALUE, NEW_BOOLEAN_VALUE);
		assertTrue(this.propertyChangeCalled);

		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.removePropertyChangeListener(PROPERTY_NAME, this);
		this.testModel.testFirePropertyChangeBooleanBoolean();
		assertNull(this.propertyChangeEvent);
		assertFalse(this.propertyChangeCalled);
	}

	public void testFireItemAddedCollection() {
		this.collectionChangeEvent = null;
		this.itemsAddedCollectionCalled = false;
		this.testModel.addCollectionChangeListener(this);
		this.testModel.testFireItemAddedCollection();
		this.verifyCollectionChangeEvent(ADDED_OBJECT_VALUE);
		assertTrue(this.itemsAddedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsAddedCollectionCalled = false;
		this.testModel.removeCollectionChangeListener(this);
		this.testModel.testFireItemAddedCollection();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.itemsAddedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsAddedCollectionCalled = false;
		this.testModel.addCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireItemAddedCollection();
		this.verifyCollectionChangeEvent(ADDED_OBJECT_VALUE);
		assertTrue(this.itemsAddedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsAddedCollectionCalled = false;
		this.testModel.removeCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireItemAddedCollection();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.itemsAddedCollectionCalled);
	}

	public void testFireItemRemovedCollection() {
		this.collectionChangeEvent = null;
		this.itemsRemovedCollectionCalled = false;
		this.testModel.addCollectionChangeListener(this);
		this.testModel.testFireItemRemovedCollection();
		this.verifyCollectionChangeEvent(REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsRemovedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsRemovedCollectionCalled = false;
		this.testModel.removeCollectionChangeListener(this);
		this.testModel.testFireItemRemovedCollection();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.itemsRemovedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsRemovedCollectionCalled = false;
		this.testModel.addCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireItemRemovedCollection();
		this.verifyCollectionChangeEvent(REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsRemovedCollectionCalled);

		this.collectionChangeEvent = null;
		this.itemsRemovedCollectionCalled = false;
		this.testModel.removeCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireItemRemovedCollection();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.itemsRemovedCollectionCalled);
	}

	public void testFireCollectionCleared() {
		this.collectionChangeEvent = null;
		this.collectionClearedCalled = false;
		this.testModel.addCollectionChangeListener(this);
		this.testModel.testFireCollectionCleared();
		this.verifyCollectionChangeEvent(null);
		assertTrue(this.collectionClearedCalled);

		this.collectionChangeEvent = null;
		this.collectionClearedCalled = false;
		this.testModel.removeCollectionChangeListener(this);
		this.testModel.testFireCollectionCleared();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.collectionClearedCalled);

		this.collectionChangeEvent = null;
		this.collectionClearedCalled = false;
		this.testModel.addCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireCollectionCleared();
		this.verifyCollectionChangeEvent(null);
		assertTrue(this.collectionClearedCalled);

		this.collectionChangeEvent = null;
		this.collectionClearedCalled = false;
		this.testModel.removeCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireCollectionCleared();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.collectionClearedCalled);
	}

	public void testFireCollectionChanged() {
		this.collectionChangeEvent = null;
		this.collectionChangedCalled = false;
		this.testModel.addCollectionChangeListener(this);
		this.testModel.testFireCollectionChanged();
		this.verifyCollectionChangeEvent(null);
		assertTrue(this.collectionChangedCalled);

		this.collectionChangeEvent = null;
		this.collectionChangedCalled = false;
		this.testModel.removeCollectionChangeListener(this);
		this.testModel.testFireCollectionChanged();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.collectionChangedCalled);

		this.collectionChangeEvent = null;
		this.collectionChangedCalled = false;
		this.testModel.addCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireCollectionChanged();
		this.verifyCollectionChangeEvent(null);
		assertTrue(this.collectionChangedCalled);

		this.collectionChangeEvent = null;
		this.collectionChangedCalled = false;
		this.testModel.removeCollectionChangeListener(COLLECTION_NAME, this);
		this.testModel.testFireCollectionChanged();
		assertNull(this.collectionChangeEvent);
		assertFalse(this.collectionChangedCalled);
	}

	public void testFireItemAddedList() {
		this.listChangeEvent = null;
		this.itemsAddedListCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireItemAddedList();
		this.verifyListChangeEvent(ADD_INDEX, ADDED_OBJECT_VALUE);
		assertTrue(this.itemsAddedListCalled);

		this.listChangeEvent = null;
		this.itemsAddedListCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireItemAddedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsAddedListCalled);

		this.listChangeEvent = null;
		this.itemsAddedListCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemAddedList();
		this.verifyListChangeEvent(ADD_INDEX, ADDED_OBJECT_VALUE);
		assertTrue(this.itemsAddedListCalled);

		this.listChangeEvent = null;
		this.itemsAddedListCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemAddedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsAddedListCalled);
	}

	public void testFireItemRemovedList() {
		this.listChangeEvent = null;
		this.itemsRemovedListCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireItemRemovedList();
		this.verifyListChangeEvent(REMOVE_INDEX, REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsRemovedListCalled);

		this.listChangeEvent = null;
		this.itemsRemovedListCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireItemRemovedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsRemovedListCalled);

		this.listChangeEvent = null;
		this.itemsRemovedListCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemRemovedList();
		this.verifyListChangeEvent(REMOVE_INDEX, REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsRemovedListCalled);

		this.listChangeEvent = null;
		this.itemsRemovedListCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemRemovedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsRemovedListCalled);
	}

	public void testFireItemReplacedList() {
		this.listChangeEvent = null;
		this.itemsReplacedListCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireItemReplacedList();
		this.verifyListChangeEvent(REPLACE_INDEX, ADDED_OBJECT_VALUE, REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsReplacedListCalled);

		this.listChangeEvent = null;
		this.itemsReplacedListCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireItemReplacedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsReplacedListCalled);

		this.listChangeEvent = null;
		this.itemsReplacedListCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemReplacedList();
		this.verifyListChangeEvent(REPLACE_INDEX, ADDED_OBJECT_VALUE, REMOVED_OBJECT_VALUE);
		assertTrue(this.itemsReplacedListCalled);

		this.listChangeEvent = null;
		this.itemsReplacedListCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemReplacedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsReplacedListCalled);
	}

	public void testFireItemMovedList() {
		this.listChangeEvent = null;
		this.itemsMovedListCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireItemMovedList();
		this.verifyListChangeEvent(TARGET_INDEX, SOURCE_INDEX);
		assertTrue(this.itemsMovedListCalled);

		this.listChangeEvent = null;
		this.itemsMovedListCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireItemMovedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsMovedListCalled);

		this.listChangeEvent = null;
		this.itemsMovedListCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemMovedList();
		this.verifyListChangeEvent(TARGET_INDEX, SOURCE_INDEX);
		assertTrue(this.itemsMovedListCalled);

		this.listChangeEvent = null;
		this.itemsMovedListCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireItemMovedList();
		assertNull(this.listChangeEvent);
		assertFalse(this.itemsMovedListCalled);
	}

	public void testFireListCleared() {
		this.listChangeEvent = null;
		this.listClearedCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireListCleared();
		this.verifyListChangeEvent(-1, null);
		assertTrue(this.listClearedCalled);

		this.listChangeEvent = null;
		this.listClearedCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireListCleared();
		assertNull(this.listChangeEvent);
		assertFalse(this.listClearedCalled);

		this.listChangeEvent = null;
		this.listClearedCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireListCleared();
		this.verifyListChangeEvent(-1, null);
		assertTrue(this.listClearedCalled);

		this.listChangeEvent = null;
		this.listClearedCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireListCleared();
		assertNull(this.listChangeEvent);
		assertFalse(this.listClearedCalled);
	}

	public void testFireListChanged() {
		this.listChangeEvent = null;
		this.listChangedCalled = false;
		this.testModel.addListChangeListener(this);
		this.testModel.testFireListChanged();
		this.verifyListChangeEvent(-1, null);
		assertTrue(this.listChangedCalled);

		this.listChangeEvent = null;
		this.listChangedCalled = false;
		this.testModel.removeListChangeListener(this);
		this.testModel.testFireListChanged();
		assertNull(this.listChangeEvent);
		assertFalse(this.listChangedCalled);

		this.listChangeEvent = null;
		this.listChangedCalled = false;
		this.testModel.addListChangeListener(LIST_NAME, this);
		this.testModel.testFireListChanged();
		this.verifyListChangeEvent(-1, null);
		assertTrue(this.listChangedCalled);

		this.listChangeEvent = null;
		this.listChangedCalled = false;
		this.testModel.removeListChangeListener(LIST_NAME, this);
		this.testModel.testFireListChanged();
		assertNull(this.listChangeEvent);
		assertFalse(this.listChangedCalled);
	}

	public void testFireNodeAddedObjectArrayPath() {
		this.treeChangeEvent = null;
		this.nodeAddedCalled = false;
		this.testModel.addTreeChangeListener(this);
		this.testModel.testFireNodeAddedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.nodeAddedCalled);

		this.treeChangeEvent = null;
		this.nodeAddedCalled = false;
		this.testModel.removeTreeChangeListener(this);
		this.testModel.testFireNodeAddedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.nodeAddedCalled);

		this.treeChangeEvent = null;
		this.nodeAddedCalled = false;
		this.testModel.addTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireNodeAddedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.nodeAddedCalled);

		this.treeChangeEvent = null;
		this.nodeAddedCalled = false;
		this.testModel.removeTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireNodeAddedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.nodeAddedCalled);
	}

	public void testFireNodeRemovedObjectArrayPath() {
		this.treeChangeEvent = null;
		this.nodeRemovedCalled = false;
		this.testModel.addTreeChangeListener(this);
		this.testModel.testFireNodeRemovedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.nodeRemovedCalled);

		this.treeChangeEvent = null;
		this.nodeRemovedCalled = false;
		this.testModel.removeTreeChangeListener(this);
		this.testModel.testFireNodeRemovedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.nodeRemovedCalled);

		this.treeChangeEvent = null;
		this.nodeRemovedCalled = false;
		this.testModel.addTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireNodeRemovedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.nodeRemovedCalled);

		this.treeChangeEvent = null;
		this.nodeRemovedCalled = false;
		this.testModel.removeTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireNodeRemovedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.nodeRemovedCalled);
	}

	public void testFireTreeCleared() {
		this.treeChangeEvent = null;
		this.treeClearedCalled = false;
		this.testModel.addTreeChangeListener(this);
		this.testModel.testFireTreeCleared();
		this.verifyTreeChangeEvent(EMPTY_PATH);
		assertTrue(this.treeClearedCalled);

		this.treeChangeEvent = null;
		this.treeClearedCalled = false;
		this.testModel.removeTreeChangeListener(this);
		this.testModel.testFireTreeCleared();
		assertNull(this.treeChangeEvent);
		assertFalse(this.treeClearedCalled);

		this.treeChangeEvent = null;
		this.treeClearedCalled = false;
		this.testModel.addTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireTreeCleared();
		this.verifyTreeChangeEvent(EMPTY_PATH);
		assertTrue(this.treeClearedCalled);

		this.treeChangeEvent = null;
		this.treeClearedCalled = false;
		this.testModel.removeTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireTreeCleared();
		assertNull(this.treeChangeEvent);
		assertFalse(this.treeClearedCalled);
	}

	public void testFireTreeChangedObjectArrayPath() {
		this.treeChangeEvent = null;
		this.treeChangedCalled = false;
		this.testModel.addTreeChangeListener(this);
		this.testModel.testFireTreeChangedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.treeChangedCalled);

		this.treeChangeEvent = null;
		this.treeChangedCalled = false;
		this.testModel.removeTreeChangeListener(this);
		this.testModel.testFireTreeChangedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.treeChangedCalled);

		this.treeChangeEvent = null;
		this.treeChangedCalled = false;
		this.testModel.addTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireTreeChangedObjectArrayPath();
		this.verifyTreeChangeEvent(OBJECT_ARRAY_PATH);
		assertTrue(this.treeChangedCalled);

		this.treeChangeEvent = null;
		this.treeChangedCalled = false;
		this.testModel.removeTreeChangeListener(TREE_NAME, this);
		this.testModel.testFireTreeChangedObjectArrayPath();
		assertNull(this.treeChangeEvent);
		assertFalse(this.treeChangedCalled);
	}

	public void testHasAnyChangeListeners() {
		assertFalse(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		this.testModel.addPropertyChangeListener(this);
		assertTrue(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		this.testModel.removePropertyChangeListener(this);

		assertFalse(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		this.testModel.addPropertyChangeListener(PROPERTY_NAME, this);
		assertTrue(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		this.testModel.removePropertyChangeListener(PROPERTY_NAME, this);

		assertFalse(this.testModel.hasAnyCollectionChangeListeners(COLLECTION_NAME));
		this.testModel.addCollectionChangeListener(this);
		assertTrue(this.testModel.hasAnyCollectionChangeListeners(COLLECTION_NAME));
		this.testModel.removeCollectionChangeListener(this);

		assertFalse(this.testModel.hasAnyCollectionChangeListeners(COLLECTION_NAME));
		this.testModel.addCollectionChangeListener(COLLECTION_NAME, this);
		assertTrue(this.testModel.hasAnyCollectionChangeListeners(COLLECTION_NAME));
		this.testModel.removeCollectionChangeListener(COLLECTION_NAME, this);

		assertFalse(this.testModel.hasAnyListChangeListeners(LIST_NAME));
		this.testModel.addListChangeListener(this);
		assertTrue(this.testModel.hasAnyListChangeListeners(LIST_NAME));
		this.testModel.removeListChangeListener(this);

		assertFalse(this.testModel.hasAnyListChangeListeners(LIST_NAME));
		this.testModel.addListChangeListener(LIST_NAME, this);
		assertTrue(this.testModel.hasAnyListChangeListeners(LIST_NAME));
		this.testModel.removeListChangeListener(LIST_NAME, this);

		assertFalse(this.testModel.hasAnyTreeChangeListeners(TREE_NAME));
		this.testModel.addTreeChangeListener(this);
		assertTrue(this.testModel.hasAnyTreeChangeListeners(TREE_NAME));
		this.testModel.removeTreeChangeListener(this);

		assertFalse(this.testModel.hasAnyTreeChangeListeners(TREE_NAME));
		this.testModel.addTreeChangeListener(TREE_NAME, this);
		assertTrue(this.testModel.hasAnyTreeChangeListeners(TREE_NAME));
		this.testModel.removeTreeChangeListener(TREE_NAME, this);
	}

	public void testAttributeValueHasChanged() {
		this.testModel.testAttributeValueHasChanged();
	}

	public void testClone() {
		assertFalse(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		this.testModel.addPropertyChangeListener(this);
		assertTrue(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));

		// verify that the clone does not have any listeners
		TestModel clone = this.testModel.clone();
		assertFalse(clone.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		clone.addPropertyChangeListener(this);
		assertTrue(clone.hasAnyPropertyChangeListeners(PROPERTY_NAME));
		// check original
		assertTrue(this.testModel.hasAnyPropertyChangeListeners(PROPERTY_NAME));

		// now test events fired by original
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		this.testModel.testFirePropertyChangeObjectObject();
		this.verifyPropertyChangeEvent(OLD_OBJECT_VALUE, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);

		// now test events fired by clone
		this.propertyChangeEvent = null;
		this.propertyChangeCalled = false;
		clone.testFirePropertyChangeObjectObject();
		this.verifyPropertyChangeEvent(clone, OLD_OBJECT_VALUE, NEW_OBJECT_VALUE);
		assertTrue(this.propertyChangeCalled);
	}

	public void testAddNullStateListener() {
		boolean exCaught = false;
		try {
			this.testModel.addStateChangeListener(null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullPropertyListener() {
		boolean exCaught = false;
		try {
			this.testModel.addPropertyChangeListener(null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullCollectionListener() {
		boolean exCaught = false;
		try {
			this.testModel.addCollectionChangeListener(null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullListListener() {
		boolean exCaught = false;
		try {
			this.testModel.addListChangeListener(null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullTreeListener() {
		boolean exCaught = false;
		try {
			this.testModel.addTreeChangeListener(null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullPropertyListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.addPropertyChangeListener("foo", null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullCollectionListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.addCollectionChangeListener("foo", null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullListListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.addListChangeListener("foo", null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testAddNullTreeListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.addTreeChangeListener("foo", null);
		} catch (NullPointerException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusStateListener() {
		boolean exCaught = false;
		try {
			this.testModel.removeStateChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeStateChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addStateChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeStateChangeListener(new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusPropertyListener() {
		boolean exCaught = false;
		try {
			this.testModel.removePropertyChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addCollectionChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removePropertyChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removePropertyChangeListener(new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusCollectionListener() {
		boolean exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addCollectionChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener(new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusListListener() {
		boolean exCaught = false;
		try {
			this.testModel.removeListChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeListChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addListChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeListChangeListener(new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusTreeListener() {
		boolean exCaught = false;
		try {
			this.testModel.removeTreeChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeTreeChangeListener(this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addTreeChangeListener(this);
		exCaught = false;
		try {
			this.testModel.removeTreeChangeListener(new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusPropertyListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.removePropertyChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addCollectionChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removePropertyChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removePropertyChangeListener("foo", new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusCollectionListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addCollectionChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeCollectionChangeListener("foo", new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusListListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.removeListChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeListChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addListChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeListChangeListener("foo", new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testRemoveBogusTreeListenerName() {
		boolean exCaught = false;
		try {
			this.testModel.removeTreeChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addPropertyChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeTreeChangeListener("foo", this);
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);

		this.testModel.addTreeChangeListener("foo", this);
		exCaught = false;
		try {
			this.testModel.removeTreeChangeListener("foo", new AbstractModelTests("dummy"));
		} catch (IllegalArgumentException ex) {
			exCaught = true;
		}
		assertTrue(exCaught);
	}

	public void testToString() {
		assertTrue(this.testModel.toString().indexOf(TEST_TO_STRING) != -1);
	}

// ********** internal methods **********

	private void verifyPropertyChangeEvent(Object oldValue, Object newValue) {
		this.verifyPropertyChangeEvent(this.testModel, oldValue, newValue);
	}

	private void verifyPropertyChangeEvent(Object source, Object oldValue, Object newValue) {
		assertNotNull(this.propertyChangeEvent);
		assertEquals(source, this.propertyChangeEvent.getSource());
		assertEquals(PROPERTY_NAME, this.propertyChangeEvent.propertyName());
		assertEquals(oldValue, this.propertyChangeEvent.oldValue());
		assertEquals(newValue, this.propertyChangeEvent.newValue());
	}

	private void verifyCollectionChangeEvent(Object item) {
		assertNotNull(this.collectionChangeEvent);
		assertEquals(this.testModel, this.collectionChangeEvent.getSource());
		assertEquals(COLLECTION_NAME, this.collectionChangeEvent.collectionName());
		if (item == null) {
			assertFalse(this.collectionChangeEvent.items().hasNext());
		} else {
			assertEquals(item, this.collectionChangeEvent.items().next());
		}
	}

	private void verifyListChangeEvent(int index, Object item) {
		this.verifyListChangeEvent(index, item, null);
	}

	private void verifyListChangeEvent(int targetIndex, int sourceIndex) {
		assertNotNull(this.listChangeEvent);
		assertEquals(this.testModel, this.listChangeEvent.getSource());
		assertEquals(LIST_NAME, this.listChangeEvent.listName());
		assertEquals(targetIndex, this.listChangeEvent.targetIndex());
		assertEquals(sourceIndex, this.listChangeEvent.sourceIndex());
	}

	private void verifyListChangeEvent(int index, Object item, Object replacedItem) {
		assertNotNull(this.listChangeEvent);
		assertEquals(this.testModel, this.listChangeEvent.getSource());
		assertEquals(LIST_NAME, this.listChangeEvent.listName());
		assertEquals(index, this.listChangeEvent.index());
		if (item == null) {
			assertFalse(this.listChangeEvent.items().hasNext());
		} else {
			assertEquals(item, this.listChangeEvent.items().next());
		}
		if (replacedItem == null) {
			assertFalse(this.listChangeEvent.replacedItems().hasNext());
		} else {
			assertEquals(replacedItem, this.listChangeEvent.replacedItems().next());
		}
	}

	private void verifyTreeChangeEvent(Object[] path) {
		assertNotNull(this.treeChangeEvent);
		assertEquals(this.testModel, this.treeChangeEvent.getSource());
		assertEquals(TREE_NAME, this.treeChangeEvent.treeName());
		assertTrue(Arrays.equals(path, this.treeChangeEvent.path()));
	}

// ********** listener implementations **********

	public void stateChanged(StateChangeEvent e) {
		this.stateChangedCalled = true;
		this.stateChangeEvent = e;
	}

	public void propertyChanged(PropertyChangeEvent e) {
		this.propertyChangeCalled = true;
		this.propertyChangeEvent = e;
	}

	public void itemsAdded(CollectionChangeEvent e) {
		this.itemsAddedCollectionCalled = true;
		this.collectionChangeEvent = e;
	}
	public void itemsRemoved(CollectionChangeEvent e) {
		this.itemsRemovedCollectionCalled = true;
		this.collectionChangeEvent = e;
	}
	public void collectionCleared(CollectionChangeEvent e) {
		this.collectionClearedCalled = true;
		this.collectionChangeEvent = e;
	}
	public void collectionChanged(CollectionChangeEvent e) {
		this.collectionChangedCalled = true;
		this.collectionChangeEvent = e;
	}

	public void itemsAdded(ListChangeEvent e) {
		this.itemsAddedListCalled = true;
		this.listChangeEvent = e;
	}
	public void itemsRemoved(ListChangeEvent e) {
		this.itemsRemovedListCalled = true;
		this.listChangeEvent = e;
	}
	public void itemsReplaced(ListChangeEvent e) {
		this.itemsReplacedListCalled = true;
		this.listChangeEvent = e;
	}
	public void itemsMoved(ListChangeEvent e) {
		this.itemsMovedListCalled = true;
		this.listChangeEvent = e;
	}
	public void listCleared(ListChangeEvent e) {
		this.listClearedCalled = true;
		this.listChangeEvent = e;
	}
	public void listChanged(ListChangeEvent e) {
		this.listChangedCalled = true;
		this.listChangeEvent = e;
	}

	public void nodeAdded(TreeChangeEvent e) {
		this.nodeAddedCalled = true;
		this.treeChangeEvent = e;
	}
	public void nodeRemoved(TreeChangeEvent e) {
		this.nodeRemovedCalled = true;
		this.treeChangeEvent = e;
	}
	public void treeCleared(TreeChangeEvent e) {
		this.treeClearedCalled = true;
		this.treeChangeEvent = e;
	}
	public void treeChanged(TreeChangeEvent e) {
		this.treeChangedCalled = true;
		this.treeChangeEvent = e;
	}

	// ********** inner class **********
	
	private static class TestModel extends AbstractModel implements Cloneable {
		TestModel() {
			super();
		}
	
		public void testFireStateChange() {
			this.fireStateChanged();
		}
	
		public void testFirePropertyChangeObjectObject() {
			this.firePropertyChanged(PROPERTY_NAME, OLD_OBJECT_VALUE, NEW_OBJECT_VALUE);
		}
	
		public void testFirePropertyChangeObject() {
			this.firePropertyChanged(PROPERTY_NAME, NEW_OBJECT_VALUE);
		}
	
		public void testFirePropertyChangeIntInt() {
			this.firePropertyChanged(PROPERTY_NAME, OLD_INT_VALUE.intValue(), NEW_INT_VALUE.intValue());
		}
	
		public void testFirePropertyChangeBooleanBoolean() {
			this.firePropertyChanged(PROPERTY_NAME, OLD_BOOLEAN_VALUE.booleanValue(), NEW_BOOLEAN_VALUE.booleanValue());
		}
	
		public void testFireItemAddedCollection() {
			this.fireItemAdded(COLLECTION_NAME, ADDED_OBJECT_VALUE);
		}
	
		public void testFireItemRemovedCollection() {
			this.fireItemRemoved(COLLECTION_NAME, REMOVED_OBJECT_VALUE);
		}
	
		public void testFireCollectionCleared() {
			this.fireCollectionCleared(COLLECTION_NAME);
		}
	
		public void testFireCollectionChanged() {
			this.fireCollectionChanged(COLLECTION_NAME);
		}
	
		public void testFireItemAddedList() {
			this.fireItemAdded(LIST_NAME, ADD_INDEX, ADDED_OBJECT_VALUE);
		}
	
		public void testFireItemRemovedList() {
			this.fireItemRemoved(LIST_NAME, REMOVE_INDEX, REMOVED_OBJECT_VALUE);
		}
	
		public void testFireItemReplacedList() {
			this.fireItemReplaced(LIST_NAME, REPLACE_INDEX, ADDED_OBJECT_VALUE, REMOVED_OBJECT_VALUE);
		}
	
		public void testFireItemMovedList() {
			this.fireItemMoved(LIST_NAME, TARGET_INDEX, SOURCE_INDEX);
		}
	
		public void testFireListCleared() {
			this.fireListCleared(LIST_NAME);
		}
	
		public void testFireListChanged() {
			this.fireListChanged(LIST_NAME);
		}
	
		public void testFireNodeAddedObjectArrayPath() {
			this.fireNodeAdded(TREE_NAME, OBJECT_ARRAY_PATH);
		}
	
		public void testFireNodeRemovedObjectArrayPath() {
			this.fireNodeRemoved(TREE_NAME, OBJECT_ARRAY_PATH);
		}
	
		public void testFireTreeCleared() {
			this.fireTreeCleared(TREE_NAME);
		}
	
		public void testFireTreeChangedObjectArrayPath() {
			this.fireTreeChanged(TREE_NAME, OBJECT_ARRAY_PATH);
		}
	
		public void testAttributeValueHasChanged() {
			assertTrue(this.attributeValueHasChanged(null, new Object()));
			assertTrue(this.attributeValueHasChanged(new Object(), null));
			assertTrue(this.attributeValueHasChanged(new Object(), new Object()));
	
			Object same = new Object();
			assertFalse(this.attributeValueHasChanged(same, same));
			assertFalse(this.attributeValueHasChanged(null, null));
		}
	
		@Override
		public TestModel clone() {
			try {
				return (TestModel) super.clone();
			} catch (CloneNotSupportedException ex) {
				throw new InternalError();
			}
		}
	
		@Override
		public void toString(StringBuffer sb) {
			sb.append(TEST_TO_STRING);
		}
	
	}


	// ********** serialization test **********	

	public void testSerialization() throws IOException, ClassNotFoundException {
		LocalModel model1 = new LocalModel();
		Foo foo1 = new Foo();
		Bar bar1 = new Bar();
		Joo joo1 = new Joo();
		Jar jar1 = new Jar();
		model1.addStateChangeListener(foo1);
		model1.addStateChangeListener(bar1);
		model1.addListChangeListener(joo1);
		model1.addListChangeListener(jar1);

		ChangeListener[] listeners1 = this.listeners(model1, StateChangeListener.class);
		assertEquals(2, listeners1.length);
		// the order of these could change...
		assertEquals(Foo.class, listeners1[0].getClass());
		assertEquals(Bar.class, listeners1[1].getClass());

		listeners1 = this.listeners(model1, ListChangeListener.class);
		assertEquals(2, listeners1.length);
		// the order of these could change...
		assertEquals(Joo.class, listeners1[0].getClass());
		assertEquals(Jar.class, listeners1[1].getClass());

		LocalModel model2 = TestTools.serialize(model1);

		ChangeListener[] listeners2 = this.listeners(model2, StateChangeListener.class);
		assertEquals(1, listeners2.length);
		assertEquals(Foo.class, listeners2[0].getClass());

		listeners2 = this.listeners(model2, ListChangeListener.class);
		assertEquals(1, listeners2.length);
		assertEquals(Joo.class, listeners2[0].getClass());
	}

	private ChangeListener[] listeners(LocalModel model, Class<? extends ChangeListener> listenerClass) {
		ChangeSupport changeSupport = (ChangeSupport) ClassTools.getFieldValue(model, "changeSupport");
		return (ChangeListener[]) ClassTools.executeMethod(changeSupport, "listeners", Class.class, listenerClass);
	}

	private static class LocalModel extends AbstractModel implements Serializable {
		LocalModel() {
			super();
		}
	}

	private static class Foo implements Serializable, StateChangeListener {
		Foo() {
			super();
		}
		public void stateChanged(StateChangeEvent event) {
			// do nothing
		}
	}

	private static class Bar implements StateChangeListener {
		Bar() {
			super();
		}
		public void stateChanged(StateChangeEvent event) {
			// do nothing
		}
	}

	private static class Joo extends ListChangeAdapter implements Serializable {
//		private static final ObjectStreamField[] serialPersistentFields = {new ObjectStreamField("changeSupport", ChangeSupport.class)};
		Joo() {
			super();
		}
	}

	private static class Jar extends ListChangeAdapter {
		Jar() {
			super();
		}
	}


	// ********** bug(?) test **********	

	private static final String ISE_MESSAGE = "this object is no longer listening to localA";

	/**
	 * Test the following situation:
	 * 	- both B and C are listening to A
	 * 	- C is also listening to B
	 * 	- when B receives an event from A, it will fire an event to C
	 * 	- when C receives an event from B, it will STOP listening to A
	 * 	- the event from B to C may be preceded or followed (depending on
	 * 		the hash positions of listeners) by an event from A to C:
	 * 		- if the A to C event comes first, no problem
	 * 		- but if the A to B event comes first, the A to C event should NOT happen
	 */
	public void testIndirectRemoveStateListener() {
		this.verifyIndirectRemoveListener(
			new NotifyCommand() {
				public void notifyListeners(LocalA localA) {
					localA.notifyStateListeners();
				}
			}
		);
	}

	public void testIndirectRemovePropertyListener() {
		this.verifyIndirectRemoveListener(
			new NotifyCommand() {
				public void notifyListeners(LocalA localA) {
					localA.notifyPropertyListeners();
				}
			}
		);
	}

	public void testIndirectRemoveCollectionListener() {
		this.verifyIndirectRemoveListener(
			new NotifyCommand() {
				public void notifyListeners(LocalA localA) {
					localA.notifyCollectionListeners();
				}
			}
		);
	}

	public void testIndirectRemoveListListener() {
		this.verifyIndirectRemoveListener(
			new NotifyCommand() {
				public void notifyListeners(LocalA localA) {
					localA.notifyListListeners();
				}
			}
		);
	}

	public void testIndirectRemoveTreeListener() {
		this.verifyIndirectRemoveListener(
			new NotifyCommand() {
				public void notifyListeners(LocalA localA) {
					localA.notifyTreeListeners();
				}
			}
		);
	}

	public void verifyIndirectRemoveListener(NotifyCommand command) {
		LocalA localA = new LocalA();
		LocalB localB = new LocalB(localA);

		// build a bunch of LocalCs so at least one of them is notified AFTER the LocalB;
		// using 1000 seemed to fail very consistently before ChangeSupport was fixed
		LocalC[] localCs = new LocalC[1000];
		for (int i = localCs.length; i-- > 0; ) {
			localCs[i] = new LocalC(localA, localB);
		}

		boolean exCaught = false;
		try {
			command.notifyListeners(localA);
		} catch (IllegalStateException ex) {
			if (ex.getMessage() == ISE_MESSAGE) {
				exCaught = true;
			} else {
				throw ex;
			}
		}
		assertFalse(exCaught);

		for (int i = localCs.length; i-- > 0; ) {
			assertFalse(localCs[i].isListeningToLocalA());
		}
	}

	private interface NotifyCommand {
		void notifyListeners(LocalA localA);
	}

	/**
	 * This object simply fires a state change event. Both LocalB and LocalC
	 * will be listeners.
	 */
	private static class LocalA extends AbstractModel {
		LocalA() {
			super();
		}
		void notifyStateListeners() {
			this.fireStateChanged();
		}
		void notifyPropertyListeners() {
			this.firePropertyChanged("foo", 1, 2);
		}
		void notifyCollectionListeners() {
			this.fireCollectionChanged("foo");
		}
		void notifyListListeners() {
			this.fireListChanged("foo");
		}
		void notifyTreeListeners() {
			this.fireTreeChanged("foo");
		}
	}

	/**
	 * This object will fire state change events whenever it receives
	 * a state change event from localA.
	 */
	private static class LocalB
		extends AbstractModel
		implements StateChangeListener, PropertyChangeListener, CollectionChangeListener, ListChangeListener, TreeChangeListener
	{
		LocalB(LocalA localA) {
			super();
			localA.addStateChangeListener(this);
			localA.addPropertyChangeListener(this);
			localA.addCollectionChangeListener(this);
			localA.addListChangeListener(this);
			localA.addTreeChangeListener(this);
		}

		public void stateChanged(StateChangeEvent e) {
			this.fireStateChanged();
		}

		public void propertyChanged(PropertyChangeEvent evt) {
			this.firePropertyChanged("bar", 1, 2);
		}

		public void collectionChanged(CollectionChangeEvent e) {
			this.fireCollectionChanged("bar");
		}
		public void collectionCleared(CollectionChangeEvent e) {/*ignore*/}
		public void itemsAdded(CollectionChangeEvent e) {/*ignore*/}
		public void itemsRemoved(CollectionChangeEvent e) {/*ignore*/}

		public void listChanged(ListChangeEvent e) {
			this.fireListChanged("bar");
		}
		public void listCleared(ListChangeEvent e) {/*ignore*/}
		public void itemsAdded(ListChangeEvent e) {/*ignore*/}
		public void itemsRemoved(ListChangeEvent e) {/*ignore*/}
		public void itemsReplaced(ListChangeEvent e) {/*ignore*/}
		public void itemsMoved(ListChangeEvent e) {/*ignore*/}

		public void treeChanged(TreeChangeEvent e) {
			this.fireTreeChanged("bar");
		}
		public void treeCleared(TreeChangeEvent e) {/*ignore*/}
		public void nodeAdded(TreeChangeEvent e) {/*ignore*/}
		public void nodeRemoved(TreeChangeEvent e) {/*ignore*/}

	}

	/**
	 * This object will listen to two other objects, localA and localB.
	 * If this object receives notification from localB, it will stop listening to
	 * localA. If this object receives notification from localA, it will check to
	 * see whether it still listening to localA. If this object is no longer
	 * listening to localA, it will complain about receiving the event and
	 * throw an exception.
	 */
	private static class LocalC
		extends AbstractModel
		implements StateChangeListener, PropertyChangeListener, CollectionChangeListener, ListChangeListener, TreeChangeListener
	{
		private LocalA localA;
		private LocalB localB;
		private boolean listeningToLocalA;

		LocalC(LocalA localA, LocalB localB) {
			super();
			this.localA = localA;
			this.localB = localB;

			localA.addStateChangeListener(this);
			localA.addPropertyChangeListener(this);
			localA.addCollectionChangeListener(this);
			localA.addListChangeListener(this);
			localA.addTreeChangeListener(this);
			this.listeningToLocalA = true;

			localB.addStateChangeListener(this);
			localB.addPropertyChangeListener(this);
			localB.addCollectionChangeListener(this);
			localB.addListChangeListener(this);
			localB.addTreeChangeListener(this);
		}
		boolean isListeningToLocalA() {
			return this.listeningToLocalA;
		}

		public void stateChanged(StateChangeEvent e) {
			Object source = e.getSource();
			if (source == this.localA) {
				if ( ! this.listeningToLocalA) {
					throw new IllegalStateException(ISE_MESSAGE);
				}
			} else if (source == this.localB) {
				this.localA.removeStateChangeListener(this);
				this.listeningToLocalA = false;
			} else {
				throw new IllegalStateException("bogus event source: " + source);
			}
		}

		public void propertyChanged(PropertyChangeEvent e) {
			Object source = e.getSource();
			if (source == this.localA) {
				if ( ! this.listeningToLocalA) {
					throw new IllegalStateException(ISE_MESSAGE);
				}
			} else if (source == this.localB) {
				this.localA.removePropertyChangeListener(this);
				this.listeningToLocalA = false;
			} else {
				throw new IllegalStateException("bogus event source: " + source);
			}
		}

		public void collectionChanged(CollectionChangeEvent e) {
			Object source = e.getSource();
			if (source == this.localA) {
				if ( ! this.listeningToLocalA) {
					throw new IllegalStateException(ISE_MESSAGE);
				}
			} else if (source == this.localB) {
				this.localA.removeCollectionChangeListener(this);
				this.listeningToLocalA = false;
			} else {
				throw new IllegalStateException("bogus event source: " + source);
			}
		}
		public void collectionCleared(CollectionChangeEvent e) {/*ignore*/}
		public void itemsAdded(CollectionChangeEvent e) {/*ignore*/}
		public void itemsRemoved(CollectionChangeEvent e) {/*ignore*/}

		public void listChanged(ListChangeEvent e) {
			Object source = e.getSource();
			if (source == this.localA) {
				if ( ! this.listeningToLocalA) {
					throw new IllegalStateException(ISE_MESSAGE);
				}
			} else if (source == this.localB) {
				this.localA.removeListChangeListener(this);
				this.listeningToLocalA = false;
			} else {
				throw new IllegalStateException("bogus event source: " + source);
			}
		}
		public void listCleared(ListChangeEvent e) {/*ignore*/}
		public void itemsAdded(ListChangeEvent e) {/*ignore*/}
		public void itemsRemoved(ListChangeEvent e) {/*ignore*/}
		public void itemsReplaced(ListChangeEvent e) {/*ignore*/}
		public void itemsMoved(ListChangeEvent e) {/*ignore*/}

		public void treeChanged(TreeChangeEvent e) {
			Object source = e.getSource();
			if (source == this.localA) {
				if ( ! this.listeningToLocalA) {
					throw new IllegalStateException(ISE_MESSAGE);
				}
			} else if (source == this.localB) {
				this.localA.removeTreeChangeListener(this);
				this.listeningToLocalA = false;
			} else {
				throw new IllegalStateException("bogus event source: " + source);
			}
		}
		public void treeCleared(TreeChangeEvent e) {/*ignore*/}
		public void nodeAdded(TreeChangeEvent e) {/*ignore*/}
		public void nodeRemoved(TreeChangeEvent e) {/*ignore*/}

	}

}
