package org.first.team2485.scoutingform.questions;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.border.EmptyBorder;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class QuestionAligner extends Question {

	private Question[] questions;
	private final int HORIZONTAL_SPACING = 15;
	private final int VERTICAL_PADDING = 10;

	public QuestionAligner(Question... questions) {
		int numComps = -1;
		
		for (Question q : questions) {
			Component[] comps = q.getComponents();

			if (comps.length > numComps) {
				numComps = comps.length;
			}
			
		}
		
		int[] widths = new int[numComps];
		
		//Set widths of grid cells so that left-align works
		for (int i = 0; i < questions.length; i++) {
			Component[] questionComps = questions[i].getComponents();
			
			for (int j = 0; j < numComps; j++ ) {
				
				Component curComp = j < questionComps.length ? questionComps[j]
						: Box.createRigidArea(new Dimension(1, 1));

				if (curComp.getPreferredSize().width > widths[j]) {
					widths[j] = curComp.getPreferredSize().width;
				}
				
			}
			
		}


		this.setLayout(new GridBagLayout());

		for (int i = 0; i < questions.length; i++) {

			Component[] questionComps = questions[i].getComponents();

			for (int j = 0; j < numComps; j++) {
				
				GridBagConstraints c = new GridBagConstraints();
				c.gridy = i;
				c.gridx = j;
				c.ipadx = 4;
				c.ipady = 4;
				
				Component curComp = j < questionComps.length ? questionComps[j]
						: Box.createRigidArea(new Dimension(1, 1));

				Box b = new Box(BoxLayout.X_AXIS);
				b.add(Box.createHorizontalStrut(HORIZONTAL_SPACING));
				b.add(curComp);
				b.setPreferredSize(new Dimension(widths[j] + HORIZONTAL_SPACING, b.getPreferredSize().height));
				b.setMaximumSize(new Dimension(widths[j] + HORIZONTAL_SPACING, b.getPreferredSize().height));
				b.setAlignmentX(Box.LEFT_ALIGNMENT);
				
				this.setBorder(new EmptyBorder(VERTICAL_PADDING, 0, VERTICAL_PADDING, 0));
				
				this.add(b, c);
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
