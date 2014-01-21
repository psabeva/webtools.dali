package org.eclipse.jpt.jpadiagrameditor.ui.internal.refactoring;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
import org.eclipse.jpt.jpa.core.JpaStructureNode;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpa.core.context.orm.EntityMappings;
import org.eclipse.jpt.jpa.core.context.orm.OrmManagedType;
import org.eclipse.jpt.jpa.core.context.persistence.MappingFileRef;
import org.eclipse.jpt.jpa.core.resource.orm.XmlAttributeMapping;
import org.eclipse.jpt.jpa.core.resource.orm.XmlTypeMapping;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditor;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.modelintegration.util.ModelIntegrationUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorDiagramTypeProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorFeatureProvider;
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

	private boolean isAttribute;
	
	private JpaStructureNode renamedObject;
	
	public RefreshJptPictogramElementChange(
			ICompilationUnit newCompilationUnit, PersistentType oldJpt,
			Map<PersistentAttribute, String> elements, boolean isAttribute) {
		super();
		this.fNewCompilationUnit = newCompilationUnit;
		this.fOldJpt = oldJpt;
		this.fElements = elements;
		this.isAttribute = isAttribute;
	}

	@Override
	public String getName() {
		return "Refresh renamed pictogram element in the JPA Diagram editor";
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
		
		refreshPictogramElementAfterRename();
		
		ICompilationUnit oldUnit = JPAEditorUtil.getCompilationUnit(fOldJpt);
		if(!oldUnit.equals(fNewCompilationUnit)) {
			JPAEditorUtil.discardWorkingCopy(oldUnit);
		}

		return null;
	}

	@Override
	public Object getModifiedElement() {
		return null;
	}

	/**
	 * Refresh the pictogram elemnt of the renamed persistent type and refresh their relationships, if any.
	 */
	private void refreshPictogramElementAfterRename() {
		Display.getDefault().syncExec(new Runnable() {

			public void run() {
				IProject project = fOldJpt.getJpaProject().getProject();
				final Diagram d = ModelIntegrationUtil.getDiagramByProject(project);
				if (d == null)
					return;
				final JPAEditorDiagramTypeProvider provider = ModelIntegrationUtil.getProviderByDiagram(d.getName());
				if (provider != null) {
					final IJPAEditorFeatureProvider featureProvider = provider.getFeatureProvider();
					if (featureProvider != null) {
						final PictogramElement pict = featureProvider.getPictogramElementForBusinessObject(fOldJpt);
						if (pict == null) {
							return;
						}

						TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(pict);
						RecordingCommand rc = new RecordingCommand(ted) {
							protected void doExecute() {
								
								refreshPictogramElement(provider, featureProvider, pict);

							}
						};

						ted.getCommandStack().execute(rc);
					}

				}
			}
		});
	}
	
	/**
	 * Refresh the pictogram elemnt of the renamed persistent type and refresh their relationships, if any.
	 * @param provider - the jpa diagram editor type provider
	 * @param featureProvider - the jpa diagram editor feature provider
	 * @param pict - the pictogram element to be refreshed
	 */
	private void refreshPictogramElement(final JPAEditorDiagramTypeProvider provider,
			final IJPAEditorFeatureProvider featureProvider, final PictogramElement pict) {
		
		final PersistentType renamedJPT = JPAEditorUtil.getJPType(fNewCompilationUnit);
		if (renamedJPT == null)
			return;
		
		Iterator<PersistentAttribute> relAtr = fOldJpt.getAllAttributes().iterator();
		while (relAtr.hasNext()) {
			((JPAEditorFeatureProvider) featureProvider).updateKeyRel(fOldJpt, renamedJPT, relAtr.next());
		}

		if (JpaArtifactFactory.instance().getTableName(fOldJpt) != null) {
			String tableName = JPAEditorUtil.formTableName(renamedJPT);
			JpaArtifactFactory.instance().setTableName(renamedJPT,	tableName);
		}

		GraphicsUpdater.updateHeader((ContainerShape) pict,	renamedJPT.getSimpleName());

		if (fElements != null && !fElements.isEmpty()) {
			Iterator<PersistentAttribute> iter = fElements.keySet().iterator();
			while (iter.hasNext()) {
				PersistentAttribute persistentAttribute = (PersistentAttribute) iter.next();
				PersistentType jpt = persistentAttribute.getDeclaringPersistentType();
				GraphicsUpdater.linkNewBoToPict(jpt, featureProvider.getPictogramElementForBusinessObject(jpt), featureProvider, jpt);
			}
			
			updateAttributesAndRelatedRelations(featureProvider, renamedJPT, fOldJpt);
			renameOrmXmlAttributesIfNecessary();
		}

		refreshEditedElementsInDiagram(fOldJpt, featureProvider, pict, renamedJPT);
		
		if(!isAttribute){
			setRenamedObject(renamedJPT);
		}
		
//		reselect(provider);

	}

	/**
	 * If there are persistent attributes that will be renamed, rename them in the orm.xml file if it exists.
	 */
	private void renameOrmXmlAttributesIfNecessary() {
		Iterator<PersistentAttribute> attributes = fElements.keySet().iterator();
		while (attributes.hasNext()) {
			PersistentAttribute persistentAttribute = (PersistentAttribute) attributes.next();
			PersistentType jpt = persistentAttribute.getDeclaringPersistentType();
			renameAttributeInOrmXml(jpt,persistentAttribute.getName(), fElements.get(persistentAttribute));
		}
	}

	/**
	 * Relink the pictogram element and all its child elements (such as compartment, attribute and so on)
	 * of the persistent type with the renamed persistent type.
	 * @param oldJPT - the old persistent type
	 * @param featureProvider - the jpa diagram editor feature provider
	 * @param pict - the pictogram element of the old persistent type
	 * @param renamedJPT - the renamed persistent type
	 */
	private void refreshEditedElementsInDiagram(final PersistentType oldJPT,
			final IJPAEditorFeatureProvider featureProvider,
			final PictogramElement pict, final PersistentType renamedJPT) {
		
		GraphicsUpdater.linkNewBoToPict(oldJPT, pict, featureProvider, renamedJPT);

		for (PersistentAttribute oldAttr : oldJPT.getAttributes()) {
			PictogramElement attrPict = featureProvider.getPictogramElementForBusinessObject(oldAttr);
			if (attrPict != null) {
				for (PersistentAttribute newAttr : renamedJPT.getAttributes()) {
					if (newAttr.getName().equals(oldAttr.getName())) {
						GraphicsUpdater.linkNewBoToPict(oldAttr, attrPict, featureProvider, newAttr);
					}
				}
			}
		}	
	}

	/**
	 * Rename all attributes in their pictogram elements, that are marked to be renamed and find all relationships
	 * in which they are involved and update them.
	 * @param fp - the jpa diagram editor feature provider
	 * @param renamedJpt - the new persistent type
	 * @param oldJPT - the old persistent type
	 */
	private void updateAttributesAndRelatedRelations(
			IJPAEditorFeatureProvider fp, PersistentType renamedJpt,
			PersistentType oldJPT) {

		Iterator<PersistentAttribute> itr = fElements.keySet().iterator();
		while (itr.hasNext()) {
			PersistentAttribute oldAttribute = itr.next();
			String attributeTypeName = JpaArtifactFactory.instance().getRelTypeName(oldAttribute);

			PersistentType refactoredAttrJPT = (PersistentType) oldAttribute.getParent();
			synchJptModelWithSources(refactoredAttrJPT);
			
			String renamedAttributeName = fElements.get(oldAttribute);
			PersistentAttribute renamedAttribute = refactoredAttrJPT.getAttributeNamed(renamedAttributeName);
			if (renamedAttribute == null) {
				return;
			}

			if(isAttribute){
				setRenamedObject(renamedAttribute);
			}
			GraphicsUpdater.renameAttributePictogramElement(fp, oldAttribute, renamedAttribute, renamedAttributeName);
			try {
				JpaArtifactFactory.instance().updateIRelationshipAttributes(
						refactoredAttrJPT, renamedJpt.getName(), fp, oldAttribute, renamedAttribute,
						attributeTypeName);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	private void synchJptModelWithSources(PersistentType jpt) {
		jpt.getJavaResourceType().getJavaResourceCompilationUnit().synchronizeWithJavaSource();
		jpt.synchronizeWithResourceModel();
		jpt.update();
	}

	
//	private void reselect(JPAEditorDiagramTypeProvider dprov){
//		JPADiagramEditor editor = dprov.getDiagramEditor();
//		editor.selectPictogramElements(new PictogramElement[] {null});
////		editor.selectionChanged(editor.getWorkbenchPart(), new StructuredSelection(pict));
//		JpaStructureNode node = getRenamedObject();
//		JpaStructureNode selectedNode = null;
//		if(node instanceof PersistentAttribute){
//			selectedNode = ((PersistentAttribute)node).getDeclaringPersistentType();
//		} else {
//			selectedNode = node;
//		}
//		PictogramElement pict = dprov.getFeatureProvider().getPictogramElementForBusinessObject(selectedNode);
//		editor.setPictogramElementForSelection(pict);
////		editor.getJpaSelectionManager().setSelection(node);
////		editor.getJpaSelectionModel().setValue(node);
//	}
	
	/**
	 * Rename the given persistent attribute in the orm.xml file, if exists.
	 * @param jpt - the persistent type in which the persistent attribute is declared
	 * @param oldName - the old name of the persistent attribute
	 * @param newName - the new name of the persistent attribute
	 */
	private void renameAttributeInOrmXml(PersistentType jpt, String oldName, String newName) {
		MappingFileRef mapFileRef = JpaArtifactFactory.instance().getOrmXmlByForPersistentType(jpt);
		if (mapFileRef == null) {
			return;
		}
		EntityMappings root = (EntityMappings) mapFileRef.getMappingFile().getRoot();
		Iterator<OrmManagedType> managedTypesIter = root.getManagedTypes().iterator();
		while (managedTypesIter.hasNext()) {
			XmlTypeMapping xmlType = (XmlTypeMapping) managedTypesIter.next().getXmlManagedType();
			if (xmlType.getAttributes() == null) {
				return;
			}
			List<XmlAttributeMapping> attributeMappings = xmlType.getAttributes().getAttributeMappings();
			for (XmlAttributeMapping attr : attributeMappings) {
				if (attr.getName().equals(oldName)) {
					attr.setName(newName);
				}
			}
		}
	}

	public JpaStructureNode getRenamedObject() {
		return renamedObject;
	}

	public void setRenamedObject(JpaStructureNode renamedObject) {
		this.renamedObject = renamedObject;
	}
}
