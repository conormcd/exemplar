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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.input.dtd.Parser;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Files;

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

	/** Get a sample of DTDs to parse.

		@return	A sample collection of DTDs for using as test input.
	*/
	public static Collection<File> getSampleDTDs() {
		Collection<File> sampleDtds = new ArrayList<File>();
		sampleDtds.add(new File("dtds/docbook-xml-42/docbookx.dtd"));
		sampleDtds.add(new File("dtds/docbook-xml-44/docbookx.dtd"));
		sampleDtds.add(new File("dtds/w3cschema/XMLSchema.dtd"));
		for (File f : Files.findFiles(new File("tests/data/valid_dtds"))) {
			if ("test.dtd".equals(f.getName())) {
				sampleDtds.add(f);
			}
		}
		return sampleDtds;
	}

	/** Test {@link Parser#parse(String)}. */
	public void testParseString() {
		for (Parser sample : samples()) {
			if (sample != null) {
				for (File sampleFile : getSampleDTDs()) {
					XMLDocumentType doctype = null;
					try {
						doctype = sample.parse(sampleFile);
					} catch (ParserException e) {
						assertNotNull("ParserException was null", e);
						fail("parse(String) threw a ParserException");
					}
					assertNotNull("parse(String) returned null", doctype);
				}
			}
		}
	}

	/** Test {@link Parser#debug_message(String)}. */
	public void testDebug_message() {
		String[] input = {
			null,
			"foo",
			"bar",
			"# Current token is #",
			"# Current token is #23",
		};
		Options.set("debug", "true");
		Options.set("debug-level", "100");

		for (Parser sample : samples()) {
			if (sample != null) {
				for (String message : input) {
					try {
						sample.debug_message(message);
					} catch (AssertionError e) {
						assertNotNull("AssertionError was null", e);
						fail("debug_message() failed an assertion");
					}
				}
			}
		}
	}

	/** Test {@link Parser#EOF_sym()}. */
	public void testEOF_sym() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#action_table()}. */
	public void testAction_table() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link
		Parser#do_action(int,java_cup.runtime.lr_parser,java.util.Stack, int)}.
	*/
	public void testDo_action() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#error_sym()}. */
	public void testError_sym() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#production_table()} ()}. */
	public void testProduction_table() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#reduce_table()}. */
	public void testReduce_table() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#start_production()}. */
	public void testStart_production() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Parser#start_state()}. */
	public void testStart_state() {
		assertTrue(DELIBERATE_PASS, true);
	}
}
