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
package com.mcdermottroe.exemplar.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Packages;

import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;
import static com.mcdermottroe.exemplar.Constants.Output.PACKAGE;

/** Output handling utility methods.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class OutputUtils {
	/** Private constructor to prevent instantiation of this class. */
	private OutputUtils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Generate a parser given an {@link XMLDocumentType} and the type of
		parser that is required.

		@param	doctype			The {@link XMLDocumentType} describing the
								vocabulary.
		@param	filename		The name of the file to output to (directory
								name for multi file output).
		@param	language		The language of the parser to be output.
		@param	api				The API (if any) that the parser to be output
								must conform to.
		@throws OutputException	if any of the required parameters are null, if
								the generator cannot be instantiated or if the
								generator throws an exception while trying to
								generate code.
	*/
	public static void generateParser	(
											XMLDocumentType doctype,
											String filename,
											String language,
											String api
										)
	throws OutputException
	{
		// Preconditions
		if (doctype == null) {
			throw new OutputException(Message.SOURCE_GENERATOR_DOCTYPE_NULL());
		}
		if (language == null) {
			throw new OutputException(Message.SOURCE_GENERATOR_LANGUAGE_NULL());
		}

		// Make the generator
		XMLParserSourceGenerator<?> gen;
		gen = XMLParserSourceGenerator.create(language, api);

		// The output file
		File outputFile = null;
		if (filename != null) {
			outputFile = new File(filename);
		}

		// Make the generator work
		try {
			gen.generateParser(doctype, outputFile);
		} catch (XMLParserGeneratorException e) {
			throw new OutputException(
				Message.SOURCE_GENERATOR_THREW_EXCEPTION(),
				e
			);
		}
	}

	/** Write a {@link String} to a file.

		@param	s				The {@link String} to write to the file.
		@param	file			A {@link String} containing the full path to the
								file to write to.
		@throws	OutputException	if anything goes wrong
	*/
	public static void writeStringToFile(String s, String file)
	throws OutputException
	{
		writeStringToFile(s, new File(file));
	}

	/** Write a {@link String} to a file.

		@param	s				The {@link String} to write to the file.
		@param	dir				A {@link File} for the directory in which the
								new file will reside.
		@param	filename		A {@link String} containing the name of the file
								in <code>dir</code> to write the {@link String}
								to.
		@throws	OutputException	if anything goes wrong
	*/
	public static void writeStringToFile(String s, File dir, String filename)
	throws OutputException
	{
		writeStringToFile(s, new File(dir, filename));
	}

	/** Write a {@link String} to a {@link File}.

		@param	s				The {@link String} to write to the {@link
								File}.
		@param	file			The {@link File} to write the {@link String}
								to.
		@throws	OutputException if anything goes wrong.
	*/
	public static void writeStringToFile(String s, File file)
	throws OutputException
	{
		DBC.REQUIRE(s != null);
		DBC.REQUIRE(file != null);
		assert file != null;

		BufferedWriter output = null;
		boolean outputOpen = false;
		try {
			// Write the string to the output file.
			output = new BufferedWriter(
				new OutputStreamWriter(
					new FileOutputStream(file),
					Options.getString("output-encoding")
				)
			);
			outputOpen = true;
			output.write(s);
			output.close();
			outputOpen = false;
		} catch (FileNotFoundException e) {
			throw new OutputException(
				Message.FILE_WRITE_IO_EXCEPTION(
					file.getAbsolutePath()
				),
				e,
				file
			);
		} catch (UnsupportedEncodingException e) {
			throw new OutputException(
				Message.FILE_WRITE_IO_EXCEPTION(
					file.getAbsolutePath()
				),
				e,
				file
			);
		} catch (IOException e) {
			throw new OutputException(
				Message.FILE_WRITE_IO_EXCEPTION(
					file.getAbsolutePath()
				),
				e,
				file
			);
		} finally {
			if (outputOpen) {
				try {
					output.close();
				} catch (IOException e) {
					DBC.IGNORED_EXCEPTION(e);
				}
			}
		}
	}

	/** Discover the available output languages.

		@return	A {@link SortedMap} where the keys are the available output
				languages and the values are the descriptions of those output
				languages.
	*/
	public static SortedMap<String, String> availableOutputLanguages() {
		SortedMap<String, String> availableOutputLanguages;
		availableOutputLanguages = new TreeMap<String, String>();

		SortedSet<LanguageAPIPair> languageAPIPairs;
		languageAPIPairs = availableLanguageAPIPairs();
		for (LanguageAPIPair pair : languageAPIPairs) {
			String language = pair.getLanguage();
			String api = pair.getAPI();
			XMLParserSourceGenerator<?> gen;
			gen = XMLParserSourceGenerator.create(language, api);
			StringBuilder description = new StringBuilder(
				gen.describeLanguage()
			);
			LanguageAPIPair containedPair = new LanguageAPIPair(language, null);
			if (!languageAPIPairs.contains(containedPair)) {
				description.append(Message.OPTION_LANGUAGE_REQUIRES_API());
			}
			availableOutputLanguages.put(language, description.toString());
		}

		return availableOutputLanguages;
	}

	/** Discover the available output APIs.

		@return A {@link SortedMap} where the keys are the available output
				APIs and the values are the descriptions of those output APIs.
	*/
	public static SortedMap<String, String> availableOutputAPIs() {
		SortedMap<String, String> availableOutputAPIs;
		availableOutputAPIs = new TreeMap<String, String>();

		for (LanguageAPIPair pair : availableLanguageAPIPairs()) {
			String language = pair.getLanguage();
			String api = pair.getAPI();
			if (api != null) {
				XMLParserSourceGenerator<?> gen;
				gen = XMLParserSourceGenerator.create(language, api);
				StringBuilder description = new StringBuilder(
					gen.describeAPI()
				);
				description.append(Message.OPTION_LANGUAGE_OF_API(language));
				availableOutputAPIs.put(api, description.toString());
			}
		}

		return availableOutputAPIs;
	}

	/** Discover the set of output language-API pairs that are legal.

		@return A {@link SortedSet} containing a {@link LanguageAPIPair} for
				every legal combination of language and API.
	*/
	public static SortedSet<LanguageAPIPair> availableLanguageAPIPairs() {
		SortedSet<LanguageAPIPair> ret = new TreeSet<LanguageAPIPair>();

		List<String> pNames = Packages.findSubPackages(PACKAGE);
		for (String packageName : pNames) {
			String outputLanguageAPI = packageName.substring(
				PACKAGE.length() + 1
			);

			LanguageAPIPair pair = null;
			int fSI = outputLanguageAPI.indexOf((int)FULL_STOP);
			if (fSI >= 0) {
				String lang = outputLanguageAPI.substring(0, fSI);
				String api = outputLanguageAPI.substring(fSI + 1);
				if (api.indexOf((int)FULL_STOP) == -1) {
					pair = new LanguageAPIPair(lang, api);
				}
			} else {
				pair = new LanguageAPIPair(outputLanguageAPI, null);
			}

			XMLParserSourceGenerator<?> gen;
			gen = XMLParserSourceGenerator.create(
				pair.getLanguage(),
				pair.getAPI()
			);
			if (gen != null) {
				ret.add(pair);
			}
		}

		return ret;
	}
}
