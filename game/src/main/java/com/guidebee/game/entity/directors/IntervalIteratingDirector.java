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
package com.guidebee.game.entity.directors;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.entity.Entity;
import com.guidebee.game.entity.EntityEngine;
import com.guidebee.game.entity.Role;
import com.guidebee.game.entity.utils.ImmutableArray;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A simple {@link Director} that processes a {@link com.guidebee.game.entity.Role} of entities
 * not once per frame, but after a given interval. Entity processing logic should
 * be placed in {@link IntervalIteratingDirector#processEntity(Entity)}.
 *
 * @author David Saltares
 */
public abstract class IntervalIteratingDirector extends IntervalDirector {
    private Role role;
    private ImmutableArray<Entity> entities;

    /**
     * @param role     represents the collection of family the director should process
     * @param interval time in seconds between calls to
     *                 {@link IntervalIteratingDirector#updateInterval()}.
     */
    public IntervalIteratingDirector(Role role, float interval) {
        this(role, interval, 0);
    }

    /**
     * @param role     represents the collection of family the director should process
     * @param interval time in seconds between calls to
     *                 {@link IntervalIteratingDirector#updateInterval()}.
     * @param priority
     */
    public IntervalIteratingDirector(Role role, float interval, int priority) {
        super(interval, priority);
        this.role = role;
    }

    @Override
    public void addedToEngine(EntityEngine entityEngine) {
        entities = entityEngine.getEntitiesFor(role);
    }

    @Override
    protected void updateInterval() {
        for (int i = 0; i < entities.size(); ++i) {
            processEntity(entities.get(i));
        }
    }

    /**
     * The user should place the entity processing logic here.
     *
     * @param entity
     */
    protected abstract void processEntity(Entity entity);
}
