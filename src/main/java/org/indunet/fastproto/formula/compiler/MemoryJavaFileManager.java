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

import javax.tools.*;
import javax.tools.JavaFileObject.Kind;
import java.io.ByteArrayOutputStream;
import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.URI;
import java.nio.CharBuffer;
import java.util.HashMap;
import java.util.Map;

/**
 * Memory Java File Manager Class.
 * This class is a custom Java file manager that manages Java files in-memory.
 * It extends ForwardingJavaFileManager and provides methods to handle Java files, such as creating a new Java file for output.
 * The class also maintains a map to store the compiled classes in bytes.
 * It is a crucial part of the fastproto library's formula compiler.
 *
 * @author Deng Ran
 * @since 3.7.0
 */
class MemoryJavaFileManager extends ForwardingJavaFileManager<JavaFileManager> {

	// compiled classes in bytes:
	final Map<String, byte[]> classBytes = new HashMap<String, byte[]>();

	MemoryJavaFileManager(JavaFileManager fileManager) {
		super(fileManager);
	}

	public Map<String, byte[]> getClassBytes() {
		return new HashMap<String, byte[]>(this.classBytes);
	}

	@Override
	public void flush() throws IOException {
	}

	@Override
	public void close() throws IOException {
		classBytes.clear();
	}

	@Override
	public JavaFileObject getJavaFileForOutput(Location location, String className, Kind kind,
                                               FileObject sibling) throws IOException {
		if (kind == Kind.CLASS) {
			return new MemoryOutputJavaFileObject(className);
		} else {
			return super.getJavaFileForOutput(location, className, kind, sibling);
		}
	}

	JavaFileObject makeStringSource(String name, String code) {
		return new MemoryInputJavaFileObject(name, code);
	}

	static class MemoryInputJavaFileObject extends SimpleJavaFileObject {

		final String code;

		MemoryInputJavaFileObject(String name, String code) {
			super(URI.create("string:///" + name), Kind.SOURCE);
			this.code = code;
		}

		@Override
		public CharBuffer getCharContent(boolean ignoreEncodingErrors) {
			return CharBuffer.wrap(code);
		}
	}

	class MemoryOutputJavaFileObject extends SimpleJavaFileObject {
		final String name;

		MemoryOutputJavaFileObject(String name) {
			super(URI.create("string:///" + name), Kind.CLASS);
			this.name = name;
		}

		@Override
		public OutputStream openOutputStream() {
			return new FilterOutputStream(new ByteArrayOutputStream()) {
				@Override
				public void close() throws IOException {
					out.close();
					ByteArrayOutputStream bos = (ByteArrayOutputStream) out;
					classBytes.put(name, bos.toByteArray());
				}
			};
		}

	}
}
