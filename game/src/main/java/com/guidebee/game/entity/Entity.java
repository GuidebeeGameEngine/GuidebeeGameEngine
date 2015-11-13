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

import com.guidebee.game.entity.EntityEngine.DataTraitOperationHandler;
import com.guidebee.game.entity.signals.Signal;
import com.guidebee.game.entity.utils.Bag;
import com.guidebee.game.entity.utils.ImmutableArray;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.Bits;


//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Simple containers of {@link DataTrait}s that give them "data".
 * The dataTrait's data
 * is then processed by {@link com.guidebee.game.entity.directors.Director}s.
 *
 * @author Stefan Bachmann
 */
public class Entity {
    private static int nextIndex;

    /**
     * A flag that can be used to bit mask this entity. Up to the user to manage.
     */
    public int flags;
    /**
     * Will dispatch an event when a dataTrait is added.
     */
    public final Signal<Entity> dataTraitAdded;
    /**
     * Will dispatch an event when a dataTrait is removed.
     */
    public final Signal<Entity> dataTraitRemoved;

    /**
     * Unique entity index for fast retrieval
     */
    private int index;
    /**
     * A collection that holds all the dataTraits indexed by their
     * {@link DataTraitType} index
     */
    private Bag<DataTrait> dataTraits;
    /**
     * An auxiliary array for user access to all the dataTraits of an entity
     */
    private Array<DataTrait> dataTraitsArray;
    /**
     * A wrapper around dataTraitsArray so users cannot modify it
     */
    private ImmutableArray<DataTrait> immutableDataTraitsArray;
    /**
     * A Bits describing all the dataTraits in this entity. For quick matching.
     */
    private Bits dataTraitBits;
    /**
     * A Bits describing all the directors this entity was matched with.
     */
    private Bits familyBits;

    DataTraitOperationHandler dataTraitOperationHandler;

    private Object userObject;

    /**
     * Creates an empty Entity.
     */
    public Entity() {
        dataTraits = new Bag<DataTrait>();
        dataTraitsArray = new Array<DataTrait>(false, 16);
        immutableDataTraitsArray = new ImmutableArray<DataTrait>(dataTraitsArray);
        dataTraitBits = new Bits();
        familyBits = new Bits();
        flags = 0;

        index = nextIndex++;

        dataTraitAdded = new Signal<Entity>();
        dataTraitRemoved = new Signal<Entity>();
    }

    /**
     * @return The Entity's unique index.
     */
    public int getIndex() {
        return index;
    }

    /**
     * Adds a {@link DataTrait} to this Entity. If a {@link DataTrait} of
     * the same type already exists, it'll be replaced.
     *
     * @return The Entity for easy chaining
     */
    public Entity add(DataTrait dataTrait) {
        if (dataTraitOperationHandler != null) {
            dataTraitOperationHandler.add(this, dataTrait);
        } else {
            addInternal(dataTrait);
        }
        return this;
    }

    /**
     * Removes the {@link DataTrait} of the specified type. Since there is
     * only ever one dataTrait of one type, we
     * don't need an instance reference.
     *
     * @return The removed {@link DataTrait}, or null if the Entity did no
     * contain such a dataTrait.
     */
    public DataTrait remove(Class<? extends DataTrait> dataTraitClass) {
        DataTraitType dataTraitType = DataTraitType.getFor(dataTraitClass);
        int dataTraitTypeIndex = dataTraitType.getIndex();
        DataTrait removeDataTrait = dataTraits.get(dataTraitTypeIndex);

        if (dataTraitOperationHandler != null) {
            dataTraitOperationHandler.remove(this, dataTraitClass);
        } else {
            removeInternal(dataTraitClass);
        }

        return removeDataTrait;
    }

    /**
     * Removes all the {@link DataTrait}'s from the Entity.
     */
    public void removeAll() {
        while (dataTraitsArray.size > 0) {
            removeInternal(dataTraitsArray.get(0).getClass());
        }
    }

    /**
     * @return immutable collection with all the Entity {@link DataTrait}s.
     */
    public ImmutableArray<DataTrait> getDataTraits() {
        return immutableDataTraitsArray;
    }

    /**
     * Retrieve a dataTrait from this {@link Entity} by class.
     * <p/>
     * <em>Note:</em> the preferred way of retrieving {@link DataTrait}s is
     * using {@link DataTraitMapper}s. This method
     * is provided for convenience; using a DataTraitMapper provides O(1)
     * access to dataTraits while this method
     * provides only O(logn).
     *
     * @param dataTraitClass the class of the dataTrait to be retrieved.
     * @return the instance of the specified {@link DataTrait} attached to
     * this {@link Entity}, or null if no such {@link DataTrait} exists.
     */
    public <T extends DataTrait> T getDataTrait(Class<T> dataTraitClass) {
        return getDataTrait(DataTraitType.getFor(dataTraitClass));
    }

    /**
     * Internal use.
     *
     * @return The {@link DataTrait} object for the specified class, null
     * if the Entity does not have any dataTraits for that class.
     */
    @SuppressWarnings("unchecked")
    <T extends DataTrait> T getDataTrait(DataTraitType dataTraitType) {
        int dataTraitTypeIndex = dataTraitType.getIndex();

        if (dataTraitTypeIndex < dataTraits.getCapacity()) {
            return (T) dataTraits.get(dataTraitType.getIndex());
        } else {
            return null;
        }
    }

    /**
     * Internal use.
     *
     * @return Whether or not the Entity has a {@link DataTrait}
     * for the specified class.
     */
    boolean hasDataTrait(DataTraitType dataTraitType) {
        return dataTraitBits.get(dataTraitType.getIndex());
    }

    /**
     * Internal use.
     *
     * @return This Entity's dataTrait bits, describing all the
     * {@link DataTrait}s it contains.
     */
    Bits getDataTraitBits() {
        return dataTraitBits;
    }

    /**
     * @return This Entity's {@link Role} bits, describing all the
     * {@link com.guidebee.game.entity.directors.Director}s it currently is being processed by.
     */
    Bits getFamilyBits() {
        return familyBits;
    }

    Entity addInternal(DataTrait dataTrait) {
        Class<? extends DataTrait> dataTraitClass = dataTrait.getClass();

        for (int i = 0; i < dataTraitsArray.size; ++i) {
            if (dataTraitsArray.get(i).getClass() == dataTraitClass) {
                dataTraitsArray.removeIndex(i);
                break;
            }
        }

        int dataTraitTypeIndex = DataTraitType.getIndexFor(dataTrait.getClass());

        dataTraits.set(dataTraitTypeIndex, dataTrait);
        dataTraitsArray.add(dataTrait);

        dataTraitBits.set(dataTraitTypeIndex);

        dataTraitAdded.dispatch(this);
        return this;
    }

    DataTrait removeInternal(Class<? extends DataTrait> dataTraitClass) {
        DataTraitType dataTraitType = DataTraitType.getFor(dataTraitClass);
        int dataTraitTypeIndex = dataTraitType.getIndex();
        DataTrait removeDataTrait = dataTraits.get(dataTraitTypeIndex);

        if (removeDataTrait != null) {
            dataTraits.set(dataTraitTypeIndex, null);
            dataTraitsArray.removeValue(removeDataTrait, true);
            dataTraitBits.clear(dataTraitTypeIndex);

            dataTraitRemoved.dispatch(this);
        }

        return removeDataTrait;
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
        if (!(obj instanceof Entity))
            return false;
        Entity other = (Entity) obj;
        return index == other.index;
    }

    /**
     * Retrieves application specific object for convenience.
     */
    public Object getUserObject() {
        return userObject;
    }


    /**
     * Sets an application specific object for convenience.
     */
    public void setUserObject(Object userObject) {
        this.userObject = userObject;
    }
}
