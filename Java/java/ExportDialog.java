package photoshop;

import java.awt.BorderLayout;
import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

public class ExportDialog extends Dialog {
	
	private Choice choice = new Choice();
	private Button export = new Button("Eksportuj");
	private Button cancel = new Button("Otkazi");
	private TextField tf = new TextField(20);
	
	private void addListeners() {
		cancel.addActionListener(ae-> {
			this.setVisible(false);
			if (Image.getInsance().isTerminated())
				Image.getInsance().dispose();
		});
		
		export.addActionListener(ae-> {
			String s = choice.getSelectedItem();
			Formatter f = null;
			if (s.equals("BMP")) {
				f = new BMPFormatter();
			}
			else {
				if (s.equals("PAM")) {
					f = new PAMFormatter();
				}
				else {
					f = new MyFormatter();
				}
			}
			Thread t = new ExportThread(f, tf.getText());
			t.start();
			this.setVisible(false);
			if (Image.getInsance().isTerminated()) {
				try {
					t.join();
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Image.getInsance().dispose();
			}
		});
		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				ExportDialog.this.setVisible(false);
				if (Image.getInsance().isTerminated())
					Image.getInsance().dispose();
			}
		});
		
		tf.addTextListener(te -> {
			if (tf.getText().equals("")) {
				export.setEnabled(false);
			}
			else {
				export.setEnabled(true);
			}
		});
	}
	
	private class ExportThread extends Thread {
		private Formatter f;
		private String s;
		public ExportThread(Formatter f, String s) {
			this.f = f; this.s = s;
		}
		@Override
		public void run() {
			synchronized (Image.getInsance()) {
				Image.getInsance().disable();
				try {
					f.exportImage(this.s, Image.getInsance().createFinalLayer());
				} catch (ParserConfigurationException | TransformerException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Image.getInsance().enable();
			}
		}
	}
	
	public ExportDialog(Frame parent) {
		super(parent, "Eksportuj kao...", true);
		setBounds(600, 300, 590, 170);
		setLayout(new FlowLayout());
		((FlowLayout)this.getLayout()).setVgap(20);
		((FlowLayout)this.getLayout()).setHgap(10);
		choice.add("BMP");
		choice.add("PAM");
		choice.add("Sopstveni format");	
		choice.select(0);	
		export.setEnabled(false);
		
		Label l = new Label("Format");
		l.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		l.setForeground(Color.white);
		Label la = new Label("Putanja");
		la.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		la.setForeground(Color.white);
		tf.setBackground(new Color(80, 80, 80));
		tf.setForeground(Color.white);
		tf.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		
		this.add(la);
		this.add(tf);
		this.add(l);
		this.add(choice);
		this.add(export);
		this.add(cancel);
		export.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		choice.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		choice.setBackground(new Color(80, 80, 80));
		choice.setForeground(Color.white);
		setBackground(Color.DARK_GRAY);
		addListeners();
		setVisible(false);
	}
}

class ExportCompOper extends Dialog {
	
	private Choice choice = new Choice();
	private TextField path = new TextField(20);
	private Button ok = new Button("OK"), cancel = new Button("Otkazi");
	private Panel leftPanel = new Panel();
	private Panel rightPanel = new Panel();
	private Label p = new Label("Putanja"), c = new Label("Format");
	
	public void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				ExportCompOper.this.setVisible(false);
				ok.setEnabled(false);
				path.setText("");
			}
		});
		
		path.addTextListener(ae -> {
			String s = path.getText();
			if (s.equals("")) {
				ok.setEnabled(false);
			}
			else if (choice.getItemCount() > 0) 
				ok.setEnabled(true);
		});
		
		cancel.addActionListener(ae -> {
			ExportCompOper.this.setVisible(false);
			ok.setEnabled(false);
			path.setText("");
		});
		
		ok.addActionListener(ae -> {
			String s = path.getText();
			Image.getInsance().exportCompOper(s, choice.getSelectedItem());
			ExportCompOper.this.setVisible(false);
			ok.setEnabled(false);
			path.setText("");
		});
	}
	
	public ExportCompOper(Frame parent) {
		super(parent, "Eksportovanje funkcije", true);
		setBounds(600, 300, 640, 150);
		setLayout(new GridLayout(1, 2));
		
		leftPanel.add(p); leftPanel.add(path);
		leftPanel.add(c); leftPanel.add(choice);
		rightPanel.add(ok); rightPanel.add(cancel);
		leftPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
		ok.setEnabled(false);
		
		Font f = new Font("Arial", Font.BOLD, 16);
		Font f1 = new Font("Arial", Font.TRUETYPE_FONT, 18);
		
		p.setFont(f); p.setForeground(Color.WHITE);
		c.setFont(f); c.setForeground(Color.WHITE);
		path.setFont(f1); path.setBackground(new Color(80, 80, 80));
		path.setForeground(Color.WHITE);
		ok.setFont(f1); cancel.setFont(f1);
		choice.setFont(f1);
		choice.setForeground(Color.WHITE);
		choice.setBackground(new Color(80, 80, 80));
		
		addListeners();
		add(leftPanel); add(rightPanel);
		setBackground(Color.DARK_GRAY);
		setVisible(false);
	}
	
	public void addNew(String s) {
		choice.add(s);
	}
	
	public void clear() {
		choice.removeAll();
	}
}
