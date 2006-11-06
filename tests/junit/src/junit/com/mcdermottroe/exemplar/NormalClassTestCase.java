// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;

/**	Base class for JUnit tests that test classes which produce "normal"
	classes. That includes all classes that are not abstract, exceptions or
	utility classes.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class NormalClassTestCase extends ExemplarTestCase {
	/** Flag to set if we allow 'public static final' members in the tested
		class. This should only be permitted in generated classes.
	*/
	protected boolean allowPublicStaticMembers;

	/** Flag to set if we don't want to run any tests on the {@link
		Object#equals(Object)} method of the {@link Object}.
	*/
	protected boolean ignoreEqualsTests;

	/** Flag to set if we don't want to run any tests on the {@link
		Object#hashCode()} method of the {@link Object}.
	*/
	protected boolean ignoreHashCodeTests;

	/** Flag to set if we don't want to run any tests on the {@link
		Object#toString()} method of the {@link Object}.
	*/
	protected boolean ignoreToStringTests;

	/** A selection of sample {@link Object}s which can be used within the
		tests. This allows more realistic, but still generalised, testing to be
		performed and avoids only testing the no-arg constructor. This {@link
		List} should be initialised in the {@link #setUp()} method.
	*/
	protected List<Object> sampleObjects;

	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		allowPublicStaticMembers = false;
		ignoreEqualsTests = false;
		ignoreHashCodeTests = false;
		ignoreToStringTests = false;
		sampleObjects = null;
	}

	/** This test ensures that there are no private constructors declared. This
		is to allow for better extensibility. Only utility classes should have
		provate constructors as they should never be subclassed.
	*/
	public void testAllConstructorsNonPrivate() {
		String testName = "No constructors may be private.";

		if (testedClass == null) {
			fail(testName);
			return;
		}

		// Make sure that they're all non-private.
		for (Constructor constructor : testedClass.getDeclaredConstructors()) {
			int modifiers = constructor.getModifiers();
			if (Modifier.isPrivate(modifiers)) {
				assertFalse(testName, Modifier.isPrivate(modifiers));
				return;
			}
		}

		assertTrue(testName, true);
	}

	/** All fields must be either 'private', 'protected'
		or (if allowed by the {@link #allowPublicStaticMembers} flag) 'public
		static final'.
	*/
	public void testAllFieldsPrivateOrProtectedOrPublicStaticFinal() {
		String testName = "All fields must be 'private' or 'protected'";
		if (allowPublicStaticMembers) {
			testName += " or 'public static final'";
		}

		if (testedClass == null) {
			fail(testName);
			return;
		}

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
					fail(testName);
					return;
				}
			}
		}
		assertTrue(testName, true);
	}

	/** This ensures that the result of {@link Object#hashCode()} does not
		change over time.
	*/
	public void testHashCodeConsistentOverTime() {
		String testName = "Ensure hashCode() is consistent over time";

		if (ignoreHashCodeTests) {
			return;
		}
		if (!hasHashCode()) {
			fail(testName);
			return;
		}

		int repeat = 5;

		if (sampleObjects != null) {
			for (Object o : sampleObjects) {
				int initialHashCode = o.hashCode();
				for (int i = 0; i < (repeat - 1); i++) {
					if (initialHashCode != o.hashCode()) {
						fail(testName);
						return;
					}
				}
			}

			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object o;
			try {
				o = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}

			int initialHashCode = o.hashCode();
			for (int i = 0; i < (repeat - 1); i++) {
				if (initialHashCode != o.hashCode()) {
					fail(testName);
					return;
				}
			}

			assertTrue(testName, true);
		}
	}

	/** This ensures that where x.equals(y), x.hashCode() == y.hashCode().
	*/
	public void testHashCodeConsistentWithEquals() {
		String testName = "Ensure hashCode() is consistent with equals()";

		if (ignoreHashCodeTests) {
			return;
		}
		if (!hasHashCode()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object a : sampleObjects) {
				for (Object b : sampleObjects) {
					if (a.equals(b) && (a.hashCode() != b.hashCode())) {
						fail(testName);
						return;
					}
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object oa;
			Object ob;
			try {
				oa = testedClass.newInstance();
				ob = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}
			assertEquals(testName, oa.hashCode(), ob.hashCode());
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		reflexive. That is, for all non-null reference values x, x.equals(x)
		must return true.
	*/
	public void testEqualsReflexive() {
		String testName = "Test that equals() is reflexive";

		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object o : sampleObjects) {
				if (o != null && !o.equals(o)) {
					fail(testName);
					return;
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object o;
			try {
				o = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}
			assertTrue(testName, o.equals(o));
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		symmetric. That is, for any non-null reference values x and y,
		x.equals(y)  should return true if and only if y.equals(x) returns
		true.
	*/
	public void testEqualsSymmetric() {
		String testName = "Test that equals() is symmetric";

		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object a : sampleObjects) {
				for (Object b : sampleObjects) {
					if (a.equals(b) && !(b.equals(a))) {
						fail(testName);
						return;
					}
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object oa;
			Object ob;
			try {
				oa = testedClass.newInstance();
				ob = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}
			assertEquals(testName, oa.equals(ob), ob.equals(oa));
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		transitive. That is, for any non-null reference values x, y, and z, if
		x.equals(y) returns true and y.equals(z) returns true then x.equals(z)
		must return true.
	*/
	public void testEqualsTransitive() {
		String testName = "Test that equals() is transitive";

		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object a : sampleObjects) {
				for (Object b : sampleObjects) {
					for (Object c : sampleObjects) {
						if (a.equals(b) && b.equals(c) && !(a.equals(c))) {
							fail(testName);
							return;
						}
					}
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object oa;
			Object ob;
			try {
				oa = testedClass.newInstance();
				ob = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}
			assertEquals(testName, oa.equals(ob), ob.equals(oa));
		}
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		consistent. That is, for all non-null reference values x and y,
		multiple invocations of x.equals(y) consistently return true or false,
		provided no information used in the equals comparisons on the objects
		is modified.
	*/
	public void testEqualsConsistent() {
		String testName = "Test that equals() is consistent";

		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail(testName);
			return;
		}

		int repeats = 5;

		if (sampleObjects != null) {
			for (Object a : sampleObjects) {
				for (Object b : sampleObjects) {
					boolean initialResult = a.equals(b);
					for (int i = 0; i < (repeats - 1); i++) {
						if (initialResult != a.equals(b)) {
							fail(testName);
							return;
						}
					}
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object oa;
			Object ob;
			try {
				oa = testedClass.newInstance();
				ob = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}

			boolean initialResult = oa.equals(ob);
			for (int i = 0; i < (repeats - 1); i++) {
				if (initialResult != oa.equals(ob)) {
					fail(testName);
					return;
				}
			}

			assertTrue(testName, true);
		}
	}

	/**	Every call to equals where the parameter is null must return false. */
	public void testEqualsNullFalse() {
		String testName = "All calls o.equals(null) must return false.";

		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object o : sampleObjects) {
				if (o.equals(null)) {
					fail(testName);
					return;
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				fail(testName);
				return;
			}
			if (!hasNoArgConstructor()) {
				fail(testName);
				return;
			}

			Object o;
			try {
				o = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}
			assertFalse(testName, o.equals(null));
		}
	}

	/** This ensures that for all non-null {@link Object}s x and y,
		x.toString().equals(y.toString()) iff x.equals(y). This is not required
		by the contract of {@link Object#toString()} but is an all-round good
		idea.
	*/
	public void testToStringConsistent() {
		String testName = "Test that toString() is consistent with equals()";

		if (ignoreToStringTests) {
			return;
		}
		if (!hasToString()) {
			fail(testName);
			return;
		}

		if (sampleObjects != null) {
			for (Object a : sampleObjects) {
				for (Object b : sampleObjects) {
					if (a != null) {
						if	(
								a.equals(b) !=
								(a.toString().equals(b.toString()))
							)
						{
							fail(testName);
							return;
						}
					}
				}
			}
			assertTrue(testName, true);
		} else {
			if (testedClass == null) {
				return;
			}
			if (!hasNoArgConstructor()) {
				return;
			}

			Object oa;
			Object ob;
			try {
				oa = testedClass.newInstance();
				ob = testedClass.newInstance();
			} catch (IllegalAccessException e) {
				fail("May not access constructor.");
				return;
			} catch (InstantiationException e) {
				fail("Failed to instantiate class.");
				return;
			}

			assertEquals(testName, oa.toString(), ob.toString());
		}
	}

	/** This checks to see if the tested class has a no-arg constructor. If
		not, we can't do some of the tests.

		@return	True if the tested class has a no-arg constructor, false
				otherwise.
	*/
	private boolean hasNoArgConstructor() {
		if (testedClass == null) {
			return false;
		}

		// Try and get the no-arg constructor.
		Constructor noArgConstructor;
		try {
			noArgConstructor = testedClass.getDeclaredConstructor();
		} catch (NoSuchMethodException e) {
			return false;
		}

		// Make sure it's public, if not we can't use it.
		return Modifier.isPublic(noArgConstructor.getModifiers());
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
			equalsMethod = testedClass.getMethod("equals", Object.class);
		} catch (NoSuchMethodException e) {
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
			hashCodeMethod = testedClass.getMethod("hashCode");
		} catch (NoSuchMethodException e) {
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
			toStringMethod = testedClass.getMethod("toString");
		} catch (NoSuchMethodException e) {
			return false;
		}

		// If we got an toString() method then return true, else false.
		return toStringMethod != null;
	}
}
