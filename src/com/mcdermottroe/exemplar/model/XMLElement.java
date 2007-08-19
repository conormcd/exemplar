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

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.HASHCODE_MAGIC_NUMBER;

/** An {@link XMLObject} which represents elements.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLElement
extends XMLNamedObject<XMLElement>
{
	/** The content model for the element. */
	private final XMLElementContentModel contentModel;

	/** A reference to the attribute list, if any that this element has. */
	private XMLAttributeList attlist;

	/** Create a new {@link XMLElement}.

		@param	elementName	The name of the element.
		@param	content		The content model of the element.
	*/
	public XMLElement(String elementName, XMLElementContentModel content) {
		super(elementName);
		DBC.REQUIRE(content != null);
		contentModel = content;
		attlist = new XMLAttributeList(
			elementName,
			new ArrayList<XMLAttribute>()
		);
	}

	/** Accessor for the content model.

		@return	The content model.
	*/
	public XMLElementContentModel getContentModel() {
		DBC.ENSURE(contentModel != null);
		return contentModel;
	}

	/**	Accessor for the content spec.

		@return	The content spec for this {@link XMLElement}
	*/
	public XMLAggregateObject getContentSpec() {
		DBC.REQUIRE(contentModel != null);
		assert contentModel != null;
		return contentModel.getContentSpec();
	}

	/** Access method to retrieve the type of content this {@link XMLElement}
		may hold.

		@return	The content type of this element.
	*/
	public XMLElementContentType getContentType() {
		DBC.REQUIRE(contentModel != null);
		assert contentModel != null;
		return contentModel.getContentType();
	}

	/** Access method to set the reference to the attribute list for
		this element.

		@param	aList	The attribute list for this element.
	*/
	public void setAttlist(XMLAttributeList aList) {
		DBC.REQUIRE(aList != null);
		attlist = aList;
	}

	/**	Access method to retrieve the attribute list for this element.

		@return This elements attribute list.
	*/
	public XMLAttributeList getAttlist() {
		DBC.ENSURE(attlist != null);
		return attlist;
	}

	/** {@inheritDoc} */
	@Override public XMLElement getCopy()
	throws CopyException
	{
		XMLElement copy = new XMLElement(name, contentModel);
		copy.setAttlist(attlist);
		return copy;
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		StringBuilder desc = new StringBuilder();
		desc.append(name);
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(contentModel.getContentType());
		desc.append(COMMA);
		desc.append(SPACE);
		desc.append(contentModel.getContentSpec());
		if (attlist != null) {
			desc.append(COMMA);
			desc.append(SPACE);
			desc.append(RIGHT_PAREN);
			desc.append(attlist.toString());
			desc.append(LEFT_PAREN);
		}

		return XMLObject.toStringHelper(getClass().getName(), desc.toString());
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLElement)) {
			return false;
		}

		if (super.equals(o)) {
			XMLElement other = (XMLElement)o;
			Object[] thisFields = {
				attlist,
				contentModel,
			};
			Object[] otherFields = {
				other.getAttlist(),
				other.getContentModel(),
			};
			return Utils.areAllDeeplyEqual(thisFields, otherFields);
		}

		return false;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		int hashCode = super.hashCode();
		hashCode *= HASHCODE_MAGIC_NUMBER;
		hashCode += Utils.genericHashCode(attlist, contentModel);
		return hashCode;
	}
}
