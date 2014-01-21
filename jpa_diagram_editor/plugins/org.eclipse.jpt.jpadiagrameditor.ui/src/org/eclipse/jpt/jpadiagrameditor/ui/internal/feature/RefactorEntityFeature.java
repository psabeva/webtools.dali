/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2011 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Dimov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.feature;

import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenamingNameSuggestor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.refactoring.reorg.RenameRefactoringWizard;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jpt.common.ui.internal.utility.SynchronousUiCommandContext;
import org.eclipse.jpt.common.utility.command.Command;
import org.eclipse.jpt.jpa.core.JpaProjectManager;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.command.RenameEntityClass;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.modelintegration.util.ModelIntegrationUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorDiagramTypeProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPASolver;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardPage;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;


public abstract class RefactorEntityFeature extends AbstractCustomFeature {

	protected Set<PersistentAttribute> ats = null;
	protected boolean hasEntitySpecifiedName = false;
//	private static final String REGEX_PATTERN = "(_[\\d]+)*"; //$NON-NLS-1$
	
	public RefactorEntityFeature(IFeatureProvider fp) {
		super(fp);
	}
	
	@Override
	public boolean isAvailable(IContext context) {
    	if (!(context instanceof ICustomContext))
    		return false;
    	ICustomContext ctx = (ICustomContext)context;
    	PictogramElement pe = ctx.getInnerPictogramElement();
    	Object bo = getFeatureProvider().getBusinessObjectForPictogramElement(pe);
    	if (bo instanceof PersistentType) {
    		PersistentType jpt = (PersistentType)bo;
    		ats = JpaArtifactFactory.instance().getRelatedAttributes(jpt);
    		hasEntitySpecifiedName = JpaArtifactFactory.instance().hasEntitySpecifiedName(jpt);
    		return true;
    	}
    	if (pe instanceof Shape) {
    		ContainerShape cs = ((Shape)pe).getContainer();
    		if (cs == null)
    			return false;
     		bo = getFeatureProvider().getBusinessObjectForPictogramElement(cs);
        	if (bo instanceof PersistentType) {
        		PersistentType jpt = (PersistentType)bo;
        		ats = JpaArtifactFactory.instance().getRelatedAttributes(jpt);
        		hasEntitySpecifiedName = JpaArtifactFactory.instance().hasEntitySpecifiedName(jpt);
        		return true;
        	}
    	}    	
		return false;
	}
	
	@Override
	public boolean canExecute(ICustomContext context) {
		return true;
	}
	
	public void execute(ICustomContext context, SelectionDispatchAction action) {
		PictogramElement pe = context.getInnerPictogramElement();
		final ContainerShape pict = ((Shape)pe).getContainer();
	    final PersistentType jpt = (PersistentType)getBusinessObjectForPictogramElement(pict);
		ICompilationUnit cu = getFeatureProvider().getCompilationUnit(jpt);
		StructuredSelection sel = new StructuredSelection(cu);
		final Semaphore s = new Semaphore(0);
		ShowBusy showBusy = new ShowBusy(s);
		JPASolver.ignoreEvents = true;
		
		try {
			configureRefactoringDialogSettingsToRenameVariables();
			action.run(sel);
		} catch (Exception e) {} 
		BusyIndicator.showWhile(Display.getCurrent(), showBusy);
		JPASolver.ignoreEvents = false;
	}
	
	public void execute(ICustomContext context, String newName, ICompilationUnit cu, PersistentType originalJPT) {
		IProject project = originalJPT.getJpaProject().getProject();
		final Diagram d = ModelIntegrationUtil.getDiagramByProject(project);
		if (d == null)
			return;
		final JPAEditorDiagramTypeProvider provider = ModelIntegrationUtil.getProviderByDiagram(d.getName());
		
		PictogramElement pe = provider.getFeatureProvider().getPictogramElementForBusinessObject(originalJPT);
		
		provider.getDiagramEditor().selectPictogramElements(new PictogramElement[] {null});
		
		final Semaphore s = new Semaphore(0);
		ShowBusy showBusy = new ShowBusy(s);
		JPASolver.ignoreEvents = true;
		Command renameEntityCommand = new RenameEntityClass(originalJPT, newName);
		try {
			getJpaProjectManager().execute(renameEntityCommand, SynchronousUiCommandContext.instance());
		} catch (InterruptedException e) {
			JPADiagramEditorPlugin.logError("Cannot delete attribute with name ", e); //$NON-NLS-1$		
		}
		
		BusyIndicator.showWhile(Display.getCurrent(), showBusy);
		JPASolver.ignoreEvents = false;
		
		provider.getDiagramEditor().setPictogramElementForSelection(pe);
		
		
	
	}

	@SuppressWarnings("restriction")
    private void configureRefactoringDialogSettingsToRenameVariables() {
            IDialogSettings javaSettings= JavaPlugin.getDefault().getDialogSettings();
            IDialogSettings refactoringSettings= javaSettings.getSection(RefactoringWizardPage.REFACTORING_SETTINGS);
            if(refactoringSettings == null)
            	return;
            refactoringSettings.put(RenameRefactoringWizard.TYPE_UPDATE_SIMILAR_ELEMENTS, true);
            refactoringSettings.put(RenameRefactoringWizard.TYPE_SIMILAR_MATCH_STRATEGY, RenamingNameSuggestor.STRATEGY_EMBEDDED);
    }
	
	@Override
	protected Diagram getDiagram() {
		return getFeatureProvider().getDiagramTypeProvider().getDiagram();
	}	
	
	@Override
	public IJPAEditorFeatureProvider getFeatureProvider() {
		return (IJPAEditorFeatureProvider)super.getFeatureProvider();
	}
	
	class ShowBusy implements Runnable {
		private Semaphore s;
		boolean moved = false;
		ShowBusy(Semaphore s) {
			this.s = s;
		}
		
		public void run() {
			try {
				moved = s.tryAcquire(2, 4, TimeUnit.SECONDS);
			} catch (InterruptedException e) {
				JPADiagramEditorPlugin.logError("Thread interrupted", e);  //$NON-NLS-1$		 							
			}
		}

		boolean isMoved() {
			return moved;
		}		
	}
	
	private JpaProjectManager getJpaProjectManager() {
		return (JpaProjectManager) ResourcesPlugin.getWorkspace().getAdapter(JpaProjectManager.class);
	}

}
