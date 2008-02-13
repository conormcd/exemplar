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
package com.mcdermottroe.exemplar.output.java.binding;

import java.io.File;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

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

import static com.mcdermottroe.exemplar.Constants.Character.DOUBLE_QUOTE;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
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

	/** The name of the root parser class. */
	protected String rootParserClassName;

	/** The name of the exception class. */
	protected String exceptionClassName;

	/** The name of the tree operation interface. */
	protected String treeOpInterfaceName;

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
		exceptionClassName = null;
		treeOpInterfaceName = null;
		rootParserClassName = null;
	}

	/** Copy constructor, see {@link
		XMLParserSourceGenerator#XMLParserSourceGenerator(Map, String)} for
		details.

		@param	code					The code fragments.
		@param	time					The timestamp.
		@param	pkg						The {@link #basePackage}.
		@param	elementNameConverter	The {@link #elementNameGenerator}.
		@param	attNameConverter		The {@link #attNameGenerator}.
		@param	rootParserClass			The {@link #rootParserClassName}.
		@param	exceptionClass			The {@link #exceptionClassName}.
		@param	treeOpInterface			The {@link #treeOpInterfaceName}.
	*/
	protected Generator(
		Map<String, String> code,
		String time,
		String pkg,
		ElementNameConverter<? extends ElementNameConverter<?>>
			elementNameConverter,
		AttributeNameConverter<? extends AttributeNameConverter<?>>
			attNameConverter,
		String rootParserClass,
		String exceptionClass,
		String treeOpInterface
	)
	{
		super(code, time);
		basePackage = pkg;
		elementNameGenerator = elementNameConverter;
		attNameGenerator = attNameConverter;
		rootParserClassName = rootParserClass;
		exceptionClassName = exceptionClass;
		treeOpInterfaceName = treeOpInterface;
	}

	/** {@inheritDoc} */
	@Override public void generateParser(
		XMLDocumentType doctype,
		File targetDirectory
	)
	throws XMLParserGeneratorException
	{
		DBC.REQUIRE(doctype != null);
		assert doctype != null;
		DBC.REQUIRE(targetDirectory != null);
		assert targetDirectory != null;

		// Set some of the common stuff
		String vocabularyName = Options.getString("vocabulary");
		rootParserClassName = Strings.upperCaseFirst(vocabularyName);
		basePackage = Options.getString("output-package");
		if (basePackage == null) {
			throw new XMLParserGeneratorException(
				Message.MANDATORY_OPTIONS_NOT_SET()
			);
		}
		StringBuilder exClassName = new StringBuilder(rootParserClassName);
		exClassName.append("Exception");
		exceptionClassName = exClassName.toString();
		StringBuilder treeOpIntName = new StringBuilder(rootParserClassName);
		treeOpIntName.append("TreeOp");
		treeOpInterfaceName = treeOpIntName.toString();

		// Get the elements
		Map<String, XMLElement> elements = doctype.elements();

		// Only create code if there are some elements
		if (elements != null && !elements.isEmpty()) {
			// Ensure that we can create unique class names for all of the
			// elements.
			SortedMap<String, XMLElement> classNames =
				new TreeMap<String, XMLElement>();
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
			createRootParserClass(targetDirectory, classNames);

			// Create the support classes
			Log.debug("Creating support classes");
			File supportDir = new File(targetDirectory, "support");
			createXMLComponentClass(supportDir);
			createXMLContentClass(supportDir);
			createAbstractElementClass(supportDir);
			createAttributeClass(supportDir);
			createProcessingInstructionClass(supportDir);
			createExceptionClass(supportDir);
			createTreeOpInterface(supportDir);

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
		@param	classNames					A {@link Map} of class names to
											{@link XMLElement}s for generating
											the visitor methods.
		@throws XMLParserGeneratorException	if the base parser class cannot be
											created.
	*/
	protected void createRootParserClass(
		File dir,
		Map<String, XMLElement> classNames
	)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating root parser class");
		DBC.REQUIRE(dir != null);
		assert dir != null;

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment("ROOT_PARSER_CLASS");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage,
			rootParserClassName,
			exceptionClassName,
			treeOpInterfaceName
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
		assert dir != null;

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

	/** Create the attribute class.

		@param	dir							The directory in which to place the
											class.
		@throws	XMLParserGeneratorException	if the attribute class cannot be
											generated.
	*/
	protected void createAttributeClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating attribute class");
		DBC.REQUIRE(dir != null);
		assert dir != null;

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment(
			"ATTRIBUTE_CLASS"
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
		File file = new File(dir, PROGRAM_NAME + "Attribute.java");
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
		assert element != null;
		DBC.REQUIRE(dir != null);
		assert dir != null;
		Log.debug("Creating the Element class for ", element.getName());

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the element name and make an appropriate class name. 
		String className = elementNameGenerator.getClassName(element);

		// Make a member and getters and setters  for each attribute of the
		// class.
		StringBuilder constructorCode = new StringBuilder();
		StringBuilder accessMethods = new StringBuilder();
		StringBuilder compareToBody = new StringBuilder();
		StringBuilder hashCodeBody = new StringBuilder();
		for (XMLAttribute attribute : element.getAttlist()) {
			String attributeName = attribute.getName();

			String fixedValue = null;
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append("attributes.put(");
			constructorCode.append(EOL);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(DOUBLE_QUOTE);
			constructorCode.append(attributeName);
			constructorCode.append("\",");
			constructorCode.append(EOL);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append("new ");
			constructorCode.append(PROGRAM_NAME);
			constructorCode.append("Attribute(");
			constructorCode.append(EOL);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(DOUBLE_QUOTE);
			constructorCode.append(attributeName);
			constructorCode.append("\",");
			constructorCode.append(EOL);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(TAB);

			XMLAttributeDefaultType type = attribute.getDefaultDeclType();
			if (XMLAttributeDefaultType.ATTVALUE("foo").sameType(type)) {
				constructorCode.append(PROGRAM_NAME);
				constructorCode.append("Attribute.DEFAULT,");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(DOUBLE_QUOTE);
				constructorCode.append(type.getValue());
				constructorCode.append(DOUBLE_QUOTE);
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(RIGHT_PAREN);
			} else if (XMLAttributeDefaultType.FIXED("foo").sameType(type)) {
				constructorCode.append(PROGRAM_NAME);
				constructorCode.append("Attribute.FIXED,");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(DOUBLE_QUOTE);
				constructorCode.append(type.getValue());
				constructorCode.append(DOUBLE_QUOTE);
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(RIGHT_PAREN);
				fixedValue = type.getValue();
			} else if (XMLAttributeDefaultType.IMPLIED().sameType(type)) {
				constructorCode.append(PROGRAM_NAME);
				constructorCode.append("Attribute.IMPLIED,");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append("null");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(RIGHT_PAREN);
			} else if (XMLAttributeDefaultType.REQUIRED().sameType(type)) {
				constructorCode.append(PROGRAM_NAME);
				constructorCode.append("Attribute.REQUIRED,");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append("null");
				constructorCode.append(EOL);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(TAB);
				constructorCode.append(RIGHT_PAREN);
			}
			constructorCode.append(EOL);
			constructorCode.append(TAB);
			constructorCode.append(TAB);
			constructorCode.append(");");
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
			if (fixedValue == null) {
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
				accessMethods.append("return attributes.get(\"");
				accessMethods.append(attributeName);
				accessMethods.append("\").getValue();");
			} else {
				accessMethods.append(EOL);
				accessMethods.append(TAB);
				accessMethods.append(TAB);
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
				accessMethods.append(PROGRAM_NAME);
				accessMethods.append("Attribute att = ");
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

			// Make the comparator
			if (fixedValue == null) {
				String cmpVar = attNameGenerator.getVariableName(
					attribute,
					"cmp"
				);
				String getterName = attNameGenerator.getGetterName(attribute);
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("// Compare the ");
				compareToBody.append(attributeName);
				compareToBody.append(" attribute.");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("if (");
				compareToBody.append(getterName);
				compareToBody.append("() != null && other.");
				compareToBody.append(getterName);
				compareToBody.append("() != null) {");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("int ");
				compareToBody.append(cmpVar);
				compareToBody.append(" = ");
				compareToBody.append(getterName);
				compareToBody.append("().compareTo(other.");
				compareToBody.append(getterName);
				compareToBody.append("());");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("if (");
				compareToBody.append(cmpVar);
				compareToBody.append(" != 0) {");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("return ");
				compareToBody.append(cmpVar);
				compareToBody.append(';');
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(RIGHT_CURLY);
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("} else if (");
				compareToBody.append(getterName);
				compareToBody.append("() != null) {");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("return 1;");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("} else if (other.");
				compareToBody.append(getterName);
				compareToBody.append("() != null) {");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append("return -1;");
				compareToBody.append(EOL);
				compareToBody.append(TAB);
				compareToBody.append(TAB);
				compareToBody.append(RIGHT_CURLY);
				compareToBody.append(EOL);

				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				if (hashCodeBody.length() == 2) {
					hashCodeBody.append("String ");
				}
				hashCodeBody.append("fieldValue = ");
				hashCodeBody.append(getterName);
				hashCodeBody.append("();");
				hashCodeBody.append(EOL);
				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				hashCodeBody.append("if (fieldValue != null) {");
				hashCodeBody.append(EOL);
				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				hashCodeBody.append("code += fieldValue.hashCode();");
				hashCodeBody.append(EOL);
				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				hashCodeBody.append(RIGHT_CURLY);
				hashCodeBody.append(EOL);
				hashCodeBody.append(TAB);
				hashCodeBody.append(TAB);
				hashCodeBody.append("code *= HASHCODE_MAGIC;");
				hashCodeBody.append(EOL);
			}
		}

		String attributeClassImport;
		if (!element.getAttlist().getAttributes().isEmpty()) {
			StringBuilder attClassImport = new StringBuilder("import ");
			attClassImport.append(basePackage);
			attClassImport.append(".support.");
			attClassImport.append(PROGRAM_NAME);
			attClassImport.append("Attribute;");
			attClassImport.append(EOL);
			attributeClassImport = attClassImport.toString();
		} else {
			attributeClassImport = "";
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
			accessMethods,
			compareToBody,
			hashCodeBody,
			attributeClassImport
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
		assert dir != null;

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
		assert dir != null;

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
		assert dir != null;

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

	/** Create the exception class.

		@param	dir							The directory in which the base
											parser class will be put.
		@throws XMLParserGeneratorException	if the base parser class cannot be
											created.
	*/
	protected void createExceptionClass(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating exception class");
		DBC.REQUIRE(dir != null);
		assert dir != null;

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment("EXCEPTION_CLASS");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage,
			exceptionClassName
		);

		// Write out the file
		try {
			OutputUtils.writeStringToFile(
				outputFileContents,
				dir,
				String.format(JAVA, exceptionClassName)
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

	/** Create the TreeOp interface.

		@param	dir							The directory in which the base
											parser class will be put.
		@throws XMLParserGeneratorException	if the base parser class cannot be
											created.
	*/
	protected void createTreeOpInterface(File dir)
	throws XMLParserGeneratorException
	{
		Log.debug("Creating TreeOp interface");
		DBC.REQUIRE(dir != null);
		assert dir != null;

		// Ensure that the directory exists
		dir.mkdirs();

		// Get the template
		String messageFormatTemplate = loadCodeFragment("TREEOP_INTERFACE");
		DBC.ASSERT(messageFormatTemplate != null);

		// Make the contents of the output file
		String outputFileContents = Strings.formatMessage(
			messageFormatTemplate,
			PROGRAM_NAME,
			timestamp,
			basePackage,
			treeOpInterfaceName,
			exceptionClassName
		);

		// Write out the file
		try {
			OutputUtils.writeStringToFile(
				outputFileContents,
				dir,
				String.format(JAVA, treeOpInterfaceName)
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

	/** {@inheritDoc} */
	public Generator getCopy()
	throws CopyException
	{
		return new Generator(
			codeFragments,
			timestamp,
			basePackage,
			elementNameGenerator.getCopy(),
			attNameGenerator.getCopy(),
			rootParserClassName,
			exceptionClassName,
			treeOpInterfaceName
		);
	}
}
