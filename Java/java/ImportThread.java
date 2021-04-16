package photoshop;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class ImportThread extends Thread {
	
	private Formatter f;
	private String s, ext; 
	
	public ImportThread(Formatter f, String s, String ext) {
		this.f = f; this.s = s; this.ext = ext;
	}
	@Override
	public void run() {
		synchronized (Image.getInsance()) {
			try {
				f.importImage(s);
			} catch (IOException | ParserConfigurationException | SAXException | FileFormatException e) {
				if (ext.equals("txt")) {
					Image.getInsance().clearInstance();
				}
				Image.getInsance().dm.setVisible(true);
			}
		}
	}
}
