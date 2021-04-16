package photoshop;

import java.awt.Button;
import java.awt.Dialog;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Label;

public class DialogMessage extends Dialog {
	
	private Label message;
	private Button ok = new Button("OK");
	
	public DialogMessage(Frame parent, String mess) {
		super(parent, "Greska!", true);
		setLayout(new FlowLayout());
		setBounds(700, 450, 250, 150);
		this.message = new Label(mess);  
		this.message.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		
		this.add(message);
		this.add(ok);
		
		ok.addActionListener(ae -> {
			this.setVisible(false);
		});
		ok.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		
		this.setVisible(false);
	}
}
