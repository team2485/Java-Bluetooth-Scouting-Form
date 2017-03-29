package org.first.team2485.scoutingform.bluetooth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.GridLayout;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.BorderFactory;
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
import org.first.team2485.scoutingform.util.Logger;

/**
 * 
 * @author Nicholas Contreras
 *
 */

@SuppressWarnings("serial")
public class BluetoothPanel extends JPanel implements ListCellRenderer<ExpandedRemoteDevice> {

	final public static String SCRIPT_URL = "https://script.google.com/macros/s/AKfycbxdQigOa6DuajIWo7HWFyCAGS0hLCPkcB-HbfSkYuk29qEQqEq3/exec";
	
	private JFrame frame;

	private String fileName, dataToSend;

	private JList<ExpandedRemoteDevice> deviceList;
	private JTextArea console;

	private JButton sendButton, sendViaWiFiButton, refreshButton, clearConsoleButton, returnToFormButton;

	private JLabel status;

	private PrintStream oldPrintStream, oldErrorStream;

	private boolean stop;

	public BluetoothPanel(String fileName, String dataToSend) {
		
		Logger.getInst().log("initing the bluetooth panel");

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
		
		Logger.getInst().log("output streams transfered");

		this.fileName = fileName;
		this.dataToSend = dataToSend;

		setUpPanel();
		setUpMainWindows();
		setUpButtons();

		frame.pack();
		frame.setVisible(true);
		
		Logger.getInst().log("frame set visible");

		new PaintThread().start();
		
		Logger.getInst().log("starting a local scan");

		BluetoothActionListener.startLocalScan();

		System.out.println("Preparing local records");
		
		Logger.getInst().log("writing data to local records");

		writeToScoutingRecords(fileName, dataToSend);
	}

	public static void writeToScoutingRecords(String fileName, String data) {
		File loc = new File(System.getProperty("user.home") + "/ScoutingRecords");

		if (!loc.exists()) {
			System.out.println(loc);
			System.out.println("Making dirs: " + loc.mkdir());
		} else {
			System.out.println("Record structure exists");
		}

		try {
			BufferedWriter bw = new BufferedWriter(new FileWriter(new File(loc, fileName)));
			bw.write(data);
			bw.close();
			System.out.println("Sucessfully wrote data to local records");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	class PaintThread extends Thread {
		
		private PaintThread() {
			setName("Bluetooth-Panel-Paint-Thread" + hashCode());
			setDaemon(true);
		}

		@Override
		public void run() {
			
			Logger.getInst().log("starting the bluetooth panel paint thread");

			while (!stop) {

				customPaint();

				frame.repaint();

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}

				continue;
			}
			
			Logger.getInst().log("exiting the bluetooth panel paint thread");
		}
	}

	private void customPaint() {

		if (BluetoothSystem.isBusy()) {
			refreshButton.setText("Please Wait");
			refreshButton.setToolTipText("Please wait...");
			sendButton.setToolTipText("Cannot send while busy");
			sendViaWiFiButton.setToolTipText("Cannot send while busy");

			refreshButton.setEnabled(false);
			sendButton.setEnabled(false);
			sendViaWiFiButton.setEnabled(false);

			status.setText("BUSY");
			status.setForeground(new Color(255, 128, 0));
		} else {
			refreshButton.setText("Scan For Devices");
			refreshButton.setToolTipText("Start looking for new devices");
			sendButton.setToolTipText("Send the scouting data to the device selected");
			sendViaWiFiButton.setToolTipText("Send the scouting data via WiFi");
			
			refreshButton.setEnabled(true);
			sendButton.setEnabled(true);
			sendViaWiFiButton.setEnabled(true);

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

		sendButton = new JButton("Send Data via Bluetooth");
		sendViaWiFiButton = new JButton("Send Data via WiFi");
		refreshButton = new JButton("Scan For Devices");
		clearConsoleButton = new JButton("Clear Console");
		returnToFormButton = new JButton("Return to Form");

		sendButton.setActionCommand("sendButton");
		sendButton.addActionListener(new BluetoothActionListener());
		sendButton.setToolTipText("Send the scouting data to the device selected");
		
		sendViaWiFiButton.setActionCommand("sendWiFiButton");
		sendViaWiFiButton.addActionListener(new BluetoothActionListener());
		sendViaWiFiButton.setToolTipText("Send the scouting data via WiFi");
		
		refreshButton.setActionCommand("refreshButton");
		refreshButton.addActionListener(new BluetoothActionListener());
		refreshButton.setToolTipText("Start looking for new devices");

		clearConsoleButton.setActionCommand("clearButton");
		clearConsoleButton.addActionListener(new BluetoothActionListener());
		clearConsoleButton.setToolTipText("Clear the console window");

		returnToFormButton.setActionCommand("returnButton");
		returnToFormButton.addActionListener(new BluetoothActionListener());
		returnToFormButton.setToolTipText("Return to the scouting form to scout again");

		buttonPanel.add(sendButton);
		buttonPanel.add(sendViaWiFiButton);
		buttonPanel.add(refreshButton);
		buttonPanel.add(clearConsoleButton);
		buttonPanel.add(returnToFormButton);

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
		
		Logger.getInst().log("starting the local scan");

		System.out.println("About to scan");
		
		Logger.getInst().log("getting paired devices");

		ExpandedRemoteDevice[] alreadyPaired = BluetoothSystem.pairedDevices();

		Logger.getInst().log("got paired devices");
		
		if (alreadyPaired.length == 0) {
			Logger.getInst().log("found no paired devices");
			System.out.println("Not currently paired");
			return;
		}

		if (stop) {
			Logger.getInst().log("stop is true, aborting");
			return;
		}
		
		Logger.getInst().log("adding devices to list");

		addToList(alreadyPaired);

		int input = JOptionPane.showConfirmDialog(null,
				"The preliminary query has gotten " + alreadyPaired.length
						+ " responses. Would you like to evaluate these devices now?",
				"Bluetooth System", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null);

		if (input == JOptionPane.YES_OPTION) {
			
			Logger.getInst().log("selected yes, preforming service search");

			for (ExpandedRemoteDevice curDevice : alreadyPaired) {

				if (stop) {
					Logger.getInst().log("stop is true, aborting");
					return;
				}

				System.out.println("****************");

				System.out.println("Setting Values For: " + curDevice.getName());

				Logger.getInst().log("setting values for device: " + curDevice.getName());
				
				BluetoothSystem.setValuesForDevice(curDevice, BluetoothSystem.OBEX);

				System.out.println("Just updated values for: " + curDevice.getName());

				System.out.println("Value is now: " + curDevice.state);

			}
		}
	}

	public void remoteScan() {

		System.out.println("Beginning remote scan");

		ExpandedRemoteDevice[] newDevices = BluetoothSystem.discoverDevices();

		System.out.println("Remote scan finished");

		if (newDevices.length == 0) {
			return;
		}

		if (stop) {
			return;
		}

		addToList(newDevices);

		for (ExpandedRemoteDevice curDevice : newDevices) {

			if (stop) {
				return;
			}

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

		text.setToolTipText("Address: " + value.getRemoteDevice() + " Status: " + text.getToolTipText());

		if (isSelected) {
			// text.setBackground(text.getBackground().darker());
			text.setText(text.getText() + " <--- SELECTED");
			text.setBorder(BorderFactory.createLoweredBevelBorder());
		} else {
			text.setBorder(BorderFactory.createRaisedBevelBorder());
		}

		return text;
	}

	public void shutdownBluetooth(boolean clearAndIncrement) {

		stop = true;

		returnToFormButton.setEnabled(false);

		BluetoothSystem.shutdown();

		System.setOut(oldPrintStream);
		System.setErr(oldErrorStream);

		frame.dispose();

		ScoutingForm.scoutingForm.restart(clearAndIncrement); // Open up another
																// form
	}
}
