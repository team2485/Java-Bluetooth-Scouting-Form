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
	
	private boolean isNumerical;
		
	public MultipleChoiceQuestion(String prompt, String internalName, boolean numerical, String... options) {
		
		this.internalName = internalName;
		
		this.isNumerical = numerical;
				
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
	
	public MultipleChoiceQuestion(String[] promptAndTooltip, String internalName, boolean numerical, String... options) {
 
		this(promptAndTooltip[0],  internalName,  numerical, options);
		
		this.setToolTipText(promptAndTooltip[1]);

	
	}

	
	public String getData() {
		for (int i = 0; i < optionButtons.length; i++) {
			if (optionButtons[i].isSelected()) {
				if (optionButtons[i].getText().equals("N/A")) {
					return internalName + ",-1,";
				} if (isNumerical) {
					return internalName + "," + i + ",";
				} else {
					return internalName + "," + optionButtons[i].getText() + ",";
				}
			}
		}
		return internalName + "," + "-1,";
	}
	public void clear() {
		optionButtonGroup.clearSelection();
	}
	
}
