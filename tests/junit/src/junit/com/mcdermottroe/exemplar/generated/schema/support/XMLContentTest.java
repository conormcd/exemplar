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
package junit.com.mcdermottroe.exemplar.generated.schema.support;

import com.mcdermottroe.exemplar.generated.schema.support.XMLContent;

/** Test class for {@link XMLContent}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class XMLContentTest
extends XMLComponentTestCase<XMLContent>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new XMLContent("foo"));
		addSample(new XMLContent("bar"));
		addSample(new XMLContent("baz"));
		addSample(new XMLContent("quux"));
		addSample(new XMLContent(" "));
		addSample(new XMLContent("\t"));
		addSample(new XMLContent("\t "));
	}

	/** Test {@link XMLContent#getContent()}. */
	public void testGetContent() {
		for (XMLContent sample : samples()) {
			if (sample != null) {
				String content = sample.getContent();
				assertNotNull("getContent() returned null", content);
			}
		}
	}

	/** Test {@link XMLContent#append(XMLContent)}. */
	public void testAppend() {
		for (XMLContent a : samples()) {
			// Test all samples with each other
			for (XMLContent b : samples()) {
				if (a != null && b != null) {
					String aContent = a.getContent();
					String bContent = b.getContent();
					String combinedContent = aContent + bContent;

					XMLContent aCopy = new XMLContent(aContent);
					assertEquals("Copy did not equal original", a, aCopy);
					XMLContent bCopy = new XMLContent(bContent);
					assertEquals("Copy did not equal original", b, bCopy);
					aCopy.append(bCopy);

					assertEquals(
						"a.append(b) != the concatenation of a and b",
						combinedContent,
						aCopy.getContent()
					);
				}
			}

			// Test appending null
			if (a != null) {
				XMLContent aCopy = new XMLContent(a.getContent());
				assertEquals("Copy did not equal original", a, aCopy);

				aCopy.append(null);

				assertEquals(
					"append(null) failed",
					a.getContent(),
					aCopy.getContent()
				);
			}
		}
	}

	/** Test {@link XMLContent#isAllWhitespace()}. */
	public void testIsAllWhitespace() {
		for (XMLContent sample : samples()) {
			if (sample != null) {
				assertEquals(
					"isAllWhitespace() failed",
					sample.getContent().matches("^\\s*$"),
					sample.isAllWhitespace()
				);
			}
		}
	}
}
