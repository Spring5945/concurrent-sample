package com.zhengyu.threadlocal;

import com.alibaba.ttl.TransmittableThreadLocal;

public class LanguageTransmittableThreadLocal {
    static TransmittableThreadLocal<LanguageContext> languageContext = new TransmittableThreadLocal<LanguageContext>() {
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
        return languageContext.get();
    }
}
