/*******************************************************************************
 * Copyright (c) 2008, 2011 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.db.internal;

import org.eclipse.jpt.jpa.db.DatabaseObject;

/**
 * Internal interface: DTP database object.
 */
interface DTPDatabaseObject
	extends DatabaseObject
{
	/**
	 * covariant override
	 */
	DTPConnectionProfileWrapper getConnectionProfile();

	/**
	 * covariant override
	 */
	public DTPDatabaseWrapper getDatabase();
}
