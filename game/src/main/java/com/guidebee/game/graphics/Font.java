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

import com.guidebee.utils.collections.FloatArray;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * interface for fonts. Font can be bitmap font or vector font.
 */
public interface Font {


    /**
     * Draws a string at the specified position.
     *
     * @see BitmapFontCache#addText(CharSequence, float, float, int, int)
     */
    public TextBounds draw(Batch batch, CharSequence str, float x, float y);

    /**
     * Draws a string at the specified position.
     *
     * @see BitmapFontCache#addText(CharSequence, float, float, int, int)
     */
    public TextBounds draw(Batch batch, CharSequence str, float x, float y, int start, int end);

    /**
     * Draws a string, which may contain newlines (\n), at the specified position.
     *
     * @see BitmapFontCache#addMultiLineText(CharSequence, float, float, float, com.guidebee.game.graphics.Font.HAlignment)
     */
    public TextBounds drawMultiLine(Batch batch, CharSequence str, float x, float y);

    /**
     * Draws a string, which may contain newlines (\n), at the specified position.
     *
     * @see BitmapFontCache#addMultiLineText(CharSequence, float, float, float, HAlignment)
     */
    public TextBounds drawMultiLine(Batch batch, CharSequence str, float x, float y,
                                    float alignmentWidth, HAlignment alignment);


    /**
     * Draws a string, which may contain newlines (\n), with the specified position.
     * Each line is automatically wrapped within the
     * specified width.
     *
     * @see BitmapFontCache#addWrappedText(CharSequence, float, float, float, com.guidebee.game.graphics.Font.HAlignment)
     */
    public TextBounds drawWrapped(Batch batch, CharSequence str, float x, float y, float wrapWidth);


    /**
     * Draws a string, which may contain newlines (\n), with the specified position.
     * Each line is automatically wrapped within the
     * specified width.
     *
     * @see BitmapFontCache#addWrappedText(CharSequence, float, float, float, com.guidebee.game.graphics.Font.HAlignment)
     */
    public TextBounds drawWrapped(Batch batch, CharSequence str, float x, float y,
                                  float wrapWidth, HAlignment alignment);

    /**
     * Returns the bounds of the specified text. Note the returned TextBounds
     * instance is reused.
     *
     * @see #getBounds(CharSequence, int, int, com.guidebee.game.graphics.Font.TextBounds)
     */
    public TextBounds getBounds(CharSequence str);

    /**
     * Returns the bounds of the specified text.
     *
     * @see #getBounds(CharSequence, int, int, com.guidebee.game.graphics.Font.TextBounds)
     */
    public TextBounds getBounds(CharSequence str, TextBounds textBounds);

    /**
     * Returns the bounds of the specified text. Note the returned TextBounds
     * instance is reused.
     *
     * @see #getBounds(CharSequence, int, int, com.guidebee.game.graphics.Font.TextBounds)
     */
    public TextBounds getBounds(CharSequence str, int start, int end);

    /**
     * Returns the size of the specified string. The height is the distance from
     * the top of most capital letters in the font (the
     *
     * @param start The first character of the string.
     * @param end   The last character of the string (exclusive).
     */
    public TextBounds getBounds(CharSequence str, int start, int end,
                                TextBounds textBounds);


    /**
     * Returns the bounds of the specified text, which may contain newlines.
     *
     * @see #getMultiLineBounds(CharSequence, com.guidebee.game.graphics.Font.TextBounds)
     */
    public TextBounds getMultiLineBounds(CharSequence str);

    /**
     * Returns the bounds of the specified text, which may contain newlines.
     * The height is the distance from the top of most
     * capital letters in the font.
     * to the baseline of the last line of text.
     */
    public TextBounds getMultiLineBounds(CharSequence str, TextBounds textBounds);

    /**
     * Returns the bounds of the specified text, which may contain newlines
     * and is wrapped within the specified width.
     *
     * @see #getWrappedBounds(CharSequence, float, com.guidebee.game.graphics.Font.TextBounds)
     */
    public TextBounds getWrappedBounds(CharSequence str, float wrapWidth);

    /**
     * Returns the bounds of the specified text, which may contain newlines
     * and is wrapped within the specified width. The height
     * is the distance from the top of most capital letters in the font
     * to the baseline of
     * the last line of text.
     *
     * @param wrapWidth Width to wrap the bounds within.
     */
    public TextBounds getWrappedBounds(CharSequence str, float wrapWidth,
                                       TextBounds textBounds);

    /**
     * Computes the glyph advances for the given character sequence and stores
     * them in the provided {@link FloatArray}. The float
     * arrays are cleared. An additional element is added at the end.
     *
     * @param glyphAdvances  the glyph advances output array.
     * @param glyphPositions the glyph positions output array.
     */
    public void computeGlyphAdvancesAndPositions(CharSequence str, FloatArray glyphAdvances,
                                                 FloatArray glyphPositions);


    /**
     * Returns the number of glyphs from the substring that can be rendered in the
     * specified width.
     *
     * @param start The first character of the string.
     * @param end   The last character of the string (exclusive).
     */
    public int computeVisibleGlyphs(CharSequence str, int start, int end,
                                    float availableWidth);

    /**
     * set font color.
     *
     * @param color
     */
    public void setColor(float color);

    /**
     * set font color.
     *
     * @param color
     */
    public void setColor(Color color);

    /**
     * set font color.
     *
     * @param r
     * @param g
     * @param b
     * @param a
     */
    public void setColor(float r, float g, float b, float a);

    /**
     * Returns the color of this font. Changing the returned color will have
     * no affect, {@link #setColor(Color)} or
     * {@link #setColor(float, float, float, float)} must be used.
     */
    public Color getColor();

    /**
     * Scales the font by the specified amounts on both axes <br>
     * <br>
     * Note that smoother scaling can be achieved if the texture backing the
     * BitmapFont is using {@link com.guidebee.game.graphics.Texture.TextureFilter#Linear}.
     * The
     * default is Nearest, so use a BitmapFont constructor that takes a {@link TextureRegion}.
     *
     * @throws IllegalArgumentException When scaleXY is zero
     */
    public void setScale(float scaleX, float scaleY);

    /**
     * Scales the font by the specified amount in both directions.
     *
     * @throws IllegalArgumentException When scaleXY is zero
     * @see #setScale(float, float)
     */
    public void setScale(float scaleXY);

    /**
     * Sets the font's scale relative to the current scale.
     *
     * @throws IllegalArgumentException When resulting scale is zero
     * @see #setScale(float, float)
     */
    public void scale(float amount);


    /**
     * get scale x.
     *
     * @return
     */
    public float getScaleX();

    /**
     * get scale y.
     *
     * @return
     */
    public float getScaleY();

    /**
     * Checks whether this BitmapFont data contains a given character.
     */
    public boolean containsCharacter(char character);


    /**
     * Defines possible horizontal alignments.
     */
    enum HAlignment {
        LEFT, CENTER, RIGHT
    }


    /**
     * Arbitrarily definable text boundary
     */
    class TextBounds {
        public float width;
        public float height;

        public TextBounds() {
        }

        public TextBounds(TextBounds bounds) {
            set(bounds);
        }

        public void set(TextBounds bounds) {
            width = bounds.width;
            height = bounds.height;
        }
    }
}
