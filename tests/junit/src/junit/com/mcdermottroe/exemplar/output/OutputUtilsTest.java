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
package junit.com.mcdermottroe.exemplar.output;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementContentModel;
import com.mcdermottroe.exemplar.model.XMLElementContentType;
import com.mcdermottroe.exemplar.model.XMLMarkupDeclaration;
import com.mcdermottroe.exemplar.output.LanguageAPIPair;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Files;
import com.mcdermottroe.exemplar.utils.Strings;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link com.mcdermottroe.exemplar.output.OutputUtils}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class OutputUtilsTest
extends UtilityClassTestCase<OutputUtils>
{
	/** A test {@link File} to use. */
	private File testFile = null;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		// Create the temp file
		testFile = new File(TMP, getClass().getName());
	}

	/** Test {@link OutputUtils#availableOutputAPIs()}. */
	public void testAvailableOutputAPIs() {
		Map<String, String> apis = OutputUtils.availableOutputAPIs();
		assertNotNull("availableOutputAPIs returns null", apis);
		assertTrue("availableOutputAPIs returns an empty Map", !apis.isEmpty());
		for (String api : apis.keySet()) {
			assertNotNull("An API was null", api);
			assertTrue("An API was a zero-length String", api.length() > 0);

			String desc = apis.get(api);
			assertNotNull("An API description was null", desc);
			assertTrue(
				"An API description was a zero-length String",
				desc.length() > 0
			);
		}
	}

	/** Test {@link OutputUtils#availableOutputLanguages()}. */
	public void testAvailableOutputLanguages() {
		Map<String, String> languages = OutputUtils.availableOutputLanguages();
		assertNotNull("availableOutputLanguages returns null", languages);
		assertTrue(
			"availableOutputLanguages returns an empty Map",
			!languages.isEmpty()
		);
		for (String language : languages.keySet()) {
			assertNotNull("A language was null", language);
			assertTrue(
				"A language was a zero-length String",
				language.length() > 0
			);

			String desc = languages.get(language);
			assertNotNull("A language description was null", desc);
			assertTrue(
				"A language description was a zero-length String",
				desc.length() > 0
			);
		}
	}


	/** Test {@link OutputUtils#availableLanguageAPIPairs()}. */
	public void testAvailableLanguageAPIPairs() {
		Set<LanguageAPIPair> languages;
		languages = OutputUtils.availableLanguageAPIPairs();
		assertNotNull("availableLanguageAPIPairs returns null", languages);
		assertTrue(
			"availableLanguageAPIPairs returns an empty Set",
			!languages.isEmpty()
		);
		for (LanguageAPIPair pair : languages) {
			assertNotNull("A pair was null", pair);
		}
	}

	/** Test {@link OutputUtils#writeStringToFile(String, String)}. */
	public void testWriteStringToFileStringString() {
		// Test data string
		String testData = "foo";

		// Write foo to the test file.
		try {
			OutputUtils.writeStringToFile(testData, testFile.getAbsolutePath());
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("writeStringToFile(String, String) threw OutputException");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("writeStringToFile(String, String) failed an assertion");
		}
		assertTrue("Test file was not created", testFile.exists());

		// Make sure that the test file contains the correct contents
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(
				new FileInputStream(testFile)
			);
			assertNotNull("BufferedInputStream was null", input);

			// Now read in the file contents
			byte[] inputData = new byte[testData.length()];
			assertEquals(
				"Input data did not read the correct number of bytes",
				testData.length(),
				input.read(inputData, 0, testData.length())
			);

			// Make sure it matches the test data
			assertEquals(
				"Input did not match test data",
				testData,
				new String(inputData)
			);

			// Make sure there's no more data left
			assertEquals("Trailing data", 0, input.available());
		} catch (FileNotFoundException e) {
			assertNotNull("FileNotFoundException was null", e);
			fail("Could not find the test file");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("IOException reading the test file");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					assertNotNull("IOException is null", e);
					fail("Caught IOException in finally block");
				}
			} else {
				fail("input is null");
			}
		}

		// Delete the test file
		testFile.delete();
		assertFalse("Test file was not deleted", testFile.exists());

		// Now try some bad data
		boolean fellThrough = false;
		try {
			OutputUtils.writeStringToFile(null, testFile.getAbsolutePath());
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("writeStringToFile(null, String) threw OutputException");
		}
		assertFalse(
			"writeStringToFile(null, String) passed an assert",
			fellThrough
		);
	}

	/** Test {@link OutputUtils#writeStringToFile(String, File)}. */
	public void testWriteStringToFileStringFile() {
		// Test data string
		String testData = "foo";

		// Write foo to the test file.
		try {
			OutputUtils.writeStringToFile(testData, testFile);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("writeStringToFile(String, File) threw OutputException");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("writeStringToFile(String, File) failed an assertion");
		}
		assertTrue("Test file was not created", testFile.exists());

		// Make sure that the test file contains the correct contents
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(
				new FileInputStream(testFile)
			);
			assertNotNull("BufferedInputStream was null", input);

			// Now read in the file contents
			byte[] inputData = new byte[testData.length()];
			assertEquals(
				"Input data did not read the correct number of bytes",
				testData.length(),
				input.read(inputData, 0, testData.length())
			);

			// Make sure it matches the test data
			assertEquals(
				"Input did not match test data",
				testData,
				new String(inputData)
			);

			// Make sure there's no more data left
			assertEquals("Trailing data", 0, input.available());
		} catch (FileNotFoundException e) {
			assertNotNull("FileNotFoundException was null", e);
			fail("Could not find the test file");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("IOException reading the test file");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					assertNotNull("IOException is null", e);
					fail("Caught IOException in finally block");
				}
			} else {
				fail("input is null");
			}
		}

		// Delete the test file
		testFile.delete();
		assertFalse("Test file was not deleted", testFile.exists());

		// Now try some bad data
		boolean fellThrough = false;
		try {
			OutputUtils.writeStringToFile(null, testFile);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("writeStringToFile(null, File) threw OutputException");
		}
		assertFalse(
			"writeStringToFile(null, File) passed an assert",
			fellThrough
		);

		try {
			OutputUtils.writeStringToFile(testData, (File)null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("writeStringToFile(String, null) threw OutputException");
		}
		assertFalse(
			"writeStringToFile(String, null) passed an assert",
			fellThrough
		);
	}

	/** Test {@link OutputUtils#writeStringToFile(String, File, String)}. */
	public void testWriteStringToFileStringFileString() {
		// Test data string
		String testData = "foo";

		// Write foo to the test file.
		try {
			OutputUtils.writeStringToFile(
				testData,
				testFile.getParentFile(),
				testFile.getName()
			);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail(
				"writeStringToFile(String, File, String) threw OutputException"
			);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail(
				"writeStringToFile(String, File, String) failed an assertion"
			);
		}
		assertTrue("Test file was not created", testFile.exists());

		// Make sure that the test file contains the correct contents
		BufferedInputStream input = null;
		try {
			input = new BufferedInputStream(
				new FileInputStream(testFile)
			);
			assertNotNull("BufferedInputStream was null", input);

			// Now read in the file contents
			byte[] inputData = new byte[testData.length()];
			assertEquals(
				"Input data did not read the correct number of bytes",
				testData.length(),
				input.read(inputData, 0, testData.length())
			);

			// Make sure it matches the test data
			assertEquals(
				"Input did not match test data",
				testData,
				new String(inputData)
			);

			// Make sure there's no more data left
			assertEquals("Trailing data", 0, input.available());
		} catch (FileNotFoundException e) {
			assertNotNull("FileNotFoundException was null", e);
			fail("Could not find the test file");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("IOException reading the test file");
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					assertNotNull("IOException is null", e);
					fail("Caught IOException in finally block");
				}
			} else {
				fail("input is null");
			}
		}

		// Delete the test file
		testFile.delete();
		assertFalse("Test file was not deleted", testFile.exists());
	}

	/** Test {@link OutputUtils#generateParser(XMLDocumentType, String, String,
		String)}.
	*/
	public void testGenerateParser() {
		// Find all of the legal language and API pairs.
		Set<LanguageAPIPair> testData = OutputUtils.availableLanguageAPIPairs();

		// Make an empty doctype
		Collection<XMLMarkupDeclaration> markup;
		markup = new ArrayList<XMLMarkupDeclaration>();
		markup.add(
			new XMLElement(
				"element",
				new XMLElementContentModel(XMLElementContentType.ANY)
			)
		);
		XMLDocumentType doctype = new XMLDocumentType(markup);

		// Make sure that the options have been set
		Options.set("output-package", "foo");
		Options.set("vocabulary", "foo");

		// Make a test destination file/dir
		File testDir = new File(TMP, getClass().getName());

		// Now try all of the combinations
		for (LanguageAPIPair pair : testData) {
			File output = new File(
				testDir,
				Strings.join(
					Constants.Character.FULL_STOP,
					pair.getLanguage(),
					pair.getAPI()
				)
			);
			output.mkdirs();
			assertTrue("Output directory has been created", output.exists());
			try {
				OutputUtils.generateParser(
					doctype,
					output.getAbsolutePath(),
					pair.getLanguage(),
					pair.getAPI()
				);
			} catch (OutputException e) {
				assertNotNull("OutputException was null", e);
				e.printStackTrace();
				fail("An OutputException was thrown by generateParser");
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				e.printStackTrace();
				fail("An assertion was thrown by generateParser");
			}

			// Make sure that something was created in the output directory,
			// we'll leave it up to the XMLParserSourceGeneratorTestCases to
			// actually verify the contents further.
			if (output.list().length <= 0) {
				System.err.println(output.getAbsolutePath() + " was empty");
			}
			assertTrue("Output directory was empty", output.list().length > 0);

			// Now remove the test directory and its contents
			Files.removeTree(output);
			assertFalse("Output directory did not delete", output.exists());
		}

		// Now clean up the test directory
		testDir.delete();
		assertFalse("Test directory did not delete", testDir.exists());
	}
}
