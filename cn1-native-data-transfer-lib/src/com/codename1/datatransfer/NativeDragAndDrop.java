/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.datatransfer;

import com.codename1.system.NativeInterface;

/**
 *
 * @author shannah
 */
public interface NativeDragAndDrop extends NativeInterface {
    
    /**
     * Starts the native drop listener on the application's canvas
     */
    public void startGlobalDropListener();
    
    /**
     * Stops teh native drop listener on the application's canvas.
     */
    public void stopGlobalDropListener();
    
    
}
