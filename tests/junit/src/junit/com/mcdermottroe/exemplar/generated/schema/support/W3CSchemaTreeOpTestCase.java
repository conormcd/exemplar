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
package junit.com.mcdermottroe.exemplar.generated.schema.support;

import java.util.HashMap;
import java.util.Map;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaException;
import com.mcdermottroe.exemplar.generated.schema.support.W3CSchemaTreeOp;
import com.mcdermottroe.exemplar.generated.schema.support.XMLComponent;

import junit.com.mcdermottroe.exemplar.NormalClassTestCase;

/** Parent class to all subclasses of {@link W3CSchemaTreeOp}.

	@author		Conor McDermottroe
	@since		0.2
	@param	<T>	The type of {@link W3CSchemaTreeOp} to test.
*/
public abstract class
	W3CSchemaTreeOpTestCase<T extends W3CSchemaTreeOp & Copyable<T>>
extends NormalClassTestCase<T>
{
	/** The set of cases to test for in {@link #testShouldApply()}. */
	private Map<XMLComponent<?>, Boolean> shouldApplyTestCases;

	/** The set of cases for test for in {@link #testExecute()}. */
	private Map<XMLComponent<?>, Boolean> executeTestCases;

	/** {@inheritDoc} */
	@Override public void setUp()
	throws Exception
	{
		super.setUp();

		shouldApplyTestCases = new HashMap<XMLComponent<?>, Boolean>();
		executeTestCases = new HashMap<XMLComponent<?>, Boolean>();
	}

	/** Add a test case for {@link #testShouldApply()}.

		@param	test	The test to pass to {@link
						W3CSchemaTreeOp#shouldApply(XMLComponent)}.
		@param	res		The result that should come back from the call to
						{@link W3CSchemaTreeOp#shouldApply(XMLComponent)}.
	*/
	protected void addShouldApplyTestCase(XMLComponent<?> test, boolean res) {
		shouldApplyTestCases.put(test, res);
	}

	/** Add a test case for {@link #testExecute()}.

		@param	test		The test to pass to {@link
							W3CSchemaTreeOp#execute(XMLComponent)}.
		@param	willThrow	True if the call to {@link
							W3CSchemaTreeOp#execute(XMLComponent)} throws an
							exception, false if it doesn't.
	*/
	protected void addExecuteTestCase(XMLComponent<?> test, boolean willThrow) {
		executeTestCases.put(test, willThrow);
	}

	/** Test {@link W3CSchemaTreeOp#shouldApply(XMLComponent)}. */
	public void testShouldApply() {
		if (shouldApplyTestCases.isEmpty()) {
			fail("No tests for shouldApply(XMLComponent)");
			return;
		}

		for (T sample : samples()) {
			if (sample != null) {
				for (XMLComponent<?> test : shouldApplyTestCases.keySet()) {
					Boolean result = sample.shouldApply(test);
					assertEquals(
						"Result not as expected",
						shouldApplyTestCases.get(test),
						result
					);
				}
			}
		}
	}

	/** Test {@link W3CSchemaTreeOp#execute(XMLComponent)}. */
	public void testExecute() {
		if (executeTestCases.isEmpty()) {
			fail("No tests for execute(XMLComponent)");
			return;
		}

		for (T sample : samples()) {
			if (sample != null) {
				for (XMLComponent<?> test : executeTestCases.keySet()) {
					Boolean threw = false;
					try {
						sample.execute(test);
					} catch (W3CSchemaException e) {
						assertNotNull("W3CSchemaException was null", e);
						threw = true;
					} catch (Exception e) {
						assertNotNull("Exception was null", e);
						e.printStackTrace();
						fail("Unexpected exception thrown");
					}

					assertEquals(
						"Result not as expected",
						executeTestCases.get(test),
						threw
					);
				}
			}
		}
	}
}
