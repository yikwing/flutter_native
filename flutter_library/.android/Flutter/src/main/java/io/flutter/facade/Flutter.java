package io.flutter.facade;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.StringCodec;
import io.flutter.view.FlutterMain;
import io.flutter.view.FlutterNativeView;
import io.flutter.view.FlutterRunArguments;
import io.flutter.view.FlutterView;
import io.flutter.plugins.GeneratedPluginRegistrant;

/**
 * Main entry point for using Flutter in Android applications.
 *
 * <p><strong>Warning:</strong> This file is auto-generated by Flutter tooling.
 * DO NOT EDIT.</p>
 */
public final class Flutter {
  private Flutter() {
    // to prevent instantiation
  }

  /**
   * Initiates the Dart VM. Calling this method at an early point may help decreasing time to first
   * frame for a subsequently created {@link FlutterView}.
   *
   * @param applicationContext the application's {@link Context}
   */
  public static void startInitialization(@NonNull Context applicationContext) {
    FlutterMain.startInitialization(applicationContext);
  }

  /**
   * Creates a {@link FlutterFragment} managing a {@link FlutterView}. The optional
   * initial route string will be made available to the Dart code
   * (via {@code window.defaultRouteName}) and may be used to determine which widget
   * should be displayed in the view. The default initialRoute is "/".
   *
   * @param initialRoute an initial route {@link String}, or null
   * @return a {@link FlutterFragment}
   */
  @NonNull
  public static FlutterFragment createFragment(String initialRoute) {
    final FlutterFragment fragment = new FlutterFragment();
    final Bundle args = new Bundle();
    args.putString(FlutterFragment.ARG_ROUTE, initialRoute);
    fragment.setArguments(args);
    return fragment;
  }

  /**
   * Creates a {@link FlutterView} linked to the specified {@link Activity} and {@link Lifecycle}.
   * The optional initial route string will be made available to the Dart code (via
   * {@code window.defaultRouteName}) and may be used to determine which widget should be displayed
   * in the view. The default initialRoute is "/".
   *
   * @param activity an {@link Activity}
   * @param lifecycle a {@link Lifecycle}
   * @param initialRoute an initial route {@link String}, or null
   * @return a {@link FlutterView}
   */
  @NonNull
  public static FlutterView createView(@NonNull final Activity activity, @NonNull final Lifecycle lifecycle, final String initialRoute) {
    FlutterMain.startInitialization(activity.getApplicationContext());
    FlutterMain.ensureInitializationComplete(activity.getApplicationContext(), null);
    final FlutterNativeView nativeView = new FlutterNativeView(activity);
    final FlutterView flutterView = new FlutterView(activity, null, nativeView) {
      private final BasicMessageChannel<String> lifecycleMessages = new BasicMessageChannel<String>(this, "flutter/lifecycle", StringCodec.INSTANCE);
      @Override
      public void onFirstFrame() {
        super.onFirstFrame();
        setAlpha(1.0f);
      }

      @Override
      public void onPostResume() {
        // Overriding default behavior to avoid dictating system UI via PlatformPlugin.
        lifecycleMessages.send("AppLifecycleState.resumed");
      }
    };
    if (initialRoute != null) {
      flutterView.setInitialRoute(initialRoute);
    }
    lifecycle.addObserver(new LifecycleObserver() {
      @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
      public void onCreate() {
        final FlutterRunArguments arguments = new FlutterRunArguments();
        arguments.bundlePath = FlutterMain.findAppBundlePath(activity.getApplicationContext());
        arguments.entrypoint = "main";
        flutterView.runFromBundle(arguments);
        GeneratedPluginRegistrant.registerWith(flutterView.getPluginRegistry());
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_START)
      public void onStart() {
        flutterView.onStart();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
      public void onResume() {
        flutterView.onPostResume();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
      public void onPause() {
        flutterView.onPause();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
      public void onStop() {
        flutterView.onStop();
      }

      @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
      public void onDestroy() {
        flutterView.destroy();
      }
    });
    flutterView.setAlpha(0.0f);
    return flutterView;
  }
}
