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
 * Provides methods for an component to participate in layout and to provide a minimum,
 * preferred, and maximum size.
 *
 * @author Nathan Sweet
 */
public interface Layout {
    /**
     * Computes and caches any information needed for drawing and, if this component has
     * children, positions and sizes each child,
     * calls {@link #invalidate()} any each child whose width or height has changed,
     * and calls {@link #validate()} on each child.
     * This method should almost never be called directly, instead {@link #validate()}
     * should be used.
     */
    public void layout();

    /**
     * Invalidates this component's layout, causing {@link #layout()} to happen the next
     * time {@link #validate()} is called. This
     * method should be called when state changes in the component that requires a layout
     * but does not change the minimum, preferred,
     * maximum, or actual size of the component (meaning it does not affect the parent
     * component's layout).
     */
    public void invalidate();

    /**
     * Invalidates this component and all its parents, calling {@link #invalidate()} on
     * all involved components. This method should be
     * called when state changes in the component that affects the minimum, preferred,
     * maximum, or actual size of the component (meaning it
     * it potentially affects the parent component's layout).
     */
    public void invalidateHierarchy();

    /**
     * Ensures the component has been laid out. Calls {@link #layout()} if
     * {@link #invalidate()} has called since the last time
     * {@link #validate()} was called, or if the component otherwise needs to be laid out.
     * This method is usually called in
     * {@link UIComponent#draw(com.guidebee.game.graphics.Batch, float)}
     * before drawing is performed.
     */
    public void validate();

    /**
     * Sizes this component to its preferred width and height, then calls
     * {@link #validate()}.
     * <p/>
     * Generally this method should not be called in an component's constructor because
     * it calls {@link #layout()}, which means a
     * subclass would have layout() called before the subclass' constructor. Instead,
     * in constructors, simply set the component's size
     * to {@link #getPrefWidth()} and {@link #getPrefHeight()}. This allows the component
     * to have a size at construction time for more
     * convenient use outside of a {@link com.guidebee.game.ui.Table}.
     */
    public void pack();

    /**
     * If true, this component will be sized to the parent in {@link #validate()}. If the
     * parent is the window, the component will be sized
     * to the window.
     */
    public void setFillParent(boolean fillParent);

    /**
     * Enables or disables the layout for this component and all child components, recursively.
     * When false, {@link #validate()} will not
     * cause a layout to occur. This is useful when an component will be manipulated
     * externally, such as with actions. Default is true.
     */
    public void setLayoutEnabled(boolean enabled);

    public float getMinWidth();

    public float getMinHeight();

    public float getPrefWidth();

    public float getPrefHeight();

    /**
     * Zero indicates no max width.
     */
    public float getMaxWidth();

    /**
     * Zero indicates no max height.
     */
    public float getMaxHeight();
}
