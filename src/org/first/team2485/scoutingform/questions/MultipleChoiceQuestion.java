package org.first.team2485.scoutingform.questions;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class MultipleChoiceQuestion extends Question {
	
	private JLabel promptLabel;
	private ButtonGroup optionButtonGroup;
	private JRadioButton[] optionButtons;
	
	private String internalName;
	
	public MultipleChoiceQuestion(String prompt, String internalName, String... options) {
		
		this.internalName = internalName;
		
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
				return internalName + "," + i + ",";
			}
		}
		return internalName + "," + "-1,";
	}
	public void clear() {
		optionButtonGroup.clearSelection();
	}
	
}
