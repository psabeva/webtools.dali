package org.eclipse.jpt.eclipselink.core.internal.context.orm;

import java.util.List;
import org.eclipse.jpt.core.context.orm.OrmPersistentAttribute;
import org.eclipse.jpt.core.internal.context.orm.GenericOrmOneToOneMapping;
import org.eclipse.jpt.core.resource.orm.AbstractXmlTypeMapping;
import org.eclipse.jpt.eclipselink.core.context.EclipseLinkOneToOneMapping;
import org.eclipse.jpt.eclipselink.core.context.JoinFetch;
import org.eclipse.jpt.eclipselink.core.context.PrivateOwned;
import org.eclipse.jpt.eclipselink.core.resource.orm.EclipseLinkOrmFactory;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlJoinFetch;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlOneToOne;
import org.eclipse.jpt.eclipselink.core.resource.orm.XmlPrivateOwned;
import org.eclipse.wst.validation.internal.provisional.core.IMessage;

public class EclipseLinkOrmOneToOneMapping extends GenericOrmOneToOneMapping
	implements EclipseLinkOneToOneMapping
{
	protected EclipseLinkOrmPrivateOwned privateOwned;
	
	protected EclipseLinkOrmJoinFetch joinFetch;
	
	
	public EclipseLinkOrmOneToOneMapping(OrmPersistentAttribute parent) {
		super(parent);
		this.privateOwned = new EclipseLinkOrmPrivateOwned(this);
		this.joinFetch = new EclipseLinkOrmJoinFetch(this);
	}
	
	
	public PrivateOwned getPrivateOwned() {
		return this.privateOwned;
	}
	
	public JoinFetch getJoinFetch() {
		return this.joinFetch;
	}
	
	
	// **************** resource-context interaction ***************************
	
	@Override
	public XmlOneToOne addToResourceModel(AbstractXmlTypeMapping typeMapping) {
		XmlOneToOne oneToOne = EclipseLinkOrmFactory.eINSTANCE.createXmlOneToOne();
		getPersistentAttribute().initialize(oneToOne);
		typeMapping.getAttributes().getOneToOnes().add(oneToOne);
		return oneToOne;
	}
	
	@Override
	public void initialize(org.eclipse.jpt.core.resource.orm.XmlOneToOne oneToOne) {
		super.initialize(oneToOne);	
		this.privateOwned.initialize((XmlPrivateOwned) oneToOne);
		this.joinFetch.initialize((XmlJoinFetch) oneToOne);
	}
	
	@Override
	public void update(org.eclipse.jpt.core.resource.orm.XmlOneToOne oneToOne) {
		super.update(oneToOne);
		this.privateOwned.update((XmlPrivateOwned) oneToOne);
		this.joinFetch.update((XmlJoinFetch) oneToOne);
	}
	
	
	// **************** validation **************************************
	
	@Override
	public void validate(List<IMessage> messages) {
		super.validate(messages);
		// TODO - private owned, join fetch validation
	}
}