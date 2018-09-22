package com.zhengyu.threadlocal;

public class LanguageContext {
    String language;
    String locale;
    String code;

    private LanguageContext(Builder builder) {
        language = builder.language;
        locale = builder.locale;
        code = builder.code;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public String getLanguage() {
        return language;
    }

    public String getLocale() {
        return locale;
    }

    public String getCode() {
        return code;
    }

    @Override
    public String toString() {
        return "LanguageContext{" +
                "language='" + language + '\'' +
                ", locale='" + locale + '\'' +
                ", code='" + code + '\'' +
                '}';
    }

    public static final class Builder {
        private String language;
        private String locale;
        private String code;

        private Builder() {
        }

        public Builder withLanguage(String val) {
            language = val;
            return this;
        }

        public Builder withLocale(String val) {
            locale = val;
            return this;
        }

        public Builder withCode(String val) {
            code = val;
            return this;
        }

        public LanguageContext build() {
            return new LanguageContext(this);
        }
    }
}
