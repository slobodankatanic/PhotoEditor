#pragma once
#include "tinyxml2.h"
#include "Formatter.h"
#include "Operation.h"
#include "CompositeOperation.h"
#include <vector>
#include <map>

class MyFormatter : public Formatter {
public:
	MyFormatter() {}
	virtual ~MyFormatter() {}
	void importImage(std::string);
	void exportImage(std::string, Layer* img);
	void exportCompOper(CompositeOperation*, tinyxml2::XMLElement*, tinyxml2::XMLDocument&);
	Operation* importCompOper(tinyxml2::XMLElement*);
	unsigned char* getHeader() {}
};

