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

import java.text.MessageFormat;

import com.mcdermottroe.exemplar.Utils;

/** Test class for {@link com.mcdermottroe.exemplar.Utils}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class UtilsTest extends UtilityClassTestCase {
	/** Basic sanity check for {@link Utils#formatMessage(String, Object...)}.
	*/
	public void testFormatMessageOneArg() {
		String testName = "Testing formatMessage(String, Object)";

		String formatString = "{0}";
		String variable = "variable";
		if (variable.equals(Utils.formatMessage(formatString, variable))) {
			assertTrue(testName, true);
		} else {
			fail(testName);
		}
	}

	/** Basic sanity check for {@link Utils#formatMessage(String, Object...)}.
	*/
	public void testFormatMessageManyArgs() {
		String testName = "Testing formatMessage(String, Object...)";

		String formatMessage = "{{0}{}{1,date,long}}";
		String result = Utils.formatMessage(formatMessage, "foo", 0);
		String expectedResult =	"{foo{}" +
								MessageFormat.format("{0,date,long}", 0) +
								"}";

		assertEquals(testName, expectedResult, result);
	}

	/** Some basic tests for the method {@link
		Utils#xmlStringToJavaCanonicalForm(String)}.
	*/
	public void testXmlStringToJavaCanonicalForm() {
		String[] input = {
			"foo & bar",
			"&&#x0026;&",
			"&&#0038;&",
		};
		String[] expectedOutput = {
			"\\u0066\\u006f\\u006f\\u0020\\u0026\\u0020\\u0062\\u0061\\u0072",
			"\\u0026\\u0026\\u0026",
			"\\u0026\\u0026\\u0026",
		};
		assertEquals("Malformed test", input.length, expectedOutput.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				input[i],
				expectedOutput[i],
				Utils.xmlStringToJavaCanonicalForm(input[i])
			);
		}
	}
}
