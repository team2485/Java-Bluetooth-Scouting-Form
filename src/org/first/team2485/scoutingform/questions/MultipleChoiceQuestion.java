package org.first.team2485.scoutingform.questions;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.SwingConstants;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
public class MultipleChoiceQuestion extends Question {
	
	private static final long serialVersionUID = -3014924975352089209L;
	
	JLabel promptLabel;
	ButtonGroup optionButtonGroup;
	JRadioButton[] optionButtons;
	
	public MultipleChoiceQuestion(String prompt, String... options) {
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
//		this.add(Box.createRigidArea(new Dimension(10, 0)));
//		this.add(Box.createHorizontalGlue());
//		this.add(Box.createRigidArea(new Dimension(10, 0)));
		
		optionButtonGroup = new ButtonGroup();
		
		optionButtons = new JRadioButton[options.length];
		for (int i = 0; i < options.length; i++) {
			optionButtons[i] = new JRadioButton(options[i]);
//			optionButtons[i].setAlignmentX(Component.RIGHT_ALIGNMENT);
			this.add(optionButtons[i]);
			optionButtonGroup.add(optionButtons[i]);
		}
	}
	
	public String getData() {
		for (int i = 0; i < optionButtons.length; i++) {
			if (optionButtons[i].isSelected()) {
				return i + ",";
			}
		}
		return "-1,";
	}
	public void clear() {
		optionButtonGroup.clearSelection();
	}
	
}
