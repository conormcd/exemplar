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
package com.mcdermottroe.exemplar.utils;

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.mcdermottroe.exemplar.DBC;

/** A collection of {@link File} handling methods.

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Files {
	/** Private constructor to prevent instantiation of this class. */
	private Files() {
		DBC.UNREACHABLE_CODE();
	}

	/** Remove a tree of files.

		@param	directory	The root directory of the tree to remove.
	*/
	public static void removeTree(File directory) {
		DBC.REQUIRE(directory != null);
		assert directory != null;
		if (directory.exists()) {
			// First delete all of the files under the tree
			for (File f : findFiles(directory)) {
				f.delete();
			}

			// Now delete all of the directories
			for (File dir : findDirectories(directory)) {
				dir.delete();
			}
		}
		DBC.ENSURE(!directory.exists());
	}

	/** Find all files under a directory.

		@param	directory	The directory to search.
		@return 			The {@link File}s found under the directory.
	*/
	public static Collection<File> findFiles(File directory) {
		DBC.REQUIRE(directory != null);
		assert directory != null;
		DBC.REQUIRE(directory.exists());

		List<File> retVal = new ArrayList<File>();
		for (String fileName : directory.list()) {
			File f = new File(directory, fileName);
			if (f.isDirectory()) {
				retVal.addAll(findFiles(f));
			} else {
				retVal.add(f);
			}
		}
		return retVal;
	}

	/** Find all the subdirectories of a directory.

		@param	directory	The directory to search.
		@return 			The {@link File}s found under the directory which
							are directories. <code>directory</code> is included
							in the returned {@link Collection}.
	*/
	public static Collection<File> findDirectories(File directory) {
		DBC.REQUIRE(directory != null);
		assert directory != null;
		DBC.REQUIRE(directory.exists());

		SortedSet<File> retVal = new TreeSet<File>(
			new Comparator<File>() {
				public int compare(File a, File b) {
					int aL = a.getAbsolutePath().length();
					int bL = b.getAbsolutePath().length();
					if (aL > bL) {
						return -1;
					} else if (bL > aL) {
						return 1;
					} else {
						return a.getAbsolutePath().compareTo(
							b.getAbsolutePath()
						);
					}
				}
			}
		);
		retVal.add(directory);
		for (String fileName : directory.list()) {
			File f = new File(directory, fileName);
			if (f.isDirectory()) {
				retVal.add(f);
				retVal.addAll(findDirectories(f));
			}
		}
		return retVal;
	}
}
