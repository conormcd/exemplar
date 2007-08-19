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

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

/** The content model for an {@link XMLElement}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class XMLElementContentModel
implements Comparable<XMLElementContentModel>, Copyable<XMLElementContentModel>
{
	/** The type of content model. */
	private final XMLElementContentType type;

	/** For some types this will be non-null and will describe the exact content
		allowed in the given element.
	*/
	private final XMLAggregateObject contentSpec;

	/** Create a new {@link XMLElementContentModel}.

		@param	contentType	The type (must be {@link XMLElementContentType#ANY}
							or {@link XMLElementContentType#EMPTY}).
	*/
	public XMLElementContentModel(XMLElementContentType contentType) {
		DBC.REQUIRE(
			contentType.equals(XMLElementContentType.ANY) ||
			contentType.equals(XMLElementContentType.EMPTY)
		);
		type = contentType;
		contentSpec = null;
	}

	/** Create a new {@link XMLElementContentModel}.

		@param	cSpec	The contentspec for this element.
	*/
	public XMLElementContentModel(XMLMixedContent cSpec) {
		DBC.REQUIRE(cSpec != null);
		type = XMLElementContentType.MIXED;
		contentSpec = cSpec;
	}

	/** Create a new {@link XMLElementContentModel}.

		@param	cSpec	The contentspec for this element.
	*/
	public XMLElementContentModel(XMLSequence cSpec) {
		DBC.REQUIRE(cSpec != null);
		type = XMLElementContentType.CHILDREN;
		contentSpec = cSpec;
	}

	/** Accessor for {@link #type}.

		@return {@link #type}.
	*/
	public XMLElementContentType getContentType() {
		return type;
	}

	/** Accessor for {@link #contentSpec}.

		@return	{@link #contentSpec}.
	*/
	public XMLAggregateObject getContentSpec() {
		return contentSpec;
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		<code>true</code> if <code>this</code> equals <code>o</code>
					and false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o ==  null) {
			return false;
		}
		if (!(o instanceof XMLElementContentModel)) {
			return false;
		}

		XMLElementContentModel other = (XMLElementContentModel)o;
		return	Utils.areDeeplyEqual(type, other.getContentType()) &&
				Utils.areDeeplyEqual(contentSpec, other.getContentSpec());
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(type, contentSpec);
	}

	/** Implement {@link Comparable#compareTo(Object)}.
		
		@param	other	The {@link XMLElementContentModel} to compare with.
		@return			A result as defined by {@link
						Comparable#compareTo(Object)}.
	*/
	public int compareTo(XMLElementContentModel other) {
		int typeCmp = Utils.compare(type, other.getContentType());
		if (typeCmp != 0) {
			return typeCmp;
		}
		return Utils.compare(contentSpec, other.getContentSpec());
	}

	/** {@inheritDoc} */
	public XMLElementContentModel getCopy() {
		if (contentSpec == null) {
			return new XMLElementContentModel(type);
		} else if (type.equals(XMLElementContentType.MIXED)) {
			return new XMLElementContentModel(
				XMLMixedContent.class.cast(contentSpec)
			);
		} else {
			return new XMLElementContentModel(
				XMLSequence.class.cast(contentSpec)
			);
		}
	}
}
