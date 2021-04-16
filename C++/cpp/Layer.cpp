#pragma once
#include "Layer.h"
#include "Image.h"
#include "BMPFormatter.h"
#include "PAMFormatter.h"
#include "Exceptions.h"
#include <algorithm>

Layer::Layer(std::string path, int w, int h) :
	active(true), transparency(100), visible(true), dimensions(std::pair<int, int>(h, w)) {

	//dimensions.fi

	if (path == "_Empty_") {
		if (w < 1 || h < 1) throw InputException();
		createEmptyLayer(w, h);
		return;
	}

	Formatter *f;
	int p = path.size() - 4;
	if ((path.find(".BMP", p) != path.npos) || (path.find(".bmp", p) != path.npos) ||
		(path.find(".Bmp", p) != path.npos) || (path.find(".bMp", p) != path.npos) ||
		(path.find(".bmP", p) != path.npos) || (path.find(".BMp", p) != path.npos) ||
		(path.find(".BmP", p) != path.npos) || (path.find(".bMP", p) != path.npos)) {
		f = new BMPFormatter();
	}
	else {
		if ((path.find(".PAM", p) != path.npos) || (path.find(".pam", p) != path.npos) ||
			(path.find(".Pam", p) != path.npos) || (path.find(".pAm", p) != path.npos) ||
			(path.find(".paM", p) != path.npos) || (path.find(".PAm", p) != path.npos) ||
			(path.find(".PaM", p) != path.npos) || (path.find(".pAM", p) != path.npos)) {
			f = new PAMFormatter();
		}
		else throw FileFormatException();
	}
	
	f->importImage(path);

	//DODAJ ZASTITU ZA SLUCAJ DA JE NEISPRAVNA DATOTEKA
	//U BMPU SAMO PROVERI DA LI JE "BM", A U PAMU SAMO DA LI JE "P7"

	this->layerPixels = f->getPixels();
	this->dimensions.first = f->getHeigth();
	this->dimensions.second = f->getWidht();

	delete f;
}

Layer::~Layer() {

	//for (std::vector<Pixel*> vp : layerPixels) {
		//for (Pixel* p : vp) delete p;
	//}
}

void Layer::resizeLayer(int w, int h) {

	for_each(layerPixels.begin(), layerPixels.end(), [this, w](std::vector<Pixel>& vp) {
		for (int i = 0; i < w - dimensions.second; i++) vp.push_back(Pixel());
	});

	for (int i = 0; i < h - dimensions.first; i++) {
		std::vector<Pixel> tmpv;
		for (int j = 0; j < w; j++) tmpv.push_back(Pixel());
		layerPixels.push_back(tmpv);
	}

	dimensions.first = h;
	dimensions.second = w;
}

void Layer::createEmptyLayer(int w, int h) {

	if (w <= 0 || h <= 0) throw InputException();

	for (int i = 0; i < h; i++) {
		std::vector<Pixel> vp;
		for (int j = 0; j < w; j++) {
			vp.push_back(Pixel(0, 0, 0, 255));
		}
		layerPixels.push_back(vp);
		vp.erase(vp.begin(), vp.end());
	}
	transparency = 0;
	visible = false;
	active = false;
	dimensions.first = h;
	dimensions.second = w;
}

std::ostream& operator<<(std::ostream& os, Layer& l) {

	os << l.dimensions.second << "x" << l.dimensions.first << " | aktivan - " << 
		(l.active ? "DA" : "NE") << " | " << "vidljiv - " << 
		(l.visible ? "DA" : "NE") << " | providnost - " << l.transparency;

	return os;
}