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
package com.guidebee.game.engine.graphics;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.graphics.opengles.IGL20;
import com.guidebee.utils.Disposable;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * @author mzechner
 */
public class NativePixmap implements Disposable {
    public static final int FORMAT_ALPHA = 1;
    public static final int FORMAT_LUMINANCE_ALPHA = 2;
    public static final int FORMAT_RGB888 = 3;
    public static final int FORMAT_RGBA8888 = 4;
    public static final int FORMAT_RGB565 = 5;
    public static final int FORMAT_RGBA4444 = 6;

    public static final int SCALE_NEAREST = 0;
    public static final int SCALE_LINEAR = 1;

    public static final int BLEND_NONE = 0;
    public static final int BLEND_SRC_OVER = 1;

    long basePtr;
    int width;
    int height;
    int format;
    ByteBuffer pixelPtr;
    long[] nativeData = new long[4];

    static {
        setBlend(BLEND_SRC_OVER);
        setScale(SCALE_LINEAR);
    }

    public NativePixmap(byte[] encodedData, int offset, int len, int requestedFormat)
            throws IOException {
        pixelPtr = load(nativeData, encodedData, offset, len);
        if (pixelPtr == null) throw new IOException("couldn't load pixmap "
                + getFailureReason());

        basePtr = nativeData[0];
        width = (int) nativeData[1];
        height = (int) nativeData[2];
        format = (int) nativeData[3];

        if (requestedFormat != 0 && requestedFormat != format) {
            convert(requestedFormat);
        }
    }

    public NativePixmap(InputStream in, int requestedFormat) throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream(1024);
        byte[] buffer = new byte[1024];
        int readBytes = 0;

        while ((readBytes = in.read(buffer)) != -1) {
            bytes.write(buffer, 0, readBytes);
        }

        buffer = bytes.toByteArray();
        pixelPtr = load(nativeData, buffer, 0, buffer.length);
        if (pixelPtr == null) throw new IOException("couldn't load pixmap "
                + getFailureReason());

        basePtr = nativeData[0];
        width = (int) nativeData[1];
        height = (int) nativeData[2];
        format = (int) nativeData[3];

        if (requestedFormat != 0 && requestedFormat != format) {
            convert(requestedFormat);
        }
    }

    /**
     * @throws com.guidebee.game.GameEngineRuntimeException
     * if allocation failed.
     */
    public NativePixmap(int width, int height, int format)
            throws GameEngineRuntimeException {
        pixelPtr = newPixmap(nativeData, width, height, format);
        if (pixelPtr == null)
            throw new GameEngineRuntimeException("couldn't load pixmap");

        this.basePtr = nativeData[0];
        this.width = (int) nativeData[1];
        this.height = (int) nativeData[2];
        this.format = (int) nativeData[3];
    }

    public NativePixmap(ByteBuffer pixelPtr, long[] nativeData) {
        this.pixelPtr = pixelPtr;
        this.basePtr = nativeData[0];
        this.width = (int) nativeData[1];
        this.height = (int) nativeData[2];
        this.format = (int) nativeData[3];
    }

    private void convert(int requestedFormat) {
        NativePixmap pixmap = new NativePixmap(width, height, requestedFormat);
        pixmap.drawPixmap(this, 0, 0, 0, 0, width, height);
        dispose();
        this.basePtr = pixmap.basePtr;
        this.format = pixmap.format;
        this.height = pixmap.height;
        this.nativeData = pixmap.nativeData;
        this.pixelPtr = pixmap.pixelPtr;
        this.width = pixmap.width;
    }

    public void dispose() {
        free(basePtr);
    }

    public void clear(int color) {
        clear(basePtr, color);
    }

    public void setPixel(int x, int y, int color) {
        setPixel(basePtr, x, y, color);
    }

    public int getPixel(int x, int y) {
        return getPixel(basePtr, x, y);
    }

    public void drawLine(int x, int y, int x2, int y2, int color) {
        drawLine(basePtr, x, y, x2, y2, color);
    }

    public void drawRect(int x, int y, int width, int height, int color) {
        drawRect(basePtr, x, y, width, height, color);
    }

    public void drawCircle(int x, int y, int radius, int color) {
        drawCircle(basePtr, x, y, radius, color);
    }

    public void fillRect(int x, int y, int width, int height, int color) {
        fillRect(basePtr, x, y, width, height, color);
    }

    public void fillCircle(int x, int y, int radius, int color) {
        fillCircle(basePtr, x, y, radius, color);
    }


    public void drawRGB(int [] rgbData){
        drawRGB(basePtr,rgbData);
    }

    public void fillTriangle(int x1, int y1, int x2, int y2, int x3,
                             int y3, int color) {
        fillTriangle(basePtr, x1, y1, x2, y2, x3, y3, color);
    }

    public void drawPixmap(NativePixmap src, int srcX, int srcY, int dstX,
                           int dstY, int width, int height) {
        drawPixmap(src.basePtr, basePtr, srcX, srcY, width, height, dstX,
                dstY, width, height);
    }

    public void drawPixmap(NativePixmap src, int srcX, int srcY, int srcWidth,
                           int srcHeight, int dstX, int dstY, int dstWidth,
                           int dstHeight) {
        drawPixmap(src.basePtr, basePtr, srcX, srcY, srcWidth, srcHeight,
                dstX, dstY, dstWidth, dstHeight);
    }

    public static NativePixmap newPixmap(InputStream in, int requestedFormat) {
        try {
            return new NativePixmap(in, requestedFormat);
        } catch (IOException e) {
            return null;
        }
    }

    public static NativePixmap newPixmap(int width, int height, int format) {
        try {
            return new NativePixmap(width, height, format);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public ByteBuffer getPixels() {
        return pixelPtr;
    }

    public int getHeight() {
        return height;
    }

    public int getWidth() {
        return width;
    }

    public int getFormat() {
        return format;
    }

    public int getGLInternalFormat() {
        switch (format) {
            case FORMAT_ALPHA:
                return IGL20.GL_ALPHA;
            case FORMAT_LUMINANCE_ALPHA:
                return IGL20.GL_LUMINANCE_ALPHA;
            case FORMAT_RGB888:
            case FORMAT_RGB565:
                return IGL20.GL_RGB;
            case FORMAT_RGBA8888:
            case FORMAT_RGBA4444:
                return IGL20.GL_RGBA;
            default:
                throw new GameEngineRuntimeException("unknown format: " + format);
        }
    }

    public int getGLFormat() {
        return getGLInternalFormat();
    }

    public int getGLType() {
        switch (format) {
            case FORMAT_ALPHA:
            case FORMAT_LUMINANCE_ALPHA:
            case FORMAT_RGB888:
            case FORMAT_RGBA8888:
                return IGL20.GL_UNSIGNED_BYTE;
            case FORMAT_RGB565:
                return IGL20.GL_UNSIGNED_SHORT_5_6_5;
            case FORMAT_RGBA4444:
                return IGL20.GL_UNSIGNED_SHORT_4_4_4_4;
            default:
                throw new GameEngineRuntimeException("unknown format: " + format);
        }
    }

    public String getFormatString() {
        switch (format) {
            case FORMAT_ALPHA:
                return "alpha";
            case FORMAT_LUMINANCE_ALPHA:
                return "luminance alpha";
            case FORMAT_RGB888:
                return "rgb888";
            case FORMAT_RGBA8888:
                return "rgba8888";
            case FORMAT_RGB565:
                return "rgb565";
            case FORMAT_RGBA4444:
                return "rgba4444";
            default:
                return "unknown";
        }
    }

    // @off
    /*JNI
	#include <native/native.h>
	#include <stdlib.h>
	 */

    private static native ByteBuffer load(long[] nativeData, byte[] buffer,
                                          int offset, int len); /*MANUAL
		const unsigned char* p_buffer = (const unsigned char*)env->GetPrimitiveArrayCritical(buffer, 0);
		native_pixmap* pixmap = native_load(p_buffer + offset, len);
		env->ReleasePrimitiveArrayCritical(buffer, (char*)p_buffer, 0);
	
		if(pixmap==0)
			return 0;
	
		jobject pixel_buffer = env->NewDirectByteBuffer((void*)pixmap->pixels, pixmap->width * pixmap->height * native_bytes_per_pixel(pixmap->format));
		jlong* p_native_data = (jlong*)env->GetPrimitiveArrayCritical(nativeData, 0);
		p_native_data[0] = (jlong)pixmap;
		p_native_data[1] = pixmap->width;
		p_native_data[2] = pixmap->height;
		p_native_data[3] = pixmap->format;
		env->ReleasePrimitiveArrayCritical(nativeData, p_native_data, 0);
	
		return pixel_buffer;
	*/

    private static native ByteBuffer newPixmap(long[] nativeData, int width,
                                               int height, int format); /*MANUAL
		native_pixmap* pixmap = native_new(width, height, format);
		if(pixmap==0)
			return 0;
	
		jobject pixel_buffer = env->NewDirectByteBuffer((void*)pixmap->pixels,
		pixmap->width * pixmap->height * native_bytes_per_pixel(pixmap->format));
		jlong* p_native_data = (jlong*)env->GetPrimitiveArrayCritical(nativeData, 0);
		p_native_data[0] = (jlong)pixmap;
		p_native_data[1] = pixmap->width;
		p_native_data[2] = pixmap->height;
		p_native_data[3] = pixmap->format;
		env->ReleasePrimitiveArrayCritical(nativeData, p_native_data, 0);
	
		return pixel_buffer;
	*/

    private static native void free(long pixmap); /*
		native_free((native_pixmap*)pixmap);
	*/

    private static native void clear(long pixmap, int color); /*
		native_clear((native_pixmap*)pixmap, color);
	*/

    private static native void setPixel(long pixmap, int x, int y, int color); /*
		native_set_pixel((native_pixmap*)pixmap, x, y, color);
	*/

    private static native int getPixel(long pixmap, int x, int y); /*
		return native_get_pixel((native_pixmap*)pixmap, x, y);
	*/

    private static native void drawLine(long pixmap, int x, int y, int x2,
                                        int y2, int color); /*
		native_draw_line((native_pixmap*)pixmap, x, y, x2, y2, color);
	*/

    private static native void drawRect(long pixmap, int x, int y, int width,
                                        int height, int color); /*
		native_draw_rect((native_pixmap*)pixmap, x, y, width, height, color);
	*/

    private static native void drawCircle(long pixmap, int x, int y,
                                          int radius, int color); /*
		native_draw_circle((native_pixmap*)pixmap, x, y, radius, color);	
	*/

    private static native void fillRect(long pixmap, int x, int y, int width,
                                        int height, int color); /*
		native_fill_rect((native_pixmap*)pixmap, x, y, width, height, color);
	*/

    private static native void fillCircle(long pixmap, int x, int y,
                                          int radius, int color); /*
		native_fill_circle((native_pixmap*)pixmap, x, y, radius, color);
	*/

    private static native void fillTriangle(long pixmap, int x1, int y1, int x2,
                                            int y2, int x3, int y3, int color); /*
		native_fill_triangle((native_pixmap*)pixmap, x1, y1, x2, y2, x3, y3, color);
	*/

    private static native void drawPixmap(long src, long dst, int srcX, int srcY,
                                          int srcWidth, int srcHeight, int dstX,
                                          int dstY, int dstWidth, int dstHeight); /*
		native_draw_pixmap((native_pixmap*)src, (native_pixmap*)dst, srcX, srcY,
		 srcWidth, srcHeight, dstX, dstY, dstWidth, dstHeight);
	*/

    public static native void setBlend(int blend); /*
		native_set_blend(blend);
	*/

    public static native void setScale(int scale); /*
		native_set_scale(scale);
	*/

    public static native String getFailureReason(); /*
     return env->NewStringUTF(native_get_failure_reason());
   */


    public static native void drawRGB(long pixmap,int [] rgbData);
}
