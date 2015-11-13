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
 * An class that describes a gradient, composed of gradient stops.
 * Classes that inherit from GradientBrush describe different ways of
 * interpreting gradient stops.
 * be inherited.
 *
 * @author James Shen.
 */
public class RadialGradientBrushFP extends BrushFP {


    /**
     * Create the gradient brush.
     *
     * @param ff_x  the top left coordinate.
     * @param ff_y  the top left coordinate.
     * @param ff_angle the angle for this gradient.
     */
    public RadialGradientBrushFP(int ff_x, int ff_y, int ff_radius,
                                 int ff_angle) {

        matrix = new MatrixFP();
        centerPt.reset(ff_x,
                ff_y);
        matrix.translate(-centerPt.x, -centerPt.y);
        matrix.rotate(-ff_angle);
        this.ff_radius = ff_radius;

    }


    /**
     * Set gradient color at given ratio.
     *
     * @param ratio the ratio value.
     * @param color the color value.
     */
    public void setGradientColor(int ratio, int color) {
        ratio = ratio >> SingleFP.DECIMAL_BITS - RATIO_BITS;
        int i;
        ratio = ratio < 0 ? 0 : (ratio > RATIO_MAX ? RATIO_MAX : ratio);
        if (ratioCount == ratios.length) {
            int[] rs = new int[ratioCount + 16];
            System.arraycopy(ratios, 0, rs, 0, ratioCount);
            ratios = rs;
        }
        gradientColors[ratio] = color;
        for (i = ratioCount; i > 0; i--) {
            if (ratio >= ratios[i - 1]) {
                break;
            }
        }
        if (!(i > 0 && ratio == ratios[i])) {
            if (i < ratioCount) {
                System.arraycopy(ratios, i, ratios, i + 1, ratioCount - i);
            }
            ratios[i] = ratio;
            ratioCount++;
        }
    }


    /**
     * update the gradient table.
     */
    public void updateGradientTable() {
        if (ratioCount == 0) {
            return;
        }
        int i;
        for (i = 0; i < ratios[0]; i++) {
            gradientColors[i] = gradientColors[ratios[0]];
        }
        for (i = 1; i < ratioCount; i++) {
            int r1 = ratios[i - 1];
            int r2 = ratios[i];
            for (int j = r1 + 1; j < r2; j++) {
                gradientColors[j] = interpolate(gradientColors[r1],
                        gradientColors[r2], 256 * (j - r1) / (r2 - r1));
            }
        }
        for (i = ratios[ratioCount - 1]; i <= RATIO_MAX; i++) {
            gradientColors[i] = gradientColors[ratios[ratioCount - 1]];
        }
    }


    /**
     * @return always return false.
     * @inheritDoc
     */
    public boolean isMonoColor() {
        return false;
    }


    /**
     * @param x           the x coordinate
     * @param y           the y coordinate
     * @param singlePoint
     * @return the color at given position.
     * @inheritDoc
     */
    public int getColorAt(int x, int y, boolean singlePoint) {
        int pos;
        PointFP p = new PointFP(x << SingleFP.DECIMAL_BITS,
                y << SingleFP.DECIMAL_BITS);
        nextPt.x = p.x + SingleFP.ONE;
        nextPt.y = p.y;
        PointFP newCenterPt = new PointFP(centerPt);
        if (finalMatrix != null) {
            p.transform(finalMatrix);
            //newCenterPt.transform(finalMatrix);

        }
        ff_currpos = MathFP.div(PointFP.distance(p.x - newCenterPt.x,
                p.y - newCenterPt.y), ff_radius);
        pos = ff_currpos >> SingleFP.DECIMAL_BITS - RATIO_BITS;

        switch (fillMode) {
            case REFLECT:
                pos = pos % (RATIO_MAX * 2);
                pos = pos < 0 ? pos + RATIO_MAX * 2 : pos;
                pos = (pos < RATIO_MAX) ? pos : RATIO_MAX * 2 - pos;
                break;
            case REPEAT:
                pos = pos % RATIO_MAX;
                pos = pos < 0 ? pos + RATIO_MAX : pos;
                break;
            case NO_CYCLE:
                pos = pos < 0 ? 0 : (pos > RATIO_MAX ? RATIO_MAX : pos);
                break;
        }

        return gradientColors[pos];
    }


    /**
     * @return next color of the gradient brush.
     * @inheritDoc
     */
    public int getNextColor() {
        int pos;
        PointFP p = new PointFP(nextPt);
        nextPt.x = p.x + SingleFP.ONE;
        nextPt.y = p.y;
        PointFP newCenterPt = new PointFP(centerPt);
        if (finalMatrix != null) {
            p.transform(finalMatrix);
            //newCenterPt.transform(finalMatrix);

        }
        ff_currpos = MathFP.div(PointFP.distance(p.x - newCenterPt.x,
                p.y - newCenterPt.y), ff_radius);
        pos = ff_currpos >> SingleFP.DECIMAL_BITS - RATIO_BITS;

        switch (fillMode) {
            case REFLECT:
                pos = pos % (RATIO_MAX * 2);
                pos = pos < 0 ? pos + RATIO_MAX * 2 : pos;
                pos = (pos < RATIO_MAX) ? pos : RATIO_MAX * 2 - pos;
                break;
            case REPEAT:
                pos = pos % RATIO_MAX;
                pos = pos < 0 ? pos + RATIO_MAX : pos;
                break;
            case NO_CYCLE:
                pos = pos < 0 ? 0 : (pos > RATIO_MAX ? RATIO_MAX : pos);
                break;
        }

        return gradientColors[pos];
    }

    private final static int RATIO_BITS = 10;
    private final static int RATIO_MAX = (1 << RATIO_BITS) - 1;

    private int[] gradientColors = new int[1 << RATIO_BITS];
    private int[] ratios = new int[64];
    private int ratioCount = 0;
    private int ff_radius;
    private int ff_currpos;
    protected PointFP centerPt = new PointFP();
    private final PointFP nextPt = new PointFP(0, 0);


    /**
     * @param a
     * @param b
     * @param pos
     * @return
     */
    private static int interpolate(int a, int b, int pos) {
        int p2 = pos & 0xFF;
        int p1 = 0xFF - p2;
        int ca = ((a >> 24) & 0xFF) * p1 + ((b >> 24) & 0xFF) * p2;
        int cr = ((a >> 16) & 0xFF) * p1 + ((b >> 16) & 0xFF) * p2;
        int cg = ((a >> 8) & 0xFF) * p1 + ((b >> 8) & 0xFF) * p2;
        int cb = (a & 0xFF) * p1 + (b & 0xFF) * p2;
        return ((ca >> 8) << 24) | ((cr >> 8) << 16) | ((cg >> 8) << 8) | ((cb >> 8));
    }

}