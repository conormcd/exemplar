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
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** An {@link XMLObject} which represents notations.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLNotation
extends XMLNamedObject
{
	/** The ExternalID or PublicID of the {@link XMLNotation}. */
	private XMLExternalIdentifier extID;

	/** No-arg constructor to aid testing. */
	public XMLNotation() {
		super();
		name = null;
		extID = new XMLExternalIdentifier();
	}

	/** Basic constructor.

		@param	notName		The name of the Notation
		@param	externalID	The ExternalID or PublicID of the Notation
	*/
	public XMLNotation(String notName, XMLExternalIdentifier externalID) {
		// Do the basic initialisation
		super();

		DBC.REQUIRE(notName != null);
		DBC.REQUIRE(externalID != null);

		// Copy in the parameters
		name = notName;
		extID = externalID;
	}

	/** Accessor for {@link #extID}.

		@return The external identifier for this notation.
	*/
	public XMLExternalIdentifier getExtID() {
		DBC.REQUIRE(extID != null);

		return extID;
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
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
	public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (extID != null) {
			hashCode += extID.hashCode();
		}

		return hashCode;
	}

	/** {@inheritDoc} */
	public String toString() {
		StringBuffer desc = new StringBuffer();
		desc.append(name);
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		desc.append(Constants.Character.LEFT_PAREN);
		if (extID != null) {
			desc.append(extID.toString());
		} else {
			desc.append(Message.XMLOBJECT_NOT_CONFIGURED);
		}
		desc.append(Constants.Character.RIGHT_PAREN);

		return toString(getClass().getName(), desc.toString());
	}
}
