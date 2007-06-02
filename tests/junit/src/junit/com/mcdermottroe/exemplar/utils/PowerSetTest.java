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
package junit.com.mcdermottroe.exemplar.utils;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

import com.mcdermottroe.exemplar.utils.PowerSet;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link PowerSet}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class PowerSetTest
extends NormalClassTestCase<PowerSet<String>>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		Set<String> testSet = new HashSet<String>(4);
		testSet.add("foo");
		testSet.add("bar");
		testSet.add("baz");
		testSet.add("quux");

		Set<String> bigTestSet = new HashSet<String>(32);
		for (int i = 0; i < 32; i++) {
			bigTestSet.add(String.valueOf(i));
		}

		addSample(new PowerSet<String>());
		addSample(new PowerSet<String>(testSet));
		addSample(new PowerSet<String>(bigTestSet));
	}

	/** Test {@link PowerSet#add(Object)}. It must throw an {@link
		UnsupportedOperationException} as {@link PowerSet}s are immutable.
	*/
	public void testAdd() {
		Set<String> testSet = new HashSet<String>(1);
		testSet.add("foobarbazquux");

		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.add(testSet);
					fail("PowerSet.add() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					// Ignore, correct path
				}
			}
		}
	}

	/** Test {@link PowerSet#addAll(java.util.Collection)}. */
	public void testAddAll() {
		Set<Set<String>> testSet = new HashSet<Set<String>>(1);
		Set<String> testSetA = new HashSet<String>(1);
		testSetA.add("foobarbazquux");
		testSet.add(testSetA);

		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.addAll(testSet);
					fail("PowerSet.addAll() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					// Ignore, correct path
				}
			}
		}
	}

	/** Test {@link PowerSet#cardinality()}. */
	public void testCardinality() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				BigInteger cardinality = sample.cardinality();
				assertTrue(
					"PowerSet.cardinality returns a positive integer",
					BigInteger.ZERO.compareTo(cardinality) <= 0
				);
			}
		}
	}

	/** Test {@link PowerSet#clear()}. */
	public void testClear() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.clear();
					fail("PowerSet.clear() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					// Correct path
				}
			}
		}
	}

	/** Test {@link PowerSet#contains(Object)}. */
	public void testContains() {
		Set<String> empty = new HashSet<String>(0);
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				// Test that it does not contain null
				try {
					sample.contains(null);
					fail("PowerSet.contains(null) did not throw NPE");
				} catch (NullPointerException e) {
					// Correct behaviour
				}

				// Test that it contains the empty set.
				assertTrue(
					"PowerSet.contains(<empty set>) == true",
					sample.contains(empty)
				);

				// Construct a test set that we know will be contained in the
				// sample power set.
				Set<String> sampleBase = sample.getBaseSet();
				Set<String> test = new HashSet<String>(2);
				String sampleElement = null;
				int count = 0;
				for (String s : sampleBase) {
					sampleElement = s;
					test.add(s);
					count++;
					if (count >= 2) {
						break;
					}
				}

				// Make sure that the set is contained in the power set.
				assertTrue(
					"PowerSet.contains(set) works as expected",
					sample.contains(test)
				);

				// If we have a sample element from one of the sets, make sure
				// that contains() does not mistakenly return true.
				if (sampleElement != null) {
					assertFalse(
						"PowerSet.contains(set element) == false",
						sample.contains(sampleElement)
					);
				}
			}
		}
	}

	/** Test {@link PowerSet#containsAll(java.util.Collection)}. */
	public void testContainsAll() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				Set<String> sampleBase = sample.getBaseSet();
				Set<Set<String>> test = new HashSet<Set<String>>(
					sampleBase.size()
				);
				for (String s : sampleBase) {
					Set<String> testElement = new HashSet<String>(1);
					testElement.add(s);
					test.add(testElement);
				}
				assertTrue(
					"PowerSet.containsAll(sample) == true",
					sample.containsAll(test)
				);

				Set<String> notInPowerSet = new HashSet<String>(1);
				notInPowerSet.add("This is not in there");
				test.add(notInPowerSet);
				assertFalse(
					"PowerSet.containsAll(sample) == false",
					sample.containsAll(test)
				);
			}
		}
	}

	/** Test {@link PowerSet#getBaseSet()}. */
	public void testGetBaseSet() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				Set<String> baseSet = sample.getBaseSet();
				assertNotNull("getBaseSet returned null", baseSet);
				if (baseSet.isEmpty()) {
					assertTrue(
						"getBaseSet => empty but PowerSet => non-empty",
						sample.isEmpty()
					);
				}
			}
		}
	}

	/** Test {@link PowerSet#isEmpty()}. */
	public void testIsEmpty() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				assertEquals(
					"PowerSet.isEmpty works correctly",
					sample.getBaseSet().isEmpty(),
					sample.isEmpty()
				);
			}
		}
	}

	/** Test {@link PowerSet#iterator()}. */
	public void testIterator() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null && sample.getBaseSet().size() <= 10) {
				if (sample.getBaseSet().size() <= 10) {
					Iterator<Set<String>> iter = sample.iterator();
					try {
						while (iter.hasNext()) {
							iter.next();
						}
					} catch (NoSuchElementException e) {
						fail("NoSuchElementException thrown");
						return;
					}
					try {
						iter.next();
						fail("iter.next() after end did not fail");
						return;
					} catch (NoSuchElementException e) {
						assertNotNull("NoSuchElementException was null", e);
					}
				}
				Iterator<Set<String>> iter = sample.iterator();
				try {
					iter.remove();
					fail("remove() did not fail");
					return;
				} catch (UnsupportedOperationException e) {
					assertNotNull("UnsupportedOperationException was null", e);
				}
				String iteratorString = iter.toString();
				if (iteratorString == null || iteratorString.length() <= 0) {
					fail("toString() failed to produce a non-empty String");
				}
				
			}
		}
	}

	/** Test {@link PowerSet#remove(Object)}. */
	public void testRemove() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.remove(new HashSet<String>(0));
					fail("PowerSet.remove() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					assertNotNull("UnsupportedOperationException was null", e);
				}
			}
		}
	}

	/** Test {@link PowerSet#removeAll(java.util.Collection)}. */
	public void testRemoveAll() {
		Set<Set<String>> toRemove = new HashSet<Set<String>>(0);
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.removeAll(toRemove);
					fail("PowerSet.removeAll() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					assertNotNull("UnsupportedOperationException was null", e);
				}
			}
		}
	}

	/** Test {@link PowerSet#retainAll(java.util.Collection)}. */
	public void testRetainAll() {
		Set<Set<String>> toRemove = new HashSet<Set<String>>(0);
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				try {
					sample.retainAll(toRemove);
					fail("PowerSet.removeAll() did not throw an exception");
					return;
				} catch (UnsupportedOperationException e) {
					assertNotNull("UnsupportedOperationException was null", e);
				}
			}
		}
	}

	/** Test {@link PowerSet#size()}. */
	public void testSize() {
		for (PowerSet<String> sample : samples()) {
			if (sample != null) {
				BigInteger maxInt = new BigInteger(
					String.valueOf(Integer.MAX_VALUE)
				);
				BigInteger cardinality = sample.cardinality();
				if (cardinality.compareTo(maxInt) <= 0) {
					try {
						assertEquals(
							"PowerSet.size() works as expected",
							cardinality.intValue(),
							sample.size()
						);
					} catch (UnsupportedOperationException e) {
						fail("PowerSet.size() threw an exception");
						return;
					}
				} else {
					try {
						int size = sample.size();
						fail("PowerSet.size() did not throw an exception");
					} catch (UnsupportedOperationException e) {
						assertNotNull(
							"UnsupportedOperationException was null",
							e
						);
					}
				}
			}
		}
	}
}
