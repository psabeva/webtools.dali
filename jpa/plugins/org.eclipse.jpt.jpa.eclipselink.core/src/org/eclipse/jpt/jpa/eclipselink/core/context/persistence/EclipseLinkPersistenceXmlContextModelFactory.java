/*******************************************************************************
 * Copyright (c) 2010, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.eclipselink.core.context.persistence;

import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceXmlContextModelFactory;

public interface EclipseLinkPersistenceXmlContextModelFactory
	extends PersistenceXmlContextModelFactory
{
	EclipseLinkLogging buildLogging(PersistenceUnit parent);

	EclipseLinkConnection buildConnection(PersistenceUnit parent);

	EclipseLinkOptions buildOptions(PersistenceUnit parent);
}
