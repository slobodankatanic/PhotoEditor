package photoshop;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.List;

public class CompositeOperation extends Operation {
	
	private List<Operation> operations;
	
	public CompositeOperation(List<Operation> op, String s) {
		super(s);
		this.operations = op;
		nameLabel.setText(name);
		nameLabel.setBackground(new Color(80, 80, 80));
		nameLabel.setForeground(Color.WHITE);
		nameLabel.setFont(new Font("Arial", Font.BOLD, 15));
		//nameLabel.setPreferredSize(new Dimension(20, this.getHeight()));
		compAp.setLabel("CO");
		compAp.setFont(new Font("Arial", Font.BOLD, 14));
		compAp.setBackground(new Color(80, 80, 80));
		compAp.setForeground(Color.WHITE);
		compAp.setPreferredSize(new Dimension(30, 20));
		this.field.setText("0");
		this.add(nameLabel, BorderLayout.CENTER);
		this.add(compAp, BorderLayout.EAST);
	}
	
	public List<Operation> getOperations() { return operations; }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < operations.size(); i++) {
			sb.append(operations.get(i).getName());
			if (i != operations.size() - 1) {
				sb.append(" | ");
			}
		}
		return sb.toString();
	}
}
