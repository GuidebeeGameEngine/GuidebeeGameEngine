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
 * Value placeholder, allowing the value to be computed on request. Values are
 * provided an component for context which reduces the
 * number of value instances that need to be created and reduces verbosity
 * in code that specifies values.
 *
 * @author Nathan Sweet
 */
abstract public class Value {
    /**
     * @param context May be null.
     */
    abstract public float get(UIComponent context);

    /**
     * A value that is always zero.
     */
    static public final Fixed zero = new Fixed(0);

    /**
     * A fixed value that is not computed each time it is used.
     *
     * @author Nathan Sweet
     */
    static public class Fixed extends Value {
        private final float value;

        public Fixed(float value) {
            this.value = value;
        }

        public float get(UIComponent context) {
            return value;
        }
    }

    /**
     * Value that is the minWidth of the component in the cell.
     */
    static public Value minWidth = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getMinWidth();
            return context == null ? 0 : context.getWidth();
        }
    };

    /**
     * Value that is the minHeight of the component in the cell.
     */
    static public Value minHeight = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getMinHeight();
            return context == null ? 0 : context.getHeight();
        }
    };

    /**
     * Value that is the prefWidth of the component in the cell.
     */
    static public Value prefWidth = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getPrefWidth();
            return context == null ? 0 : context.getWidth();

        }
    };

    /**
     * Value that is the prefHeight of the component in the cell.
     */
    static public Value prefHeight = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getPrefHeight();
            return context == null ? 0 : context.getHeight();
        }
    };

    /**
     * Value that is the maxWidth of the component in the cell.
     */
    static public Value maxWidth = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getMaxWidth();
            return context == null ? 0 : context.getWidth();
        }
    };

    /**
     * Value that is the maxHeight of the component in the cell.
     */
    static public Value maxHeight = new Value() {
        public float get(UIComponent context) {
            if (context instanceof Layout) return ((Layout) context).getMaxHeight();
            return context == null ? 0 : context.getHeight();
        }
    };

    /**
     * Returns a value that is a percentage of the component's width.
     */
    static public Value percentWidth(final float percent) {
        return new Value() {
            public float get(UIComponent component) {
                return component.getWidth() * percent;
            }
        };
    }

    /**
     * Returns a value that is a percentage of the component's height.
     */
    static public Value percentHeight(final float percent) {
        return new Value() {
            public float get(UIComponent component) {
                return component.getHeight() * percent;
            }
        };
    }

    /**
     * Returns a value that is a percentage of the specified component's width.
     * The context component is ignored.
     */
    static public Value percentWidth(final float percent, final UIComponent component) {
        if (component == null)
            throw new IllegalArgumentException("component cannot be null.");
        return new Value() {
            public float get(UIComponent context) {
                return component.getWidth() * percent;
            }
        };
    }

    /**
     * Returns a value that is a percentage of the specified component's height.
     * The context component is ignored.
     */
    static public Value percentHeight(final float percent, final UIComponent component) {
        if (component == null)
            throw new IllegalArgumentException("component cannot be null.");
        return new Value() {
            public float get(UIComponent context) {
                return component.getHeight() * percent;
            }
        };
    }
}
