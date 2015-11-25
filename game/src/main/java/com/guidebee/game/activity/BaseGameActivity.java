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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.FrameLayout;

import com.guidebee.game.ApplicationListener;
import com.guidebee.game.Audio;
import com.guidebee.game.Configuration;
import com.guidebee.game.Files;
import com.guidebee.game.Graphics;
import com.guidebee.game.LifecycleListener;
import com.guidebee.game.Net;
import com.guidebee.game.Preferences;
import com.guidebee.utils.Clipboard;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * An implementation of the {@link com.guidebee.game.Application}
 * interface for Android. Create an {@link Activity} that derives from this class. In
 * the {@link Activity#onCreate(Bundle)} method call the
 * {@link #initialize(ApplicationListener)} method specifying the
 * configuration for the GLSurfaceView.
 *
 * @author mzechner
 */
public class BaseGameActivity extends Activity implements BaseActivity {


    /**
     * Game activity wrapper.
     */
    protected final GameActivityWrapper wrapper=new GameActivityWrapper(this);

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. Uses a default {@link com.guidebee.game.Configuration}.
     *
     * @param listener the {@link com.guidebee.game.ApplicationListener} implementing the program logic *
     */
    public void initialize(ApplicationListener listener) {
        wrapper.initialize(listener);
    }

    /**
     * This method has to be called in the {@link Activity#onCreate(Bundle)} method.
     * It sets up all the things necessary to get
     * input, render via OpenGL and so on. You can configure other aspects of the
     * application with the rest of the fields in the
     * {@link com.guidebee.game.Configuration} instance.
     *
     * @param listener the {@link ApplicationListener} implementing the program logic
     * @param config   the {@link com.guidebee.game.Configuration}, defining various settings
     *                 of the application (use accelerometer,
     *                 etc.).
     */
    public void initialize(ApplicationListener listener, Configuration config) {
        wrapper.initialize(listener, config);
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
        return wrapper.initializeForView(listener);
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
        return wrapper.initializeForView(listener, config);
    }


    protected FrameLayout.LayoutParams createLayoutParams() {
        return wrapper.createLayoutParams();
    }

    protected void createWakeLock(boolean use) {
        wrapper.createWakeLock(use);
    }

    protected void hideStatusBar(boolean hide) {
        wrapper.hideStatusBar(hide);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        wrapper.onWindowFocusChanged(hasFocus);
    }

    @TargetApi(19)
    @Override
    public void useImmersiveMode(boolean use) {
        wrapper.useImmersiveMode(use);
    }

    @Override
    protected void onPause() {
        wrapper.onPause();

        super.onPause();
    }

    @Override
    protected void onResume() {
        wrapper.onResume();
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public ApplicationListener getApplicationListener() {
        return wrapper.getApplicationListener();
    }

    @Override
    public Audio getAudio() {
        return wrapper.getAudio();
    }

    @Override
    public Files getFiles() {
        return wrapper.getFiles();
    }

    @Override
    public Graphics getGraphics() {
        return wrapper.getGraphics();
    }

    @Override
    public com.guidebee.game.engine.platform.Input getInput() {
        return wrapper.getInput();
    }

    @Override
    public Net getNet() {
        return wrapper.getNet();
    }


    @Override
    public int getVersion() {
        return wrapper.getVersion();
    }

    @Override
    public long getJavaHeap() {
        return wrapper.getJavaHeap();
    }

    @Override
    public long getNativeHeap() {
        return wrapper.getNativeHeap();
    }

    @Override
    public Preferences getPreferences(String name) {
        return wrapper.getPreferences(name);
    }



    @Override
    public Clipboard getClipboard() {
        return wrapper.getClipboard();
    }

    @Override
    public void postRunnable(Runnable runnable) {
        wrapper.postRunnable(runnable);
    }

    @Override
    public void onConfigurationChanged(android.content.res.Configuration config) {
        super.onConfigurationChanged(config);
        wrapper.onConfigurationChanged(config);
    }

    @Override
    public void exit() {
       wrapper.exit();
    }

    @Override
    public void debug(String tag, String message) {
       wrapper.debug(tag,message);
    }

    @Override
    public void debug(String tag, String message, Throwable exception) {
        wrapper.debug(tag,message,exception);
    }

    @Override
    public void log(String tag, String message) {
        wrapper.log(tag, message);
    }

    @Override
    public void log(String tag, String message, Throwable exception) {
        wrapper.log(tag,message,exception);
    }

    @Override
    public void error(String tag, String message) {
        wrapper.error(tag, message);
    }

    @Override
    public void error(String tag, String message, Throwable exception) {
        wrapper.error(tag, message, exception);
    }

    @Override
    public void setLogLevel(int logLevel) {
        wrapper.setLogLevel(logLevel);
    }

    @Override
    public int getLogLevel() {
        return wrapper.getLogLevel();
    }

    @Override
    public void addLifecycleListener(LifecycleListener listener) {
        wrapper.addLifecycleListener(listener);
    }

    @Override
    public void removeLifecycleListener(LifecycleListener listener) {
        wrapper.removeLifecycleListener(listener);
    }

    @Override
    public Context getContext() {
        return wrapper.getContext();
    }

    @Override
    public Array<Runnable> getRunnables() {
        return wrapper.getRunnables();
    }

    @Override
    public Array<Runnable> getExecutedRunnables() {
        return wrapper.getExecutedRunnables();
    }

    @Override
    public Array<LifecycleListener> getLifecycleListeners() {
        return wrapper.getLifecycleListeners();
    }

    @Override
    public Window getApplicationWindow() {
        return wrapper.getApplicationWindow();
    }

    @Override
    public Handler getHandler() {
        return wrapper.getHandler();
    }
}
