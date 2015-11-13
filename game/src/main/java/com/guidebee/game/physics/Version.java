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
package com.guidebee.game.physics;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Box2d native library version.
 * <p/>
 * <hr>
 *
 * @author James Shen.
 */
public class Version {

    /**
     * significant changes
     */
    public static int Major;

    /**
     * incremental changes
     */
    public static int Minor;

    /**
     * bug fixes
     */
    public static int Revision;

    /**
     * Get native box2d version.
     */
    public static native void getVersion();


    /**
     * load the native version.
     */
    static {
        getVersion();
    }

}
