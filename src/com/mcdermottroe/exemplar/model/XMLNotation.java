// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2007
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

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.HASHCODE_MAGIC_NUMBER;

/** An {@link XMLObject} which represents notations.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLNotation
extends XMLNamedObject<XMLNotation>
{
	/** The ExternalID or PublicID of the {@link XMLNotation}. */
	private final XMLExternalIdentifier extID;

	/** Basic constructor.

		@param	notName		The name of the Notation
		@param	externalID	The ExternalID or PublicID of the Notation
	*/
	public XMLNotation(String notName, XMLExternalIdentifier externalID) {
		// Do the basic initialisation
		super(notName);
		DBC.REQUIRE(notName != null);
		DBC.REQUIRE(externalID != null);

		// Copy in the parameters
		extID = externalID;
	}

	/** Accessor for {@link #extID}.

		@return The external identifier for this notation.
	*/
	public XMLExternalIdentifier getExtID() {
		return extID;
	}

	/** {@inheritDoc} */
	@Override public int compareTo(XMLNotation other) {
		int superCmp = super.compareTo(other);
		if (superCmp != 0) {
			return superCmp;
		}
		return Utils.compare(extID, other.getExtID());
	}

	/** {@inheritDoc} */
	@Override public XMLNotation getCopy() {
		return new XMLNotation(name, extID.getCopy());
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLNotation)) {
			return false;
		}

		if (super.equals(o)) {
			XMLNotation other = (XMLNotation)o;
			if (Utils.areDeeplyEqual(extID, other.getExtID())) {
				return true;
			}
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= HASHCODE_MAGIC_NUMBER;
		if (extID != null) {
			hashCode += extID.hashCode();
		}

		return hashCode;
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(name);
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(LEFT_PAREN);
		desc.append(extID.toString());
		desc.append(RIGHT_PAREN);

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}
}
