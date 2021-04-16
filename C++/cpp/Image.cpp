#pragma once
#include "Image.h"
#include "BasicOperations.h"
#include <iostream>
#include <algorithm>
#include "Exceptions.h"
#include "tinyxml2.h"

Image* Image::instance = nullptr;
std::pair<int, int> Image::imageDimensions = std::pair<int, int>(0, 0);

Image::Image() {

	basicOperations["add"] = new Add();
	basicOperations["sub"] = new Sub();
	basicOperations["greyscale"] = new Greyscale();
	basicOperations["blackandwhite"] = new BlackAndWhite();
	basicOperations["inversion"] = new Inversion();
	basicOperations["median"] = new Median();
	basicOperations["inversesub"] = new InverseSub();
	basicOperations["inversediv"] = new InverseDiv();
	basicOperations["div"] = new Div();
	basicOperations["mul"] = new Mul();
	basicOperations["power"] = new Power();
	basicOperations["abs"] = new Abs();
	basicOperations["min"] = new Min();
	basicOperations["max"] = new Max();
	basicOperations["log"] = new Log();
	saved = true;
	finalLayer = nullptr;
}

bool Image::activeSeletions() const {

	return any_of(imageSelections.begin(), imageSelections.end(), [](std::pair<std::string, Selection*> s) {
		return s.second->isActive();
	});
}

void Image::resizeLayers(int w, int h) {

	for_each(imageLayers.begin(), imageLayers.end(), [h, w](Layer* layer) {
		layer->resizeLayer(w, h);
	});

	imageDimensions.first = h;
	imageDimensions.second = w;
}

void Image::addLayer(Layer* layer) {

	if (imageLayers.size() == 0) {
		imageLayers.push_back(layer);
		imageDimensions.first = layer->getDimensions().first;
		imageDimensions.second = layer->getDimensions().second;
		return;
	}

	if (layer->getDimensions().first == imageDimensions.first &&
		layer->getDimensions().second == imageDimensions.second) {
		imageLayers.push_back(layer);
		return;
	}

	if (layer->getDimensions().first > imageDimensions.first &&
		layer->getDimensions().second > imageDimensions.second) {
		resizeLayers(layer->getDimensions().second, layer->getDimensions().first);
		imageLayers.push_back(layer);
		return;
	}

	if (layer->getDimensions().first < imageDimensions.first &&
		layer->getDimensions().second < imageDimensions.second) {
		layer->resizeLayer(imageDimensions.second, imageDimensions.first);
		imageLayers.push_back(layer);
		return;
	}

	int h = std::max(layer->getDimensions().first, imageDimensions.first);
	int w = std::max(layer->getDimensions().second, imageDimensions.second);

	layer->resizeLayer(w, h);
	resizeLayers(w, h);
	imageLayers.push_back(layer);
}

void Image::deleteInstance() {

	Image* img = Image::getInstance();
	for (Layer* l : img->imageLayers) delete l;
	for (auto s : img->imageSelections) delete s.second;
	for (auto o : img->compositeOperation) delete o.second;

	img->imageLayers.erase(img->imageLayers.begin(), img->imageLayers.end());
	img->imageSelections.erase(img->imageSelections.begin(), img->imageSelections.end());
	img->compositeOperation.erase(img->compositeOperation.begin(), img->compositeOperation.end());

	img->imageDimensions.first = 0;
	img->imageDimensions.second = 0;

	img->saved = true;
	delete img->finalLayer;
	img->finalLayer = nullptr;
}

void Image::addSelection(Selection* s) {

	if (imageSelections.find(s->getName()) == imageSelections.end()) {
		imageSelections[s->getName()] = s;
	}
	else throw NameDuplicateException();
}

void Image::addCompOperation(Operation* o) { 

	if (compositeOperation.find(o->getName()) == compositeOperation.end()) {
		compositeOperation[o->getName()] = o;
	}
	else {
		delete o; throw NameDuplicateException();
	}
}

void Image::performBasicOperation(std::string op, int par) {

	if (numOfActLay() == 0) throw NoActiveLayers();

	basicOperations[op]->setParameter(par);
	basicOperations[op]->processImage(this);

	limitPixels();
}

void Image::performCompOperation(std::string co) {

	if (numOfActLay() == 0) throw NoActiveLayers();
	if (numOfCompOper() == 0) throw NoCompOper();
	if (compositeOperation.find(co) == compositeOperation.end()) throw InputException();

	compositeOperation[co]->processImage(this);

	limitPixels();
}

void Image::limitPixels() {

	for_each(this->begin(), this->end(), [&](Layer* layer) {
		if (layer->isActive()) {
			for_each(layer->begin(), layer->end(), [&](std::vector<Pixel>& vp) {
				for_each(vp.begin(), vp.end(), [&](Pixel& pixel) {

					if (pixel.getRed() > 255) pixel.setRed(255);
					else if (pixel.getRed() < 0) pixel.setRed(0);

					if (pixel.getGreen() > 255) pixel.setGreen(255);
					else if (pixel.getGreen() < 0) pixel.setGreen(0);

					if (pixel.getBlue() > 255) pixel.setBlue(255);
					else if (pixel.getBlue() < 0) pixel.setBlue(0);
				});
			});
		}
	});
}

Layer* Image::createFinalImage() {

	std::vector<Layer*>::iterator it = find_if(begin(), end(), [](Layer* l) {
		return l->isVisible();
	});

	if (it == end()) throw NoVisibleLayers();	
	
	finalLayer = new Layer("_Empty_");
	finalLayer->setPixels((*it)->getPixels());
	finalLayer->setTransparency((*it)->getTransparency());
	finalLayer->setVisibility();
	finalLayer->setActivity();
	finalLayer->setDimensions((*it)->getDimensions());

	for_each(it + 1, end(), [this](Layer* layer) {

		if (layer->isVisible()) {

			for (auto itlr = layer->begin(), itflr = finalLayer->begin(); itlr != layer->end() &&
				itflr != finalLayer->end(); itlr++, itflr++) {

				for (auto itlk = itlr->begin(), itflk = itflr->begin(); itlk != itlr->end() &&
					itflk != itflr->end(); itlk++, itflk++) {

					int a0 = ((itflk->getAlpha())*(finalLayer->getTransparency())) / 100;
					int a1 = ((itlk->getAlpha())*(layer->getTransparency())) / 100;

					int newAlpha = a0 + ((255 - a0) * a1)/255; 
					if (newAlpha == 0) { itflk->setAlpha(newAlpha); continue; }
					int newRed = (itflk->getRed() * a0)/newAlpha + (itlk->getRed() * (255 - a0)*a1)/(newAlpha * 255);
					int newGreen = (itflk->getGreen() * a0) / newAlpha + (itlk->getGreen() * (255 - a0)*a1) / (newAlpha * 255);
					int newBlue = (itflk->getBlue() * a0) / newAlpha + (itlk->getBlue() * (255 - a0)*a1) / (newAlpha * 255);

					itflk->setAlpha(newAlpha); itflk->setRed(newRed);
					itflk->setGreen(newGreen); itflk->setBlue(newBlue);
				}
			}
			finalLayer->setTransparency(100);
		}
	});

	return finalLayer;
}

void Image::fillSelection(std::string s, int r, int g, int b) {

	if (imageSelections.find(s) == imageSelections.end()) throw SelectionNameException();
	if (r < 0 || r > 255 || g < 0 || g > 255 || b < 0 || b > 255) throw InputException();
	if (numOfActLay() == 0) throw NoActiveLayers();

	for_each(this->begin(), this->end(), [this, s, r, g, b](Layer* layer) {
		if (layer->isActive()) {
			int num1 = -1;
			for_each(layer->begin(), layer->end(), [&](std::vector<Pixel>& vp) {

				for_each(vp.begin(), vp.end(), [&](Pixel& pixel) {

					num1++;
					if (imageSelections[s]->isActiveCoordinates(num1%Image::getImageDimensions().second,
						num1 / Image::getImageDimensions().second)) {

						pixel.setRed(r); pixel.setGreen(g); pixel.setBlue(b);
					}
				});
			});
		}
	});
}

void Image::setLayerVis(int num) {

	if (num <= imageLayers.size() && num > 0) {
		imageLayers[num - 1]->setVisibility();
	}
	else if (imageLayers.size() > 0) throw InputException();
	else throw NoLayers();
}

void Image::resetLayerVis(int num) {

	if (num <= imageLayers.size() && num > 0) {
		imageLayers[num - 1]->resetVisibility();
	}
	else if (imageLayers.size() > 0) throw InputException();
	else throw NoLayers();
}

void Image::activateLayer(int num) {

	if (num <= imageLayers.size() && num > 0) {	
		imageLayers[num - 1]->setActivity();
	}
	else if (imageLayers.size() > 0) throw InputException();
	else throw NoLayers();
}

void Image::deactivateLayer(int num) {

	if (num <= imageLayers.size() && num > 0) {
		imageLayers[num - 1]->resetActivity();
	}
	else if (imageLayers.size() > 0) throw InputException();
	else throw NoLayers();
}

void Image::activateSelection(std::string s) {

	if (imageSelections.find(s) != imageSelections.end()) {
		imageSelections[s]->setActivity();
	}
	else throw SelectionNameException();
}

void Image::deactivateSelection(std::string s) {

	if (imageSelections.find(s) != imageSelections.end()) {
		imageSelections[s]->resetActivity();
	}
	else throw SelectionNameException();
}

void Image::deleteLayer(int num) {

	if (num <= imageLayers.size() && num > 0) {
		delete imageLayers[num - 1];
		imageLayers.erase(imageLayers.begin() + num - 1);
		if (numOfLayers() == 0) {
			imageDimensions.first = 0;
			imageDimensions.second = 0;
		}
	}
	else if (imageLayers.size() > 0) throw InputException();
	else throw NoLayers();
}

void Image::deleteSelection(std::string s) {

	if (imageSelections.find(s) != imageSelections.end()) {
		delete imageSelections[s];
		imageSelections.erase(imageSelections.find(s));
	}
	else throw SelectionNameException();
}

void Image::importCompOper(std::string path) {

	int p = path.size() - 4;
	if ((path.find(".fun", p) != path.npos) || (path.find(".FUN", p) != path.npos) ||
		(path.find(".Fun", p) != path.npos) || (path.find(".fUn", p) != path.npos) ||
		(path.find(".fuN", p) != path.npos) || (path.find(".FUn", p) != path.npos) ||
		(path.find(".FuN", p) != path.npos) || (path.find(".fUN", p) != path.npos)) {

		Image* image = Image::getInstance();

		tinyxml2::XMLDocument xmldoc;
		tinyxml2::XMLError eResult = xmldoc.LoadFile(path.c_str());
		XMLCheckResult(eResult);

		tinyxml2::XMLNode *root = xmldoc.FirstChild();	
		if (root == nullptr) throw FileFormatException();

		tinyxml2::XMLElement *xmlelem = root->FirstChildElement("compoperation");
		if (xmlelem == nullptr) throw FileFormatException();

		MyFormatter* myf = new MyFormatter();
		Operation* o = myf->importCompOper(xmlelem);
		image->addCompOperation(o);
	}
	else throw FileFormatException();
}

void Image::exportCompOper(std::string name, std::string path) {

	if (compositeOperation.find(name) != compositeOperation.end()) {
		int p = path.size() - 4;
		if ((path.find(".fun", p) != path.npos) || (path.find(".FUN", p) != path.npos) ||
			(path.find(".Fun", p) != path.npos) || (path.find(".fUn", p) != path.npos) ||
			(path.find(".fuN", p) != path.npos) || (path.find(".FUn", p) != path.npos) ||
			(path.find(".FuN", p) != path.npos) || (path.find(".fUN", p) != path.npos)) {
			
			MyFormatter* myf = new MyFormatter();

			tinyxml2::XMLDocument xmldoc;

			tinyxml2::XMLElement* xmlelem = xmldoc.NewElement("compoperations");
			xmldoc.InsertFirstChild(xmlelem);

			CompositeOperation* co = dynamic_cast<CompositeOperation*>(compositeOperation[name]);
			if (co == nullptr) throw DowncastException();
			myf->exportCompOper(co, xmlelem, xmldoc);

			xmlelem->SetAttribute("num", 1);
			//xmlelem->InsertEndChild(xmlelem);
			tinyxml2::XMLError eResult = xmldoc.SaveFile(path.c_str());
			XMLCheckResult(eResult);
		}
	}
	else {
		throw CompOperationNameException();
	}
}

void Image::setLayerTransp(int l, int t) {

	if (numOfLayers() <= 0) throw NoLayers();
	if (numOfLayers() < l || t > 100 || t < 0) throw InputException();
	imageLayers[l - 1]->setTransparency(t);
}

void Image::addImageToEmptyLayer(int, std::string) {


}

void Image::displayLayerInformations() {

	if (numOfLayers() == 0) {
		std::cout << "Trenutno ne postoji nijedan sloj" << std::endl;
		return;
	}

	int no = 1;
	for_each(imageLayers.begin(), imageLayers.end(), [&no](Layer* l) {

		std::cout << "Sloj " << no++ << ": " << *l << std::endl;
	});
}

void Image::displaySelectionInformations() {

	if (numOfSelections() == 0) {
		std::cout << "Trenutno ne postoji nijedna selekcija" << std::endl;
		return;
	}

	for_each(imageSelections.begin(), imageSelections.end(), [](auto s) {
		std::cout << *(s.second) << std::endl;
	});
}

bool Image::selNameExists(std::string s) const {

	if (imageSelections.find(s) != imageSelections.end()) return true;
	else return false;
}

bool Image::compOperNameExists(std::string s) const {

	if (compositeOperation.find(s) != compositeOperation.end()) return true;
	else return false;
}

int Image::numOfActLay() const {

	int cnt =  count_if(imageLayers.begin(), imageLayers.end(), [](Layer* layer) ->bool {
		return layer->isActive();
	});

	return cnt;
}

int Image::numOfVisLay() const {

	int cnt = count_if(imageLayers.begin(), imageLayers.end(), [](Layer* layer) ->bool {
		return layer->isVisible();
	});

	return cnt;
}

void Image::displayCompOperInfo() {

	if (numOfCompOper() == 0) {
		std::cout << "Trenutno ne postoji nijedna kompozitna operacija" << std::endl;
		return;
	}
	auto it = compositeOperation.begin();
	for_each(compositeOperation.begin(), compositeOperation.end(), [](auto co) {
		std::cout << *(co.second) << std::endl;
	});
}

Operation* Image::getCompOper(std::string s) {

	if (compositeOperation.find(s) != compositeOperation.end()) {
		return compositeOperation[s];
	}
	else throw NoCompOper();
}