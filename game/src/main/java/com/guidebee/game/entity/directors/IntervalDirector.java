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

/**
 * A simple {@link Director} that does not run its direct logic every call
 * to {@link Director#direct(float)}, but after a given interval. The actual
 * logic should be placed in {@link IntervalDirector#updateInterval()}.
 *
 * @author David Saltares
 */
public abstract class IntervalDirector extends Director {
    private float interval;
    private float accumulator;

    /**
     * @param interval time in seconds between calls to
     *                 {@link IntervalDirector#updateInterval()}.
     */
    public IntervalDirector(float interval) {
        this(interval, 0);
    }

    /**
     * @param interval time in seconds between calls to
     *                 {@link IntervalDirector#updateInterval()}.
     * @param priority
     */
    public IntervalDirector(float interval, int priority) {
        super(priority);
        this.interval = interval;
        this.accumulator = 0;
    }

    @Override
    public void direct(float deltaTime) {
        accumulator += deltaTime;

        if (accumulator >= interval) {
            accumulator -= interval;
            updateInterval();
        }
    }

    /**
     * The processing logic of the director should be placed here.
     */
    protected abstract void updateInterval();
}
