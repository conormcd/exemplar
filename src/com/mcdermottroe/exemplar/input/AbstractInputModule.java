// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.model.XMLDocumentType;
import com.mcdermottroe.exemplar.ui.Message;

import static com.mcdermottroe.exemplar.Constants.CWD;

/** An abstract {@link InputModule} to make it easier to implement {@link
	InputModule}s.

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of {@link AbstractInputModule} this is.
*/
public abstract class AbstractInputModule<T>
implements Comparable<T>, Copyable<T>, InputModule
{
	/** Create a new {@link InputModule}. */
	protected AbstractInputModule() {
	}

	/** {@inheritDoc} */
	public XMLDocumentType parse(String inputFilePath)
	throws ParserException
	{
		DBC.REQUIRE(inputFilePath != null);
		if ("-".equals(inputFilePath)) {
			return parse(System.in, new File(CWD));
		}
		return parse(new File(inputFilePath));
	}

	/** {@inheritDoc} */
	public XMLDocumentType parse(File inputFile)
	throws ParserException
	{
		DBC.REQUIRE(inputFile != null);
		FileInputStream inputStream = null;
		try {
			assert inputFile != null;
			File dir = inputFile.getParentFile();
			inputStream = new FileInputStream(inputFile);
			if (dir != null) {
				return parse(inputStream, dir);
			} else {
				return parse(inputStream);
			}
		} catch (FileNotFoundException e) {
			throw new ParserException(
				Message.FILE_NOT_FOUND(inputFile.getAbsolutePath()),
				e
			);
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					// Ignore it, this is a best-effort close
					DBC.IGNORED_EXCEPTION(e);
				}
			}
		}
	}

	/** {@inheritDoc} */
	public XMLDocumentType parse(InputStream input)
	throws ParserException
	{
		DBC.REQUIRE(input != null);
		return parse(input, new File(CWD));
	}

	/** Workhorse for the other parse methods.

		@param	input			A source for the XML vocabulary description
								which is to be parsed by this {@link
								InputModule}.
		@param	baseDir			The directory in which the XML vocabulary
								description is rooted, this is used for
								resolving references to other files from within
								it.
		@return					An {@link XMLDocumentType} representing the
								vocabulary described by the input.
		@throws	ParserException	if an error occurs while parsing the input.
	*/
	protected abstract XMLDocumentType parse(InputStream input, File baseDir)
	throws ParserException;
}
