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
package com.mcdermottroe.exemplar.input.schema.type;

import java.util.EnumSet;
import java.util.Set;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.input.schema.type.facet.Facet;

/** A type in the W3C Schema language sense.

	@author	Conor McDermottroe
	@since	0.2
*/
public class AtomicType
extends Type<AtomicType>
{
	/** Create a new atomic type.

		@param	typeName	The name of this type.
		@param	complex		Whether or not this atomic type is a complex one.
		@param	finality	The type of {@link Finality} associated with this
							{@link Type}.
		@param	facets		The {@link Facet}s associated with this {@link
							Type}.
	*/
	public AtomicType(
		String typeName,
		boolean complex,
		Set<Finality> finality,
		Facet... facets
	)
	{
		super(typeName, complex, EnumSet.copyOf(finality), facets);
	}

	/** {@inheritDoc} */
	public AtomicType getCopy()
	throws CopyException
	{
		return new AtomicType(
			name,
			isComplexType,
			EnumSet.copyOf(finality),
			facets.toArray(new Facet[] {})
		);
	}
}
