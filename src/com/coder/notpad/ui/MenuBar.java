package com.coder.notpad.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.ComponentOrientation;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.im.InputContext;
import java.awt.print.PrinterException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoManager;

import say.swing.JFontChooser;

public class MenuBar extends MouseAdapter {

	Conjuctions saved;
	private JLabel statusLabel, statusResult;
	private String theme = "";
	private JMenuItem newPage, mnitmPrint, SaveAs, Save, mnitmCut, mnitmCopy, toUpperCase, toLowerCase, mnitmRedo,
			mnitmUndo, font, reverseOri;

	public JMenuBar coderMenu(JTextArea textArea, JScrollPane jScrollPane, JTextField textField) {
		statusLabel = new JLabel("Status : ");
		statusLabel.setFont(new Font("Dialog", Font.BOLD, 12));
		statusLabel.setForeground(Color.RED);
		statusResult = new JLabel("");
		statusResult.setForeground(new Color(0, 100, 0));
		statusResult.setFont(new Font("Dialog", Font.BOLD, 13));

		JMenuBar menuBar = new JMenuBar();
		JMenu mnFile = new JMenu("File");
		mnFile.setActionCommand("File");
		mnFile.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File");
			}
		});
		menuBar.add(mnFile);

		newPage = new JMenuItem("New");
		newPage.setActionCommand("New");
		newPage.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/page_icon.png")));
		newPage.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		newPage.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> New");
			}
		});
		newPage.addActionListener(e -> {
			statusResult.setText("Creating a new page...");
			String text = textArea.getText();
			int textLength = text.length();

			if (text.equals("") && textLength <= 0) {
				textArea.setBackground(Color.WHITE);
				textArea.setText("");
				textField.setText("");
				textField.setBackground(Color.WHITE);
			} else {
				int x = JOptionPane.showConfirmDialog(null, "Do you want to save changes for an Untitled?",
						"Closing window", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (x == JOptionPane.YES_OPTION) {
					SaveFile(textArea);
					textArea.setBackground(Color.WHITE);
					textArea.setText("");
					textField.setText("");
					textField.setBackground(Color.WHITE);
				} else if (x == JOptionPane.NO_OPTION) {
					textArea.setBackground(Color.WHITE);
					textArea.setText("");
					textField.setText("");
					textField.setBackground(Color.WHITE);
				}
			}
			statusResult.setText("New page created successfully...");
		});
		mnFile.add(newPage);

		JMenuItem mnitmImportFile = new JMenuItem("Import file");
		mnitmImportFile.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/import.png")));
		mnitmImportFile.setActionCommand("Import file");
		mnitmImportFile.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmImportFile.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Import file");
			}
		});
		mnitmImportFile.addActionListener(e -> {
			statusResult.setText("Importing external file...");
			JFileChooser chooser = new JFileChooser();
			FileNameExtensionFilter textFile = new FileNameExtensionFilter("Text Files(.txt)", "txt", "text");
			FileNameExtensionFilter javaFile = new FileNameExtensionFilter("Java Files(.java)", "java", "java");
			chooser.addChoosableFileFilter(textFile);
			chooser.addChoosableFileFilter(javaFile);
			chooser.setFileFilter(textFile);
			chooser.setAcceptAllFileFilterUsed(false);

			int returnVal = chooser.showOpenDialog(null);

			if (returnVal == JFileChooser.APPROVE_OPTION) {

				File file = chooser.getSelectedFile();

				try {
					BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));

					textArea.read(br, Charset.forName("UTF-8"));

					br.close();
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				statusResult.setText("File imported successfully.");
			}

		});
		mnFile.add(mnitmImportFile);

		mnitmPrint = new JMenuItem("Print");
		mnitmPrint.setActionCommand("Print");
		mnitmPrint.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/print-icon.png")));
		mnitmPrint.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_P,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmPrint.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Print");
			}
		});
		mnitmPrint.addActionListener(e -> {
			statusResult.setText("Printing the page...");

			try {
				boolean compleated = textArea.print();
				if (compleated == true) {
					statusResult.setText("Printing job completed.");
				} else {
					statusResult.setText("Print job cancelled by user!");
				}
			} catch (PrinterException e1) {
				e1.printStackTrace();
			}

		});
		mnFile.add(mnitmPrint);

		Save = new JMenuItem("Save");
		Save.setActionCommand("Save");
		Save.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Save.png")));
		Save.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_D,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		Save.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Save");
			}
		});
		Save.addActionListener(e -> {
			statusResult.setText("Saving file...");
			final OutputStreamWriter fileWriter;
			final BufferedWriter bf;
			String text = textArea.getText();
			if (text != null && !text.equals("")) {
				try {
					String fileName = text.substring(0, 6);
					if (fileName.equals("") && fileName.length() <= 0) {
						fileName = "Untitled";
					}
					
					fileWriter = new OutputStreamWriter(new FileOutputStream(System.getProperty("user.dir") + File.separator + fileName + ".txt"), "UTF-8");
					bf = new BufferedWriter(fileWriter);
					text = textArea.getText();
					bf.write(text);
					bf.flush();
					statusResult.setText("Save file completed successfully.");
					saved = Conjuctions.TRUE;
					bf.close();
				} catch (IOException e1) {
					saved = Conjuctions.FALSE;
					JOptionPane.showInternalMessageDialog(null, e1.getMessage());
				}
			} else {
				JOptionPane.showMessageDialog(null, "Text cannot be null!");

			}
		});

		mnFile.add(Save);

		SaveAs = new JMenuItem("Save As");
		SaveAs.setActionCommand("Save As");
		SaveAs.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Save.png")));
		SaveAs.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		SaveAs.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Save As");
			}
		});
		SaveAs.addActionListener(e -> {
			SaveFile(textArea);
		});

		mnFile.add(SaveAs);

		JMenuItem mnitmExit = new JMenuItem("Exit");
		mnitmExit.setActionCommand("Exit");
		mnitmExit.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/exit.png")));
		mnitmExit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Q,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmExit.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Exit");
			}
		});
		mnitmExit.addActionListener(e -> {
			String text = textArea.getText();
			int textLength = text.length();

			if (text.equals("") && textLength <= 0) {
				System.exit(0);
			} else if (saved == Conjuctions.TRUE) {
				System.exit(0);
			} else {
				int x = JOptionPane.showConfirmDialog(null, "Do you want to save changes for an Untitled?",
						"Closing window", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (x == JOptionPane.YES_OPTION) {
					SaveFile(textArea);
				} else if (x == JOptionPane.NO_OPTION) {
					System.exit(0);
				} else {
					return;
				}
			}
		});
		mnFile.add(mnitmExit);

		JMenu mnEdit = new JMenu("Edit");
		mnEdit.setActionCommand("Edit");
		mnEdit.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit");
			}
		});
		menuBar.add(mnEdit);

		mnitmCut = new JMenuItem(new DefaultEditorKit.CutAction());
		mnitmCut.setText("Cut");
		mnitmCut.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Cut-15.png")));
		mnitmCut.setActionCommand("Cut");
		mnitmCut.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_X,
				(InputEvent.META_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmCut.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit -> Cut");
			}
		});

		mnEdit.add(mnitmCut);

		mnitmCopy = new JMenuItem(new DefaultEditorKit.CopyAction());
		mnitmCopy.setText("Copy");
		mnitmCopy.setActionCommand("Copy");
		mnitmCopy.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Copy-15.png")));
		mnitmCopy.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_C,
				(InputEvent.META_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmCopy.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit -> Copy");
			}
		});

		mnEdit.add(mnitmCopy);

		JMenuItem mnitmPaste = new JMenuItem(new DefaultEditorKit.PasteAction());
		mnitmPaste.setText("Paste");
		mnitmPaste.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Paste-15.png")));
		mnitmPaste.setActionCommand("Paste");
		mnitmPaste.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_V,
				(InputEvent.META_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmPaste.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit -> Paste");
			}
		});

		mnEdit.add(mnitmPaste);

		final UndoManager undoManager = new UndoManager();
		Document document = textArea.getDocument();
		document.addUndoableEditListener(new UndoableEditListener() {

			@Override
			public void undoableEditHappened(UndoableEditEvent e) {
				undoManager.addEdit(e.getEdit());

			}
		});

		mnitmUndo = new JMenuItem("Undo");
		mnitmUndo.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Undo.png")));
		mnitmUndo.setActionCommand("Undo");
		mnitmUndo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_Z,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmUndo.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit -> Undo");
			}
		});
		mnitmUndo.addActionListener(e -> {
			statusResult.setText("Undo...");

			try {
				if (undoManager.canUndo()) {
					undoManager.undo();
				}
			} catch (CannotUndoException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}

		});
		mnEdit.add(mnitmUndo);

		mnitmRedo = new JMenuItem("Redo");
		mnitmRedo.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/refresh.png")));
		mnitmRedo.setActionCommand("Redo");
		mnitmRedo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		mnitmRedo.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Edit -> Redo");
			}
		});
		mnitmRedo.addActionListener(e -> {
			statusResult.setText("Redo..");
			try {
				if (undoManager.canRedo()) {
					undoManager.redo();
				}
			} catch (CannotRedoException ex) {
				JOptionPane.showMessageDialog(null, ex.getMessage());
			}

		});
		mnEdit.add(mnitmRedo);

		JMenu format = new JMenu("Format");
		format.setActionCommand("Format");
		format.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Format");
				InputContext context = InputContext.getInstance();
				String dedectKeyLang = context.getLocale().toString();
				if(dedectKeyLang.indexOf("-17921") != -1) {
					font.setEnabled(false);
					textArea.setFont(UIManager.getDefaults().getFont("TextArea.font"));

				}else {
					font.setEnabled(true);
				}
			}
		});
		menuBar.add(format);
		
		textArea.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				InputContext context = InputContext.getInstance();
				String dedectKeyLang = context.getLocale().toString();
				if(dedectKeyLang.indexOf("-17921") != -1) {
					jScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
					textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				}else {
					
					jScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
					textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				}
				super.mousePressed(e);
			}
		});
		font = new JMenuItem("Font");
		font.setActionCommand("Font");
		font.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/fonts.png")));
		font.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		font.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> File -> Font");
			}
		});
		font.addActionListener(e -> {
				statusResult.setText("Choosing text font...");
				JFontChooser fontChooser = new JFontChooser();
				int result = fontChooser.showDialog(null);
				if (result == JFontChooser.OK_OPTION) {
					textArea.setFont(fontChooser.getSelectedFont());
				}
				statusResult.setText("Choosed font is : " + fontChooser.getSelectedFontFamily());
			
		});
		format.add(font);

		toUpperCase = new JMenuItem("To UpperCase");
		toUpperCase.setActionCommand("To UpperCase");
		toUpperCase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_U,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		toUpperCase.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/uppercase.png")));
		toUpperCase.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Format -> To UpperCase");
			}
		});
		toUpperCase.addActionListener(e -> {
			StringBuffer sb = new StringBuffer();
			String allText = textArea.getText();
			String selectedText = textArea.getSelectedText();
			sb.append(allText.replace(selectedText, selectedText.toUpperCase()));
			textArea.setText(sb.toString());
		});
		format.add(toUpperCase);

		toLowerCase = new JMenuItem("To LowerCase");
		toLowerCase.setActionCommand("To UpperCase");
		toLowerCase.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_L,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		toLowerCase.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/lowercase.png")));
		toLowerCase.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Format -> To LowerCase");
			}
		});
		toLowerCase.addActionListener(e -> {
			StringBuffer sb = new StringBuffer();
			String allText = textArea.getText();
			String selectedText = textArea.getSelectedText();
			sb.append(allText.replace(selectedText, selectedText.toLowerCase()));
			textArea.setText(sb.toString());
		});
		format.add(toLowerCase);

		JMenuItem orientation = new JMenuItem();
		orientation.setText("From right to left");
		orientation.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/righttoleft.png")));
		orientation.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_H,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		orientation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jScrollPane.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
				textArea.setComponentOrientation(ComponentOrientation.RIGHT_TO_LEFT);
			}
		});
		format.add(orientation);

		reverseOri = new JMenuItem();
		reverseOri.setText("From left to right");
		reverseOri.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/lefttoright.png")));
		reverseOri.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_G,
				(InputEvent.SHIFT_MASK) | (Toolkit.getDefaultToolkit().getMenuShortcutKeyMask())));
		reverseOri.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jScrollPane.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
				textArea.setComponentOrientation(ComponentOrientation.LEFT_TO_RIGHT);
			}
		});
		format.add(reverseOri);

		JMenu mnThemes = new JMenu("Themes");
		mnThemes.setActionCommand("Themes");
		mnThemes.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes");
			}
		});
		menuBar.add(mnThemes);

		JMenuItem defaultTheme = new JMenuItem("Default");
		defaultTheme.setActionCommand("Default");
		defaultTheme.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes -> Default");
			}
		});
		defaultTheme.addActionListener(e -> {
			theme = defaultTheme.getActionCommand();
			ChangeTheme(theme);
		});
		mnThemes.add(defaultTheme);

		JMenuItem mnitmAero = new JMenuItem("Aero");
		mnitmAero.setActionCommand("Aero");
		mnitmAero.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes -> Aero");
			}
		});
		mnitmAero.addActionListener(e -> {
			theme = mnitmAero.getActionCommand();
			ChangeTheme(theme);
		});
		mnThemes.add(mnitmAero);

		JMenuItem mnitmBernstain = new JMenuItem("Bernstein");
		mnitmBernstain.setActionCommand("Bernstein");
		mnitmBernstain.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes -> Bernstain");
			}
		});
		mnitmBernstain.addActionListener(e -> {
			theme = mnitmBernstain.getActionCommand();
			ChangeTheme(theme);
		});
		mnThemes.add(mnitmBernstain);

		JMenuItem mnitmMint = new JMenuItem("Mint");
		mnitmMint.setActionCommand("Mint");
		mnitmMint.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes -> Mint");
			}
		});
		mnitmMint.addActionListener(e -> {
			theme = mnitmMint.getActionCommand();
			ChangeTheme(theme);
		});
		mnThemes.add(mnitmMint);

		JMenuItem mnitmMcwin = new JMenuItem("McWin");
		mnitmMcwin.setActionCommand("Mcwin");
		mnitmMcwin.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> Themes -> McWin");
			}
		});
		mnitmMcwin.addActionListener(e -> {
			theme = mnitmMcwin.getActionCommand();
			ChangeTheme(theme);
		});
		mnThemes.add(mnitmMcwin);

		JMenu mnAbout = new JMenu("About");
		mnAbout.setActionCommand("About");
		mnAbout.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> About");
			}
		});
		menuBar.add(mnAbout);

		JMenuItem mnitmDeveloperInfo = new JMenuItem("Developer info");
		mnitmDeveloperInfo.setIcon(new ImageIcon(MenuBar.class.getResource("/Icons/Info-icon.png")));
		mnitmDeveloperInfo.setActionCommand("Developer info");
		mnitmDeveloperInfo.addMouseListener(new MouseAdapter() {
			public void mouseEntered(MouseEvent e) {
				statusResult.setText("Menubar -> About -> Developer info");
			}
		});
		mnitmDeveloperInfo.addActionListener(e -> {
			
			Properties properties = new Properties();
			
			try {
				properties.load(new InputStreamReader(getClass().
						getResourceAsStream("/com/coder/notpad/util/info.properties"), 
																Charset.defaultCharset()));
				
			} catch (IOException e1) {
				System.out.println("Cannot load properties file!");
			}
			
			String message =  properties.getProperty("info.message");
			
			JOptionPane.showMessageDialog(null, message,
						"Info", JOptionPane.INFORMATION_MESSAGE);
		});
		mnAbout.add(mnitmDeveloperInfo);

		checkAllComponents(textArea);

		JSeparator separator = new JSeparator();
		separator.setForeground(new Color(105, 105, 105));
		separator.setMaximumSize(new Dimension(10, 25));
		separator.setAlignmentX(Component.LEFT_ALIGNMENT);
		separator.setAutoscrolls(true);
		separator.setPreferredSize(new Dimension(0, 2));
		separator.setOrientation(SwingConstants.VERTICAL);
		menuBar.add(separator);

		menuBar.add(statusLabel);
		menuBar.add(statusResult);
		return menuBar;

	}

	private void ChangeTheme(String theme) {
		for (Frame frame : MainFrame.getFrames()) {
			try {
				if (theme.equalsIgnoreCase("mint")) {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mint.MintLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);

				} else if (theme.equalsIgnoreCase("mcwin")) {
					UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);

				} else if (theme.equalsIgnoreCase("bernstein")) {
					UIManager.setLookAndFeel("com.jtattoo.plaf.bernstein.BernsteinLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);

				} else if (theme.equalsIgnoreCase("Aero")) {
					UIManager.setLookAndFeel("com.jtattoo.plaf.aero.AeroLookAndFeel");
					SwingUtilities.updateComponentTreeUI(frame);
				} else if (theme.equalsIgnoreCase("Default")) {
					for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
						if ("Nimbus".equals(info.getName())) {
							UIManager.setLookAndFeel(info.getClassName());
							SwingUtilities.updateComponentTreeUI(frame);
						}
					}
				}
			} catch (ClassNotFoundException | InstantiationException | IllegalAccessException
					| UnsupportedLookAndFeelException e) {
				JOptionPane.showMessageDialog(null, e.getMessage(), "Error!", JOptionPane.ERROR_MESSAGE);
			}
		}
	}

	public void checkAllComponents(JTextArea textArea) {
		String text = textArea.getText();
		int textLength = text.length();

		if (text.equals("") && textLength <= 0) {
			mnitmCopy.setEnabled(false);
			mnitmCut.setEnabled(false);
			mnitmPrint.setEnabled(false);
			toUpperCase.setEnabled(false);
			toLowerCase.setEnabled(false);
			SaveAs.setEnabled(false);
			mnitmRedo.setEnabled(false);
			mnitmUndo.setEnabled(false);
		}
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				mnitmCopy.setEnabled(true);
				mnitmCut.setEnabled(true);
				mnitmPrint.setEnabled(true);
				toUpperCase.setEnabled(true);
				toLowerCase.setEnabled(true);
				SaveAs.setEnabled(true);
				mnitmRedo.setEnabled(true);
				mnitmUndo.setEnabled(true);
			}
		});
	}

	public void SaveFile(JTextArea textArea) {

		JFileChooser chooser = new JFileChooser();
		FileNameExtensionFilter filter = new FileNameExtensionFilter("Text Files(.txt)", "txt", "text");
		chooser.setFileFilter(filter);
		chooser.setSelectedFile(new File("Untitled.txt"));
		chooser.setDialogTitle("Specify a directory to save the file.");
		int result = chooser.showSaveDialog(null);
		statusResult.setText("Saving file...");
		if (result == JFileChooser.APPROVE_OPTION) {
			File fileToSave = chooser.getSelectedFile();

			OutputStreamWriter out;
			final BufferedWriter bf;
			try {
				File file = new File(fileToSave.getAbsolutePath());
				out = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
				bf = new BufferedWriter(out);
				String text = textArea.getText();
				if (text != null && !text.equals("")) {
					bf.write(text);
					bf.flush();
					saved = Conjuctions.TRUE;
				} else {
					JOptionPane.showMessageDialog(new Frame(), "Text cannot be null!");
					saved = Conjuctions.FALSE;
				}

				bf.close();
				statusResult.setText("Save as file completed successfully.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} else {
			statusResult.setText("Save as job cancelled by the user!");
		}
	}
}
