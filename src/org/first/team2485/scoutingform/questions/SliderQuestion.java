package org.first.team2485.scoutingform.questions;

import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.Hashtable;

import javax.swing.JLabel;
import javax.swing.JSlider;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

/**
 * @author Nicholas Contreras
 */

public class SliderQuestion extends Question {

	private JSlider slider;
	private JTextField valueDisplay;

	private int min, max;
	private int startValue;

	private int scaleFactor;
	
	private String postFix;

	public SliderQuestion(String[] promptAndTooltip, String internalName, String postFix, int scaleFactor, int min,
			int max, int startValue, double majorLabels, double minorLabels) {
		this(promptAndTooltip[0], internalName, postFix, scaleFactor, min, max, startValue, majorLabels, minorLabels);
		this.setToolTipText(promptAndTooltip[1]);
	}

	@SuppressWarnings("unchecked")
	public SliderQuestion(String question, String internalName, String postFix, int scaleFactor, int min, int max,
			int startValue, double majorLabels, double minorLabels) {
		super(internalName);

		this.scaleFactor = scaleFactor;

		this.add(new JLabel(question));

		slider = new JSlider(SwingConstants.HORIZONTAL, min * scaleFactor, max * scaleFactor, startValue * scaleFactor);
		
		slider.setMinorTickSpacing((int) (minorLabels * scaleFactor));
		slider.setMajorTickSpacing((int) (majorLabels * scaleFactor));
		slider.setPaintTicks(true);
		slider.setPaintLabels(true);

		Hashtable<Integer, JLabel> labels = (Hashtable<Integer, JLabel>) slider.getLabelTable();
		Hashtable<Integer, JLabel> newLabels = new Hashtable<Integer, JLabel>();

		for (Integer key : labels.keySet()) {
			if (key % scaleFactor == 0) {
				newLabels.put(key, new JLabel((key / scaleFactor) + ""));
			}
		}

		slider.setLabelTable(newLabels);

		slider.addChangeListener((ChangeEvent) -> {
			double value = (double) slider.getValue() / scaleFactor;
			boolean isInt = value == (int) value;

			String text;

			if (isInt) {
				text = (int) value + "";
			} else {
				text = value + "";
			}

			valueDisplay.setText(text + postFix);
		});

		this.add(slider);

		valueDisplay = new JTextField(((max * scaleFactor) + "").length() + postFix.length());
		valueDisplay.setText(startValue + postFix);
		valueDisplay.setHorizontalAlignment(JTextField.CENTER);

		valueDisplay.addFocusListener(new FocusListener() {
			@Override
			public void focusLost(FocusEvent e) {
				setSliderOffText();
			}

			@Override
			public void focusGained(FocusEvent e) {
			}
		});

		valueDisplay.addActionListener((ActionEvent) -> setSliderOffText());

		this.add(valueDisplay);
		
		this.min = min;
		this.max = max;
		this.startValue = startValue;
		
		this.postFix = postFix;
	}

	private void setSliderOffText() {
		double value = Double.NaN;

		try {
			value = Double.parseDouble(valueDisplay.getText());
		} catch (NumberFormatException e) {
			try {
				value = Double.parseDouble(
						valueDisplay.getText().substring(0, valueDisplay.getText().length() - postFix.length()));
			} catch (NumberFormatException e2) {
			}
		}

		if (!Double.isNaN(value)) {

			if (value >= min && value <= max) {
				slider.setValue((int) (value * scaleFactor));
			}
		}
	}

	@Override
	public String getData() {
		return getInternalName() + "," + ((double) slider.getValue() / scaleFactor) + ",";
	}

	@Override
	public void clear() {
		slider.setValue(startValue);
	}
}
