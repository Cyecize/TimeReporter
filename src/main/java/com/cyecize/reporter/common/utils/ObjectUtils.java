package com.cyecize.reporter.common.utils;

public final class ObjectUtils {

    private ObjectUtils() {

    }

    public static <T> boolean objectsEqual(T o1, T o2) {
        return (o1 == null && o2 == null) || (o1 != null && o1.equals(o2));
    }
}
