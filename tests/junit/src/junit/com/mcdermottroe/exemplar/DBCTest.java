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
package junit.com.mcdermottroe.exemplar;

import com.mcdermottroe.exemplar.DBC;

/** Test class for the {@link DBC} class.

	@author	Conor McDermottroe
	@since	0.1
*/
public class DBCTest
extends UtilityClassTestCase<DBC>
{
	/** Test that ASSERT(true) works correctly.

		@see DBC#ASSERT(boolean)
	*/
	public void testASSERTTrue() {
		try {
			DBC.ASSERT(true);
		} catch (AssertionError e) {
			e.printStackTrace();
			fail("ASSERT(true) threw an AssertionError");
		}
	}

	/** Test that ASSERT(false) works correctly.

		@see DBC#ASSERT(boolean)
	*/
	public void testASSERTFalse() {
		boolean fellThrough = false;
		try {
			DBC.ASSERT(false);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		if (fellThrough) {
			fail("ASSERT(false) did not throw an AssertionError");
		}
	}

	/** Test that REQUIRE(true) works correctly.

		@see DBC#REQUIRE(boolean)
	*/
	public void testREQUIRETrue() {
		try {
			DBC.REQUIRE(true);
		} catch (AssertionError e) {
			e.printStackTrace();
			fail("REQUIRE(true) threw an AssertionError");
		}
	}

	/** Test that REQUIRE(false) works correctly.

		@see DBC#REQUIRE(boolean)
	*/
	public void testREQUIREFalse() {
		boolean fellThrough = false;
		try {
			DBC.REQUIRE(false);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		if (fellThrough) {
			fail("REQUIRE(false) did not throw an AssertionError");
		}
	}

	/** Test that ENSURE(true) works correctly.

		@see DBC#ENSURE(boolean)
	*/
	public void testENSURETrue() {
		try {
			DBC.ENSURE(true);
		} catch (AssertionError e) {
			e.printStackTrace();
			fail("ENSURE(true) threw an AssertionError");
		}
	}

	/** Test that ENSURE(false) works correctly.

		@see DBC#ENSURE(boolean)
	*/
	public void testENSUREFalse() {
		boolean fellThrough = false;
		try {
			DBC.ENSURE(false);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		if (fellThrough) {
			fail("ENSURE(false) did not throw an AssertionError");
		}
	}

	/** Test {@link DBC#UNREACHABLE_CODE()}. */
	public void testUNREACHABLE_CODE() {
		boolean fellThrough = false;
		try {
			DBC.UNREACHABLE_CODE();
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse(
			"UNREACHABLE_CODE() did not throw an AssertionError",
			fellThrough
		);
	}

	/** Test {@link DBC#IGNORED_EXCEPTION(Throwable)}. */
	public void testIGNORED_EXCEPTION() {
		try {
			DBC.IGNORED_EXCEPTION(new Exception());
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("DBC.IGNORED_EXCEPTION(Throwable) threw an assert");
		}
	}
}
