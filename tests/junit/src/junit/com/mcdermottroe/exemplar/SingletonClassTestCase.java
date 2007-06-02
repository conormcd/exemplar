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
package junit.com.mcdermottroe.exemplar;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

/** Test class for all singleton classes.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of the utility class being tested.
*/
public abstract class SingletonClassTestCase<T>
extends ExemplarTestCase<T>
{
	/** Test to ensure that all of the constructors are private. */
	public void testAllConstructorsArePrivate() {
		Constructor<?>[] constructors = testedClass.getDeclaredConstructors();
		assertNotNull(
			"Class.getDeclaredConstructors() returned null",
			constructors
		);
		assertTrue(
			"There were no constructors for the class",
			constructors.length > 0
		);
		for (Constructor<?> c : constructors) {
			assertNotNull("Null constructor", c);
			int mods = c.getModifiers();
			assertTrue("Constructor was not private", Modifier.isPrivate(mods));
		}
	}

	/** A test to ensure that at least one static field with the same type as
		the class.
	*/
	public void testSingletonObjectExists() {
		Field[] fields = testedClass.getDeclaredFields();
		assertNotNull("Class.getDeclaredFields() == null", fields);
		assertTrue("No fields in the class", fields.length > 0);
		for (Field f : fields) {
			if (testedClass.equals(f.getType())) {
				if (Modifier.isStatic(f.getModifiers())) {
					return;
				}
			}
		}
		fail("No singleton object for this class");
	}
}
