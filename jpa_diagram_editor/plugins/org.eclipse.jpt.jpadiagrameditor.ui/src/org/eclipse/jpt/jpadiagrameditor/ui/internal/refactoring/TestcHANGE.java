package org.eclipse.jpt.jpadiagrameditor.ui.internal.refactoring;

import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.Set;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.ui.refactoring.RenameSupport;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.modelintegration.util.ModelIntegrationUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorDiagramTypeProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.GraphicsUpdater;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.RenameProcessor;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;

public class TestcHANGE extends Change {
	
	private PersistentType jpt;
	private PersistentAttribute old;
	private String newName;
	
	public TestcHANGE(PersistentType jpt, PersistentAttribute old, String newName){
		this.jpt = jpt;
		this.old = old;
		this.newName = newName;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "Bahgo";
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		refreshPictogramElement(jpt);
		
//		ICompilationUnit oldUnit = JPAEditorUtil.getCompilationUnit(jpt);
//		if(oldUnit.isWorkingCopy()){
//			try {
//				oldUnit.discardWorkingCopy();
//			} catch (JavaModelException e) {
//				e.printStackTrace();
//			}
//		}
		
		return null;
	}

	@Override
	public Object getModifiedElement() {
		return null;
	}
	
	private void refreshPictogramElement(final PersistentType oldJPT) {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				IProject project = oldJPT.getJpaProject().getProject();
				Diagram d = ModelIntegrationUtil.getDiagramByProject(project);
				if(d == null)
					return;
				JPAEditorDiagramTypeProvider provider = ModelIntegrationUtil
						.getProviderByDiagram(d.getName());
				if (provider != null) {
					final IJPAEditorFeatureProvider featureProvider = provider
							.getFeatureProvider();
					if (featureProvider != null) {
						final PictogramElement pict = featureProvider
								.getPictogramElementForBusinessObject(old);
						
						if(pict == null) {
							return;
						}

						jpt.getJpaProject().getContextModelRoot().synchronizeWithResourceModel();
						jpt.synchronizeWithResourceModel();
						jpt.update();
						
						final PersistentAttribute newAt = jpt.getAttributeNamed(newName); 
						
						TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(pict);
						RecordingCommand rc = new RecordingCommand(ted) {
							protected void doExecute() {
								
									updateAttributesAndRelatedRelations(featureProvider, old, jpt, newName);
								
								
									GraphicsUpdater.linkNewBoToPict(old, pict, featureProvider, newAt);

							}
						};
						
						ted.getCommandStack().execute(rc);
					}
										
				}
			}
		});
	}

	
	private void updateAttributesAndRelatedRelations(IJPAEditorFeatureProvider fp, PersistentAttribute old, PersistentType refactoredAttrJPT, String newName ) {

						
//			PersistentType refactoredAttrJPT = old.getDeclaringPersistentType();
			refactoredAttrJPT.getJavaResourceType().getJavaResourceCompilationUnit().synchronizeWithJavaSource();
			refactoredAttrJPT.synchronizeWithResourceModel();
			refactoredAttrJPT.update();
			
			String attributeTypeName = JpaArtifactFactory.instance().getRelTypeName(old);
			
			PersistentAttribute at = refactoredAttrJPT.getAttributeNamed(newName);

			PersistenceUnit pu = JpaArtifactFactory.instance().getPersistenceUnit(refactoredAttrJPT);

			try {
				JpaArtifactFactory.instance().updateIRelationshipAttributes(refactoredAttrJPT, refactoredAttrJPT.getName(), fp, pu, old, at, attributeTypeName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}

}
