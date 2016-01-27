package org.first.team2485.scoutingform.bluetooth;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Reader;
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

public class BluetoothSystem implements DiscoveryListener {

	private static BluetoothSystem instance;

	public static UUID OBEX = new UUID(0x1105); // OBEX Object Push service

	private static Object lock = new Object();

	private static HashMap<RemoteDevice, String> URLmap;

	private static BluetoothSystem getInstance() {

		System.out.println("Asked for Instance: " + instance);

		if (instance == null) {
			instance = new BluetoothSystem();
		}

		return instance;

	}

	public static RemoteDevice[] pairedDevices() {
		try {
			return LocalDevice.getLocalDevice().getDiscoveryAgent().retrieveDevices(DiscoveryAgent.PREKNOWN);
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}

		return new RemoteDevice[0];
	}

	public static RemoteDevice[] discoverDevices() {

		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			DiscoveryAgent agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, getInstance());

			try {
				synchronized (lock) {
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.CACHED);

			ArrayList<RemoteDevice> arrayList = new ArrayList<RemoteDevice>();

			for (RemoteDevice device : devices) {
				arrayList.add(device);
			}

			for (int i = 0; i < arrayList.size(); i++) {
				if (arrayList.get(i) == null || arrayList.get(i).getFriendlyName(true).length() < 2) {
					arrayList.remove(i);
					i--;
				}
			}
			
			devices = arrayList.toArray(new RemoteDevice[arrayList.size()]);

			System.out.println("Device Inquiry Completed. ");

			return devices;
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

	public static Set<RemoteDevice> filterDevicesByService(RemoteDevice[] devices, UUID service) {

		UUID[] uuidSet = new UUID[] { service };

		int[] attrIDs = new int[] { 0x0100 }; // Service name

		URLmap = new HashMap<RemoteDevice, String>();

		LocalDevice localDevice = null;
		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}
		DiscoveryAgent agent = localDevice.getDiscoveryAgent();

		System.out.println("Array of Devices: " + Arrays.toString(devices));

		for (RemoteDevice device : devices) {

			System.out.println("------------------------------------");

			String deviceName = "";

			try {
				deviceName = device.getFriendlyName(true);

				System.out.println("Searching Services On: " + deviceName);

				agent.searchServices(attrIDs, uuidSet, device, getInstance());

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

			System.out.println("KeySet: " + URLmap.keySet());

			if (URLmap.containsKey(null)) {
				String url = URLmap.get(null);

				URLmap.put(device, url);

				URLmap.remove(null);

				System.out.println("Found null, swapped devices");
			} else {
				System.out.println("null not found, next cycle...");
			}

			System.out.println("KeySet2: " + URLmap.keySet());

			System.out.println("Service Search Finished On: " + deviceName);
		}

		System.out.println("All Service Searches Finshed");

		return URLmap.keySet();

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

					System.out.println("Has Service, adding to map");

					URLmap.put(null, url);
				}
			} else {
				System.out.println("Service Found: " + url);
			}
		}
	}

	public static void sendToDevice(RemoteDevice toSend, File file) {

		Set<RemoteDevice> resultSet = filterDevicesByService(new RemoteDevice[] { toSend }, OBEX);

		if (resultSet.isEmpty()) {
			try {
				System.out.println("CANNOT SEND TO: " + toSend.getFriendlyName(true));
			} catch (IOException e) {
				e.printStackTrace();
			}
			return;
		}

		String serverURL = URLmap.get(resultSet.toArray(new RemoteDevice[1])[0]);

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

				String fileData = readFile(file);

				// Send a file with meta data to the server
				final byte filebytes[] = fileData.getBytes();
				final HeaderSet hs = cs.createHeaderSet();
				hs.setHeader(HeaderSet.NAME, file.getName());
				hs.setHeader(HeaderSet.TYPE, "text/plain");
				hs.setHeader(HeaderSet.LENGTH, new Long(filebytes.length));

				putOperation = cs.put(hs);
				System.out.println("Pushing file: " + file.getName());
				System.out.println("Total file size: " + filebytes.length + " bytes");

				outputStream = putOperation.openOutputStream();
				outputStream.write(filebytes);
				System.out.println("File push complete");
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

	private static String readFile(File file) {

		String fileContents = "";

		BufferedReader bufferedReader = null;

		try {

			Reader reader = new FileReader(file);

			bufferedReader = new BufferedReader(reader);

			String currentLine = bufferedReader.readLine();

			while (currentLine != null) {

				fileContents += currentLine + "\n";

				currentLine = bufferedReader.readLine();
			}

		} catch (IOException e) {
			e.printStackTrace();
		} finally {

			if (bufferedReader != null) {
				try {
					bufferedReader.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return fileContents;
	}

	public static void disconnect() {

	}
}