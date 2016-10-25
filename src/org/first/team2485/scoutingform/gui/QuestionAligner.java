package org.first.team2485.scoutingform.gui;

import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.JComponent;
import javax.swing.JPanel;

import org.first.team2485.scoutingform.questions.Question;
import org.first.team2485.scoutingform.questions.SpinnerQuestion;

/**
 * @author Nicholas Contreras
 */

public class QuestionAligner extends LockedSizeJPanel {

	public QuestionAligner(Question... questions) {
		int numComps = -1;

		for (Question q : questions) {
			Component[] comps = q.getComponents();

			if (numComps != -1 && numComps != comps.length) {
				throw new IllegalArgumentException("Cannot align questions with different numbers of elements");
			} else {
				numComps = comps.length;
			}
		}
		this.setLayout(new GridLayout(questions.length, numComps));

		for (int i = 0; i < questions.length; i++) {
			for (Component c : questions[i].getComponents()) {
				this.add(c);
			}
		}
	}
}
