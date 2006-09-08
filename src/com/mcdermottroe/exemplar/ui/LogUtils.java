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
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;

/** A collection of logging-related utilities.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class LogUtils {
	/** A default log handler that can be passed to {@link
		#setLogHandler(Handler)} which will cause all logging to go to the bit
		bucket.
	*/
	public static final Handler NO_OP_LOG_HANDLER = new NoOpLogHandler();

	/**	The logger that everything should use. */
	private static Logger theLogger;

	/** A marker to set when a real logger has been added. */
	private static boolean realLoggerSet;

	/** The level of logging required. Should be one of the predefined levels
		in {@link Level}.
	*/
	private static Level logLevel;

	/** Private constructor to prevent instantiation of this class. */
	private LogUtils() {
		DBC.UNREACHABLE_CODE();
	}

	/** Set a no-op logger, mark the logger as not set and set the log level to
		{@link Level.ALL}.
	*/
	static {
		// Default values for everything.
		theLogger = null;
		realLoggerSet = false;
		logLevel = Level.ALL;

		// Set a no-op method and reset the "set" flag.
		setLogHandler(NO_OP_LOG_HANDLER);
		realLoggerSet = false;
	}

	/** Assumes a logger has already been created and returns it for use. This
		should never be called before {@link #setLogHandler(Handler)}. Then
		again, {@link #setLogHandler(Handler)} should be called as early as
		possible in the life of the program.

		@return	A {@link Logger} object that has already been set up somewhere
				else.
	*/
	public static Logger getLogger() {
		DBC.REQUIRE(theLogger != null);
		DBC.REQUIRE(realLoggerSet);
		if (theLogger == null) {
			return null;
		}

		// Turn up the logging if debugging is on.
		if (Options.isDebugSet()) {
			theLogger.setLevel(Level.ALL);
		}

		DBC.ENSURE(theLogger != null);
		return theLogger;
	}

	/** Set up a logger for the rest of the program to use. This creates a
		logger for the base package, removes all other handlers for it and adds
		the supplied handler. The logging level is then set to {@link
		Level#SEVERE} if debug is not set and {@link Level#ALL} if debug is
		set, unless another level had been set with {@link #setLogLevel(Level)}.

		@param handler The handler to use to process the logs.
	*/
	public static void setLogHandler(Handler handler) {
		DBC.REQUIRE(handler != null);
		if (handler == null) {
			return;
		}

		// Create the logger.
		Logger logger = Logger.getLogger(Constants.PACKAGE);

		// Remove any/all the other Handlers
		Handler[] handlers = logger.getHandlers();
		for (int i = 0; i < handlers.length; i++) {
			logger.removeHandler(handlers[i]);
		}

		// Prevent the logger from using its parent handlers
		logger.setUseParentHandlers(false);

		// Add the provided handler
		logger.addHandler(handler);

		// Set the level of logging
		if (logLevel == null) {
			if (Options.isDebugSet()) {
				logger.setLevel(Level.ALL);
			} else {
				logger.setLevel(Level.SEVERE);
			}
		} else {
			logger.setLevel(logLevel);
		}

		// Store the logger for later
		theLogger = logger;

		// Set the flag so that people can actually use the logger
		realLoggerSet = true;
	}

	/** Set the level of logging.

		@param level	The level at which to set the logging.
	*/
	public static void setLogLevel(Level level) {
		DBC.REQUIRE(level != null);
		if (level == null) {
			return;
		}

		logLevel = level;
		if (theLogger != null) {
			theLogger.setLevel(level);
		}
	}

	/** Find out the level of logging.

		@return The log level currently set.
	*/
	public static Level getLogLevel() {
		return logLevel;
	}

	/** This is a log handler that causes all logging operations handled by it
		to be a no-op.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static class NoOpLogHandler extends Handler {
		/** Nothing gets opened and hence nothing needs to be closed. */
		public void close() {}

		/** Nothing gets logged, so flushing does nothing. */
		public void flush() {}

		/** Throw away a log record.

			@param lr	The {@link LogRecord} that never gets logged.
		*/
		public void publish(LogRecord lr) {}
	}
}
