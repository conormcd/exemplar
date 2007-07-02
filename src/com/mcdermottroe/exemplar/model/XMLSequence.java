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
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.LEFT_CURLY;
import static com.mcdermottroe.exemplar.Constants.Character.RIGHT_CURLY;
import static com.mcdermottroe.exemplar.Constants.INFINITY;

/** An {@link XMLObject} which represents sequences of {@link XMLObject}s.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLSequence
extends XMLAggregateObject<XMLSequence>
{
	/** The maximum number of times this {@link XMLSequence} may occur. */
	private int maxOccurs;

	/** The minimum number of times this {@link XMLSequence} must occur. */
	private int minOccurs;

	/** Create a new, empty {@link XMLSequence}. */
	public XMLSequence() {
		super();
		maxOccurs = 1;
		minOccurs = 1;
	}

	/** A copy constructor.

		@param	containedObjects	The {@link #contents} to copy.
		@param	min					The value for {@link #minOccurs}.
		@param	max					The value for {@link #maxOccurs}.
		@throws	CopyException		if the {@link #contents} could not be
									copied.
	*/
	protected XMLSequence(List<XMLObject<?>> containedObjects, int min, int max)
	throws CopyException
	{
		super(containedObjects);
		maxOccurs = max;
		minOccurs = min;
	}

	/** Get the maximum number of times the {@link XMLSequence} may be
		repeated.

		@return						The maximum number of times the {@link
									XMLSequence} may be repeated.
	*/
	public int getMaxOccurs() {
		return maxOccurs;
	}

	/** Get the minimum number of times the {@link XMLSequence} must be
		repeated.

		@return						The minimum number of times the {@link
									XMLSequence} may be repeated.
	*/
	public int getMinOccurs() {
		return minOccurs;
	}

	/** Set the minimum and maximum number of times the {@link XMLSequence} may
		be repeated.

		@param	min					The minimum number of times the {@link
									XMLSequence} may be repeated.
		@param	max					The maximum number of times the {@link
									XMLSequence} may be repeated.
	*/
	public void setMinMaxOccurs(int min, int max) {
		DBC.REQUIRE(min >= 0);
		DBC.REQUIRE(min < INFINITY);
		DBC.REQUIRE(max > 0);
		DBC.REQUIRE(min <= max);

		maxOccurs = max;
		minOccurs = min;
	}

	/** {@inheritDoc} */
	@Override public XMLSequence getCopy()
	throws CopyException
	{
		return new XMLSequence(contents, minOccurs, maxOccurs);
	}

	/** {@inheritDoc} */
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null) {
			return false;
		}
		if (!(o instanceof XMLSequence)) {
			return false;
		}
		if (!super.equals(o)) {
			return false;
		}

		XMLSequence other = (XMLSequence)o;
		if (minOccurs != other.getMinOccurs()) {
			return false;
		}
		if (maxOccurs != other.getMaxOccurs()) {
			return false;
		}
		return true;
	}

	/** {@inheritDoc} */
	@Override public int hashCode() {
		return Utils.genericHashCode(
			super.hashCode(),
			minOccurs,
			maxOccurs
		);
	}

	/** {@inheritDoc} */
	@Override public String toString() {
		StringBuilder retVal = new StringBuilder(
			toString(getClass().getName(), COMMA)
		);
		retVal.append(LEFT_CURLY);
		retVal.append(minOccurs);
		retVal.append(COMMA);
		retVal.append(maxOccurs);
		retVal.append(RIGHT_CURLY);
		return retVal.toString();
	}
}
