/*******************************************************************************
 * Copyright 2014 See AUTHORS file.
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
package com.guidebee.game.scene.actions;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.scene.Actor;
import com.guidebee.game.tween.BaseTween;
import com.guidebee.game.tween.Tween;
import com.guidebee.game.tween.TweenAccessor;
import com.guidebee.game.tween.TweenManager;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Tween action.
 * @author James Shen.
 */
public class TweenAction extends Action {

    /**
     * tween position x.
     */
    public static final int POSITION_X = 1;

    /**
     * tween position y.
     */
    public static final int POSITION_Y = 2;

    /**
     * tween position x,y together.
     */
    public static final int POSITION_XY = 3;

    /**
     * tween scale x.
     */
    public static final int SCALE_X = 4;

    /**
     * tween scale y.
     */
    public static final int SCALE_Y = 5;

    /**
     * tween scale xy togeter.
     */
    public static final int SCALE_XY = 6;

    /**
     * tween alpha.
     */
    public static final int ALPHA = 7;

    /**
     * tween rotation.
     */
    public static final int ROTATION = 8;


    private final BaseTween<Actor> tween;
    private final TweenManager tweenManager;

    /**
     * Constructor.
     *
     * @param tween
     */
    public TweenAction(BaseTween tween) {
        tweenManager = new TweenManager();
        this.tween = tween;


    }


    /**
     * start the tween action.
     */
    public void start(){
        tween.start(tweenManager);

    }




    /**
     * return tween object.
     *
     * @return
     */
    public BaseTween getTween() {
        return tween;
    }


    static {
        Tween.registerAccessor(Actor.class, new ActorAccessor());
    }

    @Override
    public boolean act(float delta) {
        tweenManager.update(delta);
        return tween.isFinished();
    }

    static class ActorAccessor implements TweenAccessor<Actor> {
        @Override
        public int getValues(Actor target, int tweenType, float[] returnValues) {
            switch (tweenType) {
                case POSITION_X:
                    returnValues[0] = target.getX();
                    return 1;
                case POSITION_Y:
                    returnValues[0] = target.getY();
                    return 1;
                case POSITION_XY:
                    returnValues[0] = target.getX();
                    returnValues[1] = target.getY();
                    return 2;
                case SCALE_X:
                    returnValues[0] = target.getScaleX();
                    return 1;
                case SCALE_Y:
                    returnValues[0] = target.getScaleY();
                    return 1;
                case SCALE_XY:
                    returnValues[0] = target.getScaleX();
                    returnValues[1] = target.getScaleY();
                    return 2;
                case ALPHA:
                    returnValues[0] = target.getColor().r;
                    return 1;
                case ROTATION:
                    returnValues[0] = target.getRotation();
                    return 1;

            }
            return -1;
        }

        @Override
        public void setValues(Actor target, int tweenType, float[] newValues) {
            switch (tweenType) {
                case POSITION_X:
                    target.setX(newValues[0]);
                    break;
                case POSITION_Y:
                    target.setY(newValues[0]);
                    break;

                case POSITION_XY:
                    target.setX(newValues[0]);
                    target.setY(newValues[1]);
                    break;
                case SCALE_X:
                    target.setScaleX(newValues[0]);
                    break;
                case SCALE_Y:
                    target.setScaleY(newValues[0]);
                    break;
                case SCALE_XY:
                    target.setScaleX(newValues[0]);
                    target.setScaleY(newValues[1]);
                    break;
                case ALPHA:
                    target.setAlpha(newValues[0]);
                    break;
                case ROTATION:
                    target.setRotation(newValues[0]);
                    break;

            }

        }
    }


}

