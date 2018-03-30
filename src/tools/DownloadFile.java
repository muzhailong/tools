package tools;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

import tools.StuInfo.Student;

public class DownloadFile {
	private List<Student> ss;
	private File dir;

	public DownloadFile(List<Student> ss, File dir) {
		this.ss = ss;
		this.dir = dir;
	}

	public void download(Student s) {
		URL url = null;
		try {
			url = new URL(s.githubUrl);
			File f = new File(dir, s.id + ".zip");
			copyURLToFile(url, f);
			LogRecord.compleate(s.id);
		} catch (MalformedURLException e) {
			e.printStackTrace();
			LogRecord.uncomplete(s.id);
		} catch (IOException e) {
			e.printStackTrace();
			LogRecord.uncomplete(s.id);
		}
	}

	public void batchDownload() {
		for (Student s : ss) {
			download(s);
			System.out.println("ok   " + s.id + "     " + s.githubUrl);
		}
	}

	private void copyURLToFile(URL url, File f) {
		if (!f.exists()) {
			try {
				f.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		InputStream in = null;
		BufferedOutputStream out = null;
		try {
			byte[] temp = new byte[1024 * 4];
			out = new BufferedOutputStream(new FileOutputStream(f));
			in = url.openConnection().getInputStream();
			while (in.read(temp) != -1) {
				out.write(temp);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
