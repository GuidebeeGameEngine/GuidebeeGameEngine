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

import com.guidebee.game.graphics.Batch;
import com.guidebee.game.graphics.Texture;
import com.guidebee.game.graphics.TextureRegion;
import com.guidebee.math.geometry.Rectangle;


//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * A Sprite is a basic visual element that can be rendered with one of several
 * frames stored in an Image; different frames can be shown to animate the
 * Sprite. Several transforms such as flipping and rotation can also be applied
 * to a Sprite to further vary its appearance. As with all Layer subclasses,
 * a Sprite's location can be changed and it can also be made visible
 * or invisible.
 * <img src="frames.gif" />
 */
public class Sprite extends Layer {
    // transform constants
    public static final int TRANS_NONE = 0;
    public static final int TRANS_ROT90 = 5;
    public static final int TRANS_ROT180 = 3;
    public static final int TRANS_ROT270 = 6;
    public static final int TRANS_MIRROR = 2;
    public static final int TRANS_MIRROR_ROT90 = 7;
    public static final int TRANS_MIRROR_ROT180 = 1;
    public static final int TRANS_MIRROR_ROT270 = 4;

    // current frame index (within the sequence, not the absolute index)
    private int frame;

    // the frame sequence
    // null if the default is used
    private int[] sequence;


    // number of cols and rows within the image
    private int cols;
    private int rows;

    // the transform aplied to this sprite
    private int transform;

    // the image containg the frames
    private TextureRegion img;

    // the collision rectangle
    private int collX;
    private int collY;
    private int collWidth;
    private int collHeight;

    public Sprite(Texture img) {
        this(new TextureRegion(img));
    }

    public Sprite(TextureRegion img) {
        this(img, img.getRegionWidth(), img.getRegionHeight());
    }

    public Sprite(Texture img, int frameWidth, int frameHeight) {
        this(new TextureRegion(img), frameWidth, frameHeight);
    }

    public Sprite(TextureRegion img, int frameWidth, int frameHeight) {
        // initial state is visible, positioned at 0, 0
        // with a bound rectangle the same as the frame
        super(0, 0, frameWidth, frameHeight, true);
        // implicit check for null img
        if (img.getRegionWidth() % frameWidth != 0
                || img.getRegionHeight() % frameHeight != 0)
            throw new IllegalArgumentException();


        this.img = img;
        cols = img.getRegionWidth() / frameWidth;
        rows = img.getRegionHeight() / frameHeight;
        collX = collY = 0;
        collWidth = frameWidth;
        collHeight = frameHeight;
        tilesTextRegions=img.split(frameWidth,frameHeight);
    }

    public Sprite(Sprite otherSprite) {
        // copy the otherSprite
        super(otherSprite.getX(), otherSprite.getY(), otherSprite.getWidth(),
                otherSprite.getHeight(), otherSprite.isVisible());
        this.frame = otherSprite.frame;
        this.sequence = otherSprite.sequence;
        this.cols = otherSprite.cols;
        this.rows = otherSprite.rows;
        this.transform = otherSprite.transform;
        this.img = otherSprite.img;

        this.collX = otherSprite.collX;
        this.collY = otherSprite.collY;
        this.collWidth = otherSprite.collWidth;
        this.collHeight = otherSprite.collHeight;
        tilesTextRegions=img.split((int)getWidth(),(int)getHeight());

    }

    public final boolean collidesWith(TextureRegion image, int iX, int iY) {
        if (image == null)
            throw new IllegalArgumentException();

        // only check collision if visible
        if (!this.isVisible())
            return false;

        return collidesWith(image, iX, iY);
    }

    public final boolean collidesWith(TiledLayer layer) {
        if (layer == null) {
            throw new NullPointerException();
        }

        // only check collision if visible
        if (!this.isVisible())
            return false;

        // only check collision if both are visible
        if (!layer.isVisible() || !this.isVisible())
            return false;

        return collidesWith(layer, 0, 0);
    }

    public final boolean collidesWith(Sprite otherSprite) {

        if (otherSprite == null) {
            throw new NullPointerException();
        }

        // only check collision if both are visible
        if (!otherSprite.isVisible() || !this.isVisible())
            return false;

        return collidesWith(otherSprite, 0, 0);
    }



    public void defineCollisionRectangle(int x, int y, int width, int height) {
        if (width < 0 || height < 0)
            throw new IllegalArgumentException();
        collX = x;
        collY = y;
        collWidth = width;
        collHeight = height;
    }

    public void setFrameSequence(int[] sequence) {
        if (sequence == null) {
            // return to default sequence
            this.sequence = null;
            return;
        }

        int max = (rows * cols) - 1;

        int l = sequence.length;

        if (l == 0)
            throw new IllegalArgumentException();

        for (int i = 0; i < l; i++) {
            int value = sequence[i];
            if (value > max || value < 0)
                throw new ArrayIndexOutOfBoundsException();
        }

        this.sequence = sequence;
        // the frame number has to be reseted
        this.frame = 0;
    }

    public final int getFrame() {
        return frame;
    }

    public int getFrameSequenceLength() {
        return (sequence == null) ? rows * cols : sequence.length;
    }

    public void setFrame(int frame) {
        int l = (sequence == null) ? rows * cols : sequence.length;
        if (frame < 0 || frame >= l) {
            throw new IndexOutOfBoundsException();
        }
        this.frame = frame;
    }

    public void nextFrame() {
        if (frame == ((sequence == null) ? rows * cols : sequence.length) - 1)
            frame = 0;
        else
            frame++;
    }

    public void prevFrame() {
        if (frame == 0)
            frame = ((sequence == null) ? rows * cols : sequence.length) - 1;
        else
            frame--;
    }


    Rectangle srcRect = new Rectangle(0, 0, 0, 0);
    Rectangle dstRect = new Rectangle(0, 0, 0, 0);

    public final void paint(Batch g) {
        if (!isVisible())
            return;

        int f = (sequence == null) ? frame : sequence[frame];
        int w = (int) getWidth();
        int h = (int) getHeight();
        int fx = w * (f % cols);
        int fy = h * (f / cols);


        srcRect.x = fx;
        srcRect.y = fy;
        srcRect.width = w;
        srcRect.height = h;
        dstRect.x = getX();
        dstRect.y = getY();
        dstRect.width = w;
        dstRect.height = h;

        float rotation = 0;
        boolean flipX = false;
        boolean flipY = false;
        float originX = 0;
        float originY = 0;

        switch (transform) {
            case TRANS_NONE:
                break;
            case TRANS_MIRROR_ROT180:
                flipY = true;
                break;
            case TRANS_MIRROR:
                flipX = true;
                break;
            case TRANS_ROT180:
                rotation = 180;
                originY = getOriginY();
                originX = getOriginX();
                break;
            case TRANS_MIRROR_ROT270:
                rotation = 90;
                originY = getOriginY();
                originX = getOriginX();
                flipX = true;
                break;
            case TRANS_ROT90:
                rotation = 270;
                originY = getOriginY();
                originX = getOriginX();
                break;
            case TRANS_ROT270:
                rotation = 90;
                originY = getOriginY();
                originX = getOriginX();
                break;
            case TRANS_MIRROR_ROT90:
                rotation = 270;
                originY = getOriginY();
                originX = getOriginX();
                flipX = true;
                break;
        }

        setRotation(rotation);


        int xIndex=f % cols;
        int yIndex=f / cols;
        TextureRegion newRegion=tilesTextRegions[yIndex][xIndex];
        g.draw(newRegion, dstRect.x, dstRect.y, originX, originY,
                dstRect.width, dstRect.height, 1, 1, rotation,
                flipX, flipY);


    }

    public int getRawFrameCount() {
        return cols * rows;
    }

    public void setTransform(int transform) {
        if (this.transform == transform)
            return;

        this.transform = transform;
    }

    /**
     * Helper methods that check for collisions They are at the end of the file
     * because of the length of the code
     * <p/>
     * For both methods, the second and third parameters are significant only if
     * o is an Bitmap, otherwise they are ignored
     */
    private synchronized boolean collidesWith(Object o, int oX, int oY) {

        int tX = 0, tY = 0, tW = 0, tH = 0;
        int oW = 0, oH = 0;

        Sprite t = this;
        boolean another = true;

        while (another) {
            int sX, sY, sW, sH;

            int cX = t.collX;
            int cY = t.collY;
            int cW = t.collWidth;
            int cH = t.collHeight;

            // if there is a zero in a dimension
            // then it cannot intersect with anything!
            if (cW == 0 || cH == 0) {
                return false;
            }

            switch (t.transform) {
                case TRANS_NONE:
                    sX = (int) (t.getX() + cX);
                    sY = (int) (t.getY() + cY);
                    sW = cW;
                    sH = cH;
                    break;
                case TRANS_MIRROR_ROT180:
                    sX = (int) (t.getX() + cX);
                    sY = (int) (t.getY() + (t.getHeight() - cY - 1) - cH);
                    sW = cW;
                    sH = cH;
                    break;
                case TRANS_MIRROR:
                    sX = (int) (t.getX() + (t.getWidth() - cX - 1) - cW);
                    sY = (int) (t.getY() + cY);
                    sW = cW;
                    sH = cH;
                    break;
                case TRANS_ROT180:
                    sX = (int) (t.getX() + (t.getWidth() - cX - 1) - cW);
                    sY = (int) (t.getY() + (t.getHeight() - cY - 1) - cH);
                    sW = cW;
                    sH = cH;
                    break;
                case TRANS_MIRROR_ROT270:
                    sX = (int) (t.getX() + cY);
                    sY = (int) (t.getY() + cX);
                    sW = cH;
                    sH = cW;
                    break;
                case TRANS_ROT90:
                    sX = (int) (t.getX() + (t.getHeight() - cY - 1) - cH);
                    sY = (int) (t.getY() + cX);
                    sW = cH;
                    sH = cW;
                    break;
                case TRANS_MIRROR_ROT90:
                    sX = (int) (t.getX() + (t.getHeight() - cY - 1) - cH);
                    sY = (int) (t.getY() + (t.getWidth() - cX - 1) - cW);
                    sW = cH;
                    sH = cW;
                    break;
                case TRANS_ROT270:
                    sX = (int) (t.getX() + cY);
                    sY = (int) (t.getY() + (t.getWidth() - cX - 1) - cW);
                    sW = cH;
                    sH = cW;
                    break;
                default: // cant really happen, but the return keeps the
                    // compiler happy (otherwise it'll report variable
                    // may not be initialized)
                    return false;
            }

            if (o != t) {
                tX = sX;
                tY = sY;
                tW = sW;
                tH = sH;
                if (o instanceof Sprite) {
                    // two sprites first round
                    // another = true;
                    t = (Sprite) o;
                } else if (o instanceof TiledLayer) {
                    another = false;
                    TiledLayer layer = (TiledLayer) o;
                    oX = (int) (layer.getX());
                    oY = (int) (layer.getY());
                    oW = (int) (layer.getWidth());
                    oH = (int) (layer.getHeight());
                } else { // o instanceof lcdui.Bitmap
                    another = false;
                    TextureRegion img = (TextureRegion) o;
                    oW = img.getRegionWidth();
                    oH = img.getRegionHeight();
                }
            } else {
                another = false;
                // two sprites
                // second round
                oX = sX;
                oY = sY;
                oW = sW;
                oH = sH;
            }
        }

        // if there is no intersection
        // we know there is no collision
        if (tX > oX && tX >= oX + oW)
            return false;
        else if (tX < oX && tX + tW <= oX)
            return false;
        else if (tY > oY && tY >= oY + oH)
            return false;
        else if (tY < oY && tY + tH <= oY)
            return false;

        if (o instanceof TiledLayer) {
            // if o is a tiledLayer then
            // it is possible to have not a
            // collision if every intersection tile
            // has a zero value
            TiledLayer layer = (TiledLayer) o;
            // this is the intersection of the two rectangles
            int rX, rY, rW, rH;

            if (oX > tX) {
                rX = oX;
                rW = ((oX + oW < tX + tW) ? oX + oW : tX + tW) - rX;
            } else {
                rX = tX;
                rW = ((tX + tW < oX + oW) ? tX + tW : oX + oW) - rX;
            }
            if (oY > tY) {
                rY = oY;
                rH = ((oY + oH < tY + tH) ? oY + oH : tY + tH) - rY;
            } else {
                rY = tY;
                rH = ((tY + tH < oY + oH) ? tY + tH : oY + oH) - rY;
            }


            int lW = layer.getCellWidth();
            int lH = layer.getCellHeight();

            int minC = (rX - oX) / lW;
            int minR = (rY - oY) / lH;
            int maxC = (rX - oX + rW - 1) / lW;
            int maxR = (rY - oY + rH - 1) / lH;

            // travel across all cells in the collision
            // rectangle
            for (int row = minR; row <= maxR; row++) {
                for (int col = minC; col <= maxC; col++) {
                    int cell = layer.getCell(col, row);
                    // if cell is animated get current
                    // associated static tile
                    if (cell < 0)
                        cell = layer.getAnimatedTile(cell);

                    if (cell != 0)
                        return true;
                }
            }

            // if no non zero cell was found
            // there is no collision
            return false;
        } else {
            // if the other object is an image or sprite
            // collision happened
            return true;
        }
    }

    private final TextureRegion[][] tilesTextRegions;

}
