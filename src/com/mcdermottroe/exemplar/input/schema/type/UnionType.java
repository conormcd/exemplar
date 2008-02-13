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
import java.util.HashSet;
import java.util.Set;

import com.mcdermottroe.exemplar.CopyException;

/** A union type in the W3C Schema language sense.

	@author	Conor McDermottroe
	@since	0.2
*/
public class UnionType
extends Type<UnionType>
{
	/** The {@link Set} of {@link Type}s which make up this union of types. */
	private final Set<Type> unionedTypes;

	/** Create a new type which is the union of a set of other types.

		@param	typeName	The name of the new type.
		@param	finality	Whether or not this type is final.
		@param	types		The types to union to create the new type.
	*/
	public UnionType(String typeName, Set<Finality> finality, Type... types) {
		super(typeName, areAnyComplex(types), EnumSet.copyOf(finality));
		unionedTypes = new HashSet<Type>();
		for (Type t : types) {
			unionedTypes.add(t);
		}
	}

	/** Get the types which make up this type.

		@return	The {@link Set} of types which make up this type.
	*/
	public Set<Type> getUnionedTypes() {
		return new HashSet<Type>(unionedTypes);
	}

	/** Test a set of types to see if any of them are complex types.

		@param	types	The types to check.
		@return			True if any of the types are complex, false if ALL are
						simple types.
	*/
	private static boolean areAnyComplex(Type... types) {
		for (Type t : types) {
			if (t.isComplexType()) {
				return true;
			}
		}
		return false;
	}

	/** {@inheritDoc} */
	public UnionType getCopy()
		throws CopyException
	{
		return new UnionType(
			name,
			EnumSet.copyOf(finality),
			unionedTypes.toArray(new Type[] {})
		);
	}
}
