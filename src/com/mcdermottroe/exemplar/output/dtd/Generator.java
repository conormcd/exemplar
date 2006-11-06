// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
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
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** A class which generates a DTD for this XML vocabulary.

	@author	Conor McDermottroe
	@since	0.1
*/
public class Generator
extends XMLParserSourceGenerator
implements Constants.XMLExternalIdentifier
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
			Utils.formatMessage(
				Constants.Output.DTD.FILE_FMT,
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
			elementDecls.append(
				Utils.formatMessage(
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
					declTail.append(Constants.EOL);
					declTail.append(Constants.UI.INDENT);
					declTail.append(Constants.UI.INDENT);
					declTail.append(att.getName());
					declTail.append(Constants.Character.SPACE);

					// The attribute type
					XMLAttribute.ContentType attType = att.getType();
					if	(
							attType.equals(XMLAttribute.ContentType.NOTATION) ||
							attType.equals(XMLAttribute.ContentType.ENUMERATION)
						)
					{
						if (attType.equals(XMLAttribute.ContentType.NOTATION)) {
							declTail.append(XMLAttribute.ContentType.NOTATION);
							declTail.append(Constants.Character.SPACE);
						}

						declTail.append(Constants.Character.LEFT_PAREN);
						declTail.append(
							Utils.join(
								Constants.Character.PIPE,
								att.getValues()
							)
						);
						declTail.append(Constants.Character.RIGHT_PAREN);
						declTail.append(Constants.Character.SPACE);
					} else {
						declTail.append(attType);
						declTail.append(Constants.Character.SPACE);
					}

					// The DefaultDecl portion of the AttDef
					XMLAttribute.DefaultType defaultDeclType;
					defaultDeclType = att.getDefaultDeclType();
					switch (defaultDeclType) {
						case FIXED:
							declTail.append(Constants.Character.HASH);
							declTail.append(XMLAttribute.DefaultType.FIXED);
							declTail.append(Constants.Character.SPACE);
							declTail.append(Constants.Character.DOUBLE_QUOTE);
							declTail.append(att.getDefaultValue());
							declTail.append(Constants.Character.DOUBLE_QUOTE);
							break;
						case ATTVALUE:
							declTail.append(Constants.Character.DOUBLE_QUOTE);
							declTail.append(att.getDefaultValue());
							declTail.append(Constants.Character.DOUBLE_QUOTE);
							break;
						case IMPLIED:
						case REQUIRED:
							declTail.append(Constants.Character.HASH);
							declTail.append(defaultDeclType);
							break;
						case INVALID:
						default:
							DBC.IGNORED_ERROR();
					}
				}
				declTail.append(Constants.EOL);

				elementDecls.append(
					Utils.formatMessage(
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
				entityDeclTail.append(Constants.Character.DOUBLE_QUOTE);
				entityDeclTail.append(
					escapeCharactersToCharacterReferences(
						entity.value()
					)
				);
				entityDeclTail.append(Constants.Character.DOUBLE_QUOTE);
			} else {
				XMLExternalIdentifier extID = entity.externalID();
				if (extID.publicID() != null) {
					entityDeclTail.append(PUBLIC);
					entityDeclTail.append(extID.publicID());
					entityDeclTail.append(Constants.Character.SPACE);
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
						entityDeclTail.append(Constants.Character.SPACE);
						entityDeclTail.append(NDATA);
						entityDeclTail.append(Constants.Character.SPACE);
						entityDeclTail.append(entity.getNotation());
						break;
					case UNINITIALISED:
					default:
						DBC.UNREACHABLE_CODE();
						break;
				}
			}

			entityDecls.append(
				Utils.formatMessage(
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
			notationDeclTail.append(Constants.Character.DOUBLE_QUOTE);
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
			notationDeclTail.append(Constants.Character.DOUBLE_QUOTE);

			notationDecls.append(
				Utils.formatMessage(
					notationDecl,
					notationName,
					pubOrSys,
					notationDeclTail
				)
			);
		}

		// Try to write out the code
		try {
			OutputUtils.writeStringToFile(
				Utils.formatMessage(
					dtdFile,
					vocabulary,
					Constants.PROGRAM_NAME,
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
		return "The XML DTD language";
	}

	/** {@inheritDoc} */
	@Override public String describeAPI() {
		DBC.UNREACHABLE_CODE();
		return null;
	}

	/** Escape all characters in the given String with their XML character 
		references. Any character or entity references already in the {@link
		String} will be passed through.

		@param s	The String to escape the characters in.
		@return		A String containing nothing but character references.
	*/
	private static String escapeCharactersToCharacterReferences(String s) {
		DBC.REQUIRE(s != null);
		if (s == null) {
			return null;
		}

		StringBuilder result = new StringBuilder();
		char[] characters = s.toCharArray();
		for (int i = 0; i < characters.length; i++) {
			if	(
					characters[i] == Constants.Character.AMPERSAND &&
					i + 1 < characters.length
				)
			{

				int startIndex = i + 1;
				if (characters[startIndex] == Constants.Character.HASH) {
					startIndex++;
				}
				int semicolonIndex = -1;
				for (int j = startIndex; j < characters.length; j++) {
					boolean doBreak = false;
					switch (characters[j]) {
						case Constants.Character.SEMI_COLON:
							semicolonIndex = j;
							doBreak = true;
							break;
						case Constants.Character.FULL_STOP:
						case Constants.Character.MINUS:
						case Constants.Character.UNDERSCORE:
						case Constants.Character.COLON:
							// Do nothing
							break;
						default:
							if (!Character.isLetterOrDigit(characters[j])) {
								doBreak = true;
							}
					}
					if (doBreak) {
						break;
					}
				}

				if (semicolonIndex >= 0) {
					for (int j = i; j <= semicolonIndex; j++) {
						result.append(characters[j]);
					}
					i = semicolonIndex;
				} else {
					result.append(Constants.CHAR_REF_HEX_PREFIX);
					result.append(Integer.toHexString((int)characters[i]));
					result.append(Constants.Character.SEMI_COLON);
				}
			} else {
				result.append(Constants.CHAR_REF_HEX_PREFIX);
				result.append(Integer.toHexString((int)characters[i]));
				result.append(Constants.Character.SEMI_COLON);
			}
		}

		return result.toString();
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
			for (XMLObject xmlObject : seq) {
				contentSpecs.add(objectTreeToContentSpec(xmlObject));
			}
			String sep =	String.valueOf(Constants.Character.COMMA) +
							Constants.Character.SPACE;
			ret.append(Constants.Character.LEFT_PAREN);
			ret.append(Utils.join(sep, contentSpecs));
			ret.append(Constants.Character.RIGHT_PAREN);

			// Now put the ?, + or * on where appropriate
			int min = seq.getMinOccurs();
			DBC.ASSERT(min >= 0);
			int max = seq.getMaxOccurs();
			DBC.ASSERT(max >= 0);
			if (min == 0) {
				if (max == 1) {
					ret.append(Constants.Character.QUESTION_MARK);
				} else {
					ret.append(Constants.Character.STAR);
				}
			} else {
				if (max > 1) {
					ret.append(Constants.Character.PLUS);
				}
			}
		} else if (o instanceof XMLAlternative) {
			XMLAlternative alt = (XMLAlternative)o;

			// Create the alternative list
			List<String> contentSpecs = new ArrayList<String>();
			for (XMLObject xmlObject : alt) {
				contentSpecs.add(objectTreeToContentSpec(xmlObject));
			}
			String sep =	String.valueOf(Constants.Character.SPACE) +
							Constants.Character.PIPE +
							Constants.Character.SPACE;
			ret.append(Constants.Character.LEFT_PAREN);
			ret.append(Utils.join(sep, contentSpecs));
			ret.append(Constants.Character.RIGHT_PAREN);
		} else if (o instanceof XMLMixedContent) {
			XMLMixedContent mixed = (XMLMixedContent)o;

			// Create the alternative list
			Iterator<XMLObject> it = mixed.iterator();
			ret.append(Constants.Character.LEFT_PAREN);
			XMLObject first = it.next();
			DBC.ASSERT(first instanceof XMLContent);
			ret.append(objectTreeToContentSpec(first));
			if (it.hasNext()) {
				while (it.hasNext()) {
					ret.append(Constants.Character.SPACE);
					ret.append(Constants.Character.PIPE);
					ret.append(Constants.Character.SPACE);
					ret.append(objectTreeToContentSpec(it.next()));
				}
				ret.append(Constants.Character.RIGHT_PAREN);
				ret.append(Constants.Character.STAR);
			} else {
				ret.append(Constants.Character.RIGHT_PAREN);
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
}
