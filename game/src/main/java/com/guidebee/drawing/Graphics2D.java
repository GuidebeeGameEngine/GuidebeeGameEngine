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
package com.guidebee.drawing;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.drawing.geometry.AffineTransform;
import com.guidebee.drawing.geometry.Area;
import com.guidebee.drawing.geometry.IPathIterator;
import com.guidebee.drawing.geometry.IShape;
import com.guidebee.drawing.geometry.Path;
import com.guidebee.drawing.geometry.Point;
import com.guidebee.drawing.geometry.Polygon;
import com.guidebee.drawing.geometry.Polyline;
import com.guidebee.drawing.geometry.Rectangle;
import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.drawing.core.GraphicsFP;
import com.guidebee.game.engine.drawing.core.GraphicsPathDasherFP;
import com.guidebee.game.engine.drawing.core.GraphicsPathFP;
import com.guidebee.game.engine.drawing.core.MatrixFP;
import com.guidebee.game.engine.drawing.core.PenFP;
import com.guidebee.game.engine.drawing.core.PointFP;
import com.guidebee.game.engine.drawing.core.SingleFP;
import com.guidebee.game.engine.drawing.core.SolidBrushFP;
import com.guidebee.game.graphics.Pixmap;
import com.guidebee.game.graphics.TextureData;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * This Graphics2D class provides more sophisticated control over geometry,
 * coordinate transformations, color management, and text layout. This is the
 * fundamental class for rendering 2-dimensional shapes, text and images
 * on the mobile platform.
 *
 * @author James Shen.
 */
public final class Graphics2D implements TextureData {



    /**
     * Constructor. create a graphics object with given width and height
     *
     * @param width  the width of the graphics 2d object.
     * @param height the height of the graphics 2d object.
     */
    public Graphics2D(int width, int height) {
        graphicsFP = new GraphicsFP(width, height);
        defaultPen = new Pen(Color.BLACK);
        defaultBrush = new SolidBrush(Color.WHITE);
        graphicsWidth = width;
        graphicsHeight = height;
    }


    /**
     * Reset the graphics object to default settings.
     */
    public void Reset() {
        graphicsFP.setMatrix(new MatrixFP());
        defaultPen = new Pen(Color.BLACK);
        defaultBrush = new SolidBrush(Color.WHITE);
        graphicsFP.setClip(0, 0, graphicsWidth, graphicsHeight);
    }


    /**
     * return the width of the Graphics
     *
     * @return the width of the graphics object.
     */
    @Override
    public int getWidth() {
        return graphicsWidth;
    }


    /**
     * get the height of the graphics object.
     *
     * @return the height of the graphics object.
     */
    @Override
    public int getHeight() {
        return graphicsHeight;
    }


    @Override
    public TextureDataType getType() {
        return TextureDataType.Pixmap;
    }

    @Override
    public boolean isPrepared() {
        return isPrepared;
    }

    @Override
    public void prepare() {
        if (isPrepared) throw new GameEngineRuntimeException("Already prepared");
        isPrepared = true;
    }



    @Override
    public void consumeCustomData(int target) {

        throw new GameEngineRuntimeException(
                "This TextureData implementation does not upload data itself");
    }

    @Override
    public Pixmap consumePixmap() {
        if(pixMap!=null){
            pixMap.dispose();
        }
        pixMap = new Pixmap(graphicsWidth, graphicsHeight, Pixmap.Format.RGBA8888);
        pixMap.drawRGB(getRGB(), graphicsWidth, graphicsHeight);
        return pixMap;
    }

    @Override
    public boolean disposePixmap() {
        return true;
    }



    @Override
    public Pixmap.Format getFormat() {
        return Pixmap.Format.RGBA8888; // it's not true,
    }

    @Override
    public boolean useMipMaps() {
        return false;
    }

    @Override
    public boolean isManaged() {
        return true;
    }


    /**
     * Get the shape's outline with given pen.
     *
     * @param pen   pen used to draw the shape, only the pen's width have
     *              the effect on the shape's outline.
     * @param shape the input shape.
     * @return the outline shape if draw with given pen.
     */
    public static IShape getOutline(Pen pen, IShape shape) {
        if (pen != null) {
            PenFP penFP = new PenFP(pen.color.getRGB());
            penFP.endCap = pen.cap;
            penFP.startCap = pen.cap;
            penFP.lineJoin = pen.join;
            penFP.width = pen.width << SingleFP.DECIMAL_BITS;
            if (pen.brush != null) {
                penFP.brush = pen.brush.wrappedBrushFP;

            } else {
                penFP.brush = new SolidBrushFP(pen.color.value);
            }
            if (pen.dash != null) {
                penFP.dashArray = new int[pen.dash.length - pen.dash_phase];
                for (int i = 0; i < pen.dash.length - pen.dash_phase; i++) {
                    penFP.dashArray[i] =
                            pen.dash[i - pen.dash_phase]
                                    << SingleFP.DECIMAL_BITS;
                }
            } else {
                penFP.dashArray = null;
            }

            IPathIterator pathIterator = shape.getPathIterator(null);
            int[] coords = new int[6];
            int type;
            GraphicsPathFP graphicsPathFP = new GraphicsPathFP();
            PointFP pointFP1 = new PointFP();
            PointFP pointFPCtl1 = new PointFP();
            PointFP pointFPCtl2 = new PointFP();

            while (!pathIterator.isDone()) {
                type = pathIterator.currentSegment(coords);
                switch (type) {
                    case IPathIterator.SEG_MOVETO:
                        pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                                coords[1] << SingleFP.DECIMAL_BITS);
                        graphicsPathFP.addMoveTo(pointFP1);
                        break;
                    case IPathIterator.SEG_CLOSE:
                        graphicsPathFP.addClose();
                        break;
                    case IPathIterator.SEG_LINETO:
                        pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                                coords[1] << SingleFP.DECIMAL_BITS);
                        graphicsPathFP.addLineTo(pointFP1);
                        break;
                    case IPathIterator.SEG_QUADTO:
                        pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                                coords[1] << SingleFP.DECIMAL_BITS);
                        pointFP1.reset(coords[2] << SingleFP.DECIMAL_BITS,
                                coords[3] << SingleFP.DECIMAL_BITS);
                        graphicsPathFP.addQuadTo(pointFPCtl1, pointFP1);
                        break;
                    case IPathIterator.SEG_CUBICTO:
                        pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                                coords[1] << SingleFP.DECIMAL_BITS);
                        pointFPCtl2.reset(coords[2] << SingleFP.DECIMAL_BITS,
                                coords[3] << SingleFP.DECIMAL_BITS);
                        pointFP1.reset(coords[4] << SingleFP.DECIMAL_BITS,
                                coords[5] << SingleFP.DECIMAL_BITS);
                        graphicsPathFP.addCurveTo(pointFPCtl1, pointFPCtl2, pointFP1);
                        break;

                }
                pathIterator.next();

            }

            if (penFP.dashArray != null) {
                PenFP newlineStyle = new PenFP(penFP.brush, penFP.width,
                        PenFP.LINECAP_BUTT, PenFP.LINECAP_BUTT, PenFP.LINEJOIN_MITER);

                newlineStyle.dashArray = penFP.dashArray;
                GraphicsPathDasherFP dasher = new GraphicsPathDasherFP(graphicsPathFP,
                        newlineStyle.dashArray, 0);
                GraphicsPathFP newPath = dasher.GetDashedGraphicsPath();
                graphicsPathFP = newPath;


            }
            GraphicsPathFP outline = graphicsPathFP.calcOutline(penFP);
            return union(outline.toPath());
        } else {
            return null;
        }

    }


    /**
     * Strokes the outline of a IShape using the settings of the current
     * Graphics2D context.
     *
     * @param pen   the pen used to stroke the shape.
     * @param shape the IShape to be rendered.
     */
    public final void draw(Pen pen, IShape shape) {
        setGraphicsFPPenAttribute(pen);
        IPathIterator pathIterator = shape.getPathIterator(null);
        int[] coords = new int[6];
        int type;
        GraphicsPathFP graphicsPathFP = new GraphicsPathFP();
        PointFP pointFP1 = new PointFP();
        PointFP pointFPCtl1 = new PointFP();
        PointFP pointFPCtl2 = new PointFP();

        while (!pathIterator.isDone()) {
            type = pathIterator.currentSegment(coords);
            switch (type) {
                case IPathIterator.SEG_MOVETO:
                    pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addMoveTo(pointFP1);
                    break;
                case IPathIterator.SEG_CLOSE:
                    graphicsPathFP.addClose();
                    break;
                case IPathIterator.SEG_LINETO:
                    pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addLineTo(pointFP1);
                    break;
                case IPathIterator.SEG_QUADTO:
                    pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    pointFP1.reset(coords[2] << SingleFP.DECIMAL_BITS,
                            coords[3] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addQuadTo(pointFPCtl1, pointFP1);
                    break;
                case IPathIterator.SEG_CUBICTO:
                    pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    pointFPCtl2.reset(coords[2] << SingleFP.DECIMAL_BITS,
                            coords[3] << SingleFP.DECIMAL_BITS);
                    pointFP1.reset(coords[4] << SingleFP.DECIMAL_BITS,
                            coords[5] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addCurveTo(pointFPCtl1, pointFPCtl2, pointFP1);
                    break;

            }
            pathIterator.next();

        }
        synchronized (graphicsFP) {
            graphicsFP.drawPath(graphicsPathFP);
        }

    }


    /**
     * Strokes the outline of a IShape using the settings of the current
     * Graphics2D context.
     *
     * @param brush the brush used to fill the shape.
     * @param shape the IShape to be rendered.
     */
    public final void fill(Brush brush, IShape shape) {
        if (brush != null) {
            synchronized (graphicsFP) {
                graphicsFP.setBrush(brush.wrappedBrushFP);
            }
            defaultBrush = brush;
        }

        IPathIterator pathIterator = shape.getPathIterator(null);
        int[] coords = new int[6];
        int type;
        GraphicsPathFP graphicsPathFP = new GraphicsPathFP();
        PointFP pointFP1 = new PointFP();
        PointFP pointFPCtl1 = new PointFP();
        PointFP pointFPCtl2 = new PointFP();

        while (!pathIterator.isDone()) {
            type = pathIterator.currentSegment(coords);
            switch (type) {
                case IPathIterator.SEG_MOVETO:
                    pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addMoveTo(pointFP1);
                    break;
                case IPathIterator.SEG_CLOSE:
                    graphicsPathFP.addClose();
                    break;
                case IPathIterator.SEG_LINETO:
                    pointFP1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addLineTo(pointFP1);
                    break;
                case IPathIterator.SEG_QUADTO:
                    pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    pointFP1.reset(coords[2] << SingleFP.DECIMAL_BITS,
                            coords[3] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addQuadTo(pointFPCtl1, pointFP1);
                    break;
                case IPathIterator.SEG_CUBICTO:
                    pointFPCtl1.reset(coords[0] << SingleFP.DECIMAL_BITS,
                            coords[1] << SingleFP.DECIMAL_BITS);
                    pointFPCtl2.reset(coords[2] << SingleFP.DECIMAL_BITS,
                            coords[3] << SingleFP.DECIMAL_BITS);
                    pointFP1.reset(coords[4] << SingleFP.DECIMAL_BITS,
                            coords[5] << SingleFP.DECIMAL_BITS);
                    graphicsPathFP.addCurveTo(pointFPCtl1, pointFPCtl2, pointFP1);
                    break;

            }
            pathIterator.next();

        }
        synchronized (graphicsFP) {
            graphicsFP.fillPath(graphicsPathFP);
        }

    }


    /**
     * Draws a line between the points (x1, y1) and (x2, y2).
     *
     * @param pen pen used to draw the line
     * @param x1  the first point's x coordinate.
     * @param y1  the first point's y coordinate.
     * @param x2  the second point's x coordinate.
     * @param y2  the second point's y coordinate.
     */
    public final void drawLine(Pen pen, int x1, int y1, int x2, int y2) {
        setGraphicsFPPenAttribute(pen);
        synchronized (graphicsFP) {
            graphicsFP.drawLine(x1 << SingleFP.DECIMAL_BITS, y1 << SingleFP.DECIMAL_BITS,
                    x2 << SingleFP.DECIMAL_BITS, y2 << SingleFP.DECIMAL_BITS);
        }
    }


    /**
     * Draws a line between the points pt1 and pt2.
     *
     * @param pen pen used to draw the line
     * @param pt1 the first point.
     * @param pt2 the second point.
     */
    public final void drawLine(Pen pen, Point pt1, Point pt2) {
        drawLine(pen, pt1.x, pt1.y, pt2.x, pt2.y);
    }

    /**
     * Draws the specified characters using the current font and color.
     * The offset and length parameters must specify a valid range of characters
     * within the character array data. The offset parameter must be within the
     * range [0..(data.length)], inclusive. The length parameter must be a
     * non-negative integer such that (offset + length) <= data.length.
     *
     * @param font     the font object.
     * @param fontSize the size of the font.
     * @param data     the array of characters to be drawn.
     * @param x        the x coordinate of the anchor point.
     * @param y        the y coordinate of the anchor point.
     */
    public void drawChars(VectorFont font, int fontSize, char[] data,
                          int x, int y) {
        drawChars(font, fontSize, data, 0, data.length, x, y, VectorFont.TEXT_DIR_LR);
    }

    /**
     * Draws the specified characters using the current font and color.
     * The offset and length parameters must specify a valid range of characters
     * within the character array data. The offset parameter must be within the
     * range [0..(data.length)], inclusive. The length parameter must be a
     * non-negative integer such that (offset + length) <= data.length.
     *
     * @param font     the font object.
     * @param fontSize the size of the font.
     * @param data     the array of characters to be drawn.
     * @param offset   the start offset in the data..
     * @param length   the number of characters to be drawn.
     * @param x        the x coordinate of the anchor point.
     * @param y        the y coordinate of the anchor point.
     */
    public void drawChars(VectorFont font, int fontSize, char[] data, int offset,
                          int length, int x, int y) {
        drawChars(font, fontSize, data, offset, length, x, y, VectorFont.TEXT_DIR_LR);
    }

    /**
     * Draws the specified characters using the current font and color.
     * The offset and length parameters must specify a valid range of characters
     * within the character array data. The offset parameter must be within the
     * range [0..(data.length)], inclusive. The length parameter must be a
     * non-negative integer such that (offset + length) <= data.length.
     *
     * @param font     the font object.
     * @param fontSize the size of the font.
     * @param data     the array of characters to be drawn.
     * @param offset   the start offset in the data..
     * @param length   the number of characters to be drawn.
     * @param x        the x coordinate of the anchor point.
     * @param y        the y coordinate of the anchor point.
     */
    public void drawChars(VectorFont font, int fontSize, char[] data, int offset,
                          int length, int x, int y, int tdir) {
        drawChars(font, defaultBrush, defaultPen, fontSize, data, offset,
                length, x, y, tdir);
    }

    /**
     * Draws the specified characters using the current font and color.
     * The offset and length parameters must specify a valid range of characters
     * within the character array data. The offset parameter must be within the
     * range [0..(data.length)], inclusive. The length parameter must be a
     * non-negative integer such that (offset + length) <= data.length.
     *
     * @param font     the font object.
     * @param fontSize the size of the font.
     * @param data     the array of characters to be drawn.
     * @param offset   the start offset in the data..
     * @param length   the number of characters to be drawn.
     * @param x        the x coordinate of the anchor point.
     * @param y        the y coordinate of the anchor point.
     */
    public void drawChars(VectorFont font, Brush brush, Pen pen, int fontSize,
                          char[] data, int offset,
                          int length, int x, int y, int tdir) {
        AffineTransform transfrom = new AffineTransform();
        transfrom.translate(x, y);
        IShape[] shapes = font.getGlyphArray(fontSize, data, offset, length,
                tdir, transfrom);
        for (int j = 0; j < shapes.length; j++) {
            if (shapes[j] != null) {
                if (brush != null) {
                    fill(brush, shapes[j]);
                }
                if (pen != null) {
                    draw(pen, shapes[j]);
                }
            }
        }

    }


    /**
     * Set the default pen of the graphics
     *
     * @param pen default pen to be used by the graphcis if any draw method's
     *            pen set to null.
     */
    public final void setDefaultPen(Pen pen) {
        defaultPen = pen;
        setGraphicsFPPenAttribute(pen);

    }


    /**
     * Set the default pen and brush together of the graphics
     *
     * @param pen   default pen to be used by the graphcis if any draw method's
     *              pen set to null.
     * @param brush default brush to be used by the graphics.
     */
    public final void setPenAndBrush(Pen pen, Brush brush) {
        setDefaultPen(pen);
        if (brush != null) {
            synchronized (graphicsFP) {
                graphicsFP.setBrush(brush.wrappedBrushFP);
            }
            defaultBrush = brush;
        }
    }


    /**
     * Get the default pen of the graphics
     *
     * @return pen default pen used by the graphcis.
     */
    public final Pen getDefaultPen() {
        return defaultPen;
    }


    /**
     * Set the default brush of the graphics
     *
     * @param brush default brush to be used by the graphcis if any fill method's
     *              brush set to null.
     */
    public final void setDefaultBrush(Brush brush) {
        if (brush != null) {
            synchronized (graphicsFP) {
                graphicsFP.setBrush(brush.wrappedBrushFP);
            }
            defaultBrush = brush;
        }

    }


    /**
     * Get the default brush of the graphics
     *
     * @return pen default brush used by the graphcis.
     */
    public final Brush getDefaultBrush() {
        return defaultBrush;
    }


    /**
     * Draw a rectangle with given pen
     *
     * @param pen       pen used to draw the rectangle.
     * @param rectangle rectangle to be drawn.
     */
    public final void drawRectangle(Pen pen, Rectangle rectangle) {
        draw(pen, (IShape) rectangle);

    }


    /**
     * Fill a rectangle with given brush
     *
     * @param brush     brush used to fill the rectangle.
     * @param rectangle rectangle to be filled.
     */
    public final void fillRectangle(Brush brush, Rectangle rectangle) {
        fill(brush, (IShape) rectangle);

    }


    /**
     * Draws the outline of an oval. The result is a circle or ellipse that fits
     * within the rectangle specified by the x, y, width, and height arguments.
     *
     * @param pen    the pen used to draw the oval.
     * @param x      the x coordinate of the upper left corner of the oval to be drawn.
     * @param y      the y coordinate of the upper left corner of the oval to be drawn.
     * @param width  the width of the oval to be drawn.
     * @param height the height of the oval to be drawn.
     */
    public final void drawOval(Pen pen, int x, int y, int width, int height) {
        setGraphicsFPPenAttribute(pen);
        synchronized (graphicsFP) {
            graphicsFP.drawOval(x << SingleFP.DECIMAL_BITS,
                    y << SingleFP.DECIMAL_BITS,
                    (x + width) << SingleFP.DECIMAL_BITS,
                    (y + height) << SingleFP.DECIMAL_BITS);
        }
    }


    /**
     * Fills an oval bounded by the specified rectangle with the current color.
     *
     * @param brush  the brush used to fill the oval.
     * @param x      the x coordinate of the upper left corner of the oval to be filled.
     * @param y      the y coordinate of the upper left corner of the oval to be filled.
     * @param width  the width of the oval to be filled.
     * @param height the height of the oval to be filled.
     */
    public final void fillOval(Brush brush, int x, int y, int width, int height) {
        setDefaultBrush(brush);
        synchronized (graphicsFP) {
            graphicsFP.fillOval(x << SingleFP.DECIMAL_BITS,
                    y << SingleFP.DECIMAL_BITS,
                    (x + width) << SingleFP.DECIMAL_BITS,
                    (y + height) << SingleFP.DECIMAL_BITS);
        }
    }


    /**
     * Draws a polyline
     *
     * @param pen      the pen used to draw the polyline.
     * @param polyline the polyline to be drawn.
     */
    public final void drawPolyline(Pen pen, Polyline polyline) {
        draw(pen, (IShape) polyline);
    }


    /**
     * Draws a polygon
     *
     * @param pen     the pen used to draw the polygon.
     * @param polygon the polygon to be drawn.
     */
    public final void drawPolygon(Pen pen, Polygon polygon) {
        draw(pen, (IShape) polygon);
    }


    /**
     * Fill a polygon.
     *
     * @param brush   the brush used to fill the polygon.
     * @param polygon the polygon to be filled.
     */
    public final void fillPolygon(Brush brush, Polygon polygon) {
        fill(brush, (IShape) polygon);
    }


    /**
     * Set the current transformation matrix from user space to device space.
     *
     * @param matrix transformation matrix from user space to device space.
     */
    public final void setAffineTransform(AffineTransform matrix) {
        synchronized (graphicsFP) {
            graphicsFP.setMatrix(Utils.toMatrixFP(matrix));
        }
    }


    /**
     * return the current transformation matrix from user space to device space.
     *
     * @return transformation matrix from user space to device space.
     */
    public final AffineTransform getAffineTransform() {
        synchronized (graphicsFP) {
            return Utils.toMatrix(graphicsFP.getMatrix());
        }
    }


    /**
     * return Returns the current clip of this graphcis object.
     *
     * @return the current clip  rectangle.
     */
    public Rectangle getClip() {
        synchronized (graphicsFP) {
            return new Rectangle(graphicsFP.getClipX(), graphicsFP.getClipY(),
                    graphicsFP.getClipWidth(),
                    graphicsFP.getClipHeight());
        }
    }


    /**
     * Set current clip of this graphcis object.
     *
     * @param x      the x coordinate of the top left point.
     * @param y      the y coordinate of the top left point.
     * @param width  the widht of the clip rectangle.
     * @param height the height of the clip rectangle.
     */
    public void setClip(int x, int y, int width, int height) {
        synchronized (graphicsFP) {
            graphicsFP.setClip(x, y, width, height);
        }
    }


    /**
     * Draws the specified Image object at the specified location and with the
     * specified size.
     *
     * @param imageRGB Image object to draw.
     * @param dstX     x-coordinate of the upper-left corner of the drawn image.
     * @param dstY     y-coordinate of the upper-left corner of the drawn image.
     * @param width    Width of the portion of the source image to draw.
     * @param height   Height of the portion of the source image to draw.
     */
    public final void drawImage(int[] imageRGB, int width, int height,
                                int dstX, int dstY) {

        Rectangle rect1 = getClip();
        Rectangle rect2 = new Rectangle(dstX,
                dstY,
                width + dstX,
                height + dstY);
        Rectangle rect = rect1.intersection(rect2);
        if (!rect.isEmpty()) {
            int[] destBuffer = getRGB();
            int desWidth = graphicsWidth;
            int i;
            int j;
            for (i = 0; i < width; i++) {
                for (j = 0; j < height; j++) {
                    if (((dstX + i) < graphicsWidth) && ((dstY + j) < graphicsHeight) && dstX + i >= 0 && dstY + j >= 0) {
                        destBuffer[dstX + i + (j + dstY) * desWidth] = merge(destBuffer[dstX + i + (j + dstY) * desWidth],
                                imageRGB[i + j * width] & ((0xff) << 24 | 0xFFFFFF));
                    }
                }
            }
        }
    }


    /**
     * Draws the specified Image object at the specified location and with the
     * specified size.
     *
     * @param imageRGB Image object to draw.
     * @param dstX     x-coordinate of the upper-left corner of the drawn image.
     * @param dstY     y-coordinate of the upper-left corner of the drawn image.
     * @param width    Width of the portion of the source image to draw.
     * @param height   Height of the portion of the source image to draw.
     */
    public final void drawImage(int[] imageRGB, int width, int height,
                                int dstX, int dstY, int srcX, int srcY, int dstWidth, int dstHeight) {

        int[] tempRGB = imageRGB;
        dstWidth = Math.min(dstWidth, width - srcX);
        dstHeight = Math.min(dstHeight, height - srcY);
        if (dstWidth != width || dstHeight != height) {
            tempRGB = new int[dstWidth * dstHeight];
            for (int i = 0; i < dstHeight; i++) {
                System.arraycopy(imageRGB, (i + srcY) * width + srcX, tempRGB,
                        i * dstWidth, dstWidth);
            }
        }
        drawImage(tempRGB, dstWidth, dstHeight, dstX, dstY);
    }


    /**
     * Draws the transparent Image object at the specified location and with the
     * specified size
     *
     * @param imageRGB   Image object to draw.
     * @param dstX       x-coordinate of the upper-left corner of the drawn image.
     * @param dstY       y-coordinate of the upper-left corner of the drawn image.
     * @param width      Width of the portion of the source image to draw.
     * @param height     Height of the portion of the source image to draw.
     * @param transpency specify the transparent color of the image.
     */
    public final void drawImage(int[] imageRGB, int width, int height,
                                int dstX, int dstY,
                                int transpency) {

        Rectangle rect1 = getClip();
        Rectangle rect2 = new Rectangle(dstX,
                dstY,
                width + dstX,
                height + dstY);
        Rectangle rect = rect1.intersection(rect2);
        if (!rect.isEmpty()) {
            int[] destBuffer = getRGB();
            int desWidth = graphicsWidth;
            int i;
            int j;
            for (i = 0; i < width; i++) {
                for (j = 0; j < height; j++) {
                    if (((dstX + i) < graphicsWidth) && ((dstY + j) < graphicsHeight) && dstX + i >= 0 && dstY + j >= 0) {
                        if ((imageRGB[i + j * width] & 0x00ffffff)
                                != (transpency & 0x00ffffff)) {
                            destBuffer[dstX + i + (j + dstY) * desWidth] = merge(destBuffer[dstX + i + (j + dstY) * desWidth],
                                    imageRGB[i + j * width] & ((0xff) << 24 | 0xFFFFFF));
                        }
                    }
                }
            }
        }
    }


    /**
     * Draws the transparent Image object at the specified location and with the
     * specified size
     *
     * @param imageRGB   Image object to draw.
     * @param dstX       x-coordinate of the upper-left corner of the drawn image.
     * @param dstY       y-coordinate of the upper-left corner of the drawn image.
     * @param width      Width of the portion of the source image to draw.
     * @param height     Height of the portion of the source image to draw.
     * @param transpency specify the transparent color of the image.
     */
    public final void drawImage(int[] imageRGB, int width, int height,
                                int dstX, int dstY,
                                int transpency, int alpha) {

        Rectangle rect1 = getClip();
        Rectangle rect2 = new Rectangle(dstX,
                dstY,
                width + dstX,
                height + dstY);
        Rectangle rect = rect1.intersection(rect2);
        if (!rect.isEmpty()) {
            int[] destBuffer = getRGB();
            int desWidth = graphicsWidth;
            int i;
            int j;
            for (i = 0; i < width; i++) {
                for (j = 0; j < height; j++) {
                    if (((dstX + i) < graphicsWidth) && ((dstY + j) < graphicsHeight) && dstX + i >= 0 && dstY + j >= 0) {
                        if ((imageRGB[i + j * width] & 0x00ffffff)
                                != (transpency & 0x00ffffff)) {
                            destBuffer[dstX + i + (j + dstY) * desWidth] = merge(destBuffer[dstX + i + (j + dstY) * desWidth],
                                    imageRGB[i + j * width] & ((alpha & 0xff) << 24 | 0xFFFFFF));
                        }
                    }
                }
            }
        }
    }


    /**
     * Set current clip of this graphcis object.
     *
     * @param rectangle the new clip  rectangle.
     */
    public void setClip(Rectangle rectangle) {
        setClip(rectangle.x, rectangle.y,
                rectangle.width,
                rectangle.height);

    }


    /**
     * Clear the graphicis object with given color.
     *
     * @param color the color used to clear the graphics.
     */
    public final void clear(Color color) {
        clear(color.value);
    }


    /**
     * Clear the graphics content with given color.
     */
    public void clear() {
        clear(0x00FFFFFF);
    }


    /**
     * Clear the graphics content with given color.
     *
     * @param color the clear color.
     */
    public void clear(int color) {
        synchronized (graphicsFP) {
            graphicsFP.clear(color);
        }
    }


    /**
     * Returns the content of this image as ARGB array.
     *
     * @return the ARGB array of the image content.
     */
    public int[] getRGB() {
        synchronized (graphicsFP) {
            return graphicsFP.getRGB();
        }
    }

    private boolean isPrepared = false;

    private Pixmap pixMap=null;
    /**
     * graphics width
     */
    private final int graphicsWidth;
    /**
     * graphics height.
     */
    private final int graphicsHeight;
    /**
     * the wraped graphicsFP object.
     */
    private final GraphicsFP graphicsFP;
    /**
     * default pen for drawing.
     */
    private Pen defaultPen = null;
    /**
     * default brush for filling.
     */
    private Brush defaultBrush = null;


    /**
     * Union the shape into single path.
     *
     * @param shape the shape object.
     * @return a single path.
     */
    static IShape union(IShape shape) {
        IPathIterator pathIterator = shape.getPathIterator(null);
        Area area = new Area();
        int[] coords = new int[6];
        int type;
        Path path = new Path();
        while (!pathIterator.isDone()) {
            type = pathIterator.currentSegment(coords);
            switch (type) {
                case IPathIterator.SEG_CLOSE:
                    path.closePath();
                    area.add(new Area(path));
                    path = new Path();
                    break;
                case IPathIterator.SEG_CUBICTO:
                    path.curveTo(coords[0], coords[1], coords[2], coords[3],
                            coords[4], coords[5]);
                    break;
                case IPathIterator.SEG_LINETO:
                    path.lineTo(coords[0], coords[1]);
                    break;
                case IPathIterator.SEG_MOVETO:
                    path.moveTo(coords[0], coords[1]);
                    break;
                case IPathIterator.SEG_QUADTO:
                    path.quadTo(coords[0], coords[1], coords[2], coords[3]);
                    break;
            }

            pathIterator.next();

        }
        return area;
    }


    /**
     * set graphics pen attribute.
     *
     * @param pen
     */
    private void setGraphicsFPPenAttribute(Pen pen) {
        if (pen != null) {
            defaultPen = pen;
            PenFP penFP = graphicsFP.getPen();
            penFP.endCap = pen.cap;
            penFP.startCap = pen.cap;
            penFP.lineJoin = pen.join;
            penFP.width = pen.width << SingleFP.DECIMAL_BITS;
            if (pen.brush != null) {
                penFP.brush = pen.brush.wrappedBrushFP;

            } else {
                penFP.brush = new SolidBrushFP(pen.color.value);
            }
            if (pen.dash != null) {
                penFP.dashArray = new int[pen.dash.length - pen.dash_phase];
                for (int i = pen.dash_phase; i < pen.dash.length - pen.dash_phase; i++) {
                    penFP.dashArray[i - pen.dash_phase] =
                            pen.dash[i]
                                    << SingleFP.DECIMAL_BITS;
                }
            } else {
                penFP.dashArray = null;
            }
        }

    }


    

    /**
     * @param color1
     * @param color2
     * @return
     */
    private static int merge(int color1, int color2) {
        int a2 = (color2 >> 24) & 0xFF;
        if (a2 == 0xFF || color1 == 0x0) {
            return color2;
        } else if (a2 == 0) {
            return color1;
        } else {
            int a1 = 0xFF - ((color1 >> 24) & 0xFF);
            int a3 = 0xFF - a2;
            int b1 = color1 & 0xFF;
            int g1 = (color1 >> 8) & 0xFF;
            int r1 = (color1 >> 16) & 0xFF;
            int b2 = color2 & 0xFF;
            int g2 = (color2 >> 8) & 0xFF;
            int r2 = (color2 >> 16) & 0xFF;

            int Ca = (0xFF * 0xFF - a1 * a3) >> 8;
            int Cr = (r1 * a3 + r2 * a2) >> 8;
            int Cg = (g1 * a3 + g2 * a2) >> 8;
            int Cb = (b1 * a3 + b2 * a2) >> 8;
            return Ca << 24 | Cr << 16 | Cg << 8 | Cb;
        }
    }
}
