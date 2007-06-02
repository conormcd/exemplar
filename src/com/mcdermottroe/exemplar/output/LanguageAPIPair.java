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
package com.mcdermottroe.exemplar.output;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.utils.Strings;

/** A class representing a language-API pair.

	@author	Conor McDermottroe
	@since	0.1
*/
public class LanguageAPIPair
implements Comparable<LanguageAPIPair>, Copyable<LanguageAPIPair>
{
	/** The language. May not be null. */
	private final String language;

	/** The API. May be null. */
	private final String api;

	/** Basic constructor to set the members.

		@param theLanguage	The value for the language member.
		@param theApi		The value for the api member.
	*/
	public LanguageAPIPair(String theLanguage, String theApi) {
		DBC.REQUIRE(theLanguage != null);
		language = theLanguage;
		api = theApi;
	}

	/** Accessor for the language member.

		@return The language member. Guaranteed not to be null.
	*/
	public String getLanguage() {
		DBC.ENSURE(language != null);
		return language;
	}

	/** Accessor for the api member.

		@return The api member. May be null.
	*/
	public String getAPI() {
		return api;
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof LanguageAPIPair)) {
			return false;
		}

		LanguageAPIPair other = (LanguageAPIPair)o;
		if (!Utils.areDeeplyEqual(language, other.getLanguage())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(api, other.getAPI())) {
			return false;
		}

		return true;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(language, api);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		return Strings.deepToString(language, api);
	}

	/** Implement {@link Comparable} so that {@link LanguageAPIPair}
		objects can be contained in a sorted {@link java.util.Collection}.

		@param	other	The object to compare this one to.
		@return			-1, 0 or 1 if this object is less-than, equal to,
						or greater than o, respectively.
	*/
	public int compareTo(LanguageAPIPair other) {
		// This is required by the general contract of
		// Comparable.compareTo(Object)
		if (other == null) {
			throw new NullPointerException();
		}

		int result;
		int langCompare = language.compareTo(other.getLanguage());
		if (langCompare == 0) {
			if (api == null) {
				if (other.getAPI() == null) {
					result = 0;
				} else {
					result = -1;
				}
			} else {
				String otherAPI = other.getAPI();
				if (otherAPI == null) {
					result = 1;
				} else {
					result = api.compareTo(otherAPI);
				}
			}
		} else {
			result = langCompare;
		}

		return result;
	}

	/** {@inheritDoc} */
	public LanguageAPIPair getCopy() {
		return new LanguageAPIPair(language, api);
	}
}
