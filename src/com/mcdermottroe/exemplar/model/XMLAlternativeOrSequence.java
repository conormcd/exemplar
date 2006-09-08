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
public abstract class XMLAlternativeOrSequence
extends XMLObject
implements XMLObject.HasMinMax
{
	/** The contents of the collection. */
	private List contents;

	/** Simple constructor. */
	protected XMLAlternativeOrSequence() {
		super();
		contents = new ArrayList();
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
	public Iterator iterator() {
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

		contents.add(xo);

		DBC.ENSURE(contents != null);
	}

	/** Append another {@link com.mcdermottroe.exemplar.model.XMLObject} to
		this one.

		@param	other	The other {@link
						com.mcdermottroe.exemplar.model.XMLObject}.
	*/
	public void append(XMLObject other) {
		DBC.REQUIRE(other != null);
		DBC.REQUIRE(contents != null);

		if (other instanceof XMLAlternativeOrSequence) {
			XMLAlternativeOrSequence o = (XMLAlternativeOrSequence)other;
			for (Iterator it = o.iterator(); it.hasNext(); ) {
				contents.add(it.next());
			}
		} else if (other instanceof XMLElementReference) {
			contents.add(other);
		} else {
			DBC.UNREACHABLE_CODE();
		}

		DBC.ENSURE(contents != null);
	}

	/** Getter for the contents member.

		@return A copy of the contents member.
	*/
	public List getContents() {
		return new ArrayList(contents);
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof XMLAlternativeOrSequence)) {
			return false;
		}

		XMLAlternativeOrSequence other = (XMLAlternativeOrSequence)o;
		if (super.equals(other)) {
			if (Utils.areDeeplyEqual(contents, other.getContents())) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public int hashCode() {
		int hashCode = super.hashCode();

		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		hashCode += contents.hashCode();

		return hashCode;
	}
}
