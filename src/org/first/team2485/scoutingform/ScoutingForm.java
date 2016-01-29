package org.first.team2485.scoutingform;

import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

import org.first.team2485.scoutingform.questions.CheckboxQuestion;
import org.first.team2485.scoutingform.questions.FreeResponseQuestion;
import org.first.team2485.scoutingform.questions.MultipleChoiceQuestion;
import org.first.team2485.scoutingform.questions.QuestionGroup;
import org.first.team2485.scoutingform.questions.SpinnerQuestion;

/**
 * 
 * @author Jeremy McCulloch
 *
 */
@SuppressWarnings("serial")
public class ScoutingForm extends JPanel {
  
	private JFrame frame;
	private ScoutingFormTab[] tabs;
	private JTabbedPane tabbedPane;

	public ScoutingForm(ScoutingFormTab... tabs) {
		
		frame = new JFrame();
		frame.add(this);
		frame.setSize(1000, 600);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		this.tabbedPane = new JTabbedPane();
		
		this.tabs = tabs;
		for (ScoutingFormTab tab : tabs) {
			tabbedPane.add(tab.getName(), new JScrollPane(tab));
		}
		
		this.add(tabbedPane);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(new SubmitButton(this));
		buttonPane.add(new QuitButton(this.frame));//this handles all quitting logic
		this.add(buttonPane);


		frame.pack(); 
		frame.setVisible(true);
		this.repaint(); 
		
	}

	public String submit() {

		String output = "";

		for (ScoutingFormTab tab : tabs) {
			output += tab.getData();
		}

		return output;

	}
	
	public JFrame getFrame() {
		return frame;
	}

	public static void main(String[] args) {

		ScoutingFormTab prematch = new ScoutingFormTab("Prematch",
			new SpinnerQuestion("Team number"),
			new SpinnerQuestion("Match Number:")
		);
		
		ScoutingFormTab autonomous = new ScoutingFormTab("Autonomous", 
			new CheckboxQuestion("Autonomous", "Approach defense", "Cross Defense", 
					"Score Boulder in High Goal", "Score Boulder in Low Goal")
		);
		
		ScoutingFormTab teleop = new ScoutingFormTab("Teleop", 
			new QuestionGroup(false, "Defense Category A", 
				new MultipleChoiceQuestion("Which defense is up?", "Cheval de Frise", "Portcullis"),
				new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new QuestionGroup(false, "Defense Category B", 
				new MultipleChoiceQuestion("Which defense is up?", "Moat", "Ramparts"),
				new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new QuestionGroup(false, "Defense Category C", 
				new MultipleChoiceQuestion("Which defense is up?", "Drawbridge", "Sally Port"),
				new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new QuestionGroup(false, "Defense Category D", 
				new MultipleChoiceQuestion("Which defense is up?", "Rough Terrain", "Rock Wall"),
				new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new QuestionGroup(false, "Low Bar", 
				new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new SpinnerQuestion("How many high goals did they make?"),
			new SpinnerQuestion("How many low goals did they make?")
		);//<--- sad winky face 
		
		ScoutingFormTab misc = new ScoutingFormTab("Miscellaneous", 
			new SpinnerQuestion("How many tech fouls did they get?"),
			new SpinnerQuestion("How many other fouls did they get"),
			new FreeResponseQuestion("Comments:")
		);
		
		new ScoutingForm(prematch, autonomous, teleop, misc);
		
	}
}
