package photoshop;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;

import org.xml.sax.SAXException;

public abstract class Formatter {	
	
	protected BufferedImage image;
	
	public abstract void exportImage(String path, Layer layer) throws ParserConfigurationException, 
														 TransformerConfigurationException, 
														 TransformerException, IOException;	
	public abstract void importImage(String path) throws IOException, ParserConfigurationException, SAXException, FileFormatException;
	
	public BufferedImage getImage() { return image; }
}
