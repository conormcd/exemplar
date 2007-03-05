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
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

/**	Base class for JUnit tests that test classes which produce "normal"
	classes. That includes all classes that are not abstract, exceptions or
	utility classes.

	@param	<T>	The type of the class being tested.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class NormalClassTestCase<T>
extends ExemplarTestCase<T>
{
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
	private List<T> sampleObjects;

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
		if (testedClass == null) {
			fail("Tested class is null");
			return;
		}

		// Make sure that they're all non-private.
		for (Constructor constructor : testedClass.getDeclaredConstructors()) {
			int modifiers = constructor.getModifiers();
			if (Modifier.isPrivate(modifiers)) {
				assertFalse(
					"Private constructor found",
					Modifier.isPrivate(modifiers)
				);
				return;
			}
		}

		assertTrue("No private constructors found", true);
	}

	/** All fields must be either 'private', 'protected'
		or (if allowed by the {@link #allowPublicStaticMembers} flag) 'public
		static final'.
	*/
	public void testAllFieldsPrivateOrProtectedOrPublicStaticFinal() {
		if (testedClass == null) {
			fail("Tested class is null");
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
					fail("Overly public field found");
					return;
				}
			}
		}
		assertTrue("All fields are private enough", true);
	}

	/** This ensures that the result of {@link Object#hashCode()} does not
		change over time.
	*/
	public void testHashCodeConsistentOverTime() {
		if (ignoreHashCodeTests) {
			return;
		}
		if (!hasHashCode()) {
			fail("Class does not defined hashCode()");
			return;
		}

		int repeat = 5;

		for (T o : samples()) {
			if (o != null) {
				int initialHashCode = o.hashCode();
				for (int i = 0; i < (repeat - 1); i++) {
					if (initialHashCode != o.hashCode()) {
						fail("hashCode() value changed!");
						return;
					}
				}
			}
		}

		assertTrue("hashCode() value remained consistent over time", true);
	}

	/** This ensures that where x.equals(y), x.hashCode() == y.hashCode().
	*/
	public void testHashCodeConsistentWithEquals() {
		if (ignoreHashCodeTests) {
			return;
		}
		if (!hasHashCode()) {
			fail("Class does not defined hashCode()");
			return;
		}

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null) {
					if (a.equals(b) && (a.hashCode() != b.hashCode())) {
						fail(
							"Two instances are equal but have different" +
							" hash codes."
						);
						return;
					}
				}
			}
		}
		assertTrue("All equal objects have equal hashCodes", true);
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		reflexive. That is, for all non-null reference values x, x.equals(x)
		must return true.
	*/
	public void testEqualsReflexive() {
		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail("Class does not define equals(Object)");
			return;
		}

		for (T o : samples()) {
			if (o != null && !o.equals(o)) {
				fail("equals(Object) is not reflexive");
				return;
			}
		}
		assertTrue("equals(Object) is reflexive", true);
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		symmetric. That is, for any non-null reference values x and y,
		x.equals(y)  should return true if and only if y.equals(x) returns
		true.
	*/
	public void testEqualsSymmetric() {
		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail("Class does not define equals()");
			return;
		}

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					if (a.equals(b) && !(b.equals(a))) {
						fail("equals() is not symmetric");
						return;
					}
				}
			}
		}
		assertTrue("equals() is symmetric", true);
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		transitive. That is, for any non-null reference values x, y, and z, if
		x.equals(y) returns true and y.equals(z) returns true then x.equals(z)
		must return true.
	*/
	public void testEqualsTransitive() {
		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail("Class does not define equals(Object)");
			return;
		}

		for (T a : samples()) {
			for (T b : samples()) {
				for (T c : samples()) {
					if (a != null && b != null) {
						if (a.equals(b) && b.equals(c) && !(a.equals(c))) {
							fail("equals(Object) is not transitive");
							return;
						}
					}
				}
			}
		}
		assertTrue("equals(Object) is transitive", true);
	}

	/** Methods implementing {@link Object#equals(Object)} are required to be
		consistent. That is, for all non-null reference values x and y,
		multiple invocations of x.equals(y) consistently return true or false,
		provided no information used in the equals comparisons on the objects
		is modified.
	*/
	public void testEqualsConsistent() {
		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail("Class does not define equals(Object)");
			return;
		}

		int repeats = 5;

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null) {
					boolean initialResult = a.equals(b);
					for (int i = 0; i < (repeats - 1); i++) {
						if (initialResult != a.equals(b)) {
							fail("equals(Object) is not consistent over time.");
							return;
						}
					}
				}
			}
		}
		assertTrue("equals(Object) is consistent over time.", true);
	}

	/**	Every call to equals where the parameter is null must return false. */
	public void testEqualsNullFalse() {
		if (ignoreEqualsTests) {
			return;
		}
		if (!hasEquals()) {
			fail("Class does not implement equals(Object)");
			return;
		}

		for (T o : samples()) {
			if (o != null) {
				if (o.equals(null)) {
					fail("equals(null) returned true");
					return;
				}
			}
		}
		assertTrue("equals(null) returned false for all samples.", true);
	}

	/** This ensures that for all non-null {@link Object}s x and y,
		x.toString().equals(y.toString()) iff x.equals(y). This is not required
		by the contract of {@link Object#toString()} but is an all-round good
		idea.
	*/
	public void testToStringConsistent() {
		if (ignoreToStringTests) {
			return;
		}
		if (!hasToString()) {
			fail("Class does not define toString()");
			return;
		}

		for (T a : samples()) {
			for (T b : samples()) {
				if (a != null && b != null) {
					if (a.equals(b) != (a.toString().equals(b.toString()))) {
						fail("toString() isn't consistent with equals(Object)");
						return;
					}
				}
			}
		}
		assertTrue("toString() is consistent with equals(Object)", true);
	}

	/** Ensure that all classes are {@link Cloneable}. */
	public void testImplementsCloneable() {
		if (Cloneable.class.isAssignableFrom(testedClass)) {
			assertTrue("Class implements Cloneable", true);
		} else {
			fail("Class does not implement Cloneable");
		}
	}

	/** This ensures that it is possible to call {@link Object#clone()} and not
		have an exception thrown.
	*/
	public void testClone() {
		for (T sample : samples()) {
			if (sample != null) {
				// Invoke the clone method.
				Object clone;
				try {
					clone = doClone(sample);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					fail("clone() is not accessible");
					return;
				} catch (InvocationTargetException  e) {
					e.printStackTrace();
					fail("Calling clone() caused an Exception to be thrown");
					return;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					fail("Class does not implement clone()");
					return;
				}
			}
		}
		assertTrue("clone() is safe to call", true);
	}

	/** This ensures that it is possible to call {@link Object#clone()} and get
		an object which is equal to, but not the same (referentially) to the
		original object.
	*/
	public void testCloneProducesIdenticalObject() {
		for (T sample : samples()) {
			if (sample != null) {
				// Invoke the clone method.
				Object clone;
				try {
					clone = doClone(sample);
				} catch (IllegalAccessException e) {
					e.printStackTrace();
					fail("clone() is not accessible");
					return;
				} catch (InvocationTargetException  e) {
					e.printStackTrace();
					fail("Calling clone() caused an Exception to be thrown");
					return;
				} catch (NoSuchMethodException e) {
					e.printStackTrace();
					fail("Class does not implement clone()");
					return;
				}

				// Fail if the clone is the same object
				if (sample == clone) {
					fail("The clone is the same object");
					return;
				}

				// Fail if the original and clone are not equal
				if (!(sample.equals(clone))) {
					fail("The clone is not equal to the original");
					return;
				}
			}
		}
		assertTrue("clone() produces a good result", true);
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
				if (!(sample.equals(o))) {
					fail("Round-tripped object is not equal to the original");
					return;
				}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
				fail("ClassNotFoundException thrown when (de)serializing.");
				return;
			} catch (IOException e) {
				e.printStackTrace();
				fail("IOException thrown when serializing/deserializing.");
				return;
			}
		}
		assertTrue("Class is serializable", true);
	}

	/** Do a typed clone operation.

		@param	object						The object to clone.
		@return								A clone of <code>object</code>.
		@throws IllegalAccessException		if the clone method does not have
											public access.
		@throws InvocationTargetException	if the clone method throws an
											exception.
		@throws NoSuchMethodException		if there is no clone method defined
											for <code>object</code>.
	*/
	@SuppressWarnings("unchecked")
	protected T doClone(T object)
	throws	IllegalAccessException, InvocationTargetException,
			NoSuchMethodException
	{
		// Get the clone method of the object
		Class c = object.getClass();
		Class[] noArgs = {};
		Method cloneMethod = c.getMethod("clone", noArgs);

		// Invoke the clone method.
		Object[] noArg = {};
		Object clone = cloneMethod.invoke(object, noArg);

		// Cast to T
		return (T)clone;
	}

	/** Add a sample to the collection of sample objects.

		@param	sample	The sample object to add.
	*/
	protected void addSample(T sample) {
		if (sampleObjects == null) {
			sampleObjects = new ArrayList<T>();

			// Add nulls to shake out as many NPEs as possible.
			sampleObjects.add(null);
			sampleObjects.add(null);
		}

		// Add two copies to make sure that the equality tests get exercised
		// fully.
		sampleObjects.add(sample);
		sampleObjects.add(sample);

		// Try to add two clones
		try {
			sampleObjects.add(doClone(sample));
			sampleObjects.add(doClone(sample));
		} catch (IllegalAccessException e) {
			// Ignore
		} catch (InvocationTargetException e) {
			// Ignore
		} catch (NoSuchMethodException e) {
			// Ignore
		}
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
