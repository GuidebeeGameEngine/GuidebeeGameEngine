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
//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Listener for Game Controller events.
 *
 * @author James Shen
 */
public interface GameControllerListener {

    /**
     * 8 possible directions.
     */
    public enum Direction {
        NONE, NORTH, NORTHEAST, EAST, SOUTHEAST, SOUTH, SOUTHWEST, WEST, NORTHWEST
    }

    /**
     * button type.
     */
    public enum GameButton {
        BUTTON_A, BUTTON_B
    }

    /**
     * Touch pad knob moved.
     *
     * @param touchpad
     * @param direction
     */
    public void KnobMoved(Touchpad touchpad, Direction direction);

    /**
     * button pressed.
     *
     * @param button
     */
    public void ButtonPressed(GameButton button);


}
