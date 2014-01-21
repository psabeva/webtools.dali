package org.eclipse.jpt.jpadiagrameditor.ui.internal.refactoring;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.OperationCanceledException;
import org.eclipse.core.runtime.Status;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IField;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.IType;
import org.eclipse.jdt.core.refactoring.RenameTypeArguments;
import org.eclipse.jdt.core.refactoring.descriptors.RenameJavaElementDescriptor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameCompilationUnitProcessor;
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameTypeProcessor;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

public class JptPictogramElementRenameParticipant extends RenameParticipant {

    protected IJavaElement originalJavaElement;
    protected String newName;
    private Map<PersistentAttribute, String> mapAttributes = new HashMap<PersistentAttribute, String>();

    @Override
    protected boolean initialize(Object element) {
            if (!getArguments().getUpdateReferences()) {
                    return false;
            }

            newName = getArguments().getNewName();

            this.originalJavaElement = (IJavaElement) element;
            if(!originalJavaElement.getResource().getParent().exists())
                    return false;
            return true;
    }

    @Override
    public String getName() {
            return "Update element";
    }

    @Override
    public RefactoringStatus checkConditions(IProgressMonitor pm,
                    CheckConditionsContext context) throws OperationCanceledException {
            return RefactoringStatus.create(Status.OK_STATUS);
    }

    @SuppressWarnings("restriction")
    @Override
	public Change createChange(IProgressMonitor pm) throws CoreException,
			OperationCanceledException {

		RenameTypeProcessor renameProcessor = null;
		if (getProcessor() instanceof RenameCompilationUnitProcessor) {
			renameProcessor = ((RenameCompilationUnitProcessor) getProcessor()).getRenameTypeProcessor();
		} else {
			renameProcessor = ((RenameTypeProcessor) getProcessor());
		}

		IType oldType = renameProcessor.getType();
		final PersistentType oldJPT = JPAEditorUtil.getJPType((ICompilationUnit) oldType.getCompilationUnit());
		final ICompilationUnit newUnit = ((IType) renameProcessor.getRefactoredJavaElement(oldType)).getCompilationUnit();

		RenameTypeArguments renameArg = (RenameTypeArguments) getArguments();
		IJavaElement[] elements = renameArg.getSimilarDeclarations();
		if (elements != null) {
			renameProcessor.setMatchStrategy(RenameJavaElementDescriptor.STRATEGY_EMBEDDED);
			for (IJavaElement oldElement : elements) {
				IJavaElement newElement = renameProcessor.getRefactoredJavaElement(oldElement);
				if (oldElement instanceof IField) {
					IType type = ((IField) oldElement).getDeclaringType();
					PersistentType jpt = JPAEditorUtil.getJPType(type.getCompilationUnit());
					if (jpt != null) {
						jpt.getJavaResourceType().getJavaResourceCompilationUnit().synchronizeWithJavaSource();
						jpt.synchronizeWithResourceModel();
						jpt.update();
						PersistentAttribute jpa = jpt.getAttributeNamed(oldElement.getElementName());
						mapAttributes.put(jpa, newElement.getElementName());
					}
				}
			}
		}
		return new RefreshJptPictogramElementChange(newUnit, oldJPT, mapAttributes, false);

	}
}
