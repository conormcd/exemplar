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

import junit.framework.TestCase;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** A root class for all tests in Exemplar.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class ExemplarTestCase extends TestCase {
	/** The {@link Class} of the class which is being tested by the runtime 
		type of this {@link ExemplarTestCase} object.
	*/
	protected Class testedClass;

	/** Set up the JUnit test.

		@throws Exception	if {@link TestCase#setUp()} throws one.
		@see				#testedClass
		@see				TestCase#setUp()
	*/
	@Override public void setUp() throws Exception {
		super.setUp();

		testedClass = null;
		String className = getClass().getName();
		className = className.replaceFirst(
			Constants.Regex.JUNIT_PACKAGE_PREFIX,
			""
		);
		className = className.replaceFirst("Test$", "");
		try {
			testedClass = Class.forName(className);
		} catch (ClassNotFoundException e) {
			fail(
				"Failed to find class " +
				className +
				Constants.Character.SPACE +
				Constants.Character.LEFT_PAREN +
				e.toString() +
				Constants.Character.RIGHT_PAREN
			);
			return;
		}

		Message.localise();
		Options.set("debug", "true");
		Options.setUIFinished();
	}

	/** When this method is inherited by every test it will make sure that all 
		of the tested classes are in the same package as {@link Constants}
		which is in the root package.

		@see Constants
	*/
	public void testCorrectPackage() {
		if (testedClass != null) {
			assertTrue(
				"Checking tested class package is rooted in the base package.",
				testedClass.getPackage().getName().startsWith(Constants.PACKAGE)
			);
		}
	}
}
