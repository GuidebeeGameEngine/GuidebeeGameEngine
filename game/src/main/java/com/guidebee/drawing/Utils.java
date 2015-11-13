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
import com.guidebee.drawing.geometry.Point;
import com.guidebee.drawing.geometry.Rectangle;
import com.guidebee.game.engine.drawing.core.MatrixFP;
import com.guidebee.game.engine.drawing.core.PointFP;
import com.guidebee.game.engine.drawing.core.RectangleFP;
import com.guidebee.game.engine.drawing.core.SingleFP;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * utility functions to convert class from drawing to drawingfp.
 *
 * @author James Shen.
 */
abstract class Utils {


    /**
     * from Affline Matrix to it's FP matrix
     *
     * @param matrix
     * @return
     */
    static MatrixFP toMatrixFP(AffineTransform matrix) {
        if (matrix == null) {
            return null;
        }
        if (matrix.isIdentity()) {
            return MatrixFP.IDENTITY;
        }

        MatrixFP matrixFP = new MatrixFP(SingleFP.fromDouble(matrix.getScaleX()),
                SingleFP.fromDouble(matrix.getScaleY()),
                SingleFP.fromDouble(-matrix.getShearX()),
                SingleFP.fromDouble(-matrix.getShearY()),
                SingleFP.fromDouble(matrix.getTranslateX()),
                SingleFP.fromDouble(matrix.getTranslateY()));
        return matrixFP;
    }


    /**
     * from FP matrix to affine matrix
     *
     * @param matrixFP
     * @return
     */
    static AffineTransform toMatrix(MatrixFP matrixFP) {
        if (matrixFP == null) {
            return null;
        }
        if (matrixFP.isIdentity()) {
            return new AffineTransform();
        }
        AffineTransform matrix = new AffineTransform(SingleFP.toDouble(matrixFP.scaleX),
                SingleFP.toDouble(-matrixFP.rotateX),
                SingleFP.toDouble(-matrixFP.rotateY),
                SingleFP.toDouble(matrixFP.scaleY),
                SingleFP.toDouble(matrixFP.translateX),
                SingleFP.toDouble(matrixFP.translateY));
        return matrix;
    }


    /**
     * from rectange to rectangeFP
     *
     * @param rect
     * @return
     */
    static RectangleFP toRectangleFP(Rectangle rect) {
        return new RectangleFP(
                SingleFP.fromInt(rect.getMinX()),
                SingleFP.fromInt(rect.getMinY()),
                SingleFP.fromInt(rect.getMaxX()),
                SingleFP.fromInt(rect.getMaxY()));
    }


    /**
     * from point to pointFP
     *
     * @param pnt
     * @return
     */
    static PointFP toPointFP(Point pnt) {
        return new PointFP(SingleFP.fromInt(pnt.x), SingleFP.fromInt(pnt.y));
    }


    /**
     * from PointFP to Point
     *
     * @param pnt
     * @return
     */
    static Point toPoint(PointFP pnt) {
        return new Point(SingleFP.toInt(pnt.x), SingleFP.toInt(pnt.y));
    }


    /**
     * from Point array to pintFP array
     *
     * @param pnts
     * @return
     */
    static PointFP[] toPointFPArray(Point[] pnts) {
        PointFP[] result = new PointFP[pnts.length];
        for (int i = 0; i < pnts.length; i++) {
            result[i] = toPointFP(pnts[i]);
        }
        return result;
    }


    /**
     * from PointFP array to point array
     *
     * @param pnts
     * @return
     */
    static Point[] ToPointArray(PointFP[] pnts) {
        Point[] result = new Point[pnts.length];
        for (int i = 0; i < pnts.length; i++) {
            result[i] = toPoint(pnts[i]);
        }
        return result;
    }
}
