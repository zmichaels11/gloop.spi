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
 */
public interface Driver <DeviceT extends Device, BufferT extends Buffer, ListenerT extends Listener, SourceT extends Source> {
    DeviceT deviceCreate();
    void deviceDelete(DeviceT device);    
    void sourceSetPitch(SourceT source, float pitch);
    void sourceSetGain(SourceT source, float gain);
    void sourceSetPosition(SourceT source, float x, float y, float z);
    void sourceSetVelocity(SourceT source, float x, float y, float z);
    void sourceSetDirection(SourceT source, float x, float y, float z);    
    void bufferSetData(BufferT buffer, int format, ByteBuffer data, int frequency);
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
}
