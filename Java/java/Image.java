package photoshop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Canvas;
import java.awt.Checkbox;
import java.awt.CheckboxGroup;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Menu;
import java.awt.MenuBar;
import java.awt.MenuItem;
import java.awt.Panel;
import java.awt.ScrollPane;
import java.awt.Scrollbar;
import java.awt.TextField;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
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
import java.util.stream.Stream;

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

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Image extends Frame {
	
	private Menu file, lay, sel, comOp;
	private OpenDialog openDialogProj = new OpenDialog(this, "Unesite putanju do postojeceg projekta", "txt");
	private OpenDialog openFun = new OpenDialog(this, "Unesite putanju do kompozitne funkcije", "fun");
	private LayerDialog layerDialog = new LayerDialog(this);
	private ImageCanvas imageCanvas = new ImageCanvas();
	private Panel centerPan = new Panel(), canvasPanel = new CanvasPanel(), rightPanel = new Panel();
	private Panel leftPanel = new Panel(), basOp = new Panel(), compOp = new Panel();
	private Panel panSel = new Panel(), panLay = new Panel();
	private SelPanel newSel;
	private List<Layer> layers = new ArrayList<Layer>();
	private List<Selection> selections = new ArrayList<Selection>();
	private int drawHeight, drawWidth, width, height;
	private static Image image;
	private List<Operation> basicOperations = new ArrayList<>();
	private Map<String, Operation> compositeOperations = new HashMap<>();
	private CompOperDialog cod = new CompOperDialog(this);
	private CompOperShow cos = new CompOperShow(this);
	private SelectionDialog sd = new SelectionDialog(this);
	private ExportDialog ed = new ExportDialog(this);
	private FillSelectionDialog fsd = new FillSelectionDialog(this);
	private ExportCompOper eco = new ExportCompOper(this);
	private Layer finalLayer;
	private ExitDialog exid = new ExitDialog(this); 
	private boolean terminated;
	
	DialogMessage dm = new DialogMessage(this, "Neispravan format fajla!");
	DialogMessage fun = new DialogMessage(this, "Greska!");
	
	private class CanvasPanel extends Panel {
		public void paint(Graphics g) {
			g.setColor(Color.BLACK);
			g.drawLine(canvasPanel.getWidth() - 1, 0, canvasPanel.getWidth() - 1, canvasPanel.getHeight());
			g.drawLine(canvasPanel.getWidth() - 3, 0, canvasPanel.getWidth() - 3, canvasPanel.getHeight());
			g.drawLine(canvasPanel.getWidth() - 50, 0, canvasPanel.getWidth() - 50, canvasPanel.getHeight());
			g.drawLine(canvasPanel.getWidth() - 52, 0, canvasPanel.getWidth() - 53, canvasPanel.getHeight());
			g.setColor(Color.DARK_GRAY);
			g.fillRect(canvasPanel.getWidth() - 51, 0, 49, canvasPanel.getHeight());	
		}
	}
	
	private void addComponents() {
		rightPanel.setPreferredSize(new Dimension(370, 1080));
		leftPanel.setPreferredSize(new Dimension(150, 1080));
		
		panSel.setLayout(new GridLayout(14, 2, 0, 2));
		panLay.setLayout(new GridLayout(11, 1, 0, 5));
		//leftPanel.setLayout(new GridLayout(30, 1, 0, 5));
		
		canvasPanel.setBackground(new Color(40, 40, 40));
		centerPan.setLayout(new BorderLayout());
		centerPan.add(canvasPanel, BorderLayout.CENTER);
		centerPan.setBackground(new Color(40, 40, 40));
		this.add(centerPan);
		
		Panel up = new Panel();
		Panel bottom = new Panel();
		Label title = new Label("SLOJEVI");
		Label sel = new Label("SELEKCIJE");
		
		title.setPreferredSize(new Dimension(400, 55));
		title.setForeground(Color.WHITE);
		title.setAlignment(Label.CENTER);
		title.setBackground(new Color(56, 56, 56));
		title.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		sel.setForeground(Color.WHITE);
		sel.setAlignment(Label.CENTER);
		sel.setBackground(new Color(56, 56, 56));
		sel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		sel.setPreferredSize(new Dimension(400, 55));
		
		rightPanel.setLayout(new GridLayout(0, 1, 10, 0));
		rightPanel.setBackground(Color.DARK_GRAY);
		up.setLayout(new BorderLayout());
		bottom.setLayout(new BorderLayout());
		up.add(sel, BorderLayout.NORTH); up.add(panSel);
		bottom.add(title, BorderLayout.NORTH); bottom.add(panLay);

		rightPanel.add(up); rightPanel.add(bottom);
		
		Label oper = new Label("OPERACIJE");
		Label space = new Label();
		oper.setForeground(Color.WHITE);
		oper.setBackground(new Color(56, 56, 56));
		oper.setPreferredSize(new Dimension(150, 55));
		space.setPreferredSize(new Dimension(150, 20));
		space.setBackground(new Color(56, 56, 56));
		oper.setAlignment(Label.CENTER);
		oper.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		leftPanel.add(oper); 
		basOp.setLayout(new GridLayout(15, 1, 0, 0));
		for (Operation o : basicOperations) basOp.add(o);
		//
		//Panel compPanel = new Panel();
		//compPanel.add(compOp);
		//compPanel.setPreferredSize(new Dimension(130, 380));
		//compPanel.setLayout(new BorderLayout());
		//
		compOp.setLayout(new GridLayout(20, 1, 0, 10));
		leftPanel.setBackground(Color.DARK_GRAY);
		leftPanel.add(basOp);
		leftPanel.add(space);
		leftPanel.add(compOp);
		//
		//leftPanel.add(compPanel);
		//
		((FlowLayout)leftPanel.getLayout()).setHgap(50);
		
		/*ScrollPane sp = new ScrollPane();
		sp.add(panSel);
		up.add(sp);
		ScrollPane sp1 = new ScrollPane();
		sp1.add(panLay);
		bottom.add(sp1);
		ScrollPane sp2 = new ScrollPane();
		sp2.add(compOp);
		compPanel.add(sp2);*/
		
		this.add(leftPanel, BorderLayout.WEST);
		this.add(rightPanel, BorderLayout.EAST);
	}
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				exid.setVisible(true);
				//dispose();
			}
		});
	}
	
	private void addMenus() {
		file = new Menu("Fajl");
		lay = new Menu("Slojevi");
		sel = new Menu("Selekcije");
		comOp = new Menu("Kompozitne operacije");
		
		MenuItem open = new MenuItem("Otvori projekat");
		MenuItem saveAs = new MenuItem("Eksportuj kao...");
		
		MenuItem look = new MenuItem("Pregled");
		MenuItem addCO = new MenuItem("Nova kompozitna operacija");
		MenuItem importCO = new MenuItem("Ucitavanje funkcije");
		MenuItem exportCO = new MenuItem("Cuvanje funkcije");
		comOp.add(addCO); comOp.add(look);
		comOp.add(importCO); comOp.add(exportCO);
		
		
		MenuItem newLayer = new MenuItem("Novi sloj");
		lay.add(newLayer);
		
		MenuItem fillSel = new MenuItem("Popuni selekciju");
		MenuItem newSel = new MenuItem("Nova selekcija");
		newSel.setEnabled(false);
		fillSel.setEnabled(false);
		sel.add(newSel);
		sel.add(fillSel);
		
		file.add(open);
		file.add(saveAs);
		MenuBar menuBar = new MenuBar(); 
		menuBar.add(file); menuBar.add(lay);
		menuBar.add(sel); menuBar.add(comOp);
		setMenuBar(menuBar);
	
		Font font = new Font("Arial", Font.PLAIN, 20);
		sel.setFont(font); lay.setFont(font); comOp.setFont(font); file.setFont(font);
		open.setFont(font); saveAs.setFont(font); saveAs.setEnabled(false);
		
		newLayer.addActionListener(ae -> {
			layerDialog.ok.setEnabled(true);
			layerDialog.setVisible(true);
			layerDialog.gr.setSelectedCheckbox(layerDialog.img);
			layerDialog.txtf.setEnabled(true);
			layerDialog.txtf.setText("");
			layerDialog.w.setEnabled(false);
			layerDialog.h.setEnabled(false);
			layerDialog.w.setText("");
			layerDialog.h.setText("");
		});
		
		open.addActionListener(ae -> {
			openDialogProj.setVisible(true);
			
		});
		
		addCO.addActionListener(ae -> {
			Image.this.cod.setVisible(true);
		});
		
		saveAs.addActionListener(ae -> {
			ed.setVisible(true);
		});
		
		look.addActionListener(ae -> {
			cos.setVisible(true);
		});
		
		newSel.addActionListener(ae -> {
			sd.setVisible(true);
		});
		
		fillSel.addActionListener(ae -> {
			fsd.setVisible(true);
		});
		
		importCO.addActionListener(ae -> {
			openFun.setVisible(true);
		});
		
		exportCO.addActionListener(ae -> {
			eco.setVisible(true);
		});
	}
	
	private class SelPanel extends Panel {
		Button make = new Button(), cancel = new Button("Otkazi");	
		public SelPanel(String s) {
			make.setLabel(s);
			make.addActionListener(ae -> {
				imageCanvas.saveSelection();
				exitSelectMode();
				imageCanvas.repaint();
			});
			cancel.addActionListener(ae -> {
				exitSelectMode();
			});
			make.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18)); 
			cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
			make.setEnabled(false);
			this.setBackground(Color.DARK_GRAY);
			this.add(make); this.add(cancel);
		}
	}
	
	private void fillOperations() {
		basicOperations.add(new Add(0)); 		  
		basicOperations.add(new Sub(0)); 		
		basicOperations.add(new InverseSub(0));
		basicOperations.add(new Div(1));
		basicOperations.add(new InverseDiv(1));
		basicOperations.add(new Mul(0));
		basicOperations.add(new Power(0));
		basicOperations.add(new Min(0));
		basicOperations.add(new Max(0));
		basicOperations.add(new Greyscale()); 		  
		basicOperations.add(new Inversion());
		basicOperations.add(new BlackAndWhite()); 
		basicOperations.add(new Median()); 	      
		basicOperations.add(new Abs());
		basicOperations.add(new Log()); 		  
	}
	
	private Image() {
		super("Photo editor");
		fillOperations(); addComponents(); addMenus(); addListeners();
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		//setBounds(500, 500, 500, 200);
		//setSize(screenSize); 
		setResizable(true); setBackground(Color.DARK_GRAY); setVisible(true);
		setMinimumSize(new Dimension(1000, 500));
		setExtendedState(Frame.MAXIMIZED_BOTH);
	}

	public static void main(String[] args) {
		Image.getInsance();
	}
	
	private void setDrawDim(int w, int h) {
		if (w > canvasPanel.getWidth()) {
			drawWidth = canvasPanel.getWidth() - 200;
			drawHeight = height * (drawWidth * 100 / width) / 100;
		}
		else drawWidth = w;
		
		if (h > canvasPanel.getHeight()) {
			drawHeight = canvasPanel.getHeight() - 200;
			drawWidth = width * (drawHeight * 100 / height) / 100;
		}
		else drawHeight = h;
	}
	
	public void addLayer(String path) throws FileFormatException, IOException, ParserConfigurationException, SAXException {
		Layer layer = null;	
		layer = new Layer(path);
		if (layers.size() == 0) {
			width = layer.getImage().getWidth();
			height = layer.getImage().getHeight();
			setDrawDim(width, height);
			for (Selection s : selections) s.setDrawDim();
			imageCanvas.setPreferredSize(new Dimension(drawWidth, drawHeight));
			imageCanvas.setBackground(new Color(40, 40, 40));
			canvasPanel.setLayout(new GridBagLayout());
			canvasPanel.add(imageCanvas, new GridBagConstraints());
			layers.add(layer);
			panLay.add(layer);
			panLay.revalidate();
			this.getMenuBar().getMenu(2).getItem(0).setEnabled(true);
			this.getMenuBar().getMenu(2).getItem(1).setEnabled(true);
			file.getItem(1).setEnabled(true);
			for (Operation o : basicOperations) 
				o.enable();
			 for (Map.Entry<String, Operation> entry : compositeOperations.entrySet()) 
		        	entry.getValue().enable();
			repaint();
			return;
		}
		
		if (layer.getImage().getWidth() > this.width && layer.getImage().getHeight() > this.height) {
			this.resizeLayers(layer.getImage().getWidth(), layer.getImage().getHeight());
			setDrawDim(layer.getImage().getWidth(), layer.getImage().getHeight());
			for (Selection s : selections) s.setDrawDim();
			imageCanvas.setPreferredSize(new Dimension(drawWidth, drawHeight));
			canvasPanel.revalidate();
			layers.add(layer);
			panLay.add(layer);
			panLay.revalidate();
			repaint();
			return;
		}
		
		if (layer.getImage().getWidth() < this.width && layer.getImage().getHeight() < this.height) {
			layer.resizeLayer(this.width, this.height);
			setDrawDim(layer.getImage().getWidth(), layer.getImage().getHeight());
			for (Selection s : selections) s.setDrawDim();
			imageCanvas.setPreferredSize(new Dimension(drawWidth, drawHeight));
			canvasPanel.revalidate();
			layers.add(layer);
			panLay.add(layer);
			panLay.revalidate();
			repaint();
			return;
		}
		
		if (layer.getImage().getWidth() == this.width && layer.getImage().getHeight() == this.height) {
			layers.add(layer);
			panLay.add(layer);
			panLay.revalidate();
			repaint();
			return;
		}
		
		int h = Math.max(layer.getImage().getHeight(), this.height);
		int w = Math.max(layer.getImage().getWidth(), this.width);

		layer.resizeLayer(w, h);
		resizeLayers(w, h);
		setDrawDim(layer.getImage().getWidth(), layer.getImage().getHeight());
		for (Selection s : selections) s.setDrawDim();
		imageCanvas.setPreferredSize(new Dimension(drawWidth, drawHeight));
		canvasPanel.revalidate();
		layers.add(layer);
		panLay.add(layer);
		panLay.revalidate();
		repaint();
	}
		
	public void addSelection(Selection s) {
		selections.add(s);
		panSel.add(s);
		panSel.revalidate();
	}
	
	public void removeLayer(Object obj) {
		if (layers.size() == 0) return;
		layers.remove(obj);
		panLay.remove((Layer)obj);
		panLay.revalidate();
		if (layers.size() == 0) {
			this.getMenuBar().getMenu(2).getItem(0).setEnabled(false);
			this.getMenuBar().getMenu(2).getItem(1).setEnabled(false);
			this.remove(imageCanvas);
			int s = selections.size();
			for (int i = 0; i < s; i++) {
				panSel.remove(selections.remove(0));
			}
			for (Operation o : basicOperations) 
				o.disable();
			 for (Map.Entry<String, Operation> entry : compositeOperations.entrySet()) 
		        	entry.getValue().disable();
			panSel.revalidate();
		}
		repaint();
	}
	
	public void disable() {
		for (Layer l : layers)
			l.disable();
		for (Selection s : selections)
			s.disable();
		for (Operation o : basicOperations) 
			o.disable();
		 for (Map.Entry<String, Operation> entry : compositeOperations.entrySet()) 
	        	entry.getValue().disable();
		file.setEnabled(false); lay.setEnabled(false);
		sel.setEnabled(false); comOp.getItem(0).setEnabled(false);
		comOp.getItem(2).setEnabled(false);
	}
	
	public void enable() {
		for (Layer l : layers)
			l.enable();
		for (Selection s : selections)
			s.enable();
		for (Operation o : basicOperations) 
			o.enable();
		 for (Map.Entry<String, Operation> entry : compositeOperations.entrySet()) 
	        	entry.getValue().enable();
		file.setEnabled(true); lay.setEnabled(true); 
		sel.setEnabled(true); comOp.getItem(0).setEnabled(true);
		comOp.getItem(2).setEnabled(true);
		sel.getItem(1).setEnabled(true);
	}
	
	public void removeSelection(Object obj) {
		selections.remove((Selection)obj);
		panSel.remove((Selection)obj);
		panSel.revalidate();
		repaint();
	}
	
	public Canvas getCanvas() { return imageCanvas; }
	
	public int getDW() { return drawWidth; }
	public int getDH() { return drawHeight; }
	
	public List<Layer> getLayers() { return layers; } 
	public List<Selection> getSelecions() { return selections; }
	
	@Override
	public void paint(Graphics g) {
		imageCanvas.repaint();
		panLay.repaint();
	}
	
	public static Image getInsance() {
		if (image == null) image = new Image();
		return image;
	}
	
	public void resizeLayers(int w, int h) {
		for (Layer l : layers) l.resizeLayer(w, h);
		this.width = w; this.height = h;
	}
	
	public int getW() { return width; }
	public int getH() { return height; }
	
	public void addCompOper(Operation o) {
		compositeOperations.put(o.getName(), o);
		eco.addNew(o.getName());
		cos.addNew(o);
		compOp.add(o);
		cod.addToList(o.getName());
		o.setPreferredSize(new Dimension(leftPanel.getWidth(), 30));
		leftPanel.revalidate();
	}
	
	public Map<String, Operation> getCompOper() { return compositeOperations; }
	public void setCompOper(Map<String, Operation> m) { compositeOperations = m; }
	
	public boolean isSelectMode() { return (newSel != null) && this.newSel.isVisible(); }
	
	public void toSelectMode() {
		leftPanel.setEnabled(false);
		rightPanel.setEnabled(false);
		file.setEnabled(false); lay.setEnabled(false);
		comOp.setEnabled(false); sel.setEnabled(false);
		this.newSel = new SelPanel("Napravi selekciju");
		this.newSel.setPreferredSize(new Dimension(200, 40));
		centerPan.add(this.newSel, BorderLayout.NORTH);
		centerPan.revalidate();
		this.newSel.setVisible(true);
	}
	
	public void exitSelectMode() {
		leftPanel.setEnabled(true);
		rightPanel.setEnabled(true);
		file.setEnabled(true); lay.setEnabled(true);
		comOp.setEnabled(true); sel.setEnabled(true);
		Image.this.centerPan.remove(newSel);
		newSel.setVisible(false);
		Image.this.centerPan.revalidate();
		imageCanvas.clear();
	}
	
	public SelectionDialog getSelD() { return sd; }
	
	public void enableSel() {
		newSel.make.setEnabled(true);
	}
	
	public Layer createFinalLayer() {
		BufferedImage finalLayer = null;
		if (layers.size() == 0) return null;
		if (layers.stream().allMatch(l -> { return !l.isVisible(); })) return null; 
		
		Collections.reverse(layers);
		for (Layer l : layers) {
			if (finalLayer == null) {
				finalLayer = new BufferedImage(this.width, this.height, BufferedImage.TYPE_4BYTE_ABGR);
				for (int i = 0; i < this.height; i++)
					for (int j = 0; j < this.width; j++)
						finalLayer.setRGB(j, i, l.getImage().getRGB(j, i));
			}
			else {
				for (int i = 0; i < this.height; i++)
					for (int j = 0; j < this.width; j++) {
						int rgba0 = finalLayer.getRGB(j, i);
						int rgba1 = l.getImage().getRGB(j, i);
						int b0 = (rgba0 & 0xff), b1 = (rgba1 & 0xff);
						int g0 = ((rgba0 >> 8) & 0xff), g1 = ((rgba1 >> 8) & 0xff);
						int r0 = ((rgba0 >> 16) & 0xff), r1 = ((rgba1 >> 16) & 0xff);
						int a0 = ((rgba0 >> 24) & 0xff), a1 = ((rgba1 >> 24) & 0xff);
						int r, g, b, a;
						
						a = a0 + ((255 - a0) * a1) / 255; 
						if (a == 0) { 
							finalLayer.setRGB(j, i, 0x00ffffff & rgba0); 
							continue; 
						}
						r = (r0 * a0)/a + (r1 * (255 - a0)*a1)/(a * 255);
						g = (g0 * a0) / a + (g1 * (255 - a0)*a1) / (a * 255);
						b = (b0 * a0) / a + (b1 * (255 - a0)*a1) / (a * 255);
						
						int rgba = (a << 24) | (r << 16) | (g << 8) | (b);
						finalLayer.setRGB(j, i, rgba);
					}			
			}
		}
		Collections.reverse(layers);
		try {
			this.finalLayer = new Layer("_Empty_");
		} catch (FileFormatException | IOException | ParserConfigurationException | SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		this.finalLayer.setImage(finalLayer);
		return this.finalLayer;
	}
	
	public LayerDialog getLayDi() { return layerDialog; }
	
	public void clearInstance() {
		layers.clear();
		selections.clear();
		compositeOperations.clear();
		compOp.removeAll();
		panSel.removeAll();
		panLay.removeAll();
		cos.clear();
		compOp.revalidate();
		panSel.revalidate();
		panLay.revalidate();
		file.getItem(1).setEnabled(false);
		sel.setEnabled(false);
		this.repaint();
	}
	
	public void importCompOper(String path) {
		try {
			Pattern p = Pattern.compile("^.*.(...)$");
			Matcher m = p.matcher(path);
			if (m.matches()) {
				if (m.group(1).equalsIgnoreCase("fun")) {
					DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
					DocumentBuilder docBuilder;
					
					docBuilder = dbFac.newDocumentBuilder();
					File f = new File(path);
					Document doc = docBuilder.parse(f);
					if (doc == null) throw new IOException();
					
					doc.getDocumentElement().normalize();
					
					NodeList compOps = doc.getElementsByTagName("compoperations");
					if (compOps == null) throw new IOException();
					
					if (compOps.item(0).getNodeType() == Node.ELEMENT_NODE) {
						Element el = (Element) compOps.item(0);
						if (el == null) throw new IOException();
						
						NodeList e = el.getChildNodes();
						if (e.item(0).getNodeType() == Node.ELEMENT_NODE) {
							Element coel = (Element)e.item(0);
							if (coel == null) throw new IOException();
							MyFormatter myf = new MyFormatter();
							Operation o = myf.importCompOper(coel);
							Image.getInsance().addCompOper(o);
						}	
						else dm.setVisible(true);
					}
					else dm.setVisible(true);
				} else dm.setVisible(true);
			} else dm.setVisible(true);
		
		} catch(ParserConfigurationException | SAXException | IOException e) {
			dm.setVisible(true);
		}
	}
	
	public void exportCompOper(String path, String name) {
		Pattern p = Pattern.compile("^.*.(...)$");
		Matcher m = p.matcher(path);
		if (m.matches()) {
			if (m.group(1).equalsIgnoreCase("fun")) {
				try {
					DocumentBuilderFactory docF = DocumentBuilderFactory.newInstance(); 
			        DocumentBuilder documentBuilder;
					
			        documentBuilder = docF.newDocumentBuilder();
					
			        Document doc = documentBuilder.newDocument();
			        
			        Element root = doc.createElement("compoperations");
			        root.setAttribute("num", "1");
			        doc.appendChild(root);
			        
			        CompositeOperation co = (CompositeOperation) this.compositeOperations.get(name);
			        if (co == null) {
			        	dm.setVisible(true);
			        	return;
			        }
			        
			        MyFormatter myf = new MyFormatter();
			        myf.exportCompOper(co, root, doc);
			        
			        TransformerFactory transformerFactory = TransformerFactory.newInstance();
			        Transformer transformer = transformerFactory.newTransformer();
			        DOMSource domSource = new DOMSource(doc);
			        StreamResult streamResult = new StreamResult(new File(path));
			        transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			        transformer.transform(domSource, streamResult);
			        
				} catch (ParserConfigurationException | TransformerException e) {
					dm.setVisible(true);
				}
			}
			else dm.setVisible(true);
		}
	}
	
	public void fillSelection(int r, int g, int b) {
		if (layers.size() == 0) return;
		if (this.layers.stream().allMatch(l -> { return !l.isActive(); })) return;
		if (this.selections.stream().allMatch(s -> { return !s.isActive(); })) return;
		
		boolean[][] bools = new boolean[Image.getInsance().getH()][Image.getInsance().getW()];
		for (int i = 0; i < Image.getInsance().getH(); i++)
			for (int j = 0; j < Image.getInsance().getW(); j++) {
				int ii = i, jj = j;
				//if (strSel.anyMatch(s -> s.isActiveCoordinates(jj, ii))) {
					//bools[i][j] = true;
				//}
				if (this.selections.stream().filter(s -> {return s.isActive(); }).
						anyMatch(ss -> ss.isActiveCoordinates(jj, ii))) {
					bools[i][j] = true;
				}
			}
		
		for (Layer l : layers) {
			if (!l.isActive()) continue;
			for (int i = 0; i < l.getImage().getHeight(); i++) {
				for (int j = 0; j < l.getImage().getWidth(); j++) {
					if (bools[i][j]) {
						int old = l.getImage().getRGB(j, i); 
						int rgb = ((old) & 0xff000000) | ((r << 16) & 0x00ff0000) | ((g << 8) & 0x0000ff00) | ((b) & 0x000000ff);
						if (l.getName().equals("_Empty_")) {
							rgb = (rgb | 0xff000000);
							l.setAlpha(i, j);
						}
						l.getImage().setRGB(j, i, rgb);
					}
				}
			}
		}
		
		imageCanvas.repaint();
	}

	public void enableSelections() {
		for (Selection s : selections)
			s.enable();
		sel.setEnabled(true);
		sel.getItem(1).setEnabled(false);
	}
	
	public void setTerminated() { terminated = true; }
	public ExportDialog getExpDi() { return ed; }
	public boolean isTerminated() { return terminated; }
}

class LayerDialog extends Dialog {
	
	TextField txtf = new TextField(30);
	TextField w = new TextField(4), h = new TextField(4);
	CheckboxGroup gr = new CheckboxGroup();
	Checkbox img = new Checkbox("Sa slikom", gr, true);
	Checkbox noImg = new Checkbox("Prazan", gr, false);
	Label wl = new Label("Sirina"), hl = new Label("Visina");
	Label p = new Label("Putanja do slike");
	Button ok = new Button("OK");
	Button cancel = new Button("Otkazi");
	private int curW;
	private int curH;
	
	private void addComponents() {
		Panel pan = new Panel();
		pan.add(img); pan.add(noImg);
		img.setFont(new Font("Arial", Font.BOLD, 17));
		noImg.setFont(new Font("Arial", Font.BOLD, 17));
		img.setForeground(Color.WHITE);
		noImg.setForeground(Color.WHITE);
		this.add(pan);
		
		Panel pan1 = new Panel();
		pan1.add(p);
		p.setFont(new Font("Arial", Font.BOLD, 17));
		p.setForeground(Color.WHITE);
		pan1.add(txtf);
		txtf.setFont(new Font("Arial", Font.TRUETYPE_FONT, 19));
		txtf.setBackground(new Color(50, 50, 50));
		txtf.setForeground(Color.WHITE);
		this.add(pan1);
		
		Panel pan2 = new Panel();
		wl.setFont(new Font("Arial", Font.BOLD, 17));
		hl.setFont(new Font("Arial", Font.BOLD, 17));
		wl.setForeground(Color.WHITE); hl.setForeground(Color.WHITE);
		w.setBackground(new Color(50, 50, 50));
		h.setBackground(new Color(50, 50, 50));
		w.setForeground(Color.WHITE);
		h.setForeground(Color.WHITE);
		w.setFont(new Font("Arial", Font.TRUETYPE_FONT, 19));
		h.setFont(new Font("Arial", Font.TRUETYPE_FONT, 19));
		pan2.add(wl); pan2.add(w);
		pan2.add(hl); pan2.add(h);
		w.setEnabled(false); h.setEnabled(false);
		this.add(pan2);
		
		Panel pan3 = new Panel();
		pan3.add(ok);
		pan3.add(cancel);
		ok.setFont(new Font("Arial", Font.TRUETYPE_FONT, 17));
		cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 17));
		pan3.setBackground(new Color(100, 100, 100));
		this.add(pan3);
	}
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				LayerDialog.this.setVisible(false);
				w.setText("");
				h.setText("");
				gr.setSelectedCheckbox(img);
				txtf.setText("");
			}
		});
		
		img.addItemListener(ie -> {
			if (img.getState()) {
				w.setEnabled(false);
				h.setEnabled(false);
				w.setText("");
				h.setText("");
				txtf.setEnabled(true);
				ok.setEnabled(true);
			}
		});
		
		noImg.addItemListener(ie -> {
			if (noImg.getState()) {
				txtf.setEnabled(false);
				if (Image.getInsance().getLayers().size() == 0) {
					w.setEnabled(true);
					h.setEnabled(true);
					if (w.getText().equals("") || !h.getText().equals(""))
						ok.setEnabled(false);
				}
				else ok.setEnabled(true);
			}
		});
		
		ok.addActionListener(ae -> {
			new ImageLoader().start();
			/*String com = txtf.getText();
			//DODAJ PROVERE
			try {
				this.setVisible(false);
				if (noImg.getState()) {
					Image.getInsance().addLayer("_Empty_");
				}
				else {
					Image.getInsance().addLayer(com);
				}
			} catch (FileFormatException | IOException | ParserConfigurationException | SAXException e) {
				Image.getInsance().dm.setVisible(true);
			}
			txtf.setText("");*/
		});
		
		txtf.addActionListener(ae -> {
			new ImageLoader().start();
			/*String com = txtf.getText();
			//DODAJ PROVERE
			try {
				Image.getInsance().addLayer(com);
				this.setVisible(false);
			} catch (FileFormatException | IOException | ParserConfigurationException | SAXException e) {
				Image.getInsance().dm.setVisible(true);
			}
			txtf.setText("");*/
		});
		
		cancel.addActionListener(ae -> {
			this.setVisible(false);
			w.setText("");
			h.setText("");
			gr.setSelectedCheckbox(img);
			txtf.setText("");
		});
		
		w.addTextListener(te -> {
			try {
				int wi = Integer.parseInt(w.getText());
				if (wi > 0) {
					ok.setEnabled(true);
					curW = wi;
				}
				int hi = Integer.parseInt(h.getText());
			} catch (NumberFormatException e) {
				ok.setEnabled(false);
			}
		});
		
		h.addTextListener(te -> {
			try {
				int hi = Integer.parseInt(h.getText());
				if (hi > 0) {
					ok.setEnabled(true);
					curH = hi;
				}
				int wi = Integer.parseInt(w.getText());
			} catch (NumberFormatException e) {
				ok.setEnabled(false);
			}
		});
	}
	
	private class ImageLoader extends Thread {	
		@Override
		public void run() {
			synchronized (Image.getInsance()) {
				Image.getInsance().disable();
				String com = txtf.getText();
				//DODAJ PROVERE
				try {
					LayerDialog.this.setVisible(false);
					if (noImg.getState()) {
						Image.getInsance().addLayer("_Empty_");
					}
					else {
						Image.getInsance().addLayer(com);
					}
				} catch (FileFormatException | IOException | ParserConfigurationException | SAXException e) {
					Image.getInsance().dm.setVisible(true);
				}
				txtf.setText("");
				Image.getInsance().enable();
			}
		}
	}
	
	public int getCurW() { return curW; }
	public int getCurH() { return curH; }
	
	public LayerDialog(Frame parent) {
		super(parent, "Novi sloj", true);
		setBounds(650, 250, 600, 220);
		this.setLayout(new GridLayout(4, 1));
		addComponents();
		addListeners();
		setBackground(Color.DARK_GRAY);
		setVisible(false);
	}
}