// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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
package junit.com.mcdermottroe.exemplar.output;

import java.util.SortedSet;

import com.mcdermottroe.exemplar.output.LanguageLevel;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link LanguageLevel}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class LanguageLevelTest
extends NormalClassTestCase<LanguageLevel>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		addSample(new LanguageLevel("foo", "Language level foo"));
		addSample(new LanguageLevel("foo", "Language level foo foo"));
		addSample(new LanguageLevel("foo", "Language level foo", "foo"));
		addSample(new LanguageLevel("foo", "Language level foo", "foo", "bar"));
		addSample(new LanguageLevel("bar", "Language level bar"));
		addSample(new LanguageLevel("bar", "Language level bar bar"));
		addSample(new LanguageLevel("bar", "Language level bar", "foo"));
		addSample(new LanguageLevel("bar", "Language level bar", "foo", "bar"));
	}

	/** Test {@link LanguageLevel#getAliases()}. */
	public void testGetAliases() {
		for (LanguageLevel sample : samples()) {
			if (sample != null) {
				SortedSet<String> aliases = sample.getAliases();
				assertNotNull("getAliases() returned null", aliases);
				for (String alias : aliases) {
					assertNotNull("getAliases() contained a null alias", alias);
					assertNotSame(
						"getAliases() contained an empty alias",
						"",
						alias
					);
				}
			}
		}
	}

	/** Test {@link LanguageLevel#getDescription()}. */
	public void testGetDescription() {
		for (LanguageLevel sample : samples()) {
			if (sample != null) {
				String description = sample.getDescription();
				assertNotNull("getDescription() returned null", description);
				assertNotSame(
					"getDescription() returned an empty string",
					"",
					description
				);
			}
		}
	}

	/** Test {@link LanguageLevel#getLabel()}. */
	public void testGetLabel() {
		for (LanguageLevel sample : samples()) {
			if (sample != null) {
				String label = sample.getLabel();
				assertNotNull("getLabel() returned null", label);
				assertNotSame("getLabel() returned an empty string", "", label);
			}
		}
	}
}
