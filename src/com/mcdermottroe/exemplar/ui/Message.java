// vim:filetype=java:ts=4
/*
	Copyright (c) 2004, 2005, 2006, 2007
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
package com.mcdermottroe.exemplar.ui;

import java.lang.reflect.Field;
import java.util.Enumeration;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

/** A class to contain all the localised versions of messages produced by the 
	program. The text of the messages is loaded from a .properties file.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Message {
	/** Ant localisation error message. */
	public static String ANT_LOCALISATION_ERROR = Constants.DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.
	*/
	public static String DTDLEXER_ILLEGAL_CHARACTER = Constants.DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an invalid conditional section was encountered.
	*/
	public static String DTDLEXER_INVALID_CONDITIONAL_SECTION = Constants.DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.
	*/
	public static String DTDLEXER_INVALID_TEXTDECL = Constants.DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an unknown character was encountered.
	*/
	public static String DTDLEXER_UNKNOWN_CHARACTER = Constants.DEFAULT_MESSAGE;

	/** DTD parsing threw an exception. */
	public static String DTDPARSER_THREW_EXCEPTION = Constants.DEFAULT_MESSAGE;

	/** Trailing characters were found after a SystemLiteral in a parameter
		entity declaration.
	*/
	public static String DTDPE_GARBAGE_AFTER_SYSTEMLITERAL = Constants.DEFAULT_MESSAGE;

	/** Error indicating that a parameter entity declaration was malformed. */
	public static String DTDPE_INVALID_PEDECL = Constants.DEFAULT_MESSAGE;

	/** Error indicating that a PubidLiteral was malformed. */
	public static String DTDPE_INVALID_PUBIDLITERAL = Constants.DEFAULT_MESSAGE;

	/** Error indicating that a SystemLiteral was malformed. */
	public static String DTDPE_INVALID_SYSTEMLITERAL = Constants.DEFAULT_MESSAGE;

	/** The type of a parameter entity reference was unknown. */
	public static String DTDPE_UNKNOWN_PE_TYPE = Constants.DEFAULT_MESSAGE;

	/** A reference to a parameter entity could not be resolved. */
	public static String DTDPE_UNRESOLVED_PE_REF = Constants.DEFAULT_MESSAGE;

	/** What to display when an {@link com.mcdermottroe.exemplar.Exception} has
		no message.
	*/
	public static String EXCEPTION_NO_MESSAGE = Constants.DEFAULT_MESSAGE;

	/** Message to say that we are ignoring an exception. */
	public static String IGNORING_EXCEPTION = Constants.DEFAULT_MESSAGE;

	/** Warning that mandatory options were not set. */
	public static String MANDATORY_OPTIONS_NOT_SET = Constants.DEFAULT_MESSAGE;

	/** The {@link String} printed before the legal options for an {@link
		com.mcdermottroe.exemplar.ui.Options.Enum} are enumerated for the user.
	*/
	public static String OPTION_ENUM_ARGS_HEADER = Constants.DEFAULT_MESSAGE;

	/** A message to inform the user that the option is mandatory. */
	public static String OPTION_IS_MANDATORY = Constants.DEFAULT_MESSAGE;

	/** A message to inform the user that an API must be selected when using a
		particular language.
	*/
	public static String OPTION_LANGUAGE_REQUIRES_API = Constants.DEFAULT_MESSAGE;

	/** Someone passed a null doctype to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.
	*/
	public static String SOURCE_GENERATOR_DOCTYPE_NULL = Constants.DEFAULT_MESSAGE;

	/** Someone passed a null language to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.
	*/
	public static String SOURCE_GENERATOR_LANGUAGE_NULL = Constants.DEFAULT_MESSAGE;

	/** A parser generator threw an exception. */
	public static String SOURCE_GENERATOR_THREW_EXCEPTION = Constants.DEFAULT_MESSAGE;

	/** Progress message "Done". */
	public static String UI_PROGRESS_DONE = Constants.DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the program failed to
		create any output.
	*/
	public static String UI_PROGRESS_FAILED_TO_CREATE_OUTPUT = Constants.DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the output parser is
		being generated.
	*/
	public static String UI_PROGRESS_GENERATING_PARSER = Constants.DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the parsing of the
		input file failed.
	*/
	public static String UI_PROGRESS_INPUT_PARSE_FAILED = Constants.DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the program is
		currently processing the options.
	*/
	public static String UI_PROGRESS_OPTIONS = Constants.DEFAULT_MESSAGE;

	/** Unsupported input type. */
	public static String UNSUPPORTED_INPUT_TYPE = Constants.DEFAULT_MESSAGE;

	/** Unsupported feature. */
	public static String XMLDOCTYPE_UNSUPPORTED_FEATURE = Constants.DEFAULT_MESSAGE;

	/** Unknown entity type. */
	public static String XMLENTITY_UNKNOWN_TYPE = Constants.DEFAULT_MESSAGE;

	/** {@link com.mcdermottroe.exemplar.model.XMLObject} not configured. */
	public static String XMLOBJECT_NOT_CONFIGURED = Constants.DEFAULT_MESSAGE;

	/** Unsupported action in {@link
		com.mcdermottroe.exemplar.model.XMLObject}.
	*/
	public static String XMLOBJECT_UNSUPPORTED_ACTION = Constants.DEFAULT_MESSAGE;

	/** {@link com.mcdermottroe.exemplar.output.XMLParserSourceGenerator}
		failed to load a code fragment.
	*/
	public static String XMLPARSER_LOAD_CODE_FRAGMENT_FAILED = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#ANT_EXTRA_METHOD(String)}.
	*/
	private static String ANT_EXTRA_METHOD_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#ANT_MISSING_METHOD(String)}.
	*/
	private static String ANT_MISSING_METHOD_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#ASSERTION_MESSAGE(String, String)}.
	*/
	private static String ASSERTION_MESSAGE_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DEBUG_CLASS_AND_METHOD(String, String)}.
	*/
	private static String DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDLEXER_INPUT_NOT_FOUND(String)}.
	*/
	private static String DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link #DTDPEDECLTABLE(int,
		int)}.
	*/
	private static String DTDPEDECLTABLE_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDPEEXCEPTION(String, String)}.
	*/
	private static String DTDPEEXCEPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDPE_UNDECLARED_PE(String)}.
	*/
	private static String DTDPE_UNDECLARED_PE_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#FILE_WRITE_FAILED(String)}.
	*/
	private static String FILE_WRITE_FAILED_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#FILE_WRITE_IO_EXCEPTION(String)}.
	*/
	private static String FILE_WRITE_IO_EXCEPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#GENERIC_SECURITY_EXCEPTION(String, String)}.
	*/
	private static String GENERIC_SECURITY_EXCEPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#GEN_NO_SUCH_DIRECTORY(String)}.
	*/
	private static String GEN_NO_SUCH_DIRECTORY_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#LEXEREXCEPTION(String, String)}.
	*/
	private static String LEXEREXCEPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#LOCALISATION_ERROR(String)}.
	*/
	private static String LOCALISATION_ERROR_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MESSAGE_EXTRA_BUNDLE_ENTRY(String, String)}.
	*/
	private static String MESSAGE_EXTRA_BUNDLE_ENTRY_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MESSAGE_MISSING(String)}.
	*/
	private static String MESSAGE_MISSING_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MISSING_MANDATORY_OPTION(String)}.
	*/
	private static String MISSING_MANDATORY_OPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTION_DEFAULT(String)}.
	*/
	private static String OPTION_DEFAULT_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTION_LANGUAGE_OF_API(String)}.
	*/
	private static String OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTIONS_NO_SUCH_OPTION(String)}.
	*/
	private static String OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#UI_PROGRESS_FINISHED_TIME(double)}.
	*/
	private static String UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#UI_PROGRESS_PARSING_INPUT(String)}.
	*/
	private static String UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string {@link
		#XMLDOCTYPE_ORPHAN_ATTLIST(String)}.
	*/
	private static String XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string
		{@link #XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(String)}.
	*/
	private static String XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT = Constants.DEFAULT_MESSAGE;

	/** Private constructor to prevent instantiation. */
	private Message() {
		DBC.UNREACHABLE_CODE();
	}

	/** Complain that a method exists in the {@link
		com.mcdermottroe.exemplar.ui.ant.Task Ant Task UI} which does not
		correspond to a defined UI option.

		@param	methodName	The name of the extra method.
		@return				An error formatted according to
							{@link #ANT_EXTRA_METHOD_MESSAGE_FORMAT}.
	*/
	public static String ANT_EXTRA_METHOD(String methodName) {
		DBC.REQUIRE(methodName != null);
		if (methodName == null) {
			return null;
		}
		return Utils.formatMessage(ANT_EXTRA_METHOD_MESSAGE_FORMAT, methodName);
	}

	/** Complain that a method implementing a UI option is not implemented in
		the {@link com.mcdermottroe.exemplar.ui.ant.Task Ant Task UI}.

		@param methodName	The name of the missing method.
		@return				An error formatted according to
							{@link #ANT_MISSING_METHOD_MESSAGE_FORMAT}.
	*/
	public static String ANT_MISSING_METHOD(String methodName) {
		DBC.REQUIRE(methodName != null);
		if (methodName == null) {
			return null;
		}
		return Utils.formatMessage(
			ANT_MISSING_METHOD_MESSAGE_FORMAT,
			methodName
		);
	}

	/** The message portion of an assertion thrown from {@link
		DBC#ASSERT(boolean)}.

		@param	reason	The reason that the assertion was thrown.
		@param	caller	The place that the assertion was thrown from.
		@return			An assertion message formatted by {@link 
						#ASSERTION_MESSAGE_MESSAGE_FORMAT}.
	*/
	public static String ASSERTION_MESSAGE(String reason, String caller) {
		DBC.REQUIRE(reason != null);
		DBC.REQUIRE(caller != null);
		if (reason == null || caller == null) {
			return null;
		}
		return Utils.formatMessage(
			ASSERTION_MESSAGE_MESSAGE_FORMAT,
			reason,
			caller
		);
	}

	/** A class and method name formatted for debugging.

		@param	className	The name of the class.
		@param	methodName	The name of the method.
		@return				A debug message formatted by {@link
							#DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT}.
	*/
	public static String DEBUG_CLASS_AND_METHOD(String className, String methodName) {
		DBC.REQUIRE(className != null);
		DBC.REQUIRE(methodName != null);
		if (className == null || methodName == null) {
			return null;
		}
		return Utils.formatMessage(
			DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT,
			className,
			methodName
		);
	}

	/** The input DTD was not found.

		@param fileName	The name of the DTD file that was attempled to be
						opened.
		@return			An error message formatted by {@link
						#DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT}.
	*/
	public static String DTDLEXER_INPUT_NOT_FOUND(String fileName) {
		DBC.REQUIRE(fileName != null);
		if (fileName == null) {
			return null;
		}
		return Utils.formatMessage(
			DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT,
			fileName
		);
	}

	/** Format for {@link
		com.mcdermottroe.exemplar.input.dtd.PEDeclTable#toString()}.

		@param	peDecl		The number of parameter entities with immediate
							values (not URI references).
		@param	uriPeDecl	The number of parameter entities which are URI
							references.
		@return 			A string formatted by the {@link
							#DTDPEDECLTABLE_MESSAGE_FORMAT}
	*/
	public static String DTDPEDECLTABLE(int peDecl, int uriPeDecl) {
		return Utils.formatMessage(
			DTDPEDECLTABLE_MESSAGE_FORMAT,
			peDecl,
			uriPeDecl
		);
	}

	/** The 2 parameter parameter entity exception.

		@param	s		The reason the exception is being thrown.
		@param	context	Some context info from the input document.
		@return			A message formatted according to {@link
						#DTDPEEXCEPTION_MESSAGE_FORMAT}.
	*/
	public static String DTDPEEXCEPTION(String s, String context) {
		DBC.REQUIRE(s != null);
		DBC.REQUIRE(context != null);
		if (s == null || context == null) {
			return null;
		}
		return Utils.formatMessage(DTDPEEXCEPTION_MESSAGE_FORMAT, s, context);
	}

	/** A reference to an undeclared parameter entity literal was encountered.

		@param	peName	The name of the parameter entity which is undeclared.
		@return			A descriptive error message formatted from {@link
						#DTDPE_UNDECLARED_PE_MESSAGE_FORMAT}.
	*/
	public static String DTDPE_UNDECLARED_PE(String peName) {
		DBC.REQUIRE(peName != null);
		if (peName == null) {
			return null;
		}
		return Utils.formatMessage(DTDPE_UNDECLARED_PE_MESSAGE_FORMAT, peName);
	}

	/** A write to a file failed for some reason.

		@param	filename	The name of the file the write failed on.
		@return				A message formatted according to {@link
							#FILE_WRITE_FAILED_MESSAGE_FORMAT}.
	*/
	public static String FILE_WRITE_FAILED(String filename) {
		DBC.REQUIRE(filename != null);
		if (filename == null) {
			return null;
		}
		return Utils.formatMessage(FILE_WRITE_FAILED_MESSAGE_FORMAT, filename);
	}

	/** An {@link java.io.IOException} occurred when writing to a file.

		@param filename		The name of the file that was being written to when
							the exception was thrown.
		@return				A message formatted according to
							{@link #FILE_WRITE_IO_EXCEPTION_MESSAGE_FORMAT}.
	*/
	public static String FILE_WRITE_IO_EXCEPTION(String filename) {
		DBC.REQUIRE(filename != null);
		if (filename == null) {
			return null;
		}
		return Utils.formatMessage(
			FILE_WRITE_IO_EXCEPTION_MESSAGE_FORMAT,
			filename
		);
	}

	/** A non-existant directory was referenced.

		@param directory	The directory that doesn't exist.
		@return				A message formatted according to {@link
							#GEN_NO_SUCH_DIRECTORY_MESSAGE_FORMAT}.
	*/
	public static String GEN_NO_SUCH_DIRECTORY(String directory) {
		DBC.REQUIRE(directory != null);
		if (directory == null) {
			return null;
		}
		return Utils.formatMessage(
			GEN_NO_SUCH_DIRECTORY_MESSAGE_FORMAT,
			directory
		);
	}

	/** A {@link SecurityException} was thrown.

		@param fieldOrMethod	The field or method which could not be accessed.
		@param className		The class that the field or method is located
								in.
		@return					A message formatted according to {@link
								#GENERIC_SECURITY_EXCEPTION_MESSAGE_FORMAT}.
	*/
	private static String GENERIC_SECURITY_EXCEPTION(String fieldOrMethod, String className) {
		DBC.REQUIRE(fieldOrMethod != null);
		DBC.REQUIRE(className != null);
		if (fieldOrMethod == null || className == null) {
			return null;
		}
		return Utils.formatMessage(
			GENERIC_SECURITY_EXCEPTION_MESSAGE_FORMAT,
			fieldOrMethod,
			className
		);
	}

	/** The message formatter for a {@link
		com.mcdermottroe.exemplar.input.LexerException}.

		@param	s		The {@link
						com.mcdermottroe.exemplar.input.LexerException} message
		@param	context	The context in which the exception was thrown
		@return			A formatted message for the {@link
						com.mcdermottroe.exemplar.input.LexerException}
	*/
	public static String LEXEREXCEPTION(String s, String context) {
		DBC.REQUIRE(s != null);
		DBC.REQUIRE(context != null);
		if (s == null || context == null) {
			return null;
		}
		return Utils.formatMessage(LEXEREXCEPTION_MESSAGE_FORMAT, s, context);
	}

	/** A localisation error occurred.

		@param	s	The type of localisation error
		@return		A formatted message describing the localisation error
	*/
	private static String LOCALISATION_ERROR(String s) {
		DBC.REQUIRE(s != null);
		if (s == null) {
			return null;
		}
		return Utils.formatMessage(LOCALISATION_ERROR_MESSAGE_FORMAT, s);
	}

	/** There's an entry in the messages properties file which has no
		corresponding field in this class.

		@param	entryName	The name of the entry in the messages properties
							file.
		@param	thisClass	The name of this class
		@return				A message formatted according to
							{@link #MESSAGE_EXTRA_BUNDLE_ENTRY_MESSAGE_FORMAT}.
	*/
	private static String MESSAGE_EXTRA_BUNDLE_ENTRY(String entryName, String thisClass) {
		DBC.REQUIRE(entryName != null);
		DBC.REQUIRE(thisClass != null);
		if (entryName == null || thisClass == null) {
			return null;
		}

		return Utils.formatMessage(
			MESSAGE_EXTRA_BUNDLE_ENTRY_MESSAGE_FORMAT,
			entryName,
			thisClass
		);
	}

	/** Report that a message is missing from the message properties file.

		@param	s	The name of the message which is missing.
		@return		A message formatted according to {@link
					#MESSAGE_MISSING_MESSAGE_FORMAT}.
	*/
	private static String MESSAGE_MISSING(String s) {
		DBC.REQUIRE(s != null);
		if (s == null) {
			return null;
		}
		return Utils.formatMessage(MESSAGE_MISSING_MESSAGE_FORMAT, s);
	}

	/** Tell the user which mandatory option is missing.

		@param	optionName	The name of the missing option.
		@return				A message formatted according to {@link
							#MISSING_MANDATORY_OPTION_MESSAGE_FORMAT}.
	*/
	public static String MISSING_MANDATORY_OPTION(String optionName) {
		DBC.REQUIRE(optionName != null);
		if (optionName == null) {
			return null;
		}
		return Utils.formatMessage(
			MISSING_MANDATORY_OPTION_MESSAGE_FORMAT,
			optionName
		);
	}

	/** Tell the user the default value of an option.

		@param	defaultValue	The default value of the option.
		@return					A message formatted according to {@link
								#OPTION_DEFAULT_MESSAGE_FORMAT}.
	*/
	public static String OPTION_DEFAULT(String defaultValue) {
		DBC.REQUIRE(defaultValue != null);
		if (defaultValue == null) {
			return null;
		}
		return Utils.formatMessage(OPTION_DEFAULT_MESSAGE_FORMAT, defaultValue);
	}

	/** Tell the user what languages an API may be used with.

		@param	language	The language the API may be used with.
		@return				A message formatted according to {@link
							#OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT}.
	*/
	public static String OPTION_LANGUAGE_OF_API(String language) {
		DBC.REQUIRE(language != null);
		if (language == null) {
			return null;
		}
		return Utils.formatMessage(
			OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT,
			language
		);
	}

	/** The user attempted to use an option which doesn't exist.

		@param optionName	The name of the ficticious option the user
							attempted to use.
		@return				A message formatted according to {@link
							#OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT}.
	*/
	public static String OPTIONS_NO_SUCH_OPTION(String optionName) {
		DBC.REQUIRE(optionName != null);
		if (optionName != null) {
			return null;
		}
		return Utils.formatMessage(
			OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT,
			optionName
		);
	}

	/** Progress message telling the user that the program has completed and
		provides the amount of time that the program took.

		@param	time	The amount of time taken in milliseconds.
		@return			A message formatted according to {@link
						#UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT}.
	*/
	public static String UI_PROGRESS_FINISHED_TIME(double time) {
		DBC.REQUIRE(time >= 0.0);
		return Utils.formatMessage(
			UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT,
			time
		);
	}

	/** Progress message telling the user that the program is parsing the
		input.

		@param	inputFileName	The name of the input file.
		@return					A message formatted according to {@link
								#UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT}
	*/
	public static String UI_PROGRESS_PARSING_INPUT(String inputFileName) {
		DBC.REQUIRE(inputFileName != null);
		if (inputFileName == null) {
			return null;
		}
		return Utils.formatMessage(
			UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT,
			inputFileName
		);
	}

	/** Attribute list without an element.

		@param	attlist	The attribute list that has no corresponding element.
		@return			A formatted error message formatted according to {@link
						#XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT}.
	*/
	public static String XMLDOCTYPE_ORPHAN_ATTLIST(String attlist) {
		DBC.REQUIRE(attlist == null);
		if (attlist == null) {
			return null;
		}
		return Utils.formatMessage(XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT, attlist);
	}

	/** Forbidden {@link com.mcdermottroe.exemplar.model.XMLObject} in the list
		of markup declarations.

		@param	xmlObject	The forbidden {@link
							com.mcdermottroe.exemplar.model.XMLObject}.
		@return				An error message formatted according to {@link
							#XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT}.
	*/
	public static String XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(String xmlObject) {
		DBC.REQUIRE(xmlObject != null);
		if (xmlObject == null) {
			return null;
		}
		return Utils.formatMessage(XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT, xmlObject);
	}

	/** Set the appropriate options to ensure that the program is set up for
		the current locale and read the messages from the appropriate
		properties file.

		@throws MessageException	if anything goes wrong while loading the
									messages from the backing store.
	*/
	public static void localise() throws MessageException {
		// Load the resource bundle with all the messages in it.
		ResourceBundle messages = ResourceBundle.getBundle(
			Message.class.getName(),
			Locale.getDefault()
		);

		// Get the "static" messages
		String couldNotLoadMessageMissing = "Localisation error: could not load MESSAGE_MISSING_MESSAGE_FORMAT";
		try {
			MESSAGE_MISSING_MESSAGE_FORMAT = messages.getString("MESSAGE_MISSING_MESSAGE_FORMAT");
		} catch (MissingResourceException e) {
			throw new MessageException(couldNotLoadMessageMissing, e);
		}
		if (MESSAGE_MISSING_MESSAGE_FORMAT == null) {
			throw new MessageException(couldNotLoadMessageMissing);
		}
		String localisationErrorMessageFormat = "LOCALISATION_ERROR_MESSAGE_FORMAT";
		try {
			LOCALISATION_ERROR_MESSAGE_FORMAT = messages.getString(localisationErrorMessageFormat);
		} catch (MissingResourceException e) {
			throw new MessageException(MESSAGE_MISSING(localisationErrorMessageFormat), e);
		}
		if (LOCALISATION_ERROR_MESSAGE_FORMAT == null) {
			throw new MessageException(MESSAGE_MISSING(localisationErrorMessageFormat));
		}
		// Get all the other messages
		try {
			Field[] fields = Message.class.getDeclaredFields();
			for (Field field : fields) {
				// String fields are the only ones that matter
				if (!String.class.equals(field.getType())) {
					continue;
				}

				// Get the field name
				String fieldName = field.getName();

				// Get the message for this field
				String message;
				try {
					message = messages.getString(fieldName);
				} catch (MissingResourceException e) {
					throw new MessageException(MESSAGE_MISSING(fieldName), e);
				}

				// Set the value of the field
				if (message != null) {
					field.set(null, message);
				} else {
					throw new MessageException(MESSAGE_MISSING(fieldName));
				}
			}
		} catch (IllegalAccessException e) {
			throw new MessageException(LOCALISATION_ERROR("Denied access to the Message class"), e);
		}

		// Check that all of the messages in the resource bundle
		// have a corresponding member in this class
		for (Enumeration<String> entries = messages.getKeys(); entries.hasMoreElements(); ) {
			DBC.ASSERT(entries != null);
			String entry = entries.nextElement();
			try {
				Message.class.getDeclaredField(entry);
			} catch (NoSuchFieldException e) {
				throw new MessageException(MESSAGE_EXTRA_BUNDLE_ENTRY(entry, Message.class.getName()), e);
			} catch (SecurityException e) {
				throw new MessageException(GENERIC_SECURITY_EXCEPTION(entry, Message.class.getName()), e);
			}
		}
	}
}
