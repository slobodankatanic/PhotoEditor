#pragma once
#include "Operation.h"

class Add : public Operation {
	int num;
public:
	Add(int num = 0, std::string n = "add") : num(num), Operation(n) {}
	~Add() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class Sub : public Operation {
	int num;
public:
	Sub(int num = 0, std::string n = "sub") : num(num), Operation(n) {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	virtual ~Sub() {}
	void processPixel(Pixel&);
};

class InverseSub : public Operation {
	int num;
public:
	InverseSub(int num = 0, std::string n = "inversesub") : num(num), Operation(n) {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	virtual ~InverseSub() {}
	void processPixel(Pixel&);
};

class Mul : public Operation {
	int num;
public:
	Mul(int num = 0, std::string n = "mul") : num(num), Operation(n) {}
	~Mul() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class Div : public Operation {
	int num;
public:
	Div(int num = 0, std::string n = "div") : num(num), Operation(n) {}
	~Div() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class InverseDiv : public Operation {
	int num;
public:
	InverseDiv(int num = 0, std::string n = "inversediv") : num(num), Operation(n) {}
	~InverseDiv() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class Greyscale : public Operation {
public:
	Greyscale(std::string n = "greyscale") : Operation(n) {}
	void processPixel(Pixel&);
	~Greyscale() {}
};

class BlackAndWhite : public Operation {
public:
	BlackAndWhite(std::string n = "blackandwhite") : Operation(n) {}
	void processPixel(Pixel&);
	virtual ~BlackAndWhite() {}
};

class Inversion : public Operation {
public:
	Inversion(std::string n = "inversion") : Operation(n) {}
	virtual ~Inversion() {}
	void processPixel(Pixel&);
};

class Median : public Operation {
public:
	Median(std::string n = "median") : Operation(n) {}
	virtual ~Median() {}
	void processPixel(Pixel&) {}
	void processImage(Image*);
};

class Power : public Operation {
	int num;
public:
	Power(int num = 0, std::string n = "power") : num(num), Operation(n) {}
	~Power() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class Log : public Operation {
public:
	Log(std::string n = "log") : Operation(n) {}
	virtual ~Log() {}
	void processPixel(Pixel&);
};

class Abs : public Operation {
public:
	Abs(std::string n = "abs") : Operation(n) {}
	virtual ~Abs() {}
	void processPixel(Pixel&);
};

class Min : public Operation {
	int num;
public:
	Min(int num = 0, std::string n = "min") : num(num), Operation(n) {}
	~Min() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};

class Max : public Operation {
	int num;
public:
	Max(int num = 0, std::string n = "max") : num(num), Operation(n) {}
	~Max() {}
	void setParameter(int n) { num = n; }
	int getParameter() { return num; }
	void processPixel(Pixel&);
};