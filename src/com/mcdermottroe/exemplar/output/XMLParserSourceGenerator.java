// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2007
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
package com.mcdermottroe.exemplar.output;

import java.io.File;
import java.util.Date;
import java.util.Map;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Resources;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.CWD;
import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;
import static com.mcdermottroe.exemplar.Constants.Output.CLASS;
import static com.mcdermottroe.exemplar.Constants.Output.PACKAGE;
import static com.mcdermottroe.exemplar.Constants.TIMESTAMP_FORMAT;

/** An interface for XML parser generators that produce source code.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of {@link XMLParserSourceGenerator}.
*/
public abstract
class XMLParserSourceGenerator<T extends XMLParserSourceGenerator<T>>
extends XMLParserGenerator<T>
{
	/** The code fragments that the source generator will use. */
	protected Map<String, String> codeFragments;

	/** A timestamp string with the date and time the {@link
		XMLParserSourceGenerator} was created.
	*/
	protected String timestamp;

	/** Creates a new {@link XMLParserSourceGenerator}. Protected because only
		subclasses should call this. One should never instantiate this class.

		@throws	XMLParserGeneratorException	if the code fragments could not be
											loaded.
	*/
	protected XMLParserSourceGenerator()
	throws XMLParserGeneratorException
	{
		super();

		// Get the code fragments
		codeFragments = Resources.get(getClass());
		if (codeFragments.isEmpty()) {
			throw new XMLParserGeneratorException(
				Message.XMLPARSER_LOAD_CODE_FRAGMENT_FAILED()
			);
		}

		// Format the timestamp
		timestamp = Strings.formatMessage(TIMESTAMP_FORMAT, new Date());
	}

	/** Generates a parser and places the source (if any) in the given
		directory.

		@param	doctype						The description of the vocabulary of
											XML to generate a parser for.
		@param	targetDirectory				The directory to place the source
											(if any).
		@throws	XMLParserGeneratorException	if an error occurs during code
											generation. If the implementation
											wishes to report any error it should
											be via this mechanism.
	*/
	public abstract void generateParser	(
											XMLDocumentType doctype,
											File targetDirectory
										)
	throws XMLParserGeneratorException;

	/** Given a file, return a {@link File} object for the file's parent 
		directory, given a directory, return the directory. This will ensure
		that the returned directory exists.

		@param	fileOrDir					A file or directory. If this is null
											then the current working directory
											of the program will be used.
		@return								A {@link File} representing a
											directory.
		@throws	XMLParserGeneratorException	if the directory does not exist.
	*/
	protected static File getSourceDirectory(File fileOrDir)
	throws XMLParserGeneratorException
	{
		// If the passed in directory is null, then the current diectory should
		// be used.
		File sourceDirectory;
		if (fileOrDir != null) {
			sourceDirectory = fileOrDir.getAbsoluteFile();
		} else {
			sourceDirectory = new File(CWD);
		}

		// Resolve the targetDirectory parameter into an absolute path that
		// exists.
		if (sourceDirectory.exists()) {
			if (!sourceDirectory.isDirectory()) {
				sourceDirectory = sourceDirectory.getParentFile();
			}
		} else {
			throw new XMLParserGeneratorException(
				Message.GEN_NO_SUCH_DIRECTORY(
					sourceDirectory.toString()
				)
			);
		}

		return sourceDirectory;
	}

	/** Get an {@link XMLParserSourceGenerator} based on a pair of language and
		API.

		@param lang		The language.
		@param api		The API, may be null if no API applies.
		@return			An instantiated {@link XMLParserSourceGenerator} which 
						will generate code for the language and API requested.
						Will return null if anything goes wrong.
	*/
	public static XMLParserSourceGenerator<?> create(String lang, String api) {
		Log.debug(
			"Attempting to create an instance of " +	// NON-NLS
			XMLParserSourceGenerator.class +
			" for language \"" +						// NON-NLS
			lang +
			"\" and API \"" +							// NON-NLS
			api +
			"\"."
		);
		DBC.REQUIRE(lang != null);
		if (lang == null) {
			return null;
		}

		XMLParserSourceGenerator<?> generator = null;
		try {
			// Create the String version of the XMLParserSourceGenerator class
			// name.
			StringBuilder generatorClassName = new StringBuilder();
			generatorClassName.append(PACKAGE);
			generatorClassName.append(FULL_STOP);
			generatorClassName.append(lang.toLowerCase());
			if (api != null) {
				generatorClassName.append(FULL_STOP);
				generatorClassName.append(api.toLowerCase());
			}
			generatorClassName.append(FULL_STOP);
			generatorClassName.append(CLASS);

			// Now turn the class name into a class.
			Class<?> generatorClass;
			generatorClass = Class.forName(generatorClassName.toString());

			// Make sure that the class is of the approved type
			if	(
					XMLParserSourceGenerator.class.isAssignableFrom(
						generatorClass
					)
				)
			{
				// Instantiate the class
				generator = XMLParserSourceGenerator.class.cast(
					generatorClass.newInstance()
				);
			}
		} catch (ClassNotFoundException e) {
			DBC.IGNORED_EXCEPTION(e);
		} catch (IllegalAccessException e) {
			DBC.IGNORED_EXCEPTION(e);
		} catch (InstantiationException e) {
			DBC.IGNORED_EXCEPTION(e);
		} catch (SecurityException e) {
			DBC.IGNORED_EXCEPTION(e);
		}

		if (generator != null) {
			Log.debug(
				"Created an instance of " +
				generator.getClass().getName()
			);
		} else {
			Log.debug("Failed to find a suitable generator.");
		}
		return generator;
	}

	/** Load a code fragment as a {@link String} and wrap any errors in an
		{@link XMLParserGeneratorException}.

		@param fragmentName					The key for the code fragment in the
											{@link java.util.ResourceBundle}.
		@return								The {@link String} referenced by the
											given key in the {@link
											java.util.ResourceBundle}.
		@throws	XMLParserGeneratorException	if the fragment requested does not
											exist or if an error occurred while
											fetching it.
	*/
	protected String loadCodeFragment(String fragmentName)
	throws XMLParserGeneratorException
	{
		if (!codeFragments.isEmpty()) {
			return codeFragments.get(fragmentName);
		} else {
			throw new XMLParserGeneratorException(
				Message.XMLPARSER_LOAD_CODE_FRAGMENT_FAILED()
			);
		}
	}

	/**	Load a code fragment as a {@link String} if a value has been set in an 
		{@link com.mcdermottroe.exemplar.ui.options.Enum}.

		@param	fragmentName				The key for the code fragment in the
											{@link java.util.ResourceBundle}.
		@param	enumName					The name of the enumerated option to
	 										check.
		@param	enumProperty				The name of the property in the
											enumerated option to check.
		@return								The {@link String} referenced by
											<code>fragmentName</code> in the
											{@link java.util.ResourceBundle}, or
											the empty {@link String} if the
											condition in the enumerated option
											was not met.
		@throws	XMLParserGeneratorException	if the fragment requested does not
											exist or if an error occurred while
											fetching it.
	*/
	protected String loadCodeFragmentIfSet	(
												String fragmentName,
												String enumName,
												String enumProperty
											)
	throws XMLParserGeneratorException
	{
		return loadCodeFragmentIfSet(fragmentName, enumName, enumProperty, "");
	}

	/** Load a code fragment as a {@link String} if a value has been set in an 
	 	{@link com.mcdermottroe.exemplar.ui.options.Enum}.

		@param	fragmentName				The key for the code fragment in the
											{@link java.util.ResourceBundle}.
		@param	enumName					The name of the enumerated option to
											check.
		@param	enumProperty				The name of the property in the
											enumerated option to check.
		@param	defaultValue				The default value to return if the
											enumerated property was not set.
		@return								The {@link String} referenced by
											<code>fragmentName</code> in the
											{@link java.util.ResourceBundle}, or
											the empty {@link String} if the
											condition in  the enumerated option
											was not met.
		@throws	XMLParserGeneratorException	if the fragment requested does not
											exist or if an error occurred while
											fetching it.
	*/
	protected String loadCodeFragmentIfSet	(
												String fragmentName,
												String enumName,
												String enumProperty,
												String defaultValue
											)
	throws XMLParserGeneratorException
	{
		if (Options.isSet(enumName, enumProperty)) {
			return loadCodeFragment(fragmentName);
		} else {
			return defaultValue;
		}
	}

	/**	Load a code fragment as a {@link String} if a value has <i>not</i> been
		set in an enumerated option.

		@param	fragmentName				The key for the code fragment in the
											{@link java.util.ResourceBundle}.
		@param	enumName					The name of the enumerated option to
											check.
		@param	enumProperty				The name of the property in the
											enumerated option to check.
		@return								The {@link String} referenced by
											<code>fragmentName</code> in the
											{@link java.util.ResourceBundle}, or
											the empty {@link String} if the
											condition in the enumerated option
											was set.
		@throws	XMLParserGeneratorException	if the fragment requested does not
	 										exist or if an error occurred while
	 										fetching it.
	*/
	protected String loadCodeFragmentUnlessSet	(
													String fragmentName,
													String enumName,
													String enumProperty
												)
	throws XMLParserGeneratorException
	{
		return loadCodeFragmentUnlessSet(
											fragmentName,
											enumName,
											enumProperty,
											""
										);
	}

	/** Load a code fragment as a {@link String} if a value has <i>not</i> been
		set in an enumerated option.

		@param	fragmentName				The key for the code fragment in the
											{@link java.util.ResourceBundle}.
		@param	enumName					The name of the enumerated option to
											check.
		@param	enumProperty				The name of the property in the
											enumerated option to check.
		@param	defaultValue				The value to return if the option
											was set.
		@return								The {@link String} referenced by
											<code>fragmentName</code> in the
											{@link java.util.ResourceBundle}, or
											<code>defaultValue</code> if the
											condition in the enumerated option
											was set.
		@throws	XMLParserGeneratorException	if the fragment requested does not
											exist or if an error occurred while
											fetching it.
	*/
	protected String loadCodeFragmentUnlessSet	(
													String fragmentName,
													String enumName,
													String enumProperty,
													String defaultValue
												)
	throws XMLParserGeneratorException
	{
		if (Options.isSet(enumName, enumProperty)) {
			return defaultValue;
		} else {
			return loadCodeFragment(fragmentName);
		}
	}
}
