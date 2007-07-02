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

import java.text.ParseException;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;

import static com.mcdermottroe.exemplar.Constants.BASE_HEXADECIMAL;
import static com.mcdermottroe.exemplar.Constants.Character.AMPERSAND;
import static com.mcdermottroe.exemplar.Constants.Character.HASH;
import static com.mcdermottroe.exemplar.Constants.Character.SEMI_COLON;
import static com.mcdermottroe.exemplar.Constants.Format.Code.XML.CHAR_ESCAPE;

/** A collection of XML-specific {@link String} handling methods.

	@author	Conor McDermottroe
	@since	0.2
*/
public final class XML {
	/** Private constructor to prevent instantiation of this class. */
	private XML() {
		DBC.UNREACHABLE_CODE();
	}

	/** Convert all characters to their XML character references. Any existing
		entity or character references are left untouched.

		@param	s				A {@link CharSequence} possibly containing XML
								references.
		@return					The {@link CharSequence} <code>s</code> with
								all XML character references replaced by the
								characters they represent.
		@throws	ParseException	in the event of an unterminated or malformed
								sequence.
	*/
	public static String toCharacterReferences(CharSequence s)
	throws ParseException
	{
		// Pass through null CharSequences
		if (s == null) {
			return null;
		}

		StringBuilder returnValue = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == AMPERSAND) {
				// Read until a semi-colon is found
				StringBuilder refBuffer = new StringBuilder();
				int j = i;
				while (true) {
					if (j == s.length()) {
						// Unterminated reference
						throw new ParseException(Message.UNTERMINATED_REF(), i);
					}
					refBuffer.append(s.charAt(j));
					if (s.charAt(j) == SEMI_COLON) {
						break;
					}
					j++;
				}

				// refBuffer =~ /^&.*;$/
				// Append the reference to the output.
				returnValue.append(refBuffer);

				// Advance over the reference.
				i = j;
			} else {
				// This is a non-reference character, convert it to a character
				// reference and append it.
				returnValue.append(
					String.format(CHAR_ESCAPE, (int)s.charAt(i))
				);
			}
		}

		return returnValue.toString();
	}

	/** Resolve all of the character references found in a {@link CharSequence}
		with their corresponding characters.

		@param	s				A {@link CharSequence} possibly containing XML
								character references.
		@return					The {@link CharSequence} <code>s</code> with
								all XML character references replaced by the
								characters they represent.
		@throws	ParseException	in the event of an unterminated or malformed
								character sequence.
	*/
	public static String resolveCharacterReferences(CharSequence s)
	throws ParseException
	{
		// Pass through null CharSequences
		if (s == null) {
			return null;
		}

		StringBuilder returnValue = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == AMPERSAND) {
				// Read until a semi-colon is found
				StringBuilder refBuffer = new StringBuilder();
				int j = i;
				while (true) {
					if (j == s.length()) {
						// Unterminated reference
						throw new ParseException(
							Message.UNTERMINATED_REF(),
							i
						);
					}
					refBuffer.append(s.charAt(j));
					if (s.charAt(j) == SEMI_COLON) {
						break;
					}
					j++;
				}

				// refBuffer =~ /^&.*;$/

				// Ignore non-character references
				if (refBuffer.length() > 3 && refBuffer.charAt(1) == HASH) {
					// Character reference
					int charRefValue;
					if (refBuffer.length() >= 5 && refBuffer.charAt(2) == 'x') {
						// Hexadecimal character reference
						try {
							charRefValue = Integer.parseInt(
								refBuffer.substring(3, refBuffer.length() - 1),
								BASE_HEXADECIMAL
							);
						} catch (NumberFormatException e) {
							Log.debug(e);
							throw new ParseException(
								Message.MALFORMED_CHAR_REF(refBuffer),
								i
							);
						}
					} else {
						// Decimal character reference
						try {
							charRefValue = Integer.parseInt(
								refBuffer.substring(2, refBuffer.length() - 1)
							);
						} catch (NumberFormatException e) {
							Log.debug(e);
							throw new ParseException(
								Message.MALFORMED_CHAR_REF(refBuffer),
								i
							);
						}
					}

					if (charRefValue < 0) {
						throw new ParseException(
							Message.MALFORMED_CHAR_REF(refBuffer),
							i
						);
					}

					// Now append the resolved character to the returnValue
					// unless it's an ampersand, in which case we leave it be.
					if (charRefValue == (int)AMPERSAND) {
						returnValue.append(refBuffer);
					} else {
						returnValue.append(Character.toChars(charRefValue));
					}
				} else {
					// This is an entity reference, just append it to the
					// output.
					returnValue.append(refBuffer);
				}

				// Advance over the reference.
				i = j;
			} else {
				// This is a character not involved in any reference, just
				// append it to the output.
				returnValue.append(s.charAt(i));
			}
		}

		return returnValue.toString();
	}
}
