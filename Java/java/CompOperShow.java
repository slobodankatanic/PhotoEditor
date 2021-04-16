package photoshop;

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

public class CompOperShow extends Dialog {
	
	private Panel header = new Panel();
	private Label name = new Label("Naziv"), oper = new Label("Operacije");
	private Panel mainPanel = new Panel(); 
	private int n; 
	
	private void addListeners() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				CompOperShow.this.setVisible(false);
			}
		});
	}
	 
	public CompOperShow(Frame parent) {
		super(parent, "Kompozitne operacije", true);
		//this.setLayout(new GridLayout(21, 1));
		this.setLayout(new FlowLayout());
		setBounds(550, 250, 1000, 700);
		header.setLayout(new GridLayout(1, 2, 2, 0));
		header.add(name); header.add(oper);
		//header.setBackground(new Color(40, 40, 40));
		header.setBackground(Color.BLACK);
		name.setBackground(new Color(70, 70, 70));
		name.setAlignment(Label.CENTER);
		name.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		oper.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		oper.setAlignment(Label.CENTER);
		oper.setBackground(new Color(70, 70, 70));
		name.setForeground(Color.WHITE);
		oper.setForeground(Color.WHITE);	
		mainPanel.setLayout(new GridLayout(30, 1, 0, 0));
		header.setPreferredSize(new Dimension(this.getWidth(), 40));
		mainPanel.setPreferredSize(new Dimension(1000, 900));
		this.add(header); this.add(mainPanel);
		addListeners();
		setBackground(new Color(50, 50, 50));
		setVisible(false);
	}
	
	public void addNew(Operation o2) {
		Label n = new Label("  " + o2.getName());
		n.setAlignment(Label.LEFT);
		Label o = new Label(o2.toString());
		Panel p = new Panel();
		p.setBackground(Color.BLACK);
		p.setLayout(new GridLayout(1, 2, 2, 0));
		p.add(n); p.add(o);
		n.setBackground(new Color(100, 100, 100));
		o.setBackground(new Color(100, 100, 100));
		n.setForeground(Color.WHITE);
		o.setForeground(Color.WHITE);
		n.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		o.setFont(new Font("Arial", Font.BOLD, 14));
		if (this.n == 0) { 
			o.setBackground(new Color(120, 120, 120));
			n.setBackground(new Color(120, 120, 120));	
			this.n = 1; 
		}
		else { 
			o.setBackground(new Color(70, 70, 70));
			n.setBackground(new Color(70, 70, 70));
			this.n = 0; 
		}
		mainPanel.add(p);
		//mainPanel.add(n); mainPanel.add(o);
	}
	
	public void clear() {
		mainPanel.removeAll();
		mainPanel.revalidate();
	}
}
