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
package junit.com.mcdermottroe.exemplar.ui.ant;

import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.apache.tools.ant.Project;

import com.mcdermottroe.exemplar.ui.ant.Task;
import com.mcdermottroe.exemplar.ui.ant.TaskLogHandler;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Test class for {@link TaskLogHandler}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class TaskLogHandlerTest
extends NormalClassTestCase<TaskLogHandler>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		Task sampleTask = new Task();
		sampleTask.setProject(new Project());

		TaskLogHandler closedHandler = new TaskLogHandler(sampleTask);
		closedHandler.close();

		addSample(new TaskLogHandler(null));
		addSample(new TaskLogHandler(sampleTask));
		addSample(closedHandler);
	}

	/** Test {@link TaskLogHandler#close()}. */
	public void testClose() {
		for (TaskLogHandler sample : samples()) {
			if (sample != null) {
				TaskLogHandler copy = sample.getCopy();
				try {
					copy.close();
				} catch (AssertionError e) {
					assertNotNull("AssertionError is null", e);
					fail("close() threw an assertion");
				}
			}
		}
	}

	/** Test {@link TaskLogHandler#flush()}. */
	public void testFlush() {
		for (TaskLogHandler sample : samples()) {
			if (sample != null) {
				try {
					sample.flush();
				} catch (AssertionError e) {
					assertNotNull("AssertionError is null", e);
					fail("close() threw an assertion");
				}
			}
		}
	}


	/** Test {@link TaskLogHandler#publish(LogRecord)}. */
	public void testPublish() {
		LogRecord[] testData = {
			null,
			new LogRecord(Level.ALL, "message"),
		};
		for (TaskLogHandler sample : samples()) {
			if (sample != null) {
				for (LogRecord lr : testData) {
					try {
						sample.publish(lr);
					} catch (AssertionError e) {
						assertNotNull("AssertionError is null", e);
						fail("publish() threw an assertion");
					}
				}
			}
		}
	}}
