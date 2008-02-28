/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context;

import org.eclipse.jpt.db.internal.Schema;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface Table extends JpaContextNode
{
	String getName();
	
	String getDefaultName();
		String DEFAULT_NAME_PROPERTY = "defaultNameProperty";

	String getSpecifiedName();
	void setSpecifiedName(String value);
		String SPECIFIED_NAME_PROPERTY = "specifiedNameProperty";

	String getCatalog();

	String getDefaultCatalog();
		String DEFAULT_CATALOG_PROPERTY = "defaultCatalogProperty";

	String getSpecifiedCatalog();
	void setSpecifiedCatalog(String value);
		String SPECIFIED_CATALOG_PROPERTY = "specifiedCatalogProperty";


	String getSchema();

	String getDefaultSchema();
		String DEFAULT_SCHEMA_PROPERTY = "defaultSchemaProperty";

	String getSpecifiedSchema();
	void setSpecifiedSchema(String value);
		String SPECIFIED_SCHEMA_PROPERTY = "specifiedSchemaProperty";


//	EList<IUniqueConstraint> getUniqueConstraints();
//	IUniqueConstraint createUniqueConstraint(int index);

	org.eclipse.jpt.db.internal.Table dbTable();

	Schema dbSchema();
	
	/**
	 * Return true if this table is connected to a datasource
	 */
	boolean isConnected();

	/** 
	 * Return true if this table's schema can be resolved to a schema on the active connection
	 */
	boolean hasResolvedSchema();

	/** 
	 * Return true if this can be resolved to a table on the active connection
	 */
	boolean isResolved();


//
//	class UniqueConstraintOwner implements IUniqueConstraint.Owner
//	{
//		private final ITable table;
//
//		public UniqueConstraintOwner(ITable table) {
//			super();
//			this.table = table;
//		}
//
//		public Iterator<String> candidateUniqueConstraintColumnNames() {
//			return this.table.dbTable().columnNames();
//		}
//	}
}
