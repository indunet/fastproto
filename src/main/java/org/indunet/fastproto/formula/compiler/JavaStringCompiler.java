/*
 * Copyright 2019-2022 indunet.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.indunet.fastproto.formula.compiler;

import org.indunet.fastproto.exception.ResolvingException;

import javax.tools.JavaCompiler;
import javax.tools.JavaCompiler.CompilationTask;
import javax.tools.JavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Java String Compiler Class.
 * This class is responsible for compiling Java source code in-memory from a string.
 * It uses the JavaCompiler and StandardJavaFileManager from javax.tools package.
 * The class provides methods to compile the source code and load the compiled classes.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
public class JavaStringCompiler {
	/**
	 * Compile Method.
	 * This method compiles Java source code from a string in-memory.
	 * The method throws IOException if an I/O error occurs during the compilation process.
	 * The method returns a map of class names to class bytes.
	 *
	 * @param fileName the name of the file to compile
	 * @param sourceCode the source code to compile
	 * @return a map of class names to class bytes
	 * @throws IOException if an I/O error occurs during the compilation process
	 */
	public Map<String, byte[]> compile(String fileName, String sourceCode) throws IOException {
		JavaCompiler compiler = ToolProvider.getSystemJavaCompiler();
		try (StandardJavaFileManager stdManager = compiler.getStandardFileManager(null, null, null);
			 MemoryJavaFileManager manager = new MemoryJavaFileManager(stdManager)) {

			JavaFileObject javaFileObject = manager.makeStringSource(fileName, sourceCode);
			List<String> options = Arrays.asList(
					"-source", "1.8",
					"-target", "1.8",
					"-proc:none");
			CompilationTask task = compiler.getTask(null, manager, null, options, null, Arrays.asList(javaFileObject));
			Boolean result = task.call();

			if (result == null || !result.booleanValue()) {
				throw new ResolvingException("Fail compiling lambda expression.");
			}

			return manager.getClassBytes();
		}
	}

	/**
	 * Load Class Method.
	 * This method loads a class from a map of class bytes.
	 * It creates a new instance of MemoryClassLoader with the class bytes, and uses it to load the class.
	 * The method throws ClassNotFoundException if the class cannot be found, and IOException if an I/O error occurs.
	 *
	 * @param name the name of the class to load
	 * @param classBytes a map of class names to class bytes
	 * @return the loaded class
	 * @throws ClassNotFoundException if the class cannot be found
	 * @throws IOException if an I/O error occurs
	 */
	public Class<?> loadClass(String name, Map<String, byte[]> classBytes) throws ClassNotFoundException, IOException {
		try (MemoryClassLoader classLoader = new MemoryClassLoader(classBytes)) {
			return classLoader.loadClass(name);
		}
	}
}
