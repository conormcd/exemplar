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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mcdermottroe.exemplar.Exception;

/** Test class for all exception classes.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class ExceptionClassTestCase extends ExemplarTestCase {
	/** Test to ensure that Exception classes inherit from {@link Throwable}
		via the exemplar version of {@link Exception} rather than from the
		standard {@link java.lang.Exception}.
	*/
	public void testInheritsFromExemplarException() {
		assertTrue(
			"Exceptions must inherit from exemplar's exception class",
			Exception.class.isAssignableFrom(testedClass)
		);
	}

	/** Test to ensure that {@link Exception} classes have only four 
		constructors. Tests to check that each constructor is in the right
		format follow.

		@see #testNoArgConstructor()
		@see #testOneStringConstructor()
		@see #testOneThrowableConstructor()
		@see #testStringThrowableConstructor()
		@see Exception
	*/
	public void testExactlyFourConstructors() {
		if (testedClass == null) {
			return;
		}

		Constructor[] constructors = testedClass.getDeclaredConstructors();
		assertEquals(
			"Exceptions must only have 4 constructors",
			4,
			constructors.length
		);
	}

	/** Test that a no-arg constructor is available and can be used to create 
		an instance of the {@link Exception} class.

		@see Exception#Exception()
	*/
	public void testNoArgConstructor() {
		if (testedClass == null) {
			return;
		}

		Object o;
		try {
			o = testedClass.newInstance();
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
			return;
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
			return;
		}

		assertNotNull("The no-arg constructor works.", o);
	}

	/** Test that a constructor which takes a single argument of type {@link 
		String} is available and can be used to create an instance of the
		{@link Exception} class.

		@see Exception#Exception(String)
	*/
	public void testOneStringConstructor() {
		if (testedClass == null) {
			return;
		}

		Constructor oneString;
		try {
			oneString = testedClass.getDeclaredConstructor(String.class);
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (String) constructor.");
			return;
		}

		Object o;
		try {
			o = oneString.newInstance("test");
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
			return;
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
			return;
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
			return;
		}

		assertNotNull("The (String) constructor works.", o);
	}

	/** Test that a constructor which takes a single argument of type {@link 
		Throwable} is available and can be used to create an instance of the
		{@link Exception} class.

		@see Exception#Exception(Throwable)
	*/
	public void testOneThrowableConstructor() {
		if (testedClass == null) {
			return;
		}

		Constructor oneThrowable;
		try {
			oneThrowable = testedClass.getDeclaredConstructor(Throwable.class);
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (Throwable) constructor.");
			return;
		}

		Object o;
		try {
			o = oneThrowable.newInstance(new Throwable("test"));
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
			return;
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
			return;
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
			return;
		}

		assertNotNull("The (Throwable) constructor works.", o);
	}

	/** Test that a constructor which takes two arguments, a {@link String} and
		a {@link Throwable} in that order, is available and can be used to
		create an instance of the {@link Exception} class.

		@see Exception#Exception(String, Throwable)
	*/
	public void testStringThrowableConstructor() {
		if (testedClass == null) {
			return;
		}

		Constructor stringThrowable;
		try {
			stringThrowable = testedClass.getDeclaredConstructor(
				String.class,
				Throwable.class
			);
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (String, Throwable) constructor.");
			return;
		}

		Object o;
		try {
			o = stringThrowable.newInstance("test", new Throwable("test"));
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
			return;
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
			return;
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
			return;
		}

		assertNotNull("The (String, Throwable) constructor works.", o);
	}

	/** Test that there are no extra methods declared. All methods in exception
		classes should be declared in {@link Exception}.
	*/
	public void testNoMethods() {
		if (testedClass == null) {
			return;
		}

		Method[] methods = testedClass.getDeclaredMethods();
		assertEquals("No methods for this exception", methods.length, 0);
	}
}
