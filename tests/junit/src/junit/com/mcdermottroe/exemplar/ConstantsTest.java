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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.mcdermottroe.exemplar.Constants;

import static com.mcdermottroe.exemplar.Constants.Character.DOLLAR;
import static com.mcdermottroe.exemplar.Constants.Character.EXCLAMATION_MARK;
import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;
import static com.mcdermottroe.exemplar.Constants.Character.SLASH;
import static com.mcdermottroe.exemplar.Constants.PACKAGE;
import static com.mcdermottroe.exemplar.Constants.URL_JAR_PREFIX;

/** Test class for com.mcdermottroe.exemplar.Constants.

	@author	Conor McDermottroe
	@since	0.1
*/
public class ConstantsTest
extends InterfaceTestCase<Constants>
{
	/** Test a selection of constants. */
	public void testConstants() {
		assertNotNull("COPYRIGHT_MESSAGE == null", Constants.COPYRIGHT_MESSAGE);
		for (String line : Constants.COPYRIGHT_MESSAGE) {
			assertNotNull("COPYRIGHT_MESSAGE line == null", line);
		}
		assertNotNull("CWD == null", Constants.CWD);
		assertNotNull("EOL == null", Constants.EOL);
		assertNotNull("Input.PACKAGE == null", Constants.Input.PACKAGE);
		assertNotNull("NULL_STRING == null", Constants.NULL_STRING);
		assertNotNull("Output.PACKAGE == null", Constants.Output.PACKAGE);
		assertNotNull("PACKAGE == null", Constants.PACKAGE);
		assertNotNull(
			"Regex.VALID_PE_NAME == null",
			Constants.Regex.VALID_PE_NAME
		);
	}

	/** A test to ensure that all classes in the output JAR have a test case. */
	public void testAllClassesHaveATestCase() {
		// Construct the package path
		String packagePath = PACKAGE.replace(FULL_STOP, SLASH);

		// Find all of the JARs which contain the classes in PACKAGE
		List<JarFile> jars = new ArrayList<JarFile>();
		ClassLoader cl = ConstantsTest.class.getClassLoader();
		Enumeration<URL> urls = null;
		try {
			urls = cl.getResources(packagePath);
		} catch (IOException e) {
			assertNotNull("IOException was null", e);
			fail("IOException thrown by getResources()");
		}
		assertNotNull("URLs was null", urls);
		while (urls.hasMoreElements()) {
			URL u = urls.nextElement();
			if (u.toString().startsWith(URL_JAR_PREFIX)) {
				String url = u.toString();
				url = url.substring(
					URL_JAR_PREFIX.length(),
					url.indexOf((int)EXCLAMATION_MARK)
				);

				File f = new File(url);
				assertTrue(
					"File does not exist: " + f.getAbsolutePath(),
					f.exists()
				);
				JarFile jf = null;
				try {
					jf = new JarFile(f);
				} catch (IOException e) {
					assertNotNull("IOException was null", e);
					fail("IOException thrown by JarFile(File)");
				}
				assertNotNull("JarFile was null", jf);
				jars.add(jf);
			}
		}

		// Now find all the classes in the JARs and check for matching test
		// classes.
		for (JarFile jf : jars) {
			Enumeration<JarEntry> entries = jf.entries();
			while (entries.hasMoreElements()) {
				JarEntry je = entries.nextElement();
				String name = je.getName();
				if (name.startsWith(packagePath) && name.endsWith(".class")) {
					if (!name.contains(String.valueOf(DOLLAR))) {
						StringBuilder className = new StringBuilder(
							name.substring(
								0,
								name.lastIndexOf((int)FULL_STOP)
							).replace(SLASH, FULL_STOP)
						);
						className.insert(0, "junit.");
						className.append("Test");
						try {
							Class.forName(className.toString());
						} catch (ClassNotFoundException e) {
							assertNotNull("ClassNotFoundException was null", e);
							fail("Missing test class: " + className);
						}
					}
				}
			}
		}
	}
}
