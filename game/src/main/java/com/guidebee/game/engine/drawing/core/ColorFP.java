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
 * The <code>ColorFP</code> class is used to encapsulate colors in the default
 * sRGB color space  Every color has an implicit alpha value of 1.0 or
 * an explicit one provided in the constructor.  The alpha value
 * defines the transparency of a color and can be represented by
 * a int value in the range 0&nbsp;-&nbsp;255.
 * An alpha value of  255 means that the color is completely
 * opaque and an alpha value of 0 means that the color is
 * completely transparent.
 * <p/>
 *
 * @author James Shen.
 */
public class ColorFP {


    /**
     * Creates a Color structure from the four 8-bit ARGB components
     * (alpha, red, green, and blue) values.
     *
     * @param color A value specifying the 32-bit ARGB value.
     * @return The Color object that this method creates.
     */
    public static ColorFP fromArgb(int color) {
        return new ColorFP(color);
    }


    /**
     * Creates a Color structure from the four 8-bit ARGB components
     * (alpha, red, green, and blue) values.
     *
     * @param color A value specifying the 32-bit ARGB value.
     * @return The Color object that this method creates.
     */
    public static ColorFP fromArgb(int red, int green, int blue) {
        int value =
                ((red & 0xFF) << 16) |
                        ((green & 0xFF) << 8) |
                        ((blue & 0xFF) << 0);
        return new ColorFP(value);
    }


    /**
     * Creates an opaque sRGB color with the specified combined RGB value
     * consisting of the red component in bits 16-23, the green component
     * in bits 8-15, and the blue component in bits 0-7.  The actual color
     * used in rendering depends on finding the best match given the
     * color space available for a particular output device.  Alpha is
     * defaulted to 255.
     *
     * @param rgb the combined RGB components
     */
    public ColorFP(int rgb) {
        value = rgb;
        red = (value >> 16) & 0xFF;
        green = (value >> 8) & 0xFF;
        blue = value & 0xFF;
        alpha = (value >> 24) & 0xff;
    }


    /**
     * The color value.
     */
    public int value;

    /**
     * the red component.
     */
    public int red;

    /**
     * the green compoent
     */
    public int green;

    /**
     * the blue component.
     */
    public int blue;

    /**
     * the alpha compoent.
     */
    public int alpha;

}
