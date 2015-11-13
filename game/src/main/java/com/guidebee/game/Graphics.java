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
package com.guidebee.game;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.game.engine.graphics.opengles.IGL30;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * This interface encapsulates communication with the graphics processor.
 * Depending on the available hardware and the current {@link Application}
 * configuration, access to IGL20 and IGL30 are provided here.
 * <p/>
 * If supported by the backend, this interface lets you query the available
 * display modes (graphics resolution and color depth) and change it.
 * <p/>
 * This interface can be used to switch between continuous and non-continuous
 * rendering (see {@link #setContinuousRendering(boolean)}), and to explicitly
 * {@link #requestRendering()}.
 * <p/>
 *
 * @author mzechner
 */
public interface Graphics {

    /**
     * Returns whether OpenGL ES 3.0 is available. If it is you can get an
     * instance of IGL30  to
     * access OpenGL ES 3.0 functionality. Note that this functionality will
     * only be available if you instructed the
     * {@link Application} instance to use OpenGL ES 3.0!
     *
     * @return whether OpenGL ES 3.0 is available
     */
    boolean isGL30Available();

    /**
     * Get  the IGL20 instance
     * @return the IGL20 instance
     */
    IGL20 getGL20();

    /**
     * get the IGL30 instance or null if not supported
     * @return the IGL30 instance or null if not supported
     */
    IGL30 getGL30();

    /**
     * get the width in pixels of the display surface
     * @return the width in pixels of the display surface
     */
    int getWidth();

    /**
     * get the height in pixels of the display surface
     * @return the height in pixels of the display surface
     */
    int getHeight();

    /**
     * get the time span between the current frame and the last
     * frame in seconds.
     * @return the time span between the current frame and the last
     * frame in seconds. Might be smoothed over n frames.
     */
    float getDeltaTime();

    /**
     * get the time span between the current frame and the last
     * frame in seconds, without smoothing
     * @return the time span between the current frame and the last
     * frame in seconds, without smoothing.
     */
    float getRawDeltaTime();

    /**
     * get the average number of frames per second.
     * @return the average number of frames per second
     */
    int getFramesPerSecond();


    /**
     * get the pixels per inch on the x-axis.
     * @return the pixels per inch on the x-axis
     */
    float getPpiX();

    /**
     * get the pixels per inch on the y-axis
     * @return the pixels per inch on the y-axis
     */
    float getPpiY();

    /**
     * get the pixels per centimeter on the x-axis.
     * @return the pixels per centimeter on the x-axis
     */
    float getPpcX();

    /**
     * get the pixels per centimeter on the y-axis.
     * @return the pixels per centimeter on the y-axis.
     */
    float getPpcY();

    /**
     * This is a scaling factor for the Density Independent Pixel unit,
     * following the same conventions as android.util.DisplayMetrics#density,
     * where one DIP is one pixel on an approximately 160 dpi screen.
     * Thus on a 160dpi screen this density value will be 1; on a 120 dpi
     * screen it would be .75; etc.
     *
     * @return the logical density of the Display.
     */
    float getDensity();

    /**
     * Whether the given backend supports a display mode change via
     * calling {@link Graphics#setDisplayMode(DisplayMode)}
     *
     * @return whether display mode changes are supported or not.
     */
    boolean supportsDisplayModeChange();

    /**
     * Get supported display mode.
     * @return the supported fullscreen {@link DisplayMode}(s).
     */
    DisplayMode[] getDisplayModes();

    /**
     * get the display mode of the primary graphics adapter.
     * @return the display mode of the primary graphics adapter.
     */
    DisplayMode getDesktopDisplayMode();

    /**
     * Sets the current {@link DisplayMode}. Returns false in case the operation
     * failed. Not all backend support this method. See
     * {@link Graphics#supportsDisplayModeChange()}.
     *
     * @param displayMode the display mode.
     * @return whether the operation succeeded.
     */
    boolean setDisplayMode(DisplayMode displayMode);

    /**
     * Tries to set the display mode width the given width and height in pixels.
     * Will always succeed if fullscreen is set to false,
     * in which case the application will be run in windowed mode. Use
     * {@link Graphics#getDisplayModes()} to get a list of
     * supported fullscreen modes.
     *
     * @param width      the width in pixels
     * @param height     the height in pixels
     * @param fullscreen whether to use fullscreen rendering or not
     */
    boolean setDisplayMode(int width, int height, boolean fullscreen);

    /**
     * Sets the title of the window. Ignored on Android.
     *
     * @param title the title.
     */
    void setTitle(String title);

    /**
     * Enable/Disable vsynching. This is a best-effort attempt which might
     * not work on all platforms.
     *
     * @param vsync vsync enabled or not.
     */
    void setVSync(boolean vsync);

    /**
     * Get buffer format.
     * @return the format of the color, depth and stencil buffer in a
     * {@link BufferFormat} instance
     */
    BufferFormat getBufferFormat();

    /**
     * check  whether the extension is supported.
     * @param extension the extension name
     * @return whether the extension is supported
     */
    boolean supportsExtension(String extension);

    /**
     * check whether rendering is continuous.
     * @return whether rendering is continuous.
     */
    boolean isContinuousRendering();

    /**
     * Sets whether to render continuously. In case rendering is performed
     * non-continuously, the following events will trigger a
     * redraw:
     * <p/>
     * <ul>
     * <li>A call to {@link #requestRendering()}</li>
     * <li>IInput events from the touch screen/mouse or keyboard</li>
     * <li>A {@link Runnable} is posted to the rendering thread via
     * {@link Application#postRunnable(Runnable)}</li>
     * </ul>
     * <p/>
     * Life-cycle events will also be reported as usual,
     * see {@link ApplicationListener}. This method can be called from any
     * thread.
     *
     * @param isContinuous whether the rendering should be continuous or not.
     */
    void setContinuousRendering(boolean isContinuous);

    /**
     * Requests a new frame to be rendered if the rendering mode is non-continuous.
     * This method can be called from any thread.
     */
    void requestRendering();

    /**
     * Whether the app is fullscreen or not
     */
    boolean isFullscreen();


    /**
     * Clear screen with given color
     */
    void clearScreen(float red, float green, float blue, float alpha);



    /**
     * Describe a fullscreen display mode
     *
     * @author mzechner
     */
    class DisplayMode {
        public final int width;
        public final int height;
        public final int refreshRate;
        public final int bitsPerPixel;

        protected DisplayMode(int width, int height, int refreshRate,
                              int bitsPerPixel) {
            this.width = width;
            this.height = height;
            this.refreshRate = refreshRate;
            this.bitsPerPixel = bitsPerPixel;
        }

        public String toString() {
            return width + "x" + height + ", bpp: " + bitsPerPixel
                    + ", hz: " + refreshRate;
        }
    }

    /**
     * Class describing the bits per pixel, depth buffer precision,
     * stencil precision and number of MSAA samples.
     */
    class BufferFormat {
        /* number of bits per color channel */
        public final int r, g, b, a;
        /* number of bits for depth and stencil buffer */
        public final int depth, stencil;
        /**
         * number of samples for multi-sample anti-aliasing (MSAA) *
         */
        public final int samples;
        /**
         * whether coverage sampling anti-aliasing is used.
         * in that case you have to clear the coverage buffer as well!
         */
        public final boolean coverageSampling;

        public BufferFormat(int r, int g, int b, int a, int depth,
                            int stencil, int samples, boolean coverageSampling) {
            this.r = r;
            this.g = g;
            this.b = b;
            this.a = a;
            this.depth = depth;
            this.stencil = stencil;
            this.samples = samples;
            this.coverageSampling = coverageSampling;
        }

        public String toString() {
            return "r: " + r + ", g: " + g + ", b: " + b + ", a: "
                    + a + ", depth: " + depth + ", stencil: " + stencil
                    + ", num samples: " + samples + "," +
                    " coverage sampling: " + coverageSampling;
        }
    }
}
