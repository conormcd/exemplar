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
package com.mcdermottroe.exemplar.ui.cli;

import java.util.logging.Formatter;
import java.util.logging.LogRecord;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
import static com.mcdermottroe.exemplar.Constants.Character.TAB;
import static com.mcdermottroe.exemplar.Constants.EOL;

/** A {@link java.util.logging.Formatter} for messages which are output to the
	console.

	@author Conor McDermottroe
	@since	0.2
*/
public class ConsoleFormatter
extends Formatter
implements Copyable<ConsoleFormatter>
{
	/** Format a log record so that it can be sent to the console.

		@param	record	The {@link LogRecord} to format for the console.
		@return			A {@link String} suitable for the console.
	*/
	@Override public String format(LogRecord record) {
		if (record == null) {
			return "";
		}

		// Create the buffer for the message with the raw message itself.
		StringBuilder message = new StringBuilder(
			String.valueOf(record.getMessage())
		);

		// If debugging is turned on, add some extra info
		if (Options.isDebugSet()) {
			message.insert(0, SPACE);
			message.insert(
				0,
				Message.DEBUG_CLASS_AND_METHOD(
					record.getSourceClassName(),
					record.getSourceMethodName()
				)
			);
		}

		// Now add the exception, if any
		Throwable cause = record.getThrown();
		if (cause != null) {
			message.append(EOL);
			message.append(TAB);
			message.append("Exception: ");
			message.append(cause);
		}

		String messageAsString = Strings.trimTrailingSpace(message);
		if (!messageAsString.endsWith(EOL)) {
			message = new StringBuilder(messageAsString);
			message.append(EOL);
			return message.toString();
		} else {
			return messageAsString;
		}
	}

	/** {@inheritDoc} */
	public ConsoleFormatter getCopy() {
		return new ConsoleFormatter();
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}

		return getClass().equals(o.getClass());
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		return 0;
	}
}
