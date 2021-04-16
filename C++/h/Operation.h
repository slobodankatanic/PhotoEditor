#pragma once
#include "Pixel.h"
#include <vector>
#include <iostream>
#include <string>

class Image;

class Operation {
protected:
	std::string name;
public:
	Operation(std::string n) : name(n) {}
	virtual void processPixel(Pixel&) {};
	virtual void setParameter(int) {}
	virtual int getParameter() { return 0; }
	virtual void write() { std::cout << name; }
	std::string getName() { return name; }
	virtual void processImage(Image*);
	friend std::ostream& operator<<(std::ostream& os, Operation& o) { o.write(); return os; }
	virtual ~Operation() {}
};