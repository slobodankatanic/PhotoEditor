package photoshop;

import java.awt.Panel;

public class Rectangle {

	private int x, y, w, h;
	private int drawx, drawy, draww, drawh;
	
	public Rectangle(int x, int y, int w, int h) {
		super();
		this.drawx = x;
		this.drawy = y;
		this.draww = w;
		this.drawh = h;
		
		this.x = Image.getInsance().getW() * drawx / Image.getInsance().getDW(); 
		this.y = Image.getInsance().getH() * drawy / Image.getInsance().getDH();
		this.w = Image.getInsance().getW() * draww / Image.getInsance().getDW();
		this.h = Image.getInsance().getH() * drawh / Image.getInsance().getDH();
	}
	
	public int getDX() { return drawx; }
	public int getDY() { return drawy; }
	public int getDW() { return draww; }
	public int getDH() { return drawh; }
	
	public int getX() { return x; }
	public int getY() { return y; }
	public int getW() { return w; }
	public int getH() { return h; }
	
	public void setDrawDim() {
		drawx = Image.getInsance().getDW() * x / Image.getInsance().getW(); 
		drawy = Image.getInsance().getDH() * y / Image.getInsance().getH();
		draww = Image.getInsance().getDW() * w / Image.getInsance().getW();
		drawh = Image.getInsance().getDH() * h / Image.getInsance().getH(); 
	}
}
