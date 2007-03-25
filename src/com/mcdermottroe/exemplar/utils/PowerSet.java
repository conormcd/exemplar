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

import java.io.Serializable;
import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;

/** Given a {@link Set} this produces the power set for that set as an
	immutable set of sets.

	@param	<T>	The type of elements within the {@link Set} from which the
				power set is to be created.
	@author		Conor McDermottroe
	@since		0.2
*/
public class PowerSet<T>
extends AbstractSet<Set<T>>
implements Cloneable, Serializable, Set<Set<T>>, Iterable<Set<T>>
{
	/** A copy of the original {@link Set} which we were given, as a {@link
		List} to give us a guaranteed stable ordering.
	*/
	private List<T> baseSet;

	/** Create an empty power set. */
	public PowerSet() {
		baseSet = new ArrayList<T>();
	}

	/** Create a new power set of a {@link Set}.

		@param	objects	The original {@link Set} to create the power set from.
	*/
	public PowerSet(Set<T> objects) {
		baseSet = new ArrayList<T>(objects);
	}

	/** Get the cardinality of the {@link Set}.

		@return	The number of {@link Set}s in the power set.
	*/
	public BigInteger cardinality() {
		BigInteger two = new BigInteger(String.valueOf(2));
		return two.pow(baseSet.size());
	}

	/** Clone the {@link PowerSet}.

		@return								A clone of this object.
		@throws CloneNotSupportedException	if the clone cannot be created.
	*/
	public Object clone()
	throws CloneNotSupportedException
	{
		return super.clone();
	}

	/** Test equality against another {@link Object}.

		@param	o	The other {@link Object} to test against.
		@return		True if <code>this</code> is equal to <code>o</code>, false
					otherwise.
	*/
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		if (o instanceof PowerSet) {
			PowerSet other = (PowerSet)o;
			return other.getBaseSet().equals(getBaseSet());
		} else if (o instanceof Set) {
			return containsAll((Set)o);
		} else {
			return false;
		}
	}

	/** Get the base {@link Set} for which <code>this</code> is a power set.

		@return	A copy of the base {@link Set}.
	*/
	public Set<T> getBaseSet() {
		return new HashSet<T>(baseSet);
	}

	/** Generate a hash code for this {@link PowerSet}.

		@return	A hash code for this object.
	*/
	public int hashCode() {
		return baseSet.hashCode();
	}

	/** Get an {@link Iterator} over the {@link Set}s in the power set.

		@return	An {@link Iterator} over the {@link Set}s in the power set.
	*/
	public Iterator<Set<T>> iterator() {
		return new PowerSetIterator();
	}

	/** If the cardinality of the power set is less than {@link
		Integer#MAX_VALUE} then the cardinality is returned, otherwise an
		{@link UnsupportedOperationException} is thrown.
	
		@return									The cardinality of the power
												set if possible.
	*/
	public int size() {
		BigInteger maxInt = new BigInteger(String.valueOf(Integer.MAX_VALUE));
		BigInteger cardinality = cardinality();
		if (cardinality.compareTo(maxInt) <= 0) {
			return cardinality.intValue();
		} else {
			throw new UnsupportedOperationException(
				"size() is unsupported on large power sets."
			);
		}
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
			return currentIteration.compareTo(lastIteration) < 0;
		}

		/** Return the next {@link Set} in the power set.
	
			@return	The next {@link Set} in the power set.
		*/
		public Set<T> next() {
			// Blow up if we try to walk past the end of the power set.
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			// Create the Set to return.
			Set<T> toReturn = new HashSet<T>();
			int i = 0;
			for (T element : baseSet) {
				if (currentIteration.testBit(i)) {
					toReturn.add(element);
				}
				i++;
			}

			// Advance the iteration
			currentIteration = currentIteration.add(BigInteger.ONE);

			// Return the generated Set
			return toReturn;
		}

		/** It is not possible to remove a set from the power set. */
		public void remove() {
			throw new UnsupportedOperationException();
		}
	}
}
