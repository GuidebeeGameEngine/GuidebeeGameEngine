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
 * Represents a group of {@link DataTrait}s. It is used to describe what
 * {@link Entity} objects an
 * {@link com.guidebee.game.entity.directors.Director} should process.
 * <p/>
 * Example: {@code Family.getFor(PositionDataTrait.class, VelocityDataTrait.class)}
 * <p/>
 * Families can't be instantiate directly but must be accessed via
 * {@code Family.getFor()}, this is
 * to avoid duplicate families that describe the same dataTraits.
 *
 * @author Stefan Bachmann
 */
public class Role {
    /**
     * The hashmap holding all families
     */
    private static ObjectMap<String, Role> families = new ObjectMap<String, Role>();
    private static int familyIndex = 0;

    /**
     * Must contain all the dataTraits in the set
     */
    private final Bits all;
    /**
     * Must contain at least one of the dataTraits in the set
     */
    private final Bits one;
    /**
     * Cannot contain any of the dataTraits in the set
     */
    private final Bits exclude;
    /**
     * Each family has a unique index, used for bitmasking
     */
    private final int index;

    /**
     * Private constructor, use static method Family.getFamilyFor()
     */
    protected Role(Bits all, Bits any, Bits exclude) {
        this.all = all;
        this.one = any;
        this.exclude = exclude;
        this.index = familyIndex++;
    }

    /**
     * @return This family's unique index
     */
    public int getIndex() {
        return this.index;
    }

    /**
     * @return Whether the entity matches the family requirements or not
     */
    public boolean matches(Entity entity) {
        Bits entityDataTraitBits = entity.getDataTraitBits();

        if (entityDataTraitBits.isEmpty())
            return false;

        for (int i = all.nextSetBit(0); i >= 0; i = all.nextSetBit(i + 1)) {
            if (!entityDataTraitBits.get(i))
                return false;
        }

        if (!one.isEmpty() && !one.intersects(entityDataTraitBits)) {
            return false;
        }

        if (!exclude.isEmpty() && exclude.intersects(entityDataTraitBits)) {
            return false;
        }

        return true;
    }

    /**
     * @return The family matching the specified {@link DataTrait} classes as a descriptor.
     * Each set of dataTrait types will
     * always return the same Family instance.
     */
    @SafeVarargs
    public static Role getFor(Class<? extends DataTrait>... dataTraitTypes) {
        return getFor(DataTraitType.getBitsFor(dataTraitTypes), new Bits(), new Bits());
    }

    /**
     * Returns a family with the passed {@link DataTrait} classes as a descriptor.
     * Each set of dataTrait types will
     * always return the same Family instance.
     *
     * @param all     entities will have to contain all of the dataTraits in the set.
     *                See {@link DataTraitType#getBitsFor(Class<? extends DataTrait > ...)}.
     * @param one     entities will have to contain at least one of the dataTraits in the set.See
     *                {@link DataTraitType#getBitsFor(Class<? extends DataTrait > ...)}.
     * @param exclude entities cannot contain any of the dataTraits in the set.
     *                See {@link DataTraitType#getBitsFor(Class<? extends DataTrait > ...)}.
     * @return The family
     */
    public static Role getFor(Bits all, Bits one, Bits exclude) {
        String hash = getFamilyHash(all, one, exclude);
        Role role = families.get(hash, null);
        if (role == null) {
            role = new Role(all, one, exclude);
            families.put(hash, role);
        }

        return role;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((all == null) ? 0 : all.hashCode());
        result = prime * result + ((one == null) ? 0 : one.hashCode());
        result = prime * result + ((exclude == null) ? 0 : exclude.hashCode());
        result = prime * result + index;
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (!(obj instanceof Role))
            return false;
        Role other = (Role) obj;
        if (all == null) {
            if (other.all != null)
                return false;
        } else if (!all.equals(other.all))
            return false;
        if (one == null) {
            if (other.one != null)
                return false;
        } else if (!one.equals(other.one))
            return false;
        if (exclude == null) {
            if (other.exclude != null)
                return false;
        } else if (!exclude.equals(other.exclude))
            return false;

        return index == other.index;
    }

    private static String getFamilyHash(Bits all, Bits one, Bits exclude) {
        StringBuilder builder = new StringBuilder();
        builder.append("all:");
        builder.append(getBitsString(all));
        builder.append(",one:");
        builder.append(getBitsString(one));
        builder.append(",exclude:");
        builder.append(getBitsString(exclude));
        return builder.toString();
    }

    private static String getBitsString(Bits bits) {
        StringBuilder builder = new StringBuilder();

        int numBits = bits.length();
        for (int i = 0; i < numBits; ++i) {
            builder.append(bits.get(i) ? "1" : "0");
        }

        return builder.toString();
    }
}
