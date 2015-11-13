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

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import com.guidebee.game.Configuration;
import com.guidebee.game.Files.FileType;
import com.guidebee.game.GameEngineRuntimeException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * An implementation of the {@link com.guidebee.game.Audio} interface
 * for Android.
 *
 * @author mzechner
 */
public final class Audio implements com.guidebee.game.Audio {
    private final SoundPool soundPool;
    private final AudioManager manager;
    protected final List<com.guidebee.game.engine.platform.Music> musics = new ArrayList<com.guidebee.game.engine.platform.Music>();

    public Audio(Context context, Configuration config) {
        if (!config.disableAudio) {
            soundPool = new SoundPool(config.maxSimultaneousSounds,
                    AudioManager.STREAM_MUSIC, 100);
            manager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            if (context instanceof Activity) {
                ((Activity) context).setVolumeControlStream(AudioManager.STREAM_MUSIC);
            }
        } else {
            soundPool = null;
            manager = null;
        }
    }

    public void pause() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            for (com.guidebee.game.engine.platform.Music music : musics) {
                if (music.isPlaying()) {
                    music.pause();
                    music.wasPlaying = true;
                } else
                    music.wasPlaying = false;
            }
        }
        this.soundPool.autoPause();
    }

    public void resume() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            for (int i = 0; i < musics.size(); i++) {
                if (musics.get(i).wasPlaying == true) musics.get(i).play();
            }
        }
        this.soundPool.autoResume();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.guidebee.game.audio.AudioDevice newAudioDevice(int samplingRate, boolean isMono) {
        if (soundPool == null) {
            throw new GameEngineRuntimeException(
                    "Android audio is not enabled by the application config.");
        }
        return new com.guidebee.game.engine.platform.AudioDevice(samplingRate, isMono);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.guidebee.game.audio.Music newMusic(com.guidebee.game.files.FileHandle file) {
        if (soundPool == null) {
            throw new GameEngineRuntimeException(
                    "Android audio is not enabled by the application config.");
        }
        FileHandle aHandle = (FileHandle) file;

        MediaPlayer mediaPlayer = new MediaPlayer();

        if (aHandle.type() == FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.assets.openFd(aHandle.path());
                mediaPlayer.setDataSource(descriptor.getFileDescriptor(),
                        descriptor.getStartOffset(), descriptor.getLength());
                descriptor.close();
                mediaPlayer.prepare();
                com.guidebee.game.engine.platform.Music music = new com.guidebee.game.engine.platform.Music(this, mediaPlayer);
                synchronized (musics) {
                    musics.add(music);
                }
                return music;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error loading audio file: " + file
                        + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        } else {
            try {
                mediaPlayer.setDataSource(aHandle.file().getPath());
                mediaPlayer.prepare();
                com.guidebee.game.engine.platform.Music music = new com.guidebee.game.engine.platform.Music(this, mediaPlayer);
                synchronized (musics) {
                    musics.add(music);
                }
                return music;
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error loading audio file: " + file, ex);
            }
        }

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.guidebee.game.audio.Sound newSound(com.guidebee.game.files.FileHandle file) {
        if (soundPool == null) {
            throw new GameEngineRuntimeException(
                    "Android audio is not enabled by the application config.");
        }
        FileHandle aHandle = (FileHandle) file;
        if (aHandle.type() == FileType.Internal) {
            try {
                AssetFileDescriptor descriptor = aHandle.assets.openFd(aHandle.path());
                com.guidebee.game.engine.platform.Sound sound = new com.guidebee.game.engine.platform.Sound(soundPool, manager, soundPool.load(descriptor, 1));
                descriptor.close();
                return sound;
            } catch (IOException ex) {
                throw new GameEngineRuntimeException("Error loading audio file: " + file
                        + "\nNote: Internal audio files must be placed in the assets directory.", ex);
            }
        } else {
            try {
                return new com.guidebee.game.engine.platform.Sound(soundPool, manager, soundPool.load(aHandle.file().getPath(), 1));
            } catch (Exception ex) {
                throw new GameEngineRuntimeException("Error loading audio file: " + file, ex);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public com.guidebee.game.audio.AudioRecorder newAudioRecorder(int samplingRate, boolean isMono) {
        if (soundPool == null) {
            throw new GameEngineRuntimeException(
                    "Android audio is not enabled by the application config.");
        }
        return new com.guidebee.game.engine.platform.AudioRecorder(samplingRate, isMono);
    }

    /**
     * Kills the soundpool and all other resources
     */
    public void dispose() {
        if (soundPool == null) {
            return;
        }
        synchronized (musics) {
            ArrayList<com.guidebee.game.engine.platform.Music> musicsCopy = new ArrayList<com.guidebee.game.engine.platform.Music>(musics);
            for (com.guidebee.game.engine.platform.Music music : musicsCopy) {
                music.dispose();
            }
        }
        soundPool.release();
    }
}
