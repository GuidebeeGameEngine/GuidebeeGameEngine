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

/**
 * A <code>Rectangle</code> specifies an area in a coordinate space that is
 * enclosed by the <code>Rectangle</code> object's upper-left point
 * {@code (x, y)}
 * in the coordinate space, its width, and its height.
 * <p/>
 * A <code>Rectangle</code> object's <code>width</code> and
 * <code>height</code> are <code>public</code> fields. The constructors
 * that create a <code>Rectangle</code>, and the methods that can modify
 * one, do not prevent setting a negative value for width or height.
 * <p/>
 * <a name="Empty">
 * A {@code Rectangle} whose width or height is exactly zero has location
 * along those axes with zero dimension, but is otherwise considered empty.
 * The {@link #isEmpty} method will return true for such a {@code Rectangle}.
 * Methods which test if an empty {@code Rectangle} contains or intersects
 * a point or rectangle will always return false if either dimension is zero.
 * Methods which combine such a {@code Rectangle} with a point or rectangle
 * will include the location of the {@code Rectangle} on that axis in the
 * result as if the {@link #add(Point)} method were being called.
 * </a>
 * <p/>
 * <a name="NonExistant">
 * A {@code Rectangle} whose width or height is negative has neither
 * location nor dimension along those axes with negative dimensions.
 * Such a {@code Rectangle} is treated as non-existant along those axes.
 * Such a {@code Rectangle} is also empty with respect to containment
 * calculations and methods which test if it contains or intersects a
 * point or rectangle will always return false.
 * Methods which combine such a {@code Rectangle} with a point or rectangle
 * will ignore the {@code Rectangle} entirely in generating the result.
 * If two {@code Rectangle} objects are combined and each has a negative
 * dimension, the result will have at least one negative dimension.
 * </a>
 * <p/>
 * Methods which affect only the location of a {@code Rectangle} will
 * operate on its location regardless of whether or not it has a negative
 * or zero dimension along either axis.
 * <p/>
 * Note that a {@code Rectangle} constructed with the default no-argument
 * constructor will have dimensions of {@code 0x0} and therefore be empty.
 * That {@code Rectangle} will still have a location of {@code (0, 0)} and
 * will contribute that location to the union and add operations.
 * Code attempting to accumulate the bounds of a set of points should
 * therefore initially construct the {@code Rectangle} with a specifically
 * negative width and height or it should use the first point in the set
 * to construct the {@code Rectangle}.
 * For example:
 * <pre>
 *     Rectangle bounds = new Rectangle(0, 0, -1, -1);
 *     for (int i = 0; i < points.length; i++) {
 *         bounds.add(points[i]);
 *     }
 * </pre>
 * or if we know that the points array contains at least one point:
 * <pre>
 *     Rectangle bounds = new Rectangle(points[0]);
 *     for (int i = 1; i < points.length; i++) {
 *         bounds.add(points[i]);
 *     }
 * </pre>
 * <p/>
 * This class uses 32-bit integers to store its location and dimensions.
 * Frequently operations may produce a result that exceeds the range of
 * a 32-bit integer.
 * The methods will calculate their results in a way that avoids any
 * 32-bit overflow for intermediate results and then choose the best
 * representation to store the final results back into the 32-bit fields
 * which hold the location and dimensions.
 * The location of the result will be stored into the {@link #x} and
 * {@link #y} fields by clipping the true result to the nearest 32-bit value.
 * The values stored into the {@link #width} and {@link #height} dimension
 * fields will be chosen as the 32-bit values that encompass the largest
 * part of the true result as possible.
 * Generally this means that the dimension will be clipped independently
 * to the range of 32-bit integers except that if the location had to be
 * moved to store it into its pair of 32-bit fields then the dimensions
 * will be adjusted relative to the "best representation" of the location.
 * If the true result had a negative dimension and was therefore
 * non-existant along one or both axes, the stored dimensions will be
 * negative numbers in those axes.
 * If the true result had a location that could be represented within
 * the range of 32-bit integers, but zero dimension along one or both
 * axes, then the stored dimensions will be zero in those axes.
 * <p/>
 *
 * @author James Shen.
 */
public class Rectangle extends RectangularShape implements IShape {

    /**
     * The bitmask that indicates that a point lies to the left of
     * this <code>Rectangle</code>.
     */
    public static final int OUT_LEFT = 1;
    /**
     * The bitmask that indicates that a point lies above
     * this <code>Rectangle</code>.
     */
    public static final int OUT_TOP = 2;
    /**
     * The bitmask that indicates that a point lies to the right of
     * this <code>Rectangle</code>.
     */
    public static final int OUT_RIGHT = 4;
    /**
     * The bitmask that indicates that a point lies below
     * this <code>Rectangle</code>.
     */
    public static final int OUT_BOTTOM = 8;
    /**
     * The X coordinate of the upper-left corner of the <code>Rectangle</code>.
     */
    public int x;
    /**
     * The Y coordinate of the upper-left corner of the <code>Rectangle</code>.
     */
    public int y;
    /**
     * The width of the <code>Rectangle</code>.
     */
    public int width;
    /**
     * The height of the <code>Rectangle</code>.
     */
    public int height;
    private Dimension size;


    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are both zero.
     */
    public Rectangle() {
        this(0, 0, 0, 0);
    }


    /**
     * Constructs a new <code>Rectangle</code>, initialized to match
     * the values of the specified <code>Rectangle</code>.
     *
     * @param r the <code>Rectangle</code> from which to copy initial values
     *          to a newly constructed <code>Rectangle</code>
     */
    public Rectangle(Rectangle r) {
        this(r.x, r.y, r.width, r.height);
    }


    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is
     * specified as
     * {@code (x, y)} and whose width and height
     * are specified by the arguments of the same name.
     *
     * @param x      the specified X coordinate
     * @param y      the specified Y coordinate
     * @param width  the width of the <code>Rectangle</code>
     * @param height the height of the <code>Rectangle</code>
     */
    public Rectangle(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.size = new Dimension(width, height);

    }


    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner
     * is at (0,&nbsp;0) in the coordinate space, and whose width and
     * height are specified by the arguments of the same name.
     *
     * @param width  the width of the <code>Rectangle</code>
     * @param height the height of the <code>Rectangle</code>
     */
    public Rectangle(int width, int height) {
        this(0, 0, width, height);
    }


    /**
     * Constructs a new <code>Rectangle</code> with given two points.
     *
     * @param p1 one corner point of the rectangle.
     * @param p2 one corner point of the rectangle.
     */
    public Rectangle(Point p1, Point p2) {

        this(Math.min(p1.x, p2.x),
                Math.min(p1.y, p2.y),
                Math.abs(p1.x - p2.x),
                Math.abs(p1.y - p2.y));
    }

    /**
     * Creates a new instance of Rectangle at position (x, y) and with
     * predefine dimension
     *
     * @param x the x coordinate of the rectangle
     * @param y the y coordinate of the rectangle
     * @param d the {@link Dimension} of the rectangle
     */
    public Rectangle(int x, int y, Dimension d) {
        this(x, y, d.width, d.height);
    }


    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is
     * specified by the {@link Point} argument, and
     * whose width and height are specified by the
     * {@link Dimension} argument.
     *
     * @param p a <code>Point</code> that is the upper-left corner of
     *          the <code>Rectangle</code>
     * @param d a <code>Dimension</code>, representing the
     *          width and height of the <code>Rectangle</code>
     */
    public Rectangle(Point p, Dimension d) {
        this(p.x, p.y, d.width, d.height);
    }


    /**
     * Constructs a new <code>Rectangle</code> whose upper-left corner is the
     * specified <code>Point</code>, and whose width and height are both zero.
     *
     * @param p a <code>Point</code> that is the top left corner
     *          of the <code>Rectangle</code>
     */
    public Rectangle(Point p) {
        this(p.x, p.y, 0, 0);
    }


    /**
     * Constructs a new <code>Rectangle</code> whose top left corner is
     * (0,&nbsp;0) and whose width and height are specified
     * by the <code>Dimension</code> argument.
     *
     * @param d a <code>Dimension</code>, specifying width and height
     */
    public Rectangle(Dimension d) {
        this(0, 0, d.width, d.height);
    }


    /**
     * Sets this <code>Rectangle</code> to be the same as the specified
     * <code>Rectangle</code>.
     *
     * @param r the specified <code>Rectangle</code>
     */
    public void setRect(Rectangle r) {
        setRect(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }


    /**
     * Sets the bounds of this {@code Rectangle} to the integer bounds
     * which encompass the specified {@code x}, {@code y}, {@code width},
     * and {@code height}.
     * If the parameters specify a {@code Rectangle} that exceeds the
     * maximum range of integers, the result will be the best
     * representation of the specified {@code Rectangle} intersected
     * with the maximum integer bounds.
     *
     * @param x      the X coordinate of the upper-left corner of
     *               the specified rectangle
     * @param y      the Y coordinate of the upper-left corner of
     *               the specified rectangle
     * @param width  the width of the specified rectangle
     * @param height the new height of the specified rectangle
     */
    public void setRect(int x, int y, int width, int height) {
        int newx, newy, neww, newh;

        if (x > Integer.MAX_VALUE / 2) {
            // Too far in positive X direction to represent...
            // We cannot even reach the left side of the specified
            // rectangle even with both x & width set to MAX_VALUE.
            // The intersection with the "maximal integer rectangle"
            // is non-existant so we should use a width < 0.
            // REMIND: Should we try to determine a more "meaningful"
            // adjusted value for neww than just "-1"?
            newx = Integer.MAX_VALUE / 2;
            neww = -1;
        } else {
            newx = clip(x, false);
            if (width >= 0) {
                width += x - newx;
            }
            neww = clip(width, width >= 0);
        }

        if (y > Integer.MAX_VALUE) {
            // Too far in positive Y direction to represent...
            newy = Integer.MAX_VALUE / 2;
            newh = -1;
        } else {
            newy = clip(y, false);
            if (height >= 0) {
                height += y - newy;
            }
            newh = clip(height, height >= 0);
        }

        reshape(newx, newy, neww, newh);
    }


    /**
     * Tests if the specified line segment intersects the interior of this
     * <code>Rectangle</code>.
     *
     * @param x1 the X coordinate of the start point of the specified
     *           line segment
     * @param y1 the Y coordinate of the start point of the specified
     *           line segment
     * @param x2 the X coordinate of the end point of the specified
     *           line segment
     * @param y2 the Y coordinate of the end point of the specified
     *           line segment
     * @return <code>true</code> if the specified line segment intersects
     * the interior of this <code>Rectangle</code>; <code>false</code>
     * otherwise.
     */
    public boolean intersectsLine(int x1, int y1, int x2, int y2) {
        int out1, out2;
        if ((out2 = outcode(x2, y2)) == 0) {
            return true;
        }
        while ((out1 = outcode(x1, y1)) != 0) {
            if ((out1 & out2) != 0) {
                return false;
            }
            if ((out1 & (OUT_LEFT | OUT_RIGHT)) != 0) {
                int xp = getX();
                if ((out1 & OUT_RIGHT) != 0) {
                    xp += getWidth();
                }
                y1 = y1 + (xp - x1) * (y2 - y1) / (x2 - x1);
                x1 = xp;
            } else {
                int yp = getY();
                if ((out1 & OUT_BOTTOM) != 0) {
                    yp += getHeight();
                }
                x1 = x1 + (yp - y1) * (x2 - x1) / (y2 - y1);
                y1 = yp;
            }
        }
        return true;
    }


    /**
     * Tests if the specified line segment intersects the interior of this
     * <code>Rectangle</code>.
     *
     * @param l the specified {@link Line} to test for intersection
     *          with the interior of this <code>Rectangle</code>
     * @return <code>true</code> if the specified <code>Line</code>
     * intersects the interior of this <code>Rectangle</code>;
     * <code>false</code> otherwise.
     */
    public boolean intersectsLine(Line l) {
        return intersectsLine(l.getX1(), l.getY1(), l.getX2(), l.getY2());
    }


    /**
     * Determines where the specified {@link Point} lies with
     * respect to this <code>Rectangle</code>.
     * This method computes a binary OR of the appropriate mask values
     * indicating, for each side of this <code>Rectangle</code>,
     * whether or not the specified <code>Point</code> is on the same
     * side of the edge as the rest of this <code>Rectangle</code>.
     *
     * @param p the specified <code>Point</code>
     * @return the logical OR of all appropriate out codes.
     */
    public int outcode(Point p) {
        return outcode(p.getX(), p.getY());
    }


    /**
     * Sets the location and size of the outer bounds of this
     * <code>Rectangle</code> to the specified rectangular values.
     *
     * @param x the X coordinate of the upper-left corner
     *          of this <code>Rectangle</code>
     * @param y the Y coordinate of the upper-left corner
     *          of this <code>Rectangle</code>
     * @param w the width of this <code>Rectangle</code>
     * @param h the height of this <code>Rectangle</code>
     */
    public void setFrame(int x, int y, int w, int h) {
        setRect(x, y, w, h);
    }


    /**
     * Checks whether or not this Rectangle contains the point at the specified
     * location (rX, rY).
     *
     * @param rX the specified x coordinate
     * @param rY the specified y coordinate
     * @return true if the point (rX, rY) is inside this Rectangle;
     * false otherwise.
     */
    public boolean contains(int rX, int rY) {
        return inside(rX, rY);

    }

    /**
     * Determines whether or not this Rectangle and the specified Rectangle
     * location (x, y) with the specified dimensions (width, height),
     * intersect. Two rectangles intersect if their intersection is nonempty.
     *
     * @param x      the specified x coordinate
     * @param y      the specified y coordinate
     * @param width  the width of the Rectangle
     * @param height the height of the Rectangle
     * @return true if the specified Rectangle and this Rectangle intersect;
     * false otherwise.
     */
    public boolean intersects(int x, int y, int width, int height) {
        int tw = size.getWidth();
        int th = size.getHeight();
        return intersects(this.x, this.y, tw, th, x, y, width, height);
    }

    /**
     * Determines whether or not this Rectangle and the specified Rectangle
     * location (x, y) with the specified dimensions (width, height),
     * intersect. Two rectangles intersect if their intersection is nonempty.
     *
     * @param rect the Rectangle to check intersection with
     * @return true if the specified Rectangle and this Rectangle intersect;
     * false otherwise.
     */
    public boolean intersects(Rectangle rect) {
        return intersects(rect.getX(), rect.getY(),
                rect.getSize().getWidth(), rect.getSize().getHeight());
    }

    /**
     * Helper method allowing us to determine if two coordinate sets intersect. This saves
     * us the need of creating a rectangle object for a quick calculation
     *
     * @param tx     x of first rectangle
     * @param ty     y of first rectangle
     * @param tw     width of first rectangle
     * @param th     height of first rectangle
     * @param x      x of second rectangle
     * @param y      y of second rectangle
     * @param width  width of second rectangle
     * @param height height of second rectangle
     * @return true if the rectangles intersect
     */
    public static boolean intersects(int tx, int ty, int tw, int th, int x, int y, int width, int height) {
        int rw = width;
        int rh = height;
        if (rw <= 0 || rh <= 0 || tw <= 0 || th <= 0) {
            return false;
        }
        int rx = x;
        int ry = y;
        rw += rx;
        rh += ry;
        tw += tx;
        th += ty;
        return ((rw < rx || rw > tx) &&
                (rh < ry || rh > ty) &&
                (tw < tx || tw > rx) &&
                (th < ty || th > ry));

    }


    /**
     * Checks whether this Rectangle entirely contains the Rectangle
     * at the specified location (rX, rY) with the specified
     * dimensions (rWidth, rHeight).
     *
     * @param rX      the specified x coordinate
     * @param rY      the specified y coordinate
     * @param rWidth  the width of the Rectangle
     * @param rHeight the height of the Rectangle
     * @return true if the Rectangle specified by (rX, rY, rWidth, rHeight)
     * is entirely enclosed inside this Rectangle; false otherwise.
     */
    public boolean contains(int rX, int rY, int rWidth, int rHeight) {
        return x <= rX && y <= rY && x + getWidth() >= rX + rWidth &&
                y + getHeight() >= rY + rHeight;
    }


    /**
     * Intersects the pair of specified source <code>Rectangle</code>
     * objects and puts the result into the specified destination
     * <code>Rectangle</code> object.  One of the source rectangles
     * can also be the destination to avoid creating a third Rectangle
     * object, but in this case the original points of this source
     * rectangle will be overwritten by this method.
     *
     * @param src1 the first of a pair of <code>Rectangle</code>
     *             objects to be intersected with each other
     * @param src2 the second of a pair of <code>Rectangle</code>
     *             objects to be intersected with each other
     * @param dest the <code>Rectangle</code> that holds the
     *             results of the intersection of <code>src1</code> and
     *             <code>src2</code>
     */
    public static void intersect(Rectangle src1,
                                 Rectangle src2,
                                 Rectangle dest) {
        int x1 = Math.max(src1.getMinX(), src2.getMinX());
        int y1 = Math.max(src1.getMinY(), src2.getMinY());
        int x2 = Math.min(src1.getMaxX(), src2.getMaxX());
        int y2 = Math.min(src1.getMaxY(), src2.getMaxY());
        dest.setFrame(x1, y1, x2 - x1, y2 - y1);
    }


    /**
     * Unions the pair of source <code>Rectangle</code> objects
     * and puts the result into the specified destination
     * <code>Rectangle</code> object.  One of the source rectangles
     * can also be the destination to avoid creating a third Rectangle
     * object, but in this case the original points of this source
     * rectangle will be overwritten by this method.
     *
     * @param src1 the first of a pair of <code>Rectangle</code>
     *             objects to be combined with each other
     * @param src2 the second of a pair of <code>Rectangle</code>
     *             objects to be combined with each other
     * @param dest the <code>Rectangle</code> that holds the
     *             results of the union of <code>src1</code> and
     *             <code>src2</code>
     */
    public static void union(Rectangle src1,
                             Rectangle src2,
                             Rectangle dest) {
        int x1 = Math.min(src1.getMinX(), src2.getMinX());
        int y1 = Math.min(src1.getMinY(), src2.getMinY());
        int x2 = Math.max(src1.getMaxX(), src2.getMaxX());
        int y2 = Math.max(src1.getMaxY(), src2.getMaxY());
        dest.setFrameFromDiagonal(x1, y1, x2, y2);
    }


    /**
     * Adds a point, specified by the int precision arguments
     * <code>newx</code> and <code>newy</code>, to this
     * <code>Rectangle</code>.  The resulting <code>Rectangle</code>
     * is the smallest <code>Rectangle</code> that
     * contains both the original <code>Rectangle</code> and the
     * specified point.
     * <p/>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code> method does not
     * return <code>true</code> for points on the right or bottom
     * edges of a rectangle. Therefore, if the added point falls on
     * the left or bottom edge of the enlarged rectangle,
     * <code>contains</code> returns <code>false</code> for that point.
     *
     * @param newx the X coordinate of the new point
     * @param newy the Y coordinate of the new point
     */
    public void add(int newx, int newy) {
        int x1 = Math.min(getMinX(), newx);
        int x2 = Math.max(getMaxX(), newx);
        int y1 = Math.min(getMinY(), newy);
        int y2 = Math.max(getMaxY(), newy);
        setRect(x1, y1, x2 - x1, y2 - y1);
    }


    /**
     * Adds the <code>Point</code> object <code>pt</code> to this
     * <code>Rectangle</code>.
     * The resulting <code>Rectangle</code> is the smallest
     * <code>Rectangle</code> that contains both the original
     * <code>Rectangle</code> and the specified <code>Point</code>.
     * <p/>
     * After adding a point, a call to <code>contains</code> with the
     * added point as an argument does not necessarily return
     * <code>true</code>. The <code>contains</code>
     * method does not return <code>true</code> for points on the right
     * or bottom edges of a rectangle. Therefore, if the added point falls
     * on the left or bottom edge of the enlarged rectangle,
     * <code>contains</code> returns <code>false</code> for that point.
     *
     * @param pt the new <code>Point</code> to add to this
     *           <code>Rectangle</code>.
     */
    public void add(Point pt) {
        add(pt.x, pt.y);
    }


    /**
     * Adds a <code>Rectangle</code> object to this
     * <code>Rectangle</code>.  The resulting <code>Rectangle</code>
     * is the union of the two <code>Rectangle</code> objects.
     *
     * @param r the <code>Rectangle</code> to add to this
     *          <code>Rectangle</code>.
     */
    public void add(Rectangle r) {
        int x1 = Math.min(getMinX(), r.getMinX());
        int x2 = Math.max(getMaxX(), r.getMaxX());
        int y1 = Math.min(getMinY(), r.getMinY());
        int y2 = Math.max(getMaxY(), r.getMaxY());
        setRect(x1, y1, x2 - x1, y2 - y1);
    }


    /**
     * Returns an iteration object that defines the boundary of this
     * <code>Rectangle</code>.
     * The iterator for this class is multi-threaded safe, which means
     * that this <code>Rectangle</code> class guarantees that
     * modifications to the geometry of this <code>Rectangle</code>
     * object do not affect any iterations of that geometry that
     * are already in process.
     *
     * @param at an optional <code>AffineTransform</code> to be applied to
     *           the coordinates as they are returned in the iteration, or
     *           <code>null</code> if untransformed coordinates are desired
     * @return the <code>IPathIterator</code> object that returns the
     * geometry of the outline of this
     * <code>Rectangle</code>, one segment at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at) {
        return new RectIterator(this, at);
    }


    /**
     * Returns an iteration object that defines the boundary of the
     * flattened <code>Rectangle</code>.  Since rectangles are already
     * flat, the <code>flatness</code> parameter is ignored.
     * The iterator for this class is multi-threaded safe, which means
     * that this <code>Rectangle</code> class guarantees that
     * modifications to the geometry of this <code>Rectangle</code>
     * object do not affect any iterations of that geometry that
     * are already in process.
     *
     * @param at       an optional <code>AffineTransform</code> to be applied to
     *                 the coordinates as they are returned in the iteration, or
     *                 <code>null</code> if untransformed coordinates are desired
     * @param flatness the maximum distance that the line segments used to
     *                 approximate the curved segments are allowed to deviate from any
     *                 point on the original curve.  Since rectangles are already flat,
     *                 the <code>flatness</code> parameter is ignored.
     * @return the <code>IPathIterator</code> object that returns the
     * geometry of the outline of this
     * <code>Rectangle</code>, one segment at a time.
     */
    public IPathIterator getPathIterator(AffineTransform at, int flatness) {
        return new RectIterator(this, at);
    }


    /**
     * Returns the hashcode for this <code>Rectangle</code>.
     *
     * @return the hashcode for this <code>Rectangle</code>.
     */
    public int hashCode() {
        int bits = x & 0xFFFF0000 + y & 0x0000FFFF;

        return bits;
    }


    /**
     * Determines whether or not the specified <code>Object</code> is
     * equal to this <code>Rectangle</code>.  The specified
     * <code>Object</code> is equal to this <code>Rectangle</code>
     * if it is an instance of <code>Rectangle</code> and if its
     * location and size are the same as this <code>Rectangle</code>.
     *
     * @param obj an <code>Object</code> to be compared with this
     *            <code>Rectangle</code>.
     * @return <code>true</code> if <code>obj</code> is an instance
     * of <code>Rectangle</code> and has
     * the same values; <code>false</code> otherwise.
     */
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (obj instanceof Rectangle) {
            Rectangle r2d = (Rectangle) obj;
            return ((x == r2d.x) &&
                    (x == r2d.y) &&
                    (width == r2d.width) &&
                    (height == r2d.height));
        }
        return false;
    }


    /**
     * Returns the X coordinate of the bounding <code>Rectangle</code> in
     * <code>int</code> precision.
     *
     * @return the X coordinate of the bounding <code>Rectangle</code>.
     */
    public int getX() {
        return x;
    }


    /**
     * Returns the Y coordinate of the bounding <code>Rectangle</code> in
     * <code>int</code> precision.
     *
     * @return the Y coordinate of the bounding <code>Rectangle</code>.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the x position of the rectangle
     *
     * @param x the x coordinate of the rectangle
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * Sets the y position of the rectangle
     *
     * @param y the y coordinate of the rectangle
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Returns the width of the bounding <code>Rectangle</code> in
     * <code>int</code> precision.
     *
     * @return the width of the bounding <code>Rectangle</code>.
     */
    public int getWidth() {
        return width;
    }


    /**
     * Returns the height of the bounding <code>Rectangle</code> in
     * <code>int</code> precision.
     *
     * @return the height of the bounding <code>Rectangle</code>.
     */
    public int getHeight() {
        return height;
    }


    /**
     * Gets the bounding <code>Rectangle</code> of this <code>Rectangle</code>.
     *
     * @return a new <code>Rectangle</code>, equal to the
     * bounding <code>Rectangle</code> for this <code>Rectangle</code>.
     */
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }


    /**
     * Sets the bounding <code>Rectangle</code> of this <code>Rectangle</code>
     * to match the specified <code>Rectangle</code>.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     *
     * @param r the specified <code>Rectangle</code>
     */
    public void setBounds(Rectangle r) {
        setFrame(r.x, r.y, r.width, r.height);
    }


    /**
     * Sets the bounding <code>Rectangle</code> of this
     * <code>Rectangle</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setBounds</code> method of <code>Component</code>.
     *
     * @param x      the new X coordinate for the upper-left
     *               corner of this <code>Rectangle</code>
     * @param y      the new Y coordinate for the upper-left
     *               corner of this <code>Rectangle</code>
     * @param width  the new width for this <code>Rectangle</code>
     * @param height the new height for this <code>Rectangle</code>
     */
    public void setBounds(int x, int y, int width, int height) {
        reshape(x, y, width, height);
    }


    /**
     * Returns the location of this <code>Rectangle</code>.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>getLocation</code> method of <code>Component</code>.
     *
     * @return the <code>Point</code> that is the upper-left corner of
     * this <code>Rectangle</code>.
     */
    public Point getLocation() {
        return new Point(x, y);
    }


    /**
     * Moves this <code>Rectangle</code> to the specified location.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     *
     * @param p the <code>Point</code> specifying the new location
     *          for this <code>Rectangle</code>
     */
    public void setLocation(Point p) {
        setLocation(p.x, p.y);
    }


    /**
     * Moves this <code>Rectangle</code> to the specified location.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setLocation</code> method of <code>Component</code>.
     *
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     */
    public void setLocation(int x, int y) {
        move(x, y);
    }


    /**
     * Translates this <code>Rectangle</code> the indicated distance,
     * to the right along the X coordinate axis, and
     * downward along the Y coordinate axis.
     *
     * @param dx the distance to move this <code>Rectangle</code>
     *           along the X axis
     * @param dy the distance to move this <code>Rectangle</code>
     *           along the Y axis
     */
    public void translate(int dx, int dy) {
        int oldv = this.x;
        int newv = oldv + dx;
        if (dx < 0) {
            // moving leftward
            if (newv > oldv) {
                // negative overflow
                // Only adjust width if it was valid (>= 0).
                if (width >= 0) {
                    // The right edge is now conceptually at
                    // newv+width, but we may move newv to prevent
                    // overflow.  But we want the right edge to
                    // remain at its new location in spite of the
                    // clipping.  Think of the following adjustment
                    // conceptually the same as:
                    // width += newv; newv = MIN_VALUE; width -= newv;
                    width += newv - Integer.MIN_VALUE;
                    // width may go negative if the right edge went past
                    // MIN_VALUE, but it cannot overflow since it cannot
                    // have moved more than MIN_VALUE and any non-negative
                    // number + MIN_VALUE does not overflow.
                }
                newv = Integer.MIN_VALUE;
            }
        } else {
            // moving rightward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (width >= 0) {
                    // Conceptually the same as:
                    // width += newv; newv = MAX_VALUE; width -= newv;
                    width += newv - Integer.MAX_VALUE;
                    // With large widths and large displacements
                    // we may overflow so we need to check it.
                    if (width < 0) {
                        width = Integer.MAX_VALUE;
                    }
                }
                newv = Integer.MAX_VALUE;
            }
        }
        this.x = newv;

        oldv = this.y;
        newv = oldv + dy;
        if (dy < 0) {
            // moving upward
            if (newv > oldv) {
                // negative overflow
                if (height >= 0) {
                    height += newv - Integer.MIN_VALUE;
                    // See above comment about no overflow in this case
                }
                newv = Integer.MIN_VALUE;
            }
        } else {
            // moving downward (or staying still)
            if (newv < oldv) {
                // positive overflow
                if (height >= 0) {
                    height += newv - Integer.MAX_VALUE;
                    if (height < 0) {
                        height = Integer.MAX_VALUE;
                    }
                }
                newv = Integer.MAX_VALUE;
            }
        }
        this.y = newv;
    }


    /**
     * Gets the size of this <code>Rectangle</code>, represented by
     * the returned <code>Dimension</code>.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>getSize</code> method of <code>Component</code>.
     *
     * @return a <code>Dimension</code>, representing the size of
     * this <code>Rectangle</code>.
     */
    public Dimension getSize() {
        return size;
    }


    /**
     * Sets the size of this <code>Rectangle</code> to match the
     * specified <code>Dimension</code>.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method of <code>Component</code>.
     *
     * @param d the new size for the <code>Dimension</code> object
     */
    public void setSize(Dimension d) {
        setSize(d.width, d.height);
    }


    /**
     * Sets the size of this <code>Rectangle</code> to the specified
     * width and height.
     * <p/>
     * This method is included for completeness, to parallel the
     * <code>setSize</code> method of <code>Component</code>.
     *
     * @param width  the new width for this <code>Rectangle</code>
     * @param height the new height for this <code>Rectangle</code>
     */
    public void setSize(int width, int height) {
        resize(width, height);
    }


    /**
     * Checks whether or not this <code>Rectangle</code> contains the
     * specified <code>Point</code>.
     *
     * @param p the <code>Point</code> to test
     * @return <code>true</code> if the specified <code>Point</code>
     * is inside this <code>Rectangle</code>;
     * <code>false</code> otherwise.
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }


    /**
     * Checks whether or not this <code>Rectangle</code> entirely contains
     * the specified <code>Rectangle</code>.
     *
     * @param rect the specified <code>Rectangle</code>
     * @return <code>true</code> if the <code>Rectangle</code>
     * is contained entirely inside this <code>Rectangle</code>;
     * <code>false</code> otherwise
     */
    public boolean contains(Rectangle rect) {
        return contains(rect.x, rect.y, rect.size.getWidth(), rect.size.getHeight());
    }


    /**
     * Computes the intersection of this <code>Rectangle</code> with the
     * specified <code>Rectangle</code>. Returns a new <code>Rectangle</code>
     * that represents the intersection of the two rectangles.
     * If the two rectangles do not intersect, the result will be
     * an empty rectangle.
     *
     * @param r the specified <code>Rectangle</code>
     * @return the largest <code>Rectangle</code> contained in both the
     * specified <code>Rectangle</code> and in
     * this <code>Rectangle</code>; or if the rectangles
     * do not intersect, an empty rectangle.
     */
    public Rectangle intersection(Rectangle r) {
        int tx1 = this.x;
        int ty1 = this.y;
        int rx1 = r.x;
        int ry1 = r.y;
        int tx2 = tx1;
        tx2 += this.width;
        int ty2 = ty1;
        ty2 += this.height;
        int rx2 = rx1;
        rx2 += r.width;
        int ry2 = ry1;
        ry2 += r.height;
        if (tx1 < rx1) {
            tx1 = rx1;
        }
        if (ty1 < ry1) {
            ty1 = ry1;
        }
        if (tx2 > rx2) {
            tx2 = rx2;
        }
        if (ty2 > ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never overflow (they will never be
        // larger than the smallest of the two source w,h)
        // they might underflow, though...
        if (tx2 < Integer.MIN_VALUE) {
            tx2 = Integer.MIN_VALUE;
        }
        if (ty2 < Integer.MIN_VALUE) {
            ty2 = Integer.MIN_VALUE;
        }
        return new Rectangle(tx1, ty1, tx2, ty2);
    }


    /**
     * Computes the union of this <code>Rectangle</code> with the
     * specified <code>Rectangle</code>. Returns a new
     * <code>Rectangle</code> that
     * represents the union of the two rectangles.
     * <p/>
     * If either {@code Rectangle} has any dimension less than zero
     * the rules for <a href=#NonExistant>non-existant</a> rectangles
     * apply.
     * If only one has a dimension less than zero, then the result
     * will be a copy of the other {@code Rectangle}.
     * If both have dimension less than zero, then the result will
     * have at least one dimension less than zero.
     * <p/>
     * If the resulting {@code Rectangle} would have a dimension
     * too large to be expressed as an {@code int}, the result
     * will have a dimension of {@code Integer.MAX_VALUE} along
     * that dimension.
     *
     * @param r the specified <code>Rectangle</code>
     * @return the smallest <code>Rectangle</code> containing both
     * the specified <code>Rectangle</code> and this
     * <code>Rectangle</code>.
     */
    public Rectangle union(Rectangle r) {
        int tx2 = this.width;
        int ty2 = this.height;
        if ((tx2 | ty2) < 0) {
            // This rectangle has negative dimensions...
            // If r has non-negative dimensions then it is the answer.
            // If r is non-existant (has a negative dimension), then both
            // are non-existant and we can return any non-existant rectangle
            // as an answer.  Thus, returning r meets that criterion.
            // Either way, r is our answer.
            return new Rectangle(r);
        }
        int rx2 = r.width;
        int ry2 = r.height;
        if ((rx2 | ry2) < 0) {
            return new Rectangle(this);
        }
        int tx1 = this.x;
        int ty1 = this.y;
        tx2 += tx1;
        ty2 += ty1;
        int rx1 = r.x;
        int ry1 = r.y;
        rx2 += rx1;
        ry2 += ry1;
        if (tx1 > rx1) {
            tx1 = rx1;
        }
        if (ty1 > ry1) {
            ty1 = ry1;
        }
        if (tx2 < rx2) {
            tx2 = rx2;
        }
        if (ty2 < ry2) {
            ty2 = ry2;
        }
        tx2 -= tx1;
        ty2 -= ty1;
        // tx2,ty2 will never underflow since both original rectangles
        // were already proven to be non-empty
        // they might overflow, though...
        if (tx2 > Integer.MAX_VALUE) {
            tx2 = Integer.MAX_VALUE;
        }
        if (ty2 > Integer.MAX_VALUE) {
            ty2 = Integer.MAX_VALUE;
        }
        return new Rectangle(tx1, ty1, (int) tx2, (int) ty2);
    }


    /**
     * Resizes the <code>Rectangle</code> both horizontally and vertically.
     * <p/>
     * This method modifies the <code>Rectangle</code> so that it is
     * <code>h</code> units larger on both the left and right side,
     * and <code>v</code> units larger at both the top and bottom.
     * <p/>
     * The new <code>Rectangle</code> has {@code (x - h, y - v)}
     * as its upper-left corner,
     * width of {@code (width + 2h)},
     * and a height of {@code (height + 2v)}.
     * <p/>
     * If negative values are supplied for <code>h</code> and
     * <code>v</code>, the size of the <code>Rectangle</code>
     * decreases accordingly.
     * The {@code grow} method will check for integer overflow
     * and underflow, but does not check whether the resulting
     * values of {@code width} and {@code height} grow
     * from negative to non-negative or shrink from non-negative
     * to negative.
     *
     * @param h the horizontal expansion
     * @param v the vertical expansion
     */
    public void grow(int h, int v) {
        int x0 = this.x;
        int y0 = this.y;
        int x1 = this.width;
        int y1 = this.height;
        x1 += x0;
        y1 += y0;

        x0 -= h;
        y0 -= v;
        x1 += h;
        y1 += v;

        if (x1 < x0) {
            // Non-existant in X direction
            // Final width must remain negative so subtract x0 before
            // it is clipped so that we avoid the risk that the clipping
            // of x0 will reverse the ordering of x0 and x1.
            x1 -= x0;
            if (x1 < Integer.MIN_VALUE) {
                x1 = Integer.MIN_VALUE;
            }
            if (x0 < Integer.MIN_VALUE) {
                x0 = Integer.MIN_VALUE;
            } else if (x0 > Integer.MAX_VALUE) {
                x0 = Integer.MAX_VALUE;
            }
        } else { // (x1 >= x0)
            // Clip x0 before we subtract it from x1 in case the clipping
            // affects the representable area of the rectangle.
            if (x0 < Integer.MIN_VALUE) {
                x0 = Integer.MIN_VALUE;
            } else if (x0 > Integer.MAX_VALUE) {
                x0 = Integer.MAX_VALUE;
            }
            x1 -= x0;
            // The only way x1 can be negative now is if we clipped
            // x0 against MIN and x1 is less than MIN - in which case
            // we want to leave the width negative since the result
            // did not intersect the representable area.
            if (x1 < Integer.MIN_VALUE) {
                x1 = Integer.MIN_VALUE;
            } else if (x1 > Integer.MAX_VALUE) {
                x1 = Integer.MAX_VALUE;
            }
        }

        if (y1 < y0) {
            // Non-existant in Y direction
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) {
                y1 = Integer.MIN_VALUE;
            }
            if (y0 < Integer.MIN_VALUE) {
                y0 = Integer.MIN_VALUE;
            } else if (y0 > Integer.MAX_VALUE) {
                y0 = Integer.MAX_VALUE;
            }
        } else { // (y1 >= y0)
            if (y0 < Integer.MIN_VALUE) {
                y0 = Integer.MIN_VALUE;
            } else if (y0 > Integer.MAX_VALUE) {
                y0 = Integer.MAX_VALUE;
            }
            y1 -= y0;
            if (y1 < Integer.MIN_VALUE) {
                y1 = Integer.MIN_VALUE;
            } else if (y1 > Integer.MAX_VALUE) {
                y1 = Integer.MAX_VALUE;
            }
        }

        reshape(x0, y0, x1, y1);
    }


    /**
     * {@inheritDoc}
     */
    public boolean isEmpty() {
        return (width <= 0) || (height <= 0);
    }


    /**
     * Set the rectangle to empty.
     */
    public void setEmpty() {
        x = y = width = height = 0;
    }


    /**
     * Determines where the specified coordinates lie with respect
     * to this <code>Rectangle</code>.
     * This method computes a binary OR of the appropriate mask values
     * indicating, for each side of this <code>Rectangle</code>,
     * whether or not the specified coordinates are on the same side
     * of the edge as the rest of this <code>Rectangle</code>.
     *
     * @param x the specified X coordinate
     * @param y the specified Y coordinate
     * @return the logical OR of all appropriate out codes.
     */
    public int outcode(int x, int y) {

        int out = 0;
        if (this.width <= 0) {
            out |= OUT_LEFT | OUT_RIGHT;
        } else if (x < this.x) {
            out |= OUT_LEFT;
        } else if (x > this.x + (long) this.width) {
            out |= OUT_RIGHT;
        }
        if (this.height <= 0) {
            out |= OUT_TOP | OUT_BOTTOM;
        } else if (y < this.y) {
            out |= OUT_TOP;
        } else if (y > this.y + (long) this.height) {
            out |= OUT_BOTTOM;
        }
        return out;
    }


    /**
     * Returns a new <code>Rectangle</code> object representing the
     * intersection of this <code>Rectangle</code> with the specified
     * <code>Rectangle</code>.
     *
     * @param r the <code>Rectangle</code> to be intersected with
     *          this <code>Rectangle</code>
     * @return the largest <code>Rectangle</code> contained in both
     * the specified <code>Rectangle</code> and in this
     * <code>Rectangle</code>.
     */
    public Rectangle createIntersection(Rectangle r) {
        if (r instanceof Rectangle) {
            return intersection((Rectangle) r);
        }
        Rectangle dest = new Rectangle();
        Rectangle.intersect(this, r, dest);
        return dest;
    }


    /**
     * Returns a new <code>Rectangle</code> object representing the
     * union of this <code>Rectangle</code> with the specified
     * <code>Rectangle</code>.
     *
     * @param r the <code>Rectangle</code> to be combined with
     *          this <code>Rectangle</code>
     * @return the smallest <code>Rectangle</code> containing both
     * the specified <code>Rectangle</code> and this
     * <code>Rectangle</code>.
     */
    public Rectangle createUnion(Rectangle r) {
        if (r instanceof Rectangle) {
            return union((Rectangle) r);
        }
        Rectangle dest = new Rectangle();
        Rectangle.union(this, r, dest);
        return dest;
    }


    /**
     * Returns a <code>String</code> representing this
     * <code>Rectangle</code> and its values.
     *
     * @return a <code>String</code> representing this
     * <code>Rectangle</code> object's coordinate and size values.
     */
    public String toString() {
        return "RECT " + "[x=" + x + ",y=" + y + ",width=" + width + ",height=" + height + "]";
    }

    // Return best integer representation for v, clipped to integer
    // range and floor-ed or ceiling-ed, depending on the boolean.
    private static int clip(int v, boolean doceil) {
        if (v <= Integer.MIN_VALUE) {
            return Integer.MIN_VALUE;
        }
        if (v >= Integer.MAX_VALUE) {
            return Integer.MAX_VALUE;
        }
        return (int) (doceil ? Math.ceil(v) : Math.floor(v));
    }


    /**
     * Sets the bounding <code>Rectangle</code> of this
     * <code>Rectangle</code> to the specified
     * <code>x</code>, <code>y</code>, <code>width</code>,
     * and <code>height</code>.
     * <p/>
     *
     * @param x      the new X coordinate for the upper-left
     *               corner of this <code>Rectangle</code>
     * @param y      the new Y coordinate for the upper-left
     *               corner of this <code>Rectangle</code>
     * @param width  the new width for this <code>Rectangle</code>
     * @param height the new height for this <code>Rectangle</code>
     */
    private void reshape(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.size.width = width;
        this.size.height = height;
    }


    /**
     * Moves this <code>Rectangle</code> to the specified location.
     * <p/>
     *
     * @param x the X coordinate of the new location
     * @param y the Y coordinate of the new location
     */
    private void move(int x, int y) {
        this.x = x;
        this.y = y;
    }


    /**
     * Sets the size of this <code>Rectangle</code> to the specified
     * width and height.
     * <p/>
     *
     * @param width  the new width for this <code>Rectangle</code>
     * @param height the new height for this <code>Rectangle</code>
     */
    private void resize(int width, int height) {
        this.width = width;
        this.height = height;
        this.size.width = width;
        this.size.height = height;

    }


    /**
     * Checks whether or not this <code>Rectangle</code> contains the
     * point at the specified location {@code (X, Y)}.
     *
     * @param X the specified X coordinate
     * @param Y the specified Y coordinate
     * @return <code>true</code> if the point
     * {@code (X, Y)} is inside this
     * <code>Rectangle</code>;
     * <code>false</code> otherwise.
     */
    private boolean inside(int X, int Y) {
        int w = this.width;
        int h = this.height;
        if ((w | h) < 0) {
            // At least one of the dimensions is negative...
            return false;
        }
        // Note: if either dimension is zero, tests below must return false...
        int xp = this.x;
        int yp = this.y;
        if (X < xp || Y < yp) {
            return false;
        }
        w += xp;
        h += yp;
        //    overflow || intersect
        return ((w < xp || w > X) &&
                (h < yp || h > Y));
    }
}
