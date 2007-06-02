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
import java.lang.reflect.Modifier;

/** Base test class for all JUnit test cases testing abstract classes to 
	inherit from. All abstract-class-specific tests should be added here.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of class to test.
*/
public abstract class AbstractClassTestCase<T>
extends ExemplarTestCase<T>
{
	/** Ensure that the abstract class is actually abstract. */
	public void testClassIsActuallyAbstract() {
		int modifiers = testedClass.getModifiers();
		assertTrue("This class is abstract", Modifier.isAbstract(modifiers));
	}

	/** Ensure that if any constructors exist then the are protected or
		private.
	*/
	public void testNoPublicConstructors() {
		Constructor<?>[] constructors = testedClass.getDeclaredConstructors();
		for (Constructor<?> constructor : constructors) {
			int mod = constructor.getModifiers();
			if (!(Modifier.isPrivate(mod) || Modifier.isProtected(mod))) {
				fail("Insufficiently private constructor: " + constructor);
			}
		}
	}

	/** {@inheritDoc} */
	@Override public void testAllMethodsBeingTested() {
		assertTrue("This is overridden to never fail", true);
	}
}
