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
package com.guidebee.game.engine.drawing.parser;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.drawing.geometry.AffineTransform;
import com.guidebee.game.engine.math.MathEx;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * The <code>TransformListParser</code> class converts attributes
 * conforming to the SVG
 * <a href="http://www.w3.org/TR/SVG11/coords.html#TransformAttribute">
 * transform</a>
 * syntax into <code>AffineTransform</code> objects.
 * <p/>
 * @author James Shen.
 */
public class TransformListParser extends NumberParser {


    /**
     * @param txfStr the string containing the set of transform commands
     * @return An <code>AffineTransform</code> object corresponding to the
     * input transform list.
     */
    public AffineTransform parseTransformList(final String txfStr) {
        setString(txfStr);

        transform = new AffineTransform(1, 0, 0, 1, 0, 0);

        // Parse leading wsp*
        current = read();
        skipSpaces();

        // Now, iterate on 'transforms?'
        loop2:
        for (; ; ) {
            switch (current) {
                case 'm':
                    parseMatrix();
                    break;
                case 'r':
                    parseRotate();
                    break;
                case 't':
                    parseTranslate();
                    break;
                case 's':
                    current = read();
                    switch (current) {
                        case 'c':
                            parseScale();
                            break;
                        case 'k':
                            parseSkew();
                            break;
                        default:
                            throw new IllegalArgumentException();
                    }
                    break;
                case -1:
                    break loop2;
                default:
                    throw new IllegalArgumentException();
            }
            current = read();
            skipCommaSpaces();
        }

        return transform;
    }


    /**
     * Parses a matrix transform. 'm' is assumed to be the current character.
     */
    protected final void parseMatrix() {
        current = read();

        // Parse 'atrix wsp? ( wsp?'
        if (current != 'a') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 't') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'r') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'i') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'x') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();
        if (current != '(') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        float a = parseNumber();
        skipCommaSpaces();
        float b = parseNumber();
        skipCommaSpaces();
        float c = parseNumber();
        skipCommaSpaces();
        float d = parseNumber();
        skipCommaSpaces();
        float e = parseNumber();
        skipCommaSpaces();
        float f = parseNumber();

        skipSpaces();

        if (current != ')') {
            throw new IllegalArgumentException("Expected ')' and got >"
                    + (char) current + "<");
        }

        transform.concatenate(new AffineTransform(a, b, c, d, e, f));
    }


    /**
     * Parses a rotate transform. 'r' is assumed to be the current character.
     */
    protected final void parseRotate() {
        current = read();

        // Parse 'otate wsp? ( wsp?'
        if (current != 'o') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 't') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'a') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 't') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'e') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        if (current != '(') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        float theta = parseNumber();
        skipSpaces();

        switch (current) {
            case ')':
                transform.rotate(theta);
                return;
            case ',':
                current = read();
                skipSpaces();
            default:
                // nothing.
        }

        float cx = parseNumber();
        skipCommaSpaces();
        float cy = parseNumber();

        skipSpaces();
        if (current != ')') {
            throw new IllegalArgumentException();
        }

        transform.translate(cx, cy);
        transform.rotate(theta);
        transform.translate(-cx, -cy);
    }


    /**
     * Parses a translate transform. 't' is assumed to be
     * the current character.
     */
    protected final void parseTranslate() {
        current = read();

        // Parse 'ranslate wsp? ( wsp?'
        if (current != 'r') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'a') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'n') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 's') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'l') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'a') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 't') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'e') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();
        if (current != '(') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        float tx = parseNumber();
        skipSpaces();

        switch (current) {
            case ')':
                transform.translate(tx, 0);
                return;
            case ',':
                current = read();
                skipSpaces();
            default:
                // nothing
        }

        float ty = parseNumber();

        skipSpaces();
        if (current != ')') {
            throw new IllegalArgumentException();
        }

        transform.translate(tx, ty);
    }


    /**
     * Parses a scale transform. 'c' is assumed to be the current character.
     */
    protected final void parseScale() {
        current = read();

        // Parse 'ale wsp? ( wsp?'
        if (current != 'a') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'l') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'e') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();
        if (current != '(') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        float sx = parseNumber();
        skipSpaces();

        switch (current) {
            case ')':
                transform.scale(sx, sx);
                return;
            case ',':
                current = read();
                skipSpaces();
            default:
                // nothing
        }

        float sy = parseNumber();

        skipSpaces();
        if (current != ')') {
            throw new IllegalArgumentException();
        }

        transform.scale(sx, sy);
    }


    /**
     * Parses a skew transform. 'e' is assumed to be the current character.
     */
    protected final void parseSkew() {
        current = read();

        // Parse 'ew[XY] wsp? ( wsp?'
        if (current != 'e') {
            throw new IllegalArgumentException();
        }
        current = read();
        if (current != 'w') {
            throw new IllegalArgumentException();
        }
        current = read();

        boolean skewX = false;
        switch (current) {
            case 'X':
                skewX = true;
            case 'Y':
                break;
            default:
                throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();
        if (current != '(') {
            throw new IllegalArgumentException();
        }
        current = read();
        skipSpaces();

        float sk = parseNumber();

        skipSpaces();
        if (current != ')') {
            throw new IllegalArgumentException();
        }

        float tan = (float) MathEx.tan((double) MathEx.toRadians(sk));

        if (skewX) {
            AffineTransform shear = new AffineTransform(1, 0, tan, 1, 0, 0);
            transform.concatenate(shear);
            // transform.shear(tan, 0);
        } else {
            AffineTransform shear = new AffineTransform(1, tan, 0, 1, 0, 0);
            transform.concatenate(shear);
            // transform.shear(0, tan);
        }
    }

    /**
     * Captures the transform built by this parser
     */
    private AffineTransform transform;

}
