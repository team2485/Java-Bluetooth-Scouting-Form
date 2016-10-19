package org.first.team2485.scoutingform.gui;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.LookAndFeel;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import javax.swing.UnsupportedLookAndFeelException;

import org.first.team2485.scoutingform.ScoutingForm;

@SuppressWarnings("serial")
public class LookAndFeelSelector extends JPanel implements ActionListener {
	
	private final LookAndFeelInfo[] looksAndFeelsInfos;
	
	private ScoutingForm form;
	
	public LookAndFeelSelector(ScoutingForm form) {
		
		this.form = form;
		
		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		this.add(buttonPanel);
		setPreferredSize(new Dimension(200, 200));
		
		buttonPanel.add(new JLabel("Select A Theme"));
		
		looksAndFeelsInfos = UIManager.getInstalledLookAndFeels();
		
		ButtonGroup buttonGroup = new ButtonGroup();
		
		for (LookAndFeelInfo cur : looksAndFeelsInfos) {
			
			System.out.println(cur.getClassName());
			
			String fullName = cur.getClassName();
			
			String shortName = fullName.substring(fullName.lastIndexOf(".") + 1, fullName.length());
			
			shortName = shortName.replaceFirst(Pattern.quote("LookAndFeel"), "");
			
			for (int i = 1; i < shortName.length(); i++) {
				if (Character.isUpperCase(shortName.charAt(i))) {
					shortName = shortName.substring(0, i) + " " + shortName.substring(i);
					i++;
				}
			}
			
			JRadioButton radioButton = new JRadioButton(shortName);
			radioButton.setToolTipText(fullName);
			radioButton.addActionListener(this);
			radioButton.setActionCommand(fullName);
			
			buttonGroup.add(radioButton);
			buttonPanel.add(radioButton, BorderLayout.CENTER);
			
			if (fullName.contains(UIManager.getLookAndFeel().getClass().getName())) {
				buttonGroup.setSelected(radioButton.getModel(), true);
			}
		}
		repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		if (e.getSource() instanceof JRadioButton) {
			form.setLookAndFeel(e.getActionCommand());
		}
	}
}
