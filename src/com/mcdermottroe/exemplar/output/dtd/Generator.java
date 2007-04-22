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
package com.mcdermottroe.exemplar.output.dtd;

import java.io.File;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLAlternative;
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeList;
import com.mcdermottroe.exemplar.model.XMLContent;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLMarkupDeclaration;
import com.mcdermottroe.exemplar.model.XMLMixedContent;
import com.mcdermottroe.exemplar.model.XMLNamedObject;
import com.mcdermottroe.exemplar.model.XMLNotation;
import com.mcdermottroe.exemplar.model.XMLObject;
import com.mcdermottroe.exemplar.model.XMLSequence;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;
import com.mcdermottroe.exemplar.utils.XML;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.DOUBLE_QUOTE;
import static com.mcdermottroe.exemplar.Constants.Character.HASH;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.PIPE;
import static com.mcdermottroe.exemplar.Constants.Character.PLUS;
import static com.mcdermottroe.exemplar.Constants.Character.QUESTION_MARK;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.Character.STAR;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.Output.DTD.FILE_FMT;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_NAME;
import static com.mcdermottroe.exemplar.Constants.XMLExternalIdentifier.NDATA;
import static com.mcdermottroe.exemplar.Constants.XMLExternalIdentifier.PUBLIC;
import static com.mcdermottroe.exemplar.Constants.XMLExternalIdentifier.SYSTEM;

/** A class which generates a DTD for this XML vocabulary.

	@author	Conor McDermottroe
	@since	0.1
*/
public class Generator
extends XMLParserSourceGenerator<Generator>
{
	/** Creates a source generator that produces DTDs.

		@throws XMLParserGeneratorException if the super-class constructor
											throws one.
	*/
	public Generator()
	throws XMLParserGeneratorException
	{
		// The parent does all the work.
		super();
	}

	/** Generates the DTD and places it in the given file.

		@param	doctype						The description of the vocabulary
											of XML to generate a parser for.
		@param	targetDirectory				The directory in which to place the
											source.
		@throws XMLParserGeneratorException	if the code fragments could not be
											loaded or if the output file could
											not be written to.
	*/
	@Override
	public void generateParser(XMLDocumentType doctype, File targetDirectory)
	throws XMLParserGeneratorException
	{
		// Resolve the targetDirectory parameter into
		// an absolute path that exists.
		File sourceDirectory = getSourceDirectory(targetDirectory);
		DBC.ASSERT(sourceDirectory != null);

		// Now create an output file in the given directory
		String vocabulary = Options.getString("vocabulary");
		DBC.ASSERT(vocabulary != null);
		File outputFile = new File(
			sourceDirectory,
			Strings.formatMessage(
				FILE_FMT,
				vocabulary
			)
		);

		// Load the code templates
		String dtdFile = loadCodeFragment("dtdFile");
		String elementDecl = loadCodeFragment("elementDecl");
		String attlistDecl = loadCodeFragment("attlistDecl");
		String entityDecl = loadCodeFragment("entityDecl");
		String notationDecl = loadCodeFragment("notationDecl");

		// Create all of the DTD element and attribute declarations
		StringBuilder elementDecls = new StringBuilder();
		Map<String, XMLMarkupDeclaration> elements;
		elements = new TreeMap<String, XMLMarkupDeclaration>(
			doctype.elements()
		);
		for (String elementName : elements.keySet()) {
			XMLElement element = (XMLElement)elements.get(elementName);

			// The content specification for the element
			StringBuilder contentSpec = new StringBuilder();
			switch (element.getContentType()) {
				case EMPTY:
					contentSpec.append("EMPTY");
					break;
				case ANY:
					contentSpec.append("ANY");
					break;
				case MIXED:
				case CHILDREN:
					String contentSpecText = objectTreeToContentSpec(
						element.getContentSpec()
					);
					contentSpec.append(contentSpecText);
					break;
				default:
					DBC.UNREACHABLE_CODE();
			}

			// Output the element declaration
			Log.debug("Creating element declaration for " + elementName);
			elementDecls.append(
				Strings.formatMessage(
					elementDecl,
					elementName,
					contentSpec.toString()
				)
			);

			// Output the attribute list for
			// the element, if any
			XMLAttributeList attlist = element.getAttlist();
			if (attlist != null) {
				// Construct the list of attributes declared
				// in this attlist.
				StringBuilder declTail = new StringBuilder();
				for (XMLAttribute att : attlist) {
					// The attribute name
					declTail.append(EOL);
					declTail.append(Strings.indent(att.getName(), 2));
					declTail.append(SPACE);

					// The attribute type
					XMLAttribute.ContentType attType = att.getType();
					if	(
							attType.equals(XMLAttribute.ContentType.NOTATION) ||
							attType.equals(XMLAttribute.ContentType.ENUMERATION)
						)
					{
						if (attType.equals(XMLAttribute.ContentType.NOTATION)) {
							declTail.append(XMLAttribute.ContentType.NOTATION);
							declTail.append(SPACE);
						}

						declTail.append(LEFT_PAREN);
						declTail.append(Strings.join(PIPE, att.getValues()));
						declTail.append(RIGHT_PAREN);
						declTail.append(SPACE);
					} else {
						declTail.append(attType);
						declTail.append(SPACE);
					}

					// The DefaultDecl portion of the AttDef
					XMLAttribute.DefaultType defaultDeclType;
					defaultDeclType = att.getDefaultDeclType();
					switch (defaultDeclType) {
						case FIXED:
							declTail.append(HASH);
							declTail.append(XMLAttribute.DefaultType.FIXED);
							declTail.append(SPACE);
							declTail.append(DOUBLE_QUOTE);
							declTail.append(att.getDefaultValue());
							declTail.append(DOUBLE_QUOTE);
							break;
						case ATTVALUE:
							declTail.append(DOUBLE_QUOTE);
							declTail.append(att.getDefaultValue());
							declTail.append(DOUBLE_QUOTE);
							break;
						case IMPLIED:
						case REQUIRED:
							declTail.append(HASH);
							declTail.append(defaultDeclType);
							break;
						case INVALID:
						default:
							DBC.IGNORED_ERROR();
					}
				}
				declTail.append(EOL);

				Log.debug(
							"Creating attribute list declaration for " + 
							elementName
				);
				elementDecls.append(
					Strings.formatMessage(
						attlistDecl,
						elementName,
						declTail.toString()
					)
				);
			}
		}

		// Create the entity declarations
		StringBuilder entityDecls = new StringBuilder();
		Map<String, XMLMarkupDeclaration> entities;
		entities = new TreeMap<String, XMLMarkupDeclaration>(
			doctype.entities()
		);
		for (String entityName : entities.keySet()) {
			XMLEntity entity = (XMLEntity)entities.get(entityName);

			StringBuilder entityDeclTail = new StringBuilder();
			if (entity.isInternal()) {
				entityDeclTail.append(DOUBLE_QUOTE);
				try {
					entityDeclTail.append(
						XML.toCharacterReferences(entity.value())
					);
				} catch (ParseException e) {
					throw new XMLParserGeneratorException(e);
				}
				entityDeclTail.append(DOUBLE_QUOTE);
			} else {
				XMLExternalIdentifier extID = entity.externalID();
				if (extID.publicID() != null) {
					entityDeclTail.append(PUBLIC);
					entityDeclTail.append(extID.publicID());
					entityDeclTail.append(SPACE);
					entityDeclTail.append(extID.systemID());
				} else {
					entityDeclTail.append(SYSTEM);
					entityDeclTail.append(extID.systemID());
				}

				switch (entity.type()) {
					case INTERNAL:
					case EXTERNAL_PARSED:
						break;
					case EXTERNAL_UNPARSED:
						entityDeclTail.append(SPACE);
						entityDeclTail.append(NDATA);
						entityDeclTail.append(SPACE);
						entityDeclTail.append(entity.getNotation());
						break;
					case UNINITIALISED:
					default:
						DBC.UNREACHABLE_CODE();
						break;
				}
			}

			Log.debug("Creating entity declaration for " + entityName);
			entityDecls.append(
				Strings.formatMessage(
					entityDecl,
					entityName,
					entityDeclTail.toString()
				)
			);
		}

		// Create the notation declarations
		StringBuilder notationDecls = new StringBuilder();
		Map<String, XMLMarkupDeclaration> notations;
		notations = new TreeMap<String, XMLMarkupDeclaration>(
			doctype.notations()
		);
		for (String notationName : notations.keySet()) {
			XMLNotation notation = (XMLNotation)notations.get(notationName);
			XMLExternalIdentifier extID = notation.getExtID();

			String pubOrSys = "";
			StringBuilder notationDeclTail = new StringBuilder();
			notationDeclTail.append(DOUBLE_QUOTE);
			if (extID.publicID() != null && extID.systemID() != null) {
				pubOrSys = PUBLIC;
				notationDeclTail.append(extID.publicID());
				notationDeclTail.append("\" \"");
				notationDeclTail.append(extID.systemID());
			} else if (extID.publicID() != null) {
				pubOrSys = PUBLIC;
				notationDeclTail.append(extID.publicID());
			} else if (extID.systemID() != null) {
				pubOrSys = SYSTEM;
				notationDeclTail.append(extID.systemID());
			} else {
				DBC.UNREACHABLE_CODE();
			}
			notationDeclTail.append(DOUBLE_QUOTE);

			Log.debug("Creating notation declaration for " + notationName);
			notationDecls.append(
				Strings.formatMessage(
					notationDecl,
					notationName,
					pubOrSys,
					notationDeclTail
				)
			);
		}

		// Try to write out the code
		try {
			Log.debug("Writing built DTD to " + outputFile);
			OutputUtils.writeStringToFile(
				Strings.formatMessage(
					dtdFile,
					vocabulary,
					PROGRAM_NAME,
					timestamp,
					elementDecls.toString(),
					entityDecls.toString(),
					notationDecls.toString()
				),
				outputFile
			);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					outputFile.getAbsolutePath()
				),
				e
			);
		}
	}

	/** {@inheritDoc} */
	@Override public String describeLanguage() {
		return Message.LANGUAGE_DTD();
	}

	/** {@inheritDoc} */
	@Override public String describeAPI() {
		DBC.UNREACHABLE_CODE();
		return null;
	}

	/** Convert either an {@link com.mcdermottroe.exemplar.model.XMLSequence}
		or {@link XMLAlternative} to a contentspec portion of an element
		declaration.

		@param o	An {@link XMLObject} or {@link String} which is part of a 
					contentspec declaration. If the object is a {@link String}
					it must be an element name, otherwise it should be an
					{@link XMLAlternative} or an {@link
					com.mcdermottroe.exemplar.model.XMLSequence}.
		@return		A {@link String} representation of the contentspec.
	*/
	private static String objectTreeToContentSpec(Object o) {
		StringBuilder ret = new StringBuilder();

		if (o instanceof XMLSequence) {
			XMLSequence seq = (XMLSequence)o;

			// Create the sequence
			List<String> contentSpecs = new ArrayList<String>();
			for (XMLObject<?> xmlObject : seq) {
				contentSpecs.add(objectTreeToContentSpec(xmlObject));
			}
			String sep = String.valueOf(COMMA) + SPACE;
			ret.append(LEFT_PAREN);
			ret.append(Strings.join(sep, contentSpecs));
			ret.append(RIGHT_PAREN);

			// Now put the ?, + or * on where appropriate
			int min = seq.getMinOccurs();
			DBC.ASSERT(min >= 0);
			int max = seq.getMaxOccurs();
			DBC.ASSERT(max >= 0);
			if (min == 0) {
				if (max == 1) {
					ret.append(QUESTION_MARK);
				} else {
					ret.append(STAR);
				}
			} else {
				if (max > 1) {
					ret.append(PLUS);
				}
			}
		} else if (o instanceof XMLAlternative) {
			XMLAlternative alt = (XMLAlternative)o;

			// Create the alternative list
			List<String> contentSpecs = new ArrayList<String>();
			for (XMLObject<?> xmlObject : alt) {
				contentSpecs.add(objectTreeToContentSpec(xmlObject));
			}
			String sep = String.valueOf(SPACE) + PIPE + SPACE;
			ret.append(LEFT_PAREN);
			ret.append(Strings.join(sep, contentSpecs));
			ret.append(RIGHT_PAREN);
		} else if (o instanceof XMLMixedContent) {
			XMLMixedContent mixed = (XMLMixedContent)o;

			// Create the alternative list
			Iterator<XMLObject<?>> it = mixed.iterator();
			ret.append(LEFT_PAREN);
			XMLObject<?> first = it.next();
			ret.append(objectTreeToContentSpec(first));
			if (it.hasNext()) {
				while (it.hasNext()) {
					ret.append(SPACE);
					ret.append(PIPE);
					ret.append(SPACE);
					ret.append(objectTreeToContentSpec(it.next()));
				}
				ret.append(RIGHT_PAREN);
				ret.append(STAR);
			} else {
				ret.append(RIGHT_PAREN);
			}
		} else if (o instanceof XMLContent) {
			ret.append("#PCDATA");
		} else if (o instanceof XMLNamedObject) {
			ret.append(((XMLNamedObject)o).getName());
		} else {
			DBC.UNREACHABLE_CODE();
		}

		return ret.toString();
	}

	/** {@inheritDoc} */
	public Generator getCopy()
	throws CopyException
	{
		Generator copy;
		try {
			copy = new Generator();
		} catch (XMLParserGeneratorException e) {
			throw new CopyException(e);
		}
		copy.codeFragments = codeFragments;
		copy.timestamp = timestamp;
		return copy;
	}
}
