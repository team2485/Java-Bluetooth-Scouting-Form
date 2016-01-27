package org.first.team2485.scoutingform.questions;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.border.Border;

import org.jdesktop.swingx.JXCollapsiblePane;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class QuestionGroup extends Question {
	 
	JXCollapsiblePane pane;
	JCheckBox checkbox;
	Question[] questions;
	
	public QuestionGroup(boolean hasButton, String title, Question... questions) {
		
		this.setLayout(new BorderLayout());
		
		checkbox = new JCheckBox(title);
		checkbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		this.add(checkbox, BorderLayout.CENTER);
		
		pane = new JXCollapsiblePane();
		for (Question question : questions) {
			pane.add(question);
		}
		pane.setCollapsed(true);
		pane.setBorder(BorderFactory.createLineBorder(Color.BLACK));
		this.add(pane, BorderLayout.SOUTH);
		
	}
	
	public void update() {
		
		pane.setCollapsed(!checkbox.isSelected());
		
	}
	
	@Override
	public String getData() {
		return "";
	}
}
