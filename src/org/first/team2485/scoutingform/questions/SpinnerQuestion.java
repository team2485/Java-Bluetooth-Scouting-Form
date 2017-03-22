package org.first.team2485.scoutingform.questions;

import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class SpinnerQuestion extends Question {

	private JLabel promptLabel;
	private JSpinner spinner;
	private int startValue;

	public SpinnerQuestion(String prompt, String internalName) {
		this(prompt, internalName, 0);
	}

	public SpinnerQuestion(String prompt, String internalName, int min) {
		this(prompt, internalName, min, 9999);
	}

	public SpinnerQuestion(String prompt, String internalName, int min, int max) {
		this(prompt, internalName, min, max, 0);
	}

	public SpinnerQuestion(String prompt, String internalName, int min, int max, int startValue) {
		super(internalName);

		promptLabel = new JLabel(prompt);
		this.add(promptLabel);

		spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
		JSpinner.NumberEditor editor = new JSpinner.NumberEditor(spinner, "#");
		spinner.setEditor(editor);
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns((max + "").length());

		spinner.setValue(startValue);

		this.add(spinner);

		this.startValue = startValue;
	}

	public SpinnerQuestion(String[] promptAndTooltip, String internalName, int min, int max, int startValue) {
		this(promptAndTooltip[0], internalName, min, max, startValue);

		this.setToolTipText(promptAndTooltip[1]);
	}

	public SpinnerQuestion(String[] promptAndTooltip, String internalName) {
		this(promptAndTooltip, internalName, 0, 9999, 0);
	}

	public String getData() {
		return getInternalName() + "," + ((int) spinner.getValue()) + ",";
	}

	public void clear() {
		spinner.setValue(startValue);
	}

}
