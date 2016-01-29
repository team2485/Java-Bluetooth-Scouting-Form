package org.first.team2485.scoutingform.bluetooth2;

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

		case "refreshButton":
			new SearchThread().start();
			break;

		case "clearButton":
			bluetoothPanel.getConsole().setText("");
			break;
		}
	}

	class SearchThread extends Thread {

		@Override
		public void run() {
			bluetoothPanel.preformFullScan();
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

			BluetoothSystem.sendToDevice(bluetoothPanel.getSelectedDevice(), bluetoothPanel.getFileName(),
					bluetoothPanel.getDataToSend());

			bluetoothPanel.shutdownBluetooth();
		}
	}
}
