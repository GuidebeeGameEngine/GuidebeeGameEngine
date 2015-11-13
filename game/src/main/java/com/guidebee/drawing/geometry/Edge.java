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
final class Edge {

    static final int INIT_PARTS = 4;
    static final int GROW_PARTS = 10;
    Curve curve;
    int ctag;
    int etag;
    double activey;
    int equivalence;

    public Edge(Curve c, int ctag) {
        this(c, ctag, AreaOp.ETAG_IGNORE);
    }

    public Edge(Curve c, int ctag, int etag) {
        this.curve = c;
        this.ctag = ctag;
        this.etag = etag;
    }

    public Curve getCurve() {
        return curve;
    }

    public int getCurveTag() {
        return ctag;
    }

    public int getEdgeTag() {
        return etag;
    }

    public void setEdgeTag(int etag) {
        this.etag = etag;
    }

    public int getEquivalence() {
        return equivalence;
    }

    public void setEquivalence(int eq) {
        equivalence = eq;
    }

    private Edge lastEdge;
    private int lastResult;
    private double lastLimit;

    public int compareTo(Edge other, double yrange[]) {
        if (other == lastEdge && yrange[0] < lastLimit) {
            if (yrange[1] > lastLimit) {
                yrange[1] = lastLimit;
            }
            return lastResult;
        }
        if (this == other.lastEdge && yrange[0] < other.lastLimit) {
            if (yrange[1] > other.lastLimit) {
                yrange[1] = other.lastLimit;
            }
            return 0 - other.lastResult;
        }
        //long start = System.currentTimeMillis();
        int ret = curve.compareTo(other.curve, yrange);
        //long end = System.currentTimeMillis();
    /*
        System.out.println("compare: "+
        ((System.identityHashCode(this) <
        System.identityHashCode(other))
        ? this+" to "+other
        : other+" to "+this)+
        " == "+ret+" at "+yrange[1]+
        " in "+(end-start)+"ms");
         */
        lastEdge = other;
        lastLimit = yrange[1];
        lastResult = ret;
        return ret;
    }

    public void record(double yend, int etag) {
        this.activey = yend;
        this.etag = etag;
    }

    public boolean isActiveFor(double y, int etag) {
        return (this.etag == etag && this.activey >= y);
    }

    public String toString() {
        return ("Edge[" + curve + ", " +
                (ctag == AreaOp.CTAG_LEFT ? "L" : "R") + ", "
                + (etag == AreaOp.ETAG_ENTER ? "I" :
                (etag == AreaOp.ETAG_EXIT ? "O" : "N")) + "]");
    }
}
