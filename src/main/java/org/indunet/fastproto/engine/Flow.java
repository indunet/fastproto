/*
 * Copyright 2019-2021 indunet.org
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

package org.indunet.fastproto.engine;

import lombok.SneakyThrows;
import lombok.val;

import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Flow.
 *
 * @author Deng Ran
 * @since 3.2.0
 */
public interface Flow {
    boolean when(Object obj);
    void action(Object obj);

    @SneakyThrows
    default Flow proxy(Class<?> clazz) {
        val flow = clazz.newInstance();
        Method when = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(When.class))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No when method found."));
        Method action = Arrays.stream(clazz.getDeclaredMethods())
                .filter(m -> m.isAnnotationPresent(Action.class))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No action method found."));
        List<String> whenParams = Arrays.stream(when.getParameters())
                .map(p -> {
                    if (p.isAnnotationPresent(Param.class)) {
                        return p.getAnnotation(Param.class).value();
                    } else {
                        return p.getName();
                    }
                }).collect(Collectors.toList());
        List<String> actionParams = Arrays.stream(action.getParameters())
                .map(p -> {
                    if (p.isAnnotationPresent(Param.class)) {
                        return p.getAnnotation(Param.class).value();
                    } else {
                        return p.getName();
                    }
                }).collect(Collectors.toList());


        return (Flow) Proxy.newProxyInstance(this.getClass().getClassLoader(), new Class[] {Flow.class}, ((proxy, method, args) -> {
            if (method.equals(when)) {
               if (args.length == 0) {
                   return when.invoke(flow);
               } else if (args.length == 1 && args[0].getClass() == ) {

               }
           } else if (method.equals(action)) {

           } else {
               return null;
           }
        }));
    }
}
