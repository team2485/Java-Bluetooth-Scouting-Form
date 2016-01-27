package org.first.team2485.scoutingform;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;


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
		
		String output = form.submit();
		System.out.println(output);
		String filename = "ScoutForm" + (System.currentTimeMillis() % 1000000000) + ".csv";
		//TODO send via bluetooth
		
	}
	
}
