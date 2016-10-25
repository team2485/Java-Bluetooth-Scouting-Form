package org.first.team2485.scoutingform.questions;
import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JLabel;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class CheckboxQuestion extends Question {
		
	JLabel promptLabel;
	JCheckBox[] checkboxes;
	
	public CheckboxQuestion(String prompt, String... options) {
		
//		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
				
		checkboxes = new JCheckBox[options.length];
		for (int i = 0; i < options.length; i++) {
			checkboxes[i] = new JCheckBox(options[i]);		
			this.add(checkboxes[i]);
		}
	}
	
	public String getData() {
		String data = "";
		for (int i = 0; i < checkboxes.length; i++) {
			data += checkboxes[i].isSelected() ? "1," : "0,";
		}
		return data;
	}
	
	public void clear() {
		for(int i = checkboxes.length - 1; i >= 0; i--) {
			checkboxes[i].setSelected(false);
		}
	}
}
