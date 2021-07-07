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

package org.indunet.fastproto.pipeline;

/**
 * Abstract flow.
 *
 * @author Deng Ran
 * @since 1.7.0
 */
public abstract class AbstractFlow<T> {
    AbstractFlow<T> next = null;

    public abstract void process(T context);

    AbstractFlow<T> setNext(AbstractFlow<T> next) {
        this.next = next;

        return this.next;
    }

    public void nextFlow(T context) {
        if (next != null) {
            this.next.process(context);
        }
    }

    public void end() {
        this.next = null;
    }

    public abstract long getFlowCode();
}
