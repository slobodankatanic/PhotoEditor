#pragma once
#include "Formatter.h"

class PAMFormatter : public Formatter {
public:
	PAMFormatter() {}
	virtual ~PAMFormatter() {}
	virtual void exportImage(std::string path, Layer* img);
	virtual void importImage(std::string path);
};

