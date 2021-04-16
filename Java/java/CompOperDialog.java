package photoshop;

import java.awt.Button;
import java.awt.Choice;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextArea;
import java.awt.TextField;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

class CompOperDialog extends Dialog {
	
	//private Checkbox[] opers = new Checkbox[15];
	private Choice list = new Choice();
	private Panel opPanel = new Panel(), curP = new Panel();
	private TextField par = new TextField(3), nameTf = new TextField(20);
	private Button add = new Button("Dodaj"), make = new Button("Napravi"), cancel = new Button("Otkazi");
	private Label name = new Label("Naziv");
	private List<Operation> operations = new ArrayList<Operation>();
	
	private void addListeners() {
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				CompOperDialog.this.setVisible(false);
				CompOperDialog.this.clear();
			}
		});
		
		list.addItemListener(ae -> {
			String s = list.getSelectedItem();
			if (s.equals("ADD") || s.equals("SUB") || s.equals("DIV") || s.equals("MUL")
					|| s.equals("INVERSE DIV") || s.equals("MIN") || s.equals("MAX") || s.equals("POWER")
					|| s.equals("INVERSE SUB")) {
				par.setEnabled(true);
				par.setText("");
				String ss = par.getText();
				try {
					int p = Integer.parseInt(ss);
					add.setEnabled(true);
				} catch(NumberFormatException e) {
					add.setEnabled(false);
				}
			}
			else {
				//par.setText("");
				par.setEnabled(false);
				add.setEnabled(true);
			}
		});
		
		cancel.addActionListener(ae -> {
			CompOperDialog.this.setVisible(false);
			CompOperDialog.this.clear();
		});
		
		par.addTextListener(te -> {
			String s = par.getText();
			try {
				int p = Integer.parseInt(s);			
				add.setEnabled(true);
			} catch(NumberFormatException e) {
				add.setEnabled(false);
			}
		});
		
		make.addActionListener(ae -> {		
			CompositeOperation co = new CompositeOperation(operations, nameTf.getText());
			Image.getInsance().addCompOper(co);
			this.setVisible(false);
			this.clear();
		});
		
		nameTf.addTextListener(te -> {
			if (nameTf.getText().equals("") || 
					Image.getInsance().getCompOper().containsKey(nameTf.getText())) {
				make.setEnabled(false);
			}
			else {
				if (operations.size() > 0) 
					make.setEnabled(true);
			}
		});
		
		add.addActionListener(ae -> {
			String s = list.getSelectedItem();
			switch(s) {
			case "ADD" : {
				Operation o = new Add(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "SUB" : {
				Operation o = new Sub(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "MUL" : {
				Operation o = new Mul(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "DIV" : {
				Operation o = new Div(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "INVERSE DIV" : {
				Operation o = new InverseDiv(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "INVERSE SUB" : {
				Operation o = new InverseSub(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "MIN" : {
				Operation o = new Min(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "MAX" : {
				Operation o = new Max(Integer.parseInt(par.getText()));
				o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "ABS" : {
				Operation o = new Abs();
				o.setParameter(Integer.parseInt(par.getText()));
				//operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "MEDIAN" : {
				Operation o = new Median();
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "POWER" : {
				Operation o = new Power(Integer.parseInt(par.getText()));
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "GREYSCALE" : {
				Operation o = new Greyscale();
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "INVERSION" : {
				Operation o = new Inversion();
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "BLACK & WHITE" : {
				Operation o = new BlackAndWhite();
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
					make.setEnabled(true);
				return;
			}
			case "LOG" : {
				Operation o = new Log();
				//o.setParameter(Integer.parseInt(par.getText()));
				operations.add(o);
				this.addLabel(o.getName());
				curP.revalidate();
				if (!nameTf.getText().equals("") &&
						(!Image.getInsance().getCompOper().containsKey(nameTf.getText())))
					make.setEnabled(true);
				return;
			}
			}
			Operation o = Image.getInsance().getCompOper().get(s);
			operations.add(o);
			this.addLabel(o.getName());
			curP.revalidate();
			if (!nameTf.getText().equals("") &&
					!Image.getInsance().getCompOper().containsKey(nameTf.getText()))
				make.setEnabled(true);
		});
	}
	
	private void addComoponents() {
		Panel nam = new Panel();
		nam.add(name); nam.add(nameTf);
		name.setForeground(Color.WHITE);
		name.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		nameTf.setForeground(Color.WHITE);
		nameTf.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		nameTf.setBackground(new Color(80, 80, 80));
		nam.setBackground(new Color(80, 80, 80));
		nam.setPreferredSize(new Dimension(this.getWidth(), 40));
		
		list.add("ADD"); list.add("SUB");
		list.add("INVERSE SUB"); list.add("DIV");
		list.add("INVERSE DIV"); list.add("MUL");
		list.add("MIN"); list.add("MAX");
		list.add("POWER"); list.add("LOG");
		list.add("GREYSCALE"); list.add("BLACK & WHITE");
		list.add("INVERSION"); list.add("MEDIAN");
		list.add("ABS");
		list.setBackground(new Color(55, 55, 55));
		list.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		list.setForeground(Color.WHITE);
		add.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		par.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		par.setBackground(new Color(80, 80, 80));
		par.setForeground(Color.WHITE);
		par.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		curP.setLayout(new GridLayout(0, 4));
		curP.setPreferredSize(new Dimension(new Dimension(this.getWidth(), 50)));	
		
		opPanel.add(list); opPanel.add(par); opPanel.add(add);
		opPanel.add(curP);
		opPanel.setBackground(new Color(80, 80, 80));
		opPanel.setPreferredSize(new Dimension(new Dimension(this.getWidth(), 130)));
		
		Panel opt = new Panel();
		opt.add(make); opt.add(cancel);
		opt.setPreferredSize(new Dimension(this.getWidth(), 40));
		opt.setBackground(new Color(70, 70, 70));
		make.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		make.setEnabled(false);
		
		add.setEnabled(false);
		
		this.add(nam);
		this.add(opPanel);	
		this.add(opt);
	}
	
	public CompOperDialog(Frame parent) {
		super(parent, "Nova kompozitna operacija", true);
		setLayout(new FlowLayout());
		((FlowLayout)this.getLayout()).setHgap(1);
		setBounds(600, 300, 450, 270);
		addComoponents();
		addListeners();
		setBackground(new Color(45, 45, 45));
		setResizable(false);
		setVisible(false);
	}
	
	private void addLabel(String s) {
		Label l = new Label(s);
		l.setForeground(Color.WHITE);
		l.setAlignment(Label.CENTER);
		l.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		curP.add(l);
	}
	
	public void clear() {
		nameTf.setText("");
		operations = new ArrayList<>();
		curP.removeAll();
		make.setEnabled(false);
		add.setEnabled(false);
		list.select(0);
		par.setText("");
		curP.revalidate();
	}
	
	public void addToList(String s) {
		list.add(s);
	}
}
