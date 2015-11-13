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
package com.guidebee.game.maps.objects;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.maps.MapObject;
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Represents {@link com.guidebee.math.geometry.Circle} shaped map
 * objects
 */
public class CircleMapObject extends MapObject {

    private Circle circle;

    /**
     * @return circle shape
     */
    public Circle getCircle() {
        return circle;
    }

    /**
     * Creates a circle map object at (0,0) with r=1.0
     */
    public CircleMapObject() {
        this(0.0f, 0.0f, 1.0f);
    }

    /**
     * Creates a circle map object
     *
     * @param x      X coordinate
     * @param y      Y coordinate
     * @param radius Radius of the circle object.
     */
    public CircleMapObject(float x, float y, float radius) {
        super();
        circle = new Circle(x, y, radius);
        Rectangle rect=new Rectangle(x-radius/2f,y-radius/2.0f,radius,radius);
        setBoundingRect(rect);
    }


}
