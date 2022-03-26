# Jetpack

## 一、架构组件

* Lifecycle：新的生命周期感知型组件
* LiveData：创建数据对象，在基础数据库改变时通知视图
* ViewModel：存储界面相关的数据，这些数据不会在应用旋转时销毁
* Room：SQLite对象映射库。可以将SQLite表转换成java对象，并且返回Rxjava、Flowable、LiveData可观察对象
* 最佳做法
    - Activity/Fragment尽可能保持精简。随着数据更改而更新视图，或将用户操作通知给ViewModel
        - Activity/Fragment不要获取自己的数据，通过ViewModel执行此操作
        - 观察LiveData对象以更改体现到视图中
    - ViewModel为Activity/Fragment和其他应用部分的连接器。
        - ViewModel不负责获取数据(例如从网络上获取数据)，但是是调用其他组件来获取数据，然后将结果提供给Activity/Fragment
        - 避免在ViewModel中引用View或Context。ViewModel的生命周期要比Activity长，避免内存泄漏
    - LiveData进行数据绑定可以在视图与Activity/Fragment？？？？？
    - Coroutine 协程管理长时间运行的任何和其他可以异步运行的操作。

### 1.MVVM

* Activity/Fragment为View层
* ViewModel+LiveData为ViewModel层。
    - 专门存放界面相关数据，即界面上能看到的数据，其相关变量都应该存放到ViewModel中，从而减轻Activity工作。
    - 手机在横竖屏旋转的时候，Activity会重新创建，存放到Activity中的数据会丢失，但是ViewModel不会重新创建，只会当Activity退出的时候，才会销毁。
    - 理论上每个Activity/Fragment都会创建一个ViewModel
* 为了统一管理网格数据和本地数据，又引入了Repository中间管理层。本质是为了更好管理数据，可以称为Model层

### 2.ViewBinding 视图绑定 - [视图绑定代替之前的findViewById/setContentView]

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

### 2.Lifecycle - [Activity/Fragment将生命周期转移到Lifecycle进行处理]

* [什么是Lifecycle] Lifecycle是一个类，为Activity/Fragment生命周期的观察类，监听生命周期的变化并可以做出响应。并允许其他对象观察此状态。
* [解决的问题] (1)在组件的生命周期中放置大量的代码，难以维护。(2) 无法保证长时间运行的操作，无法保证在Activity/Fragment停止之前启动(
  例如：在onStart里面启动定位，但是还没有启动完成，就关闭了页面，导致该定位服务没有启动起来)。
* [实现方式]
    - 自定义类实现`DefaultLifecycleObserver`
    - 通过`Lifecycle.addObserver()`传递观察器的实例来添加观察器
* [两个角色] `观察者模式：被观察对象(LifecycleOwner)发生变化的时候，通知观察者，所以被观察对象需要注册观察者。观察者(LifecycleObserver)负责监听被观察对象的变化。`
    - 第一个是[LifecycleOwner]：接口类，只有一个`Lifecycle getLifecycle()`需要实现，在kotlin中就是lifecycle的成员变量需要复写
      。生命周期拥有者，被观察者。如果一个类实现该接口，那么该类就具有了Lifecycle。
        - support库26.1.0及更高版本中的AppCompatActivity/Fragment已经实现该接口。
        - ProcessLifecycleOwner：监听整个应用进程的生命周期。[TODO 这个以后在研究下！！！！！！！！]
            - 仅调用观察者的`onCreate`一次
            - 不会调用观察者的`onDestroy`
            - 仅在当一个Activity执行生命周期的时候才调用观察者的`onStart/onResume`
            - 最后一个Activity执行生命周期的时候延时调用观察者的`onPause/onStop`。该延时可以保证在Activity销毁和配置更改时重建时不发送任何消息。
            - 只针对当前进程
        - 自定义类实现LifecycleOwner，那么在得到Lifecycle的时候，通过` lifecycleRegistry = LifecycleRegistry(this)`
          得到一个`LifecycleRegistry`，将事件转发到该类。
          ```  
              public override fun onStart() {
                    super.onStart()
                    lifecycleRegistry.markState(Lifecycle.State.STARTED)
                }

                override fun getLifecycle(): Lifecycle {
                    return lifecycleRegistry
                }
          ```

    - 第二个是[LifecycleObserver]
      ：生命周期观察者，实现该接口后Owner就可以将该Observer添加Lifecycle中，从而在owner的生命周期发生改变时能马上接收到通知。
        - 自定义类实现`DefaultLifecycleObserver`接口即可
        - 通常在Activity的onCreate()进行实例化

* [owner注册observer] getLifecycle()
  /lifecycle获得Lifecycle，进而通过`getLifecycle()/lifecycle.addObserver(observer)`添加观察者。
* [应用场景]
    - 开始和停止网络连接。在应用处于前台时启用网络数据实时更新，在进入后台时自动暂停
    - 暂停和恢复动画可绘制资源
    - 停止和开始缓冲视频
    - 粗粒度和细粒度位置更新切换。
* [处理onStop事件]
    - onSaveInstanceState()不是生命周期方法，在应用由onPause -> onSaveInstanceState -> onStop的时调用。
    - 在AppCompatActivity/Fragment实现了Lifecycle接口，那么在调用onSaveInstanceState()时，会将状态变更为CREATED(
      即调用到Observer的onCreate)，不在分发onStop事件(即不在调用Observer的onStop)。
    - LiveData在避免这种极端情况：在观察者管理的Lifecycle还没有至少处于Started状态时避免调用其观察者。在后台，在调用其观察者之前调用isAtLeast()。
      [这个在看LiveData的时候在详细看下！！！！]

### 3. LiveData - [具有生命周期感知的可观察的数据存储类]

* 在Compose中：通过`remember`+`mutableStateOf`可以创建一个可观察的对象。
* 与其他可观察类不同的是：LiveData具有生命周期感知能力，可遵循其他组件(Activity、Fragment或Service)的生命周期。
* LiveData仅更新处于活跃状态的应用组件观察者。
  [从这句话中可以似乎可以总结为：LiveData是被观察者，负责注册观察者，而观察者是在Activity在/Fragment。发生变化的，及时通知观察者]
* 当观察者Observer的生命周期处于Started和Resumed，则LiveData会将该观察者为活跃状态。那些虽然注册LiveData但非活跃观察者不会收到通知。
* 与注册实现LifecycleOwner接口的对象为观察者。当Lifecycle对象的状态变为Destoried时，便可移除次观察者。
* AppCompatActivity/Fragment实现了LifecycleOwner接口，可以放心观察LiveData对象，而不必担心泄漏
  （在Activity）/Fragment生命周期被销毁时，系统会立刻退订它们）。
* [优势]
    - LiveData遵循观察者模式。当底层数据发生变化的时候，会自动通知Observer，通常这些Observer对象就是用来更新界面。从而在应用数据发生改变的时候自动更新UI。
    - 不会发生内存泄漏。观察者即Activity/Fragment实现了Life
    - [TODO 未完]
* [如何使用LiveData]
    - (1)在ViewModel中创建LiveData实例
        - `MutableLiveData` ：公开setValue/getValue的LiveData。
            - 可通过MutableLiveData()和MutableLiveData(T value)来创建一个对象，参数就是为观察对象设置的初始值。
            - 通常在ViewModel中公开不可变的LiveData对象。
        - `SliceLiveData.CachedSliceLiveData`
        - `MediatorLiveData`
    - (2)在Activity/Fragment中创建Observer对象，该方法可以控制在LiveData对象存储的数据发生更改的时候会发生什么。
    - (3)在Activity/Fragment中通过observe()
      将Observer对象附加到LiveData对象上。` viewModel.currentName.observe(this, observer)`。
        - 通常在onCreate()开始观察LiveData对象
        - 可通过observeForEver(Observer)
          为没有关联LifecycleOwner对象的情况下注册一个观察者。此时会认为该观察者始终为活跃状态，直到调用removeObserver(Observer)来移除这些观察者。
    - (4)调用setValue(主线程)/postValue(工作线程)更新LiveData里面的对象，然后触发Observer的onChange，从而更新UI。
* []

### 4.ViewModel - [为页面准备数据，在配置更改期间自动保留ViewModel对象，在Activity完成时，自动调用onCleared，清理资源]

* [解决问题 1] 页面销毁或重新创建界面控制器，则存储在其中的任何瞬态界面相关数据都会丢失，
  例如在某个Activity中有一个列表，为配置更改后重新创建Activity后，新Activity必须重新拉取数据。 对于简单的数据，可以通过`onSaveInstanceState()`
  从`onCreate()`中的bundle中恢复数据，但仅适合可以序列化的少量数据。 不适合可能比较大的数据，例如用户列表或位图。
* [解决问题 2] 页面的一些异步调用，需要在页面销毁后清理这些调用避免内存泄漏，引起大量维护工作，并且在为配置更改重新创建对象的情况下，会造成资源浪费。
* [生命周期] ViewModel对象存在的范围是：获取`ViewModel`时传给了`ViewModelProvider`的`Lifecycle`。
  `ViewModel`将一直留在内存中，直到限定其存在时间范围的`Lifecycle`永久消失；Activity是activity完成时，对于Fragment是Fragment分离时。
* [ViewModel特点] 将视图的数据分离。
    - 在配置更改期间自动保留ViewModel对象，以便它们存储的数据立即可提供给下一个Activity/Fragment实例使用，使用的是同一个ViewModel对象。
    - 绝对不能引用视图、Lifecycle、或任何含有Context的引用的类
    - 可以含有LifecycleObserver，如LiveData，但是不能更改[TODO这个不理解？？？？那LiveData对象的数据更改在哪里？？？]
    - 若需要Application上下文，需要实现扩展AndroidViewModel类并设置接收Application的构造函数
* [应用场景] 为页面准备数据，可以在配置发生改变的时候，保持当前ViewModel，传递给重新创建的Activity。
    - Fragment 在Activity中的两个或多个Fragment共享数据。


* ViewModel确保数据在社保配置更改后仍然存在。Room在数据库发生更改时通知LiveData

## 二、 coroutine与架构组件

### 1. ViewModelScope

* 为每个ViewModel定义了ViewModelScope。如果ViewModel清除，则此范围启动的协程会自动取消。
* 可以限定仅在ViewModel处于活动状态时才需要完成的工作，当ViewModel清除时，可自动取消工作比避免消耗资源。

``` 
        viewModelScope.launch {
        }
```

### 2. LifecycleScope

* 为每个Lifecycle对象定义了LifecycleScope。在此范围内启动的协程，会随着Lifecycle被消耗时取消。

``` 
        lifecycle.coroutineScope.launch {  }
        lifecycleowner.lifecycleScope.launch {  }
```

* 重启生命周期感知型协程。 默认的LifecycleScope在destoryed的时候会自动取消长时间运行的操作。
  但是某些情况下需要特定状态下执行代码块，并处于其他状态时取消。例如：在started时收集数据流，在stopped时取消收集。 -
    - 通过 ` lifecycle.coroutineScope.launch { }`或`lifecycleowner.lifecycleScope.launch { }`创建一个协程
    - 通过`repeatOnLifecycle`来设置在started状态时运行代码
  ```  
          this.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED){
                
            }
        }
  ```  
* 挂起生命周期：lifecycle.whenCreated/whenStarted/whenResumed：挂起在这些块内运行的任何协程。  

### 3.与LiveData一起使用。

* 在使用LiveData时，可能需要异步计算。例如