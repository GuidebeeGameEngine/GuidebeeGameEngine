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
package com.guidebee.game.scene.collision;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.Collidable;
import com.guidebee.game.physics.Body;
import com.guidebee.game.physics.Contact;
import com.guidebee.game.physics.Fixture;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * define collision object. a collision involves two objects.
 */
public class Collision {
    private Collidable objectA;
    private Collidable objectB;
    private boolean isTouching;
    private int collisionType;

    /**
     * Constructor.
     * @param obj1
     * @param obj2
     * @param type
     */
    public Collision(Collidable obj1, Collidable obj2, int type) {
        objectA = obj1;
        objectB = obj2;
        isTouching = true;
        collisionType = type;
    }

    /**
     * Constructor.
     * @param contact
     */
    public Collision(Contact contact) {
        Fixture fixture1 = contact.getFixtureA();
        Fixture fixture2 = contact.getFixtureB();

        if (fixture1 != null && fixture2 != null) {
            Body body1 = fixture1.getBody();
            Body body2 = fixture2.getBody();

            if (body1 != null) {
                objectA = (Collidable) body1.getUserData();
            }

            if (body2 != null) {
                objectB = (Collidable) body2.getUserData();
            }
        }
        collisionType = Collidable.BOX2D_CONTACT;
        isTouching = contact.isTouching();
    }

    /**
     * get first collidable object.
     * @return
     */
    public Collidable getObjectA() {
        return objectA;
    }

    /**
     * get second collidable object.
     * @return
     */
    public Collidable getObjectB() {
        return objectB;
    }

    public boolean isTouching() {
        return isTouching;
    }

    /**
     * get collision type.
     * @return
     */
    public int getCollisionType() {
        return collisionType;
    }


}
