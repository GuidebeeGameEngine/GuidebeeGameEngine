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
 * Defines objects used to fill the interiors of graphical shapes such as
 * rectangles, ellipses, pies, polygons, and paths.
 *
 * @author James Shen.
 */
public abstract class BrushFP {

    /**
     * Use the terminal colors to fill the remaining area.
     */
    public final static int NO_CYCLE = 0;

    /**
     * Cycle the gradient colors start-to-end, end-to-start
     * to fill the remaining area.
     */
    public final static int REFLECT = 1;

    /**
     * Cycle the gradient colors start-to-end, start-to-end
     * to fill the remaining area.
     */
    public final static int REPEAT = 2;

    /**
     * Fill mode of the brush.
     */
    public int fillMode = REPEAT;


    /**
     * Get the matrix associated with this brush.
     *
     * @return the matrix.
     */
    public MatrixFP getMatrix() {
        return matrix;
    }


    /**
     * Set the matrix for this brush.
     *
     * @param value a new matrix.
     */
    public void setMatrix(MatrixFP value) {
        matrix = new MatrixFP(value);
        matrix.invert();
    }


    /**
     * Transform with a new matrix.
     *
     * @param m1 a new matrix.
     */
    public void transform(MatrixFP m1) {
        MatrixFP m = new MatrixFP(m1);
        m.invert();
        if (matrix == null) {
            matrix = m;
        } else {
            matrix.multiply(m);
        }
    }


    /**
     * Check if it's a mono color brush.
     *
     * @return true if it's mono color brush.
     */
    public abstract boolean isMonoColor();


    /**
     * Get the color value at given position.
     *
     * @param x           x coordinate.
     * @param y           y coordinate.
     * @param singlePoint single point or not.
     * @return the color value.
     */
    public abstract int getColorAt(int x, int y, boolean singlePoint);


    /**
     * Get the next color for this brush.
     *
     * @return the next color.
     */
    public abstract int getNextColor();


    /**
     * get the matrix associated with the graphics object.
     *
     * @return the matrix of the graphics object.
     */
    MatrixFP getGraphicsMatrix() {
        return graphicsMatrix;
    }


    /**
     * Set the graphics matrix.
     *
     * @param value the graphics matrix.
     */
    void setGraphicsMatrix(MatrixFP value) {
        graphicsMatrix = new MatrixFP(value);
        graphicsMatrix.invert();
        finalMatrix = new MatrixFP(graphicsMatrix);
        if (matrix != null) {
            finalMatrix.multiply(matrix);
        }
    }

    /**
     * Brush matrix.
     */
    protected MatrixFP matrix;

    /**
     * Graphics matrix.
     */
    protected MatrixFP graphicsMatrix;

    /**
     * The combined matrix.
     */
    protected MatrixFP finalMatrix;
}