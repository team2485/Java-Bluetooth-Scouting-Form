package org.first.team2485.scoutingform.questions;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;

import org.first.team2485.scoutingform.gui.LockedSizeJPanel;

/**
 * @author Nicholas Contreras
 */

@SuppressWarnings("serial")
public class LocationQuestion extends Question {

	private static final String FLAG_FILE_NAME = "/flag.png";

	private BufferedImage image;

	private BufferedImage flagImage;

	private ArrayList<Point> selectedPoints;

	private JButton clearButton;

	public LocationQuestion(String question, String internalName, String img) {
		
		super(internalName);

		InputStream imgInput = this.getClass().getResourceAsStream(img);
		InputStream flagInput = this.getClass().getResourceAsStream(FLAG_FILE_NAME);

		try {
			image = ImageIO.read(imgInput);
			flagImage = ImageIO.read(flagInput);
		} catch (IOException e) {
			e.printStackTrace();
		}

		selectedPoints = new ArrayList<Point>();

		this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

		clearButton = new JButton("Clear All Flags");
		clearButton.addActionListener((ActionEvent e) -> clear());

		Box b = new Box(BoxLayout.X_AXIS);

		b.add(Box.createHorizontalStrut(5));
		b.add(new JLabel(question));
		b.add(Box.createHorizontalGlue());
		b.add(clearButton);

		this.add(b);

		ImagePanel imagePanel = new ImagePanel();
		imagePanel.addMouseListener(imagePanel);
		this.add(imagePanel);
	}

	@Override
	public String getData() {
		String data = getInternalName() + ",";

		for (Point p : selectedPoints) {
			data += "(" + (p.getX() / image.getWidth()) + ":" + (p.getY() / image.getHeight()) + ");";
		}

		return data + ",";
	}

	@Override
	public void clear() {
		selectedPoints.clear();
		repaint();
	}

	private class ImagePanel extends LockedSizeJPanel implements MouseListener {

		private ImagePanel() {
			this.setPreferredSize(new Dimension(image.getWidth(), image.getHeight()));

			this.setVisible(true);
		}

		@Override
		public void paintComponent(Graphics g) {

			g.setColor(this.getBackground());
			g.fillRect(0, 0, image.getWidth(), image.getHeight());

			g.drawImage(image, 0, 0, null);

			for (Point p : selectedPoints) {
				g.drawImage(flagImage, p.x, p.y - flagImage.getHeight(), null);
			}
		}

		@Override
		public void mouseClicked(MouseEvent arg0) {
			if (arg0.getButton() == MouseEvent.BUTTON1) {
				selectedPoints.add(arg0.getPoint());
			} else if (arg0.getButton() == MouseEvent.BUTTON3) {

				for (int i = 0; i < selectedPoints.size(); i++) {

					Point curFlag = selectedPoints.get(i);

					if (arg0.getX() > curFlag.getX() && arg0.getY() > curFlag.getY() - flagImage.getHeight()) {
						if (arg0.getX() < curFlag.getX() + flagImage.getWidth() && arg0.getY() < curFlag.getY()) {
							selectedPoints.remove(i);
							break;
						}
					}
				}
			}

			repaint();
		}

		@Override
		public void mouseEntered(MouseEvent arg0) {
		}

		@Override
		public void mouseExited(MouseEvent arg0) {
		}

		@Override
		public void mousePressed(MouseEvent arg0) {
		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
		}
	}
}
