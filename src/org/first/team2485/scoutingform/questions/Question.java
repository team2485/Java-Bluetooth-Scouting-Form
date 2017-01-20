package org.first.team2485.scoutingform.questions;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;

import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public abstract class Question extends LockedSizeJPanel {
	public abstract String getData();
	public abstract void clear();
	
	public Question() {
		this.setLayout(new FlowLayout());
//		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, super.getPreferredSize().height);
	}
}
