// vim:filetype=java:ts=4
/*
	Copyright (c) 2007, 2008
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

import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.model.XMLAttributeContentType;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link XMLAttributeContentType}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class XMLAttributeContentTypeTest
extends NormalClassTestCase<XMLAttributeContentType>
{
	/** {@inheritDoc}. */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		List<String> enumeratedType = new ArrayList<String>(2);
		enumeratedType.add("foo");
		enumeratedType.add("bar");

		addSample(XMLAttributeContentType.CDATA());
		addSample(XMLAttributeContentType.ENTITIES());
		addSample(XMLAttributeContentType.ENTITY());
		addSample(XMLAttributeContentType.ENUMERATION(enumeratedType));
		addSample(XMLAttributeContentType.ID());
		addSample(XMLAttributeContentType.IDREF());
		addSample(XMLAttributeContentType.IDREFS());
		addSample(XMLAttributeContentType.NMTOKEN());
		addSample(XMLAttributeContentType.NMTOKENS());
		addSample(XMLAttributeContentType.NOTATION(enumeratedType));
	}

	/** Test {@link XMLAttributeContentType#CDATA()}. */
	public void testCDATA() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.CDATA();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.CDATA() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.CDATA() == null", type);
		assertEquals(
			"XMLAttributeContentType.CDATA() != \"CDATA\"",
			"CDATA",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#ENTITIES()}. */
	public void testENTITIES() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.ENTITIES();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.ENTITIES() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.ENTITIES() == null", type);
		assertEquals(
			"XMLAttributeContentType.ENTITIES() != \"ENTITIES\"",
			"ENTITIES",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#ENTITY()}. */
	public void testENTITY() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.ENTITY();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.ENTITY() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.ENTITY() == null", type);
		assertEquals(
			"XMLAttributeContentType.ENTITY() != \"ENTITY\"",
			"ENTITY",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#ENUMERATION(List)}. */
	public void testENUMERATION() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.ENUMERATION(new ArrayList<String>());
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.ENUMERATION() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.ENUMERATION() == null", type);
		assertEquals(
			"XMLAttributeContentType.ENUMERATION() != \"()\"",
			"()",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#ID()}. */
	public void testID() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.ID();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.ID() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.ID() == null", type);
		assertEquals(
			"XMLAttributeContentType.ID() != \"ID\"",
			"ID",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#IDREF()}. */
	public void testIDREF() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.IDREF();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.IDREF() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.IDREF() == null", type);
		assertEquals(
			"XMLAttributeContentType.IDREF() != \"IDREF\"",
			"IDREF",
			type.toString()
		);
	}


	/** Test {@link XMLAttributeContentType#IDREFS()}. */
	public void testIDREFS() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.IDREFS();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.IDREFS() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.IDREFS() == null", type);
		assertEquals(
			"XMLAttributeContentType.IDREFS() != \"IDREFS\"",
			"IDREFS",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#NMTOKEN()}. */
	public void testNMTOKEN() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.NMTOKEN();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.NMTOKEN() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.NMTOKEN() == null", type);
		assertEquals(
			"XMLAttributeContentType.NMTOKEN() != \"NMTOKEN\"",
			"NMTOKEN",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#NMTOKENS()}. */
	public void testNMTOKENS() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.NMTOKENS();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.NMTOKENS() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.NMTOKENS() == null", type);
		assertEquals(
			"XMLAttributeContentType.NMTOKENS() != \"NMTOKENS\"",
			"NMTOKENS",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#NOTATION(List)}. */
	public void testNOTATION() {
		XMLAttributeContentType type = null;
		try {
			type = XMLAttributeContentType.NOTATION(new ArrayList<String>());
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("XMLAttributeContentType.NOTATION() failed an assertion");
		}
		assertNotNull("XMLAttributeContentType.NOTATION() == null", type);
		assertEquals(
			"XMLAttributeContentType.NOTATION() != \"NOTATION ()\"",
			"NOTATION ()",
			type.toString()
		);
	}

	/** Test {@link XMLAttributeContentType#getValues()}. */
	public void testGetValues() {
		for (XMLAttributeContentType sample : samples()) {
			if (sample != null) {
				if	(
						sample.equals(XMLAttributeContentType.CDATA()) ||
						sample.equals(XMLAttributeContentType.ENTITIES()) ||
						sample.equals(XMLAttributeContentType.ENTITY()) ||
						sample.equals(XMLAttributeContentType.ID()) ||
						sample.equals(XMLAttributeContentType.IDREF()) ||
						sample.equals(XMLAttributeContentType.IDREFS()) ||
						sample.equals(XMLAttributeContentType.NMTOKEN()) ||
						sample.equals(XMLAttributeContentType.NMTOKENS())
					)
				{
					assertTrue(
						"sample has values but shoiuldn't have",
						sample.getValues().isEmpty()
					);
				} else {
					assertFalse(
						"sample has no values but should have",
						sample.getValues().isEmpty()
					);
				}
			}
		}
	}

	/** Test {@link XMLAttributeContentType.Type}. */
}
