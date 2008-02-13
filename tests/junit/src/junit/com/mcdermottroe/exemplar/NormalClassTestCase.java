// vim:filetype=java:ts=4
/*
	Copyright (c) 2006-2008
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
package junit.com.mcdermottroe.exemplar;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;

/**	Base class for JUnit tests that test classes which produce "normal"
	classes. That includes all classes that are not abstract, exceptions or
	utility classes.

	@param	<T>	The type of the class being tested.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class NormalClassTestCase<T extends Copyable<T> & Comparable<T>>
extends NormalGeneratedClassTestCase<T>
{
	/** Ensure that all classes are {@link Copyable}. */
	public void testImplementsCopyable() {
		assertTrue(
			"Class implements Copyable",
			Copyable.class.isAssignableFrom(testedClass)
		);
	}

	/** This ensures that it is possible to call {@link Copyable#getCopy()} and
		not have an exception thrown.
	*/
	public void testCopyable() {
		for (T sample : samples()) {
			if (sample != null) {
				// Invoke the getCopy method.
				try {
					sample.getCopy();
				} catch (CopyException e) {
					e.printStackTrace();
					fail("getCopy() threw a CopyException"); // NON-NLS
					return;
				}
			}
		}
	}

	/** This ensures that it is possible to call {@link Copyable#getCopy()} and
		get an object which is equal to, but not the same (referentially) to the
		original object.
	*/
	public void testGetCopyProducesIdenticalObject() {
		for (T sample : samples()) {
			if (sample != null) {
				// Invoke the getCopy method.
				Object copy;
				try {
					copy = sample.getCopy();
				} catch (CopyException e) {
					e.printStackTrace();
					fail("getCopy() threw a CopyException");
					return;
				}

				// Fail if the copy is the same object
				assertFalse("The copy is the same object", sample == copy);

				// Fail if the original and copy are not equal
				assertEquals(
					"The copy is not equal to the original",
					sample,
					copy
				);

				// Ensure that the hashcodes are the same
				assertEquals(
					"The copy has a different hashcode to the original",
					sample.hashCode(),
					copy.hashCode()
				);
			}
		}
	}

	/** Test that:
		Integer.signum(a.compareTo(b)) == -Integer.signum(b.compareTo(a))
		as required by the contract of {@link Comparable#compareTo(Object)}.
	*/
	public void testCompareToSignsConsistent() {
		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					int ab = 0;
					boolean abThrew = false;
					int ba = 0;
					boolean baThrew = false;
					try {
						ab = Integer.signum(a.compareTo(b));
					} catch (Throwable t) {
						t.printStackTrace();
						abThrew = true;
					}
					try {
						ba = Integer.signum(b.compareTo(a));
					} catch (Throwable t) {
						t.printStackTrace();
						baThrew = true;
					}
					assertEquals(
						"Threw exception in one direction only",
						abThrew,
						baThrew
					);
					assertEquals(
						"sgn(a.compareTo(b)) != -sgn(b.compareTo(a))",
						ab,
						-ba
					);
				}
			}
		}
	}

	/** Test to ensure that:
		(a.compareTo(b) op 0 && b.compareTo(c) op 0)
	 	implies
		a.compareTo(c) op 0.
	*/
	public void testCompareToTransitive() {
		for (T a : samples()) {
			for (T b : samples()) {
				for (T c : samples()) {
					if (a != null && b != null) {
						int ab = 0;
						int bc = 0;
						int ac = 0;
						boolean abThrew = false;
						boolean bcThrew = false;
						boolean acThrew = false;
						try {
							ab = Integer.signum(a.compareTo(b));
						} catch (Throwable t) {
							abThrew = true;
						}
						try {
							bc = Integer.signum(b.compareTo(c));
						} catch (Throwable t) {
							bcThrew = true;
						}
						try {
							ac = Integer.signum(a.compareTo(c));
						} catch (Throwable t) {
							acThrew = true;
						}

						if (abThrew && bcThrew) {
							assertTrue("a.compareTo(c) did not throw", acThrew);
						} else if (abThrew) {
							// Only one threw, we don't care
						} else if (bcThrew) {
							// Only one threw, we don't care
						} else {
							if (ab == bc) {
								assertEquals(
									"comparable(T) not transitive",
									ab,
									ac
								);
							}
						}
					}
				}
			}
		}
	}

	/** Test that {@link Comparable#compareTo(Object)} throws a {@link
		NullPointerException} when given null as its parameter.
	*/
	public void testCompareToNullThrowsNPE() {
		for (T a : samples()) {
			try {
				a.compareTo(null);
				fail("compareTo(null) did not throw an NPE");
			} catch (NullPointerException e) {
				assertNotNull("NullPointerException was null", e);
			}
		}
	}

	/** Test to ensure that a.compareTo(b) == 0 implies a.equals(b). */
	public void testCompareToConsistentWithEquals() {
		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					// Test forward
					if (a.compareTo(b) == 0) {
						assertEquals(
							"compareTo not consistent with equals",
							a,
							b
						);
					}

					// Test reverse
					if (a.equals(b)) {
						assertEquals(
							"equals not consistent with compareTo",
							0,
							a.compareTo(b)
						);
					}
				}
			}
		}
	}

	/** Add a sample to the collection of sample objects.

		@param	sample			The sample object to add.
		@throws	CopyException	if copying the sample using {@link
								Copyable#getCopy()} fails.
	*/
	@Override protected void addSample(T sample)
	throws CopyException
	{
		super.addSample(sample);

		// Add two copies for more equality testing.
		sampleObjects.add(sample.getCopy());
		sampleObjects.add(sample.getCopy());
	}
}
