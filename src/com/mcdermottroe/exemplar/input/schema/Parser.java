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
package com.mcdermottroe.exemplar.input.schema;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.xml.sax.SAXException;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.generated.schema.W3CSchema;
import com.mcdermottroe.exemplar.generated.schema.element.Import;
import com.mcdermottroe.exemplar.generated.schema.element.Include;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;
import com.mcdermottroe.exemplar.input.AbstractInputModule;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLNamedObject;
import com.mcdermottroe.exemplar.ui.Message;

import static com.mcdermottroe.exemplar.Constants.CWD;

/** An input module for W3C XML schema documents.

	@author	Conor McDermottroe
	@since	0.2
*/
public class Parser
extends AbstractInputModule<Parser>
{
	/** The directory where the schema is rooted in. We can use this to resolve
		references to relative file paths.
	*/
	protected File baseDirectory;

	/** The resulting set of markup declarations. */
	protected Set<XMLNamedObject<?>> markupDeclarations;

	/** Create a new {@link Parser} for parsing a schema in the current working
		directory (unless specified otherwise in {@link #parse(InputStream,
		File)} or {@link #parse(String)} or {@link #parse(File)}.
	*/
	public Parser() {
		super();
		baseDirectory = new File(CWD);
		markupDeclarations = new HashSet<XMLNamedObject<?>>();
	}

	/** {@inheritDoc} */
	@Override protected XMLDocumentType parse(InputStream input, File baseDir)
	throws ParserException
	{
		DBC.REQUIRE(input != null);
		DBC.REQUIRE(baseDir != null);

		baseDirectory = baseDir;

		// Read in the document
		XMLComponent<?> document;
		try {
			document = W3CSchema.read(input);
		} catch (IOException e) {
			throw new ParserException(Message.SCHEMA_READ_ERROR(), e);
		} catch (SAXException e) {
			throw new ParserException(Message.SCHEMA_READ_ERROR(), e);
		}

		// Walk the tree and process all of the includes and imports
		List<W3CSchemaTreeOp> includeOps = new ArrayList<W3CSchemaTreeOp>();
		includeOps.add(new ImportProcessor(baseDirectory));
		includeOps.add(new IncludeProcessor(baseDirectory));
		while (
			document.contains(Import.class) ||
			document.contains(Include.class)
		)
		{
			try {
				W3CSchema.walk(document, includeOps);
			} catch (W3CSchemaException e) {
				throw new ParserException(e);
			}
		}

		// Learn all of the types declared in the tree
		List<W3CSchemaTreeOp> typeOps = new ArrayList<W3CSchemaTreeOp>();
		typeOps.add(new TypeProcessor());
		try {
			W3CSchema.walk(document, typeOps);
		} catch (W3CSchemaException e) {
			throw new ParserException(e);
		}

		// Now do the main pass
		List<W3CSchemaTreeOp> mainOps = new ArrayList<W3CSchemaTreeOp>();
		mainOps.add(new ElementProcessor(this));
		mainOps.add(new NotationProcessor(this));
		try {
			W3CSchema.walk(document, mainOps);
		} catch (W3CSchemaException e) {
			throw new ParserException(e);
		}

		return new XMLDocumentType(markupDeclarations);
	}

	/** A method to allow the processors to add markup objects to this parser.

		@param	decl	The {@link XMLNamedObject} to add.
	*/
	public void addMarkupDeclaration(XMLNamedObject<?> decl) {
		markupDeclarations.add(decl);
	}

	/** Private getter for the base directory property (for {@link
		#compareTo(Parser)} and friends.

		@return	The value of {@link #baseDirectory}.
	*/
	private File getBaseDirectory() {
		return baseDirectory;
	}

	/** Private getter for the markup declarations so that we can examine them
		in {@link #compareTo(Parser)} and friends.

		@return	A copy of the {@link #markupDeclarations} set.
	*/
	private Set<XMLNamedObject<?>> getMarkupDeclarations() {
		return new HashSet<XMLNamedObject<?>>(markupDeclarations);
	}

	/** Implement {@link Comparable#compareTo(Object)}.

		@param	parser	The {@link Parser} to compare against.
		@return			0, always 0.
	*/
	public int compareTo(Parser parser) {
		int cmp = Utils.compare(baseDirectory, parser.getBaseDirectory());
		if (cmp != 0) {
			return cmp;
		}

		// This is a hack, we can't compare exactly, so just compare on size and
		// if the sizes are equal and the sets aren't then we compare the hash
		// codes.
		Set<XMLNamedObject<?>> otherMarkup = parser.getMarkupDeclarations();
		if (markupDeclarations != null && otherMarkup != null) {
			cmp = Utils.compare(markupDeclarations.size(), otherMarkup.size());
			if (cmp != 0) {
				return cmp;
			}
			if (markupDeclarations.equals(otherMarkup)) {
				return 0;
			} else {
				return Utils.compare(
					markupDeclarations.hashCode(),
					otherMarkup.hashCode()
				);
			}
		} else if (markupDeclarations != null) {
			return 1;
		} else if (otherMarkup != null) {
			return -1;
		} else {
			return 0;
		}
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The {@link Object} to compare against.
		@return		True if this equals o, false otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof Parser)) {
			return false;
		}

		return compareTo(Parser.class.cast(o)) == 0;
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(baseDirectory, markupDeclarations);
	}

	/** {@inheritDoc} */
	public Parser getCopy()
	throws CopyException
	{
		return new Parser();
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		return "The input type is a W3C XML schema document.";
	}
}
