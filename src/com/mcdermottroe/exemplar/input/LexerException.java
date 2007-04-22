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

/** An {@link Exception} that can be thrown during lexical analysis of some
	input.

	@author	Conor McDermottroe
	@since	0.1
*/
public class LexerException extends Exception {
	/** LexerException without a description. */
	public LexerException() {
		super();
	}

	/** LexerException with a description.

		@param message The description of the exception.
	*/
	public LexerException(String message) {
		super(message);
	}

	/** LexerException with a description and a reference to an exception which
		caused it.

		@param message	The description of the exception.
		@param cause	The cause of the exception.
	*/
	public LexerException(String message, Throwable cause) {
		super(message, cause);
	}

	/** LexerException with a reference to the exception that caused it.

		@param cause The cause of the exception.
	*/
	public LexerException(Throwable cause) {
		super(cause);
	}

	/** {@inheritDoc} */
	public LexerException getCopy() {
		LexerException copy;

		String message = getMessage();
		Throwable cause = getCause();
		if (message != null && cause != null) {
			copy = new LexerException(message, cause);
		} else if (message != null) {
			copy = new LexerException(message);
		} else if (cause != null) {
			copy = new LexerException(cause);
		} else {
			copy = new LexerException();
		}
		copy.setStackTrace(copyStackTrace(getStackTrace()));
		return copy;
	}
}
