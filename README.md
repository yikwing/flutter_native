## android native 混合开发

  [JD-简书](https://juejin.im/post/5b7cf52e51882542c963f0f1)

1. Native集成Flutter

   ```flutter
   flutter create -t module 你要创建的库的名字
   ```

   ` setting.gradle ` 添加代码

   ```groovy
   setBinding(new Binding([gradle: this]))
   evaluate(new File(
           settingsDir.parentFile,
           '/你的工程目录名/flutter_library/.android/include_flutter.groovy'
   ))
   ```

   `build.gradle`添加依赖

   ```groovy
   dependencies {
       implementation project(':flutter'）
   }
   ```

2. Native展示Flutter

   ```kotlin
   val flutterView = Flutter.createView(this,lifecycle, "route1")
   
   addContentView(flutterView,
                  FrameLayout.LayoutParams(
                      FrameLayout.LayoutParams.MATCH_PARENT,
                      FrameLayout.LayoutParams.MATCH_PARENT));
   ```

3. 原生传Flutter数据,flutter接收

   `EventChannel`

   - native

   ```kotlin
   class MainActivity:AppcompatActivity(){
       companion object {
           val GET_NAME_CHANNEL = "sample.flutter.io/get_name"
       }
       
       EventChannel(flutterView, GET_NAME_CHANNEL)
       	.setStreamHandler(object : EventChannel.StreamHandler {
               override fun onListen(p0: Any?, events: EventChannel.EventSink?) {
                   events?.success(getName())
               }
   
               override fun onCancel(p0: Any?) {
   
               }
           })
   }
   ```

   - dart

   ```dart
      
     static const EventChannel eventChannel =
         EventChannel('sample.flutter.io/get_name');
   
     String _name = 'unknown';
   
     @override
     void initState() {
       super.initState();
       eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
     }
   
     void _onEvent(Object event) {
       setState(() {
         _name = event.toString();
       });
     }
   
     void _onError(Object error) {
       setState(() {
         _name = 'Battery status: unknown.';
       });
     }
   ```

4. flutter调用native method

   `MethodChannel`

   - kotlin

   ```kotlin
   class MainActivity:AppcompatActivity(){
       companion object {
           val PUSH_CHANNEL = "sample.flutter.io/push"
           val PULL_CHANNEL = "sample.flutter.io/pull"
       }
       
       MethodChannel(flutterView, PULL_CHANNEL)
       .setMethodCallHandler { methodCall, result ->
               run {
                   if (methodCall.method.equals("refresh")) {
                       showToast()
                       result.success("")
                   } else {
                       result.notImplemented()
                   }
               }
           }
       
       fun showToast(){
           Toast.make().show()
       }
   }
   
   ```

   - dart

   ```dart
   static const MethodChannel methodChannel =
         MethodChannel('sample.flutter.io/pull');
   
   void _receiveData() {
       var invokeMethod = methodChannel.invokeMethod('refresh');
       print(invokeMethod);
   }
   ```

5. 不同route 加载不同的界面

   ```dart
   import 'dart/ui'
   
   void main() => runApp(new MyApp(window.defaultRouteName));
   
   class MyApp extends StatelessWidget {
     final String route;
   
     MyApp(this.route);
   
     @override
     Widget build(BuildContext context) {
       switch (route) {
         case "route1":
           return new MaterialApp(
             title: "Android-Flutter-Demo",
             home: new MyHomePage(title: 'Android-Flutter-Demo'),
           );
           break;
         default:
           return Center(
             child:
                 Text('Unknown route: $route', textDirection: TextDirection.ltr),
           );
       }
     }
   }
   ```
