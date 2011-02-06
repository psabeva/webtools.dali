/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.selection;

import org.eclipse.ui.IWorkbenchPart;

public interface JpaSelectionManager 
{	
	/**
	 * Return the current selection.  
	 * This will never be null, but it may be empty.
	 */
	public JpaSelection getCurrentSelection();
	
	/**
	 * Not to be called lightly, this will affect the selection for all interested
	 * objects in a window.
	 * @param newSelection  The new selection for the current window.
	 * @param source  The selection participant (if any) that is causing the 
	 * selection.  May be null.
	 */
	public void select(JpaSelection newSelection, JpaSelectionParticipant source);
	
	/**
	 * Not to be called lightly, this will affect the selection for all interested
	 * objects in a window.
	 * @param oldSelection  The oldSelection will be deselected, iff it matches 
	 * the current selection.  If so, the new selection will be an empty JpaSelection.
	 * @param source  The selection participant (if any) that is causing the 
	 * selection.  May be null. 
	 */
	public void deselect(JpaSelection oldSelection, JpaSelectionParticipant source);
	
	/**
	 * This may be used to register a part with the selection manager if the part
	 * is known to need access to the selection manager before it is ever activated
	 * or in the case it may be activated prior to the selection manager being 
	 * created.
	 * 
	 * It should not be necessary to deregister a part, as that happens when the 
	 * part is closed.
	 */
	public void register(IWorkbenchPart part);
	
	/**
	 * Returns true if the part is currently registered to respond to selections
	 * from this selection manager
	 */
	public boolean isRegistered(IWorkbenchPart part);
}
