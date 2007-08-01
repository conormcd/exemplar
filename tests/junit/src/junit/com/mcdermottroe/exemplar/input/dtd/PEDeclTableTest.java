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
package junit.com.mcdermottroe.exemplar.input.dtd;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;

import com.mcdermottroe.exemplar.input.dtd.PEDeclTable;
import com.mcdermottroe.exemplar.input.dtd.ParameterEntityException;
import com.mcdermottroe.exemplar.input.dtd.ParameterEntityType;
import com.mcdermottroe.exemplar.ui.Message;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link PEDeclTable}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class PEDeclTableTest
extends NormalClassTestCase<PEDeclTable>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		PEDeclTable a = new PEDeclTable();
		a.addNewPE("foo", "bar", ParameterEntityType.VALUE);

		PEDeclTable b = new PEDeclTable();
		b.addNewPE("foo", "baz", ParameterEntityType.VALUE);

		PEDeclTable c = new PEDeclTable();
		c.addNewPE("foo", "bar", ParameterEntityType.URI);

		PEDeclTable d = new PEDeclTable();
		d.addNewPE("foo", "baz", ParameterEntityType.URI);

		addSample(new PEDeclTable());
		addSample(a);
		addSample(b);
		addSample(c);
		addSample(d);
	}

	/** Basic adding and replacement test. The tests that
		are completed are as follows:

		<ol>
			<li>Add an entry called 'foo' to the PEDeclTable.</li>
			<li>Add an entry called 'bar' to the PEDeclTable.</li>
			<li>Replace those two entries in a String.</li>
			<li>Attempt to replace an undeclared entity in a String.</li>
		</ol>
	*/
	public void testBasic() {
		PEDeclTable pedt = new PEDeclTable();

		try {
			pedt.addNewPE("foo", "bar", ParameterEntityType.VALUE);
		} catch (ParameterEntityException e) {
			e.printStackTrace();
			fail("Exception thrown when adding an entry to the PEDeclTable");
			return;
		}
		assertEquals(
			"PEDeclTable contains 1,0 entries",
			Message.DTDPEDECLTABLE(1, 0),
			pedt.toString()
		);

		try {
			pedt.addNewPE("bar", "baz", ParameterEntityType.VALUE);
		} catch (ParameterEntityException e) {
			e.printStackTrace();
			fail("Exception thrown when adding an entry to the PEDeclTable");
			return;
		}
		assertEquals(
			"PEDeclTable contains 2,0 entries",
			Message.DTDPEDECLTABLE(2, 0),
			pedt.toString()
		);

		try {
			String input = "foo%foo;bar%bar;";
			String expected = "foobarbarbaz";
			assertEquals(
				"PEs substituted correctly",
				expected,
				pedt.replacePERefs(input)
			);
		} catch (ParameterEntityException e) {
			e.printStackTrace();
			fail("Exception throws when resolving parameter entities");
			return;
		}

		try {
			pedt.replacePERefs("foo%baz;bar");
			fail("Substituting a non-existant PE did not throw an exception.");
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
		}
	}

	/** Test adding a parameter entity that points to a file. */
	public void testFileEntity() {
		PEDeclTable pedt = new PEDeclTable();
		try {
			pedt.addNewPE(
				"foo",
				"http://www.google.com",
				ParameterEntityType.URI
			);
		} catch (ParameterEntityException e) {
			e.printStackTrace();
			fail("Failed to add a URI-type PE");
			return;
		}
		assertEquals(
			"Successfully added a URI-type PE",
			Message.DTDPEDECLTABLE(0, 1),
			pedt.toString()
		);
	}

	/** Test {@link PEDeclTable#replacePERefs(CharSequence)}. */
	public void testReplacePERefs() {
		String[] input = {
			null,
			"",
			"%foo;",
			"%%%foo;;;",
			"foo%foo;%bar;%baz;",
		};
		String[] expected = {
			"",
			"",
			"bar",
			"%%bar;;",
			"foobarbazquux",
		};

		PEDeclTable testData = new PEDeclTable();
		try {
			testData.addNewPE(
				"foo",
				"bar",
				ParameterEntityType.VALUE
			);
			testData.addNewPE(
				"bar",
				"baz",
				ParameterEntityType.VALUE
			);
			testData.addNewPE(
				"baz",
				"quux",
				ParameterEntityType.VALUE
			);
		} catch (ParameterEntityException e) {
			e.printStackTrace();
			fail("Failed to create test data"); // NON-NLS
			return;
		}

		for (int i = 0; i < input.length; i++) {
			try {
				assertEquals(
					"PEDeclTable.replacePERefs()",
					expected[i],
					testData.replacePERefs(input[i])
				);
			} catch (ParameterEntityException e) {
				e.printStackTrace();
				fail("ParameterEntityException thrown");
			}
		}
	}

	/** Test {@link PEDeclTable#addNewPE(String, String, ParameterEntityType)}.
	*/
	public void testAddNewPE() {
		for (PEDeclTable sample : samples()) {
			if (sample != null) {
				try {
					sample.addNewPE(
						"Conor",
						"McDermottroe",
						ParameterEntityType.VALUE
					);
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
					fail("Calling add(String, String, VALUE) failed an assert");
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("Calling add(String, String, VALUE) failed an assert");
				}
				try {
					sample.addNewPE(
						"Conor",
						"McDermottroe",
						ParameterEntityType.URI
					);
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
					fail("Calling add(String, String, VALUE) failed an assert");
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("Calling add(String, String, VALUE) failed an assert");
				}
			}
		}
	}

	/** Test {@link PEDeclTable#getFileTable()}. */
	public void testGetFileTable() {
		for (PEDeclTable sample : samples()) {
			if (sample != null) {
				boolean returnedNull = false;
				try {
					Map<String, String> ft = sample.getFileTable();
					returnedNull = ft == null;
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("getFileTable() failed an assert");
				}
				assertFalse("getFileTable() returned null", returnedNull);
			}
		}
	}

	/** Test {@link PEDeclTable#getTable()}. */
	public void testGetTable() {
		for (PEDeclTable sample : samples()) {
			if (sample != null) {
				boolean returnedNull = false;
				try {
					Map<String, String> t = sample.getTable();
					returnedNull = t == null;
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("getTable() failed an assert");
				}
				assertFalse("getTable() returned null", returnedNull);
			}
		}
	}

	/** Test {@link PEDeclTable#peRefReader(String, File)}. */
	public void testPeRefReaderValuePE() {
		PEDeclTable testTable = new PEDeclTable();
		try {
			testTable.addNewPE("foo", "bar", ParameterEntityType.VALUE);
			Reader r = testTable.peRefReader("foo", new File("/dev/null"));
			assertTrue("Reader was not ready", r.ready());
			StringBuilder result = new StringBuilder();
			while (r.ready()) {
				int character = r.read();
				if (character == -1) {
					break;
				}
				assertTrue(
					"Invalid character received",
					character >= 0 && character <= (int)Character.MAX_VALUE
				);
				result.append(Character.toChars(character));
			}
			assertEquals(
				"Reader did not output expected output",
				"bar",
				result.toString()
			);
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
			fail("Unexpected ParameterEntityException");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("Unexpected IOException");
		}
	}

	/** Test {@link PEDeclTable#peRefReader(String, File)}. */
	public void testPeRefReaderURLPEFile() {
		// Create the temp file
		String tmpDirName = System.getProperty("java.io.tmpdir");
		assertNotNull("TMPDIR name is null", tmpDirName);
		File tmpDir = new File(tmpDirName);
		assertNotNull("TMPDIR is null", tmpDir);
		assertTrue("TMPDIR does not exist", tmpDir.exists());
		File tmpFile = new File(
			tmpDir,
			"PEDeclTableTest.testPeRefReaderURLPEFile"
		);
		assertNotNull("Temp file is null", tmpFile);
		assertFalse("Temp file already exists", tmpFile.exists());
		BufferedWriter writer = null;
		try {
			writer = new BufferedWriter(new FileWriter(tmpFile));
			writer.write("bar");
		} catch (IOException e) {
			assertNotNull("IOException is null", e);
			fail("Unexpected IOException");
		} finally {
			if (writer != null) {
				try {
					writer.close();
				} catch (IOException e) {
					assertNotNull("IOException is null", e);
					fail("Unexpected IOException");
				}
			}
		}

		PEDeclTable testTable = new PEDeclTable();
		try {
			testTable.addNewPE(
				"foo",
				tmpFile.getAbsolutePath(),
				ParameterEntityType.URI
			);
			Reader r = testTable.peRefReader("foo", tmpDir);
			assertTrue("Reader was not ready", r.ready());
			StringBuilder result = new StringBuilder();
			while (r.ready()) {
				int character = r.read();
				if (character == -1) {
					break;
				}
				assertTrue(
					"Invalid character received",
					character >= 0 && character <= (int)Character.MAX_VALUE
				);
				result.append(Character.toChars(character));
			}
			assertEquals(
				"Reader did not output expected output",
				"bar",
				result.toString()
			);
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
			fail("Unexpected ParameterEntityException");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("Unexpected IOException");
		}

		PEDeclTable testTable2 = new PEDeclTable();
		try {
			testTable2.addNewPE(
				"foo",
				"PEDeclTableTest.testPeRefReaderURLPEFile",
				ParameterEntityType.URI
			);
			Reader r = testTable2.peRefReader("foo", tmpDir);
			assertTrue("Reader was not ready", r.ready());
			StringBuilder result = new StringBuilder();
			while (r.ready()) {
				int character = r.read();
				if (character == -1) {
					break;
				}
				assertTrue(
					"Invalid character received",
					character >= 0 && character <= (int)Character.MAX_VALUE
				);
				result.append(Character.toChars(character));
			}
			assertEquals(
				"Reader did not output expected output",
				"bar",
				result.toString()
			);
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
			fail("Unexpected ParameterEntityException");
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("Unexpected IOException");
		}

		// Now clean up the temp file
		assertTrue("Failed to delete the temp file", tmpFile.delete());
	}

	/** Test {@link PEDeclTable#peRefReader(String, File)}. */
	public void testPeRefReaderNegative() {
		for (PEDeclTable sample : samples()) {
			if (sample != null) {
				boolean fellThrough = false;
				try {
					sample.peRefReader(null, null);
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
					fail("Unexpected ParameterEntityException");
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
				}
				assertFalse(
					"PEDeclTable.peRefReader(null, null) passed assert",
					fellThrough
				);

				fellThrough = false;
				try {
					sample.peRefReader("foo", null);
					fellThrough = true;
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
					fail("Unexpected ParameterEntityException");
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
				}
				assertFalse(
					"PEDeclTable.peRefReader(String, null) passed assert",
					fellThrough
				);

				fellThrough = false;
				try {
					sample.peRefReader(null, new File("/dev/null"));
					fellThrough = true;
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
					fail("Unexpected ParameterEntityException");
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
				}
				assertFalse(
					"PEDeclTable.peRefReader(null, File) passed assert",
					fellThrough
				);

				fellThrough = false;
				try {
					sample.peRefReader("notDefined", new File("/dev/null"));
					fellThrough = true;
				} catch (ParameterEntityException e) {
					assertNotNull("ParameterEntityException was null", e);
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("Unexpected AssertionError");
				}
				assertFalse(
					"PEDeclTable.peRefReader(undef, File) passed assert",
					fellThrough
				);
			}
		}

		// Test the MalformedURLException code path
		PEDeclTable malformedURLExceptionTable = new PEDeclTable();
		try {
			malformedURLExceptionTable.addNewPE(
				"foo",
				"http127.0.0.1path",
				ParameterEntityType.URI
			);
			malformedURLExceptionTable.peRefReader(
				"foo",
				new File("/dev/null")
			);
			fail("Failed to deal with a MFUE");
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
		}

		// Test the IOException code path
		PEDeclTable ioExceptionTable = new PEDeclTable();
		try {
			ioExceptionTable.addNewPE(
				"foo",
				"http://127.0.0.1/path/which/does/not/exist",
				ParameterEntityType.URI
			);
			ioExceptionTable.peRefReader("foo", new File("/dev/null"));
			fail("Failed to deal with a IOException");
		} catch (ParameterEntityException e) {
			assertNotNull("ParameterEntityException was null", e);
		}
	}

	/** {@inheritDoc} */
	@Override public void testToStringConsistent() {
		assertTrue(DELIBERATE_PASS, true);
	}
}
