// vim:filetype=java:ts=4
/*
	Copyright (c) 2007, 2008
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
package com.mcdermottroe.exemplar.input.schema;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.generated.schema.element.Element;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementContentModel;
import com.mcdermottroe.exemplar.model.XMLElementContentType;

/**	A {@link W3CSchemaTreeOp} which will turn portions of the parse tree into
	{@link XMLElement}s.

	@author Conor McDermottroe
	@since	0.2
*/
public class ElementProcessor
implements Copyable<ElementProcessor>, W3CSchemaTreeOp
{
	/** The parent {@link Parser}. */
	private final Parser parentParser;

	/** Create a new {@link ElementProcessor}.

		@param	parent	The parent {@link Parser}.
	*/
	public ElementProcessor(Parser parent) {
		parentParser = parent;
	}

	/** {@inheritDoc} */
	public boolean shouldApply(XMLComponent<?> treeComponent) {
		return Element.class.isAssignableFrom(treeComponent.getClass());
	}

	/** {@inheritDoc} */
	public void execute(XMLComponent<?> treeComponent)
	throws W3CSchemaException
	{
		Element el = Element.class.cast(treeComponent);

		// Extract the attributes
		boolean elementAbstract = Boolean.valueOf(el.getAbstract());
		int elementBlock = 0; // SCHEMATASK: Do this properly
		String elementDefault = el.getDefault();
		int elementFinal = 0; // SCHEMATASK: Do this properly
		String elementFixed = el.getFixed();
		String elementForm = el.getForm();
		String elementId = el.getId();
		int elementMaxOccurs = 1;
		String elementMaxOccursString = el.getMaxOccurs();
		if (elementMaxOccursString != null) {
			if ("unbounded".equals(elementMaxOccursString)) {
				elementMaxOccurs = Integer.MAX_VALUE;
			} else {
				elementMaxOccurs = Integer.valueOf(elementMaxOccursString);
			}
		}
		int elementMinOccurs = 1;
		String elementMinOccursString = el.getMinOccurs();
		if (elementMinOccursString != null) {
			elementMinOccurs = Integer.valueOf(elementMinOccursString);
		}
		String elementName = el.getName();
		boolean elementNillable = Boolean.valueOf(el.getNillable());
		String elementRef = el.getRef();
		// SCHEMATASK: below
		String elementSubstitutionGroup = el.getSubstitutionGroup();
		String elementType = el.getType();

		if (elementName == null) {
			return;
		}

		XMLElementContentModel content = new XMLElementContentModel(
			XMLElementContentType.ANY
		);
		XMLElement element = new XMLElement(elementName, content);
		parentParser.addMarkupDeclaration(element);
	}

	/** Get the parent parser for this processor.

		@return	The parent parser for this processor.
	*/
	protected Parser getParentParser() {
		return parentParser;
	}

	/** {@inheritDoc} */
	public ElementProcessor getCopy()
	throws CopyException
	{
		return new ElementProcessor(parentParser);
	}

	/** {@inheritDoc} */
	public int compareTo(ElementProcessor other) {
		return Utils.compare(parentParser, other.getParentParser());
	}

	/** Generage a hash code for this {@link ElementProcessor}.

		@return	A hashcode for this object.
	*/
	@Override public int hashCode() {
		return parentParser.hashCode();
	}

	/** Test for equality against another object.

		@param	o	The {@link Object} to test against.
		@return		True if this is equal to o, false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof ElementProcessor)) {
			return false;
		}

		return compareTo(ElementProcessor.class.cast(o)) == 0;
	}
}
