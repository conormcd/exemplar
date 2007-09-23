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
package junit.com.mcdermottroe.exemplar.output.java.binding;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementContentModel;
import com.mcdermottroe.exemplar.model.XMLElementContentType;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.java.binding.ElementNameConverter;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for children of {@link ElementNameConverter}.

	@param	<T>	The type of {@link ElementNameConverter} to test.
	@author	Conor McDermottroe
	@since	0.2
*/
public abstract class
ElementNameConverterTestCase<T extends ElementNameConverter<T>>
extends NormalClassTestCase<T>
{
	/** Test {@link ElementNameConverter#getClassName(XMLElement)}. */
	public void testGetClassName() {
		XMLElementContentModel cm = new XMLElementContentModel(
			XMLElementContentType.ANY
		);
		XMLElement[] elements = {
			null,
			new XMLElement("foo", cm),
			new XMLElement("bar:baz", cm),
		};
		String[] expected = {
			null,
			"Foo",
			"Baz",
		};
		for (T sample : samples()) {
			if (sample != null) {
				for (int i = 0; i < elements.length; i++) {
					try {
						assertEquals(
							"Output did not match expected",
							expected[i],
							sample.getClassName(elements[i])
						);
					} catch (XMLParserGeneratorException e) {
						assertNotNull(
							"XMLParserGeneratorException was null",
							e
						);
						fail("getClassName() threw an exception");
					}
				}
			}
		}
	}

	/** Test {@link ElementNameConverter#getClassName(XMLElement)}. */
	public void testGetClassNameNegative() {
		boolean fellThrough = false;
		try {
			ElementNameConverter<?> bad = new ElementNameConverter() {
				@Override protected String generateClassName(XMLElement element)
				throws XMLParserGeneratorException
				{
					return "\u0001";
				}

				public int compareTo(Object o) {
					return 0;
				}

				public Object getCopy()
				throws CopyException
				{
					return null;
				}
			};
			bad.getClassName(
				new XMLElement(
					"foo",
					new XMLElementContentModel(XMLElementContentType.ANY)
				)
			);
			fellThrough = true;
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
		}
		assertFalse("Bad ElementNameConverter fell through", fellThrough);
	}
}
