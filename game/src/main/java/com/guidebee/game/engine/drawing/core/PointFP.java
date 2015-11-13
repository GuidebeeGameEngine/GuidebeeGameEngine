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
 * a 2D point class.
 *
 * @author James Shen.
 */
public class PointFP {

    /**
     * X coordinate.
     */
    public int x = 0;

    /**
     * Y coordinate.
     */
    public int y = 0;

    /**
     * the (0,0) point.
     */
    public static final PointFP ORIGIN = new PointFP(0, 0);

    /**
     * Empty point.
     */
    public static final PointFP EMPTY = new PointFP(SingleFP.NaN, SingleFP.NaN);


    /**
     * Default constructor.
     */
    public PointFP() {
    }


    /**
     * Copy constructor.
     *
     * @param p
     */
    public PointFP(PointFP p) {
        reset(p);
    }


    /**
     * constructor.
     *
     * @param ff_x
     * @param ff_y
     */
    public PointFP(int ff_x, int ff_y) {
        reset(ff_x, ff_y);
    }


    /**
     * Check to see if the point is empty one.
     *
     * @param p
     * @return
     */
    public static boolean isEmpty(PointFP p) {
        return EMPTY.equals(p);
    }


    /**
     * reset the point to the same location as the given point.
     *
     * @param p
     * @return
     */
    public PointFP reset(PointFP p) {
        return reset(p.x, p.y);
    }


    /**
     * reset the point to give location.
     *
     * @param ff_x
     * @param ff_y
     * @return
     */
    public PointFP reset(int ff_x, int ff_y) {
        this.x = ff_x;
        this.y = ff_y;
        return this;
    }


    /**
     * transform the point with give matrix.
     *
     * @param m
     * @return
     */
    public PointFP transform(MatrixFP m) {
        reset(MathFP.mul(x, m.scaleX) + MathFP.mul(y, m.rotateY) + m.translateX,
                MathFP.mul(y, m.scaleY) + MathFP.mul(x, m.rotateX) + m.translateY);
        return this;
    }


    /**
     * the distance between 2 points.
     *
     * @param p1
     * @param p2
     * @return
     */
    static public int distance(PointFP p1, PointFP p2) {
        return distance(p1.x - p2.x, p1.y - p2.y);
    }


    /**
     * calculate the distance.
     *
     * @param dx
     * @param dy
     * @return
     */
    static public int distance(int dx, int dy) {
        dx = MathFP.abs(dx);
        dy = MathFP.abs(dy);
        if (dx == 0) {
            return dy;
        } else if (dy == 0) {
            return dx;
        }

        long len = (((long) dx * dx) >> SingleFP.DECIMAL_BITS)
                + (((long) dy * dy) >> SingleFP.DECIMAL_BITS);
        long s = (dx + dy) - (MathFP.min(dx, dy) >> 1);
        s = (s + ((len << SingleFP.DECIMAL_BITS) / s)) >> 1;
        s = (s + ((len << SingleFP.DECIMAL_BITS) / s)) >> 1;
        return (int) s;
    }


    /**
     * Add given point the location to this point.
     *
     * @param p
     * @return
     */
    public PointFP add(PointFP p) {
        reset(x + p.x, y + p.y);
        return this;
    }


    /**
     * substract given distance (x,y) to this point.
     *
     * @param p
     * @return
     */
    public PointFP sub(PointFP p) {
        reset(x - p.x, y - p.y);
        return this;
    }


    /**
     * Check to see the two point are equal.
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj instanceof PointFP) {
            PointFP p = (PointFP) obj;
            if (p != null) {
                return x == p.x && y == p.y;
            } else {
                return false;
            }
        }
        return false;
    }


    /**
     * Returns the hashcode for this <code>Point</code>.
     *
     * @return a hash code for this <code>Point</code>.
     */
    public int hashCode() {
        int bits = (x << 16) & 0xFFFF0000;
        bits ^= y;
        return (int) bits;
    }


    /**
     * convert to a string.
     *
     * @return
     */
    public String toString() {
        return "Point(" + new SingleFP(x) + "," + new SingleFP(y) + ")";
    }
}
