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

		String testName = "There must be one, private, no-arg constructor.";
		Constructor[] constructors = testedClass.getDeclaredConstructors();

		// Make sure there's only one.
		if (constructors.length != 1) {
			fail(testName);
			return;
		}

		// Make sure that it's private.
		int modifiers = constructors[0].getModifiers();
		if (!(Modifier.isPrivate(modifiers))) {
			fail(testName);
			return;
		}

		// Make sure that it's a no-arg constructor.
		Class[] params = constructors[0].getParameterTypes();
		if (params.length != 0) {
			fail(testName);
			return;
		}

		assertTrue(testName, true);
	}

	/** All of the fields in a utility class must be declared static. */
	public void testAllFieldsStatic() {
		if (testedClass == null) {
			return;
		}

		String testName = "All fields in the class must be static.";
		Field[] fields = testedClass.getDeclaredFields();
		for (int i = 0; i < fields.length; i++) {
			int modifiers = fields[i].getModifiers();
			if	(!(Modifier.isStatic(modifiers))) {
				fail(testName);
				return;
			}
		}
		assertTrue(testName, true);
	}

	/** All of the methods in a utility class must be declared static. */
	public void testAllMethodsStatic() {
		if (testedClass == null) {
			return;
		}

		String testName = "All methods in the class must be static.";
		Method[] methods = testedClass.getDeclaredMethods();
		for (int i = 0; i < methods.length; i++) {
			int modifiers = methods[i].getModifiers();
			if	(!(Modifier.isStatic(modifiers))) {
				fail(testName);
				return;
			}
		}
		assertTrue(testName, true);
	}
}
