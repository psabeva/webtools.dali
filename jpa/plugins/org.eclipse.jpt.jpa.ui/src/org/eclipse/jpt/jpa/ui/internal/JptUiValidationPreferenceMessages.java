/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.jpa.ui.internal;

import org.eclipse.osgi.util.NLS;

//Content is organized based on the categories and entries in each categories are listed alphabetically.
//If adding a new entry, please add it to the corresponding category at the right place.

public class JptUiValidationPreferenceMessages {
	
	// Project Category
	public static String PROJECT_LEVEL_CATEGORY;
	
	public static String NO_JPA_PROJECT;
	public static String PERSISTENCE_MULTIPLE_PERSISTENCE_UNITS;
	public static String PERSISTENCE_NO_PERSISTENCE_UNIT;
	public static String PERSISTENCE_XML_INVALID_CONTENT;
	public static String PERSISTENCE_XML_UNSUPPORTED_CONTENT; //3.0 M7
	public static String PROJECT_INACTIVE_CONNECTION;
	public static String PROJECT_INVALID_CONNECTION;
	public static String PROJECT_INVALID_LIBRARY_PROVIDER; // 3.0 M7
	public static String PROJECT_MULTIPLE_PERSISTENCE_XML;
	public static String PROJECT_NO_CONNECTION;
	public static String PROJECT_NO_PERSISTENCE_XML;
	public static String XML_VERSION_NOT_LATEST;

	// Persistence Unit Category
	public static String PERSISTENCE_UNIT_LEVEL_CATEGORY;
	
	public static String MAPPING_FILE_EXTRANEOUS_PERSISTENCE_UNIT_METADATA;
	public static String PERSISTENT_TYPE_DUPLICATE_CLASS; //3.0 M7
	public static String PERSISTENCE_UNIT_DUPLICATE_CLASS;
	public static String PERSISTENCE_UNIT_DUPLICATE_JAR_FILE;
	public static String PERSISTENCE_UNIT_DUPLICATE_MAPPING_FILE;
	public static String PERSISTENCE_UNIT_INVALID_CLASS;
	public static String PERSISTENCE_UNIT_INVALID_MAPPING_FILE;
	public static String PERSISTENCE_UNIT_JAR_FILE_DEPLOYMENT_PATH_WARNING;
	public static String PERSISTENCE_UNIT_NONEXISTENT_CLASS;
	public static String PERSISTENCE_UNIT_NONEXISTENT_JAR_FILE;
	public static String PERSISTENCE_UNIT_NONEXISTENT_MAPPING_FILE;
	public static String PERSISTENCE_UNIT_REDUNDANT_CLASS;
	public static String PERSISTENCE_UNIT_UNSPECIFIED_CLASS;
	public static String PERSISTENCE_UNIT_UNSPECIFIED_JAR_FILE;
	public static String PERSISTENCE_UNIT_UNSPECIFIED_MAPPING_FILE;
	public static String PERSISTENCE_UNIT_UNSUPPORTED_MAPPING_FILE_CONTENT;

	// Type Category
	public static String TYPE_LEVEL_CATEGORY;

	public static String ENTITY_NAME_DUPLICATED; //3.0 M7
	public static String ENTITY_NAME_MISSING; //3.0 M7
	public static String ENTITY_NO_PK;
	public static String ENTITY_NON_ROOT_ID_ATTRIBUTE_SPECIFIED; //3.0 M7
	public static String ENTITY_NON_ROOT_ID_CLASS_SPECIFIED; //3.0 M7
	public static String PERSISTENT_TYPE_ANNOTATED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT;
	public static String PERSISTENT_TYPE_MAPPED_BUT_NOT_INCLUDED_IN_PERSISTENCE_UNIT;
	public static String PERSISTENT_TYPE_UNRESOLVED_CLASS;
	public static String PERSISTENT_TYPE_UNSPECIFIED_CLASS;
	public static String TYPE_MAPPING_CLASS_MISSING_NO_ARG_CONSTRUCTOR;
	public static String TYPE_MAPPING_CLASS_PRIVATE_NO_ARG_CONSTRUCTOR;
	public static String TYPE_MAPPING_FINAL_CLASS;
	public static String TYPE_MAPPING_ID_AND_EMBEDDED_ID_BOTH_USED;
	public static String TYPE_MAPPING_ID_CLASS_AND_EMBEDDED_ID_BOTH_USED; 	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_DOES_NOT_EXIST; //3.2
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_MAPPING_DUPLICATE_MATCH;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_MAPPING_NO_MATCH;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_NO_MATCH; //3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_NOT_PRIMARY_KEY;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_ATTRIBUTE_TYPE_DOES_NOT_AGREE;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_MISSING_EQUALS_METHOD; //3.2
	public static String TYPE_MAPPING_ID_CLASS_MISSING_HASHCODE_METHOD; //3.2
	public static String TYPE_MAPPING_ID_CLASS_MISSING_NO_ARG_CONSTRUCTOR; //3.2
	public static String TYPE_MAPPING_ID_CLASS_MISSING_PUBLIC_NO_ARG_CONSTRUCTOR; //3.2
	public static String TYPE_MAPPING_ID_CLASS_NAME_EMPTY; //3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_NOT_IMPLEMENT_SERIALIZABLE; //3.2
	public static String TYPE_MAPPING_ID_CLASS_NOT_PUBLIC; //3.2
	public static String TYPE_MAPPING_ID_CLASS_NOT_EXIST; //3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_PROPERTY_METHOD_NOT_PUBLIC; //3.2
	public static String TYPE_MAPPING_ID_CLASS_REDEFINED;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_REQUIRED;	//3.0 M7
	public static String TYPE_MAPPING_ID_CLASS_WITH_MAPS_ID;	//3.0 M7
	public static String TYPE_MAPPING_MAPS_ID_ATTRIBUTE_TYPE_DOES_NOT_AGREE;	//3.0 M7
	public static String TYPE_MAPPING_MEMBER_CLASS;
	public static String TYPE_MAPPING_MULTIPLE_EMBEDDED_ID;	//3.0 M7
	public static String TYPE_MAPPING_PK_REDEFINED_ID_ATTRIBUTE;	//3.0 M7
	public static String TYPE_MAPPING_PK_REDEFINED_ID_CLASS;	//3.0 M7

	// Attribute Category
	public static String ATTRIBUTE_LEVEL_CATEGORY;
	
	public static String ELEMENT_COLLECTION_TARGET_CLASS_DOES_NOT_EXIST;// 3.0 M7
	public static String ELEMENT_COLLECTION_TARGET_CLASS_MUST_BE_EMBEDDABLE_OR_BASIC_TYPE;
	public static String ELEMENT_COLLECTION_TARGET_CLASS_NOT_DEFINED;
	public static String ELEMENT_COLLECTION_CONTAINS_EMBEDDABLE_WITH_ELEMENT_COLLECTION_MAPPING;
	public static String ELEMENT_COLLECTION_CONTAINS_EMBEDDABLE_WITH_PROHIBITED_RELATIONSHIP_MAPPING;
	public static String EMBEDDED_ID_CLASS_SHOULD_BE_PUBLIC;
	public static String EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_EQUALS_HASHCODE;
	public static String EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_NO_ARG_CONSTRUCTOR;
	public static String EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_SERIALIZABLE;
	public static String EMBEDDED_ID_CLASS_SHOULD_NOT_CONTAIN_RELATIONSHIP_MAPPINGS;
	public static String EMBEDDED_ID_MAPPING_MAPPED_BY_RELATIONSHIP_AND_ATTRIBUTE_OVERRIDES_SPECIFIED; //3.0 M7
	public static String ID_MAPPING_MAPPED_BY_RELATIONSHIP_AND_COLUMN_SPECIFIED; //3.0 M7
	public static String MAPS_ID_VALUE_INVALID;
	public static String MAPS_ID_VALUE_NOT_RESOLVED;
	public static String MAPS_ID_VALUE_NOT_SPECIFIED;
	public static String MAPPING_INVALID_MAPPED_BY;
	public static String MAPPING_MAPPED_BY_ON_BOTH_SIDES;
	public static String MAPPING_MAPPED_BY_WITH_JOIN_TABLE;
	public static String MAPPING_UNRESOLVED_MAPPED_BY;
	public static String ORDER_COLUMN_AND_ORDER_BY_BOTH_SPECIFIED;
	public static String PERSISTENT_ATTRIBUTE_ELEMENT_COLLECTION_INVALID_VALUE_TYPE; //3.0 M7
	public static String PERSISTENT_ATTRIBUTE_FINAL_FIELD;
	public static String PERSISTENT_ATTRIBUTE_FINAL_GETTER;
	public static String PERSISTENT_ATTRIBUTE_INHERITED_ATTRIBUTES_NOT_SUPPORTED;
	public static String PERSISTENT_ATTRIBUTE_INVALID_MAPPING;
	public static String PERSISTENT_ATTRIBUTE_INVALID_TEMPORAL_MAPPING_TYPE; //3.0 M7
	public static String PERSISTENT_ATTRIBUTE_INVALID_TEMPORAL_MAP_KEY_TYPE;
	public static String PERSISTENT_ATTRIBUTE_INVALID_VERSION_MAPPING_TYPE; //3.0 M7
	public static String PERSISTENT_ATTRIBUTE_PUBLIC_FIELD;
	public static String PERSISTENT_ATTRIBUTE_UNRESOLVED_NAME;
	public static String PERSISTENT_ATTRIBUTE_UNSPECIFIED_NAME;
	public static String TARGET_ENTITY_IS_NOT_AN_ENTITY;
	public static String TARGET_ENTITY_NOT_DEFINED;
	public static String TARGET_ENTITY_NOT_EXIST;
	public static String TARGET_NOT_AN_EMBEDDABLE; //3.0 M7
	public static String MAP_KEY_CLASS_NOT_DEFINED;
	public static String MAP_KEY_CLASS_NOT_EXIST;
	public static String MAP_KEY_CLASS_MUST_BE_ENTITY_EMBEDDABLE_OR_BASIC_TYPE;
	public static String ATTRIBUTE_TYPE_IS_NOT_SUPPORTED_COLLECTION_TYPE;

	// ---------- Implied Attributes ------------
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_TARGET_CLASS_DOES_NOT_EXIST;
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_TARGET_CLASS_MUST_BE_EMBEDDABLE_OR_BASIC_TYPE;
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_TARGET_CLASS_NOT_DEFINED;
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_CONTAINS_EMBEDDABLE_WITH_ELEMENT_COLLECTION_MAPPING;
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_CONTAINS_EMBEDDABLE_WITH_PROHIBITED_RELATIONSHIP_MAPPING;
	public static String VIRTUAL_ATTRIBUTE_TARGET_ENTITY_IS_NOT_AN_ENTITY;
	public static String VIRTUAL_ATTRIBUTE_TARGET_ENTITY_NOT_DEFINED;
	public static String VIRTUAL_ATTRIBUTE_TARGET_NOT_AN_EMBEDDABLE;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_CLASS_NOT_DEFINED;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_CLASS_MUST_BE_ENTITY_EMBEDDABLE_OR_BASIC_TYPE;
	public static String VIRTUAL_ATTRIBUTE_INVALID_TEMPORAL_MAP_KEY_TYPE;
	public static String VIRTUAL_ATTRIBUTE_ELEMENT_COLLECTION_INVALID_VALUE_TYPE;
	public static String VIRTUAL_ATTRIBUTE_INVALID_TEMPORAL_MAPPING_TYPE;
	public static String VIRTUAL_ATTRIBUTE_ATTRIBUTE_TYPE_IS_NOT_SUPPORTED_COLLECTION_TYPE;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_NO_ARG_CONSTRUCTOR;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_EQUALS_HASHCODE;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_CLASS_SHOULD_NOT_CONTAIN_RELATIONSHIP_MAPPINGS;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_CLASS_SHOULD_IMPLEMENT_SERIALIZABLE;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_CLASS_SHOULD_BE_PUBLIC;
	public static String VIRTUAL_ATTRIBUTE_EMBEDDED_ID_MAPPING_MAPPED_BY_RELATIONSHIP_AND_ATTRIBUTE_OVERRIDES_SPECIFIED;
	

	// Database Category
	public static String DATABASE_CATEGORY;
	public static String IMPLIED_ATTRIBUTE_LEVEL_CATEGORY;
	
		// *********** Table Category ***********
	public static String TABLE_CATEGORY;	
	
	public static String COLLECTION_TABLE_UNRESOLVED_CATALOG;
	public static String COLLECTION_TABLE_UNRESOLVED_NAME;
	public static String COLLECTION_TABLE_UNRESOLVED_SCHEMA;
	public static String JOIN_TABLE_UNRESOLVED_CATALOG;
	public static String JOIN_TABLE_UNRESOLVED_NAME;
	public static String JOIN_TABLE_UNRESOLVED_SCHEMA;
	public static String SECONDARY_TABLE_UNRESOLVED_CATALOG;
	public static String SECONDARY_TABLE_UNRESOLVED_NAME;
	public static String SECONDARY_TABLE_UNRESOLVED_SCHEMA;
	public static String TABLE_UNRESOLVED_CATALOG;
	public static String TABLE_UNRESOLVED_NAME;
	public static String TABLE_UNRESOLVED_SCHEMA;
		// ---------- Implied Attributes ------------
	public static String VIRTUAL_ATTRIBUTE_COLLECTION_TABLE_UNRESOLVED_CATALOG;
	public static String VIRTUAL_ATTRIBUTE_COLLECTION_TABLE_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_COLLECTION_TABLE_UNRESOLVED_SCHEMA;
	public static String VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_CATALOG;
	public static String VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_JOIN_TABLE_UNRESOLVED_SCHEMA;

	// *********** Column Category ***********
	public static String COLUMN_CATEGORY;
	
	public static String COLUMN_TABLE_NOT_VALID;
	public static String COLUMN_UNRESOLVED_NAME;
	public static String COLUMN_UNRESOLVED_TABLE;
	public static String INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String INVERSE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String JOIN_COLUMN_TABLE_NOT_VALID;
	public static String JOIN_COLUMN_UNRESOLVED_NAME;
	public static String JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String MAP_KEY_COLUMN_TABLE_NOT_VALID;
	public static String MAP_KEY_COLUMN_UNRESOLVED_NAME;  //3.0 M7
	public static String ORDER_COLUMN_UNRESOLVED_NAME;
	public static String PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String MAP_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String MAP_KEY_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String MAP_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String MAP_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String MAP_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	// ---------- Implied Attributes ------------
	public static String VIRTUAL_ATTRIBUTE_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_INVERSE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_ATTRIBUTE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_COLUMN_UNRESOLVED_NAME; //3.0 M7
	public static String VIRTUAL_ATTRIBUTE_ORDER_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_SECONDARY_TABLE_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_PRIMARY_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_MAP_KEY_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_MAP_KEY_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	
		// *********** Overrides Category ***********
	public static String OVERRIDES_CATEGORY;
	
	public static String ASSOCIATION_OVERRIDE_INVALID_NAME;
	public static String ATTRIBUTE_OVERRIDE_INVALID_NAME;
	public static String ATTRIBUTE_OVERRIDE_INVALID_TYPE; //3.0 M7
	public static String ATTRIBUTE_OVERRIDE_MAPPED_BY_RELATIONSHIP_AND_SPECIFIED; //3.0 M7
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVALID_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_INVERSE_JOIN_COLUMNS;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_INVERSE_JOIN_COLUMNS;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_REFERENCED_COLUMN_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_TABLE_UNRESOLVED_CATALOG;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_TABLE_UNRESOLVED_NAME;
	public static String VIRTUAL_ASSOCIATION_OVERRIDE_JOIN_TABLE_UNRESOLVED_SCHEMA;
	public static String VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_OVERRIDE_INVALID_NAME;
	public static String VIRTUAL_MAP_KEY_ATTRIBUTE_OVERRIDE_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_MAP_KEY_ATTRIBUTE_OVERRIDE_INVALID_NAME;
			// ---------- Implied Attributes ------------
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVALID_NAME;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_INVERSE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_INVERSE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_INVERSE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_NAME_MUST_BE_SPECIFIED_MULTIPLE_JOIN_COLUMNS;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_REFERENCED_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_ASSOCIATION_OVERRIDE_JOIN_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_ATTRIBUTE_OVERRIDE_INVALID_NAME;
	public static String VIRTUAL_ATTRIBUTE_ATTRIBUTE_OVERRIDE_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_ATTRIBUTE_OVERRIDE_COLUMN_TABLE_NOT_VALID;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_ATTRIBUTE_OVERRIDE_INVALID_NAME;
	public static String VIRTUAL_ATTRIBUTE_MAP_KEY_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME; //3.0 M7
	public static String VIRTUAL_MAP_KEY_ATTRIBUTE_OVERRIDE_COLUMN_UNRESOLVED_NAME; //3.0 M7

	// Inheritance Category
	public static String INHERITANCE_CATEGORY;
	
	public static String DISCRIMINATOR_COLUMN_UNRESOLVED_NAME;
	public static String ENTITY_ABSTRACT_DISCRIMINATOR_VALUE_DEFINED;
	public static String ENTITY_ABSTRACT_TABLE_PER_CLASS_DEFINES_TABLE;
	public static String ENTITY_NON_ROOT_DISCRIMINATOR_COLUMN_DEFINED;
	public static String ENTITY_SINGLE_TABLE_DESCENDANT_DEFINES_TABLE;
	public static String ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_COLUMN_DEFINED;
	public static String ENTITY_TABLE_PER_CLASS_DISCRIMINATOR_VALUE_DEFINED;
	public static String ENTITY_TABLE_PER_CLASS_NOT_PORTABLE_ON_PLATFORM;
	public static String ENTITY_TABLE_PER_CLASS_NOT_SUPPORTED_ON_PLATFORM;

	// Queries and Generators Category
	public static String QUERIES_GENERATORS_CATEGORY;
	
	public static String GENERATOR_DUPLICATE_NAME;
	public static String GENERATOR_NAME_UNDEFINED; // 3.0 M7
	public static String ID_MAPPING_UNRESOLVED_GENERATOR_NAME;
	public static String JPQL_QUERY_VALIDATION; //3.0 M7
	public static String QUERY_DUPLICATE_NAME;
	public static String QUERY_NAME_UNDEFINED; // 3.0 M7
	public static String QUERY_STATEMENT_UNDEFINED; // 3.0 M7	
	
	private static final String BUNDLE_NAME = "jpt_ui_validation_preferences"; //$NON-NLS-1$
	private static final Class<?> BUNDLE_CLASS = JptUiValidationPreferenceMessages.class;

	static {
		NLS.initializeMessages(BUNDLE_NAME, BUNDLE_CLASS);
	}

	private JptUiValidationPreferenceMessages() {
		throw new UnsupportedOperationException();
	}


}
