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
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.mcdermottroe.exemplar.DBC;

import static com.mcdermottroe.exemplar.Constants.Character.EXCLAMATION_MARK;
import static com.mcdermottroe.exemplar.Constants.Character.FULL_STOP;
import static com.mcdermottroe.exemplar.Constants.Character.SLASH;
import static com.mcdermottroe.exemplar.Constants.JAR_METAINF_DIR;
import static com.mcdermottroe.exemplar.Constants.PACKAGE;
import static com.mcdermottroe.exemplar.Constants.URL_JAR_PREFIX;

/** General purpose utility methods that can be used anywhere in the program.

	@author	Conor McDermottroe
	@since	0.2
*/
public final class Packages {
	/** Package discovery is an expensive operation so it is done once and the
		result is cached here.

		@see #findPackages()
		@see #findSubPackages(String)
	*/
	private static List<String> allPackages = null;

	/** Private constructor to prevent instantiation of this class. */
	private Packages() {
		DBC.UNREACHABLE_CODE();
	}

	/** Find all of the packages below a certain package.

		@param	packageName	The package to search below.
		@return				A {@link List} of all of the sub-packages of the
							given package, not including
							<code>packageName</code> itself.
	*/
	public static List<String> findSubPackages(String packageName) {
		List<String> subPackages = new ArrayList<String>();

		List<String> packages = findPackages();
		for (String packName : packages) {
			if	(
					packName.startsWith(packageName) &&
					!packName.equals(packageName)
				)
			{
				subPackages.add(packName);
			}
		}

		return subPackages;
	}

	/** Find the available packages in the classpath. The result of this method
		call cannot change during one execution of the program and hence it is
		cached in the member {@link #allPackages} after the first call.

		@return	A list of all of the packages currently visible in the
				classpath.
		@see	ClassLoader
	*/
	private static List<String> findPackages() {
		if (allPackages != null) {
			return new ArrayList<String>(allPackages);
		}

		String packagePath = PACKAGE.replace(FULL_STOP, SLASH);

		List<String> packages = new ArrayList<String>();

		// Search through the classpath
		try {
			ClassLoader cl = Packages.class.getClassLoader();
			for	(
					Enumeration<URL> e = cl.getResources(packagePath);
					e.hasMoreElements();
				)
			{
				URL url = e.nextElement();
				if (url.toString().startsWith(URL_JAR_PREFIX)) {
					packages.addAll(readPackagesFromJar(url));
				}
			}
		} catch (IOException e) {
			// Ignore this for the moment.
			DBC.IGNORED_EXCEPTION(e);
		}

		allPackages = packages;
		return packages;
	}

	/** Given a {@link URL} of a JAR file, read all of the packages contained
		within it.

		@param	url	The {@link URL} of the JAR file to read from.
		@return		A {@link List} of packages contained within the JAR file.
	*/
	private static List<String> readPackagesFromJar(URL url) {
		List<String> packages;

		if (!url.toString().startsWith(URL_JAR_PREFIX)) {
			packages = new ArrayList<String>(0);
			return packages;
		}

		String jarFilePath = url.toString();
		jarFilePath = jarFilePath.substring(
			URL_JAR_PREFIX.length(),
			jarFilePath.indexOf((int)EXCLAMATION_MARK)
		);
		JarFile jar = null;
		try {
			jar = new JarFile(new File(jarFilePath));
		} catch (IOException e) {
			DBC.IGNORED_EXCEPTION(e);
		}

		if (jar != null) {
			List<JarEntry> jarEntries = Collections.list(jar.entries());
			packages = new ArrayList<String>(jarEntries.size());
			for (JarEntry jarEntry : jarEntries) {
				String entry = jarEntry.toString();
				char lastChar = entry.charAt(entry.length() - 1);
				if (lastChar == SLASH) {
					entry = entry.substring(0, entry.length() - 1);
					if (!entry.equals(JAR_METAINF_DIR)) {
						entry = entry.replace(SLASH, FULL_STOP);
						packages.add(entry);
					}
				}
			}
		} else {
			packages = new ArrayList<String>(0);
		}

		return packages;
	}
}
