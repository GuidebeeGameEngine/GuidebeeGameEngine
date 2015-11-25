/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
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
package com.guidebee.game.tween;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.tween.equations.Back;
import com.guidebee.game.tween.equations.Bounce;
import com.guidebee.game.tween.equations.Circ;
import com.guidebee.game.tween.equations.Cubic;
import com.guidebee.game.tween.equations.Elastic;
import com.guidebee.game.tween.equations.Expo;
import com.guidebee.game.tween.equations.Linear;
import com.guidebee.game.tween.equations.Quad;
import com.guidebee.game.tween.equations.Quart;
import com.guidebee.game.tween.equations.Quint;
import com.guidebee.game.tween.equations.Sine;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Collection of miscellaneous utilities.
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class TweenUtils {
    private static TweenEquation[] easings;

    /**
     * Takes an easing name and gives you the corresponding TweenEquation.
     * You probably won't need this, but tools will love that.
     *
     * @param easingName The name of an easing, like "Quad.INOUT".
     * @return The parsed equation, or null if there is no match.
     */
    public static TweenEquation parseEasing(String easingName) {
        if (easings == null) {
            easings = new TweenEquation[]{Linear.INOUT,
                    Quad.IN, Quad.OUT, Quad.INOUT,
                    Cubic.IN, Cubic.OUT, Cubic.INOUT,
                    Quart.IN, Quart.OUT, Quart.INOUT,
                    Quint.IN, Quint.OUT, Quint.INOUT,
                    Circ.IN, Circ.OUT, Circ.INOUT,
                    Sine.IN, Sine.OUT, Sine.INOUT,
                    Expo.IN, Expo.OUT, Expo.INOUT,
                    Back.IN, Back.OUT, Back.INOUT,
                    Bounce.IN, Bounce.OUT, Bounce.INOUT,
                    Elastic.IN, Elastic.OUT, Elastic.INOUT
            };
        }

        for (int i = 0; i < easings.length; i++) {
            if (easingName.equals(easings[i].toString()))
                return easings[i];
        }

        return null;
    }
}
