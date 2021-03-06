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
package com.mcdermottroe.exemplar.output.java;

import java.io.File;
import java.text.ParseException;
import java.util.Map;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLEntityType;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;
import com.mcdermottroe.exemplar.utils.XML;

import static com.mcdermottroe.exemplar.Constants.Character.EQUALS;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.Format.Code.Java.PACKAGE;
import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JAVA_PARSER;
import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JFLEX;
import static com.mcdermottroe.exemplar.Constants.NULL_STRING;
import static com.mcdermottroe.exemplar.Constants.Output.Java.BUFFER_SIZE;
import static com.mcdermottroe.exemplar.Constants.Output.Java.ENTITIES_FILE;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_NAME;

/** A class which generates Java parsers that implement the SAX1 and SAX2
	Parser interfaces, depending on the properties files that are loaded.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of {@link XMLJavaSourceGenerator}.
*/
public abstract
class XMLJavaSourceGenerator<T extends XMLParserSourceGenerator<T>>
extends XMLParserSourceGenerator<T>
{
	/** Creates a source generator which produces Java parsers. Protected as
		this is an abstract class.

		@throws	XMLParserGeneratorException	if the super-class constructor
											throws one.
	*/
	protected XMLJavaSourceGenerator()
	throws XMLParserGeneratorException
	{
		// The parent does all the work.
		super();
	}

	/** Copy constructor, see {@link
		XMLParserSourceGenerator#XMLParserSourceGenerator(Map, String)} for
		details.

		@param	code	The code fragments.
		@param	time	The timestamp.
	*/
	protected XMLJavaSourceGenerator(Map<String, String> code, String time) {
		super(code, time);
	}

	/** Generates the Java source for a SAX parser and places the source in the
		given directory.

		@param	doctype						The description of the vocabulary of
											XML to generate a parser for.
		@param	targetDirectory				The directory in which to place the
											source.
		@throws	XMLParserGeneratorException if any of the generation methods
											throw one.
	*/
	@Override
	public void generateParser(XMLDocumentType doctype, File targetDirectory)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(doctype != null);
		DBC.REQUIRE(targetDirectory != null);
		assert doctype != null;

		// Resolve the targetDirectory parameter into an absolute path that
		// exists.
		File sourceDirectory = getSourceDirectory(targetDirectory);
		DBC.ASSERT(sourceDirectory != null);

		// Get the vocabulary name
		String vocabulary = Options.getString("vocabulary");
		DBC.ASSERT(vocabulary != null);

		// The Files to write to.
		File classFile = new File(
			sourceDirectory,
			String.format(JAVA_PARSER, vocabulary)
		);
		File parseFile = new File(
			sourceDirectory,
			String.format(JFLEX, vocabulary)
		);
		File entitiesFile = new File(sourceDirectory, ENTITIES_FILE);

		// Generate the four files.
		generateParserJavaFile(vocabulary, classFile);
		generateParserJFlexFile(
			vocabulary,
			doctype.hasAttlists(),
			doctype.entities(),
			parseFile
		);
		generateEntitiesFile(doctype.entities(), entitiesFile);
	}

	/** Write the ${VOCABULARY}Parser.java file.

		@param	vocabulary					The name of the XML vocabulary that
											the parser will parse.
		@param	outputFile					The {@link File} to write out to
		@throws	XMLParserGeneratorException	if the code fragments could not be
											loaded or if the file could not be
											written to.
	*/
	private void generateParserJavaFile(String vocabulary, File outputFile)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(vocabulary != null);
		DBC.REQUIRE(outputFile != null);
		assert vocabulary != null;
		assert outputFile != null;

		// Get the template
		String messageFormatTemplate = loadCodeFragment("JAVA_MAIN_TEMPLATE");
		DBC.ASSERT(messageFormatTemplate != null);

		// Figure out the package
		String packageStatement = "";
		String pkg = Options.getString("output-package");
		if (pkg != null) {
			packageStatement = String.format(PACKAGE, pkg);
		}

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			vocabulary,
			Integer.toString(BUFFER_SIZE),
			packageStatement
		);

		// Write out the file
		try {
			OutputUtils.writeStringToFile(outputFileContents, outputFile);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					outputFile.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Write the ${VOCABULARY}.jflex file.

		@param	vocabulary					The name of the XML vocabulary that
											the parser will parse.
		@param	usesAttlists				Whether or not the {@link
											XMLDocumentType} declares any
											attribute lists.
		@param	entities					The entities that are defined in the
											{@link XMLDocumentType}.
		@param	outputFile					The {@link File} to write out to.
		@throws	XMLParserGeneratorException	if the code fragments could not be
											loaded, if an unknown entity type is
											encountered or if the output file
											could not be written to.
	*/
	private void generateParserJFlexFile(
		String vocabulary,
		boolean usesAttlists,
		Map<String, XMLEntity> entities,
		File outputFile
	)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(vocabulary != null);
		DBC.REQUIRE(outputFile != null);
		DBC.REQUIRE(codeFragments != null);

		// Get the template strings
		String messageFormatTemplate = loadCodeFragment("JFLEX_MAIN_TEMPLATE");
		String processingInstructionProcessor = EOL;
		if (!Options.isSet("exclude", "PI")) {
			processingInstructionProcessor = loadCodeFragment(
				"PROCESSING_INSTRUCTION_PROCESSOR"
			);
		}
		String commentProcessor = "";
		if (!Options.isSet("exclude", "Comment")) {
			commentProcessor = loadCodeFragment("COMMENT_PROCESSOR");
		}
		String doctypeDeclProcessor = "";
		if (!Options.isSet("exclude", "doctypedecl")) {
			doctypeDeclProcessor = loadCodeFragment("DOCTYPEDECL_PROCESSOR");
		}
		String cdSectProcessor = "";
		if (!Options.isSet("exclude", "CDSect")) {
			cdSectProcessor = loadCodeFragment("CDSECT_PROCESSOR");
		}
		String predefinedEntities = "";
		String externalEntityRules = "";
		String intEntResolver = "";
		if (!Options.isSet("exclude", "References")) {
			predefinedEntities = loadCodeFragment("PREDEFINED_ENTITIES");
			intEntResolver = loadCodeFragment("INTERNAL_ENT_RESOLVER");
			if (Options.isSet("include", "entities")) {
				if (entities != null) {
					StringBuilder extEntities = new StringBuilder();
					for (String entityName : entities.keySet()) {
						XMLEntity entity = entities.get(entityName);
						switch (entity.type()) {
							case INTERNAL:
								// Do nothing, internal entities
								// are handled elsewhere.
								break;
							case EXTERNAL_PARSED:
								// Get the external identifer (publicID and
								// systemID) for this entity.
								XMLExternalIdentifier extID;
								extID = entity.externalID();

								String extEntPropMessageFormat =
									"EXT_ENT_PROP_TEXT_FMT";
								Object[] args = {
									entityName,
									NULL_STRING,
									extID.systemID(),
								};
								if (extID.publicID() != null) {
									args[1] = extID.publicID();
								}
								extEntities.append(
									Strings.formatMessage(
										loadCodeFragment(
											extEntPropMessageFormat
										),
										args
									)
								);
								break;
							case EXTERNAL_UNPARSED:
								// These are not handled by SAX, and virtually
								// nobody uses them either.
								break;
						}
					}
					externalEntityRules = new String(extEntities);
				}
			}
		}
		String charRefResolver = "";
		if (!Options.isSet("exclude", "CharRef")) {
			charRefResolver = loadCodeFragment("CHAR_REF_RESOLVER");
		}

		// Element rules
		String emptyElementRule;
		String startElementRule;
		if (usesAttlists) {
			startElementRule = loadCodeFragment("START_TAG_ATTLIST");
			emptyElementRule = loadCodeFragment("EMPTY_TAG_ATTLIST");
		} else {
			startElementRule = loadCodeFragment("START_TAG_NO_ATTLIST");
			emptyElementRule = loadCodeFragment("EMPTY_TAG_NO_ATTLIST");
		}

		// Figure out the package
		String packageStatement = "";
		String pkg = Options.getString("output-package");
		if (pkg != null) {
			packageStatement = String.format(PACKAGE, pkg);
		}

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			vocabulary,
			processingInstructionProcessor,
			commentProcessor,
			doctypeDeclProcessor,
			cdSectProcessor,
			emptyElementRule,
			startElementRule,
			predefinedEntities,
			externalEntityRules,
			charRefResolver,
			intEntResolver,
			packageStatement
		);
		DBC.ASSERT(outputFileContents != null);

		// Write out the file
		try {
			OutputUtils.writeStringToFile(outputFileContents, outputFile);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					outputFile.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Generate the resource file containing all of the entities. If entities
		are not included, then no file is created.

		@param	entities					The entities declared in the DTD.
		@param	outputFile					The {@link File} to output to.
		@throws	XMLParserGeneratorException	if the code fragments could not be
											loaded or if the output file could
											not be opened.
	*/
	private void generateEntitiesFile(
		Map<String, XMLEntity> entities,
		File outputFile
	)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(entities != null);
		DBC.REQUIRE(outputFile != null);
		assert entities != null;
		assert outputFile != null;

		if (Options.isSet("include", "entities")) {
			// Get the template
			String messageFormatTemplate = loadCodeFragment(
				"ENTITIES_MAIN_TEMPLATE"
			);
			DBC.ASSERT(messageFormatTemplate != null);

			// Go through all of the entities and insert all of the internal
			// entities into the properties file string in sorted order.
			StringBuilder entityProperties = new StringBuilder();
			for (String entityName : entities.keySet()) {
				XMLEntity entity = entities.get(entityName);
				if (entity.type().equals(XMLEntityType.INTERNAL)) {
					entityProperties.append(entityName);
					entityProperties.append(SPACE);
					entityProperties.append(EQUALS);
					entityProperties.append(SPACE);
					try {
						entityProperties.append(
							Strings.toCanonicalForm(
								XML.resolveCharacterReferences(
									entity.value()
								)
							)
						);
					} catch (ParseException e) {
						throw new XMLParserGeneratorException(e);
					}
					entityProperties.append(EOL);
				}
			}

			// Make the contents of the output file
			String outputFileContents = Strings.formatMessage(
				messageFormatTemplate,
				PROGRAM_NAME,
				timestamp,
				entityProperties
			);

			// Write out the file.
			try {
				OutputUtils.writeStringToFile(outputFileContents, outputFile);
			} catch (OutputException e) {
				throw new XMLParserGeneratorException(
					Message.FILE_WRITE_FAILED(
						outputFile.getAbsolutePath()
					),
					e
				);
			}
		}
	}

	/** {@inheritDoc} */
	@Override public String describeLanguage() {
		return Message.LANGUAGE_JAVA();
	}
}
