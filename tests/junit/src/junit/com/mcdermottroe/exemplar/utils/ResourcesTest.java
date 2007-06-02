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
package junit.com.mcdermottroe.exemplar.utils;

import java.util.Map;

import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.utils.Resources;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link Resources}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class ResourcesTest
extends UtilityClassTestCase<Resources>
{
	/** Test {@link Resources#get(Class)}. */
	public void testGet() {
		Map<String, String> result = null;

		try {
			result = Resources.get(Resources.class);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Resources.get(Resources.class) failed an assertion");
		}
		assertNotNull("Resources.get(Resources.class) == null", result);
		assertTrue(
			"Resources.get(Resources.class).isEmpty() == false",
			result.isEmpty()
		);

		try {
			result = Resources.get(Message.class);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Resources.get(Message.class) failed an assertion");
		}
		assertNotNull("Resources.get(Message.class) == null", result);
		assertFalse(
			"Resources.get(Resources.class).isEmpty() == true",
			result.isEmpty()
		);
	}

	/** Test {@link Resources#getString(Class, String)}. */
	public void testGetString() {
		assertNull(
			"Non-null result for non-existant resource",
			Resources.getString(Resources.class, "foo")
		);
		for (String resourceName : Resources.get(Message.class).keySet()) {
			assertNotNull(
				"Null resource value",
				Resources.getString(Message.class, resourceName)
			);
		}
	}

	/** Test {@link Resources#getBoolean(Class, String, boolean)}. */
	public void testGetBoolean() {
		boolean result = false;

		try {
			result = Resources.getBoolean(Resources.class, "foo", true);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Resources.getBoolean(Class, String, boolean) failed assert");
		}
		assertTrue("False for true default", result);

		try {
			result = Resources.getBoolean(Resources.class, "foo", false);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Resources.getBoolean(Class, String, boolean) failed assert");
		}
		assertFalse("True for false default", result);

		for (String resourceName : Resources.get(Message.class).keySet()) {
			try {
				Resources.getBoolean(Message.class, resourceName, false);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail(
					"Resources.getBoolean(Class, String, boolean) failed assert"
				);
			}
		}
	}
}
