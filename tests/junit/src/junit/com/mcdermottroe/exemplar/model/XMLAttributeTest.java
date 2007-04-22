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

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link XMLAttribute}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAttributeTest
extends NormalClassTestCase<XMLAttribute>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		XMLAttribute sample = new XMLAttribute();
		sample.setDefaultDecl(XMLAttribute.DefaultType.FIXED, "foo"); // NON-NLS

		addSample(new XMLAttribute());
		addSample(sample);
	}

	/** Test {@link XMLAttribute#getName()} and {@link
		XMLAttribute#setName(String)}.
	*/
	public void testName() {
		for (XMLAttribute sample : samples()) {
			if (sample != null) {
				String originalName = sample.getName();
				sample.setName("foo");	// NON-NLS
				assertEquals(
					"setName && getName roundtrip",
					"foo",
					sample.getName()
				);
				sample.setName(originalName);
				assertEquals(
					"setName && getName roundtrip",
					originalName,
					sample.getName()
				);
			}
		}
	}

	/** Test {@link XMLAttribute#getValues()} and {@link
		XMLAttribute#setValues(List)}.
	*/
	public void testValues() {
		List<String> testValues = new ArrayList<String>();
		testValues.add("foo"); // NON-NLS
		for (XMLAttribute sample : samples()) {
			if (sample != null) {
				List<String> originalValues = sample.getValues();
				sample.setValues(testValues);
				assertEquals(
					"setValues && getValues roundtrip",
					testValues,
					sample.getValues()
				);
				sample.setValues(originalValues);
				assertEquals(
					"setValues && getValues roundtrip",
					originalValues,
					sample.getValues()
				);
			}
		}
	}
}
