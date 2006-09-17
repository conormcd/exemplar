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

/** An {@link XMLObject} which represents elements.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLElement
extends XMLObject
implements Constants.XML.Element, XMLObject.HasName
{
	/** The type of content this XMLElement may hold. This should be one of the
		types declared in {@link
		com.mcdermottroe.exemplar.Constants.XML.Element}.
	*/
	private int contentType;

	/** A tree describing the arrangement of the contents of this {@link
		#XMLElement}.
	*/
	private XMLObject contentSpec;

	/** A reference to the attribute list, if any that this element has. */
	private XMLAttributeList attlist;

	/** A no-arg constructor to aid in testing. */
	public XMLElement() {
		super();
		contentType = ANY;
		contentSpec = null;
		attlist = null;
	}

	/** Make a new {@link XMLElement} with the specified content type.

		@param	cType	The type of content this {@link XMLElement} may hold.
	*/
	public XMLElement(int cType) {
		super();
		DBC.REQUIRE(cType >= 0);
		contentType = cType;
		contentSpec = null;
		attlist = null;
	}

	/** Make a new {@link XMLElement} with the given content specification and
		a content type of {@link
		com.mcdermottroe.exemplar.Constants.XML.Element#MIXED}.

		@param	cSpec	The content specification for this element.
	*/
	public XMLElement(XMLMixedContent cSpec) {
		super();
		DBC.REQUIRE(cSpec != null);
		contentType = MIXED;
		contentSpec = cSpec;
		attlist = null;
	}

	/** Make a new {@link XMLElement} with the give content specification and a
		content type of {@link
		com.mcdermottroe.exemplar.Constants.XML.Element#CHILDREN}.

		@param	cSpec	The content specification for this element.
	*/
	public XMLElement(XMLSequence cSpec) {
		super();
		DBC.REQUIRE(cSpec != null);
		contentType = CHILDREN;
		contentSpec = cSpec;
		attlist = null;
	}

	/**	Accessor for the content spec.

		@return	The content spec for this {@link XMLElement}
	*/
	public XMLObject getContentSpec() {
		return contentSpec;
	}

	/** Access method to retrieve the type of content this {@link XMLElement}
		may hold.

		@return	One of {@link
				com.mcdermottroe.exemplar.Constants.XML.Element#EMPTY}, {@link
				com.mcdermottroe.exemplar.Constants.XML.Element#ANY}, {@link
				com.mcdermottroe.exemplar.Constants.XML.Element#MIXED} or
				{@link
				com.mcdermottroe.exemplar.Constants.XML.Element#CHILDREN}.
	*/
	public int getContentType() {
		DBC.ENSURE	(
						contentType == EMPTY ||
						contentType == ANY ||
						contentType == MIXED ||
						contentType == CHILDREN
					);
		return contentType;
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
		return attlist;
	}

	/** {@inheritDoc} */
	public String toString() {
		StringBuffer desc = new StringBuffer(
			toStringPrefix(
				getClass().getName()
			)
		);

		desc.append(name);
		if (attlist != null) {
			desc.append(Constants.Character.COMMA);
			desc.append(Constants.Character.SPACE);
			desc.append(Constants.Character.RIGHT_PAREN);
			desc.append(attlist.toString());
			desc.append(Constants.Character.LEFT_PAREN);
		}

		desc.append(toStringSuffix());

		return desc.toString();
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
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
				contentSpec,
				new Integer(contentType),
			};
			Object[] otherFields = {
				other.getAttlist(),
				other.getContentSpec(),
				new Integer(other.getContentType()),
			};
			return Utils.areAllDeeplyEqual(thisFields, otherFields);
		}

		return false;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (attlist != null) {
			hashCode += attlist.hashCode();
		}
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		if (contentSpec != null) {
			hashCode += contentSpec.hashCode();
		}
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		hashCode += contentType;

		return hashCode;
	}
}
