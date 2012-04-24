/*******************************************************************************
 * Copyright (c) 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/

package org.eclipse.jpt.jpa.eclipselink.ui.internal.entity.data.model;

import java.util.Arrays;
import java.util.List;

import org.eclipse.jpt.jpa.ui.details.MappingUiDefinition;

public class DynamicEntityField {

	private static final String DOT = "."; //$NON-NLS-1$
	private static final char BRACKET_SQUARE = '[';
	private static final char BRACKET_ANGULAR = '<';
	private static final String PACKAGE_JAVA_LANG = "java.lang."; //$NON-NLS-1$
	private MappingUiDefinition<?,?> mappingType;
	private String name = ""; //$NON-NLS-1$
	private String type = ""; //$NON-NLS-1$
	private String fqnTypeName = ""; //$NON-NLS-1$
	private boolean key = false;

	private final static String[] PK_TYPES = {"int", "long", "short", "char", "boolean", "byte", "double", "float",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$ //$NON-NLS-7$ //$NON-NLS-8$
		"java.lang.String", "java.sql.Date", "java.util.Date", "java.lang.Integer", "java.lang.Long", "java.lang.Short", //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		"java.lang.Character", "java.lang.Boolean", "java.lang.Byte", "java.lang.Double", "java.lang.Float"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$

	private final static String[] PK_TYPES_SHORT = { "String", "Integer", "Long", "Short", "Character", "Boolean",  //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$ //$NON-NLS-5$ //$NON-NLS-6$
		 "Byte", "Double", "Float" }; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$

	private final static List<String> VALID_PK_TYPES = Arrays.asList(PK_TYPES);
	private final static List<String> VALID_PK_TYPES_SHORT = Arrays.asList(PK_TYPES_SHORT);

	/**
	 * @return the mapping type of the dynamic entity field
	 */
	public MappingUiDefinition<?,?> getMappingType() {
		return this.mappingType;
	}
	
	/**
	 * Sets the given mapping type to the dynamic entity field
	 * 
	 * @param mappingType 
	 */
	public void setMappingType(MappingUiDefinition<?,?> mappingType) {
		this.mappingType = mappingType;
	}

	/**
	 * @return the name of the dynamic entity field
	 */
	public String getName() {
		return name;
	}

	/**
	 * Sets the name of the dynamic entity field
	 * @param name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the type (as a simple name) of the dynamic entity field
	 */
	public String getType() {
		return type;
	}

	/**
	 * Sets the type (as a simple name) of the dynamic entity field
	 * 
	 * @param type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return the type (as fully qualified name) of the dynamic entity field
	 */
	public String getFqnTypeName() {
		return fqnTypeName;
	}
	
	/**
	 * Sets the fully qualified type name of the dynamic entity field
	 * 
	 * @param fqnTypeName
	 */
	public void setFqnTypeName(String fqnTypeName) {
		fqnTypeName = removeSpaces(fqnTypeName);
		String fqnBasicTypeName = getBasicFQN(fqnTypeName);
		if (fqnBasicTypeName.indexOf(DOT) == -1) {
			if (VALID_PK_TYPES_SHORT.contains(fqnBasicTypeName)) {
				this.fqnTypeName = PACKAGE_JAVA_LANG + fqnTypeName;
				setType(fqnTypeName);
			} else {
				this.fqnTypeName = fqnTypeName;
				setType(fqnTypeName);
			}			
		} else {
			this.fqnTypeName = fqnTypeName;
			setType(getSimpleName(fqnTypeName));
		}		
	}

	private String removeSpaces(String str) {
		str = str.trim();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if (!Character.isWhitespace(c)) 
				sb.append(c);
		}
		return sb.toString();
	}
	
	private String getBasicFQN(String fqn) {
		String res;
		int bsIndex = fqn.indexOf(BRACKET_SQUARE);
		int baIndex = fqn.indexOf(BRACKET_ANGULAR);
		if (bsIndex == -1) {
			if (baIndex == -1) res = fqn;
			else res = fqn.substring(0, baIndex);
		} else {
			if (baIndex == -1) res = fqn.substring(0, bsIndex);
			else res = fqn.substring(0, Math.max(bsIndex, baIndex));
		}
		return res;
	}
	
	/**
	 * For internal purpose only
	 * Convert fully qualified name to the simple one
	 * 
	 * @param fullyName
	 * @return the simple name form the fully qualified name parameter(last segment)
	 */
	private String getSimpleName(String fullyName) {
		return fullyName.substring(fullyName.lastIndexOf(DOT) + 1);
	}
	
	/**
	 * @return whether the dynamic entity field is a primary key 
	 * or part of composite primary key
	 */
	public boolean isKey() {
		return key;
	}

	/**
	 * Sets the dynamic entity field to be a primary key (or part of composite primary key)
	 * 
	 * @param key 
	 */
	public void setKey(boolean key) {
		this.key = key;
	}
		
	/**
	 * Check whether the given type of the dynamic entity field is allowed to be used as a type of primary key.
	 * @return whether the given type of the field could be used as a type of primary key
	 */
	public boolean couldTypeBePKType() {
		boolean result = false;
		result = VALID_PK_TYPES.contains(getFqnTypeName());	
		return result;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fqnTypeName == null) ? 0 : fqnTypeName.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime* result + (mappingType == null? 0 : mappingType.hashCode());
		return result;
	}

	/*
	 * Implement equals, depending from name of the dynamic entity field
	 * and its mapping and attribute types.
	 * The attribute type is presented from the fully qualified name
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final DynamicEntityField other = (DynamicEntityField) obj;
		if (fqnTypeName == null) {
			if (other.fqnTypeName != null)
				return false;
		} else if (!fqnTypeName.equals(other.fqnTypeName))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (mappingType == null) {
			if (other.mappingType != null)
				return false;
		} else if (!mappingType.equals(other.mappingType))
			return false;
		return true;
	}
}
