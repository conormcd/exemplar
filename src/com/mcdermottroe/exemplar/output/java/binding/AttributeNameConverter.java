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
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.utils.Strings;

/** A converter for turning attribute names into variable, getter and setter
	names.

	@param	<T>	The type of AttributeNameConverter.
	@author		Conor McDermottroe
	@since		0.2
*/
public abstract
class AttributeNameConverter<T extends AttributeNameConverter<T>>
implements Comparable<T>, Copyable<T>
{
	/** Create a new {@link AttributeNameConverter}. */
	protected AttributeNameConverter() {		
	}

	/** Convert an XML attribute into a variable name.

		@param	attribute					The attribute.
		@return								A name close to the attribute name
											which conforms to Java rules and
											customs for variable names.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	public final String getVariableName(XMLAttribute attribute)
	throws XMLParserGeneratorException
	{
		return getVariableName(attribute, null, null);
	}


	/** Convert an XML attribute into a variable name.

		@param	attribute					The attribute.
		@param	prefix						A prefix for the variable name.
		@return								A name close to the attribute name
											which conforms to Java rules and
											customs for variable names.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	public final String getVariableName(XMLAttribute attribute, String prefix)
	throws XMLParserGeneratorException
	{
		return getVariableName(attribute, prefix, null);
	}

	/** Convert an XML attribute into a variable name.

		@param	attribute					The attribute.
		@param	prefix						A prefix for the variable name.
		@param	suffix						A suffix for the variable name.
		@return								A name close to the attribute name
											which conforms to Java rules and
											customs for variable names.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	public final String getVariableName(
		XMLAttribute attribute,
		String prefix,
		String suffix
	)
	throws XMLParserGeneratorException
	{
		if (attribute == null) {
			return null;
		}

		// Build the variable name
		StringBuilder variableName = new StringBuilder();
		if (prefix != null) {
			variableName.append(Strings.lowerCaseFirst(prefix));
			variableName.append(
				Strings.upperCaseFirst(generateVariableName(attribute))
			);
		} else {
			variableName.append(
				Strings.lowerCaseFirst(generateVariableName(attribute))
			);
		}
		if (suffix != null) {
			variableName.append(Strings.upperCaseFirst(suffix));
		}

		// Make sure that the variable name is legal
		if (Strings.isLegalJavaIdentifier(variableName)) {
			return variableName.toString();
		} else {
			StringBuilder varName = new StringBuilder(variableName);
			varName.append("Attribute");
			String var = varName.toString();
			if (Strings.isLegalJavaIdentifier(var)) {
				return var;
			} else {
				throw new XMLParserGeneratorException(
					Message.GEN_VARNAME_FROM_ATT_FAILED(attribute.getName())
				);
			}
		}
	}

	/** Convert an XML attribute into a variable name.

		@param	attribute					The attribute.
		@return								A name close to the attribute name
											which conforms to Java rules and
											customs for variable names.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	protected abstract String generateVariableName(XMLAttribute attribute)
	throws XMLParserGeneratorException;

	/** Convert an XML attribute into the name of a getter for a similarly
		named variable.
		
		@param	attribute					The attribute.
		@return								A name close to the attribute name
											whichconforms to Java rules and
											customs for access methods.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	public final String getGetterName(XMLAttribute attribute)
	throws XMLParserGeneratorException
	{
		if (attribute == null) {
			return null;
		}
		String getterName = generateGetterName(attribute);
		if (!Strings.isLegalJavaIdentifier(getterName)) {
			throw new XMLParserGeneratorException(
				Message.GEN_GETTER_FROM_ATT_FAILED(attribute.getName())
			);
		}
		return getterName;
	}

	/** Convert an XML attribute into the name of a getter for a similarly
		named variable.
		
		@param	attribute					The attribute.
		@return								A name close to the attribute name
											whichconforms to Java rules and
											customs for access methods.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	protected String generateGetterName(XMLAttribute attribute)
	throws XMLParserGeneratorException
	{
		StringBuilder getter = new StringBuilder("get");
		getter.append(Strings.upperCaseFirst(generateVariableName(attribute)));
		return getter.toString();
	}

	/** Convert an XML attribute into the name of a setter for a similarly
		named variable.
		
		@param	attribute					The attribute.
		@return								A name close to the attribute name
											whichconforms to Java rules and
											customs for mutator methods.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	public final String getSetterName(XMLAttribute attribute)
	throws XMLParserGeneratorException
	{
		if (attribute == null) {
			return null;
		}
		String setterName = generateSetterName(attribute);
		if (!Strings.isLegalJavaIdentifier(setterName)) {
			throw new XMLParserGeneratorException(
				Message.GEN_SETTER_FROM_ATT_FAILED(attribute.getName())
			);
		}
		return setterName;
	}

	/** Convert an XML attribute into the name of a setter for a similarly
		named variable.
		
		@param	attribute					The attribute.
		@return								A name close to the attribute name
											whichconforms to Java rules and
											customs for mutator methods.
		@throws XMLParserGeneratorException	if a legal identifier could not be
											created.
	*/
	protected String generateSetterName(XMLAttribute attribute)
	throws XMLParserGeneratorException
	{
		StringBuilder setter = new StringBuilder("set");
		setter.append(Strings.upperCaseFirst(generateVariableName(attribute)));
		return setter.toString();
	}

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
