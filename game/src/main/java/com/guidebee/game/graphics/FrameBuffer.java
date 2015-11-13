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
package com.guidebee.game.graphics;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.Application;
import com.guidebee.game.GameEngine;
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.utils.Disposable;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.BufferUtils;

import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * <p>
 * Encapsulates OpenGL ES 2.0 frame buffer objects. This is a simple helper class
 * which should cover most FBO uses. It will
 * automatically create a texture for the color attachment and a renderbuffer for
 * the depth buffer. You can get a hold of the
 * texture by {@link FrameBuffer#getColorBufferTexture()}. This class will only
 * work with OpenGL ES 2.0.
 * </p>
 * <p>
 * <p>
 * FrameBuffers are managed. In case of an OpenGL context loss, which only happens
 * on Android when a user switches to another
 * application or receives an incoming call, the framebuffer will be automatically
 * recreated.
 * </p>
 * <p>
 * <p>
 * A FrameBuffer must be disposed if it is no longer needed
 * </p>
 *
 * @author mzechner
 */
public class FrameBuffer implements Disposable {
    /**
     * the frame buffers *
     */
    private final static Map<Application, Array<FrameBuffer>>
            buffers = new HashMap<Application, Array<FrameBuffer>>();

    /**
     * the color buffer texture *
     */
    protected Texture colorTexture;

    /**
     * the default framebuffer handle, a.k.a screen.
     */
    private static int defaultFramebufferHandle;
    /**
     * true if we have polled for the default handle already.
     */
    private static boolean defaultFramebufferHandleInitialized = false;

    /**
     * the framebuffer handle *
     */
    private int framebufferHandle;

    /**
     * the depthbuffer render object handle *
     */
    private int depthbufferHandle;

    /**
     * width *
     */
    protected final int width;

    /**
     * height *
     */
    protected final int height;

    /**
     * depth *
     */
    protected final boolean hasDepth;

    /**
     * format *
     */
    protected final Pixmap.Format format;

    /**
     * Creates a new FrameBuffer having the given dimensions and potentially
     * a depth buffer attached.
     *
     * @param format   the format of the color buffer; according to the
     *                 OpenGL ES 2.0 spec, only RGB565, RGBA4444 and RGB5_A1 are
     *                 color-renderable
     * @param width    the width of the framebuffer in pixels
     * @param height   the height of the framebuffer in pixels
     * @param hasDepth whether to attach a depth buffer
     * @throws com.guidebee.game.GameEngineRuntimeException in case the FrameBuffer could not be created
     */
    public FrameBuffer(Pixmap.Format format, int width, int height, boolean hasDepth) {
        this.width = width;
        this.height = height;
        this.format = format;
        this.hasDepth = hasDepth;
        build();

        addManagedFrameBuffer(GameEngine.app, this);
    }

    /**
     * Override this method in a derived class to set up the backing
     * texture as you like.
     */
    protected void setupTexture() {
        colorTexture = new Texture(width, height, format);
        colorTexture.setFilter(Texture.TextureFilter.Linear,
                Texture.TextureFilter.Linear);
        colorTexture.setWrap(Texture.TextureWrap.ClampToEdge,
                Texture.TextureWrap.ClampToEdge);
    }

    private void build() {
        IGL20 gl = GameEngine.gl20;


        if (!defaultFramebufferHandleInitialized) {
            defaultFramebufferHandleInitialized = true;
            defaultFramebufferHandle = 0;
        }

        setupTexture();

        IntBuffer handle = BufferUtils.newIntBuffer(1);
        gl.glGenFramebuffers(1, handle);
        framebufferHandle = handle.get(0);

        if (hasDepth) {
            handle.clear();
            gl.glGenRenderbuffers(1, handle);
            depthbufferHandle = handle.get(0);
        }

        gl.glBindTexture(IGL20.GL_TEXTURE_2D, colorTexture.getTextureObjectHandle());

        if (hasDepth) {
            gl.glBindRenderbuffer(IGL20.GL_RENDERBUFFER, depthbufferHandle);
            gl.glRenderbufferStorage(IGL20.GL_RENDERBUFFER,
                    IGL20.GL_DEPTH_COMPONENT16, colorTexture.getWidth(),
                    colorTexture.getHeight());
        }

        gl.glBindFramebuffer(IGL20.GL_FRAMEBUFFER, framebufferHandle);
        gl.glFramebufferTexture2D(IGL20.GL_FRAMEBUFFER,
                IGL20.GL_COLOR_ATTACHMENT0, IGL20.GL_TEXTURE_2D,
                colorTexture.getTextureObjectHandle(), 0);
        if (hasDepth) {
            gl.glFramebufferRenderbuffer(IGL20.GL_FRAMEBUFFER,
                    IGL20.GL_DEPTH_ATTACHMENT, IGL20.GL_RENDERBUFFER, depthbufferHandle);
        }
        int result = gl.glCheckFramebufferStatus(IGL20.GL_FRAMEBUFFER);

        gl.glBindRenderbuffer(IGL20.GL_RENDERBUFFER, 0);
        gl.glBindTexture(IGL20.GL_TEXTURE_2D, 0);
        gl.glBindFramebuffer(IGL20.GL_FRAMEBUFFER, defaultFramebufferHandle);

        if (result != IGL20.GL_FRAMEBUFFER_COMPLETE) {
            colorTexture.dispose();
            if (hasDepth) {
                handle.clear();
                handle.put(depthbufferHandle);
                handle.flip();
                gl.glDeleteRenderbuffers(1, handle);
            }

            handle.clear();
            handle.put(framebufferHandle);
            handle.flip();
            gl.glDeleteFramebuffers(1, handle);

            if (result == IGL20.GL_FRAMEBUFFER_INCOMPLETE_ATTACHMENT)
                throw new IllegalStateException(
                        "frame buffer couldn't be constructed: incomplete attachment");
            if (result == IGL20.GL_FRAMEBUFFER_INCOMPLETE_DIMENSIONS)
                throw new IllegalStateException(
                        "frame buffer couldn't be constructed: incomplete dimensions");
            if (result == IGL20.GL_FRAMEBUFFER_INCOMPLETE_MISSING_ATTACHMENT)
                throw new IllegalStateException(
                        "frame buffer couldn't be constructed: missing attachment");
            if (result == IGL20.GL_FRAMEBUFFER_UNSUPPORTED)
                throw new IllegalStateException(
                        "frame buffer couldn't be constructed: unsupported combination of formats");
            throw new IllegalStateException(
                    "frame buffer couldn't be constructed: unknown error " + result);
        }
    }

    /**
     * Releases all resources associated with the FrameBuffer.
     */
    public void dispose() {
        IGL20 gl = GameEngine.gl20;

        IntBuffer handle = BufferUtils.newIntBuffer(1);

        colorTexture.dispose();
        if (hasDepth) {
            handle.put(depthbufferHandle);
            handle.flip();
            gl.glDeleteRenderbuffers(1, handle);
        }

        handle.clear();
        handle.put(framebufferHandle);
        handle.flip();
        gl.glDeleteFramebuffers(1, handle);

        if (buffers.get(GameEngine.app) != null)
            buffers.get(GameEngine.app).removeValue(this, true);
    }

    /**
     * Makes the frame buffer current so everything gets drawn to it.
     */
    public void bind() {
        GameEngine.gl20.glBindFramebuffer(IGL20.GL_FRAMEBUFFER,
                framebufferHandle);
    }

    /**
     * Unbinds the framebuffer, all drawing will be performed to the
     * normal framebuffer from here on.
     */
    public static void unbind() {
        GameEngine.gl20.glBindFramebuffer(IGL20.GL_FRAMEBUFFER,
                defaultFramebufferHandle);
    }

    /**
     * Binds the frame buffer and sets the viewport accordingly,
     * so everything gets drawn to it.
     */
    public void begin() {
        bind();
        setFrameBufferViewport();
    }

    /**
     * Sets viewport to the dimensions of framebuffer. Called by {@link #begin()}.
     */
    protected void setFrameBufferViewport() {
        GameEngine.gl20.glViewport(0, 0, colorTexture.getWidth(),
                colorTexture.getHeight());
    }

    /**
     * Unbinds the framebuffer, all drawing will be performed to the
     * normal framebuffer from here on.
     */
    public void end() {
        unbind();
        setDefaultFrameBufferViewport();
    }

    /**
     * Sets viewport to the dimensions of default framebuffer (window).
     * Called by {@link #end()}.
     */
    protected void setDefaultFrameBufferViewport() {
        GameEngine.gl20.glViewport(0, 0, GameEngine.graphics.getWidth(),
                GameEngine.graphics.getHeight());
    }

    /**
     * Unbinds the framebuffer and sets viewport sizes, all drawing will
     * be performed to the normal framebuffer from here on.
     *
     * @param x      the x-axis position of the viewport in pixels
     * @param y      the y-asis position of the viewport in pixels
     * @param width  the width of the viewport in pixels
     * @param height the height of the viewport in pixels
     */
    public void end(int x, int y, int width, int height) {
        unbind();
        GameEngine.gl20.glViewport(x, y, width, height);
    }

    /**
     * @return the color buffer texture
     */
    public Texture getColorBufferTexture() {
        return colorTexture;
    }

    /**
     * @return the height of the framebuffer in pixels
     */
    public int getHeight() {
        return colorTexture.getHeight();
    }

    /**
     * @return the width of the framebuffer in pixels
     */
    public int getWidth() {
        return colorTexture.getWidth();
    }

    private static void addManagedFrameBuffer(Application app, FrameBuffer frameBuffer) {
        Array<FrameBuffer> managedResources = buffers.get(app);
        if (managedResources == null) managedResources = new Array<FrameBuffer>();
        managedResources.add(frameBuffer);
        buffers.put(app, managedResources);
    }

    /**
     * Invalidates all frame buffers. This can be used when the OpenGL context
     * is lost to rebuild all managed frame buffers. This
     * assumes that the texture attached to this buffer has already been rebuild! Use with care.
     */
    public static void invalidateAllFrameBuffers(Application app) {
        if (GameEngine.gl20 == null) return;

        Array<FrameBuffer> bufferArray = buffers.get(app);
        if (bufferArray == null) return;
        for (int i = 0; i < bufferArray.size; i++) {
            bufferArray.get(i).build();
        }
    }

    public static void clearAllFrameBuffers(Application app) {
        buffers.remove(app);
    }

    public static StringBuilder getManagedStatus(final StringBuilder builder) {
        builder.append("Managed buffers/app: { ");
        for (Application app : buffers.keySet()) {
            builder.append(buffers.get(app).size);
            builder.append(" ");
        }
        builder.append("}");
        return builder;
    }

    public static String getManagedStatus() {
        return getManagedStatus(new StringBuilder()).toString();
    }
}
