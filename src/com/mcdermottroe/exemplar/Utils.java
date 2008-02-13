// vim:filetype=java:ts=4
/*
	Copyright (c) 2005-2008
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
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

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
		if (oA == null && oB == null) {
			return true;
		}
		if (oA == null || oB == null) {
			return false;
		}
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

	/** Do a null-safe comparison of two comparable {@link Object}s.

		@param	<T>	The type of the second parameter, which may be compared with
					the first.
		@param	a	An {@link Object} which may be compared with other {@link
					Object}s of type <code>T</code>.
		@param	b	An {@link Object} which <code>a</code> may be compared with.
		@return		A negative, 0 or positive number if <code>a</code> is
					respectively less than, equal to or greater than
					<code>b</code>.
	*/
	public static <T> int compare(Comparable<T> a, T b) {
		if (a != null && b != null) {
			return a.compareTo(b);
		} else if (a != null) {
			return 1;
		} else if (b != null) {
			return -1;
		} else {
			return 0;
		}
	}

	/** Do a null-safe comparison of two {@link Collection}s of comparable
		{@link Object}s.

		@param	<X>	A type which implements <code>Comparable&lt;X&gt;</code>.
		@param	a	A {@link Collection} of {@link Object}s to compare.
		@param	b	The {@link Collection} to compare with <code>a</code>.
		@return		The kind of result you expect from an implementation of
					{@link Comparable#compareTo(Object)}.
	*/
	public static <X extends Comparable<X>> int
	compare(Collection<X> a, Collection<X> b)
	{
		if (a != null && b != null) {
			if	(
					Set.class.isAssignableFrom(a.getClass()) &&
					Set.class.isAssignableFrom(b.getClass())
				)
			{
				return compareSet((Set<X>)a, (Set<X>)b);
			}
			Iterator<X> aIter = a.iterator();
			Iterator<X> bIter = b.iterator();
			if (aIter.hasNext() && bIter.hasNext()) {
				while (aIter.hasNext() && bIter.hasNext()) {
					X aElem = aIter.next();
					X bElem = bIter.next();
					int elemCompare = compare(aElem, bElem);
					if (elemCompare != 0) {
						return elemCompare;
					}
				}
				if (aIter.hasNext()) {
					return 1;
				} else if (bIter.hasNext()) {
					return -1;
				} else {
					return 0;
				}
			} else if (aIter.hasNext()) {
				return 1;
			} else if (bIter.hasNext()) {
				return -1;
			} else {
				return 0;
			}
		} else if (a != null) {
			return 1;
		} else if (b != null) {
			return -1;
		} else {
			return 0;
		}
	}

	/** Do a null-safe comparison of two {@link Set}s of comparable
		{@link Object}s.

		@param	<X>	A type which implements <code>Comparable&lt;X&gt;</code>.
		@param	a	A {@link Set} of {@link Object}s to compare.
		@param	b	The {@link Set} to compare with <code>a</code>.
		@return		The kind of result you expect from an implementation of
					{@link Comparable#compareTo(Object)}.
	 */
	public static <X extends Comparable<X>> int compare(Set<X> a, Set<X> b) {
		return compareSet(a, b);
	}

	/** Implement {@link #compare(Set, Set)}.

		@param	<X>	A type which implements <code>Comparable&lt;X&gt;</code>.
		@param	a	A {@link Set} of {@link Object}s to compare.
		@param	b	The {@link Set} to compare with <code>a</code>.
		@return		The kind of result you expect from an implementation of
					{@link Comparable#compareTo(Object)}.
	*/
	private static <X extends Comparable<X>> int compareSet(
		Set<X> a,
		Set<X> b
	)
	{
		// Deal with the cases where either is null
		if (a == null && b == null) {
			return 0;
		} else if (a == null) {
			return -1;
		} else if (b == null) {
			return 1;
		}

		SortedSet<X> sortedA = new TreeSet<X>(a);
		SortedSet<X> sortedB = new TreeSet<X>(b);
		Iterator<X> aIter = sortedA.iterator();
		Iterator<X> bIter = sortedB.iterator();
		while (aIter.hasNext() && bIter.hasNext()) {
			X aElem = aIter.next();
			X bElem = bIter.next();
			int elemCompare = compare(aElem, bElem);
			if (elemCompare != 0) {
				return elemCompare;
			}
		}
		if (aIter.hasNext()) {
			return 1;
		} else if (bIter.hasNext()) {
			return -1;
		} else {
			return 0;
		}
	}

	/** Do a null-safe comparison of two {@link Map}s, which have {@link
		Comparable} keys and values.

		@param	<X>	A type which implements <code>Comparable&lt;X&gt;</code>.
		@param	<Y>	A type which implements <code>Comparable&lt;Y&gt;</code>.
		@param	a	A {@link Map} of &lt;X&gt; to &lt;Y&gt; to compare.
		@param	b	The {@link Map} to compare with <code>a</code>.
		@return		The kind of result you expect from an implementation of
					{@link Comparable#compareTo(Object)}.
	*/
	public static <X extends Comparable<X>, Y extends Comparable<Y>> int
	compare(Map<X, Y> a, Map<X, Y> b)
	{
		if (a != null && b != null) {
			SortedSet<X> aKeys = new TreeSet<X>(a.keySet());
			SortedSet<X> bKeys = new TreeSet<X>(b.keySet());
			int keyCmp = compare(aKeys, bKeys);
			if (keyCmp != 0) {
				return keyCmp;
			}

			for (X key : aKeys) {
				int cmp = compare(a.get(key), b.get(key));
				if (cmp != 0) {
					return cmp;
				}
			}

			return 0;
		} else if (a != null) {
			return 1;
		} else if (b != null) {
			return -1;
		} else {
			return 0;
		}
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
					Collection<?> c = Collection.class.cast(o);
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
