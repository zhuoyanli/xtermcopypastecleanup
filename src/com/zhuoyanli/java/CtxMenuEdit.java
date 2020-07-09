package com.zhuoyanli.java;

import javax.swing.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class CtxMenuEdit extends JPopupMenu {
    private final static String MENUITEM_TEXT_PASTE = "Paste";
    public final static int MENUITEM_IDX_PASTE = 1;
    private final static String MENUITEM_TEXT_PASTECLEANUPCOPY = "Paste & Clean-Up & Copy";
    public final static int MENUITEM_IDX_PASTECLEANUPCOPY = 2;
    private final static String MENUITEM_TEXT_SELALLCOPY = "Select All & Copy";
    public final static int MENUITEM_IDX_SELALLCOPY = 3;
    /**
     * MenuItem to paste content from system clipboard
     */
    private final JMenuItem miPaste;
    private final JMenuItem miPasteCleanupCopy;
    /**
     * MenuItem to select all content (in associated JTextArea) and copy
     * them to system clipboard
     */
    private final JMenuItem miSelCpAll;
    private final Map<JMenuItem, Integer> mapMiToIndex;

    public CtxMenuEdit() {
        miPaste = new JMenuItem(MENUITEM_TEXT_PASTE);
        this.add(miPaste);
        miPasteCleanupCopy = new JMenuItem(MENUITEM_TEXT_PASTECLEANUPCOPY);
        this.add(miPasteCleanupCopy);
        miSelCpAll = new JMenuItem(MENUITEM_TEXT_SELALLCOPY);
        this.add(miSelCpAll);
        // we create a HashMap to link MenuItem instances to indices
        mapMiToIndex = new HashMap<>();
        mapMiToIndex.put(miPaste, MENUITEM_IDX_PASTE);
        mapMiToIndex.put(miPasteCleanupCopy, MENUITEM_IDX_PASTECLEANUPCOPY);
        mapMiToIndex.put(miSelCpAll, MENUITEM_IDX_SELALLCOPY);
    }

    public int getMenuItemIndex(JMenuItem mi) {
        try {
            return this.mapMiToIndex.get(mi);
        }catch (Exception ex) {
            return -1;
        }
    }

    public void addMiPasteActionListener(ActionListener listener) {
        miPaste.addActionListener(listener);
    }
    public void addMiSelAllActionListener(ActionListener listener) {
        miSelCpAll.addActionListener(listener);
    }
    public void enablePasteMenu(int idxMenuItem, boolean enabled) {
        for(Map.Entry<JMenuItem, Integer> entry: mapMiToIndex.entrySet()) {
            if(entry.getValue() == idxMenuItem) {
                entry.getKey().setEnabled(enabled);
            }
        }
    }

    /**
     * Add {@link ActionListener} instance to all JMenuItem items
     * @param actionListener The {@link ActionListener} instance to be added
     */
    public void setMenuItemActionListener(ActionListener actionListener) {
        setMenuItemActionListener(actionListener, -1);
    }

    /**
     * Add {@link ActionListener} instance to the {@link JMenuItem} item to which <italic>idxMenuItem</italic>
     * is associated. If <italic>idxMenuItem</italic> is -1, <italic>actionListener</italic> will be added to
     * all {@link JMenuItem} items.
     * @param actionListener The {@link ActionListener} instance to be added.
     * @param idxMenuItem The associated index for the {@link JMenuItem} instance to which <italic>actionListener</italic>
     */
    public void setMenuItemActionListener(ActionListener actionListener, int idxMenuItem) {
        for(Map.Entry<JMenuItem, Integer> entry: mapMiToIndex.entrySet()) {
            if(idxMenuItem == -1 || entry.getValue() == idxMenuItem) {
                entry.getKey().addActionListener(actionListener);
            }
        }
    }
}
