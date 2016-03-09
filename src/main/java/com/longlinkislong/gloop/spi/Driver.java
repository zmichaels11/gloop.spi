/* 
 * Copyright (c) 2016, longlinkislong.com
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * * Redistributions of source code must retain the above copyright notice, this
 *   list of conditions and the following disclaimer.
 * * Redistributions in binary form must reproduce the above copyright notice,
 *   this list of conditions and the following disclaimer in the documentation
 *   and/or other materials provided with the distribution.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 */
package com.longlinkislong.gloop.spi;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import static org.lwjgl.opengl.EXTTextureCompressionS3TC.*;
import org.lwjgl.opengl.GL11;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;
import static org.lwjgl.opengl.GL14.*;
import static org.lwjgl.opengl.GL21.*;
import org.lwjgl.opengl.GL30;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL31.*;
import static org.lwjgl.opengl.GL33.*;

/**
 * Service provider interface for supplying graphics calls. This is a rough
 * abstraction of OpenGL by functionality. Some of the commands present in this
 * SPI are compound OpenGL calls. The intended purpose of this is to allow easy
 * backporting of newer OpenGL APIs to older versions.
 *
 * @author zmichaels
 * @param <BufferT> the SPI buffer implementation.
 * @param <FramebufferT> the SPI framebuffer implementation.
 * @param <TextureT> the SPI texture implementation.
 * @param <ShaderT> the SPI shader implementation.
 * @param <ProgramT> the SPI program implementation.
 * @param <SamplerT> the SPI sampler implementation.
 * @param <VertexArrayT> the SPI vertex array object implementation.
 * @param <QueryT> the SPI draw query object implementation.
 * @since 16.03.07
 */
public interface Driver<BufferT extends Buffer, FramebufferT extends Framebuffer, TextureT extends Texture, ShaderT extends Shader, ProgramT extends Program, SamplerT extends Sampler, VertexArrayT extends VertexArray, QueryT extends DrawQuery> {

    /**
     * Enables the specified blending mode. This call is roughly equivalent to:
     * enabling blending, setting blend equations separate, setting blend
     * functions separate.
     *
     * @param rgbEq the blend equation to use on RGB components.
     * @param aEq the blend equation to use on the ALPHA component.
     * @param rgbFuncSrc the blend function to use on RGB component source.
     * @param rgbFuncDst the blend function to use on RGB component destination.
     * @param aFuncSrc the blend function to use on the ALPHA component source.
     * @param aFuncDst the blend function to use on the ALPHA component
     * destination.
     * @since 16.03.07
     */
    void blendingEnable(long rgbEq, long aEq, long rgbFuncSrc, long rgbFuncDst, long aFuncSrc, long aFuncDst);

    /**
     * Disables blending.
     *
     * @since 16.03.07
     */
    void blendingDisable();

    /**
     * Creates a new Buffer Object. This is not required to allocate a buffer
     * handle nor buffer resources. Rather, it is required only to allocate the
     * buffer handle container. Calls to bufferAllocate,
     * bufferAllocateImmutable, or bufferSetData should be called after
     * bufferCreate.
     *
     * @return the buffer object.
     * @since 16.03.07
     */
    BufferT bufferCreate();

    /**
     * Retrieves an integer-like value from the buffer object.
     *
     * @param buffer the buffer object.
     * @param paramId the parameter name (OpenGL buffer parameter name)
     * @return the result of the buffer parameter.
     * @since 16.03.07
     */
    long bufferGetParameter(BufferT buffer, long paramId);

    /**
     * Deletes a buffer object. This should also invalidate the buffer handle.
     * This method is allowed to silently ignore calls when passed an invalid
     * buffer.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferDelete(BufferT buffer);

    /**
     * Sets the data held by the buffer. This method is allowed to (re)allocate
     * the backing buffer memory. The buffer should be considered valid after
     * this call.
     *
     * @param buffer the buffer object.
     * @param data the data to upload
     * @param usage the usage hints (OpenGL bitfield). The implementation is not
     * required to follow the hints.
     * @since 16.03.07
     */
    void bufferSetData(BufferT buffer, ByteBuffer data, long usage);

    /**
     * Allocates the buffer memory as an immutable object. This should allocate
     * the buffer memory. The properties of the buffer memory can only be
     * changed by reallocation. The buffer should be considered valid after this
     * call.
     *
     * @param buffer the buffer object.
     * @param size the size of memory in bytes.
     * @param bitflags the buffer memory properties (OpenGL bitfield). The
     * implementation is not required to follow the hints specified by the
     * bitflags.
     * @since 16.03.07
     */
    void bufferAllocateImmutable(BufferT buffer, long size, long bitflags);

    /**
     * Allocates the buffer memory. This should allocate or reallocate the
     * buffer memory. The buffer should be considered valid after this call.
     *
     * @param buffer the buffer object.
     * @param size the buffer size in bytes.
     * @param usage the usage hints (OpenGL bitfield). The implementation is not
     * required to follow the usage hints.
     * @since 16.03.07
     */
    void bufferAllocate(BufferT buffer, long size, long usage);

    /**
     * Retrieves a chunk of data from the buffer. The buffer must be valid and
     * the requested size and offset must be in the memory bounds for this call
     * to succeed. The implementation is not required to sanitize the input
     * parameters. Calls to [code]bufferGetData[/code] before data is set via
     * [code]bufferSetData[/code] or [code]bufferMapData[/code] may return
     * garbage data.
     *
     * @param buffer the buffer object.
     * @param offset the offset to begin reading data.
     * @param out the buffer to write the data to. Size is obtained by the
     * available capacity of the ByteBuffer.
     * @since 16.03.07
     */
    void bufferGetData(BufferT buffer, long offset, ByteBuffer out);

    /**
     * Maps the buffer object into local memory. It is recommended, but not
     * required, for the Buffer object to cache the ByteBuffer object.
     *
     * @param buffer the buffer object.
     * @param offset the offset to begin the map (in bytes)
     * @param length the number of bytes to map
     * @param accessFlags map access flags (OpenGL bitfield). The implementation
     * is not required to follow the access flags.
     * @return a ByteBuffer that contains a pointer to the mapped data.
     * @since 16.03.07
     */
    ByteBuffer bufferMapData(BufferT buffer, long offset, long length, long accessFlags);

    /**
     * Unmaps the buffer.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferUnmapData(BufferT buffer);

    /**
     * Copies data from one Buffer object to another Buffer object.
     *
     * @param srcBuffer the source buffer object.
     * @param srcOffset the offset (in bytes) for reading from the source buffer
     * object.
     * @param dstBuffer the destination buffer object.
     * @param dstOffset the offset (in bytes) for writing to the destination
     * buffer object.
     * @param size the number of bytes to copy.
     * @since 16.03.07
     */
    void bufferCopyData(BufferT srcBuffer, long srcOffset, BufferT dstBuffer, long dstOffset, long size);

    /**
     * Invalidates a range of memory for the buffer. This marks the range of
     * data for garbage collection.
     *
     * @param buffer the buffer object.
     * @param offset the offset (in bytes) to begin invalidation.
     * @param length the number of bytes to invalidate.
     * @since 16.03.07
     */
    void bufferInvalidateRange(BufferT buffer, long offset, long length);

    /**
     * Invalidates the entire chunk of memory held by the buffer object.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferInvalidateData(BufferT buffer);

    /**
     * Clears the current framebuffer.
     *
     * @param bitfield the drawbuffers to clear.
     * @param red the clear red color.
     * @param green the clear green color.
     * @param blue the clear blue color.
     * @param alpha the clear alpha.
     * @param depth the clear depth.
     * @since 16.03.07
     */
    void clear(long bitfield, double red, double green, double blue, double alpha, double depth);

    /**
     * Enables depth testing.
     *
     * @param depthTest the type of depth testing to perform (Uses OpenGL
     * constant)
     * @since 16.03.07
     */
    void depthTestEnable(long depthTest);

    /**
     * Disables depth testing.
     *
     * @since 16.03.07
     */
    void depthTestDisable();

    /**
     * Retrieves the framebuffer object that represents the default framebuffer.
     *
     * @return the default framebuffer.
     * @since 16.03.07
     */
    FramebufferT framebufferGetDefault();

    /**
     * Checks if the framebuffer is complete.
     *
     * @param framebuffer the framebuffer.
     * @return true if the framebuffer is complete.
     * @since 16.03.07
     */
    boolean framebufferIsComplete(FramebufferT framebuffer);

    /**
     * Allocates a new framebuffer container. This is allowed to not allocate
     * the framebuffer resources. The framebuffer object also does not have to
     * be valid on return.
     *
     * @return the framebuffer object.
     * @since 16.03.07
     */
    FramebufferT framebufferCreate();

    /**
     * Deletes the framebuffer object. This should also invalidate the
     * framebuffer object. This method is allowed to silently ignore if passed
     * an invalid framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @since 16.03.07
     */
    void framebufferDelete(FramebufferT framebuffer);

    /**
     * Binds the framebuffer object. This will result in writing all draw calls
     * to the framebuffer. The framebuffer should be valid after this call.
     *
     * @param framebuffer the framebuffer object.
     * @param attachments the list of attachments (uses OpenGL contants)
     * @since 16.03.07
     */
    void framebufferBind(FramebufferT framebuffer, IntBuffer attachments);

    /**
     * Adds a depth-stencil attachment to the framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @param texture the texture object.
     * @param mipmapLevel the mipmap level to attach.
     * @since 16.03.07
     */
    void framebufferAddDepthStencilAttachment(FramebufferT framebuffer, TextureT texture, long mipmapLevel);

    /**
     * Adds a depth attachment to the framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @param texture the texture object.
     * @param mipmapLevel the mipmap level to attach.
     * @since 16.03.07
     */
    void framebufferAddDepthAttachment(FramebufferT framebuffer, TextureT texture, long mipmapLevel);

    /**
     * Adds a color attachment to the framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @param attachmentId the id to attach to.
     * @param texture the texture object.
     * @param mipmapLevel the mipmap level to attach.
     * @since 16.03.07
     */
    void framebufferAddAttachment(FramebufferT framebuffer, long attachmentId, TextureT texture, long mipmapLevel);

    /**
     * Blits the framebuffer to another framebuffer.
     *
     * @param srcFb the framebuffer to read from.
     * @param srcX0 the leftmost pixel to read.
     * @param srcY0 the topmost pixel to read.
     * @param srcX1 the rightmost pixel to read.
     * @param srcY1 the bottommost pixel to read.
     * @param dstFb the framebuffer to write to.
     * @param dstX0 the leftmost pixel to read.
     * @param dstY0 the topmost pixel to read.
     * @param dstX1 the rightmost pixel to read.
     * @param dstY1 the bottommost pixel to read.
     * @param bitfield the bitfield specifying what to copy.
     * @param filter the filter to apply. Stencil and depth buffer mandate
     * linear.
     * @since 16.03.07
     */
    void framebufferBlit(FramebufferT srcFb, long srcX0, long srcY0, long srcX1, long srcY1, FramebufferT dstFb, long dstX0, long dstY0, long dstX1, long dstY1, long bitfield, long filter);

    /**
     * Reads pixels from a framebuffer and writes them to a buffer object.
     *
     * @param framebuffer the framebuffer object to read pixels from.
     * @param x the x pixel to begin read.
     * @param y the y pixel to begin read.
     * @param width the number of pixels across to read.
     * @param height the number of pixels down to read.
     * @param format the format to encode the pixel data as.
     * @param type the type to encode the pixel data as.
     * @param dstBuffer the buffer object to write the data to.
     * @since 16.03.07
     */
    void framebufferGetPixels(FramebufferT framebuffer, long x, long y, long width, long height, long format, long type, BufferT dstBuffer);

    /**
     * Reads the pixels from a framebuffer and writes them into a ByteBuffer.
     *
     * @param framebuffer the framebuffer object to read pixels from.
     * @param x the x pixel to begin read.
     * @param y the y pixel to begin read.
     * @param width the number of pixels across to read.
     * @param height the number of pixels down to read.
     * @param format the format to encode the pixel data as.
     * @param type the type to encode the pixel data as.
     * @param dstBuffer the ByteBuffer to write the data to.
     * @since 16.03.07
     */
    void framebufferGetPixels(FramebufferT framebuffer, long x, long y, long width, long height, long format, long type, ByteBuffer dstBuffer);

    /**
     * Applies a draw mask.
     *
     * @param red should the red channel be drawn.
     * @param green should the green channel be drawn.
     * @param blue should the blue channel be drawn.
     * @param alpha should the alpha channel be drawn.
     * @param depth should the depth channel be drawn.
     * @param stencil the stencil bitmask.
     * @since 16.03.07
     */
    void maskApply(boolean red, boolean green, boolean blue, boolean alpha, boolean depth, long stencil);

    /**
     * Sets polygon draw parameters.
     *
     * @param pointSize the size of a point.
     * @param lineWidth the thickness of a line.
     * @param frontFace the front face. Uses OpenGL enum.
     * @param cullFace the face to cull. Uses OpenGL enum; 0 culls nothing.
     * @param polygonMode the polygon mode. Uses OpenGL enum.
     * @param offsetFactor the offset factor.
     * @param offsetUnits the offset units.
     * @since 16.03.07
     */
    void polygonSetParameters(double pointSize, double lineWidth, long frontFace, long cullFace, long polygonMode, double offsetFactor, double offsetUnits);

    /**
     * The shader program to use.
     *
     * @param program the shader program.
     * @since 16.03.07
     */
    void programUse(ProgramT program);

    /**
     * The location to bind for the attribute name.
     *
     * @param program the program object.
     * @param index the attribute index.
     * @param name the attribute name.
     * @since 16.03.07
     */
    void programSetAttribLocation(ProgramT program, long index, String name);

    /**
     * Sets the attributes to write feedback data to.
     *
     * @param program the program object.
     * @param varyings the feedback names.
     * @since 16.03.07
     */
    void programSetFeedbackVaryings(ProgramT program, String[] varyings);

    /**
     * Sets a uniform double matrix. Support of matrices of size 2x2, 3x3, 4x4
     * are required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param mat the DoubleBuffer containing 64bit floating point matrix data.
     * @since 16.03.07
     */
    void programSetUniformMatD(ProgramT program, long uLoc, DoubleBuffer mat);

    /**
     * Sets a uniform float matrix. Support of matrices of size 2x2, 3x3, and
     * 4x4 are required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param mat the FlaotBuffer containing 32bit floating point matrix data.
     * @since 16.03.07
     */
    void programSetUniformMatF(ProgramT program, long uLoc, FloatBuffer mat);

    /**
     * Sets a uniform vector of 64bit doubles. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformD(ProgramT program, long uLoc, double[] value);

    /**
     * Sets a uniform vector of 32bit floats. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformF(ProgramT program, long uLoc, float[] value);

    /**
     * Sets a uniform vector of 32bit integers. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformI(ProgramT program, long uLoc, int[] value);

    /**
     * Links the shaders for the program. The program should be valid after this
     * call.
     *
     * @param program the program object.
     * @param shaders the shaders to link.
     * @since 16.03.07
     */
    void programLinkShaders(ProgramT program, Shader[] shaders);

    /**
     * Creates a new Program object. This call does not need to generate a valid
     * program. It only is required to allocate the program object container.
     *
     * @return the program object.
     * @since 16.03.07
     */
    ProgramT programCreate();

    /**
     * Deletes the program object. This should also invalidate the program
     * handle. This method is allowed to silently ignore when passed an invalid
     * program object.
     *
     * @param program the program object.
     * @since 16.03.07
     */
    void programDelete(ProgramT program);

    /**
     * Sets the shader storage in a program.
     *
     * @param program the program object.
     * @param storageName the name of the shader storage.
     * @param buffer the buffer object to bind as shader storage.
     * @param bindingPoint the binding point of the shader storage.
     * @since 16.03.07
     */
    void programSetStorage(ProgramT program, String storageName, BufferT buffer, long bindingPoint);

    /**
     * Sets a uniform block in a program.
     *
     * @param program the program object.
     * @param uniformName the uniform name.
     * @param buffer the buffer object to bind as a uniform block.
     * @param bindingPoint the binding point for the uniform storage.
     * @since 16.03.07
     */
    void programSetUniformBlock(ProgramT program, String uniformName, BufferT buffer, long bindingPoint);

    /**
     * Executes the program object as a compute shader.
     *
     * @param program the program object to execute.
     * @param numX the number of compute groups along the x-axis.
     * @param numY the number of compute groups along the y-axis.
     * @param numZ the number of compute groups along the z-axis.
     * @since 16.03.07
     */
    void programDispatchCompute(ProgramT program, long numX, long numY, long numZ);

    /**
     * Sets a buffer object to capture feedback data.
     *
     * @param program the program object to bind the feedback to.
     * @param varyingLoc the attribute to capture data from.
     * @param buffer the buffer to write the feedback data to.
     * @since 16.03.07
     */
    void programSetFeedbackBuffer(ProgramT program, long varyingLoc, BufferT buffer);

    /**
     * Retrieves the index id for a uniform by name.
     *
     * @param program the program to request the uniform index from.
     * @param name the name of the uniform.
     * @return the index location.
     * @since 16.03.07
     */
    long programGetUniformLocation(ProgramT program, String name);

    /**
     * Constructs a new Sampler Object. A sampler object contains texture
     * parameters that are applied to shader samplers. The states specified
     * override the states set by the texture.
     *
     * @return the Sampler object.
     */
    SamplerT samplerCreate();

    /**
     * Sets the sampler parameter.
     *
     * @param sampler the sampler object.
     * @param param the sampler parameter name.
     * @param value the sampler value.
     * @since 16.03.07
     */
    void samplerSetParameter(SamplerT sampler, long param, long value);

    /**
     * Sets the sampler parameter.
     *
     * @param sampler the sampler object.
     * @param param the sampler parameter name.
     * @param value the sampler value.
     * @since 16.03.07
     */
    void samplerSetParameter(SamplerT sampler, long param, double value);

    /**
     * Deletes the sampler object. This should invalidate the Sampler handle.
     * This method is allowed to silently ignore if supplied an invalid Sampler
     * object.
     *
     * @param sampler the Sampler object.
     * @since 16.03.07
     */
    void samplerDelete(SamplerT sampler);

    /**
     * Binds the sampler to the specified sampler shader unit. This will
     * override any texture parameters for the texture bound to the specified
     * unit.
     *
     * @param unit the shader sampler unit.
     * @param sampler the sampler to bind.
     * @since 16.03.07
     */
    void samplerBind(long unit, SamplerT sampler);

    /**
     * Enables a scissor test. This will discard any fragments drawn outside the
     * scissor test rectangle.
     *
     * @param left the leftmost pixel of the scissor test rectangle.
     * @param bottom the bottommost pixel of the scissor test rectangle.
     * @param width the number of pixels wide for the scissor test rectangle.
     * @param height the number of pixels tall for the scissor test rectangle.
     * @since 16.03.07
     */
    void scissorTestEnable(long left, long bottom, long width, long height);

    /**
     * Disables a scissor test.
     *
     * @since 16.03.07
     */
    void scissorTestDisable();

    /**
     * Compiles a shader and creates a valid Shader Object.
     *
     * @param type the type of shader to create (Uses OpenGL shader type names).
     * @param source the raw source of the shader
     * @return the shader object.
     * @since 16.03.07
     */
    ShaderT shaderCompile(long type, String source);

    /**
     * Gets the shader info log. This should hold any compilation information.
     *
     * @param shader the shader object.
     * @return the info log.
     * @since 16.03.07
     */
    String shaderGetInfoLog(ShaderT shader);

    /**
     * Retrieves the parameter value of the shader object.
     *
     * @param shader the shader object name.
     * @param pName the parameter name (uses OpenGL shader parameter name).
     * @return the shader parameter value.
     * @since 16.03.07
     */
    long shaderGetParameter(ShaderT shader, long pName);

    /**
     * Deletes the shader object. This should invalidate the shader object. Any
     * programs linked using this shader will be unaffected. This call is
     * allowed to silently ignore when passed an invalid Shader object.
     *
     * @param shader the shader object.
     * @since 16.03.07
     */
    void shaderDelete(ShaderT shader);

    /**
     * Allocates a new immutable texture object with backing memory. The texture
     * should be valid after this call. A minimum width, depth, and height value
     * of 1 must be specified. A 1D texture is returned if height and depth are
     * 1. A 2D texture is returned if depth is 1 and height is greater than 1.
     * Otherwise a 3D texture is returned.
     *
     * @param mipmaps the number of mipmaps to allocate.
     * @param internalFormat the internal pixel format to use. (Uses OpenGL
     * internal format glEnum).
     * @param width the number of pixels wide for the texture. Minimum value is
     * 1.
     * @param height The number of pixels tall for the texture. Minimum value is
     * 1.
     * @param depth The number of pixels deep for the texture. Minimum value is
     * 1.
     * @return the Texture Object.
     * @since 16.03.07
     */
    TextureT textureAllocate(long mipmaps, long internalFormat, long width, long height, long depth);

    /**
     * Binds the texture to the specified sampler unit id.
     * @param texture the texture object.
     * @param unit the sampler id.
     * @since 16.03.07
     */
    void textureBind(TextureT texture, long unit);

    void textureDelete(TextureT texture);

    void textureSetData(TextureT texture, long level, long xOffset, long yOffset, long zOffset, long width, long height, long depth, long format, long type, ByteBuffer data);

    void textureGetData(TextureT texture, long level, long format, long type, ByteBuffer out);

    void textureInvalidateData(TextureT texture, long level);

    void textureInvalidateRange(TextureT texture, long level, long xOffset, long yOffset, long zOffset, long width, long height, long depth);

    void textureGenerateMipmap(TextureT texture);

    long textureGetMaxSize();

    long textureGetMaxBoundTextures();

    long textureGetPageWidth(TextureT texture);

    long textureGetPageHeight(TextureT texture);

    long textureGetPageDepth(TextureT texture);

    long textureGetPreferredFormat(long internalFormat);

    void textureSetParameter(TextureT texture, long param, long value);

    void textureSetParameter(TextureT texture, long param, double value);

    void textureAllocatePage(TextureT texture, long level, long xOffset, long yOffset, long zOffset, long width, long height, long depth);

    void textureDeallocatePage(TextureT texture, long level, long xOffset, long yOffset, long zOffset, long width, long height, long depth);

    long textureGetMaxAnisotropy();

    // vertexArray
    VertexArrayT vertexArrayCreate();

    void vertexArrayDrawElementsIndirect(VertexArrayT vao, BufferT cmdBuffer, long drawMode, long indexType, long offset);

    void vertexArrayDrawArraysIndirect(VertexArrayT vao, BufferT cmdBuffer, long drawMode, long offset);

    void vertexArrayMultiDrawArrays(VertexArrayT vao, long drawMode, IntBuffer first, IntBuffer count);

    void vertexArrayDrawElementsInstanced(VertexArrayT vao, long drawMode, long count, long type, long offset, long instanceCount);

    void vertexArrayDrawArraysInstanced(VertexArrayT vao, long drawMode, long first, long count, long instanceCount);

    void vertexArrayDrawElements(VertexArrayT vao, long drawMode, long count, long type, long offset);

    void vertexArrayDrawArrays(VertexArrayT vao, long drawMode, long start, long count);

    void vertexArrayDrawTransformFeedback(VertexArrayT vao, long drawMode, long start, long count);

    void vertexArrayDelete(VertexArrayT vao);

    void vertexArrayAttachIndexBuffer(VertexArrayT vao, BufferT buffer);

    void vertexArrayAttachBuffer(VertexArrayT vao, long index, BufferT buffer, long size, long type, long stride, long offset, long divisor);

    //viewport
    void viewportApply(long x, long y, long width, long height);

    /**
     * Enables the conditional requirement for the specified DrawQuery.
     *
     * @param condition the conditional requirement for the DrawQuery to
     * succeed.
     * @param query the DrawQuery object.
     * @since 16.03.07
     */
    void drawQueryEnable(long condition, QueryT query);

    /**
     * Disables the conditional.
     *
     * @param condition the conditional.
     * @since 16.03.07
     */
    void drawQueryDisable(long condition);

    void drawQueryBeginConditionalRender(QueryT query, long mode);

    void drawQueryEndConditionRender();

    /**
     * Creates a new DrawQuery object.
     *
     * @return the DrawQuery object.
     * @since 16.03.07
     */
    QueryT drawQueryCreate();

    /**
     * Deletes the draw query object. This should invalidate the DrawQuery. This
     * method is allowed to silently ignore when passed an invalid DrawQuery
     * object.
     *
     * @param query the DrawQuery object.
     * @since 16.03.07
     */
    void drawQueryDelete(QueryT query);

    /**
     * Attempts to guess the generic format from the supplied internal format.
     *
     * @param internalFormat the internal format (Uses OpenGL enum).
     * @return the format.
     * @since 16.03.07
     */
    default int guessFormat(int internalFormat) {
        switch (internalFormat) {

            case GL_COMPRESSED_RGB_S3TC_DXT1_EXT:
            case GL_RGB:
            case GL_COMPRESSED_RGB:
            case GL_COMPRESSED_SRGB:
            case GL_R3_G3_B2:
            case GL_RGB4:
            case GL_RGB5:
            case GL_RGB8:
            case GL_RGB8_SNORM:
            case GL_RGB10:
            case GL_RGB12:
            case GL_RGB16:
            case GL_RGB16_SNORM:
            case GL_SRGB8:
            case GL_RGB16F:
            case GL_RGB32F:
            case GL_RGB8I:
            case GL_RGB8UI:
            case GL_RGB16I:
            case GL_RGB16UI:
            case GL_RGB32I:
            case GL_RGB32UI:
                return GL11.GL_RGB;
            case GL_COMPRESSED_RGBA_S3TC_DXT1_EXT:
            case GL_COMPRESSED_RGBA_S3TC_DXT3_EXT:
            case GL_COMPRESSED_RGBA_S3TC_DXT5_EXT:
            case GL_RGBA:
            case GL_COMPRESSED_RGBA:
            case GL_COMPRESSED_SRGB_ALPHA:
            case GL_RGBA2:
            case GL_RGBA4:
            case GL_RGBA16_SNORM:
            case GL_RGB5_A1:
            case GL_RGBA8:
            case GL_RGBA8_SNORM:
            case GL_RGB10_A2:
            case GL_RGB10_A2UI:
            case GL_RGBA12:
            case GL_RGBA16:
            case GL_SRGB8_ALPHA8:
            case GL_RGBA16F:
            case GL_RGBA32F:
            case GL_R11F_G11F_B10F:
            case GL_RGBA8I:
            case GL_RGBA8UI:
            case GL_RGBA16I:
            case GL_RGBA16UI:
            case GL_RGBA32I:
            case GL_RGBA32UI:
                return GL11.GL_RGBA;
            case GL_STENCIL_INDEX:
                return GL11.GL_STENCIL;
            case GL_RED:
            case GL_R8:
            case GL_R8_SNORM:
            case GL_R16_SNORM:
            case GL_COMPRESSED_RED:
            case GL_R16:
            case GL_R16F:
            case GL_R32F:
            case GL_R8I:
            case GL_R8UI:
            case GL_R16I:
            case GL_R16UI:
            case GL_R32I:
            case GL_R32UI:
                return GL11.GL_RED;
            case GL_DEPTH_COMPONENT:
                return GL11.GL_DEPTH_COMPONENT;
            case GL_DEPTH_STENCIL:
            case GL_DEPTH24_STENCIL8:
            case GL_DEPTH32F_STENCIL8:
                return GL30.GL_DEPTH_STENCIL;
            case GL_RG:
            case GL_COMPRESSED_RG:
            case GL_RG8:
            case GL_RG8_SNORM:
            case GL_RG16:
            case GL_RG16_SNORM:
            case GL_RG16F:
            case GL_RG32F:
            case GL_RG8I:
            case GL_RG8UI:
            case GL_RG16I:
            case GL_RG16UI:
            case GL_RG32I:
            case GL_RG32UI:
                return GL30.GL_RG;
            case GL_DEPTH_COMPONENT16:
            case GL_DEPTH_COMPONENT24:
            case GL_DEPTH_COMPONENT32:
            case GL_DEPTH_COMPONENT32F:
                return GL11.GL_DEPTH_COMPONENT;
            default:
                return GL11.GL_RGBA;
        }
    }
}
