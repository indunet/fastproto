/*
 * Copyright 2019-2021 indunet
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

package org.indunet.fastproto.decoder;

/**
 * Array decoder.
 *
 * @author Deng Ran
 * @since 2.2.0
 */
public class ArrayDecoder<T> implements TypeDecoder<T[]> {
    @Override
    public T[] decode(DecodeContext context) {
        return null;
    }

//    public Object decode(@NonNull final byte[] datagram, int byteOffset, int bitOffset, @NonNull ProtocolType type, int length, @NonNull EndianPolicy policy) {
//        val list = new ArrayList<Object>();
//
//        // length check.
//
//        switch (type) {
//            case ProtocolType.SHORT:
//            IntStream.range(byteOffset, byteOffset + length)
//                    .parallel()
//                    .filter(i -> (i - byteOffset % ShortType.SIZE) == 0)
//                    .forEach(i -> {
//                        list.add(DecodeUtils.shortType(datagram, i, policy));
//                    });
//            return list.toArray(new Short[10]);
//            case ProtocolType.INTEGER:
//                IntStream.range(byteOffset, byteOffset + length)
//                        .forEach();
//            break;
//        }
//    }
}
