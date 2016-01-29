package org.first.team2485.scoutingform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.first.team2485.scoutingform.ScoutingFormTab;

@SuppressWarnings("serial")
public class ClearButton extends JButton{
	private static ScoutingFormTab[] tabs;
	private static JFrame frame;
	public ClearButton(ScoutingFormTab[] tabs, JFrame frame) {
		super("Clear");
		ClearButton.tabs = tabs;
		ClearButton.frame = frame;
		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showConfirmation();
			}
		});
	}
	
	public static void clear() {
		if(tabs == null)
			return;
		for(int i = 0; i < tabs.length; i++) {
			tabs[i].clear();
		}
	}
	
	private void showConfirmation () {
		int status = JOptionPane.showConfirmDialog(frame, "Are you sure you want to clear?", "Confirmation", JOptionPane.YES_NO_OPTION);
		
		if (status == JOptionPane.YES_OPTION) {
			clear();
		}
		else {
			return;
		}
	}
}
