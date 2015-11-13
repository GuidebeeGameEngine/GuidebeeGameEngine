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

import com.guidebee.game.GameEngine;
import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.graphics.Pixmap;
import com.guidebee.game.graphics.TextureData;
import com.guidebee.utils.collections.BufferUtils;

import java.nio.FloatBuffer;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * A {@link com.guidebee.game.graphics.TextureData} implementation
 * which should be used to create float textures.
 */
public class FloatTextureData implements TextureData {

    int width = 0;
    int height = 0;
    boolean isPrepared = false;
    FloatBuffer buffer;

    public FloatTextureData(int w, int h) {
        this.width = w;
        this.height = h;
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Custom;
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void prepare() {
        if (isPrepared) throw new GameEngineRuntimeException("Already prepared");
        this.buffer = BufferUtils.newFloatBuffer(width * height * 4);
        isPrepared = true;
    }

    @Override
    public void consumeCustomData(int target) {
        if (!GameEngine.graphics.supportsExtension("texture_float"))
            throw new GameEngineRuntimeException("Extension OES_TEXTURE_FLOAT not supported!");

        // this is a const from GL 3.0, used only on desktops
        final int GL_RGBA32F = 34836;

        // GLES and WebGL defines texture format by 3rd and 8th argument,
        // so to get a float texture one needs to supply GL_RGBA and GL_FLOAT there.
        GameEngine.gl.glTexImage2D(target, 0, IGL20.GL_RGBA, width, height,
                0, IGL20.GL_RGBA, IGL20.GL_FLOAT, buffer);
    }

    @Override
    public Pixmap consumePixmap() {
        throw new GameEngineRuntimeException("This TextureData implementation does not return a Pixmap");
    }

    @Override
    public boolean disposePixmap() {
        throw new GameEngineRuntimeException("This TextureData implementation does not return a Pixmap");
    }

    @Override
    public int getWidth() {
        return width;
    }

    @Override
    public int getHeight() {
        return height;
    }

    @Override
    public Pixmap.Format getFormat() {
        return Pixmap.Format.RGBA8888; // it's not true, but FloatTextureData.getFormat() isn't used anywhere
    }

    @Override
    public boolean useMipMaps() {
        return false;
    }

    @Override
    public boolean isManaged() {
        return true;
    }
}
