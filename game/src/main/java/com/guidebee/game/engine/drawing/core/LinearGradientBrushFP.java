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
public class LinearGradientBrushFP extends BrushFP {


    /**
     * Create the gradient brush.
     *
     * @param ff_xmin  the top left coordinate.
     * @param ff_ymin  the top left coordinate.
     * @param ff_xmax  the bottom right coordinate.
     * @param ff_ymax  the bottom right coordinate.
     * @param ff_angle the angle for this gradient.
     */
    public LinearGradientBrushFP(int ff_xmin, int ff_ymin, int ff_xmax, int ff_ymax,
                                 int ff_angle) {
        bounds.reset(ff_xmin, ff_ymin,
                ff_xmax == ff_xmin ? ff_xmin + 1 : ff_xmax,
                ff_ymax == ff_ymin ? ff_ymin + 1 : ff_ymax);
        matrix = new MatrixFP();
        centerPt.reset(ff_xmin + (ff_xmax - ff_xmin) / 2,
                ff_ymin + (ff_ymax - ff_ymin) / 2);
        matrix.translate(-centerPt.x, -centerPt.y);
        matrix.rotate(-ff_angle);
        //matrix.translate((ff_xmin + ff_xmax) / 2,(ff_ymin + ff_ymax) / 2);

        int ff_ang = MathFP.atan(MathFP.div(bounds.getHeight(),
                bounds.getWidth() == 0 ? 1 : bounds.getWidth()));
        int ff_len = PointFP.distance(bounds.getHeight(), bounds.getWidth());
        ff_length = MathFP.mul(ff_len, MathFP.max(
                MathFP.abs(MathFP.cos(ff_angle - ff_ang)),
                MathFP.abs(MathFP.cos(ff_angle + ff_ang))));

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
        PointFP p1 = null;
        if (!singlePoint) {
            p1 = new PointFP(p.x + SingleFP.ONE, p.y);
        }
        if (finalMatrix != null) {
            p.transform(finalMatrix);
            if (!singlePoint) {
                p1.transform(finalMatrix);
            }
        }

        int v = p.x + ff_length / 2;
        ff_currpos = (int) (((long) v << RATIO_BITS + SingleFP.DECIMAL_BITS)
                / ff_length);
        if (!singlePoint) {
            ff_deltapos = (int) (((long) (p1.x - p.x)
                    << RATIO_BITS + SingleFP.DECIMAL_BITS) / ff_length);
        }
        pos = ff_currpos >> SingleFP.DECIMAL_BITS;
        pos -= 512;

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
        ff_currpos += ff_deltapos;
        int pos = ff_currpos >> SingleFP.DECIMAL_BITS;

        pos -= 512;

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
    private int ff_length;
    private int ff_currpos;
    private int ff_deltapos;
    protected PointFP centerPt = new PointFP();
    protected RectangleFP bounds = new RectangleFP();


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