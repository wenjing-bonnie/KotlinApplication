# Jetpack

## 一、架构组件

* Lifecycle：新的生命周期感知型组件
* LiveData：创建数据对象，在基础数据库改变时通知视图
* ViewModel：存储界面相关的数据，这些数据不会在应用旋转时销毁
* Room：SQLite对象映射库。可以将SQLite表转换成java对象，并且返回Rxjava、Flowable、LiveData可观察对象

### 1.MVVM

* Activity/Fragment为View层
* ViewModel+LiveData为ViewModel层。
    - 专门存放界面相关数据，即界面上能看到的数据，其相关变量都应该存放到ViewModel中，从而减轻Activity工作。
    - 手机在横竖屏旋转的时候，Activity会重新创建，存放到Activity中的数据会丢失，但是ViewModel不会重新创建，只会当Activity退出的时候，才会销毁。
    - 理论上每个Activity/Fragment都会创建一个ViewModel
* 为了统一管理网格数据和本地数据，又引入了Repository中间管理层。本质是为了更好管理数据，可以称为Model层

### 2.视图绑定ViewBinding - [视图绑定代替之前的findViewById/setContentView]

* 将XML布局文件生成一个绑定类。绑定类的实例中包含具有Id的控件的直接引用。
* [原理] 在编译期间会为每个布局文件生成一个绑定类，该绑定类具有以下特点：
    - 包含根视图(root)及具有Id的所有视图引用
    - 将xml名字转换为驼峰式大小写，然后添加"Binding"后缀 -编译之后的文件保存在
      `app/build/generated/data_binding_base_class_source_out/debug/out/com/wj/kotlin/databinding/`
* [配置说明] Android Studio 4.0之后使用ViewBinding模块下的build.gradle下添加

``` 
    buildFeatures {
       .....
        //Android studio 4.0 被整合到buildFeatures选项中
        viewBinding true
    }
```

之前版本配置如下：

``` 
    viewBinding {
        true
    }
```

* [禁止启动] 在需禁止该功能的布局文件中添加`tools:viewBindingIgnore="true"`
* [在Activity中使用ViewBinding] 在`Activity`的`onCreate()`中添加以下逻辑：
    - (1) 定义`ViewBinding`类的实例。例如：`private lateinit var binding: ResultProfileBinding`
      。`ResultProfileBinding`为布局文件生成的`ViewBinding`类。
    - (2) 在`onCreate()`调用`ViewBinding`类的静态方法`inflate()`初始化`ViewBinding`类的实例。
    - (3) 通过`binding.root`获取根视图，将根视图传递到`setContentView()`
      这样就可以通过`binding.`使用设置过id的控件。
* [在Fragment中使用ViewBinding] 在`Fragment`的`onCreateView()`中添加以下逻辑：
    - (1) 定义`ViewBinding`类的实例。例如
      ``` 
          private var _binding: ResultProfileBinding? = null 
            // This property is only valid between onCreateView and
             // onDestroyView.
          private val binding get() = _binding!!
      ```
    - (2) 在`onCreateView()`中对定义`ViewBinding`类的实例初始化。例如
     ``` 
        override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
        ): View? {
            //
            _binding = ResultProfileBinding.inflate(inflater, container, false)
            val view = binding.root
            return view
        }
     ``` 

[**遗留问题： 这里要确认下是不是会有重复添加UI的问题！！！！！！！**]

    - (3) Fragment存在时间比其视图长。所以在`onDestroyView()`必须清除`ViewBinding`类的实例的引用。例如
    ``` 
        override fun onDestroyView() {
            super.onDestroyView()
            _binding = null
        }
    ```

* [在Adapter使用ViewBinding]

``` 
   override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 1.实例化LayoutItemSearchResultBinding对象传给ViewHolder
        //这个inflate()参数设定应该不是最优的方式。
        val binding = LayoutItemSearchResultBinding.inflate(LayoutInflater.from(context), null, false)
        // 2. 将这个binding对象传给**ViewHolder**的构造函数
        return ViewHolder(binding)
    }
```

[**遗留问题：感觉这个inflate()参数设定应该不是最优的方式！！！！！*]

* [优势] 类型安全和空安全
* [与数据绑定对比优势]ViewBinding和DataBinding都可直接引用视图的绑定类。
    - 更快编译速度。不需要处理注释，编译时间更短
    - 易于使用。不需要特别标记的XML布局文件。启动ViewBinding就自动应用于该模块的所有的布局。

* [与数据绑定限制]
    - 视图绑定不支持布局变量或布局表达式，因此不能直接在XML中声明动态布局
    - 不支持双向数据绑定。

### 2. LiveData - [可观察的数据存储类]

* 在Compose中接触过通过`remember`+`mutableStateOf`可以创建一个可观察的对象。
* 与其他可观察类不同的是：LiveData具有生命感知能力，可遵循其他组件(Activity、Fragment或Service)的生命周期。
* 这种感知能力可确保LiveData仅更新处于活跃状态的应用组件观察者。
* 如果观察者(Observer)的生命周期处于Started或Resumed，则LiveData会认为该观察者处于活跃状态。
* LiveData只会将更新通知活跃的观察者。非活跃观察者不会收到通知。
* 注册与实现了LifecycleOwner接口的对象配对的观察者。当相应的Lifecycle对象变为Destoried时，就可移除该观察者。
* Activity/Fragment可以放心观察LiveData对象，而不担心泄漏（在Activity）/Fragment的生命周期被销毁时，系统会立刻退订它们）。
* [优势] 

### 3.ViewModel - [为页面准备数据]

* [解决问题 1] 页面销毁或重新创建界面控制器，则存储在其中的任何瞬态界面相关数据都会丢失，
  例如在某个Activity中有一个列表，为配置更改后重新创建Activity后，新Activity必须重新拉取数据。 对于简单的数据，可以通过`onSaveInstanceState()`
  从`onCreate()`中的bundle中恢复数据，但仅适合可以序列化的少量数据。 不适合可能比较大的数据，例如用户列表或位图。
* [解决问题 2]   页面的一些异步调用，需要在页面销毁后清理这些调用避免内存泄漏，引起大量维护工作，并且在为配置更改重新创建对象的情况下，会造成资源浪费。
* 将视图的数据分离。
* 注重生命周期的方式存储和管理界面相关的数据。可以让数据在发生屏幕旋转等配置更改后继续留存。（那是不是就有一个释放的问题？？？）
* 为界面准备数据。在配置更改期间自动保留ViewModel对象，以便它们存储的数据立即可提供给下一个Activity/Fragment实例使用。
* [生命周期] ViewModel对象存在的范围是：获取`ViewModel`时传给了`ViewModelProvider`的`Lifecycle`。
  `ViewModel`将一直留在内存中，直到限定其存在时间范围的`Lifecycle`永久消失；Activity是activity完成时，对于Fragment是Fragment分离时。
* ViewModel确保数据在社保配置更改后仍然存在。Room在数据库发生更改时通知LiveData

### 1.Lifecycle

* 让某一个类变成Activity/Fragment生命周期的观察类，监听生命周期的变化并可以做出响应。
* 解决的问题：在组件的生命周期中放置大量的代码，难以维护。
* 有两个角色
    - LifecycleOwner：生命周期拥有者，Activity/Fragment实现该接口，通过getLifecycle()获得Lifecycle，进而通过addObserver()
      添加观察者。
    - LifecycleObserver：生命周期观察者，实现该接口后就可以添加Lifecycle中，从而在被观察者类生命周期发生改变时能马上接收到通知。
* 将生命周期的方法转移到 `Lifecycle`：用于存储有关组件(Activity/Fragment)的生命周期状态的信息，并允许其他对象观察此状态。    
    