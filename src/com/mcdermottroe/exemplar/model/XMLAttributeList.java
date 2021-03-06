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
import java.util.Iterator;
import java.util.List;

import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.HASHCODE_MAGIC_NUMBER;

/** An {@link XMLObject} which represents attribute lists.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttributeList
extends XMLNamedObject<XMLAttributeList>
implements Iterable<XMLAttribute>
{
	/** The list of attributes. */
	private final List<XMLAttribute> attributes;

	/** Create a new attribute list.

		@param	elementName	The name of the element to which this attribute list
							is to be attached.
		@param	atts		The attribute definitions which make up this
							attribute list.
	*/
	public XMLAttributeList(String elementName, List<XMLAttribute> atts) {
		// Do the base class initialisation
		super(XMLElement.getLocalName(elementName));

		// Create the attribute list.
		attributes = new ArrayList<XMLAttribute>(atts);
	}

	/**	Access method for the {@link #attributes}.

		@return	An {@link Iterator} over the {@link #attributes}.
	*/
	public Iterator<XMLAttribute> iterator() {
		return getAttributes().iterator();
	}

	/** Getter for the {@link #attributes} member.

		@return A copy of the attributes member.
	*/
	public List<XMLAttribute> getAttributes() {
		return new ArrayList<XMLAttribute>(attributes);
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		StringBuilder sep = new StringBuilder(4);
		sep.append(RIGHT_PAREN);
		sep.append(COMMA);
		sep.append(SPACE);
		sep.append(LEFT_PAREN);

		StringBuilder desc = new StringBuilder();
		desc.append(LEFT_PAREN);
		desc.append(Strings.join(sep, attributes));
		desc.append(RIGHT_PAREN);

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}

	/** {@inheritDoc} */
	@Override public XMLAttributeList getCopy() {
		return new XMLAttributeList(getName(), attributes);
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

		hashCode *= HASHCODE_MAGIC_NUMBER;
		hashCode += attributes.hashCode();

		return hashCode;
	}
}
