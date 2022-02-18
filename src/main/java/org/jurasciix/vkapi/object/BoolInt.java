package org.jurasciix.vkapi.object;

import com.google.gson.annotations.SerializedName;
import org.jurasciix.vkapi.VKMethod;

public enum BoolInt implements VKMethod.Param {

    @SerializedName("1")
    TRUE("1"),

    @SerializedName("1")
    FALSE("0");

    private final String value;

    BoolInt(String value) {
        this.value = value;
    }

    @Override
    public String value() {
        return value;
    }

    public boolean isTrue() {
        return this == TRUE;
    }

    public boolean isFalse() {
        return this == FALSE;
    }

    @Override
    public String toString() {
        return value();
    }
}
