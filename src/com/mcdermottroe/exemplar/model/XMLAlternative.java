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

import java.util.List;

import com.mcdermottroe.exemplar.CopyException;

import static com.mcdermottroe.exemplar.Constants.Character.PIPE;

/** An {@link XMLObject} which represents alternative lists of {@link
	XMLObject}s. This roughly corresponds to the <code>(A|B)</code> structure
	found in DTDs.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLAlternative
extends XMLAggregateObject
{
	/** Create a new, empty {@link XMLAlternative}. */
	public XMLAlternative() {
		super();
	}

	/** A copy constructor.

		@param	containedObjects	The {@link #contents} to copy.
		@throws	CopyException		if the {@link #contents} cannot be copied.
	*/
	protected XMLAlternative(List<XMLObject<?>> containedObjects)
	throws CopyException
	{
		super(containedObjects);
	}

	/** {@inheritDoc} */
	@Override public XMLAlternative getCopy()
	throws CopyException
	{
		return new XMLAlternative(contents);
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		return toString(getClass().getName(), PIPE);
	}
}
