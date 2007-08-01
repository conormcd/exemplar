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
package com.mcdermottroe.exemplar.model;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.HASHCODE_MAGIC_NUMBER;

/** A feature-adding subclass of {@link XMLObject} which adds a name to the
	base {@link XMLObject}.

	@author		Conor McDermottroe
	@since		0.1
	@param	<T>	The type of XMLNamedObject.
*/
public abstract class XMLNamedObject<T extends XMLNamedObject<T>>
extends XMLObject<T>
{
	/** The name of the {@link XMLObject}. */
	protected final String name;

	/** Constructor which sets the name.

		@param	objectName	The name of the object.
	*/
	protected XMLNamedObject(String objectName) {
		super();
		DBC.REQUIRE(objectName != null);
		name = objectName;
	}

	/** Access method to retrieve the name of this {@link XMLObject}.

		@return	The name of the {@link XMLObject}.
	*/
	public String getName() {
		return name;
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (!super.equals(o)) {
			return false;
		}

		XMLNamedObject<?> other = (XMLNamedObject<?>)o;
		return Utils.areDeeplyEqual(name, other.getName());
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		int hashCode = super.hashCode();
		if (hashCode != 0) {
			hashCode *= HASHCODE_MAGIC_NUMBER;
		}
		if (name != null) {
			hashCode += name.hashCode();
		}
		hashCode *= HASHCODE_MAGIC_NUMBER;
		return hashCode;
	}
}
