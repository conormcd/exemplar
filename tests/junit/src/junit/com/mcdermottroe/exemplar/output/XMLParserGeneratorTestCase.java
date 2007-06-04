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
package junit.com.mcdermottroe.exemplar.output;

import java.io.File;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.input.InputException;
import com.mcdermottroe.exemplar.input.InputUtils;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.XMLParserGenerator;
import com.mcdermottroe.exemplar.ui.Options;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;
import junit.com.mcdermottroe.exemplar.input.InputUtilsTest;

/** Test class for children of {@link XMLParserGenerator}.

	@param	<T>	The type of {@link XMLParserGenerator} to test.
	@author	Conor McDermottroe
	@since	0.1
*/
public abstract
class XMLParserGeneratorTestCase<T extends XMLParserGenerator<T>>
extends NormalClassTestCase<T>
{
	/** A {@link Set} of {@link XMLDocumentType}s which the subclasses of this
		class can use for testing.
	*/
	private static Set<XMLDocumentType> sampleDocTypes =
		new HashSet<XMLDocumentType>();

	/** Access (and if necessary, create) {@link #sampleDocTypes}.

		@return	A collection of sample {@link XMLDocumentType}s.
	*/
	protected static Set<XMLDocumentType> getSampleDocTypes() {
		if (sampleDocTypes.isEmpty()) {
			Map<String, Collection<File>> samples;
			samples = InputUtilsTest.getSampleData();

			for (String lang : samples.keySet()) {
				Collection<File> files = samples.get(lang);
				for (File f : files) {
					try {
						sampleDocTypes.add(
							InputUtils.parse(f.getAbsolutePath(), lang)
						);
					} catch (InputException e) {
						// Ignore
					} catch (ParserException e) {
						// Ignore
					}
				}
			}
		}
		return new HashSet<XMLDocumentType>(sampleDocTypes);
	}

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		Options.reset();
		Options.set("include", "entities");
	}
}
