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
package com.mcdermottroe.exemplar.ui.cli;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.utils.Resources;

import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;

/** A class to wrap and explain exit status codes produced by the {@link Main
	CLI UI}.

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

	/** Initialisation routine to pull the exit codes out of the JAR and load
		them into the stash.
	*/
	private static void initialise() {
		exitCodes = new HashSet<ExitCode>();

		Map<String, String> resources = Resources.get(ExitStatus.class);
		for (String key : resources.keySet()) {
			String mnemonic = key.substring(0, key.indexOf((int)FULL_STOP));
			String numericForm = key.substring(key.indexOf((int)FULL_STOP) + 1);
			String description = resources.get(key);

			int number = Integer.parseInt(numericForm);
			exitCodes.add(new ExitCode(number, mnemonic, description));
		}
	}

	/** Convert a mnemonic exit code to an integer suitable for returning to
		the calling shell.

		@param mnemonic	The mnemonic String for the exit code.
		@return			An integer exit code or -1 if an error occurs.
	*/
	public static int getExitCode(String mnemonic) {
		// Mnemonics cannot be null
		DBC.REQUIRE(mnemonic != null);
		DBC.REQUIRE(!exitCodes.isEmpty());

		// Ensure that the exit codes have been loaded.
		initialise();

		// Now find the exit code.
		for (ExitCode exitCode : exitCodes) {
			if (exitCode.getMnemonic().equals(mnemonic)) {
				return exitCode.getNumericForm();
			}
		}

		// It shouldn't be possible to get here.
		DBC.UNREACHABLE_CODE();
		return -1;
	}

	/** Get all of the exit codes.

		@return	A copy of the {@link Set} of exit codes.
	*/
	public static Set<ExitCode> getExitCodes() {
		return new HashSet<ExitCode>(exitCodes);
	}
}
