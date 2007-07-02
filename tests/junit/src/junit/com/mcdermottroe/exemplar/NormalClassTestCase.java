// vim:filetype=java:ts=4
/*
	Copyright (c) 2006, 2007
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;

/**	Base class for JUnit tests that test classes which produce "normal"
	classes. That includes all classes that are not abstract, exceptions or
	utility classes.

	@param	<T>	The type of the class being tested.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class NormalClassTestCase<T extends Copyable<T>>
extends ExemplarTestCase<T>
{
	/** Flag to set if we allow 'public static final' members in the tested
		class. This should only be permitted in generated classes.
	*/
	protected boolean allowPublicStaticMembers;

	/** Flag to set if we don't want to run any tests on the {@link
		Object#hashCode()} method of the {@link Object}.
	*/
	protected boolean ignoreHashCodeTests;

	/** A selection of sample {@link Object}s which can be used within the
		tests. This allows more realistic, but still generalised, testing to be
		performed and avoids only testing the no-arg constructor. This {@link
		List} should be initialised in the {@link #setUp()} method.
	*/
	private List<T> sampleObjects;

	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		allowPublicStaticMembers = false;
		ignoreHashCodeTests = false;
		sampleObjects = null;
	}

	/** This test ensures that there are no private constructors declared. This
		is to allow for better extensibility. Only utility classes should have
		provate constructors as they should never be subclassed.
	*/
	public void testAllConstructorsNonPrivate() {
		assertNotNull("Tested class is null", testedClass);

		// Make sure that they're all non-private.
		for (Constructor<?> cons : testedClass.getDeclaredConstructors()) {
			int modifiers = cons.getModifiers();
			if (Modifier.isPrivate(modifiers)) {
				assertFalse(
					"Private constructor found",
					Modifier.isPrivate(modifiers)
				);
				return;
			}
		}
	}

	/** All fields must be either 'private', 'protected'
		or (if allowed by the {@link #allowPublicStaticMembers} flag) 'public
		static final'.
	*/
	public void testAllFieldsPrivateOrProtectedOrPublicStaticFinal() {
		assertNotNull("Tested class is null", testedClass);

		String overlyPublicFieldName = null;
		for (Field field : testedClass.getDeclaredFields()) {
			int modifiers = field.getModifiers();
			if	(
					!(
						Modifier.isPrivate(modifiers) ||
						Modifier.isProtected(modifiers)
					)
				)
			{
				if	(
						!(
							allowPublicStaticMembers &&
							Modifier.isPublic(modifiers) &&
							Modifier.isStatic(modifiers) &&
							Modifier.isFinal(modifiers)
						)
					)
				{
					overlyPublicFieldName = field.getName();
					if (overlyPublicFieldName.startsWith("$")) {
						overlyPublicFieldName = null;
					} else {
						break;
					}
				}
			}
		}
		assertNull(
			"Overly public field found: " + overlyPublicFieldName,
			overlyPublicFieldName
		);
	}

	/** This ensures that the result of {@link Object#hashCode()} does not
		change over time.
	*/
	public void testHashCodeConsistentOverTime() {
		if (ignoreHashCodeTests) {
			return;
		}
		assertTrue("Class does not defined hashCode()", hasHashCode());

		int repeat = 5;
		for (T o : samples()) {
			if (o != null) {
				int initialHashCode = o.hashCode();
				for (int i = 0; i < repeat - 1; i++) {
					assertEquals(
						"hashCode() value changed",
						initialHashCode,
						o.hashCode()
					);
				}
			}
		}
	}

	/** This ensures that where x.equals(y), x.hashCode() == y.hashCode().
	*/
	public void testHashCodeConsistentWithEquals() {
		if (ignoreHashCodeTests) {
			return;
		}
		assertTrue("Class does not defined hashCode()", hasHashCode());

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					if (a.equals(b)) {
						assertEquals(
							"Objects are equal but have different hash codes",
							a.hashCode(),
							b.hashCode()
						);
					} else {
						assertNotSame(
							"Objects are not equal but have equal hash codes",
							a.hashCode(),
							b.hashCode()
						);
					}
				}
			}
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		reflexive. That is, for all non-null reference values x, x.equals(x)
		must return true.
	*/
	public void testEqualsReflexive() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		for (T o : samples()) {
			assertEquals("equals(Object) is not reflexive", o, o);
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		symmetric. That is, for any non-null reference values x and y,
		x.equals(y)  should return true if and only if y.equals(x) returns
		true.
	*/
	public void testEqualsSymmetric() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					assertEquals(
						"equals() is not symmetric",
						a.equals(b),
						b.equals(a)
					);
				}
			}
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		transitive. That is, for any non-null reference values x, y, and z, if
		x.equals(y) returns true and y.equals(z) returns true then x.equals(z)
		must return true.
	*/
	public void testEqualsTransitive() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		for (T a : samples()) {
			for (T b : samples()) {
				for (T c : samples()) {
					if (a != null && b != null) {
						boolean isTransititive = true;
						if (a.equals(b) && b.equals(c)) {
							isTransititive = a.equals(c);
						}
						assertTrue(
							"equals(Object) is not transitive",
							isTransititive
						);
					}
				}
			}
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		consistent. That is, for all non-null reference values x and y,
		multiple invocations of x.equals(y) consistently return true or false,
		provided no information used in the equals comparisons on the objects
		is modified.
	*/
	public void testEqualsConsistent() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		int repeats = 5;
		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null) {
					boolean initialResult = a.equals(b);
					for (int i = 0; i < repeats - 1; i++) {
						assertEquals(
							"equals(Object) is not consistent over time",
							initialResult,
							a.equals(b)
						);
					}
				}
			}
		}
	}

	/**	Every call to equals where the parameter is null must return false. */
	public void testEqualsNullFalse() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		for (T o : samples()) {
			if (o != null) {
				assertFalse("equals(null) returned true", o.equals(null));
			}
		}
	}

	/** Test equals with an object which is not of the same type. */
	public void testEqualsNullPointerException() {
		assertTrue("Class does not define equals(Object)", hasEquals());

		NullPointerException bogus = new NullPointerException();
		for (T o : samples()) {
			if (o != null) {
				assertFalse(
					"equals(NullPointerException) returned true",
					o.equals(bogus)
				);
			}
		}
	}

	/** This ensures that for all non-null {@link Object}s x and y,
		x.toString().equals(y.toString()) iff x.equals(y). This is not required
		by the contract of {@link Object#toString()} but is an all-round good
		idea.
	*/
	public void testToStringConsistent() {
		assertTrue("Class does not define toString()", hasToString());

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					assertEquals(
						"toString() isn't consistent with equals(Object)",
						a.equals(b),
						a.toString().equals(b.toString())
					);
				}
			}
		}
	}

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
					fail("getCopy() threw a CopyException"); // NON-NLS
					return;
				}

				// Fail if the copy is the same object
				if (sample == copy) {
					fail("The copy is the same object"); // NON-NLS
					return;
				}

				// Fail if the original and copy are not equal
				assertEquals(
					"The copy is not equal to the original",
					sample,
					copy
				);
			}
		}
	}

	/** Test that:
		Integer.signum(a.compareTo(b)) == -Integer.signum(b.compareTo(a))
		as required by the contract of {@link Comparable#compareTo(Object)}.
	*/
	public void testCompareToSignsConsistent() {
		if (!Comparable.class.isAssignableFrom(testedClass)) {
			return;
		}
		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					int ab = 0;
					boolean abThrew = false;
					int ba = 0;
					boolean baThrew = false;
					try {
						ab = Integer.signum(((Comparable<T>)a).compareTo(b));
					} catch (Throwable t) {
						t.printStackTrace();
						abThrew = true;
					}
					try {
						ba = Integer.signum(((Comparable<T>)b).compareTo(a));
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
		if (!Comparable.class.isAssignableFrom(testedClass)) {
			return;
		}
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
							ab = Integer.signum(
								((Comparable<T>)a).compareTo(b)
							);
						} catch (Throwable t) {
							abThrew = true;
						}
						try {
							bc = Integer.signum(
								((Comparable<T>)b).compareTo(c)
							);
						} catch (Throwable t) {
							bcThrew = true;
						}
						try {
							ac = Integer.signum(
								((Comparable<T>)a).compareTo(c)
							);
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
		if (!Comparable.class.isAssignableFrom(testedClass)) {
			return;
		}
		for (T a : samples()) {
			try {
				((Comparable<T>)a).compareTo(null);
				fail("compareTo(null) did not throw an NPE");
			} catch (NullPointerException e) {
				assertNotNull("NullPointerException was null", e);
			}
		}
	}

	/** Test to ensure that a.compareTo(b) == 0 implies a.equals(b). */
	public void testCompareToConsistentWithEquals() {
		if (!Comparable.class.isAssignableFrom(testedClass)) {
			return;
		}
		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					if (((Comparable<T>)a).compareTo(b) == 0) {
						assertEquals(
							"compareTo not consistent with equals",
							a,
							b
						);
					}
				}
			}
		}
	}

	/** Test that all serializable classes serialize and unserialize without
		apparent loss of information.
	*/
	public void testSerialization() {
		// Skip test on classes not marked Serializable
		if (!Serializable.class.isAssignableFrom(testedClass)) {
			return;
		}

		for (T sample : samples()) {
			if (sample == null) {
				continue;
			}
			assertTrue(
				"Class marked as Serializable, but sample was not",
				sample instanceof Serializable
			);
			try {
				// Serialize the object
				ByteArrayOutputStream storage = new ByteArrayOutputStream();
				ObjectOutputStream out = new ObjectOutputStream(storage);
				out.writeObject(sample);
				out.close();

				// Deserialise the object
				ByteArrayInputStream input = new ByteArrayInputStream(
					storage.toByteArray()
				);
				ObjectInputStream in = new ObjectInputStream(input);
				Object o = in.readObject();
				in.close();

				// Check that the serialized and deserialized object is exactly
				// the same as the original.
				if (!sample.equals(o)) {
					assertEquals(
						"Round-tripped object is not equal to the original",
						sample,
						o
					);
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				fail("ClassNotFoundException thrown when (de)serializing.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				fail("IOException thrown when (de)serializing.");
				return;
			}
		}
	}

	/** Add a sample to the collection of sample objects.

		@param	sample			The sample object to add.
		@throws	CopyException	if copying the sample using {@link
								Copyable#getCopy()} fails.
	*/
	protected void addSample(T sample)
	throws CopyException
	{
		if (sampleObjects == null) {
			sampleObjects = new ArrayList<T>();

			// Add nulls to shake out as many NPEs as possible.
			sampleObjects.add(null);
			sampleObjects.add(null);
		}

		// Add two references to make sure that the equality tests get exercised
		// fully.
		sampleObjects.add(sample);
		sampleObjects.add(sample);

		// Add two copies for more equality testing.
		sampleObjects.add(sample.getCopy());
		sampleObjects.add(sample.getCopy());
	}

	/** Provide a {@link List} of sample objects to operate on.

		@return A guaranteed non-empty {@link List} of sample objects.
	*/
	protected List<T> samples() {
		List<T> samples = new ArrayList<T>();

		if (sampleObjects != null) {
			samples.addAll(sampleObjects);
		} else {
			throw new UnsupportedOperationException();
		}

		return samples;
	}

	/** Test whether or not the tested class implements an {@link
		Object#equals(Object)} method.

		@return	True if the tested class implements an {@link
				Object#equals(Object)} method, false otherwise.
	*/
	private boolean hasEquals() {
		// If we don't know the class we're testing, then we don't have the
		// equals method for it either.
		if (testedClass == null) {
			return false;
		}

		// Try and get the equals method.
		Method equalsMethod;
		try {
			equalsMethod = testedClass.getMethod(
				"equals",		// NON-NLS
				Object.class
			);
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		// If we got an equals method then return true, else false.
		return equalsMethod != null;
	}

	/** Test whether or not the tested class implements a {@link
		Object#hashCode()} method.

		@return	True if the tested class implements a {@link Object#hashCode()}
				method, false otherwise.
	*/
	private boolean hasHashCode() {
		// If we don't know the class we're testing, then we don't have the
		// hashCode() method for it either.
		if (testedClass == null) {
			return false;
		}

		// Try and get the hashCode method.
		Method hashCodeMethod;
		try {
			hashCodeMethod = testedClass.getMethod("hashCode"); // NON-NLS
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		// If we got a hashCode method then return true, else false.
		return hashCodeMethod != null;
	}


	/** Test whether or not the tested class implements a {@link
		Object#toString()} method.

		@return	True if the tested class implements a {@link Object#toString()}
				method, false otherwise.
	*/
	private boolean hasToString() {
		// If we don't know the class we're testing, then we don't have the
		// toString() method for it either.
		if (testedClass == null) {
			return false;
		}

		// Try and get the toString() method.
		Method toStringMethod;
		try {
			toStringMethod = testedClass.getMethod("toString"); // NON-NLS
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
			return false;
		}

		// If we got an toString() method then return true, else false.
		return toStringMethod != null;
	}
}
