// vim:filetype=java:ts=4
/*
	Copyright (c) 2005, 2006
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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;

/** A class to allow for the global tracking of options selected via any of the
	UI modules.

	@author	Conor McDermottroe
	@since	0.1
*/
public final class Options
implements Constants.Options
{
	/** The map containing all of the options. */
	private static Map options;

	/** Whether or not the UI has finished setting the options. */
	private static boolean uiFinished;

	/** Special case for the debug option to prevent infinite looping in
		assertions.
	*/
	private static boolean debug;

	/** Private constructor to make sure that nobody can instantiate this
		class.
	*/
	private Options() {
		DBC.UNREACHABLE_CODE();
	}

	/** Try and initialise this class really early. */
	static {
		options = null;
		debug = false;
		uiFinished = false;
		try {
			Message.localise();
		} catch (MessageException e) {
			// This error can be dealt with later.
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

		// The store for the options stash
		Map initOptions = new HashMap();

		// Load the options definitions from the properties file.
		ResourceBundle optionsDefinitions = null;
		try {
			optionsDefinitions = ResourceBundle.getBundle(
				Options.class.getName()
			);
		} catch (MissingResourceException e) {
			// Ignore
			DBC.IGNORED_ERROR();
		}
		DBC.ASSERT(optionsDefinitions != null);

		// Get the names of all the options
		Set optionNames = new HashSet();
		if (optionsDefinitions != null) {
			Enumeration e = optionsDefinitions.getKeys();
			while (e.hasMoreElements()) {
				String optionName = (String)e.nextElement();
				optionName = optionName.substring(
					0,
					optionName.indexOf((int)Constants.Character.FULL_STOP)
				);
				optionNames.add(optionName);
			}
		}

		// Go through the options by name and create Option objects for each of
		// them. Then store the Option objects in the stash.
		for (Iterator it = optionNames.iterator(); it.hasNext(); ) {
			String optionName = (String)it.next();

			// Get the type of the option
			String optionType = readOptionStringProperty(
				optionsDefinitions,
				optionName,
				TYPE_PROPERTY
			);
			if (optionType == null) {
				DBC.IGNORED_ERROR();
				continue;
			}

			// Get the description of the option
			String optionDesc = readOptionStringProperty(
				optionsDefinitions,
				optionName,
				DESCRIPTION_PROPERTY
			);
			if (optionDesc == null) {
				DBC.IGNORED_ERROR();
				continue;
			}

			// Get the default value(s) for the option
			String optionDefault = readOptionStringProperty(
				optionsDefinitions,
				optionName,
				DEFAULT_PROPERTY
			);

			// Find out if the option is mandatory or not.
			boolean optionMandatory = readOptionBooleanProperty(
				optionsDefinitions,
				optionName,
				MANDATORY_PROPERTY,
				false
			);

			// Find out if the option takes multiple values or not
			boolean optionMultiValue = readOptionBooleanProperty(
				optionsDefinitions,
				optionName,
				MULTIVALUE_PROPERTY,
				false
			);

			// Find out if the option is case sensitive or not.
			boolean optionCaseSensitive = readOptionBooleanProperty(
				optionsDefinitions,
				optionName,
				CASESENSITIVE_PROPERTY,
				true
			);

			Option newOption = null;
			if (optionType.equalsIgnoreCase(ENUM)) {
				// If the Enum is set from a method this is set with the name
				// of the method and its non-null status is used as a flag to
				// indicate the fact that the Enum is dynamic.
				String dynamicMethod = null;

				// Get the allowed values
				Map allowedValues = new HashMap();
				Enumeration entries = optionsDefinitions.getKeys();
				while (entries.hasMoreElements()) {
					String entry = (String)entries.nextElement();
					String prefix =	optionName +
									Constants.Character.FULL_STOP +
									VALUE_PROPERTY;
					if (entry.startsWith(prefix)) {
						if (entry.equals(prefix)) {
							// It is an error to be both dynamic and have fixed
							// values
							if (!allowedValues.isEmpty()) {
								DBC.IGNORED_ERROR();
								break;
							}

							// This is a dynamically set method
							dynamicMethod = optionsDefinitions.getString(entry);
						} else {
							// It is an error to be both dynamic and have fixed
							// values
							if (dynamicMethod != null) {
								DBC.IGNORED_ERROR();
								break;
							}

							// Get the value
							String entryValue = optionsDefinitions.getString(
								entry
							);
							entry = entry.substring(
								optionName.length() +
								VALUE_PROPERTY.length() +
								2
							);
							allowedValues.put(entry, entryValue);
						}
					}
				}
				if (dynamicMethod != null) {
					if (allowedValues.isEmpty()) {
						String dynamicMethodClass = dynamicMethod.substring(
							0,
							dynamicMethod.lastIndexOf(
								(int)Constants.Character.FULL_STOP
							)
						);
						String dynamicMethodMethod = dynamicMethod.substring(
							dynamicMethodClass.length() + 1
						);

						try {
							// Get the class and method to call to get the
							// values
							Class dMClass = Class.forName(dynamicMethodClass);
							Method dMMethod = dMClass.getMethod(
								dynamicMethodMethod,
								null
							);

							// Ensure the method is public static
							int modifiers = dMMethod.getModifiers();
							if	(
									!(
										Modifier.isPublic(modifiers) &&
										Modifier.isStatic(modifiers)
									)
								)
							{
								DBC.IGNORED_ERROR();
							}

							// Ensure the method returns a Map
							Class dMMethodReturnType = dMMethod.getReturnType();
							if (!SortedMap.class.equals(dMMethodReturnType)) {
								DBC.IGNORED_ERROR();
							}

							// Ensure that the method requires no parameters.
							if (dMMethod.getParameterTypes().length != 0) {
								DBC.IGNORED_ERROR();
							}

							// Now call the method
							try {
								Class[] args = {};
								SortedMap dynamicValues;
								dynamicValues = (SortedMap)dMMethod.invoke(
									null,
									args
								);
								allowedValues.putAll(dynamicValues);
							} catch (IllegalAccessException e) {
								DBC.UNREACHABLE_CODE();
							} catch (InvocationTargetException e) {
								DBC.UNREACHABLE_CODE();
							}
						} catch (ClassNotFoundException e) {
							DBC.IGNORED_ERROR();
						} catch (NoSuchMethodException e) {
							DBC.IGNORED_ERROR();
						}
					} else {
						// Should never get here.
						DBC.UNREACHABLE_CODE();
					}
				}

				// Get the default values
				Set defaultValues = null;
				if (optionDefault != null) {
					String[] defVal = optionDefault.split(
						String.valueOf(Constants.Character.COMMA)
					);
					defaultValues = new HashSet(defVal.length, 1.0f);
					for (int j = 0; j < defVal.length; j++) {
						if (allowedValues.containsKey(defVal[j])) {
							defaultValues.add(defVal[j]);
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
				Boolean optDefault = Boolean.valueOf(optionDefault);
				boolean defaultValue = optDefault.booleanValue();

				// Create the new Switch
				newOption = new Switch	(
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

			DBC.ASSERT(newOption != null);
			if (newOption != null) {
				initOptions.put(optionName, newOption);
			}
		}

		// Make the option stash creation an "almost atomic" operation.
		options = initOptions;

		DBC.ENSURE(options != null);
	}

	/** Allow a UI to notify other parts of the program that it has finished
		setting all the options that it's going to set.
	*/
	public static void setUIFinished() {
		uiFinished = true;

		// Set the special debug flag.
		Boolean debugOpt = getBoolean("debug");
		if (debugOpt != null) {
			debug = debugOpt.booleanValue();
		} else {
			// If there hasn't been a debug option defined, then debugging
			// should be forced on.
			debug = true;
		}

		// Get the logger to ensure that the logger has the right log level.
		LogUtils.getLogger();
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

	/** Get an iterator over the list of names of options.

		@return	An Iterator over the collection of names of defined options.
	*/
	public static Iterator optionNameIterator() {
		// Ensure that the options stash is created
		init();

		// Put the Set of keys in a TreeSet to sort it.
		SortedSet keySet = new TreeSet(options.keySet());
		return keySet.iterator();
	}

	/** Get the names of all the options.

		@return A Set of String representaions of the option names
	*/
	public static Set getAllOptionNames() {
		// Ensure that the options stash is created
		init();

		// Put the Set of keys in a TreeSet to sort it.
		return new TreeSet(options.keySet());
	}

	/** Get the value associated with a particular option.

		@param	optionName	The name of the option to return the value for.
		@return				The value associated with the given option name.
	*/
	private static Option get(String optionName) {
		if (optionName != null) {
			init();
			if (isLegal(optionName)) {
				Option ret = (Option)options.get(optionName);
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
			Option optionToSet = get(optionName);
			List vals = null;

			if (optionToSet instanceof Enum) {
				// The string is a list of comma separated values
				String[] rawValues = optionValue.split(
					String.valueOf(Constants.Character.COMMA)
				);
				vals = new ArrayList(rawValues.length);
				for (int i = 0; i < rawValues.length; i++) {
					String rawValue;
					if (optionToSet.isCaseSensitive()) {
						rawValue = rawValues[i];
					} else {
						rawValue = rawValues[i].toLowerCase();
					}
					Map enumValueMap = ((Enum)optionToSet).getEnumValues();
					if (enumValueMap.containsKey(rawValue)) {
						vals.add(rawValue);
					}
				}
			} else if (optionToSet instanceof Switch) {
				vals = new ArrayList(1);
				vals.add(Boolean.valueOf(optionValue));
			} else if (optionToSet instanceof Argument) {
				vals = new ArrayList(1);
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
			Boolean debugValue = getBoolean("debug");
			if (debugValue != null) {
				debug = debugValue.booleanValue();
			}
		}
	}

	/** Determine whether or not a given option name is legal.

		@param	optionName	The name of the option to check
		@return				True if the option name is legal, false otherwise
	*/
	public static boolean isLegal(String optionName) {
		// Ensure that the options stash is created
		init();

		for (Iterator it = options.keySet().iterator(); it.hasNext(); ) {
			String legalOptionName = (String)it.next();
			if (optionName.equals(legalOptionName)) {
				return true;
			}
		}
		return false;
	}

	/** Convenience routine for finding out if an {@link
		com.mcdermottroe.exemplar.ui.Options.Option} is mandatory or not.

		@param	optionName	The name of the option to check.
		@return				True if the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} is
							mandatory, false otherwise.
	*/
	public static boolean isMandatory(String optionName) {
		// Ensure that the options stash is created
		init();

		Option opt = get(optionName);
		if (opt != null) {
			return opt.isMandatory();
		} else {
			return false;
		}
	}

	/** Convenience routine for finding out if an {@link
		com.mcdermottroe.exemplar.ui.Options.Option} is a multi-value
		type or not.

		@param	optionName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} to
							check.
		@return				True if the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} is a
							multi-value, false otherwise.
	*/
	public static boolean isMultiValue(String optionName) {
		// Ensure that the options stash is created
		init();

		Option opt = get(optionName);
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

		Option opt = get(optionName);
		if (opt != null && opt.getDescription() != null) {
			return opt.getDescription();
		}
		return null;
	}

	/** Get the descriptions of the valid values for a given {@link
		com.mcdermottroe.exemplar.ui.Options.Enum}.

		@param	optionName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Enum}.
		@return				A {@link Map} where the keys are the names of the
							allowed {@link
							com.mcdermottroe.exemplar.ui.Options.Enum} values
							and the values are the descriptions of those
							values. Returns null if the option requested is not
							an {@link
							com.mcdermottroe.exemplar.ui.Options.Enum}.
	*/
	public static Map getEnumDescriptions(String optionName) {
		// Ensure that the options stash is created
		init();

		Option opt = get(optionName);
		if (opt != null && opt instanceof Enum) {
			return ((Enum)opt).getEnumValues();
		}
		return null;
	}

	/** Format the current value of the {@link
		com.mcdermottroe.exemplar.ui.Options.Option} as a human readable {@link
		String}.

		@param	optionName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} the
							value of which to describe.
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
			Boolean bool = getBoolean(optionName);
			DBC.ASSERT(bool != null);
			if (bool != null) {
				returnValue = bool.toString();
			}
		} else if (isEnum(optionName)) {
			Option opt = get(optionName);
			if	(
					opt != null &&
					opt.getValue() != null &&
					!opt.getValue().isEmpty()
				)
			{
				int count = 0;
				StringBuffer ret = new StringBuffer();
				for (Iterator it = opt.getValue().iterator(); it.hasNext(); ) {
					String next = (String)it.next();
					if (count == 0) {
						ret.append(next);
					} else {
						ret.append(Constants.Character.COMMA);
						ret.append(next);
					}
					count++;
				}
				returnValue = ret.toString();
			}
		}
		return returnValue;
	}

	/** Convenience routine for getting the value of an {@link
		com.mcdermottroe.exemplar.ui.Options.Option} which is known to be a
		{@link String}.

		@param	optionName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} to
							fetch.
		@return				A {@link String} if the option holds a {@link
							String}, returns null otherwise.
	*/
	public static String getString(String optionName) {
		// Ensure that the options stash is created
		init();

		String returnValue = null;
		Option opt = get(optionName);
		if (opt != null) {
			if (opt instanceof Argument) {
				if (opt.getValue().size() == 1) {
					Object obj = opt.getValue().get(0);
					if (obj != null && obj instanceof String) {
						returnValue = (String)obj;
					}
				}
			} else if (opt instanceof Enum) {
				if (!opt.isMultiValue()) {
					DBC.ASSERT(opt.getValue().size() <= 1);
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

	/** Convenience routine for getting the value of an {@link
		com.mcdermottroe.exemplar.ui.Options.Option} which is known to be a
		{@link Boolean}.

		@param	optionName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Option} to
							fetch.
		@return				A {@link Boolean} if the option holds a {@link
							Boolean}, null otherwise.
	*/
	public static Boolean getBoolean(String optionName) {
		Option opt = get(optionName);
		if (opt != null && opt instanceof Switch) {
			if (opt.getValue().size() == 1) {
				Object obj = opt.getValue().get(0);
				if (obj != null && obj instanceof Boolean) {
					return (Boolean)obj;
				}
			}
		}
		return null;
	}

	/** Convenience routine for testing whether or not a particular value in
		an {@link com.mcdermottroe.exemplar.ui.Options.Enum} is set.

		@param	enumName	The name of the {@link
							com.mcdermottroe.exemplar.ui.Options.Enum}
		@param	enumValue	The value to query
		@return				true if the value is present in the set of values
							for that {@link
							com.mcdermottroe.exemplar.ui.Options.Enum}, false
							if it is not, throws an exception for all other
							conditions.
	*/
	public static boolean isSet(String enumName, String enumValue) {
		// Ensure that the options stash is created
		init();

		Option opt = get(enumName);
		if (opt != null && opt instanceof Enum) {
			for (Iterator it = opt.getValue().iterator(); it.hasNext(); ) {
				Object val = it.next();
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

		for (Iterator it = options.keySet().iterator(); it.hasNext(); ) {
			String optionName = (String)it.next();
			if (isMandatory(optionName)) {
				Option opt = get(optionName);
				if	(
						opt == null ||
						opt.getValue() == null ||
						opt.getValue().isEmpty()
					)
				{
					return false;
				}
			}
		}

		return true;
	}

	/** Check if the named option is an {@link
		com.mcdermottroe.exemplar.ui.Options.Argument}, in other words, it
		takes one value only.

		@param	optionName	The name of the option to check.
		@return				True if the option is an {@link
							com.mcdermottroe.exemplar.ui.Options.Argument},
							false otherwise.
	*/
	public static boolean isArgument(String optionName) {
		// Ensure that the options stash is created
		init();

		return get(optionName) instanceof Argument;
	}

	/** Check if the named option is an {@link
		com.mcdermottroe.exemplar.ui.Options.Enum}, in other words, it takes
		one or more values all of which must be from a specific set of values.

		@param	optionName	The name of the option to check.
		@return				True if the option is an {@link
							com.mcdermottroe.exemplar.ui.Options.Enum}, false
							otherwise.
	*/
	public static boolean isEnum(String optionName) {
		// Ensure that the options stash is created
		init();

		return get(optionName) instanceof Enum;
	}

	/** Check if the named option is a {@link
		com.mcdermottroe.exemplar.ui.Options.Switch}, in other words, it takes
		no value. The presence of the option indicates "true" and the absence
		"false".

		@param	optionName	The name of the option to check.
		@return				True if the option is a {@link
							com.mcdermottroe.exemplar.ui.Options.Switch}, false
							otherwise.
	*/
	public static boolean isSwitch(String optionName) {
		// Ensure that the options stash is created
		init();

		return get(optionName) instanceof Switch;
	}

	/** Read a {@link String} property of an option from the {@link
		ResourceBundle} containing the options definitions.

		@param	rb				The {@link ResourceBundle} containing the
								options definitions.
		@param	optionName		The name of the option the {@link String}
								property of which to check.
		@param	propertyName	The {@link String} property to read.
		@return					The {@link String} value of the property of the
								option.
	*/
	private static String readOptionStringProperty	(
														ResourceBundle rb,
														String optionName,
														String propertyName
													)
	{
		try {
			return rb.getString(
				optionName +
				Constants.Character.FULL_STOP +
				propertyName
			);
		} catch (MissingResourceException e) {
			return null;
		}
	}

	/** Read a boolean property of an option from the {@link ResourceBundle}
		containing the options definitions.

		@param	rb				The {@link ResourceBundle} containing the
								options definitions.
		@param	optionName		The name of the option the boolean property of
								which to check.
		@param	propertyName	The boolean property to check.
		@param	defaultValue	The default value to assign should the property
								not be defined in the {@link ResourceBundle}.
		@return					The boolean value of the property of the option.
	*/
	private static boolean readOptionBooleanProperty(
														ResourceBundle rb,
														String optionName,
														String propertyName,
														boolean defaultValue
													)
	{
		try {
			String prop = rb.getString(
				optionName +
				Constants.Character.FULL_STOP +
				propertyName
			);
			return Boolean.valueOf(prop).booleanValue();
		} catch (MissingResourceException e) {
			return defaultValue;
		}
	}

	/** An option data type.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private abstract static class Option {
		/** The name of the option. */
		protected String name;

		/** The current value(s) of the option. */
		protected List value;

		/** A textual description of the option. */
		protected String description;

		/** Whether the option must be present or not. */
		protected boolean mandatory;

		/** Whether or not the option takes multiple values. */
		protected boolean multiValue;

		/** Whether or not the option is case sensitive. */
		protected boolean caseSensitive;

		/** Null initialiser, sets everything to defaults. */
		protected Option() {
			name = "";
			value = null;
			description = "";
			mandatory = false;
			multiValue = false;
			caseSensitive = true;
		}

		/** Simple constructor which initialises all of the member variables
			with given values.

			@param	optionName			The value for the member name.
			@param	optionDesc			The value for the member description.
			@param	optionMandatory		The value for the member mandatory.
			@param	optionMultiValue	The value for the member multiValue.
			@param	optionCaseSensitive	The value for the member caseSensitive.
		*/
		protected Option(
							String optionName,
							String optionDesc,
							boolean optionMandatory,
							boolean optionMultiValue,
							boolean optionCaseSensitive
						)
		{
			name = optionName;
			value = null;
			description = optionDesc;
			mandatory = optionMandatory;
			multiValue = optionMultiValue;
			caseSensitive = optionCaseSensitive;
		}

		/** Accessor for the name member.

			@return The value of the name member.
		*/
		protected String getName() {
			return name;
		}

		/** Accessor for the value member.

			@return A copy of the value of the name member.
		*/
		protected List getValue() {
			return new ArrayList(value);
		}

		/** Setter for the value member.

			@param newValue The list to copy into the value member.
		*/
		protected void setValue(List newValue) {
			value = new ArrayList(newValue);
		}

		/** Accessor for the description member.

			@return The value of the description member.
		*/
		protected String getDescription() {
			return description;
		}

		/** Accessor for the mandatory member.

			@return The value of the mandatory member.
		*/
		protected boolean isMandatory() {
			return mandatory;
		}

		/** Accessor for the multiValue member.

			@return The value of the multiValue member.
		*/
		protected boolean isMultiValue() {
			return multiValue;
		}

		/** Accessor for the caseSensitive member.

			@return The value of the caseSensitive member.
		*/
		protected boolean isCaseSensitive() {
			return caseSensitive;
		}

		/** See {@link Object#toString()}.

			@return	A descriptive {@link String}
		*/
		public String toString() {
			StringBuffer desc = new StringBuffer(getClass().getName());
			desc.append(Constants.Character.LEFT_PAREN);
			desc.append(name);
			desc.append(Constants.Character.RIGHT_PAREN);
			return desc.toString();
		}
	}

	/** Internal class for dealing with arguments. Arguments are options that
		take one, free form value.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static class Argument extends Option {
		/** Constructor that just initializes the member variables.

			@param argName			The name of the argument.
			@param argDesc			A description of what the argument is for.
			@param isMandatory		Set if this option must be set by the user.
			@param isCaseSensitive	Set if the value passed is case sensitive.
			@param aDefaultValue	A default value for this option.
		*/
		protected Argument	(
								String argName,
								String argDesc,
								boolean isMandatory,
								boolean isCaseSensitive,
								String aDefaultValue
							)
		{
			super(argName, argDesc, isMandatory, false, isCaseSensitive);
			value = new ArrayList(1);
			if (aDefaultValue != null) {
				value.add(aDefaultValue);
			}
		}
	}

	/** Internal class for dealing with enumerated options. These are options
		that take one or more values from a fixed set.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static class Enum extends Option {
		/** The allowed values. */
		protected Map enumValues;

		/** Constructor that just initializes the member variables.

			@param enumName			The name of this enumerated option.
			@param enumDesc			A description of what this option is for.
			@param enumVals			The values that this option may take. This
									is a map where the keys are the values that
									the Enum may take and the values are the
									descriptions of what those values do.
			@param isMandatory		Set if this option must be set by the user.
			@param isMultiValue		Set if more than one value from the fixed
									set can be passed to this option.
			@param isCaseSensitive	Set if the value(s) given are case
									sensitive.
			@param defaultValues	Default value(s) to set this option to.
		*/
		protected Enum	(
							String enumName,
							String enumDesc,
							Map enumVals,
							boolean isMandatory,
							boolean isMultiValue,
							boolean isCaseSensitive,
							Set defaultValues
						)
		{
			super	(
						enumName,
						enumDesc,
						isMandatory,
						isMultiValue,
						isCaseSensitive
					);
			if (defaultValues != null) {
				value = new ArrayList(defaultValues);
			} else {
				value = new ArrayList();
			}
			enumValues = new HashMap(enumVals);
		}

		/** Accessor for the allowed values of this Enum.

			@return A copy of the {@link Map} where the keys are the allowed
					values for this Enum and the values are the descriptions of
					what the values do.
		*/
		protected Map getEnumValues() {
			return new TreeMap(enumValues);
		}
	}

	/** Internal class for representing switches. Switched are options that
		do not take a value. Their presence indicates an "on" state and their
		absence indicates an "off" state.

		@author	Conor McDermottroe
		@since	0.1
	*/
	private static class Switch extends Option {
		/** Constructor that just initializes the member variables.

			@param switchName		The name of this switch.
			@param switchDesc		A description of what this switch controls.
			@param aDefaultValue	A default value for this switch.
		*/
		protected Switch(
							String switchName,
							String switchDesc,
							boolean aDefaultValue
						)
		{
			super(switchName, switchDesc, false, false, false);
			value = new ArrayList(1);
			if (aDefaultValue) {
				value.add(Boolean.TRUE);
			} else {
				value.add(Boolean.FALSE);
			}
		}
	}
}
