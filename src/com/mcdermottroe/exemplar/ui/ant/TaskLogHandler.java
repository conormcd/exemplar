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
package com.mcdermottroe.exemplar.ui.ant;

import java.util.logging.Handler;
import java.util.logging.LogRecord;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

/** A {@link Handler} for logging via an Ant {@link Task}.

	@author Conor McDermottroe
	@since	0.2
*/
public class TaskLogHandler
extends Handler
implements Copyable<TaskLogHandler>
{
	/** The {@link Task} we are logging for. Ideally I'd prefer this to be a
		function pointer of some sorts to allow us to point to the {@link
		Task#log(String)} method itself but unfortunately Java doesn't have
		those yet.
	*/
	private final Task task;

	/** This is set to true on invocation of {@link #close()}. */
	private boolean closed;

	/** Create a new handler.

		@param	taskToLogFor	The {@link Task} we'll be logging for.
	*/
	public TaskLogHandler(Task taskToLogFor) {
		super();
		task = taskToLogFor;
		closed = false;
	}

	/** A copy constructor.

		@param	taskToLogFor	The {@link Task} we'll be logging for.
		@param	finished		if this log handler is closed.
	*/
	protected TaskLogHandler(Task taskToLogFor, boolean finished) {
		super();
		task = taskToLogFor;
		closed = finished;
	}

	/** Publish a {@link LogRecord}.

		@param	record	The {@link LogRecord} to publish.
	*/
	@Override public void publish(LogRecord record) {
		if (record != null && task != null && !closed) {
			String message = record.getMessage();
			if (message != null) {
				if (task.getProject() != null) {
					task.log(message);
				}
			}
		}
	}

	/** Close the {@link Handler}. */
	@Override public void close() {
		closed = true;
	}

	/** Flush any cached log records that we might be logging. */
	@Override public void flush() {
		// This handler does not cache.
	}

	/** {@inheritDoc} */
	public TaskLogHandler getCopy() {
		Task taskCopy;
		if (task != null) {
			taskCopy = task.getCopy();
		} else {
			taskCopy = null;
		}
		return new TaskLogHandler(taskCopy, closed);
	}

	/** Get the {@link Task} we're logging for.

		@return	The task we're logging for.
	*/
	private Task getTask() {
		return task;
	}

	/** Is the logger closed.

		@return	Whether or not this log handler is closed.
	*/
	private boolean isClosed() {
		return closed;
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>o</code> is equal to this.
	*/
	@Override public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!getClass().equals(o.getClass())) {
			return false;
		}

		TaskLogHandler other = (TaskLogHandler)o;
		return	Utils.areDeeplyEqual(task, other.getTask()) &&
				Utils.areDeeplyEqual(closed, other.isClosed());
	}

	/** Implement {@link Object#hashCode()}.

		@return	The hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(task, closed);
	}

	/** Implement {@link Object#toString()}.

		@return A {@link String} representation of this object.
	*/
	@Override public String toString() {
		StringBuilder retVal = new StringBuilder();
		retVal.append("Handler for ");
		if (task != null) {
			retVal.append(task.toString());
		} else {
			retVal.append("no task");
		}
		if (closed) {
			retVal.append(" (closed)");
		}
		return retVal.toString();
	}
}
