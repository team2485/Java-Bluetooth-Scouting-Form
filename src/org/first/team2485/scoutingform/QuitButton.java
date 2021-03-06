package org.first.team2485.scoutingform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import org.first.team2485.scoutingform.util.Logger;

/**
 * 
 * @author Troy Appel
 *
 */
@SuppressWarnings("serial")
public class QuitButton extends JButton {

	public QuitButton(JFrame frame) {
		super("Quit");

		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				showConfirmation();
			}
		});

		this.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				showConfirmation();
			}
		});

	}

	private void showConfirmation() {
		int status = JOptionPane.showConfirmDialog(null, "Are you sure you want to quit?", "Confirmation",
				JOptionPane.YES_NO_OPTION);

		if (status == JOptionPane.YES_OPTION) {
			System.exit(0);
		} else {
			return;
		}
	}
}
