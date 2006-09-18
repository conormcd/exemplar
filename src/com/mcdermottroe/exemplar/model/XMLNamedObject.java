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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

/** A feature-adding subclass of {@link XMLObject} which adds a name to the
	base {@link XMLObject}.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class XMLNamedObject
extends XMLObject
{
	/** The name of the {@link XMLObject}. */
	protected String name;

	/** Constructor which just initialises storage. */
	protected XMLNamedObject() {
		name = null;
	}

	/** Access method to set the name of this {@link XMLObject}.

		@param	newName	The name of this {@link XMLObject}.
	*/
	public void setName(String newName) {
		DBC.REQUIRE(newName != null);
		name = newName;
	}

	/** Access method to retrieve the name of this {@link XMLObject}.

		@return	The name of the {@link XMLObject}.
	*/
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof XMLNamedObject)) {
			return false;
		}

		XMLNamedObject other = (XMLNamedObject)o;
		if (super.equals(o)) {
			return Utils.areDeeplyEqual(name, other.getName());
		}

		return false;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	public int hashCode() {
		int hashCode = super.hashCode();
		if (hashCode != 0) {
			hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		}
		if (name != null) {
			hashCode += name.hashCode();
		}
		hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		return hashCode;
	}
}
