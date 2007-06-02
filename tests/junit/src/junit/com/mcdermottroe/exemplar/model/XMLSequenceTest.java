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

import com.mcdermottroe.exemplar.model.XMLAlternative;
import com.mcdermottroe.exemplar.model.XMLElementReference;
import com.mcdermottroe.exemplar.model.XMLMixedContent;
import com.mcdermottroe.exemplar.model.XMLSequence;

import static com.mcdermottroe.exemplar.Constants.INFINITY;

/** Test class for {@link XMLSequence}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLSequenceTest
extends XMLAggregateObjectTestCase<XMLSequence>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		XMLAlternative sampleAlternative = new XMLAlternative();
		XMLElementReference sampleElementReference = new XMLElementReference(
			"foo"
		);
		XMLMixedContent sampleMixedContent = new XMLMixedContent();
		XMLSequence sampleSequence = new XMLSequence();

		// Sample A
		addSample(sampleSequence);

		// Sample B
		XMLSequence sampleB = new XMLSequence();
		sampleB.addObject(sampleAlternative);
		addSample(sampleB);

		// Sample C
		XMLSequence sampleC = new XMLSequence();
		sampleC.addObject(sampleElementReference);
		addSample(sampleC);

		// Sample D
		XMLSequence sampleD = new XMLSequence();
		sampleD.addObject(sampleMixedContent);
		addSample(sampleD);

		// Sample E
		XMLSequence sampleE = new XMLSequence();
		sampleE.addObject(sampleSequence);
		addSample(sampleE);

		// Sample F
		XMLSequence sampleF = new XMLSequence();
		sampleF.addObject(sampleAlternative);
		sampleF.addObject(sampleElementReference);
		sampleF.addObject(sampleMixedContent);
		sampleF.addObject(sampleSequence);
		addSample(sampleF);

		// Sample G
		XMLSequence sampleG = sampleF.getCopy();
		sampleG.setMinMaxOccurs(0, 1);
		addSample(sampleG);

		// Sample H
		XMLSequence sampleH = sampleF.getCopy();
		sampleH.setMinMaxOccurs(0, INFINITY);
		addSample(sampleH);
	}

	/** Test {@link XMLSequence#setMinMaxOccurs(int, int)}. */
	public void testSetMinMaxOccurs() {
		for (XMLSequence sample : samples()) {
			if (sample != null) {
				// Get the original settings
				int min = sample.getMinOccurs();
				int max = sample.getMaxOccurs();

				// Try some legal settings
				try {
					sample.setMinMaxOccurs(0, 1);			// ?
					sample.setMinMaxOccurs(0, INFINITY);	// *
					sample.setMinMaxOccurs(1, INFINITY);	// +
					sample.setMinMaxOccurs(1, 2);			// {1,2}
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("setMinMaxOccurs failed when given legal values");
				}

				// Try some illegal settings
				boolean fellThrough = false;
				try {
					sample.setMinMaxOccurs(-1, 0);
					sample.setMinMaxOccurs(INFINITY, INFINITY);
					sample.setMinMaxOccurs(-2, -1);
					sample.setMinMaxOccurs(2, 1);
					fellThrough = true;
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
				}
				assertFalse(
					"setMinMaxOccurs did not fail when given illegal values",
					fellThrough
				);

				// Return the values to their original settings.
				sample.setMinMaxOccurs(min, max);
			}
		}
	}

	/** Test {@link XMLSequence#getMinOccurs()}. */
	public void testGetMinOccurs() {
		for (XMLSequence sample : samples()) {
			if (sample != null) {
				int min = sample.getMinOccurs();
				assertTrue("min is negative", min >= 0);
				assertTrue("min is finite", min < INFINITY);
			}
		}
	}

	/** Test {@link XMLSequence#getMaxOccurs()}. */
	public void testGetMaxOccurs() {
		for (XMLSequence sample : samples()) {
			if (sample != null) {
				int max = sample.getMaxOccurs();
				assertTrue("max is negative or zero", max > 0);
			}
		}
	}
}
