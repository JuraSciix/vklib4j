package org.jurasciix.vklib4j.util;

import lombok.experimental.UtilityClass;

@UtilityClass
public class Asserts {

    public static void notNull(Object value, String message) {
        if (value == null) {
            throw new IllegalStateException(message);
        }
    }
}
