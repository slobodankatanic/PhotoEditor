#pragma once
#include <vector>
#include "Pixel.h"

class Layer {
	std::pair<int, int> dimensions;
	bool visible;
	bool active;
	int transparency;
	std::vector<std::vector<Pixel>> layerPixels;
	void createEmptyLayer(int, int);
public:
	Layer(std::string path = "", int w = 100, int h = 100);
	bool isActive() const { return active; }
	void setActivity() { active = true; }
	void resetActivity() { active = false; }
	bool isVisible() const { return visible; }
	void setVisibility() { visible = true; }
	void resetVisibility() { visible = false; }
	void setTransparency(int t) { transparency = t; }
	int getTransparency() const { return transparency; }
	std::pair<int, int> getDimensions() { return dimensions; }
	void setDimensions(std::pair<int, int> d) { dimensions = d; }
	std::vector<std::vector<Pixel>>::iterator begin() { return layerPixels.begin(); }
	std::vector<std::vector<Pixel>>::iterator end() { return layerPixels.end(); }
	std::vector<std::vector<Pixel>>& getPixels() { return layerPixels; }
	void setPixels(std::vector<std::vector<Pixel>>& p) { layerPixels = p; }
	void resizeLayer(int, int);
	friend std::ostream& operator<<(std::ostream&, Layer&);
	~Layer();
};
