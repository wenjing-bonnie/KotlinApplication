# Jetpack

MVVM+Jetpack Google标准化Jetpack架构模式 未来的趋势

## 一、Compose 声明式UI

### 1。

* 声明式UI，改变的式UI的状态。
* Compose UI组合方式。代替之前View ViewGroup 继承关系
* 自动更新策略
* 只会进行一次测量，替换之前的多次测量

### 2.UI相关

* @Compose：用来标记构建View的方法
* @Preview：在不运行APP的情况下确认布局。常见参数有：
    - name:String：该名字会显示在预览布局中。
    - showBackground:Boolean：是否显示背景。
    - backgroundColor:Long：设置背景颜色。
    - showDecoration:Boolean：是否显示statusbar和toolbar。
    - group:String：设置group名字，在UI中分组显示
    - fontScale:Float：对预览字体放大，范围从0.01
    - widthDp:Int：最大宽度，单位dp
    - heightDp:Int：最大高度，单位dp
* Modifier：用于设置UI摆放位置、padding等信息。链式调用
    - Modifier.padding()：设置的值必须为Dp。在Int中扩展一个方法dp，最终会转换成Dp。共四个重载方法。例如20.dp
    - Modifier.plus(otherModifier)：可以把其他的modifier加入到当前Modifier中
    - Modifier.fillMaxHeight/fillMaxWidth/fillMaxSize。类似于match_parent
    - Modifier.width/height/size：设置content的宽和高
    - Modifier.widthIn/heightIn/sizeIn：设置content的宽和高的最大值和最小值。
    - Modifier.Modifier.gravity(Alignment.CenterHorizontally)：设置在Column中的元素位置 [TODO 不知道为什么没有提示处理]
    - Modifier.rtl/ltr：开始布局的方法。从右向左/从左向右。[TODO 不知道为什么没有提示处理]
* Column/Row ：类似于纵向/横向的ViewGroup
    - modifier: Modifier
    - horizontalArrangement: Arrangement.Horizontal
    - verticalAlignment: Alignment.Vertical
    - 通常Column需要传入 Alignment.Vertical，row传入
      Arrangement.Horizontal。也可传入Center、Start、End、SpaceEvenly、SpaceBetween、SpaceAround。
        - SpaceEvenly：各个元素的空隙等比例，每个元素的左右都有间隙。
        - SpaceBetween：第一个之前和最后一个之后没有空隙，其他按等比例空隙放入各个元素之间。
        - SpaceAround：第一个之前和最后一个之后的间隙是一半，其他按2倍的间隙放入到其他各元素之间。

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
