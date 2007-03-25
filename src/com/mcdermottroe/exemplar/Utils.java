// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006, 2007
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
package com.mcdermottroe.exemplar;

import java.util.Collection;

/** General purpose utility methods that can be used anywhere in the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Utils {
	/** Private constructor to prevent instantiation of this class. */
	private Utils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Safely test deep equality on two {@link Object}s. This is quite useful
		for overriding {@link Object#equals(Object)}.

		@param	oa	The first {@link Object}
		@param	ob	The {@link Object} to compare to the first {@link Object}.
		@return		True if the {@link Object}s are equal according to
					<code>oa</code>'s version of {@link Object#equals(Object)}
					or if both are null, false otherwise.
	*/
	public static boolean areDeeplyEqual(Object oa, Object ob) {
		if (oa == null) {
			return ob == null;
		} else {
			return oa.equals(ob);
		}
	}

	/** Test two arrays of {@link Object}s and return true if all elements in
		one array deeply equal their corresponding element in the other array.

		@param	oA	The array of {@link Object}s A.
		@param	oB	The array of {@link Object}s B.
		@return		True if all elements A<sub>i</sub> deeply equal their
					corresponding element B<sub>i</sub>.
		@see	#areDeeplyEqual(Object, Object)
	*/
	public static boolean areAllDeeplyEqual(Object[] oA, Object[] oB) {
		if (oA.length == oB.length) {
			for (int i = 0; i < oA.length; i++) {
				if (!areDeeplyEqual(oA[i], oB[i])) {
					return false;
				}
			}
		} else {
			return false;
		}
		return true;
	}

	/** Generate a hash code value based on a selection of {@link Object}s,
		which allows for most versions of {@link Object#hashCode()} to simply
		hand off calculation of hashcodes to this method.

		@param	objects	One or more {@link Object}s to create the hash code
						from.
		@return			A hash code computed from all of the values supplied.
		@see	Object#hashCode()
	*/
	public static int genericHashCode(Object... objects) {
		int hashCode = 0;
		for (Object o : objects) {
			if (o != null) {
				if (o instanceof Collection) {
					Collection c = (Collection)o;
					for (Object co : c) {
						hashCode += co.hashCode();
					}
				} else {
					hashCode += o.hashCode();
				}
			} else {
				hashCode += 0;
			}
			hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		}
		return hashCode;
	}
}
