// vim:filetype=java:ts=4
/*
	Copyright (c) 2003-2007
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
package com.mcdermottroe.exemplar.input.dtd;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mcdermottroe.exemplar.Constants;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

/** A symbol table for parameter entities.

	@author	Conor McDermottroe
	@since	0.1
*/
public class PEDeclTable
implements Cloneable
{
	/** Enumerated type for the type of parameter entity this is. */
	public enum ParameterEntityType {
		/** A parameter entity having an immediate value. */
		VALUE,
		/** A parameter entity referencing its content via a URI. */
		URI
	}

	/**	A table of parameter entities that have immediate values not URI
		references.
	*/
	private Map<String, String> table;

	/** A table of parameter entities that have URI references. */
	private Map<String, String> fileTable;

	/** Simple constructor, just initialises storage. */
	public PEDeclTable() {
		table = new HashMap<String, String>();
		fileTable = new HashMap<String, String>();
	}

	/** Getter for the table member.

		@return A copy of the table member.
	*/
	public Map<String, String> getTable() {
		return new HashMap<String, String>(table);
	}

	/** Getter for the fileTable member.

		@return A copy of the fileTable member.
	*/
	public Map<String, String> getFileTable() {
		return new HashMap<String, String>(fileTable);
	}

	/** Add a new parameter entity to the symbol table.

		@param	name						The name of the parameter entity
		@param	contents					Depending on the value of
											<code>type</code> this could be
											either a URI or the actual
											replacement text of the parameter
											entity.
		@param	type						Whether the contents contains
											replacement text or a URI.
		@throws ParameterEntityException	if the required paramters are null
											or the empty string or if the type
											is not one of the predefined types.
	*/
	public void addNewPE(String name, String contents, ParameterEntityType type)
	throws ParameterEntityException
	{
		DBC.REQUIRE(name != null);
		DBC.REQUIRE(contents != null);
		DBC.REQUIRE(
			type.equals(ParameterEntityType.VALUE) ||
			type.equals(ParameterEntityType.URI)
		);
		if	(
				name == null || name.length() == 0 ||
				contents == null || contents.length() < 0
			)
		{
			throw new ParameterEntityException(Message.DTDPE_INVALID_PEDECL);
		}

		switch (type) {
			case VALUE:
				// The parameter entity contains content only
				if (!table.containsKey(name)) {
					table.put(name, replacePERefs(contents));
				}
				break;
			case URI:
				// The parameter entity contains a URI reference only
				if (!fileTable.containsKey(name)) {
					fileTable.put(name, replacePERefs(contents));
				}
				break;
			default:
				DBC.UNREACHABLE_CODE();
		}
	}

	/** Given the name of a parameter entity this creates an {@link
	 	InputStreamReader} suitable for pushing onto the input.

		@param name							The name of the parameter entity to
											create the reader from
		@param dtdPath						The path to the DTD the PERef
											appeared in.
		@return								An instance of {@link
											InputStreamReader} if the
											resolution of the entity was
											successful, null otherwise.
		@throws ParameterEntityException	if the entity cannot be resolved.
		@see InputStreamReader
	*/
	public Reader peRefReader(String name, String dtdPath)
	throws ParameterEntityException
	{
		DBC.REQUIRE(name != null);
		DBC.REQUIRE(dtdPath != null);

		Reader peRefReader;
		if (fileTable.containsKey(name)) {
			// Get the entry from the table
			String fileTableEntry = fileTable.get(name);

			try {
				// Construct the path to the file
				String pathToFile;
				if (fileTableEntry.startsWith(File.separator)) {
					pathToFile = fileTableEntry;
				} else {
					pathToFile = dtdPath + File.separator + fileTableEntry;
				}

				peRefReader = new InputStreamReader(
					new FileInputStream(
						pathToFile
					)
				);
			} catch (FileNotFoundException e) {
				// OK, so it's not a file, try and make a URL out of it
				DBC.IGNORED_EXCEPTION(e);
				try {
					URL url = new URL(fileTableEntry);

					try {
						peRefReader = new InputStreamReader(
							url.openConnection().getInputStream()
						);
					} catch(IOException ioe) {
						throw new ParameterEntityException(
							Message.DTDPE_UNRESOLVED_PE_REF,
							ioe
						);
					}
				} catch (MalformedURLException mfue) {
					throw new ParameterEntityException(
						Message.DTDPE_UNRESOLVED_PE_REF,
						mfue
					);
				}
			}
		} else if (table.containsKey(name)) {
			// Construct a string reader so that the contents can be properly
			// lexically analysed.
			String contents = replacePERefs(table.get(name)).trim();
			peRefReader = new StringReader(contents);
		} else {
			throw new ParameterEntityException(
				Message.DTDPE_UNRESOLVED_PE_REF
			);
		}

		return peRefReader;
	}

	/** Replace all the parameter entities in a {@link String}.

		@param s							The text to replace entity
											references in.
		@return								A copy of the parameter
											<code>text</code> with all the
											entity references replaced.
		@throws	ParameterEntityException	if a reference to an  undeclared
											parameter entity
	*/
	public String replacePERefs(String s)
	throws ParameterEntityException
	{
		DBC.REQUIRE(s != null);

		String text;
		if (s != null) {
			text = s;
		} else {
			text = "";
		}
		if	(
				text.indexOf((int)Constants.Character.PERCENT) != -1 &&
				text.indexOf((int)Constants.Character.SEMI_COLON) != -1
			)
		{
			// The text potentially has a parameter entity reference in it.

			// Create vectors to store the indices of % and ; occurrences
			List<Integer> percentIndices = new ArrayList<Integer>();
			List<Integer> semicolonIndices = new ArrayList<Integer>();

			// Get the indices of all the occurences of the % character
			int percent = (int)Constants.Character.PERCENT;
			int poffset = 0;
			int index;
			while ((index = text.indexOf(percent, poffset)) != -1) {
				percentIndices.add(index);
				poffset = index + 1;
			}

			// Get the indices of all the occurences of the ; character
			int semicolon = (int)Constants.Character.SEMI_COLON;
			int soffset = 0;
			while ((index = text.indexOf(semicolon, soffset)) != -1) {
				semicolonIndices.add(index);
				soffset = index + 1;
			}

			// Now use the indices of the % and ; characters to attempt
			// replacement of references.
			int strlenDiff = 0;
			for (int pIndex : percentIndices) {
				int sIndex = 0;
				for (int sI : semicolonIndices) {
					sIndex = sI;
					if (sIndex > pIndex) {
						break;
					}
				}

				// Now that the upper and a lower bound for the start and end
				// index of the potential PERef is known, extract it and see if
				// it's really a PERef.
				if (pIndex < sIndex) {
					// Extract the text of the PERef
					String peRef = text.substring	(
														pIndex + strlenDiff,
														sIndex + strlenDiff + 1
													);

					// From the text of the PERef get the name of the entity
					// (PERef minus leading % and trailing ; in other words.
					String peRefKey = peRef.substring(1, peRef.length() - 1);

					// Now see if this PERef is known about
					if (table.containsKey(peRefKey)) {
						// Extract the text before and after the PERef
						String textBefore = text.substring(
															0,
															pIndex + strlenDiff
														);
						String textAfter = text.substring(
															sIndex +
																1 +
																strlenDiff,
															text.length()
														);

						// Calculate the difference in string length that the
						// substitution would cause
						CharSequence rText = table.get(peRefKey);
						strlenDiff += rText.length() - peRef.length();

						// Actually perform the replacement.
						text = textBefore + table.get(peRefKey) + textAfter;
					} else {
						// If the PERef was valid then an error must be thrown
						// otherwise it was a false positive and should be
						// ignored.
						if (peRefKey.matches(Constants.Regex.VALID_PE_NAME)) {
							// The PERef was valid
							throw new ParameterEntityException(
								Message.DTDPE_UNDECLARED_PE(
									peRefKey
								)
							);
						}
					}
				}
			}
		}

		return text;
	}

	/** Clone this {@link PEDeclTable}.

		@return								A clone of this object.
		@throws CloneNotSupportedException	if the clone cannot be made.
	*/
	public Object clone()
	throws CloneNotSupportedException
	{
		PEDeclTable clone = (PEDeclTable)super.clone();
		clone.table = new HashMap<String, String>(table);
		clone.fileTable = new HashMap<String, String>(fileTable);
		return clone;
	}

	/** See {@link Object#toString()}.

		@return	A descriptive {@link String}.
	*/
	@Override public String toString() {
		return Message.DTDPEDECLTABLE(table.size(), fileTable.size());
	}

	/** See {@link Object#equals(Object)}.

		@param	o	The object to compare against.
		@return		True if <code>this</code> is equal to <code>o</code>.
	*/
	@Override public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || !(o instanceof PEDeclTable)) {
			return false;
		}

		PEDeclTable other = (PEDeclTable)o;
		if (!Utils.areDeeplyEqual(table, other.getTable())) {
			return false;
		}
		if (!Utils.areDeeplyEqual(fileTable, other.getFileTable())) {
			return false;
		}

		return true;
	}

	/** See {@link Object#hashCode()}.

		@return	A hash code.
	*/
	@Override public int hashCode() {
		return Utils.genericHashCode(table, fileTable);
	}
}
