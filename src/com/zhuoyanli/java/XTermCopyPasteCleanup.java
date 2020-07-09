package com.zhuoyanli.java;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

public class XTermCopyPasteCleanup {
    private JPanel paneMain;
    private JButton btnCleanup;
    private JTextArea taInput;
    private JPanel paneTextInput;
    private JPanel paneBtn;
    private JLabel lblInput;
    private JTextArea taOutput;
    private JLabel lblOutput;
    private JPanel paneOptions;
    private JCheckBox chkboxRmTrailingSlashes;
    private final CtxMenuEdit popupMenuInput;
    private final CtxMenuEditActionListener ctxMenuInputActionListener;
    private final CtxMenuEditActionListener ctxMenuOutputActionListener;
    private final CtxMenuEdit popupMenuOutput;

    /**
     * ActionListener for MenuItems in {@link CtxMenuEdit}. This
     * ActionListener should be created with instances of {@link JTextArea}
     * and {@link CtxMenuEdit}.
     */
    class CtxMenuEditActionListener implements ActionListener {
        private final JTextArea ta;
        private final JTextArea taOutput;
        private final CtxMenuEdit ctxMenuEdit;
        public CtxMenuEditActionListener(JTextArea ta, CtxMenuEdit ctxMenuEdit) {
            this(ta, ctxMenuEdit, null);
        }
        public CtxMenuEditActionListener(JTextArea ta, CtxMenuEdit ctxMenuEdit, JTextArea taOutput) {
            this.ctxMenuEdit = ctxMenuEdit;
            this.ta = ta;
            this.taOutput = taOutput;
            if(taOutput == null) {
                ctxMenuEdit.enablePasteMenu(CtxMenuEdit.MENUITEM_IDX_PASTECLEANUPCOPY, false);
            }
        }

        @Override
        /*
         * This method checks which MenuItem has been clicked in the
         * associated {@link CtxMenuEdit} instance, then perform action
         * on the associated {@link JTextArea} instance accordingly.
         */
        public void actionPerformed(ActionEvent e) {
            JMenuItem mi = (JMenuItem)e.getSource();
            int idxMi = ctxMenuEdit.getMenuItemIndex(mi);
            if(idxMi == CtxMenuEdit.MENUITEM_IDX_PASTE) {
                // the 'paste' MenuItem
                // Paste content from Clipboard into TA
                if(ta.isEditable()) {
                    ta.paste();
                }
            }else if(idxMi == CtxMenuEdit.MENUITEM_IDX_PASTECLEANUPCOPY) {
                // the 'paste&cleanup&copy' MenuItem
                // Paste content from Clipboard into TA
                if(ta.isEditable()) {
                    ta.paste();
                    // Clean-up
                    cleanUpInputTAtoOutputTA(taInput, taOutput);
                    taOutput.selectAll();
                    taOutput.copy();
                }
            }else if(idxMi == CtxMenuEdit.MENUITEM_IDX_SELALLCOPY) {
                // index 1 is the 'Select All and Copy' MenuItem
                ta.selectAll();
                ta.copy();
            }
        }
    }

    /**
     * Method to <italic>clean-up</italic> text in <italic>input</italic> {@link JTextArea}.
     *
     * The <italic>Clean-up</italic> operation here refers to remove all the line-breaks except for the
     * last one if existing at the end of the text, so that the text in <italic>input</italic> {@link JTextArea}
     * becomes one-line.
     *
     * The purpose of this <italic>clean-up</italic> is to account for the use-cases where one-long-line text copied
     * from Terminal console, where long text got wrapped into multiple lines to display, becomes multi-line text
     * and stay as that when pasted back to Terminal window, even though users expect to paste one-long-line text.
     *
     * One special treatment the method does is to detect if pasted text possibly come from emacs where
     * one-long-line text are wrapped with one backward slash inserted at the end of each wrapped line.
     *
     *
     * @param taInput {@link JTextArea} instance containing text to be cleaned up
     * @param taOutput {@link JTextArea} instance that would contain the text that have been cleaned-up.
     */
    private void cleanUpInputTAtoOutputTA(JTextArea taInput, JTextArea taOutput) {
        String txtInput = taInput.getText();
        String[] lines = txtInput.split("\n");
        boolean allLinesBeforeLastEndsWithSlash = true;
        for(int idx=0; idx<lines.length-1; idx++) {
            String line = lines[idx];
            if(!line.endsWith("\\")) {
                allLinesBeforeLastEndsWithSlash = false;
                break;
            }
        }
        if(allLinesBeforeLastEndsWithSlash) chkboxRmTrailingSlashes.setSelected(true);
        StringBuilder sb = new StringBuilder();
        boolean rmTrailingSlashes = chkboxRmTrailingSlashes.isSelected();
        for(String line: lines) {
            if(!rmTrailingSlashes) {
                sb.append(line);
            }else {
                if(line.endsWith("\\")) {
                    sb.append(line.substring(0, line.length()-2));
                } else {
                    sb.append(line);
                }
            }
        }
        taOutput.setText(sb.toString());
    }

    public XTermCopyPasteCleanup() {
        // 'Click' action listener to 'Cleanup' button
        btnCleanup.addActionListener((ActionEvent e) -> {
            cleanUpInputTAtoOutputTA(taInput, taOutput);
            taOutput.selectAll();
            taOutput.copy();
        });

        // Create the context (Pop-up) menu for input textarea
        popupMenuInput = new CtxMenuEdit();
        // create the MenuItem action listener
        ctxMenuInputActionListener = new CtxMenuEditActionListener(taInput, popupMenuInput, taOutput);
        // associate the action listener to MenuItem instances
        popupMenuInput.setMenuItemActionListener(ctxMenuInputActionListener);

        // Similar action, only for output textarea
        popupMenuOutput = new CtxMenuEdit();
        popupMenuOutput.enablePasteMenu(CtxMenuEdit.MENUITEM_IDX_PASTE, false);
        popupMenuOutput.enablePasteMenu(CtxMenuEdit.MENUITEM_IDX_PASTECLEANUPCOPY, false);
        ctxMenuOutputActionListener = new CtxMenuEditActionListener(taOutput, popupMenuOutput);
        popupMenuOutput.setMenuItemActionListener(ctxMenuOutputActionListener);

        // To pop-up the context menu, we need to add the event handler for right-clicks
        taInput.addMouseListener(new PopupMenuRightClickListener(popupMenuInput));
        taOutput.addMouseListener(new PopupMenuRightClickListener(popupMenuOutput));
    }

    public static void main(String[] args) {
        XTermCopyPasteCleanup app = new XTermCopyPasteCleanup();
        app.lblInput.setHorizontalAlignment(JLabel.CENTER);
        JFrame guiFrame = new JFrame("XTermCopyPasteCleanup");
        guiFrame.setContentPane(app.paneMain);
        guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        guiFrame.pack();
        guiFrame.setVisible(true);
    }

}
