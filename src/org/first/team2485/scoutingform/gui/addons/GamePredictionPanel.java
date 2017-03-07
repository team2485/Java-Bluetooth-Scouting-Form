package org.first.team2485.scoutingform.gui.addons;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;

import javax.swing.AbstractButton;
import javax.swing.DefaultButtonModel;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;

import org.first.team2485.scoutingform.ScoutingForm;
import org.first.team2485.scoutingform.bluetooth.BluetoothPanel;
import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class GamePredictionPanel extends LockedSizeJPanel {

	private static final int TIMEOUT = 30000;

	public static GamePredictionPanel predictionPanel;

	private boolean write;

	private ScoutingForm form;

	private JLabel timerLabel;

	private ArrayList<JLabel> matchLabels;
	private ArrayList<JSwitchBox> switchBoxes;

	private JButton commandButton;

	public GamePredictionPanel(ScoutingForm form) {

		predictionPanel = this;

		this.form = form;

		setLayout(new GridLayout(0, 2));

		JLabel title = new JLabel("Game Prediction");
		title.setHorizontalAlignment(SwingConstants.LEFT);
		add(title);

		timerLabel = new JLabel();
		timerLabel.setHorizontalAlignment(SwingConstants.CENTER);
		add(timerLabel);

		matchLabels = new ArrayList<JLabel>();
		switchBoxes = new ArrayList<JSwitchBox>();

		commandButton = new JButton("Enter Command");
		commandButton.addActionListener((ActionEvent) -> processCommand());

		File f = new File(System.getProperty("user.home") + "/ScoutingRecords/PredictionData~"
				+ ScoutingForm.scoutingForm.getScoutName() + ".spd");

		if (f.exists()) {
			try {
				BufferedReader reader = new BufferedReader(new FileReader(f));

				String data = reader.readLine();

				reader.close();

				for (int i = 0; i < data.length(); i++) {

					char curChar = data.charAt(i);

					JLabel label = new JLabel("Match " + (i + 1));
					add(label);
					matchLabels.add(label);

					JSwitchBox switchBox = new JSwitchBox("Blue", "Red");
					switchBox.setSelected(curChar == 'B');
					add(switchBox);
					switchBoxes.add(switchBox);
				}

				return;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		initSwitchBoxesAndButtons(90);

		write = true;
	}

	private void processCommand() {
		String command = JOptionPane.showInputDialog(null, "Enter Command", "Command", JOptionPane.PLAIN_MESSAGE);

		if (command.startsWith("NUM_MATCHES=")) {
			int numMatches = Integer.parseInt(command.substring("NUM_MATCHES".length() + 1));

			initSwitchBoxesAndButtons(numMatches);
		} else if (command.startsWith("SEND_RESULTS")) {

			String fileName = "PredictionData~" + form.getScoutName() + "^" + System.currentTimeMillis() + ".spd";

			new BluetoothPanel(fileName, getData());

			form.getFrame().dispose();
		}
	}

	private String getData() {
		String data = "";
		for (JSwitchBox switchBox : switchBoxes) {
			data += switchBox.isSelected() ? "B" : "R";
		}
		return data;
	}

	private void initSwitchBoxesAndButtons(int numMatches) {

		for (JLabel cur : matchLabels) {
			remove(cur);
		}
		matchLabels.clear();

		for (JSwitchBox cur : switchBoxes) {
			remove(cur);
		}
		switchBoxes.clear();

		remove(commandButton);

		for (int i = 1; i <= numMatches; i++) {

			JLabel label = new JLabel("Match " + i);
			add(label);
			matchLabels.add(label);

			JSwitchBox switchBox = new JSwitchBox("Blue", "Red");
			add(switchBox);
			switchBoxes.add(switchBox);
		}

		add(commandButton);
	}

	public void updateRecords() {
		System.out.println("Updating record");
		String fileName = "PredictionData~" + form.getScoutName() + ".spd";
		BluetoothPanel.writeToScoutingRecords(fileName, getData());
	}

	public void beginTimeoutForMatch(int matchNum) {
		Thread t = new Thread(() -> {

			long startTime = System.currentTimeMillis();

			while (System.currentTimeMillis() - TIMEOUT < startTime) {

				timerLabel.setText("" + (TIMEOUT - (System.currentTimeMillis() - startTime)) / 1000);

				try {
					Thread.sleep(500);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}

			timerLabel.setText("");

			JSwitchBox switchBox = switchBoxes.get(matchNum - 1);
			switchBox.setEnabled(false);

			JLabel matchLabel = matchLabels.get(matchNum - 1);
			matchLabel.setForeground(Color.LIGHT_GRAY);
		});

		t.setDaemon(true);
		t.start();
	}

	class JSwitchBox extends AbstractButton {
		private Color colorBright = new Color(220, 220, 220);
		private Color colorDark = new Color(150, 150, 150);
		private Color black = new Color(0, 0, 0, 100);
		private Color white = new Color(255, 255, 255, 100);
		private Color light = new Color(220, 220, 220, 100);
		private Color red = new Color(160, 130, 130);
		private Color blue = new Color(10, 10, 240);
		private Font font = new JLabel().getFont();
		private int gap = 5;
		private int globalWitdh = 0;
		private final String trueLabel;
		private final String falseLabel;
		private Dimension thumbBounds;
		private int max;

		private boolean disabled;

		public JSwitchBox(String trueLabel, String falseLabel) {
			this.trueLabel = trueLabel;
			this.falseLabel = falseLabel;
			double trueLenth = getFontMetrics(getFont()).getStringBounds(trueLabel, getGraphics()).getWidth();
			double falseLenght = getFontMetrics(getFont()).getStringBounds(falseLabel, getGraphics()).getWidth();
			max = (int) Math.max(trueLenth, falseLenght);
			gap = Math.max(5, 5 + (int) Math.abs(trueLenth - falseLenght));
			thumbBounds = new Dimension(max + gap * 2, 20);
			globalWitdh = max + thumbBounds.width + gap * 2;
			setModel(new DefaultButtonModel());
			setSelected(false);
			addMouseListener(new MouseAdapter() {
				@Override
				public void mouseReleased(MouseEvent e) {
					if (new Rectangle(getPreferredSize()).contains(e.getPoint())) {
						setSelected(!isSelected());
					}
				}
			});
		}

		@Override
		public Dimension getPreferredSize() {
			return new Dimension(globalWitdh, thumbBounds.height);
		}

		public void disable() {
			disabled = true;
		}

		@Override
		public void setSelected(boolean b) {

			if (disabled) {
				return;
			}

			if (b) {
				setText(trueLabel);
				setBackground(blue);
			} else {
				setBackground(red);
				setText(falseLabel);
			}
			super.setSelected(b);

			if (write) {
				updateRecords();
			}
		}

		@Override
		public void setText(String text) {
			super.setText(text);
		}

		@Override
		public int getHeight() {
			return getPreferredSize().height;
		}

		@Override
		public int getWidth() {
			return getPreferredSize().width;
		}

		@Override
		public Font getFont() {
			return font;
		}

		@Override
		protected void paintComponent(Graphics g) {
			g.setColor(getBackground());
			g.fillRoundRect(1, 1, getWidth() - 2 - 1, getHeight() - 2, 2, 2);
			Graphics2D g2 = (Graphics2D) g;

			g2.setColor(black);
			g2.drawRoundRect(1, 1, getWidth() - 2 - 1, getHeight() - 2 - 1, 2, 2);
			g2.setColor(white);
			g2.drawRoundRect(1 + 1, 1 + 1, getWidth() - 2 - 3, getHeight() - 2 - 3, 2, 2);

			int x = 0;
			int lx = 0;
			if (isSelected()) {
				lx = thumbBounds.width;
			} else {
				x = thumbBounds.width;
			}
			int y = 0;
			int w = thumbBounds.width;
			int h = thumbBounds.height;

			g2.setPaint(new GradientPaint(x, (int) (y - 0.1 * h), colorDark, x, (int) (y + 1.2 * h), light));
			g2.fillRect(x, y, w, h);
			g2.setPaint(new GradientPaint(x, (int) (y + .65 * h), light, x, (int) (y + 1.3 * h), colorDark));
			g2.fillRect(x, (int) (y + .65 * h), w, (int) (h - .65 * h));

			if (w > 14) {
				int size = 10;
				g2.setColor(colorBright);
				g2.fillRect(x + w / 2 - size / 2, y + h / 2 - size / 2, size, size);
				g2.setColor(new Color(120, 120, 120));
				g2.fillRect(x + w / 2 - 4, h / 2 - 4, 2, 2);
				g2.fillRect(x + w / 2 - 1, h / 2 - 4, 2, 2);
				g2.fillRect(x + w / 2 + 2, h / 2 - 4, 2, 2);
				g2.setColor(colorDark);
				g2.fillRect(x + w / 2 - 4, h / 2 - 2, 2, 6);
				g2.fillRect(x + w / 2 - 1, h / 2 - 2, 2, 6);
				g2.fillRect(x + w / 2 + 2, h / 2 - 2, 2, 6);
				g2.setColor(new Color(170, 170, 170));
				g2.fillRect(x + w / 2 - 4, h / 2 + 2, 2, 2);
				g2.fillRect(x + w / 2 - 1, h / 2 + 2, 2, 2);
				g2.fillRect(x + w / 2 + 2, h / 2 + 2, 2, 2);
			}

			g2.setColor(black);
			g2.drawRoundRect(x, y, w - 1, h - 1, 2, 2);
			g2.setColor(white);
			g2.drawRoundRect(x + 1, y + 1, w - 3, h - 3, 2, 2);

			g2.setColor(black.darker());
			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setFont(getFont());
			g2.drawString(getText(), lx + gap, y + h / 2 + h / 4);
		}
	}
}
