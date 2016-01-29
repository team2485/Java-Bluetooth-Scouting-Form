package org.first.team2485.scoutingform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.first.team2485.scoutingform.bluetooth.BluetoothPanel;


/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class SubmitButton extends JButton implements ActionListener {
	
	private ScoutingForm form;
	
	public SubmitButton(ScoutingForm form) {
		
		super("Submit");
		
		this.form = form;
		this.addActionListener(this);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		String dataToSend = form.submit();
		String fileName = "ScoutForm" + (System.currentTimeMillis() % 1000000000) + ".csv";
		
		new BluetoothPanel(fileName, dataToSend);
		
		form.getFrame().dispose();
		
	}
	
}
