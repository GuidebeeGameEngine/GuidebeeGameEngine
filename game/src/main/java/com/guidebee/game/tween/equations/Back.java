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
package com.guidebee.game.tween.equations;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.tween.TweenEquation;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Easing equation based on Robert Penner's work:
 * http://robertpenner.com/easing/
 *
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public abstract class Back extends TweenEquation {
    public static final Back IN = new Back() {
        @Override
        public final float compute(float t) {
            float s = param_s;
            return t * t * ((s + 1) * t - s);
        }

        @Override
        public String toString() {
            return "Back.IN";
        }
    };

    public static final Back OUT = new Back() {
        @Override
        public final float compute(float t) {
            float s = param_s;
            return (t -= 1) * t * ((s + 1) * t + s) + 1;
        }

        @Override
        public String toString() {
            return "Back.OUT";
        }
    };

    public static final Back INOUT = new Back() {
        @Override
        public final float compute(float t) {
            float s = param_s;
            if ((t *= 2) < 1) return 0.5f * (t * t * (((s *= (1.525f)) + 1) * t - s));
            return 0.5f * ((t -= 2) * t * (((s *= (1.525f)) + 1) * t + s) + 2);
        }

        @Override
        public String toString() {
            return "Back.INOUT";
        }
    };

    // -------------------------------------------------------------------------

    protected float param_s = 1.70158f;

    public Back s(float s) {
        param_s = s;
        return this;
    }
}