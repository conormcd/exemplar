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
package junit.com.mcdermottroe.exemplar.generated.schema;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.xml.sax.SAXException;

import com.mcdermottroe.exemplar.generated.schema.W3CSchema;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.input.schema.TypeProcessor;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;
import junit.com.mcdermottroe.exemplar.input.schema.ParserTest;

/** Test class for {@link W3CSchema}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class W3CSchemaTest
extends UtilityClassTestCase<W3CSchema>
{
	/** A collection of sample inputs to test from. */
	private List<File> sampleInput;

	/** The results of the calls to {@link W3CSchema#read(File)} made in {@link
		#testRead()}.
	*/
	private List<XMLComponent<?>> readResults;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		sampleInput = new ArrayList<File>();
		sampleInput.addAll(ParserTest.getSamples());

		readResults = new ArrayList<XMLComponent<?>>();
	}

	/** Test {@link W3CSchema#read(File)}. */
	public void testRead() {
		if (!readResults.isEmpty()) {
			return;
		}
		for (File sample : sampleInput) {
			try {
				XMLComponent<?> schema = W3CSchema.read(sample);
				assertNotNull("read(File) returned null", schema);
				readResults.add(schema);
			} catch (IOException e) {
				assertNotNull("IOException was null", e);
				fail("read(File) threw an IOException");
			} catch (SAXException e) {
				assertNotNull("SAXException was null", e);
				fail("read(File) threw a SAXException");
			}
		}
	}

	/** Test {@link W3CSchema#walk(XMLComponent, Collection)}. */
	public void testWalk() {
		if (readResults.isEmpty()) {
			testRead();
		}

		Collection<W3CSchemaTreeOp> treeOps = new ArrayList<W3CSchemaTreeOp>();
		treeOps.add(new TypeProcessor());
		for (XMLComponent<?> schema : readResults) {
			try {
				W3CSchema.walk(schema, null);
				W3CSchema.walk(schema, treeOps);
			} catch (W3CSchemaException e) {
				assertNotNull("W3CSchemaException was null", e);
				fail("walk() threw an exception");
				return;
			}
		}
	}

	/** Test {@link W3CSchema#write(XMLComponent, OutputStream)}. */
	public void testWrite() {
		if (readResults.isEmpty()) {
			testRead();
		}

		for (XMLComponent<?> schema : readResults) {
			// Try writing to a null output
			try {
				W3CSchema.write(schema, (OutputStream)null);
			} catch (IOException e) {
				assertNotNull("IOException was null", e);
				fail("write(XMLComponent, null) threw an IOException");
				return;
			}

			ByteArrayOutputStream output = new ByteArrayOutputStream();
			try {
				W3CSchema.write(schema, output);
			} catch (IOException e) {
				assertNotNull("IOException was null", e);
				fail("write(XMLComponent, OutputStream) threw an IOException");
				return;
			}
			String written = output.toString();
			assertNotNull("write() wrote null", written);
			assertNotSame("write() wrote null", "", written);
		}
	}
}
