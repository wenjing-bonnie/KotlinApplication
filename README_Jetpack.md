# Jetpack

MVVM+Jetpack Google标准化Jetpack架构模式 未来的趋势

## 一、Compose 声明式UI

### 1。

* 声明式UI，改变的式UI的状态。
* Compose UI组合方式。代替之前View ViewGroup 继承关系
* 自动更新策略
* 只会进行一次测量，替换之前的多次测量

### 2.Column/Row/Box

* Column/Row/Box ：类似于纵向/横向/FrameLayout的ViewGroup。对应的样式为[ui/layout-column-row-box.png]。
* Column：纵向布局。默认的从左上角开始放置child。可以设置下面的参数来改变排放方式。
    - modifier: Modifier
    - verticalArrangement: Arrangement.Vertical。垂直排放child的方式。
      也可传入Center、Start、End、SpaceEvenly、SpaceBetween、SpaceAround。
        - SpaceEvenly：各个元素的空隙等比例，每个元素的左右都有间隙。
        - SpaceBetween：第一个之前和最后一个之后没有空隙，其他按等比例空隙放入各个元素之间。
        - SpaceAround：第一个之前和最后一个之后的间隙是一半，其他按2倍的间隙放入到其他各元素之间。
    - horizontalAlignment: Alignment.Horizontal。在水平方向上child的对齐方式。
* Row：
    - modifier: Modifier
    - horizontalArrangement: Arrangement.Horizontal：水平排放child的方式。
      也可传入Center、Start、End、SpaceEvenly、SpaceBetween、SpaceAround。
        - SpaceEvenly：各个元素的空隙等比例，每个元素的左右都有间隙。
        - SpaceBetween：第一个之前和最后一个之后没有空隙，其他按等比例空隙放入各个元素之间。
        - SpaceAround：第一个之前和最后一个之后的间隙是一半，其他按2倍的间隙放入到其他各元素之间。
    - verticalAlignment: Alignment.Vertical。在垂直方式上child的对齐方式
* Box：类似于FrameLayout
    - matchParentSize仅在Box作用域内。子布局与父布局Box尺寸相同，但不影响到Box尺寸。

### 3.Modifier：用于设置UI摆放位置、padding等信息。链式调用

* 修饰符的顺序非常重要，每个都会对上一个返回的Modifier进行修改。譬如

```
          Modifier
            .clickable(onClick = onClick)
            .padding(padding)
            .fillMaxWidth()
```

该点击区域含有padding的区域。可以理解为：先设置了该区域都可点击，然后又为该区域添加了padding。所以点击区域包含padding。

``` 
     Modifier
            .padding(padding)
            .clickable(onClick = onClick)
            .fillMaxWidth()
```

该点击区域就无padding的区域。可以理解为：先设置了padding，在设置点击事件的时候，并没有作用到padding上。所以点击区域无padding。

* 如果指定的尺寸不符合来自布局父项的约束条件，则可能不采用该尺寸。如果希望组合项的尺寸固定不变，则使用requiredSize()
* Modifier.padding()：元素周围留出空间。设置的值必须为Dp。在Int中扩展一个方法dp，最终会转换成Dp。共四个重载方法。例如20.dp
* Modifier.paddingFromBaseline()：如需在文本基线上方添加内边距，实现从布局顶部到基线保持特定距离。
* Modifier.offset()：相对于原始布局的偏移量 。相对于padding，并没有改变测量结果。
* Modifier.align()：设置在Column中的元素位置
* Modifier.plus(otherModifier)：可以把其他的modifier加入到当前Modifier中
* Modifier.fillMaxHeight/fillMaxWidth/fillMaxSize。类似于match_parent
* Modifier.width/height/size：设置content的宽和高
* Modifier.widthIn/heightIn/sizeIn：设置content的宽和高的最大值和最小值。
* Modifier.Modifier.gravity(Alignment.CenterHorizontally)：设置在Column中的元素位置 [TODO 不知道为什么没有提示处理]
* Modifier.rtl/ltr：开始布局的方法。从右向左/从左向右。[TODO 不知道为什么没有提示处理]
* Modifier.weight：所占的比例大小。仅适用于Column和Row

### 4.Text

* Text()：若是显示字符串，则直接传入即可，如Text("xxxx")；若是显示资源中的文字，如Text(stringResource(R.string.xxx))
* buildAnnotatedString{}来创建字符级别的不同样式的字符串。该匿名函数中持有的是AnnotatedString.Builder，如下：
    - SpanStyle：用于字符级别应用
    - ParagraphStyle：应用于整个段落。使用该标记的文字，会与其他部分隔开，就像在开头和结尾有换行符一样。

```  
    Text(
        buildAnnotatedString {
            withStyle(style = SpanStyle(color = Color.Blue)) {
                append("Blue")
            }
            append("111") ddd
            withStyle(style = ParagraphStyle(lineHeight = 30.sp)) {
                withStyle(style = SpanStyle(color = Color.Red)) {
                    append("Hello\n")
                }
            }
        },
        fontSize = 30.sp
    )
```    

* maxLines：最大行数
* overflow：文字溢出。TextOverflow.Ellipsis
* SelectionContainer {}：默认的是无法选择和赋值的，需要将Text放到该匿名函数中，才可以选择文字
    - 特定部分停用选择功能：使用DisableSelection{}来包括这部分Text
* ClickableText()：监听Text点击的次数。可在点击事件中获取点击位置。

``` 
    ClickableText(text = AnnotatedString("Click Me"), onClick = { offset ->
        println("clickable is ${offset}")
    })
```
* 



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
