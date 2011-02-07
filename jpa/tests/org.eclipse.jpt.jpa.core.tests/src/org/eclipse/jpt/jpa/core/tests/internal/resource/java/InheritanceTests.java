/*******************************************************************************
 * Copyright (c) 2007, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.core.tests.internal.resource.java;

import java.util.Iterator;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jpt.common.utility.internal.iterators.ArrayIterator;
import org.eclipse.jpt.jpa.core.resource.java.InheritanceAnnotation;
import org.eclipse.jpt.jpa.core.resource.java.InheritanceType;
import org.eclipse.jpt.jpa.core.resource.java.JPA;
import org.eclipse.jpt.jpa.core.resource.java.JavaResourcePersistentType;

@SuppressWarnings("nls")
public class InheritanceTests extends JpaJavaResourceModelTestCase {

	public InheritanceTests(String name) {
		super(name);
	}

	private ICompilationUnit createTestInheritance() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.INHERITANCE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Inheritance");
			}
		});
	}
	
	private ICompilationUnit createTestInheritanceWithStrategy() throws Exception {
		return this.createTestType(new DefaultAnnotationWriter() {
			@Override
			public Iterator<String> imports() {
				return new ArrayIterator<String>(JPA.INHERITANCE, JPA.INHERITANCE_TYPE);
			}
			@Override
			public void appendTypeAnnotationTo(StringBuilder sb) {
				sb.append("@Inheritance(strategy = InheritanceType.JOINED)");
			}
		});
	}

	public void testInheritance() throws Exception {
		ICompilationUnit cu = this.createTestInheritance();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		InheritanceAnnotation inheritance = (InheritanceAnnotation) typeResource.getAnnotation(JPA.INHERITANCE);
		assertNotNull(inheritance);
	}
	
	public void testGetStrategy() throws Exception {
		ICompilationUnit cu = this.createTestInheritanceWithStrategy();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 
		
		InheritanceAnnotation inheritance = (InheritanceAnnotation) typeResource.getAnnotation(JPA.INHERITANCE);
		assertEquals(InheritanceType.JOINED, inheritance.getStrategy());
	}
	
	public void testSetStrategy() throws Exception {
		ICompilationUnit cu = this.createTestInheritance();
		JavaResourcePersistentType typeResource = buildJavaTypeResource(cu); 

		InheritanceAnnotation inheritance = (InheritanceAnnotation) typeResource.getAnnotation(JPA.INHERITANCE);
		inheritance.setStrategy(InheritanceType.TABLE_PER_CLASS);
		
		assertSourceContains("@Inheritance(strategy = TABLE_PER_CLASS)", cu);
		
		inheritance.setStrategy(null);
		
		assertSourceDoesNotContain("strategy", cu);
	}

}