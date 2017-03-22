package org.first.team2485.scoutingform.questions;

import javax.swing.Box;
import javax.swing.BoxLayout;
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

	private boolean isNumerical;

	public MultipleChoiceQuestion(String prompt, String internalName, boolean numerical, String... options) {

		super(internalName);
		
		this.isNumerical = numerical;

		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		optionButtonGroup = new ButtonGroup();
		Box bob = new Box(BoxLayout.X_AXIS);

		optionButtons = new JRadioButton[options.length];
		for (int i = 0; i < options.length; i++) {
			optionButtons[i] = new JRadioButton(options[i]);
			bob.add(optionButtons[i]);
			bob.add(Box.createHorizontalStrut(5));
			optionButtonGroup.add(optionButtons[i]);
		}
		this.add(bob);
	}
	
	public MultipleChoiceQuestion(String[] promptAndTooltip, String internalName, boolean numerical, String... options) {
		this(promptAndTooltip[0],  internalName,  numerical, options);
		this.setToolTipText(promptAndTooltip[1]);
	}

	public String getData() {
		for (int i = 0; i < optionButtons.length; i++) {
			if (optionButtons[i].isSelected()) {
				if (optionButtons[i].getText().equals("N/A")) {
					return getInternalName() + ",-1,";
				}
				if (isNumerical) {
					return getInternalName() + "," + i + ",";
				} else {
					return getInternalName() + "," + optionButtons[i].getText() + ",";
				}
			}
		}
		return getInternalName() + "," + "-1,";
	}

	public void clear() {
		optionButtonGroup.clearSelection();
	}
}
