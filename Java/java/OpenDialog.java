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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

public class OpenDialog extends Dialog {

	private TextField path = new TextField(30);
	private Label pathLab;
	private Button button = new Button("Otvori");
	private Button cancel = new Button("Otkazi");
	private String s, ext;
	
	private void addListeners() {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent we) {
				setVisible(false);
			}
		});
		button.addActionListener(ae -> {
			String com = path.getText();
			Pattern p = Pattern.compile("^.*.(...)$");
			Matcher m = p.matcher(com);
			try {
				if (m.matches()) {
					Formatter f = null;
					if (m.group(1).equalsIgnoreCase(ext)) {
						if (ext.equals("txt")) {
							f = new MyFormatter();
							Image.getInsance().clearInstance();
							Image.getInsance().disable();
							f.importImage(com);
							//new ImportThread(f, com, "txt").start();
							this.setVisible(false);
						}
						else if (ext.equals("fun")) {
							Image.getInsance().importCompOper(com);
							this.setVisible(false);
						}
					}
					else throw new FileFormatException();
				}
				else Image.getInsance().dm.setVisible(true);
			} catch(FileFormatException e) {
				Image.getInsance().dm.setVisible(true);
			} catch (IOException | ParserConfigurationException | SAXException e) {
				Image.getInsance().enable();
				Image.getInsance().clearInstance();
				Image.getInsance().dm.setVisible(true);
			}
			Image.getInsance().enable();
		});
		
		path.addTextListener(te -> {
			if (path.getText().length() == 0) button.setEnabled(false);
			else button.setEnabled(true);
		});
		
		cancel.addActionListener(ae -> {
			this.setVisible(false);
		});
	}
	
	public OpenDialog(Frame parent, String s, String ext) {
		super(parent, "Otvori fajl", true);
		setBounds(550, 250, 400, 200);
		setLayout(new BorderLayout());
		((BorderLayout)this.getLayout()).setVgap(12);
		
		button.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));
		button.setEnabled(false);
		cancel.setFont(new Font("Arial", Font.TRUETYPE_FONT, 16));
		
		pathLab = new Label(s);
		pathLab.setAlignment(Label.CENTER);
		pathLab.setFont(new Font("Arial", Font.TRUETYPE_FONT, 19));
		pathLab.setForeground(Color.WHITE);
		path.setFont(new Font("Arial", Font.TRUETYPE_FONT, 20));
		path.setBackground(new Color(50, 50, 50));
		path.setForeground(Color.WHITE);
		
		Panel panel = new Panel();
		panel.add(pathLab);
		panel.add(path);
		panel.add(button);
		panel.add(cancel);
		
		add(panel, BorderLayout.CENTER);
		addListeners();
		setVisible(false);
		
		setBackground(Color.DARK_GRAY);
		
		this.s = s; this.ext = ext;
	}		
	
	public TextField getPathTextField() { return path; }
}
