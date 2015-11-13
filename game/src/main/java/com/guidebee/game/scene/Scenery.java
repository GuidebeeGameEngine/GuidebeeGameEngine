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
package com.guidebee.game.scene;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.Collidable;
import com.guidebee.game.GameEngine;
import com.guidebee.game.engine.maps.tiled.TiledMapRenderer;
import com.guidebee.game.engine.maps.tiled.renderers.IsometricTiledMapRenderer;
import com.guidebee.game.engine.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.guidebee.game.maps.MapLayer;
import com.guidebee.game.maps.MapLayers;
import com.guidebee.game.maps.MapObject;
import com.guidebee.game.maps.tiled.TiledMap;
import com.guidebee.game.maps.tiled.TiledMapTile;
import com.guidebee.game.maps.tiled.TiledMapTileLayer;
import com.guidebee.game.physics.*;
import com.guidebee.math.geometry.Rectangle;
import com.guidebee.utils.Disposable;
import com.guidebee.utils.collections.Array;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Game scene background scenery.
 */
public class Scenery implements Disposable {


    /**
     * map type.
     */
    public enum MapType {
        OrthogonalTiled,
        IsometricTiled

    }

    protected Stage stage;
    protected final TiledMap tiledMap;
    protected TiledMapRenderer tiledMapRenderer;
    protected final MapType mapType;

    protected int[] backGroundLayers;

    protected int[] foreGroundLayers;

    /**
     * Constructor.
     *
     * @param map
     */
    public Scenery(TiledMap map) {
        this(map, MapType.OrthogonalTiled);
    }

    /**
     * Constructor.
     *
     * @param map
     * @param mapType
     */
    public Scenery(TiledMap map, MapType mapType) {
        this.tiledMap = map;
        this.mapType = mapType;
    }


    @Override
    public void dispose() {
        if (tiledMapRenderer != null) {
            if (tiledMapRenderer instanceof OrthogonalTiledMapRenderer) {
                ((OrthogonalTiledMapRenderer) tiledMapRenderer).dispose();
            } else if (tiledMapRenderer instanceof IsometricTiledMapRenderer) {
                ((IsometricTiledMapRenderer) tiledMapRenderer).dispose();
            }
        }
        if (tiledMap != null) {
            tiledMap.dispose();
        }

    }


    /**
     * Set foreground layers.
     *
     * @param foreGroundLayers
     */
    public void setForeGroundLayers(int[] foreGroundLayers) {
        this.foreGroundLayers = foreGroundLayers;
    }


    /**
     * set backgroudn layers.
     *
     * @param backGroundLayers
     */
    public void setBackGroundLayers(int[] backGroundLayers) {
        this.backGroundLayers = backGroundLayers;
    }


    /**
     * render background layers.
     */
    public void renderBackgroundLayers() {
        if (tiledMapRenderer != null) {
            if (backGroundLayers != null) {
                tiledMapRenderer.render(backGroundLayers);

            } else {
                tiledMapRenderer.render();
            }
        }
    }


    /**
     * render foreground layers.
     */
    public void renderForegroundLayers() {
        if (tiledMapRenderer != null) {
            if (foreGroundLayers != null) {
                tiledMapRenderer.render(foreGroundLayers);
            }
        }
    }


    /**
     * get all collidable object in the background.
     *
     * @return
     */
    public Array<Collidable> getAllCollidables() {
        Array<Collidable> collidables = new Array<Collidable>(false, 32);
        if (tiledMap != null) {
            MapLayers mapLayers = tiledMap.getLayers();
            for (MapLayer mapLayer : mapLayers) {
                if (mapLayer instanceof TiledMapTileLayer) {
                    TiledMapTileLayer tiledMapTileLayer = (TiledMapTileLayer) mapLayer;
                    int tilesX =tiledMapTileLayer.getWidth();
                    int tilesY = tiledMapTileLayer.getHeight();
                    for (int i = 0; i < tilesX; i++) {
                        for (int j = 0; j < tilesY; j++) {
                            TiledMapTileLayer.Cell cell = tiledMapTileLayer.getCell(i, j);
                            if (cell != null) {
                                TiledMapTile tiledMapTile = cell.getTile();
                                if (tiledMapTile != null) {
                                    if (tiledMapTile.isEnabled()) {
                                        collidables.add(tiledMapTile);
                                    }
                                }

                            }
                        }
                    }


                }else if(mapLayer instanceof MapLayer){
                    for(MapObject mapObject: mapLayer.getObjects()){
                        if(mapObject.isEnabled()){
                            collidables.add(mapObject);
                        }

                    }
                }
            }
        }
        return collidables;
    }

    /**
     * initialize box2d bodies.
     *
     * @param mapTileLayer
     * @param x
     * @param y
     */
    public void initBody(TiledMapTileLayer mapTileLayer, int x, int y) {
        TiledMapTileLayer.Cell cell = mapTileLayer.getCell(x, y);
        if (cell != null) {
            TiledMapTile tiledMapTile = cell.getTile();
            if (tiledMapTile != null) {
                Rectangle rectangle = new Rectangle(0,
                        0, mapTileLayer.getTileWidth(),
                        mapTileLayer.getTileHeight());
                initBody(mapTileLayer, x, y, BodyDef.BodyType.DynamicBody,
                        Shape.Type.Polygon, rectangle, 1.0f, 0, 1.0f);
            }
        }


    }

    /**
     * initialize box2d bodies.
     *
     * @param mapTileLayer
     * @param x
     * @param y
     * @param bodyType
     */
    public void initBody(TiledMapTileLayer mapTileLayer, int x, int y, BodyDef.BodyType bodyType) {
        TiledMapTileLayer.Cell cell = mapTileLayer.getCell(x, y);
        if (cell != null) {
            TiledMapTile tiledMapTile = cell.getTile();
            if (tiledMapTile != null) {
                Rectangle rectangle = new Rectangle(0,
                        0, mapTileLayer.getTileWidth(),
                        mapTileLayer.getTileHeight());
                initBody(mapTileLayer, x, y, bodyType, Shape.Type.Polygon, rectangle, 1.0f, 0, 1.0f);
            }
        }


    }

    /**
     * initialize box2d bodies.
     *
     * @param mapTileLayer
     * @param x
     * @param y
     * @param type
     * @param shapeType
     * @param rect
     * @param density
     * @param restitution
     * @param friction
     */
    public void initBody(TiledMapTileLayer mapTileLayer, int x, int y,
                         BodyDef.BodyType type, Shape.Type shapeType, Rectangle rect,
                         float density, float restitution, float friction) {
        if (this.stage == null) {
            throw new RuntimeException("Please add this scenery to a stage first");
        }
        World world = stage.getWorld();
        if (world == null) {
            throw new RuntimeException("Please initialize world in stage first");
        }

        TiledMapTileLayer.Cell cell = mapTileLayer.getCell(x, y);
        if (cell != null) {
            TiledMapTile tiledMapTile = cell.getTile();
            if (tiledMapTile != null) {
                Object object = tiledMapTile.getUserObject();
                if (object != null && object instanceof Body) {
                    Body body = (Body) object;
                    stage.bodiesTobeDeleted.add(body);

                }

                BodyDef bodyDef = new BodyDef();
                bodyDef.type = type;
                bodyDef.position.set(GameEngine.toBox2D((x * mapTileLayer.getTileWidth()
                                + rect.getX() + rect.getWidth() / 2)),
                        GameEngine.toBox2D(y * mapTileLayer.getHeight()
                                + rect.getY() + rect.getHeight() / 2));
                Body body = world.createBody(bodyDef);
                Shape shape;
                if (shapeType == Shape.Type.Circle) {
                    shape = new CircleShape();
                    shape.setRadius(GameEngine.toBox2D(Math.min(rect.getWidth() / 2, rect.getHeight()) / 2));

                } else {
                    shape = new PolygonShape();
                    PolygonShape polygonShape = (PolygonShape) shape;
                    polygonShape.setAsBox(GameEngine.toBox2D(rect.getWidth() / 2),
                            GameEngine.toBox2D(rect.getHeight() / 2));

                }
                FixtureDef fixtureDef = new FixtureDef();
                fixtureDef.shape = shape;
                fixtureDef.density = density;
                fixtureDef.restitution = restitution;
                fixtureDef.friction = friction;
                Fixture fixture = body.createFixture(fixtureDef);

                tiledMapTile.setUserObject(body);
                body.setUserData(tiledMapTile);
                shape.dispose();


            }
        }


    }


}
