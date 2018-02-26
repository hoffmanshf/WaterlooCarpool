package com.hoffman.carpool.error;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.lang3.StringUtils;

public class UServiceException extends RuntimeException {

    private String context;
    private final String code;
    private final Map<String, Object> attributes = new HashMap<>();

    public UServiceException(String code, String context, String description) {
        this(code, context, description, null);
    }

    public UServiceException(String code, String context, String description, Throwable cause) {
        super(description, cause);

        this.code = code;
        this.context = context;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String key) {
        return (T) attributes.get(key);
    }

    @SuppressWarnings("unchecked")
    public <T extends UServiceException> T set(String key, Object value) {
        attributes.put(key, value);
        return (T) this;
    }

    @SuppressWarnings("unchecked")
    public <T extends UServiceException> T setAll(Map<String, Object> attributes) {
        attributes.forEach((key, value) -> {
            this.attributes.put(key, value);
        });
        return (T) this;
    }

    public Map<String, Object> attributes() {
        return Collections.unmodifiableMap(attributes);
    }

    public String getCode() {
        return code;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getDescription() {
        return super.getMessage();
    }

    @Override
    public String getMessage() {
        String message = super.getMessage();
        if (message == null) {
            message = "";
        }

        if (!StringUtils.isBlank(getCode())) {
            message += "  code=" + getCode();
        }

        if (!StringUtils.isBlank(getContext())) {
            message += "  context=" + getContext();
        }

        return message;
    }
}
