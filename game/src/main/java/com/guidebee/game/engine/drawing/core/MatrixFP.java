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
 * affine matrix in fixed point format.
 *
 * @author James Shen.
 */
public class MatrixFP {

    /**
     * Scale X factor.
     */
    public int scaleX = 0; // ScaleX

    /**
     * Scale Y factor.
     */
    public int scaleY = 0; // ScaleY

    /**
     * Rotate/Shear X factor.
     */
    public int rotateX = 0; // RotateSkewX

    /**
     * Rotate/Shear Y factor.
     */
    public int rotateY = 0; // RotateSkewY

    /**
     * Translate X.
     */
    public int translateX = 0; // TranslateX

    /**
     * Translate Y.
     */
    public int translateY = 0; // TranslateY


    /**
     * Constructor.
     */
    public MatrixFP() {
        scaleX = scaleY = SingleFP.ONE;
    }


    /**
     * Constructor.
     *
     * @param ff_sx
     * @param ff_sy
     * @param ff_rx
     * @param ff_ry
     * @param ff_tx
     * @param ff_ty
     */
    public MatrixFP(int ff_sx, int ff_sy, int ff_rx, int ff_ry,
                    int ff_tx, int ff_ty) {
        reset(ff_sx, ff_sy, ff_rx, ff_ry, ff_tx, ff_ty);
    }


    /**
     * Copy constructor.
     *
     * @param m
     */
    public MatrixFP(MatrixFP m) {
        reset(m.scaleX, m.scaleY, m.rotateX, m.rotateY, m.translateX, m.translateY);
    }


    /**
     * reset to identity matrix.
     */
    public void reset() {
        reset(SingleFP.ONE, SingleFP.ONE, 0, 0, 0, 0);
    }


    /**
     * Identiry matrix.
     *
     * @return
     */
    public static MatrixFP IDENTITY = new MatrixFP(SingleFP.ONE, SingleFP.ONE, 0, 0, 0, 0);


    /**
     * Check if it's identity matrix.
     *
     * @return
     */
    public boolean isIdentity() {
        return scaleX == SingleFP.ONE && scaleY == SingleFP.ONE && rotateX == 0
                && rotateY == 0 && translateX == 0 && translateY == 0;
    }


    /**
     * check to see if its invertiable.
     *
     * @return
     */
    public boolean isInvertible() {
        return determinant() != 0;
    }


    /**
     * reset the matrix to give value.
     *
     * @param ff_sx
     * @param ff_sy
     * @param ff_rx
     * @param ff_ry
     * @param ff_tx
     * @param ff_ty
     */
    public void reset(int ff_sx, int ff_sy, int ff_rx, int ff_ry,
                      int ff_tx, int ff_ty) {
        this.scaleX = ff_sx;
        this.scaleY = ff_sy;
        this.rotateX = ff_rx;
        this.rotateY = ff_ry;
        this.translateX = ff_tx;
        this.translateY = ff_ty;
    }


    /**
     * rotate operation.
     *
     * @param ff_ang
     * @return
     */
    public MatrixFP rotate(int ff_ang) {
        int ff_sin = MathFP.sin(ff_ang);
        int ff_cos = MathFP.cos(ff_ang);
        return multiply(new MatrixFP(ff_cos, ff_cos, ff_sin, -ff_sin, 0, 0));
    }


    /**
     * Shear or rotate operation.
     *
     * @param ff_rx
     * @param ff_ry
     * @return
     */
    public MatrixFP rotateSkew(int ff_rx, int ff_ry) {
        return multiply(new MatrixFP(SingleFP.ONE, SingleFP.ONE, ff_rx, ff_ry, 0, 0));
    }


    /**
     * translate operation.
     *
     * @param ff_dx
     * @param ff_dy
     * @return
     */
    public MatrixFP translate(int ff_dx, int ff_dy) {
        this.translateX += ff_dx;
        this.translateY += ff_dy;
        return this;
    }


    /**
     * Scale operation.
     *
     * @param ff_sx
     * @param ff_sy
     * @return
     */
    public MatrixFP scale(int ff_sx, int ff_sy) {
        reset(MathFP.mul(ff_sx, this.scaleX), MathFP.mul(ff_sy, this.scaleY),
                MathFP.mul(ff_sy, this.rotateX), MathFP.mul(ff_sx, this.rotateY),
                MathFP.mul(ff_sx, this.translateX), MathFP.mul(ff_sy, this.translateY));
        return this;
    }


    /**
     * multipy with another matrix.
     *
     * @param m
     * @return
     */
    public MatrixFP multiply(MatrixFP m) {
        reset(MathFP.mul(m.scaleX, scaleX) + MathFP.mul(m.rotateY, rotateX),
                MathFP.mul(m.rotateX, rotateY) + MathFP.mul(m.scaleY, scaleY),
                MathFP.mul(m.rotateX, scaleX) + MathFP.mul(m.scaleY, rotateX),
                MathFP.mul(m.scaleX, rotateY) + MathFP.mul(m.rotateY, scaleY),
                MathFP.mul(m.scaleX, translateX) + MathFP.mul(m.rotateY, translateY) + m.translateX,
                MathFP.mul(m.rotateX, translateX) + MathFP.mul(m.scaleY, translateY) + m.translateY);
        return this;
    }


    /**
     * compare with another object.
     *
     * @param obj
     * @return
     */
    public boolean equals(Object obj) {
        if (obj instanceof MatrixFP) {
            MatrixFP m = (MatrixFP) obj;
            return m != null && m.rotateX == rotateX && m.rotateY == rotateY
                    && m.scaleX == scaleX && m.scaleY == scaleY && m.translateX == translateX && m.translateY == translateY;
        }
        return false;
    }


    /**
     * Returns the hashcode for this transform.
     *
     * @return a hash code for this transform.
     */
    public int hashCode() {
        return rotateX << 24 + rotateY << 20 + scaleX << 16 + scaleY << 8 + translateX << 4 + translateY;

    }


    /**
     * calculat the determinat of the matrix.
     *
     * @return
     */
    private int determinant() {
        int ff_det;
        ff_det = MathFP.mul(scaleX, scaleY) - MathFP.mul(rotateX, rotateY);
        return ff_det;
    }


    /**
     * invert the matrix.
     *
     * @return
     */
    public MatrixFP invert() {
        int ff_det = determinant();
        if (ff_det == 0) {
            reset();
        } else {
            int ff_sx_new = MathFP.div(scaleY, ff_det);
            int ff_sy_new = MathFP.div(scaleX, ff_det);
            int ff_rx_new = -MathFP.div(rotateX, ff_det);
            int ff_ry_new = -MathFP.div(rotateY, ff_det);
            int ff_tx_new = MathFP.div(MathFP.mul(translateY, rotateY)
                    - MathFP.mul(translateX, scaleY), ff_det);
            int ff_ty_new = -MathFP.div(MathFP.mul(translateY, scaleX)
                    - MathFP.mul(translateX, rotateX), ff_det);
            reset(ff_sx_new, ff_sy_new, ff_rx_new, ff_ry_new,
                    ff_tx_new, ff_ty_new);
        }
        return this;
    }


    /**
     * to a string.
     *
     * @return
     */
    public String toString() {
        return " Matrix(sx,sy,rx,ry,tx,ty)=(" + new SingleFP(scaleX)
                + "," + new SingleFP(scaleY) + "," + new SingleFP(rotateX)
                + "," + new SingleFP(rotateY) + "," + new SingleFP(translateX)
                + "," + new SingleFP(translateY) + ")";
    }
}
