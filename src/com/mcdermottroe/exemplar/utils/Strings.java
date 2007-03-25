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
import java.util.Arrays;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;

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
		@see	java.text.MessageFormat
	*/
	public static String formatMessage(String format, Object... args) {
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
			returnValue.append(String.format("\\u%04X", (int)s.charAt(i)));
		}

		return returnValue.toString();
	}

	/** Provide a method akin to Perl's join() function. This takes an {@link
		Iterable}, interprets every returned element as a {@link String} and
		joins them with the given separator {@link String}.

	 	@param	separator	Separator to join the elements with.
		@param	collection	Iterable collection to join.
		@return				All of the elements String-joined using the
							separator.
	*/
	public static String join(
		CharSequence separator,
		Iterable<? extends Object> collection
	)
	{
		StringBuilder joinedString = new StringBuilder();
		for (Object o : collection) {
			if (joinedString.length() > 0) {
				joinedString.append(separator);
			}
			joinedString.append(o);
		}
		return joinedString.toString();
	}

	/** An alias for {@link #join(CharSequence, Iterable)} which allows
		the use of a character as the separator.

		@param	separator	Separator to join the elements with.
		@param	collection	Iterable collection to join.
		@return				All of the elements String-joined using the
							separator.
	*/
	public static String join(
		char separator,
		Iterable<? extends Object> collection
	)
	{
		return join(String.valueOf(separator), collection);
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
}
