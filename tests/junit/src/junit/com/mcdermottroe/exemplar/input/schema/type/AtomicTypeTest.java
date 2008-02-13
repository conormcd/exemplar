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

/** Test class for {@link AtomicType}.

	@author		Conor McDermottroe
	@since		0.2
*/
public class AtomicTypeTest
extends TypeTestCase<AtomicType>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		addSample(new AtomicType("foo", false, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("bar", false, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("baz", false, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("foo", true, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("bar", true, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("baz", true, EnumSet.noneOf(Finality.class)));
		addSample(new AtomicType("foo", false, EnumSet.allOf(Finality.class)));
		addSample(new AtomicType("bar", false, EnumSet.allOf(Finality.class)));
		addSample(new AtomicType("baz", false, EnumSet.allOf(Finality.class)));
		addSample(new AtomicType("foo", true, EnumSet.allOf(Finality.class)));
		addSample(new AtomicType("bar", true, EnumSet.allOf(Finality.class)));
		addSample(new AtomicType("baz", true, EnumSet.allOf(Finality.class)));
	}
}
