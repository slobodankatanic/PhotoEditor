package photoshop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.TransformerException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class Operation extends Panel implements ActionListener {
	
	protected int num;
	protected String name;
	protected Button apply = new Button(), compAp = new Button();
	protected TextField field = new TextField(2);
	protected Label nameLabel = new Label();
	
	private void addListeners() {
		/*apply.addActionListener(ae-> {
			if ((Image.getInsance().getLayers().size() == 0) || 
					Image.getInsance().getLayers().stream().allMatch(l -> { return !(l.isActive()); } ))  
				return; 
			Image.getInsance().disable();
			Map<String, Operation> oldCO = Image.getInsance().getCompOper();
			Map<String, Operation> newCO = new HashMap<>();
			List<Operation> opers = new ArrayList<>();
			opers.add(this);
			CompositeOperation co = new CompositeOperation(opers, this.name);
			newCO.put(co.getName(), co);
			Image.getInsance().setCompOper(newCO);
			Formatter f = new MyFormatter();
			new File("saved").mkdirs();
			try {
				f.exportImage("saved\\save.txt", null);
				Image.getInsance().setCompOper(oldCO);
			} catch (ParserConfigurationException | TransformerException | IOException e) {
				Image.getInsance().setCompOper(oldCO);
				System.out.println("Greska");
				return;
			}
			new ProcessImage().start();
		});*/
		
		apply.addActionListener(this);	
		compAp.addActionListener(this);
		
		field.addTextListener(te->{
			if (field.getText().equals("")) {
				apply.setEnabled(false);
			}
			else {
				try {
					int p = Integer.parseInt(field.getText());
					if (p >= 0) {
						apply.setEnabled(true);
						num = p;
					}
					else apply.setEnabled(false);
				} catch(NumberFormatException e) {
					apply.setEnabled(false);
				}
			}
		});
	}
	
	private class ProcessImage extends Thread {
		private Formatter f;
		public ProcessImage(Formatter f) {
			this.f = f;
		}
		public ProcessImage() {}
		@Override
		public void run() {
			/*    ADDED      */
			
			Image.getInsance().disable();
			Map<String, Operation> oldCO = Image.getInsance().getCompOper();
			Map<String, Operation> newCO = new HashMap<>();
			List<Operation> opers = new ArrayList<>();
			opers.add(Operation.this);
			CompositeOperation co = new CompositeOperation(opers, Operation.this.name);
			newCO.put(co.getName(), co);
			Image.getInsance().setCompOper(newCO);
			Formatter f = new MyFormatter();
			new File("saved").mkdirs();
			try {
				f.exportImage("saved\\save.txt", null);
				Image.getInsance().setCompOper(oldCO);
			} catch (ParserConfigurationException | TransformerException | IOException e) {
				Image.getInsance().setCompOper(oldCO);
				Image.getInsance().enable();
				System.out.println("Greska");
				return;
			}
			Image.getInsance().enableSelections();
			
			/*    ADDED      */
			String file = "PhotoEditor.exe " + 
			"saved\\save.txt " + 
			"saved\\result.txt";
			Runtime runtime = Runtime.getRuntime();
			try {
				//System.out.println("Start");
				Process process = runtime.exec(file);
				//System.out.println("Wait");
				process.waitFor();
				//System.out.println("Term");
				
				//LOAD LAYERS
				DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
				DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
				Document doc = dBuilder.parse(new File("saved\\result.txt"));
				if (doc == null) throw new IOException();
				doc.getDocumentElement().normalize();
				NodeList lays = doc.getElementsByTagName("layer");
				if (lays == null) throw new IOException();
				Collections.reverse(Image.getInsance().getLayers());
				for (int i = lays.getLength() - 1; i >= 0 ; i--) {
					if (lays.item(i).getNodeType() == Node.ELEMENT_NODE) {
			            Element el = (Element) lays.item(i);
			            if (el == null) throw new IOException();
			                  
			            String pat = el.getElementsByTagName("path").item(0).getTextContent();
			    		Formatter ff = new BMPFormatter();
			    		ff.importImage(pat);
			    		BufferedImage bi = ff.getImage();
			    		Image.getInsance().getLayers().get(i).setImage(bi);
					}
				}
				Image.getInsance().repaint();
				Collections.reverse(Image.getInsance().getLayers());
				//LOAD LAYERS
			} catch(Exception e) {
				Image.getInsance().dm.setVisible(true);
			}
			Image.getInsance().enable();
		}
	}
	
	public Operation(String name) {
		//setLayout(new GridLayout(1, 2));
		setLayout(new BorderLayout());
		this.name = name;
		apply.setLabel(name);
		//this.add(apply, BorderLayout.WEST);
		apply.setBackground(new Color(80, 80, 80));
		apply.setForeground(Color.WHITE);
		apply.setFont(new Font("Arial", Font.BOLD, 15));
		apply.setPreferredSize(new Dimension(75, this.getHeight()));
		field.setFont(new Font("Arial", Font.BOLD, 20));
		field.setBackground(new Color(45, 45, 45));
		field.setForeground(Color.WHITE);
		field.setPreferredSize(new Dimension(80, this.getHeight()));
		field.setEnabled(false);
		apply.setEnabled(false);
		setBackground(new Color(40, 40, 40));
		addListeners();
	}
	
	public void setParameter(int p) {}
	
	public String getName() { return name; }
	
	public int getParameter() { return 0; }
	
	public void disable() {
		apply.setEnabled(false);
		compAp.setEnabled(false);
		field.setEnabled(false);
	}
	
	public void enable() {
		compAp.setEnabled(true);
		field.setEnabled(true);
		try {
			int p = Integer.parseInt(field.getText());
			if (p >= 0) apply.setEnabled(true);
			else apply.setEnabled(false);
		}
		catch (NumberFormatException e) {
			apply.setEnabled(false);
		}
	}

	@Override
	public void actionPerformed(ActionEvent ae) {		
		if ((Image.getInsance().getLayers().size() == 0) || 
				Image.getInsance().getLayers().stream().allMatch(l -> { return !(l.isActive()); } ))  
			return; 
		/*Image.getInsance().disable();
		Map<String, Operation> oldCO = Image.getInsance().getCompOper();
		Map<String, Operation> newCO = new HashMap<>();
		List<Operation> opers = new ArrayList<>();
		opers.add(this);
		CompositeOperation co = new CompositeOperation(opers, this.name);
		newCO.put(co.getName(), co);
		Image.getInsance().setCompOper(newCO);
		Formatter f = new MyFormatter();
		new File("saved").mkdirs();
		try {
			f.exportImage("saved\\save.txt", null);
			Image.getInsance().setCompOper(oldCO);
		} catch (ParserConfigurationException | TransformerException | IOException e) {
			Image.getInsance().setCompOper(oldCO);
			Image.getInsance().enable();
			System.out.println("Greska");
			return;
		}
		Image.getInsance().enableSelections();*/
		new ProcessImage().start();
	}
}
