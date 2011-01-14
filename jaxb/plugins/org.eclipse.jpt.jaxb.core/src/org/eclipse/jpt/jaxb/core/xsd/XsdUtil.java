/*******************************************************************************
 *  Copyright (c) 2011  Oracle. All rights reserved.
 *  This program and the accompanying materials are made available under the
 *  terms of the Eclipse Public License v1.0, which accompanies this distribution
 *  and is available at http://www.eclipse.org/legal/epl-v10.html
 *  
 *  Contributors:
 *  	IBM Corporation - initial API and implementation
 *  		(copied mainly from org.eclipse.wst.xsd.contentmodel.internal.XSDImpl)
 *  	Oracle - extensions and modifications
 *******************************************************************************/
package org.eclipse.jpt.jaxb.core.xsd;

import java.io.InputStream;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.Notifier;
import org.eclipse.emf.common.notify.impl.AdapterFactoryImpl;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.jpt.jaxb.core.JptJaxbCorePlugin;
import org.eclipse.wst.common.uriresolver.internal.provisional.URIResolverPlugin;
import org.eclipse.wst.xsd.contentmodel.internal.util.XSDSchemaLocatorAdapterFactory;
import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.XSDSimpleTypeDefinition;
import org.eclipse.xsd.impl.XSDSchemaImpl;
import org.eclipse.xsd.util.XSDConstants;
import org.eclipse.xsd.util.XSDResourceImpl;
import org.eclipse.xsd.util.XSDSwitch;

/**
 * Utility class for building XSD model and its extensions for JAXB
 */
public class XsdUtil {
	
	protected static final XsdAdapterFactoryImpl adapterFactory = new XsdAdapterFactoryImpl();
	
	/**
	 * Given uri for an XML Schema document, parse the document and build
	 * corresponding EMF object.
	 */
	public static XSDSchema buildXSDModel(String uriString) {
		XSDSchema xsdSchema = null;
		
		try {
			// if XML Schema for Schema is requested, get it through schema model 
			if (uriString.endsWith("2001/XMLSchema.xsd")) {
				xsdSchema = XSDSchemaImpl.getSchemaForSchema(XSDConstants.SCHEMA_FOR_SCHEMA_URI_2001);			
			}
			else { 	
				ResourceSet resourceSet = new ResourceSetImpl();
				resourceSet.getAdapterFactories().add(new XSDSchemaLocatorAdapterFactory());
				
				URI uri = URI.createURI(uriString);   
				
				// CS : bug 113537 ensure we perform physical resolution before opening a stream for the resource
				String physicalLocation = URIResolverPlugin.createResolver().resolvePhysicalLocation("", "", uriString);       
				InputStream inputStream = resourceSet.getURIConverter().createInputStream(URI.createURI(physicalLocation));
				XSDResourceImpl resource = (XSDResourceImpl)resourceSet.createResource(URI.createURI("*.xsd"));
				resource.setURI(uri);
				resource.load(inputStream, null);         
				xsdSchema = resource.getSchema();      
			}
		}
		catch (Exception e) {
			JptJaxbCorePlugin.log(e);
		}
		return xsdSchema;
	}
	
	public static Object getAdapter(Notifier notifier) {
		return adapterFactory.adapt(notifier);
	}
	
	
	/**
	 * The Factory for the XSD adapter model. It provides a create method for each
	 * non-abstract class of the model.
	 */
	public static class XsdAdapterFactoryImpl
			extends AdapterFactoryImpl {
		
		@Override
		public Adapter createAdapter(Notifier target) {
			XSDSwitch xsdSwitch = new XSDSwitch() {
				@Override
				public Object caseXSDSchema(XSDSchema object) {
					return new XsdSchema(object);
				}
				
				@Override
				public Object caseXSDSimpleTypeDefinition(XSDSimpleTypeDefinition object) {
					return new XsdSimpleTypeDefinition(object);
				}
				
				@Override
				public Object caseXSDComplexTypeDefinition(XSDComplexTypeDefinition object) {
					return new XsdComplexTypeDefinition(object);
				}
			};
			
			Object o = xsdSwitch.doSwitch((EObject) target);
			Adapter result = null;
			if (o instanceof Adapter) {
				result = (Adapter) o;
			}
			return result;
		}
		
		public Adapter adapt(Notifier target) {
			return adapt(target, this);
		}
	}
}