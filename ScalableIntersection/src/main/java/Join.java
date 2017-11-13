package main.java;

import java.io.FileNotFoundException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Join implements Runnable {
	
	private List<String> filenames = new LinkedList<String>();
	private ConcurrentFileWriter cfw = null;
	
	public Join(ConcurrentFileWriter cfw) {
		this.cfw = cfw;
	}
	
	public void run() {
		for(String filename : filenames) {
				try {
					ConcurrentFileReader cfr1 = new ConcurrentFileReader(Main.TEMDIR.resolve("File1").resolve(filename).toString());
					ConcurrentFileReader cfr2 = new ConcurrentFileReader(Main.TEMDIR.resolve("File2").resolve(filename).toString());
					
					Map<Long, List<Character>> list1 = cfr1.read();
					Map<Long, List<Character>> list2 = cfr2.read();
					
					Iterator<Long> it = list1.keySet().iterator();
					System.out.println(list1.size());
					while(it.hasNext()) {
						Long key = it.next();
						if(list2.containsKey(key)) {
							List<Character> values1 = list1.get(key);
							List<Character> values2 = list2.get(key);
							for(Character c : values1){
								if(values2.contains(c)) continue;
								
								values2.add(c);
							}
							this.cfw.write(key, values2);
						}
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
	}
	
	public void addFilename(String filename) throws FileNotFoundException {
		this.filenames.add(filename);
	}
	
	
	
} 

