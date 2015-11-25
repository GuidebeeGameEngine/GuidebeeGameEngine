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

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;

import com.guidebee.game.Files.FileType;
import com.guidebee.game.GameEngine;
import com.guidebee.game.GameEngineRuntimeException;

import java.io.File;
import java.io.FileFilter;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * @author mzechner
 * @author Nathan Sweet
 */
public class FileHandle extends com.guidebee.game.files.FileHandle {
    // The asset manager, or null if this is not an internal file.
    final AssetManager assets;

    FileHandle(AssetManager assets, String fileName, FileType type) {
        super(fileName.replace('\\', '/'), type);
        this.assets = assets;
    }

    FileHandle(AssetManager assets, File file, FileType type) {
        super(file, type);
        this.assets = assets;
    }

    public com.guidebee.game.files.FileHandle child(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0)
            return new FileHandle(assets, new File(name), type);
        return new FileHandle(assets, new File(file, name), type);
    }

    public com.guidebee.game.files.FileHandle sibling(String name) {
        name = name.replace('\\', '/');
        if (file.getPath().length() == 0)
            throw new GameEngineRuntimeException("Cannot get the sibling of the root.");
        return new FileHandle(assets, new File(file.getParent(), name), type);
    }

    public com.guidebee.game.files.FileHandle parent() {
        File parent = file.getParentFile();
        if (parent == null) {
            if (type == FileType.Absolute)
                parent = new File("/");
            else
                parent = new File("");
        }
        return new FileHandle(assets, parent, type);
    }

    public InputStream read() {
        if (type == FileType.Internal) {
            try {
                return assets.open(file.getPath());
            } catch (IOException ex) {
                throw new GameEngineRuntimeException(
                        "Error reading file: " + file + " (" + type + ")", ex);
            }
        }
        return super.read();
    }

    public com.guidebee.game.files.FileHandle[] list() {
        if (type == FileType.Internal) {
            try {
                String[] relativePaths = assets.list(file.getPath());
                com.guidebee.game.files.FileHandle[] handles
                        = new com.guidebee.game.files.FileHandle[relativePaths.length];
                for (int i = 0, n = handles.length; i < n; i++)
                    handles[i] = new FileHandle(assets, new File(file, relativePaths[i]), type);
                return handles;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error listing children: "
                        + file + " (" + type + ")", ex);
            }
        }
        return super.list();
    }

    public com.guidebee.game.files.FileHandle[] list(FileFilter filter) {
        if (type == FileType.Internal) {
            try {
                String[] relativePaths = assets.list(file.getPath());
                com.guidebee.game.files.FileHandle[] handles
                        = new com.guidebee.game.files.FileHandle[relativePaths.length];
                int count = 0;
                for (int i = 0, n = handles.length; i < n; i++) {
                    String path = relativePaths[i];
                    com.guidebee.game.files.FileHandle child
                            = new FileHandle(assets, new File(file, path), type);
                    if (!filter.accept(child.file())) continue;
                    handles[count] = child;
                    count++;
                }
                if (count < relativePaths.length) {
                    com.guidebee.game.files.FileHandle[] newHandles
                            = new com.guidebee.game.files.FileHandle[count];
                    System.arraycopy(handles, 0, newHandles, 0, count);
                    handles = newHandles;
                }
                return handles;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error listing children: "
                        + file + " (" + type + ")", ex);
            }
        }
        return super.list(filter);
    }

    public com.guidebee.game.files.FileHandle[] list(FilenameFilter filter) {
        if (type == FileType.Internal) {
            try {
                String[] relativePaths = assets.list(file.getPath());
                com.guidebee.game.files.FileHandle[] handles
                        = new com.guidebee.game.files.FileHandle[relativePaths.length];
                int count = 0;
                for (int i = 0, n = handles.length; i < n; i++) {
                    String path = relativePaths[i];
                    if (!filter.accept(file, path)) continue;
                    handles[count] = new FileHandle(assets, new File(file, path), type);
                    count++;
                }
                if (count < relativePaths.length) {
                    com.guidebee.game.files.FileHandle[] newHandles
                            = new com.guidebee.game.files.FileHandle[count];
                    System.arraycopy(handles, 0, newHandles, 0, count);
                    handles = newHandles;
                }
                return handles;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error listing children: "
                        + file + " (" + type + ")", ex);
            }
        }
        return super.list(filter);
    }

    public com.guidebee.game.files.FileHandle[] list(String suffix) {
        if (type == FileType.Internal) {
            try {
                String[] relativePaths = assets.list(file.getPath());
                com.guidebee.game.files.FileHandle[] handles
                        = new com.guidebee.game.files.FileHandle[relativePaths.length];
                int count = 0;
                for (int i = 0, n = handles.length; i < n; i++) {
                    String path = relativePaths[i];
                    if (!path.endsWith(suffix)) continue;
                    handles[count] = new FileHandle(assets, new File(file, path), type);
                    count++;
                }
                if (count < relativePaths.length) {
                    com.guidebee.game.files.FileHandle[] newHandles
                            = new com.guidebee.game.files.FileHandle[count];
                    System.arraycopy(handles, 0, newHandles, 0, count);
                    handles = newHandles;
                }
                return handles;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error listing children: "
                        + file + " (" + type + ")", ex);
            }
        }
        return super.list(suffix);
    }

    public boolean isDirectory() {
        if (type == FileType.Internal) {
            try {
                return assets.list(file.getPath()).length > 0;
            } catch (IOException ex) {
                return false;
            }
        }
        return super.isDirectory();
    }

    public boolean exists() {
        if (type == FileType.Internal) {
            String fileName = file.getPath();
            try {
                assets.open(fileName).close(); // Check if file exists.
                return true;
            } catch (Exception ex) {
                // This is SUPER slow! but we need it for directories.
                try {
                    return assets.list(fileName).length > 0;
                } catch (Exception ignored) {
                }
                return false;
            }
        }
        return super.exists();
    }

    public long length() {
        if (type == FileType.Internal) {
            AssetFileDescriptor fileDescriptor = null;
            try {
                fileDescriptor = assets.openFd(file.getPath());
                return fileDescriptor.getLength();
            } catch (IOException ignored) {
            } finally {
                if (fileDescriptor != null) {
                    try {
                        fileDescriptor.close();
                    } catch (IOException e) {
                    }
                    ;
                }
            }
        }
        return super.length();
    }

    public long lastModified() {
        return super.lastModified();
    }

    public File file() {
        if (type == FileType.Local)
            return new File(GameEngine.files.getLocalStoragePath(), file.getPath());
        return super.file();
    }

}
