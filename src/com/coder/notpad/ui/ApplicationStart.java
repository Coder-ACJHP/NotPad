package com.coder.notpad.ui;

import java.awt.EventQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class ApplicationStart {

	public static void main(String[] args) {
		
			for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					try {
						UIManager.setLookAndFeel(info.getClassName());
					} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
							| UnsupportedLookAndFeelException e) {
						Logger.getLogger(MainFrame.class.getName()).log(Level.WARNING, e.getMessage(), e);
					}
				}
			}
			EventQueue.invokeLater(new Runnable() {
				public void run() {
					try {
						MainFrame frame = new MainFrame();
						frame.setVisible(true);
						frame.pack();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
		}
	
}
