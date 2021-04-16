package photoshop;

import java.awt.BasicStroke;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.ListResourceBundle;

public class ImageCanvas extends Canvas {
	
	private int startX, startY;
	private int endX, endY;
	private boolean started;
	private Point minP;
	private List<Rectangle> listRect = new ArrayList<>();
	
	private class ImageDrawer extends Thread {
		
		private Layer l;
		
		public ImageDrawer(Layer layer) {
			this.l = layer;
		}
		
		@Override
		public void run() {
			synchronized (l) {
				Image img = Image.getInsance();
				Graphics g = ImageCanvas.this.getGraphics();
				g.drawImage(l.getImage(), (img.getCanvas().getWidth() - img.getDW()) / 2, 
						(img.getCanvas().getHeight() - img.getDH()) / 2, img.getDW(), img.getDH(), null);
			}
		}
	}
	
	public ImageCanvas() {
		addMouseListener(new MouseAdapter() {	
			@Override
			public void mousePressed(MouseEvent me) {
				if (!Image.getInsance().isSelectMode()) return;
				startX = me.getX();
				startY = me.getY();
				started = true;
			}
			
			@Override
			public void mouseReleased(MouseEvent me) {
				if (!Image.getInsance().isSelectMode()) return;
				listRect.add(new Rectangle(minP.x, minP.y, 
						Math.abs(endX - startX), Math.abs(endY - startY)));
				Image.getInsance().enableSel();
				ImageCanvas.this.repaint();
			}
		});
		addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseDragged(MouseEvent me) {
				if (!Image.getInsance().isSelectMode()) return;
				endX = me.getX();
				endY = me.getY();
				Graphics g = Image.getInsance().getCanvas().getGraphics();
				Point p1, p2, p3, p4;
				int s1, s2, s3, s4;
				p1 = new Point(endX, endY);
				p2 = new Point(startX, startY);
				p3 = new Point(endX, startY);
				p4 = new Point(startX, endY);
				s1 = (int) (p1.getX() + p1.getY());
				s2 = (int) (p2.getX() + p2.getY());
				s3 = (int) (p3.getX() + p3.getY());
				s4 = (int) (p4.getX() + p4.getY());
				
				minP = null;
				if (s1 <= s2 && s1 <= s3 && s1 <= s4) minP = p1;
				else if (s2 <= s1 && s2 <= s3 && s2 <= s4) minP = p2;
					else if (s3 <= s2 && s3 <= s1 && s3 <= s4) minP = p3;
				else minP = p4; 
				g.setColor(Color.WHITE);
				
				int x = (int)minP.getX(), y = (int)minP.getY(), ex, sx, ey, sy;			
				ImageCanvas img = ImageCanvas.this; 
				
				if ((int)minP.getX() >= img.getWidth()) x = img.getWidth() - 1;
				else if ((int)minP.getX() <= 0) x = 1;
				
				if ((int)minP.getY() >= img.getHeight()) y = img.getHeight() - 1;
				else if ((int)minP.getY() <= 0) y = 1;
				
				if (endX >= img.getWidth()) endX = img.getWidth() - 1;
				else if (endX <= 0) endX = 1;
				
				if (endY >= img.getHeight()) endY = img.getHeight() - 1;
				else if (endY <= 0) endY = 1;
				
				if (startX >= img.getWidth()) startX = img.getWidth() - 1;
				else if (startX <= 0) startX = 1;
				
				if (startY >= img.getHeight()) startY = img.getHeight() - 1;
				else if (startY <= 0) startY = 1;
				minP.x = x; minP.y = y;
				g.drawRect(x, y, 
				Math.abs(endX - startX), Math.abs(endY - startY));
			}
		});
	}
	
	@Override
	public void paint(Graphics g) {
		Image img = Image.getInsance();
		//synchronized(Image.getInsance()) {
		
		synchronized (img) {
			for (Layer l : img.getLayers()) {
				if (l.isVisible()) {
					//new ImageDrawer(l).start();
					g.drawImage(l.getImage(), (img.getCanvas().getWidth() - img.getDW()) / 2, 
							(img.getCanvas().getHeight() - img.getDH()) / 2, img.getDW(), img.getDH(), null);
				}
			}
		}
		
		/*Layer la = Image.getInsance().createFinalLayer();
		if (la != null) {
			g.drawImage(la.getImage(), (img.getCanvas().getWidth() - img.getDW()) / 2, 
					(img.getCanvas().getHeight() - img.getDH()) / 2, img.getDW(), img.getDH(), null);
		}*/
		
		if (!img.isSelectMode()) {
			for (Selection s : img.getSelecions())
				if (s.isActive()) {
					for (Rectangle r : s.getRectangles()) {
						Graphics2D g2d = (Graphics2D) g;
						g2d.setColor(Color.WHITE);
						float[] dash1 = { 2f, 0f, 2f };
						BasicStroke bs1 = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
														  1.0f, dash1, 2f);
						g2d.setStroke(bs1);
						g2d.drawRect(r.getDX(), r.getDY(), r.getDW(), r.getDH());
					}
				}
		}
		else {
			for (Rectangle r : listRect) {
				Graphics2D g2d = (Graphics2D) g;
				g2d.setColor(Color.WHITE);
				float[] dash1 = { 2f, 0f, 2f };
				BasicStroke bs1 = new BasicStroke(2, BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND, 
												  1.0f, dash1, 2f);
				g2d.setStroke(bs1);
				g2d.drawRect(r.getDX(), r.getDY(), r.getDW(), r.getDH());
			}
		}
			
		//}
	}
	
	public void saveSelection() {
		if (listRect.size() > 0) {
			Image.getInsance().addSelection(new Selection(listRect, 
					Image.getInsance().getSelD().getText()));
		}
	}
	
	public void clear() {
		listRect = new ArrayList<>();
	}
}