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
package com.guidebee.game.maps.tiled;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.Collidable;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.game.maps.MapProperties;
import com.guidebee.math.geometry.Rectangle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Generalises the concept of tile in a TiledMap
 */
public interface TiledMapTile extends Collidable {

    public enum BlendMode {
        NONE, ALPHA
    }

    public int getId();

    public void setId(int id);

    /**
     * @return the {@link BlendMode} to use for rendering the tile
     */
    public BlendMode getBlendMode();

    /**
     * Sets the {@link BlendMode} to use for rendering the tile
     *
     * @param blendMode the blend mode to use for rendering the tile
     */
    public void setBlendMode(BlendMode blendMode);

    /**
     * @return texture region used to render the tile
     */
    public TextureRegion getTextureRegion();

    /**
     * Sets the texture region used to render the tile
     */
    public void setTextureRegion(TextureRegion textureRegion);

    /**
     * @return the amount to offset the x position when rendering the tile
     */
    public float getOffsetX();

    /**
     * Set the amount to offset the x position when rendering the tile
     */
    public void setOffsetX(float offsetX);

    /**
     * @return the amount to offset the y position when rendering the tile
     */
    public float getOffsetY();

    /**
     * Set the amount to offset the y position when rendering the tile
     */
    public void setOffsetY(float offsetY);

    /**
     * @return tile's properties set
     */
    public MapProperties getProperties();

    /**
     * get user object.
     *
     * @return
     */
    public Object getUserObject();

    /**
     * set user object.
     *
     * @param userObject
     */
    public void setUserObject(Object userObject);


    public void setBoundingRect(Rectangle rect);
}
