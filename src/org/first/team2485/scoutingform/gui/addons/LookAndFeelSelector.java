package org.first.team2485.scoutingform.gui.addons;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.regex.Pattern;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;

import org.first.team2485.scoutingform.ScoutingForm;
import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

import com.alee.laf.WebLookAndFeel;
import com.jtattoo.plaf.acryl.AcrylLookAndFeel;
import com.jtattoo.plaf.aero.AeroLookAndFeel;
import com.jtattoo.plaf.aluminium.AluminiumLookAndFeel;
import com.jtattoo.plaf.bernstein.BernsteinLookAndFeel;
import com.jtattoo.plaf.fast.FastLookAndFeel;
import com.jtattoo.plaf.hifi.HiFiLookAndFeel;
import com.jtattoo.plaf.luna.LunaLookAndFeel;
import com.jtattoo.plaf.mcwin.McWinLookAndFeel;
import com.jtattoo.plaf.mint.MintLookAndFeel;
import com.jtattoo.plaf.noire.NoireLookAndFeel;
import com.jtattoo.plaf.smart.SmartLookAndFeel;
import com.jtattoo.plaf.texture.TextureLookAndFeel;
import com.seaglasslookandfeel.SeaGlassLookAndFeel;

import de.javasoft.plaf.synthetica.SyntheticaStandardLookAndFeel;

/**
 * 
 * @author Nicholas Contreras
 *
 */

@SuppressWarnings("serial")
public class LookAndFeelSelector extends LockedSizeJPanel implements ActionListener {

	private final LookAndFeelInfo[] looksAndFeelsInfos;

	private ScoutingForm form;

	public LookAndFeelSelector(ScoutingForm form) {

		this.form = form;

		JPanel buttonPanel = new JPanel(new GridLayout(0, 1));
		this.add(buttonPanel);

		buttonPanel.add(new JLabel("Select A Theme"));

		looksAndFeelsInfos = UIManager.getInstalledLookAndFeels();

		ButtonGroup buttonGroup = new ButtonGroup();

		for (LookAndFeelInfo cur : looksAndFeelsInfos) {

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
	}

	public static void addAdditonalLaFs() {
		UIManager.installLookAndFeel("Web", WebLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Sea Glass", SeaGlassLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Synthetica", SyntheticaStandardLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Acryl", AcrylLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Fast", FastLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Bernstein", BernsteinLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Aluminium", AluminiumLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Aero", AeroLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("HiFi", HiFiLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("McWin", McWinLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Mint", MintLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Noire", NoireLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Smart", SmartLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Luna", LunaLookAndFeel.class.getCanonicalName());
		UIManager.installLookAndFeel("Texture", TextureLookAndFeel.class.getCanonicalName());
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		if (e.getSource() instanceof JRadioButton) {
			form.setLookAndFeel(e.getActionCommand());

			int input = JOptionPane.showConfirmDialog(ScoutingForm.scoutingForm,
					"You must restart the form for all the changes to take effect.\nWould you like to restart now?",
					"Restart Required", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);
			
			if (input == JOptionPane.YES_OPTION) {
				ScoutingForm.scoutingForm.restart(true);
			}
		}
	}
}
