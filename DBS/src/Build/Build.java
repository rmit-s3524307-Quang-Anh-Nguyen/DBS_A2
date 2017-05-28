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

		
//		for (int i=0; i<Btree.nodeNumber;i++){
//			childRead(Btree.Root);
//		}
//		int k = 0;
//		while (k<Btree.Root.getNodeNumber()){
//			System.out.print(Btree.Root.getNodes()[k].key+" ");
//			k++;
//		}
		
	}
	
	public static void childRead(Nodes parentNodes){
		if (parentNodes.getChildNode()[0]!=null){
			for (int j=0; j<parentNodes.getChildNode().length; j++){
				if (parentNodes.getChildNode()[j] == null){
					System.out.println();
					return;
				}
				
				Nodes currentNodes = parentNodes.getChildNode()[j];
//				childRead(currentNodes);
				
				
				int k = 0;
				while (k<currentNodes.getNodeNumber()){
					System.out.print(currentNodes.getNodes()[k].key+" ");
					k++;
				}
				System.out.print("|");
			}
			System.out.println();
		}
	}
}
