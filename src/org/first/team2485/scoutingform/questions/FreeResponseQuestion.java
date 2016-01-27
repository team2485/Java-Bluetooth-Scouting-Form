package org.first.team2485.scoutingform.questions;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JTextArea;


@SuppressWarnings("serial")
public class FreeResponseQuestion extends Question {
		
	JLabel promptLabel;
	ButtonGroup optionButtonGroup;
	JTextArea area;
	
	public FreeResponseQuestion(String prompt) {
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		area = new JTextArea(5, 50);
		this.add(area);
	}
	
	public String getData() {
		return "\"" + area.getText() + "\",";
	}
	
}
