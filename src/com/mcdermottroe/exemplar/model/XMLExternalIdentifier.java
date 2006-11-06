// vim:filetype=java:ts=4
/*
	Copyright (c) 2004, 2005, 2006
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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.Utils;

/** A class to handle the various forms of external identifiers in DTDs.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLExternalIdentifier {
	/** The public ID portion of the external identifier. */
	private String publicID;

	/** The system literal portion of the external identifier. */
	private String systemID;

	/** No-arg constructor for testing purposes. */
	public XMLExternalIdentifier() {
		publicID = null;
		systemID = null;
	}

	/** Simple constructor to copy in the values of the external identifier.

		@param	publicId	The value of the Public ID portion
		@param	systemId	The value of the SystemLiteral/URI portion
	*/
	public XMLExternalIdentifier(String publicId, String systemId) {
		publicID = publicId;
		systemID = systemId;
	}

	/** Accessor for {@link #publicID}.

		@return	A {@link String} containing the public ID of this external
				identifier.
	*/
	public String publicID() {
		return publicID;
	}

	/** Accessor for {@link #systemID}.

		@return	A {@link String} containing the system ID of this external
				identifier.
	*/
	public String systemID() {
		return systemID;
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(systemID);
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		desc.append(publicID);

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLExternalIdentifier)) {
			return false;
		}

		XMLExternalIdentifier other = (XMLExternalIdentifier)o;
		if (!Utils.areDeeplyEqual(publicID, other.publicID())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(systemID, other.systemID())) {
			return false;
		}

		return true;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(publicID, systemID);
	}
}
