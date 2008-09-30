/*******************************************************************************
 * Copyright (c) 2008 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.eclipselink.core.internal.context.java;

import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.internal.context.java.GenericJavaOneToOneMapping;
import org.eclipse.jpt.core.resource.java.JavaResourcePersistentAttribute;
import org.eclipse.jpt.eclipselink.core.EclipseLinkJpaFactory;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkOneToOneMapping;
import org.eclipse.jpt.eclipselink.core.context.PrivateOwnable;
import org.eclipse.jpt.eclipselink.core.context.java.JavaJoinFetchable;
import org.eclipse.jpt.eclipselink.core.context.java.JavaPrivateOwnable;

public class EclipseLinkJavaOneToOneMappingImpl extends GenericJavaOneToOneMapping implements EclipseLinkOneToOneMapping
{
	
	protected final JavaJoinFetchable joinFetchable;
	protected final JavaPrivateOwnable privateOwnable;

	public EclipseLinkJavaOneToOneMappingImpl(JavaPersistentAttribute parent) {
		super(parent);
		this.joinFetchable = new EclipseLinkJavaJoinFetchable(parent);
		this.privateOwnable = new EclipseLinkJavaPrivateOwnable(parent);
	}
	
	@Override
	protected EclipseLinkJpaFactory getJpaFactory() {
		return (EclipseLinkJpaFactory) super.getJpaFactory();
	}
	
	public JavaJoinFetchable getJoinFetchable() {
		return this.joinFetchable;
	}

	public PrivateOwnable getPrivateOwnable() {
		return this.privateOwnable;
	}
	
	@Override
	public void initialize(JavaResourcePersistentAttribute jrpa) {
		super.initialize(jrpa);
		this.joinFetchable.initialize(jrpa);
		this.privateOwnable.initialize(jrpa);
	}
	
	@Override
	public void update(JavaResourcePersistentAttribute jrpa) {
		super.update(jrpa);
		this.joinFetchable.update(jrpa);
		this.privateOwnable.update(jrpa);
	}
}
