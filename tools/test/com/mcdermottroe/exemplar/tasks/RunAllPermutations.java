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
package test.com.mcdermottroe.exemplar.tasks;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.Task;

import com.mcdermottroe.exemplar.ui.Options;

/** Run exemplar with all option permutations via all UIs.

	@author	Conor McDermottroe
	@since	0.1
*/
public class RunAllPermutations extends Task {
	/** {@link com.mcdermottroe.exemplar.ui.Options.Argument}s can take any
		value, hence to test them we need to supply "hint" values.
	*/
	private Set argumentHint;

	/** Create the task. */
	public RunAllPermutations() {
		super();
		argumentHint = new HashSet();
	}

	/** Run all of the permutations. */
	public void execute() {
		SortedMap optionPossibilities = new TreeMap();
		for (Iterator it = Options.optionNameIterator(); it.hasNext(); ) {
			String optionName = (String)it.next();

			SortedSet possibilities = null;
			if (Options.isArgument(optionName)) {
				possibilities = new TreeSet();

				Iterator hintIter = argumentHint.iterator();
				while (hintIter.hasNext()) {
					ArgumentHint aH = (ArgumentHint)hintIter.next();
					if (optionName.equals(aH.getName())) {
						possibilities.add(aH.getValue());
					}
				}
			} else if (Options.isEnum(optionName)) {
				possibilities = new TreeSet();

				Set enumSet = Options.getEnumDescriptions(optionName).keySet();
				String[] enumVals = new String[enumSet.size()];
				enumVals = (String[])enumSet.toArray(enumVals);

				if (Options.isMultiValue(optionName)) {
					int inc = enumVals.length;
					if (inc == 0) {
						inc = 1;
					}
					for (long l = 0L; l < pow(enumVals.length, enumVals.length + 1); l += inc) {
						SortedSet combo = new SortableSortedSet();
						long t = l;
						for (int i = enumVals.length; i > 0; i--) {
							long index = t / pow(enumVals.length, i);
							t %= pow(enumVals.length, i);
							if (index <= Integer.MAX_VALUE) {
								combo.add(enumVals[(int)index]);
							} else {
								throw new BuildException("Too many options, this will never finish");
							}
						}
						possibilities.add(combo);
					}
				} else {
					for (int i = 0; i < enumVals.length; i++) {
						SortedSet combo = new SortableSortedSet();
						combo.add(enumVals[i]);
						possibilities.add(combo);
					}
				}
			} else if (Options.isSwitch(optionName)) {
				// Don't bother filling in the possibilities here as it's just
				// an on/off thing.
			} else {
				throw new BuildException(optionName + " is not a known type");
			}

			optionPossibilities.put(optionName, possibilities);
		}

		Iterator opi = new OptionsPermutationIterator(optionPossibilities);
		while (opi.hasNext()) {
			opi.next();
		}
	}

	/** Why is there no integer-only pow function in Java?

		@param	number	The number to raise to <code>pow</code>.
		@param	power	The power to raise <code>number</code> to.
		@return			<code>number</code> to the power of <code>power</code>.
	*/
	private static long pow(int number, int power) {
		return StrictMath.round(
			StrictMath.pow(number, power)
		);
	}

	/** A sortable version of {@link SortedSet}.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static class SortableSortedSet
	extends TreeSet
	implements Comparable
	{
		/** Implementation of {@link Comparable#compareTo(Object)}.

			@param	o	The other {@link Object} to compare <code>this</code>
						against.
			@return		-1, 0 or 1.
		*/
		public int compareTo(Object o) {
			// We want to be ordered /after/ null
			if (o == null) {
				return 1;
			}

			SortedSet other = (SortedSet)o;

			// Handle the empty cases
			if (isEmpty() && other.isEmpty()) {
				return 0;
			}
			if (!isEmpty() && other.isEmpty()) {
				return 1;
			}
			if (isEmpty() && !other.isEmpty()) {
				return -1;
			}

			// If we're bigger, we come after
			if (size() > other.size()) {
				return 1;
			}
			// vice-versa
			if (size() < other.size()) {
				return -1;
			}

			// If we make it this far, the two sorted sets are equal in size
			// and neither are empty.
			Iterator thisIter = iterator();
			Iterator otherIter = other.iterator();
			while (thisIter.hasNext()) {
				Comparable thisObj = (Comparable)thisIter.next();
				Comparable otherObj = (Comparable)otherIter.next();
				int comp = thisObj.compareTo(otherObj);
				if (comp != 0) {
					return comp;
				}
			}

			return 0;
		}
	}

	private static class OptionsPermutationIterator
	implements Iterator
	{
		/** The translation table between indices and option names. */
		private String[] optionName;

		/** The current index into each list of options. */
		private int[] index;

		/** The maximum index permissible for each option. */
		private int[] maxIndex;

		/** The values allowed for each option. */
		private List[] values;

		/** Create a new OptionsPermutationIterator.

			@param	options	The options to permute through. The keys in this
							map are option names and the values are {@link
							Set}s of possible value for the options.
		*/
		private OptionsPermutationIterator(Map options) {
			optionName = new String[options.keySet().size()];
			index = new int[options.keySet().size()];
			maxIndex = new int[options.keySet().size()];
			values = new List[options.keySet().size()];

			int i = 0;
			for (Iterator it = options.keySet().iterator(); it.hasNext();) {
				optionName[i] = (String)it.next();
				if (Options.isMandatory(optionName[i])) {
					index[i] = 0;
				} else {
					index[i] = -1;
				}
				if (Options.isSwitch(optionName[i])) {
					maxIndex[i] = 1;
				} else {
					SortedSet vals = (SortedSet)options.get(optionName[i]);
					maxIndex[i] = vals.size() - 1;
					values[i] = new ArrayList(vals.size());
					values[i].addAll(vals);
				}
				i++;
			}
		}

		public boolean hasNext() {
			for (int i = 0; i < index.length; i++) {
				if (index[i] < maxIndex[i]) {
					return true;
				}
			}
			return false;
		}

		public Object next() {
			// Make sure it's legal to call this.
			if (!hasNext()) {
				throw new NoSuchElementException();
			}

			// Increment the indices
			for (int i = index.length - 1; i >= 0; i--) {
				index[i]++;
				if (index[i] <= maxIndex[i]) {
					break;
				}
				// Rotate back to the initial number.
				if (Options.isMandatory(optionName[i])) {
					index[i] = 0;
				} else {
					index[i] = -1;
				}
			}

//			SortedMap retVal = new TreeMap();
			for (int i = 0; i < index.length; i++) {
				if (index[i] >= 0) {
					System.out.print(" --");
					System.out.print(optionName[i]);
					if (! Options.isSwitch(optionName[i])) {
						Object optionValue = values[i].get(index[i]);
						System.out.print(" ");
						if (optionValue instanceof Set) {
							String val = null;
							for (Iterator it = ((Set)optionValue).iterator(); it.hasNext(); ) {
								String next = (String)it.next();
								if (val != null) {
									val = val + "," + next;
								} else {
									val = next;
								}
							}
							System.out.print(val);
						} else {
							System.out.print(optionValue);
						}
					}
				}
			}
			System.out.println();
			return null;
		}

		public void remove() {
			throw new UnsupportedOperationException();
		}
	}

	public ArgumentHint createArgumentHint() {
		ArgumentHint argHint = new ArgumentHint();
		argumentHint.add(argHint);
		return argHint;
	}

	public static class ArgumentHint {
		private String name;
		private String value;

		public ArgumentHint() {
			name = null;
			value = null;
		}

		public String getName() {
			return name;
		}

		public void setName(String argName) {
			if (argName == null) {
				throw new BuildException("name must not be set to null.");
			}
			if (Options.isArgument(argName)) {
				name = argName;
			} else {
				throw new BuildException(argName + " is not a valid argument name.");
			}
		}

		public String getValue() {
			return value;
		}

		public void setValue(String argValue) {
			if (argValue == null) {
				throw new BuildException("value must not be set to null.");
			}
			value = argValue;
		}
	}
}
