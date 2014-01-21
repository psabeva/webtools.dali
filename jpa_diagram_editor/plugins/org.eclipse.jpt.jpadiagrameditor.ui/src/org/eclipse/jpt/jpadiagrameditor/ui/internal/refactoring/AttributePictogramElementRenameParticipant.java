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
import org.eclipse.jdt.internal.corext.refactoring.rename.RenameFieldProcessor;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.ltk.core.refactoring.Change;
import org.eclipse.ltk.core.refactoring.RefactoringStatus;
import org.eclipse.ltk.core.refactoring.participants.CheckConditionsContext;
import org.eclipse.ltk.core.refactoring.participants.RenameParticipant;

public class AttributePictogramElementRenameParticipant extends RenameParticipant{

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
		if (!originalJavaElement.getResource().getParent().exists())
			return false;
		return true;
	}

	@Override
	public String getName() {
		return "Update attribute";
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

		RenameFieldProcessor renameProcessor = (RenameFieldProcessor) getProcessor();

		IField field = renameProcessor.getField();
		IType type = field.getDeclaringType();
		final PersistentType oldJPT = JPAEditorUtil.getJPType((ICompilationUnit) type.getCompilationUnit());
		PersistentAttribute old = oldJPT.getAttributeNamed(field.getElementName());
		String newName = renameProcessor.getNewElementName();
		Object obj = renameProcessor.getNewElement();
		final ICompilationUnit newUnit = (((IField) obj).getDeclaringType()).getCompilationUnit();

		mapAttributes.put(old, newName);

		return new RefreshJptPictogramElementChange(newUnit, oldJPT, mapAttributes, true);
	}

}
