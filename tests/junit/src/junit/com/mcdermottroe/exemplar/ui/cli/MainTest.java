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
package junit.com.mcdermottroe.exemplar.ui.cli;

import java.io.File;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.ui.cli.ExitHandler;
import com.mcdermottroe.exemplar.ui.cli.Main;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link com.mcdermottroe.exemplar.ui.cli.Main}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class MainTest
extends UtilityClassTestCase<Main>
{
	/** The original {@link ExitHandler} for {@link Main}. */
	private ExitHandler mainsOriginalHandler;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		mainsOriginalHandler = Main.getExitHandler();
	}

	/** Test {@link Main#main(String[])}. */
	public void testMain() {
		File outputDir = new File(TMP, getClass().getName());
		String[][] passInputs = {
			{"--help", },
			{"--version", },
			{
				"--input",
				"/dev/null",
				"--output",
				outputDir.getAbsolutePath(),
				"--output-language",
				"dtd",
			},
			{
				"--debug",
				"--input",
				"/dev/null",
				"--output",
				outputDir.getAbsolutePath(),
				"--output-language",
				"dtd",
			},
			{
				"--verbose",
				"--input",
				"/dev/null",
				"--output",
				outputDir.getAbsolutePath(),
				"--output-language",
				"dtd",
			},
		};
		String[][] failInputs = {
			null,
			{null, },
			{"--not-an-option", },
		};

		// Save the original exit handler and replace it with one that does not
		// call System.exit(int).
		setMainExit(false);

		for (String[] args : passInputs) {
			Options.reset();
			assertNotNull("Output dir is null", outputDir);
			outputDir.mkdirs();
			for (String fileName : outputDir.list()) {
				new File(outputDir, fileName).delete();
			}
			assertTrue("Failed to create output dir", outputDir.exists());
			try {
				Main.main(args);
			} catch (AssertionError e) {
				e.printStackTrace();
				assertNotNull("AssertionError was null", e);
				fail("main() failed an assertion");
			}
			for (String fileName : outputDir.list()) {
				new File(outputDir, fileName).delete();
			}
			outputDir.delete();
			assertFalse("Failed to delete output dir", outputDir.exists());
		}
		for (String[] args : failInputs) {
			Options.reset();
			boolean fellThrough = false;
			try {
				Main.main(args);
				fellThrough = true;
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
			}
			assertFalse("main() passed an assertion error", fellThrough);
		}

		// Now restore Main's exit handler.
		setMainExit(true);
	}

	/** Test {@link Main#getExitHandler()}. */
	public void testGetExitHandler() {
		ExitHandler handler = null;
		try {
			handler = Main.getExitHandler();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Main.getExitHandler() failed an assertion");
		}
		assertNotNull("getExitHandler returned null", handler);
	}

	/** Test {@link Main#setExitHandler(ExitHandler)}. */
	public void testSetExitHandler() {
		// Make a test handler.
		ExitHandler testExitHandler = new ExitHandler() {
			public void exit(int exitCode) {
				// Do nothing.
			}
		};
		assertNotNull("Bad test data", testExitHandler);

		// Try to set the handler
		try {
			Main.setExitHandler(testExitHandler);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("setExitHandler failed an assertion");
		}
		assertEquals(
			"Exit handler did not stick",
			testExitHandler,
			Main.getExitHandler()
		);

		// Put back the original main handler
		Main.setExitHandler(mainsOriginalHandler);
	}

	/** Turn on or off the calling of {@link System#exit(int)} in {@link Main}.

		@param	on	True to make {@link Main} call {@link System#exit(int)},
					false otherwise.
	*/
	private void setMainExit(boolean on) {
		if (on) {
			Main.setExitHandler(mainsOriginalHandler);
		} else {
			Main.setExitHandler(
				new ExitHandler() {
					public void exit(int exitCode) {
						DBC.ASSERT(exitCode == 0);
					}
				}
			);
		}
	}
}
