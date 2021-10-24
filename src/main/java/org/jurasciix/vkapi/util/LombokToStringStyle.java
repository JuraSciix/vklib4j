package org.jurasciix.vkapi.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

// I liked this style
public final class LombokToStringStyle extends ToStringStyle {

    public static final LombokToStringStyle STYLE = new LombokToStringStyle();

    public static ToStringBuilder getToStringBuilder(Object object) {
        return new ToStringBuilder(object, STYLE);
    }

    private LombokToStringStyle() {
        setUseShortClassName(true);
        setUseIdentityHashCode(false);
        setArrayStart("[");
        setArrayEnd("]");
        setArraySeparator(", ");
        setContentStart("(");
        setContentEnd(")");
        setFieldSeparator(", ");
        setNullText("null");
    }
}
