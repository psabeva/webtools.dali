/*******************************************************************************
 * Copyright (c) 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt2_0.core.internal.context.orm;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.JpaFactory;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.core.MappingKeys;
import org.eclipse.jpt.core.context.orm.OrmPersistentType;
import org.eclipse.jpt.core.context.orm.OrmTypeMapping;
import org.eclipse.jpt.core.context.orm.OrmTypeMappingProvider;
import org.eclipse.jpt.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt2_0.core.internal.platform.Generic2_0JpaFactory;
import org.eclipse.jpt2_0.core.resource.orm.Orm2_0Factory;
import org.eclipse.jpt2_0.core.resource.orm.XmlEmbeddable;

public class Generic2_0OrmEmbeddableMappingProvider
	implements OrmTypeMappingProvider
{
	// singleton
	private static final OrmTypeMappingProvider INSTANCE = new Generic2_0OrmEmbeddableMappingProvider();

	/**
	 * Return the singleton.
	 */
	public static OrmTypeMappingProvider instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private Generic2_0OrmEmbeddableMappingProvider() {
		super();
	}

	public IContentType getContentType() {
		return JptCorePlugin.ORM2_0_XML_CONTENT_TYPE;
	}

	public String getKey() {
		return MappingKeys.EMBEDDABLE_TYPE_MAPPING_KEY;
	}
	
	public XmlTypeMapping buildResourceMapping() {
		return Orm2_0Factory.eINSTANCE.createXmlEmbeddable();
	}

	public OrmTypeMapping buildMapping(OrmPersistentType parent, XmlTypeMapping resourceMapping, JpaFactory factory) {
		return ((Generic2_0JpaFactory) factory).build2_0OrmEmbeddable(parent, (XmlEmbeddable) resourceMapping);
	}

}
