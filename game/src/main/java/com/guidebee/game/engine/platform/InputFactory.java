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
import android.content.Context;
import com.guidebee.game.Application;
import com.guidebee.game.Configuration;

import java.lang.reflect.Constructor;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Class that instantiates Input or InputThreePlus depending on the SDK level,
 * via reflection.
 *
 * @author mzechner
 */
public class InputFactory {
    public static Input newAndroidInput(Application activity, Context context,
                                        Object view,
                                        Configuration config) {
        try {
            Class<?> clazz = null;
            Input input = null;

            int sdkVersion = android.os.Build.VERSION.SDK_INT;
            if (sdkVersion >= 12) {
                clazz = Class.forName("com.guidebee.game.engine.platform.InputThreePlus");
            } else {
                clazz = Class.forName("com.guidebee.game.engine.platform.Input");
            }
            Constructor<?> constructor = clazz.getConstructor(Application.class,
                    Context.class, Object.class,
                    Configuration.class);
            input = (Input) constructor.newInstance(activity, context,
                    view, config);
            return input;
        } catch (Exception e) {
            throw new RuntimeException(
                    "Couldn't construct Input, this should never happen", e);
        }
    }
}
