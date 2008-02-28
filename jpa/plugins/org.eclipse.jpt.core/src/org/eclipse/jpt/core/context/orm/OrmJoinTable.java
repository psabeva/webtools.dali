/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core.context.orm;

import java.util.ListIterator;
import org.eclipse.jpt.core.context.JoinColumn;
import org.eclipse.jpt.core.context.JoinTable;
import org.eclipse.jpt.core.resource.orm.XmlRelationshipMapping;

/**
 * 
 * 
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface OrmJoinTable extends JoinTable, OrmJpaContextNode
{
	OrmRelationshipMapping parent();
	
	@SuppressWarnings("unchecked")
	ListIterator<OrmJoinColumn> joinColumns();

	ListIterator<OrmJoinColumn> defaultJoinColumns();

	@SuppressWarnings("unchecked")
	ListIterator<OrmJoinColumn> specifiedJoinColumns();

	OrmJoinColumn addSpecifiedJoinColumn(int index);

	void removeSpecifiedInverseJoinColumn(JoinColumn joinColumn);

	@SuppressWarnings("unchecked")
	ListIterator<OrmJoinColumn> inverseJoinColumns();

	ListIterator<OrmJoinColumn> defaultInverseJoinColumns();

	@SuppressWarnings("unchecked")
	ListIterator<OrmJoinColumn> specifiedInverseJoinColumns();

	OrmJoinColumn addSpecifiedInverseJoinColumn(int index);

	void initialize(XmlRelationshipMapping relationshipMapping);

	void update(XmlRelationshipMapping relationshipMapping);
	
	boolean isSpecified();
	
	void initializeFrom(JoinTable oldJoinTable);

}