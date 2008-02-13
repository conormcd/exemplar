// vim:filetype=java:ts=4
/*
	Copyright (c) 2006-2008
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
package junit.com.mcdermottroe.exemplar.input;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;

import com.mcdermottroe.exemplar.input.InputException;
import com.mcdermottroe.exemplar.input.InputUtils;
import com.mcdermottroe.exemplar.input.ParserException;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;
import junit.com.mcdermottroe.exemplar.input.dtd.ParserTest;

/** Test class for {@link com.mcdermottroe.exemplar.input.InputUtils}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class InputUtilsTest
extends UtilityClassTestCase<InputUtils>
{
	/** Get a collection of input languages and sample files in those languages
		for testing the parsing.

		@return	Some sample input languages and files.
	*/
	public static Map<String, Collection<File>> getSampleData() {
		// All the input modules to test.
		Map<String, Class<?>> inputModules = new HashMap<String, Class<?>>();
		inputModules.put("dtd", ParserTest.class);
		inputModules.put(
			"schema",
			junit.com.mcdermottroe.exemplar.input.schema.ParserTest.class
		);

		Map<String, Collection<File>> retVal =
			new HashMap<String, Collection<File>>();
		for (String inputModuleName : inputModules.keySet()) {
			Class<?> inputModule = inputModules.get(inputModuleName);

			// Find the getSamples() method.
			Method samplesMethod;
			try {
				samplesMethod = inputModule.getMethod("getSamples");
			} catch (NoSuchMethodException e) {
				assertNotNull("NoSuchMethodException was null", e);
				fail("Failed to find sample input.");
				break;
			}

			// Call getSamples()
			try {
				retVal.put(
					inputModuleName,
					(Collection<File>)samplesMethod.invoke(null)
				);
			} catch (IllegalAccessException e) {
				assertNotNull("InvocationTargetException was null", e);
				fail("Failed to access getSamples()");
				break;
			} catch (InvocationTargetException e) {
				assertNotNull("InvocationTargetException was null", e);
				fail("getSamples() threw an exception");
				break;
			}
		}
		return retVal;
	}

	/** {@link InputUtils#availableInputLanguages()}. */
	public void testAvailableInputLanguages() {
		SortedMap<String, String> langs = InputUtils.availableInputLanguages();
		assertNotNull("availableInputLanguages returned null", langs);
		assertFalse(
			"availableInputLanguages returned an empty Map",
			langs.isEmpty()
		);
		for (String key : langs.keySet()) {
			assertNotNull("Input type name was null", key);
			assertNotSame(
				"Input type name was a zero-length String",
				0,
				key.length()
			);
			String value = langs.get(key);
			assertNotNull("Input type description was null", value);
			assertNotSame(
				"Input type description was a zero-length String",
				0,
				value.length()
			);
		}
	}

	/** Test {@link InputUtils#parse(String, String)}. */
	public void testParse() {
		Map<String, Collection<File>> sampleData = getSampleData();
		for (String lang : sampleData.keySet()) {
			Collection<File> sampleFiles = sampleData.get(lang);
			for (File f : sampleFiles) {
				try {
					InputUtils.parse(f.getAbsolutePath(), lang);
				} catch (InputException e) {
					assertNotNull("InputException was null", e);
					fail("parse(String, String) threw an InputException");
				} catch (ParserException e) {
					assertNotNull("ParserException was null", e);
					e.printStackTrace();
					fail("parse(String, String) threw an ParserException");
				}
			}
		}
	}

	/** Test {@link InputUtils#parse(String, String)}. */
	public void testParseNonExistantType() {
		boolean fellThrough = false;
		try {
			InputUtils.parse("", "nonexistant.type");
			fellThrough = true;
		} catch (InputException e) {
			assertNotNull("InputException was null", e);
		} catch (ParserException e) {
			assertNotNull("ParserException was null", e);
		}
		assertFalse("parse(String, String) fell through", fellThrough);
	}
}
