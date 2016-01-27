package org.first.team2485.scoutingform;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class QuitButton extends JPanel{
	
	private static JFrame comp;
	
	public QuitButton(JFrame frame) {
		frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		frame.addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				int status = JOptionPane.showConfirmDialog(QuitButton.comp, "Are you sure you want to quit?", "Confirmation", JOptionPane.YES_NO_OPTION);
				
				if (status == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				else {
					return;
				}
			}
		});
		comp = frame;
		JButton quit = new JButton("Quit");

		quit.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int status = JOptionPane.showConfirmDialog(QuitButton.comp, "Are you sure you want to quit?", "Confirmation", JOptionPane.YES_NO_OPTION);
			
				if (status == JOptionPane.YES_OPTION) {
					System.exit(0);
				}
				else {
					return;
				}
			}
		});
		this.add(quit);
		
	}


}
