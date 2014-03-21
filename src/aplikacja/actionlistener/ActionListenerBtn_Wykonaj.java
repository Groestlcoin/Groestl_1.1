package aplikacja.actionlistener;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import aplikacja.implementation.HashFunction;

public class ActionListenerBtn_Wykonaj implements ActionListener {

	private JTextField textField;

	public ActionListenerBtn_Wykonaj(JTextField textField_Input) {
		this.textField = textField_Input;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		HashFunction hf = new HashFunction();
		hf.calculateHash(textField.getText());
	}

}
