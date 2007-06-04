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

import java.util.ArrayList;

import com.mcdermottroe.exemplar.model.XMLAggregateObject;
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeList;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementContentModel;
import com.mcdermottroe.exemplar.model.XMLElementContentType;
import com.mcdermottroe.exemplar.model.XMLMixedContent;
import com.mcdermottroe.exemplar.model.XMLSequence;

/** Test class for {@link XMLElement}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLElementTest
extends XMLNamedObjectTestCase<XMLElement>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		XMLElement[] samples = new XMLElement[8];
		samples[0] = new XMLElement(
			"foo",
			new XMLElementContentModel(XMLElementContentType.EMPTY)
		);
		samples[1] = new XMLElement(
			"bar",
			new XMLElementContentModel(XMLElementContentType.ANY)
		);
		samples[2] = new XMLElement(
			"baz",
			new XMLElementContentModel(new XMLMixedContent())
		);
		samples[3] = new XMLElement(
			"quux",
			new XMLElementContentModel(new XMLSequence())
		);
		samples[4] = samples[0].getCopy();
		samples[4].setAttlist(
			new XMLAttributeList("foo", new ArrayList<XMLAttribute>())
		);
		samples[5] = samples[1].getCopy();
		samples[5].setAttlist(
			new XMLAttributeList("bar", new ArrayList<XMLAttribute>())
		);
		samples[6] = samples[2].getCopy();
		samples[6].setAttlist(
			new XMLAttributeList("baz", new ArrayList<XMLAttribute>())
		);
		samples[7] = samples[3].getCopy();
		samples[7].setAttlist(
			new XMLAttributeList("quux", new ArrayList<XMLAttribute>())
		);

		for (XMLElement sample : samples) {
			addSample(sample);
		}
	}

	/** Test the constructors of {@link XMLElement}. */
	public void testConstructors() {
		// Tests that should pass
		XMLElement test = null;
		try {
			test = new XMLElement(
				"foo",
				new XMLElementContentModel(XMLElementContentType.EMPTY)
			);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Legitimate constructor caused an assertion error");
		}
		assertNotNull("Constructor returned a null object", test);

		// Tests that should fail
		boolean fellThrough = false;
		try {
			test = new XMLElement(null, null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertNotNull("Constructor returned a null object", test);
		assertFalse(
			"Illegitimate constructor did not cause an assertion error",
			fellThrough
		);
		try {
			test = new XMLElement("name", null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertNotNull("Constructor returned a null object", test);
		assertFalse(
			"Illegitimate constructor did not cause an assertion error",
			fellThrough
		);
		try {
			test = new XMLElement(
				null,
				new XMLElementContentModel(XMLElementContentType.EMPTY)
			);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertNotNull("Constructor returned a null object", test);
		assertFalse(
			"Illegitimate constructor did not cause an assertion error",
			fellThrough
		);
	}

	/** Test {@link XMLElement#getAttlist()}. */
	public void testGetAttlist() {
		for (XMLElement sample : samples()) {
			if (sample != null) {
				try {
					sample.getAttlist();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("XMLElement.getAttlist() caused an assertion error");
				}
			}
		}
	}

	/** Test {@link XMLElement#getContentModel()}. */
	public void testGetContentModel() {
		for (XMLElement sample : samples()) {
			if (sample != null) {
				try {
					sample.getContentModel();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("getContentModel() failed an assertion");
				}
			}
		}
	}

	/** Test {@link XMLElement#getContentSpec()}. */
	public void testGetContentSpec() {
		for (XMLElement sample : samples()) {
			if (sample != null) {
				XMLElementContentType cType = null;
				XMLAggregateObject<?> cSpec = null;
				try {
					cType = sample.getContentType();
					cSpec = sample.getContentSpec();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail(
						"XMLElement.getContentSpec() caused an assertion error"
					);
				}
				switch (cType) {
					case ANY:
					case EMPTY:
						assertNull(
							"ContentSpec must be null for ANY or EMPTY",
							cSpec
						);
						break;
					case MIXED:
						assertNotNull(
							"ContentSpec must not be null for MIXED",
							cSpec
						);
						assertTrue(
							"ContentSpec must be XMLMixedContent",
							XMLMixedContent.class.isAssignableFrom(
								cSpec.getClass()
							)
						);
						break;
					case CHILDREN:
						assertNotNull(
							"ContentSpec must not be null for CHILDREN",
							cSpec
						);
						assertTrue(
							"ContentSpec must be XMLSequence",
							XMLSequence.class.isAssignableFrom(
								cSpec.getClass()
							)
						);
						break;
					default:
						fail("Illegal content type");
				}
			}
		}
	}

	/** Test {@link XMLElement#getContentType()}. */
	public void testGetContentType() {
		for (XMLElement sample : samples()) {
			if (sample != null) {
				XMLElementContentType cType = sample.getContentType();
				assertNotNull("Content type was null", cType);
				switch (cType) {
					case ANY:
					case EMPTY:
					case MIXED:
					case CHILDREN:
						break;
					default:
						fail("Illegal content type");
				}
			}
		}
	}

	/** Test {@link XMLElement#setAttlist(XMLAttributeList)}. */
	public void testSetAttlist() {
		for (XMLElement sample : samples()) {
			if (sample != null) {
				XMLAttributeList attlist = new XMLAttributeList(
					sample.getName(),
					new ArrayList<XMLAttribute>()
				);
				try {
					sample.setAttlist(attlist);
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("setAttlist() threw an assertion error");
				}
				assertEquals(
					"setAttlist did not stick",
					attlist,
					sample.getAttlist()
				);
				boolean fellThrough = false;
				try {
					sample.setAttlist(null);
					fellThrough = true;
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
				}
				assertFalse(
					"setAttlist() failed to throw an assertion error",
					fellThrough
				);
			}
		}
	}
}
