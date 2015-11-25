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
package com.guidebee.game.graphics;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.graphics.NativePixmap;
import com.guidebee.game.files.FileHandle;
import com.guidebee.utils.Disposable;
import com.guidebee.utils.StreamUtils;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.InflaterInputStream;
//[------------------------------ MAIN CLASS ----------------------------------]

/**
 * <p>
 * A Pixmap represents an image in memory. It has a width and height expressed
 * in pixels as well as a {@link Format} specifying
 * the number and order of color components per pixel. Coordinates of pixels are
 * specified with respect to the top left corner of
 * the image, with the x-axis pointing to the right and the y-axis pointing downwards.
 * </p>
 * <p>
 * <p>
 * By default all methods use blending. You can disable blending with
 * {@link Pixmap#setBlending(Blending)}. The
 * {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}
 * method will scale and stretch the source image to a
 * target image. There either nearest neighbour or bilinear filtering can be used.
 * </p>
 * <p>
 * <p>
 * A Pixmap stores its data in native heap memory. It is mandatory to call
 * {@link Pixmap#dispose()} when the pixmap is no longer
 * needed, otherwise memory leaks will result
 * </p>
 *
 * @author badlogicgames@gmail.com
 */
public class Pixmap implements Disposable {
    /**
     * Different pixel formats.
     *
     * @author mzechner
     */
    public enum Format {
        Alpha, Intensity, LuminanceAlpha, RGB565, RGBA4444, RGB888, RGBA8888;

        public static int toNativePixmapFormat(Format format) {
            if (format == Alpha) return NativePixmap.FORMAT_ALPHA;
            if (format == Intensity) return NativePixmap.FORMAT_ALPHA;
            if (format == LuminanceAlpha) return NativePixmap.FORMAT_LUMINANCE_ALPHA;
            if (format == RGB565) return NativePixmap.FORMAT_RGB565;
            if (format == RGBA4444) return NativePixmap.FORMAT_RGBA4444;
            if (format == RGB888) return NativePixmap.FORMAT_RGB888;
            if (format == RGBA8888) return NativePixmap.FORMAT_RGBA8888;
            throw new GameEngineRuntimeException("Unknown Format: " + format);
        }

        public static Format fromNativePixmapFormat(int format) {
            if (format == NativePixmap.FORMAT_ALPHA) return Alpha;
            if (format == NativePixmap.FORMAT_LUMINANCE_ALPHA) return LuminanceAlpha;
            if (format == NativePixmap.FORMAT_RGB565) return RGB565;
            if (format == NativePixmap.FORMAT_RGBA4444) return RGBA4444;
            if (format == NativePixmap.FORMAT_RGB888) return RGB888;
            if (format == NativePixmap.FORMAT_RGBA8888) return RGBA8888;
            throw new GameEngineRuntimeException("Unknown NativePixmap Format: " + format);
        }
    }

    /**
     * Blending functions to be set with {@link Pixmap#setBlending}.
     *
     * @author mzechner
     */
    public enum Blending {
        None, SourceOver
    }

    /**
     * Filters to be used with
     * {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
     *
     * @author mzechner
     */
    public enum Filter {
        NearestNeighbour, BiLinear
    }

    /**
     * global blending state *
     */
    private static Blending blending = Blending.SourceOver;

    final NativePixmap pixmap;
    int color = 0;

    private boolean disposed;

    /**
     * Sets the type of {@link Blending} to be used for all
     * operations. Default is {@link Blending#SourceOver}.
     *
     * @param blending the blending type
     */
    public static void setBlending(Blending blending) {
        Pixmap.blending = blending;
        NativePixmap.setBlend(blending == Blending.None ? 0 : 1);
    }

    /**
     * Sets the type of interpolation {@link Filter} to be used in conjunction with
     * {@link Pixmap#drawPixmap(Pixmap, int, int, int, int, int, int, int, int)}.
     *
     * @param filter the filter.
     */
    public static void setFilter(Filter filter) {
        NativePixmap.setScale(filter == Filter.NearestNeighbour ?
                NativePixmap.SCALE_NEAREST : NativePixmap.SCALE_LINEAR);
    }

    /**
     * Creates a new Pixmap instance with the given width, height and format.
     *
     * @param width  the width in pixels
     * @param height the height in pixels
     * @param format the {@link Format}
     */
    public Pixmap(int width, int height, Format format) {
        pixmap = new NativePixmap(width, height, Format.toNativePixmapFormat(format));
        setColor(0, 0, 0, 0);
        fill();
    }

    /**
     * Creates a new Pixmap instance from the given encoded image data.
     * The image can be encoded as JPEG, PNG or BMP.
     *
     * @param encodedData the encoded image data
     * @param offset      the offset
     * @param len         the length
     */
    public Pixmap(byte[] encodedData, int offset, int len) {
        try {
            pixmap = new NativePixmap(encodedData, offset, len, 0);
        } catch (IOException e) {
            throw new GameEngineRuntimeException("Couldn't load pixmap from image data", e);
        }
    }

    /**
     * Creates a new Pixmap instance from the given file. The file must be a Png,
     * Jpeg or Bitmap. Paletted formats are not
     * supported.
     *
     * @param file the {@link FileHandle}
     */
    public Pixmap(FileHandle file) {
        try {
            byte[] bytes = file.readBytes();
            pixmap = new NativePixmap(bytes, 0, bytes.length, 0);
        } catch (Exception e) {
            throw new GameEngineRuntimeException("Couldn't load file: " + file, e);
        }
    }


    /**
     * Constructs a new Pixmap from a {@link NativePixmap}.
     *
     * @param pixmap
     */
    public Pixmap(NativePixmap pixmap) {
        this.pixmap = pixmap;
    }

    /**
     * Sets the color for the following drawing operations
     *
     * @param color the color, encoded as RGBA8888
     */
    public void setColor(int color) {
        this.color = color;
    }

    /**
     * Sets the color for the following drawing operations.
     *
     * @param r The red component.
     * @param g The green component.
     * @param b The blue component.
     * @param a The alpha component.
     */
    public void setColor(float r, float g, float b, float a) {
        color = Color.rgba8888(r, g, b, a);
    }

    /**
     * Sets the color for the following drawing operations.
     *
     * @param color The color.
     */
    public void setColor(Color color) {
        this.color = Color.rgba8888(color.r, color.g, color.b, color.a);
    }

    /**
     * Fills the complete bitmap with the currently set color.
     */
    public void fill() {
        pixmap.clear(color);
    }

// /**
// * Sets the width in pixels of strokes.
// *
// * @param width The stroke width in pixels.
// */
// public void setStrokeWidth (int width);

    /**
     * Draws a line between the given coordinates using the currently set color.
     *
     * @param x  The x-coodinate of the first point
     * @param y  The y-coordinate of the first point
     * @param x2 The x-coordinate of the first point
     * @param y2 The y-coordinate of the first point
     */
    public void drawLine(int x, int y, int x2, int y2) {
        pixmap.drawLine(x, y, x2, y2, color);
    }

    /**
     * Draws a rectangle outline starting at x, y extending by width to the
     * right and by height downwards (y-axis points downwards)
     * using the current color.
     *
     * @param x      The x coordinate
     * @param y      The y coordinate
     * @param width  The width in pixels
     * @param height The height in pixels
     */
    public void drawRectangle(int x, int y, int width, int height) {
        pixmap.drawRect(x, y, width, height, color);
    }

    /**
     * Draws an area form another Pixmap to this Pixmap.
     *
     * @param pixmap The other Pixmap
     * @param x      The target x-coordinate (top left corner)
     * @param y      The target y-coordinate (top left corner)
     */
    public void drawPixmap(Pixmap pixmap, int x, int y) {
        drawPixmap(pixmap, x, y, 0, 0, pixmap.getWidth(), pixmap.getHeight());
    }

    /**
     * Draws an area form another Pixmap to this Pixmap.
     *
     * @param pixmap    The other Pixmap
     * @param x         The target x-coordinate (top left corner)
     * @param y         The target y-coordinate (top left corner)
     * @param srcx      The source x-coordinate (top left corner)
     * @param srcy      The source y-coordinate (top left corner);
     * @param srcWidth  The width of the area form the other Pixmap in pixels
     * @param srcHeight The height of the area form the other Pixmap in pixles
     */
    public void drawPixmap(Pixmap pixmap, int x, int y, int srcx, int srcy,
                           int srcWidth, int srcHeight) {
        this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, x, y, srcWidth, srcHeight);
    }

    /**
     * Draws an area form another Pixmap to this Pixmap. This will automatically
     * scale and stretch the source image to the
     * specified target rectangle. Use {@link Pixmap#setFilter(Filter)} to
     * specify the type of filtering to be used (nearest
     * neighbour or bilinear).
     *
     * @param pixmap    The other Pixmap
     * @param srcx      The source x-coordinate (top left corner)
     * @param srcy      The source y-coordinate (top left corner);
     * @param srcWidth  The width of the area form the other Pixmap in pixels
     * @param srcHeight The height of the area form the other Pixmap in pixles
     * @param dstx      The target x-coordinate (top left corner)
     * @param dsty      The target y-coordinate (top left corner)
     * @param dstWidth  The target width
     * @param dstHeight the target height
     */
    public void drawPixmap(Pixmap pixmap, int srcx, int srcy, int srcWidth,
                           int srcHeight, int dstx, int dsty, int dstWidth,
                           int dstHeight) {
        this.pixmap.drawPixmap(pixmap.pixmap, srcx, srcy, srcWidth, srcHeight,
                dstx, dsty, dstWidth, dstHeight);
    }

    /**
     * Fills a rectangle starting at x, y extending by width to the right and by
     * height downwards (y-axis points downwards) using
     * the current color.
     *
     * @param x      The x coordinate
     * @param y      The y coordinate
     * @param width  The width in pixels
     * @param height The height in pixels
     */
    public void fillRectangle(int x, int y, int width, int height) {
        pixmap.fillRect(x, y, width, height, color);
    }

    /**
     * Draws a circle outline with the center at x,y and a radius using the
     * current color and stroke width.
     *
     * @param x      The x-coordinate of the center
     * @param y      The y-coordinate of the center
     * @param radius The radius in pixels
     */
    public void drawCircle(int x, int y, int radius) {
        pixmap.drawCircle(x, y, radius, color);
    }

    /**
     * Fills a circle with the center at x,y and a radius using the current color.
     *
     * @param x      The x-coordinate of the center
     * @param y      The y-coordinate of the center
     * @param radius The radius in pixels
     */
    public void fillCircle(int x, int y, int radius) {
        pixmap.fillCircle(x, y, radius, color);
    }

    /**
     * Fills a triangle with vertices at x1,y1 and x2,y2 and x3,y3
     * using the current color.
     *
     * @param x1 The x-coordinate of vertex 1
     * @param y1 The y-coordinate of vertex 1
     * @param x2 The x-coordinate of vertex 2
     * @param y2 The y-coordinate of vertex 2
     * @param x3 The x-coordinate of vertex 3
     * @param y3 The y-coordinate of vertex 3
     */
    public void fillTriangle(int x1, int y1, int x2, int y2, int x3, int y3) {
        pixmap.fillTriangle(x1, y1, x2, y2, x3, y3, color);
    }

    /**
     * Returns the 32-bit RGBA8888 value of the pixel at x, y. For Alpha formats
     * the RGB components will be one.
     *
     * @param x The x-coordinate
     * @param y The y-coordinate
     * @return The pixel color in RGBA8888 format.
     */
    public int getPixel(int x, int y) {
        return pixmap.getPixel(x, y);
    }

    /**
     * @return The width of the Pixmap in pixels.
     */
    public int getWidth() {
        return pixmap.getWidth();
    }

    /**
     * @return The height of the Pixmap in pixels.
     */
    public int getHeight() {
        return pixmap.getHeight();
    }


    public void drawRGB(int[] rgbData, int width, int height) {
        if (this.getFormat() != Format.RGBA8888 || width != getWidth() || height != getHeight()) {
            throw new IllegalArgumentException("Can only draw on same sized RGBA 8888 pixmap");
        }
        pixmap.drawRGB(rgbData);
    }


    public int[] getRGB() {
        int[] rgb = new int[pixmap.getWidth() * pixmap.getHeight()];
        int dest = 0;
        for (int y = 0; y < pixmap.getHeight(); y++) {
            for (int x = 0; x < pixmap.getWidth(); x++) {
                int pixel = pixmap.getPixel(x, y);
                int mask = pixel & 0xFFFFFFFF;
                int rr = mask >> 24 & 0xff;
                int gg = mask >> 16 & 0xff;
                int bb = mask >> 8 & 0xff;
                int aa = mask & 0xff;
                rgb[dest++] = ((aa & 0xFF) << 24) |
                        ((rr & 0xFF) << 16) |
                        ((gg & 0xFF) << 8) |
                        ((bb & 0xFF) << 0);
            }
        }

        return rgb;
    }

    /**
     * Releases all resources associated with this Pixmap.
     */
    public void dispose() {
        if (disposed)
            throw new GameEngineRuntimeException("Pixmap already disposed!");
        pixmap.dispose();
        disposed = true;
    }

    /**
     * Draws a pixel at the given location with the current color.
     *
     * @param x the x-coordinate
     * @param y the y-coordinate
     */
    public void drawPixel(int x, int y) {
        pixmap.setPixel(x, y, color);
    }

    /**
     * Draws a pixel at the given location with the given color.
     *
     * @param x     the x-coordinate
     * @param y     the y-coordinate
     * @param color the color in RGBA8888 format.
     */
    public void drawPixel(int x, int y, int color) {
        pixmap.setPixel(x, y, color);
    }

    /**
     * Returns the OpenGL ES format of this Pixmap. Used as the seventh parameter to
     * {@link com.guidebee.game.engine.graphics.opengles.IGL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     *
     * @return one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA.
     */
    public int getGLFormat() {
        return pixmap.getGLFormat();
    }

    /**
     * Returns the OpenGL ES format of this Pixmap. Used as the third parameter to
     * {@link com.guidebee.game.engine.graphics.opengles.IGL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     *
     * @return one of GL_ALPHA, GL_RGB, GL_RGBA, GL_LUMINANCE, or GL_LUMINANCE_ALPHA.
     */
    public int getGLInternalFormat() {
        return pixmap.getGLInternalFormat();
    }

    /**
     * Returns the OpenGL ES type of this Pixmap. Used as the eighth parameter to
     * {@link com.guidebee.game.engine.graphics.opengles.IGL20#glTexImage2D(int, int, int, int, int, int, int, int, java.nio.Buffer)}.
     *
     * @return one of GL_UNSIGNED_BYTE, GL_UNSIGNED_SHORT_5_6_5, GL_UNSIGNED_SHORT_4_4_4_4
     */
    public int getGLType() {
        return pixmap.getGLType();
    }

    /**
     * Returns the direct ByteBuffer holding the pixel data. For the format Alpha
     * each value is encoded as a byte. For the format
     * LuminanceAlpha the luminance is the first byte and the alpha is the second
     * byte of the pixel. For the formats RGB888 and
     * RGBA8888 the color components are stored in a single byte each in the order
     * red, green, blue (alpha). For the formats RGB565
     * and RGBA4444 the pixel colors are stored in shorts in machine dependent order.
     *
     * @return the direct {@link ByteBuffer} holding the pixel data.
     */
    public ByteBuffer getPixels() {
        if (disposed) throw new GameEngineRuntimeException("Pixmap already disposed");
        return pixmap.getPixels();
    }

    /**
     * @return the {@link Format} of this Pixmap.
     */
    public Format getFormat() {
        return Format.fromNativePixmapFormat(pixmap.getFormat());
    }

    /**
     * @return the currently set {@link Blending}
     */
    public static Blending getBlending() {
        return blending;
    }


    /**
     * Writes the {@link Pixmap} to the given file using a custom compression scheme.
     * First three integers define the width, height
     * and format, remaining bytes are zlib compressed pixels. To be able to load the
     * Pixmap to a Texture, use ".cim" as the file
     * suffix! Throws a GameEngineRuntimeException in case the Pixmap couldn't be written
     * to the file.
     *
     * @param file the file to write the Pixmap to
     */
    static public void writeCIM(FileHandle file, Pixmap pixmap) {
        CIM.write(file, pixmap);
    }

    /**
     * Reads the {@link Pixmap} from the given file, assuming the Pixmap was
     * written with the
     * {@link Pixmap#writeCIM(FileHandle, Pixmap)} method. Throws a
     * GameEngineRuntimeException in case the file couldn't be read.
     *
     * @param file the file to read the Pixmap from
     */
    static public Pixmap readCIM(FileHandle file) {
        return CIM.read(file);
    }

    /**
     * Writes the pixmap as a PNG. Note this method uses quite a bit of working memory.
     * {@link #writeCIM(FileHandle, Pixmap)} is
     * faster if the file does not need to be read outside of libgameengine.
     */
    static public void writePNG(FileHandle file, Pixmap pixmap) {
        try {
            file.writeBytes(PNG.write(pixmap), false);
        } catch (IOException ex) {
            throw new GameEngineRuntimeException("Error writing PNG: " + file, ex);
        }
    }

    /**
     * @author mzechner
     */
    static private class CIM {
        static private final int BUFFER_SIZE = 32000;
        static private final byte[] writeBuffer = new byte[BUFFER_SIZE];
        static private final byte[] readBuffer = new byte[BUFFER_SIZE];

        static public void write(FileHandle file, Pixmap pixmap) {
            DataOutputStream out = null;

            try {
                // long start = System.nanoTime();
                DeflaterOutputStream deflaterOutputStream
                        = new DeflaterOutputStream(file.write(false));
                out = new DataOutputStream(deflaterOutputStream);
                out.writeInt(pixmap.getWidth());
                out.writeInt(pixmap.getHeight());
                out.writeInt(Pixmap.Format.toNativePixmapFormat(pixmap.getFormat()));

                ByteBuffer pixelBuf = pixmap.getPixels();
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());

                int remainingBytes = pixelBuf.capacity() % BUFFER_SIZE;
                int iterations = pixelBuf.capacity() / BUFFER_SIZE;

                synchronized (writeBuffer) {
                    for (int i = 0; i < iterations; i++) {
                        pixelBuf.get(writeBuffer);
                        out.write(writeBuffer);
                    }

                    pixelBuf.get(writeBuffer, 0, remainingBytes);
                    out.write(writeBuffer, 0, remainingBytes);
                }

                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
                // GameEngine.app.log("Pixmap", "write (" + file.name() + "):"
                // + (System.nanoTime() - start) / 1000000000.0f + ", " +
                // Thread.currentThread().getName());
            } catch (Exception e) {
                throw new GameEngineRuntimeException("Couldn't write Pixmap to file '"
                        + file + "'", e);
            } finally {
                StreamUtils.closeQuietly(out);
            }
        }

        static public Pixmap read(FileHandle file) {
            DataInputStream in = null;

            try {
                // long start = System.nanoTime();
                in = new DataInputStream(new
                        InflaterInputStream(new BufferedInputStream(file.read())));
                int width = in.readInt();
                int height = in.readInt();
                Pixmap.Format format = Pixmap.Format.fromNativePixmapFormat(in.readInt());
                Pixmap pixmap = new Pixmap(width, height, format);

                ByteBuffer pixelBuf = pixmap.getPixels();
                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());

                synchronized (readBuffer) {
                    int readBytes = 0;
                    while ((readBytes = in.read(readBuffer)) > 0) {
                        pixelBuf.put(readBuffer, 0, readBytes);
                    }
                }

                pixelBuf.position(0);
                pixelBuf.limit(pixelBuf.capacity());
                // GameEngine.app.log("Pixmap", "read:" + (System.nanoTime() - start)
                // / 1000000000.0f);
                return pixmap;
            } catch (Exception e) {
                throw new GameEngineRuntimeException("Couldn't read Pixmap from file '"
                        + file + "'", e);
            } finally {
                StreamUtils.closeQuietly(in);
            }
        }
    }

    /**
     * Minimal PNG encoder to create PNG streams (and MIDP images) from RGBA arrays.<br>
     * Copyright 2006-2009 Christian Fröschlin www.chrfr.de<br>
     * Terms of Use: You may use the PNG encoder free of charge for any purpose you desire,
     * as long as you do not claim credit for
     * the original sources and agree not to hold me responsible for any damage arising
     * out of its use.<br>
     * If you have a suitable location in GUI or documentation for giving credit,
     * I'd appreciate a non-mandatory mention of:<br>
     * PNG encoder (C) 2006-2009 by Christian Fröschlin, www.chrfr.de
     */
    static private class PNG {
        static int[] crcTable;
        static final int ZLIB_BLOCK_SIZE = 32000;

        static byte[] write(Pixmap pixmap) throws IOException {
            byte[] signature = new byte[]{(byte) 137, (byte) 80, (byte) 78, (byte) 71,
                    (byte) 13, (byte) 10, (byte) 26, (byte) 10};
            byte[] header = PNG.createHeaderChunk(pixmap.getWidth(), pixmap.getHeight());
            byte[] data = PNG.createDataChunk(pixmap);
            byte[] trailer = PNG.createTrailerChunk();

            ByteArrayOutputStream png = new ByteArrayOutputStream(signature.length
                    + header.length + data.length + trailer.length);
            png.write(signature);
            png.write(header);
            png.write(data);
            png.write(trailer);
            return png.toByteArray();
        }

        static private byte[] createHeaderChunk(int width, int height) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(13);
            DataOutputStream chunk = new DataOutputStream(baos);
            chunk.writeInt(width);
            chunk.writeInt(height);
            chunk.writeByte(8); // Bitdepth
            chunk.writeByte(6); // Colortype ARGB
            chunk.writeByte(0); // Compression
            chunk.writeByte(0); // Filter
            chunk.writeByte(0); // Interlace
            return toChunk("IHDR", baos.toByteArray());
        }

        static private byte[] createDataChunk(Pixmap pixmap) throws IOException {
            int width = pixmap.getWidth();
            int height = pixmap.getHeight();
            int dest = 0;
            byte[] raw = new byte[4 * width * height + height];
            for (int y = 0; y < height; y++) {
                raw[dest++] = 0; // No filter
                for (int x = 0; x < width; x++) {
                    // 32-bit RGBA8888
                    int pixel = pixmap.getPixel(x, y);

                    int mask = pixel & 0xFFFFFFFF;
                    int rr = mask >> 24 & 0xff;
                    int gg = mask >> 16 & 0xff;
                    int bb = mask >> 8 & 0xff;
                    int aa = mask & 0xff;

                    raw[dest++] = (byte) rr;
                    raw[dest++] = (byte) gg;
                    raw[dest++] = (byte) bb;
                    raw[dest++] = (byte) aa;
                }
            }
            return toChunk("IDAT", toZLIB(raw));
        }

        static private byte[] createTrailerChunk() throws IOException {
            return toChunk("IEND", new byte[]{});
        }

        static private byte[] toChunk(String id, byte[] raw) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length + 12);
            DataOutputStream chunk = new DataOutputStream(baos);

            chunk.writeInt(raw.length);

            byte[] bid = new byte[4];
            for (int i = 0; i < 4; i++) {
                bid[i] = (byte) id.charAt(i);
            }

            chunk.write(bid);

            chunk.write(raw);

            int crc = 0xFFFFFFFF;
            crc = updateCRC(crc, bid);
            crc = updateCRC(crc, raw);
            chunk.writeInt(~crc);

            return baos.toByteArray();
        }

        static private void createCRCTable() {
            crcTable = new int[256];
            for (int i = 0; i < 256; i++) {
                int c = i;
                for (int k = 0; k < 8; k++)
                    c = (c & 1) > 0 ? 0xedb88320 ^ c >>> 1 : c >>> 1;
                crcTable[i] = c;
            }
        }

        static private int updateCRC(int crc, byte[] raw) {
            if (crcTable == null) createCRCTable();
            for (byte element : raw)
                crc = crcTable[(crc ^ element) & 0xFF] ^ crc >>> 8;
            return crc;
        }

        /*
         * This method is called to encode the image data as a zlib block as required
         * by the PNG specification. This file comes with
         * a minimal ZLIB encoder which uses uncompressed deflate blocks (fast, short,
         * easy, but no compression). If you want
         * compression, call another encoder (such as JZLib?) here.
         */
        static private byte[] toZLIB(byte[] raw) throws IOException {
            ByteArrayOutputStream baos = new ByteArrayOutputStream(raw.length + 6
                    + raw.length / ZLIB_BLOCK_SIZE * 5);
            DataOutputStream zlib = new DataOutputStream(baos);

            byte tmp = (byte) 8;
            zlib.writeByte(tmp); // CM = 8, CMINFO = 0
            zlib.writeByte((31 - (tmp << 8) % 31) % 31); // FCHECK
            // (FDICT/FLEVEL=0)

            int pos = 0;
            while (raw.length - pos > ZLIB_BLOCK_SIZE) {
                writeUncompressedDeflateBlock(zlib, false, raw, pos, (char) ZLIB_BLOCK_SIZE);
                pos += ZLIB_BLOCK_SIZE;
            }

            writeUncompressedDeflateBlock(zlib, true, raw, pos, (char) (raw.length - pos));

            // zlib check sum of uncompressed data
            zlib.writeInt(calcADLER32(raw));

            return baos.toByteArray();
        }

        static private void writeUncompressedDeflateBlock(DataOutputStream zlib,
                                                          boolean last, byte[] raw, int off, char len)
                throws IOException {
            zlib.writeByte((byte) (last ? 1 : 0)); // Final flag, Compression type 0
            zlib.writeByte((byte) (len & 0xFF)); // Length LSB
            zlib.writeByte((byte) ((len & 0xFF00) >> 8)); // Length MSB
            zlib.writeByte((byte) (~len & 0xFF)); // Length 1st complement LSB
            zlib.writeByte((byte) ((~len & 0xFF00) >> 8)); // Length 1st complement
            // MSB
            zlib.write(raw, off, len); // Data
        }

        private static int calcADLER32(final byte[] raw) {
            int s1 = 1;
            int s2 = 0;
            for (int i = 0; i < raw.length; i++) {
                final int abs = raw[i] >= 0 ? raw[i] : (raw[i] + 256);
                s1 = (s1 + abs) % 65521;
                s2 = (s2 + s1) % 65521;
            }
            return (s2 << 16) + s1;
        }
    }
}
