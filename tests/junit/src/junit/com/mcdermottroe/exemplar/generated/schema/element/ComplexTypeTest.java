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

import com.mcdermottroe.exemplar.generated.schema.element.ComplexType;

import junit.com.mcdermottroe.exemplar.generated.schema.support.exemplarElementTestCase;

/** Test class for {@link ComplexType}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class ComplexTypeTest
extends exemplarElementTestCase<ComplexType>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new ComplexType());
	}

	/** Test {@link ComplexType#getAbstract()}. */
	public void testGetAbstract() {
		assertTrue("getterTest returned false", getterTest("getAbstract"));
	}

	/** Test {@link ComplexType#setAbstract(String)}. */
	public void testSetAbstract() {
		assertTrue("setterTest returned false", setterTest("setAbstract"));
	}

	/** Test {@link ComplexType#getBlock()}. */
	public void testGetBlock() {
		assertTrue("getterTest returned false", getterTest("getBlock"));
	}

	/** Test {@link ComplexType#setBlock(String)}. */
	public void testSetBlock() {
		assertTrue("setterTest returned false", setterTest("setBlock"));
	}

	/** Test {@link ComplexType#getFinal()}. */
	public void testGetFinal() {
		assertTrue("getterTest returned false", getterTest("getFinal"));
	}

	/** Test {@link ComplexType#setFinal(String)}. */
	public void testSetFinal() {
		assertTrue("setterTest returned false", setterTest("setFinal"));
	}

	/** Test {@link ComplexType#getId()}. */
	public void testGetId() {
		assertTrue("getterTest returned false", getterTest("getId"));
	}

	/** Test {@link ComplexType#setId(String)}. */
	public void testSetId() {
		assertTrue("setterTest returned false", setterTest("setId"));
	}

	/** Test {@link ComplexType#getMixed()}. */
	public void testGetMixed() {
		assertTrue("getterTest returned false", getterTest("getMixed"));
	}

	/** Test {@link ComplexType#setMixed(String)}. */
	public void testSetMixed() {
		assertTrue("setterTest returned false", setterTest("setMixed"));
	}

	/** Test {@link ComplexType#getName()}. */
	public void testGetName() {
		assertTrue("getterTest returned false", getterTest("getName"));
	}

	/** Test {@link ComplexType#setName(String)}. */
	public void testSetName() {
		assertTrue("setterTest returned false", setterTest("setName"));
	}
}
