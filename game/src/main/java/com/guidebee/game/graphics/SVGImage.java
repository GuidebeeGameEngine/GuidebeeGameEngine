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
package com.guidebee.game.graphics;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.platform.svg.SVG;
import com.guidebee.game.files.FileHandle;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * SVG images.
 */
public class SVGImage {

    final SVG svg;

    /**
     * Constructor .
     *
     * @param file svg file name.
     */
    public SVGImage(FileHandle file) {
        try {
            svg = SVG.getFromInputStream(file.read());
        } catch (Exception e) {
            throw new GameEngineRuntimeException("Couldn't load file: " + file, e);
        }
    }

    /**
     * get svg document/viewport width.
     *
     * @return
     */
    public float getWidth() {
        return svg.getDocumentWidth();
    }

    /**
     * get SVG document/viwport height.
     *
     * @return
     */
    public float getHeight() {
        return svg.getDocumentHeight();
    }

    /**
     * Convert to pixmap with given width and height.
     *
     * @param width
     * @param height
     * @return
     */
    public Pixmap getPixmap(int width, int height) {
        int[] rgbData = svg.getRGB(width, height);
        Pixmap pixmap = new Pixmap(width, height, Pixmap.Format.RGBA8888);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int mask = rgbData[y * width + x] & 0xFFFFFFFF;
                int aa = mask >> 24 & 0xff;
                int rr = mask >> 16 & 0xff;
                int gg = mask >> 8 & 0xff;
                int bb = mask & 0xff;

                int color = ((aa & 0xFF) << 24) |
                        ((gg & 0xFF) << 16) |
                        ((rr & 0xFF) << 8) |
                        ((bb & 0xFF) << 0);

                pixmap.drawPixel(x, y, color);
            }
        }
        return pixmap;

    }


    /**
     * Convert to pixmap with given scale.
     *
     * @param scale
     * @return
     */
    public Pixmap getPixmap(float scale) {
        int width = (int) (getWidth() * scale);
        int height = (int) (getHeight() * scale);
        return getPixmap(width, height);
    }
}
