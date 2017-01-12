package org.first.team2485.scoutingform.questions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class QuestionAligner extends Question {

	private Question[] questions;

	public QuestionAligner(Question... questions) {
		int numComps = -1;

		for (Question q : questions) {
			Component[] comps = q.getComponents();

			if (comps.length > numComps) {
				numComps = comps.length;
			}
		}

		this.setLayout(new GridLayout(questions.length, numComps));

		for (int i = 0; i < questions.length; i++) {

			Component[] questionComps = questions[i].getComponents();

			for (int j = 0; j < numComps; j++) {

				Component curComp = j < questionComps.length ? questionComps[j]
						: Box.createRigidArea(new Dimension(1, 1));

				Box b = new Box(BoxLayout.X_AXIS);
				b.add(Box.createHorizontalStrut(5));
				b.add(curComp);
				this.add(b);
			}
		}

		this.questions = questions;
	}

	@Override
	public String getData() {
		String data = "";

		for (Question q : questions) {
			data += q.getData();
		}

		return data;
	}

	@Override
	public void clear() {
		for (Question q : questions) {
			q.clear();
		}
	}
}
