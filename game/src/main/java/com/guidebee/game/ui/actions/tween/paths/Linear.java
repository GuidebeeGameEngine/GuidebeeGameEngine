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
package com.guidebee.game.ui.actions.tween.paths;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.ui.actions.tween.TweenPath;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Linear path.
 * @author Aurelien Ribon | http://www.aurelienribon.com/
 */
public class Linear implements TweenPath {
    @Override
    public float compute(float t, float[] points, int pointsCnt) {
        int segment = (int) Math.floor((pointsCnt - 1) * t);
        segment = Math.max(segment, 0);
        segment = Math.min(segment, pointsCnt - 2);

        t = t * (pointsCnt - 1) - segment;

        return points[segment] + t * (points[segment + 1] - points[segment]);
    }
}
