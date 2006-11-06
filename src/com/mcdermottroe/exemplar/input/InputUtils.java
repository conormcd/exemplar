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
package com.mcdermottroe.exemplar.input;

import java.util.List;
import java.util.Locale;
import java.util.SortedMap;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.Message;

/** Input handling utilities.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class InputUtils {
	/** Private constructor to prevent instantiation of this class. */
	private InputUtils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Locate the input parsing class for a given input module. The returned
		class implements the {@link InputModule} interface.

		@param	name					The name of the input module. The class
										returned will be
										$PACKAGE.input.$name.Parser.
		@return							The Class that can be used to read in
										the XML vocabulary specification.
		@throws	ClassNotFoundException	When the class
										$PACKAGE.input.$name.Parser could not be
										found.
	*/
	private static Class<?> parserClass(String name)
	throws ClassNotFoundException
	{
		DBC.REQUIRE(name != null);
		if (name == null) {
			return null;
		}

		StringBuilder parserClassName = new StringBuilder();
		parserClassName.append(Constants.Input.PACKAGE);
		parserClassName.append(Constants.Character.FULL_STOP);
		parserClassName.append(name.toLowerCase(Locale.getDefault()));
		parserClassName.append(Constants.Character.FULL_STOP);
		parserClassName.append(Constants.Input.CLASS);
		Class<?> parserClass = Class.forName(parserClassName.toString());

		DBC.ENSURE(InputModule.class.isAssignableFrom(parserClass));
		return parserClass;
	}

	/** Given an input file and the type of that file, create the {@link
		XMLDocumentType} for it. This method hides away the implementations of
		the input modules to allow a plug-in style way of accessing them.
		Students of design patterns will recognise this as something like the
		Abstract Factory pattern (with {@link InputModule} as the abstract
		interface. However, this goes one step further by invoking the {@link
		InputModule#parse(String)} method on the created class.

		@param inputFile		The input file.
		@param inputType		The type of file inputFile is.
		@return					An {@link XMLDocumentType} describing the
								vocabulary of XML that was contained in
								inputFile.
		@throws	InputException	if the parsing method did not conform to the
								required interface or was otherwise
								inaccessible.
		@throws ParserException	if an error was thrown inside the parser.
	*/
	public static XMLDocumentType parse(String inputFile, String inputType)
	throws InputException, ParserException
	{
		Log.debug("Attempting to parse " + inputFile + " as a " + inputType);
		DBC.REQUIRE(inputFile != null);
		DBC.REQUIRE(inputType != null);
		if (inputFile == null || inputType == null) {
			return null;
		}

		try {
			Object inputMod = parserClass(inputType).newInstance();
			Log.debug(
				"Created new parser instace: " +
				inputMod.getClass().getName()
			);
			if (inputMod instanceof InputModule) {
				InputModule inputModule = (InputModule)inputMod;
				Log.debug("Handing off " + inputFile + " to parser");
				return inputModule.parse(inputFile);
			} else {
				throw new InputException(Message.UNSUPPORTED_INPUT_TYPE);
			}
		} catch (ClassNotFoundException e) {
			// Couldn't find the parser class. This usually means that the
			// requested input method is not supported.
			throw new InputException(Message.UNSUPPORTED_INPUT_TYPE, e);
		} catch (IllegalAccessException e) {
			// This is only reachable if the no-arg constructor for the
			// InputModule is private/protected.
			throw new InputException(Message.UNSUPPORTED_INPUT_TYPE, e);
		} catch (InstantiationException e) {
			// This is only reachable if the constructor for the InputModule
			// fails in some way. This can happen if the code fragments for the
			// module fail to load.
			throw new InputException(Message.UNSUPPORTED_INPUT_TYPE, e);
		}
	}

	/** Find out the available input languages. This is done by querying the
		{@link Utils#findSubPackages} method to find all of the packages
		below {@link com.mcdermottroe.exemplar.Constants.Input#PACKAGE} which 
		are in the prescribed form.

		@return	A {@link SortedMap} where the keys are tokens representing 
				legal input languages and the values are descriptions of those 
				languages.
	*/
	public static SortedMap<String, String> availableInputLanguages() {
		SortedMap<String, String> availableInputLanguages;
		availableInputLanguages = new TreeMap<String, String>();

		List<String> packages = Utils.findSubPackages(Constants.Input.PACKAGE);
		for (String packageName : packages) {
			String inputMethod = packageName.substring(
				Constants.Input.PACKAGE.length() + 1
			);
			if (inputMethod.indexOf((int)Constants.Character.FULL_STOP) == -1) {
				try {
					Class<?> generatorClass = parserClass(inputMethod);
					Object gen = generatorClass.newInstance();
					availableInputLanguages.put(inputMethod, gen.toString());
				} catch (ClassNotFoundException e) {
					DBC.IGNORED_EXCEPTION(e);
				} catch (IllegalAccessException e) {
					DBC.IGNORED_EXCEPTION(e);
				} catch (InstantiationException e) {
					DBC.IGNORED_EXCEPTION(e);
				} catch (SecurityException e) {
					DBC.IGNORED_EXCEPTION(e);
				}
			}
		}

		return availableInputLanguages;
	}
}
