package main.java;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Main {
	
	public static final Path TEMDIR = Paths.get(System.getProperty("java.io.tmpdir")).resolve("hashing"); 

	public static void main(String[] args) {
		Calendar start = Calendar.getInstance();
		Runtime rt = Runtime.getRuntime();
	
		int availableProcessors = (int)((float) rt.availableProcessors() / 2f);
		if(availableProcessors < 1) availableProcessors = 1;

		System.out.println("Starte Hashing!");
		
		Thread[] worker = new Thread[availableProcessors*2];
		ConcurrentFileReader reader = null;
		try {
			reader = new ConcurrentFileReader("C:\\Users\\Malte\\DWH\\Serie01\\resources\\datafiles\\file1.txt");
		} catch (FileNotFoundException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
		
		for(int i = 0; i < availableProcessors; i++) {
			worker[i] = new Thread(new Hashing(reader));
			worker[i].setName("File1");
			worker[i].start();
		}
		try {
			reader = new ConcurrentFileReader("C:\\Users\\Malte\\DWH\\Serie01\\resources\\datafiles\\file2.txt");
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		for(int i = 0; i < availableProcessors; i++) {
			worker[availableProcessors+i] = new Thread(new Hashing(reader));
			worker[availableProcessors+i].setName("File2");
			worker[availableProcessors+i].start();
		}
		
		for(int i = 0; i < worker.length; i++) {
			try {
				worker[i].join();
			} catch (InterruptedException e) {
				continue;
			}
		}
		
		Iterator<ConcurrentFileWriter> it = Hashing.writer.values().iterator();
		while(it.hasNext()) {
			try {
				it.next().close();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}
		
		rt.gc();
		
		
		System.out.println("Starte Join");
		List<File> files1 = Arrays.asList(Main.TEMDIR.resolve("File1").toFile().listFiles());
		List<File> files2 = Arrays.asList(Main.TEMDIR.resolve("File2").toFile().listFiles());
		availableProcessors = 1;
		Join[] joins = new Join[availableProcessors];
		
		ConcurrentFileWriter cfw = null;
		try {
			cfw = new ConcurrentFileWriter("result", "result", false);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		for(int i = 0; i < joins.length; i++) {
			joins[i] = new Join(cfw);
		}
		
		int cnt = 0;
		for(File file : files1) {
			String filename = file.getName();
			for(File file2 : files2) {
				String filename2 = file2.getName();
				if(filename2.equals(filename)) {
					System.out.println("########" + filename + "########" + filename2 + "########");
					System.out.println("File1: " + file.exists() + " " + file.length());
					System.out.println("File2: " + file2.exists() + " " + file2.length());
					try {
						joins[cnt % joins.length].addFilename(filename);
						cnt++;
						break;
					} catch (FileNotFoundException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
		Thread[] joinWorker = new Thread[joins.length];
		for(int i = 0; i < joins.length; i++) {
			joinWorker[i] = new Thread(joins[i]);
			joinWorker[i].start();
		}
		
		for(int i = 0; i < joinWorker.length; i++) {
			try {
				joinWorker[i].join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		try {
			cfw.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Calendar ende = Calendar.getInstance();
		System.out.println("Start: " + start.getTimeInMillis());
		System.out.println("Ende: " + ende.getTimeInMillis());
		System.out.println("Diff: " + (ende.getTimeInMillis() - start.getTimeInMillis()) / 1000f);
		
	}
	
}
