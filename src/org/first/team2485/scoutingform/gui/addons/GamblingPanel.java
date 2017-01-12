package org.first.team2485.scoutingform.gui.addons;

import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;

import org.first.team2485.scoutingform.ScoutingForm;
import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

/**
* @author Nicholas Contreras
*/

public class GamblingPanel extends LockedSizeJPanel {
	
	private ScoutingForm form;
	
	public GamblingPanel(ScoutingForm form) {
		this.form = form;
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.add(new JLabel("Gamble on Future Games"));
	}

}
