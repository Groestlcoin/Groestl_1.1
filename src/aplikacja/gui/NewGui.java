package aplikacja.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;

import aplikacja.implementation.HashFunction;

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
	public static HashMap<String, int[][]> map = new LinkedHashMap<>();
	private JComboBox<String> comboBox;
	private JTextArea textArea;

	public NewGui() {
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
		rdbtnHex.setBounds(10, 38, 109, 23);
		panelCalculate.add(rdbtnHex);

		rdbtnText = new JRadioButton("TEXT");
		rdbtnText.setBounds(10, 64, 109, 23);
		panelCalculate.add(rdbtnText);

		radioButtonGroup = new ButtonGroup();
		radioButtonGroup.add(rdbtnHex);
		radioButtonGroup.add(rdbtnText);

		btnOblicz = new JButton("Oblicz");
		btnOblicz.setBounds(125, 38, 89, 49);
		btnOblicz.addActionListener(new ActionListenerOblicz());
		panelCalculate.add(btnOblicz);

		this.textAreaOutput = new JTextArea();
		this.textAreaOutput.setLineWrap(true);
		this.textAreaOutput.setBorder(UIManager.getBorder("TextField.border"));
		this.textAreaOutput.setBounds(10, 94, 619, 169);
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
					int[][] state = map.get(key);
					StringBuilder sb = new StringBuilder();
					for (int i = 0; i < 8; i++) {
						for (int j = 0; j < 8; j++) {
							sb.append(String.format("%02X ", (state[i][j] & 0xFF)));
						}
						sb.append("\n");
					}

					textArea.setText(sb.toString());

				}
			}
		});
		panelSummary.add(this.comboBox);

		this.textArea = new JTextArea();
		this.textArea.setFont(new Font("Courier New", Font.PLAIN, 16));
		this.textArea.setBorder(UIManager.getBorder("TextField.border"));
		this.textArea.setBounds(265, 11, 364, 263);
		panelSummary.add(this.textArea);

		JPanel panelHelp = new JPanel();
		tabbedPane.addTab("Pomoc", null, panelHelp, null);
	}

	class ActionListenerOblicz implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (radioButtonGroup.isSelected(rdbtnHex.getModel())) {
				HashFunction hf = new HashFunction();
				textAreaOutput.setText(hf.calculateHash(textField_Input.getText()));
				Collection<String> col = map.keySet();
				Iterator<String> iterator = col.iterator();
				while (iterator.hasNext()) {
					comboBox.addItem(iterator.next());
				}
			} else {

			}

		}
	}
}
