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

/** A {@link Facet} representing a length facet in W3C schema.

	@author	Conor McDermottroe
	@since	0.2
*/
public class LengthFacet
implements Facet<LengthFacet, Integer>
{
	/** The number length units this facet represents. */
	private final int lengthUnits;

	/** If this is true, then types derived from a type with this facet may not
		override the value of {@link #lengthUnits}.
	*/
	private final boolean immutable;

	/** Create a new {@link LengthFacet}.

		@param	value	The exact length in type-specific length units.
		@param	fixed	If true, this enforces the value of this facet on all
						subtypes of a type with this facet.
	*/
	public LengthFacet(int value, boolean fixed) {
		lengthUnits = value;
		immutable = fixed;
	}

	/** Get the number of length units allowed.

		@return	The number of length units allowed.
	*/
	public Integer getValue() {
		return lengthUnits;
	}

	/** Find out if this facet cannot be overridden by a subtype.

		@return	Whether or not this facet may be overridden.
	*/
	public boolean getFixed() {
		return immutable;
	}

	/** {@inheritDoc} */
	public LengthFacet getCopy()
	throws CopyException
	{
		return new LengthFacet(lengthUnits, immutable);
	}

	/** {@inheritDoc} */
	public int compareTo(LengthFacet other) {
		int cmp = Utils.compare(lengthUnits, other.getValue());
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
		if (!(o instanceof LengthFacet)) {
			return false;
		}

		return compareTo(LengthFacet.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(lengthUnits, immutable);
	}
}
