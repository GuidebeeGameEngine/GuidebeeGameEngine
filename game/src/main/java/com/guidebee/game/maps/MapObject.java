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
package com.guidebee.game.maps;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.Collidable;
import com.guidebee.game.graphics.Color;
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Polygon;
import com.guidebee.math.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Generic Map entity with basic attributes like name, opacity, color
 */
public abstract class MapObject implements Collidable {
    private String name = "";
    private float opacity = 1.0f;
    private boolean visible = true;
    private MapProperties properties = new MapProperties();
    private Color color = Color.WHITE.cpy();
    private boolean collisionEnabled = false;

    private Rectangle boundingRect;

    private Polygon boundingPolygon = new Polygon();

    private Circle boundingCircle = new Circle();


    /**
     * is collision enabled or not for this object.
     * @return
     */
    public void setEnabled(boolean enabled){
        collisionEnabled=enabled;
    }

    @Override
    public boolean isEnabled() {
        return collisionEnabled;
    }

    /**
     * @return object's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name new name for the object
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return object's color
     */
    public Color getColor() {
        return color;
    }

    /**
     * @param color new color for the object
     */
    public void setColor(Color color) {
        this.color = color;
    }

    /**
     * @return object's opacity
     */
    public float getOpacity() {
        return opacity;
    }

    /**
     * @param opacity new opacity value for the object
     */
    public void setOpacity(float opacity) {
        this.opacity = opacity;
    }

    /**
     * @return whether the object is visible or not
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * @param visible toggles object's visibility
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    /**
     * @return object's properties set
     */
    public MapProperties getProperties() {
        return properties;
    }


    public void setBoundingRect(Rectangle rect) {
        boundingRect = rect;

    }

    @Override
    public Rectangle getBoundingAABB() {

        return boundingRect;
    }

    @Override
    public Polygon getBoundingPolygon() {

        if (boundingRect != null) {
            float[] vertices = new float[]{boundingRect.x, boundingRect.y,
                    boundingRect.x + boundingRect.width, boundingRect.y,
                    boundingRect.x + boundingRect.width,
                    boundingRect.y + boundingRect.height, boundingRect.y,
                    boundingRect.y + boundingRect.height};
            boundingPolygon.setVertices(vertices);
            return boundingPolygon;
        }
        return null;
    }

    @Override
    public Circle getBoundingCircle() {
        if (boundingRect != null) {
            boundingCircle.setPosition(boundingRect.x + boundingRect.width / 2,
                    boundingRect.y + boundingRect.height / 2);
            boundingCircle.setRadius(Math.min(boundingRect.width / 2, boundingRect.height / 2));
            return boundingCircle;

        }
        return null;
    }
}
