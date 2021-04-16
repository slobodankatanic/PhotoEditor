package photoshop;

import java.awt.Button;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ComponentAdapter;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class SelectionDialog extends Dialog {
	
	private TextField tf = new TextField(20);
	private String name;
	private Label label = new Label("Naziv");
	private Button cancel = new Button("Otkazi"), ok = new Button("OK");
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				SelectionDialog.this.setVisible(false);
				name = tf.getText();
				tf.setText("");
				ok.setEnabled(false);
			}
		});
		ok.addActionListener(ae -> {
			SelectionDialog.this.setVisible(false);
			Image.getInsance().toSelectMode();
			name = tf.getText();
			tf.setText("");
			ok.setEnabled(false);
		});
		cancel.addActionListener(ae -> {
			SelectionDialog.this.setVisible(false);
			name = tf.getText();
			tf.setText("");
			ok.setEnabled(false);
		});
		tf.addTextListener(te -> {
			String s = tf.getText();
			if (s.equals("") || (Image.getInsance().getSelecions().stream().anyMatch(ss->{
				return ss.getName().equals(tf.getText());
			}))) {
				ok.setEnabled(false);
			}
			else ok.setEnabled(true);
		});
		tf.addActionListener(ae -> {
			if (ok.isEnabled()) {
				SelectionDialog.this.setVisible(false);
				Image.getInsance().toSelectMode();
				name = tf.getText();
				tf.setText("");
				ok.setEnabled(false);
			}
		});
	}
	
	public SelectionDialog(Frame parent) {
		super(parent, "Nova selekcija", true);
		setBounds(550, 300, 350, 150);
		setLayout(new FlowLayout());
		((FlowLayout)this.getLayout()).setVgap(10);
		label.setFont(new Font("Arial", Font.BOLD, 16));
		this.add(label);
		label.setForeground(Color.WHITE);
		this.add(tf);
		tf.setBackground(new Color(50, 50, 50));
		tf.setForeground(Color.WHITE);
		tf.setFont(new Font("Arial", Font.TRUETYPE_FONT, 19));
		this.setBackground(Color.DARK_GRAY);
		ok.setEnabled(false);
		ok.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		this.add(ok); this.add(cancel);
		addListeners();
		setVisible(false);
	}
	
	public String getText() { return name; } 
}

class FillSelectionDialog extends Dialog {
	
	private TextField red = new TextField(3);
	private TextField green = new TextField(3);
	private TextField blue = new TextField(3);
	private Button ok = new Button("OK"), cancel = new Button("Otkazi");
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				FillSelectionDialog.this.setVisible(false);
				red.setText(""); green.setText(""); blue.setText("");
				ok.setEnabled(false);
			}
		});
		
		cancel.addActionListener(ae -> {
			FillSelectionDialog.this.setVisible(false);
			red.setText(""); green.setText(""); blue.setText("");
			ok.setEnabled(false);
		});
		
		ok.addActionListener(ae -> {
			int r = Integer.parseInt(red.getText());
			int g = Integer.parseInt(green.getText());
			int b = Integer.parseInt(blue.getText());
			Image.getInsance().fillSelection(r, g, b);
			FillSelectionDialog.this.setVisible(false);
			red.setText(""); green.setText(""); blue.setText("");
			ok.setEnabled(false);
		});
		
		red.addTextListener(te -> {
			try {
				int r = Integer.parseInt(red.getText());
				int g = Integer.parseInt(green.getText());
				int b = Integer.parseInt(blue.getText());
				if (g < 0 || g > 255 || r < 0 || r > 255 || b < 0 || b > 255) 
					ok.setEnabled(false);
				else ok.setEnabled(true);
			} catch(NumberFormatException e) {
				ok.setEnabled(false);
			}
		});
		
		blue.addTextListener(te -> {
			try {
				int r = Integer.parseInt(red.getText());
				int g = Integer.parseInt(green.getText());
				int b = Integer.parseInt(blue.getText());
				if (g < 0 || g > 255 || r < 0 || r > 255 || b < 0 || b > 255) 
					ok.setEnabled(false);
				else ok.setEnabled(true);
			} catch(NumberFormatException e) {
				ok.setEnabled(false);
			}
		});

		green.addTextListener(te -> {
			try {
				int r = Integer.parseInt(red.getText());
				int g = Integer.parseInt(green.getText());
				int b = Integer.parseInt(blue.getText());
				if (g < 0 || g > 255 || r < 0 || r > 255 || b < 0 || b > 255) 
					ok.setEnabled(false);
				else ok.setEnabled(true);
			} catch(NumberFormatException e) {
				ok.setEnabled(false);
			}
		});
	}
	
	public FillSelectionDialog(Frame parent) {
		super(parent, "Popuni selekciju", true);
		setBounds(600, 250, 390, 150);
		setLayout(new FlowLayout());
		((FlowLayout)this.getLayout()).setVgap(20);
		Label r = new Label("Red"), g = new Label("Green"), b = new Label("Blue");
		Font f = new Font("Arial", Font.TRUETYPE_FONT, 18);
		r.setFont(f); g.setFont(f); b.setFont(f);
		red.setFont(f); blue.setFont(f); green.setFont(f);
		red.setBackground(new Color(80, 80, 80));
		green.setBackground(new Color(80, 80, 80));
		blue.setBackground(new Color(80, 80, 80));
		r.setForeground(Color.WHITE);
		g.setForeground(Color.WHITE);
		b.setForeground(Color.WHITE);
		red.setForeground(Color.WHITE);
		green.setForeground(Color.WHITE);
		blue.setForeground(Color.WHITE);
		ok.setFont(f); cancel.setFont(f);
		ok.setEnabled(false);
		this.add(r); this.add(red);
		this.add(g); this.add(green);
		this.add(b); this.add(blue);
		this.add(ok); this.add(cancel);
		addListeners();
		this.setBackground(Color.DARK_GRAY);
		this.setVisible(false);
	}		
}