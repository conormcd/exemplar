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
import java.util.List;

import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeContentType;
import com.mcdermottroe.exemplar.model.XMLAttributeDefaultType;
import com.mcdermottroe.exemplar.model.XMLAttributeList;

/** Test class for {@link XMLAttributeList}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttributeListTest
extends XMLNamedObjectTestCase<XMLAttributeList>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		XMLAttribute att1 = new XMLAttribute(
			"foo",
			XMLAttributeContentType.CDATA(),
			XMLAttributeDefaultType.IMPLIED()
		);
		XMLAttribute att2 = new XMLAttribute(
			"bar",
			XMLAttributeContentType.CDATA(),
			XMLAttributeDefaultType.REQUIRED()
		);

		List<XMLAttribute> atts1 = new ArrayList<XMLAttribute>();
		atts1.add(att1);

		List<XMLAttribute> atts2 = new ArrayList<XMLAttribute>();
		atts2.add(att1);
		atts2.add(att2);

		addSample(new XMLAttributeList("baz", atts1));
		addSample(new XMLAttributeList("quux", atts2));
	}

	/** Test {@link XMLAttributeList#iterator()}. */
	public void testIterator() {
		for (XMLAttributeList sample : samples()) {
			if (sample != null) {
				List<XMLAttribute> expected = sample.getAttributes();
				assertNotNull("List of attributes returned was null", expected);
				List<XMLAttribute> received = new ArrayList<XMLAttribute>();
				for (XMLAttribute xa : sample) {
					received.add(xa);
				}
				assertEquals(
					"iterator did not return the expected contents",
					expected,
					received
				);
			}
		}
	}

	/** Test {@link XMLAttributeList#getAttributes()}. */
	public void testGetAttributes() {
		for (XMLAttributeList sample : samples()) {
			if (sample != null) {
				assertNotNull(
					"getAttributes returned null",
					sample.getAttributes()
				);
			}
		}
	}
}
