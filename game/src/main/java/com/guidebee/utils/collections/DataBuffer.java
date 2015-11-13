/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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
package com.guidebee.utils.collections;

//--------------------------------- IMPORTS ------------------------------------

//[------------------------------ MAIN CLASS ----------------------------------]

import com.guidebee.utils.DataOutput;
import com.guidebee.utils.StreamUtils;

/**
 * Extends {@link com.guidebee.utils.DataOutput} that writes bytes to a byte array.
 *
 * @author Nathan Sweet
 */
public class DataBuffer extends DataOutput {
    private final StreamUtils.OptimizedByteArrayOutputStream outStream;

    public DataBuffer() {
        this(32);
    }

    public DataBuffer(int initialSize) {
        super(new StreamUtils.OptimizedByteArrayOutputStream(initialSize));
        outStream = (StreamUtils.OptimizedByteArrayOutputStream) out;
    }

    /**
     * Returns the backing array, which has 0 to {@link #size()} items.
     */
    public byte[] getBuffer() {
        return outStream.getBuffer();
    }

    public byte[] toArray() {
        return outStream.toByteArray();
    }
}
