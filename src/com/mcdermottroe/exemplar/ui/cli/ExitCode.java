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
package com.mcdermottroe.exemplar.ui.cli;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.Utils;

/** A structure for keeping exit codes.

	@author	Conor McDermottroe
	@since	0.2
*/
public class ExitCode
implements Comparable<ExitCode>, Copyable<ExitCode>
{
	/** The numeric form to be passed to {@link System#exit(int)}. */
	private final int numericForm;

	/** The short name for the exit code. */
	private final String mnemonic;

	/** A description of the exit code and what it's used for. */
	private final String description;

	/** Simple constructor, just sets the members.

		@param code	The value for {@link #numericForm}.
		@param name The value for {@link #mnemonic}.
		@param desc The value for {@link #description}.
	*/
	public ExitCode(int code, String name, String desc) {
		numericForm = code;
		mnemonic = name;
		description = desc;
	}

	/** Implement {@link Comparable#compareTo(Object)}.
		
		@param	other	The {@link ExitCode} to compare with.
		@return			A result as defined by {@link
						Comparable#compareTo(Object)}.
	*/
	public int compareTo(ExitCode other) {
		int numCmp = Utils.compare(numericForm, other.getNumericForm());
		if (numCmp != 0) {
			return numCmp;
		}

		int mnCmp = Utils.compare(mnemonic, other.getMnemonic());
		if (mnCmp != 0) {
			return mnCmp;
		}

		return Utils.compare(description, other.getDescription());
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
		return mnemonic;
	}

	/** Implement {@link Object#equals(Object)}.

		@param	o	The other object to compare this with.
		@return		True if <code>this</code> equals <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof ExitCode)) {
			return false;
		}

		ExitCode other = (ExitCode)o;
		if (!Utils.areDeeplyEqual(numericForm, other.getNumericForm())) {
			return false;
		}

		return true;
	}

	/** Implement {@link Object#hashCode()}.

		@return A hash code for this object.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(numericForm, mnemonic, description);
	}

	/** {@inheritDoc} */
	public ExitCode getCopy() {
		return new ExitCode(numericForm, mnemonic, description);
	}
}
