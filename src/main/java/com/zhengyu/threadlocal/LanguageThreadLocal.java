package com.zhengyu.threadlocal;

public class LanguageThreadLocal {
    private static ThreadLocal<LanguageContext> languageThreadLocal = new ThreadLocal<LanguageContext>() {
        @Override
        protected LanguageContext initialValue() {
            return super.initialValue();
        }
    };


    public static void setLanguageContext(LanguageContext languageContext) {
        languageThreadLocal.set(languageContext);
    }

    public static void remove() {
        languageThreadLocal.remove();
    }

    public static LanguageContext getLanguageContext() {
//        return Optional.ofNullable(languageThreadLocal.get()).orElseThrow(NullPointerException::new);
        return languageThreadLocal.get();
    }

}
