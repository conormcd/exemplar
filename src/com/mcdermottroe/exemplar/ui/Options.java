// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006, 2007
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
package com.mcdermottroe.exemplar.ui;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.ui.options.Argument;
import com.mcdermottroe.exemplar.ui.options.Enum;
import com.mcdermottroe.exemplar.ui.options.Option;
import com.mcdermottroe.exemplar.ui.options.Switch;
import com.mcdermottroe.exemplar.utils.Resources;
import com.mcdermottroe.exemplar.utils.Strings;

import static com.mcdermottroe.exemplar.Constants.Character.COMMA;
import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;
import static com.mcdermottroe.exemplar.Constants.Options.ARGUMENT;
import static com.mcdermottroe.exemplar.Constants.Options.CASESENSITIVE_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.DEFAULT_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.DESCRIPTION_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.ENUM;
import static com.mcdermottroe.exemplar.Constants.Options.MANDATORY_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.MULTIVALUE_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.SWITCH;
import static com.mcdermottroe.exemplar.Constants.Options.TYPE_PROPERTY;
import static com.mcdermottroe.exemplar.Constants.Options.VALUE_PROPERTY;

/** A class to allow for the global tracking of options selected via any of the
	UI modules.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Options {
	/** The map containing all of the options. */
	private static Map<String, Option<?>> options = null;

	/** Whether or not the UI has finished setting the options. */
	private static boolean uiFinished = false;

	/** Special case for the debug option to prevent infinite looping in
		assertions.
	*/
	private static boolean debug = false;

	/** Private constructor to make sure that nobody can instantiate this
		class.
	*/
	private Options() {
		DBC.UNREACHABLE_CODE();
	}

	/** Try and initialise this class really early. */
	static {
		try {
			Message.localise();
		} catch (MessageException e) {
			DBC.IGNORED_EXCEPTION(e);
		}
		init();
	}

	/** Initialisation routine to get the options Map synced up with the
		.properties file which defines the legal options and their values. To
		avoid having to throw exceptions for nearly every method this routine
		guarantees that the options stash will be initialised, however it does
		NOT guarantee that there will be anything in it.
	*/
	private static void init() {
		// Make sure this only really runs if needed.
		if (options != null && !options.isEmpty()) {
			return;
		}
		Log.debug(Message.OPTIONS_INITIALISING());

		// The store for the options stash
		Map<String, Option<?>> initOptions = new HashMap<String, Option<?>>();

		// Load the options definitions from the properties file.
		Map<String, String> optionsDefinitions = Resources.get(Options.class);

		// Get the option names
		Set<String> optionNames = new HashSet<String>();
		for (String optionProp : optionsDefinitions.keySet()) {
			optionNames.add(
				optionProp.substring(0, optionProp.indexOf((int)FULL_STOP))
			);
		}

		// Go through the options by name and create Option objects for each of
		// them. Then store the Option objects in the stash.
		for (String optionName : optionNames) {
			// Get the type of the option
			String optionType = optionsDefinitions.get(
				Strings.join(FULL_STOP, optionName, TYPE_PROPERTY)
			);

			// Get the description of the option
			String optionDesc = optionsDefinitions.get(
				Strings.join(FULL_STOP, optionName, DESCRIPTION_PROPERTY)
			);

			// Get the default value(s) for the option
			String optionDefault = optionsDefinitions.get(
				Strings.join(FULL_STOP, optionName, DEFAULT_PROPERTY)
			);

			// Find out if the option is mandatory or not.
			boolean optionMandatory = Resources.getBoolean(
				Options.class,
				Strings.join(FULL_STOP, optionName, MANDATORY_PROPERTY),
				false
			);

			// Find out if the option takes multiple values or not
			boolean optionMultiValue = Resources.getBoolean(
				Options.class,
				Strings.join(FULL_STOP, optionName, MULTIVALUE_PROPERTY),
				false
			);

			// Find out if the option is case sensitive or not.
			boolean optionCaseSensitive = Resources.getBoolean(
				Options.class,
				Strings.join(FULL_STOP, optionName, CASESENSITIVE_PROPERTY),
				true
			);

			Option<?> newOption = null;
			if (optionType.equalsIgnoreCase(ENUM)) {
				// If the Enum is set from a method this is set with the name
				// of the method and its non-null status is used as a flag to
				// indicate the fact that the Enum is dynamic.
				String dynamicMethod = null;

				// Get the allowed values
				Map<String, String> allowedValues;
				allowedValues = new HashMap<String, String>();
				for (String entry : optionsDefinitions.keySet()) {
					String prefix = Strings.join(
						FULL_STOP,
						optionName,
						VALUE_PROPERTY
					);
					if (entry.startsWith(prefix)) {
						if (entry.equals(prefix)) {
							// This is a dynamically set method
							dynamicMethod = optionsDefinitions.get(entry);
						} else {
							// Get the value
							String entryValue = optionsDefinitions.get(entry);
							entry = entry.substring(prefix.length() + 1);
							allowedValues.put(entry, entryValue);
						}
					}
				}
				if (dynamicMethod != null) {
					if (allowedValues.isEmpty()) {
						String dynamicMethodClass = dynamicMethod.substring(
							0,
							dynamicMethod.lastIndexOf((int)FULL_STOP)
						);
						String dynamicMethodMethod = dynamicMethod.substring(
							dynamicMethodClass.length() + 1
						);

						try {
							// Get the class and method to call to get the
							// values
							Class<?> dMClass;
							dMClass = Class.forName(dynamicMethodClass);
							Method dMMethod = dMClass.getMethod(
								dynamicMethodMethod,
								(Class<?>[])null
							);

							// Ensure the method is public static
							int modifiers = dMMethod.getModifiers();
							DBC.ASSERT(Modifier.isPublic(modifiers));
							DBC.ASSERT(Modifier.isStatic(modifiers));

							// Ensure the method returns a SortedMap
							DBC.ASSERT(
								SortedMap.class.equals(dMMethod.getReturnType())
							);

							// Ensure that the method requires no parameters.
							DBC.ASSERT(
								dMMethod.getParameterTypes().length == 0
							);

							// Now call the method
							try {
								SortedMap<?, ?> dynValues;
								dynValues = SortedMap.class.cast(
									dMMethod.invoke(null)
								);
								for (Object key : dynValues.keySet()) {
									allowedValues.put(
										key.toString(),
										dynValues.get(key).toString()
									);
								}
							} catch (IllegalAccessException e) {
								DBC.IGNORED_EXCEPTION(e);
								DBC.UNREACHABLE_CODE();
							} catch (InvocationTargetException e) {
								DBC.IGNORED_EXCEPTION(e);
								DBC.UNREACHABLE_CODE();
							}
						} catch (ClassNotFoundException e) {
							DBC.IGNORED_EXCEPTION(e);
						} catch (NoSuchMethodException e) {
							DBC.IGNORED_EXCEPTION(e);
						}
					} else {
						// Should never get here.
						DBC.UNREACHABLE_CODE();
					}
				}

				// Get the default values
				Set<String> defaultValues = null;
				if (optionDefault != null) {
					String[] defVal = optionDefault.split(
						String.valueOf(COMMA)
					);
					defaultValues = new HashSet<String>(defVal.length, 1.0f);
					for (String defValue : defVal) {
						if (allowedValues.containsKey(defValue)) {
							defaultValues.add(defValue);
						}
					}
				}

				// Create the Enum
				newOption = new Enum(
										optionName,
										optionDesc,
										allowedValues,
										optionMandatory,
										optionMultiValue,
										optionCaseSensitive,
										defaultValues
									);
			} else if (optionType.equalsIgnoreCase(SWITCH)) {
				// Convert the default value to a boolean
				boolean defaultValue = Boolean.valueOf(optionDefault);

				// Create the new Switch
				newOption = new Switch(
											optionName,
											optionDesc,
											defaultValue
										);
			} else if (optionType.equalsIgnoreCase(ARGUMENT)) {
				// Create the new Argument
				newOption = new Argument(
											optionName,
											optionDesc,
											optionMandatory,
											optionCaseSensitive,
											optionDefault
										);
			}

			if (newOption != null) {
				initOptions.put(optionName, newOption);
			}
		}

		// Make the option stash creation an "almost atomic" operation.
		options = initOptions;
	}

	/** Reset the options back to the default. */
	public static void reset() {
		options.clear();
		init();
	}

	/** Allow a UI to notify other parts of the program that it has finished
		setting all the options that it's going to set.
	*/
	public static void setUIFinished() {
		uiFinished = true;

		// Set the special debug flag.
		debug = getBoolean("debug");	// NON-NLS
		if (debug) {
			Log.setLevel(LogLevel.DEBUG);
		}
	}

	/** Find out if debugging functionality is activated.

		@return True if debugging is active, false otherwise.
	*/
	public static boolean isDebugSet() {
		return debug;
	}

	/** Check if the options store has been initialised.

		@return	True if the options store has been initialised, false otherwise.
	*/
	public static boolean isInitialised() {
		return options != null && !options.isEmpty() && uiFinished;
	}

	/** Get the names of all the options.

		@return A Set of String representaions of the option names
	*/
	public static Set<String> getOptionNames() {
		// Ensure that the options stash is created
		init();

		// Put the Set of keys in a TreeSet to sort it.
		return new TreeSet<String>(options.keySet());
	}

	/** Get the value associated with a particular option.

		@param	optionName	The name of the option to return the value for.
		@return				The value associated with the given option name.
	*/
	private static Option<?> get(String optionName) {
		if (optionName != null) {
			init();
			if (isLegal(optionName)) {
				Option<?> ret = options.get(optionName);
				if (ret != null) {
					DBC.ASSERT(optionName.equals(ret.getName()));
					return ret;
				}
			}
		}
		return null;
	}

	/** Set the value of a particular option in the stash.

		@param	optionName	The name of the option to set.
		@param	optionValue	The value of the option to set.
	*/
	public static void set(String optionName, String optionValue) {
		// Ensure that the options stash is created
		init();

		// Make sure that the option being set is an allowed one
		if (isLegal(optionName)) {
			Option<?> optionToSet = get(optionName);
			List<String> vals = null;

			if (isEnum(optionName)) {
				// The string is a list of comma separated values
				String[] rawValues = optionValue.split(
					String.valueOf(COMMA)
				);
				vals = new ArrayList<String>(rawValues.length);
				for (String rawV : rawValues) {
					String rawValue;
					if (optionToSet.isCaseSensitive()) {
						rawValue = rawV;
					} else {
						rawValue = rawV.toLowerCase();
					}
					Map<String, String> enumValueMap;
					enumValueMap = Enum.class.cast(optionToSet).getEnumValues();
					if (enumValueMap.containsKey(rawValue)) {
						vals.add(rawValue);
					}
				}
			} else if (isSwitch(optionName)) {
				vals = new ArrayList<String>(1);
				vals.add(String.valueOf(Boolean.valueOf(optionValue)));
			} else if (isArgument(optionName)) {
				vals = new ArrayList<String>(1);
				if (optionToSet.isCaseSensitive()) {
					vals.add(optionValue);
				} else {
					vals.add(optionValue.toLowerCase());
				}
			}

			if (vals != null) {
				optionToSet.setValue(vals);
			}

			options.put(optionName, optionToSet);

			// Handle the special case of debug
			Boolean debugValue = getBoolean("debug");	// NON-NLS
			if (debugValue != null) {
				debug = debugValue;
			}
		}
	}

	/** Determine whether or not a given option name is legal.

		@param	optionName	The name of the option to check
		@return				True if the option name is legal, false otherwise.
	*/
	public static boolean isLegal(String optionName) {
		// Ensure that the options stash is created
		init();

		return options.keySet().contains(optionName);
	}

	/** Convenience routine for finding out if an {@link Option} is mandatory or
		not.

		@param	optionName	The name of the option to check.
		@return				True if the {@link Option} is mandatory, false
							otherwise.
	*/
	public static boolean isMandatory(String optionName) {
		// Ensure that the options stash is created
		init();

		Option<?> opt = get(optionName);
		if (opt != null) {
			return opt.isMandatory();
		} else {
			return false;
		}
	}

	/** Convenience routine for finding out if an {@link Option} is a
		multi-value type or not.

		@param	optionName	The name of the {@link Option} to check.
		@return				True if the {@link Option} is a multi-value, false
							otherwise.
	*/
	public static boolean isMultiValue(String optionName) {
		// Ensure that the options stash is created
		init();

		Option<?> opt = get(optionName);
		if (opt != null) {
			return opt.isMultiValue();
		} else {
			return false;
		}
	}

	/** Get the description registered for the given option.

		@param optionName	The name of the option the description of which to
							fetch.
		@return				The description of the given option.
	*/
	public static String getDescription(String optionName) {
		// Ensure that the options stash is created
		init();

		Option<?> opt = get(optionName);
		if (opt != null && opt.getDescription() != null) {
			return opt.getDescription();
		}
		return null;
	}

	/** Get the descriptions of the valid values for a given {@link Enum}.

		@param	optionName	The name of the {@link Enum}.
		@return				A {@link Map} where the keys are the names of the
							allowed {@link Enum} values and the values are the
							descriptions of those values. Returns null if the
							option requested is not an {@link Enum}.
	*/
	public static Map<String, String> getEnumDescriptions(String optionName) {
		// Ensure that the options stash is created
		init();

		Option<?> opt = get(optionName);
		if (opt != null && isEnum(optionName)) {
			return Enum.class.cast(opt).getEnumValues();
		}
		return null;
	}

	/** Format the current value of the {@link Option} as a human readable
		{@link String}.

		@param	optionName	The name of the {@link Option} the value of which to
							describe.
		@return				A human readable representation of the requested
							object.
	*/
	public static String describeDefault(String optionName) {
		// Ensure that the options stash is created
		init();

		String returnValue = null;
		if (isArgument(optionName)) {
			returnValue = getString(optionName);
		} else if (isSwitch(optionName)) {
			returnValue = String.valueOf(getBoolean(optionName));
		} else if (isEnum(optionName)) {
			Option<?> opt = get(optionName);
			if	(
					opt != null &&
					opt.getValue() != null &&
					!opt.getValue().isEmpty()
				)
			{
				int count = 0;
				StringBuilder ret = new StringBuilder();
				for (Object next : opt.getValue()) {
					if (count == 0) {
						ret.append(next.toString());
					} else {
						ret.append(COMMA);
						ret.append(next.toString());
					}
					count++;
				}
				returnValue = ret.toString();
			}
		}
		return returnValue;
	}

	/** Convenience routine for getting the value of an {@link Option} which is
		known to be a {@link String}.

		@param	optionName	The name of the {@link Option} to fetch.
		@return				A {@link String} if the option holds a {@link
							String}, returns null otherwise.
	*/
	public static String getString(String optionName) {
		// Ensure that the options stash is created
		init();

		String returnValue = null;
		Option<?> opt = get(optionName);
		if (opt != null) {
			if (isArgument(optionName)) {
				if (opt.getValue().size() == 1) {
					Object obj = opt.getValue().get(0);
					if (obj != null && obj instanceof String) {
						returnValue = (String)obj;
					}
				}
			} else if (isEnum(optionName)) {
				if (!opt.isMultiValue()) {
					if (opt.getValue().size() == 1) {
						Object obj = opt.getValue().get(0);
						if (obj != null && obj instanceof String) {
							returnValue = (String)obj;
						}
					}
				}
			}
		}
		return returnValue;
	}

	/** Convenience routine for getting the value of an {@link Option} which is
		known to be a {@link Boolean}.

		@param	optionName	The name of the {@link Option} to fetch.
		@return				A {@link Boolean} if the option holds a {@link
							Boolean}, null otherwise.
	*/
	public static Boolean getBoolean(String optionName) {
		Option<?> opt = get(optionName);
		if (opt != null && isSwitch(optionName)) {
			if (opt.getValue().size() == 1) {
				String s = opt.getValue().get(0);
				if (s != null) {
					return Boolean.valueOf(s);
				}
			}
		}
		return null;
	}

	/** Convenience routine for getting the value of an {@link Option} which is
		known to be a {@link Integer}.

		@param	optionName	The name of the {@link Option} to fetch.
		@return				A {@link Integer} if the option holds a {@link
							Integer}, null otherwise.
	*/
	public static Integer getInteger(String optionName) {
		String number = getString(optionName);
		if (number != null) {
			try {
				return Integer.valueOf(number);
			} catch (NumberFormatException e) {
				// Ignore
				DBC.IGNORED_EXCEPTION(e);
			}
		}
		return null;
	}

	/** Convenience routine for testing whether or not a particular value in
		an {@link Enum} is set.

		@param	enumName	The name of the {@link Enum}
		@param	enumValue	The value to query
		@return				true if the value is present in the set of values
							for that {@link Enum}, false if it is not, throws an
							exception for all other conditions.
	*/
	public static boolean isSet(String enumName, String enumValue) {
		// Ensure that the options stash is created
		init();

		Option<?> opt = get(enumName);
		if (opt != null && isEnum(enumName)) {
			for (Object val : opt.getValue()) {
				if (val instanceof String) {
					if (val.equals(enumValue)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/** Check that all of the mandatory options have been set.

		@return	True if all of the mandatory options have been given a value,
				false otherwise.
	*/
	public static boolean allMandatoryOptionsSet() {
		// Ensure that the options stash is created
		init();

		for (String optionName : options.keySet()) {
			if (isMandatory(optionName)) {
				Option<?> opt = get(optionName);
				if	(
						opt == null ||
						opt.getValue() == null ||
						opt.getValue().isEmpty()
					)
				{
					Log.warning(Message.MISSING_MANDATORY_OPTION(optionName));
					return false;
				}
			}
		}

		return true;
	}

	/** Check if the named option is an {@link Argument}, in other words, it
		takes one value only.

		@param	optionName	The name of the option to check.
		@return				True if the option is an {@link Argument}, false
							otherwise.
	*/
	public static boolean isArgument(String optionName) {
		// Ensure that the options stash is created
		init();

		return Argument.class.isAssignableFrom(get(optionName).getClass());
	}

	/** Check if the named option is an {@link Enum}, in other words, it takes
		one or more values all of which must be from a specific set of values.

		@param	optionName	The name of the option to check.
		@return				True if the option is an {@link Enum}, false
							otherwise.
	*/
	public static boolean isEnum(String optionName) {
		// Ensure that the options stash is created
		init();

		return Enum.class.isAssignableFrom(get(optionName).getClass());
	}

	/** Check if the named option is a {@link Switch}, in other words, it takes
		no value. The presence of the option indicates "true" and the absence
		"false".

		@param	optionName	The name of the option to check.
		@return				True if the option is a {@link Switch}, false
							otherwise.
	*/
	public static boolean isSwitch(String optionName) {
		// Ensure that the options stash is created
		init();

		return Switch.class.isAssignableFrom(get(optionName).getClass());
	}
}
