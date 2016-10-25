package org.first.team2485.scoutingform.questions;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

import org.first.team2485.scoutingform.gui.LockedSizeJPanel;
import org.first.team2485.scoutingform.gui.QuestionAligner;

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
