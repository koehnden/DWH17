package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class ConcurrentFileReader {
	
	private BufferedReader br = null;
	private File file = null;
	
	private ConcurrentFileReader() {
		
	}
	
	public ConcurrentFileReader(String filepath) throws FileNotFoundException {
		this.file = new File(filepath);
		System.out.println(file.getAbsolutePath());
		if(file.exists() == false) throw new FileNotFoundException();
		
		br = new BufferedReader(new FileReader(this.file));
	}
	
	public synchronized Vector<Pair<Long, Character>> read(int lines) throws NumberFormatException, IOException {
		if(br == null) return null;
//		System.out.println(Thread.currentThread());
		
		Vector<Pair<Long, Character>> list = new Vector<Pair<Long, Character>>();
		
		for(int i = 0; i < lines; i++) {
			if(br.ready()){
				String line = br.readLine();
				Long key = Long.parseLong(line.substring(1));
								
				list.addElement(new Pair<Long, Character>(key, line.substring(0, 1).charAt(0)));
				
			} else {
				close();
				break;
			}
		}
		
		return list;
	}
	
	public Map<Long, List<Character>> read() throws NumberFormatException, IOException {
		Map<Long, List<Character>> map = new HashMap<Long, List<Character>>();
		
		while(br.ready()){
			String line = br.readLine();
			Long key = Long.parseLong(line.substring(1));
			char c = line.substring(0,1).charAt(0);
			
			List<Character> values = null;
			if(map.containsKey(key)) {
				values = map.get(key);
				if(values.contains(c) == false) values.add(c);
			} else {
				values = new LinkedList<Character>();
				values.add(c);
			}
			map.put(key, values);
			
		}
		close();
			
		return map;
	}
	
	public synchronized boolean ready() throws IOException {
		if(br == null) return false;
			
		return br.ready();
	}
	
	public synchronized void close() throws IOException {
		if(br != null) {
			br.close();
			this.br = null;
		}
		
	}
	
	
}
