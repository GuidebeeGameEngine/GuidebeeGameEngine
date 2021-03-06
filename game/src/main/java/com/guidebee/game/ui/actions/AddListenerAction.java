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
 * Adds a listener to an component.
 *
 * @author Nathan Sweet
 */
public class AddListenerAction extends Action {
    private UIComponent targetComponent;
    private EventListener listener;
    private boolean capture;

    public boolean act(float delta) {
        UIComponent component = (targetComponent != null ? targetComponent : this.component);
        if (capture)
            component.addCaptureListener(listener);
        else
            component.addListener(listener);
        return true;
    }

    public UIComponent getTargetComponent() {
        return targetComponent;
    }

    /**
     * Sets the component to add a listener to. If null (the default), the
     * {@link #getComponent() component} will be used.
     */
    public void setTargetComponent(UIComponent component) {
        this.targetComponent = component;
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
        targetComponent = null;
        listener = null;
    }
}
