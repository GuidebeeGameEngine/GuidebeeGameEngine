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
package com.guidebee.game.microedition;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.scene.Actor;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A Layer is an abstract class representing a visual element of a game. Each
 * Layer has position (in terms of the upper-left corner of its visual bounds),
 * width, height, and can be made visible or invisible. Layer subclasses must
 * implement a paint(Canvas) method so that they can be rendered.
 * <hr>
 * The Layer's (x,y) position is always interpreted relative to the coordinate
 * system of the Graphics object that is passed to the Layer's paint() method.
 * This coordinate system is referred to as the painter's coordinate system.
 * The initial location of a Layer is (0,0).
 * @author James Shen.
 */
public abstract class Layer extends Actor {

    /**
     * Constructor.
     *
     * @param x       the top left x position
     * @param y       the top left y position
     * @param width   the width of the layer
     * @param height  the height of the layer
     * @param visible visible or not
     */
    protected Layer(float x, float y, float width, float height, boolean visible) {
        setSize(width, height);
        setPosition(x, y);
        setVisible(visible);
    }

    /**
     * Moves this Layer by the specified horizontal and vertical distances.
     *
     * @param dx the distance to move along horizontal axis
     * @param dy the distance to move along vertical axis
     */
    public void move(float dx, float dy) {
        moveBy(dx, dy);
    }

    /**
     * Paints this Layer if it is visible.
     *
     * @param g the graphics object for rendering the Layer
     */
    public abstract void paint(Batch g);


    @Override
    public void draw(Batch batch, float parentAlpha) {
        paint(batch);
    }


}
