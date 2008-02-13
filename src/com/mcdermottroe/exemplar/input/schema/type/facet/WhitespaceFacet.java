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
package com.mcdermottroe.exemplar.input.schema.type.facet;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Utils;

/** A facet describing the whitespace handling strategy to be used for a given
	Type.

	@author	Conor McDermottroe
	@since	0.2
*/
public class WhitespaceFacet
implements Facet<WhitespaceFacet, WhitespaceBehaviour>
{
	/** The {@link WhitespaceBehaviour} that this facet represents. */
	private final WhitespaceBehaviour whitespaceBehaviour;

	/** Whether or not this facet is immutable for subtypes which hold this
		facet.
	*/
	private final boolean immutable;

	/** Create a new {@link WhitespaceFacet}.

		@param	value	The behaviour which this {@link WhitespaceFacet}
						represents.
		@param	fixed	Whether or not this facet is immutable for subtypes
						which have this facet.
	*/
	public WhitespaceFacet(WhitespaceBehaviour value, boolean fixed) {
		whitespaceBehaviour = value;
		immutable = fixed;
	}

	/** Get the {@link WhitespaceBehaviour} associated with this facet.

		@return	The {@link WhitespaceBehaviour} associated with this facet.
	*/
	public WhitespaceBehaviour getValue() {
		return whitespaceBehaviour;
	}

	/** Find out if this facet cannot be overridden by a subtype.

		@return	Whether or not this facet may be overridden.
	*/
	public boolean getFixed() {
		return immutable;
	}

	/** {@inheritDoc} */
	public WhitespaceFacet getCopy()
	throws CopyException
	{
		return new WhitespaceFacet(whitespaceBehaviour, immutable);
	}

	/** {@inheritDoc} */
	public int compareTo(WhitespaceFacet other) {
		int cmp = Utils.compare(whitespaceBehaviour, other.getValue());
		if (cmp != 0) {
			return cmp;
		}
		return Utils.compare(immutable, other.getFixed());
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The {@link Object} to compare against.
		@return		True if this is equal to o.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof WhitespaceFacet)) {
			return false;
		}

		return compareTo(WhitespaceFacet.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(whitespaceBehaviour, immutable);
	}
}
