package org.jurasciix.vkapi;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.jurasciix.vkapi.util.LombokToStringStyle;

import java.util.List;

public class Error {

    protected static final String JSON_ERROR_CODE = "error_code";
    protected static final String JSON_ERROR_MESSAGE = "error_msg";
    protected static final String JSON_REQUEST_PARAMS = "request_params";
    protected static final String JSON_CAPTCHA_IMAGE = "captcha_img";
    protected static final String JSON_CAPTCHA_SID = "captcha_sid";

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
        if (null == o || getClass() != o.getClass()) return false;
        Error another = (Error) o;
        return new EqualsBuilder()
                .append(code, another.code)
                .append(captchaSid, another.captchaSid)
                .append(message, another.message)
                .append(requestParams, another.requestParams)
                .append(captchaImage, another.captchaImage).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder()
                .append(code)
                .append(message)
                .append(requestParams)
                .append(captchaImage)
                .append(captchaSid).toHashCode();
    }

    @Override
    public String toString() {
        return LombokToStringStyle.getToStringBuilder(this)
                .append("code", code)
                .append("message", message)
                .append("requestParams", requestParams)
                .append("captchaImage", captchaImage)
                .append("captchaSid", captchaSid).toString();
    }

    public static class RequestParam {

        protected static final String JSON_KEY = "key";
        protected static final String JSON_VALUE = "value";

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
            if (null == o || getClass() != o.getClass()) return false;
            RequestParam another = (RequestParam) o;
            return new EqualsBuilder().append(key, another.key).append(value, another.value).isEquals();
        }

        @Override
        public int hashCode() {
            return new HashCodeBuilder().append(key).append(value).toHashCode();
        }

        @Override
        public String toString() {
            return LombokToStringStyle.getToStringBuilder(this)
                    .append("key", key)
                    .append("value", value).toString();
        }
    }
}
