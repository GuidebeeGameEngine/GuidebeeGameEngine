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

import com.guidebee.game.scene.actions.Action;
import com.guidebee.game.scene.actions.Actions;
import com.guidebee.math.Interpolation;
import com.guidebee.utils.collections.ObjectMap;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Displays a dialog, which is a modal window containing a content table with a
 * button table underneath it. Methods are provided
 * to add a label to the content table and buttons to the button table, but any
 * widgets can be added. When a button is clicked,
 * {@link #result(Object)} is called and the dialog is removed from the stage.
 *
 * @author Nathan Sweet
 */
public class Dialog extends ChildWindow {
    Table contentTable, buttonTable;
    private Skin skin;
    ObjectMap<UIComponent, Object> values = new ObjectMap();
    boolean cancelHide;
    UIComponent previousKeyboardFocus, previousScrollFocus;

    InputListener ignoreTouchDown = new InputListener() {
        public boolean touchDown(InputEvent event, float x, float y,
                                 int pointer, int button) {
            event.cancel();
            return false;
        }
    };

    public Dialog(String title, Skin skin) {
        super(title, skin.get(WindowStyle.class));
        this.skin = skin;
        initialize();
    }

    public Dialog(String title, Skin skin, String windowStyleName) {
        super(title, skin.get(windowStyleName, WindowStyle.class));
        setSkin(skin);
        this.skin = skin;
        initialize();
    }

    public Dialog(String title, WindowStyle windowStyle) {
        super(title, windowStyle);
        initialize();
    }

    private void initialize() {
        setModal(true);

        defaults().space(6);
        add(contentTable = new Table(skin)).expand().fill();
        row();
        add(buttonTable = new Table(skin));

        contentTable.defaults().space(6);
        buttonTable.defaults().space(6);

        buttonTable.addListener(new ChangeListener() {
            public void changed(ChangeEvent event, UIComponent actor) {
                if (!values.containsKey(actor)) return;
                while (actor.getParent() != buttonTable)
                    actor = actor.getParent();
                result(values.get(actor));
                if (!cancelHide) hide();
                cancelHide = false;
            }
        });

        addListener(new FocusListener() {
            public void keyboardFocusChanged(FocusEvent event, UIComponent actor,
                                             boolean focused) {
                if (!focused) focusChanged(event);
            }

            public void scrollFocusChanged(FocusEvent event, UIComponent actor,
                                           boolean focused) {
                if (!focused) focusChanged(event);
            }

            private void focusChanged(FocusEvent event) {
                UIWindow stage = getStage();
                if (isModal && stage != null && stage.getRoot().getChildren().size > 0
                        && stage.getRoot().getChildren().peek() == Dialog.this) {
                    // Dialog is top most actor.
                    UIComponent newFocusedActor = event.getRelatedActor();
                    if (newFocusedActor != null
                            && !newFocusedActor.isDescendantOf(Dialog.this)) event.cancel();
                }
            }
        });
    }

    public Table getContentTable() {
        return contentTable;
    }

    public Table getButtonTable() {
        return buttonTable;
    }

    /**
     * Adds a label to the content table. The dialog must have been constructed
     * with a skin to use this method.
     */
    public Dialog text(String text) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the" +
                    " dialog was constructed with a Skin.");
        return text(text, skin.get(Label.LabelStyle.class));
    }

    /**
     * Adds a label to the content table.
     */
    public Dialog text(String text, Label.LabelStyle labelStyle) {
        return text(new Label(text, labelStyle));
    }

    /**
     * Adds the given Label to the content table
     */
    public Dialog text(Label label) {
        contentTable.add(label);
        return this;
    }

    /**
     * Adds a text button to the button table. Null will be passed to
     * {@link #result(Object)} if this button is clicked. The dialog
     * must have been constructed with a skin to use this method.
     */
    public Dialog button(String text) {
        return button(text, null);
    }

    /**
     * Adds a text button to the button table. The dialog must have been constructed
     * with a skin to use this method.
     *
     * @param object The object that will be passed to {@link #result(Object)} if
     *               this button is clicked. May be null.
     */
    public Dialog button(String text, Object object) {
        if (skin == null)
            throw new IllegalStateException("This method may only be used if the" +
                    " dialog was constructed with a Skin.");
        return button(text, object, skin.get(TextButton.TextButtonStyle.class));
    }

    /**
     * Adds a text button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)}
     *               if this button is clicked. May be null.
     */
    public Dialog button(String text, Object object, TextButton.TextButtonStyle buttonStyle) {
        return button(new TextButton(text, buttonStyle), object);
    }

    /**
     * Adds the given button to the button table.
     */
    public Dialog button(Button button) {
        return button(button, null);
    }

    /**
     * Adds the given button to the button table.
     *
     * @param object The object that will be passed to {@link #result(Object)}
     *               if this button is clicked. May be null.
     */
    public Dialog button(Button button, Object object) {
        buttonTable.add(button);
        setObject(button, object);
        return this;
    }

    /**
     * {@link #pack() Packs} the dialog and adds it to the stage with custom action
     * which can be null for instant show
     */
    public Dialog show(UIWindow stage, Action action) {
        clearActions();
        removeCaptureListener(ignoreTouchDown);

        previousKeyboardFocus = null;
        UIComponent actor = stage.getKeyboardFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousKeyboardFocus = actor;

        previousScrollFocus = null;
        actor = stage.getScrollFocus();
        if (actor != null && !actor.isDescendantOf(this)) previousScrollFocus = actor;

        pack();
        stage.addActor(this);
        stage.setKeyboardFocus(this);
        stage.setScrollFocus(this);
        if (action != null)
            addAction(action);

        return this;
    }

    /**
     * {@link #pack() Packs} the dialog and adds it to the stage, centered with
     * default fadeIn action
     */
    public Dialog show(UIWindow stage) {
        show(stage, Actions.sequence(Actions.alpha(0),
                Actions.fadeIn(0.4f, Interpolation.fade)));
        setPosition(Math.round((stage.getWidth() - getWidth()) / 2),
                Math.round((stage.getHeight() - getHeight()) / 2));
        return this;
    }

    /**
     * Hides the dialog with the given action and then removes it from the stage.
     */
    public void hide(Action action) {
        UIWindow stage = getStage();
        if (stage != null) {
            if (previousKeyboardFocus != null
                    && previousKeyboardFocus.getStage() == null) previousKeyboardFocus = null;
            UIComponent actor = stage.getKeyboardFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setKeyboardFocus(previousKeyboardFocus);

            if (previousScrollFocus != null
                    && previousScrollFocus.getStage() == null)
                previousScrollFocus = null;
            actor = stage.getScrollFocus();
            if (actor == null || actor.isDescendantOf(this))
                stage.setScrollFocus(previousScrollFocus);
        }
        if (action != null) {
            addCaptureListener(ignoreTouchDown);
            addAction(Actions.sequence(action,
                    Actions.removeListener(ignoreTouchDown, true),
                    Actions.removeActor()));
        } else
            remove();
    }

    /**
     * Hides the dialog. Called automatically when a button is clicked. The default
     * implementation fades out the dialog over 400
     * milliseconds and then removes it from the stage.
     */
    public void hide() {
        hide(Actions.sequence(Actions.fadeOut(0.4f, Interpolation.fade),
                Actions.removeListener(ignoreTouchDown, true), Actions.removeActor()));
    }

    public void setObject(UIComponent actor, Object object) {
        values.put(actor, object);
    }

    /**
     * If this key is pressed, {@link #result(Object)} is called with the specified object.
     *
     * @see com.guidebee.game.Input.Keys
     */
    public Dialog key(final int keycode, final Object object) {
        addListener(new InputListener() {
            public boolean keyDown(InputEvent event, int keycode2) {
                if (keycode == keycode2) {
                    result(object);
                    if (!cancelHide) hide();
                    cancelHide = false;
                }
                return false;
            }
        });
        return this;
    }

    /**
     * Called when a button is clicked. The dialog will be hidden after this
     * method returns unless {@link #cancel()} is called.
     *
     * @param object The object specified when the button was added.
     */
    protected void result(Object object) {
    }

    public void cancel() {
        cancelHide = true;
    }
}
