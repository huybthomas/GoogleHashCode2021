package com.stevecorp.codecontest.hashcode.facilitator.util;

import static java.text.MessageFormat.format;

public class ClassUtils {

    public static String simpleName(final Object object) {
        return simpleName(object.getClass());
    }

    public static String simpleName(final Class<?> clazz) {
        return clazz.getSimpleName();
    }

    public static <T> T constructInstance(final Class<T> clazz) {
        if (clazz == null) {
            return null;
        }
        try {
            return clazz.getConstructor().newInstance();
        } catch (final Exception e) {
            throw new RuntimeException(format("Failed to create instance of class {0}", simpleName(clazz)));
        }
    }

}
