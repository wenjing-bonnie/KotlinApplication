# Jetpack

* MVVM+Jetpack Google标准化Jetpack架构模式 未来的趋势
* Jetpack框架中的常用组件有： Lifecycle ViewModel LiveData Paging Room Navigation WorkManager 七个组件。

## 一、Compose基础

### 1.编程思想

* 可组合函数：就是构建界面的函数，也就是被@Composable标记的函数。 可组合函数可以调用其他的可组合函数来发出界面层次结构。
* 可组合函数不需要返回任何内容。仅仅是描述的是屏幕的状态，而不是构造界面的微件。
* 一个被@Composable标记的被称为一个微件。
* 在命令式界面模式中，如需更改某个微件，可以调用setter已更改其内部状态。 而在Compose，微件无setter/getter。可以调用带有不同参数的同一可组合函数来更新界面。
* 可组合项负责在每次可观察数据更新时，将当前应用状态转换为界面。可以使用新数据再次调用可组合函数，导致重新绘制界面元素，称为重组。
* 可组合函数可以接收动态数据来更新 。

这样就会导致函数进行重组（系统会根据需要使用新数据重新绘制函数发出的微件）。 Compose框架可以智能的仅重组已更改的组件。

### 1.管理状态

#### (1) Compose通过状态变化来更新UI

* Android应用都会向用户显示状态。下面就是一些状态实例：
    - 无法建立网络显示的信息提示控件
    - 博文和相关评论
    - 用户点击按钮时播放的波纹动画
    - 用户可以在图片上贴贴纸
* Compose是声明式工具集，更新界面的唯一方法就是通过新参数调用同一可组合项。每次状态更新的时候，都会发生重组更新界面。
* 在通俗点：[Compose更新UI的时候，是通过状态变化(该状态就是对应这UI中的某些对象值)，引起重组，从而刷新UI]
* 关键术语：
    - [组合] 对执行可组合项时构建的界面描述
    - [初始组合] 通过首次运行可组合项创建组合
    - [重组] 在数据发生变化时重新运行可组合项以更新组合
* 可组合函数可使用`remember`为可组合项记住单个对象。系统会在初始组合期间将由`remember`计算的值存储在组合中，并在重组期间返回存储的值[TODO 这句话还没有理解]。
* `remember`会将对象存储在组合中，当调用到`remember`的组合项从组合中移除后[TODO 怎么移除呢]，它就会忘记该对象。

#### (2) `remember`+`mutableStateOf`来得到一个可观察的状态(对应到一个变量)，状态变化，会自动重组使用该状态的可组合函数

* `mutableStateOf`创建可观察的`MutableState<T>`：如果里面的value发生变换，系统就会重新读取value的所有可组合函数，然后进行重组。

```
    var mutableName by remember {
        mutableStateOf("123")
    }
    val mutableName1 = remember {
        mutableStateOf(name)
    }
    // [TODO 这种定义的语法没有看明白]
    val (mutableName2, setValue) = remember {
        mutableStateOf(name)
    }
```

* Compose并不要求使用`MutableState<T>`存储状态。支持其他可观察类型。在读取其他可观察类型之前，必须转换为State<T>。
* 常用的可观察类型创建State<T>：
    - [LiveData] 也应该就是jetpack中去更新UI的方式
    - Flow
    - RxJava2
* Compose将通过读取`State<T>`对象自动重组界面。如果使用LiveData等其他可观察类型，应该先将其转换为`State<T>`，然后在使用LiveData<T>
  .observeAsState()之类的可组合扩展函数在可组合项中读取。

#### (3)`remember`和`rememberSaveable`区别

* `remember`和`rememberSaveable`是可以用来记住一个对象，在系统这边有一个配置（这个配置就是Bundle）来存储这个对象。`remember`不会将对象存储到这个配置(
  也就是Bundle)中，而`rememberSaveable`不仅记住该对象，并且还将对象存储到这个配置(也就是Bundle)。
    - `rememberSaveable`应用场景显然就是可以在重新创建 activity 和进程后保持状态
* 当使用`rememberSaveable`的时候，只要添加到Bundle的类型都会自动保持。但是也有一些对象无法要保存无法添加到Bundle。解决方案有如下几种：
    - 将要存储对象的类添加`@Parcelize`注解
    - 使用`mapSaver`定义自己的规则原理：就是将类的属性以value的形式存放到map中，然后使用的时候，从map中根据key找到value，然后创建出对象返回。
      ``` 
      val CitySaver = run {
          val nameKey = "Name"
          val countryKey = "Country"
           mapSaver(
                //保存的时候按照map分别进行保存每个字段
                save = { mapOf(nameKey to it.name, countryKey to it.country) },
                //使用的时候，将map中的值取出来，然后赋值给对象的参数
                restore = { City(it[nameKey] as String, it[countryKey] as String) })
        }
        val selectedCity by rememberSaveable(stateSaver = CitySaver) {
        mutableStateOf(City("北京", "China"))
      }
      ```
    - 使用`ListSaver`定义自己的规则原理：就是将类的属性依次存放到List中，然后使用的时候，从List中依次取出创建对象返回.
    ``` 
      val CityListSaver = listSaver<City, Any>(
         save = { listOf(it.name, it.country) },
        restore = { City(it[0] as String, it[1] as String) }
      )
  
    ```

#### (4) compose管理状态

* 对于简单的状态，例如一个变量就可以完成一个UI变化，可以采用`remember`+`mutableStateOf`来进行监听变化，然后进行重组。
* 但是如果跟踪的状态数增加或者组合函数中出现要执行的逻辑 ->将逻辑和状态事物委派给其他类（状态容器）
* [状态容器] 管理可组合项的逻辑和状态。状态容器可以称为[提升的状态对象]
    - 可组合项：简单的界面元素状态
    - 状态容器：复杂的界面元素状态，且拥有界面元素的状态和界面逻辑。
    - ViewModel：特殊的状态容器，用于业务逻辑以及屏幕或界面状态的访问权限。
* 状态的类型
    - 界面元素状态：界面元素的提升状态。（大胆猜想：仅仅为UI状态变化，如有未收藏状态->收藏状态的UI变化）
    - 屏幕或界面状态：就是屏幕上需要显示的内容。该状态通常会与其他层相关联。（大胆猜想：譬如购物车初始化界面）
* 逻辑类型
    - 界面行为逻辑或界面逻辑：与屏幕上显示状态变化相关。例如：导航逻辑决定接下来显示哪个屏幕
    - 业务逻辑：如果处理状态变化。例如：对购物车内容删除。该逻辑位于业务层或数据层

### 2.生命周期慨览

#### (1) Compose 生命周期为初始化 -> 执行0次或多次重构 -> 退出组合

* Compose的生命周期：进入组合 -> 执行0次或多次重组 -> 退出组合
* 重组由State<T>对象的更改触发。Compose会跟踪这些操作，并运行读取使用State<T>对象的所有组合项。进行更新界面

#### (2) 一般来说可组合项(理解为一个Composable 微件)被调用多次，就存在多个实例。

* 当某一组合项(这个组合项就理解为一个Composable 微件)被多次调用，在组合中将放置多个实例。每次调用在组合中都有自己的生命周期。
    - 这句话通俗点理解就是当一个组合项中有多个相同的Composable 微件，那么每个微件都是一个实例，那么就存在多个微件实例。
* 当可组合项需要管理生命周期确实更复杂的外部资源与之互动时，[应使用效应]。[TODO 这句话还没有理解]

### (3) 可组合项的实例是有可调用点[调用可组合项的源代码位置]进行标识。若在同一个调用点有多次可组合项的实例的创建,在重组的时候，如果输入未发生改变，Compose会避免重组这些可组合实例。

* 组合中的可组合项(这个组合项就理解为一个Composable 微件)的实例有其调用点（调用点就是调用这个微件的源代码位置）进行标识。
    - 在重组期间，[可组合项（这个理解为调用微件的那个微件函数）]调用的[可组合项（理解为一个Composable 微件）]与上一次的[可组合项（这个理解为调用微件的那个微件函数）]
      调用的[可组合项（理解为一个Composable 微件）]不同。如果在这两次调用过程中，其中[可组合项（理解为一个Composable 微件）]
      的输入未改变，那么Compose会避免重组这些[可组合项（理解为一个Composable 微件）]。
    - [通俗点理解上面]
      ：在重组期间，如果该微件中调用的另外一个微件（为方便描述，称为微件A）的输入没有发生变化，那么Compose会跳出对微件A的重组，但是该微件A的实例仍会保留在这次重组过程中，只不过和上一次是同一个实例。

### (4) 智能重组 - 用view复用的思想来思考这个问题 - 针对的是在同一个调用点有多次调用同一可组合项(这个组合项就理解为一个Composable 微件)

* 总结几句 ->解决方案的宗旨就是尽量保持输入的不变
    - 通过唯一的key来封装重复调用的可组合项(这个组合项就理解为一个Composable 微件)

* [智能重组]
  多次调用同一可组合项（即多次调用同一微件），将会多次添加到组合中。但是如果从同一个调用点多次调用同一可组合项（即多次调用同一微件），那么Compose将无法唯一标识对该可组合项（即多次调用同一微件）的每次调用。
  这种情况出现在列表的可组合项中。例如：
  ```  
    @Composable
    fun MoviesScreen(movies: List<Movie>) {
        Column {
            for (movie in movies) {
                  // MovieOverview composables are placed in Composition given its
                  // index position in the for loop
                  MovieOverview(movie)
            }
        }
    }
  ```
    - 解决方案：使用执行顺序来区分实例。像上面的实例中集合movies的内容变化将会引起MoviesScreen重组
        - 第一种情况：假设直接往集合movies后面添加新的Movie，那么Compose就可以直接使用既有的MovieOverview实例。
          但是如果往集合movies顶部或中间新增内容，就会引起MovieOverview的调用发生重组。还有如果MovieOverview有附带效应（即为列表添加了header）
        - 理想情况下，我们认为MovieOverview实例的身份应该与movie的身份相关联，若对影片进行重排序，理想情况下，应该只是对MovieOverview实例的进行重排序。
        - 解决方案：对MovieOverview通过唯一的key进行封装代码块。该key值不必全局唯一，只需要在调用点处可组合项的作用域内确保其唯一性即可。并且附带效应也将继续执行。
            - 对于LazyColumn中自带了 key： items(movies, key = { movie -> movie.id })
        ``` 
          @Composable
          fun MoviesScreen(movies: List<Movie>) {
                Column {
                      for (movie in movies) {
                          key(movie.id) { // Unique ID for this movie
                              MovieOverview(movie)
                          }
                      }
                }
          }
        ```
    
* 组合中已有的可组合项，当输入都处于稳定状态且没有变化时，可以跳过重组。稳定类型必须符合以下协定： [TODO 下面的这些内容暂时没有理解]
    - 相同的两个实例，即equals相同
    - 如果类型的某个公共属性发生变化，组合将收到通知
    - 所有公共属性类型也都是稳定。
* Compose被认为是稳定类型：
    - 被@Stable注解的类型
    - 即使未被@Stable标记，也被认为是稳定类型：
        - 所有基元值类型：如Boolean、Int等
        - 字符串
        - 所有的函数类型(lambda)
* 上面是不可变的类型，永远不笔通知组合更改方面的信息。

## 二、Compose 声明式UI的设计

### 1。

* 声明式UI，改变的式UI的状态。
* Compose UI组合方式。代替之前View ViewGroup 继承关系
* 自动更新策略
* 只会进行一次测量，替换之前的多次测量
* @Composable：用来标记构建View的方法。称为一个微件

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

* pushStringAnnotation()来创建一个Text。点击Text的时候，需要附加额外信息。例如特定字词下面附加浏览器链接地址。需要此时下面三个参数：
    - 一个标记(String)
    - 一个项(String)
    - 一个文字范围
    ``` 
      val annotatedText = buildAnnotatedString {
        append("Click")
        pushStringAnnotation(tag = "URL", annotation = "https://baidu.com")
        withStyle(style = SpanStyle(color = Color.Blue)) {
            // 要添加额外信息的文字
            append("here")
        }
        //将"URL"注释附加到下面的内容直到调用pop()
        pop()
        }
    //可通过下面的方式读取相应的信息
      ClickableText(text = annotatedText, onClick = { offset ->
        println("clickable is ${offset}")
        // url返回的是[Range(item=https://baidu.com, start=5, end=9, tag=URL)]
        val url = annotatedText.getStringAnnotations(tag = "URL", start = offset, end = offset)
        println("url ${url}")
        //通过这种方式取出
        url?.let {
            println(it.firstOrNull()?.let {
                println("${it.item}, ${it.start},${it.end}, ${it.tag}")
            })
        }
    })
    ```
* 输入和修改文字
    - TextField：默认的是填充样式。OutlinedTextField：轮廓样式
    - BasicTextField：允许用户通过硬件或者软件键盘编辑我脑子，但没有提供提示或占位符等装饰。
    - 若是设计中调用是Material TextField或OutlineTextField，建议使用TextField。无需Material规范，直接使用BasicTextField
    - 设置键盘的类型：keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    ```  
            keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Text,
            autoCorrect = true,
            capitalization = KeyboardCapitalization.Words,
            imeAction = ImeAction.Search)
    ```
    - 通过visualTransformation = PasswordVisualTransformation('*')来将输入的内容转换成特殊字符。

*

### 5.Compose之状态管理 [TODO 这个地方还没有搞清楚]

* mutableStateOf()：使得变量有了被观察的才能，当值发生改变的时候就会告诉运用该变量

### 6.图形Canvas

* 需要知道哪些配置项是在Paint中设置，哪些是在方法调用设置
* Canvas：精确控制元素的样式和位置来绘制元素。通过`Canvas() {}`来创建画布，然后通过drawLine/drawRect/drawCircle来创建图形。
* 每个`Canvas() {}`提供`DrawScope`(限定了作用域的绘图环境)
    - 可通过`DrawScope.insert()`来调整作用域的默认参数，以改变绘图的边界。
    - 通过` rotate(degrees = 20.0f) {}`对图形进行旋转
    - 若要对绘图执行多个转换，不能创建嵌套的`DrawScope`进行操作，而是使用`withTransform({ 增加多次变换逻辑 }){ }`

### 7. @Preview：

* 在不运行APP的情况下确认布局。常见参数有：
    - name:String：该名字会显示在预览布局中。
    - showBackground:Boolean：是否显示背景。
    - backgroundColor:Long：设置背景颜色。
    - showDecoration:Boolean：是否显示statusbar和toolbar。
    - group:String：设置group名字，在UI中分组显示
    - fontScale:Float：对预览字体放大，范围从0.01
    - widthDp:Int：最大宽度，单位dp
    - heightDp:Int：最大高度，单位dp

### 8.延迟可组合项

* LazyColumn和LazyRow
* 不接受@Composable内容块参数，而是提供一个`LazyListScope.()`块。即在`LazyColumn(content = {item {这里可接收@Composable}})`
  。通过`item{}`来添加单个列表项。通过`item(Int){}`用于添加多个列表项。
* 其中参数contentPadding：设置的是整个列表的padding，相当于设置之前对ListView这个控件设置padding
* LazyColumn其中参数`verticalArrangement = Arrangement.spacedBy()`
  ：设置的每个item之间的垂直方向的间距。那么对于LazyRow其中的参数就是`horizontalArrangement = Arrangement.SpaceEvenly`在水平方向的间距
* `LazyVerticalGrid(cells = GridCells.Adaptive(minSize = 50.dp)) {}`创建网格式UI。
    - `GridCells.Adaptive()`要求每列至少给定值的宽度。
    - `GridCells.Fixed()`固定列数

## 二、主题

### 1.主题分类

* Material主题设置：
* 自定义设计系统
* 主题详解

### 2. MaterialTheme

* 由颜色、排版和形状属性组成。
*

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
