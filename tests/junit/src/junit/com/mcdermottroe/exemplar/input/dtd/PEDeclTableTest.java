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

import com.mcdermottroe.exemplar.input.dtd.PEDeclTable;
import com.mcdermottroe.exemplar.input.dtd.ParameterEntityException;
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
		a.addNewPE("foo", "bar", PEDeclTable.ParameterEntityType.VALUE);

		PEDeclTable b = new PEDeclTable();
		a.addNewPE("foo", "baz", PEDeclTable.ParameterEntityType.VALUE);

		addSample(new PEDeclTable());
		addSample(a);
		addSample(b);
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
			pedt.addNewPE("foo", "bar", PEDeclTable.ParameterEntityType.VALUE);
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
			pedt.addNewPE("bar", "baz", PEDeclTable.ParameterEntityType.VALUE);
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
			assertTrue(
				"Substituting a non-existant PE correctly threw an exception",
				true
			);
		}
	}

	/** Test adding a parameter entity that points to a file. */
	public void testFileEntity() {
		PEDeclTable pedt = new PEDeclTable();
		try {
			pedt.addNewPE(
				"foo",
				"http://www.google.com",
				PEDeclTable.ParameterEntityType.URI
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

	/** Test {@link PEDeclTable#replacePERefs(String)}. */
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
				PEDeclTable.ParameterEntityType.VALUE
			);
			testData.addNewPE(
				"bar",
				"baz",
				PEDeclTable.ParameterEntityType.VALUE
			);
			testData.addNewPE(
				"baz",
				"quux",
				PEDeclTable.ParameterEntityType.VALUE
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
}
