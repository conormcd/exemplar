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
package com.mcdermottroe.exemplar.utils;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.MILLISECONDS_IN_A_SECOND;

/** A class to allow the timing of any task. To use this class do the following:

	<pre>
// Create the timer
Timer timer = new Timer();
...
// Get the number of elapsed seconds since the timer
// was created.
double elapsed = timer.getElapsedSeconds();
	</pre>

	@author	Conor McDermottroe
	@since	0.2
*/
public class Timer
implements Comparable<Timer>, Copyable<Timer>
{
	/** The time at which the timer was created. */
	private final long startTime;

	/** Create and start a new timer. */
	public Timer() {
		startTime = System.currentTimeMillis();
	}

	/** A copy constructor for implementing {@link Copyable#getCopy()}.

		@param	start	The start time.
	*/
	protected Timer(long start) {
		startTime = start;
	}

	/** Get the time (in millisecond epoch time) that this {@link Timer} was
		started at.

		@return	The {@link Timer} start time.
	*/
	public long getStartTime() {
		return startTime;
	}

	/** Get the number of seconds elapsed since the timer was started.

		@return	The number of seconds since the timer was started.
	*/
	public double getElapsedSeconds() {
		return (double)(System.currentTimeMillis() - startTime) /
			   MILLISECONDS_IN_A_SECOND;
	}

	/** {@inheritDoc} */
	public Timer getCopy() {
		return new Timer(startTime);
	}

	/** Implement {@link Comparable#compareTo(Object)}.

		@param	other	The {@link Timer} to compare against.
		@return			See {@link Comparable#compareTo(Object)}.
	*/
	public int compareTo(Timer other) {
		return Utils.compare(startTime, other.getStartTime());
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The {@link Object} to compare against.
		@return		True if <code>this</code> equals <code>o</code>, false
					otherwise.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		Timer other = (Timer)o;
		return startTime == other.getStartTime();
	}

	/** Implement {@link Object#hashCode()}.

		@return	A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(startTime);
	}
}
