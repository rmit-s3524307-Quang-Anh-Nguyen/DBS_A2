package Build;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

public class Build {

	public static void main(String[] args) throws Exception {
		//get the path
        Path pathToFile = Paths.get("ped.csv");
        //
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			String line = br.readLine(); 
			while (line != null) {
				//split the tuple
				String[] attributes = line.split(","); 
				//process the data
				System.out.println(attributes[1]);
				line = br.readLine(); 
			} 
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		}
	}
}
