import java.util.ArrayList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.ButtonUI;


public class ScoutingForm extends JPanel {
	private JFrame frame;
	private ArrayList<Question> questions;
	public ScoutingForm() {
		frame = new JFrame();
		frame.add(this);
		frame.setSize(1000, 600);
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		questions = new ArrayList<Question>();
		questions.add(new FreeResponseQuestion("jklsdsa"));
		for (Question question : questions) {
			this.add(question);
		}
		
		frame.pack();
		frame.setVisible(true);
		this.repaint();
	}
	
	public static void main(String[] args) {
		new ScoutingForm();
	}
}
