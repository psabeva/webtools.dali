/*******************************************************************************
 *  Copyright (c) 2010  Oracle. 
 *  All rights reserved.  This program and the accompanying materials are 
 *  made available under the terms of the Eclipse Public License v1.0 which 
 *  accompanies this distribution, and is available at 
 *  http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors: 
 *  	Oracle - initial API and implementation
 *******************************************************************************/
package org.eclipse.jpt.core.internal.jpa1.context;

import org.eclipse.jpt.core.context.BaseJoinColumn;
import org.eclipse.jpt.core.context.SecondaryTable;
import org.eclipse.jpt.core.internal.context.BaseJoinColumnTextRangeResolver;
import org.eclipse.jpt.core.internal.validation.DefaultJpaValidationMessages;
import org.eclipse.jpt.core.internal.validation.JpaValidationMessages;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class SecondaryTablePrimaryKeyJoinColumnValidator extends PrimaryKeyJoinColumnValidator
{
	private final SecondaryTable secondaryTable;
	
	public SecondaryTablePrimaryKeyJoinColumnValidator(
				SecondaryTable secondaryTable,
				BaseJoinColumn column,
				BaseJoinColumn.Owner owner,
				BaseJoinColumnTextRangeResolver textRangeResolver) {
		super(column, owner, textRangeResolver);
		this.secondaryTable = secondaryTable;
	}

	protected boolean isSecondaryTableVirtual() {
		return this.secondaryTable.isVirtual();
	}

	protected String getSecondaryTableName() {
		return this.secondaryTable.getName();
	}
	
	@Override
	public IMessage buildUnresolvedNameMessage() {
		if (isSecondaryTableVirtual())  {
			return this.buildVirtualSecondaryTableUnresolvedNameMessage();
		}
		return super.buildUnresolvedNameMessage();
	}

	protected IMessage buildVirtualSecondaryTableUnresolvedNameMessage() {
		return DefaultJpaValidationMessages.buildMessage(
			IMessage.HIGH_SEVERITY,
			JpaValidationMessages.VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME,
			new String[] {this.getSecondaryTableName(), getColumn().getName(), getColumn().getDbTable().getName()},
			getColumn(), 
			getTextRangeResolver().getNameTextRange()
		);
	}
	
	@Override
	protected String getVirtualAttributeUnresolvedNameMessage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMessage buildUnresolvedReferencedColumnNameMessage() {
		if (isSecondaryTableVirtual())  {
			return this.buildVirtualSecondaryTableUnresolvedReferencedColumnNameMessage();
		}
		return super.buildUnresolvedReferencedColumnNameMessage();
	}

	protected IMessage buildVirtualSecondaryTableUnresolvedReferencedColumnNameMessage() {
		return DefaultJpaValidationMessages.buildMessage(
			IMessage.HIGH_SEVERITY,
			JpaValidationMessages.VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME,
			new String[] {this.getSecondaryTableName(), getColumn().getReferencedColumnName(), getColumn().getReferencedColumnDbTable().getName()},
			getColumn(), 
			getTextRangeResolver().getReferencedColumnNameTextRange()
		);
	}	

	@Override
	protected String getVirtualAttributeUnresolvedReferencedColumnNameMessage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMessage buildUnspecifiedNameMultipleJoinColumnsMessage() {
		if (this.isSecondaryTableVirtual()) {
			return this.buildVirtualSecondaryTableUnspecifiedNameMultipleJoinColumnsMessage();
		}
		return super.buildUnspecifiedNameMultipleJoinColumnsMessage();
	}

	protected IMessage buildVirtualSecondaryTableUnspecifiedNameMultipleJoinColumnsMessage() {
		return DefaultJpaValidationMessages.buildMessage(
			IMessage.HIGH_SEVERITY,
			JpaValidationMessages.VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS,
			new String[] {this.getSecondaryTableName()},
			getColumn(), 
			getTextRangeResolver().getNameTextRange()
		);
	}

	@Override
	protected String getVirtualAttributeUnspecifiedNameMultipleJoinColumnsMessage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public IMessage buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		if (this.isSecondaryTableVirtual()) {
			return this.buildVirtualSecondaryTableUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();
		}
		return super.buildUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage();
	}

	protected IMessage buildVirtualSecondaryTableUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		return DefaultJpaValidationMessages.buildMessage(
			IMessage.HIGH_SEVERITY,
			JpaValidationMessages.VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS,
			new String[] {this.getSecondaryTableName()},
			getColumn(), 
			getTextRangeResolver().getReferencedColumnNameTextRange()
		);
	}

	@Override
	protected String getVirtualAttributeUnspecifiedReferencedColumnNameMultipleJoinColumnsMessage() {
		throw new UnsupportedOperationException();
	}
}