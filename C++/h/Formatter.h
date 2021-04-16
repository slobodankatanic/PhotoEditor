#pragma once
#include <iostream>
#include <vector>
#include "Pixel.h"
#include "Layer.h"

class Formatter {
protected:
	int width;
	int heigth;
	std::vector<std::vector<Pixel>> pixels;
public:
	Formatter() {}
	virtual ~Formatter() {}
	virtual void exportImage(std::string path, Layer* img = nullptr) = 0;
	virtual void importImage(std::string path) = 0;
	std::vector<std::vector<Pixel>>::iterator begin() { return pixels.begin(); }
	std::vector<std::vector<Pixel>>::iterator end() { return pixels.end(); }
	int getWidht() const { return width; }
	int getHeigth() const { return heigth; }
	std::vector<std::vector<Pixel>> getPixels() { return pixels; }
};

