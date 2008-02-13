// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
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
package com.mcdermottroe.exemplar.input.schema;

import java.util.HashSet;
import java.util.Set;

import com.mcdermottroe.exemplar.generated.schema.element.Import;
import com.mcdermottroe.exemplar.generated.schema.element.Include;

/** A manager for keeping track of include and import declarations in a schema.

	@author Conor McDermottroe
	@since	0.2
*/
public final class IncludeManager {
	/** A {@link Set} of the {@link Import}s already processed. */
	private static Set<Import> alreadyImported = new HashSet<Import>();

	/** A {@link Set} of the {@link Include}s already processed. */
	private static Set<Include> alreadyIncluded = new HashSet<Include>();

	/** Prevent instantiaton of this class. */
	private IncludeManager() {
	}

	/** Mark an {@link Import} as having been processed.

		@param	imp	The {@link Import} which has been processed.
	*/
	public static void markAsImported(Import imp) {
		alreadyImported.add(imp);
	}

	/** Check whether an {@link Import} has been processed.

		@param	imp	The {@link Import} to check.
		@return		True if <code>imp</code> was already processed, false
					otherwise.
	*/
	public static boolean alreadyImported(Import imp) {
		return alreadyImported.contains(imp);
	}

	/** Mark an {@link Include} as having been processed.

		@param	inc	The {@link Include} which has been processed.
	*/
	public static void markAsIncluded(Include inc) {
		alreadyIncluded.add(inc);
	}

	/** Check whether an {@link Include} has been processed.

		@param	inc	The {@link Include} to check.
		@return		True if <code>inc</code> was already processed, false
					otherwise.
	*/
	public static boolean alreadyIncluded(Include inc) {
		return alreadyIncluded.contains(inc);
	}
}
