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
package com.guidebee.drawing.geometry;

//--------------------------------- IMPORTS ------------------------------------
import java.util.NoSuchElementException;
/**
 * A utility class to iterate over the path segments of an ellipse
 * through the IPathIterator interface.
 *
 * @author James Shen.
 */
class EllipseIterator implements IPathIterator {

    double x, y, w, h;
    AffineTransform affine;
    int index;


    /**
     * @param e
     * @param at
     */
    EllipseIterator(Ellipse e, AffineTransform at) {
        this.x = e.getX();
        this.y = e.getY();
        this.w = e.getWidth();
        this.h = e.getHeight();
        this.affine = at;
        if (w < 0 || h < 0) {
            index = 6;
        }
    }


    /**
     * Return the winding rule for determining the insideness of the
     * path.
     *
     * @see #WIND_EVEN_ODD
     * @see #WIND_NON_ZERO
     */
    public int getWindingRule() {
        return WIND_NON_ZERO;
    }


    /**
     * Tests if there are more points to read.
     *
     * @return true if there are more points to read
     */
    public boolean isDone() {
        return index > 5;
    }


    /**
     * Moves the iterator to the next segment of the path forwards
     * along the primary direction of traversal as long as there are
     * more points in that direction.
     */
    public void next() {
        index++;
    }    // ArcIterator.btan(Math.PI/2)

    public static final double CtrlVal = 0.5522847498307933;

    /*
     * ctrlpts contains the control points for a set of 4 cubic
     * bezier curves that approximate a circle of radius 0.5
     * centered at 0.5, 0.5
     */
    private static final double pcv = 0.5 + CtrlVal * 0.5;
    private static final double ncv = 0.5 - CtrlVal * 0.5;
    private static double ctrlpts[][] = {
            {1.0, pcv, pcv, 1.0, 0.5, 1.0},
            {ncv, 1.0, 0.0, pcv, 0.0, 0.5},
            {0.0, ncv, ncv, 0.0, 0.5, 0.0},
            {pcv, 0.0, 1.0, ncv, 1.0, 0.5}
    };


    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A int array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of int x,y coordinates.
     * SEG_MOVETO and SEG_LINETO types will return one point,
     * SEG_QUADTO will return two points,
     * SEG_CUBICTO will return 3 points
     * and SEG_CLOSE will not return any points.
     *
     * @see #SEG_MOVETO
     * @see #SEG_LINETO
     * @see #SEG_QUADTO
     * @see #SEG_CUBICTO
     * @see #SEG_CLOSE
     */
    public int currentSegment(int[] coords) {
        if (isDone()) {
            throw new NoSuchElementException("ellipse iterator out of bounds");
        }
        if (index == 5) {
            return SEG_CLOSE;
        }
        if (index == 0) {
            double ctrls[] = ctrlpts[3];
            coords[0] = (int) (x + ctrls[4] * w + .5);
            coords[1] = (int) (y + ctrls[5] * h + .5);
            if (affine != null) {
                affine.transform(coords, 0, coords, 0, 1);
            }
            return SEG_MOVETO;
        }
        double ctrls[] = ctrlpts[index - 1];
        coords[0] = (int) (x + ctrls[0] * w + .5);
        coords[1] = (int) (y + ctrls[1] * h + .5);
        coords[2] = (int) (x + ctrls[2] * w + .5);
        coords[3] = (int) (y + ctrls[3] * h + .5);
        coords[4] = (int) (x + ctrls[4] * w + .5);
        coords[5] = (int) (y + ctrls[5] * h + .5);
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 3);
        }
        return SEG_CUBICTO;
    }
}
