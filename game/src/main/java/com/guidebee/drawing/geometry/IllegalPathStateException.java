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

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * The <code>IllegalPathStateException</code> represents an
 * exception that is thrown if an operation is performed on a path
 * that is in an illegal state with respect to the particular
 * operation being performed, such as appending a path segment
 * to a {@link Path} without an initial moveto.
 * <p/>
 * @author James Shen.
 */
public class IllegalPathStateException extends RuntimeException {


    /**
     * Constructs an <code>IllegalPathStateException</code> with no
     * detail message.
     */
    public IllegalPathStateException() {
    }


    /**
     * Constructs an <code>IllegalPathStateException</code> with the
     * specified detail message.
     *
     * @param s the detail message
     */
    public IllegalPathStateException(String s) {
        super(s);
    }
}
