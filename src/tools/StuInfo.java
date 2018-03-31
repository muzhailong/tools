package tools;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.DomNode;
import com.gargoylesoftware.htmlunit.html.HtmlPage;

public class StuInfo {

	private List<Student> lt = new LinkedList<Student>();
	public static Properties prop = new Properties();
	public static WebClient client = new WebClient();
	static {
		try {
			prop.load(new FileInputStream("config.properties"));
			client.getOptions().setCssEnabled(true);
			client.getOptions().setJavaScriptEnabled(true);
			client.getOptions().setTimeout(40000);
			client.getOptions().setThrowExceptionOnFailingStatusCode(false);
			client.setJavaScriptTimeout(50000);
			client.getOptions().setThrowExceptionOnScriptError(false);
			client.getOptions().setRedirectEnabled(true);
			client.setAjaxController(new NicelyResynchronizingAjaxController());
			client.getOptions().setRedirectEnabled(true);
			client.getCookieManager().setCookiesEnabled(true);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static class Student {
		String githubUrl;
		String id;
		String blogUrl;

		public Student(String githubUrl, String id, String blogUrl) {
			this.githubUrl = githubUrl;
			this.id = id;
			this.blogUrl = blogUrl;
		}
	}
	public List<Student> acquire() {
		lt.clear();
		Set<String> set = new HashSet<String>();
		try {
			HtmlPage page = client.getPage(prop.getProperty("blogUrl"));
			client.waitForBackgroundJavaScript(1000 * 6);
			List<DomNode> nodes = page.getByXPath("//div[@class='blog_comment_body']");
			Student st = null;
			for (DomNode dn : nodes) {
				List<DomNode> ch = dn.getChildNodes();
				if (set.contains(ch.get(2).asText().trim())) {
					continue;
				}
				if (ch.get(1).asText().trim().length() == 5) {
					st = new Student(ch.get(0).asText(), ch.get(1).asText(), ch.get(2).asText());
					lt.add(st);
					set.add(ch.get(2).asText().trim());
				}
			}
		} catch (FailingHttpStatusCodeException | IOException e) {
			e.printStackTrace();
		}
		changeGitHubUrl();
		return lt;
	}
	
	public List<Student> acquire(File f){
		lt.clear();
		BufferedReader reader=null;
		try {
			reader=new BufferedReader(new FileReader(f));
			String s=null;
			Student st=null;
			while((s=reader.readLine())!=null) {
				String[]ss=s.split(" ");
				st=new Student(ss[1],ss[0],null);
				lt.add(st);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		changeGitHubUrl();
		return lt;
	}

	private void changeGitHubUrl() {
		HtmlPage p=null;
		for (Student s : lt) {
			try {
				p=client.getPage(s.githubUrl+"?tab=repositories");
			} catch (FailingHttpStatusCodeException | IOException e) {
				e.printStackTrace();
			}
			client.waitForBackgroundJavaScript(6*1000);
			DomNode dn=p.getFirstByXPath("//a[@itemprop='name codeRepository']");
			s.githubUrl=s.githubUrl+"/"+dn.asText()+"/archive/master.zip";
		}
	}
	
	public void writeStuInfo(File f) {
		BufferedWriter writer=null;
		try {
			writer=new BufferedWriter(new FileWriter(f));
			for(Student s:lt) {
				writer.write(s.id+"   "+s.githubUrl+"\r\n");
			}
			writer.flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
