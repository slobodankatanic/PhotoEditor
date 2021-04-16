package photoshop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Font;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class Layer extends Panel {
	
	private BufferedImage layerImage;
	private static int ID;
	private int id = ++ID;
	private TextField text = new TextField(1);
	private Checkbox visible = new Checkbox("Vidljiv", true);
	private Checkbox active = new Checkbox("Aktivan", true);
	private boolean vis = true, act = true;
	private int transparency;
	private int alphas[][];
	private DeleteDialog dd;
	private Button delete = new Button("Obrisi");
	private String name;
	
	private void addListeners() {
		visible.addItemListener(ie -> {
			if (!visible.getState()) 
				vis = false;
			else vis = true;
			//Image.getInsance().getCanvas().revalidate();
			Image.getInsance().getCanvas().repaint();
		});
		text.addTextListener(te -> {
			//new TransparentChanger().start();
			/*try {
				int newO = Integer.parseInt(text.getText());
				if (newO < 0) newO = 0;
				else if (newO > 100) newO = 100;
				int newT = newO;
				for (int i = 0; i < layerImage.getWidth(); i++)
					for (int j = 0; j < layerImage.getHeight(); j++) {
						int rgb = layerImage.getRGB(i, j);
						newO = alphas[j][i] * newT / 100;/////////////RESIZE ALPHAS
						newO <<= 24;
						rgb &= 0x00ffffff;
						layerImage.setRGB(i, j, rgb | newO);
					}
				transparency = newT;
				Image.getInsance().getCanvas().repaint();	
			} catch(NumberFormatException e) {}*/	
		});
		text.addActionListener(ae -> {
			new TransparentChanger().start();
		});
		text.addFocusListener(new FocusAdapter() {
			@Override
			public void focusLost(FocusEvent fe) {
				text.setText(transparency + "");
			}
		});
		delete.addActionListener(ae -> {
			dd.setVisible(true);
		});
	}
	
	private void addComponents() {
		Label lab = new Label("Sloj " + id + "    ");
		lab.setForeground(Color.WHITE);
		lab.setFont(new Font("Arial", Font.BOLD, 17));
		lab.setAlignment(Label.LEFT);
		visible.setForeground(Color.WHITE);
		active.setForeground(Color.WHITE);
		Label op = new Label("Providnost");
		op.setForeground(Color.WHITE);
		text.setText("100");
		this.add(lab, BorderLayout.WEST);
		this.add(visible);
		this.add(active);
		this.add(text);
		this.add(op);
		this.add(delete);
		dd = new DeleteDialog(Image.getInsance(), this, 
				"Da li zaista zelite da obrisete sloj");
	}
	
	public void fillAlpha() {
		alphas = new int[layerImage.getHeight()][layerImage.getWidth()];
		for (int i = 0; i < layerImage.getHeight(); i++)
			for (int j = 0; j < layerImage.getWidth(); j++)
				alphas[i][j] = ((layerImage.getRGB(j, i) >> 24) * 100 / this.transparency) & 0xff;
	}
	
	private class TransparentChanger extends Thread {
		@Override
		public void run() {
			synchronized (layerImage) {
				try {
					int newO = Integer.parseInt(text.getText());
					if (newO < 0) newO = 0;
					else if (newO > 100) newO = 100;
					int newT = newO;
					for (int i = 0; i < layerImage.getWidth(); i++)
						for (int j = 0; j < layerImage.getHeight(); j++) {
							int rgb = layerImage.getRGB(i, j);
							newO = alphas[j][i] * newT / 100;/////////////RESIZE ALPHAS
							newO <<= 24;
							rgb &= 0x00ffffff;
							layerImage.setRGB(i, j, rgb | newO);
						}
					transparency = newT;
					Image.getInsance().getCanvas().repaint();
				}
				catch(NumberFormatException e) {}
			}
		}
	}
	
	public Layer(String path) throws FileFormatException, IOException, ParserConfigurationException, SAXException {
		this.name = "_NonEmpty_";
		if (path.equals("_Empty_")) {
			this.name = "_Empty_";
			this.createEmptyLayer(Image.getInsance().getLayDi().getCurW(),
					Image.getInsance().getLayDi().getCurH());
			addComponents();
			addListeners();
			setBackground(new Color(80, 80, 80));
			return;
		}
		Pattern p = Pattern.compile("^.*.(...)$");
		Matcher m = p.matcher(path);
		if (m.matches()) {
			Formatter f = null;
			if (m.group(1).equalsIgnoreCase("bmp")) f = new BMPFormatter();
			else if (m.group(1).equalsIgnoreCase("pam")) f = new PAMFormatter();
			else throw new FileFormatException();
			f.importImage(path);
			layerImage = f.getImage();
			this.transparency = 100;
			fillAlpha();
			addComponents();
			addListeners();
			setBackground(new Color(80, 80, 80));
		}
		else throw new FileFormatException();
	}

	public BufferedImage getImage() { return layerImage; }
	
	public void setActivity() { act = true; active.setState(true); }
	
	public void resetActivity() { act = false; active.setState(false); }
	
	public void setVisibility() { vis = true; visible.setState(true); }
	
	public void resteVisibility() { vis = false; visible.setState(false); }
	
	public boolean isActive() { return active.getState(); }
	
	public boolean isVisible() { return visible.getState(); }
	
	public int getTransparency() { return transparency; }
	
	public void setTransparency(int t) { transparency = t; text.setText(t + ""); }
	
	public void resizeLayer(int w, int h) {
		if (w > layerImage.getWidth() || h > layerImage.getHeight()) {
			BufferedImage newImg = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
			int newAl[][] = new int[h][w];
			
			for (int i = 0; i < layerImage.getHeight(); i++)
				for (int j = 0; j < layerImage.getWidth(); j++) {
					newImg.setRGB(j, i, layerImage.getRGB(j, i));
					newAl[i][j] = alphas[i][j];
				}
					
			for (int i = layerImage.getHeight(); i < h; i++) {
				for (int j = 0; j < w; j++) { 
					newImg.setRGB(j, i, 0);
					newAl[i][j] = 0;
				}
			}
			
			for (int i = 0; i < layerImage.getHeight(); i++)
				for (int j = layerImage.getWidth(); j < w; j++) {
					newImg.setRGB(j, i, 0);
					newAl[i][j] = 0;
				}
			
			layerImage = newImg;
			alphas = newAl;
		}
	}
	
	public String getName() { return name; }
	
	public void setImage(BufferedImage bi) {
		this.layerImage = bi;
	}
	
	public void createEmptyLayer(int w, int h) {
		if (Image.getInsance().getLayers().size() > 0) {
			w = Image.getInsance().getW();
			h = Image.getInsance().getH();
		}
		BufferedImage bi = new BufferedImage(w, h, BufferedImage.TYPE_4BYTE_ABGR);
		alphas = new int[h][w];
		
		for (int i = 0; i < h; i++)
			for (int j = 0; j < w; j++) {
				bi.setRGB(j, i, 0);
				alphas[i][j] = 0;
			}
		
		this.transparency = 100;	
		this.layerImage = bi;
	}
	
	public void disable() {
		text.setEnabled(false);
		active.setEnabled(false);
		visible.setEnabled(false);
		delete.setEnabled(false);
	}
	
	public void enable() {
		text.setEnabled(true);
		active.setEnabled(true);
		visible.setEnabled(true);
		delete.setEnabled(true); 
	}
	
	public void setAlpha(int i, int j) {
		alphas[i][j] = 0xff;
	}
}