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
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureAtlas;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * {@link AssetLoader} to load
 * {@link com.guidebee.game.graphics.TextureAtlas} instances.
 * Passing a {@link TextureAtlasParameter} to
 * {@link com.guidebee.game.engine.assets.AssetManager#load(String, Class,
 * com.guidebee.game.engine.assets.AssetLoaderParameters)} allows to specify
 * whether the atlas regions should be flipped
 * on the y-axis or not.
 *
 * @author mzechner
 */
public class TextureAtlasLoader extends SynchronousAssetLoader<TextureAtlas,
        TextureAtlasLoader.TextureAtlasParameter> {
    public TextureAtlasLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    TextureAtlas.TextureAtlasData data;

    @Override
    public TextureAtlas load(AssetManager assetManager, String fileName,
                             FileHandle file, TextureAtlasParameter parameter) {
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages()) {
            Texture texture = assetManager.get(page.textureFile.path()
                    .replaceAll("\\\\", "/"), Texture.class);
            page.texture = texture;
        }

        return new TextureAtlas(data);
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
                                                  FileHandle atlasFile,
                                                  TextureAtlasParameter parameter) {
        FileHandle imgDir = atlasFile.parent();

        if (parameter != null)
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, parameter.flip);
        else {
            data = new TextureAtlas.TextureAtlasData(atlasFile, imgDir, false);
        }

        Array<AssetDescriptor> dependencies = new Array();
        for (TextureAtlas.TextureAtlasData.Page page : data.getPages()) {
            TextureLoader.TextureParameter params = new TextureLoader.TextureParameter();
            params.format = page.format;
            params.genMipMaps = page.useMipMaps;
            params.minFilter = page.minFilter;
            params.magFilter = page.magFilter;
            dependencies.add(new AssetDescriptor(page.textureFile, Texture.class, params));
        }
        return dependencies;
    }

    static public class TextureAtlasParameter extends AssetLoaderParameters<TextureAtlas> {
        /**
         * whether to flip the texture atlas vertically *
         */
        public boolean flip = false;

        public TextureAtlasParameter() {
        }

        public TextureAtlasParameter(boolean flip) {
            this.flip = flip;
        }
    }
}
