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
        KotlinToJavaKt kt = new KotlinToJavaKt();
        //kt.getNameField();
        System.out.println(kt.nameField);
        kt.show("zhangsan", 12, 'F');
        kt.show("zhangsan");
        //InnerClass.Companion.getName();
        //InnerClass.Companion.show();
        String name = InnerClass.name;
        InnerClass.show();
    }
}
