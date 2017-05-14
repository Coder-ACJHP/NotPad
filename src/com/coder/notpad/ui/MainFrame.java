package com.coder.notpad.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Highlighter;

import com.apple.eawt.Application;

public class MainFrame extends JFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private static final JTextArea textArea = new JTextArea();
	private JPanel panel;
	private JTextField searchField;
	private JSeparator separator;
	private JCheckBox LwChkbox;
	private JSeparator separator_1;
	private JCheckBox chckbxAddDate;
	private JSeparator separator_2;
	private JButton btnBackgroundColor;
	private JLabel lblNewLabel;
	final MenuBar menuBar;
	private JButton btnMyNotes;
	private ShowFavoriteFiles f = new ShowFavoriteFiles();
	private static MainFrame frame;

	/**
	 * Create the frame.
	 */
	public MainFrame() {

		setTitle("Coder NotePad");
		setIconImage(Toolkit.getDefaultToolkit().getImage(MainFrame.class.getResource("/Icons/icon2.png")));
		SwingUtilities.updateComponentTreeUI(this);
		String version = System.getProperty("os.name").toLowerCase();
		if (version.indexOf("window") != -1) {
			setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/Icons/icon2.png")));
		} else {
			Application.getApplication()
					.setDockIconImage(new ImageIcon(getClass().getResource("/Icons/icon2.png")).getImage());
		}

		menuBar = new MenuBar();

		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 719);
		setMinimumSize(new Dimension(600, 719));
		setLocationRelativeTo(null);
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				String text = textArea.getText();
				int textLength = text.length();

				if (text.equals("") && textLength <= 0) {
					System.exit(0);
				} else if (menuBar.saved == Conjuctions.TRUE) {
					System.exit(0);
				} else {
					int x = JOptionPane.showConfirmDialog(null, "Do you want to save changes for an Untitled?",
							"Closing window", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);
					if (x == JOptionPane.YES_OPTION) {
						menuBar.SaveFile(textArea);
					} else if (x == JOptionPane.NO_OPTION) {
						System.exit(0);
					} else {
						setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
					}
				}
			}
		});
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);

		panel = new JPanel();
		contentPane.add(panel, BorderLayout.NORTH);
		panel.setAutoscrolls(true);
		panel.setBorder(new TitledBorder(null, "ToolBox", TitledBorder.LEADING, TitledBorder.TOP, null, null));

		searchField = new JTextField();
		searchField.setInheritsPopupMenu(true);
		searchField.setFocusCycleRoot(true);
		searchField.getDocument().addDocumentListener(new DocumentListener() {

			@Override
			public void removeUpdate(DocumentEvent e) {
				if (searchField.getText().equals("")) {
					searchField.setBackground(Color.PINK);
					Highlighter hilite = textArea.getHighlighter();
					Highlighter.Highlight[] hilites = hilite.getHighlights();

					for (int i1 = 0; i1 < hilites.length; i1++) {
						if (hilites[i1].getPainter() instanceof MyHighlightPainter) {
							hilite.removeHighlight(hilites[i1]);
						}
					}
				}
			}

			@Override
			public void insertUpdate(DocumentEvent e) {
				Highlighter focus = textArea.getHighlighter();
				String word = searchField.getText();
				String text = textArea.getText();
				MyHighlightPainter painter = new MyHighlightPainter(Color.YELLOW);
				try {
					searchField.setBackground(Color.WHITE);
					for (int i = 0; i + word.length() <= text.length(); i++) {

						String match = textArea.getText(i, word.length());
						if (word.equalsIgnoreCase(match)) {
							focus.addHighlight(i, i + word.length(), painter);
						}

					}
				} catch (BadLocationException e1) {
					e1.printStackTrace();
				}
			}

			@Override
			public void changedUpdate(DocumentEvent e) {
			}
		});
		
				lblNewLabel = new JLabel("");
				panel.add(lblNewLabel);
				lblNewLabel.setAutoscrolls(true);
				lblNewLabel.setIcon(new ImageIcon(MainFrame.class.getResource("/Icons/file_search.png")));
		searchField.setColumns(10);
		panel.add(searchField);

		separator = new JSeparator();
		separator.setOrientation(SwingConstants.VERTICAL);
		separator.setFocusable(true);
		separator.setForeground(Color.GRAY);
		separator.setAutoscrolls(true);
		separator.setOpaque(true);
		separator.setPreferredSize(new Dimension(10, 20));
		panel.add(separator);

		LwChkbox = new JCheckBox("Line wrap");
		LwChkbox.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (LwChkbox.isSelected() == true) {
					textArea.setLineWrap(true);
					textArea.setWrapStyleWord(true);
				} else {
					textArea.setLineWrap(false);
					textArea.setWrapStyleWord(false);
				}

			}
		});
		panel.add(LwChkbox);

		separator_1 = new JSeparator();
		separator_1.setOpaque(true);
		separator_1.setFocusable(true);
		separator_1.setForeground(Color.GRAY);
		separator_1.setAutoscrolls(true);
		separator_1.setOrientation(SwingConstants.VERTICAL);
		separator_1.setPreferredSize(new Dimension(10, 20));
		panel.add(separator_1);

		chckbxAddDate = new JCheckBox("Date & Time");
		chckbxAddDate.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (chckbxAddDate.isSelected()) {
					StringBuilder sb = new StringBuilder(textArea.getText());
					GregorianCalendar calendar = new GregorianCalendar();
					SimpleDateFormat format = new SimpleDateFormat("HH:mm a dd.MM.YYYY");
					String date = format.format(calendar.getTime());
					sb.append(" " + date);
					textArea.setText(sb.toString());
				}
			}
		});
		panel.add(chckbxAddDate);

		separator_2 = new JSeparator();
		separator_2.setPreferredSize(new Dimension(10, 20));
		separator_2.setOrientation(SwingConstants.VERTICAL);
		separator_2.setOpaque(true);
		separator_2.setForeground(Color.GRAY);
		separator_2.setFocusable(true);
		separator_2.setAutoscrolls(true);
		panel.add(separator_2);

		btnBackgroundColor = new JButton("Colors");
		btnBackgroundColor.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color initialColor = Color.WHITE;
				Color myColor = JColorChooser.showDialog(null, "Choose Background Color", initialColor);
				if (myColor.getRGB() == Color.BLACK.getRGB()) {

					textArea.setCaretColor(Color.WHITE);
					textArea.setForeground(Color.WHITE);
				} else {
					textArea.setCaretColor(Color.BLACK);
					textArea.setForeground(Color.BLACK);
				}
				textArea.setBackground(myColor);
			}
		});
		panel.add(btnBackgroundColor);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setOpaque(true);
		scrollPane.setBorder(
				new TitledBorder(null, "Note It", TitledBorder.LEADING, TitledBorder.TOP, null, Color.BLACK));
		scrollPane.setFocusTraversalPolicyProvider(true);
		scrollPane.setFocusCycleRoot(true);
		scrollPane.setAutoscrolls(true);
		contentPane.add(scrollPane, BorderLayout.CENTER);
		Border border = BorderFactory.createLoweredSoftBevelBorder();
		textArea.setBorder(BorderFactory.createCompoundBorder(border,
						BorderFactory.createEmptyBorder(10, 10, 10, 10)));
		textArea.setInheritsPopupMenu(true);
		textArea.setFocusCycleRoot(true);
		textArea.setIgnoreRepaint(true);
		textArea.addKeyListener(new KeyAdapter() {
			public void keyTyped(KeyEvent e) {
				searchField.setBackground(Color.WHITE);
			}
		});

		scrollPane.setViewportView(textArea);

		setJMenuBar(menuBar.coderMenu(textArea, scrollPane, searchField));

		btnMyNotes = new JButton("My Notes");
		btnMyNotes.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				String buttonName = btnMyNotes.getText();
				if (buttonName.equalsIgnoreCase("My Notes")) {
					btnMyNotes.setText("Hide");
					btnMyNotes.setBackground(new Color(43, 166, 242));
					f.getFavoriteFiles();
					frame.getContentPane().add(f.getFavoriteFiles(), BorderLayout.EAST);
					frame.revalidate();
					frame.repaint();
				}

				if (buttonName.equalsIgnoreCase("Hide")) {
					btnMyNotes.setText("My Notes");
					btnMyNotes.setBackground(null);
					f.setVisibility();
					frame.revalidate();
					frame.repaint();
				}
			}
		});

		btnMyNotes.setPreferredSize(new Dimension(85, 27));
		btnMyNotes.setMinimumSize(new Dimension(63, 23));
		btnMyNotes.setMaximumSize(new Dimension(63, 23));
		panel.add(btnMyNotes);

		final PopupMenu popupMenu = new PopupMenu();
		popupMenu.getPopupMenu(textArea, scrollPane);

	}

	class MyHighlightPainter extends DefaultHighlighter.DefaultHighlightPainter {

		public MyHighlightPainter(Color color) {
			super(color);
		}
	}

	public class ShowFavoriteFiles {

		/**
		 * 
		 */
		private File file;
		private String selectedItem = "";
		private JScrollPane scrollPane;

		public JScrollPane getFavoriteFiles() {

			JPanel panel = new JPanel();

			JLabel lblAllNotes = new JLabel("All Notes : ");
			panel.add(lblAllNotes);

			SelectedListCellRenderer renderer = new SelectedListCellRenderer();
			scrollPane = new JScrollPane();
			scrollPane.setBorder(new TitledBorder(null, "Favorite notes", TitledBorder.LEADING, TitledBorder.TOP, null,
					Color.BLACK));

			file = new File(System.getProperty("user.dir") + File.separator);
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setCurrentDirectory(file);
			fileChooser.setMultiSelectionEnabled(true);

			DefaultListModel<String> model = new DefaultListModel<String>();
			JList<String> list = new JList<String>(model);
			list.setPreferredSize(new Dimension(85, 500));
			list.setFont(new Font("Dialog", Font.PLAIN, 13));
			list.setSelectionBackground(new Color(255, 215, 0));
			list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			list.setSelectedIndex(0);
			list.addMouseListener(new MouseAdapter() {
				public void mousePressed(MouseEvent e) {
					if (e.getClickCount() == 2) {
						selectedItem = list.getSelectedValue();
						getSelectedFileName();
					}
				}
			});
			list.setCellRenderer(renderer);

			File[] selectedFiles = fileChooser.getCurrentDirectory().listFiles();

			for (File f : selectedFiles) {
				if (f.getName().indexOf(".txt") != -1) {
					model.addElement(f.getName());
				}
			}
			if(model.isEmpty()) {
				model.addElement("List is empty!");
			}
			scrollPane.setViewportView(list);
			return scrollPane;
		}

		public void setVisibility() {
			scrollPane.setVisible(false);
		}

		public void getSelectedFileName() {

			BufferedReader br;
			try {
				String getFileFrom = file.getAbsolutePath() + File.separator + selectedItem;
				br = new BufferedReader(new FileReader(getFileFrom));
				textArea.read(br, Charset.defaultCharset());
			} catch (FileNotFoundException e) {
				JOptionPane.showMessageDialog(new Frame(), "There is no file to read! \n At first you need to 'Save' a file", "Exception", JOptionPane.ERROR_MESSAGE);
			} catch (IOException e) {
				e.printStackTrace();

			}

		}

		class SelectedListCellRenderer extends DefaultListCellRenderer {
			/**
			* 
			*/
			private static final long serialVersionUID = 1L;

			@Override
			public Component getListCellRendererComponent(@SuppressWarnings("rawtypes") JList list, Object value,
					int index, boolean isSelected, boolean cellHasFocus) {
				Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
				if (index % 2 == 0) {
					c.setBackground(new Color(212, 231, 249));
				}
				if (isSelected == true) {
					c.setBackground(Color.CYAN.darker());
				}
				return c;
			}

		}
	}

}
