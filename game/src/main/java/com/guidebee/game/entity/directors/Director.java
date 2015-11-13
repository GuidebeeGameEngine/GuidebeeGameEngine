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

//[------------------------------ MAIN CLASS ----------------------------------]

import com.guidebee.game.entity.EntityEngine;

/**
 * Abstract class for processing sets of {@link com.guidebee.game.entity.Entity} objects.
 *
 * @author Stefan Bachmann
 */
public abstract class Director {
    /**
     * Use this to set the priority of the director.
     * Lower means it'll get executed first.
     */
    public int priority;

    private boolean processing;

    /**
     * Default constructor that will initialise an EntityDirector with priority 0.
     */
    public Director() {
        this(0);
    }

    /**
     * Initialises the EntityDirector with the priority specified.
     *
     * @param priority The priority to execute this director with
     *                 (lower means higher priority).
     */
    public Director(int priority) {
        this.priority = priority;
        this.processing = true;
    }

    /**
     * Called when this EntityDirector is added to an {@link com.guidebee.game.entity.EntityEngine}.
     *
     * @param entityEngine The {@link com.guidebee.game.entity.EntityEngine} this director was added to.
     */
    public void addedToEngine(EntityEngine entityEngine) {
    }

    /**
     * Called when this EntityDirector is removed from an {@link EntityEngine}.
     *
     * @param entityEngine The {@link EntityEngine} the director was removed from.
     */
    public void removedFromEngine(EntityEngine entityEngine) {
    }

    /**
     * The direct method called every tick.
     *
     * @param deltaTime The time passed since last frame in seconds.
     */
    public void direct(float deltaTime) {
    }

    /**
     * @return Whether or not the director should be processed.
     */
    public boolean checkProcessing() {
        return processing;
    }

    /**
     * Sets whether or not the director should be processed by the {@link EntityEngine}.
     */
    public void setProcessing(boolean processing) {
        this.processing = processing;
    }
}
