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
import javax.swing.ToolTipManager;
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

	private static int matchNumber;

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

		scoutName = "";

		while (scoutName.equals("")) {
			scoutName = JOptionPane.showInputDialog(null, "Enter your name", "Name", JOptionPane.QUESTION_MESSAGE);

			if (scoutName == null) {
				System.exit(0);
			}
		}

		matchNumber = 1;

		LookAndFeelSelector.addAdditonalLaFs();

		setUIFont(new javax.swing.plaf.FontUIResource("SansSerif", Font.PLAIN, 20));

		ToolTipManager.sharedInstance().setDismissDelay(Integer.MAX_VALUE);

	}

	/**
	 * Runs on LaF switch
	 */
	public static void displayForm() {

		//@formatter:off

		ScoutingFormTab prematch = new ScoutingFormTab("Prematch",
				new QuestionAligner(
						new SpinnerQuestion("Team Number", "teamNumber", 0, 9999),
						new SpinnerQuestion("Match Number", "matchNumber", 0, 9999, matchNumber)
				),
				new QuestionSeperator(),
				new CheckboxQuestion(new String[] {"No Show?", "<html>Check this box if your robot is scheduled to play "
						+ "<br/>this match, but did not show up. Be careful! "
						+ "<br/>It could be that you just can't see it from "
						+ "where you are sitting.</html>"}, 
						"noShow", "")
				
		);
		
		ScoutingFormTab auto = new ScoutingFormTab("Automous", 
				new LocationQuestion("Where did they start their AUTO from?", "autoStartPos", "/field.png"),
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals Made in AUTO", "autoLow", 0),
						new SpinnerQuestion("High Goals Made in AUTO", "autoHigh", 0)
				),
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Gear State",
						"<html>RAN OUT OF TIME: The gear is on the LIFT, but was not pulled into the AIRSHIP before AUTONOMOUS ended."
						+ "<br>DROPPED: The team attempted to collect a gear in AUTO, but the gear was dropped on the floor."
						+ "<br>FAILED: The team attempted to collect a gear in AUTO, but failed, without the robot dropping it."
						+ "<br>SUCCESS: The PILOT collected a gear in AUTO."
						+ "<br>If the GEAR fell because the PILOT pulled too fast and it slipped off the list, please mark that in the appropriate section of the POSTMATCH tab.</html>"
				}, "gearState", false, "Did not attempt", "Dropped", "Failed", "Success"),
				new MultipleChoiceQuestion("Gear Hook Attempted", "autoGearPos", false, "Boiler Side Hook", "Center Hook", "Feeder Side Hook", "N/A")		
		);
 
		ScoutingFormTab duringMatch = new ScoutingFormTab("During Match",
				new QuestionAligner(
					new SpinnerQuestion("Gears Placed on the Hook", "gearsScored", 0, 21)
				),
				new QuestionSeperator(),
				new SpinnerQuestion(new String[] {"Gears Dropped by Robot",
						"<html>Increment this when a robot drops a gear while operating a lift, moving across the field,"
						+ "<br/>or intaking a gear, and it is the robot's fault.</html>"
				}, "gearsDropped", 0, 999),
				new SpinnerQuestion("Gears Intook by Robot (Ground)", "gearsIntookGround"),
				new QuestionSeperator(),
				new QuestionAligner(
						new SpinnerQuestion("Low Goals Made in TELEOPERATED", "teleopLow", 0),
						new SpinnerQuestion("High Goals Made in TELEOPERATED", "teleopHigh", 0)
				),
				new SpinnerQuestion(new String[] {"Number of Fuel Cycles",
						"The number of times a team set up and attempted to shoot."
				}, "shootingCycles", 0, 99),

				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"What type of defense did they play?",
						"<html>PURPOSEFUL: Dedicated defense, accomplishing few other actions in the meantime."
						+ "<br>ON THE WAY: Harasses opponent's robots while moving between tasks.</html>"
				}, "defenseType", false, "Purposeful", "On the way", "None"),			
				new QuestionSeperator(),
				new MultipleChoiceQuestion(new String[] {"Climbing",
						"<html>CLIMBED: Successfully climbed the ROPE and depressed the TOUCHPAD."
						+ "<br>FELL OFF: The robot lost grip on the rope and was unable to climb again."
						+ "<br>FAILED: The robot either failed to latch on to the rope or failed to fully climb and depress the touchpad.</html>"
				}, "climber", false, "Climbed", "Fell Off", "Ran out of Time", "Got Stuck", "No Attempt"),
				new SpinnerQuestion("Time taken to climb(seconds)", "climberTime", 0, 135)
		);
		
		ScoutingFormTab pilot = new ScoutingFormTab("Pilot",
				
				new QuestionAligner(
					new SpinnerQuestion(new String[] {"Successes in Pulling Up Gear", 
							"<html>Increment every time the PILOT successfully retrives a gear from the BOILER side hook" },
							"pilotBoilerSuccesses", 0, 999),
					new SpinnerQuestion(new String[] {"Failures in Pulling Up Gear", 
							"<html>Increment every time the PILOT drops a gear from the BOILER side hook" },
								"pilotBoilerFailures", 0, 999)
				),
				new QuestionSeperator(),
				new MultipleChoiceQuestion("Were the ropes deployed efficiently?", "ropesDeployed", true, "Yes", "No"),
				new QuestionSeperator(),
				new FreeResponseQuestion(new String[] {"Additional Comments on Pilot", "<html>If you feel that there is additional information about the pilot that would be useful, put it here"} , "pilotComments")
		);

		ScoutingFormTab postMatch = new ScoutingFormTab("Post Match",
				
				new QuestionSeperator(),
				new QuestionAligner(
						new MultipleChoiceQuestion("Shooter Accuracy", "shooterAccuracy", true, "0%", "25%",
								"50%", "75%", "100%", "N/A")
				),
				
				new QuestionSeperator(),
				new QuestionAligner(	
						new MultipleChoiceQuestion(new String[] {"Manueverability",
								"A measurement of the general alignment ability and speed of the robot"
						}, "manueverability", true, "Sluggish", "Unresponsive",
								"Average", "Responsive", "Nimble"),

						new MultipleChoiceQuestion(new String[] {"Driver Skill",
								"A measurement of how intentional and practiced the drive team appears."
						}, "driverSkill", true, "Hopeless", "Bad", "Average", "Skilled", "God-like")				),

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

		new ScoutingForm(prematch, auto, duringMatch, pilot, postMatch);
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
			currPane.getHorizontalScrollBar().setUnitIncrement(16);
			tabbedPane.add(tab.getName(), currPane);
		}

		this.add(tabbedPane);

		outerPanel.add(this, BorderLayout.CENTER);
		outerPanel.add(new JScrollPane(new LookAndFeelSelector(this)), BorderLayout.WEST);
		outerPanel.add(new JScrollPane(new GamePredictionPanel(this, matchNumber)), BorderLayout.EAST);

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

		frame.setExtendedState(JFrame.MAXIMIZED_BOTH);

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

			Question q = superMegaQuestionFinder5000("matchNumber");
			System.out.println("Found: " + q.getData());
			String data = q.getData();
			int num = Integer.parseInt(data.substring(data.indexOf(",") + 1, data.lastIndexOf(",")));
			System.out.println("Old match num: " + num);
			matchNumber = num + 1;

			displayForm();

			// GamePredictionPanel.predictionPanel.beginTimeoutForMatch(matchNumber);
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
