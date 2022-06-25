package org.jurasciix.vklib4j.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Args {

    public static <T> T notNull(T value, String name) {
        if (value == null) {
            throw new IllegalArgumentException(name + " must not be null");
        }
        return value;
    }
}
