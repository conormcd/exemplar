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
package com.mcdermottroe.exemplar;

import com.mcdermottroe.exemplar.ui.Message;
import com.mcdermottroe.exemplar.ui.Options;

/** Provide some rudimentary Design By Contract&trade; facilities. This class
	attempts to mimic the Design By Contract&trade; mechanisms present in
	Eiffel. It would be nice to have invariants as well, but that would require
	more language support. If only Java&trade; had Design By Contract&trade;
	built in or if Eiffel didn't have hideous syntax!

	@author	Conor McDermottroe
	@since	0.1
	@see	<a href="http://archive.eiffel.com/doc/oosc/">Meyer, Bertrand.
			1997. <i>Object Oriented Software Construction, 2nd Edition.</i>
			Prentice-Hall.</a>
*/
public final class DBC {
	/** Sometimes an {@link AssertionError} must be stored and thrown later
		rather than throwing it immediately due to bootstrapping issues.
	*/
	private static AssertionError storedAssertion;

	/** Prevent this class from being externally instantiated. */
	private DBC() {
		UNREACHABLE_CODE();
	}

	/** Ensure static fields are initialised. */
	static {
		storedAssertion = null;
	}

	/** An assertion mechanism which does more than the <code>assert</code>
		keyword found in Java version 1.4 and later. This assertion mechanism
		includes information on where the assertion was thrown from. These
		assertions only run if the "debug" option is set. The correct way to
		test for this condition is with {@link Options#isDebugSet()}, otherwise
		the call to this method could become recursive.

		@param	assertion		The boolean expression which should be true.
		@see 	Options#isDebugSet()
	*/
	public static void ASSERT(boolean assertion) {
		// Quickly exit if the assertion will pass anyway.
		if (assertion && storedAssertion == null) {
			return;
		}

		// Figure out what course of action to take.
		//
		// The options are:
		// 1) Discard the assertion thrown.
		// 2) Defer the throwing of the assertion
		//    until later.
		// 3) Throw a previously deferred assertion.
		// 4) Throw the current assertion.
		boolean throwLater;
		if (Options.isInitialised()) {
			throwLater = !Options.isDebugSet();
		} else {
			throwLater = true;
		}

		// If there is already an assertion it is dealt with here rather than
		// wasting time craeting a new one.
		if (storedAssertion != null) {
			if (throwLater) {
				// The earliest assertion should be the one fired, so ignore
				// the one failing here.
				return;
			} else {
				throw storedAssertion;
			}
		}

		// Find the current fully qualified class name in a package-neutral way.
		String thisClass = DBC.class.getName();

		// Find the caller. This should be as easy as caller(), but this is
		// Java not Perl.
		int assertionPoint = -1;
		int caller = -1;
		StackTraceElement[] trace = (new AssertionError()).getStackTrace();
		for (int i = 0; i < trace.length; i++) {
			String traceMessage = trace[i].toString();
			if (!traceMessage.startsWith(thisClass)) {
				assertionPoint = i;
				if (i + 1 < trace.length) {
					caller = i + 1;
				}
				break;
			}
		}

		// Make an AssertionError to mimic assert
		String assertPoint = null;
		if (assertionPoint != -1) {
			assertPoint = trace[assertionPoint].toString();
		}
		String callerPoint = null;
		if (caller != -1) {
			callerPoint = trace[caller].toString();
		}
		AssertionError newAssertion = new AssertionError(
			Message.ASSERTION_MESSAGE(
				assertPoint,
				callerPoint
			)
		);

		// Now either throw or defer the assertion.
		if (throwLater) {
			storedAssertion = newAssertion;
		} else {
			throw newAssertion;
		}
	}

	/** Alias of {@link #ASSERT(boolean)} for describing a precondition for a
		method.

		@param	precondition	The condition which must be true at the start
								of a routine.
		@see	#ASSERT(boolean)
	*/
	public static void REQUIRE(boolean precondition) {
		ASSERT(precondition);
	}

	/** Alias of {@link #ASSERT(boolean)} for describing a postcondition for a
		method.

		@param	postcondition	The condition which must be true at the end of
								a routine.
		@see	#ASSERT(boolean)
	*/
	public static void ENSURE(boolean postcondition) {
		ASSERT(postcondition);
	}

	/** A marker for unreachable code. This should be used in cases where the
		programmer believes that it is impossible to reach and hence does not
		require more elaborate error reporting. An example of such would be in
		the <code>default</code> case of a <code>switch</code> statement
		switching on an enumerated type.

		@see #ASSERT(boolean)
	*/
	public static void UNREACHABLE_CODE() {
		ASSERT(false);
	}

	/** A marker for an ignored error. This should be used in cases where the
		programmer does not care about the existance of an error, but would
		like to be notified of it if running in debug mode. This can be useful
		in situations where an error is successfully avoided by the code, yet
		the existance of the error might be useful while debugging.

		@see #ASSERT(boolean)
	*/
	public static void IGNORED_ERROR() {
		ASSERT(false);
	}

	/** A method to allow tests to clear the stored delayed assertion. This
		must not be called from actual code, only from test code.
	*/
	public static void _clearDelayedAssertation() {
		storedAssertion = null;
	}
}
