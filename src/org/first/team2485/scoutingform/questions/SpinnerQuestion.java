package org.first.team2485.scoutingform.questions;
import java.awt.Dimension;
import java.awt.Font;

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
public class SpinnerQuestion extends Question{
		
	private JLabel promptLabel;
	private JSpinner spinner;
	
	private String internalName;
	
	private final int PADDING = 0;
	
	public SpinnerQuestion(String prompt, String internalName) {
		
		this.internalName = internalName;
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		spinner = new JSpinner(new SpinnerNumberModel());
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(4);
		
		this.setBorder(new EmptyBorder(PADDING, 0, PADDING, 0));
				
		this.add(spinner);
	
	}
	
	public SpinnerQuestion(String prompt, String internalName, int min) {
		
		this.internalName = internalName;
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		spinner = new JSpinner(new SpinnerNumberModel(min, min, Integer.MAX_VALUE, 1));
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(4);
		
		this.setBorder(new EmptyBorder(PADDING, 0, PADDING, 0));
		
		this.add(spinner);
	
	}	
	public SpinnerQuestion(String prompt, String internalName, int min, int max) {
		
		this.internalName = internalName;
		
		promptLabel = new JLabel(prompt);
		this.add(promptLabel);
		
		spinner = new JSpinner(new SpinnerNumberModel(min, min, max, 1));
		((DefaultEditor) spinner.getEditor()).getTextField().setColumns(4);
		
		this.setBorder(new EmptyBorder(PADDING, 0, PADDING, 0));
		
		this.add(spinner);
	
	}
	
	public SpinnerQuestion(String[] promptAndTooltip, String internalName, int min, int max) {
		this(promptAndTooltip[0], internalName, min, max);
		
		this.setToolTipText(promptAndTooltip[1]);
		
	}
	
	public String getData() {
		return internalName + "," + ((int) spinner.getValue()) + ",";	
	}
	
	public void clear() {
		spinner.setValue(0);
	}
}
