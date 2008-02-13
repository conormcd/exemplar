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
package com.mcdermottroe.exemplar.output;

import java.util.HashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.ui.Message;

/** A wrapper for all of the supported language levels/versions for all of the
	languages produced by the program.

	@author Conor McDermottroe
	@since	0.2
*/
public final class LanguageLevels {
	/** All of the language levels and their descriptions. */
	private static final Set<LanguageLevel> levels =
		new HashSet<LanguageLevel>();
	static {
		levels.add(
			new LanguageLevel(
				"Java 1.4",
				Message.LANGUAGE_LEVEL_JAVA_14(),
				"java1.4",
				"java14"
			)
		);
		levels.add(
			new LanguageLevel(
				"Java 5",
				Message.LANGUAGE_LEVEL_JAVA_15(),
				"java1.5",
				"java15",
				"java5"
			)
		);
	}

	/** Prevent this utility class from being instantiated. */
	private LanguageLevels() {
	}

	/** Get the language levels that we know about.

		@return	A {@link SortedMap} where the keys are level identifiers and
				the values are {@link LanguageLevel} objects.
	*/
	public static SortedMap<String, String> getAllLanguageLevels() {
		SortedMap<String, String> retval = new TreeMap<String, String>();
		for (LanguageLevel level : levels) {
			for (String alias : level.getAliases()) {
				retval.put(
					alias,
					level.getDescription()
				);
			}
		}
		return retval;
	}
}
