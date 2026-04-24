package org.indunet.fastproto.ros2.bag;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Decodes CDR payloads using generated classes from fastproto-ros2-msg when available.
 */
public final class Ros2MessageDecoder {
    private static final String GENERATED_PACKAGE_PREFIX = "org.indunet.fastproto.ros2.";

    private Ros2MessageDecoder() {
    }

    public static Object decode(String ros2Type, byte[] payload) {
        Class<?> messageClass = resolveMessageClass(ros2Type);
        if (messageClass == null) {
            return null;
        }

        try {
            Method decode = messageClass.getMethod("decode", byte[].class);
            return decode.invoke(null, payload);
        } catch (NoSuchMethodException e) {
            return null;
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Cannot access ROS2 decoder for type " + ros2Type, e);
        } catch (InvocationTargetException e) {
            Throwable cause = e.getCause();
            if (cause instanceof RuntimeException) {
                throw (RuntimeException) cause;
            }
            throw new IllegalStateException("Failed to decode ROS2 message type " + ros2Type, cause);
        }
    }

    private static Class<?> resolveMessageClass(String ros2Type) {
        if (ros2Type == null || !ros2Type.contains("/msg/")) {
            return null;
        }

        String className = GENERATED_PACKAGE_PREFIX + ros2Type.replace("/msg/", ".msg.");
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            return null;
        }
    }
}
