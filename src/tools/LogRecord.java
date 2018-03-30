package tools;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;

public class LogRecord {
	static BufferedWriter writer1;
	static BufferedWriter writer2;
	static {
		
		try {
			writer1=new BufferedWriter(new FileWriter("download.txt",true));
			writer2=new  BufferedWriter(new FileWriter("err.txt",true));
			writer1.write("\r\n"+new Date().toString()+"\r\n");
			writer2.write("\r\n"+new Date().toString()+"\r\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void compleate(String uid) {
		try {
			writer1.write(uid+"\r\n");
			writer1.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void uncomplete(String uid) {
		try {
			writer2.write(uid+"\r\n");
			writer2.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
