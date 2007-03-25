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

import java.text.ParseException;

import com.mcdermottroe.exemplar.utils.XML;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link XML}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class XMLTest extends UtilityClassTestCase {
	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing no character references.
	*/
	public void testResolveCharacterReferencesNoRefs() {
		try {
			assertEquals(
				"foobarbaz",
				XML.resolveCharacterReferences("foobarbaz")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one decimal character reference.
	*/
	public void testResolveCharacterReferencesOneDecimalRef() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("f&#111;o")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one hexadecimal character reference.
	*/
	public void testResolveCharacterReferencesOneHexadecimalRef() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("f&#x6f;o")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one decimal character reference at the start of the
		{@link String}.
	*/
	public void testResolveCharacterReferencesOneDecimalRefAtStart() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("&#102;oo")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one hexadecimal character reference at the start of
		the {@link String}.
	*/
	public void testResolveCharacterReferencesOneHexadecimalRefAtStart() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("&#x66;oo")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one decimal character reference at the end of the
		{@link String}.
	*/
	public void testResolveCharacterReferencesOneDecimalRefAtEnd() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("fo&#111;")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing one hexadecimal character reference at the end of
		the {@link String}.
	*/
	public void testResolveCharacterReferencesOneHexadecimalRefAtEnd() {
		try {
			assertEquals(
				"foo",
				XML.resolveCharacterReferences("fo&#x6f;")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} consisting solely of a decimal character reference.
	*/
	public void testResolveCharacterReferencesDecimalRefOnly() {
		try {
			assertEquals(
				"o",
				XML.resolveCharacterReferences("&#111;")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} consisting solely of a hexadecimal character reference.
	*/
	public void testResolveCharacterReferencesHexadecimalRefOnly() {
		try {
			assertEquals(
				"o",
				XML.resolveCharacterReferences("&#x6f;")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a character reference.
	*/
	public void testResolveCharacterReferencesEntityReference() {
		try {
			assertEquals(
				"Q&amp;A",
				XML.resolveCharacterReferences("Q&amp;A")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing entity and character references.
	*/
	public void testResolveCharacterReferencesMixedEntityAndCharacterRefs() {
		try {
			assertEquals(
				"Q&amp;A",
				XML.resolveCharacterReferences("&#x51;&amp;&#65;")
			);
		} catch (ParseException e) {
			e.printStackTrace();
			fail("ParseException thrown when resolving character references");
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing an unterminated reference.
	*/
	public void testResolveCharacterReferencesUnterminatedReference() {
		try {
			XML.resolveCharacterReferences("A&ampA");
			fail("Unterminated reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue("Unterminated reference correctly detected.", true);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing an unterminated reference at the start of the {@link
		String}.
	*/
	public void testResolveCharacterReferencesUnterminatedReferenceAtStart() {
		try {
			XML.resolveCharacterReferences("&ampA");
			fail("Unterminated reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue("Unterminated reference correctly detected.", true);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing an unterminated reference at the end of the {@link
		String}.
	*/
	public void testResolveCharacterReferencesUnterminatedReferenceAtEnd() {
		try {
			XML.resolveCharacterReferences("Q&");
			fail("Unterminated reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue("Unterminated reference correctly detected.", true);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a malformed decimal character reference at the end
		of the {@link String}.
	*/
	public void testResolveCharacterReferencesMalformedDecimalCharRef() {
		try {
			XML.resolveCharacterReferences("f&#6f;o");
			fail("Malformed character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Malformed character reference correctly detected.",
				true
			);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a malformed hexadecimal character reference at the
		end of the {@link String}.
	*/
	public void testResolveCharacterReferencesMalformedHexadecimalCharRef() {
		try {
			XML.resolveCharacterReferences("f&#x123z;o");
			fail("Malformed character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Malformed character reference correctly detected.",
				true
			);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a negative decimal character reference.
	*/
	public void testResolveCharacterReferencesNegativeDecimalReference() {
		try {
			XML.resolveCharacterReferences("&#-10;");
			fail("Negative character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Negative character reference correctly detected.",
				true
			);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a negative hexadecimal character reference.
	*/
	public void testResolveCharacterReferencesNegativeHexadecimalReference() {
		try {
			XML.resolveCharacterReferences("&#x-A;");
			fail("Negative character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Negative character reference correctly detected.",
				true
			);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a decimal character reference which refers to a
		character number which is outside the range of characters.
	*/
	public void testResolveCharacterReferencesOutOfRangeDecimalReference() {
		try {
			XML.resolveCharacterReferences("&#123456;");
			fail("Out of range character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Out of range character reference correctly detected.",
				true
			);
		}
	}

	/** Test {@link XML#resolveCharacterReferences(CharSequence)} with a {@link
		String} containing a hexadecimal character reference which refers to a
		character number which is outside the range of characters.
	*/
	public void testResolveCharacterReferencesOutOfRangeHexadecimalReference() {
		try {
			XML.resolveCharacterReferences("&#x10000;");
			fail("Out of range character reference not correctly detected.");
		} catch (ParseException e) {
			assertTrue(
				"Out of range character reference correctly detected.",
				true
			);
		}
	}
}
