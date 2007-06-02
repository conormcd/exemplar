// vim:filetype=java:ts=4
/*
	Copyright (c) 2007
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
package junit.com.mcdermottroe.exemplar.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.model.XMLAggregateObject;
import com.mcdermottroe.exemplar.model.XMLElementReference;
import com.mcdermottroe.exemplar.model.XMLObject;
import com.mcdermottroe.exemplar.model.XMLSequence;

/** Parent class to all subclasses of {@link XMLAggregateObject}.

	@param	<T>	The type of {@link XMLAggregateObject}.
	@author	Conor McDermottroe
	@since	0.2
*/
public abstract
class XMLAggregateObjectTestCase<T extends XMLAggregateObject<T>>
extends XMLObjectTestCase<T>
{
	/** Test {@link XMLAggregateObject#iterator()}. */
	public void testIterator() {
		boolean success = true;
		for (T sample : samples()) {
			if (sample != null) {
				int count = 0;
				for (Object o : sample) {
					assertNotNull("Null object returned from iterator", o);
					count++;
				}
				if (count != sample.getContents().size()) {
					success = false;
				}
			}
		}
		assertTrue("iterator() returns numElements() objects", success);
	}

	/** Test {@link XMLAggregateObject#getContents()}. */
	public void testGetContents() {
		for (T sample : samples()) {
			if (sample != null) {
				List<XMLObject<?>> contents = sample.getContents();
				Iterator<XMLObject<?>> i = contents.iterator();
				Iterator<XMLObject<?>> j = sample.iterator();
				if (i.hasNext() != j.hasNext()) {
					fail(
						"Mismatch in number of objects returned from" +
						" iterator() vs getContents()"
					);
					return;
				}
				while (i.hasNext() && j.hasNext()) {
					if (!Utils.areDeeplyEqual(i.next(), j.next())) {
						fail(
							"Mismatch in objects returned from" +
							" iterator() vs getContents()"
						);
						return;
					}
				}
				if (i.hasNext() || j.hasNext()) {
					fail(
						"Mismatch in number of objects returned from" +
						" iterator() vs getContents()"
					);
					return;
				}
			}
		}
	}

	/** Test {@link XMLAggregateObject#append(XMLAggregateObject)}. */
	public void testAppend() {
		// The list of added objects
		List<XMLObject<?>> objectsToAdd = new ArrayList<XMLObject<?>>();
		objectsToAdd.add(new XMLElementReference("foo"));
		objectsToAdd.add(new XMLElementReference("bar"));

		// Now create an XMLSequence with those objects
		XMLSequence seq = new XMLSequence();
		for (XMLObject<?> xo : objectsToAdd) {
			seq.addObject(xo);
		}

		// Now test all the non-null samples
		for (T sample : samples()) {
			if (sample != null) {
				List<XMLObject<?>> expected = sample.getContents();
				expected.addAll(objectsToAdd);
				sample.append(seq);
				assertEquals(
					"Appending null changed the contents!",
					expected,
					sample.getContents()
				);
			}
		}
	}

	/** Test {@link XMLAggregateObject#append(XMLAggregateObject)}. */
	public void testAppendNull() {
		for (T sample : samples()) {
			if (sample != null) {
				List<XMLObject<?>> originalContents = sample.getContents();
				sample.append(null);
				assertEquals(
					"Appending null changed the contents!",
					originalContents,
					sample.getContents()
				);
			}
		}
	}
}
