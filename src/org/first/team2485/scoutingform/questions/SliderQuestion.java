package org.first.team2485.scoutingform.questions;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeEvent;

/**
 * @author Nicholas Contreras
 */

public class SliderQuestion extends Question {

	private JSlider slider;
	private JTextField valueDisplay;

	private int startValue;

	public SliderQuestion(String[] promptAndTooltip, String internalName, int min, int max, int startValue) {
		this(promptAndTooltip[0], internalName, min, max, startValue);
		this.setToolTipText(promptAndTooltip[1]);
	}

	public SliderQuestion(String question, String internalName, int min, int max, int startValue) {
		super(internalName);

		this.add(new JLabel(question));

		slider = new JSlider(SwingConstants.HORIZONTAL, min, max, startValue);
		slider.setMajorTickSpacing(25);
		slider.setMinorTickSpacing(5);
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		slider.addChangeListener((ChangeEvent) -> {
			valueDisplay.setText(slider.getValue() + "%");
		});

		this.add(slider);

		valueDisplay = new JTextField((max + "").length() + 1);
		valueDisplay.setText(startValue + "%");
		valueDisplay.setHorizontalAlignment(JTextField.CENTER);

		valueDisplay.addActionListener((ActionEvent) -> {
			double value = Double.NaN;

			try {
				value = Integer.parseInt(valueDisplay.getText());
			} catch (NumberFormatException e) {
				try {
					value = Integer.parseInt(valueDisplay.getText().substring(0, valueDisplay.getText().length() - 1));
				} catch (NumberFormatException e2) {
				}
			}

			if (!Double.isNaN(value)) {
				if (value >= min && value <= max) {
					slider.setValue((int) value);
				}
			}
		});

		this.add(valueDisplay);
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
