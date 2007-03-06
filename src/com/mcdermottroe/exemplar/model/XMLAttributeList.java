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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.utils.Strings;

/** An {@link XMLObject} which represents attribute lists.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttributeList
extends XMLNamedObject
implements XMLMarkupDeclaration, Iterable<XMLAttribute>
{
	/** The list of attributes. */
	private List<XMLAttribute> attributes;

	/** Basic constructor. All XMLAttributeLists start empty and anonymous. */
	public XMLAttributeList() {
		// Do the base class initialisation
		super();

		// Create the attribute list.
		attributes = new ArrayList<XMLAttribute>();
	}

	/** Add a new attribute to {@link #attributes the list}.

		@param	newAttribute	The new attribute to add to the list.
	*/
	public void addAttribute(XMLAttribute newAttribute) {
		DBC.REQUIRE(attributes != null);
		DBC.REQUIRE(newAttribute != null);

		attributes.add(newAttribute);

		DBC.ENSURE(attributes != null);
		DBC.ENSURE(attributes.get(attributes.size() - 1).equals(newAttribute));
	}

	/**	Access method for the {@link #attributes}.

		@return	An {@link Iterator} over the {@link #attributes}.
	*/
	public Iterator<XMLAttribute> iterator() {
		DBC.REQUIRE(attributes != null);

		Collections.sort(attributes);
		return attributes.iterator();
	}

	/** Getter for the {@link #attributes} member.

		@return A copy of the attributes member.
	*/
	public List<XMLAttribute> getAttributes() {
		return new ArrayList<XMLAttribute>(attributes);
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		String sep =	String.valueOf(Constants.Character.RIGHT_PAREN) +
						Constants.Character.COMMA +
						Constants.Character.SPACE +
						Constants.Character.LEFT_PAREN;

		StringBuilder desc = new StringBuilder();
		desc.append(Constants.Character.LEFT_PAREN);
		desc.append(Strings.join(sep, this));
		desc.append(Constants.Character.RIGHT_PAREN);

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLAttributeList)) {
			return false;
		}

		XMLAttributeList other = (XMLAttributeList)o;
		if (super.equals(o)) {
			if (Utils.areDeeplyEqual(attributes, other.getAttributes())) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		hashCode += attributes.hashCode();

		return hashCode;
	}
}
