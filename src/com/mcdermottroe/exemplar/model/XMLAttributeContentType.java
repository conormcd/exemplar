// vim:filetype=java:ts=4
/*
	Copyright (c) 2007, 2008
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
import java.util.List;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.PIPE;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** Pseudo-enumerated type for the content types of XML attributes.

	@author Conor McDermottroe
	@since	0.2
*/
public class XMLAttributeContentType
implements	Comparable<XMLAttributeContentType>,
			Copyable<XMLAttributeContentType>
{
	/** The actual type for this. */
	public enum Type {
		/** CDATA. */
		CDATA,
		/** ID. */
		ID,
		/** IDREF. */
		IDREF,
		/** IDREFS. */
		IDREFS,
		/** ENTITY. */
		ENTITY,
		/** ENTITIES. */
		ENTITIES,
		/** NMTOKEN. */
		NMTOKEN,
		/** NMTOKENS. */
		NMTOKENS,
		/** NOTATION. */
		NOTATION,
		/** ENUMERATION. */
		ENUMERATION
	}

	/** To save on re-allocating identical immutable objects we keep a pool of
		them here.
	*/
	private static final XMLAttributeContentType[] staticTypesPool = {
		new XMLAttributeContentType(Type.CDATA),
		new XMLAttributeContentType(Type.ID),
		new XMLAttributeContentType(Type.IDREF),
		new XMLAttributeContentType(Type.IDREFS),
		new XMLAttributeContentType(Type.ENTITY),
		new XMLAttributeContentType(Type.ENTITIES),
		new XMLAttributeContentType(Type.NMTOKEN),
		new XMLAttributeContentType(Type.NMTOKENS),
	};

	/** The type of attribute this type refers to. */
	private final Type type;

	/** The values, for enumerated types ({@link Type#NOTATION}, {@link
		Type#ENUMERATION}) only.
	*/
	private final List<String> values;

	/** Create a new content type.

		@param	t	The type of this type.
	*/
	protected XMLAttributeContentType(Type t) {
		type = t;
		values = null;
	}

	/** Create a new content type.

		@param	t		The type of this type.
		@param	vals	The allowable values for this type.
	*/
	protected XMLAttributeContentType(Type t, List<String> vals) {
		type = t;
		values = new ArrayList<String>(vals);
	}

	/** An XML attribute with value type CDATA.

		@return	An XML attribute with value type CDATA
	*/
	public static XMLAttributeContentType CDATA() {
		return staticTypesPool[0];
	}

	/** An XML attribute with value type ID.

		@return	An XML attribute with value type ID
	*/
	public static XMLAttributeContentType ID() {
		return staticTypesPool[1];
	}

	/** An XML attribute with value type IDREF.

		@return	An XML attribute with value type IDREF
	*/
	public static XMLAttributeContentType IDREF() {
		return staticTypesPool[2];
	}

	/** An XML attribute with value type IDREFS.

		@return	An XML attribute with value type IDREFS
	*/
	public static XMLAttributeContentType IDREFS() {
		return staticTypesPool[3];
	}

	/** An XML attribute with value type ENTITY.

		@return	An XML attribute with value type ENTITY
	*/
	public static XMLAttributeContentType ENTITY() {
		return staticTypesPool[4];
	}

	/** An XML attribute with value type ENTITIES.

		@return	An XML attribute with value type ENTITIES
	*/
	public static XMLAttributeContentType ENTITIES() {
		return staticTypesPool[5];
	}

	/** An XML attribute with value type NMTOKEN.

		@return	An XML attribute with value type NMTOKEN
	*/
	public static XMLAttributeContentType NMTOKEN() {
		return staticTypesPool[6];
	}

	/** An XML attribute with value type NMTOKENS.

		@return	An XML attribute with value type NMTOKENS
	*/
	public static XMLAttributeContentType NMTOKENS() {
		return staticTypesPool[7];
	}

	/** An XML attribute with value type NOTATION.

		@param	val	The values for this NOTATION type.
		@return		An XML attribute with value type NOTATION
	*/
	public static XMLAttributeContentType NOTATION(List<String> val) {
		return new XMLAttributeContentType(Type.NOTATION, val);
	}

	/** An XML attribute with value type ENUMERATION.

		@param	val	The values for this NOTATION type.
		@return		An XML attribute with value type ENUMERATION
	*/
	public static XMLAttributeContentType ENUMERATION(List<String> val) {
		return new XMLAttributeContentType(Type.ENUMERATION, val);
	}

	/** Implement {@link Comparable#compareTo(Object)}.

		@param	other	The other {@link XMLAttributeContentType} to compare
						this with.
		@return			The correct return values for an implementation of
						{@link Comparable#compareTo(Object)}.
	*/
	public int compareTo(XMLAttributeContentType other) {
		if (other == null) {
			throw new NullPointerException();
		}

		int cmp = Utils.compare(type, other.getType());
		if (cmp != 0) {
			return cmp;
		}

		return Utils.compare(getValues(), other.getValues());
	}

	/** Access the values for an enumerated type.

		@return	A copy of the list of values.
	*/
	public List<String> getValues() {
		if (values != null) {
			return new ArrayList<String>(values);
		} else {
			return new ArrayList<String>(0);
		}
	}

	/** Accessor for {@link #type}.

		@return	{@link #type}.
	*/
	private Type getType() {
		return type;
	}

	/** {@inheritDoc} */
	public XMLAttributeContentType getCopy() {
		if (values != null) {
			return new XMLAttributeContentType(type, values);
		} else {
			return new XMLAttributeContentType(type);
		}
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if the objects are equal, false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!getClass().equals(o.getClass())) {
			return false;
		}

		return compareTo(XMLAttributeContentType.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(type, values);
	}

	/** Return the {@link String} representation of this value.

		@return The {@link String} representation of this value.
	*/
	@Override public String toString() {
		StringBuilder retVal = new StringBuilder();
		if (!type.equals(Type.ENUMERATION)) {
			retVal.append(type.name());
		}
		if (values != null) {
			if (!type.equals(Type.ENUMERATION)) {
				retVal.append(SPACE);
			}
			retVal.append(LEFT_PAREN);
			retVal.append(
				Strings.join(
					String.valueOf(SPACE) + PIPE + SPACE,
					values
				)
			);
			retVal.append(RIGHT_PAREN);
		}
		return retVal.toString();
	}
}
