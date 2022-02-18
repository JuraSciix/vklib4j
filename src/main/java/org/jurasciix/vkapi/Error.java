package org.jurasciix.vkapi;

import com.google.gson.annotations.SerializedName;

import java.util.List;
import java.util.Objects;

public class Error {

    public static final String JSON_ERROR_CODE = "error_code";

    public static final String JSON_ERROR_MESSAGE = "error_msg";

    public static final String JSON_REQUEST_PARAMS = "request_params";

    public static final String JSON_CAPTCHA_URL = "captcha_img";

    public static final String JSON_CAPTCHA_SID = "captcha_sid";

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
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Error e = (Error) o;
        return (code == e.code &&
                Objects.equals(message, e.message) &&
                Objects.equals(requestParams, e.requestParams) &&
                Objects.equals(captchaUrl, e.captchaUrl) &&
                captchaSid == e.captchaSid);
    }

    @Override
    public int hashCode() {
        return ((((Integer.hashCode(code)) * 31 +
                Objects.hashCode(message)) * 31 +
                Objects.hashCode(requestParams)) * 31 +
                Objects.hashCode(captchaUrl)) * 31 +
                Long.hashCode(captchaSid);
    }

    @Override
    public String toString() {
        return getClass().getName() + '(' +
                "code=" + code + ',' +
                "message=" + message + ',' +
                "requestParams=" + requestParams + ',' +
                "captchaUrl=" + captchaUrl + ',' +
                "captchaSid=" + captchaSid +
                ')';
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
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            RequestParam p = (RequestParam) o;
            return Objects.equals(key, p.key) && Objects.equals(value, p.value);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key) * 31 + Objects.hashCode(value);
        }

        @Override
        public String toString() {
            return getClass().getName() + '(' +
                    "key=" + key + ',' +
                    "value=" + value +
                    ')';
        }
    }
}
