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

import com.mcdermottroe.exemplar.Utils;

/** Test class for {@link com.mcdermottroe.exemplar.Utils}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class UtilsTest extends UtilityClassTestCase {
	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two null
		references.
	*/
	public void testAreDeeplyEqualNullNull() {
		assertTrue(
			"null is equal to null",
			Utils.areDeeplyEqual(null, null)
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with one null and one
		non-null reference.
	*/
	public void testAreDeeplyEqualNullObject() {
		assertFalse(
			"null is not equal to a non-null Object",
			Utils.areDeeplyEqual(null, "NOT NULL")
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with one null and one
		non-null reference.
	*/
	public void testAreDeeplyEqualObjectNull() {
		assertFalse(
			"A non-null Object is not equal to null",
			Utils.areDeeplyEqual("NOT NULL", null)
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two non-null,
		equal {@link Object}s.
	*/
	public void testAreDeeplyEqualObjectObjectEqual() {
		assertTrue(
			"Two equal objects are equal",
			Utils.areDeeplyEqual("EQUAL", "EQUAL")
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two non-null,
		non-equal {@link Object}s.
	*/
	public void testAreDeeplyEqualObjectObjectNotEqual() {
		assertFalse(
			"Two non-equal objects are non-equal",
			Utils.areDeeplyEqual("EQUAL", "NOT EQUAL")
		);
	}
}
