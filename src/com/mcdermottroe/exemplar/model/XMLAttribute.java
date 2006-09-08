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
implements Constants.XML.Attribute, Comparable
{
	/** The name of this attribute. */
	private String name;

	/** The type of this attribute. Should be one of the predefined types. */
	private String attributeType;

	/** Possible values for this attribute. This will only be set for
		attributes of type {@link #NOTATION} or {@link #ENUMERATION}.
	*/
	private List values;

	/** The type of default value this attribute has. Should be one of the
		predefined types in {@link
		com.mcdermottroe.exemplar.Constants.XML.Attribute}.
	*/
	private String defaultDeclType;

	/** The default value for this attribute. */
	private String defaultValue;

	/** Create an invalid, empty XMLAttribute. */
	public XMLAttribute() {
		name = null;
		attributeType = INVALID;
		values = null;
		defaultDeclType = INVALID;
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
	public String getType() {
		return attributeType;
	}

	/** Method to set the type of this attribute.

		@param	type	The type of this attribute. This should be one of the
						predefined types but not {@link #NOTATION} or {@link
						#ENUMERATION}.
	*/
	public void setType(String type) {
		DBC.REQUIRE(type != null);
		if (type == null) {
			return;
		}
		DBC.REQUIRE(!type.equals(NOTATION));
		DBC.REQUIRE(!type.equals(ENUMERATION));

		attributeType = type;
		values = null;
	}

	/** Method to set the type of this attribute.

		@param	type	The type of this attribute. This should be either
						{@link #NOTATION} or {@link #ENUMERATION}.
		@param	vals	The range of values permissible for this attribute.
	*/
	public void setType(String type, List vals) {
		DBC.REQUIRE(type != null);
		if (type == null) {
			return;
		}
		DBC.REQUIRE	(type.equals(NOTATION) || type.equals(ENUMERATION));
		DBC.REQUIRE(vals != null);
		if (vals == null) {
			return;
		}

		attributeType = type;
		values = new ArrayList(vals);

		DBC.ENSURE(values.size() == vals.size());
	}

	/** Accessor for the {@link #values}.

		@return A copy of the values.
	*/
	public List getValues() {
		if (values != null) {
			Collections.sort(values);
			return new ArrayList(values);
		} else {
			return null;
		}
	}

	/** Accessor for the {@link #values}.

		@param	vals	The values to add to the attribute.
	*/
	public void setValues(List vals) {
		DBC.REQUIRE(vals != null);
		if (vals == null) {
			return;
		}

		values = new ArrayList(vals);

		DBC.ENSURE(values.size() == vals.size());
	}

	/** Accessor for the {@link #defaultDeclType}.

		@return	The {@link #defaultDeclType} of this {@link
				com.mcdermottroe.exemplar.model.XMLAttribute}.
	*/
	public String getDefaultDeclType() {
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
						{@link #REQUIRED}, {@link #IMPLIED},
						{@link #FIXED} and {@link #ATTVALUE}.
		@param value	The default value for this attribute. Should be
						<code>null</code> for {@link #REQUIRED} and
						{@link #IMPLIED} types.
	*/
	public void setDefaultDecl(String type, String value) {
		DBC.REQUIRE	(
						(
							(type.equals(REQUIRED) || type.equals(IMPLIED)) &&
							(value == null)
						) ||
						(
							(type.equals(FIXED) || type.equals(ATTVALUE)) &&
							(value != null)
						)
					);

		defaultDeclType = type;
		defaultValue = value;

		DBC.ENSURE	(
						type.equals(REQUIRED) ||
						type.equals(IMPLIED) ||
						type.equals(FIXED) ||
						type.equals(ATTVALUE)
					);
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

		DBC.ENSURE	(
						(
							(
								defaultDeclType.equals(REQUIRED) ||
								defaultDeclType.equals(IMPLIED)
							) &&
							(defaultValue == null)
						) ||
						(
							(
								defaultDeclType.equals(FIXED) ||
								defaultDeclType.equals(ATTVALUE)
							) &&
							(defaultValue != null)
						)
					);
	}

	/** Compare this attribute declaration with another, the value by which
		comparisons are made is the name. In other words, if a {@link
		java.util.Collection} of {@link XMLAttribute} objects is sorted, it is
		ordered by their names.

		@param	o	Another {@link Object} to compare this {@link XMLAttribute}
					to.
		@return		A negative integer, zero or a positive integer if this
					object is less than, equal to or greater than the object
					<code>o</code> respectively.
	*/
	public int compareTo(Object o) {
		DBC.REQUIRE(o instanceof XMLAttribute);
		return name.compareTo(((XMLAttribute)o).getName());
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	public boolean equals(Object o) {
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
	public int hashCode() {
		Object[] hashCodeVars = {
			name,
			attributeType,
			values,
			defaultDeclType,
			defaultValue,
		};
		return Utils.genericHashCode(hashCodeVars);
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	public String toString() {
		StringBuffer desc = new StringBuffer(
			XMLObject.toStringPrefix(
				getClass().getName()
			)
		);

		desc.append(name);
		desc.append(Constants.Character.COMMA);
		desc.append(Constants.Character.SPACE);
		if (attributeType.equals(INVALID)) {
			desc.append(Message.XMLOBJECT_NOT_CONFIGURED);
		} else if	(
						attributeType.equals(NOTATION) ||
						attributeType.equals(ENUMERATION)
					)
		{
			if (attributeType.equals(NOTATION)) {
				desc.append(NOTATION);
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
				defaultDeclType.equals(FIXED) ||
				defaultDeclType.equals(ATTVALUE)
			)
		{
			if (defaultDeclType.equals(FIXED)) {
				desc.append(FIXED);
			}
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(defaultValue);
			desc.append(Constants.Character.RIGHT_PAREN);
		} else {
			desc.append(defaultDeclType);
		}

		desc.append(XMLObject.toStringSuffix());

		return desc.toString();
	}
}
