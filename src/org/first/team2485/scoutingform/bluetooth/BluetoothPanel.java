package org.first.team2485.scoutingform.bluetooth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.text.DefaultCaret;

import org.first.team2485.scoutingform.QuitButton;
import org.first.team2485.scoutingform.ScoutingForm;

/**
 * 
 * @author Nicholas Contreras
 *
 */

@SuppressWarnings("serial")
public class BluetoothPanel extends JPanel implements ListCellRenderer<ExpandedRemoteDevice> {

	private JFrame frame;

	private String fileName, dataToSend;

	private JList<ExpandedRemoteDevice> deviceList;
	private JTextArea console;

	private JButton sendButton, refreshButton, clearConsoleButton;

	private JLabel status;

	private PrintStream oldPrintStream, oldErrorStream;

	private boolean stop;

	public BluetoothPanel(String fileName, String dataToSend) {

		oldPrintStream = System.out;
		oldErrorStream = System.err;

		PrintStream consoleStream = new PrintStream(new OutputStream() {

			@Override
			public void write(int b) throws IOException {
				writeToConsole(((char) b) + "");
			}
		});

		System.setOut(consoleStream);
		System.setErr(consoleStream);

		this.fileName = fileName;
		this.dataToSend = dataToSend;

		setUpPanel();
		setUpMainWindows();
		setUpButtons();

		frame.pack();
		frame.setVisible(true);

		new PaintThread().start();

		BluetoothActionListener.startLocalScan();
	}

	class PaintThread extends Thread {

		@Override
		public void run() {

			while (!stop) {

				customPaint();

				frame.repaint();

				try {
					Thread.sleep(20);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				continue;
			}
		}
	}

	private void customPaint() {

		// System.out.println("Is Busy: " + BluetoothSystem.isBusy());

		if (BluetoothSystem.isBusy()) {
			refreshButton.setText("Please Wait");
			refreshButton.setToolTipText("Please wait...");

			refreshButton.setEnabled(false);
			
			status.setText("BUSY");
			status.setForeground(new Color(255, 128, 0));
		} else {
			refreshButton.setText("Scan For Devices");
			refreshButton.setToolTipText("Start looking for new devices");

			refreshButton.setEnabled(true);
			
			status.setText("IDLE");
			status.setForeground(new Color(0, 200, 0));
		}
	}

	private void setUpPanel() {

		frame = new JFrame("Bluetooth Connection Window");

		frame.add(this);

		new QuitButton(frame);

		this.setLayout(new BorderLayout());

	}

	private void setUpMainWindows() {

		JLabel deviceListLabel = new JLabel("Known Devices", SwingConstants.CENTER);
		JLabel consoleLabel = new JLabel("Console Output", SwingConstants.CENTER);

		BluetoothActionListener.setBluetoothPanel(this);

		DefaultListModel<ExpandedRemoteDevice> listModel = new DefaultListModel<ExpandedRemoteDevice>();
		deviceList = new JList<ExpandedRemoteDevice>(listModel);
		deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		deviceList.setCellRenderer(this);

		console = new JTextArea();
		console.setEditable(false);

		JScrollPane deviceListScroller = new JScrollPane(deviceList);
		JScrollPane consoleScroller = new JScrollPane(console);

		DefaultCaret caret = (DefaultCaret) console.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JPanel mainWindow = new JPanel(new GridLayout(1, 2));

		JPanel leftCol = new JPanel();
		JPanel rightCol = new JPanel();
		leftCol.setLayout(new BoxLayout(leftCol, BoxLayout.Y_AXIS));
		rightCol.setLayout(new BoxLayout(rightCol, BoxLayout.Y_AXIS));

		leftCol.add(deviceListLabel);
		leftCol.add(deviceListScroller);

		rightCol.add(consoleLabel);
		rightCol.add(consoleScroller);

		mainWindow.add(leftCol);
		mainWindow.add(rightCol);

		this.add(mainWindow, BorderLayout.CENTER);

	}

	private void setUpButtons() {

		JPanel buttonPanel = new JPanel(new GridLayout(1, 5));

		JLabel statusLabel = new JLabel("Status: ");
		statusLabel.setHorizontalAlignment(SwingConstants.RIGHT);

		status = new JLabel("IDLE");
		status.setHorizontalAlignment(SwingConstants.LEFT);

		buttonPanel.add(statusLabel);
		buttonPanel.add(status);

		sendButton = new JButton("Send Data");
		refreshButton = new JButton("Scan For Devices");
		clearConsoleButton = new JButton("Clear Console");

		sendButton.setActionCommand("sendButton");
		sendButton.addActionListener(new BluetoothActionListener());
		sendButton.setToolTipText("Send the scouting data to the device selected");

		refreshButton.setActionCommand("refreshButton");
		refreshButton.addActionListener(new BluetoothActionListener());
		refreshButton.setToolTipText("Start looking for new devices");

		clearConsoleButton.setActionCommand("clearButton");
		clearConsoleButton.addActionListener(new BluetoothActionListener());
		clearConsoleButton.setToolTipText("Clear the console window");

		buttonPanel.add(sendButton);
		buttonPanel.add(refreshButton);
		buttonPanel.add(clearConsoleButton);

		this.add(buttonPanel, BorderLayout.SOUTH);
	}

	public ExpandedRemoteDevice getSelectedDevice() {
		return deviceList.getSelectedValue();
	}

	public String getFileName() {
		return fileName;
	}

	public String getDataToSend() {
		return dataToSend;
	}

	public JTextArea getConsole() {
		return console;
	}

	public void localScan() {

		System.out.println("About to scan");

		ExpandedRemoteDevice[] alreadyPaired = BluetoothSystem.pairedDevices();

		System.out.println("Scan");
		
		if (alreadyPaired.length == 0) {
			System.out.println("Not currently paired");
			return;
		}

		addToList(alreadyPaired);

		int input = JOptionPane.showConfirmDialog(null,
				"The preliminary query has gotten " + alreadyPaired.length
						+ " responces. Would you like to evaluate these devices now?",
				"Bluetooth System", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);

		if (input == JOptionPane.YES_OPTION) {

			for (ExpandedRemoteDevice curDevice : alreadyPaired) {

				System.out.println("****************");

				System.out.println("Setting Values For: " + curDevice.getName());

				BluetoothSystem.setValuesForDevice(curDevice, BluetoothSystem.OBEX);

				System.out.println("Just updated values for: " + curDevice.getName());

				System.out.println("Value is now: " + curDevice.state);

			}
		}
	}

	public void remoteScan() {
		
		System.out.println("Beginning remote scan");

		ExpandedRemoteDevice[] newDevices = BluetoothSystem.discoverDevices();
		
		System.out.println("Remote scaan finished");
		
		if (newDevices.length == 0) {
			return;
		}

		addToList(newDevices);

		for (ExpandedRemoteDevice curDevice : newDevices) {

			System.out.println("****************");

			System.out.println("Setting Values For: " + curDevice.getName());

			BluetoothSystem.setValuesForDevice(curDevice, BluetoothSystem.OBEX);

			System.out.println("Just updated values for: " + curDevice.getName());

			System.out.println("Value is now: " + curDevice.state);

		}

		System.out.println("Scan complete");
	}

	private void addToList(ExpandedRemoteDevice[] newDevices) {

		System.out.println("adding");

		DefaultListModel<ExpandedRemoteDevice> listModel = (DefaultListModel<ExpandedRemoteDevice>) deviceList
				.getModel();

		System.out.println("Got list model");

		for (ExpandedRemoteDevice newDevice : newDevices) {

			System.out.println("Attempting to add: " + newDevice.getName());

			boolean breakOut = false;

			for (int i = 0; i < listModel.size(); i++) {

				if (listModel.get(i).equals(newDevice) || listModel.get(i).getRemoteDevice().getBluetoothAddress()
						.equals(newDevice.getRemoteDevice().getBluetoothAddress())) {

					System.out.println("Found equal");

					System.out.println(
							"Old Bluetooth Address: " + listModel.get(i).getRemoteDevice().getBluetoothAddress());
					System.out.println("New device Address: " + newDevice.getRemoteDevice().getBluetoothAddress());

					listModel.set(i, newDevice);

					breakOut = true;
					break;
				}
			}

			if (breakOut) {
				continue;
			}

			listModel.addElement(newDevice);
		}

		System.out.println("Added new values");

	}

	private void writeToConsole(String s) {
		console.setText(console.getText() + s);
	}

	@Override
	public Component getListCellRendererComponent(JList<? extends ExpandedRemoteDevice> list,
			ExpandedRemoteDevice value, int index, boolean isSelected, boolean cellHasFocus) {

		JTextField text = new JTextField(value.getName());

		switch (value.state) {

		case ExpandedRemoteDevice.OBEX_SUPPORTED:
			text.setBackground(Color.GREEN);
			text.setToolTipText("This device is ready to recieve files");
			break;

		case ExpandedRemoteDevice.UNCHECKED_DEVICE:
			text.setBackground(Color.YELLOW);
			text.setToolTipText("This device has been detected, but its status is unknown");
			break;

		case ExpandedRemoteDevice.OBEX_UNSUPPORTED:
			text.setBackground(Color.RED);
			text.setToolTipText("This device does not support file transfers");
			break;
		}

		text.setToolTipText(text.getToolTipText() + "\n" + value.getRemoteDevice());

		if (isSelected) {
			text.setBackground(text.getBackground().darker());
			text.setText(text.getText() + " <--- SELECTED");
		}

		return text;
	}

	public void shutdownBluetooth() {
		System.setOut(oldPrintStream);
		System.setErr(oldErrorStream);

		stop = true;
		
		frame.dispose();

		ScoutingForm.displayForm(); // Open up another form
	}
}
