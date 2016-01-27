import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;


public class Reciever {
	public static void updateSpreadsheet(File downloadsFolder, File output) throws IOException {
		File[] files = downloadsFolder.listFiles();
		String compiledOutput = "";
		for (File file : files) {
			if (file.getName().contains("ScoutForm")) {
				BufferedReader reader = new BufferedReader(new FileReader(file));
				compiledOutput += reader.readLine()+ "\n";
				reader.close();
			}
		}
		BufferedWriter writer = new BufferedWriter(new FileWriter(output));
		writer.write(compiledOutput);
		writer.close();
	}
	public static void main(String[] args) {
		File downloadsFolder = new File("/Users/jeremymcculloch/Downloads");
		File output = new File("/Users/jeremymcculloch/scoutingData.csv");
		try {
			updateSpreadsheet(downloadsFolder, output);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}