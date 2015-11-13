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
 * provide dash support.
 *
 * @author James Shen.
 */
public class GraphicsPathDasherFP extends GraphicsPathSketchFP {


    /**
     * Constructor.
     *
     * @param from      the path which need to be dashed.
     * @param dashArray the dash array.
     * @param offset    from where the dash starts.
     */
    public GraphicsPathDasherFP(GraphicsPathFP from, int[] dashArray, int offset) {
        fromPath = new GraphicsPathFP(from);
        int arrayLength = dashArray.length - offset;
        if (arrayLength > 1) {
            pnts = new PointFP[BLOCKSIZE];
            cmds = new int[BLOCKSIZE];
            this.dashArray = new int[dashArray.length - offset];
            System.arraycopy(dashArray, offset,
                    this.dashArray, 0, dashArray.length);
            VisitPath(this);
        }

    }


    /**
     * return the dashed path, if the dash array is null, return the path
     * unchanged.
     *
     * @return the dash path.
     */
    public GraphicsPathFP GetDashedGraphicsPath() {
        if (dashArray == null) {
            return fromPath;
        }

        GraphicsPathFP dashedPath = new GraphicsPathFP();
        LineFP lineFP = new LineFP();
        int j = 0;
        for (int i = 0; i < cmdsSize; i++) {
            switch (cmds[i]) {

                case CMD_MOVETO:
                    dashedPath.addMoveTo(pnts[j++]);
                    break;
                case CMD_LINETO: {
                    int pointIndex = j;
                    lineFP.reset(pnts[pointIndex - 1], pnts[pointIndex]);
                    DashLine(dashedPath, lineFP);
                    j++;
                }
                break;
                case CMD_CLOSE:
                    dashedPath.addClose();
                    break;
            }
        }

        return dashedPath;
    }


    /**
     * @param point
     * @inheritDoc
     */
    public void moveTo(PointFP point) {
        super.moveTo(point);
        ExtendIfNeeded(1, 1);
        addMoveTo(point);
    }


    /**
     * @param point
     * @inheritDoc
     */
    public void lineTo(PointFP point) {
        super.lineTo(point);
        ExtendIfNeeded(1, 1);
        addLineTo(point);
    }


    /**
     * @inheritDoc
     */
    public void close() {
        super.close();
        ExtendIfNeeded(1, 0);
        addClose();
    }


    private final static int CMD_NOP = 0;
    private final static int CMD_MOVETO = 1;
    private final static int CMD_LINETO = 2;
    private final static int CMD_QCURVETO = 3;
    private final static int CMD_CCURVETO = 4;
    private final static int CMD_CLOSE = 6;
    private final static int BLOCKSIZE = 16;
    private int[] cmds = null;
    private PointFP[] pnts = null;
    private int cmdsSize = 0;
    private int pntsSize = 0;
    private GraphicsPathFP fromPath;
    private int[] dashArray = null;
    private int dashIndex = 0;
    private int nextDistance = -1;
    private boolean isEmpty = false;

    private void addMoveTo(PointFP point) {

        cmds[cmdsSize++] = CMD_MOVETO;
        pnts[pntsSize++] = new PointFP(point);
    }

    private void addLineTo(PointFP point) {

        cmds[cmdsSize++] = CMD_LINETO;
        pnts[pntsSize++] = new PointFP(point);
    }

    private void addClose() {
        cmds[cmdsSize++] = CMD_CLOSE;
    }

    private void ExtendIfNeeded(int cmdsAddNum, int pntsAddNum) {
        if (cmds == null) {
            cmds = new int[BLOCKSIZE];
        }
        if (pnts == null) {
            pnts = new PointFP[BLOCKSIZE];
        }

        if (cmdsSize + cmdsAddNum > cmds.length) {
            int[] newdata = new int[cmds.length + (cmdsAddNum > BLOCKSIZE
                    ? cmdsAddNum : BLOCKSIZE)];
            if (cmdsSize > 0) {
                System.arraycopy(cmds, 0, newdata, 0, cmdsSize);
            }
            cmds = newdata;
        }
        if (pntsSize + pntsAddNum > pnts.length) {
            PointFP[] newdata = new PointFP[pnts.length +
                    (pntsAddNum > BLOCKSIZE ? pntsAddNum : BLOCKSIZE)];
            if (pntsSize > 0) {
                System.arraycopy(pnts, 0, newdata, 0, pntsSize);
            }
            pnts = newdata;
        }
    }

    private void DashLine(GraphicsPathFP path, LineFP line) {

        if (nextDistance < 0) {
            nextDistance = dashArray[dashIndex];
            dashIndex = (dashIndex + 1) % dashArray.length;
        }
        int distance = nextDistance;

        PointFP pt = line.getPointAtDistance(distance);
        while (pt != null) {
            if (isEmpty) {
                path.addMoveTo(pt);
            } else {
                path.addLineTo(pt);
            }

            isEmpty = !isEmpty;
            nextDistance += dashArray[dashIndex];
            distance = nextDistance;
            pt = line.getPointAtDistance(distance);
            dashIndex = (dashIndex + 1) % dashArray.length;
        }
        if (isEmpty) {
            path.addMoveTo(line.pt2);
        } else {
            path.addLineTo(line.pt2);
        }
        nextDistance = nextDistance - line.getLength();


    }

    private void VisitPath(IGraphicsPathIteratorFP iterator) {
        if (iterator != null) {

            iterator.begin();
            int j = 0;
            for (int i = 0; i < fromPath.cmdsSize; i++) {
                switch (fromPath.cmds[i]) {

                    case CMD_NOP:
                        break;

                    case CMD_MOVETO:
                        iterator.moveTo(fromPath.pnts[j++]);
                        break;

                    case CMD_LINETO:
                        iterator.lineTo(fromPath.pnts[j++]);
                        break;

                    case CMD_QCURVETO:
                        iterator.quadTo(fromPath.pnts[j++],
                                fromPath.pnts[j++]);
                        break;

                    case CMD_CCURVETO:
                        iterator.curveTo(fromPath.pnts[j++],
                                fromPath.pnts[j++], fromPath.pnts[j++]);
                        break;

                    case CMD_CLOSE:
                        iterator.close();
                        break;

                    default:
                        return;

                }
            }
            iterator.end();
        }
    }
}