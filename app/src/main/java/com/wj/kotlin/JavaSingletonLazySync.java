package com.wj.kotlin;

/**
 * create by wenjing.liu at 2022/3/17
 */
public class JavaSingletonLazySync {
    private static volatile JavaSingletonLazySync singletonLazySync;

    public static JavaSingletonLazySync getSingletonLazySync() {
        if (singletonLazySync == null) {
            synchronized (JavaSingletonLazySync.class) {
                singletonLazySync = new JavaSingletonLazySync();
            }
        }

        return singletonLazySync;
    }

    private void ge() {
       // KotlinSpecialClassKt cls = new KotlinSpecialClassKt();

    }
}
