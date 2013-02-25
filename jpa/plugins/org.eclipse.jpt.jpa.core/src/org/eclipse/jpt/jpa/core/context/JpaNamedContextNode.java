/*******************************************************************************
 * Copyright (c) 2010, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.context;

import java.io.Serializable;

import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * Named context node. Sorta. :-)
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 */
public interface JpaNamedContextNode
	extends JpaContextModel
{
	String getName();
		String NAME_PROPERTY = "name"; //$NON-NLS-1$
	void setName(String name);

	Class<? extends JpaNamedContextNode> getType();

	/**
	 * Return whether the specified node is <em>not</em> this node and it has
	 * the same state. Typically the specified node would be the same type as
	 * this node.
	 * @see #getType()
	 */
	boolean isEquivalentTo(JpaNamedContextNode node);

	final class NameTransformer<N extends JpaNamedContextNode>
		implements Transformer<N, String>, Serializable
	{
		@SuppressWarnings("rawtypes")
		public static final NameTransformer INSTANCE = new NameTransformer();
		@SuppressWarnings("unchecked")
		public static <T extends JpaNamedContextNode> Transformer<T, String> instance() {
			return INSTANCE;
		}
		// ensure single instance
		private NameTransformer() {
			super();
		}
		public String transform(N node) {
			return node.getName();
		}
		@Override
		public String toString() {
			return ObjectTools.singletonToString(this);
		}
		private static final long serialVersionUID = 1L;
		private Object readResolve() {
			// replace this object with the singleton
			return INSTANCE;
		}
	}
}
