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
package junit.com.mcdermottroe.exemplar.output;

import java.io.File;
import java.util.Collection;

import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.utils.Files;

/** Test class for children of {@link XMLParserSourceGenerator}.

	@param	<T>	The type of {@link XMLParserSourceGenerator} to test.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class
XMLParserSourceGeneratorTestCase<T extends XMLParserSourceGenerator<T>>
extends XMLParserGeneratorTestCase<T>
{
	/** {@inheritDoc} */
	@Override public void tearDown()
	throws Exception
	{
		super.tearDown();
		Files.removeTree(
			Files.tempDir(getClass().getName())
		);
	}

	/** Test {@link XMLParserSourceGenerator#describeAPI()}. */
	public void testDescribeAPI() {
		for (T sample : samples()) {
			if (sample != null) {
				String api = sample.describeAPI();
				assertNotNull("describeAPI returned null", api);
				assertTrue(
					"describeAPI returned a zero-length String",
					api.length() > 0
				);
			}
		}
	}

	/** Test {@link XMLParserSourceGenerator#describeLanguage()}. */
	public void testDescribeLanguage() {
		for (T sample : samples()) {
			if (sample != null) {
				String language = sample.describeLanguage();
				assertNotNull("describeLanguage returned null", language);
				assertTrue(
					"describeLanguage returned a zero-length String",
					language.length() > 0
				);
			}
		}
	}

	/** Test {@link XMLParserSourceGenerator#generateParser(XMLDocumentType,
		File)}.
	*/
	public void testGenerateParser() {
		for (T sample : samples()) {
			if (sample != null) {
				for (XMLDocumentType docType : getSampleDocTypes()) {
					// Create the output directory
					File outputDir = Files.tempDir(getClass().getName());
					assertNotNull(
						"Failed to create output directory",
						outputDir
					);

					// Generate the parser.
					try {
						sample.generateParser(docType, outputDir);
					} catch (XMLParserGeneratorException e) {
						e.printStackTrace();
						assertNotNull(
							"XMLParserGeneratorException was null",
							e
						);
						fail(
							"generateParser threw XMLParserGeneratorException"
						);
					} catch (AssertionError e) {
						assertNotNull("AssertionError was null", e);
						fail("generateParser threw an assertion");
					}

					// Now make sure that the generated files exist.
					Collection<File> generatedFiles = generatedFiles(
						sample,
						outputDir,
						docType
					);
					for (File f : generatedFiles) {
						assertTrue(
							"Generated file did not exist: " + f.getPath(),
							f.exists()
						);
					}
					for (File f : Files.findFiles(outputDir)) {
						assertTrue(
							"Extraneous file found: " + f.getAbsolutePath(),
							generatedFiles.contains(f)
						);
					}

					// Finally, clean up.
					Files.removeTree(outputDir);
					assertFalse(
						"Faild to delete output dir",
						outputDir.exists()
					);
				}
			}
		}
	}

	/** Get the names of the {@link File}s which were created by {@link
		XMLParserSourceGenerator#generateParser(XMLDocumentType, File)}.

		@param	generator	The {@link XMLParserSourceGenerator} which generated
							the files.
		@param	outputDir	The directory in which the files were constructed.
		@param	docType		The {@link XMLDocumentType} for which we are
							generating a parser.
		@return				A {@link Collection} of {@link File}s which should
							have been generated by the output module.
	*/
	public abstract Collection<File> generatedFiles(
		T generator,
		File outputDir,
		XMLDocumentType docType
	);
}
