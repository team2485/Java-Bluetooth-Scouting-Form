package org.first.team2485.scoutingform.questions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.jdesktop.swingx.JXCollapsiblePane;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class QuestionGroup extends Question {

	private final JXCollapsiblePane pane;
	private final JCheckBox checkbox;
	private final Question[] questions;

	public QuestionGroup(String title, Question... questions) {

		this.setLayout(new BorderLayout());

		checkbox = new JCheckBox(title);
		checkbox.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		
		Box b = new Box(BoxLayout.Y_AXIS);
		b.add(checkbox);
		JSeparator seperator = new JSeparator(SwingConstants.HORIZONTAL);
		seperator.setMaximumSize(new Dimension(Integer.MAX_VALUE, 1) );
//		b.add(seperator);
		this.add(b, BorderLayout.NORTH);

		pane = new JXCollapsiblePane();
		pane.setLayout(new BoxLayout(pane.getContentPane(), BoxLayout.Y_AXIS));
		// pane.setLayout(new GridLayout(0, 1));
		for (Question question : questions) {
			question.setAlignmentX(Component.LEFT_ALIGNMENT);
			pane.add(question);
		}
		pane.setCollapsed(true);
		
		this.add(pane, BorderLayout.CENTER);
		
		this.questions = questions;
	}

	public void update() {
		pane.setCollapsed(!checkbox.isSelected());
	}

	@Override
	public String getData() {
		
		String data = "";
		
		for (Question q :  questions) {
			data += q.getData();
		}
		
		return data;
	}

	@Override
	public void clear() {
		System.out.println(questions.length);
		for (int i = 0; i < questions.length; i++) {
			System.out.println(questions[i].getData());
			questions[i].clear();
		}
		this.pane.setCollapsed(true);
		this.checkbox.setSelected(false);
	}
}
