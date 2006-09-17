// vim:filetype=java:ts=4
/*
	Copyright (c) 2003-2006
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
package com.mcdermottroe.exemplar.ui.cli;

import java.util.Iterator;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.logging.StreamHandler;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Exception;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.input.InputException;
import com.mcdermottroe.exemplar.input.InputUtils;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.ui.LogUtils;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.MessageException;
import com.mcdermottroe.exemplar.ui.Options;

/** A class to provide a main entry point for the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Main
implements Constants.UI.CLI
{
	/** Private constructor to prevent instantiation of this class. */
	private Main() {
		DBC.UNREACHABLE_CODE();
	}

	/** The main entry point for the program.

		@param	args	The arguments passed to the program
	*/
	public static void main(String[] args) {
		// Make a logger to handle error and status output
		LogUtils.setLogHandler(
			new StreamHandler(
				System.out,
				new Formatter() {
					public String format(LogRecord record) {
						return record.getMessage();
					}
				}
			)
		);
		Logger logger = LogUtils.getLogger();

		// Localise the program if possible
		try {
			Message.localise();
		} catch (MessageException e) {
			exit(ExitStatus.getExitCode(EXIT_FAIL_L10N), logger, e);
		}

		// Process the arguments
		for (int i = 0; i < args.length; i++) {
			String helpOption = OPTION_PREFIX + HELP_OPTION_NAME;
			String versionOption = OPTION_PREFIX + VERSION_OPTION_NAME;
			if (args[i].equals(helpOption)) {
				exit(
					ExitStatus.getExitCode(EXIT_SUCCESS),
					logger,
					usageMessage()
				);
			} else if (args[i].equals(versionOption)) {
				exit(
					ExitStatus.getExitCode(EXIT_SUCCESS),
					logger,
					versionMessage()
				);
			} else if (args[i].startsWith(OPTION_PREFIX)) {
				String argName = args[i].substring(OPTION_PREFIX.length());

				if (!Options.isLegal(argName)) {
					exit(
							ExitStatus.getExitCode(EXIT_FAIL_ARGS),
							logger,
							Message.OPTIONS_NO_SUCH_OPTION(args[i])
						);
				}

				if (Options.isSwitch(argName)) {
					Options.set(argName, Boolean.toString(true));
				} else {
					Options.set(argName, args[i+1]);
					i++;
				}
			} else {
				exit(
						ExitStatus.getExitCode(EXIT_FAIL_ARGS),
						logger,
						Message.OPTIONS_NO_SUCH_OPTION(args[i])
					);
			}
		}
		Options.setUIFinished();

		// Make sure that all of the mandatory options were set.
		if (!Options.allMandatoryOptionsSet()) {
			exit(
					ExitStatus.getExitCode(EXIT_FAIL_ARGS),
					logger,
					Message.MANDATORY_OPTIONS_NOT_SET
				);
		}

		// Reset the logger to catch --debug in case it was set.
		logger = LogUtils.getLogger();

		// The internal description of the XML document type
		XMLDocumentType doctype = null;

		// Parse the input
		try {
			doctype =	InputUtils.parse(
							Options.getString("input"),
							Options.getString("input-type")
						);
		} catch (InputException e) {
			exit(ExitStatus.getExitCode(EXIT_FAIL_INPUT), logger, e);
		} catch (ParserException e) {
			exit(ExitStatus.getExitCode(EXIT_FAIL_INPUT), logger, e);
		}

		// Create the output
		try {
			OutputUtils.generateParser(
				doctype,
				Options.getString("output"),
				Options.getString("output-language"),
				Options.getString("output-api")
			);
		} catch (OutputException e) {
			exit(ExitStatus.getExitCode(EXIT_FAIL_CODE_GEN), logger, e);
		}

		// Indicate success
		exit(ExitStatus.getExitCode(EXIT_SUCCESS), null, null);
	}

	/** Display a version message.

		@return	The version of the program, unadorned.
	*/
	private static String versionMessage() {
		return	Constants.PROGRAM_VERSION +
				Constants.EOL;
	}

	/** Format a usage message.

		@return		A String containing the usage message.
	*/
	private static String usageMessage() {
		return usageMessage("");
	}

	/** Format a usage message, optionally displaying why the use is seeing it.

		@param why	The reason why the user is getting a usage message.
		@return		A String containing the usage message.
	*/
	private static String usageMessage(String why) {
		DBC.REQUIRE(why != null);
		if (why == null) {
			return null;
		}

		// Get the ResourceBundle containing all of the MessageFormats for
		// constructing the useage message.
		ResourceBundle usageBits;
		usageBits = ResourceBundle.getBundle(Main.class.getName());

		// Get all of the usageBits.
		String argArg = usageBits.getString(ARG_ARG);
		String enumArg = usageBits.getString(ENUM_ARG);
		String optionsLine = usageBits.getString(OPTIONS_LINE);
		String usageLineMessageFormat = usageBits.getString(USAGE_LINE_MSG_FMT);
		DBC.ASSERT(argArg != null);
		DBC.ASSERT(enumArg != null);
		DBC.ASSERT(optionsLine != null);
		DBC.ASSERT(usageLineMessageFormat != null);
		if	(
				argArg == null ||
				enumArg == null ||
				optionsLine == null ||
				usageLineMessageFormat == null
			)
		{
			return null;
		}

		// The usage message to construct
		StringBuffer usage = new StringBuffer();

		// Output any diagnostic message before doing anything else.
		if (why.length() > 0) {
			usage.append(why.trim());
			usage.append(Constants.EOL);
			usage.append(Constants.EOL);
		}

		// Output the name of the program and the copyright message
		usage.append(Constants.PROGRAM_NAME);
		usage.append(Constants.Character.SPACE);
		usage.append(Constants.PROGRAM_VERSION);
		usage.append(Constants.EOL);
		for (int i = 0; i < Constants.COPYRIGHT_MESSAGE.length; i++) {
			usage.append(Constants.COPYRIGHT_MESSAGE[i]);
			usage.append(Constants.EOL);
		}
		usage.append(Constants.EOL);

		// Now describe the usage of the program
		usage.append(
			Utils.formatMessage(
				usageLineMessageFormat,
				Constants.PROGRAM_NAME
			)
		);

		// Find the column that option descriptions start in.
		int optionDescColumn = 0;
		for (Iterator it = Options.optionNameIterator(); it.hasNext(); ) {
			String optionName = (String)it.next();
			int length =	Constants.UI.INDENT.length() +
							OPTION_PREFIX.length() +
							optionName.length();
			if (Options.isArgument(optionName)) {
				length += argArg.length();
			} else if (Options.isEnum(optionName)) {
				if (Options.isMultiValue(optionName)) {
					length += enumArg.length();
				} else {
					length += argArg.length();
				}
			}
			length++;
			if (length > optionDescColumn) {
				optionDescColumn = length;
			}
		}

		// Enumerate the options
		usage.append(optionsLine);
		for	(
				Iterator optionNames = Options.optionNameIterator();
				optionNames.hasNext();
			)
		{
			String optionName = (String)optionNames.next();

			// Indent the option
			usage.append(Constants.UI.INDENT);

			// Print the option and any arguments it may take.
			usage.append(OPTION_PREFIX).append(optionName);
			if (Options.isArgument(optionName)) {
				usage.append(argArg);
			} else if (Options.isEnum(optionName)) {
				if (Options.isMultiValue(optionName)) {
					usage.append(enumArg);
				} else {
					usage.append(argArg);
				}
			}

			// Find out the length of the option including the indent before it
			// and the arguments to it if any.
			int length =	Constants.UI.INDENT.length() +
							OPTION_PREFIX.length() +
							optionName.length();
			if (Options.isArgument(optionName)) {
				length += argArg.length();
			} else if (Options.isEnum(optionName)) {
				if (Options.isMultiValue(optionName)) {
					length += enumArg.length();
				} else {
					length += argArg.length();
				}
			}
			length++;

			// Construct a string to use to indent things to the column that
			// the description is in.
			StringBuffer descColumnIndent = new StringBuffer(optionDescColumn);
			for (int j = 0; j < optionDescColumn; j++) {
				descColumnIndent.append(Constants.Character.SPACE);
			}

			// Pad out the space between the end of the option name and the
			// description.
			for (int j = 0; j <= optionDescColumn - length; j++) {
				usage.append(Constants.Character.SPACE);
			}

			// Add the description of the option
			usage.append(
					wrapText(
								Options.getDescription(optionName),
								optionDescColumn
							)
			);
			usage.append(Constants.EOL);

			// If the option is an Enum, print the legal options and their
			// descriptions.
			if (Options.isEnum(optionName)) {
				usage.append(Constants.EOL);
				usage.append(descColumnIndent);
				usage.append(
						wrapText(
									Message.OPTION_ENUM_ARGS_HEADER,
									optionDescColumn
								)
				);
				usage.append(Constants.EOL);

				// Get the set of potential Enum values
				Map eo = Options.getEnumDescriptions(optionName);
				DBC.ASSERT(eo != null);
				Map enumOptions = new TreeMap(eo);

				// Find the length of the longest Enum value.
				int longestEnumVal = 0;
				for	(
						Iterator it = enumOptions.keySet().iterator();
						it.hasNext();
					)
				{
					int len = ((CharSequence)it.next()).length();
					if (len > longestEnumVal) {
						longestEnumVal = len;
					}
				}

				// Print out all potential Enum values and their descriptions.
				for	(
						Iterator it = enumOptions.keySet().iterator();
						it.hasNext();
					)
				{
					String enumValue = (String)it.next();
					usage.append(descColumnIndent);
					usage.append(enumValue);
					for
						(
							int j = 0;
							j <= longestEnumVal - enumValue.length();
							j++
						)
					{
						usage.append(Constants.Character.SPACE);
					}
					usage.append(Constants.Character.MINUS);
					usage.append(Constants.Character.SPACE);
					usage.append(
						wrapText(
							(String)enumOptions.get(enumValue),
							optionDescColumn + longestEnumVal + 3
						)
					);
					usage.append(Constants.EOL);
				}
			}

			// Tell the user if the option is mandatory
			if (Options.isMandatory(optionName)) {
				usage.append(Constants.EOL);
				usage.append(descColumnIndent);
				usage.append(
					wrapText(
						Message.OPTION_IS_MANDATORY,
						optionDescColumn
					)
				);
				usage.append(Constants.EOL);
			}

			// If the option has a default, describe it now
			String defaultValue = Options.describeDefault(optionName);
			if (defaultValue != null && !(Options.isSwitch(optionName))) {
				usage.append(Constants.EOL);
				usage.append(descColumnIndent);
				usage.append(
					wrapText(
						Message.OPTION_DEFAULT(defaultValue),
						optionDescColumn
					)
				);
				usage.append(Constants.EOL);
			}

			// Put a blank line between option descriptions.
			usage.append(Constants.EOL);
		}

		return usage.toString();
	}

	/** Wrap and indent a {@link String} to fit a certain terminal width.

		@param	text	The text to wrap.
		@param	indent	How far indented the text is to start with.
		@return			A wrapped and indented copy of the text parameter.
	*/
	private static String wrapText(String text, int indent) {
		DBC.REQUIRE(text != null);
		DBC.REQUIRE(indent >= 0);
		if (text == null || indent < 0) {
			return null;
		}

		// WARNING: If any word is longer than this value then this method will
		// loop infnitely.
		int longestAllowableWord = DEFAULT_TERMINAL_WIDTH - indent;

		// Split the text into words
		String[] words = text.split(Constants.Regex.WHITESPACE);
		DBC.ASSERT(words.length > 0);

		// Start wrapping
		DBC.ASSERT(words[0].length() < longestAllowableWord);
		StringBuffer wrappedText = new StringBuffer(words[0]);
		int cursor = indent + words[0].length();
		for (int i = 1; i < words.length; i++) {
			DBC.ASSERT(words[i].length() < longestAllowableWord);
			if (cursor + words[i].length() + 1 < DEFAULT_TERMINAL_WIDTH) {
				wrappedText.append(Constants.Character.SPACE);
				wrappedText.append(words[i]);
				cursor += words[i].length() + 1;
			} else {
				wrappedText.append(Constants.EOL);
				for (int j = 0; j < indent; j++) {
					wrappedText.append(Constants.Character.SPACE);
				}
				wrappedText.append(words[i]);
				cursor = indent + words[i].length();
			}
		}

		return wrappedText.toString();
	}

	/** Shorthand for exiting the program.

		@param	exitCode	The exit code to return to the calling shell.
		@param	logger		The {@link Logger} to use to output messages. If
							null this will output nothing.
		@param	cause		Either an {@link Exception} or a message,
							describing why the failure occurred. If a failure
							had not occurred then this parameter MUST be null.
	*/
	private static void exit(int exitCode, Logger logger, Object cause) {
		DBC.REQUIRE(exitCode >= 0);
		DBC.REQUIRE(
			logger != null && cause != null ||
			logger == null && cause == null
		);
		DBC.REQUIRE(
			cause == null ||
			cause instanceof String ||
			cause instanceof Exception
		);

		// Exit early if the logger is null
		if (logger == null) {
			System.exit(exitCode);
			return;
		}

		// Make the diagnostic message
		String message = "";
		if (cause != null) {
			if (cause instanceof Exception) {
				message = cause.toString();
			} else if (cause instanceof String) {
				message = (String)cause;
			} else {
				DBC.UNREACHABLE_CODE();
			}
		}

		// If a failure occurs during argument processing, then the usage
		// statement must be printed.
		if (exitCode == ExitStatus.getExitCode(EXIT_FAIL_ARGS)) {
			logger.severe(usageMessage(message));
		} else {
			logger.severe(message);
		}

		// Now exit
		System.exit(exitCode);
	}
}