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
import java.util.List;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** A class representing attributes for use within {@link
	com.mcdermottroe.exemplar.model.XMLAttributeList}s.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttribute
implements Cloneable, Comparable<XMLAttribute>
{
	/** Enumerated type for the content types of XML attributes. */
	public enum ContentType {
		/** Invalid content type, should never be used outside of this class. */
		INVALID		("<<INVALID>>"),
		/** An XML attribute with value type CDATA. */
		CDATA		("CDATA"),
		/** An XML attribute with value type ID. */
		ID			("ID"),
		/** An XML attribute with value type IDREF. */
		IDREF		("IDREF"),
		/** An XML attribute with value type IDREFS. */
		IDREFS		("IDREFS"),
		/** An XML attribute with value type ENTITY. */
		ENTITY		("ENTITY"),
		/** An XML attribute with value type ENTITIES. */
		ENTITIES	("ENTITIES"),
		/** An XML attribute with value type NMTOKEN. */
		NMTOKEN		("NMTOKEN"),
		/** An XML attribute with value type NMTOKENS. */
		NMTOKENS	("NMTOKENS"),
		/** An XML attribute with value type NOTATION. */
		NOTATION	("NOTATION"),
		/** An XML attribute with value type ENUMERATION. */
		ENUMERATION	("ENUMERATION");

		/** The {@link String} representation of the type of attribute. */
		private String stringRepresentation;

		/** Create a new content type.

			@param stringRep	The {@link String} representation of this value.
		*/
		ContentType(String stringRep) {
			stringRepresentation = stringRep;
		}

		/** Return the {@link String} representation of this value.

			@return The {@link String} representation of this value.
		*/
		@Override public String toString() {
			return stringRepresentation;
		}
	}

	/** Enumerated type for the default type of XML attributes. */
	public enum DefaultType {
		/** Invalid default type. */
		INVALID	("<<INVALID>>"),
		/** Required default type. */
		REQUIRED("REQUIRED"),
		/** Implied default type. */
		IMPLIED	("IMPLIED"),
		/** Fixed default type. */
		FIXED	("FIXED"),
		/** Attvalue defualt type. */
		ATTVALUE("ATTVALUE");

		/** The {@link String} representation of the type of attribute. */
		private String stringRepresentation;

		/** Create a new content type.

		 @param stringRep	The {@link String} representation of this value.
		 */
		DefaultType(String stringRep) {
			stringRepresentation = stringRep;
		}

		/** Return the {@link String} representation of this value.

			@return The {@link String} representation of this value.
		*/
		@Override public String toString() {
			return stringRepresentation;
		}
	}

	/** The name of this attribute. */
	private String name;

	/** The type of this attribute. */
	private ContentType attributeType;

	/** Possible values for this attribute. This will only be set for
		attributes of type {@link ContentType#NOTATION} or {@link
		ContentType#ENUMERATION}.
	*/
	private List<String> values;

	/** The type of default value this attribute has. */
	private DefaultType defaultDeclType;

	/** The default value for this attribute. */
	private String defaultValue;

	/** Create an invalid, empty XMLAttribute. */
	public XMLAttribute() {
		name = null;
		attributeType = ContentType.INVALID;
		values = null;
		defaultDeclType = DefaultType.INVALID;
		defaultValue = null;
	}

	/** Access method to retrieve the {@link #name} of the attribute.

		@return The name of this XMLAttribute
	*/
	public String getName() {
		return name;
	}

	/** Access method to set the {@link #name} of this attribute.

		@param	attName	The name of this attribute.
	*/
	public void setName(String attName) {
		name = attName;
	}

	/** Accessor for the attribute type.

		@return The type of attribute this is.
	*/
	public ContentType getType() {
		return attributeType;
	}

	/** Method to set the type of this attribute.

		@param	type	The type of this attribute. This should be one of the
						predefined types but not {@link ContentType#NOTATION} or
						{@link ContentType#ENUMERATION}.
	*/
	public void setType(ContentType type) {
		DBC.REQUIRE(type != null);
		if (type == null) {
			return;
		}
		DBC.REQUIRE(!type.equals(ContentType.NOTATION));
		DBC.REQUIRE(!type.equals(ContentType.ENUMERATION));

		attributeType = type;
		values = null;
	}

	/** Method to set the type of this attribute.

		@param	type	The type of this attribute. This should be either
						{@link ContentType#NOTATION} or {@link
						ContentType#ENUMERATION}.
		@param	vals	The range of values permissible for this attribute.
	*/
	public void setType(ContentType type, List<String> vals) {
		DBC.REQUIRE(type != null);
		if (type == null) {
			return;
		}
		DBC.REQUIRE	(
			type.equals(ContentType.NOTATION) ||
			type.equals(ContentType.ENUMERATION)
		);
		DBC.REQUIRE(vals != null);
		if (vals == null) {
			return;
		}

		attributeType = type;
		values = new ArrayList<String>(vals);

		DBC.ENSURE(values.size() == vals.size());
	}

	/** Accessor for the {@link #values}.

		@return A copy of the values.
	*/
	public List<String> getValues() {
		if (values != null) {
			Collections.sort(values);
			return new ArrayList<String>(values);
		} else {
			return null;
		}
	}

	/** Accessor for the {@link #values}.

		@param	vals	The values to add to the attribute.
	*/
	public void setValues(List<String> vals) {
		DBC.REQUIRE(vals != null);
		if (vals == null) {
			return;
		}

		values = new ArrayList<String>(vals);

		DBC.ENSURE(values.size() == vals.size());
	}

	/** Accessor for the {@link #defaultDeclType}.

		@return	The {@link #defaultDeclType} of this {@link
				com.mcdermottroe.exemplar.model.XMLAttribute}.
	*/
	public DefaultType getDefaultDeclType() {
		return defaultDeclType;
	}

	/** Accessor for the {@link #defaultValue}.

		@return	The {@link #defaultValue} of this {@link
				com.mcdermottroe.exemplar.model.XMLAttribute}.
	*/
	public String getDefaultValue() {
		return defaultValue;
	}

	/** Method to set the default type and value for this attribute.

		@param type 	The type of default value this is. Should be one of
						{@link DefaultType#REQUIRED}, {@link
						DefaultType#IMPLIED}, {@link DefaultType#FIXED} and
						{@link DefaultType#ATTVALUE}.
		@param value	The default value for this attribute. Should be
						<code>null</code> for {@link DefaultType#REQUIRED} and
						{@link DefaultType#IMPLIED} types.
	*/
	public void setDefaultDecl(DefaultType type, String value) {
		defaultDeclType = type;
		defaultValue = value;
	}

	/** Method to set the default type and value for this attribute to be equal
		to those fields in another {@link XMLAttribute}.

		@param	other	The other {@link XMLAttribute}.
	*/
	public void setDefaultDecl(XMLAttribute other) {
		DBC.REQUIRE(other != null);
		if (other == null) {
			return;
		}

		defaultDeclType = other.getDefaultDeclType();
		defaultValue = other.getDefaultValue();
	}

	/** Compare this attribute declaration with another, the value by which
		comparisons are made is the name. In other words, if a {@link
		java.util.Collection} of {@link XMLAttribute} objects is sorted, it is
		ordered by their names.

		@param	other	Another {@link XMLAttribute} to compare this {@link
						XMLAttribute} to.
		@return			A negative integer, zero or a positive integer if this
						object is less than, equal to or greater than the
						object <code>other</code> respectively.
	*/
	public int compareTo(XMLAttribute other) {
		return name.compareTo(other.getName());
	}

	/** Implement {@link Object#clone()}.

		@return								A clone of this object.
		@throws CloneNotSupportedException	if the clone cannot be created.
	*/
	@Override public Object clone()
	throws CloneNotSupportedException
	{
		XMLAttribute clone = (XMLAttribute)super.clone();
		clone.name = name;
		clone.attributeType = attributeType;
		clone.values = null;
		if (values != null) {
			clone.values = new ArrayList<String>(values);
		}
		clone.defaultDeclType = defaultDeclType;
		clone.defaultValue = defaultValue;
		return clone;
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

		XMLAttribute other = (XMLAttribute)o;
		Object[] thisFields = {
			attributeType,
			defaultDeclType,
			defaultValue,
			name,
			values,
		};
		Object[] otherFields = {
			other.getType(),
			other.getDefaultDeclType(),
			other.getDefaultValue(),
			other.getName(),
			other.getValues(),
		};
		return Utils.areAllDeeplyEqual(thisFields, otherFields);
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(
			name,
			attributeType,
			values,
			defaultDeclType,
			defaultValue
		);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(name);
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		if (attributeType.equals(ContentType.INVALID)) {
			desc.append(Message.XMLOBJECT_NOT_CONFIGURED);
		} else if	(
						attributeType.equals(ContentType.NOTATION) ||
						attributeType.equals(ContentType.ENUMERATION)
					)
		{
			if (attributeType.equals(ContentType.NOTATION)) {
				desc.append(ContentType.NOTATION.toString());
			}
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(values.toString());
			desc.append(Constants.Character.RIGHT_PAREN);
		} else {
			desc.append(attributeType);
		}
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		if	(
				defaultDeclType.equals(DefaultType.FIXED) ||
				defaultDeclType.equals(DefaultType.ATTVALUE)
			)
		{
			if (defaultDeclType.equals(DefaultType.FIXED)) {
				desc.append(DefaultType.FIXED);
			}
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(defaultValue);
			desc.append(Constants.Character.RIGHT_PAREN);
		} else {
			desc.append(defaultDeclType);
		}

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}
}
