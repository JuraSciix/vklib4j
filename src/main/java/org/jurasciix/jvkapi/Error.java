package org.jurasciix.jvkapi;

import com.google.gson.annotations.SerializedName;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.jurasciix.jvkapi.util.LombokToStringStyle;

import java.util.List;

public class Error {

    private static final String JSON_ERROR_CODE = "error_code";
    private static final String JSON_ERROR_MESSAGE = "error_msg";
    private static final String JSON_REQUEST_PARAMS = "request_params";
    private static final String JSON_CAPTCHA_URL = "captcha_img";
    private static final String JSON_CAPTCHA_SID = "captcha_sid";

    @SerializedName(JSON_ERROR_CODE)
    private int code;

    @SerializedName(JSON_ERROR_MESSAGE)
    private String message;

    @SerializedName(JSON_REQUEST_PARAMS)
    private List<RequestParam> requestParams;

    @SerializedName(JSON_CAPTCHA_URL)
    private String captchaUrl;

    @SerializedName(JSON_CAPTCHA_SID)
    private long captchaSid;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<RequestParam> getRequestParams() {
        return requestParams;
    }

    public void setRequestParams(List<RequestParam> requestParams) {
        this.requestParams = requestParams;
    }

    public String getCaptchaUrl() {
        return captchaUrl;
    }

    public void setCaptchaUrl(String captchaUrl) {
        this.captchaUrl = captchaUrl;
    }

    public long getCaptchaSid() {
        return captchaSid;
    }

    public void setCaptchaSid(long captchaSid) {
        this.captchaSid = captchaSid;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Error another = (Error) o;
        EqualsBuilder builder = new EqualsBuilder();
        builder.append(code, another.code);
        builder.append(captchaSid, another.captchaSid);
        builder.append(message, another.message);
        builder.append(requestParams, another.requestParams);
        builder.append(captchaUrl, another.captchaUrl);
        return builder.isEquals();
    }

    @Override
    public int hashCode() {
        HashCodeBuilder builder = new HashCodeBuilder();
        builder.append(code);
        builder.append(message);
        builder.append(requestParams);
        builder.append(captchaUrl);
        builder.append(captchaSid);
        return builder.toHashCode();
    }

    @Override
    public String toString() {
        ToStringBuilder builder = LombokToStringStyle.getToStringBuilder(this);
        builder.append("code", code);
        builder.append("message", message);
        builder.append("requestParams", requestParams);
        builder.append("captchaImage", captchaUrl);
        builder.append("captchaSid", captchaSid);
        return builder.toString();
    }

    public static class RequestParam {

        private static final String JSON_KEY = "key";
        private static final String JSON_VALUE = "value";

        @SerializedName(JSON_KEY)
        private String key;

        @SerializedName(JSON_VALUE)
        private String value;

        public String getKey() {
            return key;
        }

        public void setKey(String key) {
            this.key = key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
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
            ToStringBuilder builder = LombokToStringStyle.getToStringBuilder(this);
            builder.append("key", key);
            builder.append("value", value);
            return builder.toString();
        }
    }
}
