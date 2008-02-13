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

import com.mcdermottroe.exemplar.generated.schema.element.Attribute;

import junit.com.mcdermottroe.exemplar.generated.schema.support.exemplarElementTestCase;

/** Test class for {@link Attribute}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class AttributeTest
extends exemplarElementTestCase<Attribute>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new Attribute());
	}

	/** Test {@link Attribute#getDefault()}. */
	public void testGetDefault() {
		assertTrue("getterTest returned false", getterTest("getDefault"));
	}

	/** Test {@link Attribute#setDefault(String)}. */
	public void testSetDefault() {
		assertTrue("setterTest returned false", setterTest("setDefault"));
	}

	/** Test {@link Attribute#getFixed()}. */
	public void testGetFixed() {
		assertTrue("getterTest returned false", getterTest("getFixed"));
	}

	/** Test {@link Attribute#setFixed(String)}. */
	public void testSetFixed() {
		assertTrue("setterTest returned false", setterTest("setFixed"));
	}

	/** Test {@link Attribute#getForm()}. */
	public void testGetForm() {
		assertTrue("getterTest returned false", getterTest("getForm"));
	}

	/** Test {@link Attribute#setForm(String)}. */
	public void testSetForm() {
		assertTrue("setterTest returned false", setterTest("setForm"));
	}

	/** Test {@link Attribute#getId()}. */
	public void testGetId() {
		assertTrue("getterTest returned false", getterTest("getId"));
	}

	/** Test {@link Attribute#setId(String)}. */
	public void testSetId() {
		assertTrue("setterTest returned false", setterTest("setId"));
	}

	/** Test {@link Attribute#getName()}. */
	public void testGetName() {
		assertTrue("getterTest returned false", getterTest("getName"));
	}

	/** Test {@link Attribute#setName(String)}. */
	public void testSetName() {
		assertTrue("setterTest returned false", setterTest("setName"));
	}

	/** Test {@link Attribute#getRef()}. */
	public void testGetRef() {
		assertTrue("getterTest returned false", getterTest("getRef"));
	}

	/** Test {@link Attribute#setRef(String)}. */
	public void testSetRef() {
		assertTrue("setterTest returned false", setterTest("setRef"));
	}

	/** Test {@link Attribute#getType()}. */
	public void testGetType() {
		assertTrue("getterTest returned false", getterTest("getType"));
	}

	/** Test {@link Attribute#setType(String)}. */
	public void testSetType() {
		assertTrue("setterTest returned false", setterTest("setType"));
	}

	/** Test {@link Attribute#getUse()}. */
	public void testGetUse() {
		assertTrue("getterTest returned false", getterTest("getUse"));
	}

	/** Test {@link Attribute#setUse(String)}. */
	public void testSetUse() {
		assertTrue("setterTest returned false", setterTest("setUse"));
	}
}
