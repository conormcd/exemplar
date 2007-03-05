// vim:filetype=java:ts=4
/*
	Copyright (c) 2004-2007
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
public abstract class XMLObject
implements Cloneable
{
	/** Clone this {@link XMLObject}.

		@return								A clone of this object.
		@throws	CloneNotSupportedException	if the clone fails.
	*/
	public Object clone()
	throws CloneNotSupportedException
	{
		return super.clone();
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		return o instanceof XMLObject;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return getClass().getName().hashCode();
	}

	/** A helper to make implementing {@link Object#toString()} easier for
		children of {@link XMLObject}s.

		@param	className		The name of the class to describe.
		@param	classDetails	The details of the contents of the object. 
		@return					A {@link String} which should only be used
								within a {@link XMLObject#toString()} method.
	*/
	static String toStringHelper(String className, String classDetails) {
		StringBuilder description = new StringBuilder(className);
		description.append(Constants.Character.SPACE);
		description.append(Constants.Character.EQUALS);
		description.append(Constants.Character.SPACE);
		description.append(Constants.Character.LEFT_PAREN);
		description.append(classDetails);
		description.append(Constants.Character.RIGHT_PAREN);
		return description.toString();
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public abstract String toString();
}
