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
 * a 2D rectangle class.
 *
 * @author James Shen.
 */
public class RectangleFP {


    /**
     * return the bottom.
     *
     * @return
     */
    public int getBottom() {
        return ff_ymax;
    }


    /**
     * return the top.
     *
     * @return
     */
    public int getTop() {
        return ff_ymin;
    }


    /**
     * return the left.
     *
     * @return
     */
    public int getLeft() {
        return ff_xmin;
    }


    /**
     * return the right.
     *
     * @return
     */
    public int getRight() {
        return ff_xmax;
    }


    /**
     * get the width.
     *
     * @return
     */
    public int getWidth() {
        return ff_xmax - ff_xmin;
    }


    /**
     * set the width.
     *
     * @param value
     */
    public void setWidth(int value) {
        if (value < 0) {
            return;
        }
        ff_xmax = ff_xmin + value;
    }


    /**
     * get the height.
     *
     * @return
     */
    public int getHeight() {
        return ff_ymax - ff_ymin;
    }


    /**
     * set the height.
     *
     * @param value
     */
    public void setHeight(int value) {

        if (value < 0) {
            return;
        }
        ff_ymax = ff_ymin + value;
    }


    /**
     * get the X.
     *
     * @return
     */
    public int getX() {
        return ff_xmin;
    }


    /**
     * Set the X.
     *
     * @param value
     */
    public void setX(int value) {
        ff_xmin = value;
    }


    /**
     * Get the Y.
     *
     * @return
     */
    public int getY() {
        return ff_ymin;
    }


    /**
     * Set the Y.
     *
     * @param value
     */
    public void setY(int value) {
        ff_ymin = value;

    }

    /**
     * The empty rectangle.
     */
    public static final RectangleFP EMPTY = new RectangleFP();
    private int ff_xmin;
    private int ff_xmax;
    private int ff_ymin;
    private int ff_ymax;


    /**
     * Default constructor.
     */
    public RectangleFP() {
        ff_xmin = ff_xmax = ff_ymin = ff_ymax = SingleFP.NaN;
    }


    /**
     * Copy constructor.
     *
     * @param r
     */
    public RectangleFP(RectangleFP r) {
        reset(r);
    }


    /**
     * Constructor.
     *
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     */
    public RectangleFP(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        reset(ff_xmin, ff_ymin, ff_xmax, ff_ymax);
    }


    /**
     * Reset the rectangle.
     *
     * @param ff_xmin
     * @param ff_ymin
     * @param ff_xmax
     * @param ff_ymax
     * @return
     */
    public RectangleFP reset(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax) {
        this.ff_xmin = MathFP.min(ff_xmin, ff_xmax);
        this.ff_xmax = MathFP.max(ff_xmin, ff_xmax);
        this.ff_ymin = MathFP.min(ff_ymin, ff_ymax);
        this.ff_ymax = MathFP.max(ff_ymin, ff_ymax);
        return this;
    }


    /**
     * reset the rectangle.
     *
     * @param r
     * @return
     */
    public RectangleFP reset(RectangleFP r) {
        return reset(r.ff_xmin, r.ff_ymin, r.ff_xmax, r.ff_ymax);
    }


    /**
     * Check if the rectangle is empty.
     *
     * @return
     */
    public boolean isEmpty() {
        return ff_xmin == SingleFP.NaN || ff_xmax == SingleFP.NaN ||
                ff_ymin == SingleFP.NaN || ff_ymax == SingleFP.NaN;
    }


    /**
     * Translate the rectangle.
     *
     * @param ff_dx
     * @param ff_dy
     */
    public void offset(int ff_dx, int ff_dy) {
        if (!isEmpty()) {
            ff_xmin += ff_dx;
            ff_xmax += ff_dx;
            ff_ymin += ff_dy;
            ff_ymax += ff_dy;
        }
    }


    /**
     * Calculate the union of the two rectangle.
     *
     * @param r
     * @return
     */
    public RectangleFP union(RectangleFP r) {
        if (!r.isEmpty()) {
            if (isEmpty()) {
                reset(r);
            } else {
                reset(MathFP.min(ff_xmin, r.ff_xmin),
                        MathFP.max(ff_xmax, r.ff_xmax),
                        MathFP.min(ff_ymin, r.ff_ymin),
                        MathFP.max(ff_ymax, r.ff_ymax));
            }
        }
        return this;
    }


    /**
     * return the union of the rectangle and the given point.
     *
     * @param p
     * @return
     */
    public RectangleFP union(PointFP p) {
        if (!isEmpty()) {
            reset(MathFP.min(ff_xmin, p.x), MathFP.max(ff_xmax, p.x),
                    MathFP.min(ff_ymin, p.y), MathFP.max(ff_ymax, p.y));
        }
        return this;
    }


    /**
     * Check to see this rectange intersect with given rectange.
     *
     * @param r
     * @return
     */
    public boolean intersectsWith(RectangleFP r) {
        return ff_xmin <= r.ff_xmax && r.ff_xmin <= ff_xmax &&
                ff_ymin <= r.ff_ymax && r.ff_ymin <= ff_ymax;
    }


    /**
     * Check to see if this rectangle contains given point.
     *
     * @param p
     * @return
     */
    public boolean contains(PointFP p) {
        return ff_xmin <= p.x && p.x <= ff_xmax
                && ff_ymin <= p.y && p.y <= ff_ymax;
    }


    /**
     * Check to see two rect has same location.
     *
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (o instanceof RectangleFP) {
            RectangleFP r = (RectangleFP) o;
            if (r == null) {
                return false;
            } else {
                return r.ff_xmax == ff_xmax && r.ff_xmin == ff_xmin
                        && r.ff_ymax == ff_ymax && r.ff_ymin == ff_ymin;
            }
        }
        return false;
    }


    /**
     * Returns the hashcode for this <code>Rectangle</code>.
     *
     * @return the hashcode for this <code>Rectangle</code>.
     */
    public int hashCode() {
        int bits = ff_xmin & 0xFFFF0000 + ff_ymin & 0x0000FFFF;

        return bits;
    }


    /**
     * to string.
     *
     * @return
     */
    public String toString() {
        return "Rectangle" + " (" + new SingleFP(ff_xmin) + "," +
                new SingleFP(ff_ymin) + ")-(" + new SingleFP(ff_xmax) + "," +
                new SingleFP(ff_ymax) + ")";
    }
}