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

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.input.schema.type.facet.Facet;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** A W3C Schema type.

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of this type. :)
*/
public abstract class Type<T extends Type<T>>
implements Copyable<T>
{
	/** The name of the type. */
	protected final String name;

	/** The {@link Facet}s associated with the type. */
	protected final Set<Facet> facets;

	/** How final is this type. */
	protected final Set<Finality> finality;

	/** Whether this type is complex or simple. True means complex. */
	protected final boolean isComplexType;

	/** Create a new {@link Type}.

		@param	typeName		The name of the type.
		@param	complex			True if the type is complex, false otherwise.
		@param	finalityType	The type of {@link Finality} this type has.
		@param	typeFacets		A set of {@link Facet}s associated with this
								type.
	*/
	protected Type(
		String typeName,
		boolean complex,
		Set<Finality> finalityType,
		Facet... typeFacets
	)
	{
		name = typeName;
		isComplexType = complex;
		finality = EnumSet.copyOf(finalityType);
		facets = new HashSet<Facet>();
		for (Facet f : typeFacets) {
			facets.add(f);
		}
	}

	/** Get the name of this type.

		@return	The name of this type.
	*/
	public String getName() {
		return name;
	}

	/** Add a {@link Facet} to this type.

		@param	facet	The {@link Facet} to add}.
	*/
	public void addFacet(Facet facet) {
		facets.add(facet);
	}

	/** Get the {@link Facet}s associated with this type.

		@return	The {@link Set} of {@link Facet}s associated with this type.
	*/
	public Set<Facet> getFacets() {
		return new HashSet<Facet>(facets);
	}

	/** Test whether or not this type is complex.

		@return	True if this type is complex, false otherwise.
	*/
	public boolean isComplexType() {
		return isComplexType;
	}

	/** Test whether or not this type is final with respect to a particular
		{@link Finality}.

		@param	f	The type of {@link Finality} one wishes to check.
		@return		True if this type is final for that particular type of
					{@link Finality}.
	*/
	public boolean isFinal(Finality f) {
		boolean isFinal = false;
		for (Finality fin : finality) {
			if (fin.includes(f)) {
				isFinal = true;
			}
		}
		return isFinal;
	}

	/** Get the finality set.

		@return	A copy of {@link #finality}.
	*/
	protected Set<Finality> getFinality() {
		return EnumSet.copyOf(finality);
	}

	/** {@inheritDoc} */
	public int compareTo(T other) {
		int cmp = Utils.compare(name, other.getName());
		if (cmp != 0) {
			return cmp;
		}

		cmp = Utils.compare(isComplexType, other.isComplexType());
		if (cmp != 0) {
			return cmp;
		}

		cmp = Utils.compare(finality, other.getFinality());
		if (cmp != 0) {
			return cmp;
		}

		cmp = Utils.compare(facets, other.getFacets());
		if (cmp != 0) {
			Utils.compare(facets, other.getFacets());
		}
		return cmp;
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The {@link Object} to compare against.
		@return		True if this equals o, false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!Type.class.isAssignableFrom(o.getClass())) {
			return false;
		}
		return compareTo((T)o) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(name, facets, finality, isComplexType);	
	}

	/** Implement {@link Object#toString()}.

		@return	A {@link String} description for this {@link Type}.
	*/
	@Override public String toString() {
		StringBuilder description = new StringBuilder();
		if (isComplexType) {
			description.append("Complex type: ");
		} else {
			description.append("Simple type: ");
		}
		description.append(name);
		description.append(SPACE);
		description.append(LEFT_CURLY);
		description.append(finality);
		description.append(COMMA);
		description.append(SPACE);
		description.append(facets);
		description.append(RIGHT_CURLY);
		return description.toString();
	}
}
