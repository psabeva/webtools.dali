/*******************************************************************************
 * Copyright (c) 2006, 2009 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.runtime.content.IContentType;
import org.eclipse.jpt.core.context.MappingFileDefinition;
import org.eclipse.jpt.core.context.java.JavaAttributeMappingProvider;
import org.eclipse.jpt.core.context.java.JavaPersistentAttribute;
import org.eclipse.jpt.core.context.java.JavaPersistentType;
import org.eclipse.jpt.core.context.java.JavaTypeMapping;
import org.eclipse.jpt.core.utility.jdt.AnnotationEditFormatter;
import org.eclipse.jpt.db.ConnectionProfileFactory;
import org.eclipse.jpt.db.DatabaseFinder;

/**
 * This interface is to be implemented by a JPA vendor to provide extensions to 
 * the core JPA model. The core JPA model will provide functionality for JPA
 * spec annotations in Java, <code>persistence.xml</code> and mapping
 * (<code>orm.xml</code>) files.
 * The <code>org.eclipse.jpt.core.generic</code> extension supplies 
 * resource models for those file types. As another vendor option you 
 * will have to supply those resource models as well or different ones 
 * as necessary. In the extension point you actually provide a
 * {@link JpaPlatformFactory} that will build the JPA platform.
 * <p>
 * Provisional API: This interface is part of an interim API that is still
 * under development and expected to change significantly before reaching
 * stability. It is available at this early stage to solicit feedback from
 * pioneering adopters on the understanding that any code that uses this API
 * will almost certainly be broken (repeatedly) as the API evolves.
 * <p>
 * See the <code>org.eclipse.jpt.core.jpaPlatforms</code> extension point.
 * @see JpaPlatformFactory
 * @see JpaPlatformProvider
 */
public interface JpaPlatform
{
	/**
	 * Get the ID for this platform
	 */
	String getId();

	// ********** factory **********

	/**
	 * Return a factory responsible for creating core (e.g. JpaProject), resource
	 * (e.g. PersistenceResource), and context (e.g. PersistenceUnit) model
	 * objects
	 */
	JpaFactory getJpaFactory();


	// ********** JPA files **********

	/**
	 * Return a JPA file corresponding to the specified Eclipse file.
	 * Return null if the file's content is unsupported.
	 */
	JpaFile buildJpaFile(JpaProject jpaProject, IFile file);


	// ********** Java annotations **********

	/**
	 * Return an annotation provider responsible for determining what Java
	 * annotations are supported and constructing java resource model objects.
	 */
	JpaAnnotationProvider getAnnotationProvider();

	/**
	 * Return a formatter that can clean up the Java annotations added to source
	 * code.
	 */
	AnnotationEditFormatter getAnnotationEditFormatter();


	// ********** Java type/attribute mappings **********
	
	/**
	 * Build a Java type mapping for the specified key and persistent type.
	 * Use identity when comparing keys; so clients must use the same key
	 * constants as the providers.
	 * Throw an IllegalArgumentException if the key is not supported by the
	 * platform.
	 */
	JavaTypeMapping buildJavaTypeMappingFromMappingKey(String key, JavaPersistentType type);
	
	/**
	 * Build a Java type mapping for the specified annotation and persistent
	 * type. Use identity when comparing annotation names; so clients must
	 * use the same name constants as the providers.
	 * Throw an IllegalArgumentException if the mapping annotation is not
	 * supported by the platform.
	 */
	JavaTypeMapping buildJavaTypeMappingFromAnnotation(String annotationName, JavaPersistentType type);
	
	/**
	 * Return a {@link JavaAttributeMappingProvider} that describes the interpretation of the attribute
	 * as it exists, ignoring all annotations.
	 * This may not be null (@see {@link NullDefaultJavaAttributeMappingProvider},) else
	 * an {@link IllegalStateException} is thrown.
	 * 
	 * @param attribute The persistent attribute to analyze
	 * @return The mapping provider describing the unannotated state of the attribute
	 */
	JavaAttributeMappingProvider getDefaultJavaAttributeMappingProvider(JavaPersistentAttribute attribute);
	
	/**
	 * Return a {@link JavaAttributeMappingProvider} that describes the interpretation of the attribute
	 * as it exists, complete with annotations.  It is assumed that the attribute's default mapping
	 * has already been determined.
	 * This may be null.
	 * 
	 * @param attribute The persistent attribute to analyze
	 * @return The mapping provider describing the annotated state of the attribute
	 */
	JavaAttributeMappingProvider getSpecifiedJavaAttributeMappingProvider(JavaPersistentAttribute attribute);
	
	/**
	 * Return a {@link JavaAttributeMappingProvider} for the given mapping key.
	 * Throw an {@link IllegalArgumentException} if the key is not supported by the platform.
	 */
	JavaAttributeMappingProvider getSpecifiedJavaAttributeMappingProvider(String mappingKey);
	
	
	// ********** Mapping Files **********
	
	/**
	 * Return a {@link MappingFileDefinition} to describe the context model for a file of the given
	 * content type.
	 * Thrown an {@link IllegalArgumentException} if the content type is not supported by the platform.
	 * 
	 * @param contentType The content type of a potential mapping file
	 * @return The mapping file definition that can be used to describe the context model of such
	 * a file
	 */
	MappingFileDefinition getMappingFileDefinition(IContentType contentType);
	
	
	// ********** database **********

	/**
	 * Return a connection repository that can be used to query the database
	 * about database metadata.
	 */
	ConnectionProfileFactory getConnectionProfileFactory();

	/**
	 * Return an entity generator database annotation name builder, which is
	 * used by Entity Generation to determine whether and how the entity generator
	 * prints the names of various database objects.
	 */
	EntityGeneratorDatabaseAnnotationNameBuilder getEntityGeneratorDatabaseAnnotationNameBuilder();

	/**
	 * Return a finder that can be used to look up various objects (schemata,
	 * tables, columns, etc.) on the database, respecting the platform's and
	 * database's case-sensitivity.
	 */
	DatabaseFinder getDatabaseFinder();

	
	// ********** platform variation **********
	/**
	 * Return a platform variation that is used to determine differences platforms and/or JPA specification versions
	 */
	JpaPlatformVariation getJpaVariation();
}
