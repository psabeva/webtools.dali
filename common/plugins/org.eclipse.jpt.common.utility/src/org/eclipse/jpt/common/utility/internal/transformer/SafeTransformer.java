/*******************************************************************************
 * Copyright (c) 2013 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal.transformer;

import org.eclipse.jpt.common.utility.ExceptionHandler;
import org.eclipse.jpt.common.utility.internal.ObjectTools;
import org.eclipse.jpt.common.utility.transformer.Transformer;

/**
 * Tranformer wrapper that will handle any exceptions thrown by the wrapped
 * transformer with an {@link ExceptionHandler exception handler}. If the
 * wrapped transformer throws an exception, the safe transformer will handle
 * the exception and return a client-configured output.
 * 
 * @param <I> input: the type of the object passed to the transformer
 * @param <O> output: the type of the object returned by the transformer
 */
public class SafeTransformer<I, O>
	implements Transformer<I, O>
{
	private final Transformer<? super I, ? extends O> transformer;
	private final ExceptionHandler exceptionHandler;
	private final O exceptionOutput;

	public SafeTransformer(Transformer<? super I, ? extends O> transformer, ExceptionHandler exceptionHandler, O exceptionOutput) {
		super();
		if ((transformer == null) || (exceptionHandler == null)) {
			throw new NullPointerException();
		}
		this.transformer = transformer;
		this.exceptionHandler = exceptionHandler;
		this.exceptionOutput = exceptionOutput;
	}

	public O transform(I input) {
		try {
			return this.transformer.transform(input);
		} catch (Throwable ex) {
			this.exceptionHandler.handleException(ex);
			return this.exceptionOutput;
		}
	}

	@Override
	public String toString() {
		return ObjectTools.toString(this, this.transformer);
	}
}
