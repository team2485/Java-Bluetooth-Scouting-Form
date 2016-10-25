package org.first.team2485.scoutingform;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.util.ArrayList;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.SwingConstants;

import org.first.team2485.scoutingform.questions.Question;

/**
 * 
 * @author Jeremy McCulloch
 * @author Nicholas Contreras
 *
 */
@SuppressWarnings("serial")
public class ScoutingFormTab extends JPanel {
	
	private Question[] questions;
	private String name;

	public ScoutingFormTab(String name, JComponent... items) {
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.name = name;
		
		ArrayList<Question> questionAL = new ArrayList<Question>();

		for (JComponent item : items) {
			item.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(item);
			if (item instanceof Question) {
				questionAL.add((Question) item);
			}
		}
		questions = questionAL.toArray(new Question[0]);
	}
	
	public String getName() {
		return name;	
	}
	
	public String getData() {
		
		String output = "";

		for (Question question : questions) {
			output += question.getData();
		}

		return output;
		
	}
	
	public void clear() {
		for (int i = 0; i < questions.length; i++) {
			questions[i].clear();
		}
	}
	
	
	
}
