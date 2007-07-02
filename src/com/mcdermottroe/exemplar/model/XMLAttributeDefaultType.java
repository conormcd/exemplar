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
package com.mcdermottroe.exemplar.model;

import java.util.HashMap;
import java.util.Map;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.DOUBLE_QUOTE;
import static com.mcdermottroe.exemplar.Constants.Character.HASH;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** Pseudo-enumerated type for the default type of XML attributes.

	@author Conor McDermottroe
	@since	0.2
*/
public class XMLAttributeDefaultType
implements	Comparable<XMLAttributeDefaultType>,
			Copyable<XMLAttributeDefaultType>
{
	/** The actual internal type. */
	private enum Type {
		/** Required default type. */
		REQUIRED,
		/** Implied default type. */
		IMPLIED,
		/** Fixed default type. */
		FIXED,
		/** Attvalue defualt type. */
		ATTVALUE,
	}

	/** A pool of {@link Type#ATTVALUE} {@link XMLAttributeDefaultType} objects.
	*/
	private static final Map<String, XMLAttributeDefaultType> poolATTVALUE =
		new HashMap<String, XMLAttributeDefaultType>();

	/** A pool of {@link Type#FIXED} {@link XMLAttributeDefaultType} objects.
	*/
	private static final Map<String, XMLAttributeDefaultType> poolFIXED =
		new HashMap<String, XMLAttributeDefaultType>();

	/** A singleton instance of an {@link Type#IMPLIED} {@link
		XMLAttributeDefaultType}.
	*/
	private static final XMLAttributeDefaultType poolIMPLIED =
		new XMLAttributeDefaultType(Type.IMPLIED);

	/** A singleton instance of an {@link Type#REQUIRED} {@link
		XMLAttributeDefaultType}.
	*/
	private static final XMLAttributeDefaultType poolREQUIRED =
		new XMLAttributeDefaultType(Type.REQUIRED);

	/** The type of this default decl. */
	private final Type type;

	/** The default value for the attribute. */
	private final String value;

	/** Create a new content type.

		@param t	The type of this default declaration.
	*/
	protected XMLAttributeDefaultType(Type t) {
		type = t;
		value = null;
	}


	/** Create a new content type.

		@param	t	The type of this default declaration.
		@param	val	THe value of this default declaration.
	*/
	protected XMLAttributeDefaultType(Type t, String val) {
		type = t;
		value = val;
	}

	/** A #REQUIRED attribute.

		@return The {@link XMLAttributeDefaultType} for a #REQUIRED attribute.
	*/
	public static XMLAttributeDefaultType REQUIRED() {
		return poolREQUIRED;
	}

	/** A #IMPLIED attribute.

		@return The {@link XMLAttributeDefaultType} for a #IMPLIED attribute.
	*/
	public static XMLAttributeDefaultType IMPLIED() {
		return poolIMPLIED;
	}

	/** A #FIXED attribute.

		@param	defVal	The fixed default value the attribute will have.
		@return			The {@link XMLAttributeDefaultType} for a #FIXED
						attribute.
	*/
	public static XMLAttributeDefaultType FIXED(String defVal) {
		if (!poolFIXED.containsKey(defVal)) {
			poolFIXED.put(
				defVal,
				new XMLAttributeDefaultType(Type.FIXED, defVal)
			);
		}
		return poolFIXED.get(defVal);
	}

	/** A attribute with just a value.

		@param	defVal	The value for the attribute.
		@return			The {@link XMLAttributeDefaultType} for an attribute
						with just a value.
	*/
	public static XMLAttributeDefaultType ATTVALUE(String defVal) {
		if (!poolATTVALUE.containsKey(defVal)) {
			poolATTVALUE.put(
				defVal,
				new XMLAttributeDefaultType(Type.ATTVALUE, defVal)
			);
		}
		return poolATTVALUE.get(defVal);
	}

	/** Get the default value.

		@return	The default value.
	*/
	public String getValue() {
		return value;
	}

	/** Accessor for {@link #type}.

		@return	{@link #type}.
	*/
	private Type getType() {
		return type;
	}

	/** Whether this is the same type as another {@link
		XMLAttributeDefaultType}.

		@param	other	The other {@link XMLAttributeDefaultType}.
		@return			True if <code>this</code> is the same type as
						<code>other</code>.
	*/
	public boolean sameType(XMLAttributeDefaultType other) {
		return type == other.getType();
	}

	/** Compare this {@link XMLAttributeDefaultType} to another.

		@param	other	The other {@link XMLAttributeDefaultType} to compare
						this with.
		@return			An integer as defined by {@link
						Comparable#compareTo(Object)}.
	*/
	public int compareTo(XMLAttributeDefaultType other) {
		if (other == null) {
			throw new NullPointerException();
		}

		int cmp = Utils.compare(type, other.getType());
		if (cmp != 0) {
			return cmp;
		}

		return Utils.compare(value, other.getValue());
	}

	/** {@inheritDoc} */
	public XMLAttributeDefaultType getCopy() {
		if (value != null) {
			return new XMLAttributeDefaultType(type, value);
		} else {
			return new XMLAttributeDefaultType(type);
		}
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The other {@link Object} to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
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

		XMLAttributeDefaultType other = (XMLAttributeDefaultType)o;
		return compareTo(other) == 0;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(type, value);
	}

	/** Return the {@link String} representation of this value.

		@return The {@link String} representation of this value.
	*/
	@Override public String toString() {
		StringBuilder retVal = new StringBuilder();
		switch (type) {
			case ATTVALUE:
				retVal.append(DOUBLE_QUOTE);
				retVal.append(value);
				retVal.append(DOUBLE_QUOTE);
				break;
			case FIXED:
				retVal.append(HASH);
				retVal.append(type.name());
				retVal.append(SPACE);
				retVal.append(DOUBLE_QUOTE);
				retVal.append(value);
				retVal.append(DOUBLE_QUOTE);
				break;
			case IMPLIED:
			case REQUIRED:
				retVal.append(HASH);
				retVal.append(type.name());
				break;
		}
		return retVal.toString();
	}
}
