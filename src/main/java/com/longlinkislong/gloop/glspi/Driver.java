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
package com.longlinkislong.gloop.glspi;

import java.nio.ByteBuffer;
import java.nio.DoubleBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import org.slf4j.LoggerFactory;

/**
 * Service provider interface for supplying graphics calls. This is a rough
 * abstraction of OpenGL by functionality. Some of the commands present in this
 * SPI are compound OpenGL calls. The intended purpose of this is to allow easy
 * backporting of newer OpenGL APIs to older versions.
 *
 * @author zmichaels
 * @param <BufferT> the SPI buffer implementation.
 * @param <FramebufferT> the SPI framebuffer implementation.
 * @param <RenderbufferT> the SPI renderbuffer implementation.
 * @param <TextureT> the SPI texture implementation.
 * @param <ShaderT> the SPI shader implementation.
 * @param <ProgramT> the SPI program implementation.
 * @param <SamplerT> the SPI sampler implementation.
 * @param <VertexArrayT> the SPI vertex array object implementation.
 * @since 16.03.07
 */
public interface Driver<BufferT extends Buffer, FramebufferT extends Framebuffer, RenderbufferT extends Renderbuffer, TextureT extends Texture, ShaderT extends Shader, ProgramT extends Program, SamplerT extends Sampler, VertexArrayT extends VertexArray> {

    /**
     * Retrieves a pointer for the texture.
     *
     * @param t the texture
     * @return the pointer.
     * @since 16.07.28
     */
    long textureMap(TextureT t);

    /**
     * Frees the pointer used for the texture.
     *
     * @param t the texture.
     * @since 16.07.28
     */
    void textureUnmap(TextureT t);

    /**
     * Retrieves the supported shader version. Defaults to 1.00.
     *
     * @return the shader version.
     * @since 16.04.05
     */
    int shaderGetVersion();

    /**
     * Applies performance tweaks to the driver. These may be ignored if the
     * driver does not support the tweaks.
     *
     * @param tweaks tweaks to apply
     * @since 16.03.24
     */
    default void applyTweaks(Tweaks tweaks) {
        LoggerFactory.getLogger(this.getClass()).warn("This driver does not support tweaks!");
    }

    /**
     * Creates a new renderbuffer object.
     *
     * @param internalFormat the format for the renderbuffer object.
     * @param width the width of the renderbuffer.
     * @param height the height of the renderbuffer.
     * @return the new renderbuffer object.
     * @since 16.04.04
     */
    RenderbufferT renderbufferCreate(int internalFormat, int width, int height);

    /**
     * Deletes the renderbuffer object.
     *
     * @param renderbuffer the renderbuffer object.
     * @since 16.04.04
     */
    void renderbufferDelete(RenderbufferT renderbuffer);

    /**
     * Disables blending.
     *
     * @since 16.03.07
     */
    void blendingDisable();

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
    void blendingEnable(int rgbEq, int aEq, int rgbFuncSrc, int rgbFuncDst, int aFuncSrc, int aFuncDst);

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
    void bufferAllocate(BufferT buffer, long size, int usage);

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
    void bufferAllocateImmutable(BufferT buffer, long size, int bitflags);

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
     * Deletes a buffer object. This should also invalidate the buffer handle.
     * This method is allowed to silently ignore calls when passed an invalid
     * buffer.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferDelete(BufferT buffer);

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

    void bufferGetData(BufferT buffer, long offset, int[] out);

    void bufferGetData(BufferT buffer, long offset, float[] out);

    /**
     * Retrieves an integer-like value from the buffer object.
     *
     * @param buffer the buffer object.
     * @param paramId the parameter name (OpenGL buffer parameter name)
     * @return the result of the buffer parameter.
     * @since 16.03.07
     */
    int bufferGetParameterI(BufferT buffer, int paramId);

    /**
     * Invalidates the entire chunk of memory held by the buffer object.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferInvalidateData(BufferT buffer);

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
    ByteBuffer bufferMapData(BufferT buffer, long offset, long length, int accessFlags);

    /**
     * Sets the data held by the buffer
     *
     * @param buffer the buffer object.
     * @param data the data to upload
     * @param offset the offset to write the data to
     * @since 16.03.07
     */
    void bufferSetData(BufferT buffer, long offset, ByteBuffer data);

    /**
     * Sets the data held by the array
     *
     * @param buffer the data to upload
     * @param offset the offset to write the data to
     * @param data the data to write
     */
    void bufferSetData(BufferT buffer, long offset, float[] data);

    /**
     * Sets the data held by the array
     *
     * @param buffer the data to upload
     * @param offset the offset to write the data to
     * @param data the data to write
     */
    void bufferSetData(BufferT buffer, long offset, int[] data);

    /**
     * Unmaps the buffer.
     *
     * @param buffer the buffer object.
     * @since 16.03.07
     */
    void bufferUnmapData(BufferT buffer);

    /**
     * Binds the buffer to the specified binding point. Uses UBO.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @since 16.07.05
     */
    void bufferBindUniform(BufferT buffer, int bindingPoint);

    /**
     * Binds a segment of the buffer to the specified binding point. Uses UBO.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @param offset the offset within the buffer for binding.
     * @param size the amount of bytes to bind.
     * @since 16.07.05
     */
    void bufferBindUniform(BufferT buffer, int bindingPoint, long offset, long size);

    void bufferBindAtomic(BufferT buffer, int bindingPoint);

    void bufferBindAtomic(BufferT buffer, int bindingPoint, long offset, long size);

    /**
     * Binds the buffer to the specified binding point. Uses TFB.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @since 16.07.05
     */
    void bufferBindFeedback(BufferT buffer, int bindingPoint);

    /**
     * Binds a segment of a buffer to the specified binding point. Uses TFB.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @param offset the offset within the buffer.
     * @param size the amount of bytes to bind.
     * @since 16.07.15
     */
    void bufferBindFeedback(BufferT buffer, int bindingPoint, long offset, long size);

    /**
     * Retrieves the maximum size for a uniform block.
     *
     * @return the maximum size of a uniform block.
     * @since 16.07.28
     */
    int bufferGetMaxUniformBlockSize();

    int bufferGetMaxUniformBindings();

    /**
     * Binds the buffer to the specified binding point. Uses SSBO.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @since 16.07.05
     */
    void bufferBindStorage(BufferT buffer, int bindingPoint);

    /**
     * Binds a segment of the buffer to the specified binding point. Uses SSBO.
     *
     * @param buffer the buffer.
     * @param bindingPoint the binding point.
     * @param offset the offset within the buffer for binding.
     * @param size the amount of bytes to bind.
     * @since 16.07.05
     */
    void bufferBindStorage(BufferT buffer, int bindingPoint, long offset, long size);

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
    void clear(int bitfield, float red, float green, float blue, float alpha, double depth);

    /**
     * Disables depth testing.
     *
     * @since 16.03.07
     */
    void depthTestDisable();

    /**
     * Enables depth testing.
     *
     * @param depthTest the type of depth testing to perform (Uses OpenGL
     * constant)
     * @since 16.03.07
     */
    void depthTestEnable(int depthTest);

    /**
     * Adds a renderbuffer attachment to the framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @param attachmentId the id to attach to.
     * @param renderbuffer the renderbuffer object.
     * @since 16.04.04
     */
    void framebufferAddRenderbuffer(FramebufferT framebuffer, int attachmentId, RenderbufferT renderbuffer);

    /**
     * Adds a texture attachment to the framebuffer.
     *
     * @param framebuffer the framebuffer object.
     * @param attachmentId the id to attach to.
     * @param texture the texture object.
     * @param mipmapLevel the mipmap level to attach.
     * @since 16.03.07
     */
    void framebufferAddAttachment(FramebufferT framebuffer, int attachmentId, TextureT texture, int mipmapLevel);

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
    void framebufferBlit(
            FramebufferT srcFb, int srcX0, int srcY0, int srcX1, int srcY1,
            FramebufferT dstFb, int dstX0, int dstY0, int dstX1, int dstY1,
            int bitfield, int filter);

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
     * Retrieves the framebuffer object that represents the default framebuffer.
     *
     * @return the default framebuffer.
     * @since 16.03.07
     */
    FramebufferT framebufferGetDefault();

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
    void framebufferGetPixels(
            FramebufferT framebuffer, int x, int y, int width, int height,
            int format, int type,
            BufferT dstBuffer);

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
    void framebufferGetPixels(
            FramebufferT framebuffer, int x, int y, int width, int height,
            int format, int type,
            ByteBuffer dstBuffer);

    void framebufferGetPixels(
            FramebufferT framebuffer,
            int x, int y, int width, int height,
            int format, int type,
            int[] dst);

    void framebufferGetPixels(
            FramebufferT framebuffer,
            int x, int y, int width, int height,
            int format, int type,
            float[] dst);

    /**
     * Checks if the framebuffer is complete.
     *
     * @param framebuffer the framebuffer.
     * @return true if the framebuffer is complete.
     * @since 16.03.07
     */
    boolean framebufferIsComplete(FramebufferT framebuffer);

    /**
     * Attempts to guess the generic format from the supplied internal format.
     *
     * @param internalFormat the internal format (Uses OpenGL enum).
     * @return the format.
     * @since 16.03.07
     */
    default int guessFormat(int internalFormat) {
        switch (internalFormat) {

            case 0x83F0 /* GL_COMPRESSED_RGB_S3TC_DXT1_EXT*/:
            case 0x1907 /* GL_RGB */:
            case 0x84ED /* GL_COMPRESSED_RGB */:
            case 0x8C48 /* GL_COMPRESSED_SRGB */:
            case 0x2A10 /* GL_R3_G3_B2 */:
            case 0x804F /* GL_RGB4 */:
            case 0x8050 /* GL_RGB5 */:
            case 0x8051 /* GL_RGB8 */:
            case 0x8F96 /* GL_RGB8_SNORM */:
            case 0x8052 /* GL_RGB10 */:
            case 0x8053 /* GL_RGB12 */:
            case 0x8054 /* GL_RGB16 */:
            case 0x8F9A /* GL_RGB16_SNORM */:
            case 0x8C41 /* GL_SRGB8 */:
            case 0x881B /* GL_RGB16F */:
            case 0x8815 /* GL_RGB32F */:
            case 0x8D8F /* GL_RGB8I */:
            case 0x8D7D /* GL_RGB8UI */:
            case 0x8D89 /* GL_RGB16I */:
            case 0x8D77 /* GL_RGB16UI */:
            case 0x8D83 /* GL_RGB32I */:
            case 0x8D71 /* GL_RGB32UI */:
            case 0x8C3A /* GL_R11F_G11F_B10F */:
                return 0x1907 /* GL_RGB */;
            case 0x83F1 /*GL_COMPRESSED_RGBA_S3TC_DXT1_EXT */:
            case 0x83F2 /* GL_COMPRESSED_RGBA_S3TC_DXT3_EXT */:
            case 0x83F3 /* GL_COMPRESSED_RGBA_S3TC_DXT5_EXT */:
            case 0x1908 /* GL_RGBA */:
            case 0x84EE /* GL_COMPRESSED_RGBA */:
            case 0x8C49 /* GL_COMPRESSED_SRGB_ALPHA */:
            case 0x8055 /* GL_RGBA2 */:
            case 0x8056 /* GL_RGBA4 */:
            case 0x8F9B /* GL_RGBA16_SNORM */:
            case 0x8057 /* GL_RGB5_A1 */:
            case 0x8058 /* GL_RGBA8 */:
            case 0x8F97 /* GL_RGBA8_SNORM */:
            case 0x8059 /* GL_RGB10_A2 */:
            case 0x906F /* GL_RGB10_A2UI */:
            case 0x805A /* GL_RGBA12 */:
            case 0x805B /* GL_RGBA16 */:
            case 0x8C43 /* GL_SRGB8_ALPHA8 */:
            case 0x881A /* GL_RGBA16F */:
            case 0x8814 /* GL_RGBA32F */:
            case 0x8D8E /* GL_RGBA8I */:
            case 0x8D7C /* GL_RGBA8UI */:
            case 0x8D88 /* GL_RGBA16I */:
            case 0x8D76 /* GL_RGBA16UI */:
            case 0x8D82 /* GL_RGBA32I */:
            case 0x8D70/* GL_RGBA32UI */:
                return 0x1908 /* GL_RGBA */;            
            case 0x1901 /* GL_STENCIL_INDEX */:
                return 0x1802 /* GL_STENCIL */;
            case 0x1903 /* GL_RED */:
            case 0x8229 /* GL_R8 */:
            case 0x8F94 /* GL_R8_SNORM */:
            case 0x8F98 /* GL_R16_SNORM */:
            case 0x8225 /* GL_COMPRESSED_RED */:
            case 0x822A /* GL_R16 */:
            case 0x822D /* GL_R16F */:
            case 0x822E /* GL_R32F */:
            case 0x8231 /* GL_R8I */:
            case 0x8232 /* GL_R8UI */:
            case 0x8233 /* GL_R16I */:
            case 0x8234 /* GL_R16UI */:
            case 0x8235 /* GL_R32I */:
            case 0x8236 /* GL_R32UI */:
                return 0x1903 /* GL_RED */;
            case 0x1902 /* GL_DEPTH_COMPONENT */:
                return 0x1902 /* GL_DEPTH_COMPONENT */;
            case 0x84F9 /* GL_DEPTH_STENCIL */:
            case 0x88F0 /* GL_DEPTH24_STENCIL8 */:
            case 0x8CAD /* GL_DEPTH32F_STENCIL8 */:
                return 0x84F9 /* GL_DEPTH_STENCIL */;
            case 0x8227 /* GL_RG */:
            case 0x8226 /* GL_COMPRESSED_RG */:
            case 0x822B /* GL_RG8 */:
            case 0x8F95 /* GL_RG8_SNORM */:
            case 0x822C /* GL_RG16 */:
            case 0x8F99 /* GL_RG16_SNORM */:
            case 0x822F /* GL_RG16F */:
            case 0x8230 /* GL_RG32F */:
            case 0x8237 /* GL_RG8I */:
            case 0x8238 /* GL_RG8UI */:
            case 0x8239 /* GL_RG16I */:
            case 0x823A /* GL_RG16UI */:
            case 0x823B /* GL_RG32I */:
            case 0x823C /* GL_RG32UI */:
                return 0x8227 /* GL30.GL_RG */;
            case 0x81A5 /* GL_DEPTH_COMPONENT16 */:
            case 0x81A6 /* GL_DEPTH_COMPONENT24 */:
            case 0x81A7 /* GL_DEPTH_COMPONENT32 */:
            case 0x8CAC /* GL_DEPTH_COMPONENT32F */:
                return 0x1902  /* GL_DEPTH_COMPONENT */;
            default:
                return 0x1908 /* GL_RGBA */;
        }
    }

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
    void maskApply(
            boolean red, boolean green, boolean blue, boolean alpha,
            boolean depth,
            int stencil);

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
    void polygonSetParameters(
            float pointSize, float lineWidth,
            int frontFace, int cullFace, int polygonMode,
            float offsetFactor, float offsetUnits);

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
     * Executes the program object as a compute shader.
     *
     * @param program the program object to execute.
     * @param numX the number of compute groups along the x-axis.
     * @param numY the number of compute groups along the y-axis.
     * @param numZ the number of compute groups along the z-axis.
     * @since 16.03.07
     */
    void programDispatchCompute(ProgramT program, int numX, int numY, int numZ);

    /**
     * Retrieves the index id for a uniform by name.
     *
     * @param program the program to request the uniform index from.
     * @param name the name of the uniform.
     * @return the index location.
     * @since 16.03.07
     */
    int programGetUniformLocation(ProgramT program, String name);

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
     * The location to bind for the attribute name.
     *
     * @param program the program object.
     * @param index the attribute index.
     * @param name the attribute name.
     * @since 16.03.07
     */
    void programSetAttribLocation(ProgramT program, int index, String name);

    /**
     * Sets the attributes to write feedback data to.
     *
     * @param program the program object.
     * @param varyings the feedback names.
     * @since 16.03.07
     */
    void programSetFeedbackVaryings(ProgramT program, String[] varyings);

    /**
     * Sets the location of a uniform block binding for the Program.
     *
     * @param program the program.
     * @param uniformBlockName the uniform block name.
     * @param binding the binding.
     * @since 16.07.05
     */
    void programSetUniformBlockBinding(ProgramT program, String uniformBlockName, int binding);

    /**
     * Sets the location of a shader storage block name.
     *
     * @param program the program.
     * @param uniformBlockName the shader storage block name.
     * @param binding the binding point.
     * @since 16.07.05
     */
    void programSetStorageBlockBinding(ProgramT program, String uniformBlockName, int binding);

    /**
     * Retrieves the location of the uniform block binding or -1 if no binding
     * was assigned.
     *
     * @param program the program.
     * @param uniformBlockName the uniform block name.
     * @return the binding location.
     * @since 16.07.05
     */
    int programGetUniformBlockBinding(ProgramT program, String uniformBlockName);

    /**
     * Retrieves the location of the shader storage block binding or -1 if no
     * binding was assigned/found.
     *
     * @param program the program.
     * @param storageBlockName the shader storage block name.
     * @return the binding location.
     * @since 16.07.05
     */
    int programGetStorageBlockBinding(ProgramT program, String storageBlockName);

    /**
     * Sets a uniform vector of 64bit doubles. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformD(ProgramT program, int uLoc, double[] value);

    /**
     * Sets a uniform vector of 32bit floats. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformF(ProgramT program, int uLoc, float[] value);

    /**
     * Sets a uniform vector of 32bit integers. Support of vector length 1-4 is
     * required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param value the values to write.
     * @since 16.03.07
     */
    void programSetUniformI(ProgramT program, int uLoc, int[] value);

    /**
     * Sets a uniform double matrix. Support of matrices of size 2x2, 3x3, 4x4
     * are required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param mat the DoubleBuffer containing 64bit floating point matrix data.
     * @since 16.03.07
     */
    void programSetUniformMatD(ProgramT program, int uLoc, DoubleBuffer mat);
    
    void programSetUniformMatD(ProgramT program, int uLoc, double[] mat);

    /**
     * Sets a uniform float matrix. Support of matrices of size 2x2, 3x3, and
     * 4x4 are required.
     *
     * @param program the program object.
     * @param uLoc the uniform index.
     * @param mat the FlaotBuffer containing 32bit floating point matrix data.
     * @since 16.03.07
     */
    void programSetUniformMatF(ProgramT program, int uLoc, FloatBuffer mat);
    
    void programSetUniformMatF(ProgramT program, int uLoc, float[] mat);

    /**
     * The shader program to use.
     *
     * @param program the shader program.
     * @since 16.03.07
     */
    void programUse(ProgramT program);

    /**
     * Binds the sampler to the specified sampler shader unit. This will
     * override any texture parameters for the texture bound to the specified
     * unit.
     *
     * @param unit the shader sampler unit.
     * @param sampler the sampler to bind.
     * @since 16.03.07
     */
    void samplerBind(int unit, SamplerT sampler);

    /**
     * Constructs a new Sampler Object. A sampler object contains texture
     * parameters that are applied to shader samplers. The states specified
     * override the states set by the texture.
     *
     * @return the Sampler object.
     */
    SamplerT samplerCreate();

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
     * Sets the sampler parameter.
     *
     * @param sampler the sampler object.
     * @param param the sampler parameter name.
     * @param value the sampler value.
     * @since 16.03.07
     */
    void samplerSetParameter(SamplerT sampler, int param, int value);

    /**
     * Sets the sampler parameter.
     *
     * @param sampler the sampler object.
     * @param param the sampler parameter name.
     * @param value the sampler value.
     * @since 16.03.07
     */
    void samplerSetParameter(SamplerT sampler, int param, float value);

    /**
     * Disables a scissor test.
     *
     * @since 16.03.07
     */
    void scissorTestDisable();

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
    void scissorTestEnable(int left, int bottom, int width, int height);

    /**
     * Compiles a shader and creates a valid Shader Object.
     *
     * @param type the type of shader to create (Uses OpenGL shader type names).
     * @param source the raw source of the shader
     * @return the shader object.
     * @since 16.03.07
     */
    ShaderT shaderCompile(int type, String source);

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
    int shaderGetParameterI(ShaderT shader, int pName);

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
     * @param dataType the data type to use. Default is GL_UNSIGNED_BYTE
     * @return the Texture Object.
     * @since 16.03.07
     */
    TextureT textureAllocate(int mipmaps, int internalFormat, int width, int height, int depth, int dataType);

    /**
     * Binds the texture to the specified sampler unit id.
     *
     * @param texture the texture object.
     * @param unit the sampler id.
     * @since 16.03.07
     */
    void textureBind(TextureT texture, int unit);

    /**
     * Deletes a texture. This should also invalidate the texture handle. This
     * method is allowed to silently ignore when passed an invalid texture.
     *
     * @param texture the texture object.
     * @since 16.03.08
     */
    void textureDelete(TextureT texture);

    /**
     * Generates mipmaps for the texture object. This method generates all
     * mipmap levels based on the base mipmap. The texture must be valid for
     * this method to succeed. Calling this method without setting the base
     * mipmap will result in undefined behavior.
     *
     * @param texture the texture object.
     * @since 16.03.08
     */
    void textureGenerateMipmap(TextureT texture);

    /**
     * Reads data from a texture. The texture must have its memory allocated
     * prior to calling this method. Calling this method before data is set may
     * result in reading garbage data.
     *
     * @param texture the texture object.
     * @param level the mipmap level.
     * @param format the pixel format.
     * @param type the pixel pack type.
     * @param out the ByteBuffer to write the data to.
     * @since 16.03.08
     */
    void textureGetData(
            TextureT texture, int level,
            int format, int type,
            ByteBuffer out);

    void textureGetData(
            TextureT texture, int level,
            int format, int type,
            int[] out);

    void textureGetData(
            TextureT texture, int level,
            int format, int type,
            float[] out);

    /**
     * Reads data from a texture. The texture must have its memory allocated
     * prior to calling this method. Calling this method before data is set may
     * result in reading garbage data.
     *
     * @param texture the texture object
     * @param level the mipmap level.
     * @param format the pixel format.
     * @param type the pixel pack type.
     * @param out the Buffer object to read into
     * @param size the number of bytes to read into the buffer.
     * @param offset the offset in bytes to write at.
     */
    void textureGetData(
            TextureT texture, int level,
            int format, int type,
            BufferT out, long offset, int size);

    /**
     * Retrieves the maximum level of anisotropic filtering supported for
     * textures.
     *
     * @return the maximum level of anisotropic filtering.
     */
    float textureGetMaxAnisotropy();

    /**
     * Retrieves the maximum number of textures bound for a shader stage.
     * Usually this will be 16.
     *
     * @return the maximum number of textures allowed to be bound.
     * @since 16.03.08
     */
    int textureGetMaxBoundTextures();

    /**
     * Retrieves the maximum size in pixels for any dimension of a texture.
     *
     * @return the maximum size along any axis.
     * @since 16.03.08
     */
    int textureGetMaxSize();

    /**
     * Retrieves the preferred format type for the corresponding internal
     * format. For example, some GPUs prefer using BGRA instead of RGBA. This
     * is, however, usually only a thing on older hardware.
     *
     * @param internalFormat the internal pixel format.
     * @return the preferred pixel format.
     * @since 16.03.08
     */
    int textureGetPreferredFormat(int internalFormat);

    /**
     * Invalidates a mipmap level of the texture. This indicates that the mipmap
     * level may be garbage collected.
     *
     * @param texture the texture object.
     * @param level the mipmap level.
     * @since 16.03.08
     */
    void textureInvalidateData(TextureT texture, int level);

    /**
     * Invalidates a segment of a mipmap level of the texture. This indicates
     * that part of the mipmap may be garbage collected.
     *
     * @param texture the texture object.
     * @param level the mipmap level.
     * @param xOffset the offset along the x-axis for the invalidation cube.
     * @param yOffset the offset along the y-axis for the invalidation cube.
     * Expected to be 0 for 1D textures.
     * @param zOffset the offset along the z-axis for the invalidation cube.
     * Expected to be 0 for 1D or 2D textures.
     * @param width the width of the invalidation cube.
     * @param height the height of the invalidation cube. Expected to be 0 for
     * 1D textures.
     * @param depth the depth of the invalidation cube. Expected to be 0 for 1D
     * and 2D textures.
     * @since 16.03.08
     */
    void textureInvalidateRange(
            TextureT texture, int level,
            int xOffset, int yOffset, int zOffset,
            int width, int height, int depth);

    /**
     * Sets data in the texture. The texture's memory must be allocated prior to
     * calling this method. 1D textures expect yOffset and zOffset both to be 0
     * and for height and depth to be 1. 2D textures expect zOffset to be 0 and
     * depth to be 1.
     *
     * @param texture the texture object.
     * @param level the mipmap level to write data to.
     * @param xOffset the offset along the x-axis.
     * @param yOffset the offset along the y-axis. Expected to be 0 if texture
     * is 1D.
     * @param zOffset the offset along the z-axis. Expected to be 0 if texture
     * is 1D or 2D.
     * @param width the width of the data uploaded.
     * @param height the height of the data uploaded. Expected to be 1 if
     * texture is 1D.
     * @param depth the depth of the data uploaded. Expected to be 1 if texture
     * is 1D or 2D.
     * @param format the pixel format.
     * @param type the pixel packing type.
     * @param data the data.
     * @since 16.03.08
     */
    void textureSetData(
            TextureT texture, int level,
            int xOffset, int yOffset, int zOffset,
            int width, int height, int depth,
            int format, int type, ByteBuffer data);

    void textureSetData(
            TextureT texture, int level,
            int xOffset, int yOffset, int zOffset,
            int width, int height, int depth,
            int format, int type, int[] data);

    void textureSetData(
            TextureT texture, int level,
            int xOffset, int yOffset, int zOffset,
            int width, int height, int depth,
            int format, int type, float[] data);

    /**
     * Sets data in a texture. The texture's memory must be allocated prior to
     * calling this method. 1D textures expect yOffset and zOffset both to be 0
     * and for height and depth to be 1. 2D textures expect zOffset to be 0 and
     * depth to be 1.
     *
     * @param texture the texture
     * @param level the mipmap level
     * @param xOffset xOffset
     * @param yOffset yOffset. Expected to be 0 if texture is 1D.
     * @param zOffset zOffset. Expected to be 0 if texture is 1D or 2D
     * @param width the width
     * @param height the height. Expected to be 1 if texture is 1D.
     * @param depth the depth. Expected to be 1 if texture is 1D or 2D.
     * @param format the pixel format.
     * @param type the pixel packing type.
     * @param buffer the buffer holding the data
     * @param offset the offset to look into the buffer.
     */
    void textureSetData(
            TextureT texture, int level,
            int xOffset, int yOffset, int zOffset,
            int width, int height, int depth,
            int format, int type, BufferT buffer, long offset);

    /**
     * Sets a texture parameter.
     *
     * @param texture the texture object.
     * @param param the parameter name.
     * @param value the parameter value.
     * @since 16.03.08
     */
    void textureSetParameter(TextureT texture, int param, int value);

    /**
     * Sets the texture parameter.
     *
     * @param texture the texture object.
     * @param param the parameter name.
     * @param value the parameter value.
     * @since 16.03.08
     */
    void textureSetParameter(TextureT texture, int param, float value);

    void vertexArrayAttachBuffer(
            VertexArrayT vao, int index,
            BufferT buffer, int size, int type,
            int stride, long offset, int divisor);

    void vertexArrayAttachIndexBuffer(VertexArrayT vao, BufferT buffer);

    // vertexArray
    VertexArrayT vertexArrayCreate();

    void vertexArrayDelete(VertexArrayT vao);

    void vertexArrayDrawArrays(VertexArrayT vao, int drawMode, int start, int count);

    void vertexArrayDrawArraysIndirect(VertexArrayT vao, BufferT cmdBuffer, int drawMode, long offset);

    void vertexArrayDrawArraysInstanced(VertexArrayT vao, int drawMode, int first, int count, int instanceCount);

    void vertexArrayDrawElements(VertexArrayT vao, int drawMode, int count, int type, long offset);

    void vertexArrayDrawElementsIndirect(VertexArrayT vao, BufferT cmdBuffer, int drawMode, int indexType, long offset);

    void vertexArrayDrawElementsInstanced(VertexArrayT vao, int drawMode, int count, int type, long offset, int instanceCount);

    // transform feedback
    void transformFeedbackBegin(int drawMode);

    void transformFeedbackEnd();

    //viewport
    void viewportApply(int x, int y, int width, int height);

}
