package org.first.team2485.scoutingform.questions;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JCheckBox;
import javax.swing.JRadioButton;

import org.jdesktop.swingx.JXCollapsiblePane;
import org.jdesktop.swingx.JXTree;


@SuppressWarnings("serial")
public class QuestionGroup extends Question {
	 
	JXCollapsiblePane pane;
	JCheckBox checkbox;
	JRadioButton titleButton;
	JXTree tree;
	Question[] questions;
	
	public QuestionGroup(boolean hasButton, String title, Question... questions) {
		
		this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		
		checkbox = new JCheckBox(title);
		checkbox.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
			}
		});
		this.add(checkbox);
		
		pane = new JXCollapsiblePane();
		//pane.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
		for (Question question : questions) {
			pane.add(question);
		}
		pane.setCollapsed(true);
		this.add(pane);
		
//		titleLabel = new DefaultMutableTreeNode(title);
//		DefaultMutableTreeNode sub = new DefaultMutableTreeNode("Sub");
//		titleLabel.add(sub);
//		tree = new JXTree(titleLabel);
//		this.add(tree);
		
	}
	
	public void update() {
		
		pane.setCollapsed(checkbox.isSelected());
		
	}
	
	@Override
	public String getData() {
		return "";
	}
}
