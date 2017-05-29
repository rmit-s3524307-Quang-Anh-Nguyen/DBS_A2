package Build;
import java.io.*;
import java.nio.charset.*;
import java.nio.file.*;

public class Build {
	public static final String FILE_NAME = "ped.csv";
	private static int max = 0;
	final static int HOURLY_COUNTS_COLUMN = 9;
	public static void main(String[] args) throws Exception {
		//get the path
		Path pathToFile = Paths.get(FILE_NAME);

		//setting page size
		if (args[0].equals("0")){
			Page.setPageSize(4096);
		} else {
			Page.setPageSize(8192);
		}

		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			br.readLine();
			String line = br.readLine(); 
			while (line != null) {
				//convert to byte
				byte[] size = line.getBytes();
				
				//find maximum size of record
				if (max < size.length)
					max = size.length;
				
				//calculate maximum record per page
				Page.setMaxRecords(max);
				line = br.readLine();
			} 
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		}
		
		try (BufferedReader br = Files.newBufferedReader(pathToFile, StandardCharsets.US_ASCII)) {
			br.readLine();
			String line = br.readLine(); 
			while (line != null) {
				//split the tuple
				String[] attributes = line.split(",");
				//get hourly counts
				String key = attributes[HOURLY_COUNTS_COLUMN];
				
				//process the data
				//convert to byte
				byte[] record = line.getBytes();
				//insert into heap page and add to Btree
				Page.insertRecord(Integer.parseInt(key), record);
				
				line = br.readLine();
			} 
		} catch (IOException ioe) { 
			ioe.printStackTrace(); 
		}
		
		//write the last page to file
		Page.finishPage();
	}
}
