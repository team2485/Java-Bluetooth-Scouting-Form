package org.first.team2485.scoutingform;

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

		this.add(new SubmitButton(this));
		this.add(new QuitButton(this.frame));//this handles all quitting logic

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
	

	public static void main(String[] args) {

		ScoutingFormTab prematch = new ScoutingFormTab("Prematch",
			new SpinnerQuestion("Team number"),
			new SpinnerQuestion("Match Number:")
		);
		
		ScoutingFormTab autonomous = new ScoutingFormTab("Autonomous", 
			new CheckboxQuestion("Autonomous", "Approach defense", "Cross Defense", "Score Boulder")
		);
		
		ScoutingFormTab teleop = new ScoutingFormTab("Teleop", 
			new QuestionGroup(false, "Defense Category A", 
				new MultipleChoiceQuestion("How many seconds did it take?", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
				new SpinnerQuestion("How many times did they cross it?")
			),
			new FreeResponseQuestion("Comments: ")
		);//<--- sad winky face
		
		new ScoutingForm(prematch, autonomous, teleop);
		
	}
}
