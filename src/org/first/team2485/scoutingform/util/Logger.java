package org.first.team2485.scoutingform.util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedList;

/**
 * @author Nicholas Contreras
 */

public class Logger {

	private static Logger instance;

	private final BufferedWriter writer;

	private final LinkedList<WriteRequest> writeRequests;
	
	private boolean stop;

	private Logger() {

		File loc = new File(System.getProperty("user.home") + "/ScoutingRecords");

		if (!loc.exists()) {
			loc.mkdirs();
		}

		File logFile = new File(loc, "log.csv");

		FileWriter fileWriter = null;

		try {
			fileWriter = new FileWriter(logFile, true);
		} catch (IOException e) {
			e.printStackTrace();
		}

		if (fileWriter != null) {
			writer = new BufferedWriter(fileWriter);
		} else {
			writer = null;
		}

		writeRequests = new LinkedList<WriteRequest>();

		synchronized (writeRequests) {
			writeRequests.add(new WriteRequest("starting logger"));
		}
		
		stop = false;
		
		Thread logThread = new Thread(() -> runLogger(), "Logging-Thread");
		logThread.setDaemon(true);
		logThread.start();
	}

	public void log(String msg) {
		if (writer == null) {
			return;
		}
		if (msg == null || msg.equals("")) {
			return;
		}

		synchronized (writeRequests) {
			writeRequests.add(new WriteRequest(msg));
		}
	}

	private void runLogger() {

		while (!stop) {
			
			synchronized (writeRequests) {
				if (!writeRequests.isEmpty()) {
					
					WriteRequest cur = writeRequests.removeFirst();
					
					try {
						writer.write(cur.getWriteableData());
						writer.newLine();
						writer.flush();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			Thread.yield();
		}
	}

	public static Logger getInst() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}
	
	public void shutdownLogger() {
		stop = true;
		
		writeRequests.add(new WriteRequest("shutting down logger"));
		
		while (!writeRequests.isEmpty()) {
			try {
				writer.write(writeRequests.removeFirst().getWriteableData());
				writer.newLine();
				writer.flush();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		try {
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private class WriteRequest {

		private final long msgTime;
		private final String msg;

		private WriteRequest(String msg) {
			msgTime = System.currentTimeMillis();
			this.msg = msg.replace(",", ";");
		}

		private String getWriteableData() {
			return msgTime + "," + System.currentTimeMillis() + "," + msg;
		}
	}
}
