package org.eclipse.jpt.jpadiagrameditor.ui.internal.refactoring;

import java.util.Iterator;
import java.util.Map;
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
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.persistence.PersistenceUnit;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.modelintegration.util.ModelIntegrationUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorDiagramTypeProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.GraphicsUpdater;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.swt.widgets.Display;

public class RefreshJptPictogramElementChange extends Change {
	
	private ICompilationUnit fNewCompilationUnit;
	
	private PersistentType fOldJpt;
	
	private Map<PersistentAttribute, String> fElements;
	
	public RefreshJptPictogramElementChange(ICompilationUnit newCompilationUnit, PersistentType oldJpt, Map<PersistentAttribute, String> elements){
		super();
		this.fNewCompilationUnit = newCompilationUnit;
		this.fOldJpt = oldJpt;
		this.fElements = elements;
	}

	@Override
	public String getName() {
		return "Refresh pictogram element in the JPA Diagram";
	}

	@Override
	public void initializeValidationData(IProgressMonitor pm) {		
	}

	@Override
	public RefactoringStatus isValid(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {
		return null;
	}

	@Override
	public Change perform(IProgressMonitor pm) throws CoreException {
		refreshPictogramElement(fNewCompilationUnit, fOldJpt);
		
		ICompilationUnit oldUnit = JPAEditorUtil.getCompilationUnit(fOldJpt);
		if(oldUnit.isWorkingCopy()){
			try {
				oldUnit.discardWorkingCopy();
			} catch (JavaModelException e) {
				e.printStackTrace();
			}
		}
		
		return null;
	}

	@Override
	public Object getModifiedElement() {
		return null;
	}
	
	private void refreshPictogramElement(final ICompilationUnit newCompilationUnit, final PersistentType oldJPT) {
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
								.getPictogramElementForBusinessObject(oldJPT);
						
						if(pict == null) {
							return;
						}
						final PersistentType newJPT = JPAEditorUtil
								.getJPType(newCompilationUnit);
						if (newJPT == null)
							return;

						if(JpaArtifactFactory.instance().getTableName(oldJPT) != null) {
							String tableName = JPAEditorUtil
									.formTableName(newJPT);
							JpaArtifactFactory.instance().setTableName(newJPT,
									tableName);
						}

						GraphicsUpdater.updateHeader((ContainerShape) pict,
								newJPT.getSimpleName());
						
						TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(pict);
						RecordingCommand rc = new RecordingCommand(ted) {
							protected void doExecute() {
								
								if(fElements != null && !fElements.isEmpty()) {
									updateAttributesAndRelatedRelations(featureProvider, newJPT, fOldJpt);
								}
								
								refreshEditedElementsInDiagram(oldJPT,
										featureProvider, pict, newJPT);
							}
						};
						
						ted.getCommandStack().execute(rc);
					}
										
				}
			}
		});
	}
	
	private void refreshEditedElementsInDiagram(
			final PersistentType oldJPT,
			final IJPAEditorFeatureProvider featureProvider,
			final PictogramElement pict,
			final PersistentType newJPT) {
		
		GraphicsUpdater.linkNewBoToPict(oldJPT, pict, featureProvider, newJPT);

		for (PersistentAttribute oldAttr : oldJPT
				.getAttributes()) {
			PictogramElement attrPict = featureProvider
					.getPictogramElementForBusinessObject(oldAttr);
			if (attrPict != null) {
				for (PersistentAttribute newAttr : newJPT
						.getAttributes()) {
					if (newAttr.getName().equals(
							oldAttr.getName())) {
						GraphicsUpdater.linkNewBoToPict(oldAttr, attrPict,
								featureProvider, newAttr);
					}
				}
			}									
		}
	}
	
	private void updateAttributesAndRelatedRelations(IJPAEditorFeatureProvider fp, PersistentType newJpt, PersistentType oldJPT) {

		final Set<PersistentAttribute> ats = JpaArtifactFactory.instance()
				.getRelatedAttributes(oldJPT);

		Iterator<PersistentAttribute> itr = ats.iterator();
		while (itr.hasNext()) {
			PersistentAttribute old = itr.next();
						
			PersistentType refactoredAttrJPT = old.getDeclaringPersistentType();
			refactoredAttrJPT.getJavaResourceType().getJavaResourceCompilationUnit().synchronizeWithJavaSource();
			refactoredAttrJPT.synchronizeWithResourceModel();
			refactoredAttrJPT.update();
			
			String attributeTypeName = JpaArtifactFactory.instance().getRelTypeName(old);
			
			PersistentAttribute at = refactoredAttrJPT.getAttributeNamed(fElements.get(old));

			PersistenceUnit pu = JpaArtifactFactory.instance().getPersistenceUnit(refactoredAttrJPT);

			try {
				JpaArtifactFactory.instance().updateIRelationshipAttributes(refactoredAttrJPT, newJpt.getName(), fp, pu, old, at, attributeTypeName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
