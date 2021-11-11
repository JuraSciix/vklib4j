package org.jurasciix.jvkapi.util;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

public final class LombokToStringStyle extends ToStringStyle {

    public static final LombokToStringStyle STYLE = new LombokToStringStyle();

    public static ToStringBuilder getToStringBuilder(Object o) {
        return new ToStringBuilder(o, STYLE);
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
