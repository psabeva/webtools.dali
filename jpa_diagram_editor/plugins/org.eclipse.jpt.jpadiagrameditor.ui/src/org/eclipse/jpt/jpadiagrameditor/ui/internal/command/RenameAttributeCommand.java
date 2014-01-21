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
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class RenameAttributeCommand {

	private PersistentType jpt;
	private String oldName;
	private String newName;

	public RenameAttributeCommand(PersistentType jpt, String oldName, String newName) {
		super();
		this.jpt = jpt;
		this.oldName = oldName;
		this.newName = newName;
	}

	public void execute() {
		try {
			
			IType type = JpaArtifactFactory.instance().getType(jpt.getJpaProject().getJavaProject(), jpt.getName());
			IField attributeField = type.getField(oldName);
			
			ICompilationUnit unit = type.getCompilationUnit();
			if(unit.isWorkingCopy() && unit.hasUnsavedChanges()){
				unit.commitWorkingCopy(false, new NullProgressMonitor());
			}

			RefactoringContribution contribution = RefactoringCore.getRefactoringContribution(IJavaRefactorings.RENAME_FIELD);
			RenameJavaElementDescriptor descriptor = (RenameJavaElementDescriptor) contribution.createDescriptor();
			descriptor.setProject(jpt.getJpaProject().getName());
			descriptor.setNewName(newName);
			descriptor.setJavaElement(attributeField);
			descriptor.setUpdateReferences(true);
			descriptor.setRenameGetters(true);
			descriptor.setRenameSetters(true);
			descriptor.setUpdateTextualOccurrences(true);

			RenameSupport renameSupport = RenameSupport.create(descriptor);
			IWorkbenchWindow ww = JPADiagramEditorPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
			Shell sh = ww.getShell();
			renameSupport.perform(sh, ww);

		} catch (InterruptedException e) {
			JPADiagramEditorPlugin.logError("Cannot rename attribute", e); //$NON-NLS-1$
		} catch (InvocationTargetException e) {
			JPADiagramEditorPlugin.logError("Cannot rename attribute", e); //$NON-NLS-1$
		} catch (CoreException e1) {
			JPADiagramEditorPlugin.logError("Cannot rename attribute", e1); //$NON-NLS-1$
		}
	}

}