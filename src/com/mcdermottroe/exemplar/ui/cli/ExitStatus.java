// vim:filetype=java:ts=4
/*
	Copyright (c) 2006
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
package com.mcdermottroe.exemplar.ui.cli;

import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.TreeMap;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;

/** A class to wrap and explain exit status codes produced by the {@link
	com.mcdermottroe.exemplar.ui.cli.Main CLI UI}.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class ExitStatus {
	/** The stash of all known exit codes. */
	private static Set<ExitCode> exitCodes;

	/** Private constructor to prevent instantiation of this class. */
	private ExitStatus() {
		DBC.UNREACHABLE_CODE();
	}

	/** Try to initialise this class as soon as possible. */
	static {
		initialise();
	}

	/** Initialisation routine to pull the exit codes out of the
		{@link ResourceBundle} and load them into the stash.
	*/
	private static void initialise() {
		exitCodes = new HashSet<ExitCode>();

		ResourceBundle rb = null;
		try {
			rb = ResourceBundle.getBundle(ExitStatus.class.getName());
		} catch (MissingResourceException e) {
			DBC.IGNORED_EXCEPTION(e);
			abort();
		}
		DBC.ASSERT(rb != null);

		for (Enumeration<String> e = rb.getKeys(); e.hasMoreElements(); ) {
			String exitSpec = e.nextElement();
			String mnemonic = exitSpec.substring(
				0,
				exitSpec.indexOf((int)Constants.Character.FULL_STOP)
			);
			String numericForm = exitSpec.substring(
				exitSpec.indexOf((int)Constants.Character.FULL_STOP) + 1
			);
			if	(
					mnemonic.matches(Constants.Regex.EXIT_STATUS_MNEMONIC) &&
					numericForm.matches(Constants.Regex.DIGITS)
				)
			{
				int number = Integer.parseInt(numericForm);
				String description = rb.getString(exitSpec);
				exitCodes.add(new ExitCode(number, mnemonic, description));
			}
		}
	}

	/** Abort the program by exiting -1 (255). */
	private static void abort() {
		System.exit(-1);
	}

	/** Convert a mnemonic exit code to an integer suitable for returning to
		the calling shell.

		@param mnemonic	The mnemonic String for the exit code.
		@return			An integer exit code.
	*/
	public static int getExitCode(String mnemonic) {
		DBC.REQUIRE(mnemonic != null);
		initialise();
		for (ExitCode exitCode : exitCodes) {
			if (exitCode.getMnemonic().equals(mnemonic)) {
				return exitCode.getNumericForm();
			}
		}

		// It shouldn't be possible to get here.
		//
		// The following is what should happen if it makes it to here (in order
		// of increasing desperation :-)).
		abort();
		DBC.UNREACHABLE_CODE();
		return -1;
	}

	/** Get an {@link Iterator} over the exit codes.

		@return An Iterator over the exit codes.
	*/
	public static Iterator<String> iterator() {
		initialise();
		Map<Integer, String> ret = new TreeMap<Integer, String>();
		for (ExitCode exitCode : exitCodes) {
			ret.put(
				exitCode.getNumericForm(),
				exitCode.getMnemonic()
			);
		}
		return ret.values().iterator();
	}

	/** Retrieve the description of the exit status.

		@param	mnemonic	The mnemonic that the exit status is known by.
		@return				A description of the exit status.
	*/
	public static String getDescription(String mnemonic) {
		DBC.REQUIRE(mnemonic != null);
		initialise();
		for (ExitCode exitCode : exitCodes) {
			if (exitCode.getMnemonic().equals(mnemonic)) {
				return exitCode.getDescription();
			}
		}

		abort();
		DBC.UNREACHABLE_CODE();
		return null;
	}

	/** A struct for keeping exit codes.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static final class ExitCode {
		/** The numeric form to be passed to {@link System#exit(int)}. */
		private int numericForm;

		/** The short name for the exit code. */
		private String mnemonic;

		/** A description of the exit code and what it's used for. */
		private String description;

		/** Simple constructor, just sets the members.

			@param code	The value for {@link #numericForm}.
			@param name The value for {@link #mnemonic}.
			@param desc The value for {@link #description}.
		*/
		private ExitCode(int code, String name, String desc) {
			numericForm = code;
			mnemonic = name;
			description = desc;
		}

		/** Getter for the {@link #numericForm} member.

			@return The value of the {@link #numericForm} member.
		*/
		public int getNumericForm() {
			return numericForm;
		}

		/** Getter for the {@link #mnemonic} member.

			@return The value of the {@link #mnemonic} member.
		*/
		public String getMnemonic() {
			return mnemonic;
		}

		/** Getter for the {@link #description} member.

			@return The value of the {@link #description} member.
		*/
		public String getDescription() {
			return description;
		}

		/** Synonym for {@link #getMnemonic()}.

			@return The value of the {@link #mnemonic} member.
		*/
		@Override public String toString() {
			return getMnemonic();
		}
	}
}
