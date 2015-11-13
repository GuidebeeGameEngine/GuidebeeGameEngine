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
import com.guidebee.game.engine.drawing.core.BrushFP;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Classes derived from this abstract base class define objects used to fill the
 * interiors of graphical shapes such as rectangles, ellipses, pies, polygons,
 * and paths.
 *
 * @author James Shen.
 */
public abstract class Brush {

    /**
     * Use the terminal colors to fill the remaining area.
     */
    public final static int NO_CYCLE = BrushFP.NO_CYCLE;

    /**
     * Cycle the gradient colors start-to-end, end-to-start
     * to fill the remaining area.
     */
    public final static int REFLECT = BrushFP.REFLECT;

    /**
     * Cycle the gradient colors start-to-end, start-to-end
     * to fill the remaining area.
     */
    public final static int REPEAT = BrushFP.REPEAT;


    /**
     * Default private constructor to avoid this class be intantialized.
     */
    protected Brush() {

    }


    /**
     * Get the matrix associated with this brush.
     *
     * @return the matrix.
     */
    public AffineTransform getMatrix() {
        return Utils.toMatrix(wrappedBrushFP.getMatrix());
    }


    /**
     * Set the matrix for this brush.
     *
     * @param value a new matrix.
     */
    public void setMatrix(AffineTransform value) {
        wrappedBrushFP.setMatrix(Utils.toMatrixFP(value));
    }


    /**
     * Transform with a new matrix.
     *
     * @param m1 a new matrix.
     */
    public void transform(AffineTransform m1) {
        wrappedBrushFP.transform(Utils.toMatrixFP(m1));
    }


    /**
     * Returns the transparency mode for this <code>Color</code>.  This is
     * required to implement the <code>Paint</code> interface.
     *
     * @return this <code>Color</code> object's transparency mode.
     */
    public abstract int getTransparency();

    /**
     * internal wrapped brush in the drawing core package.
     */
    protected BrushFP wrappedBrushFP = null;

}
