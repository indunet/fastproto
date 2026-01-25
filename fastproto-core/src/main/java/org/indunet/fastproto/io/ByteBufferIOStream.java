/**
 * Copyright 2019-2023 indunet.org
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

package org.indunet.fastproto.io;

/**
 * Abstract ByteBuffer IO stream.
 *
 * @author Deng Ran
 * @since 3.10.2
 */
public abstract class ByteBufferIOStream {
    protected ByteBuffer byteBuffer;
    protected int byteIndex;
    protected int bitIndex;

    public ByteBufferIOStream(ByteBuffer buffer) {
        this.byteBuffer = buffer;
        this.byteIndex = 0;
        this.bitIndex = 0;
    }

    public void align(int alignment) {
        if (alignment <= 0 || (alignment & (alignment - 1)) != 0) {
            throw new IllegalArgumentException("alignment must be a positive power of two");
        }

        int index = this.byteIndex;
        int after = ((index + (alignment - 1)) & ~(alignment - 1));

        this.byteIndex = Math.max(after, 0);
    }

    public void skip() {
        this.byteIndex ++;
    }

    public void skip(int num) {
        if (num >= 0) {
            this.byteIndex += num;
        } else {
            throw new IllegalArgumentException("num must be a positive number.");
        }
    }

    public ByteBuffer toByteBuffer() {
        return this.byteBuffer;
    }
}
