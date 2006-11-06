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
package com.mcdermottroe.exemplar;

import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** A base class for all of the exceptions in the program. This is intended to
	provide nice chained exception backtraces for all derived exceptions.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class Exception
extends java.lang.Exception
{
	/** Exception without a description. */
	protected Exception() {
		super();
	}

	/** Exception with a description.

		@param message The description of the exception.
	*/
	protected Exception(String message) {
		super(message);
		DBC.REQUIRE(message != null);
	}

	/** Exception with a description and a reference to an exception which
		caused it.

		@param message	The description of the exception.
		@param cause	The cause of the exception.
	*/
	protected Exception(String message, Throwable cause) {
		super(message, cause);
		DBC.REQUIRE(message != null);
		DBC.REQUIRE(cause != null);
	}

	/** Exception with a reference to the exception that caused it.

		@param cause The cause of the exception.
	*/
	protected Exception(Throwable cause) {
		super(cause);
		DBC.REQUIRE(cause != null);
	}

	/** Get the backtrace as a {@link List} of {@link String}s.

		@return A {@link List} where each element is a step in the backtrace.
	*/
	public List<String> getBackTrace() {
		List<String> trace = new ArrayList<String>();

		// Walk the backtrace collecting information
		Throwable cause = this;
		while (cause != null) {
			String causeName = cause.getClass().getName();
			String message = cause.getMessage();
			if (message == null) {
				message = Message.EXCEPTION_NO_MESSAGE;
			}

			StringBuilder traceMessage = new StringBuilder(causeName);
			traceMessage.append(Constants.Character.COLON);
			traceMessage.append(Constants.Character.SPACE);
			traceMessage.append(message);
			traceMessage.append(Constants.EOL);
			if (Options.isDebugSet()) {
				traceMessage.append(exceptionStackTrace(cause));
			}
			trace.add(traceMessage.toString());
			cause = cause.getCause();
		}

		return trace;
	}

	/** Provide a full backtrace of all the exceptions which caused this
		exception to be thrown, (with a stacktrace for each exception if
	 	debugging is turned on).

		@return	A descriptive debugging {@link String} for this {@link
				Exception}.
	*/
	@Override public String toString() {
		StringBuilder ret = new StringBuilder();
		for (String backTraceElement : getBackTrace()) {
			ret.append(backTraceElement);
		}
		return ret.toString();
	}

	/** Get the stack trace from the point at which the given exception was
		thrown.

		@param t	The {@link Throwable} to get the stack trace from.
		@return		A {@link String} representation of the stack trace in the
					format of one frame per line. All lines are indented by one
					indentation unit as defined by {@link Constants.UI#INDENT}.
	*/
	private static String exceptionStackTrace(Throwable t) {
		DBC.REQUIRE(t != null);
		if (t == null) {
			return null;
		}

		StackTraceElement[] trace = t.getStackTrace();
		StringBuilder ret = new StringBuilder();
		for (StackTraceElement traceElement : trace) {
			ret.append(Constants.UI.INDENT);
			ret.append(traceElement);
			ret.append(Constants.EOL);
		}
		return ret.toString();
	}
}
