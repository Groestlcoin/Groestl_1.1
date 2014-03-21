package aplikacja.gui;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JLabel;
import java.awt.Dimension;
import javax.swing.JTextField;
import javax.swing.JButton;

import aplikacja.actionlistener.ActionListenerBtn_Wykonaj;

public class Gui extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public JTextField textField_Input;
	public JTextField textField_Output;
	public Gui() {
		setMinimumSize(new Dimension(400, 250));
		setPreferredSize(new Dimension(400, 250));
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel panel = new JPanel();
		panel.setPreferredSize(new Dimension(10, 100));
		getContentPane().add(panel, BorderLayout.NORTH);
		panel.setLayout(null);
		
		JLabel lblIn = new JLabel("IN:");
		lblIn.setBounds(10, 14, 25, 14);
		panel.add(lblIn);
		
		textField_Input = new JTextField();
		textField_Input.setBounds(49, 11, 247, 20);
		panel.add(textField_Input);
		textField_Input.setColumns(10);
		
		textField_Output = new JTextField();
		textField_Output.setBounds(49, 42, 247, 20);
		panel.add(textField_Output);
		textField_Output.setColumns(10);
		
		JLabel lblOu = new JLabel("OUT:");
		lblOu.setBounds(10, 45, 25, 14);
		panel.add(lblOu);
		
		ActionListenerBtn_Wykonaj actionListenerBtn_Wykonaj = new ActionListenerBtn_Wykonaj(textField_Input);
		JButton btn_Wykonaj = new JButton("Wykonaj");
		btn_Wykonaj.setBounds(306, 10, 75, 52);
		btn_Wykonaj.addActionListener(actionListenerBtn_Wykonaj);
		panel.add(btn_Wykonaj);
	}
}
