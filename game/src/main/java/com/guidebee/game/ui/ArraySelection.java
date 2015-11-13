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
package com.guidebee.game.ui;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngine;
import com.guidebee.game.Input;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A selection that supports range selection by knowing about the array of
 * items being selected.
 *
 * @author Nathan Sweet
 */
public class ArraySelection<T> extends Selection<T> {
    private Array<T> items;
    private boolean rangeSelect = true;

    public ArraySelection(Array<T> items) {
        this.items = items;
    }

    public void choose(T item) {
        if (item == null) throw new IllegalArgumentException("item cannot be null.");
        if (isDisabled) return;
        if (selected.size > 0 && rangeSelect && multiple
                && (GameEngine.input.isKeyPressed(Input.Keys.SHIFT_LEFT)
                || GameEngine.input.isKeyPressed(Input.Keys.SHIFT_RIGHT))) {
            int low = items.indexOf(getLastSelected(), false);
            int high = items.indexOf(item, false);
            if (low > high) {
                int temp = low;
                low = high;
                high = temp;
            }
            snapshot();
            if (!Utils.ctrl()) selected.clear();
            for (; low <= high; low++)
                selected.add(items.get(low));
            if (fireChangeEvent()) revert();
            cleanup();
            return;
        }
        super.choose(item);
    }

    public boolean getRangeSelect() {
        return rangeSelect;
    }

    public void setRangeSelect(boolean rangeSelect) {
        this.rangeSelect = rangeSelect;
    }
}
