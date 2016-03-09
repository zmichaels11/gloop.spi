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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

/**
 *
 * @author zmichaels
 */
public interface DriverProvider {

    @SuppressWarnings("rawtypes")
    Driver getDriverInstance();

    default String getDriverName() {
        return getDriverInstance().getClass().getName();
    }

    boolean isSupported();

    boolean isBufferObjectSupported();

    boolean isImmutableBufferStorageSupported();

    boolean isDrawQuerySupported();

    boolean isFramebufferObjectSupported();

    boolean isProgramSupported();

    boolean isSamplerSupported();

    boolean isComputeShaderSupported();

    boolean isSparseTextureSupported();

    boolean isDrawIndirectSupported();

    boolean isDrawInstancedSupported();

    boolean isInvalidateSubdataSupported();

    boolean isSeparateShaderObjectsSupported();

    boolean is64bitUniformsSupported();

    boolean isVertexArrayObjectSupported();

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

    default Logger getLogger() {
        return LoggerFactory.getLogger(this.getClass());
    }

    default void logCapabilities() {
        final Logger logger = this.getLogger();
        final Marker marker = MarkerFactory.getMarker("gloop-spi");

        logger.info(marker, "Driver Capabilities [{}]", this.getClass().getSimpleName());
        logger.info(marker, "Driver supported:\t\t{}", this.isSupported());
        logger.info(marker, "64bit uniform:\t\t{}", this.is64bitUniformsSupported());
        logger.info(marker, "Buffer object:\t\t{}", this.isBufferObjectSupported());
        logger.info(marker, "Compute shader:\t\t{}", this.isComputeShaderSupported());
        logger.info(marker, "Draw indirect:\t\t{}", this.isDrawIndirectSupported());
        logger.info(marker, "Draw instanced:\t\t{}", this.isDrawInstancedSupported());
        logger.info(marker, "Framebuffer object:\t{}", this.isFramebufferObjectSupported());
        logger.info(marker, "Immutable buffer storage:\t{}", this.isImmutableBufferStorageSupported());
        logger.info(marker, "Invalidate subdata:\t{}", this.isInvalidateSubdataSupported());
        logger.info(marker, "Shader program:\t\t{}", this.isProgramSupported());
        logger.info(marker, "Sampler object:\t\t{}", this.isSamplerSupported());
        logger.info(marker, "Separate shader objects:\t{}", this.isSeparateShaderObjectsSupported());
        logger.info(marker, "Sparse texture:\t\t{}", this.isSparseTextureSupported());
        logger.info(marker, "Vertex array object:\t{}", this.isVertexArrayObjectSupported());
        logger.info(marker, "Support rating:\t\t{}", this.getSupportRating());

    }
}
