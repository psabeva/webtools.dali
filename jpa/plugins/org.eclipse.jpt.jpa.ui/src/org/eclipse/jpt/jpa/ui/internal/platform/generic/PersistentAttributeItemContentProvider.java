/*******************************************************************************
 *  Copyright (c) 2008  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.platform.generic;

import org.eclipse.jpt.common.ui.internal.jface.AbstractTreeItemContentProvider;
import org.eclipse.jpt.common.ui.internal.jface.DelegatingTreeContentAndLabelProvider;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;

@SuppressWarnings("unchecked")
public class PersistentAttributeItemContentProvider extends AbstractTreeItemContentProvider
{
	public PersistentAttributeItemContentProvider(
			PersistentAttribute persistentAttribute, DelegatingTreeContentAndLabelProvider contentProvider) {
		super(persistentAttribute, contentProvider);
	}
	
	@Override
	public PersistentAttribute getModel() {
		return (PersistentAttribute) super.getModel();
	}
	
	@Override
	public Object getParent() {
		return getModel().getParent();
	}
	
	@Override
	public boolean hasChildren() {
		return false;
	}
}