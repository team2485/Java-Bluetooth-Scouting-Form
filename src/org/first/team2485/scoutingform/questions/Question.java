package org.first.team2485.scoutingform.questions;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public abstract class Question extends JPanel {
	public abstract String getData();
	public abstract void clear();
	
	public Question() {
//		this.setBorder(BorderFactory.createLineBorder(Color.BLUE, 2, true));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, super.getPreferredSize().height);
	}
	
	public Dimension getMaximumSize() {
		return getPreferredSize();
	}
	public Dimension getMinimumSize() {
		return getPreferredSize();
	}
}
