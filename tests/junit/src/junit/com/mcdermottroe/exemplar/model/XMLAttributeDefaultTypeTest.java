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
package junit.com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.model.XMLAttributeDefaultType;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link XMLAttributeDefaultType}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class XMLAttributeDefaultTypeTest
extends NormalClassTestCase<XMLAttributeDefaultType>
{
	/** {@inheritDoc}. */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		addSample(XMLAttributeDefaultType.ATTVALUE("foo"));
		addSample(XMLAttributeDefaultType.ATTVALUE("bar"));
		addSample(XMLAttributeDefaultType.FIXED("foo"));
		addSample(XMLAttributeDefaultType.FIXED("bar"));
		addSample(XMLAttributeDefaultType.IMPLIED());
		addSample(XMLAttributeDefaultType.REQUIRED());
	}

	/** Test {@link XMLAttributeDefaultType#ATTVALUE(String)}. */
	public void testATTVALUE() {
		XMLAttributeDefaultType[] testData = {
			XMLAttributeDefaultType.ATTVALUE("foo"),
			XMLAttributeDefaultType.ATTVALUE("foo"),
			XMLAttributeDefaultType.ATTVALUE("bar"),
		};
		assertEquals("toString()", "\"foo\"", testData[0].toString());
		assertEquals("toString()", "\"foo\"", testData[1].toString());
		assertEquals("toString()", "\"bar\"", testData[2].toString());
		assertEquals("0 != 1", testData[0], testData[1]);
		assertNotSame("0 == 2", testData[0], testData[2]);
		assertNotSame("1 == 2", testData[1], testData[2]);
		assertTrue("0 != 1 referentially", testData[0] == testData[1]);
	}

	/** Test {@link XMLAttributeDefaultType#FIXED(String)}. */
	public void testFIXED() {
		XMLAttributeDefaultType[] testData = {
			XMLAttributeDefaultType.FIXED("foo"),
			XMLAttributeDefaultType.FIXED("foo"),
			XMLAttributeDefaultType.FIXED("bar"),
		};
		assertEquals("toString()", "#FIXED \"foo\"", testData[0].toString());
		assertEquals("toString()", "#FIXED \"foo\"", testData[1].toString());
		assertEquals("toString()", "#FIXED \"bar\"", testData[2].toString());
		assertEquals("0 != 1", testData[0], testData[1]);
		assertNotSame("0 == 2", testData[0], testData[2]);
		assertNotSame("1 == 2", testData[1], testData[2]);
		assertTrue("0 != 1 referentially", testData[0] == testData[1]);
	}

	/** Test {@link XMLAttributeDefaultType#IMPLIED()}. */
	public void testIMPLIED() {
		XMLAttributeDefaultType orig = XMLAttributeDefaultType.IMPLIED();
		for (int i = 0; i < 5; i++) {
			XMLAttributeDefaultType test = XMLAttributeDefaultType.IMPLIED();
			assertEquals("Not equal", orig, test);
			assertTrue("Not referentially equal", orig == test);
		}
	}

	/** Test {@link XMLAttributeDefaultType#REQUIRED()}. */
	public void testREQUIRED() {
		XMLAttributeDefaultType orig = XMLAttributeDefaultType.REQUIRED();
		for (int i = 0; i < 5; i++) {
			XMLAttributeDefaultType test = XMLAttributeDefaultType.REQUIRED();
			assertEquals("Not equal", orig, test);
			assertTrue("Not referentially equal", orig == test);
		}
	}

	/** Test {@link XMLAttributeDefaultType#getValue()}. */
	public void testGetValue() {
		for (XMLAttributeDefaultType sample : samples()) {
			if (sample != null) {
				String value = sample.getValue();
				if (value != null) {
					assertFalse(
						"Is a #REQUIRED or #IMPLIED attribute",
						XMLAttributeDefaultType.REQUIRED().equals(sample) ||
						XMLAttributeDefaultType.IMPLIED().equals(sample)
					);
				} else {
					assertTrue(
						"Is not a #REQUIRED or #IMPLIED attribute",
						XMLAttributeDefaultType.REQUIRED().equals(sample) ||
						XMLAttributeDefaultType.IMPLIED().equals(sample)
					);
				}
			}
		}
	}

	/** Test {@link XMLAttributeDefaultType#sameType(XMLAttributeDefaultType)}.
	*/
	public void testSameType() {
		for (XMLAttributeDefaultType a : samples()) {
			for (XMLAttributeDefaultType b : samples()) {
				if (a != null && b != null) {
					if (a.equals(b)) {
						assertTrue("sameType fails", a.sameType(b));
					}
				}
			}
		}
	}
}
