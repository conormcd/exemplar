// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006
	Conor McDermottroe.  All rights reserved.

	Redistribution and use in source and binary forms, with or without
	modification, are permitted provided that the following conditions
	are met:
	1. Redistributions of source code must retain the above copyright
	   notice, this list of conditions and the following disclaimer.
	2. Redistributions in binary form must reproduce the above copyright
	   notice, this list of conditions and the following disclaimer in the
	   documentation and/or other materials provided with the distribution.
	3. Neither the name of the author nor the names of any contributors to
	   the software may be used to endorse or promote products derived from
	   this software without specific prior written permission.

	THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
	"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
	LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
	A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
	HOLDERS OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
	SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED
	TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
	OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY
	OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
	NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
	SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
*/
package com.mcdermottroe.exemplar;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.jar.JarFile;

/** General purpose utility methods that can be used anywhere in the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Utils {
	/** Package discovery is an expensive operation so it is done once and the
		result is cached here.

		@see #findPackages()
		@see #findSubPackages(String)
	*/
	private static List allPackages = null;

	/** Private constructor to prevent instantiation of this class. */
	private Utils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Useful version of {@link #formatMessage(String, Object[])} for when
		there's only one argument.

		@param	format	The {@link MessageFormat} string to process.
		@param	arg		The {@link Object} to interpolate.
		@return			The message correctly formatted.
		@see	#formatMessage(String,Object[])
	*/
	public static String formatMessage(String format, Object arg) {
		DBC.REQUIRE(format != null);
		DBC.REQUIRE(arg != null);
		if (format == null || arg == null) {
			return null;
		}

		Object[] args = {
			arg,
		};
		return formatMessage(format, args);
	}

	/** Due to {@link MessageFormat} formats being completely lame with respect
		to parsing strings with '{' characters in it, the {@link
		MessageFormat#format(String, Object[])} method must be wrapped to
		prevent format strings containing code from being incorrectly
		formatted.  This method takes care of all of the oddities except for
		when the format contains null characters or if SubFormatPatterns are
		used.

		@param	format	The {@link MessageFormat} format string to process.
		@param	args	The array of {@link Object}s to interpolate.
		@return			The message correctly formatted.
		@see	java.text.MessageFormat
	*/
	public static String formatMessage(String format, Object[] args) {
		DBC.REQUIRE(format != null);
		DBC.REQUIRE(args != null && args.length > 0);
		if (format == null || args == null || args.length <= 0) {
			return null;
		}

		// The following are the replacements for left and right curlies in
		// several contexts.
		String beginVariableStandin =	Character.toString(
											Constants.Character.NULL
										);
		String endVariableStandin =		Character.toString(
											Constants.Character.NULL
										);
		String leftCurlyStandin = beginVariableStandin + endVariableStandin;

		// Escape all of the left curlies in the template
		String template = format;
		template = template.replaceAll(
			Constants.Regex.MESSAGE_FORMAT_VAR_START +
				Constants.Regex.MESSAGE_FORMAT_VAR_NAME +
				Constants.Regex.MESSAGE_FORMAT_VAR_END,
			beginVariableStandin +
				Constants.Regex.FIRST_BACKREFERENCE +
				endVariableStandin
		);
		template = template.replaceAll(
			Constants.Regex.MESSAGE_FORMAT_VAR_START,
			leftCurlyStandin
		);
		template = template.replaceAll(
			beginVariableStandin +
				Constants.Regex.MESSAGE_FORMAT_VAR_NAME +
				endVariableStandin,
			Constants.Character.LEFT_CURLY +
				Constants.Regex.FIRST_BACKREFERENCE +
				Constants.Character.RIGHT_CURLY
		);

		// Format the string
		String formattedMessage = MessageFormat.format(template, args);

		// Put all of the curlies back.
		formattedMessage = formattedMessage.replaceAll(
			leftCurlyStandin,
			Character.toString(Constants.Character.LEFT_CURLY)
		);

		// Remove all the trailing whitespace
		formattedMessage = formattedMessage.replaceAll(
			Constants.Regex.TRAILING_WHITESPACE,
			Constants.Regex.FIRST_BACKREFERENCE
		);

		// Now the message is formatted
		return formattedMessage;
	}

	/** For the given {@link String}, replace all XML character references with
		their corresponding Java Unicode escape string form and replace all
		other characters with their Java Unicode escape string form. This
		method is only useful when the {@link String} returned is going
		somewhere (like a .properties {@link java.util.PropertyResourceBundle}
		file) that will cause the {@link String} to be re-interpreted by Java.

		@param	s	A {@link String} from XML which must be escaped for Java's
					use.
		@return		A {@link String} with all of the character references
					resolved.
		@see		java.util.Properties
	*/
	public static String xmlStringToJavaCanonicalForm(String s) {
		DBC.REQUIRE(s != null);
		if (s == null) {
			return null;
		}

		StringBuffer returnValue = new StringBuffer(s.length());

		char[] characters = s.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if (characters[i] == Constants.Character.AMPERSAND) {
				if	(
						characters.length > i + 2 &&
						characters[i + 1] == Constants.Character.HASH
					)
				{
					int startIndex = i + 2;
					int radix = Constants.BASE_DECIMAL;
					if (characters[i + 2] == 'x') {
						startIndex++;
						radix = Constants.BASE_HEXADECIMAL;
					}

					StringBuffer potentialNumber = new StringBuffer(
						characters.length - i
					);
					int j;
					for (j = startIndex; j < characters.length; j++) {
						if (characters[j] == Constants.Character.SEMI_COLON) {
							break;
						}
						potentialNumber.append(characters[j]);
					}

					int number = -1;
					try {
						number = Integer.parseInt(
							potentialNumber.toString(),
							radix
						);
					} catch (NumberFormatException e) {
						// Ignore
					}

					if (number >= 0) {
						String numberInHex = Integer.toHexString(number);
						returnValue.append(Constants.UNICODE_ESCAPE_PREFIX);
						int numZeros =	Constants.UNICODE_ESCAPE_NUM_DIGITS -
										numberInHex.length();
						for (int k = 0; k < numZeros; k++) {
							returnValue.append(0);
						}
						returnValue.append(numberInHex);
						i = j;
					}
				} else {
					String hexValue = Integer.toHexString((int)characters[i]);
					returnValue.append(Constants.UNICODE_ESCAPE_PREFIX);
					for	(
							int j = 0;
							j < Constants.UNICODE_ESCAPE_NUM_DIGITS -
								hexValue.length();
							j++
						)
					{
						returnValue.append(0);
					}
					returnValue.append(hexValue);
				}
			} else {
				String hexValue = Integer.toHexString((int)characters[i]);
				returnValue.append(Constants.UNICODE_ESCAPE_PREFIX);
				for	(
						int j = 0;
						j < Constants.UNICODE_ESCAPE_NUM_DIGITS -
							hexValue.length();
						j++
					)
				{
					returnValue.append(0);
				}
				returnValue.append(hexValue);
			}
		}

		return returnValue.toString();
	}

	/** Transform all of the characters in a {@link String} into their relevant
		XML character references.

		@param	s	The {@link String} to transform.
		@return		A String consisting entirely of XML character references.
	*/
	public static String escapeToXMLCharRefs(String s) {
		String canonicalJavaString = xmlStringToJavaCanonicalForm(s);
		DBC.ASSERT(canonicalJavaString.length() % 6 ==0);

		StringBuffer ret = new StringBuffer();
		int n = 0;
		for (int i = 0; i < canonicalJavaString.length(); i++) {
			char curChar = canonicalJavaString.charAt(i);
			switch (n) {
				case 0:
					DBC.ASSERT(curChar == Constants.Character.BACKSLASH);
					n++;
					break;
				case 1:
					DBC.ASSERT(curChar == 'u');
					ret.append(Constants.Character.AMPERSAND);
					ret.append(Constants.Character.HASH);
					ret.append('x');
					n++;
					break;
				case 2:
				case 3:
				case 4:
					DBC.ASSERT(Character.isDigit(curChar));
					ret.append(canonicalJavaString.charAt(i));
					n++;
					break;
				case 5:
					DBC.ASSERT(Character.isDigit(curChar));
					ret.append(canonicalJavaString.charAt(i));
					ret.append(Constants.Character.SEMI_COLON);
					n = 0;
					break;
				default:
					DBC.UNREACHABLE_CODE();
			}
		}

		return ret.toString();
	}

	/** Find the available packages in the classpath. The result of this method
		call cannot change during one execution of the program and hence it is
		cached in the member {@link #allPackages} after the first call.

		@return	A list of all of the packages currently visible in the
				classpath.
		@see	ClassLoader
	*/
	public static List findPackages() {
		if (allPackages != null) {
			return new ArrayList(allPackages);
		}

		String packagePath = Constants.PACKAGE.replace(
			Constants.Character.FULL_STOP,
			Constants.Character.SLASH
		);

		List packages = new ArrayList();

		// Search through the classpath
		try {
			ClassLoader cl = Utils.class.getClassLoader();
			for	(
					Enumeration e = cl.getResources(packagePath);
					e.hasMoreElements();
				)
			{
				URL url = (URL)e.nextElement();
				if (url.toString().startsWith(Constants.URL_JAR_PREFIX)) {
					packages.addAll(readPackagesFromJar(url));
				}
			}
		} catch (IOException e) {
			// Ignore this for the moment.
			DBC.IGNORED_ERROR();
		}

		allPackages = packages;
		return packages;
	}

	/** Find all of the packages below a certain package.

		@param	packageName	The package to search below.
		@return				A {@link List} of all of the sub-packages of the
							given package, not including
							<code>packageName</code> itself.
	*/
	public static List findSubPackages(String packageName) {
		List subPackages = new ArrayList();

		List packages = findPackages();
		for (Iterator it = packages.iterator(); it.hasNext(); ) {
			String packName = (String)it.next();
			if	(
					packName.startsWith(packageName) &&
					!packName.equals(packageName)
				)
			{
				subPackages.add(packName);
			}
		}

		return subPackages;
	}

	/** Given a {@link URL} of a JAR file, read all of the packages contained
		within it.

		@param	url	The {@link URL} of the JAR file to read from.
		@return		A list of packages contained within the JAR file
	*/
	public static List readPackagesFromJar(URL url) {
		List packages = new ArrayList();

		if (!url.toString().startsWith(Constants.URL_JAR_PREFIX)) {
			return packages;
		}

		String jarFilePath = url.toString();
		jarFilePath = jarFilePath.substring(
			Constants.URL_JAR_PREFIX.length(),
			jarFilePath.indexOf((int)Constants.Character.EXCLAMATION_MARK)
		);
		JarFile jar = null;
		try {
			jar = new JarFile(new File(jarFilePath));
		} catch (IOException e) {
			// Ignore
		}

		if (jar != null) {
			for (Enumeration e = jar.entries(); e.hasMoreElements(); ) {
				String entry = e.nextElement().toString();
				char lastChar = entry.charAt(entry.length() - 1);
				if (lastChar == Constants.Character.SLASH) {
					entry = entry.substring(0, entry.length() - 1);
					if (!entry.equals(Constants.JAR_METAINF_DIR)) {
						entry = entry.replace(
							Constants.Character.SLASH,
							Constants.Character.FULL_STOP
						);
						packages.add(entry);
					}
				}
			}
		}

		return packages;
	}

	/** Safely test deep equality on two {@link Object}s. This is quite useful
		for overriding {@link Object#equals(Object)}.

		@param	oa	The first {@link Object}
		@param	ob	The {@link Object} to compare to the first {@link Object}.
		@return		True if the {@link Object}s are equal according to
					<code>oa</code>'s version of {@link Object#equals(Object)}
					or if both are null, false otherwise.
	*/
	public static boolean areDeeplyEqual(Object oa, Object ob) {
		if (oa == null) {
			return ob == null;
		} else {
			return oa.equals(ob);
		}
	}

	/** Test two arrays of {@link Object}s and return true if all elements in
		one array deeply equal their corresponding element in the other array.

		@param	oA	The array of {@link Object}s A.
		@param	oB	The array of {@link Object}s B.
		@return		True if all elements A<sub>i</sub> deeply equal their
					corresponding element B<sub>i</sub>.
		@see	#areDeeplyEqual(Object, Object)
	*/
	public static boolean areAllDeeplyEqual(Object[] oA, Object[] oB) {
		if (oA.length == oB.length) {
			for (int i = 0; i < oA.length; i++) {
				if (!areDeeplyEqual(oA[i], oB[i])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/** Generate a hash code value based on an array of {@link Object}s, which
		allows for most versions of {@link Object#hashCode()} to simply
		generate an array of the relevant objects and then hand off to this
		method.

		@param	objects	The array of {@link Object}s to create the hash code
						from.
		@return			A hash code computed from all of the values in the
						supplied array.
		@see	Object#hashCode()
	*/
	public static int genericHashCode(Object[] objects) {
		DBC.REQUIRE(objects != null);
		if (objects == null) {
			return 0;
		}
		int hashCode = 0;
		for (int i = 0; i < objects.length; i++) {
			if (objects[i] != null) {
				hashCode += objects[i].hashCode();
			} else {
				hashCode += 0;
			}
			hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		}
		return hashCode;
	}
}
