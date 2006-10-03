// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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

import com.mcdermottroe.exemplar.model.XMLAlternative;
import com.mcdermottroe.exemplar.model.XMLElementReference;
import com.mcdermottroe.exemplar.model.XMLMixedContent;
import com.mcdermottroe.exemplar.model.XMLSequence;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link com.mcdermottroe.exemplar.model.XMLAlternative}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAlternativeTest extends NormalClassTestCase {
	/** {@inheritDoc} */
	public void setUp() throws Exception {
		super.setUp();

		sampleObjects = new ArrayList();

		XMLAlternative sampleAlternative = new XMLAlternative();
		XMLElementReference sampleElementReference = new XMLElementReference();
		XMLMixedContent sampleMixedContent = new XMLMixedContent();
		XMLSequence sampleSequence = new XMLSequence();

		// Sample A
		sampleObjects.add(sampleAlternative);

		// Sample B
		XMLAlternative sampleB = new XMLAlternative();
		sampleB.addObject(sampleAlternative);
		sampleObjects.add(sampleB);

		// Sample C
		XMLAlternative sampleC = new XMLAlternative();
		sampleC.addObject(sampleElementReference);
		sampleObjects.add(sampleC);

		// Sample D
		XMLAlternative sampleD = new XMLAlternative();
		sampleD.addObject(sampleMixedContent);
		sampleObjects.add(sampleD);

		// Sample E
		XMLAlternative sampleE = new XMLAlternative();
		sampleE.addObject(sampleSequence);
		sampleObjects.add(sampleE);

		// Sample F
		XMLAlternative sampleF = new XMLAlternative();
		sampleF.addObject(sampleAlternative);
		sampleF.addObject(sampleElementReference);
		sampleF.addObject(sampleMixedContent);
		sampleF.addObject(sampleSequence);
		sampleObjects.add(sampleF);
	}
}
