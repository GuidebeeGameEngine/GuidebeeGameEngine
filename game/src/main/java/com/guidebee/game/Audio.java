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
package com.guidebee.game;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.audio.AudioDevice;
import com.guidebee.game.audio.AudioRecorder;
import com.guidebee.game.audio.Music;
import com.guidebee.game.audio.Sound;
import com.guidebee.game.files.FileHandle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * This interface encapsulates the creation and management of audio resources.
 * It allows you to get direct access to the audio hardware via the
 * {@link com.guidebee.game.audio.AudioDevice} and {@link com.guidebee.game.audio.AudioRecorder} interfaces, create sound
 * effects via the {@link com.guidebee.game.audio.Sound} interface and play music streams via the
 * {@link com.guidebee.game.audio.Music} interface.
 * <p>
 * <p>
 * All resources created via this interface have to be disposed as soon as
 * they are no longer used.
 * </p>
 * <p>
 * <p>
 * Note that all {@link com.guidebee.game.audio.Music} instances will be automatically paused when
 * the {@link ApplicationListener#pause()} method is called, and automatically
 * resumed when the {@link ApplicationListener#resume()} method is called.
 * </p>
 *
 * @author mzechner
 */
public interface Audio {
    /**
     * Creates a new {@link com.guidebee.game.audio.AudioDevice} either in mono or stereo mode.
     * The IAudioDevice has to be disposed via its
     * {@link com.guidebee.game.audio.AudioDevice#dispose()} method when it is no longer used.
     *
     * @param samplingRate the sampling rate.
     * @param isMono       whether the IAudioDevice should be in mono or stereo mode
     * @return the IAudioDevice
     * @throws GameEngineRuntimeException in
     *                                    case the device could not be created
     */
    public AudioDevice newAudioDevice(int samplingRate, boolean isMono);

    /**
     * Creates a new {@link com.guidebee.game.audio.AudioRecorder}. The AudioRecorder has to
     * be disposed after it is no longer used.
     *
     * @param samplingRate the sampling rate in Hertz
     * @param isMono       whether the recorder records in mono or stereo
     * @return the AudioRecorder
     * @throws GameEngineRuntimeException in
     *                                    case the recorder could not be created
     */
    public AudioRecorder newAudioRecorder(int samplingRate, boolean isMono);

    /**
     * <p>
     * Creates a new {@link com.guidebee.game.audio.Sound} which is used to play back audio effects
     * such as gun shots or explosions. The Sound's audio data
     * is retrieved from the file specified via the {@link FileHandle}.
     * Note that the complete audio data is loaded into RAM. You
     * should therefore not load big audio files with this methods. The
     * current upper limit for decoded audio is 1 MB.
     * </p>
     * <p>
     * <p>
     * Currently supported formats are WAV, MP3 and OGG.
     * </p>
     * <p>
     * <p>
     * The Sound has to be disposed if it is no longer used via the
     * {@link com.guidebee.game.audio.Sound#dispose()} method.
     * </p>
     *
     * @return the new Sound
     * @throws GameEngineRuntimeException in case the sound could not be loaded
     */
    public Sound newSound(FileHandle fileHandle);

    /**
     * Creates a new {@link com.guidebee.game.audio.Music} instance which is used to play back
     * a music stream from a file. Currently supported formats are
     * WAV, MP3 and OGG. The IMusic instance has to be disposed if it is
     * no longer used via the {@link com.guidebee.game.audio.Music#dispose()} method.
     * IMusic instances are automatically paused when
     * {@link ApplicationListener#pause()} is called and resumed when
     * {@link ApplicationListener#resume()} is called.
     *
     * @param file the FileHandle
     * @return the new IMusic or null if the IMusic could not be loaded
     * @throws GameEngineRuntimeException in case the music could not be loaded
     */
    public Music newMusic(FileHandle file);
}
