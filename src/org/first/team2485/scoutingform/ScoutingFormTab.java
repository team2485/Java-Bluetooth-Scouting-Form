package org.first.team2485.scoutingform;
import java.awt.Component;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;

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

	public ScoutingFormTab(String name, Question... questions) {
		
		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		
		this.name = name;

		for (JComponent item : questions) {
			item.setAlignmentX(Component.LEFT_ALIGNMENT);
			this.add(item);
		}
		this.questions = questions;
		
		
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
	
	public Question[] getQuestions() {
		return questions;
	}
}
