package org.first.team2485.scoutingform.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import javax.bluetooth.BluetoothStateException;
import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;
import javax.microedition.io.Connection;
import javax.microedition.io.Connector;
import javax.obex.ClientSession;
import javax.obex.HeaderSet;
import javax.obex.Operation;

import com.intel.bluetooth.RemoteDeviceHelper;

/**
 * 
 * @author Nicholas Contreras
 *
 */
public class BluetoothSystem implements DiscoveryListener {

	private static BluetoothSystem instance;

	public static UUID OBEX = new UUID(0x1105); // OBEX Object Push service

	private static Object lock = new Object();

	private static ExpandedRemoteDevice currentDevice;
	
	public static void callMethod() {
		
	}
	
	private class MethodThread extends Thread {
		
		private int method;
		
		public MethodThread(int method) {
			this.method = method;
		}
		
		@Override
		public void run() {
			
		}
	}

	private static BluetoothSystem getInstance() {

		System.out.println("Asked for Instance: " + instance);

		if (instance == null) {
			instance = new BluetoothSystem();
		}

		return instance;

	}

	public static ExpandedRemoteDevice[] pairedDevices() {
		
		System.out.println("Starting looking for paired devices");
		
		try {
			RemoteDevice[] devices = LocalDevice.getLocalDevice().getDiscoveryAgent()
					.retrieveDevices(DiscoveryAgent.PREKNOWN);
			
			System.out.println("Raw Length: " + devices.length);

			ExpandedRemoteDevice[] expandedDevices = new ExpandedRemoteDevice[devices.length];

			for (int i = 0; i < devices.length; i++) {
				expandedDevices[i] = new ExpandedRemoteDevice(devices[i]);
			}

			return expandedDevices;

		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		
		System.out.println("Returning empty array");

		return new ExpandedRemoteDevice[0];
	}

	public static ExpandedRemoteDevice[] discoverDevices() {
		
		System.out.println("Now discovering devices");

		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, getInstance());
			
			System.out.println("Started, waiting on lock...");

			try {
				synchronized (lock) {
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			System.out.println("Lock opened, waiting on timer");
			
			Thread.sleep(5000);
			
			System.out.println("Timer ended, reading...");

			RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.CACHED);
			
			System.out.println("Raw length: " + devices.length);

			ArrayList<ExpandedRemoteDevice> arrayList = new ArrayList<ExpandedRemoteDevice>();

			for (RemoteDevice device : devices) {
				arrayList.add(new ExpandedRemoteDevice(device));
			}

			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i) == null || arrayList.get(i).getName().length() < 2) {
					arrayList.remove(i);
					i--;
				}
			}

			ExpandedRemoteDevice[] expandedDevices = arrayList.toArray(new ExpandedRemoteDevice[arrayList.size()]);

			System.out.println("Device Inquiry Completed. ");

			return expandedDevices;
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("RETURNING NULL");

		return null;
	}

	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {

		String name;

		try {
			name = btDevice.getFriendlyName(false);
		} catch (Exception e) {
			name = btDevice.getBluetoothAddress();
		}

		System.out.println("Device Found: " + name);

	}

	@Override
	public void inquiryCompleted(int arg0) {
		synchronized (lock) {
			lock.notify();
		}
	}

	public static void setValuesForDevice(ExpandedRemoteDevice device, UUID service) {
		
		System.out.println("Setting values on: " + device.getName());

		UUID[] uuidSet = new UUID[] { service };

		int[] attrIDs = new int[] { 0x0100 }; // Service name

		currentDevice = device;

		LocalDevice localDevice = null;

		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}

		DiscoveryAgent agent = localDevice.getDiscoveryAgent();

		System.out.println("------------------------------------");

		String deviceName = device.getName();

		try {
			System.out.println("Searching Services On: " + deviceName);

			agent.searchServices(attrIDs, uuidSet, device.getRemoteDevice(), getInstance());

			System.out.println("Started Search...");

		} catch (IOException e) {
			e.printStackTrace();
		}

		System.out.println("Waiting on lock");

		try {
			synchronized (lock) {
				lock.wait();
			}

		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Lock opened, waiting on timer...");

		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		System.out.println("Timer ended, resuming");

	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {

		System.out.println("Service Search Completed, unlocking");

		System.out.println("Arg0: " + arg0 + " Arg1: " + arg1);

		synchronized (lock) {
			lock.notify();
		}
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {

		System.out.println("Services Discovered... -->");

		System.out.println("Sevices Discovered: " + transID + " | Service Record: [");

		for (ServiceRecord sr : servRecord) {
			System.out.println(sr.toString() + ", ");
		}

		System.out.println("]");

		for (int i = 0; i < servRecord.length; i++) {
			String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

			if (url == null) {
				System.out.println("Service URL Is null, Skipping...");

				continue;
			}

			DataElement serviceName = servRecord[i].getAttributeValue(0x0100);

			if (serviceName != null) {
				System.out.println("Service: " + serviceName.getValue() + " Found: " + url);

				if (serviceName.getValue().equals("OBEX Object Push")) {

					System.out.println("Has Service, setting values");

					currentDevice.state = ExpandedRemoteDevice.OBEX_SUPPORTED;
					currentDevice.URL = url;
					return;
				}
			} else {
				System.out.println("Service Found: " + url);
			}
		}
		currentDevice.state = ExpandedRemoteDevice.OBEX_UNSUPPORTED;
	}

	public static void sendToDevice(ExpandedRemoteDevice device, String fileName, String dataToSend) {

		String serverURL = device.URL;

		try {
			Connection connection = null;
			OutputStream outputStream = null;
			Operation putOperation = null;
			ClientSession cs = null;
			try {
				// Send a request to the server to open a connection
				connection = Connector.open(serverURL);
				cs = (ClientSession) connection;
				cs.connect(null);

				System.out.println("OPP session created");

				// Send a file with meta data to the server
				final byte filebytes[] = dataToSend.getBytes();
				final HeaderSet hs = cs.createHeaderSet();
				hs.setHeader(HeaderSet.NAME, fileName);
				hs.setHeader(HeaderSet.TYPE, "text/plain");
				hs.setHeader(HeaderSet.LENGTH, new Long(filebytes.length));

				putOperation = cs.put(hs);
				System.out.println("Pushing: " + fileName);
				System.out.println("Total size: " + filebytes.length + " bytes");

				outputStream = putOperation.openOutputStream();
				outputStream.write(filebytes);
				System.out.println("Push complete");
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				outputStream.close();
				putOperation.close();
				cs.disconnect(null);
				connection.close();
				System.out.println("Connection Closed");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}
}