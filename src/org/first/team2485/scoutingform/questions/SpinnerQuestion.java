package org.first.team2485.scoutingform.questions;

import java.awt.BorderLayout;
import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JSpinner.DefaultEditor;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.plaf.UIResource;
import javax.swing.plaf.basic.BasicArrowButton;
/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class SpinnerQuestion extends Question {

	private JLabel promptLabel;
	private JSpinner spinner;

	private final int PADDING = 0;

	public SpinnerQuestion(String prompt, String internalName) {
		this(prompt, internalName, Integer.MIN_VALUE);
	}

	public SpinnerQuestion(String prompt, String internalName, int min) {
		this(prompt, internalName, min, Integer.MAX_VALUE);
	}

	public SpinnerQuestion(String prompt, String internalName, int min, int max) {
		this(prompt, internalName, min, max, min);
	}

	public SpinnerQuestion(String prompt, String internalName, int min, int max, int startValue) {
		super(internalName);
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);

		spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(4);
					
		this.setBorder(new EmptyBorder(PADDING, 0, PADDING, 0));

		spinner.setValue(startValue);
	    
	    this.add(spinner);
	}
	
	public SpinnerQuestion(String[] promptAndTooltip, String internalName, int min, int max) {
		this(promptAndTooltip[0], internalName, min, max);
		
		this.setToolTipText(promptAndTooltip[1]);
		
	}
	
	public String getData() {
		return getInternalName() + "," + ((int) spinner.getValue()) + ",";
	}

	public void clear() {
		spinner.setValue(0);
	}
	 
}
