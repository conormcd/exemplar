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
package com.mcdermottroe.exemplar.ui;

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;

/** A collection of logging-related utilities.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class LogUtils {
	/** Private constructor to prevent instantiation of this class. */
	private LogUtils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Get the logger.

		@return	A {@link Logger}.
	*/
	public static Logger getLogger() {
		Logger logger = Logger.getLogger(Constants.PACKAGE);
		if (Options.isDebugSet()) {
			logger.setLevel(Level.ALL);
		} else {
			logger.setLevel(Level.INFO);
		}
		return logger;
	}

	/** Set up a logger for the rest of the program to use. This takes the
		logger for the base package, removes all other {@link Handler}s for it
		and adds the supplied {@link Handler}. The logging level is then set to
		{@link Level#SEVERE} if debug is not set and {@link Level#ALL} if debug
		is set.

		@param	handler	The {@link Handler} to use to process the logs.
	*/
	public static void setLogHandler(Handler handler) {
		DBC.REQUIRE(handler != null);

		Logger logger = Logger.getLogger(Constants.PACKAGE);

		// Remove any/all the other Handlers
		for (Handler h : logger.getHandlers()) {
			logger.removeHandler(h);
		}

		// Prevent the logger from using its parent handlers
		logger.setUseParentHandlers(false);

		// Add the provided handler
		if (handler != null) {
			logger.addHandler(handler);
		}
	}
}
