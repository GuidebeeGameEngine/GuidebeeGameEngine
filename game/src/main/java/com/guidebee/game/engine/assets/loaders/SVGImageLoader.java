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
import com.guidebee.game.graphics.SVGImage;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * {@link AssetLoader} for {@link com.guidebee.game.graphics.SVGImage}
 * instances. The SVGImage is loaded asynchronously.
 *
 * @author mzechner
 */
public class SVGImageLoader extends AsynchronousAssetLoader<SVGImage,
        SVGImageLoader.SVGImageParameter> {
    public SVGImageLoader(FileHandleResolver resolver) {
        super(resolver);
    }

    SVGImage svgImage;

    @Override
    public void loadAsync(AssetManager manager, String fileName,
                          FileHandle file, SVGImageParameter parameter) {
        svgImage = null;
        svgImage = new SVGImage(file);
    }

    @Override
    public SVGImage loadSync(AssetManager manager, String fileName,
                           FileHandle file, SVGImageParameter parameter) {
        SVGImage pixmap = this.svgImage;
        this.svgImage = null;
        return pixmap;
    }

    @Override
    public Array<AssetDescriptor> getDependencies(String fileName,
                                                  FileHandle file,
                                                  SVGImageParameter parameter) {
        return null;
    }

    static public class SVGImageParameter extends AssetLoaderParameters<SVGImage> {
    }
}
