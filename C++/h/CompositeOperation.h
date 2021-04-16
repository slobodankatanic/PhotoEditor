#pragma once
#include "Operation.h"
#include <iostream>
#include <vector>

class CompositeOperation : public Operation {
	std::vector<Operation*> operations;
public:
	CompositeOperation(std::vector<Operation*> o, std::string n) : operations(o), Operation(n) {}
	int getSize() const { return operations.size(); }
	void processPixel(Pixel&) {}
	virtual void processImage(Image*);
	std::vector<Operation*>::iterator begin() { return operations.begin(); }
	std::vector<Operation*>::iterator end() { return operations.end(); }
	virtual ~CompositeOperation();
	void write();
};

