/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kiril Mitov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.facade;

import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.IDisplayFacade;
import org.eclipse.swt.widgets.Display;

public class DisplayFacade implements IDisplayFacade {

	public void asyncExec(Runnable runnable) {
		Display.getDefault().asyncExec(runnable);
	}

}
