// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
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
package com.mcdermottroe.exemplar.utils;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import com.mcdermottroe.exemplar.DBC;

import static com.mcdermottroe.exemplar.Constants.Character.CARRIAGE_RETURN;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.NEW_LINE;
import static com.mcdermottroe.exemplar.Constants.Character.NULL;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.Character.TAB;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.Format.UNICODE;
import static com.mcdermottroe.exemplar.Constants.UI.INDENT;

/** A collection of {@link String} handling methods.

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Strings {
	/** Private constructor to prevent instantiation of this class. */
	private Strings() {
		DBC.UNREACHABLE_CODE();
	}

	/** Due to {@link MessageFormat} formats being completely lame with respect
		to parsing strings with '{' characters in it, the {@link
		MessageFormat#format(String, Object...)} method must be wrapped to
		prevent format strings containing code from being incorrectly
		formatted.  This method takes care of all of the oddities except for
		when the format contains null characters or if SubFormatPatterns are
		used.

		@param	format	The {@link MessageFormat} format string to process.
		@param	args	The array of {@link Object}s to interpolate.
		@return			The message correctly formatted.
		@see	MessageFormat
	*/
	public static String formatMessage(String format, Object... args) {
		if (format == null) {
			return null;
		}
		if (args == null || args.length <= 0) {
			return format;
		}

		// Replace all instances of the character '{' with a null when they are
		// not immediately followed by a digit.
		StringBuilder messageFormat = new StringBuilder(format.length());
		for (int i = 0; i < format.length(); i++) {
			if (format.charAt(i) == LEFT_CURLY) {
				if	(
						i < format.length() - 1 &&
						Character.isDigit(format.charAt(i + 1))
					)
				{
					messageFormat.append(format.charAt(i));
				} else {
					messageFormat.append(NULL);
				}
			} else {
				messageFormat.append(format.charAt(i));
			}
		}

		// Format the string
		String formattedMessage = MessageFormat.format(
			messageFormat.toString(),
			args
		);

		// Translate the nulls back to left curlies.
		StringBuilder returnValue = new StringBuilder(formattedMessage);
		for (int i = 0; i < returnValue.length(); i++) {
			if (returnValue.charAt(i) == NULL) {
				returnValue.replace(i, i + 1, String.valueOf(LEFT_CURLY));
			}
		}

		// Return the string
		return returnValue.toString();
	}

	/** Convert a {@link CharSequence} to a sequence of Java Unicode escapes so
		that the resulting {@link CharSequence} is safe to put in a file and
		then read back into Java without loss of information.

		@param	s	The {@link CharSequence} to convert.
		@return		A {@link String} containing nothing but Java Unicode 
					escapes.
	*/
	public static String toCanonicalForm(CharSequence s) {
		// Pass through null CharSequences
		if (s == null) {
			return null;
		}

		StringBuilder returnValue = new StringBuilder(s.length() * 6);
		for (int i = 0; i < s.length(); i++) {
			returnValue.append(String.format(UNICODE, (int)s.charAt(i)));
		}

		return returnValue.toString();
	}

	/** Provide a method akin to Perl's join() function. This takes a number of
		{@link Object}s, interprets every one of them as a {@link String} and
		joins them with the given separator {@link String}.

	 	@param	separator	Separator to join the elements with.
		@param	objects		The set of {@link Object}s to join.
		@return				All of the elements String-joined using the
							separator.
	*/
	public static String join(CharSequence separator, Object... objects) {
		// Figure out what we're going to iterate through.
		Collection<Object> collection;
		if (objects.length == 1 && objects[0] instanceof Collection) {
			collection = (Collection<Object>)objects[0];
		} else {
			collection = new ArrayList<Object>();
			Collections.addAll(collection, objects);
		}

		StringBuilder joinedString = new StringBuilder();
		for (Object o : collection) {
			if (joinedString.length() > 0) {
				joinedString.append(separator);
			}
			joinedString.append(o);
		}
		return joinedString.toString();
	}

	/** An alias for {@link #join(CharSequence, Object...)} which allows
		the use of a character as the separator.

		@param	separator	Separator to join the elements with.
		@param	objects		The set of {@link Object}s to join.
		@return				All of the elements String-joined using the
							separator.
	*/
	public static String join(char separator, Object... objects) {
		return join(String.valueOf(separator), objects);
	}

	/** A shim to work around the fact that {@link
		Arrays#deepToString(Object[])} wants an array of {@link Object}s rather
		than a variadic list.

		@param	objects	The {@link Object}s to format as a {@link String}.
		@return			A {@link String} as formatted by {@link
						Arrays#deepToString(Object[])}.
	*/
	public static String deepToString(Object... objects) {
		return Arrays.deepToString(objects);
	}

	/** Remove space or tab characters from between the last non-space character
		and either the the trailing line endings or the end of the string,
		whichever is leftmost. This will also convert any line endings into the
		native local line ending.

		@param	string	The {@link CharSequence} to transform.
		@return			The original CharSequence with any trailing,
						non-line-ending whitespace removed.
	*/
	public static String trimTrailingSpace(CharSequence string) {
		if (string == null) {
			return null;
		}
		StringBuilder retValue = new StringBuilder(nativeLineEndings(string));
		stringBackToFront: for (int i = retValue.length() - 1; i >= 0; i--) {
			switch (retValue.charAt(i)) {
				case SPACE:
				case TAB:
					retValue.deleteCharAt(i);
					break;
				case CARRIAGE_RETURN:
				case NEW_LINE:
					// Ignore
					break;
				default:
					break stringBackToFront;
			}
		}
		return retValue.toString();
	}

	/** Convert all the line-endings in a {@link CharSequence} to the local
		idea of a line ending sequence.

		@param	input	The {@link CharSequence} to transform.
		@return			The {@link CharSequence} <code>input</code> with all
						line-endings converted to the local native line ending.
	*/
	public static String nativeLineEndings(CharSequence input) {
		if (input == null) {
			return null;
		}
		String returnValue = input.toString();
		returnValue = returnValue.replace("\r\n", EOL);
		returnValue = returnValue.replace("\n\r", EOL);
		returnValue = returnValue.replace("\r", EOL);
		returnValue = returnValue.replace("\n", EOL);
		return returnValue;
	}

	/** Indent a {@link String} by one indentation unit.

		@param	s		A {@link String} to indent.
		@return			The original {@link String} indented by one indentation
						unit.
	*/
	public static String indent(String s) {
		return indent(new StringBuilder(s)).toString();
	}

	/** Indent a {@link String} using the program-standard indentation.

		@param	s		A {@link String} to indent.
		@param	indent	The number of times to indent the {@link String}.
		@return			The original {@link String} indented <code>indent</code>
						number of times.
	*/
	public static String indent(String s, int indent) {
		return indent(new StringBuilder(s), indent).toString();
	}

	/** Indent a {@link StringBuilder} by one indentation unit.

		@param	buffer	A {@link StringBuilder} to indent.
		@return			The original {@link StringBuilder} indented by one
						indentation unit.
	*/
	public static StringBuilder indent(StringBuilder buffer) {
		return indent(buffer, 1);
	}

	/** Indent a {@link StringBuilder} using the program-standard indentation.

		@param	buffer	A {@link StringBuilder} to indent.
		@param	indent	The number of indentations to apply.
		@return			The original {@link StringBuilder} indented.
	*/
	public static StringBuilder indent(StringBuilder buffer, int indent) {
		for (int i = 0; i < indent; i++) {
			buffer.insert(0, INDENT);
		}
		return buffer;
	}

	/** Wrap text to fit into a given paragraph or column width. If it is not
		possible to wrap the text as desired (e.g. there is a word which is too
		long to wrap) then the text will overflow the right-hand boundary.

		@param	string	The string to wrap into a paragraph.
		@param	width	The width of the paragraph, including any leading
						indentation.
		@param	indent	The number of columns the entire paragraph should be
						indented by.
		@return			The <code>string</code>, wrapped to the given width and
						indented as requested.
	*/
	public static String wrap(CharSequence string, int width, int indent) {
		DBC.REQUIRE(width > 0);
		DBC.REQUIRE(indent >= 0);
		DBC.REQUIRE(width > indent);

		// Pass through null
		if (string == null) {
			return null;
		}

		// Split the string into a list of words.
		List<String> words = new ArrayList<String>();
		StringBuilder word = new StringBuilder();
		for (int i = 0; i < string.length(); i++) {
			if (Character.isWhitespace(string.charAt(i))) {
				if (word.length() > 0) {
					words.add(word.toString());
					word.delete(0, word.length());
				}
			} else {
				word.append(string.charAt(i));
			}
		}
		if (word.length() > 0) {
			words.add(word.toString());
		}

		// Now flow the words into the paragraph.
		StringBuilder returnValue = new StringBuilder();
		int currentColumn = indent;
		for (String s : words) {
			if (s.length() + currentColumn > width) {
				if (currentColumn != indent) {
					returnValue.append(EOL);
					for (int i = 0; i < indent; i++) {
						returnValue.append(SPACE);
					}
				}
				returnValue.append(s);
				if (indent + s.length() + 1 <= width) {
					returnValue.append(SPACE);
				}
				currentColumn = indent + s.length() + 1;
			} else if (s.length() + currentColumn == width) {
				returnValue.append(s);
				returnValue.append(EOL);
				for (int i = 0; i < indent; i++) {
					returnValue.append(SPACE);
				}
				currentColumn = indent;
			} else {
				returnValue.append(s);
				returnValue.append(SPACE);
				currentColumn += s.length() + 1;
			}
		}
		for (int i = returnValue.length() - 1; i >= 0; i--) {
			if (returnValue.charAt(i) == SPACE) {
				returnValue.deleteCharAt(i);
			} else {
				break;
			}
		}

		return returnValue.toString();
	}

	/** A form of {@link Strings#wrap(CharSequence, int, int)} where the indent
		is 0.

		@param	string	The string to wrap into a paragraph.
		@param	width	The width of the paragraph.
		@return			The <code>string</code> wrapped into the given width.
		@see			#wrap(CharSequence, int, int)
	*/
	public static String wrap(CharSequence string, int width) {
		return wrap(string, width, 0);
	}

	/** Return the input with the first character upper-cased.

		@param	string	The {@link CharSequence} to transform.
		@return			The input {@link CharSequence} with the first character
						upper-cased.
	*/
	public static String upperCaseFirst(CharSequence string) {
		if (string == null) {
			return null;
		}
		if (string.length() < 1) {
			return string.toString();
		}
		StringBuilder retVal = new StringBuilder(string);
		retVal.replace(0, 1, string.subSequence(0, 1).toString().toUpperCase());
		return retVal.toString();
	}

	/** Check whether or not a sequence of characters constitutes a legal Java
		identifier.

		@param	string	The string to check.
		@return			True if <code>string</code> is a legal Java identifier,
						false otherwise.
	*/
	public static boolean isLegalJavaIdentifier(CharSequence string) {
		if (string == null) {
			return false;
		}
		if (string.length() <= 0) {
			return false;
		}
		for (int i = 0; i < string.length(); i++) {
			if (i == 0) {
				if (!Character.isJavaIdentifierStart(string.charAt(i))) {
					return false;
				}
			} else {
				if (!Character.isJavaIdentifierPart(string.charAt(i))) {
					return false;
				}
			}
		}
		String[] javaKeywords = {
			"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"const",
			"continue",
			"default",
			"do",
			"double",
			"else",
			"enum",
			"extends",
			"final",
			"finally",
			"float",
			"for",
			"goto",
			"if",
			"implements",
			"import",
			"instanceof",
			"int",
			"interface",
			"long",
			"native",
			"new",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"short",
			"static",
			"strictfp",
			"super",
			"switch",
			"synchronized",
			"this",
			"throw",
			"throws",
			"transient",
			"try",
			"void",
			"volatile",
			"while",
		};
		for (String keyword : javaKeywords) {
			if (string.equals(keyword)) {
				return false;
			}
		}
		return true;
	}
}
