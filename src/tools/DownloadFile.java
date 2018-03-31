package tools;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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
			int cacheSize = Integer.parseInt(StuInfo.prop.getProperty("cacheSize"));
			byte[] temp = new byte[1024 * cacheSize];
			out = new BufferedOutputStream(new FileOutputStream(f));
			URLConnection conn = url.openConnection();

			in = conn.getInputStream();
			int all = conn.getContentLength();
			long sm = 0;
			int len = 0;
			while ((len = in.read(temp)) != -1) {
				out.write(temp, 0, len);
				sm += len;
				LogRecord.printProcess(f.getName(), (double) sm / all);
			}
			LogRecord.reset();
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
