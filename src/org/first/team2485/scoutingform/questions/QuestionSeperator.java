package org.first.team2485.scoutingform.questions;

import java.awt.BorderLayout;
import java.awt.Dimension;

import javax.swing.JSeparator;
import javax.swing.SwingConstants;

/**
 * 
 * @author Nicholas Contreras
 *
 */

@SuppressWarnings("serial")
public class QuestionSeperator extends Question {

	public QuestionSeperator() {

		this.setLayout(new BorderLayout());

		JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
		seperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1));
		this.add(seperator, BorderLayout.NORTH);
	}

	@Override
	public Dimension getMaximumSize() {
		return new Dimension(Integer.MAX_VALUE, 1);
	}

	@Override
	public String getData() {
		return "";
	}

	@Override
	public void clear() {
	}
}
