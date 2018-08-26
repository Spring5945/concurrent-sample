package com.zhengyu.threadlocal;

import java.util.Optional;

public class LanguageInheritableThreadLocal {

    static InheritableThreadLocal<LanguageContext> languageContext = new InheritableThreadLocal<LanguageContext>() {
        @Override
        protected LanguageContext initialValue() {
            return super.initialValue();
        }
    };

    public static void setLanguageContext(LanguageContext language) {
        languageContext.set(language);
    }

    public static void remove() {
        languageContext.remove();
    }

    public static LanguageContext getLanguageContext() {
        return Optional.ofNullable(languageContext.get()).orElseThrow(NullPointerException::new);
    }
}
