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
 * Gets notified of {@link Entity} related events.
 *
 * @author David Saltares
 */
public interface EntityListener {
    /**
     * Called whenever an {@link Entity} is added to {@link EntityEngine} or a
     * specific {@link Role}
     * <p/>
     * See {@link EntityEngine#addEntityListener(EntityListener)} and
     * {@link EntityEngine#addEntityListener(Role, EntityListener)}}
     *
     * @param entity
     */
    public void entityAdded(Entity entity);

    /**
     * Called whenever an {@link Entity} is removed from {@link EntityEngine}
     * or a specific {@link Role}
     * <p/>
     * See {@link EntityEngine#addEntityListener(EntityListener)} and
     * {@link EntityEngine#addEntityListener(Role, EntityListener)}}
     *
     * @param entity
     */
    public void entityRemoved(Entity entity);
}
