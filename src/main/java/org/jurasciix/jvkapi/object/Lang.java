package org.jurasciix.jvkapi.object;

import org.jurasciix.jvkapi.VKMethod;

public enum Lang implements VKMethod.Param {

    RU("ru"),
    UA("ua"),
    BE("be"),
    EN("en"),
    ES("es"),
    FI("fi"),
    DE("de"),
    IT("it");

    private final String value;

    Lang(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    @Override
    public String toString() {
        return value();
    }
}