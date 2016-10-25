package org.first.team2485.scoutingform.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
* @author Nicholas Contreras
*/

public class LockedSizeJPanel extends JPanel {
	
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

}
