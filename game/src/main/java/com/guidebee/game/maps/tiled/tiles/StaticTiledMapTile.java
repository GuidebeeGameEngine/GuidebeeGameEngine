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
package com.guidebee.game.maps.tiled.tiles;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.maps.MapProperties;
import com.guidebee.game.maps.tiled.TiledMapTile;
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Polygon;
import com.guidebee.math.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Represents a non changing {@link TiledMapTile} (can be cached)
 */
public class StaticTiledMapTile implements TiledMapTile {

    private int id;

    private BlendMode blendMode = BlendMode.ALPHA;

    private MapProperties properties;

    private TextureRegion textureRegion;

    private float offsetX;

    private float offsetY;

    private Object userObject;

    private Rectangle boundingRect;

    private Polygon boundingPolygon = new Polygon();

    private Circle boundingCircle = new Circle();

    private boolean collisionEnabled = false;

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public BlendMode getBlendMode() {
        return blendMode;
    }

    @Override
    public void setBlendMode(BlendMode blendMode) {
        this.blendMode = blendMode;
    }

    @Override
    public MapProperties getProperties() {
        if (properties == null) {
            properties = new MapProperties();
        }
        return properties;
    }

    @Override
    public TextureRegion getTextureRegion() {
        return textureRegion;
    }

    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    @Override
    public float getOffsetX() {
        return offsetX;
    }

    @Override
    public void setOffsetX(float offsetX) {
        this.offsetX = offsetX;
    }

    @Override
    public float getOffsetY() {
        return offsetY;
    }

    @Override
    public void setOffsetY(float offsetY) {
        this.offsetY = offsetY;
    }

    /**
     * Creates a static tile with the given region
     *
     * @param textureRegion the {@link TextureRegion} to use.
     */
    public StaticTiledMapTile(TextureRegion textureRegion) {
        this.textureRegion = textureRegion;
    }

    /**
     * Copy constructor
     *
     * @param copy the StaticTiledMapTile to copy.
     */
    public StaticTiledMapTile(StaticTiledMapTile copy) {
        if (copy.properties != null) {
            getProperties().putAll(copy.properties);
        }
        this.textureRegion = copy.textureRegion;
        this.id = copy.id;
    }

    /**
     * Retrieves application specific object for convenience.
     */
    public Object getUserObject() {
        return userObject;
    }


    /**
     * Sets an application specific object for convenience.
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }

    @Override
    public void setBoundingRect(Rectangle rect) {
        boundingRect = rect;

    }

    public void setCollisionEnabled(Boolean value) {
        collisionEnabled = value;
    }

    @Override
    public boolean isEnabled() {
        return collisionEnabled;
    }

    @Override
    public String getName() {
        return getClass().getName();
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
