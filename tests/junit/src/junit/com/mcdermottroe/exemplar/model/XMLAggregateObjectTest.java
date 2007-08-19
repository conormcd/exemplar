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

import com.mcdermottroe.exemplar.model.XMLAggregateObject;
import com.mcdermottroe.exemplar.model.XMLAlternative;
import com.mcdermottroe.exemplar.model.XMLMixedContent;
import com.mcdermottroe.exemplar.model.XMLSequence;

import junit.com.mcdermottroe.exemplar.AbstractClassTestCase;

/** Test class for {@link XMLAggregateObject}.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of {@link XMLAggregateObject} to test.
*/
public class XMLAggregateObjectTest<T extends XMLAggregateObject>
extends AbstractClassTestCase<XMLAggregateObject>
{
	/** Do a basic set of tests comparing some {@link XMLAggregateObject}s with
		each other.
	*/
	public void testAggregateObjectsBasic() {
		XMLAlternative alt = new XMLAlternative();
		XMLMixedContent mix = new XMLMixedContent();
		XMLSequence seq = new XMLSequence();

		assertSame("XMLAlternative != XMLAlternative", alt, alt);
		assertSame("XMLMixedContent != XMLMixedContent", mix, mix);
		assertSame("XMLSequence != XMLSequence", seq, seq);

		assertNotSame("XMLAlternative == XMLMixedContent", alt, mix);
		assertNotSame("XMLAlternative == XMLSequence", alt, seq);
		assertNotSame("XMLMixedContent == XMLAlternative", mix, alt);
		assertNotSame("XMLMixedContent == XMLSequence", mix, seq);
		assertNotSame("XMLSequence == XMLAlternative", seq, alt);
		assertNotSame("XMLSequence == XMLMixedContenet", seq, mix);

		assertNotSame(
			"XMLAlternative cmp XMLMixedContent == 0",
			0,
			alt.compareTo(mix)
		);
		assertNotSame(
			"XMLAlternative cmp XMLSequence == 0",
			0,
			alt.compareTo(seq)
		);
		assertNotSame(
			"XMLMixedContent cmp XMLAlternative == 0",
			0,
			mix.compareTo(alt)
		);
		assertNotSame(
			"XMLMixedContent cmp XMLSequence == 0",
			0,
			mix.compareTo(seq)
		);
		assertNotSame(
			"XMLSequence cmp XMLAlternative == 0",
			0,
			seq.compareTo(alt)
		);
		assertNotSame(
			"XMLSequence cmp XMLMixedContenet == 0",
			0,
			seq.compareTo(mix)
		);
	}
}
