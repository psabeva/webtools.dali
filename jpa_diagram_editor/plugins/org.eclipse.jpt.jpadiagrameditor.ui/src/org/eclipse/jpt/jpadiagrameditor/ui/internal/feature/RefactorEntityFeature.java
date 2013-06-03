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

import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IContext;
import org.eclipse.graphiti.features.context.ICustomContext;
import org.eclipse.graphiti.features.custom.AbstractCustomFeature;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.refactoring.IJavaRefactorings;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenamingNameSuggestor;
import org.eclipse.jdt.internal.ui.JavaPlugin;
import org.eclipse.jdt.internal.ui.refactoring.reorg.RenameRefactoringWizard;
import org.eclipse.jdt.ui.actions.SelectionDispatchAction;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.command.RenameEntityClass;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPASolver;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.ltk.core.refactoring.RefactoringContribution;
import org.eclipse.ltk.core.refactoring.RefactoringCore;
import org.eclipse.ltk.ui.refactoring.RefactoringWizardPage;
import org.eclipse.swt.custom.BusyIndicator;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;


public abstract class RefactorEntityFeature extends AbstractCustomFeature {

	protected Set<PersistentAttribute> ats = null;
	protected boolean hasEntitySpecifiedName = false;
	
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
		} catch (Exception e) {
			JPADiagramEditorPlugin.logError("Cannot rename the type " + jpt.getSimpleName(), e); //$NON-NLS-1$
		}
		
		BusyIndicator.showWhile(Display.getCurrent(), showBusy);
		JPASolver.ignoreEvents = false;
	}
	
	public void execute(ICustomContext context, String newName, ICompilationUnit cu, PersistentType originalJPT) {
		final Semaphore s = new Semaphore(0);
		ShowBusy showBusy = new ShowBusy(s);
		JPASolver.ignoreEvents = true;
		
		RenameEntityClass renameEntityCommand = new RenameEntityClass(originalJPT, newName);
		renameEntityCommand.execute();
		
		BusyIndicator.showWhile(Display.getCurrent(), showBusy);
		JPASolver.ignoreEvents = false;		
	}
	
//	public void remapEntity(final PersistentType oldJPT,
//								   final Shape pict,
//								   final PersistenceUnit pu,
//								   final boolean rename,
//								   final JPAProjectListener lsnr,
//								   final IJPAEditorFeatureProvider fp) {
//		BusyIndicator.showWhile(Display.getCurrent(), new Runnable() {
//			public void run() {
//				fp.getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer().selectPictogramElements(new PictogramElement[] {});				 
//
//				String newJPTName = lsnr.getNewJPTName();
//				lsnr.setOldJptName(oldJPT.getSimpleName());
//				
//				if (!JpaPreferences.getDiscoverAnnotatedClasses(oldJPT.getJpaProject().getProject())) {
//					JPAEditorUtil.createUnregisterEntityFromXMLJob(oldJPT.getJpaProject(), oldJPT.getName());
//					JPAEditorUtil.createRegisterEntityInXMLJob(oldJPT.getJpaProject(), newJPTName);
//				}
//				
//				PersistentType newJPT = JpaArtifactFactory.instance().getJPT(newJPTName, pu);
//					
//				if(newJPT == null)
//					return;
//				
////				if (!JpaPreferences.getDiscoverAnnotatedClasses(oldJPT.getJpaProject().getProject())) {
////					JPAEditorUtil.createUnregisterEntityFromXMLJob(oldJPT.getJpaProject(), oldJPT.getName());
////					JPAEditorUtil.createRegisterEntityInXMLJob(oldJPT.getJpaProject(), newJPTName);
////				}
//				
//				if (rename) {
//					String tableName = JPAEditorUtil.formTableName(newJPT);
//					JpaArtifactFactory.instance().setTableName(newJPT, tableName);
//				}
//				
//				GraphicsUpdater.updateHeader((ContainerShape)pict, newJPT.getSimpleName());
//				linkNewElement(oldJPT, pict, fp, newJPT);
//
//				for(PersistentAttribute oldAttr : oldJPT.getAttributes()){
//					PictogramElement attrPict = fp.getPictogramElementForBusinessObject(oldAttr);
//					if(attrPict != null){
//						for(PersistentAttribute newAttr : newJPT.getAttributes()){
//							if(newAttr.getName().equals(oldAttr.getName())){
//								linkNewElement(oldAttr, attrPict, fp, newAttr);
//							}
//						}
//					}
//				}
//
//				fp.getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer().setPictogramElementForSelection(pict);
//				
//				IWorkbenchSite ws = ((IDiagramContainerUI)fp.getDiagramTypeProvider().getDiagramBehavior().getDiagramContainer()).getSite();
//		        ICompilationUnit cu = fp.getCompilationUnit(newJPT);
//		        fp.getJPAEditorUtil().formatCode(cu, ws);
//			}
//
//			private void linkNewElement(Object oldBO, PictogramElement pict,
//					IJPAEditorFeatureProvider fp, Object newBO) {
//				fp.link((ContainerShape)pict, newBO);
//				LayoutContext context = new LayoutContext((ContainerShape)pict);
//				fp.layoutIfPossible(context);
//				
//				String oldBoKey = fp.getKeyForBusinessObject(oldBO);
//				if(oldBoKey != null){
//					fp.remove(oldBoKey);
//				}
//				String newBoKey = fp.getKeyForBusinessObject(newBO);
//				if (fp.getBusinessObjectForKey(newBoKey) == null) {
//					fp.putKeyToBusinessObject(newBoKey, newBO);
//				}
//			}
//		});
//		
//	}
	
	@SuppressWarnings("restriction")
	private void configureRefactoringDialogSettingsToRenameVariables() {
		IDialogSettings javaSettings= JavaPlugin.getDefault().getDialogSettings();
		IDialogSettings refactoringSettings= javaSettings.getSection(RefactoringWizardPage.REFACTORING_SETTINGS);
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
}
