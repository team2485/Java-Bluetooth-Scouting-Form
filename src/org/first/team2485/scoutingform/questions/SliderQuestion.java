package org.first.team2485.scoutingform.questions;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.SwingConstants;

/**
 * @author Nicholas Contreras
 */

public class SliderQuestion extends Question {

	private JSlider slider;
	
	private int startValue;

	public SliderQuestion(String[] promptAndTooltip, String internalName, int min, int max, int startValue) {
		this(promptAndTooltip[0], internalName, min, max, startValue);
		this.setToolTipText(promptAndTooltip[1]);
	}

	public SliderQuestion(String question, String internalName, int min, int max, int startValue) {
		super(internalName);

		this.add(new JLabel(question));

		slider = new JSlider(SwingConstants.HORIZONTAL, min, max, startValue);
		slider.setMajorTickSpacing(10);
		slider.setMinorTickSpacing(1);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		this.add(slider);
		
		this.startValue = startValue;
	}

	@Override
	public String getData() {
		return getInternalName() + "," + slider.getValue() + ",";
	}

	@Override
	public void clear() {
		slider.setValue(startValue);
	}
}
