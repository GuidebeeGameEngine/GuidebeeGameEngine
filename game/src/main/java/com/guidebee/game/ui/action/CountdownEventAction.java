/*******************************************************************************
 * Copyright 2011 See AUTHORS file.
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
package com.guidebee.game.ui.action;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.ui.Event;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * An EventAction that is complete once it receives X number of events.
 *
 * @author JavadocMD
 * @author Nathan Sweet
 */
public class CountdownEventAction<T extends Event> extends EventAction<T> {
    int count, current;

    public CountdownEventAction(Class<? extends T> eventClass, int count) {
        super(eventClass);
        this.count = count;
    }

    public boolean handle(T event) {
        current++;
        return current >= count;
    }
}
