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

import com.guidebee.game.engine.utils.reflect.ClassReflection;
import com.guidebee.utils.Pool;
import com.guidebee.utils.Pool.Poolable;
import com.guidebee.utils.ReflectionPool;
import com.guidebee.utils.collections.Array;
import com.guidebee.utils.collections.ObjectMap;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Supports {@link Entity} and {@link DataTrait} pooling. This improves
 * performance in environments
 * where creating/deleting entities is frequent as it greatly reduces memory allocation.
 * <p/>
 * <ul>
 * <li>Create entities using {@link #createEntity()}</li>
 * <li>Create dataTraits using {@link #createDataTrait(Class)}</li>
 * <li>DataTraits should implement the {@link Poolable} interface when in
 * need to reset its state upon removal</li>
 * </ul>
 *
 * @author David Saltares
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class PooledEntityEngine extends EntityEngine {

    private EntityPool entityPool;
    private DataTraitPools dataTraitPools;

    /**
     * Creates a new PooledEngine with a maximum of 100 entities and 100
     * dataTraits of each type.
     * Use {@link #PooledEntityEngine(int, int, int, int)} to configure the
     * entity and dataTrait pools.
     */
    public PooledEntityEngine() {
        this(10, 100, 10, 100);
    }

    /**
     * Creates new PooledEngine with the specified pools size configurations.
     *
     * @param entityPoolInitialSize    initial number of pre-allocated entities.
     * @param entityPoolMaxSize        maximum number of pooled entities.
     * @param dataTraitPoolInitialSize initial size for each dataTrait type pool.
     * @param dataTraitPoolMaxSize     maximum size for each dataTrait type pool.
     */
    public PooledEntityEngine(int entityPoolInitialSize,
                              int entityPoolMaxSize,
                              int dataTraitPoolInitialSize,
                              int dataTraitPoolMaxSize) {
        super();

        entityPool = new EntityPool(entityPoolInitialSize, entityPoolMaxSize);
        dataTraitPools = new DataTraitPools(dataTraitPoolInitialSize, dataTraitPoolMaxSize);
    }

    /**
     * @return Clean {@link Entity} from the Engine pool. In order to add it
     * to the {@link EntityEngine}, use {@link #addEntity(Entity)}.
     */
    public Entity createEntity() {
        return entityPool.obtain();
    }

    /**
     * Retrieves a new {@link DataTrait} from the {@link EntityEngine} pool.
     * It will be placed back in the
     * pool whenever it's removed from an {@link Entity} or the
     * {@link Entity} itself it's removed.
     */
    public <T extends DataTrait> T createDataTrait(Class<T> dataTraitType) {
        return dataTraitPools.obtain(dataTraitType);
    }

    /**
     * Removes all free entities and dataTraits from their pools.
     * Although this will likely result in garbage collection, it will free up memory.
     */
    public void clearPools() {
        entityPool.clear();
        dataTraitPools.clear();
    }

    @Override
    protected void removeEntityInternal(Entity entity) {
        super.removeEntityInternal(entity);

        if (ClassReflection.isAssignableFrom(PooledEntity.class, entity.getClass())) {
            PooledEntity pooledEntity = (PooledEntity) entity;
            entityPool.free(pooledEntity);
        }
    }

    private class PooledEntity extends Entity implements Poolable {

        @Override
        DataTrait removeInternal(Class<? extends DataTrait> dataTraitType) {
            DataTrait dataTrait = super.removeInternal(dataTraitType);
            dataTraitPools.free(dataTrait);
            return dataTrait;
        }

        @Override
        public void reset() {
            removeAll();
            flags = 0;
        }
    }

    private class EntityPool extends Pool<PooledEntity> {

        public EntityPool(int initialSize, int maxSize) {
            super(initialSize, maxSize);
        }

        @Override
        protected PooledEntity newObject() {
            return new PooledEntity();
        }
    }

    private class DataTraitPools {
        private ObjectMap<Class<?>, ReflectionPool> pools;
        private int initialSize;
        private int maxSize;

        public DataTraitPools(int initialSize, int maxSize) {
            this.pools = new ObjectMap<Class<?>, ReflectionPool>();
            this.initialSize = 0;
            this.maxSize = 0;
        }

        public <T> T obtain(Class<T> type) {
            ReflectionPool pool = pools.get(type);

            if (pool == null) {
                pool = new ReflectionPool(type, initialSize, maxSize);
                pools.put(type, pool);
            }

            return (T) pool.obtain();
        }

        public void free(Object object) {
            if (object == null) {
                throw new IllegalArgumentException("object cannot be null.");
            }

            ReflectionPool pool = pools.get(object.getClass());

            if (pool == null) {
                return; // Ignore freeing an object that was never retained.
            }

            pool.free(object);
        }

        public void freeAll(Array objects) {
            if (objects == null) throw new IllegalArgumentException("objects cannot be null.");

            for (int i = 0, n = objects.size; i < n; i++) {
                Object object = objects.get(i);
                if (object == null) continue;
                free(object);
            }
        }

        public void clear() {
            for (Pool pool : pools.values()) {
                pool.clear();
            }
        }
    }
}
