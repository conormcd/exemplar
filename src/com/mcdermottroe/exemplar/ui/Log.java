// vim:filetype=java:ts=4
/*
	Copyright (c) 2006, 2007
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.mcdermottroe.exemplar.DBC;

import static com.mcdermottroe.exemplar.Constants.PACKAGE;

/** A single point of entry for all logging within the program.

	<p>To set up the program for logging (this should be done within a UI):
		<pre>
// Create a {@link Handler}
{@link Handler} handler = new MyCustomHandler();

// Register the {@link Handler} with this class
Log.{@link Log#registerHandler(Handler handler)};
		</pre>
	</p>

	<p>To publish log messages, simply pick the level of message you wish to
		send and use the corresponding method. For example, to publish a
		warning:

		<pre>
Log.warning("The foo subsystem is almost out of memory!");
		</pre>
	</p>

	<p>Conversion table between log levels in this class and the {@link Level}s
		in the java.util.logging package.
		<table border="1">
			<tr>
				<th>Method in this class</th>
				<th>Log {@link Level} produced</th>
			</tr>
			<tr>
				<td>{@link Log#error(Object)}</td>
				<td>{@link java.util.logging.Level#SEVERE}</td>
			</tr>
			<tr>
				<td>{@link Log#warning(Object)}</td>
				<td>{@link java.util.logging.Level#WARNING}</td>
			</tr>
			<tr>
				<td>{@link Log#info(Object)}</td>
				<td>{@link java.util.logging.Level#INFO}</td>
			</tr>
			<tr>
				<td>{@link Log#debug(Object)}</td>
				<td>{@link java.util.logging.Level#FINE}</td>
			</tr>
		</table>
	</p>

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Log {
	/** An enumerated type describing the legal levels of logging. */
	public enum LogLevel {
		/** Fatal or unignorable errors. */
		ERROR,
		/** Ignorable errors. */
		WARNING,
		/** Informational messages. */
		INFO,
		/** Debugging aids. */
		DEBUG,
	}

	/** The underlying {@link Logger} through which all logging will flow. */
	private static final Logger LOGGER = Logger.getLogger(PACKAGE);

	/** Log messages have to be delayed until after the UI has been
		initialised. Any messages logged before then are stored here for later
		logging.
	*/
	private static List<LogRecord> delayedLogMessages = null;

	/** To ensure that code called by the initialiser does not cause problems
		we use this boolean to force all calls to {@link #doLog(Object,
		Throwable, Level)} to delay log messages until the initialiser is done.
	*/
	private static boolean classInitialised = false;

	/** Initialiser for this class. We remove all handlers and disable sending
		messages up the chain of {@link Handler}s so that the user can be sure
		of where the messages are going once they register their own {@link
		Handler}(s) using {@link #registerHandler(Handler)}. The default {@link
		Level} is set to {@link Level#INFO} (or {@link Level#ALL} if debugging
		is turned on).
	*/
	static {
		// Remove all of the handlers
		for (Handler h : LOGGER.getHandlers()) {
			LOGGER.removeHandler(h);
		}

		// Don't pass logging events up the chain
		LOGGER.setUseParentHandlers(false);

		// Set the default log level
		if (Options.isDebugSet()) {
			LOGGER.setLevel(Level.ALL);
		} else {
			LOGGER.setLevel(Level.INFO);
		}

		// The delayed log messages go here.
		if (delayedLogMessages == null) {
			delayedLogMessages = new ArrayList<LogRecord>();
		}
		classInitialised = true;
	}

	/** Private constructor to prevent instantiation of this class. */
	private Log() {
		DBC.UNREACHABLE_CODE();
	}

	/** Register a {@link Handler} as the destination for all log messages.

		@param	handler	The {@link Handler} which all log messages should be
						passed to.
	*/
	public static void registerHandler(Handler handler) {
		LOGGER.addHandler(handler);
	}

	/** Get the current {@link LogLevel} of the underlying {@link Logger}.

		@return	THe current {@link LogLevel}.
	*/
	public static LogLevel getLevel() {
		int level = LOGGER.getLevel().intValue();

		int severe = Level.SEVERE.intValue();
		int warning = Level.WARNING.intValue();
		int info = Level.INFO.intValue();

		LogLevel returnValue = null;
		if (level >= severe) {
			returnValue = LogLevel.ERROR;
		} else if (level == warning) {
			returnValue = LogLevel.WARNING;
		} else if (level == info) {
			returnValue = LogLevel.INFO;
		} else if (level < info) {
			returnValue = LogLevel.DEBUG;
		}

		DBC.ENSURE(returnValue != null);
		return returnValue;
	}

	/** Set the {@link LogLevel} of logging to something other than the default.

		@param	level	The lowest {@link LogLevel} of message which should be
						logged.
	*/
	public static void setLevel(LogLevel level) {
		switch (level) {
			case ERROR:
				LOGGER.setLevel(Level.SEVERE);
				break;
			case WARNING:
				LOGGER.setLevel(Level.WARNING);
				break;
			case INFO:
				LOGGER.setLevel(Level.INFO);
				break;
			case DEBUG:
				LOGGER.setLevel(Level.FINE);
				break;
			default:
				DBC.UNREACHABLE_CODE();
		}
	}

	/** Flush out any delayed messages. UIs should call this once they have
		finished initialising the {@link Options}. Calling this method before
		{@link Options#isInitialised()} returns true is a no-op.
	*/
	public static void flushDelayedMessages() {
		if (Options.isInitialised()) {
			List<LogRecord> dlm = new ArrayList<LogRecord>(
				delayedLogMessages
			);
			delayedLogMessages.clear();
			for (LogRecord message : dlm) {
				doLog(message);
			}
		}
	}

	/** Log an error message. These are typically un-ignorable or fatal
		messages.

		@param	message	The error message to log.
	*/
	public static void error(Object message) {
		error(message, null);
	}

	/** Log an error message with the exception that caused the error. These
		are typically un-ignorable or fatal messages.

		@param	message	The error message to log.
		@param	cause	The {@link Exception} that caused the error condition.
	*/
	public static void error(Object message, Throwable cause) {
		doLog(message, cause, Level.SEVERE);
	}

	/** Log a warning message. These are typically important but non-fatal
		messages about exceptional circumstances.

		@param	message	The warning message to log.
	*/
	public static void warning(Object message) {
		warning(message, null);
	}

	/** Log a warning message with the exception that caused the problem. These
		are typically important but non-fatal messages about exceptional
		circumstances.

		@param	message	The warning message to log.
		@param	cause	The {@link Exception} that caused the problem.
	*/
	public static void warning(Object message, Throwable cause) {
		doLog(message, cause, Level.WARNING);
	}

	/** Log an informational message. Informational messages do not describe
		error conditions, they merely serve to inform the user of progress.

		@param	message	The informational message to log.
	*/
	public static void info(Object message) {
		info(message, null);
	}

	/** Log an informational message about an exception which occurred and has
		been either successfully dealt with or ignored.

		@param	message	The informational message to log.
		@param	cause	The {@link Exception} ignored or safely handled.
	*/
	public static void info(Object message, Throwable cause) {
		doLog(message, cause, Level.INFO);
	}

	/** Log a debugging message. Debug messages can be used for anything, but
		will no be visible unless debugging is turned on.

		@param	message	The debug message to log.
	*/
	public static void debug(Object message) {
		debug(message, null);
	}

	/** Log a debug message about an exception.

		@param	message	The debug message to log.
		@param	cause	The {@link Exception} which caused the problem.
	*/
	public static void debug(Object message, Throwable cause) {
		doLog(message, cause, Level.FINE);
	}

	/** Convenient form for {@link #doLog(LogRecord)}.

		@param	m	The message to log.
		@param	t	If the message is being logged in response to an {@link
					Exception}, then the exception may be passed here.
		@param 	l	The {@link Level} at which the message is to be logged.
	*/
	private static void doLog(Object m, Throwable t, Level l) {
		String message = "";
		if (m != null) {
			message = m.toString();
		}
		LogRecord logRecord = new LogRecord(l, message);
		if (t != null) {
			logRecord.setThrown(t);
		}
		StackTraceElement callPoint = caller();
		logRecord.setSourceClassName(callPoint.getClassName());
		logRecord.setSourceMethodName(callPoint.getMethodName());
		doLog(logRecord);
	}

	/** Actual work of logging is done here.

		@param	logRecord	The {@link LogRecord} to log.
	*/
	private static void doLog(LogRecord logRecord) {
		DBC.REQUIRE(logRecord != null);
		if (logRecord == null) {
			return;
		}

		// Make sure that none of the handlers are null, because the people
		// who wrote java.util.logging.Logger don't check.
		boolean hasHandlers = false;
		Handler[] handlers = LOGGER.getHandlers();
		for (Handler h : handlers) {
			DBC.ASSERT(h != null);
			if (h != null) {
				hasHandlers = true;
			}
		}

		if (classInitialised && hasHandlers && Options.isInitialised()) {
			// Flush any delayed logs.
			flushDelayedMessages();

			// Hack to make levels work, because for some reason, levels below
			// INFO don't ever log out.
			Level level = logRecord.getLevel();
			if (Options.isDebugSet()) {
				if (Level.INFO.intValue() > level.intValue()) {
					level = Level.SEVERE;
				}
			}
			logRecord.setLevel(level);

			LOGGER.log(logRecord);
		} else {
			if (delayedLogMessages == null) {
				delayedLogMessages = new ArrayList<LogRecord>();
			}
			delayedLogMessages.add(logRecord);
		}
	}

	/** Find the method and class from where the "outer" method of this class
		was called.

		@return	The name of the class from which the calling method has called
				from.
	*/
	private static StackTraceElement caller() {
		String thisClassName = Log.class.getName();
		StackTraceElement[] trace = new Exception().getStackTrace();
		StackTraceElement caller = null;
		for (StackTraceElement te : trace) {
			caller = te;
			String traceClassName = te.getClassName();
			if (!thisClassName.equals(traceClassName)) {
				break;
			}
		}
		DBC.ENSURE(caller != null);
		return caller;
	}
}
