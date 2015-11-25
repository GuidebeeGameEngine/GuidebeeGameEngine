/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 ******************************************************************************/
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.activity;

//--------------------------------- IMPORTS ------------------------------------

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Debug;
import android.os.Handler;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

import com.guidebee.game.ApplicationListener;
import com.guidebee.game.Configuration;
import com.guidebee.game.GameEngine;
import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.LifecycleListener;
import com.guidebee.game.ResourceManager;
import com.guidebee.game.engine.platform.InputFactory;
import com.guidebee.game.engine.platform.surfaceview.FillResolutionStrategy;
import com.guidebee.game.engine.utils.GameEngineiNativesLoader;
import com.guidebee.utils.Clipboard;
import com.guidebee.utils.collections.Array;

import java.lang.reflect.Method;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * An implementation of the {@link com.guidebee.game.Application}
 * interface for Android.This class can wrapper any android Activity. In
 * the {@link Activity#onCreate(Bundle)} method call the
 * {@link #initialize(ApplicationListener)} method specifying the
 * configuration for the GLSurfaceView.
 *
 * @author mzechner
 */
public class GameActivityWrapper implements BaseActivity {
    static {
        GameEngineiNativesLoader.load();
    }

    protected Activity activity;

    protected com.guidebee.game.engine.platform.Graphics graphics;
    protected com.guidebee.game.engine.platform.Input input;
    protected com.guidebee.game.engine.platform.Audio audio;
    protected com.guidebee.game.engine.platform.Files files;
    protected com.guidebee.game.engine.platform.Net net;
    protected ApplicationListener listener;
    public Handler handler;
    protected boolean firstResume = true;
    protected final Array<Runnable> runnables = new Array<Runnable>();
    protected final Array<Runnable> executedRunnables = new Array<Runnable>();
    protected final Array<LifecycleListener> lifecycleListeners
            = new Array<LifecycleListener>();
    protected int logLevel = LOG_INFO;
    protected boolean useImmersiveMode = false;
    protected boolean hideStatusBar = false;
    private int wasFocusChanged = -1;
    private boolean isWaitingForAudio = false;


    public GameActivityWrapper(Activity activity) {
        this.activity = activity;
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. Uses a default {@link Configuration}.
     *
     * @param listener the {@link ApplicationListener} implementing the program logic *
     */
    public void initialize(ApplicationListener listener) {
        Configuration config = new Configuration();
        initialize(listener, config);
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. You can configure other aspects of the
     * application with the rest of the fields in the
     * {@link Configuration} instance.
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @param config   the {@link Configuration}, defining various settings
     *                 of the application (use accelerometer,
     *                 etc.).
     */
    public void initialize(ApplicationListener listener, Configuration config) {
        init(listener, config, false);
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. Uses a default {@link Configuration}.
     * <p/>
     * Note: you have to add the returned view to your layout!
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @return the GLSurfaceView of the application
     */
    public View initializeForView(ApplicationListener listener) {
        Configuration config = new Configuration();
        return initializeForView(listener, config);
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. You can configure other aspects of the
     * application with the rest of the fields in the
     * {@link Configuration} instance.
     * <p/>
     * Note: you have to add the returned view to your layout!
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @param config   the {@link Configuration}, defining various settings of
     *                 the application (use accelerometer,
     *                 etc.).
     * @return the GLSurfaceView of the application
     */
    public View initializeForView(ApplicationListener listener, Configuration config) {
        init(listener, config, true);
        return graphics.getView();
    }

    private void init(ApplicationListener listener, Configuration config,
                      boolean isForView) {
        if (this.getVersion() < MINIMUM_SDK) {
            throw new GameEngineRuntimeException("GameEngine requires Android API Level "
                    + MINIMUM_SDK + " or later.");
        }
        graphics = new com.guidebee.game.engine.platform.Graphics(this, config,
                config.resolutionStrategy == null ? new FillResolutionStrategy()
                        : config.resolutionStrategy);
        input = InputFactory.newAndroidInput(this, activity, graphics.view, config);
        audio = new com.guidebee.game.engine.platform.Audio(activity, config);
        activity.getFilesDir(); // workaround for Android bug #10515463
        files = new com.guidebee.game.engine.platform.Files(activity.getAssets(), activity.getFilesDir().getAbsolutePath());
        net = new com.guidebee.game.engine.platform.Net(this);
        this.listener = listener;
        this.handler = new Handler();
        this.useImmersiveMode = config.useImmersiveMode;
        this.hideStatusBar = config.hideStatusBar;

        // Add a specialized audio lifecycle listener
        addLifecycleListener(new LifecycleListener() {

            @Override
            public void resume() {
                // No need to resume audio here
            }

            @Override
            public void pause() {
                audio.pause();
            }

            @Override
            public void dispose() {
                audio.dispose();
            }
        });

        GameEngine.app = this;
        GameEngine.input = this.getInput();
        GameEngine.audio = this.getAudio();
        GameEngine.files = this.getFiles();
        GameEngine.graphics = this.getGraphics();
        GameEngine.net = this.getNet();
        GameEngine.assetManager = new ResourceManager();
        GameEngine.pixelToBox2DUnit = config.pixelToBox2DUnit;

        if (!isForView) {
            try {
                activity.requestWindowFeature(Window.FEATURE_NO_TITLE);
            } catch (Exception ex) {
                log("Application", "Content already displayed, cannot request FEATURE_NO_TITLE", ex);
            }
            activity.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
            activity.setContentView(graphics.getView(), createLayoutParams());
        }

        createWakeLock(config.useWakelock);
        hideStatusBar(this.hideStatusBar);
        useImmersiveMode(this.useImmersiveMode);
        if (this.useImmersiveMode && getVersion() >= 19) {
            try {
                Class<?> vlistener = Class.forName("com.guidebee.game.engine.platform.VisibilityListener");
                Object o = vlistener.newInstance();
                Method method = vlistener.getDeclaredMethod("createListener", BaseActivity.class);
                method.invoke(o, this);
            } catch (Exception e) {
                log("Application", "Failed to create VisibilityListener", e);
            }
        }
    }

    protected FrameLayout.LayoutParams createLayoutParams() {
        FrameLayout.LayoutParams layoutParams
                = new FrameLayout.LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                android.view.ViewGroup.LayoutParams.MATCH_PARENT);
        layoutParams.gravity = Gravity.CENTER;
        return layoutParams;
    }

    protected void createWakeLock(boolean use) {
        if (use) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    protected void hideStatusBar(boolean hide) {
        if (!hide || getVersion() < 11) return;

        View rootView = activity.getWindow().getDecorView();

        try {
            Method m = View.class.getMethod("setSystemUiVisibility", int.class);
            if (getVersion() <= 13)
                m.invoke(rootView, 0x0);
            m.invoke(rootView, 0x1);
        } catch (Exception e) {
            log("Application", "Can't hide status bar", e);
        }
    }


    @TargetApi(19)
    @Override
    public void useImmersiveMode(boolean use) {
        if (!use || getVersion() < 19) return;

        View view = activity.getWindow().getDecorView();
        try {
            Method m = View.class.getMethod("setSystemUiVisibility", int.class);
            int code = View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            code ^= View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION;
            code ^= View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
            code ^= View.SYSTEM_UI_FLAG_FULLSCREEN;
            code ^= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
            code ^= View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            m.invoke(view, code);
        } catch (Exception e) {
            log("Application", "Can't set immersive mode", e);
        }
    }


    @Override
    public ApplicationListener getApplicationListener() {
        return listener;
    }

    @Override
    public com.guidebee.game.Audio getAudio() {
        return audio;
    }

    @Override
    public com.guidebee.game.Files getFiles() {
        return files;
    }

    @Override
    public com.guidebee.game.Graphics getGraphics() {
        return graphics;
    }

    @Override
    public com.guidebee.game.engine.platform.Input getInput() {
        return input;
    }

    @Override
    public com.guidebee.game.Net getNet() {
        return net;
    }



    @Override
    public int getVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    @Override
    public long getJavaHeap() {
        return Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
    }

    @Override
    public long getNativeHeap() {
        return Debug.getNativeHeapAllocatedSize();
    }

    @Override
    public com.guidebee.game.Preferences getPreferences(String name) {
        return new com.guidebee.game.engine.platform.Preferences(activity.getSharedPreferences(name, Context.MODE_PRIVATE));
    }

    com.guidebee.game.engine.platform.Clipboard clipboard;

    @Override
    public Clipboard getClipboard() {
        if (clipboard == null) {
            clipboard = new com.guidebee.game.engine.platform.Clipboard(activity);
        }
        return clipboard;
    }

    @Override
    public void postRunnable(Runnable runnable) {
        synchronized (runnables) {
            runnables.add(runnable);
            GameEngine.graphics.requestRendering();
        }
    }


    @Override
    public void exit() {
        handler.post(new Runnable() {
            @Override
            public void run() {
                activity.finish();
            }
        });
    }

    @Override
    public void debug(String tag, String message) {
        if (logLevel >= LOG_DEBUG) {
            Log.d(tag, message);
        }
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_DEBUG) {
            Log.d(tag, message, exception);
        }
    }

    @Override
    public void log(String tag, String message) {
        if (logLevel >= LOG_INFO) Log.i(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_INFO) Log.i(tag, message, exception);
    }

    @Override
    public void error(String tag, String message) {
        if (logLevel >= LOG_ERROR) Log.e(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        if (logLevel >= LOG_ERROR) Log.e(tag, message, exception);
    }

    @Override
    public void setLogLevel(int logLevel) {
        this.logLevel = logLevel;
    }

    @Override
    public int getLogLevel() {
        return logLevel;
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.add(listener);
        }
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        synchronized (lifecycleListeners) {
            lifecycleListeners.removeValue(listener, true);
        }
    }

    @Override
    public Context getContext() {
        return activity;
    }

    @Override
    public Array<Runnable> getRunnables() {
        return runnables;
    }

    @Override
    public Array<Runnable> getExecutedRunnables() {
        return executedRunnables;
    }

    @Override
    public void runOnUiThread(Runnable runnable) {
        activity.runOnUiThread(runnable);
    }

    @Override
    public void startActivity(Intent intent) {
        activity.startActivity(intent);
    }

    @Override
    public Array<LifecycleListener> getLifecycleListeners() {
        return lifecycleListeners;
    }

    @Override
    public Window getApplicationWindow() {
        return activity.getWindow();
    }

    @Override
    public WindowManager getWindowManager() {
        return activity.getWindowManager();
    }

    @Override
    public Handler getHandler() {
        return this.handler;
    }

    //this need to be override by wrapped activity


    public void onConfigurationChanged(android.content.res.Configuration config) {
        boolean keyboardAvailable = false;
        if (config.hardKeyboardHidden
                == android.content.res.Configuration.HARDKEYBOARDHIDDEN_NO)
            keyboardAvailable = true;
        input.keyboardAvailable = keyboardAvailable;
    }


    protected void onPause() {
        boolean isContinuous = graphics.isContinuousRendering();
        graphics.setContinuousRendering(true);
        graphics.pause();

        input.onPause();

        if (activity.isFinishing()) {
            graphics.clearManagedCaches();
            graphics.destroy();
        }
        graphics.setContinuousRendering(isContinuous);

        graphics.onPauseGLSurfaceView();


    }


    protected void onResume() {
        GameEngine.app = this;
        GameEngine.input = this.getInput();
        GameEngine.audio = this.getAudio();
        GameEngine.files = this.getFiles();
        GameEngine.graphics = this.getGraphics();
        GameEngine.net = this.getNet();
        input.onResume();
        if (graphics != null) {
            graphics.onResumeGLSurfaceView();
        }
        if (!firstResume) {
            graphics.resume();
        } else
            firstResume = false;

        this.isWaitingForAudio = true;
        if (this.wasFocusChanged == 1 || this.wasFocusChanged == -1) {
            this.audio.resume();
            this.isWaitingForAudio = false;
        }

    }


    public void onWindowFocusChanged(boolean hasFocus) {

        useImmersiveMode(this.useImmersiveMode);
        hideStatusBar(this.hideStatusBar);
        if (hasFocus) {
            this.wasFocusChanged = 1;
            if (this.isWaitingForAudio) {
                this.audio.resume();
                this.isWaitingForAudio = false;
            }
        } else {
            this.wasFocusChanged = 0;
        }
    }

}
