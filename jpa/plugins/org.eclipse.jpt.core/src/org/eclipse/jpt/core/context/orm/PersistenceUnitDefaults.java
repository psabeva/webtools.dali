/*******************************************************************************
 * Copyright (c) 2006, 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import org.eclipse.jpt.core.context.AccessType;
import org.eclipse.jpt.core.context.XmlContextNode;
import org.eclipse.jpt.core.resource.orm.XmlEntityMappings;
import org.eclipse.jpt.db.Catalog;
import org.eclipse.jpt.db.Schema;
import org.eclipse.jpt.db.SchemaContainer;

/**
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface PersistenceUnitDefaults
	extends XmlContextNode
{

	AccessType getAccess();
	void setAccess(AccessType value);
		String ACCESS_PROPERTY = "access"; //$NON-NLS-1$

	SchemaContainer getDbSchemaContainer();

	/**
	 * Return the specified catalog if present, otherwise return the default catalog.
	 */
	String getCatalog();
	String getSpecifiedCatalog();
	void setSpecifiedCatalog(String newSpecifiedCatalog);
		String SPECIFIED_CATALOG_PROPERTY = "specifiedCatalog"; //$NON-NLS-1$
	String getDefaultCatalog();
		String DEFAULT_CATALOG_PROPERTY = "defaultCatalog"; //$NON-NLS-1$
	Catalog getDbCatalog();

	/**
	 * Return the specified schema if present, otherwise return the default schema.
	 */
	String getSchema();
	String getSpecifiedSchema();
	void setSpecifiedSchema(String newSpecifiedSchema);
		String SPECIFIED_SCHEMA_PROPERTY = "specifiedSchema"; //$NON-NLS-1$
	String getDefaultSchema();
		String DEFAULT_SCHEMA_PROPERTY = "defaultSchema"; //$NON-NLS-1$
	Schema getDbSchema();

	boolean isCascadePersist();
	void setCascadePersist(boolean value);
		String CASCADE_PERSIST_PROPERTY = "cascadePersist"; //$NON-NLS-1$
	
	/**
	 * Update the PersistenceUnitDefaults context model object to match the XmlEntityMappings 
	 * resource model object. see {@link org.eclipse.jpt.core.JpaProject#update()}
	 */
	void update(XmlEntityMappings entityMappings);
	
	/**
	 * Return whether the underlying resource exists for the PersistenceUnitDefaults object
	 * If there is a node in the orm.xml file for persistence-unit-default, this returns true, otherwise false.
	 * @return
	 */
	boolean resourceExists();

}
