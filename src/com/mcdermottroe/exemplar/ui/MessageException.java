// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2007
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
package com.mcdermottroe.exemplar.ui;

import com.mcdermottroe.exemplar.Exception;

/** An exception that can be thrown in response to an error in the process of
	localising the program.

	@author	Conor McDermottroe
	@since	0.1
*/
public class MessageException
extends Exception
{
	/** MessageException without a description. */
	public MessageException() {
		super();
	}

	/** MessageException with a description.

		@param message The description of the exception.
	*/
	public MessageException(String message) {
		super(message);
	}

	/** MessageException with a description and a reference to an exception
		which caused it.

		@param message	The description of the exception.
		@param cause	The cause of the exception.
	*/
	public MessageException(String message, Throwable cause) {
		super(message, cause);
	}

	/** MessageException with a reference to the exception that caused it.

		@param cause The cause of the exception.
	*/
	public MessageException(Throwable cause) {
		super(cause);
	}

	/** {@inheritDoc} */
	public MessageException getCopy() {
		MessageException copy;

		String message = getMessage();
		Throwable cause = getCause();
		if (message != null && cause != null) {
			copy = new MessageException(message, cause);
		} else if (message != null) {
			copy = new MessageException(message);
		} else if (cause != null) {
			copy = new MessageException(cause);
		} else {
			copy = new MessageException();
		}
		copy.setStackTrace(copyStackTrace(getStackTrace()));
		return copy;
	}
}
