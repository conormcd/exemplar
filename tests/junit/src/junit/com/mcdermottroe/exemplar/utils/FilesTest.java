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
package junit.com.mcdermottroe.exemplar.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.utils.Files;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link com.mcdermottroe.exemplar.utils.Files}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class FilesTest
extends UtilityClassTestCase<Files>
{
	/** A root directory for this test. */
	private File testDirectory;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		testDirectory = new File(TMP, getClass().getName());
	}

	/** Test {@link Files#findDirectories(File)}. */
	public void testFindDirectories() {
		// Give a clean test directory
		removeTestTree();
		createTestTree();

		// Create the expected set
		File fooDir = new File(testDirectory, "foo");
		File barDir = new File(fooDir, "bar");
		Collection<File> expected = new ArrayList<File>();
		expected.add(testDirectory);
		expected.add(fooDir);
		expected.add(barDir);

		// Do the test.
		Collection<File> found = Files.findDirectories(testDirectory);
		assertTrue(
			"Expected directory list did not match found directory list",
			expected.containsAll(found) && found.containsAll(expected)
		);

		// Clean up
		removeTestTree();
	}

	/** Test {@link Files#findFiles(File)}. */
	public void testFindFiles() {
		// Give a clean test directory
		removeTestTree();
		createTestTree();

		// Create the expected set
		File fooDir = new File(testDirectory, "foo");
		File barDir = new File(fooDir, "bar");
		File bazFile = new File(fooDir, "baz");
		File quuxFile = new File(barDir, "quux");
		Collection<File> expected = new ArrayList<File>();
		expected.add(bazFile);
		expected.add(quuxFile);

		// Do the test.
		Collection<File> found = Files.findFiles(testDirectory);
		assertTrue(
			"Expected directory list did not match found directory list",
			expected.containsAll(found) && found.containsAll(expected)
		);

		// Clean up
		removeTestTree();
	}

	/** Test {@link Files#removeTree(File)}. */
	public void testRemoveTree() {
		removeTestTree();
		createTestTree();
		Files.removeTree(testDirectory);
		assertFalse("Test directory still exists", testDirectory.exists());
	}

	/** Create a test tree of files and directories. */
	private void createTestTree() {
		File fooDir = new File(testDirectory, "foo");
		File barDir = new File(fooDir, "bar");
		File bazFile = new File(fooDir, "baz");
		File quuxFile = new File(barDir, "quux");

		// Make the directories
		assertFalse("Test directory already exists", testDirectory.exists());
		testDirectory.mkdirs();
		assertTrue("Test directory did not create", testDirectory.exists());
		fooDir.mkdirs();
		assertTrue("foo directory did not create", fooDir.exists());
		barDir.mkdirs();
		assertTrue("bar directory did not create", barDir.exists());

		// Make the files
		try {
			OutputUtils.writeStringToFile("baz", bazFile);
			OutputUtils.writeStringToFile("quux", quuxFile);
		} catch (OutputException e) {
			assertNotNull("OutputException was null", e);
			fail("OutputException thrown when writing files");
		}
	}

	/** Remove the test tree created by {@link #createTestTree()}. */
	private void removeTestTree() {
		File fooDir = new File(testDirectory, "foo");
		File barDir = new File(fooDir, "bar");
		File bazFile = new File(fooDir, "baz");
		File quuxFile = new File(barDir, "quux");

		File[] filesToDelete = {
			quuxFile,
			bazFile,
			barDir,
			fooDir,
			testDirectory,
		};
		for (File f : filesToDelete) {
			if (f.exists()) {
				f.delete();
				assertFalse(
					"File still exists: " + f.getAbsolutePath(),
					f.exists()
				);
			}
		}
	}
}
