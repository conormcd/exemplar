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
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** The superclass for most objects contained within an {@link
	XMLDocumentType}.

	@author	Conor McDermottroe
	@since	0.1
*/
public abstract class XMLObject {
	/** Interface which must be implemented by {@link XMLObject}s wishing to
		have name operations.
	*/
	protected interface HasName {
		/** Get the name of the {@link XMLObject}.

			@return						The name of the {@link XMLObject}.
			@throws	XMLObjectException	if calling this method is an unupported
										action.
		*/
		String getName() throws XMLObjectException;

		/** Set the name of the {@link XMLObject}.

			@param	name				The name to call the {@link XMLObject}.
			@throws	XMLObjectException	if calling this method is an unupported
										action.
		*/
		void setName(String name) throws XMLObjectException;
	}

	/** The name of this {@link XMLObject}. */
	protected String name;

	/** Constructor which just initialises storage. */
	protected XMLObject() {
		name = null;
	}

	/** Access method to set the name of this
		{@link XMLObject}.

		@param	newName				The name of this {@link XMLObject}.
		@throws	XMLObjectException	if calling this method is an unupported
									action.
	*/
	public final void setName(String newName)
	throws XMLObjectException
	{
		DBC.REQUIRE(newName != null);

		// Make sure it's a supported operation
		if (!(this instanceof HasName)) {
			throw new XMLObjectException(Message.XMLOBJECT_UNSUPPORTED_ACTION);
		}

		name = newName;
	}

	/** Access method to retrieve the name of this
		{@link XMLObject}.

		@return						The name of the {@link XMLObject}.
		@throws	XMLObjectException	if calling this method is an unupported
									action.
	*/
	public final String getName()
	throws XMLObjectException
	{
		// Make sure it's a supported operation
		if (!(this instanceof HasName)) {
			throw new XMLObjectException(Message.XMLOBJECT_UNSUPPORTED_ACTION);
		}

		return name;
	}

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
		if (!getClass().equals(o.getClass())) {
			return false;
		}

		XMLObject other = (XMLObject)o;
		if (other instanceof HasName) {
			try {
				if (!Utils.areDeeplyEqual(name, other.getName())) {
					return false;
				}
			} catch (XMLObjectException e) {
				DBC.UNREACHABLE_CODE();
			}
		}
		return true;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	public int hashCode() {
		int hashCode = 0;
		if (this instanceof HasName) {
			if (name != null) {
				hashCode += name.hashCode();
			}
			hashCode *= Constants.HASHCODE_MAGIC_NUMBER;
		}
		return hashCode;
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
