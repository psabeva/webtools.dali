/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2010 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Kiril Mitov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.feature;

import java.text.MessageFormat;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IDirectEditingContext;
import org.eclipse.graphiti.features.impl.AbstractDirectEditingFeature;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.jdt.core.JavaConventions;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jpt.jpa.core.context.PersistentAttribute;
import org.eclipse.jpt.jpa.core.context.PersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.i18n.JPAEditorMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;


public class DirectEditAttributeFeature extends AbstractDirectEditingFeature {
	
	private boolean isMethodAnnotated = false;

	public DirectEditAttributeFeature(IFeatureProvider fp) {
		super(fp);
	}

	public int getEditingType() {
		return TYPE_TEXT;
	}

	@Override
	public boolean canDirectEdit(IDirectEditingContext context) {
		return true;
	}

	public String getInitialValue(IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		PersistentAttribute jpa = (PersistentAttribute)getFeatureProvider().
											getBusinessObjectForPictogramElement(pe);
		isMethodAnnotated = JpaArtifactFactory.instance().isMethodAnnotated(jpa);
		Text txt = (Text) pe.getGraphicsAlgorithm().getGraphicsAlgorithmChildren().get(0);
		String value =  txt.getValue();
		if (isMethodAnnotated)
			value = JPAEditorUtil.produceValidAttributeName(value);		
		return value;
	}

	@Override
	public String checkValueValid(String value, IDirectEditingContext context) {
		if (isMethodAnnotated)
			value = JPAEditorUtil.produceValidAttributeName(value);		
		IStatus status = JavaConventions.validateFieldName(value, JavaCore.VERSION_1_5, JavaCore.VERSION_1_5);
		if (!status.isOK())
			return status.getMessage();
		status = checkDuplicateAttribute(value, context);
		if (!status.isOK())
			return status.getMessage();
		return null;
	}

	private IStatus checkDuplicateAttribute(String value, IDirectEditingContext context) {
		PictogramElement pe = context.getPictogramElement();
		PersistentAttribute oldAt = (PersistentAttribute) getBusinessObjectForPictogramElement(pe);
		PersistentAttribute newAl = ((PersistentType) oldAt.getParent()).getAttributeNamed(value);
		if (newAl != null && !newAl.equals(oldAt)) {
			String message = MessageFormat.format(JPAEditorMessages.DirectEditAttributeFeature_attributeExists, value);
			return new Status(IStatus.ERROR, JPADiagramEditorPlugin.PLUGIN_ID, message);
		}
		return Status.OK_STATUS;
	}

	@Override
	public IJPAEditorFeatureProvider getFeatureProvider() {
		return (IJPAEditorFeatureProvider) super.getFeatureProvider();
	}

	@Override
	public void setValue(String value, IDirectEditingContext context) {

		if (isMethodAnnotated) {
			value = JPAEditorUtil.produceValidAttributeName(value);
		}
		PictogramElement pe = context.getPictogramElement();
		PersistentAttribute oldAt = (PersistentAttribute) getBusinessObjectForPictogramElement(pe);

		try {
			JpaArtifactFactory.instance().renameAttribute(
					(PersistentType) oldAt.getParent(), oldAt.getName(), value);
			return;
		} catch (InterruptedException e) {
			return;
		}
	}
}
