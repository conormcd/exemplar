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
package com.mcdermottroe.exemplar.input;

import com.mcdermottroe.exemplar.Exception;

/** An {@link Exception} that can be thrown in response to an error in the
	parsing process.

	@author	Conor McDermottroe
	@since	0.1
*/
public class ParserException extends Exception {
	/** ParserException without a description. */
	public ParserException() {
		super();
	}

	/** ParserException with a description.

		@param message The description of the exception.
	*/
	public ParserException(String message) {
		super(message);
	}

	/** ParserException with a description and a reference to
		an exception which caused it.

		@param message	The description of the exception.
		@param cause	The cause of the exception.
	*/
	public ParserException(String message, Throwable cause) {
		super(message, cause);
	}

	/** ParserException with a reference to the exception
		that caused it.

		@param cause The cause of the exception.
	*/
	public ParserException(Throwable cause) {
		super(cause);
	}

	/** {@inheritDoc} */
	public ParserException getCopy() {
		ParserException copy;

		String message = getMessage();
		Throwable cause = getCause();
		if (message != null && cause != null) {
			copy = new ParserException(message, cause);
		} else if (message != null) {
			copy = new ParserException(message);
		} else if (cause != null) {
			copy = new ParserException(cause);
		} else {
			copy = new ParserException();
		}
		copy.setStackTrace(copyStackTrace(getStackTrace()));
		return copy;
	}
}
