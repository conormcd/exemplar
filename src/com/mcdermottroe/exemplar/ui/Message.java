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
package com.mcdermottroe.exemplar.ui;

import java.lang.reflect.Field;
import java.util.Map;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.utils.Resources;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.COPYRIGHT_MESSAGE;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.DEFAULT_MESSAGE;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_NAME;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_VERSION;
import static com.mcdermottroe.exemplar.Constants.UI.COULD_NOT_LOAD_MESSAGE_MISSING;
import static com.mcdermottroe.exemplar.Constants.UI.DENIED_ACCESS_TO_MESSAGE_CLASS;

/** A class to contain all the localised versions of messages produced by the
	program. The text of the messages is loaded from a .properties file.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Message {
	/** {@link java.text.MessageFormat} string for {@link
		#ANT_EXTRA_METHOD(String)}.
	*/
	private static String ANT_EXTRA_METHOD_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Ant localisation error message. */
	private static String ANT_LOCALISATION_ERROR = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#ANT_MISSING_METHOD(String)}.
	*/
	private static String ANT_MISSING_METHOD_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#ASSERTION_MESSAGE(String, String)}.
	*/
	private static String ASSERTION_MESSAGE_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Attempting to parse foo as bar. */
	private static String ATTEMPTING_TO_PARSE_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DEBUG_CLASS_AND_METHOD(String, String)}.
	*/
	private static String DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.
	*/
	private static String DTDLEXER_ILLEGAL_CHARACTER = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDLEXER_INPUT_NOT_FOUND(String)}.
	*/
	private static String DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an invalid conditional section was encountered.
	*/
	private static String DTDLEXER_INVALID_CONDITIONAL_SECTION = DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.
	*/
	private static String DTDLEXER_INVALID_TEXTDECL = DEFAULT_MESSAGE;

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an unknown character was encountered.
	*/
	private static String DTDLEXER_UNKNOWN_CHARACTER = DEFAULT_MESSAGE;

	/** DTD parsing threw an exception. */
	private static String DTDPARSER_THREW_EXCEPTION = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link #DTDPEDECLTABLE(int,
		int)}.
	*/
	private static String DTDPEDECLTABLE_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDPEEXCEPTION(String, String)}.
	*/
	private static String DTDPEEXCEPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Trailing characters were found after a SystemLiteral in a parameter
		entity declaration.
	*/
	private static String DTDPE_GARBAGE_AFTER_SYSTEMLITERAL = DEFAULT_MESSAGE;

	/** Error indicating that a parameter entity declaration was malformed. */
	private static String DTDPE_INVALID_PEDECL = DEFAULT_MESSAGE;

	/** Error indicating that a PubidLiteral was malformed. */
	private static String DTDPE_INVALID_PUBIDLITERAL = DEFAULT_MESSAGE;

	/** Error indicating that a SystemLiteral was malformed. */
	private static String DTDPE_INVALID_SYSTEMLITERAL = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#DTDPE_UNDECLARED_PE(String)}.
	*/
	private static String DTDPE_UNDECLARED_PE_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** A reference to a parameter entity could not be resolved. */
	private static String DTDPE_UNRESOLVED_PE_REF = DEFAULT_MESSAGE;

	/** What to display when an {@link com.mcdermottroe.exemplar.Exception} has
		no message.
	*/
	private static String EXCEPTION_NO_MESSAGE = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#FILE_WRITE_FAILED(String)}.
	*/
	private static String FILE_WRITE_FAILED_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#FILE_WRITE_IO_EXCEPTION(String)}.
	*/
	private static String FILE_WRITE_IO_EXCEPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#GENERIC_SECURITY_EXCEPTION(String, String)}.
	*/
	private static String GENERIC_SECURITY_EXCEPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#GEN_NO_SUCH_DIRECTORY(String)}.
	*/
	private static String GEN_NO_SUCH_DIRECTORY_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Message to say that we are ignoring an error. */
	private static String IGNORING_ERROR = DEFAULT_MESSAGE;

	/** Message to say that we are ignoring an exception. */
	private static String IGNORING_EXCEPTION = DEFAULT_MESSAGE;

	/** The XML DTD language. */
	private static String LANGUAGE_DTD = DEFAULT_MESSAGE;

	/** The Java language. */
	private static String LANGUAGE_JAVA = DEFAULT_MESSAGE;

	/** The XSLT language. */
	private static String LANGUAGE_XSLT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#LEXEREXCEPTION(String, String)}.
	*/
	private static String LEXEREXCEPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#LOCALISATION_ERROR(String)}.
	*/
	private static String LOCALISATION_ERROR_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MALFORMED_CHAR_REF(CharSequence)}.
	*/
	private static String MALFORMED_CHAR_REF_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Warning that mandatory options were not set. */
	private static String MANDATORY_OPTIONS_NOT_SET = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MESSAGE_EXTRA_BUNDLE_ENTRY(String, String)}.
	*/
	private static String MESSAGE_EXTRA_BUNDLE_ENTRY_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MESSAGE_MISSING(String)}.
	*/
	private static String MESSAGE_MISSING_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#MISSING_MANDATORY_OPTION(String)}.
	*/
	private static String MISSING_MANDATORY_OPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTION_DEFAULT(String)}.
	*/
	private static String OPTION_DEFAULT_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** The {@link String} printed before the legal options for an {@link
		com.mcdermottroe.exemplar.ui.options.Enum} are enumerated for the user.
	*/
	private static String OPTION_ENUM_ARGS_HEADER = DEFAULT_MESSAGE;

	/** A message to inform the user that the option is mandatory. */
	private static String OPTION_IS_MANDATORY = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTION_LANGUAGE_OF_API(String)}.
	*/
	private static String OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** A message to inform the user that an API must be selected when using a
		particular language.
	*/
	private static String OPTION_LANGUAGE_REQUIRES_API = DEFAULT_MESSAGE;

	/** A progress message saying that we're initialising the {@link Options}
		store.
	*/
	private static String OPTIONS_INITIALISING = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#OPTIONS_NO_SUCH_OPTION(String)}.
	*/
	private static String OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Someone passed a null doctype to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.
	 */
	private static String SOURCE_GENERATOR_DOCTYPE_NULL = DEFAULT_MESSAGE;

	/** Someone passed a null language to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.
	 */
	private static String SOURCE_GENERATOR_LANGUAGE_NULL = DEFAULT_MESSAGE;

	/** A parser generator threw an exception. */
	private static String SOURCE_GENERATOR_THREW_EXCEPTION = DEFAULT_MESSAGE;

	/** Progress message "Done". */
	private static String UI_PROGRESS_DONE = DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the program failed to
		create any output.
	*/
	private static String UI_PROGRESS_FAILED_TO_CREATE_OUTPUT = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#UI_PROGRESS_FINISHED_TIME(double)}.
	*/
	private static String UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the output parser is
		being generated.
	*/
	private static String UI_PROGRESS_GENERATING_PARSER = DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the parsing of the
		input file failed.
	*/
	private static String UI_PROGRESS_INPUT_PARSE_FAILED = DEFAULT_MESSAGE;

	/** Progress message in the UI telling the user that the program is
		currently processing the options.
	*/
	private static String UI_PROGRESS_OPTIONS = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string for {@link
		#UI_PROGRESS_PARSING_INPUT(String)}.
	*/
	private static String UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** A message explaining that unreachable code has been reached. */
	private static String UNREACHABLE_CODE_REACHED = DEFAULT_MESSAGE;

	/** Unsupported input type. */
	private static String UNSUPPORTED_INPUT_TYPE = DEFAULT_MESSAGE;

	/** Unterminated character reference. */
	private static String UNTERMINATED_REF = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string {@link
		#XMLDOCTYPE_ORPHAN_ATTLIST(String)}.
	*/
	private static String XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** Unsupported feature. */
	private static String XMLDOCTYPE_UNSUPPORTED_FEATURE = DEFAULT_MESSAGE;

	/** {@link java.text.MessageFormat} string
		{@link #XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(String)}.
	*/
	private static String XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT = DEFAULT_MESSAGE;

	/** {@link com.mcdermottroe.exemplar.model.XMLObject} not configured. */
	private static String XMLOBJECT_NOT_CONFIGURED = DEFAULT_MESSAGE;

	/** {@link com.mcdermottroe.exemplar.output.XMLParserSourceGenerator}
		failed to load a code fragment.
	*/
	private static String XMLPARSER_LOAD_CODE_FRAGMENT_FAILED = DEFAULT_MESSAGE;

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
		return Strings.formatMessage(
			ANT_EXTRA_METHOD_MESSAGE_FORMAT,
			methodName
		);
	}

	/** Ant localisation error message.

		@return The error message.
	*/
	public static String ANT_LOCALISATION_ERROR() {
		return ANT_LOCALISATION_ERROR;
	}

	/** Complain that a method implementing a UI option is not implemented in
		the {@link com.mcdermottroe.exemplar.ui.ant.Task Ant Task UI}.

		@param methodName	The name of the missing method.
		@return				An error formatted according to
							{@link #ANT_MISSING_METHOD_MESSAGE_FORMAT}.
	*/
	public static String ANT_MISSING_METHOD(String methodName) {
		return Strings.formatMessage(
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
		return Strings.formatMessage(
			ASSERTION_MESSAGE_MESSAGE_FORMAT,
			reason,
			caller
		);
	}

	/** Attempting to parse [file] as [type].

		@param	file	The file we're attempting to parse.
		@param	type	The assumed type of the file.
		@return			A status message.
	*/
	public static String ATTEMPTING_TO_PARSE(String file, String type) {
		return Strings.formatMessage(
			ATTEMPTING_TO_PARSE_MESSAGE_FORMAT,
			file,
			type
		);
	}

	/** Format the standard copyright message.

		@return	A copyright message. 
	*/
	public static String COPYRIGHT() {
		StringBuilder retVal = new StringBuilder(PROGRAM_NAME);
		retVal.append(SPACE);
		retVal.append(PROGRAM_VERSION);
		retVal.append(EOL);
		for (String line : COPYRIGHT_MESSAGE) {
			retVal.append(line);
			retVal.append(EOL);
		}
		return retVal.toString();
	}

	/** A class and method name formatted for debugging.

		@param	className	The name of the class.
		@param	methodName	The name of the method.
		@return				A debug message formatted by {@link
							#DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT}.
	*/
	public static String DEBUG_CLASS_AND_METHOD(String className, String methodName) {
		return Strings.formatMessage(
			DEBUG_CLASS_AND_METHOD_MESSAGE_FORMAT,
			className,
			methodName
		);
	}

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.

		@return The error message.
	*/
	public static String DTDLEXER_ILLEGAL_CHARACTER() {
		return DTDLEXER_ILLEGAL_CHARACTER;
	}

	/** The input DTD was not found.

		@param fileName	The name of the DTD file that was attempled to be
						opened.
		@return			An error message formatted by {@link
						#DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT}.
	*/
	public static String DTDLEXER_INPUT_NOT_FOUND(String fileName) {
		return Strings.formatMessage(
			DTDLEXER_INPUT_NOT_FOUND_MESSAGE_FORMAT,
			fileName
		);
	}

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an invalid conditional section was encountered.

		@return The message.
	*/
	public static String DTDLEXER_INVALID_CONDITIONAL_SECTION() {
		return DTDLEXER_INVALID_CONDITIONAL_SECTION;
	}

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an illegal character was encountered.

		@return The error message.
	*/
	public static String DTDLEXER_INVALID_TEXTDECL() {
		return DTDLEXER_INVALID_TEXTDECL;
	}

	/** Error from the {@link com.mcdermottroe.exemplar.input.dtd.Lexer DTD
		Lexer} indicating that an unknown character was encountered.

		@return	A message.
	*/
	public static String DTDLEXER_UNKNOWN_CHARACTER() {
		return DTDLEXER_UNKNOWN_CHARACTER;
	}

	/** DTD parsing threw an exception.

		@return A message.
	*/
	public static String DTDPARSER_THREW_EXCEPTION() {
		return DTDPARSER_THREW_EXCEPTION;
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
		return Strings.formatMessage(
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
		return Strings.formatMessage(DTDPEEXCEPTION_MESSAGE_FORMAT, s, context);
	}

	/** Trailing characters were found after a SystemLiteral in a parameter
		entity declaration.

		@return The error message.
	*/
	public static String DTDPE_GARBAGE_AFTER_SYSTEMLITERAL() {
		return DTDPE_GARBAGE_AFTER_SYSTEMLITERAL;
	}

	/** Error indicating that a parameter entity declaration was malformed.

		@return The message.
	*/
	public static String DTDPE_INVALID_PEDECL() {
		return DTDPE_INVALID_PEDECL;
	}

	/** Error indicating that a PubidLiteral was malformed.

		@return The message.
	*/
	public static String DTDPE_INVALID_PUBIDLITERAL() {
		return DTDPE_INVALID_PUBIDLITERAL;
	}

	/** Error indicating that a SystemLiteral was malformed.

		@return The error message.
	*/
	public static String DTDPE_INVALID_SYSTEMLITERAL() {
		return DTDPE_INVALID_SYSTEMLITERAL;
	}

	/** A reference to an undeclared parameter entity literal was encountered.

		@param	peName	The name of the parameter entity which is undeclared.
		@return			A descriptive error message formatted from {@link
						#DTDPE_UNDECLARED_PE_MESSAGE_FORMAT}.
	*/
	public static String DTDPE_UNDECLARED_PE(String peName) {
		return Strings.formatMessage(DTDPE_UNDECLARED_PE_MESSAGE_FORMAT, peName);
	}

	/** A reference to a parameter entity could not be resolved.

		@return The message.
	*/
	public static String DTDPE_UNRESOLVED_PE_REF() {
		return DTDPE_UNRESOLVED_PE_REF;
	}

	/** What to display when an {@link com.mcdermottroe.exemplar.Exception} has
		no message.

		@return The default {@link com.mcdermottroe.exemplar.Exception} message.
	*/
	public static String EXCEPTION_NO_MESSAGE() {
		return EXCEPTION_NO_MESSAGE;
	}

	/** A write to a file failed for some reason.

		@param	filename	The name of the file the write failed on.
		@return				A message formatted according to {@link
							#FILE_WRITE_FAILED_MESSAGE_FORMAT}.
	*/
	public static String FILE_WRITE_FAILED(String filename) {
		return Strings.formatMessage(FILE_WRITE_FAILED_MESSAGE_FORMAT, filename);
	}

	/** An {@link java.io.IOException} occurred when writing to a file.

		@param filename		The name of the file that was being written to when
							the exception was thrown.
		@return				A message formatted according to
							{@link #FILE_WRITE_IO_EXCEPTION_MESSAGE_FORMAT}.
	*/
	public static String FILE_WRITE_IO_EXCEPTION(String filename) {
		return Strings.formatMessage(
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
		return Strings.formatMessage(
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
	public static String GENERIC_SECURITY_EXCEPTION(String fieldOrMethod, String className) {
		return Strings.formatMessage(
			GENERIC_SECURITY_EXCEPTION_MESSAGE_FORMAT,
			fieldOrMethod,
			className
		);
	}

	/** Message to say that we are ignoring an error.

		@return The message.
	*/
	public static String IGNORING_ERROR() {
		return IGNORING_ERROR;
	}

	/** Message to say that we are ignoring an exception.

		@return The message. 
	*/
	public static String IGNORING_EXCEPTION() {
		return IGNORING_EXCEPTION;
	}

	/** The XML DTD language.

		@return "The XML DTD language"
	*/
	public static String LANGUAGE_DTD() {
		return LANGUAGE_DTD;
	}

	/** The Java language.

		@return "The Java language"
	*/
	public static String LANGUAGE_JAVA() {
		return LANGUAGE_JAVA;
	}

	/** The XSLT language.

		@return "The XSLT language"
	*/
	public static String LANGUAGE_XSLT() {
		return LANGUAGE_XSLT;
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
		return Strings.formatMessage(LEXEREXCEPTION_MESSAGE_FORMAT, s, context);
	}

	/** A localisation error occurred.

		@param	s	The type of localisation error
		@return		A formatted message describing the localisation error
	*/
	public static String LOCALISATION_ERROR(String s) {
		return Strings.formatMessage(LOCALISATION_ERROR_MESSAGE_FORMAT, s);
	}

	/** A malformed character reference was encountered.

		@param	malformedReference	The malformed reference.
		@return						A formatted message displaying the
									malformed reference.
	*/
	public static String MALFORMED_CHAR_REF(CharSequence malformedReference) {
		return Strings.formatMessage(
			MALFORMED_CHAR_REF_MESSAGE_FORMAT,
			malformedReference
		);
	}

	/** Warning that mandatory options were not set.

		@return The message.
	*/
	public static String MANDATORY_OPTIONS_NOT_SET() {
		return MANDATORY_OPTIONS_NOT_SET;
	}

	/** There's an entry in the messages properties file which has no
		corresponding field in this class.

		@param	entryName	The name of the entry in the messages properties
							file.
		@param	thisClass	The name of this class
		@return				A message formatted according to
							{@link #MESSAGE_EXTRA_BUNDLE_ENTRY_MESSAGE_FORMAT}.
	*/
	public static String MESSAGE_EXTRA_BUNDLE_ENTRY(String entryName, String thisClass) {
		return Strings.formatMessage(
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
	public static String MESSAGE_MISSING(String s) {
		return Strings.formatMessage(MESSAGE_MISSING_MESSAGE_FORMAT, s);
	}

	/** Tell the user which mandatory option is missing.

		@param	optionName	The name of the missing option.
		@return				A message formatted according to {@link
							#MISSING_MANDATORY_OPTION_MESSAGE_FORMAT}.
	*/
	public static String MISSING_MANDATORY_OPTION(String optionName) {
		return Strings.formatMessage(
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
		return Strings.formatMessage(OPTION_DEFAULT_MESSAGE_FORMAT, defaultValue);
	}

	/** The {@link String} printed before the legal options for an {@link
		com.mcdermottroe.exemplar.ui.options.Enum} are enumerated for the user.

		@return The message.
	*/
	public static String OPTION_ENUM_ARGS_HEADER() {
		return OPTION_ENUM_ARGS_HEADER;
	}

	/** A message to inform the user that the option is mandatory.

		@return The message.
	*/
	public static String OPTION_IS_MANDATORY() {
		return OPTION_IS_MANDATORY;
	}

	/** Tell the user what languages an API may be used with.

		@param	language	The language the API may be used with.
		@return				A message formatted according to {@link
							#OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT}.
	*/
	public static String OPTION_LANGUAGE_OF_API(String language) {
		return Strings.formatMessage(
			OPTION_LANGUAGE_OF_API_MESSAGE_FORMAT,
			language
		);
	}

	/** A message to inform the user that an API must be selected when using a
		particular language.

		@return The message.
	*/
	public static String OPTION_LANGUAGE_REQUIRES_API() {
		return OPTION_LANGUAGE_REQUIRES_API;
	}

	/** Initialising options...

		@return The message.
	*/
	public static String OPTIONS_INITIALISING() {
		return OPTIONS_INITIALISING;
	}

	/** The user attempted to use an option which doesn't exist.

		@param optionName	The name of the ficticious option the user
							attempted to use.
		@return				A message formatted according to {@link
							#OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT}.
	*/
	public static String OPTIONS_NO_SUCH_OPTION(String optionName) {
		return Strings.formatMessage(
			OPTIONS_NO_SUCH_OPTION_MESSAGE_FORMAT,
			optionName
		);
	}

	/** Someone passed a null doctype to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.

		@return The message.
	*/
	public static String SOURCE_GENERATOR_DOCTYPE_NULL() {
		return SOURCE_GENERATOR_DOCTYPE_NULL;
	}

	/** Someone passed a null language to {@link
		com.mcdermottroe.exemplar.output.OutputUtils#generateParser(
		com.mcdermottroe.exemplar.model.XMLDocumentType,
		String, String, String)}.

		@return	The message.
	*/
	public static String SOURCE_GENERATOR_LANGUAGE_NULL() {
		return SOURCE_GENERATOR_LANGUAGE_NULL;
	}

	/** A parser generator threw an exception.

		@return The error message.
	*/
	public static String SOURCE_GENERATOR_THREW_EXCEPTION() {
		return SOURCE_GENERATOR_THREW_EXCEPTION;
	}

	/** Progress message "Done".

		@return	The message.
	*/
	public static String UI_PROGRESS_DONE() {
		return UI_PROGRESS_DONE;
	}

	/** Progress message in the UI telling the user that the program failed to
		create any output.

		@return	The message
	*/
	public static String UI_PROGRESS_FAILED_TO_CREATE_OUTPUT() {
		return UI_PROGRESS_FAILED_TO_CREATE_OUTPUT;
	}

	/** Progress message telling the user that the program has completed and
		provides the amount of time that the program took.

		@param	time	The amount of time taken in milliseconds.
		@return			A message formatted according to {@link
						#UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT}.
	*/
	public static String UI_PROGRESS_FINISHED_TIME(double time) {
		return Strings.formatMessage(
			UI_PROGRESS_FINISHED_TIME_MESSAGE_FORMAT,
			time
		);
	}

	/** Progress message in the UI telling the user that the output parser is
		being generated.

		@return The message.
	*/
	public static String UI_PROGRESS_GENERATING_PARSER() {
		return UI_PROGRESS_GENERATING_PARSER;
	}

	/** Progress message in the UI telling the user that the parsing of the
		input file failed.

		@return	The message.
	*/
	public static String UI_PROGRESS_INPUT_PARSE_FAILED() {
		return UI_PROGRESS_INPUT_PARSE_FAILED;
	}

	/** Progress message in the UI telling the user that the program is
		currently processing the options.

		@return The message.
	*/
	public static String UI_PROGRESS_OPTIONS() {
		return UI_PROGRESS_OPTIONS;	
	}

	/** Progress message telling the user that the program is parsing the
		input.

		@param	inputFileName	The name of the input file.
		@return					A message formatted according to {@link
								#UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT}
	*/
	public static String UI_PROGRESS_PARSING_INPUT(String inputFileName) {
		return Strings.formatMessage(
			UI_PROGRESS_PARSING_INPUT_MESSAGE_FORMAT,
			inputFileName
		);
	}

	/** Unreachable code has been reached.

		@return A message to that effect.
	*/
	public static String UNREACHABLE_CODE_REACHED() {
		return UNREACHABLE_CODE_REACHED;
	}

	/** Unsupported input type.

		@return The message. 
	*/
	public static String UNSUPPORTED_INPUT_TYPE() {
		return UNSUPPORTED_INPUT_TYPE;
	}

	/** Unterminated character reference.

		@return The message.
	*/
	public static String UNTERMINATED_REF() {
		return UNTERMINATED_REF;
	}

	/** Attribute list without an element.

		@param	attlist	The attribute list that has no corresponding element.
		@return			A formatted error message formatted according to {@link
						#XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT}.
	*/
	public static String XMLDOCTYPE_ORPHAN_ATTLIST(String attlist) {
		return Strings.formatMessage(XMLDOCTYPE_ORPHAN_ATTLIST_MESSAGE_FORMAT, attlist);
	}

	/** Unsupported feature.

		@return The message.
	*/
	public static String XMLDOCTYPE_UNSUPPORTED_FEATURE() {
		return XMLDOCTYPE_UNSUPPORTED_FEATURE;
	}

	/** Forbidden {@link com.mcdermottroe.exemplar.model.XMLObject} in the list
		of markup declarations.

		@param	xmlObject	The forbidden {@link
							com.mcdermottroe.exemplar.model.XMLObject}.
		@return				An error message formatted according to {@link
							#XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT}.
	*/
	public static String XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS(String xmlObject) {
		return Strings.formatMessage(XMLDOCTYPE_XMLOBJECT_IN_MARKUPDECLS_MESSAGE_FORMAT, xmlObject);
	}

	/** {@link com.mcdermottroe.exemplar.model.XMLObject} not configured.

		@return An error message.
	*/
	public static String XMLOBJECT_NOT_CONFIGURED() {
		return XMLOBJECT_NOT_CONFIGURED;
	}

	/** {@link com.mcdermottroe.exemplar.output.XMLParserSourceGenerator}
		failed to load a code fragment.

	 	@return The message.
	*/
	public static String XMLPARSER_LOAD_CODE_FRAGMENT_FAILED() {
		return XMLPARSER_LOAD_CODE_FRAGMENT_FAILED;
	}

	/** Set the appropriate options to ensure that the program is set up for
		the current locale and read the messages from the appropriate
		properties file.

		@throws MessageException	if anything goes wrong while loading the
									messages from the backing store.
	*/
	public static void localise()
	throws MessageException
	{
		// Load the resource bundle with all the messages in it.
		Map<String, String> messages = Resources.get(Message.class);

		// Get the "static" messages
		MESSAGE_MISSING_MESSAGE_FORMAT = messages.get("MESSAGE_MISSING_MESSAGE_FORMAT");
		if (MESSAGE_MISSING_MESSAGE_FORMAT == null) {
			throw new MessageException(COULD_NOT_LOAD_MESSAGE_MISSING);
		}
		String localisationErrorMessageFormat = "LOCALISATION_ERROR_MESSAGE_FORMAT";
			LOCALISATION_ERROR_MESSAGE_FORMAT = messages.get(localisationErrorMessageFormat);
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
				String message = messages.get(fieldName);

				// Set the value of the field
				if (message != null) {
					field.set(null, message);
				} else {
					throw new MessageException(MESSAGE_MISSING(fieldName));
				}
			}
		} catch (IllegalAccessException e) {
			throw new MessageException(
				LOCALISATION_ERROR(DENIED_ACCESS_TO_MESSAGE_CLASS),
				e
			);
		}

		// Check that all of the messages in the resource bundle
		// have a corresponding member in this class
		for (String entry : messages.keySet()) {
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
