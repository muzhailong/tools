package tools;

import java.io.File;
import java.util.List;

import tools.StuInfo.Student;

public class StartUp {

	public static void run1() {
		StuInfo info = new StuInfo();
		List<Student> ss = info.acquire();
		File f1=new File("1.txt");
		info.writeStuInfo(f1);
		File f = new File(info.prop.getProperty("saveDir"));
		if (!f.exists()) {
			f.mkdir();
		}
		DownloadFile df = new DownloadFile(ss, f);
		df.batchDownload();
	}

	public static void run2(File ff) {
		StuInfo info = new StuInfo();
		List<Student> ss = info.acquire(ff);
		File f = new File(info.prop.getProperty("saveDir"));
		if (!f.exists()) {
			f.mkdir();
		}
		DownloadFile df = new DownloadFile(ss, f);
		df.batchDownload();
	}

	public static void main(String[] args) {
		if (args.length == 0) {
			run1();
		}else {
			File f=new File(args[0]);
			run2(f);
		}
	}
}
