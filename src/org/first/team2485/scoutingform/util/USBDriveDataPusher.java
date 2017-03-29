package org.first.team2485.scoutingform.util;

import java.io.File;
import java.io.IOException;
import java.nio.file.CopyOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import javax.swing.JOptionPane;

import org.first.team2485.scoutingform.ScoutingForm;

public class USBDriveDataPusher {

	private static File[] oldListRoot = File.listRoots();

	public static void start() {
		Thread t = new Thread(new Runnable() {
			public void run() {
				while (true) {
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					if (File.listRoots().length > oldListRoot.length) {
						Logger.getInst().log("new drive detected");
						oldListRoot = File.listRoots();
						Logger.getInst().log("drive " + oldListRoot[oldListRoot.length - 1] + " detected");

						int input = JOptionPane.showConfirmDialog(null, "Do you want to push data to this drive?",
								"USB Drive", JOptionPane.YES_NO_OPTION);

						if (input == JOptionPane.YES_OPTION) {

							File copyFrom = new File(System.getProperty("user.home"), "ScoutingRecords");
							File copyTo = new File(oldListRoot[oldListRoot.length - 1],
									"ScoutingRecords-" + ScoutingForm.scoutingForm.getScoutName());

							try {
								Files.copy(copyFrom.toPath(), copyTo.toPath(), StandardCopyOption.REPLACE_EXISTING);
							} catch (IOException e) {
								Logger.getInst().log("USB drive folder already exists");
							}

							File[] contents = copyFrom.listFiles();

							for (File f : contents) {
								try {
									Files.copy(f.toPath(), new File(copyTo, f.getName()).toPath(),
											StandardCopyOption.REPLACE_EXISTING);
								} catch (IOException e) {
									Logger.getInst().log("Failed to copy: " + f);
								}
							}
						}

					} else if (File.listRoots().length < oldListRoot.length) {
						Logger.getInst().log(oldListRoot[oldListRoot.length - 1] + " drive removed");
						oldListRoot = File.listRoots();
					}
				}
			}
		});
		t.setName("USB-Data-Pusher-Thread");
		t.setDaemon(true);
		t.start();
	}
}