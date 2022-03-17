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

/*    private void ge() {
        //不能调用含有扩展函数的类，因为在KotlinRxjava.kt中对所有的T进行了扩展，所以此时这里编译不通过
        KotlinToJavaKt kt = new KotlinToJavaKt();
        //kt.getNameField();
        System.out.println(kt.nameField);
        kt.show("zhangsan", 12, 'F');
        kt.show("zhangsan");
        //InnerClass.Companion.getName();
        //InnerClass.Companion.show();
        String name = InnerClass.name;
        InnerClass.show();
    }*/
}
