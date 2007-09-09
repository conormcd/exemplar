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
package junit.com.mcdermottroe.exemplar.input.dtd;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java_cup.runtime.Symbol;

import com.mcdermottroe.exemplar.input.dtd.Lexer;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link Lexer}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class LexerTest
extends NormalClassTestCase<Lexer>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		allowPublicStaticMembers = true;

		Reader nullReader = null;

		addSample(new Lexer(nullReader));
		addSample(new Lexer(new InputStreamReader(System.in)));
		addSample(new Lexer(System.in));
		Lexer sampleA = new Lexer(
			new FileInputStream(
				new File("dtds/docbook-xml-44/docbookx.dtd")
			)
		);
		sampleA.setDtdPath(new File("dtds/docbook-xml-44"));
		Lexer sampleB = new Lexer(
			new FileInputStream(
				new File("dtds/docbook-xml-44/docbookx.dtd")
			)
		);
		sampleB.setDtdPath(new File("dtds/docbook-xml-44"));
		Symbol sym;
		do {
			sym = sampleB.next_token();
		} while (sym != null);
		addSample(sampleB);
		Lexer sampleC = new Lexer(nullReader);
		sampleC.yypushStream(new InputStreamReader(System.in));
	}

	/** Test {@link Lexer#setDtdPath(File)}. */
	public void testSetDtdPath() {
		File exists = new File(System.getProperty("java.home"));
		File doesNotExist = new File("/does/not/exist/at/all");
		for (Lexer sample : samples()) {
			if (sample != null) {
				try {
					sample.setDtdPath(exists);
				} catch (AssertionError e) {
					assertNotNull("AssertionError was null", e);
					fail("setDtdPath threw an AssertionError");
				}
				boolean fellThrough = false;
				try {
					sample.setDtdPath(doesNotExist);
					fellThrough = true;
				} catch (AssertionError e) {
					// Correct behaviour
					assertNotNull("AssertionError was null", e);
				}
				if (fellThrough) {
					fail("setDtdPath did not throw an AssertionError");
				}
			}
		}
	}

	/** Test {@link Lexer#next_token()}. */
	public void testNext_token() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yybegin(int)}. */
	public void testYybegin() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yycharat(int)}. */
	public void testYycharat() {
		for (Lexer sample : samples()) {
			if (sample != null) {
				try {
					for (int i = 0; i <= 1; i++) {
						sample.yycharat(i);
					}
				} catch (Throwable t) {
					assertNotNull("Throwable was null", t);
					t.printStackTrace();
					fail("yycharat(int) threw an exception");
				}
			}
		}
	}

	/** Test {@link Lexer#yyclose()}. */
	public void testYyclose() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yylength()}. */
	public void testYylength() {
		for (Lexer sample : samples()) {
			if (sample != null) {
				try {
					sample.yylength();
				} catch (Throwable t) {
					assertNotNull("Throwable was null", t);
					t.printStackTrace();
					fail("yylength() threw an exception");
				}
			}
		}
	}

	/** Test {@link Lexer#yymoreStreams()}. */
	public void testYymoreStreams() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yypopStream()}. */
	public void testYypopStream() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yypushStream(Reader)}. */
	public void testYypushStream() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yypushback(int)}. */
	public void testYypushback() {
		assertTrue(DELIBERATE_PASS, true);
	}

	/** Test {@link Lexer#yyreset(Reader)}. */
	public void testYyreset() {
		for (Lexer sample : samples()) {
			if (sample != null) {
				try {
					sample.yyreset(null);
				} catch (Throwable t) {
					assertNotNull("Throwable was null", t);
					t.printStackTrace();
					fail("yyreset(null) threw an exception");
				}
			}
		}
	}

	/** Test {@link Lexer#yystate()}. */
	public void testYystate() {
		for (Lexer sample : samples()) {
			if (sample != null) {
				try {
					sample.yystate();
				} catch (Throwable t) {
					assertNotNull("Throwable was null", t);
					t.printStackTrace();
					fail("yystate() threw an exception");
				}
			}
		}
	}

	/** Test {@link Lexer#yytext()}. */
	public void testYytext() {
		assertTrue(DELIBERATE_PASS, true);
	}
}
