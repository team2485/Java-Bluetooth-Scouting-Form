package org.first.team2485.scoutingform.bluetooth;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;

import javax.bluetooth.RemoteDevice;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JComponent;
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

@SuppressWarnings("serial")
public class BluetoothPanel extends JPanel implements ActionListener, ListCellRenderer<ExpandedRemoteDevice> {

	private final JFrame frame;

	private final JList<ExpandedRemoteDevice> deviceList;
	private final DefaultListModel<ExpandedRemoteDevice> listModel;

	private final JTextArea console;

	private final JButton connectButton;
	private final JButton refreshButton;

	private final String fileName;
	private final String dataToSend;

	private final PrintStream oldPrintStream;
	private boolean runThread;

	public static void main(String[] args) {
		initBluetooth("testName", "testData");
	}

	public static void initBluetooth(String fileName, String dataToSend) {

		BluetoothPanel bp = new BluetoothPanel(fileName, dataToSend);

		bp.loadAlreadyPairedDevices();

	}

	private BluetoothPanel(String fileName, String dataToSend) {

		oldPrintStream = System.out;

		// System.setOut(new PrintStream(new OutputStream() {
		//
		// @Override
		// public void write(int b) throws IOException {
		// writeToConsole(((char) b) + "");
		// }
		// }));

		console = new JTextArea("*Bluetooth Selector Started*\n");
		console.setEditable(false);

		System.out.println("Switched consoles");

		this.fileName = fileName;
		this.dataToSend = dataToSend;
		runThread = true;

		frame = new JFrame("Bluetooth Device Connector");

		this.setPreferredSize(new Dimension(600, 500));

		frame.add(this);

		this.setLayout(new GridLayout(2, 2));

		listModel = new DefaultListModel<ExpandedRemoteDevice>();

		deviceList = new JList<ExpandedRemoteDevice>(listModel);

		deviceList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		deviceList.setCellRenderer(this);

		connectButton = new JButton("Select A Device To Connect To");

		connectButton.setEnabled(false);

		connectButton.addActionListener(this);

		refreshButton = new JButton("RESCAN FOR DEVICES");

		refreshButton.addActionListener(this);

		JScrollPane scrollPane = new JScrollPane(deviceList);

		this.add(scrollPane);
		this.add(console);
		this.add(connectButton);
		this.add(refreshButton);
		
		new RepaintThread().start();

		frame.pack();

		frame.setVisible(true);

		System.out.println("Panel constructed");
	}

	private void addToList(ExpandedRemoteDevice newDevice) {

		for (int i = 0; i < listModel.size(); i++) {

			if (listModel.get(i).equals(newDevice)) {
				listModel.set(i, newDevice);
				return;
			}
		}

		listModel.addElement(newDevice);
	}

	private void refreshDevices() {

		listModel.clear();

		loadAlreadyPairedDevices();

		scanAndLoadNewDevices();

	}

	private void scanAndLoadNewDevices() {

		System.out.println("Scanning for new devices");

		ExpandedRemoteDevice[] newDevices = BluetoothSystem.discoverDevices();

		System.out.println("Devices1: " + Arrays.toString(newDevices));

		ArrayList<ExpandedRemoteDevice> devicesArrayList = new ArrayList<ExpandedRemoteDevice>();

		for (ExpandedRemoteDevice curDevice : newDevices) {
			addToList(curDevice);
			devicesArrayList.add(curDevice);
		}

		for (int i = 0; i < devicesArrayList.size(); i++) {

			BluetoothSystem.setValuesForDevice(devicesArrayList.get(i), BluetoothSystem.OBEX);
			addToList(devicesArrayList.get(i));
		}
	}

	private void loadAlreadyPairedDevices() {

		System.out.println("Scanning for pre-paired");

		ExpandedRemoteDevice[] alreadyPaired = BluetoothSystem.pairedDevices();

		System.out.println("Got pre-paired devices");

		if (alreadyPaired != null) {

			for (ExpandedRemoteDevice curDevice : alreadyPaired) {
				curDevice.state = ExpandedRemoteDevice.PAIRED_DEVICE;
				this.addToList(curDevice);
				System.out.println("Added: " + curDevice.getName());
			}
		}
	}

	private void writeToConsole(String toWrite) {
		console.setText(console.getText() + toWrite);
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		Object source = e.getSource();

		if (source.equals(connectButton)) {
			BluetoothSystem.sendToDevice(deviceList.getSelectedValue(), fileName, dataToSend);
			close();
		} else if (source.equals(refreshButton)) {
			refreshDevices();
		}
	}

	public void close() {

		System.out.println("CLOSING");

		System.setOut(oldPrintStream);

		runThread = false;

		frame.dispose();
	}

	private class RepaintThread extends Thread {

		@Override
		public void run() {

			while (runThread) {
				frame.repaint();

				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Override
	public Component getListCellRendererComponent(JList list, ExpandedRemoteDevice device, int index, boolean isSelected,
			boolean cellHasFocus) {

		JLabel text = new JLabel();

		text.setText(device.getName());

		switch (device.state) {

		case ExpandedRemoteDevice.OBEX_SUPPORTED:
			text.setBackground(Color.GREEN);
			text.setToolTipText("This device is a valid recipient");
			break;

		case ExpandedRemoteDevice.PAIRED_DEVICE:
			text.setBackground(Color.BLUE);
			text.setToolTipText("This device is already paired");
			break;

		case ExpandedRemoteDevice.UNCHECKED_DEVICE:
			text.setBackground(Color.YELLOW);
			text.setToolTipText("This device has not yet been queried");
			break;

		case ExpandedRemoteDevice.OBEX_UNSUPPORTED:
			text.setBackground(Color.RED);
			text.setToolTipText("This device cannot recieve files");
			break;
		}
		
		System.out.println("State: " + device.state);

		System.out.println("Called Expanded Graphics: " + text.getText());

		return text;
	}
}
