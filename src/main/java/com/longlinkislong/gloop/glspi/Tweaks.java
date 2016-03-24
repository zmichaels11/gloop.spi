/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.glspi;

/**
 *
 * @author zmichaels
 */
public final class Tweaks {
    public final boolean ignoreVaoStateReset;
    public final boolean memorizeProgram;
    
    public Tweaks() {
        this(false, false);
    }
    
    public Tweaks(final boolean ignoreVaoStateReset, final boolean memorizeProgram) {
        this.ignoreVaoStateReset = ignoreVaoStateReset;
        this.memorizeProgram = memorizeProgram;
    }
    
    public Tweaks withIgnoreVaoStateReset(final boolean ignore) {
        return new Tweaks(ignore, this.memorizeProgram);
    }
    
    public Tweaks withMemorizeProgram(final boolean memorize) {
        return new Tweaks(this.ignoreVaoStateReset, memorize);
    }
}
