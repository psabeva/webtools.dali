/*******************************************************************************
 * Copyright (c) 2006, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.selection;

import java.util.HashMap;
import java.util.HashSet;
import org.eclipse.jpt.common.ui.internal.utility.PartAdapter2;
import org.eclipse.jpt.common.utility.internal.StringTools;
import org.eclipse.jpt.common.utility.internal.Transformer;
import org.eclipse.jpt.common.utility.internal.AbstractTransformer;
import org.eclipse.jpt.common.utility.internal.model.value.DoubleModifiablePropertyValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.DoublePropertyValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.SimplePropertyValueModel;
import org.eclipse.jpt.common.utility.internal.model.value.TransformationPropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.PropertyValueModel;
import org.eclipse.jpt.common.utility.model.value.ModifiablePropertyValueModel;
import org.eclipse.jpt.jpa.core.JpaFile;
import org.eclipse.jpt.jpa.core.JpaStructureNode;
import org.eclipse.jpt.jpa.ui.selection.JpaEditorManager;
import org.eclipse.jpt.jpa.ui.selection.JpaSelectionManager;
import org.eclipse.jpt.jpa.ui.selection.JpaViewManager;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IViewPart;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchPartReference;

/**
 * Maintain:<ul>
 * <li>a collection of {@link JpaEditorManager JPA editor managers}
 * for the page's {@link IEditorPart editors}: The page manager creates
 * and disposes the editor managers. The editor manager is the <em>master</em>
 * part manager and holds the JPA file and JPA selection models that can be
 * used by the {@link #viewManagers view managers} and/or the page manager.
 * <li>a collection of {@link JpaViewManager JPA view managers}
 * for the page's JPA {@link IViewPart views}: The JPA views themselves create
 * and dispose the {@link #viewManagers view managers}.
 * </ul>
 * Forward the JPA selection to the active editor manager's JPA selection model,
 * which should notify the appropriate view managers etc.
 */
class JpaPageManager
	implements JpaSelectionManager, JpaViewManager.PageManager
{
	/**
	 * The manager's parent window manager.
	 */
	private final JpaWindowManager windowManager;

	/**
	 * The manager's page.
	 */
	private final IWorkbenchPage page;

	/**
	 * Editor managers keyed by corresponding editor.
	 * Lazily populated as managers are requested by
	 * {@link #viewManagers view managers}.
	 */
	private final HashMap<IEditorPart, JpaEditorManager> editorManagers = new HashMap<IEditorPart, JpaEditorManager>();

	/**
	 * Model that holds the editor manager corresponding to the active editor.
	 */
	private final SimplePropertyValueModel<JpaEditorManager> editorManagerModel = new SimplePropertyValueModel<JpaEditorManager>();

	/**
	 * The page's current JPA file. This wraps the current editor manager's
	 * JPA file model. It can be modified only by the editor manager.
	 */
	private final PropertyValueModel<JpaFile> jpaFileModel;

	/**
	 * The page's current JPA selection. This wraps the current editor manager's
	 * JPA selection model. It can be modified by either the editor manager or
	 * any view manager.
	 */
	private final ModifiablePropertyValueModel<JpaStructureNode> jpaSelectionModel;

	/**
	 * Listen to {@link #page} to maintain {@link #editorManagers}.
	 */
	private final IPartListener2 partListener = new PartListener();

	/**
	 * List of view managers for the page's JPA views.
	 * This is maintained by the JPA views themselves.
	 * It should be empty only immediately after construction
	 * and immediately before disposal.
	 */
	private final HashSet<JpaViewManager> viewManagers = new HashSet<JpaViewManager>();

	private volatile boolean disposed = false;


	JpaPageManager(JpaWindowManager windowManager, IWorkbenchPage page) {
		super();
		if (page == null) {
			throw new NullPointerException();
		}
		this.windowManager = windowManager;
		this.page = page;
		this.jpaFileModel = this.buildJpaFileModel();
		this.jpaSelectionModel = this.buildJpaSelectionModel();
		this.page.addPartListener(this.partListener);
		this.updateEditorManagerModel();
	}


	// ********** JPA file model **********

	public PropertyValueModel<JpaFile> getJpaFileModel() {
		return this.jpaFileModel;
	}

	private PropertyValueModel<JpaFile> buildJpaFileModel() {
		return new DoublePropertyValueModel<JpaFile>(this.buildEditorManagerJpaFileModel());
	}

	private PropertyValueModel<PropertyValueModel<JpaFile>> buildEditorManagerJpaFileModel() {
		return new TransformationPropertyValueModel<JpaEditorManager, PropertyValueModel<JpaFile>>(this.editorManagerModel, JPA_FILE_MODEL_TRANSFORMER);
	}

	private static final Transformer<JpaEditorManager, PropertyValueModel<JpaFile>> JPA_FILE_MODEL_TRANSFORMER = new JpaFileModelTransformer();

	/* CU private */ static class JpaFileModelTransformer
		extends AbstractTransformer<JpaEditorManager, PropertyValueModel<JpaFile>>
	{
		@Override
		public PropertyValueModel<JpaFile> transform_(JpaEditorManager editorManager) {
			return editorManager.getJpaFileModel();
		}
	}


	// ********** JPA selection model **********

	public ModifiablePropertyValueModel<JpaStructureNode> getJpaSelectionModel() {
		return this.jpaSelectionModel;
	}

	private ModifiablePropertyValueModel<JpaStructureNode> buildJpaSelectionModel() {
		return new DoubleModifiablePropertyValueModel<JpaStructureNode>(this.buildEditorManagerJpaSelectionModel());
	}

	private PropertyValueModel<ModifiablePropertyValueModel<JpaStructureNode>> buildEditorManagerJpaSelectionModel() {
		return new TransformationPropertyValueModel<JpaEditorManager, ModifiablePropertyValueModel<JpaStructureNode>>(this.editorManagerModel, JPA_SELECTION_MODEL_TRANSFORMER);
	}

	private static final Transformer<JpaEditorManager, ModifiablePropertyValueModel<JpaStructureNode>> JPA_SELECTION_MODEL_TRANSFORMER = new JpaSelectionModelTransformer();

	/* CU private */ static class JpaSelectionModelTransformer
		extends AbstractTransformer<JpaEditorManager, ModifiablePropertyValueModel<JpaStructureNode>>
	{
		@Override
		public ModifiablePropertyValueModel<JpaStructureNode> transform_(JpaEditorManager editorManager) {
			return editorManager.getJpaSelectionModel();
		}
	}


	// ********** selection **********

	/**
	 * This should only be called from the
	 * {@link #windowManager window manager}.
	 */
	public void setSelection(JpaStructureNode selection) {
		this.jpaSelectionModel.setValue(selection);
	}


	// ********** editor managers **********

	public JpaEditorManager getEditorManager(IEditorPart editor) {
		return (editor == null) ? null : this.getEditorManager_(editor);
	}

	private synchronized JpaEditorManager getEditorManager_(IEditorPart editor) {
		JpaEditorManager editorManager = this.editorManagers.get(editor);
		if (editorManager == null) {
			editorManager = this.buildEditorManager(editor);
			if (editorManager != null) {
				JpaWorkbenchManager.debug("add editor manager:", editor); //$NON-NLS-1$
				this.editorManagers.put(editor, editorManager);
			}
		}
		return editorManager;
	}

	/**
	 * <strong>NB:</strong> The editor manager is built once (when first
	 * requested by a view manager) and not disposed until all the view
	 * managers are removed from the page manager. As a result, the editor
	 * manager is most likely driven by the class of the editor, as opposed
	 * to the editor's content....
	 */
	private JpaEditorManager buildEditorManager(IEditorPart editor) {
		return (JpaEditorManager) editor.getAdapter(JpaEditorManager.class);
	}


	// ********** view managers **********

	public synchronized void addViewManager(JpaViewManager viewManager) {
		JpaWorkbenchManager.debug("add view manager:", viewManager); //$NON-NLS-1$
		if (this.disposed) {
			// This can happen if the page manager's last view manager is removed
			// after a page manager was returned to a new view manager but before that
			// view manager adds itself to the page manager....
			throw new IllegalStateException("page manager is disposed: " + this); //$NON-NLS-1$
		}
		if ( ! this.viewManagers.add(viewManager)) {
			throw new IllegalArgumentException("duplicate view manager: " + viewManager); //$NON-NLS-1$
		}
	}

	public synchronized void removeViewManager(JpaViewManager viewManager) {
		JpaWorkbenchManager.debug("remove view manager:", viewManager); //$NON-NLS-1$
		if ( ! this.viewManagers.remove(viewManager)) {
			throw new IllegalArgumentException("missing view manager: " + viewManager); //$NON-NLS-1$
		}
		if (this.viewManagers.isEmpty()) {
			this.dispose();
		}
	}


	// ********** misc **********

	/**
	 * When all the view managers are gone we get rid of the page manager.
	 */
	private void dispose() {
		this.disposed = true;
		this.page.removePartListener(this.partListener);
		for (JpaEditorManager editorManager : this.editorManagers.values()) {
			editorManager.dispose();
		}
		this.windowManager.removePageManager(this.page);
	}

	@Override
	public String toString() {
		return StringTools.buildToStringFor(this, this.page);
	}


	// ********** part listener **********

	/**
	 * Remove the corresponding editor manager whenever an editor is closed.
	 */
	/* CU private */ class PartListener
		extends PartAdapter2
	{
		@Override
		public void partActivated(IWorkbenchPartReference partRef) {
			JpaPageManager.this.updateEditorManagerModel();
		}

		@Override
		public void partBroughtToTop(IWorkbenchPartReference partRef) {
			JpaPageManager.this.updateEditorManagerModel();
		}

		@Override
		public void partClosed(IWorkbenchPartReference partRef) {
			IWorkbenchPart part = partRef.getPart(false);
			if (part != null) {
				JpaPageManager.this.partClosed(part);
			}
		}
	}

	/* CU private */ synchronized void partClosed(IWorkbenchPart part) {
		JpaEditorManager editorManager = this.editorManagers.remove(part);
		if (editorManager != null) {
			JpaWorkbenchManager.debug("removed editor manager:", part); //$NON-NLS-1$
			editorManager.dispose();
		}
		this.updateEditorManagerModel();
	}

	/* CU private */ void updateEditorManagerModel() {
		this.editorManagerModel.setValue(this.getEditorManager(this.page.getActiveEditor()));
	}
}
