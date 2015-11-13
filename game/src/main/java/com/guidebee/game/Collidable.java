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
package com.guidebee.game;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Polygon;
import com.guidebee.math.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Collidable interface. game object that can collide with other can implement
 * this interface, then game engine may notify when collision happens.
 */
public interface Collidable {


    /**
     * use bounding rectangle(AABB) for collision checking.
     */
    public final int BOUNDING_RECT = 0x1;

    /**
     * use bounding area(polygon or other shapes) for collision checking.
     */
    public final int BOUNDING_AREA = 0x2;

    /**
     * use bounding circle for collision checking.
     */
    public final int BOUNDING_CIRCLE = 0x04;

    /**
     * use box2d world for collison checking.
     */
    public final int BOX2D_CONTACT = 0x08;

    /**
     * is collision enabled or not for this object.
     * @return
     */
    public boolean isEnabled();

    /**
     * Get the colliable object's name
     * @return
     */
    public String getName();


    /**
     * get bounding AABB rectangle
     * @return bounding rectangle(AABB)
     */
    public Rectangle getBoundingAABB();

    /**
     * get bounding polygon.
     * @return bounding polygon (usually smaller than AABB rectangle)
     */
    public Polygon getBoundingPolygon();

    /**
     * get bounding circle.
     * @return the bounding circle.
     */
    public Circle getBoundingCircle();
}
