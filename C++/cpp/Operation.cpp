#include "Operation.h"
#include "Exceptions.h"
#include "Image.h"
#include <algorithm>

void Operation::processImage(Image* img) {
	
	if (img->numOfActLay() == 0) throw NoActiveLayers();

	for_each(img->begin(), img->end(), [this, img](Layer* layer) {

		if (layer->isActive()) {

			int row = -1;
			for_each(layer->begin(), layer->end(), [img, this, &row](std::vector<Pixel>& vp) {

				row++;
				int column = -1;
				for_each(vp.begin(), vp.end(), [&](Pixel& pixel) {

					column++;
					if ((img->numOfSelections() == 0) || (!(img->activeSeletions())) || any_of(img->beginSelection(), img->endSelection(), [&](std::pair<std::string, Selection*> s) {
						return s.second->isActive() && s.second->isActiveCoordinates(column, row); })
						) {					

						this->processPixel(pixel);
					}
				});
			});
		}
	});
}