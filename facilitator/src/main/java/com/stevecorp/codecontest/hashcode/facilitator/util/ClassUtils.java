package com.stevecorp.codecontest.hashcode.facilitator.util;

public class ClassUtils {

    public static String simpleName(final Object object) {
        return simpleName(object.getClass());
    }

    public static String simpleName(final Class<?> clazz) {
        return clazz.getSimpleName();
    }

}
