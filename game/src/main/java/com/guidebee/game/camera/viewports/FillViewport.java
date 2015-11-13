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
package com.guidebee.game.camera.viewports;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.camera.Camera;
import com.guidebee.utils.Scaling;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A ScalingViewport that uses {@link com.guidebee.utils.Scaling#fill}
 * so it keeps the aspect ratio by scaling the world up to take the whole screen
 * (some of the world may be off screen).
 *
 * @author Daniel Holderbaum
 * @author Nathan Sweet
 */
public class FillViewport extends ScalingViewport {
    /**
     * Creates a new viewport using a
     * new {@link com.guidebee.game.camera.OrthographicCamera}.
     */
    public FillViewport(float worldWidth, float worldHeight) {
        super(Scaling.fill, worldWidth, worldHeight);
    }

    public FillViewport(float worldWidth, float worldHeight, Camera camera) {
        super(Scaling.fill, worldWidth, worldHeight, camera);
    }
}
