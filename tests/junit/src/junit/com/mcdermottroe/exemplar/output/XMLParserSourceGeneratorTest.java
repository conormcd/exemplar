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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.output.dtd.Generator;

import static com.mcdermottroe.exemplar.Constants.CWD;

import junit.com.mcdermottroe.exemplar.AbstractClassTestCase;

/** Test class for {@link XMLParserSourceGenerator}.

	@author	Conor McDermottroe
	@since	0.1
	@param	<T>	The type of {@link XMLParserSourceGenerator} to test.
*/
public class XMLParserSourceGeneratorTest<T extends XMLParserSourceGenerator<T>>
extends AbstractClassTestCase<T>
{
	/** Test the constructor which has no resource bundle attached to it. */
	public void testNonExistantResourceBundle() {
		boolean fellThrough = false;
		try {
			NoRBXMLParserSourceGenerator g = new NoRBXMLParserSourceGenerator();
			fellThrough = true;
			assertNotNull("TestXMLParserSourceGenerator returned false", g);
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
		}
		assertFalse("Fell through!", fellThrough);
	}

	/** Test {@link XMLParserSourceGenerator#getSourceDirectory(File)}. */
	public void testGetSourceDirectory() {
		List<File> goodData = new ArrayList<File>();
		goodData.add(null);
		goodData.add(new File(CWD));
		try {
			for (File f : goodData) {
				File dir = ExtendedDTDSourceGenerator.pubGetSourceDirectory(f);
				assertNotNull("getSourceDirectory(File) returned null", dir);
				assertTrue(dir + " does not exist.", dir.exists());
				assertTrue(dir + " is not a directory", dir.isDirectory());
			}
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
			fail("getSourceDirectory(File) threw");
		}

		List<File> badData = new ArrayList<File>();
		badData.add(new File("/definitely/does/not/exist"));
		for (File f : badData) {
			boolean fellThrough = false;
			try {
				File dir = ExtendedDTDSourceGenerator.pubGetSourceDirectory(f);
				fellThrough = true;
			} catch (XMLParserGeneratorException e) {
				assertNotNull("XMLParserGeneratorException was null", e);
			}
			assertFalse("getSourceDirectory(File) fell through", fellThrough);
		}
	}

	/** Test extension of the DTD source generator to allow us to expose some
		of the protected members.

		@author	Conor McDermottroe
		@since	0.2
	*/
	public static class ExtendedDTDSourceGenerator
	extends Generator
	{
		/** Simply extend the noarg constructor to pass on the exception.
	
			@throws	XMLParserGeneratorException	if the parent class constructor
												throws it.
		*/
		public ExtendedDTDSourceGenerator()
		throws XMLParserGeneratorException
		{
			super();
		}

		/** Expose {@link XMLParserSourceGenerator#getSourceDirectory(File)}.
	
			@param	fileOrDir					See getSourceDirectory(File) in
												XMLParserSourceGenerator.
			@return								See getSourceDirectory(File) in
												XMLParserSourceGenerator.
			@throws	XMLParserGeneratorException	See getSourceDirectory(File) in
												XMLParserSourceGenerator
		*/
		public static File pubGetSourceDirectory(File fileOrDir)
		throws XMLParserGeneratorException
		{
			return getSourceDirectory(fileOrDir);
		}
	}

	/** A simple implementation of an {@link XMLParserSourceGenerator} to test
		the code paths encountered when there is no matching resource bundle.

		@author	Conor McDermottroe
		@since	0.2
	*/
	public class NoRBXMLParserSourceGenerator
	extends XMLParserSourceGenerator<NoRBXMLParserSourceGenerator>
	{
		/** Simply extend the noarg constructor to pass on the exception.
	
			@throws	XMLParserGeneratorException	if the parent class constructor
												throws it.
		*/
		public NoRBXMLParserSourceGenerator()
		throws XMLParserGeneratorException
		{
			super();
		}

		/** {@inheritDoc} */
		@Override public void generateParser(
			XMLDocumentType doctype,
			File targetDirectory
		)
		throws XMLParserGeneratorException
		{
			throw new XMLParserGeneratorException();
		}

		/** {@inheritDoc} */
		@Override public String describeLanguage() {
			return "Test Language";
		}

		/** {@inheritDoc} */
		@Override public String describeAPI() {
			return null;
		}

		/** {@inheritDoc} */
		public NoRBXMLParserSourceGenerator getCopy()
		throws CopyException
		{
			try {
				return new NoRBXMLParserSourceGenerator();
			} catch (XMLParserGeneratorException e) {
				throw new CopyException(e);
			}
		}
	}
}
