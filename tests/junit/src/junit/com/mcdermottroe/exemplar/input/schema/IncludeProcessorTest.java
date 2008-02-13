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

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.generated.schema.element.All;
import com.mcdermottroe.exemplar.generated.schema.element.Include;
import com.mcdermottroe.exemplar.generated.schema.element.Schema;
import com.mcdermottroe.exemplar.input.schema.IncludeProcessor;

import static com.mcdermottroe.exemplar.Constants.CWD;

import junit.com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOpTestCase;

/** Test class for {@link IncludeProcessor}.

	@author		Conor McDermottroe
	@since		0.2
*/
public class IncludeProcessorTest
extends W3CSchemaTreeOpTestCase<IncludeProcessor>
{
	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		addSample(new IncludeProcessor(new File(CWD)));

		// Sample imports
		List<Include> sampleIncludes = new ArrayList<Include>();
		Schema parentSchema = new Schema();
		for (File sample : ParserTest.getSamples()) {
			Include sampleInclude = new Include();
			sampleInclude.setSchemaLocation(sample.getAbsolutePath());
			parentSchema.add(sampleInclude);
			sampleIncludes.add(sampleInclude);
		}

		addShouldApplyTestCase(new All(), false);
		for (Include sampleInclude : sampleIncludes) {
			addShouldApplyTestCase(sampleInclude, true);
			addExecuteTestCase(sampleInclude, false);
		}
	}
}
