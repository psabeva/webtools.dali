/*******************************************************************************
 * <copyright>
 *
 * Copyright (c) 2005, 2012 SAP AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *    Stefan Dimov - initial API, implementation and documentation
 *
 * </copyright>
 *
 *******************************************************************************/
package org.eclipse.jpt.jpadiagrameditor.ui.internal.feature;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import org.eclipse.emf.transaction.RecordingCommand;
import org.eclipse.emf.transaction.TransactionalEditingDomain;
import org.eclipse.emf.transaction.util.TransactionUtil;
import org.eclipse.graphiti.features.IFeatureProvider;
import org.eclipse.graphiti.features.context.IAddContext;
import org.eclipse.graphiti.features.context.impl.AddContext;
import org.eclipse.graphiti.features.impl.AbstractAddShapeFeature;
import org.eclipse.graphiti.mm.algorithms.Image;
import org.eclipse.graphiti.mm.algorithms.Polyline;
import org.eclipse.graphiti.mm.algorithms.Rectangle;
import org.eclipse.graphiti.mm.algorithms.RoundedRectangle;
import org.eclipse.graphiti.mm.algorithms.Text;
import org.eclipse.graphiti.mm.algorithms.styles.Font;
import org.eclipse.graphiti.mm.algorithms.styles.LineStyle;
import org.eclipse.graphiti.mm.algorithms.styles.Orientation;
import org.eclipse.graphiti.mm.pictograms.ContainerShape;
import org.eclipse.graphiti.mm.pictograms.Diagram;
import org.eclipse.graphiti.mm.pictograms.PictogramElement;
import org.eclipse.graphiti.mm.pictograms.Shape;
import org.eclipse.graphiti.services.Graphiti;
import org.eclipse.graphiti.services.IGaService;
import org.eclipse.graphiti.ui.services.GraphitiUi;
import org.eclipse.graphiti.util.IColorConstant;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.internal.core.SourceType;
import org.eclipse.jpt.jpa.core.JpaProject;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.JPADiagramEditorPlugin;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.i18n.JPAEditorMessages;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.modelintegration.util.ModelIntegrationUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IAddEntityContext;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.JPAEditorImageProvider;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.GraphicsUpdater;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorConstants;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorConstants.ShapeType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorPredefinedColoredAreas;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JPAEditorUtil;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.JpaArtifactFactory;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.util.Wrp;


@SuppressWarnings({ "restriction" })
public class AddJPAEntityFeature extends AbstractAddShapeFeature {

	private boolean shouldRearrangeIsARelations = true;
	private static ContainerShape primaryShape;
	private static ContainerShape relationShape;
	private static ContainerShape basicShape;


	public AddJPAEntityFeature(IFeatureProvider fp, boolean shouldRearrangeIsARelations) {
		super(fp);
		this.shouldRearrangeIsARelations = shouldRearrangeIsARelations;
	}

	public AddJPAEntityFeature(IFeatureProvider fp) {
		super(fp);
	}

	public boolean canAdd(IAddContext context) {
		Object newObj = context.getNewObject();

		if (newObj instanceof JavaPersistentType) {
			if (context.getTargetContainer() instanceof Diagram) {
				JavaPersistentType jpt = (JavaPersistentType) newObj;
				return checkJPTForAdding(jpt);
			}
		} else if (newObj instanceof ICompilationUnit) {
			if (context.getTargetContainer() instanceof Diagram) {
				ICompilationUnit cu = (ICompilationUnit) newObj;
				JavaPersistentType jpt = JPAEditorUtil.getJPType(cu);
				return checkJPTForAdding(jpt);
			}
		} if (newObj instanceof SourceType) {
			if (context.getTargetContainer() instanceof Diagram) {
				ICompilationUnit cu = ((SourceType)newObj).getCompilationUnit();
				JavaPersistentType jpt = JPAEditorUtil.getJPType(cu);
				return checkJPTForAdding(jpt);
			}
		}
		return false;
	}

	private boolean checkJPTForAdding(JavaPersistentType jpt) {
		if (jpt == null)
			return false;

		PictogramElement[] pictograms = getFeatureProvider()
				.getAllPictogramElementsForBusinessObject(jpt);
		JpaProject proj = ModelIntegrationUtil.getProjectByDiagram(getDiagram().getName());
		if (proj != null)
			if (proj != jpt.getJpaProject())
				return false;
		return (pictograms == null) || (pictograms.length == 0);
	}

	@Override
	public IJPAEditorFeatureProvider getFeatureProvider() {
		return (IJPAEditorFeatureProvider) super.getFeatureProvider();
	}

	public PictogramElement add(final IAddContext context) {
		final IJPAEditorFeatureProvider fp = getFeatureProvider();
		Object newObj = context.getNewObject();
		JavaPersistentType jpt = null;
		if (newObj instanceof JavaPersistentType) {
			jpt = (JavaPersistentType) newObj;
		} else if (newObj instanceof ICompilationUnit) {
			ICompilationUnit cu = (ICompilationUnit) newObj;
			jpt = JPAEditorUtil.getJPType(cu);

		} else if (newObj instanceof SourceType) {
			ICompilationUnit cu = ((SourceType)newObj).getCompilationUnit();
			jpt = JPAEditorUtil.getJPType(cu);
		}
	
		//TODO this is wrong, should not need to do any of these updates or syncs.
		//should be changing the dali model synchronously so that all the syncs/updates are completed
		//take a look at the JpaProjectManager.execute(Command, ExtendedCommandExecutor) 
		jpt.getJavaResourceType().getJavaResourceCompilationUnit().synchronizeWithJavaSource();
		jpt.update();
		jpt.synchronizeWithResourceModel();
		
		final Diagram targetDiagram = (Diagram) context.getTargetContainer();
		final Wrp wrp = new Wrp();
		createEntity(context, fp, targetDiagram, wrp, jpt);
		return (PictogramElement) wrp.getObj();
	}

	private void createEntity(final IAddContext context, final IJPAEditorFeatureProvider fp, final Diagram targetDiagram,
			final Wrp wrp, final JavaPersistentType jpt) {
		
		TransactionalEditingDomain ted = TransactionUtil.getEditingDomain(targetDiagram);
		
		ted.getCommandStack().execute(new RecordingCommand(ted) {
			@Override
			protected void doExecute() {

				ContainerShape entityShape = Graphiti.getPeService().createContainerShape(targetDiagram, true);

				JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot = JpaArtifactFactory.instance().determineDiagramObjectType(jpt);
				createEntityRectangle(context, entityShape, dot, fp.getDiagramTypeProvider().getDiagram());
				link(entityShape, jpt);
				Shape shape = Graphiti.getPeService().createShape(entityShape,	false);
				Polyline headerBottomLine = Graphiti.getGaService()
						.createPolyline(shape, new int[] { 0, 30, JPAEditorConstants.ENTITY_WIDTH, 30 });
				headerBottomLine.setForeground(manageColor(JPAEditorConstants.ENTITY_BORDER_COLOR));
				headerBottomLine.setLineWidth(JPAEditorConstants.ENTITY_BORDER_WIDTH);

				addHeader(jpt, entityShape, JPAEditorConstants.ENTITY_WIDTH, dot);

				createCompartments(context, jpt, entityShape);
				fillCompartments(jpt, entityShape);

				String key = fp.getKeyForBusinessObject(jpt);
				if (fp.getBusinessObjectForKey(key) == null)
					fp.putKeyToBusinessObject(key, jpt);

				Graphiti.getPeService().createChopboxAnchor(entityShape);
				entityShape.setVisible(true);
				layoutPictogramElement(entityShape);

				UpdateAttributeFeature updateFeature = new UpdateAttributeFeature(fp);
				updateFeature.reconnect(jpt);
				if (shouldRearrangeIsARelations)
					JpaArtifactFactory.instance().rearrangeIsARelations(getFeatureProvider());
				wrp.setObj(entityShape);
			}
		});
	}
	
	private void createCompartments(IAddContext context, JavaPersistentType jpt,
			ContainerShape entityShape) {
		JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot = JpaArtifactFactory.instance().determineDiagramObjectType(jpt);
		primaryShape = createCompartmentRectangle(entityShape, 
												  JPAEditorConstants.ENTITY_MIN_HEIGHT, 
												  JPAEditorMessages.AddJPAEntityFeature_primaryKeysShape,
												  dot);
		relationShape = createCompartmentRectangle(entityShape, 
												   GraphicsUpdater.getNextCompartmentY(primaryShape) + JPAEditorConstants.SEPARATOR_HEIGHT,
												   JPAEditorMessages.AddJPAEntityFeature_relationAttributesShapes,
												   dot);
		basicShape = createCompartmentRectangle(entityShape, 
												GraphicsUpdater.getNextCompartmentY(relationShape) + JPAEditorConstants.SEPARATOR_HEIGHT, 
												JPAEditorMessages.AddJPAEntityFeature_basicAttributesShapes,
												dot);
		if (IAddEntityContext.class.isInstance(context)) {
			IAddEntityContext entityContext = (IAddEntityContext) context;
			GraphicsUpdater.setCollapsed(primaryShape, entityContext.isPrimaryCollapsed());
			GraphicsUpdater.setCollapsed(basicShape, entityContext.isBasicCollapsed());
			GraphicsUpdater.setCollapsed(relationShape, entityContext.isRelationCollapsed());
		} else {
			GraphicsUpdater.setCollapsed(primaryShape, false);
			GraphicsUpdater.setCollapsed(basicShape, false);
			GraphicsUpdater.setCollapsed(relationShape, false);
		}
	}

	private void fillCompartments(JavaPersistentType jpt, ContainerShape entityShape) {
		String[] primaryKeyAnnotations = new String[] {JPAEditorConstants.ANNOTATION_ID, JPAEditorConstants.ANNOTATION_EMBEDDED_ID, JPAEditorConstants.ANNOTATION_MAPS_ID};
		for(String annotation : primaryKeyAnnotations){
			addCompartmentChildren(primaryShape, jpt, annotation, null);
		}
		String[] relationAnnotations = new String[] {JPAEditorConstants.ANNOTATION_MANY_TO_MANY, 
				JPAEditorConstants.ANNOTATION_MANY_TO_ONE, JPAEditorConstants.ANNOTATION_ONE_TO_MANY,
				JPAEditorConstants.ANNOTATION_ONE_TO_ONE};
	    for(String annotation : relationAnnotations){
		   addCompartmentChildren(relationShape, jpt, annotation, primaryKeyAnnotations);
	    }
        addBasicAttributes(basicShape, jpt);
        GraphicsUpdater.updateEntityShape(entityShape);
	}

	private ContainerShape createCompartmentRectangle(
			ContainerShape entityShape, int y, String attribTxt,
			JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot) {
		int width = entityShape.getGraphicsAlgorithm().getWidth();
		ContainerShape containerShape = Graphiti.getPeService().createContainerShape(
				entityShape, false);
		Graphiti.getPeService().setPropertyValue(containerShape,
				JPAEditorConstants.PROP_SHAPE_TYPE, ShapeType.COMPARTMENT
						.toString());
		Rectangle rect = addCompartmentRectangle(y, width, containerShape);
		
		addCompartmentHeaderText(attribTxt, width, rect);
		
		UpdateAttributeFeature updateFeature = new UpdateAttributeFeature(getFeatureProvider());
		
		updateFeature.addSeparatorsToShape(containerShape, dot);
		
		return containerShape;
	}

	private Rectangle addCompartmentRectangle(int y, int width,
			ContainerShape containerShape) {
		Rectangle rect = Graphiti.getGaService().createRectangle(containerShape);
		rect.setFilled(Boolean.FALSE);
		rect.setLineVisible(Boolean.FALSE);
		rect.setHeight(0);
		rect.setWidth(width);
		rect.setX(0);
		rect.setY(y);
		containerShape.setActive(true);
		return rect;
	}

	private void addCompartmentHeaderText(String attribTxt, int width,
			Rectangle rect) {
		IJPAEditorFeatureProvider fp = getFeatureProvider();
		Text text = UpdateAttributeFeature.addText(fp, rect, attribTxt);
		Font font = GraphitiUi.getGaService().manageFont(getDiagram(),
				IGaService.DEFAULT_FONT, 7, true, false);
		text.setFont(font);
		Graphiti.getGaService().setWidth(text, width);
		Graphiti.getGaService().setLocationAndSize(text, 0, 2, width,
				13);
		text.setHorizontalAlignment(Orientation.ALIGNMENT_CENTER);
		text.setVerticalAlignment(Orientation.ALIGNMENT_CENTER);
	}

	private void addCompartmentChildren(
			ContainerShape containerShape, JavaPersistentType jpt,
			String attributeAnnotations, String[] excludeAnnotations) {
		List<JavaPersistentAttribute> attributes = new ArrayList<JavaPersistentAttribute>();

		for (JavaPersistentAttribute attribute : jpt.getAttributes()) {
			HashSet<String> annotations = JpaArtifactFactory.instance().getAnnotationNames(attribute);
			if (annotations.contains(attributeAnnotations) && canAddAttribute(annotations, excludeAnnotations)) {
				attributes.add(attribute);
			}
		}
		addAttributes(containerShape, attributes);
	}
	
	private boolean canAddAttribute(HashSet<String> annotations, String[] excludeAnnotations){
		if(excludeAnnotations == null || excludeAnnotations.length == 0)
			return true;
		for(String annotation : excludeAnnotations){
			if(annotations.contains(annotation))
				return false;
		}
		
		return true;
	}
	
	private void addBasicAttributes(ContainerShape containerShape, JavaPersistentType jpt){
		List<JavaPersistentAttribute> attributes = new ArrayList<JavaPersistentAttribute>();
		
		for (JavaPersistentAttribute attribute : jpt.getAttributes()){
			HashSet<String> annotations = JpaArtifactFactory.instance().getAnnotationNames(attribute);
			if(!(annotations.contains(JPAEditorConstants.ANNOTATION_ID))&& !(annotations.contains(JPAEditorConstants.ANNOTATION_EMBEDDED_ID)) && !(annotations.contains(JPAEditorConstants.ANNOTATION_MANY_TO_MANY)) && 
					!(annotations.contains(JPAEditorConstants.ANNOTATION_MANY_TO_ONE)) && !(annotations.contains(JPAEditorConstants.ANNOTATION_ONE_TO_MANY))&&
					!(annotations.contains(JPAEditorConstants.ANNOTATION_ONE_TO_ONE)) && !(annotations.contains(JPAEditorConstants.ANNOTATION_MAPS_ID)) || annotations.isEmpty()){
				attributes.add(attribute);
			}
		}
		addAttributes(containerShape, attributes);
	}
	
	private void addAttributes(ContainerShape entityShape,
			List<JavaPersistentAttribute> attributes) {
		for (int i = 0; i < attributes.size(); i++) {
			JavaPersistentAttribute jpa = attributes.get(i);
			addAttribute(jpa, entityShape);
		}
	}

	public static RoundedRectangle createEntityRectangle(IAddContext context,
														 ContainerShape entityShape, 
														 JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot,  
														 Diagram diagram) {

		IColorConstant foreground = JpaArtifactFactory.instance().getForeground(dot);
		IColorConstant background = JpaArtifactFactory.instance().getBackground(dot);
		String renderingStyle = JpaArtifactFactory.instance().getRenderingStyle(dot);
		
		RoundedRectangle entityRectangle = Graphiti.getGaService().createRoundedRectangle(
				entityShape, JPAEditorConstants.ENTITY_CORNER_WIDTH,
				JPAEditorConstants.ENTITY_CORNER_HEIGHT);
		entityRectangle
				.setForeground(Graphiti.getGaService().manageColor(diagram, foreground));
		entityRectangle
				.setBackground(Graphiti.getGaService().manageColor(diagram, background));
		Graphiti.getGaService().setRenderingStyle(entityRectangle.getPictogramElement().getGraphicsAlgorithm(), 
				JPAEditorPredefinedColoredAreas.getAdaptedGradientColoredAreas(renderingStyle));
		entityRectangle.setLineWidth(JPAEditorConstants.ENTITY_BORDER_WIDTH);
		entityRectangle.setLineStyle(LineStyle.SOLID);
		Graphiti.getGaService().setLocationAndSize(entityRectangle, context
				.getX(), context.getY(),
				(context.getWidth() == -1) ? JPAEditorConstants.ENTITY_WIDTH
						: context.getWidth(),
				(context.getHeight() == -1) ? JPAEditorConstants.ENTITY_HEIGHT
						: context.getHeight());
		return entityRectangle;
	}
	
	private String determineTheAppropriateTypeCommand(JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot){
		String typeIconId = ""; //$NON-NLS-1$
		if(dot.equals(JPAEditorConstants.DIAGRAM_OBJECT_TYPE.Entity)){
			typeIconId = JPAEditorImageProvider.JPA_ENTITY;
		} else if (dot.equals(JPAEditorConstants.DIAGRAM_OBJECT_TYPE.MappedSupeclass)){
			typeIconId = JPAEditorImageProvider.MAPPED_SUPERCLASS;
		} else if (dot.equals(JPAEditorConstants.DIAGRAM_OBJECT_TYPE.Embeddable)){
			typeIconId = JPAEditorImageProvider.EMBEDDABLE;
		}
		
		return typeIconId;		
	}
	
	private ContainerShape addHeader(JavaPersistentType addedWrapper,
									 ContainerShape entityShape, 
									 int width,
									 JPAEditorConstants.DIAGRAM_OBJECT_TYPE dot) {
		
		String entityIconId = determineTheAppropriateTypeCommand(dot);
		
		ContainerShape headerIconShape = Graphiti.getPeService().createContainerShape(
				entityShape, false);
		Rectangle iconRect = Graphiti.getGaService().createRectangle(headerIconShape);
		iconRect.setFilled(Boolean.FALSE);
		iconRect.setLineVisible(Boolean.FALSE);
		iconRect.setHeight(JPAEditorConstants.ICON_RECT_HEIGHT);
		iconRect.setX(0);
		iconRect.setWidth(JPAEditorConstants.HEADER_ICON_RECT_WIDTH);
		iconRect.setHeight(JPAEditorConstants.HEADER_ICON_RECT_HEIGHT);
		iconRect.setY(0);
		Image headerIcon = Graphiti.getGaService().createImage(iconRect,
				entityIconId);
		Graphiti.getGaService().setLocationAndSize(headerIcon, 
													JPAEditorConstants.ICON_HEADER_X,
													JPAEditorConstants.ICON_HEADER_Y,
													JPAEditorConstants.ICON_WIDTH, 
													JPAEditorConstants.ICON_HEIGHT);
		Graphiti.getPeService().setPropertyValue(headerIconShape,
				JPAEditorConstants.PROP_SHAPE_TYPE, ShapeType.ICON.toString());

		ContainerShape entityHeaderTextShape = Graphiti.getPeService().createContainerShape(entityShape, false);
		Graphiti.getPeService()
				.setPropertyValue(entityHeaderTextShape,
						JPAEditorConstants.PROP_SHAPE_TYPE, ShapeType.HEADER
								.toString());
		Rectangle headerRect = Graphiti.getGaService().createRectangle(entityHeaderTextShape);
		headerRect.setFilled(Boolean.FALSE);
		headerRect.setLineVisible(Boolean.FALSE);
		headerRect.setWidth(width - JPAEditorConstants.HEADER_TEXT_RECT_WIDTH_REDUCER);
		headerRect.setHeight(JPAEditorConstants.HEADER_TEXT_RECT_HEIGHT);
		headerRect.setY(2);
		headerRect.setX(JPAEditorConstants.HEADER_TEXT_RECT_X);

		String headerTextString = JPAEditorUtil.getText(addedWrapper);
		ICompilationUnit cu = getFeatureProvider().getCompilationUnit(addedWrapper);
		JPAEditorUtil.becomeWorkingCopy(cu);
		headerTextString = JPAEditorUtil.returnSimpleName(headerTextString); 
		try {
			if (cu.hasUnsavedChanges()) {
				headerTextString = JPAEditorConstants.HEADER_PREFIX_DIRTY + headerTextString;
			}
		} catch (JavaModelException e) { 
			JPADiagramEditorPlugin.logError("Cannot check compilation unit for unsaved changes", e);  //$NON-NLS-1$		 
		}
		Text headerTextObj = Graphiti.getGaService().createText(getDiagram(), headerRect,
				headerTextString, IGaService.DEFAULT_FONT, IGaService.DEFAULT_FONT_SIZE, false, true);
		headerTextObj.setForeground(manageColor(JPAEditorConstants.ENTITY_TEXT_FOREGROUND));
		headerTextObj.setHorizontalAlignment(Orientation.ALIGNMENT_LEFT);
		headerTextObj.setVerticalAlignment(Orientation.ALIGNMENT_TOP);
		Graphiti.getGaService().setLocationAndSize(headerTextObj, 1, 2,	width, 20);

		return entityHeaderTextShape;

	}

	private void addAttribute(JavaPersistentAttribute pa,
			ContainerShape compartmentShape) {
		IJPAEditorFeatureProvider fp = getFeatureProvider();
		fp.putKeyToBusinessObject(fp.getKeyForBusinessObject(pa), pa);
		JavaPersistentType jpt = (JavaPersistentType)pa.getParent();
		String key = fp.getKeyForBusinessObject(jpt);
		if (fp.getBusinessObjectForKey(key) == null)
			fp.putKeyToBusinessObject(key, jpt);
		AddContext addContext = new AddContext();
		addContext.setTargetContainer(compartmentShape.getContainer());
		addContext.setNewObject(pa);
		GraphicalAddAttributeFeature graphicalAdd = new GraphicalAddAttributeFeature(fp);
		graphicalAdd.add(addContext);
	}

}
