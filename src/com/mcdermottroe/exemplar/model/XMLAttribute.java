// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2008
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

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** A class representing attributes for use within {@link XMLAttributeList}s.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttribute
implements Comparable<XMLAttribute>, Copyable<XMLAttribute>
{
	/** The name of this attribute. */
	private final String name;

	/** The type of this attribute. */
	private final XMLAttributeContentType attributeType;

	/** The type of default value this attribute has. */
	private final XMLAttributeDefaultType defaultDeclType;

	/** Create an XMLAttribute.

		@param	attributeName	The name of the attribute.
		@param	type			The content type of the attribute, and
								permissible values for enumerated types.
		@param	defaultDecl		The default declaration for the attribute
								including default value where appropriate.
	*/
	public XMLAttribute	(
							String attributeName,
							XMLAttributeContentType type,
							XMLAttributeDefaultType defaultDecl
						)
	{
		name = attributeName;
		attributeType = type;
		defaultDeclType = defaultDecl;
	}

	/** Access method to retrieve the {@link #name} of the attribute.

		@return The name of this XMLAttribute
	*/
	public String getName() {
		return name;
	}

	/** Accessor for the attribute type.

		@return The type of attribute this is.
	*/
	public XMLAttributeContentType getType() {
		return attributeType;
	}

	/** Accessor for the {@link #defaultDeclType}.

		@return	The {@link #defaultDeclType} of this {@link
				com.mcdermottroe.exemplar.model.XMLAttribute}.
	*/
	public XMLAttributeDefaultType getDefaultDeclType() {
		return defaultDeclType;
	}

	/** Compare this attribute declaration with another.

		@param	other	Another {@link XMLAttribute} to compare this {@link
						XMLAttribute} to.
		@return			A negative integer, zero or a positive integer if this
						object is less than, equal to or greater than the
						object <code>other</code> respectively.
	*/
	public int compareTo(XMLAttribute other) {
		if (other == null) {
			throw new NullPointerException();
		}

		// For field comparisons
		int fieldCompare;

		// Compare names
		fieldCompare = Utils.compare(name, other.getName());
		if (fieldCompare != 0) {
			return fieldCompare;
		}

		// Compare content types
		fieldCompare = Utils.compare(attributeType, other.getType());
		if (fieldCompare != 0) {
			return fieldCompare;
		}

		// Compare defaultDeclTypes
		return Utils.compare(defaultDeclType, other.getDefaultDeclType());
	}

    /** {@inheritDoc} */
    public XMLAttribute getCopy() {
        return new XMLAttribute(
			name,
			attributeType.getCopy(),
			defaultDeclType.getCopy()
		);
    }

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLAttribute)) {
			return false;
		}

		return compareTo(XMLAttribute.class.cast(o)) == 0;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(
			name,
			attributeType,
			defaultDeclType
		);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(name);
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(attributeType);
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(defaultDeclType);

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}
}
