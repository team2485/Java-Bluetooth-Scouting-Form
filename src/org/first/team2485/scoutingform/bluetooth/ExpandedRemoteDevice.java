package org.first.team2485.scoutingform.bluetooth;

import java.io.IOException;

import javax.bluetooth.RemoteDevice;

/**
 * 
 * @author Nicholas Contreras
 *
 */

public class ExpandedRemoteDevice {

	public static final int OBEX_SUPPORTED = 1, UNCHECKED_DEVICE = 0, OBEX_UNSUPPORTED = -1;

	public String URL;
	public int state;

	private String name;
	
	private RemoteDevice remoteDevice;

	public ExpandedRemoteDevice(RemoteDevice remoteDevice) {
		
		this.remoteDevice = remoteDevice;

		state = UNCHECKED_DEVICE;
		
		getName();
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
