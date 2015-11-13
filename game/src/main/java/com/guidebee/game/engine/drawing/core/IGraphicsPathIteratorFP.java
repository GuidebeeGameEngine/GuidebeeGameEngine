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
 * The IPathIterator interface provides the mechanism for objects that
 *  return the geometry of their boundary by allowing a caller to retrieve the
 * path of that boundary a segment at a time.
 * This class cannot be inherited.
 * @author      James Shen.
 */
interface IGraphicsPathIteratorFP {

    void begin();

    void end();

    void moveTo(PointFP point);

    void lineTo(PointFP point);

    void quadTo(PointFP control, PointFP point);

    void curveTo(PointFP control1, PointFP control2, PointFP point);

    void close();
}
