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
package junit.com.mcdermottroe.exemplar.generated.schema.element;

import com.mcdermottroe.exemplar.generated.schema.element.Group;

import junit.com.mcdermottroe.exemplar.generated.schema.support.exemplarElementTestCase;

/** Test class for {@link Group}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class GroupTest
extends exemplarElementTestCase<Group>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new Group());
	}

	/** Test {@link Group#getId()}. */
	public void testGetId() {
		assertTrue("getterTest returned false", getterTest("getId"));
	}

	/** Test {@link Group#setId(String)}. */
	public void testSetId() {
		assertTrue("setterTest returned false", setterTest("setId"));
	}

	/** Test {@link Group#getMaxOccurs()}. */
	public void testGetMaxOccurs() {
		assertTrue("getterTest returned false", getterTest("getMaxOccurs"));
	}

	/** Test {@link Group#setMaxOccurs(String)}. */
	public void testSetMaxOccurs() {
		assertTrue("setterTest returned false", setterTest("setMaxOccurs"));
	}

	/** Test {@link Group#getMinOccurs()}. */
	public void testGetMinOccurs() {
		assertTrue("getterTest returned false", getterTest("getMinOccurs"));
	}

	/** Test {@link Group#setMinOccurs(String)}. */
	public void testSetMinOccurs() {
		assertTrue("setterTest returned false", setterTest("setMinOccurs"));
	}

	/** Test {@link Group#getName()}. */
	public void testGetName() {
		assertTrue("getterTest returned false", getterTest("getName"));
	}

	/** Test {@link Group#setName(String)}. */
	public void testSetName() {
		assertTrue("setterTest returned false", setterTest("setName"));
	}

	/** Test {@link Group#getRef()}. */
	public void testGetRef() {
		assertTrue("getterTest returned false", getterTest("getRef"));
	}

	/** Test {@link Group#setRef(String)}. */
	public void testSetRef() {
		assertTrue("setterTest returned false", setterTest("setRef"));
	}
}
