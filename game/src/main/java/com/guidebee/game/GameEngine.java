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
package com.guidebee.game;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.engine.drawing.parser.NumberListParser;
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.game.engine.graphics.opengles.IGL30;
import com.guidebee.game.physics.World;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Environment class holding references to the {@link Application},
 * {@link Graphics}, {@link Audio}, {@link Files} and {@link Input} instances.
 * The references are held in public static fields which allows static access
 * to all sub systems. Do not use Graphics in a thread that is not the rendering
 * thread.
 * <p/>
 * This is normally a design faux pas but in this case is better than
 * the alternatives.
 *
 * @author mzechner
 */
public class GameEngine {

    /**
     * Application instance.
     */
    public static Application app;

    /**
     * Graphics instance.
     */
    public static Graphics graphics;

    /**
     * Audio instance.
     */
    public static Audio audio;

    /**
     * Input instance.
     */
    public static Input input;

    /**
     * Files instance.
     */
    public static Files files;

    /**
     * Net instance.
     */
    public static Net net;

    /**
     * World instance.
     */
    public static World world;

    /**
     * ResourceManager instance.
     */
    public static ResourceManager assetManager;

    /**
     * GL instance depend on current OpenGL ES configuration.
     */
    public static IGL20 gl;

    /**
     * GL20 instance.
     */
    public static IGL20 gl20;

    /**
     * GL20 instance if avaiable.
     */
    public static IGL30 gl30;

    /**
     * pixel to box2d unit ratio
     */
    public static float pixelToBox2DUnit = 32.0f;

    /**
     * Conversion between pixel unit and box2d unit
     * @param pixel
     * @return
     */
    public static float toBox2D(float pixel) {
        return pixel / pixelToBox2DUnit;
    }


    /**
     * To box2d vertices
     * @param vertices
     * @return
     */
    public static float [] toBox2DVertices(float[] vertices){
        for(int i=0;i<vertices.length;i++){
            vertices[i]= toBox2D(vertices[i]);
        }
        return vertices;
    }


    /**
     * to pixel vertices
     * @param vertices
     * @return
     */
    public static float [] toPixelVertices(float[] vertices){
        for(int i=0;i<vertices.length;i++){
            vertices[i]= toPixel(vertices[i]);
        }
        return vertices;
    }

    /**
     * Conversion between box2d unit and pixel unit
     * @param box2d
     * @return
     */
    public static float toPixel(float box2d) {
        return box2d * pixelToBox2DUnit;
    }


    private static final NumberListParser numberListParser = new NumberListParser();

    /**
     * Get vertices from string
     * @param input like "69, -20  , 67, -18  , 56, -16 " .
     * @return
     */
    public static float [] getVerticesFromString(String input){
        synchronized (numberListParser) {
            float[] coords = numberListParser.parseNumberList(input);
            return coords;
        }
    }

}
