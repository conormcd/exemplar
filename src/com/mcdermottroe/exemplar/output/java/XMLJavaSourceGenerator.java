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
package com.mcdermottroe.exemplar.output.java;

import java.io.File;
import java.util.Map;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLEntity;
import com.mcdermottroe.exemplar.model.XMLExternalIdentifier;
import com.mcdermottroe.exemplar.model.XMLMarkupDeclaration;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** A class which generates Java parsers that implement the SAX1 and SAX2
	Parser interfaces, depending on the properties files that are loaded.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class XMLJavaSourceGenerator
extends XMLParserSourceGenerator
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
		if (doctype == null) {
			return;
		}

		// Resolve the targetDirectory parameter into an absolute path that
		// exists.
		File sourceDirectory = getSourceDirectory(targetDirectory);
		DBC.ASSERT(sourceDirectory != null);

		// Get the vocabulary name
		String vocabulary = Options.getString("vocabulary");
		DBC.ASSERT(vocabulary != null);

		// The Files to write to.
		File buildFile = new File(
			sourceDirectory,
			Constants.Output.Java.BUILD_FILE
		);
		File classFile = new File(
			sourceDirectory,
			Utils.formatMessage(
				Constants.Output.Java.PARSER_FILE_FMT,
				vocabulary
			)
		);
		File parseFile = new File(
			sourceDirectory,
			Utils.formatMessage(
				Constants.Output.Java.JFLEX_FILE_FMT,
				vocabulary
			)
		);
		File entitiesFile = new File(
			sourceDirectory,
			Constants.Output.Java.ENTITIES_FILE
		);

		// Generate the four files.
		generateParserJavaFile(vocabulary, classFile);
		generateParserJFlexFile(
			vocabulary,
			doctype.hasAttlists(),
			doctype.entities(),
			parseFile
		);
		generateBuildFile(vocabulary, buildFile);
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
		if (vocabulary == null || outputFile == null) {
			return;
		}

		// Get the template
		String messageFormatTemplate = loadCodeFragment("JAVA_MAIN_TEMPLATE");
		DBC.ASSERT(messageFormatTemplate != null);

		// Figure out the package
		String packageStatement = "";
		String pkg = Options.getString("output-package");
		if (pkg != null) {
			packageStatement = "package " + pkg + ";";
		}

		// Make the contents of the output file
		String outputFileContents = Utils.formatMessage(
			messageFormatTemplate,
			Constants.PROGRAM_NAME,
			timestamp,
			vocabulary,
			Integer.toString(Constants.Output.Java.BUFFER_SIZE),
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
		Map<String, XMLMarkupDeclaration> entities,
		File outputFile
	)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(vocabulary != null);
		DBC.REQUIRE(outputFile != null);
		DBC.REQUIRE(codeFragments != null);
		if	(
				vocabulary == null ||
				outputFile == null ||
				codeFragments == null
			)
		{
			return;
		}

		// Get the template strings
		String messageFormatTemplate = loadCodeFragment("JFLEX_MAIN_TEMPLATE");
		String processingInstructionProcessor = loadCodeFragmentUnlessSet(
			"PROCESSING_INSTRUCTION_PROCESSOR",
			"exclude",
			"PI",
			Constants.EOL
		);
		String commentProcessor = loadCodeFragmentUnlessSet(
			"COMMENT_PROCESSOR",
			"exclude",
			"Comment"
		);
		String doctypeDeclProcessor = loadCodeFragmentUnlessSet(
			"DOCTYPEDECL_PROCESSOR",
			"exclude",
			"doctypedecl"
		);
		String cdSectProcessor = loadCodeFragmentUnlessSet(
			"CDSECT_PROCESSOR",
			"exclude",
			"CDSect"
		);
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
						XMLEntity entity = (XMLEntity)entities.get(entityName);
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
									Constants.NULL_STRING,
									extID.systemID(),
								};
								if (extID.publicID() != null) {
									args[1] = extID.publicID();
								}
								extEntities.append(
									Utils.formatMessage(
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
							case UNINITIALISED:
							default:
								// We shouldn't get here ever.
								DBC.UNREACHABLE_CODE();
								break;
						}
					}
					externalEntityRules = new String(extEntities);
				}
			}
		}
		String charRefResolver = loadCodeFragmentUnlessSet(
			"CHAR_REF_RESOLVER",
			"exclude",
			"CharRef"
		);

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
			packageStatement = "package " + pkg + ";";
		}

		// Make the contents of the output file
		String outputFileContents = Utils.formatMessage(
			messageFormatTemplate,
			Constants.PROGRAM_NAME,
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


	/** Generate the build file which will build the output code.

		@param	vocabulary					The name of the XML vocabulary that
											the parser will parse.
		@param	outputFile					The {@link File} to write out to
		@throws XMLParserGeneratorException	if the output file could not be
											written to.
	*/
	private void generateBuildFile(String vocabulary, File outputFile)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(vocabulary != null);
		DBC.REQUIRE(outputFile != null);
		if (vocabulary == null || outputFile == null) {
			return;
		}

		// Get the template
		String messageFormatTemplate = loadCodeFragment("BUILD_MAIN_TEMPLATE");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Utils.formatMessage(
			messageFormatTemplate,
			Constants.PROGRAM_NAME,
			timestamp,
			vocabulary
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

	/** Generate the resource file containing all of the entities. If entities
		are not included, then no file is created.

		@param	entities					The entities declared in the DTD.
		@param	outputFile					The {@link File} to output to.
		@throws	XMLParserGeneratorException	if the code fragments could not be
											loaded or if the output file could
											not be opened.
	*/
	private void generateEntitiesFile(
		Map<String, XMLMarkupDeclaration> entities,
		File outputFile
	)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(entities != null);
		DBC.REQUIRE(outputFile != null);
		if (entities == null || outputFile == null) {
			return;
		}

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
				XMLEntity entity = (XMLEntity)entities.get(entityName);
				if (entity.type().equals(XMLEntity.EntityType.INTERNAL)) {
					entityProperties.append(entityName);
					entityProperties.append(Constants.Character.SPACE);
					entityProperties.append(Constants.Character.EQUALS);
					entityProperties.append(Constants.Character.SPACE);
					entityProperties.append(
						Utils.xmlStringToJavaCanonicalForm(
							entity.value()
						)
					);
					entityProperties.append(Constants.EOL);
				}
			}

			// Make the contents of the output file
			String outputFileContents = Utils.formatMessage(
				messageFormatTemplate,
				Constants.PROGRAM_NAME,
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
		return "The Java language";
	}
}
