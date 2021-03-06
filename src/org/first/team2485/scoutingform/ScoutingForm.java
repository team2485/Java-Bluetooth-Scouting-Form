package org.first.team2485.scoutingform;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.util.Date;

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
import org.first.team2485.scoutingform.questions.SliderQuestion;
import org.first.team2485.scoutingform.questions.SpinnerQuestion;
import org.first.team2485.scoutingform.util.Logger;
import org.first.team2485.scoutingform.util.USBDriveDataPusher;

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
		
		Logger.getInst().log("starting bluetooth scouting form");
		
		USBDriveDataPusher.start();
		
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
 
		ScoutingFormTab robotScouting = new ScoutingFormTab("Robot Scouting",
				new QuestionGroup("Pre-Match",
						new QuestionAligner(
								new SpinnerQuestion("Match Number", "robotMatchNumber", 0, 9999, matchNumber),
								new SpinnerQuestion("Team Number", "robotTeamNumber")
						),
						new CheckboxQuestion(new String[] {"No Show", "Check this box if you are sure you're robot is not in this match"}, "noShow", "")
				),
				new QuestionSeperator(),
				new QuestionGroup("Automous",
						new LocationQuestion("Where does this robot start auto?", "autoStartPos", "/field.png"),
						new QuestionSeperator(),
						new MultipleChoiceQuestion(new String[] {"Auto Gear", "Select what the robot did with it's gear in auto"}, "autoGear", false, "No Attempt", "Failed", "Success"),
						new QuestionAligner(
								new SpinnerQuestion(new String[] {"High Goals Made", "How many high goals did they make in auto"}, "autoHighGoals"),
								new SpinnerQuestion(new String[] {"Low Goals Made", "How many low goals did they make in auto" }, "autoLowGoals")
						)
				),
				new QuestionSeperator(),
				new QuestionGroup("Teleop",
						new QuestionAligner(
								new SpinnerQuestion("Gears Intook From Loading Station", "loadingStationGearIntake"),
								new SpinnerQuestion("Gears Intook From Ground", "groundGearIntake"),
								new SpinnerQuestion("Gears Dropped at Loading Station", "gearsDroppedLoadingStation"),
								new SpinnerQuestion("Gears Hung On Lift", "gearsHung")
						),
						new QuestionSeperator(),
						
						new QuestionAligner(
								new SpinnerQuestion("High Goals", "highGoals"),
								new SpinnerQuestion("Low Goals", "lowGoals")
						),
						new QuestionSeperator(),

						new CheckboxQuestion("Fuel Intake", "fuelIntake", "Loading Station", "Hopper", "Ground"),
						new SliderQuestion("Shooter Acccuracy", "shooterAccuracy", "%", 1, 0, 100, 0, 25, 5),
						
						new QuestionSeperator(),
						
						new MultipleChoiceQuestion("Climbing", "climbState", false, "No Attempt", "Ran Out Of Time", "Got Stuck", "Fell Off", "Success"),
						
						new QuestionSeperator(),

						new MultipleChoiceQuestion("Did they play defense?", "defense", false, "None", "On the way", "Purposeful"),
						new MultipleChoiceQuestion("Driver Skill", "driving", true, "Bad", "Average", "Excellent"),
						
						new QuestionSeperator(),
						
						new CheckboxQuestion("Did they have a yellow card at the end of the match?", "yellowCard", ""),
						new CheckboxQuestion("Did they break down?", "breakdown", "")

				),
				new QuestionSeperator(),
				new FreeResponseQuestion("Comments", "comments")
		);
		
		ScoutingFormTab pilotScouting = new ScoutingFormTab("Airship Scouting",
					new SpinnerQuestion("Match Number", "pilotMatchNumber", 0, 9999, matchNumber),
					new QuestionSeperator(),
					new QuestionGroup("Pilot 1",
							new SpinnerQuestion("Pilot Team Number", "pilot1Team"),
							new QuestionAligner(
									new SpinnerQuestion(new String[] {"Successes in Pulling Up Gear", 
									"Increment every time the pilot successfully retrives a gear from the hook" },
											"pilot1Successes"),
									new SpinnerQuestion(new String[] {"Failures in Pulling Up Gear", 
									"Increment every time the pilot drops a gear from the hook" },
											"pilot1Failures")	
							),
							new QuestionSeperator(),
							new MultipleChoiceQuestion("Were the ropes deployed efficiently?", "ropesDeployed1", true, "Yes", "No"),
							new MultipleChoiceQuestion("Pilot Skill", "pilotSkill1", false, "Incompetent", "Average", "Exceptional"),
							new QuestionSeperator(),
							new FreeResponseQuestion(new String[] {"Comments", "If you feel that there is additional information about the pilot that would be useful, put it here"} , "pilot1Comments")
				),
				
				new QuestionSeperator(),
				
				new QuestionGroup("Pilot 2", 
						new SpinnerQuestion("Pilot Team Number", "pilot2Team"),
						new QuestionAligner(
								new SpinnerQuestion(new String[] {"Successes in Pulling Up Gear", 
								"Increment every time the pilot successfully retrives a gear from the hook" },
										"pilot2Successes"),
								new SpinnerQuestion(new String[] {"Failures in Pulling Up Gear", 
								"Increment every time the pilot drops a gear from the hook" },
										"pilot2Failures")
						),
						new QuestionSeperator(),
						new MultipleChoiceQuestion("Were the ropes deployed efficiently?", "ropesDeployed2", true, "Yes", "No"),
						new MultipleChoiceQuestion("Pilot Skill", "pilotSkill2", false, "Incompetent", "Average", "Exceptional"),
						new QuestionSeperator(),
						new FreeResponseQuestion(new String[] {"Comments", "If you feel that there is additional information about the pilot that would be useful, put it here"} , "pilot2Comments")
				)	
		);
		
		//@formatter:on

		new ScoutingForm(robotScouting, pilotScouting);
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
		
		output += "name," + scoutName +",timestamp," + System.currentTimeMillis() +",";

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
			JOptionPane.showMessageDialog(null, "Something went very wrong loading that theme.");
		}
	}

	public void restart(boolean clearAndIncrement) {
		frame.dispose();

		if (clearAndIncrement) {

			Question q1 = superMegaQuestionFinder5000("robotMatchNumber");
			Question q2 = superMegaQuestionFinder5000("pilotMatchNumber");

			String data1 = q1.getData();
			String data2 = q2.getData();

			int num1 = Integer.parseInt(data1.substring(data1.indexOf(",") + 1, data1.lastIndexOf(",")));
			int num2 = Integer.parseInt(data2.substring(data2.indexOf(",") + 1, data2.lastIndexOf(",")));

			matchNumber = Math.max(num1, num2) + 1;

			displayForm();
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
