// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006, 2007
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
package com.mcdermottroe.exemplar;

import java.util.regex.Pattern;

/** Holds non-user-visible constant values for the entire program.
	The constants are organized into several inner interfaces to keep them
	categorized and to allow some classes to "implement" them in order to
	import the constants into their namespace. This import-by-implement pattern
	should be used sparingly and only where there exists a sub-interface which
	is clearly related to the implementing class.

	@author	Conor McDermottroe
	@since	0.1
*/
public interface Constants {
	/** The name of the program. Where possible all references to the name of
		the program should be made via this. It would be preferable to not have
		to go through the same kind of pains as Phoenix^WFirebird^WFirefox.
	*/
	String PROGRAM_NAME = "exemplar";

	/** The version of the program. This should only be changed when dictated
		by the roadmap. A copy of the roadmap can be found in the root
		directory of the source distribution.
	*/
	String PROGRAM_VERSION = "0.1.1";

	/** A short message about the copyright and license. The years mentioned in
		this message correspond to the range of years for all of the files
		making up the program. Some of the files do not date back to 2003. It is
		an error to modify the contents of this array from code.
	*/
	String[] COPYRIGHT_MESSAGE = {
		"Copyright (c) 2003-2007",
		"Conor McDermottroe. All rights reserved.",
		"See LICENSE and README files for licensing information.",
	};

	/** The package that the program is rooted in. All references to packages
		within the program must be computed from this.
	*/
	String PACKAGE = Constants.class.getPackage().getName();

	/** The system property name for the current working directory. */
	String CWD_PROPERTY = "user.dir";

	/** The current working directory at runtime. */
	String CWD = System.getProperty(CWD_PROPERTY);

	/** The system property name for the end of line marker. */
	String EOL_PROPERTY = "line.separator";

	/** The system end of line marker. */
	String EOL = System.getProperty(EOL_PROPERTY);

	/** The timestamp format used in the templates. A timestamp which includes
		the time might seem more appropriate, but it breaks test code which
		relies on files generated seconds apart to be identical.
	*/
	String TIMESTAMP_FORMAT = "{0,date,full}";

	/** The default message that is displayed if something really screws up in
		the localisation process. This <i>should</i> never be seen by users.
	*/
	String DEFAULT_MESSAGE =	"An error occurred before any diagnostic messages could be loaded.";

	/** A close approximation to infinity. :-) */
	int INFINITY = Integer.MAX_VALUE;

	/** The magic number for calculating hash codes in implementations of
		{@link Object#hashCode()}.
	*/
	int HASHCODE_MAGIC_NUMBER = 29;

	/** The base for the decimal number system. */
	int BASE_DECIMAL = 10;

	/** The base for the hexadecimal number system. */
	int BASE_HEXADECIMAL = 16;

	/** The prefix to a URL that is pointing to a file. */
	String FILE_JAR_PREFIX = "file:";

	/** The prefix to a URL that is pointing to a JAR file. */
	String URL_JAR_PREFIX = "jar:file:";

	/** The META-INF directory inside a JAR file. */
	String JAR_METAINF_DIR = "META-INF";

	/** The {@link String} representation of null. */
	String NULL_STRING = String.valueOf((Object)null);

	/** The number of milliseconds in a second. */
	double MILLISECONDS_IN_A_SECOND = 1000.0;

	/** Character literal constants. */
	interface Character {
		/** The ampersand character '&', '\u0026'. */
		char AMPERSAND = '&';

		/** The carriage return character '\r', '\u000d'. */
		char CARRIAGE_RETURN = '\r';

		/** The colon character ':', '\u003a'. */
		char COLON = ':';

		/** The comma character ',', '\u002c'. */
		char COMMA = ',';

		/** The dollar character '$', '\u0024'. */
		char DOLLAR = '$';

		/** The double quote character '"', '\u0022'. */
		char DOUBLE_QUOTE = '"';

		/** The equals character '=', '\u003d'. */
		char EQUALS = '=';

		/** The exclamation mar character '!', '\u0021'. */
		char EXCLAMATION_MARK = '!';

		/** The full stop (period, dot) character '.', '\u002e'. */
		char FULL_STOP = '.';

		/** The hash (octothorpe, pound) character '#', '\u0023'. */
		char HASH = '#';

		/** The opening brace (curly brace, curly bracket) character '{',
			'\u007b'.
		*/
		char LEFT_CURLY = '{';

		/** The opening parenthesis (round bracket) character '(', '\u0028'. */
		char LEFT_PAREN = '(';

		/** The minus character '-', '\u002d'. */
		char MINUS = '-';

		/** The new line character '\n', '\u000a'. */
		char NEW_LINE = '\n';

		/** The null character '\0', '\u0000'. */
		char NULL = '\0';

		/** The percent character '%', '\u0025'. */
		char PERCENT = '%';

		/** The pipe character '|', '\u007c'. */
		char PIPE = '|';

		/** The plus character '+', '\u002b'. */
		char PLUS = '+';

		/** The question mark character '?', '\u003f'. */
		char QUESTION_MARK = '?';

		/** The closing brace (curly brace, curly bracket) character '}',
			'\u007d'.
		*/
		char RIGHT_CURLY = '}';

		/** The closing parenthesis (round bracket) character ')', '\u0029'. */
		char RIGHT_PAREN = ')';

		/** The semi-colon character ';', '\u003b'. */
		char SEMI_COLON = ';';

		/** The slash character '/', '\u002f'. */
		char SLASH = '/';

		/** The space character ' ', '\u0020'. */
		char SPACE = ' ';

		/** The asterisk (star) character '*', '\u002a'. */
		char STAR = '*';

		/** The tab character '\t', '\u0009'. */
		char TAB = '\t';

		/** The underscore character '_', '\u005f'. */
		char UNDERSCORE = '_';
	}

	/** Format strings. */
	interface Format {
		/** The Java Unicode character escape. */
		String UNICODE = "\\u%04X";

		/** Format strings used in code output. */
		interface Code {
			/** Java code. */
			interface Java {
				/** A Java package statement. */
				String PACKAGE = "package %s;";
			}

			/** XML markup. */
			interface XML {
				/** An XML character reference. */
				String CHAR_ESCAPE = "&#x%04X;";
			}
		}

		/** Format strings to construct file names. */
		interface Filenames {
			/** A Java file name. */
			String JAVA = "%s.java";

			/** A Java parser file name. */
			String JAVA_PARSER = "%sParser.java";

			/** A JFlex file name. */
			String JFLEX = "%s.jflex";
		}

		/** Format strings for use in UIs. */
		interface UI {
			/** The format of a command line option. */
			String OPTION = "--%s";
		}
	}

	/** Input module constants. */
	interface Input {
		/** The class name that drives all input modules. The class must
			implement {@link com.mcdermottroe.exemplar.input.InputModule}.
		*/
		String CLASS = "Parser";

		/** The package under which all input modules reside. */
		String PACKAGE = Constants.PACKAGE + ".input";
	}

	/** Options constants. */
	interface Options {
		/** The option type that takes one value. */
		String ARGUMENT = "Argument";

		/** The option type that takes one or more values from a fixed set. */
		String ENUM = "Enum";

		/** The option type that takes no value; its presence or absence sets
			its value to true or false.
		*/
		String SWITCH = "Switch";

		/** The label for the 'type' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String TYPE_PROPERTY = "type";

		/** The label for the 'description' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String DESCRIPTION_PROPERTY = "description";

		/** The label for the 'value' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String VALUE_PROPERTY = "value";

		/** The label for the 'default' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String DEFAULT_PROPERTY = "default";

		/** The label for the 'mandatory' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String MANDATORY_PROPERTY = "mandatory";

		/** The label for the 'multivalue' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String MULTIVALUE_PROPERTY = "multivalue";

		/** The label for the 'casesensitive' property of an option in the
			PropertyResourceBundle which declares the options.
		*/
		String CASESENSITIVE_PROPERTY = "casesensitive";
	}

	/** Output module constants. */
	interface Output {
		/** The name of the code-generating class in an output module. The
			class must implement {@link
			com.mcdermottroe.exemplar.output.XMLParserGenerator}.
		*/
		String CLASS = "Generator";

		/** The name of the package in which all output modules reside. */
		String PACKAGE = Constants.PACKAGE + ".output";

		/** The extension that all code fragments resource bundle files must
			have.
		*/
		String CODE_FRAGMENTS_FILE_NAME = ".codeFragments";

		/** DTD output module constants. */
		interface DTD {
			/** A message format string for the output DTD file name. */
			String FILE_FMT = "{0}.dtd";
		}

		/** Java output module constants. */
		interface Java {
			/** The name of the Java (Ant) build file. */
			String BUILD_FILE = "build.xml";

			/** The name of the PropertyResourceBundle file which contains the
				entity definitions.
			*/
			String ENTITIES_FILE = "entities.properties";

			/** The internal buffer size for the parser. */
			int BUFFER_SIZE = 512;
		}

		/** XSLT output module constants. */
		interface XSLT {
			/** A message format string for the XSLT stylesheet file name. */
			String FILE_FMT = "{0}.xsl";
		}
	}

	/** Regular expression constants. The dialect of regular expressions used is
		the one described in {@link Pattern}.
	*/
	interface Regex {
		/** A regex that matches nothing but 1 or more digits. */
		String DIGITS = "^\\d+$";

		/** A regex that matches valid keys in the exit status resource file. */
		String EXIT_STATUS_MNEMONIC = "^[A-Z0-9][A-Z0-9_]+[A-Z0-9]$";

		/** A regex matching valid parameter entity names. */
		Pattern VALID_PE_NAME = Pattern.compile("^[A-Za-z0-9\\._:-]+$"); // NON-NLS
	}

	/** Constants for UI. */
	interface UI {
		/** An English message to be used where we can't even load a message
			in the native tongue which complains that a basic message is missing
			from the localisation properties file.
		*/
		String COULD_NOT_LOAD_MESSAGE_MISSING = "Localisation error: could not load MESSAGE_MISSING_MESSAGE_FORMAT";

		/** A message to display if we've been denied access to the Message
			class which holds all of the localised messages.
		*/
		String DENIED_ACCESS_TO_MESSAGE_CLASS = "Denied access to the Message class.";

		/** The standard indent in the UI is 4 space characters. Spaces are used
			in lieu of tabs as tab characters are mangled by many terminals.
		*/
		String INDENT = "    ";

		/** Ant UI constants. */
		interface Ant {
			/** The prefix for all setter methods in the Ant Task UI. */
			String SETTER_PREFIX = "set";
		}

		/** CLI UI constants. */
		interface CLI {
			/** The width of a "standard" terminal. One day there may be smart
				terminal width detection code, but for now it is assumed that
				all users of the CLI interface have a terminal 80 characters
				wide or wider.
			*/
			int DEFAULT_TERMINAL_WIDTH = 80;

			/** The mnemonic for use with {@link
				com.mcdermottroe.exemplar.ui.cli.ExitStatus} to indicate that
				the program has terminated without encountering any error.
			*/
			String EXIT_SUCCESS = "EXIT_SUCCESS";

			/** The mnemonic for use with {@link
				com.mcdermottroe.exemplar.ui.cli.ExitStatus} to indicate that
				the program has terminated due to an error in the localisation
				(L10N) phase.
			*/
			String EXIT_FAIL_L10N = "EXIT_FAIL_L10N";

			/** The mnemonic for use with {@link
				com.mcdermottroe.exemplar.ui.cli.ExitStatus} to indicate that
				the program has terminated due to an error encountered while
				processing the command line arguments.
			*/
			String EXIT_FAIL_ARGS = "EXIT_FAIL_ARGS";

			/** The mnemonic for use with {@link
				com.mcdermottroe.exemplar.ui.cli.ExitStatus} to indicate that
				the program has terminated due to an error encountered while
				processing the input.
			*/
			String EXIT_FAIL_INPUT = "EXIT_FAIL_INPUT";

			/** The mnemonic for use with {@link
				com.mcdermottroe.exemplar.ui.cli.ExitStatus} to indicate that
				the program has terminated due to an error encountered while
				generating the output.
			 */
			String EXIT_FAIL_CODE_GEN = "EXIT_FAIL_CODE_GEN";

			/** The key for the string that is displayed as the argument to an
				{@link com.mcdermottroe.exemplar.ui.options.Argument}
				description.
			*/
			String ARG_ARG = "argArg";

			/** The key for the string that is displayed as the argument to an
				{@link com.mcdermottroe.exemplar.ui.options.Enum}
				description.
			*/
			String ENUM_ARG = "enumArg";

			/** The key for the string that describe the options. */
			String OPTIONS_LINE = "optionsLine";

			/** The key for the message format that makes up the usage line. */
			String USAGE_LINE_MSG_FMT = "usageLineMessageFormat";

			/** The name of the option which produces the usage message. */
			String HELP_OPTION_NAME = "help";

			/** The name of the option which causes the program to be more
				verbose.
			*/
			String VERBOSE_OPTION_NAME = "verbose";

			/** The name of the option which produces the version message. */
			String VERSION_OPTION_NAME = "version";

			/** The prefix for all command line options. The UNIX convention is
				used here. Perhaps in future there can be some platform
				checking magic to change this to the conventional form for the
				local platform? Would this break cross-platform wrappers?
			*/
			String OPTION_PREFIX = "--";
		}
	}

	/** Constants for XML external identifiers. */
	interface XMLExternalIdentifier {
		/** The fixed string "PUBLIC" used in the description of XML
			external identifiers.
		*/
		String PUBLIC = "PUBLIC";

		/** The fixed string "SYSTEM" used in the description of XML
			external identifiers.
		*/
		String SYSTEM = "SYSTEM";

		/** The fixed string "NDATA" used in the description of XML
			external identifiers.
		*/
		String NDATA = "NDATA";
	}
}
