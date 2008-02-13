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

import com.mcdermottroe.exemplar.generated.schema.element.Schema;

import junit.com.mcdermottroe.exemplar.generated.schema.support.exemplarElementTestCase;

/** Test class for {@link Schema}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class SchemaTest
extends exemplarElementTestCase<Schema>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new Schema());
	}

	/** Test {@link Schema#getAttributeFormDefault()}. */
	public void testGetAttributeFormDefault() {
		assertTrue(
			"getterTest returned false",
			getterTest("getAttributeFormDefault")
		);
	}

	/** Test {@link Schema#setAttributeFormDefault(String)}. */
	public void testSetAttributeFormDefault() {
		assertTrue(
			"setterTest returned false",
			setterTest("setAttributeFormDefault")
		);
	}

	/** Test {@link Schema#getBlockDefault()}. */
	public void testGetBlockDefault() {
		assertTrue("getterTest returned false", getterTest("getBlockDefault"));
	}

	/** Test {@link Schema#setBlockDefault(String)}. */
	public void testSetBlockDefault() {
		assertTrue("setterTest returned false", setterTest("setBlockDefault"));
	}

	/** Test {@link Schema#getElementFormDefault()}. */
	public void testGetElementFormDefault() {
		assertTrue(
			"getterTest returned false",
			getterTest("getElementFormDefault")
		);
	}

	/** Test {@link Schema#setElementFormDefault(String)}. */
	public void testSetElementFormDefault() {
		assertTrue(
			"setterTest returned false",
			setterTest("setElementFormDefault")
		);
	}

	/** Test {@link Schema#getFinalDefault()}. */
	public void testGetFinalDefault() {
		assertTrue("getterTest returned false", getterTest("getFinalDefault"));
	}

	/** Test {@link Schema#setFinalDefault(String)}. */
	public void testSetFinalDefault() {
		assertTrue("setterTest returned false", setterTest("setFinalDefault"));
	}

	/** Test {@link Schema#getId()}. */
	public void testGetId() {
		assertTrue("getterTest returned false", getterTest("getId"));
	}

	/** Test {@link Schema#setId(String)}. */
	public void testSetId() {
		assertTrue("setterTest returned false", setterTest("setId"));
	}

	/** Test {@link Schema#getLang()}. */
	public void testGetLang() {
		assertTrue("getterTest returned false", getterTest("getLang"));
	}

	/** Test {@link Schema#setLang(String)}. */
	public void testSetLang() {
		assertTrue("setterTest returned false", setterTest("setLang"));
	}

	/** Test {@link Schema#getTargetNamespace()}. */
	public void testGetTargetNamespace() {
		assertTrue(
			"getterTest returned false",
			getterTest("getTargetNamespace")
		);
	}

	/** Test {@link Schema#setTargetNamespace(String)}. */
	public void testSetTargetNamespace() {
		assertTrue(
			"setterTest returned false",
			setterTest("setTargetNamespace")
		);
	}

	/** Test {@link Schema#getVersion()}. */
	public void testGetVersion() {
		assertTrue("getterTest returned false", getterTest("getVersion"));
	}

	/** Test {@link Schema#setVersion(String)}. */
	public void testSetVersion() {
		assertTrue("setterTest returned false", setterTest("setVersion"));
	}

	/** Test {@link Schema#getXmlns()}. */
	public void testGetXmlns() {
		assertTrue("getterTest returned false", getterTest("getXmlns"));
	}

	/** Test {@link Schema#setXmlns(String)}. */
	public void testSetXmlns() {
		assertTrue("setterTest returned false", setterTest("setXmlns"));
	}

	/** Test {@link Schema#getXs()}. */
	public void testGetXs() {
		assertTrue("getterTest returned false", getterTest("getXs"));
	}
}
