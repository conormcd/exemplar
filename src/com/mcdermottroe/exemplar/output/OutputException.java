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

import java.io.File;

import com.mcdermottroe.exemplar.Exception;

import static com.mcdermottroe.exemplar.Constants.Character.LEFT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_PAREN;
import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.EOL;

/** An exception that can be thrown in response to any error in the output 
	phase of the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public class OutputException
extends Exception
{
	/** A {@link File} which was involved in the {@link OutputException}. */
	private final File file;

	/** OutputException without a description. */
	public OutputException() {
		super();
		file = null;
	}

	/** OutputException with a description.

		@param	message	The description of the exception.
	*/
	public OutputException(String message) {
		super(message);
		file = null;
	}

	/** OutputException with an originating {@link File}.

		@param	f	The file involved in the OutputException.
	*/
	public OutputException(File f) {
		super();
		file = f;
	}

	/** OutputException with a description and an originating {@link File}.

		@param	message	The description of the exception.
		@param	f		The file involved in the OutputException.
	*/
	public OutputException(String message, File f) {
		super(message);
		file = f;
	}

	/** OutputException with a description and a reference to an exception
		which caused it.

		@param message	The description of the exception.
		@param cause	The cause of the exception.
	*/
	public OutputException(String message, Throwable cause) {
		super(message, cause);
		file = null;
	}

	/** OutputException with a description, a reference to an exception
		which caused it and an originating {@link File}.

		@param	message	The description of the exception.
		@param	cause	The cause of the exception.
		@param	f		The file involved in the OutputException.
	*/
	public OutputException(String message, Throwable cause, File f) {
		super(message, cause);
		file = f;
	}

	/** OutputException with a reference to the exception that caused it.

		@param	cause	The cause of the exception.
	*/
	public OutputException(Throwable cause) {
		super(cause);
		file = null;
	}

	/** OutputException with a reference to the exception that caused it and an
		originating {@link File}.

		@param	cause	The cause of the exception.
		@param	f		The {@link File} involved in the OutputException.
	*/
	public OutputException(Throwable cause, File f) {
		super(cause);
		file = f;
	}

	/** {@inheritDoc} */
	public OutputException getCopy() {
		OutputException copy;

		String message = getMessage();
		Throwable cause = getCause();
		if (message != null && cause != null && file != null) {
			copy = new OutputException(message, cause, file);
		} else if (message != null && cause != null) {
			copy = new OutputException(message, cause);
		} else if (message != null && file != null) {
			copy = new OutputException(message, file);
		} else if (cause != null && file != null) {
			copy = new OutputException(cause, file);
		} else if (message != null) {
			copy = new OutputException(message);
		} else if (cause != null) {
			copy = new OutputException(cause);
		} else if (file != null) {
			copy = new OutputException(file);
		} else {
			copy = new OutputException();
		}
		copy.setStackTrace(copyStackTrace(getStackTrace()));
		return copy;
	}

	/** Get the file associated with this exception.

		@return	A {@link File} object for the file associated with this
				exception.
	*/
	public File getFile() {
		return file;
	}

	/** Extend {@link Exception#toString()}. */
	@Override public String toString() {
		if (file != null) {
			StringBuilder fileMessage = new StringBuilder();
			fileMessage.append(SPACE);
			fileMessage.append(LEFT_PAREN);
			fileMessage.append(file.getAbsolutePath());
			fileMessage.append(RIGHT_PAREN);
			fileMessage.append(EOL);

			return super.toString().replaceFirst(EOL, fileMessage.toString());
		} else {
			return super.toString();
		}
	}
}
