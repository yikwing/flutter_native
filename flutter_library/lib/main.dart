import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

import 'dart:ui';

void main() => runApp(new MyApp(window.defaultRouteName));

class MyApp extends StatelessWidget {
  final String route;

  MyApp(this.route);

  // This widget is the root of your application.
  @override
  Widget build(BuildContext context) {
    switch (route) {
      case 'route1':
        return new MaterialApp(
          title: 'Flutter Demo',
          theme: new ThemeData(
            primarySwatch: Colors.teal,
          ),
          home: new MyHomePage(title: 'Flutter Demo Home Page'),
        );
      default:
        return MaterialApp(
          theme: new ThemeData(
            primarySwatch: Colors.teal,
          ),
          home: Scaffold(
            appBar: AppBar(
              title: Text('Error route'),
            ),
            body: Center(
              child: Text("Error" + route),
            ),
          ),
        );
    }
  }
}

class MyHomePage extends StatefulWidget {
  MyHomePage({Key key, this.title}) : super(key: key);

  final String title;

  @override
  _MyHomePageState createState() => new _MyHomePageState();
}

class _MyHomePageState extends State<MyHomePage> {
  //Flutter接收原生数据
  static const EventChannel eventChannel =
      EventChannel('sample.flutter.io/get_name');

  static const MethodChannel methodChannel =
      MethodChannel('sample.flutter.io/pull');

  String _name = 'unknown';

  @override
  void initState() {
    super.initState();

    //Flutter接收原生数据
    eventChannel.receiveBroadcastStream().listen(_onEvent, onError: _onError);
  }

  void _onEvent(Object event) {
    setState(() {
      _name = event.toString();
    });
  }

  void _onError(Object error) {
    setState(() {
      _name = 'Battery status: unknown';
    });
  }

  void _receiveData() {
    var invokeMethod = methodChannel.invokeMethod('refresh');
    print(invokeMethod);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text(widget.title),
      ),
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.spaceEvenly,
          children: <Widget>[
            Column(
              mainAxisAlignment: MainAxisAlignment.center,
              children: <Widget>[
                Text(
                  'Flutter',
                  key: const Key('Battery level label'),
                ),
                Padding(
                  padding: const EdgeInsets.all(16.0),
                  child: RaisedButton(
                    onPressed: _receiveData,
                    child: const Text('Refresh'),
                  ),
                )
              ],
            ),
            Text('原生push' + _name),
          ],
        ),
      ),
    );
  }
}
