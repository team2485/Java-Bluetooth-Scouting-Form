package org.first.team2485.scoutingform.questions;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;

import javax.swing.Box;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class QuestionAligner extends Question {

	private Question[] questions;

	public QuestionAligner(Question... questions) {

		super("");

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

				Container c = new Container();
				c.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 2));
				c.add(curComp);
				this.add(c);
			}
		}
		this.questions = questions;
	}

	public Question[] getQuestions() {
		return questions;
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
