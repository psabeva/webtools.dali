package org.eclipse.jpt.jpadiagrameditor.ui.internal.relations;

import org.eclipse.graphiti.mm.pictograms.Connection;
import org.eclipse.jpt.jpa.core.context.java.JavaPersistentType;
import org.eclipse.jpt.jpadiagrameditor.ui.internal.provider.IJPAEditorFeatureProvider;

public class HasSingleReferenceRelation extends HasReferanceRelation{

	public HasSingleReferenceRelation(JavaPersistentType embeddingEntity,
			JavaPersistentType embeddable) {
		super(embeddingEntity, embeddable);
	}
	
	public HasSingleReferenceRelation(IJPAEditorFeatureProvider fp,
			Connection conn) {
		super(fp, conn);
	}

	@Override
	public HasReferenceType getReferenceType() {
		return HasReferenceType.SINGLE;
	}

}