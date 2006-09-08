// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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
package junit.com.mcdermottroe.exemplar;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.ui.Options;

/** Test class for the {@link DBC} class.

	@author	Conor McDermottroe
	@since	0.1
*/
public class DBCTest extends UtilityClassTestCase {
	/** Test that ASSERT(true) works correctly.

		@see DBC#ASSERT(boolean)
	*/
	public void testAssertTrue() {
		String testName = "Testing that ASSERT(true) works";
		try {
			DBC.ASSERT(true);
		} catch (AssertionError e) {
			fail(testName);
			DBC._clearDelayedAssertation();
			return;
		}
		assertTrue(testName, true);
		DBC._clearDelayedAssertation();
	}

	/** Test that ASSERT(false) works correctly.

		@see DBC#ASSERT(boolean)
	*/
	public void testAssertFalse() {
		String testName = "Testing that ASSERT(false) works";
		try {
			DBC.ASSERT(false);
		} catch (AssertionError e) {
			assertTrue(testName, true);
			DBC._clearDelayedAssertation();
			return;
		}
		fail(testName);
		DBC._clearDelayedAssertation();
	}

	/** Test that the delayed assertion mechanism works as expected.

		@see DBC#ASSERT(boolean)
	*/
	public void testDelayedAssert() {
		Options.set("debug", "false");
		String testName = "Testing the delayed assertion mechanism.";
		try {
			DBC.ASSERT(false);
		} catch (AssertionError e) {
			if (Options.isDebugSet()) {
				assertTrue(testName, true);
				DBC._clearDelayedAssertation();
			} else {
				fail(testName);
				DBC._clearDelayedAssertation();
			}
			return;
		}
		Options.set("debug", "true");
		try {
			DBC.ASSERT(true);
		} catch (AssertionError e) {
			assertTrue(testName, true);
			DBC._clearDelayedAssertation();
			return;
		}
		fail(testName);
		DBC._clearDelayedAssertation();
	}
}
