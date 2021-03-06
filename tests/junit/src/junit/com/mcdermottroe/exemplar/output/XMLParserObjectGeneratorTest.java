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
package junit.com.mcdermottroe.exemplar.output;

import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.XMLParserObjectGenerator;

import junit.com.mcdermottroe.exemplar.AbstractClassTestCase;

/** Test class for {@link XMLParserObjectGenerator}.

	@author	Conor McDermottroe
	@since	0.1
	@param	<T>	The type of {@link XMLParserObjectGenerator} to test.
*/
public class XMLParserObjectGeneratorTest<T extends XMLParserObjectGenerator<T>>
extends AbstractClassTestCase<T>
{
	/** Test a {@link TrivialXMLParserObjectGenerator} to test the {@link
		XMLParserObjectGenerator}.
	*/
	public void testTrivialExtension() {
		TrivialXMLParserObjectGenerator gen =
			new TrivialXMLParserObjectGenerator();
		assertNotNull("Failed to create an XMLParserObjectGenerator", gen);
		assertNull("generateParser failed", gen.generateParser(null));
	}

	/** A trivial stub of {@link XMLParserObjectGenerator} for testing purposes.

		@author Conor McDermottroe
		@since	0.2
	*/
	private class TrivialXMLParserObjectGenerator
	extends XMLParserObjectGenerator<TrivialXMLParserObjectGenerator>
	{
		/** {@inheritDoc} */
		@Override public Object generateParser(XMLDocumentType doctype) {
			return null;
		}

		/** {@inheritDoc} */
		@Override public String describeLanguage() {
			return "Trivial";
		}

		/** {@inheritDoc} */
		@Override public String describeAPI() {
			return null;
		}

		/** Compare this {@link TrivialXMLParserObjectGenerator} to another.

			@param	other	The {@link TrivialXMLParserObjectGenerator} to
							compare against.
			@return			See {@link Comparable#compareTo(Object)}.
		*/
		public int compareTo(TrivialXMLParserObjectGenerator other) {
			if (other == null) {
				throw new NullPointerException();
			}
			return 0;
		}

		/** {@inheritDoc} */
		public TrivialXMLParserObjectGenerator getCopy() {
			return new TrivialXMLParserObjectGenerator();
		}
	}
}
