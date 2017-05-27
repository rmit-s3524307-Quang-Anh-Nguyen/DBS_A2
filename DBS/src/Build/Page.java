package Build;

import java.io.*;

public final class Page {
	private static final int COUNT_SIZE = 3;
	private static int size;
	private static int maxRecordSize;
	private static int maxRecords;
	private static int pageID = 0;
	private static int recordNumber = 0;
	private static ByteArrayOutputStream page = new ByteArrayOutputStream();
	
	private Page(){}
	
	public static void setPageSize(int size){
		Page.size = size;
	}

	public static void setMaxRecords(int maxRecordSize){
		Page.maxRecordSize = maxRecordSize;
		Page.maxRecords = (int) Math.floor((Page.size-COUNT_SIZE)/maxRecordSize);
	}
	
	//insert the record to write
	public static void insertRecord(int key, byte[] record) throws IOException{		
		//insert record
		byte[] page = new byte[Page.size];
		System.arraycopy(Page.page.toByteArray(), 0, page, 0, Page.page.toByteArray().length);
		System.arraycopy(record, 0, page, recordNumber*maxRecordSize, record.length);
		Page.page.reset();
		Page.page.write(page);
		
		int rid[] = {pageID, recordNumber};
		Node node = new Node(key, rid);

		//add node to Btree
		Btree.addNode(node);
		
		recordNumber++;
		
		//when page is full, write to file and reset page
		if (recordNumber == maxRecords){
			writePage();
			
			pageID++;

			//reset the page
			Page.page.reset();
			recordNumber = 0;
		}
	}
	
	//write last page to file if there is any unwritten record left
	public static void finishPage() throws IOException{
		if (recordNumber != 0){
			writePage();
		}
	}
	
	//write to heap file
	private static void writePage() throws IOException{
		byte[] page = Page.page.toByteArray();
		
		//write to file on disk
		writeToRandomAccessFile("heap.txt", Page.size*Page.pageID, page);
		
	}
	

	
	public static void writeToRandomAccessFile(String file, int position, byte[] page) { 
		try { 
			RandomAccessFile fileStore = new RandomAccessFile(file, "rw");
			
			//reset file on disk
			if (pageID == 0){
				fileStore.setLength(0); 
			}
			
			// moves file pointer to position specified
			fileStore.seek(position);

			//write number of record of the page
			fileStore.write(String.valueOf(recordNumber).getBytes());
			
			// moves file pointer to data starting point
			fileStore.seek(position + COUNT_SIZE); 
			
			//write records 
			fileStore.write(page); 
			fileStore.close(); 
		} 
		catch (IOException e) { 
			e.printStackTrace(); 
		} 
		
	}
}
