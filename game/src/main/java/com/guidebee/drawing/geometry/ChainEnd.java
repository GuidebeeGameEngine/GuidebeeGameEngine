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
final class ChainEnd {

    CurveLink head;
    CurveLink tail;
    ChainEnd partner;
    int etag;

    public ChainEnd(CurveLink first, ChainEnd partner) {
        this.head = first;
        this.tail = first;
        this.partner = partner;
        this.etag = first.getEdgeTag();
    }

    public CurveLink getChain() {
        return head;
    }

    public void setOtherEnd(ChainEnd partner) {
        this.partner = partner;
    }

    public ChainEnd getPartner() {
        return partner;
    }

    /*
     * Returns head of a complete chain to be added to subcurves
     * or null if the links did not complete such a chain.
     */
    public CurveLink linkTo(ChainEnd that) {
        if (etag == AreaOp.ETAG_IGNORE ||
                that.etag == AreaOp.ETAG_IGNORE) {
            throw new RuntimeException("ChainEnd linked more than once!");
        }
        if (etag == that.etag) {
            throw new RuntimeException("Linking chains of the same type!");
        }
        ChainEnd enter, exit;
        // assert(partner.etag != that.partner.etag);
        if (etag == AreaOp.ETAG_ENTER) {
            enter = this;
            exit = that;
        } else {
            enter = that;
            exit = this;
        }
        // Now make sure these ChainEnds are not linked to any others...
        etag = AreaOp.ETAG_IGNORE;
        that.etag = AreaOp.ETAG_IGNORE;
        // Now link everything up...
        enter.tail.setNext(exit.head);
        enter.tail = exit.tail;
        if (partner == that) {
            // Curve has closed on itself...
            return enter.head;
        }
        // Link this chain into one end of the chain formed by the partners
        ChainEnd otherenter = exit.partner;
        ChainEnd otherexit = enter.partner;
        otherenter.partner = otherexit;
        otherexit.partner = otherenter;
        if (enter.head.getYTop() < otherenter.head.getYTop()) {
            enter.tail.setNext(otherenter.head);
            otherenter.head = enter.head;
        } else {
            otherexit.tail.setNext(enter.head);
            otherexit.tail = enter.tail;
        }
        return null;
    }

    public void addLink(CurveLink newlink) {
        if (etag == AreaOp.ETAG_ENTER) {
            tail.setNext(newlink);
            tail = newlink;
        } else {
            newlink.setNext(head);
            head = newlink;
        }
    }

    public double getX() {
        if (etag == AreaOp.ETAG_ENTER) {
            return tail.getXBot();
        } else {
            return head.getXBot();
        }
    }
}
