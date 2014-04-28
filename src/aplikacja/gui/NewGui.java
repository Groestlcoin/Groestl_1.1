package aplikacja.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import aplikacja.implementation.HashFunction;
import aplikacja.implementation.State;
import aplikacja.implementation.ToArray;

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
	private JComboBox<String> comboBox;
	private JTextArea textAreaDetails;
	private JRadioButton rdbtnPlik;

	private byte[] textBytes;

	public NewGui() {
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
		setSize(new Dimension(660, 480));
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
				textAreaOutput.setText("");
				textAreaDetails.setText("");
			}
		});

		rdbtnHex.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Input.setText("");
				textAreaOutput.setText("");
				textAreaDetails.setText("");
			}
		});

		rdbtnPlik.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				textField_Input.setText("");
				textAreaOutput.setText("");
				textAreaDetails.setText("");
				JFileChooser fileChooser = new JFileChooser();
				int result = fileChooser.showOpenDialog(NewGui.this);
				if (result == JFileChooser.APPROVE_OPTION) {
					File selectedFile = fileChooser.getSelectedFile();

					readTextFromFile(selectedFile);

				}
			}
		});

		btnOblicz = new JButton("Oblicz");
		btnOblicz.setBounds(125, 38, 89, 49);
		btnOblicz.addActionListener(new ActionListenerOblicz());
		panelCalculate.add(btnOblicz);

		this.textAreaOutput = new JTextArea();
		this.textAreaOutput.setEditable(false);
		this.textAreaOutput.setLineWrap(true);
		this.textAreaOutput.setBorder(UIManager.getBorder("TextField.border"));
		this.textAreaOutput.setBounds(10, 150, 619, 169);
		panelCalculate.add(this.textAreaOutput);

		JPanel panelSummary = new JPanel();
		tabbedPane.addTab("Podsumowanie", null, panelSummary, null);
		panelSummary.setLayout(null);

		this.comboBox = new JComboBox<String>();
		this.comboBox.setBounds(10, 11, 245, 20);
		this.comboBox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				String key = (String) comboBox.getSelectedItem();
				if (key != null) {
					for (State s : stateList) {
						if (s.getKey().equals(key)) {
							int[][] state = s.getArray();
							StringBuilder sb = new StringBuilder();
							for (int i = 0; i < 8; i++) {
								for (int j = 0; j < 8; j++) {
									sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
								}
								sb.append("\n");
							}

							textAreaDetails.setText(sb.toString());
						}
					}
				}
			}
		});
		panelSummary.add(this.comboBox);

		this.textAreaDetails = new JTextArea();
		this.textAreaDetails.setFont(new Font("Courier New", Font.PLAIN, 16));
		this.textAreaDetails.setBorder(UIManager.getBorder("TextField.border"));
		this.textAreaDetails.setBounds(265, 11, 364, 263);
		panelSummary.add(this.textAreaDetails);

		JPanel panelHelp = new JPanel();
		tabbedPane.addTab("Pomoc", null, panelHelp, null);
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
		Iterator<State> iterator = stateList.iterator();
		while (iterator.hasNext()) {
			comboBox.addItem(iterator.next().getKey());
		}
	}
}
