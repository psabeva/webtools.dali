/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2012 SAP AG and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Petya Sabeva - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/

package org.eclipse.jpt.jpadiagrameditor.ui.internal.command;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class RenameEntityClass {
	
	private PersistentType jpt;
	private String newEntityName;
	
	public RenameEntityClass(PersistentType jpt, String newEntityName){
		super();
		this.jpt = jpt;
		this.newEntityName = newEntityName;
	}

	public void execute() {
		
		ICompilationUnit cu = JPAEditorUtil.getCompilationUnit(jpt);		
		try {
			RefactoringContribution contribution =
			    RefactoringCore.getRefactoringContribution(IJavaRefactorings.RENAME_TYPE);
			RenameJavaElementDescriptor descriptor =
			    (RenameJavaElementDescriptor) contribution.createDescriptor();
			descriptor.setProject(jpt.getJpaProject().getName( ));
			descriptor.setNewName(newEntityName); // new name for a Class
			descriptor.setJavaElement(cu.getType(jpt.getSimpleName()));
			descriptor.setUpdateReferences(true);
			descriptor.setUpdateSimilarDeclarations(true);
			descriptor.setMatchStrategy(RenameJavaElementDescriptor.STRATEGY_EMBEDDED);
			
			RenameSupport sup = RenameSupport.create(descriptor);
			IWorkbenchWindow ww = JPADiagramEditorPlugin.getDefault()
					.getWorkbench().getActiveWorkbenchWindow();
			Shell sh = ww.getShell();
			try {
				sup.perform(sh, ww);
			} catch (InterruptedException e) {
				JPADiagramEditorPlugin.logError("Cannot rename the type " + jpt.getSimpleName(), e); //$NON-NLS-1$

			} catch (InvocationTargetException e) {
				JPADiagramEditorPlugin.logError("Cannot rename the type " + jpt.getSimpleName(), e); //$NON-NLS-1$

			}

		} catch (CoreException e) {
			JPADiagramEditorPlugin.logError("Cannot rename the type " + jpt.getSimpleName(), e); //$NON-NLS-1$
		}
		
	}	
}
