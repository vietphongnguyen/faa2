package indexDocs;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.awt.print.Book;
import java.awt.print.PageFormat;
import java.awt.print.Paper;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Formatter;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import javax.swing.AbstractListModel;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JLayeredPane;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToggleButton;
import javax.swing.RowSorter;
import javax.swing.SortOrder;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.GroupLayout.Alignment;
import javax.swing.JTable.PrintMode;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

import indexDocs.createTextDataUsingTika;
import indexDocs.RestartProgram;

public class IndexDocsGUI extends JFrame {

	private static final long serialVersionUID = 1L;

	private JPanel contentPane;
	public static JButton btnExtractTextContents;
	public static JTextArea txtrCdata;
	public static JTextArea console;
	public static JProgressBar progressBar;
	public static boolean process_GenerateTopics_running = false;
	public static boolean processPrintCompositionTable_running = false;
	public static JButton btnXCancel;
	public static JButton btnGetFiles;
	@SuppressWarnings("rawtypes")
	public static JList list;
	public static createTextDataUsingTika process_createTextDataUsingTika;
	
	@SuppressWarnings("rawtypes")
	public static DefaultListModel JListOfFiles;
	private JTextField txtDatatextfolder;
	private JToggleButton tglbtnUsedefaultdatatext;

	@SuppressWarnings("rawtypes")
	public static DefaultListModel JListOfTopics, JListOfTopicsWordnet, JListOfTopicsWordnetParent;
	public static DefaultTableModel compositionTable;
	private JMenuItem mntmRestartThisApplication;
	private JMenuItem mntmExit;
	private JMenuItem mntmPreferences;
	private JMenuItem mntmClose;
	private JMenuItem mntmSaveAs;
	private JMenuItem mntmSave;
	private JMenuItem mntmOpen;
	private JMenuItem mntmNew;
	private JMenuItem mntmClearConsole;
	private JMenuItem mntmTurnOff;
	private JMenuItem mntmExtractTextContents;
	private JMenuItem mntmOption;
	private JMenuItem mntmGetHelp;
	private JMenuItem mntmUpdate;
	private JMenuItem mntmAbout;
	private JTabbedPane tabbedPane;
	
	public static boolean NoConsoleOutput;

	public static boolean ProcessAutoDetectNumberOfTopics_running;
	
	static int NumberOfTopic;

	static int SizeOfInstance;
	private JMenuItem mntmSaveAllText;
	public static JCheckBox chckbxEnglishOnly;
	private JLayeredPane layeredPane_1;
	private JLabel lblLevelOfText;
	public static JSpinner spinnerLevelTextSize;
	private JLabel lblMaxNumberOf;
	public static JSpinner spinnerMaxCharacter;
	public static JSpinner spinnerToPage;
	public static JSpinner spinnerFromPage;

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private void initComponents() {
		setTitle("Index Documents for Searching");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1200, 700);

		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);

		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);

		mntmNew = new JMenuItem("New");
		mnFile.add(mntmNew);

		mntmOpen = new JMenuItem("Open");
		mnFile.add(mntmOpen);

		JMenu mnOpenRecently = new JMenu("Open recently");
		mnFile.add(mnOpenRecently);

		mntmSave = new JMenuItem("Save");
		mnFile.add(mntmSave);

		mntmSaveAs = new JMenuItem("Save as ...");
		mnFile.add(mntmSaveAs);

		mntmClose = new JMenuItem("Close");
		mnFile.add(mntmClose);

		JSeparator separator = new JSeparator();
		mnFile.add(separator);

		mntmPreferences = new JMenuItem("Preferences");
		mnFile.add(mntmPreferences);

		JSeparator separator_16 = new JSeparator();
		mnFile.add(separator_16);

		mntmRestartThisApplication = new JMenuItem("Restart this application ...");

		mnFile.add(mntmRestartThisApplication);

		JSeparator separator_12 = new JSeparator();
		mnFile.add(separator_12);

		mntmExit = new JMenuItem("Exit");

		mnFile.add(mntmExit);

		JMenu mnConsole = new JMenu("Console");
		menuBar.add(mnConsole);

		mntmClearConsole = new JMenuItem("Clear console");

		mnConsole.add(mntmClearConsole);
		
		JSeparator separator_29 = new JSeparator();
		mnConsole.add(separator_29);
		
		mntmSaveAllText = new JMenuItem("Save all text in console to a text file");
		
		mnConsole.add(mntmSaveAllText);
		
		JSeparator separator_28 = new JSeparator();
		mnConsole.add(separator_28);

		mntmTurnOff = new JMenuItem("Turn off");

		mnConsole.add(mntmTurnOff);

		JMenu mnImportLectures = new JMenu("Extract documents");
		menuBar.add(mnImportLectures);

		mntmExtractTextContents = new JMenuItem("Extract text contents from folder");

		mnImportLectures.add(mntmExtractTextContents);

		JSeparator separator_15 = new JSeparator();
		mnImportLectures.add(separator_15);

		mntmOption = new JMenuItem("Option ...");
		mnImportLectures.add(mntmOption);
		
		JMenu mnAutomateTask = new JMenu("Automate tasks");
		menuBar.add(mnAutomateTask);

		JMenu mnHelp = new JMenu("Help");
		menuBar.add(mnHelp);

		mntmGetHelp = new JMenuItem("Get help");
		mnHelp.add(mntmGetHelp);

		JSeparator separator_1 = new JSeparator();
		mnHelp.add(separator_1);

		mntmUpdate = new JMenuItem("Check for updates");
		mnHelp.add(mntmUpdate);

		JSeparator separator_2 = new JSeparator();
		mnHelp.add(separator_2);

		mntmAbout = new JMenuItem("About");
		mnHelp.add(mntmAbout);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);

		tabbedPane = new JTabbedPane(JTabbedPane.TOP);

		console = new JTextArea(6, 0);
		console.setText(" Output console text: \n ");

		JScrollPane scrollPane = new JScrollPane(console);

		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.TRAILING)
						.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 1173, Short.MAX_VALUE))
					.addGap(1))
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
					.addComponent(tabbedPane, GroupLayout.DEFAULT_SIZE, 507, Short.MAX_VALUE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 92, GroupLayout.PREFERRED_SIZE)
					.addGap(20))
		);

		JLayeredPane layeredPane = new JLayeredPane();
		tabbedPane.addTab("Extract text data", null, layeredPane, null);

		JLabel lblFilesAndDocuments = new JLabel("Files and documents");
		lblFilesAndDocuments.setFont(new Font("Tahoma", Font.BOLD, 11));

		JScrollPane scrollPane_1 = new JScrollPane();

		txtrCdata = new JTextArea();

		scrollPane_1.setViewportView(txtrCdata);
		txtrCdata.setBackground(Color.WHITE);
		txtrCdata.setToolTipText("Please input the the folder where you want to get documents and files");
		txtrCdata.setText("C:\\FAA2\\data");

		JButton btnNew = new JButton("New");
		btnNew.setEnabled(false);

		JButton btnCheck = new JButton("Check");
		btnCheck.setEnabled(false);

		JButton btnBrowse = new JButton("Browse...");
		btnBrowse.setEnabled(false);

		JButton btnSave = new JButton("Save");
		btnSave.setEnabled(false);

		JScrollPane scrollPane_2 = new JScrollPane();

		JListOfFiles = new DefaultListModel();
		list = new JList(JListOfFiles);
		
		scrollPane_2.setViewportView(list);

		btnGetFiles = new JButton("Get files >>");
		btnGetFiles.setEnabled(false);

		btnExtractTextContents = new JButton("Extract text contents");

		progressBar = new JProgressBar();

		btnXCancel = new JButton("Cancel");
		btnXCancel.setEnabled(false);

		txtDatatextfolder = new JTextField();
		txtDatatextfolder.setEnabled(false);

		txtDatatextfolder.setEditable(false);
		txtDatatextfolder.setText("data_text");
		txtDatatextfolder.setColumns(10);

		tglbtnUsedefaultdatatext = new JToggleButton("");

		tglbtnUsedefaultdatatext.setSelected(true);

		JLabel lblDefaultOutputText = new JLabel("Ues default output text folder:");
		lblDefaultOutputText.setFont(new Font("Tahoma", Font.PLAIN, 10));
		lblDefaultOutputText.setHorizontalAlignment(SwingConstants.RIGHT);
		
		chckbxEnglishOnly = new JCheckBox("Extract English documents only");
		chckbxEnglishOnly.setEnabled(false);
		
		JLabel lblNewLabel = new JLabel("From Page");
		
		spinnerFromPage = new JSpinner();
		
		spinnerFromPage.setModel(new SpinnerNumberModel(new Integer(1), new Integer(1), null, new Integer(1)));
		
		JLabel lblTo = new JLabel("To");
		
		spinnerToPage = new JSpinner();
		
		spinnerToPage.setModel(new SpinnerNumberModel(new Integer(10), new Integer(1), null, new Integer(1)));
		
		lblLevelOfText = new JLabel("Level of text size");
		
		spinnerLevelTextSize = new JSpinner();
		spinnerLevelTextSize.setModel(new SpinnerNumberModel(new Integer(3), new Integer(1), null, new Integer(1)));
		
		lblMaxNumberOf = new JLabel("Max number of Characters");
		
		spinnerMaxCharacter = new JSpinner();
		spinnerMaxCharacter.setModel(new SpinnerNumberModel(new Integer(500), new Integer(10), null, new Integer(10)));
		GroupLayout gl_layeredPane = new GroupLayout(layeredPane);
		gl_layeredPane.setHorizontalGroup(
			gl_layeredPane.createParallelGroup(Alignment.TRAILING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addContainerGap()
							.addComponent(lblFilesAndDocuments, GroupLayout.DEFAULT_SIZE, 294, Short.MAX_VALUE)
							.addGap(48)
							.addComponent(lblDefaultOutputText, GroupLayout.PREFERRED_SIZE, 164, GroupLayout.PREFERRED_SIZE)
							.addGap(107)
							.addComponent(tglbtnUsedefaultdatatext, GroupLayout.PREFERRED_SIZE, 28, GroupLayout.PREFERRED_SIZE)
							.addGap(10)
							.addComponent(txtDatatextfolder, GroupLayout.PREFERRED_SIZE, 304, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(chckbxEnglishOnly))
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addContainerGap()
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.TRAILING, false)
								.addGroup(gl_layeredPane.createSequentialGroup()
									.addComponent(btnNew, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
									.addGap(20)
									.addComponent(btnCheck, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
									.addGap(41)
									.addComponent(btnBrowse, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
								.addGroup(gl_layeredPane.createSequentialGroup()
									.addComponent(btnSave, GroupLayout.PREFERRED_SIZE, 72, GroupLayout.PREFERRED_SIZE)
									.addPreferredGap(ComponentPlacement.RELATED, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addComponent(btnGetFiles, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)))
							.addGap(48)
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING, false)
								.addGroup(Alignment.TRAILING, gl_layeredPane.createSequentialGroup()
									.addComponent(lblNewLabel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
									.addGap(18)
									.addComponent(spinnerFromPage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE)
									.addGap(18)
									.addComponent(spinnerToPage, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
								.addComponent(btnExtractTextContents, GroupLayout.PREFERRED_SIZE, 242, GroupLayout.PREFERRED_SIZE))
							.addGap(33)
							.addComponent(lblLevelOfText, GroupLayout.PREFERRED_SIZE, 105, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(spinnerLevelTextSize, GroupLayout.PREFERRED_SIZE, 31, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.UNRELATED)
							.addComponent(lblMaxNumberOf, GroupLayout.PREFERRED_SIZE, 167, GroupLayout.PREFERRED_SIZE)
							.addGap(18)
							.addComponent(spinnerMaxCharacter, GroupLayout.PREFERRED_SIZE, 44, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.TRAILING)
								.addGroup(gl_layeredPane.createSequentialGroup()
									.addGap(10)
									.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 294, GroupLayout.PREFERRED_SIZE)
									.addGap(48)
									.addComponent(scrollPane_2, GroupLayout.DEFAULT_SIZE, 701, Short.MAX_VALUE))
								.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, 1053, Short.MAX_VALUE))
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnXCancel, GroupLayout.PREFERRED_SIZE, 99, GroupLayout.PREFERRED_SIZE)))
					.addContainerGap())
		);
		gl_layeredPane.setVerticalGroup(
			gl_layeredPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_layeredPane.createSequentialGroup()
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(4)
							.addComponent(tglbtnUsedefaultdatatext, GroupLayout.PREFERRED_SIZE, 23, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(4)
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(txtDatatextfolder, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(chckbxEnglishOnly)))
						.addComponent(lblFilesAndDocuments, GroupLayout.PREFERRED_SIZE, 26, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(7)
							.addComponent(lblDefaultOutputText, GroupLayout.PREFERRED_SIZE, 14, GroupLayout.PREFERRED_SIZE)))
					.addGap(10)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 321, GroupLayout.PREFERRED_SIZE)
						.addComponent(scrollPane_2, GroupLayout.PREFERRED_SIZE, 325, GroupLayout.PREFERRED_SIZE))
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnNew)
								.addComponent(btnCheck)
								.addComponent(btnBrowse)))
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(11)
							.addComponent(btnExtractTextContents)))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(10)
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING)
								.addComponent(btnGetFiles)
								.addComponent(btnSave)))
						.addGroup(gl_layeredPane.createParallelGroup(Alignment.BASELINE)
							.addComponent(lblLevelOfText, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(spinnerToPage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addComponent(lblTo, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
							.addComponent(spinnerFromPage, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE))
						.addGroup(gl_layeredPane.createSequentialGroup()
							.addGap(2)
							.addGroup(gl_layeredPane.createParallelGroup(Alignment.BASELINE)
								.addComponent(spinnerLevelTextSize, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
								.addComponent(lblMaxNumberOf, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE)
								.addComponent(spinnerMaxCharacter, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)))
						.addComponent(lblNewLabel, GroupLayout.PREFERRED_SIZE, 24, GroupLayout.PREFERRED_SIZE))
					.addPreferredGap(ComponentPlacement.RELATED)
					.addGroup(gl_layeredPane.createParallelGroup(Alignment.LEADING, false)
						.addComponent(progressBar, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
						.addComponent(btnXCancel, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
					.addContainerGap())
		);
		layeredPane.setLayout(gl_layeredPane);
		
		layeredPane_1 = new JLayeredPane();
		tabbedPane.addTab("Content Parser ", null, layeredPane_1, null);

		compositionTable = new DefaultTableModel(new Object[][] {

		}, new String[] { "#", "Composition of the InstanceList:", "Topic 0" });

		JListOfTopics = new DefaultListModel();
		
		JListOfTopicsWordnet = new DefaultListModel();
		
		JListOfTopicsWordnetParent = new DefaultListModel();
		contentPane.setLayout(gl_contentPane);
		
		
		// Application start initiating  value ////////////////////////////////////////////////////////////////////////////////////////////////////////////////
		NoConsoleOutput = false;
		//console.setVisible(false);

	}



	
	private void createEvents() {

		spinnerToPage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				// If from page change its value then update the from page value too
				if (	(int)	spinnerToPage.getValue() < 	(int)	spinnerFromPage.getValue()	)
					spinnerFromPage.setValue((int)spinnerToPage.getValue());
			}
		});
		spinnerFromPage.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent arg0) {
				// If from page change its value then update the to page value too
				if (	(int)	spinnerToPage.getValue() < 	(int)	spinnerFromPage.getValue()	)
					spinnerToPage.setValue((int)spinnerFromPage.getValue());
				
			}
		});
		
		mntmRestartThisApplication.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					RestartProgram.restart(null);
				} catch (IOException e1) {
					Outln(e1.toString());
					e1.printStackTrace();
				}
			}
		});

		mntmSaveAllText.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String content = console.getText().trim();
				if (content.length()<= 0) return;
				
				String outputFolder = "./logs/";
				File dir = new File(outputFolder);
				dir.mkdir();
				
				String outputFileName ="console";
				String timeStamp = new SimpleDateFormat("_yyyy-MM-dd_HH-mm-ss").format(Calendar.getInstance().getTime());
				outputFileName += timeStamp +  ".txt";
				
				try {
					FileOutputStream outputStream = new FileOutputStream(outputFolder + outputFileName);
					OutputStreamWriter outputStreamWriter = new OutputStreamWriter(outputStream, "UTF-8");
					BufferedWriter bufferedWriter = new BufferedWriter(outputStreamWriter);
		
					bufferedWriter.write(content);
					bufferedWriter.newLine();

					bufferedWriter.close();
					
					console.setText(""); // Clear the console content after saved to a file
					
				} catch (IOException e2) {
					e2.printStackTrace();
					Outln(e2.toString());
				}
				
			}
		});

		///////////////////////////////////////////////// Extract text data from
		///////////////////////////////////////////////// documents
		///////////////////////////////////////////////// tab/////////////////////////////////////////////////////////////////////////////
		mntmExtractTextContents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				tabbedPane.setSelectedIndex(0);
				actionPerformed_createTextDataUsingTika();
			}
		});

		txtDatatextfolder.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent e) {
				String s = txtDatatextfolder.getText();
				s = getFoulderNameOf(s);
				if (s == "")
					s = "data_text";
				txtDatatextfolder.setText(s);

			}
		});

		txtrCdata.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseExited(MouseEvent e) {
				if (!tglbtnUsedefaultdatatext.isSelected()) {
					// update text output folder name
					txtDatatextfolder.setText(getFoulderNameOf(txtrCdata.getText()) + "_text");
					txtDatatextfolder.setEditable(true);
				}
			}
		});

		txtrCdata.addKeyListener(new KeyAdapter() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (!tglbtnUsedefaultdatatext.isSelected()) {
					// update text output folder name
					txtDatatextfolder.setText(getFoulderNameOf(txtrCdata.getText()) + "_text");
					txtDatatextfolder.setEditable(true);
				}
			}

		});

		tglbtnUsedefaultdatatext.addChangeListener(new ChangeListener() {
			public void stateChanged(ChangeEvent e) {
				if (tglbtnUsedefaultdatatext.isSelected()) {
					// use defaule data_text folder
					txtDatatextfolder.setText("data_text");
					txtDatatextfolder.setEditable(false);
				} else {
					txtDatatextfolder.setText(getFoulderNameOf(txtrCdata.getText()) + "_text");
					txtDatatextfolder.setEditable(true);
				}
			}
		});

		btnGetFiles.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionPerformed_createTextDataUsingTika();
			}
		});

		btnXCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				process_createTextDataUsingTika.cancel(true);
				btnXCancel.setEnabled(false);
			}
		});

		btnExtractTextContents.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				actionPerformed_createTextDataUsingTika();
			}
		});

		mntmExit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				System.exit(1);
			}
		});

		mntmClearConsole.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				console.setText("");
			}
		});
		mntmTurnOff.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if (console.isVisible()) {
					console.setVisible(false);
					mntmTurnOff.setText("Turn on");
				} else {
					console.setVisible(true);
					mntmTurnOff.setText("Turn off");
				}
			}
		});

	}
	public void actionPerformed_createTextDataUsingTika() {
		btnXCancel.setEnabled(true);
		process_createTextDataUsingTika = null;
		try {
			process_createTextDataUsingTika = new createTextDataUsingTika(getFoulderNameOf(txtrCdata.getText()),
					getFoulderNameOf(txtDatatextfolder.getText()));
		} catch (IOException e2) {
			e2.printStackTrace();
			Outln(e2.toString());
		} catch (SAXException e2) {
			e2.printStackTrace();
			Outln(e2.toString());
		} catch (TikaException e2) {
			e2.printStackTrace();
			Outln(e2.toString());
		}
		try {
			btnGetFiles.setEnabled(false);
			btnExtractTextContents.setEnabled(false);
			process_createTextDataUsingTika.execute();
		} catch (Exception e1) {
			e1.printStackTrace();
			Outln(e1.toString());
		}
	}

	protected String getFoulderNameOf(String text) {
		String s = text;

		// delete newline and space at the beginning of S
		while ((s.length() > 0) && ((s.charAt(0) == '\n') || (s.charAt(0) == ' ')))
			s = s.substring(1);

		int vtnewline = s.indexOf('\n');
		if (vtnewline < 0)
			vtnewline = s.length();
		String fouldername = s.substring(0, vtnewline);

		// delete newline and space at the ending of fouldername
		while ((fouldername.length() > 0) && ((fouldername.endsWith("\n") || (fouldername.endsWith(" ")))))
			fouldername = fouldername.substring(0, fouldername.length() - 1);

		return fouldername;
	}

	public IndexDocsGUI() throws IOException {
		initComponents();
		createEvents();
		
	}

	public static void Outln(String string) {
		if (NoConsoleOutput) return;
		if (!console.isVisible()) return;
		Out(string + "\n");
		console.setCaretPosition(console.getDocument().getLength());
	}

	public static void Out(String s) {
		if (NoConsoleOutput) return;
		if (!console.isVisible()) return;
		console.setText(console.getText() + s);
		console.setCaretPosition(console.getDocument().getLength());
	}

	public int getNumberOfPages(Printable delegate, PageFormat pageFormat) throws PrinterException {
		Graphics g = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB).createGraphics();
		int numPages = 0;
		while (true) {
			int result = delegate.print(g, pageFormat, numPages);
			if (result == Printable.PAGE_EXISTS) {
				++numPages;
			} else {
				break;
			}
		}

		return numPages;
	}


	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					IndexDocsGUI frame = new IndexDocsGUI();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	private static void addPopup(Component component, final JPopupMenu popup) {
		component.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			public void mouseReleased(MouseEvent e) {
				if (e.isPopupTrigger()) {
					showMenu(e);
				}
			}
			private void showMenu(MouseEvent e) {
				popup.show(e.getComponent(), e.getX(), e.getY());
			}
		});
	}
}
