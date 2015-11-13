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
 * the class uses line to draw a sketch for a given path.
 *
 * @author James Shen.
 */
public class GraphicsPathSketchFP implements IGraphicsPathIteratorFP {


    /**
     * Get the current point.
     *
     * @return
     */
    public PointFP currentPoint() {
        return currPoint;
    }


    /**
     * get the start point.
     *
     * @return
     */
    public PointFP startPoint() {
        return startPoint;
    }


    /**
     * Default constructor.
     */
    public GraphicsPathSketchFP() {
    }


    /**
     *
     */
    public void begin() {
        started = false;
    }


    /**
     *
     */
    public void end() {
    }


    /**
     * @param point
     */
    public void moveTo(PointFP point) {
        if (!started) {
            startPoint.reset(point);
            started = true;
        }
        currPoint.reset(point);
    }


    /**
     * @param point
     */
    public void lineTo(PointFP point) {
        currPoint.reset(point);
    }


    /**
     * @param control
     * @param point
     */
    public void quadTo(PointFP control, PointFP point) {
        // Compute forward difference values for a quadratic
        // curve of type A*(1-t)^2 + 2*B*t*(1-t) + C*t^2

        PointFP f = new PointFP(currPoint);
        PointFP tmp = new PointFP((currPoint.x - control.x * 2 + point.x)
                / SUBDIVIDE2, (currPoint.y - control.y * 2 + point.y)
                / SUBDIVIDE2);
        PointFP ddf = new PointFP(tmp.x * 2, tmp.y * 2);
        PointFP df = new PointFP(tmp.x + (control.x - currPoint.x) * 2
                / SUBDIVIDE, tmp.y + (control.y - currPoint.y) * 2 / SUBDIVIDE);

        for (int c = 0; c < SUBDIVIDE - 1; c++) {
            f.add(df);
            df.add(ddf);
            lineTo(f);
        }

        // We specify the last point manually since
        // we obtain rounding errors during the
        // forward difference computation.
        lineTo(point);
    }


    /**
     * @inheritDoc
     */
    public void curveTo(PointFP control1, PointFP control2, PointFP point) {
        PointFP tmp1 = new PointFP(currPoint.x - control1.x * 2 + control2.x,
                currPoint.y - control1.y * 2 + control2.y);
        PointFP tmp2 = new PointFP((control1.x - control2.x) * 3 - currPoint.x
                + point.x, (control1.y - control2.y) * 3 - currPoint.y + point.y);

        PointFP f = new PointFP(currPoint);
        PointFP df = new PointFP((control1.x - currPoint.x) * 3 / SUBDIVIDE
                + tmp1.x * 3 / SUBDIVIDE2 + tmp2.x / SUBDIVIDE3,
                (control1.y - currPoint.y) * 3 / SUBDIVIDE + tmp1.y * 3
                        / SUBDIVIDE2 + tmp2.y / SUBDIVIDE3);
        PointFP ddf = new PointFP(tmp1.x * 6 / SUBDIVIDE2 + tmp2.x * 6
                / SUBDIVIDE3, tmp1.y * 6 / SUBDIVIDE2 + tmp2.y * 6 / SUBDIVIDE3);
        PointFP dddf = new PointFP(tmp2.x * 6
                / SUBDIVIDE3, tmp2.y * 6 / SUBDIVIDE3);

        for (int c = 0; c < SUBDIVIDE - 1; c++) {
            f.add(df);
            df.add(ddf);
            ddf.add(dddf);
            lineTo(f);
        }

        lineTo(point);
    }


    /**
     * @inheritDoc
     */
    public void close() {
        // Connect start point with end point
        lineTo(startPoint);
        started = false;
    }

    protected final static int SUBDIVIDE = 24;
    protected final static int SUBDIVIDE2 = SUBDIVIDE * SUBDIVIDE;
    protected final static int SUBDIVIDE3 = SUBDIVIDE2 * SUBDIVIDE;
    protected final PointFP startPoint = new PointFP();
    protected final PointFP currPoint = new PointFP();
    protected boolean started;
}