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
package com.guidebee.game.engine.graphics.opengles;

//--------------------------------- IMPORTS ------------------------------------

import com.guidebee.game.GameEngine;
import com.guidebee.game.GameEngineRuntimeException;
import com.guidebee.game.engine.graphics.VertexAttribute;
import com.guidebee.game.engine.graphics.VertexAttributes;
import com.guidebee.utils.collections.BufferUtils;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

//[------------------------------ MAIN CLASS ----------------------------------]
/**
 * <p>
 * A {@link VertexData} implementation based on OpenGL vertex buffer objects.
 * </p>
 * <p>
 * <p>
 * If the OpenGL ES context was lost you can call {@link #invalidate()} to
 * recreate a new OpenGL vertex buffer object. This class
 * can be used seamlessly with OpenGL ES 1.x and 2.0.
 * </p>
 * <p>
 * <p>
 * In case OpenGL ES 2.0 is used in the application the data is bound via
 * glVertexAttribPointer() according to the attribute
 * aliases specified via {@link VertexAttributes} in the constructor.
 * </p>
 * <p>
 * <p>
 * Uses indirect Buffers on Android 1.5/1.6 to fix GC invocation due to
 * leaking PlatformAddress instances.
 * </p>
 * <p>
 * <p>
 * VertexBufferObjects must be disposed via the {@link #dispose()}
 * method when no longer needed
 * </p>
 *
 * @author mzechner
 */
public class VertexBufferObjectSubData implements VertexData {
    final static IntBuffer tmpHandle = BufferUtils.newIntBuffer(1);

    final VertexAttributes attributes;
    final FloatBuffer buffer;
    final ByteBuffer byteBuffer;
    int bufferHandle;
    final boolean isDirect;
    final boolean isStatic;
    final int usage;
    boolean isDirty = false;
    boolean isBound = false;

    /**
     * Constructs a new interleaved VertexBufferObject.
     *
     * @param isStatic    whether the vertex data is static.
     * @param numVertices the maximum number of vertices
     * @param attributes  the {@link com.guidebee.game.engine.graphics.VertexAttribute}s.
     */
    public VertexBufferObjectSubData(boolean isStatic, int numVertices,
                                     VertexAttribute... attributes) {
        this.isStatic = isStatic;
        this.attributes = new VertexAttributes(attributes);
        byteBuffer = BufferUtils.newByteBuffer(this.attributes.vertexSize
                * numVertices);
        isDirect = true;

        usage = isStatic ? IGL20.GL_STATIC_DRAW : IGL20.GL_DYNAMIC_DRAW;
        buffer = byteBuffer.asFloatBuffer();
        bufferHandle = createBufferObject();
        buffer.flip();
        byteBuffer.flip();
    }

    private int createBufferObject() {
        GameEngine.gl20.glGenBuffers(1, tmpHandle);
        GameEngine.gl20.glBindBuffer(IGL20.GL_ARRAY_BUFFER,
                tmpHandle.get(0));
        GameEngine.gl20.glBufferData(IGL20.GL_ARRAY_BUFFER,
                byteBuffer.capacity(), null, usage);
        GameEngine.gl20.glBindBuffer(IGL20.GL_ARRAY_BUFFER, 0);
        return tmpHandle.get(0);
    }

    @Override
    public VertexAttributes getAttributes() {
        return attributes;
    }

    @Override
    public int getNumVertices() {
        return buffer.limit() * 4 / attributes.vertexSize;
    }

    @Override
    public int getNumMaxVertices() {
        return byteBuffer.capacity() / attributes.vertexSize;
    }

    @Override
    public FloatBuffer getBuffer() {
        isDirty = true;
        return buffer;
    }

    private void bufferChanged() {
        if (isBound) {
            GameEngine.gl20.glBufferSubData(IGL20.GL_ARRAY_BUFFER, 0,
                    byteBuffer.limit(), byteBuffer);
            isDirty = false;
        }
    }

    @Override
    public void setVertices(float[] vertices, int offset, int count) {
        isDirty = true;
        if (isDirect) {
            BufferUtils.copy(vertices, byteBuffer, count, offset);
            buffer.position(0);
            buffer.limit(count);
        } else {
            buffer.clear();
            buffer.put(vertices, offset, count);
            buffer.flip();
            byteBuffer.position(0);
            byteBuffer.limit(buffer.limit() << 2);
        }

        bufferChanged();
    }

    @Override
    public void updateVertices(int targetOffset, float[] vertices,
                               int sourceOffset, int count) {
        isDirty = true;
        if (isDirect) {
            final int pos = byteBuffer.position();
            byteBuffer.position(targetOffset * 4);
            BufferUtils.copy(vertices, sourceOffset,
                    count, byteBuffer);
            byteBuffer.position(pos);
        } else
            throw new GameEngineRuntimeException("Buffer must be allocated direct.");
            // Should never happen

        bufferChanged();
    }

    /**
     * Binds this VertexBufferObject for rendering via glDrawArrays or glDrawElements
     *
     * @param shader the shader
     */
    @Override
    public void bind(final ShaderProgram shader) {
        bind(shader, null);
    }

    @Override
    public void bind(final ShaderProgram shader, final int[] locations) {
        final IGL20 gl = GameEngine.gl20;

        gl.glBindBuffer(IGL20.GL_ARRAY_BUFFER, bufferHandle);
        if (isDirty) {
            byteBuffer.limit(buffer.limit() * 4);
            gl.glBufferData(IGL20.GL_ARRAY_BUFFER, byteBuffer.limit(),
                    byteBuffer, usage);
            isDirty = false;
        }

        final int numAttributes = attributes.size();
        if (locations == null) {
            for (int i = 0; i < numAttributes; i++) {
                final VertexAttribute attribute = attributes.get(i);
                final int location = shader.getAttributeLocation(attribute.alias);
                if (location < 0) continue;
                shader.enableVertexAttribute(location);

                shader.setVertexAttribute(location, attribute.numComponents,
                        attribute.type, attribute.normalized, attributes.vertexSize,
                        attribute.offset);
            }
        } else {
            for (int i = 0; i < numAttributes; i++) {
                final VertexAttribute attribute = attributes.get(i);
                final int location = locations[i];
                if (location < 0) continue;
                shader.enableVertexAttribute(location);

                shader.setVertexAttribute(location, attribute.numComponents,
                        attribute.type, attribute.normalized, attributes.vertexSize,
                        attribute.offset);
            }
        }
        isBound = true;
    }

    /**
     * Unbinds this VertexBufferObject.
     *
     * @param shader the shader
     */
    @Override
    public void unbind(final ShaderProgram shader) {
        unbind(shader, null);
    }

    @Override
    public void unbind(final ShaderProgram shader, final int[] locations) {
        final IGL20 gl = GameEngine.gl20;
        final int numAttributes = attributes.size();
        if (locations == null) {
            for (int i = 0; i < numAttributes; i++) {
                shader.disableVertexAttribute(attributes.get(i).alias);
            }
        } else {
            for (int i = 0; i < numAttributes; i++) {
                final int location = locations[i];
                if (location >= 0) shader.disableVertexAttribute(location);
            }
        }
        gl.glBindBuffer(IGL20.GL_ARRAY_BUFFER, 0);
        isBound = false;
    }

    /**
     * Invalidates the VertexBufferObject so a new OpenGL buffer handle is
     * created. Use this in case of a context loss.
     */
    public void invalidate() {
        bufferHandle = createBufferObject();
        isDirty = true;
    }

    /**
     * Disposes of all resources this VertexBufferObject uses.
     */
    @Override
    public void dispose() {
        tmpHandle.clear();
        tmpHandle.put(bufferHandle);
        tmpHandle.flip();
        IGL20 gl = GameEngine.gl20;
        gl.glBindBuffer(IGL20.GL_ARRAY_BUFFER, 0);
        gl.glDeleteBuffers(1, tmpHandle);
        bufferHandle = 0;
    }

    /**
     * Returns the VBO handle
     *
     * @return the VBO handle
     */
    public int getBufferHandle() {
        return bufferHandle;
    }
}
