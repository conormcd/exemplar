// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006
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

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

/** An {@link com.mcdermottroe.exemplar.model.XMLObject} which represents
	alternative lists or sequences of {@link
	com.mcdermottroe.exemplar.model.XMLObject}s.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class XMLAggregateObject
extends XMLObject
implements Iterable<XMLObject>
{
	/** The contents of the collection. */
	private List<XMLObject> contents;

	/** Simple constructor. */
	protected XMLAggregateObject() {
		super();
		contents = new ArrayList<XMLObject>();
	}

	/** Return the number of elements in the object.

		@return	The number of elements in this object.
	*/
	public int numElements() {
		DBC.ASSERT(contents != null);
		return contents.size();
	}

	/** An {@link Iterator} over the contents contained in the object.

		@return An {@link Iterator} over the contents in the object.
	*/
	public Iterator<XMLObject> iterator() {
		DBC.ASSERT(contents != null);
		return contents.iterator();
	}

	/** Add an object to the internal contents.

		@param	xo	The {@link com.mcdermottroe.exemplar.model.XMLObject} to
					add to the calculation.
	*/
	public void addObject(XMLObject xo) {
		DBC.REQUIRE(xo != null);
		DBC.REQUIRE(contents != null);
		if (xo == null || contents == null) {
			return;
		}

		contents.add(xo);

		DBC.ENSURE(contents != null);
	}

	/** Append an {@link com.mcdermottroe.exemplar.model.XMLElementReference}
		to this object.

		@param	elementRef	The {@link
							com.mcdermottroe.exemplar.model.XMLElementReference}
							to add.
	*/
	public void append(XMLElementReference elementRef) {
		addObject(elementRef);
	}

	/** Append another {@link
		com.mcdermottroe.exemplar.model.XMLAggregateObject} to this one.

		@param	other	The other {@link
						com.mcdermottroe.exemplar.model.XMLAggregateObject}.
	*/
	public void append(XMLAggregateObject other) {
		DBC.REQUIRE(other != null);
		if (other == null) {
			return;
		}

		for (XMLObject xmlObject : other) {
			addObject(xmlObject);
		}
	}

	/** Getter for the contents member.

		@return A copy of the contents member.
	*/
	public List<XMLObject> getContents() {
		return new ArrayList<XMLObject>(contents);
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !o.getClass().equals(getClass())) {
			return false;
		}

		XMLAggregateObject other = (XMLAggregateObject)o;
		if (super.equals(other)) {
			if (Utils.areDeeplyEqual(contents, other.getContents())) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		hashCode += contents.hashCode();

		return hashCode;
	}

	/** Helper for doing most of the work of making {@link Object#toString()}
		for {@link XMLAggregateObject} objects.

		@param	className	The name of the aggregate object.
		@param	separator	The separator to display
		@return				A descriptive {@link String} for an {@link
							XMLAggregateObject}.
	*/
	protected String toString(String className, char separator) {
		return XMLObject.toStringHelper(
			className,
			Utils.join(separator, contents)
		);
	}
}
