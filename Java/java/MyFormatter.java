package photoshop;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.soap.Node;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class MyFormatter extends Formatter {
	
	@Override
	public void exportImage(String path, Layer lay) throws ParserConfigurationException, TransformerException, IOException {
		Image img = Image.getInsance();
		
		DocumentBuilderFactory df = DocumentBuilderFactory.newInstance(); 
        DocumentBuilder docb = df.newDocumentBuilder();
        Document d = docb.newDocument();
        
        Element root = d.createElement("image");
        d.appendChild(root);
        
        String dim = img.getW() + " " + img.getH();
        Element header = d.createElement("header");
        header.appendChild(d.createTextNode(dim));
        root.appendChild(header);
        
        Element layers = d.createElement("layers");
        root.appendChild(layers);
        
        Attr attr = d.createAttribute("num");
        dim = img.getLayers().size() + "";
        attr.setValue(dim);
        layers.setAttributeNode(attr);
        int ind = 0;
        Collections.reverse(Image.getInsance().getLayers());
        for (Layer l : Image.getInsance().getLayers()) {
        	Element layer = d.createElement("layer");
        	Element info = d.createElement("info");
        	Element pat = d.createElement("path");
        	
        	String p = l.getClass().hashCode() + "";
        	
        	//String pa = ".\\saved\\layer" + ind + "_" + p + ".bmp"; 
        	//String wrPath = "\\saved\\layer" + ind + "_" + p + ".bmp";
        	String pa = "saved\\layer" + ind + "_" + p + ".bmp"; 
        	String wrPath = "saved\\layer" + ind + "_" + p + ".bmp";
        	ind++;
			String inf = l.getImage().getWidth() + " " + l.getImage().getHeight()
				+ " " + (l.isActive() ? "true" : "false") + " " + (l.isVisible() ? "true" : "false") 
				+ " " + l.getTransparency();
			
			pat.appendChild(d.createTextNode(pa));
	        layer.appendChild(pat);
	        
	        info.appendChild(d.createTextNode(inf));
	        layer.appendChild(info);
	        
	        layers.appendChild(layer);
	        
	        Formatter f = new BMPFormatter();
	        
	        f.exportImage(wrPath, l);
	        
	        /*File outputfile = new File("image.bmp");
	        try {
				ImageIO.write(l.getImage(), "bmp", outputfile);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println("alo");
				e.printStackTrace();
			}*/
        }
        Collections.reverse(Image.getInsance().getLayers());
        /*SELECTIONS*/
        
        Element selections = d.createElement("selections");
        root.appendChild(selections);
        
        Attr attrSel = d.createAttribute("num");
        dim = img.getSelecions().size() + "";
        attrSel.setValue(dim);
        selections.setAttributeNode(attrSel);
        
        Image.getInsance().getSelecions().stream().forEach(s -> {
        	Element selection = d.createElement("selection");
        	Attr attrN = d.createAttribute("name");
        	Attr attrA = d.createAttribute("active");
        	Attr attrNum = d.createAttribute("num");
        	String nam = s.getName();
        	String act = s.isActive() ? "true" : "false";
        	String no = s.getRectangles().size() + "";
        	attrN.setValue(nam);
        	attrA.setValue(act);
        	attrNum.setValue(no);
        	selection.setAttributeNode(attrN);
        	selection.setAttributeNode(attrA);
        	selection.setAttributeNode(attrNum);
        	s.getRectangles().stream().forEach(r -> {
        		Element rect = d.createElement("rectangle");            	
            	String dd = r.getX() + " " + r.getY() + " " + r.getW() + " " + r.getH();
            	rect.appendChild(d.createTextNode(dd));
    	        selection.appendChild(rect);
        	});
	        selections.appendChild(selection);
        });
        
        /*SELECTIONS*/
        
        
        /*COMPOSITE OPERATIONS*/
        
        Element compOpers = d.createElement("compoperations");
        root.appendChild(selections);
        
        Attr attrNum = d.createAttribute("num");
        String no = img.getCompOper().size() + "";
        attrNum.setValue(no);
        compOpers.setAttributeNode(attrNum);
        root.appendChild(compOpers);
        
        for (Map.Entry<String, Operation> entry : img.getCompOper().entrySet()) {
            //System.out.println(entry.getKey() + "/" + entry.getValue());
        	CompositeOperation co = (CompositeOperation)entry.getValue();
        	this.exportCompOper(co, compOpers, d);
        }
        
        /*COMPOSITE OPERATIONS*/
        
        TransformerFactory tranf = TransformerFactory.newInstance();
        Transformer tr = tranf.newTransformer();
        DOMSource domSource = new DOMSource(d);
        File f = new File(path);
        StreamResult streamResult = new StreamResult(f);
        tr.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        tr.transform(domSource, streamResult);
	}
	
	public void exportCompOper(CompositeOperation co, Element compOp, Document document) {
		Element operat = document.createElement("compoperation");
		
		for (Operation o : co.getOperations()) {
			if (!(o instanceof CompositeOperation)) {
				Element op = document.createElement("operation");
				Attr attrName = document.createAttribute("type");
		        String n = "basic";
		        attrName.setValue(n);
		        op.setAttributeNode(attrName);
				
				String s = o.toString();
				
				op.appendChild(document.createTextNode(s));
    	        operat.appendChild(op);
			}
			else {
				CompositeOperation cop = (CompositeOperation)(o);
				exportCompOper(cop, operat, document);
			}
		}
		operat.setAttribute("name", co.getName());
		operat.setAttribute("num", co.getOperations().size() + "");
		operat.setAttribute("type", "comp");
		compOp.appendChild(operat);
	}
	
	@Override
	public void importImage(String path) throws IOException, ParserConfigurationException, SAXException, FileFormatException {
		Image img = Image.getInsance();
		File f = new File(path);
		DocumentBuilderFactory dbF = DocumentBuilderFactory.newInstance();
		DocumentBuilder dB = dbF.newDocumentBuilder();
		Document doc = dB.parse(f);
		if (doc == null) throw new IOException();
		doc.getDocumentElement().normalize();

		NodeList header = doc.getElementsByTagName("header");
		if (header == null) throw new IOException();
		
		int w = 0, h = 0;
		for (int i = 0; i < header.getLength(); i++) {
			if (header.item(i).getNodeType() == Node.ELEMENT_NODE) {
	            Element el = (Element) header.item(i);
	            String dim = el.getTextContent();
	            Pattern p = Pattern.compile("^([0-9]*) ([0-9]*)$");
	    		Matcher m = p.matcher(dim);
	    		if (m.matches()) {
	    			w = Integer.parseInt(m.group(1));
	    			h = Integer.parseInt(m.group(2));
	    		}
	    		else throw new IOException();
			}
		}
		
		
		/*LAYERS*/
		
		List<String> paths = new ArrayList<String>();
		
		NodeList lays = doc.getElementsByTagName("layer");
		if (lays == null) throw new IOException();
		
		for (int i = lays.getLength() - 1; i >= 0; i--) {
			if (lays.item(i).getNodeType() == Node.ELEMENT_NODE) {
	            Element el = (Element) lays.item(i);
	            if (el == null) throw new IOException();
	            
	            String info = el.getElementsByTagName("info").item(0).getTextContent();      
	            String pat = el.getElementsByTagName("path").item(0).getTextContent();
	            if (info == null || pat == null) throw new IOException();
	        
	            Pattern p = Pattern.compile("^([0-9]*) ([0-9]*) ([^ ]*) ([^ ]*) ([0-9]*)$");
	    		Matcher m = p.matcher(info);
	    		int wid, hei, transp;
	    		boolean act, vis;
	    		
	    		if (m.matches()) {
	    			wid = Integer.parseInt(m.group(1));
	    			hei = Integer.parseInt(m.group(2));
	    			if (wid != w || hei != h)
	    				throw new IOException();
	    			
	    			if (m.group(3).equals("true")) act = true;
	    			else if (m.group(3).equals("false")) act = false;
	    			else throw new IOException();
	    			
	    			if (m.group(4).equals("true")) vis = true;
	    			else if (m.group(4).equals("false")) vis = false;
	    			else throw new IOException();
	    			
	    			transp = Integer.parseInt(m.group(5));
	    			if (transp > 100 || transp < 0)				
		    			throw new IOException();
	    		}
	    		else throw new IOException();
	    		
	    		Image.getInsance().addLayer(pat);
	    		Layer newLay = Image.getInsance().getLayers().
	    				get(Image.getInsance().getLayers().size() - 1);
	    		if (act) newLay.setActivity();
	    		else newLay.resetActivity();
	    		if (vis) newLay.setVisibility();
	    		else newLay.resetActivity();
	    		newLay.setTransparency(transp);
	    		newLay.fillAlpha();
	    		newLay.revalidate();
			}
			
			/*LAYERS*/
		}
			
			
			/*SELECTIONS*/
			
		NodeList selects = doc.getElementsByTagName("selection");
		if (selects == null) throw new IOException();
		
		for (int i1 = 0; i1 < selects.getLength(); i1++) {
			if (selects.item(i1).getNodeType() == Node.ELEMENT_NODE) {
	            Element el = (Element) selects.item(i1);
	            if (el == null) throw new IOException();
	            
	            String nam = el.getAttribute("name");
	            boolean ac;
	            if (el.getAttribute("active").equals("true")) ac = true;
	            else if (el.getAttribute("active").equals("false")) ac = false;
	            else throw new IOException();
	            
	            NodeList rects = el.getChildNodes();
	            if (rects == null) throw new IOException();
	            
	            List<Rectangle> recs = new ArrayList<>();
	            for (int j = 0; j < rects.getLength(); j++) {
	            	if (rects.item(j).getNodeType() == Node.ELEMENT_NODE) {
			            Element elem = (Element) rects.item(j);
			            if (elem == null) throw new IOException();
			            
			            String dims = elem.getTextContent();
			            
			            Pattern p = Pattern.compile("^([0-9]*) ([0-9]*) ([0-9]*) ([0-9]*)$");
			    		Matcher m = p.matcher(dims);
			    		int x, y, wid, hei;
			    		
			    		if (m.matches()) {
			    			x = Integer.parseInt(m.group(1));
			    			y = Integer.parseInt(m.group(2)); 
			    			wid = Integer.parseInt(m.group(3));
			    			hei = Integer.parseInt(m.group(4));
			    			if (x < 0 || y < 0 || wid < 0 || hei < 0)
			    				throw new IOException();
			    			x = x * Image.getInsance().getDW() / Image.getInsance().getW();
			    			wid = wid * Image.getInsance().getDW() / Image.getInsance().getW();
			    			y = y * Image.getInsance().getDH() / Image.getInsance().getH();
			    			hei = hei * Image.getInsance().getDH() / Image.getInsance().getH();
			    			Rectangle r = new Rectangle(x, y, wid, hei);				    			
			    			recs.add(r);
			    		}
			    		else throw new IOException();
	            	}
	            	else throw new IOException();
	            }
	            
	            Selection s = new Selection(recs, nam);
	            if (ac) s.setActivity();
	            else s.resetActivity();
	            
	            Image.getInsance().addSelection(s);
			}
			else throw new IOException();
		}
			
			/*SELECTIONS*/
			
			
			
			/*COMP OPERS*/
			
		NodeList compOps = doc.getElementsByTagName("compoperations");
		if (compOps == null) throw new IOException();
		
		if (compOps.item(0).getNodeType() == Node.ELEMENT_NODE) {
			Element el = (Element) compOps.item(0);
			if (el == null) throw new IOException();
			
			int n = Integer.parseInt(el.getAttribute("num"));
			
			NodeList comps = el.getChildNodes();
			if (comps == null) throw new IOException();
			
			for (int j = 0; j < comps.getLength(); j++) {
				if (comps.item(j).getNodeType() == Node.ELEMENT_NODE) {
					Element coel = (Element)comps.item(j);
					if (coel == null) throw new IOException();
					
					Operation o = importCompOper(coel);
					Image.getInsance().addCompOper(o);
				}
				else throw new IOException();
			}
		}
		else throw new IOException();
			
			/*COMP OPERS*/
	}
	
	public Operation importCompOper(Element compop) throws IOException {
		String type, name;
		
		type = compop.getAttribute("type");
		name = compop.getAttribute("name");
		
		if (!type.equals("comp")) throw new IOException();
		
		List<Operation> opers = new ArrayList<>();
		NodeList childs = compop.getChildNodes();
		
		for (int i = 0; i < childs.getLength(); i++) {
			if (childs.item(i).getNodeType() == Node.ELEMENT_NODE) {
				Element op = (Element)childs.item(i);
				if (op == null) throw new IOException();
				
				String typ = op.getAttribute("type");
				
				if (typ.equals("basic")) {
					String txt = op.getTextContent();
					Pattern p = Pattern.compile("^([^ ]*) ([0-9]*)$");
		    		Matcher m = p.matcher(txt);	
		    		if (m.matches()) {
		    			String opnam = m.group(1);
		    			int par = Integer.parseInt(m.group(2));
		    			
		    			Operation newo = null;
		    			
		    			if (opnam.equals("add")) newo = new Add(par);
						else if (opnam.equals("sub")) newo = new Sub(par);
						else if (opnam.equals("greyscale")) newo = new Greyscale();
						else if (opnam.equals("median")) newo = new Median();
						else if (opnam.equals("log")) newo = new Log();
						else if (opnam.equals("inversion")) newo = new Inversion();
						else if (opnam.equals("blackandwhite")) newo = new BlackAndWhite();
						else if (opnam.equals("mul")) newo = new Mul(par);
						else if (opnam.equals("div")) newo = new Div(par);
						else if (opnam.equals("inversesub")) newo = new InverseSub(par);
						else if (opnam.equals("inversediv")) newo = new InverseDiv(par);
						else if (opnam.equals("power")) newo = new Power(par);
						else if (opnam.equals("min")) newo = new Min(par);
						else if (opnam.equals("max")) newo = new Max(par);
						else if (opnam.equals("abs")) newo = new Abs();
		    			if (newo == null) throw new IOException();
		    			opers.add(newo);
		    		}
		    		else throw new IOException();
				}
				else {
					if (typ.equals("comp")) {
						opers.add(importCompOper(op));
					}
					else {				
						throw new IOException();
					}
				}
			}
			else throw new IOException();
		}
		
		return new CompositeOperation(opers, name);
	}
}