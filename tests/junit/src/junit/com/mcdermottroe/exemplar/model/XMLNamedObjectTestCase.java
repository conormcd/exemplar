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
package junit.com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.model.XMLNamedObject;

/** Parent class to all subclasses of {@link XMLNamedObject}.

	@param	<T>	The type of {@link XMLNamedObject} being tested.
	@author	Conor McDermottroe
	@since	0.2
*/
public abstract class XMLNamedObjectTestCase<T extends XMLNamedObject>
extends XMLObjectTestCase<T>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();
	}

	/** Test that it is possible to both name and re-name a named object. */
	public void testNamingWorks() {
		// Now test the samples
		for (XMLNamedObject obj : samples()) {
			if (obj != null) {
				String originalName = obj.getName();
				obj.setName("test");
				assertEquals("Setting name to test", "test", obj.getName());
				obj.setName(originalName);
				assertEquals("Resetting old name", originalName, obj.getName());
			}
		}
	}
}
