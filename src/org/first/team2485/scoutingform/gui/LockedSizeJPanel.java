package org.first.team2485.scoutingform.gui;

import java.awt.Dimension;

import javax.swing.JPanel;

/**
* @author Nicholas Contreras
*/

@SuppressWarnings("serial")
public class LockedSizeJPanel extends JPanel {
	
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}

}
