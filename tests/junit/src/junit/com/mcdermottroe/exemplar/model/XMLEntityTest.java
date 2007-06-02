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

import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;

/** Test class for {@link XMLEntity}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLEntityTest
extends XMLNamedObjectTestCase<XMLEntity>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		XMLEntity[] samples = new XMLEntity[3];

		samples[0] =	new XMLEntity("name", "value");
		samples[1] =	new XMLEntity(
							"name",
							new XMLExternalIdentifier("foo", "bar")
						);
		samples[2] =	new XMLEntity(
							"name",
							new XMLExternalIdentifier("foo", "bar"),
							"notation"
						);

		for (XMLEntity sample : samples) {
			addSample(sample);
		}
	}

	/** Test the constructor for {@link XMLEntity}. */
	public void testConstructor() {
		boolean fellThrough = false;
		XMLEntity xe = null;

		try {
			xe = new XMLEntity(null, (String)null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("new XMLEntity((String)null)", fellThrough);
		assertNull("XMLEntity was not null", xe);

		try {
			xe = new XMLEntity(null, (XMLExternalIdentifier)null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("new XMLEntity((XMLExternalIdentifier)null)", fellThrough);
		assertNull("XMLEntity was not null", xe);

		try {
			xe = new XMLEntity(null, null, null);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("new XMLEntity(null, null, null)", fellThrough);
		assertNull("XMLEntity was not null", xe);

		try {
			xe = new XMLEntity(
				null,
				new XMLExternalIdentifier("foo", "bar"),
				null
			);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("new XMLEntity(null, XEI(foo, bar), null)", fellThrough);
		assertNull("XMLEntity was not null", xe);

		try {
			xe = new XMLEntity(null, null, "notation");
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("new XMLEntity(null, null, \"notation\")", fellThrough);
		assertNull("XMLEntity was not null", xe);
	}

	/** Test {@link XMLEntity#externalID()}. */
	public void testExternalID() {
		for (XMLEntity sample : samples()) {
			if (sample != null) {
				try {
					sample.externalID();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("XMLExternalIdentifier.externalID() threw assertion");
				}
			}
		}
	}

	/** Test {@link XMLEntity#getNotation()}. */
	public void testGetNotation() {
		for (XMLEntity sample : samples()) {
			if (sample != null) {
				try {
					sample.getNotation();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("XMLExternalIdentifier.getNotation() threw assertion");
				}
			}
		}
	}

	/** Test {@link XMLEntity#isInternal()}. */
	public void testIsInternal() {
		for (XMLEntity sample : samples()) {
			if (sample != null) {
				assertEquals(
					"isInternal() lies",
					sample.externalID() == null && sample.getNotation() == null,
					sample.isInternal()
				);
			}
		}
	}

	/** Test {@link XMLEntity#type()}. */
	public void testType() {
		for (XMLEntity sample : samples()) {
			if (sample != null) {
				try {
					sample.type();
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("sample.type() threw an AsssertionError");
				}
			}
		}
	}

	/** Test {@link XMLEntity#value()}. */
	public void testValue() {
		for (XMLEntity sample : samples()) {
			if (sample != null) {
				String value = sample.value();
				if (sample.isInternal()) {
					assertNotNull("Value was null for internal entity", value);
				} else {
					assertNull(
						"Value was not null for non-internal entity",
						value
					);
				}
			}
		}
	}
}
