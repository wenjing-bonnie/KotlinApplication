# Jetpack

MVVM+Jetpack Google标准化Jetpack架构模式 未来的趋势

## 一、Compose 声明式UI
### 1。
* 声明式UI，改变的式UI的状态。
* Compose UI组合方式。代替之前View ViewGroup 继承关系
* 自动更新策略
* 只会进行一次测量，替换之前的多次测量












## 一、

### 1.传统的开发模式下的问题

* Activity/Fragment是大管家，代码臃肿。既要处理逻辑、Model层数据、实时更新UI及代码切换。

### 2.MVVM

* ViewModel：管理UI的数据。
    - 自定义类继承于ViewModel来创建一个ViewModel。
    - 在ViewModel中定义LiveDate实例，通常为MutableLiveData，感应改变数据。
* DataBinding：数据绑定。数据发生变化，布局文件内容会及时更新。
    - 布局文件要交给DataBinding来管理。
    - 需要在build.gradle中设置dataBinding{ enable = true }
    - 在布局文件中添加<layout >
* 在Activity中负责绑定
