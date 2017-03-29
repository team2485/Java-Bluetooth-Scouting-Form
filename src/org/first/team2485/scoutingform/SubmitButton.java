package org.first.team2485.scoutingform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;

import org.first.team2485.scoutingform.bluetooth.BluetoothPanel;
import org.first.team2485.scoutingform.util.Logger;

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

		String dataToSend = form.getScoutName() + "," + System.currentTimeMillis() + "," + form.submit();
		String fileName = "ScoutingData~" + form.getScoutName() + "^" + System.currentTimeMillis() + ".sfd";

		System.out.println(dataToSend);
		
		Logger.getInst().log("Starting the bluetooth panel");
		
		new BluetoothPanel(fileName, dataToSend);
		
		Logger.getInst().log("disposing the form window");

		form.getFrame().dispose();

	}

}
