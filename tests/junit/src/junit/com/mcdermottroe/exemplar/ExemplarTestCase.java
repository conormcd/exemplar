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

import java.io.File;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.SortedSet;
import java.util.TreeSet;

import junit.framework.TestCase;

import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.PACKAGE;

/** A root class for all tests in Exemplar.

	@param	<T>	The type of class being tested.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class ExemplarTestCase<T>
extends TestCase
{
	/** This is the message to use when an always-pass assert is used. */
	protected static final String DELIBERATE_PASS = "Deliberate pass, no test.";

	/** A {@link File} for the temp directory. */
	protected static final File TMP = new File(
		System.getProperty("java.io.tmpdir")
	);

	/** The {@link Class} of the class which is being tested by the runtime
		type of this {@link ExemplarTestCase} object.
	*/
	protected Class<?> testedClass;

	/** Set up the JUnit test.

		@throws Exception	if {@link TestCase#setUp()} throws one.
		@see				#testedClass
		@see				TestCase#setUp()
	*/
	@Override public void setUp() throws Exception {
		super.setUp();

		testedClass = getTestedClass();
		assertNotNull("Failed to calculate the class to test", testedClass);

		assertNotNull("TMP is null", TMP);
		assertTrue("TMP dir does not exist", TMP.exists());

		Options.reset();
		Message.localise();
		Log.clearHandlers();
	}

	/** Calculate the class to be tested. The default version of this method
		should be sufficient for almost all cases.

		@return	The {@link Class} of the class to test.
	*/
	protected Class<?> getTestedClass() {
		Class<?> c = null;
		String className = getClass().getName();
		if (className.startsWith("junit.")) {
			className = className.substring(6);
		}
		if (className.endsWith("Test")) {
			className = className.substring(0, className.length() - 4);
		}
		try {
			c = Class.forName(className);
		} catch (ClassNotFoundException e) {
			fail(
				"Failed to find class " +
				className +
				SPACE +
				LEFT_PAREN +
				e.toString() +
				RIGHT_PAREN
			);
		}
		return c;
	}

	/** When this method is inherited by every test it will make sure that all
		of the tested classes are in the same package as {@link
		com.mcdermottroe.exemplar.Constants} which is in the root package.

		@see com.mcdermottroe.exemplar.Constants
	*/
	public void testCorrectPackage() {
		if (testedClass != null) {
			assertTrue(
				"Checking tested class package is rooted in the base package.",
				testedClass.getPackage().getName().startsWith(PACKAGE)
			);
		}
	}

	/** Test that all of the methods in the tested class have at least some
		testing methods for them.
	*/
	public void testAllMethodsBeingTested() {
		Method[] methods = testedClass.getDeclaredMethods();
		SortedSet<String> missingMethods = new TreeSet<String>();
		for (Method m : methods) {
			String methodName = m.getName();
			if (!Modifier.isPublic(m.getModifiers())) {
				continue;
			}
			if (methodName.startsWith("_")) {
				continue;
			}
			String testMethodName =	"test" +
									Strings.upperCaseFirst(methodName);

			boolean found = false;
			Method[] testMethods = getClass().getMethods();
			for (Method testMethod : testMethods) {
				if (testMethod.getName().startsWith(testMethodName)) {
					found = true;
					break;
				}
			}

			if (!found) {
				missingMethods.add(methodName);
			}
		}

		String missing = Strings.join(
			String.valueOf(COMMA) + SPACE,
			missingMethods
		);
		assertTrue(
			"Missing test methods for: " + missing, missingMethods.isEmpty()
		);
	}
}
