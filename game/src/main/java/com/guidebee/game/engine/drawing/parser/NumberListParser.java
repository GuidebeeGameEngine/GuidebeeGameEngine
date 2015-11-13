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

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * The <code>NumberListParser</code> class converts attributes
 * conforming to the SVG Tiny definition of coordinate or number
 * list (see <a href="http://www.w3.org/TR/SVG11/types.html#BasicDataTypes">
 * Basic Data Types</a>)..
 *
 * @author James Shen.
 */
public class NumberListParser extends NumberParser {


    /**
     * @param listStr the string containing the list of numbers
     * @param sep     the separator between number values
     * @return An array of numbers
     */
    public float[] parseNumberList(final String listStr, final char sep) {
        setString(listStr);

        current = read();
        skipSpaces();

        boolean requireMore = false;
        float[] numbers = null;
        int cur = 0;
        for (; ; ) {
            if (current != -1) {
                float v = parseNumber(false);
                if (numbers == null) {
                    numbers = new float[1];
                } else if (numbers.length <= cur) {
                    float[] tmpNumbers = new float[numbers.length * 2];
                    System.arraycopy(numbers, 0, tmpNumbers, 0, numbers.length);
                    numbers = tmpNumbers;
                }
                numbers[cur++] = v;
            } else {
                if (!requireMore) {
                    break;
                } else {
                    throw new IllegalArgumentException();
                }
            }
            skipSpaces();
            requireMore = (current == sep);
            skipSepSpaces(sep);
        }

        if (numbers != null && cur != numbers.length) {
            float[] tmpNumbers = new float[cur];
            System.arraycopy(numbers, 0, tmpNumbers, 0, cur);
            numbers = tmpNumbers;
        }

        return numbers;
    }


    /**
     * @param listStr the string containing the list of numbers
     * @return An array of numbers
     */
    public float[] parseNumberList(final String listStr) {
        return parseNumberList(listStr, ',');
    }
}
