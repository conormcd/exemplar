// vim:filetype=java:ts=4
/*
	Copyright (c) 2003-2007
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
import java.util.logging.ConsoleHandler;
import java.util.logging.Formatter;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.input.InputException;
import com.mcdermottroe.exemplar.input.InputUtils;
import com.mcdermottroe.exemplar.input.ParserException;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.output.OutputException;
import com.mcdermottroe.exemplar.output.OutputUtils;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.MessageException;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.MINUS;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.Character.TAB;
import static com.mcdermottroe.exemplar.Constants.EOL;
import static com.mcdermottroe.exemplar.Constants.Format.UI.OPTION;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_NAME;
import static com.mcdermottroe.exemplar.Constants.PROGRAM_VERSION;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.ARG_ARG;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.DEFAULT_TERMINAL_WIDTH;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.ENUM_ARG;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.EXIT_FAIL_ARGS;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.EXIT_FAIL_CODE_GEN;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.EXIT_FAIL_INPUT;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.EXIT_FAIL_L10N;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.EXIT_SUCCESS;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.HELP_OPTION_NAME;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.OPTIONS_LINE;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.OPTION_PREFIX;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.USAGE_LINE_MSG_FMT;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.VERBOSE_OPTION_NAME;
import static com.mcdermottroe.exemplar.Constants.UI.CLI.VERSION_OPTION_NAME;
import static com.mcdermottroe.exemplar.Constants.UI.INDENT;

/** A class to provide a main entry point for the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Main {
	/** The time that {@link #main(String[])} was called. */
	private static long startTime = 0L;

	/** Private constructor to prevent instantiation of this class. */
	private Main() {
		DBC.UNREACHABLE_CODE();
	}

	/** The main entry point for the program.

		@param	args	The arguments passed to the program
	*/
	public static void main(String[] args) {
		// Record the start time, so we can report on how long the entire
		// process took.
		startTime = System.currentTimeMillis();

		// Make a log handler to handle error and status output
		Handler consoleHandler = new ConsoleHandler();
		consoleHandler.setFormatter(new ConsoleFormatter());
		Log.registerHandler(consoleHandler);
		// CLI apps should be quiet unless something goes wrong.
		Log.setLevel(Log.LogLevel.WARNING);

		// Print a copyright header
		for (String line : Message.COPYRIGHT().split(EOL)) {
			Log.info(line);
		}
		Log.info("");

		// Localise the program if possible
		try {
			Message.localise();
		} catch (MessageException e) {
			abort(ExitStatus.getExitCode(EXIT_FAIL_L10N), e);
		}

		// Process the arguments
		Log.debug(Message.UI_PROGRESS_OPTIONS());
		for (int i = 0; i < args.length; i++) {
			String helpOption = String.format(OPTION, HELP_OPTION_NAME);
			String verboseOption = String.format(OPTION, VERBOSE_OPTION_NAME);
			String versionOption = String.format(OPTION, VERSION_OPTION_NAME);
			if (args[i].equals(helpOption)) {
				cleanExit(usageMessage());
			} else if (args[i].equals(verboseOption)) {
				Log.setLevel(Log.LogLevel.INFO);
			} else if (args[i].equals(versionOption)) {
				cleanExit(versionMessage());
			} else if (args[i].startsWith(OPTION_PREFIX)) {
				String argName = args[i].substring(OPTION_PREFIX.length());

				if (!Options.isLegal(argName)) {
					abort(
						ExitStatus.getExitCode(EXIT_FAIL_ARGS),
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
				abort(
						ExitStatus.getExitCode(EXIT_FAIL_ARGS),
						Message.OPTIONS_NO_SUCH_OPTION(args[i])
				);
			}
		}
		Log.debug(Message.UI_PROGRESS_DONE());
		Options.setUIFinished();

		// Make sure that all of the mandatory options were set.
		if (!Options.allMandatoryOptionsSet()) {
			abort(
					ExitStatus.getExitCode(EXIT_FAIL_ARGS),
					Message.MANDATORY_OPTIONS_NOT_SET()
			);
		}

		// The internal description of the XML document type
		XMLDocumentType doctype = null;

		// Parse the input
		try {
			Log.info(
				Message.UI_PROGRESS_PARSING_INPUT(
					Options.getString("input") // NON-NLS
				)
			);
			doctype =	InputUtils.parse(
							Options.getString("input"),		// NON-NLS
							Options.getString("input-type")	// NON-NLS
						);
		} catch (InputException e) {
			abort(ExitStatus.getExitCode(EXIT_FAIL_INPUT), e);
		} catch (ParserException e) {
			abort(ExitStatus.getExitCode(EXIT_FAIL_INPUT), e);
		}

		// Create the output
		try {
			Log.info(Message.UI_PROGRESS_GENERATING_PARSER());
			OutputUtils.generateParser(
				doctype,
				Options.getString("output"),			// NON-NLS
				Options.getString("output-language"),	// NON-NLS
				Options.getString("output-api")			// NON-NLS
			);
		} catch (OutputException e) {
			abort(ExitStatus.getExitCode(EXIT_FAIL_CODE_GEN), e);
		}

		// Indicate success
		cleanExit(null);
	}

	/** Display a version message.

		@return	The version of the program, unadorned.
	*/
	private static String versionMessage() {
		return PROGRAM_VERSION;
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
		StringBuilder usage = new StringBuilder();

		// Output any diagnostic message before doing anything else.
		if (why.length() > 0) {
			usage.append(why.trim());
			usage.append(EOL);
			usage.append(EOL);
		}

		// Output the name of the program and the copyright message
		usage.append(Message.COPYRIGHT());
		usage.append(EOL);

		// Now describe the usage of the program
		usage.append(
			Strings.formatMessage(usageLineMessageFormat, PROGRAM_NAME)
		);

		// Find the column that option descriptions start in.
		int optionDescColumn = 0;
		for (String optionName : Options.getAllOptionNames()) {
			int length =	INDENT.length() +
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
				Iterator<String> optionNames = Options.optionNameIterator();
				optionNames.hasNext();
			)
		{
			String optionName = optionNames.next();

			// Print the option and any arguments it may take.
			usage.append(Strings.indent(String.format(OPTION, optionName)));
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
			int length =	INDENT.length() +
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
			StringBuilder descColumnIndent;
			descColumnIndent = new StringBuilder(optionDescColumn);
			for (int j = 0; j < optionDescColumn; j++) {
				descColumnIndent.append(SPACE);
			}

			// Pad out the space between the end of the option name and the
			// description.
			for (int j = 0; j <= optionDescColumn - length; j++) {
				usage.append(SPACE);
			}

			// Add the description of the option
			usage.append(
				Strings.wrap(
					Options.getDescription(optionName),
					DEFAULT_TERMINAL_WIDTH,
					optionDescColumn
				)
			);
			usage.append(EOL);

			// If the option is an Enum, print the legal options and their
			// descriptions.
			if (Options.isEnum(optionName)) {
				usage.append(EOL);
				usage.append(descColumnIndent);
				usage.append(
					Strings.wrap(
						Message.OPTION_ENUM_ARGS_HEADER(),
						DEFAULT_TERMINAL_WIDTH,
						optionDescColumn
					)
				);
				usage.append(EOL);

				// Get the set of potential Enum values
				Map<String, String> enumOptions;
				enumOptions = new TreeMap<String, String>(
					Options.getEnumDescriptions(optionName)
				);

				// Find the length of the longest Enum value.
				int longestEnumVal = 0;
				for (String enumOption : enumOptions.keySet()) {
					int len = enumOption.length();
					if (len > longestEnumVal) {
						longestEnumVal = len;
					}
				}

				// Print out all potential Enum values and their descriptions.
				for (String enumValue : enumOptions.keySet()) {
					usage.append(descColumnIndent);
					usage.append(enumValue);
					for
						(
							int j = 0;
							j <= longestEnumVal - enumValue.length();
							j++
						)
					{
						usage.append(SPACE);
					}
					usage.append(MINUS);
					usage.append(SPACE);
					usage.append(
						Strings.wrap(
							enumOptions.get(enumValue),
							DEFAULT_TERMINAL_WIDTH,
							optionDescColumn + longestEnumVal + 3
						)
					);
					usage.append(EOL);
				}
			}

			// Tell the user if the option is mandatory
			if (Options.isMandatory(optionName)) {
				usage.append(EOL);
				usage.append(descColumnIndent);
				usage.append(
					Strings.wrap(
						Message.OPTION_IS_MANDATORY(),
						DEFAULT_TERMINAL_WIDTH,
						optionDescColumn
					)
				);
				usage.append(EOL);
			}

			// If the option has a default, describe it now
			String defaultValue = Options.describeDefault(optionName);
			if (defaultValue != null && !Options.isSwitch(optionName)) {
				usage.append(EOL);
				usage.append(descColumnIndent);
				usage.append(
					Strings.wrap(
						Message.OPTION_DEFAULT(defaultValue),
						DEFAULT_TERMINAL_WIDTH,
						optionDescColumn
					)
				);
				usage.append(EOL);
			}

			// Put a blank line between option descriptions.
			usage.append(EOL);
		}

		return usage.toString();
	}

	/** Shorthand for a clean exit. The program will exit with the exit code
		corresponding to {@link
		com.mcdermottroe.exemplar.Constants.UI.CLI#EXIT_SUCCESS}.

		@param	message	A message to send to the user. If this is null, nothing
						is sent.
	*/
	private static void cleanExit(Object message) {
		if (!Options.isInitialised()) {
			Options.setUIFinished();
		}
		if (message != null) {
			Log.LogLevel oldLevel = Log.getLevel();
			Log.setLevel(Log.LogLevel.INFO);
			Log.info(message);
			Log.setLevel(oldLevel);
		}
		Log.info(
			Message.UI_PROGRESS_FINISHED_TIME(
				(double)(System.currentTimeMillis() - startTime) / 1000.0
			)
		);
		System.exit(ExitStatus.getExitCode(EXIT_SUCCESS));
	}

	/** Shorthand for an unclean exit.

		@param	code	The value to pass to {@link System#exit(int)}.
		@param	message	An optional message to log (only logged if non-null).
	*/
	private static void abort(int code, Object message) {
		if (!Options.isInitialised()) {
			Options.setUIFinished();
		}
		if (message != null) {
			Log.error(message);
		}
		Log.debug("Aborting, code " + code);
		Log.info(
			Message.UI_PROGRESS_FINISHED_TIME(
				(double)(System.currentTimeMillis() - startTime) / 1000.0
			)
		);
		System.exit(code);
	}

	/** A {@link Formatter} for messages which are output to the console. 

		@author Conor McDermottroe
		@since	0.2
	*/
	private static class ConsoleFormatter
	extends Formatter
	{
		/** Format a log record so that it can be sent to the console.

			@param	record	The {@link LogRecord} to format for the console.
			@return			A {@link String} suitable for the console.
		*/
		@Override public String format(LogRecord record) {
			if (record == null) {
				return "";
			}

			// Create the buffer for the message with the raw message itself.
			StringBuilder message = new StringBuilder(
				String.valueOf(record.getMessage())
			);

			// If debugging is turned on, add some extra info
			if (Options.isDebugSet()) {
				message.insert(0, SPACE);
				message.insert(
					0,
					Message.DEBUG_CLASS_AND_METHOD(
						record.getSourceClassName(),
						record.getSourceMethodName()
					)
				);
			}

			// Now add the exception, if any
			Throwable cause = record.getThrown();
			if (cause != null) {
				message.append(EOL);
				message.append(TAB);
				message.append("Exception: ");
				message.append(cause);
			}

			String messageAsString = Strings.trimTrailingSpace(message);
			if (!messageAsString.endsWith(EOL)) {
				message = new StringBuilder(messageAsString);
				message.append(EOL);
				return message.toString();
			} else {
				return messageAsString;
			}
		}
	}
}
