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
package build.com.mcdermottroe.exemplar.tasks;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.Task;
import org.apache.tools.ant.types.FileSet;

/** An Ant {@link Task} to check if all the files in an Ant {@link FileSet}
	are equal.

	@author	Conor McDermottroe
	@since	0.1
*/
public class FilesEqual extends Task {
	/** The nested fileset. */
	private FileSet fileset;

	/** Trivial constructor. */
	public FilesEqual() {
		super();
		fileset = null;
	}

	/** Get the nested fileset element.

		@param	aFileset	The nested fileset.
	*/
	public void addConfiguredFileSet(FileSet aFileset) {
		fileset = aFileset;
	}

	/** Compare all of the files in the nested fileset.

		@throws	BuildException	if the files are not all equal to each other.
	*/
	public void execute() {
		super.execute();

		// It is an error to attempt to run this task without a fileset
		// defining the files to compare
		if (fileset == null) {
			throw new BuildException("You must define a fileset to compare the files.");
		}

		// We delay throwing the exception to allow us to attempt to close the
		// files.
		BuildException failure = null;

		// Open all of the files in the fileset
		File fileSetDir = fileset.getDir(getProject());
		String[] filenames = fileset.getDirectoryScanner(getProject()).getIncludedFiles();
		BufferedReader[] readers = new BufferedReader[filenames.length];
		for (int i = 0; i < filenames.length; i++) {
			try {
				readers[i] = new BufferedReader(
					new FileReader(
						new File(fileSetDir, filenames[i])
					)
				);
			} catch (FileNotFoundException e) {
				if (failure == null) {
					failure = new BuildException(e);
				}
				readers[i] = null;
			}
		}

		if (readers[0] != null && failure == null) {
			if (readers.length > 1) {
				try {
					String notEqualMessage = "Not equal.";
					while (readers[0].ready()) {
						// Read the next character from the first stream.
						int character = readers[0].read();

						// If we've reached the end of the first stream, make
						// sure that we've reached the end of all of the
						// streams.
						if (character == -1) {
							for (int i = 1; i < readers.length; i++) {
								if (readers[i].read() != -1) {
									failure = new BuildException(notEqualMessage);
									break;
								}
							}
						}
						if (failure != null) {
							break;
						}

						// OK, we haven't reached the end of the stream,
						// compare the next character in each of the other
						// streams.
						for (int i = 1; i < readers.length; i++) {
							int other = readers[i].read();
							if (character != other) {
								failure = new BuildException(notEqualMessage);
								break;
							}
						}
						if (failure != null) {
							break;
						}
					}
				} catch (IOException e) {
					failure = new BuildException(e);
				}
			}
		}

		// Try to close all of the readers.
		for (int i = 0; i < readers.length; i++) {
			try {
				if (readers[i] != null) {
					readers[i].close();
				}
			} catch (IOException e) {
				if (failure != null) {
					failure = new BuildException("Failed to close a reader.", e);
				}
			}
		}

		// If we have a failure from earlier then we should throw it now.
		if (failure != null) {
			throw failure;
		}
	}
}
