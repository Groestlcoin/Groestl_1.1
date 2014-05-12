package aplikacja.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import aplikacja.implementation.CompressionFunction;
import aplikacja.implementation.HashFunction;
import aplikacja.implementation.State;
import aplikacja.implementation.ToArray;
import java.awt.Font;

public class NewGui extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private JTextField textField_Input;

	private JRadioButton rdbtnHex;
	private JRadioButton rdbtnText;

	private JButton btnOblicz;

	private ButtonGroup radioButtonGroup;
	private JTextArea textAreaOutput;
	// public static IdentityHashMap<String, int[][]> map = new
	// IdentityHashMap<>();
	public static List<State> stateList = new ArrayList<State>();
	private JRadioButton rdbtnPlik;

	private byte[] textBytes;
	private JLabel labelPath;
	private JTextArea textArea_0;
	private JTextArea textArea_1;
	private JTextArea textArea_2;
	private JTextArea textArea_3;
	private JTextArea textArea_4;
	private JTextArea textArea_5;
	private JLabel label_0;
	private JLabel label_1;
	private JLabel label_2;
	private JLabel label_3;
	private JLabel label_4;
	private JLabel label_5;
	private JRadioButton rdbtnP;
	private JRadioButton rdbtnQ;
	private JComboBox<String> comboBoxRoundNumber;
	private JComboBox<String> comboBoxBlockNumber;

	private boolean calculationFocus;

	// private ComboBoxModel<String> myDefaultComboBoxModel = new
	// DefaultComboBoxModel<String>(new String[] { "mi", "hm", "xorHm",
	// "runda 1", "runda 2",
	// "runda 3", "runda 4", "runda 5", "runda 6", "runda 7", "runda 8",
	// "runda 9", "runda 10", "xorAll" });
	// private ComboBoxModel<String> myOutputComboBoxModel = new
	// DefaultComboBoxModel<String>(new String[] { "runda 1", "runda 2",
	// "runda 3", "runda 4",
	// "runda 5", "runda 6", "runda 7", "runda 8", "runda 9", "runda 10",
	// "xorAll" });

	public NewGui() {

		Font areaFont = new Font("Monospaced", Font.PLAIN, 13);

		try {
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch (ClassNotFoundException e1) {
			e1.printStackTrace();
		} catch (InstantiationException e1) {
			e1.printStackTrace();
		} catch (IllegalAccessException e1) {
			e1.printStackTrace();
		} catch (UnsupportedLookAndFeelException e1) {
			e1.printStackTrace();
		}
		setSize(new Dimension(699, 499));
		setPreferredSize(new Dimension(660, 480));

		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		getContentPane().add(tabbedPane, BorderLayout.CENTER);

		JPanel panelCalculate = new JPanel();
		tabbedPane.addTab("Obliczanie", null, panelCalculate, null);
		panelCalculate.setLayout(null);

		textField_Input = new JTextField();
		textField_Input.setBounds(10, 11, 619, 20);
		panelCalculate.add(textField_Input);
		textField_Input.setColumns(10);

		rdbtnHex = new JRadioButton("HEX");
		rdbtnHex.setBounds(10, 38, 65, 23);
		panelCalculate.add(rdbtnHex);
		rdbtnHex.setSelected(true);

		rdbtnText = new JRadioButton("TEXT");
		rdbtnText.setBounds(10, 64, 65, 23);
		panelCalculate.add(rdbtnText);

		this.rdbtnPlik = new JRadioButton("PLIK");
		this.rdbtnPlik.setBounds(10, 90, 65, 23);
		panelCalculate.add(this.rdbtnPlik);

		radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(rdbtnHex);
		radioButtonGroup.add(rdbtnText);
		radioButtonGroup.add(rdbtnPlik);

		rdbtnText.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Input.setText("");
				textField_Input.setEnabled(true);
				textAreaOutput.setText("");
				textArea_0.setText("");
				textArea_1.setText("");
				textArea_2.setText("");
				textArea_3.setText("");
				textArea_4.setText("");
				textArea_5.setText("");
				comboBoxBlockNumber.removeAllItems();
				textField_Input.requestFocus();
			}
		});

		rdbtnHex.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Input.setText("");
				textField_Input.setEnabled(true);
				textAreaOutput.setText("");
				textArea_0.setText("");
				textArea_1.setText("");
				textArea_2.setText("");
				textArea_3.setText("");
				textArea_4.setText("");
				textArea_5.setText("");
				comboBoxBlockNumber.removeAllItems();
				textField_Input.requestFocus();

			}
		});

		rdbtnPlik.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Input.setText("");
				textField_Input.setEnabled(false);
				textAreaOutput.setText("");
				textArea_0.setText("");
				textArea_1.setText("");
				textArea_2.setText("");
				textArea_3.setText("");
				textArea_4.setText("");
				textArea_5.setText("");
				comboBoxBlockNumber.removeAllItems();

				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(NewGui.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();
					readTextFromFile(selectedFile);
					labelPath.setText(selectedFile.getAbsolutePath());
				}
			}
		});

		btnOblicz = new JButton("Oblicz");
		btnOblicz.setBounds(125, 38, 89, 42);
		btnOblicz.addActionListener(new ActionListenerOblicz());
		panelCalculate.add(btnOblicz);

		this.textAreaOutput = new JTextArea();
		this.textAreaOutput.setEditable(false);
		this.textAreaOutput.setLineWrap(true);
		this.textAreaOutput.setBorder(UIManager.getBorder("TextField.border"));
		this.textAreaOutput.setBounds(10, 150, 619, 169);
		panelCalculate.add(this.textAreaOutput);

		this.labelPath = new JLabel("");
		this.labelPath.setBounds(81, 94, 212, 14);
		panelCalculate.add(this.labelPath);

		JPanel panelSummary = new JPanel();
		tabbedPane.addTab("Podsumowanie", null, panelSummary, null);
		panelSummary.setLayout(null);

		this.textArea_0 = new JTextArea();
		this.textArea_0.setFont(areaFont);
		this.textArea_0.setBounds(10, 65, 213, 158);
		panelSummary.add(this.textArea_0);

		this.textArea_1 = new JTextArea();
		this.textArea_1.setFont(areaFont);
		this.textArea_1.setBounds(233, 65, 213, 158);
		panelSummary.add(this.textArea_1);

		this.textArea_2 = new JTextArea();
		this.textArea_2.setFont(areaFont);
		this.textArea_2.setBounds(456, 65, 213, 158);
		panelSummary.add(this.textArea_2);

		this.textArea_3 = new JTextArea();
		this.textArea_3.setFont(areaFont);
		this.textArea_3.setBounds(10, 259, 213, 158);
		panelSummary.add(this.textArea_3);

		this.textArea_4 = new JTextArea();
		this.textArea_4.setFont(areaFont);
		this.textArea_4.setBounds(233, 259, 213, 158);
		panelSummary.add(this.textArea_4);

		this.textArea_5 = new JTextArea();
		this.textArea_5.setFont(areaFont);
		this.textArea_5.setBounds(456, 259, 213, 158);
		panelSummary.add(this.textArea_5);

		this.label_0 = new JLabel("");
		this.label_0.setBounds(10, 40, 213, 14);
		panelSummary.add(this.label_0);

		this.label_1 = new JLabel("");
		this.label_1.setBounds(233, 40, 213, 14);
		panelSummary.add(this.label_1);

		this.label_2 = new JLabel("");
		this.label_2.setBounds(456, 40, 213, 14);
		panelSummary.add(this.label_2);

		this.label_3 = new JLabel("");
		this.label_3.setBounds(10, 234, 213, 14);
		panelSummary.add(this.label_3);

		this.label_4 = new JLabel("");
		this.label_4.setBounds(233, 234, 213, 14);
		panelSummary.add(this.label_4);

		this.label_5 = new JLabel("");
		this.label_5.setBounds(456, 234, 213, 14);
		panelSummary.add(this.label_5);

		this.rdbtnP = new JRadioButton("P()");
		this.rdbtnP.setBounds(244, 10, 39, 23);
		this.rdbtnP.setSelected(true);
		panelSummary.add(this.rdbtnP);

		this.rdbtnQ = new JRadioButton("Q()");
		this.rdbtnQ.setBounds(285, 10, 50, 23);
		panelSummary.add(this.rdbtnQ);

		ButtonGroup buttonGroupPandQ = new ButtonGroup();
		buttonGroupPandQ.add(rdbtnP);
		buttonGroupPandQ.add(rdbtnQ);

		rdbtnP.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateResults();
			}
		});

		rdbtnQ.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				updateResults();
			}
		});

		this.comboBoxRoundNumber = new JComboBox<String>();
		this.comboBoxRoundNumber.setModel(new DefaultComboBoxModel<String>(new String[] { "runda 1", "runda 2", "runda 3", "runda 4", "runda 5", "runda 6",
				"runda 7", "runda 8", "runda 9", "runda 10" }));
		this.comboBoxRoundNumber.setBounds(129, 11, 109, 20);
		panelSummary.add(this.comboBoxRoundNumber);
		this.comboBoxRoundNumber.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!calculationFocus) {
					updateResults();
				}
			}
		});

		this.comboBoxBlockNumber = new JComboBox<String>();
		this.comboBoxBlockNumber.setBounds(10, 11, 109, 20);
		panelSummary.add(this.comboBoxBlockNumber);
		this.comboBoxBlockNumber.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (!calculationFocus) {
					int selectedIndex = comboBoxBlockNumber.getSelectedIndex();
					if (selectedIndex == CompressionFunction.blockNumber) {
						rdbtnQ.setEnabled(false);
						rdbtnP.setSelected(true);
						// comboBoxRoundNumber.setModel(myOutputComboBoxModel);
					} else {
						rdbtnQ.setEnabled(true);
						rdbtnP.setSelected(true);
						// comboBoxRoundNumber.setModel(myDefaultComboBoxModel);
						comboBoxRoundNumber.setSelectedIndex(0);
					}
					updateResults();
				}
			}
		});

		JPanel panelHelp = new JPanel();
		tabbedPane.addTab("Pomoc", null, panelHelp, null);
	}

	protected void updateResults() {

		try {
			int blockNumber = comboBoxBlockNumber.getSelectedIndex();
			int roundNumber = comboBoxRoundNumber.getSelectedIndex();
			String permutation;
			if (rdbtnP.isSelected()) {
				permutation = "P";
			} else {
				permutation = "Q";
			}

			label_0.setText("Wejsie do permutacji");
			label_1.setText("Sta³a rundowa");
			label_2.setText("Dodanie sta³ej rundowej");
			label_3.setText("Metoda SubBytes");
			label_4.setText("Metoda ShiftBytes");
			label_5.setText("Metoda MixBytes");

			for (State s : stateList) {

				String[] sta = s.getKey().split("#");
				String[] staBlckNS = sta[0].split(":");
				int staBlckNmb = Integer.parseInt(staBlckNS[1]);
				String[] staRndNS = sta[3].split(":");
				int staRndNmb = Integer.parseInt(staRndNS[1]);
				String staQorP = sta[1];
				String staMethod = sta[2];

				if (staBlckNmb == blockNumber && staRndNmb == roundNumber && staQorP.equals(permutation)) {
					switch (staMethod.toUpperCase()) {
					case "INPUT": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_0.setText(sb.toString());
					}
						break;
					case "ROUNDCONST": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_1.setText(sb.toString());
					}
						break;
					case "ADDROUNDCONSTANT": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_2.setText(sb.toString());
					}
						break;
					case "SUBBYTES": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_3.setText(sb.toString());
					}
						break;
					case "SHIFTBYTES": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_4.setText(sb.toString());
					}
						break;
					case "MIXBYTES": {
						int[][] state = s.getArray();
						StringBuilder sb = new StringBuilder();
						for (int i = 0; i < 8; i++) {
							for (int j = 0; j < 8; j++) {
								sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
							}
							sb.append("\n");
						}
						textArea_5.setText(sb.toString());
					}
						break;
					default: {
					}
						break;
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void readTextFromFile(File selectedFile) {

		try {
			textBytes = Files.readAllBytes(selectedFile.toPath());
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	class ActionListenerOblicz implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (radioButtonGroup.isSelected(rdbtnHex.getModel())) {
				byte[] hexbyteArray = null;
				try {
					if (!textField_Input.getText().equals("")) {
						hexbyteArray = ToArray.toByteArray(textField_Input.getText());
					} else {
						throw new NullPointerException();
					}
					doCalculate(hexbyteArray);
				} catch (IllegalArgumentException e1) {
					JOptionPane.showMessageDialog(NewGui.this, "Z³y format wejœciowy!", "B³¹d", JOptionPane.ERROR_MESSAGE);
				} catch (NullPointerException e2) {
					JOptionPane.showMessageDialog(NewGui.this, "Brak danych wejœciowych!", "B³¹d", JOptionPane.ERROR_MESSAGE);
				}
			} else if (radioButtonGroup.isSelected(rdbtnText.getModel())) {
				doCalculate(textField_Input.getText().getBytes());
			} else if (radioButtonGroup.isSelected(rdbtnPlik.getModel())) {
				doCalculate(textBytes);
			}

		}
	}

	public void doCalculate(byte[] bytes) {
		HashFunction hf = new HashFunction();
		textAreaOutput.setText(hf.calculateHash(bytes));
		calculationFocus = true;
		for (int i = 0; i <= CompressionFunction.blockNumber - 1; i++) {
			comboBoxBlockNumber.addItem("blok nr: " + i);
		}
		comboBoxBlockNumber.addItem("output transformation: " + CompressionFunction.blockNumber + 1);
		calculationFocus = false;
	}
}
