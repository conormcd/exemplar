// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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
package com.mcdermottroe.exemplar.output;

import java.util.SortedSet;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

/** A class representing the level or version of a language.

	@author	Conor McDermottroe
	@since	0.2
*/
public class LanguageLevel
implements Comparable<LanguageLevel>, Copyable<LanguageLevel>
{
	/** The label which will uniquely identify this language level. */
	private final String label;

	/** A description of the language level. */
	private final String description;

	/** A {@link SortedSet} of aliases for this language level. */
	private final SortedSet<String> aliases;

	/** Create a new {@link LanguageLevel}.

		@param	levelLabel			A unique label which can be used to
									identify this {@link LanguageLevel} in
									message bundles.
		@param	levelDescription	A description of the language level.
		@param	levelAliases		One or more aliases for this level.
	*/
	public LanguageLevel(
		String levelLabel, 
		String levelDescription,
		String... levelAliases
	)
	{
		label = levelLabel;
		description = levelDescription;
		aliases = new TreeSet<String>();
		for (String alias : levelAliases) {
			aliases.add(alias);
		}
	}

	/** Get the label for this level.

		@return	The label for this level.
	*/
	public String getLabel() {
		return label;
	}

	/** Get the description of the language level.

		@return	A description of the language level.
	*/
	public String getDescription() {
		return description;
	}

	/** Get all of the aliases for this language level.

		@return All of the aliases for this language level.
	*/
	public SortedSet<String> getAliases() {
		return new TreeSet<String>(aliases);
	}

	/** {@inheritDoc} */
	public LanguageLevel getCopy()
	throws CopyException
	{
		return new LanguageLevel(
			label,
			description,
			aliases.toArray(new String[] {})
		);
	}

	/** Implement {@link Comparable#compareTo(Object)}.

		@param	other	See {@link Comparable#compareTo(Object)}.
		@return			See {@link Comparable#compareTo(Object)}.
	*/
	public int compareTo(LanguageLevel other) {
		int labelCmp = Utils.compare(label, other.getLabel());
		if (labelCmp != 0) {
			return labelCmp;
		}
		int descriptionCmp = Utils.compare(description, other.getDescription());
		if (descriptionCmp != 0) {
			return descriptionCmp;
		}
		return Utils.compare(aliases, other.getAliases());
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The object to test equality with.
		@return		True if <code>this</code> equals <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof LanguageLevel)) {
			return false;
		}

		return compareTo(LanguageLevel.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(label, description, aliases);
	}
}
