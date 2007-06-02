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

import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link XMLExternalIdentifier}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLExternalIdentifierTest
extends NormalClassTestCase<XMLExternalIdentifier>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new XMLExternalIdentifier("foo", null));
		addSample(new XMLExternalIdentifier(null, "bar"));
		addSample(new XMLExternalIdentifier("baz", "quux"));
	}

	/** Test {@link XMLExternalIdentifier#publicID()}. */
	public void testPublicID() {
		for (XMLExternalIdentifier sample : samples()) {
			if (sample != null) {
				String pubID = sample.publicID();
				if (pubID != null) {
					assertNotSame("PUBLIC ID was an empty String", "", pubID);
				}
			}
		}
	}


	/** Test {@link XMLExternalIdentifier#systemID()}. */
	public void testSystemID() {
		for (XMLExternalIdentifier sample : samples()) {
			if (sample != null) {
				String systemID = sample.systemID();
				if (systemID != null) {
					assertNotSame(
						"SYSTEM ID was an empty String",
						"",
						systemID
					);
				}
			}
		}
	}}
