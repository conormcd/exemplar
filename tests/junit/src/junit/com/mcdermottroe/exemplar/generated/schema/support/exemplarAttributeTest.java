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
package junit.com.mcdermottroe.exemplar.generated.schema.support;

import java.util.ArrayList;
import java.util.List;

import com.mcdermottroe.exemplar.generated.schema.support.exemplarAttribute;

/** Test class for {@link exemplarAttribute}.

	@author	Conor McDermottroe
	@since	0.2
*/
public class exemplarAttributeTest
extends XMLComponentTestCase<exemplarAttribute>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();

		allowPublicStaticMembers = true;
		ignoreToStringConsistencyTest = true;

		String[] names = {"foo", "bar", "baz", "quux", };
		int[] types = {
			exemplarAttribute.DEFAULT,
			exemplarAttribute.FIXED,
			exemplarAttribute.IMPLIED,
			exemplarAttribute.REQUIRED,
		};
		String[] values = {"foo", "bar", "baz", "quux", };

		List<exemplarAttribute> samples = new ArrayList<exemplarAttribute>();
		for (String name : names) {
			for (int type : types) {
				if	(
						type == exemplarAttribute.DEFAULT ||
						type == exemplarAttribute.FIXED
					)
				{
					for (String value : values) {
						samples.add(new exemplarAttribute(name, type, value));
					}
				} else {
					samples.add(new exemplarAttribute(name, type, null));
				}
			}
		}
		for (exemplarAttribute att : samples) {
			if (att.getType() != exemplarAttribute.FIXED) {
				att.setValue("nonDefaultValue");
			}
			addSample(att);
		}
	}

	/** Test {@link exemplarAttribute#getDefaultValue()}. */
	public void testGetDefaultValue() {
		for (exemplarAttribute sample : samples()) {
			if (sample != null) {
				try {
					String defaultValue = sample.getDefaultValue();
					assertTrue(
						"Attribute of wrong type with a default value",
						sample.getType() == exemplarAttribute.DEFAULT ||
							sample.getType() == exemplarAttribute.FIXED
					);
					assertNotNull("Attribute default was null", defaultValue);
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					assertFalse(
						"Attribute without default did not throw an exception",
						sample.getType() == exemplarAttribute.DEFAULT ||
							sample.getType() == exemplarAttribute.FIXED
					);
				}
			}
		}
	}

	/** Test {@link exemplarAttribute#getName()}. */
	public void testGetName() {
		for (exemplarAttribute sample : samples()) {
			if (sample != null) {
				try {
					String name = sample.getName();
					assertNotNull("Null name for attribute", name);
					assertNotSame("Empty name for attribute", "", name);
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					fail("getName() threw an exception");
				}
			}
		}
	}

	/** Test {@link exemplarAttribute#getType()}. */
	public void testGetType() {
		for (exemplarAttribute sample : samples()) {
			if (sample != null) {
				try {
					int type = sample.getType();
					assertTrue(
						"Type was not one of the defined types",
						type == exemplarAttribute.DEFAULT ||
							type == exemplarAttribute.FIXED ||
							type == exemplarAttribute.IMPLIED ||
							type == exemplarAttribute.REQUIRED
					);
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					fail("getType() threw an exception");
				}
			}
		}
	}

	/** Test {@link exemplarAttribute#getValue()}. */
	public void testGetValue() {
		for (exemplarAttribute sample : samples()) {
			if (sample != null) {
				try {
					sample.getValue();
				} catch (Exception e) {
					assertNotNull("Exception was null", e);
					fail("getValue() threw an exception");
				}
			}
		}
	}

	/** Test {@link exemplarAttribute#setValue(String)}. */
	public void testSetValue() {
		String[] sampleValues = {
			null,
			"",
			"foo",
			"bar",
		};
		for (exemplarAttribute sample : samples()) {
			if (sample != null) {
				if (sample.getType() != exemplarAttribute.FIXED) {
					try {
						String originalValue = sample.getValue();
						for (String sampleValue : sampleValues) {
							sample.setValue(sampleValue);
							assertEquals(
								"Value did not stick",
								sampleValue,
								sample.getValue()
							);
						}
						sample.setValue(originalValue);
						assertEquals(
							"Value did not reset",
							originalValue,
							sample.getValue()
						);
					} catch (Exception e) {
						assertNotNull("Exception was null", e);
						fail("{get,set}Value() threw an exception");
					}
				} else {
					boolean fellThrough = false;
					try {
						sample.setValue("foo");
						fellThrough = true;
					} catch (Exception e) {
						assertNotNull("Exception was null", e);
					}
					assertFalse(
						"setValue on a FIXED attribute fell through",
						fellThrough
					);
				}
			}
		}
	}
}
