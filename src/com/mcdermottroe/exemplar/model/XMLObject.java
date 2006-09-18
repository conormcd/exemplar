// vim:filetype=java:ts=4
/*
	Copyright (c) 2004, 2005, 2006
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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.Constants;

/** The superclass for most objects contained within an {@link
	XMLDocumentType}.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class XMLObject {
	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof XMLObject)) {
			return false;
		}

		return true;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	public int hashCode() {
		return getClass().getName().hashCode();
	}

	/** All {@link XMLObject#toString()} methods have a common format, this is 
		the first portion of it.

		@param	className	The name of the class to describe.
		@return				A {@link String} which should only be used within a
							{@link XMLObject#toString()} method.
	*/
	protected static String toStringPrefix(String className) {
		StringBuffer prefix = new StringBuffer(className);
		prefix.append(Constants.Character.SPACE);
		prefix.append(Constants.Character.EQUALS);
		prefix.append(Constants.Character.SPACE);
		prefix.append(Constants.Character.LEFT_PAREN);
		return prefix.toString();
	}

	/** All {@link XMLObject#toString()} methods have a common format, this is 
		the last portion of it.

		@return A {@link String} which should only be used within a {@link 
				XMLObject#toString()} method.
	*/
	protected static String toStringSuffix() {
		StringBuffer suffix = new StringBuffer(1);
		suffix.append(Constants.Character.RIGHT_PAREN);
		return suffix.toString();
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	public abstract String toString();
}
