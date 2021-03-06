/*******************************************************************************
 * Copyright (c) 2006, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.ui.jface;

/**
 * This provider supplies the factories used by a tree view to build
 * the content and labels its tree.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface ItemTreeStateProviderFactoryProvider {
	/**
	 * Return a factory to be used to create {@link ItemTreeContentProvider}s
	 * for a tree view.
	 */
	ItemTreeContentProvider.Factory getItemContentProviderFactory();
	
	/**
	 * Return a factory to be used to create {@link ItemExtendedLabelProvider}s
	 * for a tree view.
	 */
	ItemExtendedLabelProvider.Factory getItemLabelProviderFactory();
}
