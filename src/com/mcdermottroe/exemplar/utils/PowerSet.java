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
package com.mcdermottroe.exemplar.utils;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;

/** Given a {@link Set} this produces the power set for that set as an
	immutable set of sets.

	@param	<T>	The type of elements within the {@link Set} from which the
				power set is to be created.
	@author		Conor McDermottroe
	@since		0.2
*/
public class PowerSet<T>
extends AbstractSet<Set<T>>
implements Comparable<PowerSet<T>>, Copyable<PowerSet<T>>
{
	/** A copy of the original {@link Set} which we were given, as a {@link
		List} to give us a guaranteed stable ordering.
	*/
	private final List<T> baseSet;

	/** Create an empty power set. */
	public PowerSet() {
		super();
		baseSet = new ArrayList<T>(0);
	}

	/** Create a new power set of a {@link Set}.

		@param	objects	The original {@link Set} to create the power set from.
	*/
	public PowerSet(Set<T> objects) {
		super();
		baseSet = new ArrayList<T>(objects);
	}

	/** Create a new power set of a set of objects.

		@param	objects The original set to create the power set from.
	*/
	public PowerSet(T... objects) {
		super();
		baseSet = new ArrayList<T>(objects.length);
		for (T object : objects) {
			baseSet.add(object);
		}
	}

	/** A copy constructor.

		@param	objects	A {@link List} for the base set, to preserve ordering
						when copying.
	*/
	protected PowerSet(List<T> objects) {
		super();
		baseSet = new ArrayList<T>(objects);
	}

	/** Override {@link java.util.AbstractCollection#addAll(Collection)} for the
		purposes of making it always throw an {@link
		UnsupportedOperationException} regardless of whether or not the {@link
		Collection} being added actually contains any elements.

		@param	c	The {@link Collection} of objects to add.
		@return		This method will never return.
	*/
	@Override public boolean addAll(Collection<? extends Set<T>> c) {
		throw new UnsupportedOperationException();
	}

	/** Get the cardinality of the {@link Set}.

		@return	The number of {@link Set}s in the power set.
	*/
	public BigInteger cardinality() {
		BigInteger two = new BigInteger(String.valueOf(2));
		return two.pow(baseSet.size());
	}

	/** Clear the {@link Set} of all elements. This always throws an {@link
		UnsupportedOperationException} because this {@link Set} is immutable.
	*/
	@Override public void clear() {
		throw new UnsupportedOperationException();
	}

	/** Implement {@link Comparable#compareTo(Object)}.
		
		@param	other	The {@link PowerSet} to compare with.
		@return			A result as defined by {@link
						Comparable#compareTo(Object)}.
	*/
	public int compareTo(PowerSet<T> other) {
		SortedSet<T> thisBaseSet = new TreeSet<T>(baseSet);
		SortedSet<T> otherBaseset = new TreeSet<T>(other.getBaseSet());
		Iterator<T> thisIter = thisBaseSet.iterator();
		Iterator<T> otherIter = otherBaseset.iterator();
		while (thisIter.hasNext() && otherIter.hasNext()) {
			T a = thisIter.next();
			T b = otherIter.next();
			if (Comparable.class.isAssignableFrom(a.getClass())) {
				Comparable<T> ac = (Comparable<T>)a;
				int cmp = ac.compareTo(b);
				if (cmp != 0) {
					return cmp;
				}
			}
		}
		if (thisIter.hasNext()) {
			return 1;
		}
		if (otherIter.hasNext()) {
			return -1;
		}
		return 0;
	}

	/** Check if this {@link PowerSet} contains a given {@link Object}. Since
		this is a {@link Set} of {@link Set}s, the {@link Object} provided must
		be a {@link Set}. This is a faster implementation than the default as if
		<code>o</code> is a {@link Set} and all the elements in <code>o</code>
		are contained in the {@link #baseSet} then it is guaranteed that the
		{@link Set} represented by <code>o</code> is contained in
		<code>this</code>.

		@param	o	An {@link Object} which may be contained in this {@link
					PowerSet}.
		@return		True if <code>o</code> is a {@link Set} which is
					contained within this {@link PowerSet}.
	*/
	@Override public boolean contains(Object o) {
		if (o == null) {
			throw new NullPointerException();
		}
		if (!(o instanceof Set)) {
			return false;
		}

		return baseSet.containsAll(Collection.class.cast(o));
	}

	/** Check if this {@link PowerSet} contains all of the elements in the given
		{@link Collection}. This method simply calls {@link #contains(Object)}
		on all of the elements.

		@param	c	A {@link Collection} of {@link Object}s which could be in
					this {@link PowerSet}.
		@return		True if all of the elements in the given {@link Collection}
					are {@link Set}s in this {@link PowerSet}.
	*/
	@Override public boolean containsAll(Collection<?> c) {
		for (Object o : c) {
			if (!contains(o)) {
				return false;
			}
		}
		return true;
	}

	/** Test if this {@link PowerSet} is empty.

		@return	True if this power set is empty, false otherwise.
	*/
	@Override public boolean isEmpty() {
		return baseSet.isEmpty();
	}

	/** {@inheritDoc} */
	public PowerSet<T> getCopy()
	throws CopyException
	{
		return new PowerSet<T>(baseSet);
	}

	/** Test equality against another {@link Object}.

		@param	o	The other {@link Object} to test against.
		@return		True if <code>this</code> is equal to <code>o</code>, false
					otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		if (PowerSet.class.isAssignableFrom(o.getClass())) {
			PowerSet<?> other = PowerSet.class.cast(o);
			Set<?> otherBaseSet = other.getBaseSet();
			if (baseSet.size() == otherBaseSet.size()) {
				return otherBaseSet.containsAll(baseSet);
			} else {
				return false;
			}
		} else {
			return super.equals(o);
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
	@Override public int hashCode() {
		return baseSet.hashCode();
	}

	/** Get an {@link Iterator} over the {@link Set}s in the power set.

		@return	An {@link Iterator} over the {@link Set}s in the power set.
	*/
	@Override public Iterator<Set<T>> iterator() {
		return new PowerSetIterator();
	}

	/** This {@link Set} is immutable, hence a call to this method will always
		result in an {@link UnsupportedOperationException}.

		@param	o	The {@link Object} to remove.
		@return		True if the {@link Object} was removed.
	*/
	@Override public boolean remove(Object o) {
		throw new UnsupportedOperationException();
	}

	/** This {@link Set} is immutable, hence a call to this method will always
		result in an {@link UnsupportedOperationException}.

		@param	c	A {@link Collection} full of {@link Object}s to remove.
		@return		True if the {@link Object} was removed.
	*/
	@Override public boolean removeAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/** This {@link Set} is immutable, hence a call to this method will always
		result in an {@link UnsupportedOperationException}.

		@param	c	A {@link Collection} full of {@link Object}s to retain.
		@return		True if the {@link Object} was removed.
	*/
	@Override public boolean retainAll(Collection<?> c) {
		throw new UnsupportedOperationException();
	}

	/** If the cardinality of the power set is less than {@link
		Integer#MAX_VALUE} then the cardinality is returned, otherwise an
		{@link UnsupportedOperationException} is thrown.
	
		@return	The cardinality of the power set, if possible.
	*/
	@Override public int size() {
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

	/** Implementation of {@link Object#toString()} for debugging purposes.

		@return A {@link String} representation of this power set.
	*/
	@Override public String toString() {
		StringBuilder ret = new StringBuilder("Power set of the set { ");
		for (T object : baseSet) {
			ret.append(object.toString());
			ret.append(COMMA);
			ret.append(SPACE);
		}
		ret.append(" }");
		return ret.toString();
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
		PowerSetIterator() {
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
			Set<T> toReturn = new HashSet<T>(baseSet.size());
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

		/** Implement {@link Object#toString()} for debugging purposes.

			@return A {@link String} representation of this iterator.
		*/
		@Override public String toString() {
			StringBuilder ret = new StringBuilder(baseSet.toString().length());
			ret.append("Iteration ");
			ret.append(currentIteration);
			ret.append(" of ");
			ret.append(lastIteration);
			ret.append(" over ");
			ret.append(baseSet);
			return ret.toString();
		}
	}
}
