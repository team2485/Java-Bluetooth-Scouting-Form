package org.first.team2485.scoutingform;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;

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
import org.first.team2485.scoutingform.gui.addons.GamblingPanel;
//import org.first.team2485.scoutingform.gui.addons.GamblingPanel;
import org.first.team2485.scoutingform.gui.addons.LookAndFeelSelector;
import org.first.team2485.scoutingform.questions.CheckboxQuestion;
import org.first.team2485.scoutingform.questions.FreeResponseQuestion;
import org.first.team2485.scoutingform.questions.LocationQuestion;
import org.first.team2485.scoutingform.questions.MultipleChoiceQuestion;
import org.first.team2485.scoutingform.questions.QuestionAligner;
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

	private static String scoutName;

	public static void main(String[] args) {
		init();
		displayForm();
	}

	/**
	 * Runs once and IS NEVER CALLED AGAIN EVER EVER ERVER!!!!!!!!!!!1
	 */
	public static void init() {

		while (scoutName == null) {
			scoutName = JOptionPane.showInputDialog(null, "Enter your name", "Name", JOptionPane.QUESTION_MESSAGE);
		}

		LookAndFeelSelector.addAdditonalLaFs();
	}

	/**
	 * Runs on LaF switch
	 */
	public static void displayForm() {

		//@formatter:off
		
		setUIFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 20));

		ScoutingFormTab prematch = new ScoutingFormTab("Prematch",
				new QuestionAligner(
						new SpinnerQuestion("Team Number", "teamNumber"),
						new SpinnerQuestion("Match Number", "matchNumber")
				)
		);
		
		ScoutingFormTab auto = new ScoutingFormTab("Automous", 
				new CheckboxQuestion("Did they...", "autoAction", "Cross the Baseline", "Enter Opponents Launchpad"),
				new MultipleChoiceQuestion ("Hoppers Dumped in Auto", "autoHoppersDumped", true, "0", "1", "2", "3", "4"),
				new MultipleChoiceQuestion ("Hopper Reload", "hopperReload", false, "Yes", "No"),

				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals", "autoLow"),
						new SpinnerQuestion("High Goals", "autoHigh")
				),
				new QuestionSeperator(),
				new LocationQuestion("If they shot high goals, where did they shoot from?", "autoShootingPos", "field.png"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("Gear State", "gearState", false, "Did not attempt", "Ran out of time", "Dropped", "Sucess"),
				new MultipleChoiceQuestion("Gear Hook Used", "autoGearPos", false, "Boiler Side Hook", "Center Hook", "Feeder Side Hook")		
		);

		ScoutingFormTab duringMatch = new ScoutingFormTab("During Match",
				new MultipleChoiceQuestion ("Hoppers Dumped in Teleop", "teleopHoppersDumped", true, "0", "1", "2", "3", "4"),
				new QuestionSeperator(),
				new SpinnerQuestion("Gears Scored", "gearsScored"),
				new QuestionSeperator(),
				new SpinnerQuestion("Average Time per Gear Cycle", "gearCycleTime"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("Gear Pickup", "gearPickup", false, "Floor", "Loading Station", "Both", "Neither"),		
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals", "teleopLow"),
						new SpinnerQuestion("High Goals", "teleopHigh")
				),
				new SpinnerQuestion("Number of Fuel Cycles", "shootingCycles"),
				new SpinnerQuestion("Average Time per Fuel Cycle", "shootingCycleTime"),
				new LocationQuestion("Where does this robot shoot from?", "teleopShootingPos", "field.png"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("What type of defense did they play?", "defenseType", false, "Purposeful", "On the way", "None"),			
				new SpinnerQuestion("How long did they play defense?", "defenseTime"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("Climbing", "climber", false, "Climbed", "Fell Off", "Ran Out Of Time", "No Attempt"),
				new SpinnerQuestion("Time from beginning of climb to end of match", "climberTime")
		);

		ScoutingFormTab postMatch = new ScoutingFormTab("Post Match",
				new QuestionSeperator(),
				new QuestionAligner(
						new MultipleChoiceQuestion("Intake Speed", "intakeSpeed", true, "Snail", "Slow", "Average", "Speedy",
								"Lightning", "N/A"),
						new MultipleChoiceQuestion("Manueverability", "manueverability", true, "Sluggish", "Unresponsive",
								"Average", "Responsive", "Nimble"),
						new MultipleChoiceQuestion ("Hopper Intake", "hopperIntake", true, "Useless", "Unreliable", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion ("Loading Station Intake", "loadingStationIntake", true, "Useless", "Unreliable", "Average", "Good", "Excellent", "N/A"),

						new MultipleChoiceQuestion ("Ground Intake", "groundIntake", true, "Useless", "Unreliable", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion("Shooter Accuracy", "shooterAccuracy", true, "Unreliable", "Sketchy",
								"Average", "Consistent", "Reliable", "N/A"),
						new MultipleChoiceQuestion("Shooter Speed", "shooterSpeed", true, "Snail", "Slow", "Average",
								"Speedy", "Lightning", "N/A"),
						new MultipleChoiceQuestion("Driver Skill", "driverSkill", true, "Hopeless", "Bad", "Average", "Skilled", "God-like"),
						new MultipleChoiceQuestion("Overall Impression", "overall", true, "Shitty", "Decent", "Average",
								"Good", "Badass")),
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("How many tech fouls did they get?", "techFouls"),
						new SpinnerQuestion("How many other fouls did they get?", "normalFouls")
				),
				new MultipleChoiceQuestion("Did they break down?", "breakDown", false, "Yes", "No"),
				new FreeResponseQuestion("Comments", "comments")
		);
		
		//@formatter:on

		new ScoutingForm(prematch, auto, duringMatch, postMatch);
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
		outerPanel.add(new JScrollPane(new GamblingPanel(this)), BorderLayout.EAST);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout());
		buttonPane.add(new SubmitButton(this));
		buttonPane.add(new QuitButton(this.frame));// this handles all quitting
													// logic
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
		
		output += "name," + this.scoutName;
		
		System.out.println(output);

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

	public String getScoutName() {
		return scoutName;
	}

	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value != null && value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}
}
