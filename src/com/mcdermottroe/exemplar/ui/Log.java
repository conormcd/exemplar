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

import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.SPACE;
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
				<td>{@link Log#error(CharSequence...)}</td>
				<td>{@link Level#SEVERE}</td>
			</tr>
			<tr>
				<td>{@link Log#warning(CharSequence...)}</td>
				<td>{@link Level#WARNING}</td>
			</tr>
			<tr>
				<td>{@link Log#info(CharSequence...)}</td>
				<td>{@link Level#INFO}</td>
			</tr>
			<tr>
				<td>{@link Log#debug(CharSequence...)}</td>
				<td>{@link Level#FINE}</td>
			</tr>
		</table>
	</p>

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Log {
	/** The singleton instance for this class. */
	private static final Log logInst = new Log();

	/** The underlying {@link Logger} through which all logging will flow. */
	private final Logger logger;

	/** Constructor for the singleton object. */
	private Log() {
		// Set the underlying Logger
		logger = Logger.getLogger(PACKAGE);

		// Don't pass logging events up the chain
		logger.setUseParentHandlers(false);

		// Set the default log level
		logger.setLevel(Level.INFO);
	}

	/** Get the underlying {@link Logger}.

		@return	The underlying {@link Logger}.
	*/
	private Logger getLogger() {
		return logger;
	}

	/** Register a {@link Handler} as the destination for all log messages.

		@param	handler	The {@link Handler} which all log messages should be
						passed to.
	*/
	public static void registerHandler(Handler handler) {
		logInst.getLogger().addHandler(handler);
	}

	/** Clear all {@link Handler}s from the underlying logger. */
	public static void clearHandlers() {
		Logger l = logInst.getLogger();
		for (Handler h : l.getHandlers()) {
			l.removeHandler(h);
		}
	}

	/** Get the current {@link LogLevel} of the underlying {@link Logger}.

		@return	THe current {@link LogLevel}.
	*/
	public static LogLevel getLevel() {
		int level = logInst.getLogger().getLevel().intValue();

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

		return returnValue;
	}

	/** Set the {@link LogLevel} of logging to something other than the default.

		@param	level	The lowest {@link LogLevel} of message which should be
						logged.
	*/
	public static void setLevel(LogLevel level) {
		Logger l = logInst.getLogger();
		switch (level) {
			case ERROR:
				l.setLevel(Level.SEVERE);
				break;
			case WARNING:
				l.setLevel(Level.WARNING);
				break;
			case INFO:
				l.setLevel(Level.INFO);
				break;
			case DEBUG:
			default:
				l.setLevel(Level.FINE);
				break;
		}
	}

	/** Log an error message. These are typically un-ignorable or fatal
		messages.

		@param	messages	The error message to log.
	*/
	public static void error(CharSequence... messages) {
		error(null, messages);
	}

	/** Log an error message with the exception that caused the error. These
		are typically un-ignorable or fatal messages.

		@param	messages	The error message to log.
		@param	cause		The {@link Exception} that caused the error
							condition.
	*/
	public static void error(Throwable cause, CharSequence... messages) {
		doLog(messages, cause, Level.SEVERE);
	}

	/** Log a warning message. These are typically important but non-fatal
		messages about exceptional circumstances.

		@param	messages	The warning message to log.
	*/
	public static void warning(CharSequence... messages) {
		warning(null, messages);
	}

	/** Log a warning message with the exception that caused the problem. These
		are typically important but non-fatal messages about exceptional
		circumstances.

		@param	messages	The warning message to log.
		@param	cause		The {@link Exception} that caused the problem.
	*/
	public static void warning(Throwable cause, CharSequence... messages) {
		doLog(messages, cause, Level.WARNING);
	}

	/** Log an informational message about an exception which occurred and has
		been either successfully dealt with or ignored.

		@param	messages	The informational message to log.
	*/
	public static void info(CharSequence... messages) {
		doLog(messages, null, Level.INFO);
	}

	/** Log a debugging message. Debug messages can be used for anything, but
		will no be visible unless debugging is turned on.

		@param	messages	The debug message to log.
	*/
	public static void debug(CharSequence... messages) {
		debug(null, messages);
	}

	/** Log a debug message about an exception.

		@param	messages	The debug message to log.
		@param	cause		The {@link Exception} which caused the problem.
	*/
	public static void debug(Throwable cause, CharSequence... messages) {
		doLog(messages, cause, Level.FINE);
	}

	/** Convenient form for {@link #doLog(LogRecord)}.

		@param	m	The message to log.
		@param	t	If the message is being logged in response to an {@link
					Exception}, then the exception may be passed here.
		@param 	l	The {@link Level} at which the message is to be logged.
	*/
	private static void doLog(CharSequence[] m, Throwable t, Level l) {
		String message = "";
		if (m != null) {
			message = Strings.join(SPACE, (Object[])m);
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
		assert logRecord != null;

		// Get the underlying logger
		Logger l = logInst.getLogger();

		// Don't bother logging if there are no handlers.
		Handler[] handlers = l.getHandlers();
		if (handlers.length <= 0) {
			return;
		}

		// Hack to make levels work, because for some reason, levels below
		// INFO don't ever log out.
		Level level = logRecord.getLevel();
		if (Options.isDebugSet()) {
			if (Level.INFO.intValue() > level.intValue()) {
				level = Level.INFO;
			}
		}
		logRecord.setLevel(level);

		// Now do the logging
		l.log(logRecord);
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
		return caller;
	}
}
