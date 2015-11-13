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
package com.guidebee.game.engine.graphics.opengles;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.graphics.FrameBuffer;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.Texture.TextureFilter;
import com.guidebee.game.graphics.Texture.TextureWrap;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * This is a {@link com.guidebee.game.graphics.FrameBuffer} variant backed by a float texture.
 */
public class FloatFrameBuffer extends FrameBuffer {

    /**
     * Creates a new FrameBuffer with a float backing texture, having the
     * given dimensions and potentially a depth buffer attached.
     *
     * @param width    the width of the framebuffer in pixels
     * @param height   the height of the framebuffer in pixels
     * @param hasDepth whether to attach a depth buffer
     * @throws com.guidebee.game.GameEngineRuntimeException
     * in case the FrameBuffer could not be created
     */
    public FloatFrameBuffer(int width, int height, boolean hasDepth) {
        super(null, width, height, hasDepth);
    }

    /**
     * Override this method in a derived class to set up the backing
     * texture as you like.
     */
    protected void setupTexture() {
        FloatTextureData data = new FloatTextureData(width, height);
        colorTexture = new Texture(data);
        // no filtering for float textures in OpenGL ES
        colorTexture.setFilter(TextureFilter.Nearest, TextureFilter.Nearest);
        colorTexture.setWrap(TextureWrap.ClampToEdge, TextureWrap.ClampToEdge);
    }
}
