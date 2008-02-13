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
import com.mcdermottroe.exemplar.Utils;

/** A list type in W3C Schema.

	@author					Conor McDermottroe
	@since					0.2
	@param	<ListedType>	The type which is being listed in this list type.
*/
public class ListType<ListedType extends Type<ListedType>>
extends Type<ListType<ListedType>>
{
	/** The {@link Type} of the list. */
	private final ListedType listedType;

	/** Create a new {@link Type} which is a list of a particular {@link Type}.

		@param	typeName	The name of the type.
		@param	finality	The type of {@link Finality} associated with this
							{@link Type}.
		@param	type		The {@link Type} of elements in the list.
	*/
	public ListType(String typeName, Set<Finality> finality, ListedType type) {
		super(typeName, type.isComplexType(), EnumSet.copyOf(finality));
		listedType = type;
		// SCHEMATASK: Add facets here
	}

	/** Get the type of the elements in the list.

		@return	The {@link Type} of the elements in the list.
	*/
	public ListedType getListedType() {
		return listedType;
	}

	/** {@inheritDoc} */
	public ListType<ListedType> getCopy()
	throws CopyException
	{
		return new ListType<ListedType>(
			name,
			EnumSet.copyOf(finality),
			listedType
		);
	}

	/** {@inheritDoc} */
	@Override public int compareTo(ListType<ListedType> other) {
		int cmp = super.compareTo(other);
		if (cmp != 0) {
			return cmp;
		}
		return Utils.compare(listedType, other.getListedType());
	}
}
