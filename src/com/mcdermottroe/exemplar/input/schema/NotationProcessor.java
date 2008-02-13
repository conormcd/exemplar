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
import com.mcdermottroe.exemplar.generated.schema.element.Notation;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLNotation;

/**	A {@link W3CSchemaTreeOp} to process notation declarations in a schema
	document.

	@author Conor McDermottroe
	@since	0.2
*/
public class NotationProcessor
implements Copyable<NotationProcessor>, W3CSchemaTreeOp
{
	/** The {@link Parser} which is to use this {@link W3CSchemaTreeOp}. */
	private final Parser parentParser;

	/** Create a new {@link NotationProcessor} and provide a reference to the
		{@link Parser} which is using it.

		@param	parent	The parent {@link Parser} for this notation processor.
	*/
	public NotationProcessor(Parser parent) {
		parentParser = parent;
	}

	/** {@inheritDoc} */
	public boolean shouldApply(XMLComponent<?> treeComponent) {
		return Notation.class.isAssignableFrom(treeComponent.getClass());
	}

	/** {@inheritDoc} */
	public void execute(XMLComponent<?> treeComponent)
	throws W3CSchemaException
	{
		Notation not = Notation.class.cast(treeComponent);

		XMLExternalIdentifier ext = new XMLExternalIdentifier(
			not.getPublic(),
			not.getSystem()
		);
		XMLNotation notation = new XMLNotation(not.getName(), ext);
		parentParser.addMarkupDeclaration(notation);
	}

	/** Get the parent parser for this {@link NotationProcessor}.

		@return	The parent parser for this {@link NotationProcessor}.
	*/
	protected Parser getParentParser() {
		return parentParser;
	}

	/** {@inheritDoc} */
	public NotationProcessor getCopy()
	throws CopyException
	{
		return new NotationProcessor(parentParser);
	}

	/** {@inheritDoc} */
	public int compareTo(NotationProcessor other) {
		return Utils.compare(parentParser, other.getParentParser());
	}

	/** Generage a hash code for this {@link NotationProcessor}.

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
		if (!(o instanceof NotationProcessor)) {
			return false;
		}

		return compareTo(NotationProcessor.class.cast(o)) == 0;
	}
}
