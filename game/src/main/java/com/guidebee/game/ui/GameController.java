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

import com.guidebee.game.ui.drawable.TextureRegionDrawable;
import com.guidebee.math.MathUtils;
import com.guidebee.utils.collections.DelayedRemovalArray;


//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * On screen game controller, it includes a touch pad and two optional buttons.
 * button A -- fire button, Button B -- any other usage.
 */
public class GameController extends WidgetGroup {

    private final DelayedRemovalArray<GameControllerListener>
            listeners = new DelayedRemovalArray(0);


    private final Touchpad touchpad;
    private final ImageButton shootButton;
    private final ImageButton powerButton;

    /**
     * Constructor.
     *
     * @param touchBackground
     * @param touchKnob
     * @param buttonA
     * @param buttonAPressed
     * @param buttonB
     * @param buttonBPressed
     */
    public GameController(TextureRegionDrawable touchBackground,
                          TextureRegionDrawable touchKnob,
                          TextureRegionDrawable buttonA,
                          TextureRegionDrawable buttonAPressed,
                          TextureRegionDrawable buttonB,
                          TextureRegionDrawable buttonBPressed) {

        Touchpad.TouchpadStyle touchpadStyle = new Touchpad.TouchpadStyle();
        touchpadStyle.background = touchBackground;
        touchpadStyle.knob = touchKnob;

        int regionWidth = touchBackground.getRegion().getRegionWidth();
        int regionHeight = touchBackground.getRegion().getRegionHeight();
        touchpad = new Touchpad(regionWidth / 4, touchpadStyle);
        touchpad.setBounds(0, 0, regionWidth, regionHeight);
        shootButton = new ImageButton(buttonA, buttonAPressed);
        powerButton = new ImageButton(buttonB, buttonBPressed);
        addComponent(touchpad);
        addComponent(shootButton);
        addComponent(powerButton);
        addListener(new ChangeListener() {

            @Override
            public void changed(ChangeEvent event, UIComponent component) {
                for (GameControllerListener listener : listeners) {
                    if (component == touchpad) {
                        GameControllerListener.Direction direction = getDirection(touchpad);
                        if (direction != GameControllerListener.Direction.NONE) {
                            listener.KnobMoved(touchpad, getDirection(touchpad));
                        }

                    } else if (component == shootButton) {
                        listener.ButtonPressed(GameControllerListener.GameButton.BUTTON_A);

                    } else if (component == powerButton) {
                        listener.ButtonPressed(GameControllerListener.GameButton.BUTTON_B);

                    }

                }

            }
        });

    }

    private GameControllerListener.Direction getDirection(Touchpad touchpad) {
        float knobY = touchpad.getKnobPercentY();
        float konbX = touchpad.getKnobPercentX();
        if (!(knobY == 0.0 && konbX == 0.0)) {
            float alpha = MathUtils.atan2(knobY, konbX) *
                    MathUtils.radiansToDegrees;
            if (alpha >= -22.5 & alpha < 0) return GameControllerListener.Direction.EAST;
            else if (alpha >= 0 & alpha < 22.5) return GameControllerListener.Direction.EAST;
            else if (alpha >= 22.5 && alpha < 67.5) return GameControllerListener.Direction.NORTHEAST;
            else if (alpha >= 67.5 && alpha < 112.5) return GameControllerListener.Direction.NORTH;
            else if (alpha >= 112.5 && alpha < 157.5) return GameControllerListener.Direction.NORTHWEST;
            else if (alpha >= 157.5 && alpha < 180) return GameControllerListener.Direction.WEST;
            else if (alpha <= -157.5 && alpha > -180) return GameControllerListener.Direction.WEST;
            else if (alpha <= -112.5 && alpha > -157.5) return GameControllerListener.Direction.SOUTHWEST;
            else if (alpha <= -67.5 && alpha > -112.5) return GameControllerListener.Direction.SOUTH;
            else if (alpha <= -22.5 && alpha > -67.5) return GameControllerListener.Direction.SOUTHEAST;
        }

        return GameControllerListener.Direction.NONE;

    }

    @Override
    public void layout() {
        float height = touchpad.getHeight();
        float width = getParent().getWidth();
        shootButton.setX(width - 2 * shootButton.getWidth());
        shootButton.setY(height / 2 - shootButton.getHeight() / 2);
        powerButton.setX(width - powerButton.getWidth());
        powerButton.setY(height / 2 - powerButton.getHeight() / 2);
    }


    /**
     * Add a listener to receive events for this GameController
     *
     * @see com.guidebee.game.ui.InputListener
     * @see com.guidebee.game.ui.ClickListener
     */
    public boolean addGameControllerListener(GameControllerListener listener) {
        if (!listeners.contains(listener, true)) {
            listeners.add(listener);
            return true;
        }
        return false;
    }

    /**
     * remove a game controller listener.
     *
     * @param listener
     * @return
     */
    public boolean removeGameControllerListener(GameControllerListener listener) {
        return listeners.removeValue(listener, true);
    }

    /**
     * Removes all game controller listeners.
     */
    public void clearGameControllerListeners() {
        listeners.clear();
    }

    /**
     * Add a listener to receive events that {@link #hit(float, float, boolean) hit}
     * this component. See {@link #fire(com.guidebee.game.ui.Event)}.
     *
     * @see com.guidebee.game.ui.InputListener
     * @see com.guidebee.game.ui.ClickListener
     */
    public boolean addListener(EventListener listener) {
        boolean ret = touchpad.addListener(listener);
        if (shootButton.isVisible()) {
            ret &= shootButton.addListener(listener);
        }
        if (powerButton.isVisible()) {
            ret &= powerButton.addListener(listener);
        }
        return ret;
    }

    /**
     * @param deadzoneRadius The distance in pixels from the center of the touchpad
     *                       required for the knob to be moved.
     */
    public void setDeadzone(float deadzoneRadius) {
        touchpad.setDeadzone(deadzoneRadius);
    }

    /**
     * Returns the x-position of the knob relative to the center of the widget.
     * The positive direction is right.
     */
    public float getKnobX() {
        return touchpad.getKnobX();
    }

    /**
     * Returns the y-position of the knob relative to the center of the widget.
     * The positive direction is up.
     */
    public float getKnobY() {
        return touchpad.getKnobY();
    }

    /**
     * Returns the x-position of the knob as a percentage from the center of the
     * touchpad to the edge of the circular movement
     * area. The positive direction is right.
     */
    public float getKnobPercentX() {
        return touchpad.getKnobPercentX();
    }

    /**
     * Returns the y-position of the knob as a percentage from the center of the
     * touchpad to the edge of the circular movement
     * area. The positive direction is up.
     */
    public float getKnobPercentY() {
        return touchpad.getKnobPercentY();
    }


    /**
     * Set alpha value when draw the game controller on screen.
     *
     * @param a
     */
    public void setAlpha(float a) {
        touchpad.setColor(1f, 1f, 1f, a);
        shootButton.setColor(1f, 1f, 1f, a);
        powerButton.setColor(1f, 1f, 1f, a);
    }


    /**
     * check if button A is pressed.
     *
     * @return
     */
    public boolean isButtonAPressed() {
        return shootButton.isPressed();
    }

    /**
     * check if button B is pressed.
     *
     * @return
     */
    public boolean isButtonBPressed() {
        return powerButton.isPressed();
    }

    /**
     * set the visibility of button A.
     *
     * @param visible
     */
    public void setButtonAVisible(boolean visible) {
        shootButton.setVisible(visible);
    }

    /**
     * Set the visibility of button B.
     *
     * @param visible
     */
    public void setButtonBVisible(boolean visible) {
        powerButton.setVisible(visible);
    }
}
