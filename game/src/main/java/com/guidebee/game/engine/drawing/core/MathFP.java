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
 * Fixed point float math.
 *
 * @author James Shen.
 */
public class MathFP {

    /**
     * PI.
     */
    public final static int PI = 205887;

    /**
     * E
     */
    public final static int E = 178145;


    /**
     * return the minimun of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static int min(int x, int y) {
        return x < y ? x : y;
    }


    /**
     * return the maximum of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static int max(int x, int y) {
        return x > y ? x : y;
    }


    /**
     * return the abs of the value.
     *
     * @param x
     * @return
     */
    public static int abs(int x) {
        return x < 0 ? -x : x;
    }


    /**
     * return the product of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static int mul(int x, int y) {
        long res = (long) x * (long) y >> SingleFP.DECIMAL_BITS;
        return (int) res;
    }


    /**
     * return the division of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static int div(int x, int y) {
        long res = ((long) x << SingleFP.DECIMAL_BITS) / (long) y;
        return (int) res;
    }


    /**
     * return the square root of the given value.
     *
     * @param n
     * @return
     */
    public static int sqrt(int n) {
        int s;
        if (n < (1000 << SingleFP.DECIMAL_BITS)) {
            s = n / 20;
        } else if (n < (2500 << SingleFP.DECIMAL_BITS)) {
            s = n / 40;
        } else if (n < (5000 << SingleFP.DECIMAL_BITS)) {
            s = n / 60;
        } else if (n < (10000 << SingleFP.DECIMAL_BITS)) {
            s = n / 86;
        } else if (n < (25000 << SingleFP.DECIMAL_BITS)) {
            s = n / 132;
        } else {
            s = n / 168;
        }

        s = (s + div(n, s)) >> 1;
        s = (s + div(n, s)) >> 1;
        s = (s + div(n, s)) >> 1;
        return s;
    }


    /**
     * calculat the IEEE Reminder.
     *
     * @param n
     * @param m
     * @return
     */
    public static int IEEERemainder(int n, int m) {
        return n - mul(floor(div(n, m)), m);
    }


    /**
     * calculate the floor of the value.
     *
     * @param x
     * @return
     */
    public static int floor(int x) {
        return x < 0 && (-x & 0xFFFF) != 0 ?
                -((-x + SingleFP.ONE >> SingleFP.DECIMAL_BITS) << SingleFP.DECIMAL_BITS)
                : ((x >> SingleFP.DECIMAL_BITS) << SingleFP.DECIMAL_BITS);
    }


    /**
     * Calculate the round the value.
     *
     * @param x
     * @return
     */
    public static int round(int x) {
        if (x < 0) {
            return -(((-x + SingleFP.ONE / 2) >> SingleFP.DECIMAL_BITS)
                    << SingleFP.DECIMAL_BITS);
        } else {
            return ((x + SingleFP.ONE / 2) >> SingleFP.DECIMAL_BITS)
                    << SingleFP.DECIMAL_BITS;
        }
    }


    /**
     * to degree.
     *
     * @param f
     * @return
     */
    public static int toDegrees(int f) {
        return div(f * 180, PI);
    }


    /**
     * to radians.
     *
     * @param f
     * @return
     */
    public static int toRadians(int f) {
        return mul(f, PI) / 180;
    }


    /**
     * calculate of the sine.
     *
     * @param f
     * @return
     */
    public static int sin(int f) {
        if (f < 0 || f >= PI * 2) {
            f = IEEERemainder(f, PI * 2);
        }
        int sign = 1;
        if ((f > PI / 2) && (f <= PI)) {
            f = PI - f;
        } else if ((f > PI) && (f <= (PI + PI / 2))) {
            f = f - PI;
            sign = -1;
        } else if (f > (PI + PI / 2)) {
            f = (PI << 1) - f;
            sign = -1;
        }

        int sqr = mul(f, f);
        int result = 498;
        result = mul(result, sqr);
        result -= 10882;
        result = mul(result, sqr);
        result += (1 << SingleFP.DECIMAL_BITS);
        result = mul(result, f);
        return sign * result;
    }


    /**
     * calculate the cosine.
     *
     * @param ff_ang
     * @return
     */
    public static int cos(int ff_ang) {
        return sin(PI / 2 - ff_ang);
    }


    /**
     * return the tan.
     *
     * @param f
     * @return
     */
    public static int tan(int f) {
        return div(sin(f), cos(f));
    }


    /**
     * return the atan.
     *
     * @param ff_val
     * @return
     */
    public static int atan(int ff_val) {
        int ff_val1 = ff_val > SingleFP.ONE ? div(SingleFP.ONE, ff_val)
                : (ff_val < -SingleFP.ONE ? div(SingleFP.ONE, -ff_val) : ff_val);
        int sqr = mul(ff_val1, ff_val1);
        int result = 1365;
        result = mul(result, sqr);
        result -= 5579;
        result = mul(result, sqr);
        result += 11805;
        result = mul(result, sqr);
        result -= 21646;
        result = mul(result, sqr);
        result += 65527;
        result = mul(result, ff_val1);
        return ff_val > SingleFP.ONE ? PI / 2 - result
                : (ff_val < -SingleFP.ONE ? -(PI / 2 - result) : result);
    }


    /**
     * return the asin.
     *
     * @param f
     * @return
     */
    public static int asin(int f) {
        return PI / 2 - acos(f);
    }


    /**
     * return the acosine.
     *
     * @param f
     * @return
     */
    public static int acos(int f) {
        int fRoot = sqrt(SingleFP.ONE - f);
        int result = -1228;
        result = mul(result, f);
        result += 4866;
        result = mul(result, f);
        result -= 13901;
        result = mul(result, f);
        result += 102939;
        result = mul(fRoot, result);
        return result;
    }


    /**
     * return the minimum of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static long min(long x, long y) {
        return x < y ? x : y;
    }


    /**
     * return the maximum of the two values.
     *
     * @param x
     * @param y
     * @return
     */
    public static long max(long x, long y) {
        return x > y ? x : y;
    }


    /**
     * return the abs of the value.
     *
     * @param x
     * @return
     */
    public static long abs(long x) {
        return x < 0 ? -x : x;
    }
}
