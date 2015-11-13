/**
 *
 */
//--------------------------------- PACKAGE ------------------------------------
package com.guidebee.game.maps.objects;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.maps.MapObject;
import com.guidebee.math.geometry.Polygon;

//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * Represents {@link Polygon} map objects
 */
public class PolygonMapObject extends MapObject {

    private Polygon polygon;

    /**
     * @return polygon shape
     */
    public Polygon getPolygon() {
        return polygon;
    }

    /**
     * @param polygon new object's polygon shape
     */
    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }

    /**
     * Creates empty polygon map object
     */
    public PolygonMapObject() {
        this(new float[0]);
    }

    /**
     * @param vertices polygon defining vertices (at least 3)
     */
    public PolygonMapObject(float[] vertices) {
        polygon = new Polygon(vertices);
    }

    /**
     * @param polygon the polygon
     */
    public PolygonMapObject(Polygon polygon) {
        this.polygon = polygon;
        setBoundingRect(polygon.getBoundingRectangle());
    }


}
