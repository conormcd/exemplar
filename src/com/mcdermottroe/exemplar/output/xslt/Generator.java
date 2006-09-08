// vim:filetype=java:ts=4
/*
	Copyright (c) 2004, 2005, 2006
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
package com.mcdermottroe.exemplar.output.xslt;

import java.io.File;
import java.util.Iterator;
import java.util.Map;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeList;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** A class which generates skeleton XSLT for this XML vocabulary.

	@author	Conor McDermottroe
	@since	0.1
*/
public class Generator extends XMLParserSourceGenerator {
	/** Override the default constructor to add throws clause.

		@throws XMLParserGeneratorException	if the superclass constructor throws
											it.
	*/
	public Generator()
	throws XMLParserGeneratorException
	{
		// The parent does all the work.
		super();
	}

	/** Generates the XSLT and places it in the given file.

		@param	doctype						The description of the vocabulary
											of XML to generate a parser for.
		@param	targetDirectory				The directory in which to place the
											generated source.
		@throws	XMLParserGeneratorException if the code fragments cannot be
											loaded from the backing store, or
											if the output file cannot be
											written to.
	*/
	public void generateParser(XMLDocumentType doctype, File targetDirectory)
	throws XMLParserGeneratorException
	{
		// Resolve the targetDirectory parameter into an absolute path that
		// exists.
		File sourceDirectory = getSourceDirectory(targetDirectory);
		DBC.ASSERT(sourceDirectory != null);

		// Now create an output file in the given directory
		String vocabulary = Options.getString("vocabulary");
		DBC.ASSERT(vocabulary != null);
		File outputFile = new File(
			sourceDirectory,
			Utils.formatMessage(
				Constants.Output.XSLT.FILE_FMT,
				vocabulary
			)
		);

		// Get the attribute lists
		Map attlists = doctype.attlists();

		// Load all the code fragments.
		String stylesheet = loadCodeFragment("stylesheet");
		String attributeTemplate = loadCodeFragment("attributeTemplate");
		String elementTemplate = loadCodeFragment("elementTemplate");

		// Run through the elements and create rules for each of them,
		// processing attributes as necessary
		StringBuffer body = new StringBuffer();
		StringBuffer attributeMatchers = new StringBuffer();
		for	(
				Iterator it = doctype.elements().keySet().iterator();
				it.hasNext();
			)
		{
			String elementName = (String)it.next();
			DBC.ASSERT(elementName != null);

			// Make the attribute matchers if they're called for.
			attributeMatchers.delete(0, attributeMatchers.length());
			if (attlists != null) {
				XMLAttributeList attlist;
				attlist = (XMLAttributeList)attlists.get(elementName);
				if (attlist != null) {
					// There are attributes for this element
					for (
							Iterator atts = attlist.attributes();
							atts.hasNext();
						)
					{
						String attName = ((XMLAttribute)atts.next()).getName();
						DBC.ASSERT(attName != null);
						Object[] args = {
							attName,
						};
						attributeMatchers.append(
							Utils.formatMessage(
								attributeTemplate,
								args
							)
						);
					}
				}
			}

			// Make the element matcher
			Object[] args = {
				elementName,
				attributeMatchers.toString(),
			};
			body.append(Utils.formatMessage(elementTemplate, args));
		}

		Object[] args =	{
							Constants.PROGRAM_NAME,
							timestamp,
							"",
							body.toString(),
						};
		body.delete(0, body.length());
		body.append(Utils.formatMessage(stylesheet, args));

		// Try to write out the code
		try {
			OutputUtils.writeStringToFile(new String(body), outputFile);
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
		return "The XSL-T language";
	}

	/** {@inheritDoc} */
	public String describeAPI() {
		DBC.UNREACHABLE_CODE();
		return null;
	}
}
