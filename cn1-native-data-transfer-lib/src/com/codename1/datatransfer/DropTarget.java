/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.codename1.datatransfer;

import com.codename1.io.Log;
import com.codename1.system.NativeLookup;
import com.codename1.ui.Display;
import com.codename1.ui.events.ActionEvent;
import com.codename1.ui.events.ActionListener;
import java.util.ArrayList;

/**
 * Provides drag and drop support for dragging files from the native environment (e.g. Desktop) into the app.
 * 
 * <p>The API is designed to be similar to the <a href="https://www.codenameone.com/javadoc/com/codename1/ui/Display.html#openGallery-com.codename1.ui.events.ActionListener-int-">Display.openGallery() API</a>.</p>
 * 
 * <h3>Usage</h3>
 * <p>See <a href="https://github.com/shannah/cn1-native-data-transfer/blob/master/DNDDemo/src/com/codename1/demos/dnd/DNDDemo.java">DNDDemo</a></p>
 * @author shannah
 */
public class DropTarget {
    
    /**
     * Response listener that is called.  The {@code source} of the {@code ActionEvent} will be a string pointing to a temporary 
     * file on the file system.
     */
    private ActionListener response;
    
    /**
     * Static list of all active drop targets in the app.
     */
    private static ArrayList<DropTarget> targets = new ArrayList<DropTarget>();
    
    /**
     * Native peer.
     */
    private static NativeDragAndDrop peer;
    
    /**
     * The type of the target.  One of {@code Display.GALLERY_IMAGE}, {@code Display.GALLERY_VIDEO}, or {@code Display.GALLERY_ALL}.
     * The specifies what kinds of files will be accepted by this drop target.
     */
    private int type;
    
    private DropTarget(ActionListener response, int type) {
        this.response = response;
        this.type = type;
        try {
            peer = (NativeDragAndDrop)NativeLookup.create(NativeDragAndDrop.class);
        } catch (Throwable t) {
            Log.e(t);
        }
    }
    
    /**
     * Registers a listener to accept drop events onto the app.
     * @param response Listener called when files (of the allowed type) is dropped on the app.  The {@code source} will be a {@code String} with the path to a 
     * temporary file in FileSystemStorage.  The {@code x} and {@code y} coordinates are the absolute coordinates within the application window where the drop occurred.
     * @param type The types of files that are accepted by this drop target.  One of {@code Display.GALLERY_IMAGE}, {@code Display.GALLERY_VIDEO}, and {@code Display.GALLERY_ALL}.
     * @return The {@link DropTarget} object that was created.  You can use this to manipulate the target later.
     */
    public static DropTarget create(ActionListener response, int type) {
        DropTarget target = new DropTarget(response, type);
        target.start();
        return target;
    }
    
    /**
     * Restarts drag and drop after if has been stopped.
     */
    public void start() {
        if (!targets.contains(this)) {
            targets.add(this);
            if (targets.size() == 1) {
                peer.startGlobalDropListener();
            }
        }
    }
    
    /**
     * Disables the drop target so that it is no longer accepting file drops.
     */
    public void stop() {
        targets.remove(this);
        if (targets.isEmpty()) {
            peer.stopGlobalDropListener();
        }
    }
    
    
    /**
     * Called from native code to fire the actual drop event.
     * @param x
     * @param y
     * @param filePath 
     */
    public static void fireDropEvent(final int x, final int y, final String filePath) {
        Display.getInstance().callSerially(new Runnable() {

            public void run() {
                for (DropTarget target : targets) {
                    target.response.actionPerformed(new ActionEvent(filePath, x, y));
                }
            }
            
        });
    }
    
    /**
     * Check whether drag and drop is supported on the current platform.
     * @return 
     */
    public static boolean isSupported() {
        try {
            NativeDragAndDrop dnd = (NativeDragAndDrop)NativeLookup.create(NativeDragAndDrop.class);
            return dnd.isSupported();
        } catch (Throwable t) {
            return false;
        }
    }
    
    /**
     * Used by native code.  Checks to see if a particular type is currently being accepted by ANY active DropTarget.
     * @param type One of {@code Display.GALLERY_IMAGE}, {@code Display.GALLERY_VIDEO}, {@code Display.GALLERY_ALL}.
     * @return True if there are ANY active drop targets accepting the specified type of file.
     */
    public static boolean isTypeAccepted(int type) {
        for (DropTarget target : targets) {
            if (target.type == Display.GALLERY_ALL || target.type == type ) {
                return true;
            }
        }
        return false;
    }
    
    /**
     * Used by native code.  Checks to see if there are any active drop targets that will accept the given mimetype.
     * @param mimetype The mimetype to check (e.g. image/jpeg).
     * @return True if there exists at least one active drop target that will accept files of the provided mimetype.
     */
    public static boolean isMimetypeAccepted(String mimetype) {
        if (mimetype.indexOf("image/") == 0 && isTypeAccepted(Display.GALLERY_IMAGE)) {
            return true;
        } else if (mimetype.indexOf("video/") == 0 && isTypeAccepted(Display.GALLERY_VIDEO)) {
            return true;
        } else if (isTypeAccepted(Display.GALLERY_ALL)) {
            return true;
        } else {
            return false;
        }
    }
    
}
