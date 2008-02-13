// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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
package junit.com.mcdermottroe.exemplar.input.schema;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.input.schema.Parser;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLNamedObject;
import com.mcdermottroe.exemplar.model.XMLNotation;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link Parser}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class ParserTest
extends NormalClassTestCase<Parser>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new Parser());
	}

	/** Get a selection of sample schemas.

		@return	A {@link Collection} of sample schemas.
	*/
	public static Collection<File> getSamples() {
		Collection<File> sampleSchemas = new ArrayList<File>();
		sampleSchemas.add(
			new File("tests/data/valid_w3c_schemas/docbook-xml-44/docbook.xsd")
		);
		return sampleSchemas;
	}

	/** Test {@link Parser#addMarkupDeclaration(XMLNamedObject)}. */
	public void testAddMarkupDeclaration() {
		XMLNamedObject<XMLNotation> sampleMarkup = new XMLNotation(
			"foo",
			new XMLExternalIdentifier("bar", "baz")
		);
		for (Parser sample : samples()) {
			if (sample != null) {
				// Copy the parser to avoid damaging it.
				Parser copy = null;
				try {
					copy = sample.getCopy();
				} catch (CopyException e) {
					assertNotNull("CopyException was null", e);
					fail("Failed to copy parser");
					break;
				}

				// Add some markup
				try {
					copy.addMarkupDeclaration(sampleMarkup);
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					fail("Adding markup caused an exception to be thrown");
				}
			}
		}
	}
}
