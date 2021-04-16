package photoshop;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class ExitDialog extends Dialog {

	private Button yes = new Button("Yes"), no = new Button("No");
	private Label label = new Label("Da li zaista zelite da napustite program?");
	private EndDialog expd = new EndDialog(this);
	
	private void addListeners() {
		yes.addActionListener(ae -> {
			this.setVisible(false);
			Image.getInsance().setTerminated();
			if (Image.getInsance().getLayers().size() > 0)
				expd.setVisible(true);
			else Image.getInsance().dispose();		
		});
		no.addActionListener(ae -> {
			this.setVisible(false);
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				ExitDialog.this.setVisible(false);
			}
		});
	}
	
	public ExitDialog(Frame parent) {
		super(parent, "Izlaz", true);
		setBounds(600, 300, 400, 150);
		setLayout(new FlowLayout());
		Font f = new Font("Arial", Font.TRUETYPE_FONT, 20);
		this.add(label);
		this.add(yes); this.add(no);
		label.setFont(f);
		yes.setFont(f);
		no.setFont(f);
		addListeners();
		setVisible(false);
	}
}

class EndDialog extends Dialog {

	private Button yes = new Button("Yes"), no = new Button("No");
	private Label label = new Label("Da li zelite da sacuvate sliku?");
	
	private void addListeners() {
		yes.addActionListener(ae -> {
			this.setVisible(false);
			Image.getInsance().getExpDi().setVisible(true);
		});
		no.addActionListener(ae -> {
			this.setVisible(false);
			Image.getInsance().dispose();
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				EndDialog.this.setVisible(false);
			}
		});
	}
	
	public EndDialog(ExitDialog exitDialog) {
		super(exitDialog, "Cuvanje", true);
		setBounds(600, 300, 330, 150);
		setLayout(new FlowLayout());
		Font f = new Font("Arial", Font.TRUETYPE_FONT, 20);
		this.add(label);
		this.add(yes); this.add(no);
		label.setFont(f);
		yes.setFont(f);
		no.setFont(f);
		addListeners();
		setVisible(false);
	}
}