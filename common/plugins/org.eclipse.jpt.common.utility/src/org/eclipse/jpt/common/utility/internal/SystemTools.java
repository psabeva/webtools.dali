/*******************************************************************************
 * Copyright (c) 2009, 2012 Oracle. All rights reserved.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0, which accompanies this distribution
 * and is available at http://www.eclipse.org/legal/epl-v10.html.
 * 
 * Contributors:
 *     Oracle - initial API and implementation
 ******************************************************************************/
package org.eclipse.jpt.common.utility.internal;

/**
 * Various system utility methods.
 * @see System#getProperty(String)
 */
@SuppressWarnings("nls")
public final class SystemTools {

	// ********** JVM **********

	/**
	 * Return whether the current JVM is IBM's.
	 */
	public static boolean jvmIsIBM() {
		return jvmIs("IBM");
	}

	/**
	 * Return whether the current JVM is Oracle's (or Sun's).
	 */
	public static boolean jvmIsOracle() {
		return jvmIsSun();
	}

	/**
	 * Return whether the current JVM is Sun's (or Oracle's).
	 */
	public static boolean jvmIsSun() {
		return jvmIsStrictlySun() || jvmIsStrictlyOracle();
	}

	/**
	 * Return whether the current JVM is <em>strictly</em> Oracle's
	 * (as opposed to Sun's).
	 */
	public static boolean jvmIsStrictlyOracle() {
		return jvmIs("Oracle");
	}

	/**
	 * Return whether the current JVM is <em>strictly</em> Sun's
	 * (as opposed to Oracle's).
	 */
	public static boolean jvmIsStrictlySun() {
		return jvmIs("Sun");
	}

	private static boolean jvmIs(String jvmVendorName) {
		return System.getProperty("java.vendor").startsWith(jvmVendorName);
	}


	// ********** Java specification version **********

	/**
	 * Return whether the Java specification version of the current JVM
	 * is greater than the specified version (e.g. <code>"1.5"</code>).
	 * <p>
	 * This is useful for a test that should fail if it encounters a newer
	 * JVM than what the current code supports, serving as a reminder to the
	 * developers. :-)
	 */
	public static boolean javaSpecificationVersionIsGreaterThan(String version) {
		return VersionComparator.INTEGER_VERSION_COMPARATOR.compare(javaSpecificationVersion(), version) > 0;
	}

	/**
	 * Return whether the Java specification version of the current JVM
	 * is less than or equal to the specified version (e.g. <code>"1.5"</code>).
	 */
	public static boolean javaSpecificationVersionIsLessThanOrEqualTo(String version) {
		return ! javaSpecificationVersionIsGreaterThan(version);
	}

	/**
	 * Return whether the Java specification version of the current JVM
	 * is less than the specified version (e.g. <code>"1.5"</code>).
	 */
	public static boolean javaSpecificationVersionIsLessThan(String version) {
		return VersionComparator.INTEGER_VERSION_COMPARATOR.compare(javaSpecificationVersion(), version) < 0;
	}

	/**
	 * Return whether the Java specification version of the current JVM
	 * is greater than or equal to the specified version (e.g. <code>"1.5"</code>).
	 */
	public static boolean javaSpecificationVersionIsGreaterThanOrEqualTo(String version) {
		return ! javaSpecificationVersionIsLessThan(version);
	}

	/**
	 * Return the Java specification version of the current JVM.
	 */
	public static String javaSpecificationVersion() {
		return System.getProperty("java.specification.version");
	}


	// ********** file encoding **********

	/**
	 * Return whether the current file encoding is Microsoft Windows
	 * (i.e. "Cp1252").
	 */
	public static boolean fileEncodingIsWindows() {
		return fileEncoding().equals("Cp1252");
	}

	/**
	 * Return whether the current file encoding is UTF-8.
	 */
	public static boolean fileEncodingIsUTF8() {
		return fileEncoding().equals("UTF-8");
	}

	private static String fileEncoding() {
		return System.getProperty("file.encoding");
	}


	// ********** O/S **********

	/**
	 * Return whether the current operating system is Microsoft Windows.
	 */
	public static boolean osIsWindows() {
		return osIs("Windows");
	}

	/**
	 * Return whether the current operating system is Linux.
	 */
	public static boolean osIsLinux() {
		return osIs("Linux");
	}

	/**
	 * Return whether the current operating system is Mac OS X.
	 */
	public static boolean osIsMac() {
		return osIs("Mac");
	}

	private static boolean osIs(String osName) {
		return System.getProperty("os.name").indexOf(osName) != -1;
	}


	// ********** constructor **********

	/**
	 * Suppress default constructor, ensuring non-instantiability.
	 */
	private SystemTools() {
		super();
		throw new UnsupportedOperationException();
	}
}
