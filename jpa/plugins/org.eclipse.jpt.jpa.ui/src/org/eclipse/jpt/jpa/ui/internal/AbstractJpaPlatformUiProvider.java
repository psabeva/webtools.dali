/*******************************************************************************
 * Copyright (c) 2007, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal;

import java.util.ArrayList;
import java.util.List;
import org.eclipse.jpt.jpa.ui.JpaPlatformUiProvider;
import org.eclipse.jpt.jpa.ui.ResourceUiDefinition;
import org.eclipse.jpt.jpa.ui.details.JpaDetailsProvider;

/**
 * All the state in the JPA platform UI provider should be "static"
 * (i.e. unchanging once it is initialized).
 */
public abstract class AbstractJpaPlatformUiProvider
	implements JpaPlatformUiProvider
{
	private ArrayList<JpaDetailsProvider> detailsProviders;

	private ArrayList<ResourceUiDefinition> resourceUiDefinitions;


	/**
	 * zero-argument constructor
	 */
	public AbstractJpaPlatformUiProvider() {
		super();
	}


	// ********** details providers **********

	public synchronized Iterable<JpaDetailsProvider> getDetailsProviders() {
		if (this.detailsProviders == null) {
			this.detailsProviders = this.buildDetailsProviders();
		}
		return this.detailsProviders;
	}

	protected ArrayList<JpaDetailsProvider> buildDetailsProviders() {
		ArrayList<JpaDetailsProvider> providers = new ArrayList<JpaDetailsProvider>();
		this.addDetailsProvidersTo(providers);
		return providers;
	}

	/**
	 * Implement this to specify JPA details providers.
	 */
	protected abstract void addDetailsProvidersTo(List<JpaDetailsProvider> providers);



	// ********** structure providers **********

	public Iterable<ResourceUiDefinition> getResourceUiDefinitions() {
		if (this.resourceUiDefinitions == null) {
			this.resourceUiDefinitions = this.buildResourceUiDefinitions();
		}
		return this.resourceUiDefinitions;
	}

	protected ArrayList<ResourceUiDefinition> buildResourceUiDefinitions() {
		ArrayList<ResourceUiDefinition> definitions = new ArrayList<ResourceUiDefinition>();
		this.addResourceUiDefinitionsTo(definitions);
		return definitions;
	}

	/**
	 * Implement this to specify JPA resource UI definitions.
	 */
	protected abstract void addResourceUiDefinitionsTo(List<ResourceUiDefinition> definitions);

	@Override
	public String toString() {
		return this.getClass().getSimpleName();
	}
}
