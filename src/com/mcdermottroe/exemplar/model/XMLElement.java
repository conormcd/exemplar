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

import java.util.Iterator;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

/** An {@link XMLObject} which represents elements.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLElement
extends XMLObject
implements Constants.XML.Element, XMLObject.HasMinMax, XMLObject.HasName
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
	public XMLElement(XMLAlternative cSpec) {
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

	/** Remove unnecessary boxing of items within the contentspec. This is
		simply a driver for {@link #doOptimiseContentSpec(XMLObject)}.
	*/
	public void optimiseContentSpec() {
		switch (contentType) {
			case MIXED:
			case CHILDREN:
				contentSpec = doOptimiseContentSpec(contentSpec);
				break;
			case ANY:
			case EMPTY:
				// Nothing to optimise
				break;
			default:
				DBC.UNREACHABLE_CODE();
		}
	}

	/** Recursive worker for the alteration of contentspec trees to remove
		extra boxing of components introduced during the parsing of the
		vocabulary specification.

		For example, the contentspecs <code>(((foo)))</code> and
		<code>((bar+)?)</code> can be reduced to <code>(foo)</code> and
		<code>(bar*)</code> respectively.

		@param	contentSpecPart	A contentspec or part thereof which is to be
								altered.
		@return					A contentspec or part thereof which has been
								reduced in complexity yet describes the same
								content specification as
								<code>contentSpecPart</code>.
	*/
	private static XMLObject doOptimiseContentSpec(XMLObject contentSpecPart) {
		DBC.REQUIRE(contentSpecPart != null);

		if (!(contentSpecPart instanceof XMLAlternativeOrSequence)) {
			return contentSpecPart;
		}

		// Save the input XMLObject for comparison later
		XMLObject newContentSpecPart = contentSpecPart;

		XMLAlternativeOrSequence xo;
		xo = (XMLAlternativeOrSequence)newContentSpecPart;

		// Recursively optimise the children
		for (Iterator it = xo.iterator(); it.hasNext(); ) {
			XMLObject child = (XMLObject)it.next();
			child = doOptimiseContentSpec(child);
			DBC.ASSERT(child != null);
		}

		// With the removal of spurious parentheses and by making use of some
		// simple identies the expressions can be simplified quite a bit.
		if (xo.numElements() == 1) {
			Iterator it = xo.iterator();
			DBC.ASSERT(it.hasNext());
			XMLObject singleElement = (XMLObject)it.next();
			DBC.ASSERT(!it.hasNext());

			int min = -1;
			int max = -1;
			if (singleElement instanceof XMLObject.HasMinMax) {
				try {
					int innerMin = singleElement.getMinOccurs();
					int innerMax = singleElement.getMaxOccurs();
					int outerMin = xo.getMinOccurs();
					int outerMax = xo.getMaxOccurs();

					// Only the types allowed for in DTDs ('', '?', '*' and
					// '+') will be dealt with because it gets far too tricky
					// otherwise.
					//
					// Some day this will have to be replaced with a more
					// general algorithm to allow for the types that may be
					// expressed in W3C Schema.
					if	(
							(innerMin == 0 || innerMin == 1) &&
							(innerMax == 1 || innerMax == Constants.INFINITY) &&
							(outerMin == 0 || outerMin == 1) &&
							(outerMax == 1 || outerMax == Constants.INFINITY)
						)
					{
						if (innerMin == 0) {
							min = 0;
						} else {
							min = outerMin;
						}
						if (innerMax == 1) {
							max = outerMax;
						} else {
							max = Constants.INFINITY;
						}
					}
				} catch (XMLObjectException e) {
					DBC.UNREACHABLE_CODE();
				}
			}

			if	(
					min != -1 &&
// ALWAYS TRUE:		max != -1 &&
					!(singleElement instanceof XMLContent)
				)
			{
				newContentSpecPart = singleElement;
				try {
					newContentSpecPart.setMinMaxOccurs(min, max);
				} catch (XMLObjectException e) {
					DBC.UNREACHABLE_CODE();
				}
			}
		}

		// If nothing has changed, then it's OK to return. If stuff has
		// changed, then recurse again.
		if (!contentSpecPart.equals(newContentSpecPart)) {
			newContentSpecPart = doOptimiseContentSpec(newContentSpecPart);
		}

		// Make sure that there's at least one XMLAlternativeOrSequence around
		// this element.
		if (!(newContentSpecPart instanceof XMLAlternativeOrSequence)) {
			XMLObject tmp = newContentSpecPart;
			newContentSpecPart = new XMLAlternative();
			((XMLAlternativeOrSequence)newContentSpecPart).append(tmp);
		}

		return newContentSpecPart;
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
