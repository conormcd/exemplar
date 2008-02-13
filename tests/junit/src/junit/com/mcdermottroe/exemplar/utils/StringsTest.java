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

import java.text.MessageFormat;

import com.mcdermottroe.exemplar.utils.PowerSet;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.EOL;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link Strings}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class StringsTest
extends UtilityClassTestCase<Strings>
{
	/** Basic sanity check for {@link Strings#formatMessage(String, Object...)}.
	*/
	public void testFormatMessageOneArg() {
		String formatString = "{0}";
		String variable = "variable"; // NON-NLS
		assertEquals(
			"Bad result from Strings.formatMessage(String, Object)",
			variable,
			Strings.formatMessage(formatString, variable)
		);
	}

	/** Basic sanity check for {@link Strings#formatMessage(String, Object...)}.
	*/
	public void testFormatMessageManyArgs() {
		String formatMessage = "{{0}{}{1,date,long}} ";
		String result = Strings.formatMessage(formatMessage, "foo", 0);
		String expectedResult =	"{foo{}" +
								MessageFormat.format("{0,date,long}", 0) +
								"} ";

		assertEquals(
			"Strings.formatMessage(String, Object...)",
			expectedResult,
			result
		);
	}

	/** Basic sanity check for {@link Strings#formatMessage(String, Object...)}.
	*/
	public void testFormatMessageWithArgs() {
		String formatMessage = "if ({0} == {1}) {";
		String result = Strings.formatMessage(formatMessage, "a", "b");
		String expectedResult =	"if (a == b) {";

		assertEquals(
			"Strings.formatMessage(String, Object...)",
			expectedResult,
			result
		);
	}

	/** Test {@link Strings#formatMessage(String, Object...)}. */
	public void testFormatMessageNullFormat() {
		assertNull(
			"Strings.formatMessage(null, ...) returns null",
			Strings.formatMessage(null, "foo")
		);
	}

	/** Test {@link Strings#formatMessage(String, Object...)}. */
	public void testFormatMessageNullArgs() {
		assertEquals(
			"Strings.formatMessage(\"foo {0}\", null) returns \"foo {0}\"",
			"foo {0}",
			Strings.formatMessage("foo {0}", (Object[])null)
		);
	}

	/** Test {@link Strings#toCanonicalForm(CharSequence)}. */
	public void testToCanonicalForm() {
		String[] input = {
			null,
			"",
			"foo",
		};
		String[] expected = {
			null,
			"",
			"\\u0066\\u006F\\u006F",
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				Strings.join("", "Strings.toCanonicalForm(", input[i], ")"),
				expected[i],
				Strings.toCanonicalForm(input[i])
			);
		}
	}

	/** Test {@link Strings#trimTrailingSpace(CharSequence)}. */
	public void testTrimTrailingSpace() {
		String[] input = {
			null,
			"",
			" ",
			"\n",
			"\r",
			"\r\n",
			"\n\r",
			"foo",
			"foo ",
			"foo bar",
			"foo bar ",
			"foo\t",
			"foo\tbar",
			"foo\tbar\t",
			"foo\n",
			"foo \n",
			"foo bar\n",
			"foo bar \n",
			"foo\t\n",
			"foo\tbar\n",
			"foo\tbar\t\n",
		};
		String[] expected = {
			null,
			"",
			"",
			EOL,
			EOL,
			EOL,
			EOL,
			"foo",
			"foo",
			"foo bar",
			"foo bar",
			"foo",
			"foo\tbar",
			"foo\tbar",
			"foo" + EOL,
			"foo" + EOL,
			"foo bar" + EOL,
			"foo bar" + EOL,
			"foo" + EOL,
			"foo\tbar" + EOL,
			"foo\tbar" + EOL,
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Strings.trimTrailingSpace(\"" + input[i] + "\")",
				expected[i],
				Strings.trimTrailingSpace(input[i])
			);
		}
	}

	/** Test {@link Strings#wrap(CharSequence, int)}. */
	public void testWrapTwoArgs() {
		String[] input = {
			null,
			"a aa aaa aaaa aaaaa aaaa aaa aa a",
			"12345678901234567890",
			"a a a a a 012345678901234567890 a a a a a",
		};
		String[] expected = {
			null,
			"a aa aaa " + EOL + "aaaa aaaaa" + EOL + "aaaa aaa " + EOL + "aa a",
			"12345678901234567890",
			"a a a a a " + EOL + "012345678901234567890" + EOL + "a a a a a",
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Strings.wrap(\"" + input[i] + "\")",
				expected[i],
				Strings.wrap(input[i], 10)
			);
		}
	}

	/** Do some negative testing on {@link Strings#wrap(CharSequence, int)}. */
	public void testWrapTwoArgsNegative() {
		boolean fellThrough = false;
		try {
			Strings.wrap("foo", -1);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
		try {
			Strings.wrap("foo", 0);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
	}

	/** Test {@link Strings#wrap(CharSequence, int, int)}. */
	public void testWrapThreeArgs() {
		String[] input = {
			null,
			"a aa aaa aaaa aaaaa aaaa aaa aa a",
			"12345678901234567890",
			"a a a a a 012345678901234567890 a a a a a",
		};
		String[] expected = {
			null,
			"a aa aaa " + EOL + "  aaaa aaaaa" + EOL +
				"  aaaa aaa " + EOL + "  aa a",
			"12345678901234567890",
			"a a a a a " + EOL + "  012345678901234567890" +
				EOL + "  a a a a a",
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Strings.wrap(\"" + input[i] + "\")",
				expected[i],
				Strings.wrap(input[i], 12, 2)
			);
		}
	}

	/** Do some negative testing on {@link
		Strings#wrap(CharSequence, int, int)}.
	*/
	public void testWrapThreeArgsNegative() {
		boolean fellThrough = false;
		try {
			Strings.wrap("foo", -1, 0);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
		try {
			Strings.wrap("foo", 0, 0);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
		try {
			Strings.wrap("foo", 100, -1);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
		try {
			Strings.wrap("foo", 10, 20);
			fellThrough = true;
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		assertFalse("Fell through", fellThrough);
	}

	/** Test {@link Strings#nativeLineEndings(CharSequence)}. */
	public void testNativeLineEndings() {
		String[] input = {
			null,
			"foo",
			"foo\r",
			"foo\r\n",
			"foo\n\r",
			"foo\n",
			"foo\rbar",
			"foo\r\nbar",
			"foo\n\rbar",
			"foo\nbar",
			"\r\r",
			"\n\n",
		};
		String[] expected = {
			null,
			"foo",
			"foo" + EOL,
			"foo" + EOL,
			"foo" + EOL,
			"foo" + EOL,
			"foo" + EOL + "bar",
			"foo" + EOL + "bar",
			"foo" + EOL + "bar",
			"foo" + EOL + "bar",
			EOL + EOL,
			EOL + EOL,
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Strings.nativeLineEndings(\"" + input[i] + "\")",
				expected[i],
				Strings.nativeLineEndings(input[i])
			);
		}
	}

	/** Test {@link Strings#deepToString(Object[])}. */
	public void testDeepToString() {
		Object[] input = {
			null,
			"foo",
			new PowerSet(),
		};
		Object[] expected = {
			"[null]",
			"[foo]",
			"[Power set of the set {  }]",
		};

		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Strings.deepToString(\"" + input[i] + "\")",
				expected[i],
				Strings.deepToString(input[i])
			);
		}
		assertEquals(
			"Strings.deepToString(input)",
			"[null, foo, Power set of the set {  }]",
			Strings.deepToString((Object[])input)
		);
	}

	/** Test {@link Strings#join(CharSequence, Object...)}. */
	public void testJoin() {
		String[][][] input = {
			{
				{
					null,
				},
				{
				},
			},
			{
				{
					"",
				},
				{
					"foo",
					"bar",
					"baz",
				},
			},
			{
				{
					",",
				},
				{
					"foo",
					"bar",
					"baz",
				},
			},
		};
		String[] expected = {
			"",
			"foobarbaz",
			"foo,bar,baz",
		};

		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals("Broken test data", 2, input[i].length);
			assertEquals("Broken test data", 1, input[i][0].length);
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.join(input[i][0][0], (Object[])input[i][1])
			);
		}
	}

	/** Test {@link Strings#indent(String, int)}. */
	public void testIndent() {
		Object[][] input = {
			{"foo", 0, },
		};
		Object[] expected = {
			"foo",
		};

		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals("Broken test data", 2, input[i].length);
			assertTrue("Broken test data", input[i][0] instanceof String);
			assertTrue("Broken test data", input[i][1] instanceof Integer);
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.indent(
					(String)input[i][0],
					(Integer)input[i][1]
				)
			);
		}
	}

	/** Test {@link Strings#upperCaseFirst(CharSequence)}. */
	public void testUpperCaseFirst() {
		String[] input = {
			null,
			"",
			"foo",
			"FOO",
			"!",
		};
		String[] expected = {
			null,
			"",
			"Foo",
			"FOO",
			"!",
		};
		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.upperCaseFirst(input[i])
			);
		}
	}

	/** Test {@link Strings#lowerCaseFirst(CharSequence)}. */
	public void testLowerCaseFirst() {
		String[] input = {
			null,
			"",
			"foo",
			"FOO",
			"!",
		};
		String[] expected = {
			null,
			"",
			"foo",
			"fOO",
			"!",
		};
		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.lowerCaseFirst(input[i])
			);
		}
	}

	/** Test {@link Strings#isLegalJavaIdentifier(CharSequence)}. */
	public void testIsLegalJavaIdentifier() {
		String[] input = {
			null,
			"",
			"foo",
			"!bar",
			"b!ar",
			"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"const",
			"continue",
			"default",
			"do",
			"double",
			"else",
			"enum",
			"extends",
			"final",
			"finally",
			"float",
			"for",
			"goto",
			"if",
			"implements",
			"import",
			"instanceof",
			"int",
			"interface",
			"long",
			"native",
			"new",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"short",
			"static",
			"strictfp",
			"super",
			"switch",
			"synchronized",
			"this",
			"throw",
			"throws",
			"transient",
			"try",
			"void",
			"volatile",
			"while",
		};
		boolean[] expected = {
			false,
			false,
			true,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
			false,
		};
		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.isLegalJavaIdentifier(input[i])
			);
		}
	}

	/** Test {@link Strings#isJavaKeyword(CharSequence)}. */
	public void testIsJavaKeyword() {
		String[] input = {
			null,
			"",
			"foo",
			"!bar",
			"b!ar",
			"abstract",
			"assert",
			"boolean",
			"break",
			"byte",
			"case",
			"catch",
			"char",
			"class",
			"const",
			"continue",
			"default",
			"do",
			"double",
			"else",
			"enum",
			"extends",
			"final",
			"finally",
			"float",
			"for",
			"goto",
			"if",
			"implements",
			"import",
			"instanceof",
			"int",
			"interface",
			"long",
			"native",
			"new",
			"package",
			"private",
			"protected",
			"public",
			"return",
			"short",
			"static",
			"strictfp",
			"super",
			"switch",
			"synchronized",
			"this",
			"throw",
			"throws",
			"transient",
			"try",
			"void",
			"volatile",
			"while",
		};
		boolean[] expected = {
			false,
			false,
			false,
			false,
			false,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
			true,
		};
		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Input does not match expected output",
				expected[i],
				Strings.isJavaKeyword(input[i])
			);
		}
	}
}
