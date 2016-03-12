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

import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 * A Service Provider Interface that supplies driver implementations.
 *
 * @author zmichaels
 * @since 16.03.08
 */
public interface DriverProvider {

    /**
     * Retrieves the instance of the driver. This may initialize the driver and
     * lock the system to using that driver.
     *
     * @return the driver.
     * @since 16.03.10
     */
    @SuppressWarnings("rawtypes")
    Driver getDriverInstance();

    /**
     * Retrieves the driver's name. The default implementation is to return the
     * simple class name.
     *
     * @return the driver's name.
     * @since 16.03.10
     */
    default String getDriverName() {
        return getDriverInstance().getClass().getSimpleName();
    }

    /**
     * Returns a list of Strings that describe the driver. These strings can be
     * used to select drivers by the DriverFactory.
     *
     * @return the list of driver descriptors.
     * @since 16.03.10
     */
    List<String> getDriverDescription();

    /**
     * Checks if the driver is supported by the current context.
     *
     * @return true if the driver may be used.
     * @since 16.03.10
     */
    boolean isSupported();

    /**
     * Checks if buffer objects are supported.
     *
     * @return true if buffer objects are supported.
     * @since 16.03.10
     */
    boolean isBufferObjectSupported();

    /**
     * Checks if immutable buffer objects are supported.
     *
     * @return true if immutable buffer objects are supported.
     * @since 16.03.10
     */
    boolean isImmutableBufferStorageSupported();

    /**
     * Checks if draw query objects are supported.
     *
     * @return true if draw query objects are supported.
     * @since 16.03.10
     */
    boolean isDrawQuerySupported();

    /**
     * Checks if framebuffer objects are supported.
     *
     * @return true if framebuffer objects are supported.
     * @since 16.03.10
     */
    boolean isFramebufferObjectSupported();

    /**
     * Checks if program objects are supported.
     *
     * @return true if program objects are supported.
     * @since 16.03.10
     */
    boolean isProgramSupported();

    /**
     * Checks if sampler objects are supported.
     *
     * @return true if sampler objects are supported.
     * @since 16.03.10
     */
    boolean isSamplerSupported();

    /**
     * Checks if compute shaders are supported.
     *
     * @return true if compute shaders are supported.
     * @since 16.03.10
     */
    boolean isComputeShaderSupported();

    /**
     * Checks if sparse textures are supported.
     *
     * @return true if sparse textures are supported.
     * @since 16.03.10
     */
    boolean isSparseTextureSupported();

    /**
     * Checks if draw indirect is supported.
     *
     * @return true if draw indirect is supported.
     * @since 16.03.10
     */
    boolean isDrawIndirectSupported();

    /**
     * Checks if drawI instanced is supported.
     *
     * @return true if drawInstanced is supported.
     * @since 16.03.10
     */
    boolean isDrawInstancedSupported();

    /**
     * Checks if invalidation of subdata is supported.
     *
     * @return true if buffers and textures may invalidate data.
     * @since 16.03.10
     */
    boolean isInvalidateSubdataSupported();

    /**
     * Checks if separate shader objects is supported.
     *
     * @return true if separate shader objects are supported.
     * @since 16.03.10
     */
    boolean isSeparateShaderObjectsSupported();

    /**
     * Checks if 64bit uniforms are supported.
     *
     * @return true if 64bit uniforms are supported.
     * @since 16.03.10
     */
    boolean is64bitUniformsSupported();

    /**
     * Checks if native vertex array objects are supported.
     *
     * @return true if native vertex array objects are supported.
     * @since 16.03.10
     */
    boolean isVertexArrayObjectSupported();

    /**
     * Calculates the support rating. This is a number between 0.0 and 1.0 that
     * reflects how well the driver is supported by the current context.
     *
     * @return the support rating.
     * @since 16.03.10
     */
    default double getSupportRating() {
        double rating = 0.0;
        rating += isBufferObjectSupported() ? 1.0 : 0.0;
        rating += isImmutableBufferStorageSupported() ? 1.0 : 0.0;
        rating += isDrawQuerySupported() ? 1.0 : 0.0;
        rating += isFramebufferObjectSupported() ? 1.0 : 0.0;
        rating += isProgramSupported() ? 1.0 : 0.0;
        rating += isSamplerSupported() ? 1.0 : 0.0;
        rating += isComputeShaderSupported() ? 1.0 : 0.0;
        rating += isSparseTextureSupported() ? 1.0 : 0.0;
        rating += isDrawIndirectSupported() ? 1.0 : 0.0;
        rating += isDrawInstancedSupported() ? 1.0 : 0.0;
        rating += isInvalidateSubdataSupported() ? 1.0 : 0.0;
        rating += isSeparateShaderObjectsSupported() ? 1.0 : 0.0;
        rating += is64bitUniformsSupported() ? 1.0 : 0.0;
        rating += isVertexArrayObjectSupported() ? 1.0 : 0.0;

        return rating / 14.0;
    }

    /**
     * Retrieves the logger associated with the DriverProvider.
     *
     * @return the logger.
     * @since 16.03.10
     */
    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    /**
     * Logs the driver capabilities. All feature support is logged to debug. The
     * name and rating are logged to info.
     *
     * @since 16.03.10
     */
    default void logCapabilities() {
        final Logger logger = this.getLogger();
        final Marker marker = MarkerFactory.getMarker("gloop-spi");

        logger.info(marker, "Driver Capabilities [{}]", this.getClass().getSimpleName());
        logger.info(marker, "Driver supported:\t\t{}", this.isSupported());
        logger.debug(marker, "64bit uniform:\t\t{}", this.is64bitUniformsSupported());
        logger.debug(marker, "Buffer object:\t\t{}", this.isBufferObjectSupported());
        logger.debug(marker, "Compute shader:\t\t{}", this.isComputeShaderSupported());
        logger.debug(marker, "Draw indirect:\t\t{}", this.isDrawIndirectSupported());
        logger.debug(marker, "Draw instanced:\t\t{}", this.isDrawInstancedSupported());
        logger.debug(marker, "Framebuffer object:\t{}", this.isFramebufferObjectSupported());
        logger.debug(marker, "Immutable buffer storage:\t{}", this.isImmutableBufferStorageSupported());
        logger.debug(marker, "Invalidate subdata:\t{}", this.isInvalidateSubdataSupported());
        logger.debug(marker, "Shader program:\t\t{}", this.isProgramSupported());
        logger.debug(marker, "Sampler object:\t\t{}", this.isSamplerSupported());
        logger.debug(marker, "Separate shader objects:\t{}", this.isSeparateShaderObjectsSupported());
        logger.debug(marker, "Sparse texture:\t\t{}", this.isSparseTextureSupported());
        logger.debug(marker, "Vertex array object:\t{}", this.isVertexArrayObjectSupported());
        logger.info(marker, "Support rating:\t\t{}", this.getSupportRating());

    }
}
