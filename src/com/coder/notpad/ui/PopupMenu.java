package com.coder.notpad.ui;

import java.awt.ComponentOrientation;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.ImageIcon;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.text.DefaultEditorKit;

public class PopupMenu extends MouseAdapter {

	public void getPopupMenu(JTextArea textArea, JScrollPane jScrollPane) {
		JPopupMenu popupMenu = new JPopupMenu();
		JMenuItem copy = new JMenuItem(new DefaultEditorKit.CopyAction());
		copy.setText("Copy");
		copy.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Copy-15.png")));
		copy.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_C, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		popupMenu.add(copy);

		JMenuItem paste = new JMenuItem(new DefaultEditorKit.PasteAction());
		paste.setText("Paste");
		paste.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Paste-15.png")));
		paste.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_V, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		popupMenu.add(paste);

		JMenuItem cut = new JMenuItem(new DefaultEditorKit.CutAction());
		cut.setText("Cut");
		cut.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Cut-15.png")));
		cut.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_X, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		popupMenu.add(cut);

		JMenuItem orientation = new JMenuItem();
		orientation.setText("From right to left");
		orientation.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/righttoleft.png")));
		orientation.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_H, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		orientation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
		});
		popupMenu.add(orientation);
		
		JMenuItem reverseOri = new JMenuItem();
		reverseOri.setText("From left to right");
		reverseOri.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/lefttoright.png")));
		reverseOri.setAccelerator(
				KeyStroke.getKeyStroke(KeyEvent.VK_G, (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		reverseOri.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		});
		popupMenu.add(reverseOri);
	
		textArea.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {

				if (e.isPopupTrigger()) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());

				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					popupMenu.show(e.getComponent(), e.getX(), e.getY());
				}
			}

		});
	}
}
