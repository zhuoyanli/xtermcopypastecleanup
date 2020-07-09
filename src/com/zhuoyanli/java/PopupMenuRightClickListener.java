package com.zhuoyanli.java;


import javax.swing.JPopupMenu;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

/**
 * Event handler to pop-up associated {@link JPopupMenu} instance
 * upon right-click events.
 */
public class PopupMenuRightClickListener extends MouseAdapter {
    private final JPopupMenu popupMenu;

    public PopupMenuRightClickListener(JPopupMenu popupMenu) {
        this.popupMenu = popupMenu;
    }

    public void mousePressed(MouseEvent e) {
        if (e.isPopupTrigger())
            popupCtxMenuEdit(e);
    }

    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger())
            popupCtxMenuEdit(e);
    }

    private void popupCtxMenuEdit(MouseEvent e) {
         popupMenu.show(e.getComponent(), e.getX(), e.getY());
    }
}