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

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.graphics.GLTexture;
import com.guidebee.game.graphics.Pixmap;
import com.guidebee.game.graphics.Pixmap.Format;
import com.guidebee.game.graphics.TextureData;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * This class will load each contained TextureData to the chosen mipmap level.
 * All the mipmap levels must be defined and cannot be null.
 */
public class MipMapTextureData implements TextureData {
    TextureData[] mips;

    /**
     * @param mipMapData must be != null and its length must be >= 1
     */
    public MipMapTextureData(TextureData... mipMapData) {
        mips = new TextureData[mipMapData.length];
        System.arraycopy(mipMapData, 0, mips, 0, mipMapData.length);
    }

    @Override
    public TextureDataType getType() {
        return TextureDataType.Custom;
    }

    @Override
    public boolean isPrepared() {
        return true;
    }

    @Override
    public void prepare() {
    }

    @Override
    public Pixmap consumePixmap() {
        throw new GameEngineRuntimeException("It's compressed, use the compressed method");
    }

    @Override
    public boolean disposePixmap() {
        return false;
    }

    @Override
    public void consumeCustomData(int target) {
        for (int i = 0; i < mips.length; ++i) {
            GLTexture.uploadImageData(target, mips[i], i);
        }
    }

    @Override
    public int getWidth() {
        return mips[0].getWidth();
    }

    @Override
    public int getHeight() {
        return mips[0].getHeight();
    }

    @Override
    public Format getFormat() {
        return mips[0].getFormat();
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