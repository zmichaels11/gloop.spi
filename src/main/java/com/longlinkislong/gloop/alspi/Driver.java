/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.alspi;

import java.nio.ByteBuffer;

/**
 *
 * @author zmichaels
 * @param <DeviceT>
 * @param <BufferT>
 * @param <ListenerT>
 * @param <SourceT>
 * @param <AuxEffectSlotT>
 * @param <FilterT>
 * @param <EffectT>
 */
public interface Driver<
        DeviceT extends Device, BufferT extends Buffer, ListenerT extends Listener, SourceT extends Source, AuxEffectSlotT extends AuxiliaryEffectSlot, EffectT extends Effect, FilterT extends Filter> {

    default int  sourceGetMaxAuxiliaryEffectSlotSends() {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }
    
    default AuxEffectSlotT auxiliaryEffectSlotCreate() {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }

    default void auxiliaryEffectSlotDelete(AuxEffectSlotT slot) {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }

    default void auxiliaryEffectSlotAttachEffect(AuxEffectSlotT slot, EffectT effect) {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }

    default void sourceSendAuxiliaryEffectSlot(SourceT source, AuxEffectSlotT slot, int send) {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }

    default void sourceSendAuxiliaryEffectSlot(SourceT source, AuxEffectSlotT slot, int send, FilterT filter) {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }
    
    default void sourceSendDisable(SourceT source, int send) {
        throw new UnsupportedOperationException("Auxiliary Effect Slot objects are not supported by this implementation!");
    }
    
    default void sourceAttachDirectFilter(SourceT source, FilterT filter) {
        throw new UnsupportedOperationException("Source objects do not support applying Filter objects!");
    }
    
    default void sourceRemoveDirectFilter(SourceT source) {
        throw new UnsupportedOperationException("Source objects do not support applying Filter objects!");
    }

    default EffectT effectCreate(int effectType) {
        throw new UnsupportedOperationException("Effect objects are not supported!");
    }

    default void effectDelete(EffectT effect) {
        throw new UnsupportedOperationException("Effect objects are not supported!");
    }
    
    default void effectSetProperty(EffectT effect, int name, int value) {
        throw new UnsupportedOperationException("Effect objects are not supported!");
    }
    
    default void effectSetProperty(EffectT effect, int name, float value) {
        throw new UnsupportedOperationException("Effect objects are not supported!");
    }
    
    default FilterT filterCreate(int filterType) {
        throw new UnsupportedOperationException("Filter objects are not supported!");
    }
    
    default void filterDelete(FilterT filter) {
        throw new UnsupportedOperationException("Filter objects are not supported!");
    }
    
    default void filterSetProperty(FilterT filter, int name, int value) {
        throw new UnsupportedOperationException("Filter objects are not supported!");
    }
    
    default void filterSetProperty(FilterT filter, int name, float value) {
        throw new UnsupportedOperationException("Filter objects are not supported!");
    }
    
    DeviceT deviceCreate();

    void deviceDelete(DeviceT device);

    void sourceSetPitch(SourceT source, float pitch);

    void sourceSetGain(SourceT source, float gain);

    void sourceSetPosition(SourceT source, float x, float y, float z);

    void sourceSetVelocity(SourceT source, float x, float y, float z);

    void sourceSetDirection(SourceT source, float x, float y, float z);

    void bufferSetData(BufferT buffer, int format, ByteBuffer data, int frequency);
    
    void bufferSetData(BufferT buffer, int format, short[] data, int frequency);
    
    void bufferSetData(BufferT buffer, int format, int[] data, int frequency);
    
    void bufferSetData(BufferT buffer, int format, float[] data, int frequency);

    void sourceEnqueueBuffer(SourceT source, BufferT buffer);

    BufferT sourceDequeueBuffer(SourceT source);

    int sourceGetBuffersProcessed(SourceT source);

    int sourceGetBuffersQueued(SourceT source);

    SourceT sourceCreate();

    BufferT bufferCreate();

    void sourceDelete(SourceT source);

    void bufferDelete(BufferT buffer);

    ListenerT listenerGetInstance();

    void listenerSetPosition(ListenerT listener, float x, float y, float z);

    void listenerSetVelocity(ListenerT listener, float x, float y, float z);

    void listenerSetOrientation(ListenerT listener, float atX, float atY, float atZ, float upX, float upY, float upZ);

    void listenerSetGain(ListenerT listener, float gain);

    void sourcePlay(SourceT source);

    void sourceSetDistance(SourceT source, float relative, float rolloff, float max);

    void sourceSetBuffer(SourceT source, BufferT buffer);

    void sourceSetLooping(SourceT source, boolean shouldLoop);

    void distanceModelApply(int model);

    void sourceSetCone(final SourceT source, final float innerAngle, final float outerAngle, final float outerGain);
    
    int sourceGetState(SourceT source);
}
