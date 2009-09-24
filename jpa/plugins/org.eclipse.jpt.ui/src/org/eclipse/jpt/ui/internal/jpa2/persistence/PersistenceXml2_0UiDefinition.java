/*******************************************************************************
 *  Copyright (c) 2009  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.ui.internal.jpa2.persistence;

import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.JptCorePlugin;
import org.eclipse.jpt.ui.ResourceUiDefinition;
import org.eclipse.jpt.ui.internal.jpa2.structure.Persistence2_0ResourceModelStructureProvider;
import org.eclipse.jpt.ui.internal.persistence.details.AbstractPersistenceXmlResourceUiDefinition;
import org.eclipse.jpt.ui.internal.persistence.details.PersistenceXmlUiFactory;
import org.eclipse.jpt.ui.structure.JpaStructureProvider;

public class PersistenceXml2_0UiDefinition extends AbstractPersistenceXmlResourceUiDefinition
{
	// singleton
	private static final ResourceUiDefinition INSTANCE = new PersistenceXml2_0UiDefinition();

	/**
	 * Return the singleton.
	 */
	public static ResourceUiDefinition instance() {
		return INSTANCE;
	}

	/**
	 * Ensure single instance.
	 */
	private PersistenceXml2_0UiDefinition() {
		super();
	}
	
	@Override
	protected PersistenceXmlUiFactory buildPersistenceXmlUiFactory() {
		return new Generic2_0PersistenceXmlUiFactory();
	}
	
	public IContentType getContentType() {
		return JptCorePlugin.PERSISTENCE2_0_XML_CONTENT_TYPE;
	}

	public JpaStructureProvider getStructureProvider() {
		return Persistence2_0ResourceModelStructureProvider.instance();
	}
}
