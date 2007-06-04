// vim:filetype=java:ts=4
/*
	Copyright (c) 2006, 2007
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
package junit.com.mcdermottroe.exemplar.ui;

import java.util.Map;
import java.util.Set;

import com.mcdermottroe.exemplar.ui.Options;

import junit.com.mcdermottroe.exemplar.UtilityClassTestCase;

/** Test class for {@link Options}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class OptionsTest
extends UtilityClassTestCase<Options>
{
	/** Test {@link Options#allMandatoryOptionsSet()}. */
	public void testAllMandatoryOptionsSet() {
		try {
			Options.allMandatoryOptionsSet();
		} catch (AssertionError e) {
			assertNotNull("AssertionError", e);
			fail("allMandatoryOptionsSet() failed an assertion.");
		}
	}

	/** Test {@link Options#describeDefault(String)}. */
	public void testDescribeDefault() {
		for (String optionName : Options.getOptionNames()) {
			try {
				Options.describeDefault(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("describeDefault failed an assertion");
			}
		}
	}

	/** Test {@link Options#getBoolean(String)}. */
	public void testGetBoolean() {
		for (String optionName : Options.getOptionNames()) {
			Boolean bool = null;
			try {
				bool = Options.getBoolean(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getBoolean failed assertion");
			}
			if (Options.isSwitch(optionName)) {
				assertNotNull("Switch was null", bool);
			} else {
				assertNull("Non-switch was not null", bool);
			}
		}
	}

	/** Test {@link Options#getDescription(String)}. */
	public void testGetDescription() {
		for (String optionName : Options.getOptionNames()) {
			String desc = null;
			try {
				desc = Options.getDescription(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getDescription failed an assertion");
			}
			assertNotNull("getDescription returned null", desc);
			assertTrue(
				"getDescription returned a zero-length string",
				desc.length() > 0
			);
		}
		assertNull(
			"getDescription(null) did not return null",
			Options.getDescription(null)
		);
	}

	/** Test {@link Options#getEnumDescriptions(String)}. */
	public void testGetEnumDescriptions() {
		for (String optionName : Options.getOptionNames()) {
			Map<String, String> descriptions = null;
			try {
				descriptions = Options.getEnumDescriptions(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getEnumDescriptions was null");
			}
			if (Options.isEnum(optionName)) {
				assertNotNull(
					"getEnumDescriptions returned null for an enum",
					descriptions
				);
				assertFalse(
					"getEnumDescriptions returned an empty Map",
					descriptions.isEmpty()
				);
			} else {
				assertNull(
					"getEnumDescriptions returned non-null for a non-enum",
					descriptions
				);
			}
		}
	}

	/** Test {@link Options#getInteger(String)}. */
	public void testGetInteger() {
		for (String optionName : Options.getOptionNames()) {
			String stringValue = null;
			try {
				stringValue = Options.getString(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getString failed an assertion");
			}
			Integer integerValue = null;
			try {
				integerValue = Options.getInteger(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getInteger failed an assertion");
			}
			if (stringValue != null) {
				if (stringValue.matches("-?\\d+")) {
					assertNotNull("Integer value was null", integerValue);
				} else {
					assertNull("Bad integer value was not null", integerValue);
				}
			} else {
				assertNull(
					"String value was null, integer value was not",
					integerValue
				);
			}
		}
	}

	/** Test {@link Options#getString(String)}. */
	public void testGetString() {
		for (String optName : Options.getOptionNames()) {
			String stringValue = null;
			try {
				stringValue = Options.getString(optName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("getString failed an assertion");
			}
			if (Options.isSwitch(optName)) {
				assertNull(
					"getString returned non-null for a switch",
					stringValue
				);
			}
			if (Options.isEnum(optName) && Options.isMultiValue(optName)) {
				assertNull(
					"getString returned non-null for a multi-value enum",
					stringValue
				);
			}
		}
	}

	/** Test {@link Options#getOptionNames()}. */
	public void testGetOptionNames() {
		Set<String> optionNames = null;
		try {
			optionNames = Options.getOptionNames();
		} catch (AssertionError e) {
			assertNotNull("AssertionError", e);
			fail("getAllOptionNames() failed an assertion.");
		}
		assertNotNull("getAllOptionNames() == null", optionNames);
		assertFalse("No option names were returned.", optionNames.isEmpty());
	}

	/** Test {@link Options#isArgument(String)}. */
	public void testIsArgument() {
		for (String optionName : Options.getOptionNames()) {
			try {
				Options.isArgument(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isArgument failed an assertion");
			}
		}
	}

	/** Test {@link Options#isDebugSet()}. */
	public void testIsDebugSet() {
		try {
			Options.isDebugSet();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("isDebugSet failed an assertion");
		}
	}

	/** Test {@link Options#isEnum(String)}. */
	public void testIsEnum() {
		for (String optionName : Options.getOptionNames()) {
			try {
				Options.isEnum(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isEnum failed an assertion");
			}
		}
	}

	/** Test {@link Options#isInitialised()}. */
	public void testIsInitialised() {
		try {
			Options.isInitialised();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("isInitialised failed an assertion");
		}
	}

	/** Test {@link Options#isLegal(String)}. */
	public void testIsLegal() {
		for (String optionName : Options.getOptionNames()) {
			boolean legal = false;
			try {
				legal = Options.isLegal(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isLegal failed an assertion");
			}
			assertTrue("Illegal option", legal);
		}
		boolean legal = false;
		try {
			legal = Options.isLegal("deliberately-false-option-name");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("isLegal failed an assertion");
		}
		assertFalse("Illegal option marked as legal", legal);
	}

	/** Test {@link Options#isMandatory(String)}. */
	public void testIsMandatory() {
		for (String optionName : Options.getOptionNames()) {
			try {
				Options.isMandatory(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isMandatory failed an assertion");
			}
		}
		boolean mandatory = false;
		try {
			mandatory = Options.isLegal("deliberately-false-option-name");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("isMandatory failed an assertion");
		}
		assertFalse("Illegal option marked as mandatory", mandatory);
	}

	/** Test {@link Options#isMultiValue(String)}. */
	public void testIsMultiValue() {
		for (String optionName : Options.getOptionNames()) {
			boolean multiValue = false;
			try {
				multiValue = Options.isMultiValue(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isMultiValue failed an assertion");
			}
			if (!Options.isEnum(optionName)) {
				assertFalse(
					"Non-enum option reported as multi-value",
					multiValue
				);
			}
		}
	}

	/** Test {@link Options#isSet(String, String)}. */
	public void testIsSet() {
		for (String optionName : Options.getOptionNames()) {
			if (Options.isEnum(optionName)) {
				Set<String> enumNames;
				enumNames = Options.getEnumDescriptions(optionName).keySet();
				for (String enumName : enumNames) {
					try {
						Options.isSet(optionName, enumName);
					} catch (AssertionError e) {
						assertNotNull("AssertionError was null", e);
						fail("isSet failed an assertion");
					}
				}
			} else {
				assertFalse(
					"isSet returned true on a non-enum",
					Options.isSet(optionName, "foo")
				);
			}
		}
	}

	/** Test {@link Options#isSwitch(String)}. */
	public void testIsSwitch() {
		for (String optionName : Options.getOptionNames()) {
			try {
				Options.isSwitch(optionName);
			} catch (AssertionError e) {
				assertNotNull("AssertionError was null", e);
				fail("isSwitch failed an assertion");
			}
		}
	}

	/** Test {@link Options#reset()}. */
	public void testReset() {
		try {
			Options.reset();
		} catch (AssertionError e) {
			assertNotNull("AssertionError was not null", e);
			fail("reset() failed an assertion");
		}
	}

	/** Test {@link Options#set(String, String)}. */
	public void testSet() {
		try {
			Options.set("debug", "true");
		} catch (AssertionError e) {
			assertNotNull("AssertionError was null", e);
			fail("set(String, String) failed an assertion");
		}
	}

	/** Test {@link Options#setUIFinished()}. */
	public void testSetUIFinished() {
		Options.reset();
		Options.setUIFinished();
		assertTrue("setUIFinished did not work", Options.isInitialised());
	}
}
