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
public final class TextureBrushFP extends BrushFP {


    /**
     * @inheritDoc Always return true.
     */
    public boolean isMonoColor() {
        return false;
    }


    /**
     * Default constructor.default color is white.
     */
    public TextureBrushFP(int[] image, int width, int height) {
        textureBuffer = new int[image.length];
        System.arraycopy(image, 0, textureBuffer, 0, textureBuffer.length);
        this.width = width;
        this.height = height;

    }


    /**
     * @param x           the x coordinate
     * @param y           the y coordinate
     * @param singlePoint single point
     * @return the color value at given location.
     * @inheritDoc
     */
    public int getColorAt(int x, int y, boolean singlePoint) {
        PointFP p = new PointFP(x << SingleFP.DECIMAL_BITS,
                y << SingleFP.DECIMAL_BITS);
        nextPt.x = p.x + SingleFP.ONE;
        nextPt.y = p.y;
        if (finalMatrix != null) {
            p.transform(finalMatrix);

        }
        int xPos = (p.x >> SingleFP.DECIMAL_BITS) % width;
        int yPos = (p.y >> SingleFP.DECIMAL_BITS) % height;

        if (xPos < 0) xPos += width;
        if (yPos < 0) yPos += height;

        return textureBuffer[(xPos + yPos * width)];
    }


    /**
     * @return next color.
     * @inheritDoc
     */
    public int getNextColor() {
        PointFP p = new PointFP(nextPt);

        nextPt.x += SingleFP.ONE;
        nextPt.y = p.y;

        if (finalMatrix != null) {
            p.transform(finalMatrix);

        }
        int xPos = (p.x >> SingleFP.DECIMAL_BITS) % width;
        int yPos = (p.y >> SingleFP.DECIMAL_BITS) % height;

        if (xPos < 0) xPos += width;
        if (yPos < 0) yPos += height;

        return textureBuffer[xPos + yPos * width];
    }


    /**
     * the width of the texture
     */
    private int width = 1;

    /**
     * the height of the texture brush
     */
    private int height = 1;

    /**
     * the texture buffer
     */
    private int[] textureBuffer = null;

    /**
     * next point position.
     */
    private final PointFP nextPt = new PointFP(0, 0);
}
