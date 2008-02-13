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

import com.mcdermottroe.exemplar.generated.schema.element.Element;

import junit.com.mcdermottroe.exemplar.generated.schema.support.exemplarElementTestCase;

/** Test class for {@link Element}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class ElementTest
extends exemplarElementTestCase<Element>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new Element());
	}

	/** Test {@link Element#getAbstract()}. */
	public void testGetAbstract() {
		assertTrue("getterTest returned false", getterTest("getAbstract"));
	}

	/** Test {@link Element#setAbstract(String)}. */
	public void testSetAbstract() {
		assertTrue("setterTest returned false", setterTest("setAbstract"));
	}

	/** Test {@link Element#getBlock()}. */
	public void testGetBlock() {
		assertTrue("getterTest returned false", getterTest("getBlock"));
	}

	/** Test {@link Element#setBlock(String)}. */
	public void testSetBlock() {
		assertTrue("setterTest returned false", setterTest("setBlock"));
	}

	/** Test {@link Element#getDefault()}. */
	public void testGetDefault() {
		assertTrue("getterTest returned false", getterTest("getDefault"));
	}

	/** Test {@link Element#setDefault(String)}. */
	public void testSetDefault() {
		assertTrue("setterTest returned false", setterTest("setDefault"));
	}

	/** Test {@link Element#getFinal()}. */
	public void testGetFinal() {
		assertTrue("getterTest returned false", getterTest("getFinal"));
	}

	/** Test {@link Element#setFinal(String)}. */
	public void testSetFinal() {
		assertTrue("setterTest returned false", setterTest("setFinal"));
	}

	/** Test {@link Element#getFixed()}. */
	public void testGetFixed() {
		assertTrue("getterTest returned false", getterTest("getFixed"));
	}

	/** Test {@link Element#setFixed(String)}. */
	public void testSetFixed() {
		assertTrue("setterTest returned false", setterTest("setFixed"));
	}

	/** Test {@link Element#getForm()}. */
	public void testGetForm() {
		assertTrue("getterTest returned false", getterTest("getForm"));
	}

	/** Test {@link Element#setForm(String)}. */
	public void testSetForm() {
		assertTrue("setterTest returned false", setterTest("setForm"));
	}

	/** Test {@link Element#getId()}. */
	public void testGetId() {
		assertTrue("getterTest returned false", getterTest("getId"));
	}

	/** Test {@link Element#setId(String)}. */
	public void testSetId() {
		assertTrue("setterTest returned false", setterTest("setId"));
	}

	/** Test {@link Element#getMaxOccurs()}. */
	public void testGetMaxOccurs() {
		assertTrue("getterTest returned false", getterTest("getMaxOccurs"));
	}

	/** Test {@link Element#setMaxOccurs(String)}. */
	public void testSetMaxOccurs() {
		assertTrue("setterTest returned false", setterTest("setMaxOccurs"));
	}

	/** Test {@link Element#getMinOccurs()}. */
	public void testGetMinOccurs() {
		assertTrue("getterTest returned false", getterTest("getMinOccurs"));
	}

	/** Test {@link Element#setMinOccurs(String)}. */
	public void testSetMinOccurs() {
		assertTrue("setterTest returned false", setterTest("setMinOccurs"));
	}

	/** Test {@link Element#getName()}. */
	public void testGetName() {
		assertTrue("getterTest returned false", getterTest("getName"));
	}

	/** Test {@link Element#setName(String)}. */
	public void testSetName() {
		assertTrue("setterTest returned false", setterTest("setName"));
	}

	/** Test {@link Element#getNillable()}. */
	public void testGetNillable() {
		assertTrue("getterTest returned false", getterTest("getNillable"));
	}

	/** Test {@link Element#setNillable(String)}. */
	public void testSetNillable() {
		assertTrue("setterTest returned false", setterTest("setNillable"));
	}

	/** Test {@link Element#getRef()}. */
	public void testGetRef() {
		assertTrue("getterTest returned false", getterTest("getRef"));
	}

	/** Test {@link Element#setRef(String)}. */
	public void testSetRef() {
		assertTrue("setterTest returned false", setterTest("setRef"));
	}

	/** Test {@link Element#getSubstitutionGroup()}. */
	public void testGetSubstitutionGroup() {
		assertTrue(
			"getterTest returned false",
			getterTest("getSubstitutionGroup")
		);
	}

	/** Test {@link Element#setSubstitutionGroup(String)}. */
	public void testSetSubstitutionGroup() {
		assertTrue(
			"setterTest returned false",
			setterTest("setSubstitutionGroup")
		);
	}

	/** Test {@link Element#getType()}. */
	public void testGetType() {
		assertTrue("getterTest returned false", getterTest("getType"));
	}

	/** Test {@link Element#setType(String)}. */
	public void testSetType() {
		assertTrue("setterTest returned false", setterTest("setType"));
	}
}
