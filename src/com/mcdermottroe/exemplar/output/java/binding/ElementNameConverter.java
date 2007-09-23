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
package com.mcdermottroe.exemplar.output.java.binding;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.utils.Strings;

/** A converter for defining classes which change element names to class
	names.

	@param	<T>	The type of {@link ElementNameConverter}.
	@author		Conor McDermottroe
	@since		0.2
*/
public abstract class ElementNameConverter<T extends ElementNameConverter<T>>
implements Comparable<T>, Copyable<T>
{
	/** Create a new {@link ElementNameConverter}. */
	protected ElementNameConverter() {
	}

	/** Convert an XML element name into a class name.

		@param	element						The name of the element, potentially
											qualified with a namespace.
		@return								A name close to the element name
											which conforms to Java rules and
											customs.
		@throws	XMLParserGeneratorException	if a legal identifer is not
											generated.
	*/
	public final String getClassName(XMLElement element)
	throws XMLParserGeneratorException
	{
		if (element == null) {
			return null;
		}
		String className = generateClassName(element);
		if (!Strings.isLegalJavaIdentifier(className)) {
			throw new XMLParserGeneratorException(
				Message.GEN_CLASS_FROM_ELEMENT_FAILED(element.getName())
			);
		}
		return className;
	}

	/** Do the actual work of creating the class name.

		@param	element						The name of the element, potentially
											qualified with a namespace.
		@return								A name close to the element name
											which conforms to Java rules and
											customs.
		@throws	XMLParserGeneratorException	if a legal identifer is not
											generated.
	*/
	protected abstract String generateClassName(XMLElement element)
	throws XMLParserGeneratorException;

	/** Implement {@link Comparable#compareTo(Object)}.

		@param	other	See {@link Comparable#compareTo(Object)}.
		@return			See {@link Comparable#compareTo(Object)}.
	*/
	public int compareTo(T other) {
		return getClass().getName().compareTo(other.getClass().getName());
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	See {@link Object#equals(Object)}.
		@return		See {@link Object#equals(Object)}.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!getClass().equals(o.getClass())) {
			return false;
		}

		return true;		
	}

	/** Implement {@link Object#hashCode()}.

		@return	See {@link Object#hashCode()}.
	*/
	@Override public int hashCode() {
		return getClass().hashCode();
	}
}
