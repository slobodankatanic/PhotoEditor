package photoshop;

import java.awt.BorderLayout;
import java.awt.TextField;

import javax.swing.border.Border;

class Add extends Operation {
	; 
	public Add(int num) {
		super("ADD");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "add " + num; }
};

class Sub extends Operation {
	; 
	public Sub(int num) {
		super("SUB");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "sub " + num; }
};

class InverseSub extends Operation {
	; 
	public InverseSub(int num) {
		super("INV. SUB");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "inversesub " + num; }
};

class Mul extends Operation {
	; 
	public Mul(int num) {
		super("MUL");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "mul " + num; }
};

class Div extends Operation {	
	; 
	public Div(int num) {
		super("DIV");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "div " + num; }
};

class InverseDiv extends Operation {	
	; 
	public InverseDiv(int num) {
		super("INV. DIV");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "inversediv " + num; }
};

class Greyscale extends Operation {
	public Greyscale() {
		super("GREYSCALE");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "greyscale" + " 0"; }
};

class BlackAndWhite extends Operation {
	public BlackAndWhite() {
		super("BLK & WHT");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "blackandwhite" + " 0"; }
};

class Inversion extends Operation {
	public Inversion() {
		super("INVERSION");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "inversion" + " 0"; }
};

class Median extends Operation {
	public Median() {
		super("MEDIAN");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "median" + " 0"; }
};

class Power extends Operation {
	; 
	public Power(int num) {
		super("POWER");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	@Override
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return "power" + " " + num; }
}

class Log extends Operation {
	public Log() {
		super("LOG");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "log" + " 0"; }
}

class Abs extends Operation {
	public Abs() {
		super("ABS");
		this.add(apply, BorderLayout.CENTER);
		this.field.setText("0");
	}	
	@Override
	public String getName() { return this.name; }
	@Override
	public String toString() { return "abs" + " 0"; }
}

class Min extends Operation {
	public Min(int num) {
		super("MIN");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	@Override
	public String getName() { return this.name + " " + num; }
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String toString() { return "min " + num; }
}

class Max extends Operation {
	public Max(int num) {
		super("MAX");
		this.num = num;
		this.add(apply, BorderLayout.WEST);
		this.add(field, BorderLayout.EAST);
	}
	public void setParameter(int n) { num = n; }
	public int getParameter() { return num; }
	@Override
	public String getName() { return this.name + " " + num; }
	@Override
	public String toString() { return "max " + num; }
}