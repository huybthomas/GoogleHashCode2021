package com.stevecorp.codecontest.hashcode.facilitator.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CollectionUtils {

    public static <T> List<T> join(final T element, final T... additionalElements) {
        final List<T> joined = new ArrayList<>();
        joined.add(element);
        joined.addAll(Arrays.asList(additionalElements));
        return joined;
    }

}
