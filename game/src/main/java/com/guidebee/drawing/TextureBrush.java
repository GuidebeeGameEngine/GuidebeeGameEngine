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
package com.guidebee.drawing;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.engine.drawing.core.TextureBrushFP;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Defines a brush of a texture (bitmap).
 *
 * @author James Shen.
 */
public class TextureBrush extends Brush {


    /**
     * Default constructor.default color is white.
     */
    public TextureBrush(int[] image, int width, int height) {
        wrappedBrushFP = new TextureBrushFP(image, width, height);
    }


    /**
     * Default constructor.default color is white.
     */
    public TextureBrush(int[] image, int width, int height, int alpha) {
        for (int i = 0; i < image.length; i++) {
            image[i] &= ((alpha & 0xff) << 24 | 0xFFFFFF);
        }
        wrappedBrushFP = new TextureBrushFP(image, width, height);
    }


    /**
     * get the transparency type.
     *
     * @return the transparency type.
     */
    public int getTransparency() {
        return Color.TRANSLUCENT;
    }

}
