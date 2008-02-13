// vim:filetype=java:ts=4
/*
	Copyright (c) 2008
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

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/** Test class for all generated exception classes.

	@param	<T>	The type of {@link Exception} being tested.
	@author	Conor McDermottroe
	@since	0.2
*/
public abstract class GeneratedExceptionClassTestCase<T extends Exception>
extends NormalGeneratedClassTestCase<T>
{
	/** {@inheritDoc} */
	@Override public void setUp() throws Exception {
		super.setUp();
		addSample(sampleFromNoArgConstructor());
		addSample(sampleFromStringConstructor());
		addSample(sampleFromThrowableConstructor());
		addSample(sampleFromStringThrowableConstructor());
	}

	/** Test that a no-arg constructor is available and can be used to create 
		an instance of the {@link Exception} class.

		@see Exception#Exception()
	*/
	public void testNoArgConstructor() {
		try {
			assertNotNull(
				"The no-arg constructor works.",
				sampleFromNoArgConstructor()
			);
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
		} catch (NoSuchMethodException e) {
			fail("Class does not have a no-arg constructor.");
		}
	}

	/** Test that a constructor which takes a single argument of type {@link 
		String} is available and can be used to create an instance of the
		{@link Exception} class.

		@see Exception#Exception(String)
	*/
	public void testOneStringConstructor() {
		try {
			assertNotNull(
				"The (String) constructor works.",
				sampleFromStringThrowableConstructor()
			);
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (String) constructor.");
		}
	}

	/** Test that a constructor which takes a single argument of type {@link 
		Throwable} is available and can be used to create an instance of the
		{@link Exception} class.

		@see Exception#Exception(Throwable)
	*/
	public void testOneThrowableConstructor() {
		try {
			assertNotNull(
				"The (Throwable) constructor works.",
				sampleFromThrowableConstructor()
			);
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (Throwable) constructor.");
		}
	}

	/** Test that a constructor which takes two arguments, a {@link String} and
		a {@link Throwable} in that order, is available and can be used to
		create an instance of the {@link Exception} class.

		@see Exception#Exception(String, Throwable)
	*/
	public void testStringThrowableConstructor() {
		try {
			assertNotNull(
				"The (String, Throwable) constructor works.",
				sampleFromStringThrowableConstructor()
			);
		} catch (IllegalAccessException e) {
			fail("Forbidden from instantiating the class.");
		} catch (InstantiationException e) {
			fail("Failed to instantiate the class.");
		} catch (InvocationTargetException e) {
			fail("An exception was thrown when instantiating the class.");
		} catch (NoSuchMethodException e) {
			fail("Class does not have a (String, Throwable) constructor.");
		}
	}

	/** Create a sample object using the no-arg constructor.

		@return								An {@link Exception} created using
											the no-arg constructor.
		@throws IllegalAccessException		if the constructor is not
											accessible.
		@throws InstantiationException		if the constructor fails to be
											called.
		@throws InvocationTargetException	if the constructor throws an
											exception.
		@throws NoSuchMethodException		if the no-arg constructor does not
											exist.
	*/
	protected T sampleFromNoArgConstructor()
	throws	IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException
	{
		return (T)testedClass.newInstance();
	}

	/** Create a sample object using the {@link String} constructor.

		@return								An {@link Exception} created using
											the {@link String} constructor.
		@throws IllegalAccessException		if the constructor is not
											accessible.
		@throws InstantiationException		if the constructor fails to be
											called.
		@throws InvocationTargetException	if the constructor throws an
											exception.
		@throws NoSuchMethodException		if the no-arg constructor does not
											exist.
	*/
	protected T sampleFromStringConstructor()
	throws	IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException
	{
		Constructor<?> oneString = testedClass.getDeclaredConstructor(
			String.class
		);
		return (T)oneString.newInstance("test");
	}

	/** Create a sample object using the {@link Throwable} constructor.

		@return								An {@link Exception} created using
											the {@link Throwable} constructor.
		@throws IllegalAccessException		if the constructor is not
											accessible.
		@throws InstantiationException		if the constructor fails to be
											called.
		@throws InvocationTargetException	if the constructor throws an
											exception.
		@throws NoSuchMethodException		if the no-arg constructor does not
											exist.
	*/
	protected T sampleFromThrowableConstructor()
	throws	IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException
	{
		Constructor<?> oneString = testedClass.getDeclaredConstructor(
			Throwable.class
		);
		return (T)oneString.newInstance(new Throwable("test"));
	}

	/** Create a sample object using the {@link String}, {@link Throwable}
		constructor.

		@return								An {@link Exception} created using
											the {@link String}, {@link
											Throwable} constructor.
		@throws IllegalAccessException		if the constructor is not
											accessible.
		@throws InstantiationException		if the constructor fails to be
											called.
		@throws InvocationTargetException	if the constructor throws an
											exception.
		@throws NoSuchMethodException		if the no-arg constructor does not
											exist.
	*/
	protected T sampleFromStringThrowableConstructor()
	throws	IllegalAccessException, InstantiationException,
			InvocationTargetException, NoSuchMethodException
	{
		Constructor<?> oneString = testedClass.getDeclaredConstructor(
			String.class,
			Throwable.class
		);
		return (T)oneString.newInstance("test", new Throwable("test"));
	}
}
