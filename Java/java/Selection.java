package photoshop;

import java.awt.Button;
import java.awt.Checkbox;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

public class Selection extends Panel {
	
	private static int ID;
	private int id = ++ID;
	private String name;
	private List<Rectangle> rectangles = new ArrayList<>();
	private boolean active;
	private Checkbox act = new Checkbox("Aktivna", true);
	private Label label;
	private Button delete = new Button("Obrisi");
	private DeleteDialog dd;
	
	private void addListeners() {
		act.addItemListener(ie -> {
			if (act.getState())
				active = true;
			else 
				active = false;
			Image.getInsance().repaint();
		});
		
		delete.addActionListener(ae -> {
			dd.setVisible(true);
		});
	}
	
	public Selection(List<Rectangle> r, String s) {
		this.active = true;
		//String s = String.format("[%-4s, %-4s, %-4s, %-4s]", x, y, w, h);
		//label = new Label(s);
		this.rectangles = r;
		label = new Label(s);
		label.setFont(new Font("Arial", Font.BOLD, 17));
		label.setForeground(Color.WHITE);
		label.setAlignment(Label.LEFT);
		label.setPreferredSize(new Dimension(200, 15));
		act.setForeground(Color.WHITE);
		act.setEnabled(true);
		add(label); add(act); add(delete);
		setBackground(new Color(80, 80, 80));
		dd = new DeleteDialog(Image.getInsance(), this,
				"Da li zaista zelite da obrisete selekciju?");
		this.name = s;
		addListeners();
	}

	public boolean isActive() { return act.getState(); }
	
	public void setActivity() { active = true; act.setState(true); }
	public void resetActivity() { active = false; act.setState(false); }

	/*public int getX() { return x; }
	public void setX(int x) { this.x = x; }
	
	public int getY() { return y; }
	public void setY(int y) { this.y = y; }
	
	public int getW() { return w; }
	public void setW(int w) { this.w = w; }
	
	public int getH() { return h; }
	public void setH(int h) { this.h = h; }*/
	
	public String getName() { return name; }
	public void setName(String name) { this.name = name; label.setText(name); }
	
	public List<Rectangle> getRectangles() { return rectangles; }
	
	public void enable() {
		delete.setEnabled(true);
		act.setEnabled(true);
	}
	
	public void disable() {
		delete.setEnabled(false);
		act.setEnabled(false);
	}
	
	public boolean isActiveCoordinates(int x, int y) {
		return rectangles.stream().anyMatch(r -> {
			return (r.getX() + r.getW() - 1 >= x) && 
				   (r.getX() <= x) &&
				   (r.getY() + r.getH() - 1 >= y) &&
				   (r.getY() <= y);
		});
	}
	
	public void setDrawDim() {
		for (Rectangle r : rectangles) 
			r.setDrawDim();
	}
}
