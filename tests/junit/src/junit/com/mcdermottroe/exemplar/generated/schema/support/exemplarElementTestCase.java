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
package junit.com.mcdermottroe.exemplar.generated.schema.support;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.mcdermottroe.exemplar.generated.schema.support.exemplarElement;

/** Test class for {@link exemplarElement}.

	@author	Conor McDermottroe
	@since	0.2
	@param	<T>	The type of {@link exemplarElement} to test.
*/
public abstract class exemplarElementTestCase<T extends exemplarElement<T>>
extends XMLComponentTestCase<T>
{
	/** Generic test for getters in subclasses of {@link exemplarElement}.

		@param	methodName	The name of the get method to test.
		@return				True if the test succeeds. If the test fails it will
							assert just like a normal JUnit test.
	*/
	protected boolean getterTest(String methodName) {
		for (T sample : samples()) {
			if (sample != null) {
				// Get the getter
				Method getMethod;
				try {
					getMethod = sample.getClass().getMethod(methodName);
				} catch (NoSuchMethodException e) {
					assertNotNull("NoSuchMethodException was null", e);
					fail("No such method in the tested object's class");
					return false;
				}

				// Invoke the method and check for exceptions
				try {
					getMethod.invoke(sample);
				} catch (IllegalAccessException e) {
					assertNotNull("IllegalAccessException was null", e);
					fail("Non-public get method in tested class");
					return false;
				} catch (InvocationTargetException e) {
					assertNotNull("InvocationTargetException was null", e);
					fail("Calling get method threw an exception");
					return false;
				}
			}
		}
		return true;
	}

	/** Generic test for setters in subclasses of {@link exemplarElement}.

		@param	methodName	The name of the set method to test.
		@param	values		One or more values to test the setter with.
		@return				True if the test succeeds. If the test fails it will
							assert just like a normal JUnit test.
	*/
	protected boolean setterTest(String methodName, String... values) {
		String[] testValues;
		if (values.length >= 1) {
			testValues = values;
		} else {
			testValues = new String[] {null, "foo", "bar", "baz", };
		}
		for (T sample : samples()) {
			if (sample != null) {
				// Get the setter
				Method setMethod;
				try {
					setMethod = sample.getClass().getMethod(
						methodName, String.class
					);
				} catch (NoSuchMethodException e) {
					assertNotNull("NoSuchMethodException was null", e);
					fail("No such method in the tested object's class");
					return false;
				}

				// Get the getter
				Method getMethod;
				try {
					getMethod = sample.getClass().getMethod(
						methodName.replaceFirst("^set", "get")
					);
				} catch (NoSuchMethodException e) {
					assertNotNull("NoSuchMethodException was null", e);
					fail("No such method in the tested object's class");
					return false;
				}

				// Get the original value of the property
				String originalValue = null;
				try {
					Object o = getMethod.invoke(sample);
					if (o != null) {
						originalValue = o.toString();
					}
				} catch (IllegalAccessException e) {
					assertNotNull("IllegalAccessException was null", e);
					fail("Non-public get method in tested class");
					return false;
				} catch (InvocationTargetException e) {
					assertNotNull("InvocationTargetException was null", e);
					fail("Calling get method threw an exception");
					return false;
				}
				for (String testValue : testValues) {
					// Set the property to the test value
					try {
						setMethod.invoke(sample, testValue);
					} catch (IllegalAccessException e) {
						assertNotNull("IllegalAccessException was null", e);
						fail("Non-public set method in tested class");
						return false;
					} catch (InvocationTargetException e) {
						assertNotNull("InvocationTargetException was null", e);
						fail("Calling set method threw an exception");
						return false;
					}

					// Get the value of the property to check that it stuck
					String propValue = null;
					try {
						Object o = getMethod.invoke(sample);
						if (o != null) {
							propValue = o.toString();
						}
					} catch (IllegalAccessException e) {
						assertNotNull("IllegalAccessException was null", e);
						fail("Non-public get method in tested class");
						return false;
					} catch (InvocationTargetException e) {
						assertNotNull("InvocationTargetException was null", e);
						fail("Calling get method threw an exception");
						return false;
					}
					assertEquals(
						"Value did not stick",
						testValue,
						propValue
					);

					// Re-set the value to the original value
					try {
						setMethod.invoke(sample, originalValue);
					} catch (IllegalAccessException e) {
						assertNotNull("IllegalAccessException was null", e);
						fail("Non-public set method in tested class");
						return false;
					} catch (InvocationTargetException e) {
						assertNotNull("InvocationTargetException was null", e);
						fail("Calling set method threw an exception");
						return false;
					}
				}

				// Get the value of the property to check that it was reset to
				// the original value
				String propValue = null;
				try {
					Object o = getMethod.invoke(sample);
					if (o != null) {
						propValue = o.toString();
					}
				} catch (IllegalAccessException e) {
					assertNotNull("IllegalAccessException was null", e);
					fail("Non-public get method in tested class");
					return false;
				} catch (InvocationTargetException e) {
					assertNotNull("InvocationTargetException was null", e);
					fail("Calling get method threw an exception");
					return false;
				}
				assertEquals(
					"Value did not reset",
					originalValue,
					propValue
				);
			}
		}
		return true;
	}
}
