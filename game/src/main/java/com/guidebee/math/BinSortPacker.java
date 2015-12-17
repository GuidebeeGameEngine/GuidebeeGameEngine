package com.guidebee.math;

import com.guidebee.math.geometry.Rectangle;

/**
 * This class is used to pack 2D rectangle optimally.
 */
public final class BinSortPacker {

    /**
     * Root node
     */
    private BinSortNode root;

    /**
     * current width
     */
    private float usedWidth;

    /**
     * current height
     */
    private float usedHeight;

    private boolean resized=false;


    /**
     * Constructor
     * @param width initial width
     * @param height initial height
     */
    public BinSortPacker(float width, float height) {
        root = new BinSortNode();
        reset(width,height);

    }

    public void reset(){
        root.x = 0;
        root.y = 0;
        root.left = null;
        root.right = null;

    }

    /**
     * reset the bin packer.
     * @param width
     * @param height
     */
    public void reset(float width, float height) {
        root.x = 0;
        root.y = 0;
        root.width = width;
        root.height = height;
        root.left = null;
        root.right = null;
        usedWidth = 0;
        usedHeight = 0;
    }

    /**
     * Get demension of the bin packer
     * @return
     */
    public Rectangle getDemensions() {
        return new Rectangle(0, 0, root.width, root.height);
    }


    /**
     * Add one rectangle to the packer
     * @param width
     * @param height
     * @return location of the top left of the retangle.
     */
    public Vector2 addRectangle(float width, float height) {
        Vector2 Coords = recursiveFindCoords(root, width, height);
        resized=false;
        if (Coords != null) {
            if (usedWidth < Coords.x + width) {
                usedWidth = Coords.x + width;
                resized=true;
            }
            if (usedHeight < Coords.y + height){
                usedHeight = Coords.y + height;
                resized=true;
            }
        }

        return Coords;
    }


    /**
     * Check to see if resized.
     * @return
     */
    public boolean isResized(){
        return resized;
    }

    private Vector2 recursiveFindCoords(BinSortNode node, float width, float height) {
        // if we are not at a leaf then go deeper
        if (node.left != null) {
            // check first the left branch if not found then go by the right
            Vector2 coords = recursiveFindCoords(node.left, width, height);
            if (coords != null) {
                return coords;
            } else {
                return recursiveFindCoords(node.right, width, height);
            }

        }


        // if already used or it's too big then return
        if (node.used || width > node.width || height > node.height)
            return null;

        // if it fits perfectly then use this gap
        if (Math.abs(width - node.width) < .0001 && Math.abs(height - node.height) < .0001) {
            node.used = true;
            return new Vector2(node.x, node.y);
        }

        // initialize the left and right leafs by clonning the current one
        node.left = node.clone();
        node.right = node.clone();

        // checks if we partition in vertical or horizontal
        if (node.width - width > node.height - height) {
            node.left.width = width;
            node.right.x = node.x + width;
            node.right.width = node.width - width;
        } else {
            node.left.height = height;
            node.right.y = node.y + height;
            node.right.height = node.height - height;
        }

        return recursiveFindCoords(node.left, width, height);
    }



}


class BinSortNode extends Rectangle implements Cloneable {

    public boolean used;

    public BinSortNode left;
    public BinSortNode right;

    @Override
    public BinSortNode clone() {
        BinSortNode node = new BinSortNode();
        node.x = x;
        node.y = y;
        node.width = width;
        node.height = height;

        return node;
    }

}