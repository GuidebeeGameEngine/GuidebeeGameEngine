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

import com.guidebee.game.entity.directors.Director;
import com.guidebee.game.entity.signals.Listener;
import com.guidebee.game.entity.signals.Signal;
import com.guidebee.game.entity.utils.ImmutableArray;
import com.guidebee.utils.Pool;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.ObjectMap;
import com.guidebee.utils.collections.ObjectMap.Entry;
import com.guidebee.utils.collections.SnapshotArray;

import java.util.Comparator;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * The heart of the Entity framework. It is responsible for keeping track of
 * {@link Entity} and
 * managing {@link com.guidebee.game.entity.directors.Director} objects. The Engine should be updated every
 * tick via the {@link #update(float)} method.
 * <p/>
 * With the Engine you can:
 * <p/>
 * <ul>
 * <li>Add/Remove {@link Entity} objects</li>
 * <li>Add/Remove {@link com.guidebee.game.entity.directors.Director}s</li>
 * <li>Obtain a list of entities for a specific {@link Role}</li>
 * <li>Update the main loop</li>
 * <li>Register/unregister {@link EntityListener} objects</li>
 * </ul>
 *
 * @author Stefan Bachmann
 */
public class EntityEngine {
    private static DirectorComparator comparator = new DirectorComparator();

    /**
     * An unordered array that holds all entities in the Engine
     */
    private Array<Entity> entities;
    /**
     * An unoredered array that keeps track of entities pending
     * removal for safe in-loop removal
     */
    private Array<Entity> pendingRemovalEntities;
    /**
     * An unordered list of EntityDirector
     */
    private Array<Director> directors;
    /**
     * An unordered and immutable list of EntityDirector
     */
    private ImmutableArray<Director> immutableDirectors;
    /**
     * A hashmap that organises EntityDirectors by class for easy retrieval
     */
    private ObjectMap<Class<?>, Director> directorsByClass;
    /**
     * A hashmap that organises all entities into family buckets
     */
    private ObjectMap<Role, Array<Entity>> families;
    /**
     * A hashmap that organises all entities into immutable family buckets
     */
    private ObjectMap<Role, ImmutableArray<Entity>> immutableFamilies;
    /**
     * A collection of entity added/removed event listeners
     */
    private SnapshotArray<EntityListener> listeners;
    /**
     * Entity added/removed event listeners per family
     */
    private ObjectMap<Role, SnapshotArray<EntityListener>> familyListeners;

    /**
     * A listener for the Engine that's called every time a dataTrait is added.
     */
    private final Listener<Entity> dataTraitAdded;
    /**
     * A listener for the Engine that's called every time a dataTrait is removed.
     */
    private final Listener<Entity> dataTraitRemoved;

    /**
     * Whether or not the engine is ticking
     */
    private boolean updating;

    private Object userObject;

    /**
     * Mechanism to delay dataTrait addition/removal to avoid affecting director processing
     */
    private DataTraitOperationPool dataTraitOperationsPool;
    private Array<DataTraitOperation> dataTraitOperations;
    private DataTraitOperationHandler dataTraitOperationHandler;

    public EntityEngine() {
        entities = new Array<Entity>(false, 16);
        pendingRemovalEntities = new Array<Entity>(false, 16);
        directors = new Array<Director>(false, 16);
        immutableDirectors = new ImmutableArray<Director>(directors);
        directorsByClass = new ObjectMap<Class<?>, Director>();
        families = new ObjectMap<Role, Array<Entity>>();
        immutableFamilies = new ObjectMap<Role, ImmutableArray<Entity>>();
        listeners = new SnapshotArray<EntityListener>(false, 16);
        familyListeners = new ObjectMap<Role, SnapshotArray<EntityListener>>();

        dataTraitAdded = new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                updateFamilyMembership(object);
            }
        };

        dataTraitRemoved = new Listener<Entity>() {
            @Override
            public void receive(Signal<Entity> signal, Entity object) {
                updateFamilyMembership(object);
            }
        };

        updating = false;

        dataTraitOperationsPool = new DataTraitOperationPool();
        dataTraitOperations = new Array<DataTraitOperation>();
        dataTraitOperationHandler = new DataTraitOperationHandler() {
            public void add(Entity entity, DataTrait dataTrait) {
                if (updating) {
                    DataTraitOperation operation = dataTraitOperationsPool.obtain();
                    operation.makeAdd(entity, dataTrait);
                    dataTraitOperations.add(operation);
                } else {
                    entity.addInternal(dataTrait);
                }
            }

            public void remove(Entity entity, Class<? extends DataTrait> dataTraitClass) {
                if (updating) {
                    DataTraitOperation operation = dataTraitOperationsPool.obtain();
                    operation.makeRemove(entity, dataTraitClass);
                    dataTraitOperations.add(operation);
                } else {
                    entity.removeInternal(dataTraitClass);
                }
            }
        };
    }

    /**
     * Adds an entity to this Engine.
     */
    public void addEntity(Entity entity) {
        entities.add(entity);

        updateFamilyMembership(entity);

        entity.dataTraitAdded.add(dataTraitAdded);
        entity.dataTraitRemoved.add(dataTraitRemoved);
        entity.dataTraitOperationHandler = dataTraitOperationHandler;

        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            EntityListener listener = (EntityListener) items[i];
            listener.entityAdded(entity);
        }
        listeners.end();
    }

    /**
     * Removes an entity from this Engine.
     */
    public void removeEntity(Entity entity) {
        if (updating) {
            pendingRemovalEntities.add(entity);
        } else {
            removeEntityInternal(entity);
        }
    }

    /**
     * Removes all entities registered with this Engine.
     */
    public void removeAllEntities() {
        while (entities.size > 0) {
            removeEntity(entities.first());
        }
    }

    /**
     * Adds the {@link Director} to this Engine.
     */
    public void addDirector(Director director) {
        Class<? extends Director> directorType = director.getClass();

        if (!directorsByClass.containsKey(directorType)) {
            directors.add(director);
            directorsByClass.put(directorType, director);
            director.addedToEngine(this);

            directors.sort(comparator);
        }
    }

    /**
     * Removes the {@link Director} from this Engine.
     */
    public void removeDirector(Director director) {
        if (directors.removeValue(director, true)) {
            directorsByClass.remove(director.getClass());
            director.removedFromEngine(this);
        }
    }

    /**
     * Quick {@link Director} retrieval.
     */
    @SuppressWarnings("unchecked")
    public <T extends Director> T getDirector(Class<T> directorType) {
        return (T) directorsByClass.get(directorType);
    }

    /**
     * @return immutable array of all entity directors managed by the {@link EntityEngine}.
     */
    public ImmutableArray<Director> getDirectors() {
        return immutableDirectors;
    }

    /**
     * Returns immutable collection of entities for the specified {@link Role}.
     * Will return the same instance every time.
     */
    public ImmutableArray<Entity> getEntitiesFor(Role role) {
        registerFamily(role);
        return immutableFamilies.get(role);
    }

    /**
     * Adds an {@link EntityListener}.
     * <p/>
     * The listener will be notified every time an entity is added/removed to/from the engine.
     */
    public void addEntityListener(EntityListener listener) {
        listeners.add(listener);
    }

    /**
     * Adds an {@link EntityListener} for a specific {@link Role}.
     * <p/>
     * The listener will be notified every time an entity is added/removed to/from the given family.
     */
    public void addEntityListener(Role role, EntityListener listener) {
        registerFamily(role);
        SnapshotArray<EntityListener> listeners = familyListeners.get(role);

        if (listeners == null) {
            listeners = new SnapshotArray<EntityListener>(false, 16);
            familyListeners.put(role, listeners);
        }

        listeners.add(listener);
    }

    /**
     * Removes an {@link EntityListener}
     */
    public void removeEntityListener(EntityListener listener) {
        listeners.removeValue(listener, true);

        for (SnapshotArray<EntityListener> familyListenerArray : familyListeners.values()) {
            familyListenerArray.removeValue(listener, true);
        }
    }

    /**
     * Updates all the directors in this Engine.
     *
     * @param deltaTime The time passed since the last frame.
     */
    public void update(float deltaTime) {
        updating = true;
        for (int i = 0; i < directors.size; i++) {
            if (directors.get(i).checkProcessing()) {
                directors.get(i).direct(deltaTime);
            }

            processDataTraitOperations();
            removePendingEntities();
        }

        updating = false;
    }

    private void updateFamilyMembership(Entity entity) {
        for (Entry<Role, Array<Entity>> entry : families.entries()) {
            Role role = entry.key;
            Array<Entity> entities = entry.value;
            int familyIndex = role.getIndex();


            boolean belongsToFamily = entity.getFamilyBits().get(familyIndex);
            boolean matches = role.matches(entity);

            if (!belongsToFamily && matches) {
                entities.add(entity);
                entity.getFamilyBits().set(familyIndex);

                notifyFamilyListenersAdd(role, entity);
            } else if (belongsToFamily && !matches) {
                entities.removeValue(entity, true);
                entity.getFamilyBits().clear(familyIndex);

                notifyFamilyListenersRemove(role, entity);
            }
        }
    }

    private void removePendingEntities() {
        int numPending = pendingRemovalEntities.size;

        for (int i = 0; i < numPending; ++i) {
            removeEntityInternal(pendingRemovalEntities.get(i));
        }

        pendingRemovalEntities.clear();
    }

    protected void removeEntityInternal(Entity entity) {
        entities.removeValue(entity, true);

        if (!entity.getFamilyBits().isEmpty()) {
            for (Entry<Role, Array<Entity>> entry : families.entries()) {
                Role role = entry.key;
                Array<Entity> entities = entry.value;

                if (role.matches(entity)) {
                    entities.removeValue(entity, true);
                    entity.getFamilyBits().clear(role.getIndex());
                    notifyFamilyListenersRemove(role, entity);
                }
            }
        }

        entity.dataTraitAdded.remove(dataTraitAdded);
        entity.dataTraitRemoved.remove(dataTraitRemoved);

        Object[] items = listeners.begin();
        for (int i = 0, n = listeners.size; i < n; i++) {
            EntityListener listener = (EntityListener) items[i];
            listener.entityRemoved(entity);
        }
        listeners.end();
    }

    private void notifyFamilyListenersAdd(Role role, Entity entity) {
        SnapshotArray<EntityListener> listeners = familyListeners.get(role);

        if (listeners != null) {
            Object[] items = listeners.begin();
            for (int i = 0, n = listeners.size; i < n; i++) {
                EntityListener listener = (EntityListener) items[i];
                listener.entityAdded(entity);
            }
            listeners.end();
        }
    }

    private void notifyFamilyListenersRemove(Role role, Entity entity) {
        SnapshotArray<EntityListener> listeners = familyListeners.get(role);

        if (listeners != null) {
            Object[] items = listeners.begin();
            for (int i = 0, n = listeners.size; i < n; i++) {
                EntityListener listener = (EntityListener) items[i];
                listener.entityRemoved(entity);
            }
            listeners.end();
        }
    }

    private Array<Entity> registerFamily(Role role) {
        Array<Entity> entities = families.get(role);

        if (entities == null) {
            entities = new Array<Entity>(false, 16);
            families.put(role, entities);
            immutableFamilies.put(role, new ImmutableArray<Entity>(entities));

            for (Entity e : this.entities) {
                if (role.matches(e)) {
                    entities.add(e);
                    e.getFamilyBits().set(role.getIndex());
                }
            }
        }

        return entities;
    }

    private void processDataTraitOperations() {
        int numOperations = dataTraitOperations.size;

        for (int i = 0; i < numOperations; ++i) {
            DataTraitOperation operation = dataTraitOperations.get(i);

            if (operation.type == DataTraitOperation.Type.Add) {
                operation.entity.addInternal(operation.dataTrait);
            } else if (operation.type == DataTraitOperation.Type.Remove) {
                operation.entity.removeInternal(operation.dataTraitClass);
            }

            dataTraitOperationsPool.free(operation);
        }

        dataTraitOperations.clear();
    }

    static interface DataTraitOperationHandler {
        public void add(Entity entity, DataTrait dataTrait);

        public void remove(Entity entity, Class<? extends DataTrait> dataTraitClass);
    }

    private static class DataTraitOperation {
        public enum Type {
            Add,
            Remove,
        }

        public Type type;
        public Entity entity;
        public DataTrait dataTrait;
        public Class<? extends DataTrait> dataTraitClass;

        public void makeAdd(Entity entity, DataTrait dataTrait) {
            this.type = Type.Add;
            this.entity = entity;
            this.dataTrait = dataTrait;
            this.dataTraitClass = null;
        }

        public void makeRemove(Entity entity, Class<? extends DataTrait> dataTraitClass) {
            this.type = Type.Remove;
            this.entity = entity;
            this.dataTrait = null;
            this.dataTraitClass = dataTraitClass;
        }
    }

    private static class DataTraitOperationPool extends Pool<DataTraitOperation> {
        @Override
        protected DataTraitOperation newObject() {
            return new DataTraitOperation();
        }
    }

    private static class DirectorComparator implements Comparator<Director> {
        @Override
        public int compare(Director a, Director b) {
            return a.priority > b.priority ? 1 : (a.priority == b.priority) ? 0 : -1;
        }
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
