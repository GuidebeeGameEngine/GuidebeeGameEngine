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
package com.guidebee.game.engine.assets.loaders;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.engine.assets.AssetDescriptor;
import com.guidebee.game.engine.assets.AssetLoaderParameters;
import com.guidebee.game.engine.assets.AssetManager;
import com.guidebee.game.files.FileHandle;
import com.guidebee.game.graphics.TrueTypeFont;
import com.guidebee.utils.collections.Array;

import java.io.IOException;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * {@link AssetLoader} for {@link com.guidebee.game.graphics.TrueTypeFont}
 * instances. The TrueTypeFont is loaded asynchronously.
 *
 * @author mzechner
 */
public class TrueTypeFontLoader extends AsynchronousAssetLoader<TrueTypeFont,
        TrueTypeFontLoader.TrueTypeFontParameter> {
    public TrueTypeFontLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    TrueTypeFont trueTypeFont;

    @Override
    public void loadAsync(AssetManager manager, String fileName,
                          FileHandle file, TrueTypeFontParameter parameter) {
        trueTypeFont = null;
        try {
            trueTypeFont = new TrueTypeFont(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public TrueTypeFont loadSync(AssetManager manager, String fileName,
                             FileHandle file, TrueTypeFontParameter parameter) {
        TrueTypeFont trueTypeFont = this.trueTypeFont;
        this.trueTypeFont = null;
        return trueTypeFont;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
                                                  FileHandle file,
                                                  TrueTypeFontParameter parameter) {
        return null;
    }

    static public class TrueTypeFontParameter extends AssetLoaderParameters<TrueTypeFont> {
    }
}
