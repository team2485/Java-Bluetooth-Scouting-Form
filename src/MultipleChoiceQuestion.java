import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JRadioButton;


public class MultipleChoiceQuestion extends Question {
	
	private static final long serialVersionUID = -3014924975352089209L;
	
	JLabel promptLabel;
	ButtonGroup optionButtonGroup;
	JRadioButton[] optionButtons;
	
	public MultipleChoiceQuestion(String prompt, String[] options) {
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		optionButtonGroup = new ButtonGroup();
		
		optionButtons = new JRadioButton[options.length];
		for (int i = 0; i < options.length; i++) {
			optionButtons[i] = new JRadioButton(options[i]);		
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
	
	
}
