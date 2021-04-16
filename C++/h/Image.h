#pragma once
#include "Layer.h"
#include "Selection.h"
#include "Operation.h"
#include <iostream>
#include <vector>
#include <map>
#include "MyFormatter.h"

class Image {
	bool saved;
	std::map<std::string, Selection*> imageSelections;
	std::vector<Layer*> imageLayers;
	static Image* instance;
	std::map<std::string, Operation*> basicOperations;
	std::map<std::string, Operation*> compositeOperation;
	static std::pair<int, int> imageDimensions;
	void limitPixels();
	Layer* finalLayer;
	Image();
public:
	static Image* getInstance() {
		if (instance == nullptr) {
			instance = new Image();
		}
		return instance;
	}
	friend class MyFormatter;
	std::vector<Layer*>::iterator begin() { return imageLayers.begin(); }
	std::vector<Layer*>::iterator end() { return imageLayers.end(); }
	std::map<std::string, Selection*>::iterator beginSelection() { return imageSelections.begin(); }
	std::map<std::string, Selection*>::iterator endSelection() { return imageSelections.end(); }
	bool activeSeletions() const;
	void resizeLayers(int, int);
	void addLayer(Layer*);
	static std::pair<int, int> getImageDimensions() { return imageDimensions; }
	void addSelection(Selection*);
	void addCompOperation(Operation* o);
	void performBasicOperation(std::string, int par = 0);
	void performCompOperation(std::string);
	void fillSelection(std::string, int, int, int);
	int numOfActLay() const;
	int numOfVisLay() const;
	int numOfLayers() const { return imageLayers.size(); }
	int numOfSelections() const { return imageSelections.size(); }
	int numOfCompOper() const { return compositeOperation.size(); }
	Layer* createFinalImage();
	Operation* getCompOper(std::string);
	void activateLayer(int);
	void deactivateLayer(int);
	void setLayerVis(int);
	void resetLayerVis(int);
	void activateSelection(std::string);
	void deactivateSelection(std::string);
	void setLayerTransp(int, int);
	void addImageToEmptyLayer(int, std::string);
	void importCompOper(std::string);
	void exportCompOper(std::string, std::string);
	bool isSaved() const { return saved; }
	void save() { saved = true; }
	void unsave() { saved = false; }
	void displayLayerInformations();
	void displaySelectionInformations();
	void displayCompOperInfo();
	bool selNameExists(std::string) const;
	bool compOperNameExists(std::string) const;
	void deleteLayer(int);
	void deleteSelection(std::string);
	std::map<std::string, Operation*>& getCompositeOperations() { return compositeOperation; }
	static void deleteInstance();
};
