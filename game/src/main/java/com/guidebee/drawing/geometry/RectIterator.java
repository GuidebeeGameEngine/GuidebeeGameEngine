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

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A utility class to iterate over the path segments of a rectangle
 * through the IPathIterator interface.
 * <p/>
 * @author James Shen.
 */
class RectIterator implements IPathIterator {

    int x, y, w, h;
    AffineTransform affine;
    int index;


    /**
     * Constructor
     *
     * @param r
     * @param at
     */
    RectIterator(Rectangle r, AffineTransform at) {
        this.x = r.getX();
        this.y = r.getY();
        this.w = r.getWidth();
        this.h = r.getHeight();
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
    }


    /**
     * Returns the coordinates and type of the current path segment in
     * the iteration.
     * The return value is the path segment type:
     * SEG_MOVETO, SEG_LINETO, SEG_QUADTO, SEG_CUBICTO, or SEG_CLOSE.
     * A long array of length 6 must be passed in and may be used to
     * store the coordinates of the point(s).
     * Each point is stored as a pair of long x,y coordinates.
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
            throw new NoSuchElementException("rect iterator out of bounds");
        }
        if (index == 5) {
            return SEG_CLOSE;
        }
        coords[0] = x;
        coords[1] = y;
        if (index == 1 || index == 2) {
            coords[0] += w;
        }
        if (index == 2 || index == 3) {
            coords[1] += h;
        }
        if (affine != null) {
            affine.transform(coords, 0, coords, 0, 1);
        }
        return (index == 0 ? SEG_MOVETO : SEG_LINETO);
    }
}
