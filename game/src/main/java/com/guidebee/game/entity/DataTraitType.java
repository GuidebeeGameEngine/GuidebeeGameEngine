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
package com.guidebee.game.entity;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.utils.collections.Bits;
import com.guidebee.utils.collections.ObjectMap;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Uniquely identifies a {@link DataTrait} sub-class.
 * It assigns them an index which is used internally for fast comparison and retrieval.
 * See {@link Role} and {@link Entity}.
 * <p/>
 * DataTraitType is a package protected class.
 * <p/>
 * You cannot instantiate a DataTraitType. They can only be accessed via
 * {@link #getIndexFor(Class<? extends DataTrait >)}. Each
 * dataTrait class will always return the same instance of DataTraitType.
 *
 * @author Stefan Bachmann
 */
public final class DataTraitType {
    /**
     * Hashmap to keep track of all DataTrait subclasses hashed by their Class
     */
    private static ObjectMap<Class<? extends DataTrait>, DataTraitType>
            dataTraitTypes = new ObjectMap<Class<? extends DataTrait>, DataTraitType>();
    private static int typeIndex = 0;

    /**
     * This DataTraitType's unique index
     */
    private final int index;

    private DataTraitType() {
        index = typeIndex++;
    }

    /**
     * @return This DataTraitType's unique index
     */
    public int getIndex() {
        return index;
    }

    /**
     * @param dataTraitType The {@link DataTrait} class
     * @return A DataTraitType matching the DataTrait Class
     */
    public static DataTraitType getFor(Class<? extends DataTrait> dataTraitType) {
        DataTraitType type = dataTraitTypes.get(dataTraitType);

        if (type == null) {
            type = new DataTraitType();
            dataTraitTypes.put(dataTraitType, type);
        }

        return type;
    }

    /**
     * Quick helper method. The same could be done via
     * {@link #getFor(Class<? extends DataTrait >)}.
     *
     * @param dataTraitType The {@link DataTrait} class
     * @return The index for the specified {@link DataTrait} Class
     */
    public static int getIndexFor(Class<? extends DataTrait> dataTraitType) {

        return getFor(dataTraitType).getIndex();
    }

    /**
     * @param dataTraitTypes list of {@link DataTrait} classes
     * @return Bits representing the collection of dataTraits for quick
     * comparison and matching. See {@link Role#getFor(Bits, Bits, Bits)}.
     */
    public static Bits getBitsFor(Class<? extends DataTrait>... dataTraitTypes) {
        Bits bits = new Bits();

        int typesLength = dataTraitTypes.length;
        for (int i = 0; i < typesLength; i++) {
            bits.set(DataTraitType.getIndexFor(dataTraitTypes[i]));
        }

        return bits;
    }

    @Override
    public int hashCode() {
        return index;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        DataTraitType other = (DataTraitType) obj;
        return index == other.index;
    }
}
