package org.first.team2485.scoutingform.bluetooth;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

		case "sendWiFiButton":
			new SendWiFiThread().start();
			break;

		case "refreshButton":
			new SearchThread(false).start();
			break;

		case "clearButton":
			bluetoothPanel.getConsole().setText("");
			break;

		case "returnButton":
			new ReturnThread().start();
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

			this.setName("SearchThread:local=" + local + ":" + hashCode());

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

			this.setName("SendThread:" + hashCode());

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

			bluetoothPanel.shutdownBluetooth(true);
		}
	}

	class SendWiFiThread extends Thread {

		@Override
		public void run() {

			this.setName("SendWiFiThread:" + hashCode());

			System.out.println("Sending data via WiFi...");

			boolean result = BluetoothSystem.sendViaWiFi(bluetoothPanel.getDataToSend());

			if (result) {
				bluetoothPanel.shutdownBluetooth(true);
			}
		}
	}

	class ReturnThread extends Thread {

		@Override
		public void run() {

			this.setName("ReturnThread:" + hashCode());

			int result = JOptionPane.showConfirmDialog(null,
					"Are you sure you want to go back to the form?\nThe current data will not be sent.",
					"Confirm Return to Form", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE, null);

			if (result == JOptionPane.YES_OPTION) {
				bluetoothPanel.shutdownBluetooth(false);
			}
		}
	}

	public static void startLocalScan() {
		new SearchThread(true).start();
	}
}
