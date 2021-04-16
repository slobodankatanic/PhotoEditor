#pragma once
#include "Formatter.h"

class BMPFormatter : public Formatter {
public:
	BMPFormatter() {}
	virtual ~BMPFormatter() {}
	virtual void exportImage(std::string path, Layer* img);
	virtual void importImage(std::string path);
};

