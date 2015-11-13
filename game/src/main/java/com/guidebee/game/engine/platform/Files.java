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
import android.content.res.AssetManager;
import android.os.Environment;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * @author mzechner
 * @author Nathan Sweet
 */
public class Files implements com.guidebee.game.Files {
    protected final String sdcard
            = Environment.getExternalStorageDirectory().getAbsolutePath() + "/";
    protected final String localpath;

    protected final AssetManager assets;

    public Files(AssetManager assets) {
        this.assets = assets;
        localpath = sdcard;
    }

    public Files(AssetManager assets, String localpath) {
        this.assets = assets;
        this.localpath = localpath.endsWith("/") ? localpath : localpath + "/";
    }

    @Override
    public com.guidebee.game.files.FileHandle getFileHandle(String path,
                                                                   FileType type) {
        return new FileHandle(type == FileType.Internal ? assets : null, path, type);
    }

    @Override
    public com.guidebee.game.files.FileHandle classpath(String path) {
        return new FileHandle(null, path, FileType.Classpath);
    }

    @Override
    public com.guidebee.game.files.FileHandle internal(String path) {
        return new FileHandle(assets, path, FileType.Internal);
    }

    @Override
    public com.guidebee.game.files.FileHandle external(String path) {
        return new FileHandle(null, path, FileType.External);
    }

    @Override
    public com.guidebee.game.files.FileHandle absolute(String path) {
        return new FileHandle(null, path, FileType.Absolute);
    }

    @Override
    public com.guidebee.game.files.FileHandle local(String path) {
        return new FileHandle(null, path, FileType.Local);
    }

    @Override
    public String getExternalStoragePath() {
        return sdcard;
    }

    @Override
    public boolean isExternalStorageAvailable() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    @Override
    public String getLocalStoragePath() {
        return localpath;
    }

    @Override
    public boolean isLocalStorageAvailable() {
        return true;
    }
}
