package org.jurasciix.vkapi;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.List;

public class Error {

    public static final String JSON_ERROR_CODE = "error_code";
    public static final String JSON_ERROR_MESSAGE = "error_msg";
    public static final String JSON_REQUEST_PARAMS = "request_params";
    public static final String JSON_CAPTCHA_IMAGE = "captcha_img";
    public static final String JSON_CAPTCHA_SID = "captcha_sid";

    @SerializedName(JSON_ERROR_CODE)
    private int code;

    @SerializedName(JSON_ERROR_MESSAGE)
    private String message;

    @SerializedName(JSON_REQUEST_PARAMS)
    private List<RequestParam> requestParams;

    @SerializedName(JSON_CAPTCHA_IMAGE)
    private String captchaImage;

    @SerializedName(JSON_CAPTCHA_SID)
    private long captchaSid;

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    public List<RequestParam> getRequestParams() {
        return requestParams;
    }

    public String getCaptchaImage() {
        return captchaImage;
    }

    public long getCaptchaSid() {
        return captchaSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Error)) return false;
        Error another = (Error) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(code, another.code);
        builder.append(captchaSid, another.captchaSid);
        builder.append(message, another.message);
        builder.append(requestParams, another.requestParams);
        builder.append(captchaImage, another.captchaImage);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(code);
        builder.append(message);
        builder.append(requestParams);
        builder.append(captchaImage);
        builder.append(captchaSid);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
        builder.append("code", code);
        builder.append("message", message);
        builder.append("requestParams", requestParams);
        builder.append("captchaImage", captchaImage);
        builder.append("captchaSid", captchaSid);
        return builder.toString();
    }

    public static class RequestParam {

        public static final String JSON_KEY = "key";
        public static final String JSON_VALUE = "value";

        @SerializedName(JSON_KEY)
        private String key;

        @SerializedName(JSON_VALUE)
        private String value;

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (!(o instanceof RequestParam)) return false;
            RequestParam another = (RequestParam) o;
            EqualsBuilder builder = new EqualsBuilder();
            builder.append(key, another.key);
            builder.append(value, another.value);
            return builder.isEquals();
        }

        @Override
        public int hashCode() {
            HashCodeBuilder builder = new HashCodeBuilder();
            builder.append(key);
            builder.append(value);
            return builder.toHashCode();
        }

        @Override
        public String toString() {
            ToStringBuilder builder = new ToStringBuilder(this, ToStringStyle.JSON_STYLE);
            builder.append("key", key);
            builder.append("value", value);
            return builder.toString();
        }
    }
}
