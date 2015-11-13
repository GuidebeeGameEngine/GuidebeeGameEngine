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

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Provides super fast {@link DataTrait} retrieval from {@Link Entity} objects.
 *
 * @param <T> the class type of the {@link DataTrait}.
 * @author David Saltares
 */
public final class DataTraitMapper<T extends DataTrait> {
    private final DataTraitType dataTraitType;

    /**
     * @param dataTraitClass DataTrait class to be retrieved by the mapper.
     * @return New instance that provides fast access to the {@link DataTrait}
     * of the specified class.
     */
    public static <T extends DataTrait> DataTraitMapper<T> getFor(Class<T> dataTraitClass) {
        return new DataTraitMapper<T>(dataTraitClass);
    }

    /**
     * @return The {@link DataTrait} of the specified class belonging to entity.
     */
    public T get(Entity entity) {
        return entity.getDataTrait(dataTraitType);
    }

    /**
     * @return Whether or not entity has the dataTrait of the specified class.
     */
    public boolean has(Entity entity) {
        return entity.hasDataTrait(dataTraitType);
    }

    private DataTraitMapper(Class<T> dataTraitClass) {
        dataTraitType
                = DataTraitType.getFor(dataTraitClass);
    }
}
