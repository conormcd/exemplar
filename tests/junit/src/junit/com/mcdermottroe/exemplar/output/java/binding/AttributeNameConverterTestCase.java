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
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeContentType;
import com.mcdermottroe.exemplar.model.XMLAttributeDefaultType;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.java.binding.AttributeNameConverter;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for children of {@link AttributeNameConverter}.

	@param	<T>	The type of {@link AttributeNameConverter} to test.
	@author	Conor McDermottroe
	@since	0.2
*/
public abstract class
AttributeNameConverterTestCase<T extends AttributeNameConverter<T>>
extends NormalClassTestCase<T>
{
	/** Test {@link AttributeNameConverter#getVariableName(XMLAttribute)}. */
	public void testGetVariableName() {
		XMLAttribute[] attributes = {
			null,
		};
		String[] expected = {
			null,
		};
		for (T sample : samples()) {
			if (sample != null) {
				for (int i = 0; i < attributes.length; i++) {
					try {
						assertEquals(
							"Output did not match expected",
							expected[i],
							sample.getVariableName(attributes[i])
						);
					} catch (XMLParserGeneratorException e) {
						assertNotNull(
							"XMLParserGeneratorException was null",
							e
						);
						fail("getVariableName() threw an exception");
					}
				}
			}
		}
	}

	/** Test {@link AttributeNameConverter#getGetterName(XMLAttribute)}. */
	public void testGetGetterName() {
		XMLAttribute[] attributes = {
			null,
		};
		String[] expected = {
			null,
		};
		for (T sample : samples()) {
			if (sample != null) {
				for (int i = 0; i < attributes.length; i++) {
					try {
						assertEquals(
							"Output did not match expected",
							expected[i],
							sample.getGetterName(attributes[i])
						);
					} catch (XMLParserGeneratorException e) {
						assertNotNull(
							"XMLParserGeneratorException was null",
							e
						);
						fail("getGetterName() threw an exception");
					}
				}
			}
		}
	}

	/** Test {@link AttributeNameConverter#getSetterName(XMLAttribute)}. */
	public void testGetSetterName() {
		XMLAttribute[] attributes = {
			null,
		};
		String[] expected = {
			null,
		};
		for (T sample : samples()) {
			if (sample != null) {
				for (int i = 0; i < attributes.length; i++) {
					try {
						assertEquals(
							"Output did not match expected",
							expected[i],
							sample.getSetterName(attributes[i])
						);
					} catch (XMLParserGeneratorException e) {
						assertNotNull(
							"XMLParserGeneratorException was null",
							e
						);
						fail("getSetterName() threw an exception");
					}
				}
			}
		}
	}

	/** Test {@link AttributeNameConverter#getVariableName(XMLAttribute)}. */
	public void testGetVariableNameNegative() {
		boolean fellThrough = false;
		try {
			AttributeNameConverter<?> bad = new BadAttributeNameConverter();
			bad.getVariableName(
				new XMLAttribute(
					"foo",
					XMLAttributeContentType.CDATA(),
					XMLAttributeDefaultType.IMPLIED()
				)
			);
			fellThrough = true;
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
		}
		assertFalse("Bad AttributeNameConverter fell through", fellThrough);
	}

	/** Test {@link AttributeNameConverter#getGetterName(XMLAttribute)}. */
	public void testGetGetterNameNegative() {
		boolean fellThrough = false;
		try {
			AttributeNameConverter<?> bad = new BadAttributeNameConverter();
			bad.getGetterName(
				new XMLAttribute(
					"foo",
					XMLAttributeContentType.CDATA(),
					XMLAttributeDefaultType.IMPLIED()
				)
			);
			fellThrough = true;
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
		}
		assertFalse("Bad AttributeNameConverter fell through", fellThrough);
	}

	/** Test {@link AttributeNameConverter#getSetterName(XMLAttribute)}. */
	public void testGetSetterNameNegative() {
		boolean fellThrough = false;
		try {
			AttributeNameConverter<?> bad = new BadAttributeNameConverter();
			bad.getSetterName(
				new XMLAttribute(
					"foo",
					XMLAttributeContentType.CDATA(),
					XMLAttributeDefaultType.IMPLIED()
				)
			);
			fellThrough = true;
		} catch (XMLParserGeneratorException e) {
			assertNotNull("XMLParserGeneratorException was null", e);
		}
		assertFalse("Bad AttributeNameConverter fell through", fellThrough);
	}

	/** A bad implementation of an {@link AttributeNameConverter} which returns
		illegal results for {@link
		AttributeNameConverter#generateVariableName(XMLAttribute)}, {@link
		AttributeNameConverter#generateGetterName(XMLAttribute)} and {@link
		AttributeNameConverter#generateSetterName(XMLAttribute)}.

		@author	Conor McDermottroe
		@since	0.2
	*/
	private static class BadAttributeNameConverter
	extends AttributeNameConverter<BadAttributeNameConverter>
	{
		/** {@inheritDoc} */
		@Override protected String generateVariableName(XMLAttribute attribute)
		throws XMLParserGeneratorException
		{
			return "\u0001";
		}

		/** {@inheritDoc} */
		@Override protected String generateGetterName(XMLAttribute attribute)
		throws XMLParserGeneratorException
		{
			return "\u0002";
		}

		/** {@inheritDoc} */
		@Override protected String generateSetterName(XMLAttribute attribute)
		throws XMLParserGeneratorException
		{
			return "\u0003";
		}

		/** {@inheritDoc} */
		@Override public int compareTo(BadAttributeNameConverter o) {
			return 0;
		}

		/** {@inheritDoc} */
		public BadAttributeNameConverter getCopy()
		throws CopyException
		{
			return new BadAttributeNameConverter();
		}
	}
}
