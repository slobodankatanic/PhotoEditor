package photoshop;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class DeleteDialog extends Dialog {
	
	private Button yes = new Button("Yes"), no = new Button("No");
	private Label label;
	private Object obj;
	private String s;
	
	private void addListeners() {
		yes.addActionListener(ae -> {
			if (obj instanceof Selection) 
				Image.getInsance().removeSelection(obj);
			else if (obj instanceof Layer)
				Image.getInsance().removeLayer(obj);
			this.setVisible(false);
		});
		no.addActionListener(ae -> {
			this.setVisible(false);
		});
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				DeleteDialog.this.setVisible(false);
			}
		});
	}
	
	public DeleteDialog(Frame parent, Object o, String s) {
		super(parent, true);
		setBounds(750, 400, 330, 150);
		setLayout(new FlowLayout(FlowLayout.CENTER));
		
		label = new Label(s);
		label.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		label.setAlignment(Label.CENTER);
		yes.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		no.setFont(new Font("Arial", Font.TRUETYPE_FONT, 18));
		
		add(label); add(yes); add(no);
		setVisible(false);	
		addListeners();
		
		this.obj = o; this.s = s;
	}
}
