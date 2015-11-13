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
import com.guidebee.game.GameEngine;
import com.guidebee.game.audio.Sound;
import com.guidebee.game.engine.assets.AssetDescriptor;
import com.guidebee.game.engine.assets.AssetLoaderParameters;
import com.guidebee.game.engine.assets.AssetManager;
import com.guidebee.game.files.FileHandle;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * {@link AssetLoader} to load {@link com.guidebee.game.audio.Sound} instances.
 *
 * @author mzechner
 */
public class SoundLoader extends AsynchronousAssetLoader<Sound,
        SoundLoader.SoundParameter> {

    private Sound sound;

    public SoundLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    @Override
    public void loadAsync(AssetManager manager, String fileName,
                          FileHandle file, SoundParameter parameter) {
        sound = GameEngine.audio.newSound(file);
    }

    @Override
    public Sound loadSync(AssetManager manager, String fileName,
                          FileHandle file, SoundParameter parameter) {
        Sound sound = this.sound;
        this.sound = null;
        return sound;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
                                                  FileHandle file, SoundParameter parameter) {
        return null;
    }

    static public class SoundParameter extends AssetLoaderParameters<Sound> {
    }

}
