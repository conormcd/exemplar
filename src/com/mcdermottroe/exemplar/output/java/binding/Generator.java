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

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import com.mcdermottroe.exemplar.CopyException;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLAttribute;
import com.mcdermottroe.exemplar.model.XMLAttributeDefaultType;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.model.XMLElement;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.output.XMLParserGeneratorException;
import com.mcdermottroe.exemplar.output.XMLParserSourceGenerator;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.TAB;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.Format.Filenames.JAVA;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_NAME;

/** A class which generates data binding Java parsers.

	@author	Conor McDermottroe
	@since	0.2
*/
public class Generator
extends XMLParserSourceGenerator<Generator>
{
	/** The base package, if any, where the output code is going to live. */
	protected String basePackage;

	/** The converter for creating class names from element names. */
	protected ElementNameConverter<? extends ElementNameConverter<?>>
		elementNameGenerator;

	/** The converter for creating variable, getter and setter names from
		attribute names.
	*/
	protected AttributeNameConverter<? extends AttributeNameConverter<?>>
		attNameGenerator;

	/** Creates a source generator which produces data binding parsers in the
		Java language.

		@throws XMLParserGeneratorException	if the super-class constructor
											throws one.
	*/
	public Generator()
	throws XMLParserGeneratorException
	{
		super();
		basePackage = null;
		elementNameGenerator = new DefaultElementNameConverter();
		attNameGenerator = new DefaultAttributeNameConverter();
	}

	/** Copy constructor, see {@link
		XMLParserSourceGenerator#XMLParserSourceGenerator(Map, String)} for
		details.

		@param	code					The code fragments.
		@param	time					The timestamp.
		@param	pkg						The {@link #basePackage}.
		@param	elementNameConverter	The {@link #elementNameGenerator}.
		@param	attNameConverter		The {@link #attNameGenerator}.
	*/
	protected Generator(
		Map<String, String> code,
		String time,
		String pkg,
		ElementNameConverter<? extends ElementNameConverter<?>>
			elementNameConverter,
		AttributeNameConverter<? extends AttributeNameConverter<?>>
			attNameConverter
	)
	{
		super(code, time);
		basePackage = pkg;
		elementNameGenerator = elementNameConverter;
		attNameGenerator = attNameConverter;
	}

	/** {@inheritDoc} */
	@Override public void generateParser(
		XMLDocumentType doctype,
		File targetDirectory
	)
	throws XMLParserGeneratorException
	{
		// Set some of the common stuff
		basePackage = Options.getString("output-package");
		if (basePackage == null) {
			throw new XMLParserGeneratorException(
				Message.MANDATORY_OPTIONS_NOT_SET()
			);
		}

		// Get the elements
		Map<String, XMLElement> elements = doctype.elements();

		// Only create code if there are some elements
		if (elements != null && !elements.isEmpty()) {
			// Ensure that we can create unique class names for all of the
			// elements.
			Map<String, XMLElement> classNames =
				new HashMap<String, XMLElement>();
			for (XMLElement element : elements.values()) {
				String className = elementNameGenerator.getClassName(element);
				if (classNames.containsKey(className)) {
					XMLElement previous = classNames.get(className);
					throw new XMLParserGeneratorException(
						Strings.join(
							" ",
							"Both",
							previous,
							"and",
							element,
							"want the class name of",
							className
						)
					);
				}
				classNames.put(className, element);
			}

			// Create the root parser class
			Log.debug("Creating root parser class");
			createRootParserClass(targetDirectory);

			// Create the support classes
			Log.debug("Creating support classes");
			File supportDir = new File(targetDirectory, "support");
			createXMLComponentClass(supportDir);
			createXMLContentClass(supportDir);
			createAbstractElementClass(supportDir);
			createProcessingInstructionClass(supportDir);

			// Create one class per element
			Log.debug("Creating element classes");
			File elementsDir = new File(targetDirectory, "element");
			for (String elementName : elements.keySet()) {
				createElementClass(elements.get(elementName), elementsDir);
			}
		}
	}

	/** {@inheritDoc} */
	@Override public String describeLanguage() {
		return Message.LANGUAGE_JAVA();
	}

	/** {@inheritDoc} */
	@Override public String describeAPI() {
		return "A data binding API.";
	}

	/** Get the {@link ElementNameConverter} used to generate names from
		elements.

		@return	The {@link ElementNameConverter} used to generate names from
				elements.
	*/
	public ElementNameConverter<? extends ElementNameConverter<?>>
	getElementNameConverter()
	{
		return elementNameGenerator;
	}

	/** Set the {@link ElementNameConverter} to use to generate names from
		elements.

		@param	converter	The {@link ElementNameConverter} to use to generate
							names from elements.
	*/
	public void setElementNameConverter(
		ElementNameConverter<? extends ElementNameConverter<?>> converter
	)
	{
		elementNameGenerator = converter;
	}

	/** Get the {@link AttributeNameConverter} used to generate names from
		attributes.

		@return The {@link AttributeNameConverter} used to generate names from
				attributes.
	*/
	public AttributeNameConverter<? extends AttributeNameConverter<?>>
	getAttributeNameConverter()
	{
		return attNameGenerator;
	}

	/** Set the {@link AttributeNameConverter} to use to generate names from
		attributes.

		@param	converter	The {@link AttributeNameConverter} to use to
							generate names from attributes.
	*/
	public void setAttributeNameConverter(
		AttributeNameConverter<? extends AttributeNameConverter<?>> converter
	)
	{
		attNameGenerator = converter;
	}

	/** Create the base parser class which can be used to parse the input.

		@param	dir							The directory in which the base
											parser class will be put.
		@throws XMLParserGeneratorException	if the base parser class cannot be
											created.
	*/
	protected void createRootParserClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating root parser class");
		DBC.REQUIRE(dir != null);

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment("ROOT_PARSER_CLASS");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the root parser class name
		String vocabularyName = Options.getString("vocabulary");
		String rootParserClassName = Strings.upperCaseFirst(vocabularyName);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage,
			rootParserClassName
		);

		// Write out the file
		try {
			OutputUtils.writeStringToFile(
				outputFileContents,
				dir,
				String.format(JAVA, rootParserClassName)
			);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					e.getFile().getAbsolutePath()
				),
				e
			);
		}
	}

	/** Create the abstract element class which is the root of all element
		classes.

		@param	dir							The directory in which to place the
											class.
		@throws	XMLParserGeneratorException	if the abstract element class
											cannot be generated.
	*/
	protected void createAbstractElementClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating abstract element class");
		DBC.REQUIRE(dir != null);

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment(
			"ABSTRACT_ELEMENT_CLASS"
		);
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage
		);

		// Write out the file
		File file = new File(dir, PROGRAM_NAME + "Element.java");
		try {
			OutputUtils.writeStringToFile(outputFileContents, file);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					file.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Create a class for a given element.

		@param	element						The element to create the class
											for.
		@param	dir							The directory in which to create
											the class.
		@throws	XMLParserGeneratorException	if the abstract element class
											cannot be generated.
	*/
	protected void createElementClass(XMLElement element, File dir)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(element != null);
		DBC.REQUIRE(dir != null);
		Log.debug("Creating the Element class for ", element.getName());

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the element name and make an appropriate class name. 
		String className = elementNameGenerator.getClassName(element);

		// Make a member and getters and setters  for each attribute of the
		// class.
		StringBuilder constructorCode = new StringBuilder();
		StringBuilder accessMethods = new StringBuilder();
		for (XMLAttribute attribute : element.getAttlist()) {
			String attributeName = attribute.getName();

			String fixedValue = null;
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append("attributes.put(\"");
			constructorCode.append(attributeName);
			constructorCode.append("\", new Attribute(\"");
			constructorCode.append(attributeName);
			constructorCode.append("\", ");

			XMLAttributeDefaultType type = attribute.getDefaultDeclType();
			if (XMLAttributeDefaultType.ATTVALUE("foo").sameType(type)) {
				constructorCode.append("Attribute.DEFAULT, \"");
				constructorCode.append(type.getValue());
				constructorCode.append("\"));");
			} else if (XMLAttributeDefaultType.FIXED("foo").sameType(type)) {
				constructorCode.append("Attribute.FIXED, \"");
				constructorCode.append(type.getValue());
				constructorCode.append("\"));");
				fixedValue = type.getValue();
			} else if (XMLAttributeDefaultType.IMPLIED().sameType(type)) {
				constructorCode.append("Attribute.IMPLIED, null));");
			} else if (XMLAttributeDefaultType.REQUIRED().sameType(type)) {
				constructorCode.append("Attribute.REQUIRED, null));");
			}
			constructorCode.append(EOL);

			// The getter
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append("/** Getter for the ");
			accessMethods.append(attributeName);
			accessMethods.append(" attribute.");
			accessMethods.append(EOL);
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append(TAB);
			accessMethods.append("@return The value of the ");
			accessMethods.append(attributeName);
			accessMethods.append(" attribute.");
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append("*/");
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append("public String ");
			accessMethods.append(attNameGenerator.getGetterName(attribute));
			accessMethods.append("() {");
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append(TAB);
			if (fixedValue == null) {
				accessMethods.append("return ((Attribute)attributes.get(\"");
				accessMethods.append(attributeName);
				accessMethods.append("\")).getValue();");
			} else {
				accessMethods.append("return \"");
				accessMethods.append(fixedValue);
				accessMethods.append("\";");
			}
			accessMethods.append(EOL);
			accessMethods.append(TAB);
			accessMethods.append(RIGHT_CURLY);
			accessMethods.append(EOL);

			// The setter
			if (fixedValue == null) {
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append("/** Setter for the ");
				accessMethods.append(attributeName);
				accessMethods.append(" attribute.");
				accessMethods.append(EOL);
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
				accessMethods.append("@param value The value to set the ");
				accessMethods.append(attributeName);
				accessMethods.append(" attribute to.");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append("*/");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append("public void ");
				accessMethods.append(attNameGenerator.getSetterName(attribute));
				accessMethods.append("(String value) {");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
				accessMethods.append("Attribute att = (Attribute)");
				accessMethods.append("attributes.remove(\"");
				accessMethods.append(attributeName);
				accessMethods.append("\");");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
				accessMethods.append("att.setValue(value);");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
				accessMethods.append("attributes.put(\"");
				accessMethods.append(attributeName);
				accessMethods.append("\", att);");
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(RIGHT_CURLY);
				accessMethods.append(EOL);
			}
		}

		// Get the template
		String messageFormatTemplate = loadCodeFragment("ELEMENT_CLASS");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage,
			element.getName(),
			className,
			constructorCode,
			accessMethods
		);

		// Write out the file
		File file = new File(dir, String.format(JAVA, className));
		try {
			OutputUtils.writeStringToFile(outputFileContents, file);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					file.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Create the XMLContent class.

		@param	dir							The directory in which to create
											the class.
		@throws	XMLParserGeneratorException	if the XMLContent class cannot be
											generated.
	*/
	protected void createXMLContentClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating the XMLContent class");
		DBC.REQUIRE(dir != null);

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment(
			"XML_CONTENT_CLASS"
		);
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage
		);

		// Write out the file
		File file = new File(dir, String.format(JAVA, "XMLContent"));
		try {
			OutputUtils.writeStringToFile(outputFileContents, file);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					file.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Create the XMLComponent class.

		@param	dir							The directory in which to create
											the class.
		@throws	XMLParserGeneratorException	if the XMLComponent class
											cannot be generated.
	*/
	protected void createXMLComponentClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating the XMLComponent class");
		DBC.REQUIRE(dir != null);

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment(
			"XML_COMPONENT_CLASS"
		);
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage
		);

		// Write out the file
		File file = new File(dir, String.format(JAVA, "XMLComponent"));
		try {
			OutputUtils.writeStringToFile(outputFileContents, file);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					file.getAbsolutePath()
				),
				e
			);
		}
	}

	/** Create the ProcessingInstruction class.

		@param	dir							The directory in which to create
											the class.
		@throws	XMLParserGeneratorException	if the ProcessingInstruction class
											cannot be generated.
	*/
	protected void createProcessingInstructionClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating the ProcessingInstruction class");
		DBC.REQUIRE(dir != null);

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment(
			"PI_CLASS"
		);
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage
		);

		// Write out the file
		File file = new File(dir, String.format(JAVA, "ProcessingInstruction"));
		try {
			OutputUtils.writeStringToFile(outputFileContents, file);
		} catch (OutputException e) {
			throw new XMLParserGeneratorException(
				Message.FILE_WRITE_FAILED(
					file.getAbsolutePath()
				),
				e
			);
		}
	}

	/** {@inheritDoc} */
	public Generator getCopy()
	throws CopyException
	{
		return new Generator(
			codeFragments,
			timestamp,
			basePackage,
			elementNameGenerator.getCopy(),
			attNameGenerator.getCopy()
		);
	}
}
