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

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.ui.Layout;
import com.guidebee.game.ui.UIComponent;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Sets an actor's
 * {@link com.guidebee.game.ui.Layout#setLayoutEnabled(boolean) layout}
 * to enabled or disabled. The actor must implements
 * {@link com.guidebee.game.ui.Layout}.
 *
 * @author Nathan Sweet
 */
public class LayoutAction extends Action {
    private boolean enabled;

    public void setActor(UIComponent actor) {
        if (actor != null && !(actor instanceof Layout))
            throw new GameEngineRuntimeException("UIComponent must implement layout: "
                    + actor);
        super.setActor(actor);
    }

    public boolean act(float delta) {
        ((Layout) actor).setLayoutEnabled(enabled);
        return true;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setLayoutEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
