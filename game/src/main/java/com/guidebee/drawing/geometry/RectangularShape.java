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

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * <code>RectangularShape</code> is the base class for a number of
 * {@link IShape} objects whose geometry is defined by a rectangular frame.
 * This class does not directly specify any specific geometry by
 * itself, but merely provides manipulation methods inherited by
 * a whole category of <code>IShape</code> objects.
 * The manipulation methods provided by this class can be used to
 * query and modify the rectangular frame, which provides a reference
 * for the subclasses to define their geometry.
 * <p/>
 * @author James Shen.
 */
public abstract class RectangularShape implements IShape {


    /**
     * This is an abstract class that cannot be instantiated directly.
     */
    protected RectangularShape() {
    }


    /**
     * Returns the X coordinate of the upper-left corner of
     * the framing rectangle in <code>long</code> precision.
     *
     * @return the X coordinate of the upper-left corner of
     * the framing rectangle.
     */
    public abstract int getX();


    /**
     * Returns the Y coordinate of the upper-left corner of
     * the framing rectangle in <code>long</code> precision.
     *
     * @return the Y coordinate of the upper-left corner of
     * the framing rectangle.
     */
    public abstract int getY();


    /**
     * Returns the width of the framing rectangle in
     * <code>long</code> precision.
     *
     * @return the width of the framing rectangle.
     */
    public abstract int getWidth();


    /**
     * Returns the height of the framing rectangle
     * in <code>long</code> precision.
     *
     * @return the height of the framing rectangle.
     */
    public abstract int getHeight();


    /**
     * Returns the smallest X coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the smallest X coordinate of the framing
     * rectangle of the <code>IShape</code>.
     */
    public int getMinX() {
        return getX();
    }


    /**
     * Returns the smallest Y coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the smallest Y coordinate of the framing
     * rectangle of the <code>IShape</code>.
     */
    public int getMinY() {
        return getY();
    }


    /**
     * Returns the largest X coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the largest X coordinate of the framing
     * rectangle of the <code>IShape</code>.
     */
    public int getMaxX() {
        return getX() + getWidth();
    }


    /**
     * Returns the largest Y coordinate of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the largest Y coordinate of the framing
     * rectangle of the <code>IShape</code>.
     */
    public int getMaxY() {
        return getY() + getHeight();
    }


    /**
     * Returns the X coordinate of the center of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the X coordinate of the center of the framing rectangle
     * of the <code>IShape</code>.
     */
    public int getCenterX() {
        return getX() + getWidth() / 2;
    }


    /**
     * Returns the Y coordinate of the center of the framing
     * rectangle of the <code>IShape</code> in <code>long</code>
     * precision.
     *
     * @return the Y coordinate of the center of the framing rectangle
     * of the <code>IShape</code>.
     */
    public int getCenterY() {
        return getY() + getHeight() / 2;
    }


    /**
     * Returns the framing {@link Rectangle}
     * that defines the overall shape of this object.
     *
     * @return a <code>Rectangle</code>, specified in
     * <code>long</code> coordinates.
     */
    public Rectangle getFrame() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }


    /**
     * Determines whether the <code>RectangularShape</code> is empty.
     * When the <code>RectangularShape</code> is empty, it encloses no
     * area.
     *
     * @return <code>true</code> if the <code>RectangularShape</code> is empty;
     * <code>false</code> otherwise.
     */
    public abstract boolean isEmpty();


    /**
     * Sets the location and size of the framing rectangle of this
     * <code>IShape</code> to the specified rectangular values.
     *
     * @param x the X coordinate of the upper-left corner of the
     *          specified rectangular shape
     * @param y the Y coordinate of the upper-left corner of the
     *          specified rectangular shape
     * @param w the width of the specified rectangular shape
     * @param h the height of the specified rectangular shape
     */
    public abstract void setFrame(int x, int y, int w, int h);


    /**
     * Sets the location and size of the framing rectangle of this
     * <code>IShape</code> to the specified {@link Point} and
     * {@link Dimension}, respectively.  The framing rectangle is used
     * by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     *
     * @param loc  the specified <code>Point</code>
     * @param size the specified <code>Dimension</code>
     */
    public void setFrame(Point loc, Dimension size) {
        setFrame(loc.getX(), loc.getY(), size.getWidth(), size.getHeight());
    }


    /**
     * Sets the framing rectangle of this <code>IShape</code> to
     * be the specified <code>Rectangle</code>.  The framing rectangle is
     * used by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     *
     * @param r the specified <code>Rectangle</code>
     */
    public void setFrame(Rectangle r) {
        setFrame(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }


    /**
     * Sets the diagonal of the framing rectangle of this <code>IShape</code>
     * based on the two specified coordinates.  The framing rectangle is
     * used by the subclasses of <code>RectangularShape</code> to define
     * their geometry.
     *
     * @param x1 the X coordinate of the start point of the specified diagonal
     * @param y1 the Y coordinate of the start point of the specified diagonal
     * @param x2 the X coordinate of the end point of the specified diagonal
     * @param y2 the Y coordinate of the end point of the specified diagonal
     */
    public void setFrameFromDiagonal(int x1, int y1,
                                     int x2, int y2) {
        if (x2 < x1) {
            int t = x1;
            x1 = x2;
            x2 = t;
        }
        if (y2 < y1) {
            int t = y1;
            y1 = y2;
            y2 = t;
        }
        setFrame(x1, y1, x2 - x1, y2 - y1);
    }


    /**
     * Sets the diagonal of the framing rectangle of this <code>IShape</code>
     * based on two specified <code>Point</code> objects.  The framing
     * rectangle is used by the subclasses of <code>RectangularShape</code>
     * to define their geometry.
     *
     * @param p1 the start <code>Point</code> of the specified diagonal
     * @param p2 the end <code>Point</code> of the specified diagonal
     */
    public void setFrameFromDiagonal(Point p1, Point p2) {
        setFrameFromDiagonal(p1.getX(), p1.getY(), p2.getX(), p2.getY());
    }


    /**
     * Sets the framing rectangle of this <code>IShape</code>
     * based on the specified center point coordinates and corner point
     * coordinates.  The framing rectangle is used by the subclasses of
     * <code>RectangularShape</code> to define their geometry.
     *
     * @param centerX the X coordinate of the specified center point
     * @param centerY the Y coordinate of the specified center point
     * @param cornerX the X coordinate of the specified corner point
     * @param cornerY the Y coordinate of the specified corner point
     */
    public void setFrameFromCenter(int centerX, int centerY,
                                   int cornerX, int cornerY) {
        int halfW = Math.abs(cornerX - centerX);
        int halfH = Math.abs(cornerY - centerY);
        setFrame(centerX - halfW, centerY - halfH, halfW * 2, halfH * 2);
    }


    /**
     * Sets the framing rectangle of this <code>IShape</code> based on a
     * specified center <code>Point</code> and corner
     * <code>Point</code>.  The framing rectangle is used by the subclasses
     * of <code>RectangularShape</code> to define their geometry.
     *
     * @param center the specified center <code>Point</code>
     * @param corner the specified corner <code>Point</code>
     */
    public void setFrameFromCenter(Point center, Point corner) {
        setFrameFromCenter(center.getX(), center.getY(),
                corner.getX(), corner.getY());
    }


    /**
     * {@inheritDoc}
     */
    public boolean contains(Point p) {
        return contains(p.x, p.y);
    }


    /**
     * {@inheritDoc}
     */
    public boolean intersects(Rectangle r) {
        return intersects(r.x, r.y, r.width, r.height);
    }


    /**
     * {@inheritDoc}
     */
    public boolean contains(Rectangle r) {
        return contains(r.x, r.y, r.width, r.height);
    }


    /**
     * {@inheritDoc}
     */
    public Rectangle getBounds() {
        long width = getWidth();
        long height = getHeight();
        if (width < 0 || height < 0) {
            return new Rectangle();
        }
        long x = getX();
        long y = getY();
        long x1 = MathFP.floor(x);
        long y1 = MathFP.floor(y);
        long x2 = MathFP.ceil(x + width);
        long y2 = MathFP.ceil(y + height);
        return new Rectangle((int) x1, (int) y1,
                (int) (x2 - x1), (int) (y2 - y1));
    }


    /**
     * Returns an iterator object that iterates along the
     * <code>IShape</code> object's boundary and provides access to a
     * flattened view of the outline of the <code>IShape</code>
     * object's geometry.
     * <p/>
     * Only SEG_MOVETO, SEG_LINETO, and SEG_CLOSE point types will
     * be returned by the iterator.
     * <p/>
     * The amount of subdivision of the curved segments is controlled
     * by the <code>flatness</code> parameter, which specifies the
     * maximum distance that any point on the unflattened transformed
     * curve can deviate from the returned flattened path segments.
     * An optional {@link AffineTransform} can
     * be specified so that the coordinates returned in the iteration are
     * transformed accordingly.
     *
     * @param at       an optional <code>AffineTransform</code> to be applied to the
     *                 coordinates as they are returned in the iteration,
     *                 or <code>null</code> if untransformed coordinates are desired.
     * @param flatness the maximum distance that the line segments used to
     *                 approximate the curved segments are allowed to deviate
     *                 from any point on the original curve
     * @return a <code>IPathIterator</code> object that provides access to
     * the <code>IShape</code> object's flattened geometry.
     */
    public IPathIterator getPathIterator(AffineTransform at, int flatness) {
        return new FlatteningPathIterator(getPathIterator(at), flatness);
    }

}
