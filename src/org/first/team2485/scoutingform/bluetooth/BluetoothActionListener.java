package org.first.team2485.scoutingform.bluetooth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import javax.swing.JOptionPane;

/**
 * 
 * @author Nicholas Contreras
 *
 */

public class BluetoothActionListener implements ActionListener {

	private static BluetoothPanel bluetoothPanel;

	public static void setBluetoothPanel(BluetoothPanel bp) {
		bluetoothPanel = bp;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		String command = e.getActionCommand();

		switch (command) {

		case "sendButton":
			new SendThread().start();
			break;

		case "refreshButton":
			new SearchThread(false).start();
			break;

		case "clearButton":
			bluetoothPanel.getConsole().setText("");
			break;
		}
	}

	static class SearchThread extends Thread {

		private boolean local;

		SearchThread(boolean local) {
			this.local = local;
		}

		@Override
		public void run() {

			if (local) {
				bluetoothPanel.localScan();
			} else {
				bluetoothPanel.remoteScan();
			}

			System.out.println("Exiting scan thread");
		}
	}

	class SendThread extends Thread {

		@Override
		public void run() {

			if (bluetoothPanel.getSelectedDevice() == null
					|| bluetoothPanel.getSelectedDevice().state != ExpandedRemoteDevice.OBEX_SUPPORTED) {
				JOptionPane.showMessageDialog(null,
						"Cannot send a file to this device\nREASON:\nUnknown or incompatable device", "Failed To Send",
						JOptionPane.ERROR_MESSAGE, null);
				return;
			}

			boolean result = BluetoothSystem.sendToDevice(bluetoothPanel.getSelectedDevice(),
					bluetoothPanel.getFileName(), bluetoothPanel.getDataToSend());

			if (!result) {
				System.out.println("***BLUETOOTH SEND FAILED***");
				return;
			}

			bluetoothPanel.shutdownBluetooth();
		}
	}

	public static void startLocalScan() {
		new SearchThread(true).start();
	}
}
