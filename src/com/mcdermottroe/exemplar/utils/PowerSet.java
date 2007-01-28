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
package com.mcdermottroe.exemplar.utils;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/** Given a {@link Set} this produces the power set for that set.

	@author	Conor McDermottroe
	@since	0.2
*/
public class PowerSet<T> implements Iterable<Set<T>> {
	/** A copy of the original {@link Set} which we were given, as a {@link
		List} to give us a guaranteed stable ordering.
	*/
	private List<T> baseSet;

	/** Create a new power set of a {@link Set}.

		@param	objects	The original {@link Set} to create the power set from.
	*/
	public PowerSet(Set<T> objects) {
		baseSet = new ArrayList<T>(objects);
	}

	/** Get an {@link Iterator} over the {@link Set}s in the power set.
	*/
	public Iterator<Set<T>> iterator() {
		return new PowerSetIterator();
	}

	/** A class to represent an {@link Iterator} over a {@link PowerSet}. We
		use {@link BigInteger}s as a bit vector with one bit per element in the
		original set. When a bit is set then that element should be present in
		the returned {@link Set}.

		@author	Conor McDermottroe
		@since	0.2
	*/
	private class PowerSetIterator implements Iterator<Set<T>> {
		/** The current iteration. */
		private BigInteger currentIteration;

		/** The last iteration. */
		private BigInteger lastIteration;

		/** Create a new {@link PowerSetIterator} initialised at the start of
			the iteration.
		*/
		public PowerSetIterator() {
			currentIteration = BigInteger.ZERO;
			lastIteration = BigInteger.ZERO;
			for (int i = 0; i < baseSet.size(); i++) {
				lastIteration = lastIteration.setBit(i);
			}
		}

		/** Returns true so long as there are more sets left unreturned from
			the power set.

			@return	True if there are more sets to return from the power set.
		*/
		public boolean hasNext() {
			return (currentIteration.compareTo(lastIteration) < 0);
		}

		/** Return the next {@link Set} in the power set.
	
			@return	The next {@link Set} in the power set.
		*/
		public Set<T> next() {
			Set<T> toReturn = new HashSet<T>();

			int i = 0;
			for (T element : baseSet) {
				if (currentIteration.testBit(i)) {
					toReturn.add(element);
				}
				i++;
			}

			currentIteration = currentIteration.add(BigInteger.ONE);
			return toReturn;
		}

		/** It is not possible to remove a set from the power set.
	
			@throws	UnsupportedOperationException	always.
		*/
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
