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
	}

	public static Logger getInst() {
		if (instance == null) {
			instance = new Logger();
		}
		return instance;
	}

	public void log(String msg) {
		if (writer == null) {
			return;
		}
		if (msg == null || msg.equals("")) {
			return;
		}

		try {
			writer.write(System.currentTimeMillis() + "," + Thread.currentThread().getName() + "," + msg);
			writer.newLine();
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
