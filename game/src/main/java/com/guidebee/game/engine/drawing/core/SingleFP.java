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
 * Single is a fix point single class.
 *
 * @author James Shen.
 */
public class SingleFP {

    /**
     * Positive Infinity.
     */
    private final static int POSITIVE_INFINITY = Integer.MAX_VALUE;

    /**
     * Negative infinity.
     */
    private final static int NEGATIVE_INFINITY = Integer.MIN_VALUE;

    /**
     * Max value.
     */
    public final static int MAX_VALUE = POSITIVE_INFINITY - 1;

    /**
     * Min value.
     */
    public final static int MIN_VALUE = NEGATIVE_INFINITY + 2;

    /**
     * Not a number.
     */
    public final static int NaN = NEGATIVE_INFINITY + 1;

    /**
     * Fix point length.
     */
    public final static int DECIMAL_BITS = 16;

    /**
     * the number 1 in this fix point float.
     */
    public final static int ONE = 1 << DECIMAL_BITS;

    /**
     * int format for this single.
     */
    private int value;


    /**
     * Counstructor.
     *
     * @param v the integer format for this fixed point number.
     */
    public SingleFP(int v) {
        value = v;
    }


    /**
     * copy constructor.
     *
     * @param f a fixed point float.
     */
    public SingleFP(SingleFP f) {
        value = f.value;
    }


    /**
     * Test if it's NaN.
     *
     * @param x check if the input is a NaN
     * @return true, if it's NaN.
     */
    public static boolean isNaN(int x) {
        return x == NaN;
    }


    /**
     * Test if it's infinity.
     *
     * @param x the fixed point number to be tested.
     * @return true it's positive or negitive infinity.
     */
    public static boolean isInfinity(int x) {
        return x == NEGATIVE_INFINITY || x == POSITIVE_INFINITY;
    }


    /**
     * Test if it's negative infinity.
     *
     * @param x the fixed point number to be tested.
     * @return true it's  negitive infinity.
     */
    public static boolean isNegativeInfinity(int x) {
        return x == NEGATIVE_INFINITY;
    }


    /**
     * Test if it's positive infinity.
     *
     * @param x the fixed point number to be tested.
     * @return true it's  positive infinity.
     */
    public static boolean isPositiveInfinity(int x) {
        return x == POSITIVE_INFINITY;
    }


    /**
     * convert a float to this fixed point float.
     *
     * @param f a float number
     * @return a fixed point number.
     */
    public static int fromFloat(float f) {
        return (int) (f * ONE + 0.5);
    }


    /**
     * convert a double to this fixed point float.
     *
     * @param f a double number
     * @return a fixed point number.
     */
    public static int fromDouble(double f) {
        return (int) (f * ONE + 0.5);
    }


    /**
     * Convert this fixed point float to a float.
     *
     * @param x a fixed point number
     * @return a float.
     */
    public static float toFloat(int x) {
        return (float) x / ONE;
    }


    /**
     * Convert this fixed point float to a double.
     *
     * @param x a fixed point number
     * @return a double
     */
    public static double toDouble(int x) {
        return (double) x / ONE;
    }


    /**
     * Convert an integer to the fixed point float.
     *
     * @param x a integer
     * @return a fixed point number.
     */
    public static int fromInt(int x) {
        return x << DECIMAL_BITS;
    }


    /**
     * Convert the fixed point float back to an integer.
     *
     * @param ff_x the fixed point number
     * @return an integer.
     */
    public static int toInt(int ff_x) {
        return ff_x >> DECIMAL_BITS;
    }


    /**
     * Parse an string can convert it to fixed point float.
     *
     * @param strValue a string reprents a float.
     * @return the fixed point number.
     */
    public static SingleFP parseSingle(String strValue) {
        String s = strValue;
        boolean e_neg = false;
        int v, e = 0;
        int posE = s.indexOf((char) 'E');
        if (posE == -1) {
            posE = s.indexOf((char) 'e');
        }
        if (posE != -1) {
            e = Integer.parseInt(s.substring(posE + 1));
            if (e < 0) {
                e_neg = true;
                e = -e;
            }
            s = s.substring(0, (posE) - (0));
        }
        int posDot = s.indexOf((char) '.');
        if (posDot == -1) {
            v = Integer.parseInt(s);
            v = v << DECIMAL_BITS;
        } else {
            v = Integer.parseInt(s.substring(0, (posDot) - (0))) << DECIMAL_BITS;
            s = s.substring(posDot + 1);
            s = s + "0000";
            s = s.substring(0, (4) - (0));
            int f = Integer.parseInt(s);
            f = (f << DECIMAL_BITS) / 10000;
            if (v < 0) {
                v -= f;
            } else {
                v += f;
            }
        }
        for (int i = 0; i < e; i++) {
            if (e_neg) {
                v /= 10;
            } else {
                v *= 10;
            }
        }
        return new SingleFP(v);
    }


    /**
     * to string format.
     *
     * @return a string repents the fixed point number.
     */
    public String toString() {
        String s = "";
        int v = value;
        if (v < 0) {
            s = "-";
            v = -v;
        }
        s = s + (v >> DECIMAL_BITS);
        v = 0xFFFF & v;
        if (v != 0) {
            s = s + ".";
        }
        //while (v != 0)
        for (int i = 0; i < 4; i++) {
            v = v * 10;
            s = s + (v >> DECIMAL_BITS);
            v = 0xFFFF & v;
        }
        return s;
    }
}