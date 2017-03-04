package org.first.team2485.scoutingform;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseWheelEvent;

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
						new SpinnerQuestion("Team Number", "teamNumber", 0, 9999),
						new SpinnerQuestion("Match Number", "matchNumber", 0, 9999)
				),
				new QuestionSeperator(),
				new CheckboxQuestion(new String[] {"No Show?", "Check this box if your robot is scheduled to play "
						+ "this match, but did not show up. Be careful! "
						+ "It could be that you just can't see it from "
						+ "where you are sitting."}, 
						"noShow", "")
				
		);
		
		ScoutingFormTab auto = new ScoutingFormTab("Automous", 
				new CheckboxQuestion(new String[] {"Did they...", "<html>CROSSING THE BASELINE means moving in front of the line defined"
						+ " by the back face of the airship."
						+ "<br>ENTERING THE OPPONENT'S LAUNCHPAD is crossing the line marked by the "
						+ "NEAR PLANE of the OPPONENT'S LAUNCHPAD. This is a foul. </html>"},
						"autoAction", "Cross the Baseline", "Enter Opponents Launchpad"),
				new MultipleChoiceQuestion (new String[] {"Hoppers Triggered in Auto", 
						"<html>A HOPPER is a pair of containers located just outside the FIELD and used "
						+ "to store FUEL at the start ofthe MATCH. "
						+ "<br>TRIGGERING a hopper is pressing the button on the hopper.</html>"}, "autoHoppersDumped", true, "0", "1", "2", "3", "4"),
				new MultipleChoiceQuestion (new String[] {"Hopper Reload",
						"<html>A HOPPER is a pair of containers located just outside the FIELD and used "
						+ "to store FUEL at the start ofthe MATCH. "
						+ "<br>Mark \"Yes\" if the team intook balls from a HOPPER during AUTO."
				}, "hopperReload", false, "Yes", "No"),

				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals Made in AUTO", "autoLow", 0),
						new SpinnerQuestion("High Goals Made in AUTO", "autoHigh", 0)
				),
				new QuestionSeperator(),
				new LocationQuestion("If they shot high goals, where did they shoot from?", "autoShootingPos", "field.png"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Gear State",
						"<html>RAN OUT OF TIME: The gear is on the LIFT, but was not pulled into the AIRSHIP before AUTONOMOUS ended."
						+ "<br>DROPPED: The team attempted to collect a gear in AUTO, but the gear was dropped on the floor."
						+ "<br>FAILED: The team attempted to collect a gear in AUTO, but failed."
						+ "<br>SUCCESS: The PILOT collected a gear in AUTO"
				}, "gearState", false, "Did not attempt", "Ran out of time", "Dropped", "Failed", "Success"),
				new MultipleChoiceQuestion("Gear Hook Used", "autoGearPos", false, "Boiler Side Hook", "Center Hook", "Feeder Side Hook")		
		);

		ScoutingFormTab duringMatch = new ScoutingFormTab("During Match",
				new MultipleChoiceQuestion (new String[] {"Hoppers Triggered in Teleoperated", 
						"<html>A HOPPER is a pair of containers located just outside the FIELD and used "
						+ "to store FUEL at the start ofthe MATCH. "
						+ "<br>TRIGGERING a hopper is pressing the button on the hopper.</html>"}, "teleopHoppersDumped", true, "0", "1", "2", "3", "4", "5"),
				new QuestionSeperator(),
				new QuestionAligner(
					new SpinnerQuestion("Gears Scored on Boiler Side Hook", "gearsScoredBoiler", 0, 21),
					new SpinnerQuestion("Gears Scored on Center Hook", "gearsScoredCenter", 0, 21),
					new SpinnerQuestion("Gears Scored on Feeder Side Hook", "gearsScoredFeeder", 0, 21)
				),
				new QuestionSeperator(),
				new QuestionAligner(
					new SpinnerQuestion(new String[] {"Gears Mismanaged on Boiler Side Hook",
							"<html>Increment this whenever a team struggles with the Boiler Side Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "gearsFailedBoiler", 0, 999),
					new SpinnerQuestion(new String[] {"Gears Mismanaged on Center Hook",
							"<html>Increment this whenever a team struggles with the Center Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "gearsFailedCenter", 0, 999),
					new SpinnerQuestion(new String[] {"Gears Mismanaged on Feeder Side Hook",
							"<html>Increment this whenever a team struggles with the Feeder Side Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "gearsFailedFeeder", 0, 999)
				),
				new QuestionSeperator(),
				new SpinnerQuestion(new String[] {"Average Time per Gear Cycle (seconds)",
						"Approximate time, in seconds, it takes for the team to move across the field, collect a GEAR, return, and deposit it."
				}, "gearCycleTime", 0, 135),
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals Made in TELEOPERATED", "teleopLow", 0),
						new SpinnerQuestion("High Goals Made in TELEOPERATED", "teleopHigh", 0)
				),
				new SpinnerQuestion(new String[] {"Number of Fuel Cycles",
						"The number of times a team set up and attempted to shoot."
				}, "shootingCycles", 0, 99),
				new SpinnerQuestion(new String[] {"Average Time per Fuel Cycle (seconds)",
						"Approximate time for a team to collect FUEL and shoot."
				},"shootingCycleTime", 0, 135),
				new LocationQuestion(new String[] {"Where does this robot shoot from in TELEOP?",
						"If they shot from the same place multiple times, you only need one flag."
				}, "teleopShootingPos", "field.png"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"What type of defense did they play?",
						"<html>PURPOSEFUL: Dedicated defense, accomplishing few other actions in the meantime."
						+ "<br>ON THE WAY: Harasses opponent's robots while moving between tasks.</html>"
				}, "defenseType", false, "Purposeful", "On the way", "None"),			
				new SpinnerQuestion("How long did they play defense? (Seconds)", "defenseTime", 0, 135),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Climbing",
						"<html>CLIMBED: Successfully climbed the ROPE and depressed the TOUCHPAD."
						+ "<br>FELL OFF: The robot lost grip on the rope and was unable to climb again."
						+ "<br>FAILED: The robot either failed to latch on to the rope or failed to fully climb and depress the touchpad.</html>"
				}, "climber", false, "Climbed", "Fell Off", "Failed", "No Attempt"),
				new SpinnerQuestion("Time from beginning of climb to end of match (seconds)", "climberTime", 0, 135)
		);

		ScoutingFormTab postMatch = new ScoutingFormTab("Post Match",
				new QuestionAligner(
						new MultipleChoiceQuestion(new String[] {"Ground Intake (Gear)",
								"A measurement of the reliability and speed of GEAR intake from the ground."
						}, "groundIntakeGear", true, "Useless", "Bad", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion(new String[] {"Loading Station Intake (Gear)",
								"A measurement of the reliability and speed of GEAR intake from the LOADING STATION."
						}, "loadingStationIntakeGear", true, "Useless", "Bad", "Average", "Good", "Excellent", "N/A")
						),
				new QuestionSeperator(),
				new QuestionAligner(
						new MultipleChoiceQuestion (new String[] {"Ground Intake (Fuel)",
								"A measurement of the reliability and speed of FUEL intake from the ground."
						}, "groundIntake", true, "Useless", "Bad", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion (new String[] {"Loading Station Intake (Fuel)",
								"A measurement of the reliability and speed of FUEL intake from the LOADING STATION."
						}, "loadingStationIntake", true, "Useless", "Bad", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion (new String[] {"Hopper Intake (Fuel)",
								"<html>A measurement of the reliability and speed of FUEL intake from a HOPPER."
								+ "<br>A HOPPER is a pair of containers located just outside the FIELD and used "
								+ "to store FUEL at the start ofthe MATCH.</html>"
						}, "hopperIntake", true, "Useless", "Bad", "Average", "Good", "Excellent", "N/A"),
						new MultipleChoiceQuestion("Shooter Accuracy", "shooterAccuracy", true, "Unreliable", "Sketchy",
								"Average", "Consistent", "Reliable", "N/A"),
						new MultipleChoiceQuestion("Shooter Speed", "shooterSpeed", true, "Snail", "Slow", "Average",
								"Speedy", "Lightning", "N/A")
						),
				new QuestionSeperator(),
				new QuestionAligner(	
						new MultipleChoiceQuestion(new String[] {"Manueverability",
								"A measurement of the general alignment ability and speed of the robot"
						}, "manueverability", true, "Sluggish", "Unresponsive",
								"Average", "Responsive", "Nimble"),
						new MultipleChoiceQuestion(new String[] {"Driver Skill",
								"A measurement of how intentional and practiced the drive team appears."
						}, "driverSkill", true, "Hopeless", "Bad", "Average", "Skilled", "God-like"),
						new MultipleChoiceQuestion("Overall Impression", "overall", true, "Do not pick", "Mediocre", "Average",
								"Good", "Incredible")),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Did they break down?",
						"<html>Check this box if the robot was on the field, but unable to operate due to jamming/disconnection from the FMS/physical damage."
						+ "<br>Please mark jams regarding GEARS and LIFTS in the appropriate section of the \"During Match\" tab."
						+ "<br>Please provide more information about the nature of a break-down in comments.</html>"
				}, "breakDown", false, "Yes", "No"),
				new FreeResponseQuestion(new String[] {"Comments",
						"General comments, strategic information not defined by the questions above, etc."
				}, "comments")
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
			JScrollPane currPane = new JScrollPane(tab);
			currPane.setWheelScrollingEnabled(false); //This doesnt work :)
			tabbedPane.add(tab.getName(), currPane);
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
