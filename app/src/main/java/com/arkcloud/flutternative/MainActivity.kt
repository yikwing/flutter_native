package com.arkcloud.flutternative

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.FrameLayout
import android.widget.Toast
import io.flutter.facade.Flutter
import io.flutter.plugin.common.EventChannel
import io.flutter.plugin.common.MethodChannel

class MainActivity : AppCompatActivity() {

    companion object {
        val GET_NAME_CHANNEL = "sample.flutter.io/get_name"
        val PULL_CHANNEL = "sample.flutter.io/pull"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val flutterView = Flutter.createView(this, lifecycle, "route1")
        addContentView(flutterView, FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT))


        //原生给 Flutter 传送数据
        EventChannel(flutterView, GET_NAME_CHANNEL).setStreamHandler(object : EventChannel.StreamHandler {
            override fun onListen(p0: Any?, events: EventChannel.EventSink?) {
                events?.success(getName())
            }

            override fun onCancel(p0: Any?) {

            }
        })

        //Flutter 调用 原生方法
        MethodChannel(flutterView, PULL_CHANNEL).setMethodCallHandler { methodCall, result ->
            run {
                if (methodCall.method == "refresh") {
                    val name = methodCall.argument<String>("name")
                    showtoast(name)
                    result.success("")
                } else {
                    result.notImplemented()
                }
            }
        }


    }

    private fun showtoast(s: String?) {
        Toast.makeText(this, "原生Toast: $s", Toast.LENGTH_SHORT).show()
    }


    fun getName(): String? = "flutter_library"
}
