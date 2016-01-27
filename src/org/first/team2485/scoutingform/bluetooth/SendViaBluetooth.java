package org.first.team2485.scoutingform.bluetooth;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.bluetooth.RemoteDevice;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class SendViaBluetooth {

	private static void send(String fileName, String dataToSend) {

		RemoteDevice[] alreadyPaired = BluetoothSystem.pairedDevices();

		if (alreadyPaired != null) {
			int input = JOptionPane.showConfirmDialog(null,
					"You are already paired with some devices, do you want to use one of those?",
					"Use Existing Devices?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

			if (input == JOptionPane.YES_OPTION) {
				String[] options = new String[alreadyPaired.length];

				for (int i = 0; i < alreadyPaired.length; i++) {

					if (alreadyPaired[i] != null) {

						try {
							options[i] = alreadyPaired[i].getFriendlyName(true);
							System.out.println("Added Name: " + options[i]);
						} catch (IOException e) {
							e.printStackTrace();
						}
					} else {
						System.out.println("Location: " + i + " is null");
					}
				}

				System.out.println("String Options: " + Arrays.toString(options));

				System.out.println("Showing PopUp");

				String input2 = (String) JOptionPane.showInputDialog(null, "Pick A Device:", "Connected Devices",
						JOptionPane.QUESTION_MESSAGE, null, options, null);
				
				attemptSend(input2, alreadyPaired, fileName, dataToSend);
				return;
			}
		}

		RemoteDevice[] devices = BluetoothSystem.discoverDevices();

		System.out.println("Devices1: " + Arrays.toString(devices));

		ArrayList<RemoteDevice> devicesArrayList = new ArrayList<RemoteDevice>();

		devicesArrayList.addAll(BluetoothSystem.filterDevicesByService(devices, BluetoothSystem.OBEX));

		System.out.println("ArrayList1: " + devicesArrayList);

		while (devicesArrayList.contains(null)) {
			devicesArrayList.remove(null);
		}

		System.out.println("ArrayList2: " + devicesArrayList);

		devices = devicesArrayList.toArray(new RemoteDevice[devicesArrayList.size()]);

		System.out.println("Devices Length: " + devices.length);

		if (devices.length == 0) {
			System.out.println("ARRAY IS EMPTY");
			return;
		}

		System.out.println("Devices2: " + Arrays.toString(devices));

		try {
			System.out.println("Item at Devices Pos 0: " + devices[0].getFriendlyName(true));
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		String[] options = new String[devices.length];

		for (int i = 0; i < devices.length; i++) {

			if (devices[i] != null) {

				try {
					options[i] = devices[i].getFriendlyName(true);
					System.out.println("Added Name: " + options[i]);
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				System.out.println("Location: " + i + " is null");
			}
		}

		System.out.println("String Options: " + Arrays.toString(options));

		System.out.println("Showing PopUp");

		String input = (String) JOptionPane.showInputDialog(null, "Pick A Device:", "Devices Found",
				JOptionPane.QUESTION_MESSAGE, null, options, null);

		System.out.println("PopUp closed");
		
		attemptSend(input, devices, fileName, dataToSend);
	}

	private static void attemptSend(String deviceName, RemoteDevice[] devices, String fileName, String data) {
		
		if (deviceName == null || deviceName == "") {
			return;
		}

		for (RemoteDevice device : devices) {
			try {
				if (device.getFriendlyName(true).equals(deviceName)) {
					BluetoothSystem.sendToDevice(device, fileName, data);
					break;
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
