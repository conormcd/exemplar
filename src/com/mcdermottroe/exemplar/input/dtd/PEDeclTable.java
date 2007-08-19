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
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;

import com.mcdermottroe.exemplar.Copyable;
import com.mcdermottroe.exemplar.DBC;
import com.mcdermottroe.exemplar.Utils;
import com.mcdermottroe.exemplar.ui.Message;

import static com.mcdermottroe.exemplar.Constants.Character.PERCENT;
import static com.mcdermottroe.exemplar.Constants.Character.SEMI_COLON;
import static com.mcdermottroe.exemplar.Constants.Regex.VALID_PE_NAME;

/** A symbol table for parameter entities.

	@author	Conor McDermottroe
	@since	0.1
*/
public class PEDeclTable
implements Comparable<PEDeclTable>, Copyable<PEDeclTable>
{
	/**	A table of parameter entities that have immediate values not URI
		references.
	*/
	private final Map<String, String> table;

	/** A table of parameter entities that have URI references. */
	private final Map<String, String> fileTable;

	/** Simple constructor, just initialises storage. */
	public PEDeclTable() {
		table = new HashMap<String, String>();
		fileTable = new HashMap<String, String>();
	}

	/** Create a new {@link PEDeclTable} with a given set of values.

		@param	tbl		A table of parameter entities which have immediate
						values.
		@param	files	A table of parameter entities which have URI references.
	*/
	protected PEDeclTable(Map<String, String> tbl, Map<String, String> files) {
		table = new HashMap<String, String>(tbl);
		fileTable = new HashMap<String, String>(files);
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
		assert name != null;
		DBC.REQUIRE(name.length() > 0);
		DBC.REQUIRE(contents != null);
		assert contents != null;
		DBC.REQUIRE(contents.length() >= 0);

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
	public Reader peRefReader(String name, File dtdPath)
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
				File file;
				if (fileTableEntry.startsWith(File.separator)) {
					file = new File(fileTableEntry);
				} else {
					file = new File(dtdPath, fileTableEntry);
				}

				peRefReader = new InputStreamReader(
					new FileInputStream(
						file
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
							Message.DTDPE_UNRESOLVED_PE_REF(),
							ioe
						);
					}
				} catch (MalformedURLException mfue) {
					throw new ParameterEntityException(
						Message.DTDPE_UNRESOLVED_PE_REF(),
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
				Message.DTDPE_UNRESOLVED_PE_REF()
			);
		}

		return peRefReader;
	}

	/**	Replace all the parameter entities in a {@link CharSequence}.

		@param string						The text to replace entity
	 										references in.
		@return								A copy of the parameter
											<code>string</code> with all the
											entity references replaced.
		@throws	ParameterEntityException	if a reference to an  undeclared
											parameter entity is encountered.
	*/
	public String replacePERefs(CharSequence string)
	throws ParameterEntityException
	{
		if (string == null) {
			return "";
		}

		StringBuilder retVal = new StringBuilder(string.length());
		for (int i = 0; i < string.length(); i++) {
			if (string.charAt(i) == PERCENT) {
				for (int j = i + 1; j < string.length(); j++) {
					if (string.charAt(j) == SEMI_COLON) {
						String peName = string.subSequence(i + 1, j).toString();
						if (table.containsKey(peName)) {
							retVal.append(table.get(peName));
							i = j;
						} else {
							Matcher m = VALID_PE_NAME.matcher(peName);
							if (m.matches()) {
								throw new ParameterEntityException(
									Message.DTDPE_UNDECLARED_PE(peName)
								);
							} else {
								retVal.append(string.charAt(i));
							}
						}
						break;
					}
				}
			} else {
				retVal.append(string.charAt(i));
			}
		}
		return retVal.toString();
	}

	/** Implement {@link Comparable#compareTo(Object)}.
		
		@param	other	The {@link PEDeclTable} to compare with.
		@return			A result as defined by {@link
						Comparable#compareTo(Object)}.
	*/
	public int compareTo(PEDeclTable other) {
		int tableCmp = Utils.compare(table, other.getTable());
		if (tableCmp != 0) {
			return tableCmp;
		}
		return Utils.compare(fileTable, other.getFileTable());
	}

	/** {@inheritDoc} */
    public PEDeclTable getCopy() {
		return new PEDeclTable(table, fileTable);
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
