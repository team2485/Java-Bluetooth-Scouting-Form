package org.first.team2485.scoutingform.bluetooth;

import java.awt.Color;
import java.awt.Component;
import java.io.IOException;

import javax.bluetooth.RemoteDevice;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;

public class ExpandedRemoteDevice {

	public static final int OBEX_SUPPORTED = 2, PAIRED_DEVICE = 1, UNCHECKED_DEVICE = -1, OBEX_UNSUPPORTED = -2;

	public String URL;
	public int state;

	private String name;
	
	private RemoteDevice remoteDevice;

	public ExpandedRemoteDevice(RemoteDevice remoteDevice) {
		
		this.remoteDevice = remoteDevice;

		state = UNCHECKED_DEVICE;
	}
	
	public RemoteDevice getRemoteDevice() {
		return remoteDevice;
	}

	public String getName() {

		if (name == null) {

			try {
				name = remoteDevice.getFriendlyName(true);
			} catch (IOException e) {
				name = remoteDevice.getBluetoothAddress();
			}
		}
		return name;
	}

	@Override
	public String toString() {
		return URL;
	}
}
