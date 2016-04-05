/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.glspi;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author zmichaels
 */
public final class Tweaks {

    public final boolean ignoreVaoStateReset;
    public final boolean memorizeProgram;
    public final boolean memorizeVao;
    public final boolean memorizeBuffer;
    public final boolean ignoreBufferStateReset;
    public final boolean ignoreTextureStateReset;
    public final boolean ignoreFramebufferStateReset;
    public final boolean memorizeFramebuffer;
    public final boolean ignoreProgramStateReset;
    public final boolean ignoreFramebufferCheck;
    public final Map<String, Boolean> additionalTweaks;

    public Tweaks() {
        this(false, false, false, false, false, false, false, false, false, false, Collections.emptyMap());
    }

    public Tweaks(
            final boolean ignoreVaoStateReset,
            final boolean memorizeProgram,
            final boolean memorizeVao,
            final boolean ignoreBufferStateReset,
            final boolean ignoreTextureStateReset,
            final boolean ignoreFramebufferStateReset,
            final boolean memorizeFramebuffer,
            final boolean ignoreProgramStateReset,
            final boolean memorizeBuffer,
            final boolean ignoreFramebufferCheck,
            final Map<String, Boolean> additionalTweaks) {

        this.ignoreVaoStateReset = ignoreVaoStateReset;
        this.memorizeProgram = memorizeProgram;
        this.memorizeVao = memorizeVao;
        this.ignoreBufferStateReset = ignoreBufferStateReset;
        this.ignoreTextureStateReset = ignoreTextureStateReset;
        this.ignoreFramebufferStateReset = ignoreFramebufferStateReset;
        this.memorizeFramebuffer = memorizeFramebuffer;
        this.ignoreProgramStateReset = ignoreProgramStateReset;
        this.memorizeBuffer = memorizeBuffer;
        this.ignoreFramebufferCheck = ignoreFramebufferCheck;
        this.additionalTweaks = Collections.unmodifiableMap(new HashMap<>(additionalTweaks));
    }

    public Tweaks withIgnoreVaoStateReset(final boolean ignore) {
        return new Tweaks(
                ignore,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withMemorizeProgram(final boolean memorize) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                memorize, this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withMemorizeVao(final boolean memorize) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                memorize,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withIgnoreBufferStateReset(final boolean ignore) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                ignore,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withIgnoreTextureStateReset(final boolean ignore) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                ignore,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withIgnoreFramebufferStateReset(final boolean ignore) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                ignore,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }

    public Tweaks withAdditionalTweaks(final Map<String, Boolean> tweaks) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                tweaks);
    }

    public Tweaks withMemorizeFramebuffer(final boolean memorize) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                memorize,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }
    
    public Tweaks withIgnoreProgramStateReset(final boolean ignore) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                ignore,
                this.memorizeBuffer,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }
    
    public Tweaks withMemorizeBuffer(final boolean memorize) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                memorize,
                this.ignoreFramebufferCheck,
                this.additionalTweaks);
    }
    
    public Tweaks withIgnoreFramebufferCheck(final boolean check) {
        return new Tweaks(
                this.ignoreVaoStateReset,
                this.memorizeProgram,
                this.memorizeVao,
                this.ignoreBufferStateReset,
                this.ignoreTextureStateReset,
                this.ignoreFramebufferStateReset,
                this.memorizeFramebuffer,
                this.ignoreProgramStateReset,
                this.memorizeBuffer,
                check,
                this.additionalTweaks);
    }

    public Tweaks withAllPerformanceTweaks(final boolean tweaks) {
        return new Tweaks(tweaks, tweaks, tweaks, tweaks, tweaks, tweaks, tweaks, tweaks, tweaks, tweaks, this.additionalTweaks);
    }
}
