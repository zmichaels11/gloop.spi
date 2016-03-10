/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.longlinkislong.gloop.spi;

/**
 *
 * @author zmichaels
 */
public final class WindowCallbacks {
    private WindowCallbacks() {}
    
    public static interface KeyCallback {}
    
    public static interface MouseButtonCallback {}
    
    public static interface CursorEnterCallback {}
    
    public static interface CursorPosCallback {}
    
    public static interface ScrollCallback {}
    
    public static interface CharCallback {}
}
