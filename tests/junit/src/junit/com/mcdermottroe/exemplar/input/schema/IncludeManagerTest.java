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
package junit.com.mcdermottroe.exemplar.input.schema;

import com.mcdermottroe.exemplar.generated.schema.element.Import;
import com.mcdermottroe.exemplar.generated.schema.element.Include;
import com.mcdermottroe.exemplar.input.schema.IncludeManager;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link IncludeManager}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class IncludeManagerTest
extends UtilityClassTestCase<IncludeManager>
{
	/** Test {@link IncludeManager#alreadyImported(Import)}. */
	public void testAlreadyImported() {
		Import imported = new Import();
		imported.setId("imported");
		Import notImported = new Import();
		notImported.setId("notImported");

		assertFalse(
			"Marked as imported when not imported",
			IncludeManager.alreadyImported(imported)
		);
		assertFalse(
			"Marked as imported when not imported",
			IncludeManager.alreadyImported(notImported)
		);
		IncludeManager.markAsImported(imported);
		assertTrue(
			"Not marked as imported when imported",
			IncludeManager.alreadyImported(imported)
		);
		assertFalse(
			"Marked as imported when not imported",
			IncludeManager.alreadyImported(notImported)
		);
	}

	/** Test {@link IncludeManager#alreadyIncluded(Include)}. */
	public void testAlreadyIncluded() {
		Include included = new Include();
		included.setId("included");
		Include notIncluded = new Include();
		notIncluded.setId("notIncluded");

		assertFalse(
			"Marked as included when not included",
			IncludeManager.alreadyIncluded(included)
		);
		assertFalse(
			"Marked as included when not included",
			IncludeManager.alreadyIncluded(notIncluded)
		);
		IncludeManager.markAsIncluded(included);
		assertTrue(
			"Not marked as included when included",
			IncludeManager.alreadyIncluded(included)
		);
		assertFalse(
			"Marked as included when not included",
			IncludeManager.alreadyIncluded(notIncluded)
		);
	}

	/** Test {@link IncludeManager#markAsImported(Import)}. */
	public void testMarkAsImported() {
		assertTrue("See testAlreadyImported", true);
	}

	/** Test {@link IncludeManager#markAsIncluded(Include)}. */
	public void testMarkAsIncluded() {
		assertTrue("See testAlreadyIncluded", true);
	}
}
