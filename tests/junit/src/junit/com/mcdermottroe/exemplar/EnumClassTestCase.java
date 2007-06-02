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

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/** Test case class for enumerated types.

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of the {@link Enum} to test.
*/
public abstract class EnumClassTestCase<T extends Enum<T>>
extends ExemplarTestCase<T>
{
	/** The values for the entity. */
	protected T[] types;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		types = null;
	}

	/** No type may be null. */
	public void testNoNullTypes() {
		assertNotNull("Bad test", types);
		for (T type : types) {
			assertNotNull("Null type", type);
		}
	}

	/** No type name may be null. */
	public void testNoNullNames() {
		assertNotNull("Bad test", types);
		for (T type : types) {
			assertNotNull("Null type name", type.name());
		}
	}

	/** toString must return a good String. */
	public void testToString() {
		assertNotNull("Bad test", types);
		for (T type : types) {
			assertNotNull("toString() returned null", type.toString());
			assertNotSame(
				"toString returned the empty string",
				0,
				type.toString().length()
			);
		}
	}

	/** Test {@link Object#hashCode()}. */
	public void testHashCode() {
		assertNotNull("Bad test", types);
		Set<Integer> hashCodes = new HashSet<Integer>();
		for (T type : types) {
			hashCodes.add(type.hashCode());
		}
		assertEquals("hashCodes not unique", types.length, hashCodes.size());
	}

	/** Test {@link Enum#ordinal()}. */
	public void testOrdinal() {
		assertNotNull("Bad test", types);
		Set<Integer> ordinals = new HashSet<Integer>();
		for (T type : types) {
			ordinals.add(type.ordinal());
		}
		assertEquals("ordinals not unique", types.length, ordinals.size());
	}

	/** Test {@link Object#equals(Object)}. */
	public void testEquals() {
		assertNotNull("Bad test", types);
		for (int i = 0; i < types.length; i++) {
			for (int j = 0; j < types.length; j++) {
				if (i != j) {
					assertNotSame(
						"Types must never equal other types",
						types[i],
						types[j]
					);
				}
			}
		}
	}

	/** Test {@link Enum#compareTo(Object)}. */
	public void testCompareTo() {
		assertNotNull("Bad test", types);
		for (int i = 0; i < types.length; i++) {
			for (int j = 0; j < types.length; j++) {
				if (i != j) {
					assertNotSame(
						"Types must compare equally to other types",
						0,
						types[i].compareTo(types[j])
					);
				}
			}
		}
	}

	/** Test <code>Enum.values()</code>. */
	public void testValues() {
		assertNotNull("values() returned null", types);
	}

	/** Test <code>Enum#valueOf(String)</code>. */
	public void testValueOfString() {
		// Get the valueOf method
		Method valueOf = null;
		try {
			valueOf = testedClass.getMethod(
				"valueOf",
				String.class
			);
		} catch (NoSuchMethodException e) {
			assertNotNull("NoSuchMethodException was null", e);
			fail("Could not find Enum.valueOf(Class, String)");
		}
		assertNotNull("valueOf is null", valueOf);

		// Check that things match
		for (T type : types) {
			try {
				assertEquals(
					"valueOf(String) did not round-trip",
					type,
					valueOf.invoke(null, type.name())
				);
			} catch (IllegalAccessException e) {
				assertNull("IllegalAccessException was null", e);
				fail("valueOf(String) threw an IllegalAccessException");
			} catch (InvocationTargetException e) {
				assertNull("InvocationTargetException was null", e);
				fail("valueOf(String) threw an InvocationTargetException");
			}
		}
	}

	/** Test {@link Enum#valueOf(Class, String)}. */
	public void testValueOfClassString() {
		// Get the valueOf method
		Method valueOf = null;
		try {
			valueOf = testedClass.getMethod(
				"valueOf",
				Class.class,
				String.class
			);
		} catch (NoSuchMethodException e) {
			assertNotNull("NoSuchMethodException was null", e);
			fail("Could not find Enum.valueOf(Class, String)");
		}
		assertNotNull("valueOf is null", valueOf);

		// Check that everything matches
		for (T type : types) {
			Object enumValue = null;
			try {
				enumValue = valueOf.invoke(null, testedClass, type.name());
			} catch (IllegalAccessException e) {
				assertNotNull("IllegalAccessException was null", e);
				fail("IllegalAccessException caught");
			} catch (InvocationTargetException e) {
				assertNotNull("InvocationTargetException was null", e);
				fail("InvocationTargetException caught");
			}
			assertNotNull("enumValue is null", enumValue);

			assertSame(
				"valueOf did not return the correct value",
				type,
				enumValue
			);
		}
	}

	/** Test {@link Enum#getDeclaringClass()}. */
	public void testGetDeclaringClass() {
		assertNotNull("Bad test", types);
		for (T type : types) {
			assertEquals(
				"Declaring class not correct",
				testedClass,
				type.getDeclaringClass()
			);
		}
	}
}
