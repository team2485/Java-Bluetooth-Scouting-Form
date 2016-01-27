import javax.swing.JCheckBox;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class CheckboxQuestion extends Question {
		
	JLabel promptLabel;
	JCheckBox[] checkboxes;
	
	public CheckboxQuestion(String prompt, String[] options) {
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
}
