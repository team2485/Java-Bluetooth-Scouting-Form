package org.first.team2485.scoutingform.bluetooth;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;

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
import javax.obex.ResponseCodes;

import org.first.team2485.scoutingform.util.HTTPUtils;
import org.first.team2485.scoutingform.util.Logger;

import com.intel.bluetooth.BlueCoveConfigProperties;
import com.intel.bluetooth.BlueCoveImpl;

/**
 * @author Nicholas Contreras
 */
public class BluetoothSystem implements DiscoveryListener {

	private static BluetoothSystem instance;

	public static UUID OBEX = new UUID(0x1105); // OBEX Object Push service

	private static Object lock = new Object();

	private static ExpandedRemoteDevice currentDevice;
	private static DiscoveryAgent agent;

	private static int serviceSearchID;

	private static boolean isBusy;

	private static BluetoothSystem getInstance() {

		if (instance == null) {
			Logger.getInst().log("Initing Bluetooth System");
			instance = new BluetoothSystem();
			BlueCoveImpl.setConfigProperty(BlueCoveConfigProperties.PROPERTY_CONNECT_TIMEOUT, "10000");
			BlueCoveImpl.setConfigProperty(BlueCoveConfigProperties.PROPERTY_OBEX_TIMEOUT, "10000");
		}
		return instance;
	}

	public static void shutdown() {

		try {

			Logger.getInst().log("Shuting down bluetooth system");
			System.out.println("Shutting down bluetooth system...");
			agent.cancelInquiry(getInstance());
			agent.cancelServiceSearch(serviceSearchID);
			System.out.println("Bluetooth system shut down");
			Logger.getInst().log("Bluetooth shut down");

		} catch (Exception e) {
			e.printStackTrace();
			Logger.getInst().log("Bluetooth threw exception shutting down");
		}

		isBusy = false;
		instance = null;
	}

	public static ExpandedRemoteDevice[] pairedDevices() {

		Logger.getInst().log("finding paired devices");

		isBusy = true;

		System.out.println("Starting looking for paired devices");

		try {
			agent = LocalDevice.getLocalDevice().getDiscoveryAgent();

			RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.PREKNOWN);

			if (devices == null) {
				Logger.getInst().log("did not find any local devices");
				isBusy = false;
				return new ExpandedRemoteDevice[0];
			}

			System.out.println("Raw Length: " + devices.length);

			ExpandedRemoteDevice[] expandedDevices = new ExpandedRemoteDevice[devices.length];

			System.out.println("New List");

			for (int i = 0; i < devices.length; i++) {
				expandedDevices[i] = new ExpandedRemoteDevice(devices[i]);
				expandedDevices[i].getName();
			}

			Logger.getInst().log("constructed local device list");

			System.out.println("Tagging not busy");

			isBusy = false;

			return expandedDevices;

		} catch (BluetoothStateException e) {
			e.printStackTrace();
			Logger.getInst().log("got exception: " + e.getMessage());
		}

		System.out.println("Returning empty array");

		Logger.getInst().log("exception thrown, returning empty list");

		isBusy = false;

		return new ExpandedRemoteDevice[0];
	}

	public static ExpandedRemoteDevice[] discoverDevices() {

		isBusy = true;

		Logger.getInst().log("preforming a device discovery");

		System.out.println("Now discovering devices");

		try {
			LocalDevice localDevice = LocalDevice.getLocalDevice();
			agent = localDevice.getDiscoveryAgent();
			agent.startInquiry(DiscoveryAgent.GIAC, getInstance());

			Logger.getInst().log("started the inquiry");

			System.out.println("Started, waiting on lock...");

			try {
				synchronized (lock) {
					lock.wait();
				}
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			Logger.getInst().log("lock opened");

			System.out.println("Lock opened, waiting on timer");

			Thread.sleep(500);

			System.out.println("Timer ended, reading...");

			Logger.getInst().log("timed ended, getting list");

			RemoteDevice[] devices = agent.retrieveDevices(DiscoveryAgent.CACHED);

			if (devices == null) {
				System.out.println("No devices found, returning empty");
				Logger.getInst().log("no devices found");
				isBusy = false;
				return new ExpandedRemoteDevice[0];
			}

			System.out.println("Raw length: " + devices.length);

			ArrayList<ExpandedRemoteDevice> arrayList = new ArrayList<ExpandedRemoteDevice>();

			for (RemoteDevice device : devices) {

				ExpandedRemoteDevice newDevice = new ExpandedRemoteDevice(device);

				System.out.println("Device Found: " + newDevice.getName());

				arrayList.add(newDevice);
			}

			for (int i = 0; i < arrayList.size(); i++) {

				if (arrayList.get(i) == null || arrayList.get(i).getName().length() < 2) {
					Logger.getInst().log("device length was less than 2, removing");
					arrayList.remove(i);
					i--;
				}
			}

			ExpandedRemoteDevice[] expandedDevices = arrayList.toArray(new ExpandedRemoteDevice[arrayList.size()]);

			System.out.println("Device Inquiry Completed");

			Logger.getInst().log("device inquiry completed");

			isBusy = false;

			return expandedDevices;
		} catch (Exception e) {
			e.printStackTrace();
			Logger.getInst().log("got exception: " + e.getMessage());
		}
		System.out.println("RETURNING NULL");

		Logger.getInst().log("returning null");

		isBusy = false;
		return null;
	}

	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
	}

	@Override
	public void inquiryCompleted(int arg0) {
		synchronized (lock) {
			lock.notify();
		}
	}

	public static void setValuesForDevice(ExpandedRemoteDevice device, UUID service) {

		isBusy = true;

		Logger.getInst().log("setting values for device: " + device.getName());

		System.out.println("Setting values on: " + device.getName());

		UUID[] uuidSet = new UUID[] { service };

		int[] attrIDs = new int[] { 0x0100 }; // Service name (0x0100)

		currentDevice = device;

		LocalDevice localDevice = null;

		try {
			localDevice = LocalDevice.getLocalDevice();
		} catch (BluetoothStateException e) {
			e.printStackTrace();
		}

		agent = localDevice.getDiscoveryAgent();

		System.out.println("------------------------------------");

		String deviceName = device.getName();

		try {
			System.out.println("Searching Services On: " + deviceName);

			serviceSearchID = agent.searchServices(attrIDs, uuidSet, device.getRemoteDevice(), getInstance());

			System.out.println("Started Search on: " + device.getRemoteDevice());

			Logger.getInst().log("started search");

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
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		Logger.getInst().log("lock and timer ended");

		System.out.println("Timer ended, resuming");

		if (currentDevice.state == ExpandedRemoteDevice.UNCHECKED_DEVICE) {

			System.out.println("Device returned without change, failing...");

			currentDevice.state = ExpandedRemoteDevice.OBEX_UNSUPPORTED;
		}

		isBusy = false;
	}

	@Override
	public void serviceSearchCompleted(int transID, int result) {

		System.out.println("Service Search with id: " + transID + " has finished:");

		Logger.getInst().log("search done, result: " + result);

		switch (result) {

		case SERVICE_SEARCH_COMPLETED:
			System.out.println("Service Search Completed Sucessfully");
			break;
		case SERVICE_SEARCH_DEVICE_NOT_REACHABLE:
			System.out.println("Service Search Failed: Device Not Reachable");
			break;
		case SERVICE_SEARCH_ERROR:
			System.out.println("Service Search Failed: Unknown Error");
			break;
		case SERVICE_SEARCH_NO_RECORDS:
			System.out.println("Service Search Failed: No Records");
			break;
		case SERVICE_SEARCH_TERMINATED:
			System.out.println("Service Search Failed: Terminated");
			break;
		}

		synchronized (lock) {
			lock.notify();
		}
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {

		System.out.println("Service Search with id " + transID + " has found a service");

		System.out.println("Service(s) Discovered: [");

		for (ServiceRecord sr : servRecord) {
			System.out.println(sr.toString() + " [[NEXT SERVICE]] ");
		}

		System.out.println("]");

		for (int i = 0; i < servRecord.length; i++) {
			String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);

			if (url == null) {
				System.out.println("Service URL Is null, Skipping...");

				continue;
			}

			DataElement serviceName = servRecord[i].getAttributeValue(0x0100);

			System.out.println("Service: " + serviceName);

			if (serviceName != null) {

				System.out.println("Service Data Type: " + serviceName.getDataType());

				System.out.println("Service URL: " + url);

				System.out.println("Service Name: " + serviceName.getValue());

				System.out.println("Is PIM: " + (serviceName.getValue().toString()).contains("PIM Item Transfer"));

				if (serviceName.getValue().toString().trim().equals("OBEX Object Push")
						|| serviceName.getValue().toString().trim().contains("PIM Item Transfer")) {

					System.out.println("Has OBEX/PIM Service, setting values");

					currentDevice.state = ExpandedRemoteDevice.OBEX_SUPPORTED;
					currentDevice.URL = url;
					isBusy = false;
					return;
				}
			} else {
				System.out.println("Null Service Found: " + url);
			}
		}

		System.out.println("Service not found on device, writing incompable");
		currentDevice.state = ExpandedRemoteDevice.OBEX_UNSUPPORTED;
		isBusy = false;
	}

	public static boolean sendToDevice(ExpandedRemoteDevice device, String fileName, String dataToSend) {

		isBusy = true;

		String serverURL = device.URL;

		try {
			Connection connection = null;
			OutputStream outputStream = null;
			Operation putOperation = null;
			ClientSession cs = null;

			boolean success = false;

			try {
				// Send a request to the server to open a connection
				connection = Connector.open(serverURL);
				cs = (ClientSession) connection;
				HeaderSet hsConnectReply = cs.connect(null);

				if (hsConnectReply.getResponseCode() != ResponseCodes.OBEX_HTTP_OK) {
					System.out.println("Failed to connect: " + hsConnectReply);
					isBusy = false;
					return false;
				}

				System.out.println("OPP session created");

				// Send a file with meta data to the server
				System.out.println("Data: " + dataToSend);
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
				success = true;
			} catch (Exception e) {
				System.out.println(e.getMessage());
			} finally {
				outputStream.close();
				putOperation.close();
				cs.disconnect(null);
				connection.close();
				System.out.println("Connection Closed");
			}
			isBusy = false;
			return success;
		} catch (final Exception e) {
			System.out.println(e.getMessage());
			isBusy = false;
			return false;
		}
	}

	public static boolean sendViaWiFi(String data) {

		isBusy = true;

		String[] header = { "data" };
		String[] param = { data };

		try {
			HTTPUtils.sendPost(BluetoothPanel.SCRIPT_URL, header, param);
			isBusy = false;
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			isBusy = false;
			return false;
		}
	}

	public static boolean isBusy() {
		return isBusy;
	}
}