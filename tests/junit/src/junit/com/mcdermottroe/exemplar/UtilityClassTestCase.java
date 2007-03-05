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
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/** Test class for all utility classes (classes with no public constructors and
	with all fields and methods declared static).

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class UtilityClassTestCase extends ExemplarTestCase {
	/** Utility classes must only have one constructor and it must be private.
		This prevents someone from accidentally instantiating the class.
	*/
	public void testOnePrivateConstructor() {
		if (testedClass == null) {
			return;
		}

		Constructor[] constructors = testedClass.getDeclaredConstructors();

		// Make sure there's only one.
		if (constructors.length != 1) {
			fail("There is more or less than one constructor");
			return;
		}

		// Make sure that it's private.
		if (!(Modifier.isPrivate(constructors[0].getModifiers()))) {
			fail("The constructor is not private");
			return;
		}

		// Make sure that it's a no-arg constructor.
		assertTrue(
			"The constructor takes no arguments",
			constructors[0].getParameterTypes().length == 0
		);
	}

	/** All of the fields in a utility class must be declared static. */
	public void testAllFieldsStatic() {
		if (testedClass == null) {
			return;
		}

		for (Field field : testedClass.getDeclaredFields()) {
			if (!(Modifier.isStatic(field.getModifiers()))) {
				fail(field.getName() + " is not static");
				return;
			}
		}
		assertTrue("All fields are static", true);
	}

	/** All of the methods in a utility class must be declared static. */
	public void testAllMethodsStatic() {
		if (testedClass == null) {
			return;
		}

		for (Method method : testedClass.getDeclaredMethods()) {
			if (!(Modifier.isStatic(method.getModifiers()))) {
				fail(method.getName() + " is not static");
				return;
			}
		}
		assertTrue("All methods are static", true);
	}
}
