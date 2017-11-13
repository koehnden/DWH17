package main.java;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

public class ConcurrentFileWriter {
	private BufferedWriter bw = null;
	private File file = null;
	private String threadname = null;
	private int size = 0;
	
	private ConcurrentFileWriter() {
		
	}
	
	public ConcurrentFileWriter(String threadname, String filename, boolean deleteOnExit) throws IOException {
		this.threadname = threadname;
		Path filePath = Main.TEMDIR.resolve(threadname).resolve(filename);
		
		this.file = filePath.toFile();
		
		if(file.getParentFile().exists() == false) {
			file.getParentFile().mkdirs();
		}
		if(file.exists() == false) {
			file.createNewFile();
		}
		if(deleteOnExit) this.file.deleteOnExit();
		
		this.bw = new BufferedWriter(new FileWriter(this.file));
	}
	
	public synchronized void write(Pair<Long, Character> pair) throws NumberFormatException, IOException {
		if(bw == null) return;
//		System.out.println(String.valueOf(pair.second) + pair.first);
		bw.append(String.valueOf(pair.second) + pair.first + System.lineSeparator());
		
		this.size += 1;
		
		if(this.size > 0 && this.size % 1000 == 0) bw.flush();
	}
	
	public void write(Long key, List<Character> values) throws NumberFormatException, IOException {
		if(bw == null) return;
		
		for(char c : values)
			bw.append(String.valueOf(c) + key + System.lineSeparator());

		this.size += 1;
		
		if(this.size > 0 && this.size % 1000 == 0) bw.flush();
	}
	
	public synchronized void close() throws IOException {
		if(bw != null) {
			bw.flush();
			bw.close();
			this.bw = null;
		}
		
	}
	
	public File getFile(){
		return this.file;
	}
	
	public String getThreadname() {
		return this.threadname;
	}
	

}
