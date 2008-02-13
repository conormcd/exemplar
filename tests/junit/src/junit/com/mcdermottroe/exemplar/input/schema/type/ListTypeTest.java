// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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
package junit.com.mcdermottroe.exemplar.input.schema.type;

import java.util.EnumSet;

import com.mcdermottroe.exemplar.input.schema.type.AtomicType;
import com.mcdermottroe.exemplar.input.schema.type.Finality;
import com.mcdermottroe.exemplar.input.schema.type.ListType;

/** Test class for {@link ListType}.

	@author		Conor McDermottroe
	@since		0.2
*/
public class ListTypeTest
extends TypeTestCase<ListType<AtomicType>>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		// The listed types 
		AtomicType a = new AtomicType(
			"atomicTypeA",
			false,
			EnumSet.noneOf(Finality.class)
		);
		AtomicType b = new AtomicType(
			"atomicTypeB",
			true,
			EnumSet.allOf(Finality.class)
		);

		addSample(
			new ListType<AtomicType>("foo", EnumSet.noneOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("bar", EnumSet.noneOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("baz", EnumSet.noneOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("foo", EnumSet.allOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("bar", EnumSet.allOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("baz", EnumSet.allOf(Finality.class), a)
		);
		addSample(
			new ListType<AtomicType>("foo", EnumSet.noneOf(Finality.class), b)
		);
		addSample(
			new ListType<AtomicType>("bar", EnumSet.noneOf(Finality.class), b)
		);
		addSample(
			new ListType<AtomicType>("baz", EnumSet.noneOf(Finality.class), b)
		);
		addSample(
			new ListType<AtomicType>("foo", EnumSet.allOf(Finality.class), b)
		);
		addSample(
			new ListType<AtomicType>("bar", EnumSet.allOf(Finality.class), b)
		);
		addSample(
			new ListType<AtomicType>("baz", EnumSet.allOf(Finality.class), b)
		);
	}

	/** Test {@link ListType#getListedType()}. */
	public void testGetListedType() {
		for (ListType<AtomicType> sample : samples()) {
			if (sample != null) {
				try {
					AtomicType at = sample.getListedType();
					assertNotNull("getListedType() returned null", at);
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					fail("getListedType() threw an exception");
				}
			}
		}
	}
}
