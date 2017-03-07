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
import org.first.team2485.scoutingform.gui.addons.GamePredictionPanel;
import org.first.team2485.scoutingform.gui.addons.LookAndFeelSelector;
import org.first.team2485.scoutingform.questions.CheckboxQuestion;
import org.first.team2485.scoutingform.questions.FreeResponseQuestion;
import org.first.team2485.scoutingform.questions.LocationQuestion;
import org.first.team2485.scoutingform.questions.MultipleChoiceQuestion;
import org.first.team2485.scoutingform.questions.Question;
import org.first.team2485.scoutingform.questions.QuestionAligner;
import org.first.team2485.scoutingform.questions.QuestionGroup;
import org.first.team2485.scoutingform.questions.QuestionSeperator;
import org.first.team2485.scoutingform.questions.SpinnerQuestion;

/**
 * @author Jeremy McCulloch
 * @author Troy Appel
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

		setUIFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 20));
	}

	/**
	 * Runs on LaF switch
	 */
	public static void displayForm() {

		//@formatter:off

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
				new LocationQuestion("Where did they start their AUTO from?", "autoStartPos", "/field.png"),
				new QuestionSeperator(),
				new MultipleChoiceQuestion (new String[] {"Did they cross the baseline?",
						"CROSSING THE BASELINE means moving in front of the line defined"
						+ " by the back face of the airship."
				}, "crossBaseline", false, "Yes", "No"),
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals Made in AUTO", "autoLow", 0),
						new SpinnerQuestion("High Goals Made in AUTO", "autoHigh", 0)
				),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Gear State",
						"<html>RAN OUT OF TIME: The gear is on the LIFT, but was not pulled into the AIRSHIP before AUTONOMOUS ended."
						+ "<br>DROPPED: The team attempted to collect a gear in AUTO, but the gear was dropped on the floor."
						+ "<br>FAILED: The team attempted to collect a gear in AUTO, but failed."
						+ "<br>SUCCESS: The PILOT collected a gear in AUTO."
						+ "<br>If the GEAR fell because the PILOT pulled too fast and it slipped off the list, please mark that in the appropriate section of the POSTMATCH tab.</html>"
				}, "gearState", false, "Did not attempt", "Ran out of time", "Dropped", "Failed", "Success"),
				new MultipleChoiceQuestion("Gear Hook Attempted", "autoGearPos", false, "Boiler Side Hook", "Center Hook", "Feeder Side Hook", "N/A")		
		);

		ScoutingFormTab duringMatch = new ScoutingFormTab("During Match",
				new QuestionAligner(
					new SpinnerQuestion("Gears Scored on Boiler Side Hook", "gearsScoredBoiler", 0, 21),
					new SpinnerQuestion("Gears Scored on Center Hook", "gearsScoredCenter", 0, 21),
					new SpinnerQuestion("Gears Scored on Feeder Side Hook", "gearsScoredFeeder", 0, 21)
				),

				new QuestionSeperator(),
				new QuestionAligner(
					new SpinnerQuestion(new String[] {"Boiler Side LIFT Failure",
							"<html>Increment this whenever a team struggles with the Boiler Side Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "liftBoilerFailure", 0, 999),
					new SpinnerQuestion(new String[] {"Center LIFT Failure",
							"<html>Increment this whenever a team struggles with the Center Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "liftCenterFailure", 0, 999),
					new SpinnerQuestion(new String[] {"Feeder Side LIFT Failure",
							"<html>Increment this whenever a team struggles with the Feeder Side Hook in any way, besides simply missing the hook."
							+ "<br>This include dropping a GEAR, and jamming on the LIFT.</html>"
					}, "liftFeederFailure", 0, 999)
				),
				new QuestionSeperator(),
				new SpinnerQuestion(new String[] {"Gears Dropped by Robot",
						"<html>Increment this when a robot drops a gear while operating a lift,"
						+ "<br/>moving across the field, or intaking a gear, and it is the robot's fault.</html>"
				}, "gearsDropped", 0, 999),
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

				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"What type of defense did they play?",
						"<html>PURPOSEFUL: Dedicated defense, accomplishing few other actions in the meantime."
						+ "<br>ON THE WAY: Harasses opponent's robots while moving between tasks.</html>"
				}, "defenseType", false, "Purposeful", "On the way", "None"),			
				new SpinnerQuestion("How long did they play defense? (seconds)", "defenseTime", 0, 135),
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
						new SpinnerQuestion(new String[] {"Human Player GEAR Loading Successes",
								"<html>Increment every time a HUMAN PLAYER pushes a GEAR through the LOADING STATION,"
								+ "<br>where the gear correctly falls into the target robot.</html>"
						}, "humanPlayer", 0, 999),

						new SpinnerQuestion(new String[] {"Human Player GEAR Loading Failures",
								"<html>Increment every time a HUMAN PLAYER pushes a GEAR through the LOADING STATION,"
								+ "<br>where the gear falls on the floor because of an error by the HUMAN PLAYER</html>"
						}, "humanPlayer", 0, 999)
				),
				
				
				new QuestionSeperator(),
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
								+ "to store FUEL at the start of the MATCH.</html>"
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
						new MultipleChoiceQuestion("Overall Impression", "overall", true, "Do not pick", "Mediocre", "Average", "Good", "Incredible")
				),

				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Did they break down?",
						"<html>Check this box if the robot was on the field, but unable to operate due to jamming/disconnection from the FMS/physical damage."
						+ "<br>Please mark jams regarding GEARS and LIFTS in the appropriate section of the \"During Match\" tab."
						+ "<br>Please provide more information about the nature of a break-down in comments.</html>"
				}, "breakDown", false, "Yes", "No"),
				new FreeResponseQuestion(new String[] {"Comments",
						"<html>General comments, strategic information not defined by the questions above, etc."
						+ "<br>We are especially interested in whether or not teams get stuck on FUEL.</html>"
				}, "comments")
		);
		
		//@formatter:on

		new ScoutingForm(prematch, auto, duringMatch, postMatch);
	}

	public ScoutingForm(ScoutingFormTab... tabs) {

		scoutingForm = this;

		frame = new JFrame("Scouting Form");

		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		frame.add(outerPanel);

		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));

		this.tabbedPane = new JTabbedPane();

		this.tabs = tabs;

		for (ScoutingFormTab tab : tabs) {
			JScrollPane currPane = new JScrollPane(tab);
			currPane.setWheelScrollingEnabled(true);
			currPane.getVerticalScrollBar().setUnitIncrement(16); // sets scroll
																	// speed
			tabbedPane.add(tab.getName(), currPane);
		}

		this.add(tabbedPane);

		outerPanel.add(this, BorderLayout.CENTER);
		outerPanel.add(new JScrollPane(new LookAndFeelSelector(this)), BorderLayout.WEST);
		outerPanel.add(new JScrollPane(new GamePredictionPanel(this)), BorderLayout.EAST);

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

	public Question superMegaQuestionFinder5000(String internalName) {
		for (ScoutingFormTab tab : tabs) {
			for (Question q : tab.getQuestions()) {

				Question result = getFromSelfOrSubQuestionsIfPossibleRecursive(internalName, q);

				if (result != null) {
					return result;
				}
			}
		}
		return null;
	}

	private Question getFromSelfOrSubQuestionsIfPossibleRecursive(String internalName, Question container) {

		if (container instanceof QuestionAligner) {

			for (Question q : ((QuestionAligner) container).getQuestions()) {

				Question result = getFromSelfOrSubQuestionsIfPossibleRecursive(internalName, q);

				if (result != null) {
					return result;
				} else if (q.getInternalName().equals(internalName)) {
					return q;
				}
			}
		} else if (container instanceof QuestionGroup) {

			for (Question q : ((QuestionGroup) container).getQuestions()) {

				Question result = getFromSelfOrSubQuestionsIfPossibleRecursive(internalName, q);

				if (result != null) {
					return result;
				} else if (q.getInternalName().equals(internalName)) {
					return q;
				}
			}
		} else {
			return container.getInternalName().equals(internalName) ? container : null;
		}
		return null;
	}

	public String submit() {

		String output = "";

		for (ScoutingFormTab tab : tabs) {
			output += tab.getData();
		}

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

	public void restart(boolean clearAndIncrement) {
		frame.dispose();

		if (clearAndIncrement) {
			displayForm();

			Question q = superMegaQuestionFinder5000("matchNumber");
			System.out.println("Found: " + q.getData());
			String data = q.getData();
			int num = Integer.parseInt(data.substring(data.indexOf(",") + 1, data.lastIndexOf(",")));
			System.out.println("Old match num: " + num);
			q = new SpinnerQuestion("Match Number", "matchNumber", 0, 9999, num + 1);
			System.out.println("q = " + q.getData());

			System.out.println("global: " + superMegaQuestionFinder5000("matchNumber").getData());

			GamePredictionPanel.predictionPanel.beginTimeoutForMatch(num + 1);
		} else {
			new ScoutingForm(tabs.clone());
		}
	}

	public String getScoutName() {
		return scoutName;
	}

	@SuppressWarnings("rawtypes")
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
