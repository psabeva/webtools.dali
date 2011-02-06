/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 *
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal.details;

import org.eclipse.jpt.common.ui.internal.widgets.ValidatingDialog;
import org.eclipse.jpt.jpa.core.context.ReadOnlyBaseJoinColumn;
import org.eclipse.swt.widgets.Shell;

/**
 * The abstract definition the dialog showing the information for a join column
 * to edit or to create.
 *
 * @see BaseJoinColumnStateObject
 *
 * @version 2.0
 * @since 1.0
 */
public abstract class BaseJoinColumnDialog<T extends BaseJoinColumnStateObject>
	extends ValidatingDialog<T>
{

	/**
	 * Either the join column to edit or <code>null</code> if this state object
	 * is used to create a new one.
	 */
	private ReadOnlyBaseJoinColumn joinColumn;

	/**
	 * The owner of the join column to create or where it is located.
	 */
	private Object owner;

	/**
	 * Creates a new <code>BaseJoinColumnDialog</code>.
	 *
	 * @param parent The parent shell
	 * @param owner The owner of the join column to create or where it is located
	 * @param joinColumn Either the join column to edit or <code>null</code> if
	 * this state object is used to create a new one
	 */
	public BaseJoinColumnDialog(Shell parent,
	                            Object owner,
	                            ReadOnlyBaseJoinColumn joinColumn) {

		super(parent);

		this.owner      = owner;
		this.joinColumn = joinColumn;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected String getDescription() {
		return JptUiDetailsMessages.JoinColumnDialog_description;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected String getDescriptionTitle() {

		if (joinColumn == null) {
			return JptUiDetailsMessages.JoinColumnDialog_addJoinColumnDescriptionTitle;
		}

		return JptUiDetailsMessages.JoinColumnDialog_editJoinColumnDescriptionTitle;
	}

	/**
	 * Returns the join column used by this state object for editing or
	 * <code>null</code> if this state object is used to create a new one.
	 *
	 * @return Either the edited join column or <code>null</code>
	 */
	public ReadOnlyBaseJoinColumn getJoinColumn() {
		return joinColumn;
	}

	/**
	 * Returns the owner where the join column is located or where a new one can
	 * be added.
	 *
	 * @return The parent of the join column
	 */
	protected Object getOwner() {
		return owner;
	}

	/*
	 * (non-Javadoc)
	 */
	@Override
	protected String getTitle() {

		if (joinColumn == null) {
			return JptUiDetailsMessages.JoinColumnDialog_addJoinColumnTitle;
		}

		return JptUiDetailsMessages.JoinColumnDialog_editJoinColumnTitle;
	}
}