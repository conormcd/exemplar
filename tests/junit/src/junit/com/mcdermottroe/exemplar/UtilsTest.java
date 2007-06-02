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
package junit.com.mcdermottroe.exemplar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.mcdermottroe.exemplar.Utils;

/** Test class for {@link com.mcdermottroe.exemplar.Utils}.

	@author	Conor McDermottroe
	@since	0.1
*/
public class UtilsTest
extends UtilityClassTestCase<Utils>
{
	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two null
		references.
	*/
	public void testAreDeeplyEqualNullNull() {
		assertTrue(
			"null is equal to null",
			Utils.areDeeplyEqual(null, null)
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with one null and one
		non-null reference.
	*/
	public void testAreDeeplyEqualNullObject() {
		assertFalse(
			"null is not equal to a non-null Object",
			Utils.areDeeplyEqual(null, "NOT NULL")	// NON-NLS
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with one null and one
		non-null reference.
	*/
	public void testAreDeeplyEqualObjectNull() {
		assertFalse(
			"A non-null Object is not equal to null",
			Utils.areDeeplyEqual("NOT NULL", null)	// NON-NLS
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two non-null,
		equal {@link Object}s.
	*/
	public void testAreDeeplyEqualObjectObjectEqual() {
		assertTrue(
			"Two equal objects are equal",
			Utils.areDeeplyEqual("EQUAL", "EQUAL")	// NON-NLS
		);
	}

	/** Test {@link Utils#areDeeplyEqual(Object, Object)} with two non-null,
		non-equal {@link Object}s.
	*/
	public void testAreDeeplyEqualObjectObjectNotEqual() {
		assertFalse(
			"Two non-equal objects are non-equal",
			Utils.areDeeplyEqual("EQUAL", "NOT EQUAL")	// NON-NLS
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with two null
		arrays.
	*/
	public void testAreAllDeeplyEqualTwoNull() {
		assertTrue(
			"Two null arrays are equal",
			Utils.areAllDeeplyEqual(null, null)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with one null
		and one empty array.
	*/
	public void testAreAllDeeplyEqualOneNullOneEmpty() {
		String[] a = {};
		assertFalse(
			"A null array is different from an empty one",
			Utils.areAllDeeplyEqual(null, a)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with one empty
		and one null array.
	*/
	public void testAreAllDeeplyEqualOneEmptyOneNull() {
		String[] a = {};
		assertFalse(
			"An empty array is different from a null one",
			Utils.areAllDeeplyEqual(a, null)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with two empty
		arrays.
	*/
	public void testAreAllDeeplyEqualTwoEmpty() {
		String[] a = {};
		String[] b = {};
		assertTrue(
			"Two empty arrays are equal",
			Utils.areAllDeeplyEqual(a, b)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with one empty
		and one populated array.
	*/
	public void testAreAllDeeplyEqualOneEmptyOnePopulated() {
		String[] a = {};
		String[] b = {"foo"}; // NON-NLS
		assertFalse(
			"An emtpy array is different from a populated one",
			Utils.areAllDeeplyEqual(a, b)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with one
		populated and one empty array.
	*/
	public void testAreAllDeeplyEqualOnePopulatedOneEmpty() {
		String[] a = {"foo"};	// NON-NLS
		String[] b = {};
		assertFalse(
			"A populated array is different from an emtpy one",
			Utils.areAllDeeplyEqual(a, b)
		);
	}

	/** Test {@link Utils#areAllDeeplyEqual(Object[], Object[])} with two
		populated equal arrays.
	*/
	public void testAreAllDeeplyEqualTwoEqualPopulated() {
		String[] a = {"foo"};	// NON-NLS
		String[] b = {"foo"}; // NON-NLS
		assertTrue(
			"Two populated arrays are equal",
			Utils.areAllDeeplyEqual(a, b)
		);
	}

	/** Test {@link Utils#compare(Comparable, Object)}. */
	public void testCompare() {
		List<String> emptyList = new ArrayList<String>(0);
		List<String> oneElementList = new ArrayList<String>(1);
		oneElementList.add("one");
		List<String> twoElementList = new ArrayList<String>(2);
		twoElementList.add("one");
		twoElementList.add("two");
		List<String> threeElementList = new ArrayList<String>(3);
		threeElementList.add("one");
		threeElementList.add("two");
		threeElementList.add("three");
		Object[][] input = {
			{null, null},
			{null, ""},
			{"", null},
			{"", ""},
			{"foo", "foo"},
			{"foo", "bar"},
			{"bar", "foo"},
			{null, emptyList},
			{emptyList, null},
			{emptyList, emptyList},
			{emptyList, oneElementList},
			{oneElementList, emptyList},
			{oneElementList, oneElementList},
			{oneElementList, twoElementList},
			{twoElementList, oneElementList},
			{twoElementList, twoElementList},
			{twoElementList, threeElementList},
			{threeElementList, twoElementList},
			{threeElementList, threeElementList},
		};
		int[] expected = {
			0,
			-1,
			1,
			0,
			0,
			1,
			-1,
			-1,
			1,
			0,
			-1,
			1,
			0,
			-1,
			1,
			0,
			-1,
			1,
			0,
		};

		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals("Broken test data", 2, input[i].length);
			try {
				if (input[i][0] instanceof Collection) {
					assertEquals(
						"Output did not match expected",
						expected[i],
						Integer.signum(
							Utils.compare(
								(Collection)input[i][0],
								(Collection)input[i][1]
							)
						)
					);
				} else {
					assertEquals(
						"Output did not match expected",
						expected[i],
						Integer.signum(
							Utils.compare(
								(Comparable)input[i][0],
								input[i][1]
							)
						)
					);
				}
			} catch (ClassCastException e) {
				assertNotNull("ClassCastException was null", e);
				fail("Broken test data");
			}
		}
	}

	/** Test {@link Utils#genericHashCode(Object[])}. */
	public void testGenericHashCode() {
		List<String> list = new ArrayList<String>();
		list.add("foo");
		Object[][] input = {
			{null},
			{null, null},
			{null, null, null},
			{"foo"},
			{"foo", "foo"},
			{new ArrayList()},
			{list},
		};
		int[] expected = {
			0,
			0,
			0,
			2945646,
			88369380,
			0,
			2945646,
		};

		assertEquals("Broken test data", input.length, expected.length);
		for (int i = 0; i < input.length; i++) {
			assertEquals(
				"Output did not match expected output",
				expected[i],
				Utils.genericHashCode(input[i])
			);
		}
	}
}
