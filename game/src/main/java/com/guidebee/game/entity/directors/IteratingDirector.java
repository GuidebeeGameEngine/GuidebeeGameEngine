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
 * A simple EntityDirector that iterates over each entity and calls
 * processEntity() for each entity every time
 * the EntityDirector is updated. This is really just a convenience
 * class as most directors iterate over a list
 * of entities.
 *
 * @author Stefan Bachmann
 */
public abstract class IteratingDirector extends Director {
    /**
     * The family describing this directors entities
     */
    private Role role;
    /**
     * The entities used by this director
     */
    private ImmutableArray<Entity> entities;

    /**
     * Instantiates a director that will iterate over the entities described by the Family.
     *
     * @param role The family of entities iterated over in this Director
     */
    public IteratingDirector(Role role) {
        this(role, 0);
    }

    /**
     * Instantiates a director that will iterate over the entities described by the Family, with a
     * specific priority.
     *
     * @param role     The family of entities iterated over in this Director
     * @param priority The priority to execute this director with (lower means higher priority)
     */
    public IteratingDirector(Role role, int priority) {
        super(priority);

        this.role = role;
    }

    @Override
    public void addedToEngine(EntityEngine entityEngine) {
        entities = entityEngine.getEntitiesFor(role);
    }

    @Override
    public void removedFromEngine(EntityEngine entityEngine) {
        entities = null;
    }

    @Override
    public void direct(float deltaTime) {
        for (int i = 0; i < entities.size(); ++i) {
            processEntity(entities.get(i), deltaTime);
        }
    }

    /**
     * This method is called on every entity on every direct call of the EntityDirector.
     * Override this to implement
     * your director's specific processing.
     *
     * @param entity    The current Entity being processed
     * @param deltaTime The delta time between the last and current frame
     */
    protected abstract void processEntity(Entity entity, float deltaTime);
}
