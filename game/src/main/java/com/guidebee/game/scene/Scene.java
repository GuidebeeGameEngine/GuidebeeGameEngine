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
package com.guidebee.game.scene;

//--------------------------------- IMPORTS ------------------------------------
import com.guidebee.game.ScreenAdapter;
import com.guidebee.game.camera.viewports.Viewport;
import com.guidebee.game.graphics.Batch;



//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * Alias of ScreenAdapter to support the game play concepts.
 *
 * @author semtiko
 */

public class Scene extends ScreenAdapter{

    /**
     * scene associated stage.
     */
    protected final Stage sceneStage;


    /**
     * Constructor with passed in stage.
     * @param stage
     */
    public Scene(Stage stage){
        sceneStage=stage;
    }

    /**
     * Default constructor.
     */
    public Scene(){
        sceneStage=new Stage();
    }

    /**
     * Constructor
     * @param viewPort
     */
    public Scene(Viewport viewPort){
        sceneStage=new Stage(viewPort);
    }


    /**
     * Constructor
     * @param viewport
     * @param batch
     */
    public Scene(Viewport viewport,Batch batch){
        sceneStage=new Stage(viewport,batch);
    }


    @Override
    public void render(float delta){
        sceneStage.act();
        sceneStage.draw();
    }


    @Override
    public void dispose(){
        sceneStage.dispose();

    }

    /**
     * Get scene associated stage object.
     * @return scene associated stage object.
     */
    public Stage getStage(){
        return sceneStage;
    }

    @Override
    public void resize(int width, int height){
        sceneStage.getViewport().update(width,height,false);
    }


}
