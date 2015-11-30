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
package com.guidebee.game.ui.actions;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.ui.EventListener;
import com.guidebee.game.ui.UIComponent;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Removes a listener from an component.
 *
 * @author Nathan Sweet
 */
public class RemoveListenerAction extends Action {
    private UIComponent targetActor;
    private EventListener listener;
    private boolean capture;

    public boolean act(float delta) {
        UIComponent component = (targetActor != null ? targetActor : this.component);
        if (capture)
            component.removeCaptureListener(listener);
        else
            component.removeListener(listener);
        return true;
    }

    public UIComponent getTargetActor() {
        return targetActor;
    }

    /**
     * Sets the component to remove a listener from. If null (the default),
     * the {@link #getActor() component} will be used.
     */
    public void setTargetActor(UIComponent component) {
        this.targetActor = component;
    }

    public EventListener getListener() {
        return listener;
    }

    public void setListener(EventListener listener) {
        this.listener = listener;
    }

    public boolean getCapture() {
        return capture;
    }

    public void setCapture(boolean capture) {
        this.capture = capture;
    }

    public void reset() {
        super.reset();
        targetActor = null;
        listener = null;
    }
}
