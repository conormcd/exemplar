// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006
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
package com.mcdermottroe.exemplar.ui.ant;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import org.apache.tools.ant.BuildException;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Exception;
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

/** The Ant task "UI" for Exemplar.

	@author	Conor McDermottroe
	@since	0.1
*/
public class Task
extends org.apache.tools.ant.Task
implements Constants.UI.Ant
{
	/** Any errors encountered will be added to this list and this list will be
		flushed periodically.
	*/
	private List<String> errors;

	/** Create the task. This constructor may not throw a {@link
		BuildException}, so in order to catch errors the error message is added
		to the {@link #errors} private member {@link List} and let the {@link
		#execute()} method handle the actual throwing of the error.
	*/
	public Task() {
		super();

		// Make a logger which will get picked up by the rest of the program.
		Log.registerHandler(
			new Handler() {
				@Override public void close() {}
				@Override public void flush() {}
				@Override public void publish(LogRecord record) {
					log(record.getMessage());
				}
			}
		);

		// Make a new list for the errors
		errors = new ArrayList<String>();

		// Localise the program, if possible.
		try {
			Message.localise();
		} catch (MessageException e) {
			errors.add(Message.ANT_LOCALISATION_ERROR);
			for (String traceElement : e.getBackTrace()) {
				errors.add(Constants.UI.INDENT + traceElement);
			}
			return;
		}

		// Ensure that all of the options have a method and all of the methods
		// here correspond to an option. This must be done to ensure that the
		// options definition file and this Task are in sync. Unfortuantely,
		// Ant is not smart enough to allow dynamically defined tasks.
		//
		// This chunk of code is designed to force the Ant Task and the options
		// definition to stay in sync and to ensure that the task fails early
		// if they are not in sync.
		//
		// The basic algorithm is as follows:
		//
		// 1) Get the Set of options defined in the master options definition.
		// 2) For each method declared in this class of the form "set*", if the
		//    method name is in the set, remove it, if not fail on the "extra
		//    method" failing case.
		// 3) After 2) has completed, if the set is non-empty, then all of the
		//    method names in the set correspond to "missing methods"
		Method[] methods = getClass().getDeclaredMethods();
		Set<String> defOptions = Options.getAllOptionNames();
		if (methods != null) {
			for (Method method : methods) {
				String methodName = method.getName();
				if (methodName.startsWith(SETTER_PREFIX)) {
					String optionName = methodNameToOptionName(methodName);
					boolean foundMethod = false;
					for	(
							Iterator<String> it = defOptions.iterator();
							it.hasNext();
						)
					{
						String definedOption = it.next();
						if (definedOption.equals(optionName)) {
							it.remove();
							foundMethod = true;
							break;
						}
					}
					if (!foundMethod) {
						errors.add(Message.ANT_EXTRA_METHOD(methodName));
					}
				}
			}
			for (Object defOption : defOptions) {
				errors.add(
					Message.ANT_MISSING_METHOD(
						optionNameToMethodName(defOption.toString())
					)
				);
			}
		} else {
			DBC.UNREACHABLE_CODE();
		}
	}

	/** The execute method required by the Task. */
	@Override public void execute() {
		super.execute();

		// All of the options should be set by now.
		Options.setUIFinished();

		// Record the start time
		long startTime = System.currentTimeMillis();

		// Ant needs a non-empty String to make a blank line - lame.
		String blankLine = Character.toString(Constants.Character.SPACE);

		// Print a header for the build
		Log.info(
			Constants.PROGRAM_NAME +
			Constants.Character.SPACE +
			Constants.PROGRAM_VERSION
		);
		for (String copyrightLine : Constants.COPYRIGHT_MESSAGE) {
			Log.info(copyrightLine);
		}
		Log.info(blankLine);

		// Fail the build if there were any errors thrown during the setup of
		// the Task
		if (!errors.isEmpty()) {
			for (String error : errors) {
				Log.error(error);
			}
			throw new BuildException("");
		}

		// Make sure that the mandatory options have been set
		if (!Options.allMandatoryOptionsSet()) {
			die(Message.MANDATORY_OPTIONS_NOT_SET);
		}

		// Get all of the options
		Log.info(Message.UI_PROGRESS_OPTIONS);
		String inputType = Options.getString("input-type");
		DBC.ASSERT(inputType != null);
		String input = Options.getString("input");
		DBC.ASSERT(input != null);
		String outputApi = Options.getString("output-api");
		String outputLanguage = Options.getString("output-language");
		DBC.ASSERT(outputLanguage != null);
		String output = Options.getString("output");
		DBC.ASSERT(output != null);

		// Parse the input
		Log.info(Message.UI_PROGRESS_PARSING_INPUT(input));
		XMLDocumentType doctype = null;
		try {
			doctype = InputUtils.parse(input, inputType);
		} catch (InputException e) {
			die(Message.UI_PROGRESS_INPUT_PARSE_FAILED, e);
		} catch (ParserException e) {
			die(Message.UI_PROGRESS_INPUT_PARSE_FAILED, e);
		}

		// Generate the output
		Log.info(Message.UI_PROGRESS_GENERATING_PARSER);
		try {
			OutputUtils.generateParser(
				doctype,
				output,
				outputLanguage,
				outputApi
			);
		} catch (OutputException e) {
			die(Message.UI_PROGRESS_FAILED_TO_CREATE_OUTPUT, e);
		}
		Log.info(Message.UI_PROGRESS_DONE);

		// Record the end time and generate the String number of seconds this
		// task took.
		long endTime = System.currentTimeMillis();
		double elapsedTime = (double)(endTime - startTime) / 1000.0;

		// Say that it's finished
		Log.info(blankLine);
		Log.info(Message.UI_PROGRESS_FINISHED_TIME(elapsedTime));
	}

	/** Setter for the debug attribute of the task.

		@param	debug	Whether or not debugging is turned on.
	*/
	public static void setDebug(String debug) {
		optionSetHelper(debug);
	}

	/** Setter for the exclude attribute of the task.

		@param	exclude	This is the string value of the exclude attribute.
	*/
	public static void setExclude(String exclude) {
		optionSetHelper(exclude);
	}

	/** Setter for the extra_plugin_packages attribute of the task.

		@param	extraPluginPackages	This is the string value of the
									extra_plugin_package attribute.
	*/
	public static void setExtra_plugin_packages(String extraPluginPackages) {
		optionSetHelper(extraPluginPackages);
	}

	/** Setter for the help attribute of the task.

		@param	help	This is the string value of the help attribute.
	*/
	public static void setHelp(String help) {
		optionSetHelper(help);
	}

	/** Setter for the include attribute of the task.

		@param	include	This is the string value of the include attribute.
	*/
	public static void setInclude(String include) {
		optionSetHelper(include);
	}

	/** Setter for the input attribute of the task.

		@param	input	This is the string value of the input attribute.
	*/
	public static void setInput(String input) {
		optionSetHelper(input);
	}

	/** Setter for the input_type attribute of the task.

		@param	inputType	This is the string value of the input_type
							attribute.
	*/
	public static void setInput_type(String inputType) {
		optionSetHelper(inputType);
	}

	/** Setter for the output attribute of the task.

		@param	output	This is the string value of the output attribute.
	*/
	public static void setOutput(String output) {
		optionSetHelper(output);
	}

	/** Setter for the output_api attribute of the task.

		@param	outputAPI	This is the string value of the output_api
							attribute.
	*/
	public static void setOutput_api(String outputAPI) {
		optionSetHelper(outputAPI);
	}

	/** Setter for the output_language attribute of the task.

		@param	outputLanguage	This is the string value of the output_language
								attribute.
	*/
	public static void setOutput_language(String outputLanguage) {
		optionSetHelper(outputLanguage);
	}

	/** Setter for the output_package attribute of the task.

		@param	outputPackage	This is the string value of the output_package
								attribute.
	*/
	public static void setOutput_package(String outputPackage) {
		optionSetHelper(outputPackage);
	}

	/** Setter for the version attribute of the task.

		@param	version	This is the string value of the version attribute.
	*/
	public static void setVersion(String version) {
		optionSetHelper(version);
	}

	/** Setter for the vocabulary attribute of the task.

		@param	vocabulary	This is the string value of the vocabulary
							attribute.
	*/
	public static void setVocabulary(String vocabulary) {
		optionSetHelper(vocabulary);
	}

	/** Allow for simpler setting of options. The option to set is taken from
		the method which calls this method. The calling method name is presumed
		to begin with "set".

		@param value	The value of the option to set.
	*/
	private static void optionSetHelper(String value) {
		// Get the stack trace for where this has been called from.
		StackTraceElement[] stackTrace = new BuildException().getStackTrace();
		DBC.ASSERT(stackTrace != null);
		if (stackTrace == null) {
			return;
		}
		DBC.ASSERT(stackTrace.length >= 2);
		if (stackTrace.length < 2) {
			return;
		}

		// Find out which option to set.
		String option = methodNameToOptionName(stackTrace[1].getMethodName());

		// Now set the option.
		Options.set(option, value);
	}

	/** Convert an option name into a method name.

		@param	optName	The option name to convert.
		@return			The name of the method used to set <code>optName</code>
	*/
	private static String optionNameToMethodName(String optName) {
		DBC.REQUIRE(optName != null);
		if (optName == null) {
			return null;
		}

		String methodName =	SETTER_PREFIX +
							optName.substring(0, 1).toUpperCase(
								Locale.getDefault()
							) +
							optName.substring(1);
		methodName = methodName.replace(
			Constants.Character.MINUS,
			Constants.Character.UNDERSCORE
		);
		return methodName;
	}

	/** Do the reverse of {@link #optionNameToMethodName(String)}.

		@param	methodName	The name of the method to convert.
		@return				The name of the option set by
							<code>methodName</code>.
		@see 				#optionNameToMethodName(String)
	*/
	private static String methodNameToOptionName(String methodName) {
		DBC.REQUIRE(methodName != null);
		if (methodName == null) {
			return null;
		}
		DBC.REQUIRE(methodName.startsWith(SETTER_PREFIX));

		String optionName = methodName.substring(SETTER_PREFIX.length());
		optionName = optionName.toLowerCase(Locale.getDefault());
		optionName = optionName.replace(
			Constants.Character.UNDERSCORE,
			Constants.Character.MINUS
		);

		return optionName;
	}

	/** Abort the build with a diagnostic message.

		@param	message	A descriptive error message which will be displayed
						when the build fails.
	*/
	private static void die(String message) {
		DBC.REQUIRE(message != null);
		Log.error(message);
		throw new BuildException("");
	}

	/** Abort the build with a diagnostic message, acknowledging the exception
		which caused the failure.

		@param	message	A descriptive error message which will be displayed
						when the build fails.
		@param	e		The {@link Exception} which caused the build to fail.
	*/
	private static void die(String message, Exception e) {
		DBC.REQUIRE(message != null);
		DBC.REQUIRE(e != null);

		Log.error(message, e);
		throw new BuildException("");
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		return o instanceof Task;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		if (errors != null) {
			return errors.hashCode();
		} else {
			return 0;
		}
	}
}
