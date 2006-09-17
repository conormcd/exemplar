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

import java.util.Iterator;

import com.mcdermottroe.exemplar.Constants;

/** An {@link com.mcdermottroe.exemplar.model.XMLObject} which represents the
	concept of mixed content in XML.  This roughly corresponds to the
	<code>(#PCDATA|A|B)*</code> structure found in DTDs.

	@author	Conor McDermottroe
	@since	0.1
*/
public class XMLMixedContent
extends XMLAggregateObject
{
	/** {@inheritDoc} */
	public String toString() {
		StringBuffer desc = new StringBuffer(
			toStringPrefix(
				getClass().getName()
			)
		);

		Iterator it = iterator();
		if (it.hasNext()) {
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(it.next().toString());
			desc.append(Constants.Character.RIGHT_PAREN);
			while (it.hasNext()) {
				desc.append(Constants.Character.PIPE);
				desc.append(Constants.Character.LEFT_PAREN);
				desc.append(it.next().toString());
				desc.append(Constants.Character.RIGHT_PAREN);
			}
		}

		desc.append(toStringSuffix());

		return desc.toString();
	}
}