package org.first.team2485.scoutingform.questions;

import java.awt.Dimension;
import java.awt.FlowLayout;

import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public abstract class Question extends LockedSizeJPanel {
	
	private String internalName;
	
	public Question(String internalName) {
		this.internalName = internalName;
		this.setLayout(new FlowLayout());
	}
	
	public abstract String getData();

	public abstract void clear();

	public Dimension getPreferredSize() {
		return new Dimension(super.getPreferredSize().width, super.getPreferredSize().height);
	}

	public String getInternalName() {
		return internalName;
	}
}
