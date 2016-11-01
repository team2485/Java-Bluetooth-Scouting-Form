package org.first.team2485.scoutingform;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import org.first.team2485.scoutingform.gui.LockedSizeJPanel;
import org.first.team2485.scoutingform.gui.addons.LookAndFeelSelector;
import org.first.team2485.scoutingform.questions.CheckboxQuestion;
import org.first.team2485.scoutingform.questions.FreeResponseQuestion;
import org.first.team2485.scoutingform.questions.MultipleChoiceQuestion;
import org.first.team2485.scoutingform.questions.QuestionAligner;
import org.first.team2485.scoutingform.questions.QuestionGroup;
import org.first.team2485.scoutingform.questions.QuestionSeperator;
import org.first.team2485.scoutingform.questions.SpinnerQuestion;

/**
 * 
 * @author Jeremy McCulloch
 * @author Troy Appel
 *
 */
@SuppressWarnings("serial")
public class ScoutingForm extends LockedSizeJPanel {
	
	public static ScoutingForm scoutingForm;
  
	private JFrame frame;
	private ScoutingFormTab[] tabs;
	private JTabbedPane tabbedPane;
	
	public static void main(String[] args) {
		init();
		displayForm();
	}
	
	/**
	 * Runs once and IS NEVER CALLED AGAIN EVER EVER ERVER!!!!!!!!!!!1 
	 */
	public static void init() {
		LookAndFeelSelector.addAdditonalLaFs();
	}
	
	/**
	 * Runs on LaF switch
	 */
	public static void displayForm() {
			
		ScoutingFormTab prematch = new ScoutingFormTab("Prematch",
				new SpinnerQuestion("Team Number:", "teamNumber"),
				new QuestionSeperator(),
				new SpinnerQuestion("Match Number:", "matchNumber")
			);
			
			ScoutingFormTab duringMatch = new ScoutingFormTab("During Match",
				new QuestionGroup("Autonomous", 
						new CheckboxQuestion("Did they...", "autoAction", "Approach a Defense", "Cross a Defense"), 
						new SpinnerQuestion("How many high goals did they make?", "autoHigh"),
						new SpinnerQuestion("How many low goals did they make?", "autoLow")
				),
				new QuestionSeperator(),
				new QuestionGroup("Defense Category A", 
						new MultipleChoiceQuestion("Which defense is up?", "defA", "Cheval de Frise", "Portcullis"),
						new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "defATime", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
						new SpinnerQuestion("How many times did they cross it?", "defACount")
				),
				new QuestionSeperator(),
				new QuestionGroup("Defense Category B", 
						new MultipleChoiceQuestion("Which defense is up?", "defB", "Moat", "Ramparts"),
						new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "defBTime", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
						new SpinnerQuestion("How many times did they cross it?", "defBCount")
				),
				new QuestionSeperator(),
				new QuestionGroup("Defense Category C", 
						new MultipleChoiceQuestion("Which defense is up?", "defC", "Drawbridge", "Sally Port"),
						new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "defCTime", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
						new SpinnerQuestion("How many times did they cross it?", "defCCount")
				),
				new QuestionSeperator(),
				new QuestionGroup("Defense Category D", 
						new MultipleChoiceQuestion("Which defense is up?", "defD", "Rough Terrain", "Rock Wall"),
						new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "defDTime", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
						new SpinnerQuestion("How many times did they cross it?", "defDCount")
				),
				new QuestionSeperator(),
				new QuestionGroup("Low Bar", 
						new MultipleChoiceQuestion("How long did it take to cross on avg (seconds)?", "barTime", "0 - 5", "5 - 10", "10 - 15", "15+", "Failed"),
						new SpinnerQuestion("How many times did they cross it?", "barCount")
				),
				new QuestionSeperator(),
				new SpinnerQuestion("How many high goals did they make?", "highMade"),
				new SpinnerQuestion("How many high goals did they miss?", "highMiss"),
				new QuestionSeperator(),
				new SpinnerQuestion("How many low goals did they make?", "lowMade"),
				new SpinnerQuestion("How many low goals did they miss?", "lowMiss"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("End Game State:", "endGame", "Challenge", "Scale", "Neither")
			);
			
			ScoutingFormTab postMatch = new ScoutingFormTab("Post Match",
				new QuestionAligner(
					new MultipleChoiceQuestion("Intake Speed", "intakeSpeed", "Snail", "Slow", "Average", "Speedy", "Lightning", "N/A"),
					new MultipleChoiceQuestion("Manueverability", "manueverability", "Sluggish", "Unresponsive", "Average" , "Responsive", "Nimble"),
					new MultipleChoiceQuestion("Shooter Repeatability", "shooterRepeat", "Unreliable", "Sketchy", "Average", "Consistent", "Reliable", "N/A"), 
					new MultipleChoiceQuestion("Shooter Speed", "shooterSpeed", "Snail", "Slow", "Average", "Speedy", "Lightning", "N/A"),
					new MultipleChoiceQuestion("Defense", "defence", "Shitty", "Decent", "Average", "Good", "Badass", "N/A"), 
					new MultipleChoiceQuestion("Defense Evasion", "defenceEvade", "Shitty", "Decent", "Average", "Good", "Badass", "N/A"), 
					new MultipleChoiceQuestion("Overall Impression", "overall", "Shitty", "Decent", "Average", "Good", "Badass")
				),
				new SpinnerQuestion("How many tech fouls did they get?", "techFouls"),
				new SpinnerQuestion("How many other fouls did they get?", "normalFouls"),
				new CheckboxQuestion("What role(s) did they play?", "roles", "Shooter", "Breacher", "Defender", "Support"),
				new MultipleChoiceQuestion("Did they break down?", "breakDown", "Yes", "No"),
				new FreeResponseQuestion("Comments:", "comments")
			);
			
			new ScoutingForm(prematch, duringMatch, postMatch);	
	}

	public ScoutingForm(ScoutingFormTab... tabs) {
		
		scoutingForm = this;
		
		frame = new JFrame();
		
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		frame.add(outerPanel);
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		this.tabbedPane = new JTabbedPane();
		
		this.tabs = tabs;
		for (ScoutingFormTab tab : tabs) {
			tabbedPane.add(tab.getName(), new JScrollPane(tab));
		}
		
		this.add(tabbedPane);
		
		outerPanel.add(this, BorderLayout.CENTER);
		outerPanel.add(new JScrollPane(new LookAndFeelSelector(this)), BorderLayout.WEST);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(new SubmitButton(this));
		buttonPane.add(new QuitButton(this.frame));// this handles all quitting logic
		buttonPane.add(new ClearButton(tabs, this.frame));
		this.add(buttonPane);

		try {
			UIManager.setLookAndFeel(UIManager.getLookAndFeel());
		} catch (UnsupportedLookAndFeelException e) {
			e.printStackTrace();
		}
		SwingUtilities.updateComponentTreeUI(frame);
		
		frame.pack(); 
		frame.setVisible(true);
		frame.repaint(); 
		
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

	public void setLookAndFeel(String laf) {
		try {
			UIManager.setLookAndFeel(Class.forName(laf).newInstance().getClass().getCanonicalName());
			SwingUtilities.updateComponentTreeUI(frame);
			frame.pack();
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, "Something went very wrong loading that theme");
		}
	}
	
	public void restart() {
		frame.dispose();
		displayForm();
	}
}
