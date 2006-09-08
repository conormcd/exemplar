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
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.model.XMLAlternative;
import com.mcdermottroe.exemplar.model.XMLAlternativeOrSequence;
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeList;
import com.mcdermottroe.exemplar.model.XMLContent;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.model.XMLElementReference;
import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLNotation;
import com.mcdermottroe.exemplar.model.XMLObject;
import com.mcdermottroe.exemplar.model.XMLObjectException;
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
implements Constants.XML
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
		StringBuffer elementDecls = new StringBuffer();
		Map elements = new TreeMap(doctype.elements());
		for (Iterator els = elements.keySet().iterator(); els.hasNext(); ) {
			String elementName = (String)els.next();
			XMLElement element = (XMLElement)elements.get(elementName);

			// The content specification for the element
			StringBuffer contentSpec = new StringBuffer();
			if (element.getContentType() == Element.EMPTY) {
				contentSpec.append("EMPTY");
			} else if (element.getContentType() == Element.ANY) {
				contentSpec.append("ANY");
			} else if (element.getContentType() == Element.MIXED) {
				String contentSpecText = objectTreeToContentSpec(
					element.getContentSpec()
				);
				contentSpec.append(contentSpecText);
			} else if (element.getContentType() == Element.CHILDREN) {
				String contentSpecText = objectTreeToContentSpec(
					element.getContentSpec()
				);
				contentSpec.append(contentSpecText);
			} else {
				DBC.UNREACHABLE_CODE();
			}

			// Output the element declaration
			Object[] elArgs =	{
									elementName,
									contentSpec.toString(),
								};
			elementDecls.append(Utils.formatMessage(elementDecl, elArgs));

			// Output the attribute list for
			// the element, if any
			XMLAttributeList attlist = element.getAttlist();
			if (attlist != null) {
				// Construct the list of attributes declared
				// in this attlist.
				StringBuffer declTail = new StringBuffer();
				for (Iterator it = attlist.attributes(); it.hasNext(); ) {
					XMLAttribute att = (XMLAttribute)it.next();

					// The attribute name
					declTail.append(Constants.EOL);
					declTail.append(Constants.UI.INDENT);
					declTail.append(Constants.UI.INDENT);
					declTail.append(att.getName());
					declTail.append(Constants.Character.SPACE);

					// The attribute type
					String attType = att.getType();
					if	(
							attType.equals(Attribute.NOTATION) ||
							attType.equals(Attribute.ENUMERATION)
						)
					{
						if (attType.equals(Attribute.NOTATION)) {
							declTail.append(Attribute.NOTATION);
							declTail.append(Constants.Character.SPACE);
						}

						declTail.append(Constants.Character.LEFT_PAREN);
						List nValues = att.getValues();
						if (!nValues.isEmpty()) {
							Iterator iter = nValues.iterator();
							declTail.append((String)iter.next());
							while (iter.hasNext()) {
								declTail.append(Constants.Character.PIPE);
								declTail.append((String)iter.next());
							}
						}
						declTail.append(") ");
					} else {
						declTail.append(attType);
						declTail.append(Constants.Character.SPACE);
					}

					// The DefaultDecl portion of the AttDef
					String defaultDeclType = att.getDefaultDeclType();
					if (defaultDeclType.equals(Attribute.FIXED)) {
						declTail.append(Constants.Character.HASH);
						declTail.append(Attribute.FIXED);
						declTail.append(Constants.Character.SPACE);
						declTail.append(Constants.Character.DOUBLE_QUOTE);
						declTail.append(att.getDefaultValue());
						declTail.append(Constants.Character.DOUBLE_QUOTE);
					} else if (defaultDeclType.equals(Attribute.ATTVALUE)) {
						declTail.append(Constants.Character.DOUBLE_QUOTE);
						declTail.append(att.getDefaultValue());
						declTail.append(Constants.Character.DOUBLE_QUOTE);
					} else {
						declTail.append(Constants.Character.HASH);
						declTail.append(defaultDeclType);
					}
				}
				declTail.append(Constants.EOL);

				Object[] attArgs =	{
										elementName,
										declTail.toString(),
									};
				elementDecls.append(Utils.formatMessage(attlistDecl, attArgs));
			}
		}

		// Create the entity declarations
		StringBuffer entityDecls = new StringBuffer();
		Map entities = new TreeMap(doctype.entities());
		for (Iterator it = entities.keySet().iterator(); it.hasNext(); ) {
			String entityName = (String)it.next();
			XMLEntity entity = (XMLEntity)entities.get(entityName);

			StringBuffer entityDeclTail = new StringBuffer();
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
					entityDeclTail.append(ExternalIdentifier.PUBLIC);
					entityDeclTail.append(extID.publicID());
					entityDeclTail.append(Constants.Character.SPACE);
					entityDeclTail.append(extID.systemID());
				} else {
					entityDeclTail.append(ExternalIdentifier.SYSTEM);
					entityDeclTail.append(extID.systemID());
				}

				if (entity.type() == Entity.EXTERNAL_UNPARSED) {
					entityDeclTail.append(Constants.Character.SPACE);
					entityDeclTail.append(ExternalIdentifier.NDATA);
					entityDeclTail.append(Constants.Character.SPACE);
					entityDeclTail.append(entity.getNotation());
				}
			}

			Object[] enArgs =	{
									entityName,
									entityDeclTail.toString(),
								};
			entityDecls.append(Utils.formatMessage(entityDecl, enArgs));
		}

		// Create the notation declarations
		StringBuffer notationDecls = new StringBuffer();
		Map notations = new TreeMap(doctype.notations());
		for (Iterator it = notations.keySet().iterator(); it.hasNext(); ) {
			String notationName = (String)it.next();
			XMLNotation notation = (XMLNotation)notations.get(notationName);
			XMLExternalIdentifier extID = notation.getExtID();

			String pubOrSys = "";
			StringBuffer notationDeclTail = new StringBuffer();
			notationDeclTail.append(Constants.Character.DOUBLE_QUOTE);
			if (extID.publicID() != null && extID.systemID() != null) {
				pubOrSys = ExternalIdentifier.PUBLIC;
				notationDeclTail.append(extID.publicID());
				notationDeclTail.append("\" \"");
				notationDeclTail.append(extID.systemID());
			} else if (extID.publicID() != null) {
				pubOrSys = ExternalIdentifier.PUBLIC;
				notationDeclTail.append(extID.publicID());
			} else if (extID.systemID() != null) {
				pubOrSys = ExternalIdentifier.SYSTEM;
				notationDeclTail.append(extID.systemID());
			} else {
				DBC.UNREACHABLE_CODE();
			}
			notationDeclTail.append(Constants.Character.DOUBLE_QUOTE);

			Object[] notArgs =	{
									notationName,
									pubOrSys,
									notationDeclTail,
								};
			notationDecls.append(Utils.formatMessage(notationDecl, notArgs));
		}

		//
		Object[] args =	{
							vocabulary,
							Constants.PROGRAM_NAME,
							timestamp,
							elementDecls.toString(),
							entityDecls.toString(),
							notationDecls.toString(),
						};

		// Try to write out the code
		try {
			OutputUtils.writeStringToFile(
				Utils.formatMessage(
					dtdFile,
					args
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
	public String describeLanguage() {
		return "The XML DTD language";
	}

	/** {@inheritDoc} */
	public String describeAPI() {
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

		StringBuffer result = new StringBuffer();
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
		StringBuffer ret = new StringBuffer();

		if (o instanceof XMLAlternativeOrSequence) {
			XMLAlternativeOrSequence xo = (XMLAlternativeOrSequence)o;
			Iterator it = xo.iterator();
			DBC.ASSERT(it.hasNext());

			ret.append(Constants.Character.LEFT_PAREN);
			ret.append(objectTreeToContentSpec(it.next()));
			while (it.hasNext()) {
				if (o instanceof XMLAlternative) {
					ret.append(Constants.Character.PIPE);
				} else {
					ret.append(Constants.Character.COMMA);
				}
				ret.append(objectTreeToContentSpec(it.next()));
			}
			ret.append(Constants.Character.RIGHT_PAREN);

			int min = -1;
			int max = -1;
			try {
				min = xo.getMinOccurs();
				DBC.ASSERT(min >= 0);
				max = xo.getMaxOccurs();
				DBC.ASSERT(max > 0);
			} catch (XMLObjectException e) {
				// Should never get here
				// becuase these  objects
				// always have a max and min
				DBC.UNREACHABLE_CODE();
			}
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
		} else if (o instanceof XMLContent) {
			ret.append("#PCDATA");
		} else if (o instanceof XMLElementReference) {
			XMLObject xo = (XMLObject)o;
			try {
				ret.append(xo.getName());
				if (xo.getMinOccurs() == 0) {
					if (xo.getMaxOccurs() == 1) {
						ret.append(Constants.Character.QUESTION_MARK);
					} else {
						ret.append(Constants.Character.STAR);
					}
				} else {
					if (xo.getMinOccurs() > 1) {
						ret.append(Constants.Character.PLUS);
					}
				}
			} catch (XMLObjectException e) {
				DBC.UNREACHABLE_CODE();
			}
		} else {
			DBC.UNREACHABLE_CODE();
		}

		return ret.toString();
	}
}
