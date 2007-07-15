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
package junit.com.mcdermottroe.exemplar.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.mcdermottroe.exemplar.ui.Log;
import com.mcdermottroe.exemplar.ui.LogLevel;
import com.mcdermottroe.exemplar.ui.Options;

import junit.com.mcdermottroe.exemplar.SingletonClassTestCase;

/** Test class for {@link com.mcdermottroe.exemplar.ui.Log}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class LogTest
extends SingletonClassTestCase<Log>
{
	/** A test {@link Handler} to capture logged {@link LogRecord}s for
		inspection.
	*/
	public static class LogTestLogHandler
	extends Handler
	{
		/** The records that have been logged. */
		private List<LogRecord> records;

		/** Create a new {@link LogTestLogHandler}. */
		public LogTestLogHandler() {
			super();
			records = new ArrayList<LogRecord>();
		}

		/** Implement {@link Handler#publish(LogRecord)}.

			@param	logRecord	The log record to publish.
		*/
		@Override public void publish(LogRecord logRecord) {
			records.add(logRecord);
		}

		/** Implement {@link Handler#flush()}. */
		@Override public void flush() {
			// Do nothing
		}

		/** Implement {@link Handler#close()}. */
		@Override public void close() {
			// Do nothing
		}

		/** Get the last record which was logged.

			@return	The last {@link LogRecord} which was logged.
		*/
		public LogRecord getLastRecord() {
			if (!records.isEmpty()) {
				return records.get(records.size() - 1);
			} else {
				return null;
			}
		}
	}

	/** The {@link Handler} to use. */
	private LogTestLogHandler testHandler = null;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();
		testHandler = new LogTestLogHandler();
		Log.registerHandler(testHandler);
	}

	/** Test {@link Log#clearHandlers()}. */
	public void testClearHandlers() {
		try {
			Log.clearHandlers();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
		}
		Log.registerHandler(testHandler);
	}

	/** Test {@link Log#getLevel()}. */
	public void testGetLevel() {
		try {
			Log.getLevel();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.getLevel() failed an assert.");
		}
	}

	/** Test {@link Log#setLevel(LogLevel)}. */
	public void testSetLevel() {
		LogLevel originalLevel = null;
		try {
			originalLevel = Log.getLevel();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Assertion failure when getting original log level");
		}
		assertNotNull("Original level is null", originalLevel);
		for (LogLevel l : LogLevel.values()) {
			Log.setLevel(l);
			assertEquals("Log.setLevel(new) did not stick", l, Log.getLevel());
			Log.setLevel(originalLevel);
			assertEquals(
				"Log.setLevel(original) did not stick",
				originalLevel,
				Log.getLevel()
			);
		}
	}

	/** Test {@link Log#registerHandler(Handler)}. */
	public void testRegisterHandler() {
		try {
			Log.registerHandler(testHandler);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.registerHandler(Handler) failed an assertion");
		}
		try {
			Log.clearHandlers();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.clearHandlers() failed an assertion");
		}
		try {
			Log.registerHandler(testHandler);
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.registerHandler(Handler) failed an assertion");
		}
	}

	/** Test {@link Log#debug(CharSequence...)}. */
	public void testDebugOneArg() {
		Options.reset();
		Options.set("debug", "true");
		try {
			Log.debug("foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.debug(Object) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
		Options.reset();
	}

	/** Test {@link Log#debug(Throwable, CharSequence...)}. */
	public void testDebugTwoArgs() {
		Options.reset();
		Options.set("debug", "true");
		Exception testException = new Exception();
		try {
			Log.debug(testException, "foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.debug(Object, Throwable) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
		assertEquals(
			"logRecord.getThrown() != testException",
			testException,
			logRecord.getThrown()
		);
		Options.reset();
	}

	/** Test {@link Log#error(CharSequence...)}. */
	public void testErrorOneArg() {
		try {
			Log.error("foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.error(Object) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
	}

	/** Test {@link Log#error(Throwable, CharSequence...)}. */
	public void testErrorTwoArgs() {
		Exception testException = new Exception();
		try {
			Log.error(testException, "foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.debug(Object, Throwable) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
		assertEquals(
			"logRecord.getThrown() != testException",
			testException,
			logRecord.getThrown()
		);
	}

	/** Test {@link Log#info(CharSequence...)}. */
	public void testInfoOneArg() {
		try {
			Log.info("foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.info(Object) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
	}

	/** Test {@link Log#warning(CharSequence...)}. */
	public void testWarningOneArg() {
		try {
			Log.warning("foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.warning(Object) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
	}

	/** Test {@link Log#warning(Throwable, CharSequence...)}. */
	public void testWarningTwoArgs() {
		Exception testException = new Exception();
		try {
			Log.warning(testException, "foo");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("Log.warning(Object, Throwable) failed an assertion");
		}
		LogRecord logRecord = testHandler.getLastRecord();
		assertNotNull("testHandler.getLastRecord() == null", logRecord);
		assertEquals(
			"logRecord.getMessage() != \"foo\"",
			"foo",
			logRecord.getMessage()
		);
		assertEquals(
			"logRecord.getThrown() != testException",
			testException,
			logRecord.getThrown()
		);
	}
}
