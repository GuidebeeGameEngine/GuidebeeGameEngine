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
package com.guidebee.game.engine.drawing.core;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Defines a brush of a single color. Brushes are used to fill graphics shapes,
 * such as rectangles, ellipses, pies, polygons, and paths. This class cannot
 * be inherited.
 *
 * @author James Shen.
 */
public final class SolidBrushFP extends BrushFP {


    /**
     * @inheritDoc Always return true.
     */
    public boolean isMonoColor() {
        return true;
    }


    /**
     * Default constructor.default color is white.
     */
    public SolidBrushFP() {
    }


    /**
     * Constructor.
     *
     * @param color the color for this solid brush.
     */
    public SolidBrushFP(int color) {
        this.color = color;
    }


    /**
     * @param x           the x coordinate
     * @param y           the y coordinate
     * @param singlePoint single point
     * @return the color value at given location.
     * @inheritDoc
     */
    public int getColorAt(int x, int y, boolean singlePoint) {
        return color;
    }


    /**
     * @return next color.
     * @inheritDoc
     */
    public int getNextColor() {
        return color;
    }

    /**
     * The color for this solid brush.
     */
    private int color = 0xFFFFFFFF;
}
