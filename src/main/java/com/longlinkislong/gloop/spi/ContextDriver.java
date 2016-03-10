/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.spi;

/**
 *
 * @author zmichaels
 * @param <WindowT>
 * @param <CursorT>
 * @param <CommandQueueT>
 * @param <CommandBufferT>
 */
public interface ContextDriver<
        WindowT extends Window, CursorT extends Cursor,
        CommandQueueT extends CommandQueue, CommandBufferT extends CommandBuffer> {

    WindowT windowOpen(String title, int width, int height);

    void windowClose(WindowT window);

    CommandQueueT windowGetCommandQueue(WindowT window);

    void submitCommandBuffer(CommandQueueT queue, CommandBufferT cmd);
    
    void scheduleCommandBuffer(CommandQueueT queue, CommandBufferT cmd);

    void windowResetCreationHints();

    void windowSetCreationHint(int hint, int value);
    
    int windowGetFramebufferWidth(WindowT window);
    
    int windowGetFramebufferHeight(WindowT window);
    
    void windowShow(WindowT window);
    
    void windowHide(WindowT window);
 
    void windowSetSize(WindowT window, int width, int height);
    
    void windowSetCursor(WindowT window, CursorT cursor);
    
    int windowGetXPosition(WindowT window);
    
    int windowGetYPosition(WindowT window);
    
    int windowGetWidth(WindowT window);
    
    int windowGetHeight(WindowT window);
    
    int windowGetFrameLeft(WindowT window);
    
    int windowGetFrameTop(WindowT window);
    
    int windowGetFrameRight(WindowT window);
    
    int windowGetFrameBottom(WindowT window);
}
