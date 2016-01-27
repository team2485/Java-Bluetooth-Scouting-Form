import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;


@SuppressWarnings("serial")
public class SpinnerQuestion extends Question{
		
	JLabel promptLabel;
	ButtonGroup optionButtonGroup;
	JSpinner spinner;
	
	public SpinnerQuestion(String prompt) {
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		spinner = new JSpinner(new SpinnerNumberModel());
		this.add(spinner);
	}
	
	public String getData() {
		return "" + (int) spinner.getValue();
	}
}
