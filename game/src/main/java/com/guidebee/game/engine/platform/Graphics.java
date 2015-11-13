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
package com.guidebee.game.engine.platform;

//--------------------------------- IMPORTS ------------------------------------

import android.opengl.GLSurfaceView;
import android.opengl.GLSurfaceView.EGLConfigChooser;
import android.opengl.GLSurfaceView.Renderer;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import com.guidebee.game.Configuration;
import com.guidebee.game.GameEngine;
import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.LifecycleListener;
import com.guidebee.game.activity.BaseActivity;
import com.guidebee.game.engine.graphics.Mesh;
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.game.engine.graphics.opengles.IGL30;
import com.guidebee.game.engine.graphics.opengles.ShaderProgram;
import com.guidebee.game.engine.platform.surfaceview.*;
import com.guidebee.game.graphics.FrameBuffer;
import com.guidebee.game.graphics.Texture;
import com.guidebee.math.WindowedMean;
import com.guidebee.utils.collections.Array;

import javax.microedition.khronos.egl.EGL10;
import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.egl.EGLContext;
import javax.microedition.khronos.egl.EGLDisplay;
import javax.microedition.khronos.opengles.GL10;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * An implementation of {@link com.guidebee.game.Graphics} for Android.
 *
 * @author mzechner
 */
public class Graphics implements com.guidebee.game.Graphics, Renderer {

    private static final String LOG_TAG = "Graphics";

    public final View view;
    int width;
    int height;
    BaseActivity app;
    IGL20 gl20;
    IGL30 gl30;
    EGLContext eglContext;
    String extensions;

    protected long lastFrameTime = System.nanoTime();
    protected float deltaTime = 0;
    protected long frameStart = System.nanoTime();
    protected int frames = 0;
    protected int fps;
    protected WindowedMean mean = new WindowedMean(5);

    volatile boolean created = false;
    volatile boolean running = false;
    volatile boolean pause = false;
    volatile boolean resume = false;
    volatile boolean destroy = false;

    private float ppiX = 0;
    private float ppiY = 0;
    private float ppcX = 0;
    private float ppcY = 0;
    private float density = 1;

    protected final Configuration config;
    private BufferFormat bufferFormat = new BufferFormat(5, 6, 5, 0, 16, 0, 0, false);
    private boolean isContinuous = true;

    public Graphics(BaseActivity application, Configuration config,
                    ResolutionStrategy resolutionStrategy) {
        this(application, config, resolutionStrategy, true);
    }

    public Graphics(BaseActivity application, Configuration config,
                    ResolutionStrategy resolutionStrategy, boolean focusableView) {
        this.config = config;
        this.app = application;
        view = createGLSurfaceView(application, resolutionStrategy);
        preserveEGLContextOnPause();
        if (focusableView) {
            view.setFocusable(true);
            view.setFocusableInTouchMode(true);
        }
    }

    protected void preserveEGLContextOnPause() {
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if ((sdkVersion >= 11 && view instanceof GLSurfaceView20)
                || view instanceof GLSurfaceView20API18) {
            try {
                view.getClass().getMethod("setPreserveEGLContextOnPause",
                        boolean.class).invoke(view, true);
            } catch (Exception e) {
                GameEngine.app.log(LOG_TAG,
                        "Method GLSurfaceView.setPreserveEGLContextOnPause not found");
            }
        }
    }

    protected View createGLSurfaceView(BaseActivity application,
                                       final ResolutionStrategy resolutionStrategy) {
        if (!checkGL20()) throw new GameEngineRuntimeException("GameEngine requires OpenGL ES 2.0");

        EGLConfigChooser configChooser = getEglConfigChooser();
        int sdkVersion = android.os.Build.VERSION.SDK_INT;
        if (sdkVersion <= 10 && config.useGLSurfaceView20API18) {
            GLSurfaceView20API18 view = new GLSurfaceView20API18(application.getContext(),
                    resolutionStrategy);
            if (configChooser != null)
                view.setEGLConfigChooser(configChooser);
            else
                view.setEGLConfigChooser(config.r, config.g, config.b, config.a,
                        config.depth, config.stencil);
            view.setRenderer(this);
            return view;
        } else {
            GLSurfaceView20 view = new GLSurfaceView20(application.getContext(),
                    resolutionStrategy);
            if (configChooser != null)
                view.setEGLConfigChooser(configChooser);
            else
                view.setEGLConfigChooser(config.r, config.g, config.b, config.a,
                        config.depth, config.stencil);
            view.setRenderer(this);
            return view;
        }
    }

    public void onPauseGLSurfaceView() {
        if (view != null) {
            if (view instanceof GLSurfaceViewAPI18)
                ((GLSurfaceViewAPI18) view).onPause();
            if (view instanceof GLSurfaceView)
                ((GLSurfaceView) view).onPause();
        }
    }

    public void onResumeGLSurfaceView() {
        if (view != null) {
            if (view instanceof GLSurfaceViewAPI18)
                ((GLSurfaceViewAPI18) view).onResume();
            if (view instanceof GLSurfaceView)
                ((GLSurfaceView) view).onResume();
        }
    }

    protected EGLConfigChooser getEglConfigChooser() {
        return new EglConfigChooser(config.r, config.g, config.b,
                config.a, config.depth, config.stencil, config.numSamples);
    }

    private void updatePpi() {
        DisplayMetrics metrics = new DisplayMetrics();
        app.getWindowManager().getDefaultDisplay().getMetrics(metrics);

        ppiX = metrics.xdpi;
        ppiY = metrics.ydpi;
        ppcX = metrics.xdpi / 2.54f;
        ppcY = metrics.ydpi / 2.54f;
        density = metrics.density;
    }

    protected boolean checkGL20() {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);

        int[] version = new int[2];
        egl.eglInitialize(display, version);

        int EGL_OPENGL_ES2_BIT = 4;
        int[] configAttribs = {EGL10.EGL_RED_SIZE, 4, EGL10.EGL_GREEN_SIZE, 4,
                EGL10.EGL_BLUE_SIZE, 4, EGL10.EGL_RENDERABLE_TYPE,
                EGL_OPENGL_ES2_BIT, EGL10.EGL_NONE};

        EGLConfig[] configs = new EGLConfig[10];
        int[] num_config = new int[1];
        egl.eglChooseConfig(display, configAttribs, configs, 10, num_config);
        egl.eglTerminate(display);
        return num_config[0] > 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public IGL20 getGL20() {
        return gl20;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getHeight() {
        return height;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int getWidth() {
        return width;
    }

    /**
     * This instantiates the GL10, GL11 and IGL20 instances. Includes the
     * check for certain devices that pretend to support GL11 but
     * fuck up vertex buffer objects. This includes the pixelflinger which
     * segfaults when buffers are deleted as well as the
     * Motorola CLIQ and the Samsung Behold II.
     *
     * @param gl
     */
    private void setupGL(javax.microedition.khronos.opengles.GL10 gl) {
        if (gl20 != null) return;

        gl20 = new GL20();

        GameEngine.gl = gl20;
        GameEngine.gl20 = gl20;

        GameEngine.app.log(LOG_TAG, "OGL renderer: "
                + gl.glGetString(GL10.GL_RENDERER));
        GameEngine.app.log(LOG_TAG, "OGL vendor: "
                + gl.glGetString(GL10.GL_VENDOR));
        GameEngine.app.log(LOG_TAG, "OGL version: "
                + gl.glGetString(GL10.GL_VERSION));
        GameEngine.app.log(LOG_TAG, "OGL extensions: "
                + gl.glGetString(GL10.GL_EXTENSIONS));
    }

    @Override
    public void onSurfaceChanged(javax.microedition.khronos.opengles.GL10 gl,
                                 int width, int height) {
        this.width = width;
        this.height = height;
        updatePpi();
        gl.glViewport(0, 0, this.width, this.height);
        if (created == false) {
            app.getApplicationListener().create();
            created = true;
            synchronized (this) {
                running = true;
            }
        }
        app.getApplicationListener().resize(width, height);
    }

    @Override
    public void onSurfaceCreated(javax.microedition.khronos.opengles.GL10 gl,
                                 EGLConfig config) {
        eglContext = ((EGL10) EGLContext.getEGL()).eglGetCurrentContext();
        setupGL(gl);
        logConfig(config);
        updatePpi();

        Mesh.invalidateAllMeshes(app);
        Texture.invalidateAllTextures(app);
        ShaderProgram.invalidateAllShaderPrograms(app);
        FrameBuffer.invalidateAllFrameBuffers(app);

        logManagedCachesStatus();

        Display display = app.getWindowManager().getDefaultDisplay();
        this.width = display.getWidth();
        this.height = display.getHeight();
        this.mean = new WindowedMean(5);
        this.lastFrameTime = System.nanoTime();

        gl.glViewport(0, 0, this.width, this.height);
    }

    private void logConfig(EGLConfig config) {
        EGL10 egl = (EGL10) EGLContext.getEGL();
        EGLDisplay display = egl.eglGetDisplay(EGL10.EGL_DEFAULT_DISPLAY);
        int r = getAttrib(egl, display, config, EGL10.EGL_RED_SIZE, 0);
        int g = getAttrib(egl, display, config, EGL10.EGL_GREEN_SIZE, 0);
        int b = getAttrib(egl, display, config, EGL10.EGL_BLUE_SIZE, 0);
        int a = getAttrib(egl, display, config, EGL10.EGL_ALPHA_SIZE, 0);
        int d = getAttrib(egl, display, config, EGL10.EGL_DEPTH_SIZE, 0);
        int s = getAttrib(egl, display, config, EGL10.EGL_STENCIL_SIZE, 0);
        int samples = Math.max(getAttrib(egl, display, config,
                        EGL10.EGL_SAMPLES, 0),
                getAttrib(egl, display, config,
                        EglConfigChooser.EGL_COVERAGE_SAMPLES_NV, 0));
        boolean coverageSample = getAttrib(egl, display, config,
                EglConfigChooser.EGL_COVERAGE_SAMPLES_NV, 0) != 0;

        GameEngine.app.log(LOG_TAG, "framebuffer: (" + r + ", "
                + g + ", " + b + ", " + a + ")");
        GameEngine.app.log(LOG_TAG, "depthbuffer: (" + d + ")");
        GameEngine.app.log(LOG_TAG, "stencilbuffer: (" + s + ")");
        GameEngine.app.log(LOG_TAG, "samples: (" + samples + ")");
        GameEngine.app.log(LOG_TAG, "coverage sampling: ("
                + coverageSample + ")");

        bufferFormat = new BufferFormat(r, g, b, a, d, s,
                samples, coverageSample);
    }

    int[] value = new int[1];

    private int getAttrib(EGL10 egl, EGLDisplay display,
                          EGLConfig config, int attrib, int defValue) {
        if (egl.eglGetConfigAttrib(display, config, attrib, value)) {
            return value[0];
        }
        return defValue;
    }

    Object synch = new Object();

    public void resume() {
        synchronized (synch) {
            running = true;
            resume = true;
        }
    }

    public void pause() {
        synchronized (synch) {
            if (!running) return;
            running = false;
            pause = true;
            while (pause) {
                try {
                    // TODO: fix deadlock race condition with quick resume/pause.
                    // Temporary workaround:
                    // Android ANR time is 5 seconds, so wait up to 4 seconds before assuming
                    // deadlock and killing process. This can easily be triggered by opening the
                    // Recent Apps list and then double-tapping the Recent Apps button with
                    // ~500ms between taps.
                    synch.wait(4000);
                    if (pause) {
                        GameEngine.app.error(LOG_TAG, "waiting for pause synchronization took too "
                                + "long; assuming deadlock and killing");
                        android.os.Process.killProcess(android.os.Process.myPid());
                    }
                } catch (InterruptedException ignored) {
                    GameEngine.app.log(LOG_TAG, "waiting for pause synchronization failed!");
                }
            }
        }
    }

    public void destroy() {
        synchronized (synch) {
            running = false;
            destroy = true;

            while (destroy) {
                try {
                    synch.wait();
                } catch (InterruptedException ex) {
                    GameEngine.app.log(LOG_TAG, "waiting for destroy synchronization failed!");
                }
            }
        }
    }

    @Override
    public void onDrawFrame(javax.microedition.khronos.opengles.GL10 gl) {
        long time = System.nanoTime();
        deltaTime = (time - lastFrameTime) / 1000000000.0f;
        lastFrameTime = time;

        // After pause deltaTime can have somewhat huge value that
        // destabilizes the mean, so let's cut it off
        if (!resume) {
            mean.addValue(deltaTime);
        } else {
            deltaTime = 0;
        }

        boolean lrunning = false;
        boolean lpause = false;
        boolean ldestroy = false;
        boolean lresume = false;

        synchronized (synch) {
            lrunning = running;
            lpause = pause;
            ldestroy = destroy;
            lresume = resume;

            if (resume) {
                resume = false;
            }

            if (pause) {
                pause = false;
                synch.notifyAll();
            }

            if (destroy) {
                destroy = false;
                synch.notifyAll();
            }
        }

        if (lresume) {
            Array<LifecycleListener> listeners = app.getLifecycleListeners();
            synchronized (listeners) {
                for (LifecycleListener listener : listeners) {
                    listener.resume();
                }
            }
            app.getApplicationListener().resume();
            GameEngine.app.log(LOG_TAG, "resumed");
        }

        if (lrunning) {
            synchronized (app.getRunnables()) {
                app.getExecutedRunnables().clear();
                app.getExecutedRunnables().addAll(app.getRunnables());
                app.getRunnables().clear();
            }

            for (int i = 0; i < app.getExecutedRunnables().size; i++) {
                try {
                    app.getExecutedRunnables().get(i).run();
                } catch (Throwable t) {
                    t.printStackTrace();
                }
            }
            app.getInput().processEvents();
            app.getApplicationListener().render();
        }

        if (lpause) {
            Array<LifecycleListener> listeners = app.getLifecycleListeners();
            synchronized (listeners) {
                for (LifecycleListener listener : listeners) {
                    listener.pause();
                }
            }
            app.getApplicationListener().pause();
            GameEngine.app.log(LOG_TAG, "paused");
        }

        if (ldestroy) {
            Array<LifecycleListener> listeners = app.getLifecycleListeners();
            synchronized (listeners) {
                for (LifecycleListener listener : listeners) {
                    listener.dispose();
                }
            }
            app.getApplicationListener().dispose();
            GameEngine.app.log(LOG_TAG, "destroyed");
        }

        if (time - frameStart > 1000000000) {
            fps = frames;
            frames = 0;
            frameStart = time;
        }
        frames++;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public float getDeltaTime() {
        return mean.getMean() == 0 ? deltaTime : mean.getMean();
    }

    @Override
    public float getRawDeltaTime() {
        return deltaTime;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public int getFramesPerSecond() {
        return fps;
    }

    public void clearManagedCaches() {
        Mesh.clearAllMeshes(app);
        Texture.clearAllTextures(app);
        ShaderProgram.clearAllShaderPrograms(app);
        FrameBuffer.clearAllFrameBuffers(app);

        logManagedCachesStatus();
    }

    protected void logManagedCachesStatus() {
        GameEngine.app.log(LOG_TAG, Mesh.getManagedStatus());
        GameEngine.app.log(LOG_TAG, Texture.getManagedStatus());
        GameEngine.app.log(LOG_TAG, ShaderProgram.getManagedStatus());
        GameEngine.app.log(LOG_TAG, FrameBuffer.getManagedStatus());
    }

    public View getView() {
        return view;
    }

    @Override
    public float getPpiX() {
        return ppiX;
    }

    @Override
    public float getPpiY() {
        return ppiY;
    }

    @Override
    public float getPpcX() {
        return ppcX;
    }

    @Override
    public float getPpcY() {
        return ppcY;
    }

    @Override
    public float getDensity() {
        return density;
    }

    @Override
    public boolean supportsDisplayModeChange() {
        return false;
    }

    @Override
    public boolean setDisplayMode(DisplayMode displayMode) {
        return false;
    }

    @Override
    public DisplayMode[] getDisplayModes() {
        return new DisplayMode[]{getDesktopDisplayMode()};
    }

    @Override
    public boolean setDisplayMode(int width, int height, boolean fullscreen) {
        return false;
    }

    @Override
    public void setTitle(String title) {

    }

    private class AndroidDisplayMode extends DisplayMode {
        protected AndroidDisplayMode(int width, int height,
                                     int refreshRate, int bitsPerPixel) {
            super(width, height, refreshRate, bitsPerPixel);
        }
    }

    @Override
    public DisplayMode getDesktopDisplayMode() {
        DisplayMetrics metrics = new DisplayMetrics();
        app.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new AndroidDisplayMode(metrics.widthPixels,
                metrics.heightPixels, 0, 0);
    }

    @Override
    public BufferFormat getBufferFormat() {
        return bufferFormat;
    }

    @Override
    public void setVSync(boolean vsync) {
    }

    @Override
    public boolean supportsExtension(String extension) {
        if (extensions == null) extensions
                = GameEngine.gl.glGetString(GL10.GL_EXTENSIONS);
        return extensions.contains(extension);
    }

    @Override
    public void setContinuousRendering(boolean isContinuous) {
        if (view != null) {
            this.isContinuous = isContinuous;
            int renderMode = isContinuous
                    ? GLSurfaceView.RENDERMODE_CONTINUOUSLY
                    : GLSurfaceView.RENDERMODE_WHEN_DIRTY;
            if (view instanceof GLSurfaceViewAPI18)
                ((GLSurfaceViewAPI18) view).setRenderMode(renderMode);
            if (view instanceof GLSurfaceView)
                ((GLSurfaceView) view).setRenderMode(renderMode);
            mean.clear();
        }
    }

    @Override
    public boolean isContinuousRendering() {
        return isContinuous;
    }

    @Override
    public void requestRendering() {
        if (view != null) {
            if (view instanceof GLSurfaceViewAPI18)
                ((GLSurfaceViewAPI18) view).requestRender();
            if (view instanceof GLSurfaceView)
                ((GLSurfaceView) view).requestRender();
        }
    }

    @Override
    public boolean isFullscreen() {
        return true;
    }

    @Override
    public void clearScreen(float red, float green, float blue, float alpha) {
        gl20.glClearColor(red, green, blue, alpha);
        gl20.glClear(IGL20.GL_COLOR_BUFFER_BIT);
    }

    @Override
    public boolean isGL30Available() {
        return gl30 != null;
    }

    @Override
    public IGL30 getGL30() {
        return gl30;
    }
}
