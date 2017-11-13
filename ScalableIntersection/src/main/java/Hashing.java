package main.java;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

public class Hashing implements Runnable{

	private ConcurrentFileReader reader = null;
	public static Map<Long, ConcurrentFileWriter> writer  = new HashMap<Long, ConcurrentFileWriter>();
	private String name = null;
	
	private Hashing() {}
	
	public Hashing(ConcurrentFileReader reader) {
		this.reader = reader;
	}
	
	public void run() {
		this.name = Thread.currentThread().getName();
	
		try {
			Vector<Pair<Long, Character>> list = null;
			while (reader.ready()) {
				list = reader.read(150000);
				
				Iterator<Pair<Long, Character>> it = list.iterator();
				while(it.hasNext()) {
					Pair<Long, Character> pair = it.next();
					long last = pair.first % 600;
					long key = this.name.equals("File1") ? 1000 + last : 2000 + last;
					ConcurrentFileWriter cfw = null;
					
					if(writer.containsKey(key)) {
						cfw = writer.get(key);
					} else {
						cfw = new ConcurrentFileWriter(this.name, String.valueOf(last), true);
						writer.put(key, cfw);
					}
					cfw.write(pair);
				}				
			}

		} catch (Exception e) {
			System.out.println("Reading Failed");
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
