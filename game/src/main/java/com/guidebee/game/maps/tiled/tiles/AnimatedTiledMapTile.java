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

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.maps.MapProperties;
import com.guidebee.game.maps.tiled.TiledMapTile;
import com.guidebee.math.geometry.Circle;
import com.guidebee.math.geometry.Polygon;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.TimeUtils;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.LongArray;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Represents a changing
 * {@link com.guidebee.game.maps.tiled.TiledMapTile}.
 */
public class AnimatedTiledMapTile implements TiledMapTile {

    private static long lastTiledMapRenderTime = 0;

    private int id;

    private BlendMode blendMode = BlendMode.ALPHA;

    private MapProperties properties;

    private StaticTiledMapTile[] frameTiles;

    private long[] animationIntervals;
    private long frameCount = 0;
    private long loopDuration;
    private static final long initialTimeOffset = TimeUtils.millis();

    private boolean collisionEnabled = false;

    private Object userObject;

    private Rectangle boundingRect;

    private Polygon boundingPolygon = new Polygon();

    private Circle boundingCircle = new Circle();

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

    private TiledMapTile getCurrentFrame() {
        long currentTime = lastTiledMapRenderTime % loopDuration;

        for (int i = 0; i < animationIntervals.length; ++i) {
            long animationInterval = animationIntervals[i];
            if (currentTime <= animationInterval) return frameTiles[i];
            currentTime -= animationInterval;
        }

        throw new GameEngineRuntimeException(
                "Could not determine current animation frame in AnimatedTiledMapTile. " +
                        " This should never happen.");
    }

    @Override
    public TextureRegion getTextureRegion() {
        return getCurrentFrame().getTextureRegion();
    }

    @Override
    public void setTextureRegion(TextureRegion textureRegion) {
        throw new GameEngineRuntimeException(
                "Cannot set the texture region of AnimatedTiledMapTile.");
    }

    @Override
    public float getOffsetX() {
        return getCurrentFrame().getOffsetX();
    }

    @Override
    public void setOffsetX(float offsetX) {
        throw new GameEngineRuntimeException(
                "Cannot set offset of AnimatedTiledMapTile.");
    }

    @Override
    public float getOffsetY() {
        return getCurrentFrame().getOffsetY();
    }

    @Override
    public void setOffsetY(float offsetY) {
        throw new GameEngineRuntimeException(
                "Cannot set offset of AnimatedTiledMapTile.");
    }

    @Override
    public MapProperties getProperties() {
        if (properties == null) {
            properties = new MapProperties();
        }
        return properties;
    }

    /**
     * Function is called by BatchTiledMapRenderer render(),
     * lastTiledMapRenderTime is used to keep all of the tiles in lock-step
     * animation and avoids having to call TimeUtils.millis() in getTextureRegion()
     */
    public static void updateAnimationBaseTime() {
        lastTiledMapRenderTime = TimeUtils.millis() - initialTimeOffset;
    }

    /**
     * Creates an animated tile with the given animation interval and frame tiles.
     *
     * @param interval   The interval between each individual frame tile.
     * @param frameTiles An array of {@link StaticTiledMapTile}s that make up the animation.
     */
    public AnimatedTiledMapTile(float interval, Array<StaticTiledMapTile> frameTiles) {
        this.frameTiles = new StaticTiledMapTile[frameTiles.size];
        this.frameCount = frameTiles.size;

        this.loopDuration = (long) (frameTiles.size * interval * 1000f);
        this.animationIntervals = new long[frameTiles.size];
        for (int i = 0; i < frameTiles.size; ++i) {
            this.frameTiles[i] = frameTiles.get(i);
            this.animationIntervals[i] = (long) (interval * 1000f);
        }
    }

    /**
     * Creates an animated tile with the given animation intervals and frame tiles.
     *
     * @param intervals  The intervals between each individual frame tile in milliseconds.
     * @param frameTiles An array of {@link StaticTiledMapTile}s that make up the animation.
     */
    public AnimatedTiledMapTile(LongArray intervals, Array<StaticTiledMapTile> frameTiles) {
        this.frameTiles = new StaticTiledMapTile[frameTiles.size];
        this.frameCount = frameTiles.size;

        this.animationIntervals = intervals.toArray();
        this.loopDuration = 0;

        for (int i = 0; i < intervals.size; ++i) {
            this.frameTiles[i] = frameTiles.get(i);
            this.loopDuration += intervals.get(i);
        }
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
