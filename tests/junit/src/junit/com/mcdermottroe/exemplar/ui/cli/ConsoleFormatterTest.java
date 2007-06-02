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
package junit.com.mcdermottroe.exemplar.ui.cli;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;
import com.mcdermottroe.exemplar.ui.cli.ConsoleFormatter;

import static com.mcdermottroe.exemplar.Constants.Character.TAB;
import static com.mcdermottroe.exemplar.Constants.EOL;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link ConsoleFormatter}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class ConsoleFormatterTest
extends NormalClassTestCase<ConsoleFormatter>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		addSample(new ConsoleFormatter());
	}

	/** Test {@link ConsoleFormatter#format(LogRecord)}. */
	public void testFormat() {
		LogRecord[] input = {
			null,
			new LogRecord(Level.ALL, "foo"),
			new LogRecord(Level.CONFIG, "foo"),
			new LogRecord(Level.FINE, "foo"),
			new LogRecord(Level.FINER, "foo"),
			new LogRecord(Level.FINEST, "foo"),
			new LogRecord(Level.INFO, "foo"),
			new LogRecord(Level.OFF, "foo"),
			new LogRecord(Level.SEVERE, "foo"),
			new LogRecord(Level.WARNING, "foo"),
			new LogRecord(Level.ALL, "foo" + EOL),
			new LogRecord(Level.CONFIG, "foo" + EOL),
			new LogRecord(Level.FINE, "foo" + EOL),
			new LogRecord(Level.FINER, "foo" + EOL),
			new LogRecord(Level.FINEST, "foo" + EOL),
			new LogRecord(Level.INFO, "foo" + EOL),
			new LogRecord(Level.OFF, "foo" + EOL),
			new LogRecord(Level.SEVERE, "foo" + EOL),
			new LogRecord(Level.WARNING, "foo" + EOL),
			new LogRecord(Level.ALL, "foo"),
			new LogRecord(Level.CONFIG, "foo"),
			new LogRecord(Level.FINE, "foo"),
			new LogRecord(Level.FINER, "foo"),
			new LogRecord(Level.FINEST, "foo"),
			new LogRecord(Level.INFO, "foo"),
			new LogRecord(Level.OFF, "foo"),
			new LogRecord(Level.SEVERE, "foo"),
			new LogRecord(Level.WARNING, "foo"),
		};
		String className = "ConsoleFormmatterTest";
		String methodName = "testFormat";
		String[] expected = {
			"",
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
			Message.DEBUG_CLASS_AND_METHOD(className, methodName) + " foo" +
				EOL + TAB + "Exception: java.lang.Exception: bar" + EOL,
		};

		assertNotNull("Bad test data", input);
		assertNotNull("Bad test data", expected);
		assertEquals("Bad test data", input.length, expected.length);

		// Set the class and method name on all records
		for (LogRecord record : input) {
			if (record != null) {
				record.setSourceClassName(className);
				record.setSourceMethodName(methodName);
			}
		}

		// Add exceptions to some of the records
		for (int i = 19; i < input.length; i++) {
			input[i].setThrown(new Exception("bar", new Exception()));
		}

		Options.reset();
		Options.set("debug", "true");
		for (ConsoleFormatter sample : samples()) {
			if (sample != null) {
				for (int i = 0; i < input.length; i++) {
					String message = sample.format(input[i]);
					assertNotNull("message was null", message);
					assertEquals(
						"Output did not match expected",
						expected[i],
						message
					);
				}
			}
		}
		Options.reset();
	}
}
