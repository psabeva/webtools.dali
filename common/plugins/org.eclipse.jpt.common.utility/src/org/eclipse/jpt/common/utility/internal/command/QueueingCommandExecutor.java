/*******************************************************************************
 * Copyright (c) 2012, 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.command;

import org.eclipse.jpt.common.utility.command.CommandContext;
import org.eclipse.jpt.common.utility.command.StatefulCommandContext;

/**
 * @see AbstractQueueingCommandContext
 */
public class QueueingCommandExecutor
	extends AbstractQueueingCommandContext<StatefulCommandContext>
{
	public QueueingCommandExecutor() {
		this(DefaultCommandContext.instance());
	}

	public QueueingCommandExecutor(CommandContext commandExecutor) {
		this(new SimpleStatefulCommandExecutor(commandExecutor));
	}

	public QueueingCommandExecutor(StatefulCommandContext commandExecutor) {
		super(commandExecutor);
	}
}
