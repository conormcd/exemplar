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

import java.util.regex.Pattern;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Utils;

/** A {@link Facet} representing a pattern facet in W3C schema.

	@author	Conor McDermottroe
	@since	0.2
*/
public class PatternFacet
implements Facet<PatternFacet, Pattern>
{
	/** The regular expression for this pattern facet. */
	private final Pattern pattern;

	/**	Create a new {@link PatternFacet}.

		@param	value	The regular expression value of this facet.
	*/
	public PatternFacet(String value) {
		pattern = Pattern.compile(value);
	}

	/** Create a new {@link PatternFacet} from a {@link Pattern}.

		@param	p	The pattern for this facet.
	*/
	protected PatternFacet(Pattern p) {
		pattern = p;
	}

	/** Get the {@link Pattern} that this {@link PatternFacet} represents.

		@return The {@link Pattern} that this {@link PatternFacet} represents.
	*/
	public Pattern getValue() {
		return pattern;
	}

	/** {@inheritDoc} */
	public PatternFacet getCopy()
	throws CopyException
	{
		return new PatternFacet(pattern);
	}

	/** {@inheritDoc} */
	public int compareTo(PatternFacet other) {
		return Utils.compare(pattern.pattern(), other.getValue().pattern());
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
		if (!(o instanceof PatternFacet)) {
			return false;
		}

		return compareTo(PatternFacet.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return pattern.hashCode();
	}
}
