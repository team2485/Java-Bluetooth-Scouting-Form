package org.first.team2485.scoutingform.questions;
import javax.swing.JLabel;
import javax.swing.JTextArea;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class FreeResponseQuestion extends Question {
		
	private JLabel promptLabel;
	private JTextArea area;
	
	public FreeResponseQuestion(String prompt, String internalName) {
		
		super(internalName);
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		area = new JTextArea(5, 50);
		this.add(area);
	}
	
	public FreeResponseQuestion(String[] promptAndTooltip, String internalName) {
		
		this(promptAndTooltip[0], internalName);
		
		this.setToolTipText(promptAndTooltip[1]);
		
	}
	
	public String getData() {
		return getInternalName() + ",\"" + area.getText().replaceAll(",", "%2C") + "\",";
	}
	public void clear() {
		this.area.setText("");
	}
	
}
