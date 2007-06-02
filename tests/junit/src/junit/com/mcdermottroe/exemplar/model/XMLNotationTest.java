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
package junit.com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLNotation;

/** Test class for {@link XMLNotation}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLNotationTest
extends XMLNamedObjectTestCase<XMLNotation>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();
		addSample(
			new XMLNotation(
				"notationName",
				new XMLExternalIdentifier("foo", "bar")
			)
		);
		addSample(
			new XMLNotation(
				"notationName",
				new XMLExternalIdentifier("foo", "baz")
			)
		);
		addSample(
			new XMLNotation(
				"otherNotation",
				new XMLExternalIdentifier("foo", "bar")
			)
		);
		addSample(
			new XMLNotation(
				"otherNotation",
				new XMLExternalIdentifier("foo", "baz")
			)
		);
	}

	/** Test the constructor for {@link XMLNotation}. */
	public void testConstructor() {
		XMLNotation xn = null;
		boolean fellThrough = false;

		try {
			xn = new XMLNotation(null, null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse(
			"Illegal use of constructor did not result in exception",
			fellThrough
		);
		assertNull("Object constructed", xn);

		try {
			xn = new XMLNotation(null, new XMLExternalIdentifier("foo", "bar"));
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse(
			"Illegal use of constructor did not result in exception",
			fellThrough
		);
		assertNull("Object constructed", xn);


		try {
			xn = new XMLNotation("foo", null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse(
			"Illegal use of constructor did not result in exception",
			fellThrough
		);
		assertNull("Object constructed", xn);
	}

	/** Test {@link XMLNotation#getExtID()}. */
	public void testGetExtID() {
		for (XMLNotation sample : samples()) {
			if (sample != null) {
				XMLExternalIdentifier xei = null;
				try {
					xei = sample.getExtID();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("getExtID returned null");
				}
				assertNotNull("External identifier was null", xei);
			}
		}
	}
}
