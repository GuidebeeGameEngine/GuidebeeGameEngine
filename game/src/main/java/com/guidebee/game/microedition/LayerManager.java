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
package com.guidebee.game.microedition;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.camera.Camera;
import com.guidebee.game.camera.viewports.Viewport;
import com.guidebee.game.graphics.Batch;
import com.guidebee.game.scene.Stage;
import com.guidebee.math.Vector3;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * The LayerManager manages a series of Layers. The LayerManager simplifies the
 * process of rendering the Layers that have been added to it by automatically
 * rendering the correct regions of each Layer in the appropriate order. The
 * LayerManager maintains an ordered list to which Layers can be appended,
 * inserted and removed. A Layer's index correlates to its z-order; the layer
 * at index 0 is closest to the user while a the Layer with the highest index
 * is furthest away from the user. The indices are always contiguous; that is,
 * if a Layer is removed, the indices of subsequent Layers will be adjusted to
 * maintain continuity. The LayerManager class provides several features that
 * control how the game's Layers are rendered on the screen. The view window
 * controls the size of the visible region and its position relative to
 * the LayerManager's coordinate system. Changing the position of the view
 * window enables effects such as scrolling or panning the user's view.
 * For example, to scroll to the right, simply move the view window's
 * location to the right. The size of the view window controls how large
 * the user's view will be, and is usually fixed at a size that is appropriate
 * for the device's screen.
 * <hr>
 *
 * @author James Shen.
 */
public class LayerManager extends Stage {

    /**
     * Creates a stage with a
     * {@link com.guidebee.game.camera.viewports.ScalingViewport}
     * set to {@link com.guidebee.utils.Scaling#fill}.
     * The stage will use its own {@link Batch} which
     * will be disposed when the stage is disposed.
     */
    public LayerManager() {
        super();
    }


    /**
     * Creates a stage with the specified viewport. The stage will use its own
     * {@link Batch} which will be disposed when the stage
     * is disposed.
     */
    public LayerManager(Viewport viewport) {
        super(viewport);
    }


    /**
     * Creates a stage with the specified viewport and batch. This can be used to
     * avoid creating a new batch (which can be somewhat
     * slow) if multiple stages are used during an application's life time.
     *
     * @param batch Will not be disposed if {@link #dispose()} is called, handle
     *              disposal yourself.
     */
    public LayerManager(Viewport viewport, Batch batch) {
        super(viewport, batch);
    }

    /**
     * Appends a Layer to this LayerManager.The Layer is appended to the list of
     * existing Layers such that it has the highest index (i.e. it is furthest
     * away from the user). The Layer is first removed from this LayerManager
     * if it has already been added.
     *
     * @param layer the Layer to be added
     */
    public void append(Layer layer) {
        addActor(layer);
    }

    /**
     * Gets the Layer with the specified index.
     *
     * @param i the Layer to be inserted
     * @return the index at which the new Layer is to be inserted
     */
    public Layer getLayerAt(int i) {
        // needs not be synchronized
        return (Layer) getActors().get(i);
    }

    /**
     * Gets the number of Layers in this LayerManager.
     *
     * @return the number of Layers
     */
    public int getSize() {
        // needs not be synchronized
        return getActors().size;
    }

    /**
     * Removes the specified Layer from this LayerManager. This method does
     * nothing if the specified Layer is not added to the this LayerManager
     *
     * @param layer the Layer to be removed
     */
    public void remove(Layer layer) {
        layer.remove();
    }



    private final Vector3 oldCameraPos=new Vector3();
    /**
     * Renders the LayerManager's current view window at the specified location.
     * @param x  the horizontal location at which to render the view window.
     * @param y  the vertical location at which to render the view window.
     */

    public void draw(int x,int y){
        Camera camera=getCamera();
        oldCameraPos.x=camera.position.x;
        oldCameraPos.y=camera.position.y;
        camera.translate(-x,-y,0);
        draw();
        camera.position.x=oldCameraPos.x;
        camera.position.y=oldCameraPos.y;
    }

}
