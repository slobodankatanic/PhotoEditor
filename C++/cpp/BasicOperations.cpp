#include "Pixel.h"
#include "BasicOperations.h"
#include <algorithm>
#include <cmath>
#include "Exceptions.h"
#include "Image.h"
#include <algorithm>
#include <valarray>

void Add::processPixel(Pixel& pixel) {

	pixel.setRed(pixel.getRed() + this->num);
	pixel.setGreen(pixel.getGreen() + this->num);
	pixel.setBlue(pixel.getBlue() + this->num);
}

void Sub::processPixel(Pixel& pixel) {

	pixel.setRed(pixel.getRed() - this->num);
	pixel.setGreen(pixel.getGreen() - this->num);
	pixel.setBlue(pixel.getBlue() - this->num);
}

void InverseSub::processPixel(Pixel& pixel) {

	pixel.setRed(this->num - pixel.getRed());
	pixel.setGreen(this->num - pixel.getGreen());
	pixel.setBlue(this->num - pixel.getBlue());
}

void Mul::processPixel(Pixel& pixel) {

	pixel.setRed(pixel.getRed() * this->num);
	pixel.setGreen(pixel.getGreen() * this->num);
	pixel.setBlue(pixel.getBlue() * this->num);
}

void Div::processPixel(Pixel& pixel) {

	pixel.setRed((this->num != 0) ? (pixel.getRed() / this->num) : 255);
	pixel.setGreen((this->num != 0) ? (pixel.getGreen() / this->num) : 255);
	pixel.setBlue((this->num != 0) ? (pixel.getBlue() / this->num) : 255);
}

void InverseDiv::processPixel(Pixel& pixel) {

	pixel.setRed((pixel.getRed() != 0) ? (this->num / pixel.getRed()) : 255);
	pixel.setGreen((pixel.getGreen() != 0) ? (this->num / pixel.getGreen()) : 255);
	pixel.setBlue((pixel.getBlue() != 0) ? (this->num / pixel.getBlue()) : 255);
}

void Greyscale::processPixel(Pixel& pixel) {

	int res = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
	pixel.setRed(res);
	pixel.setGreen(res);
	pixel.setBlue(res);
}

void BlackAndWhite::processPixel(Pixel& pixel) {

	int res = (pixel.getRed() + pixel.getGreen() + pixel.getBlue()) / 3;
	if (res < 127) {
		pixel.setRed(0);
		pixel.setGreen(0);
		pixel.setBlue(0);
	}
	else {
		pixel.setRed(255);
		pixel.setGreen(255);
		pixel.setBlue(255);
	}
}

void Inversion::processPixel(Pixel& pixel) {

	pixel.setRed(255 - pixel.getRed());
	pixel.setGreen(255 - pixel.getGreen());
	pixel.setBlue(255 - pixel.getBlue());
}
	
void Median::processImage(Image* img) {

	if (img->numOfActLay() == 0) throw NoActiveLayers();

	for_each(img->begin(), img->end(), [this, img](Layer* layer) {

		if (layer->isActive()) {

			std::vector<std::vector<Pixel>> mp = layer->getPixels();
			int row = 0;
			for_each(layer->begin() + 1, layer->end() - 1, [img, this, &row, &mp](std::vector<Pixel>& vp) {

				row++;
				int column = 0;
				for_each(vp.begin() + 1, vp.end() - 1, [&](Pixel& pixel) {

					column++;
					if ((img->numOfSelections() == 0) || (!(img->activeSeletions())) || any_of(img->beginSelection(), img->endSelection(), [row, column](auto s) {
						return s.second->isActive() && s.second->isActiveCoordinates(column, row); })
						) {
						
						std::vector<Pixel> v{ mp[row - 1][column - 1], mp[row - 1][column], mp[row - 1][column + 1],
						mp[row][column - 1], mp[row][column], mp[row][column + 1], mp[row + 1][column - 1],
						mp[row + 1][column], mp[row + 1][column + 1] };
					
						int n = 4;

						sort(v.begin(), v.end(), [](Pixel& p1, Pixel& p2) {
							return p1.getRed() < p2.getRed();
						});
						pixel.setRed(v[n].getRed());

						sort(v.begin(), v.end(), [](Pixel& p1, Pixel& p2) {
							return p1.getGreen() < p2.getGreen();
						});
						pixel.setGreen(v[n].getGreen());

						sort(v.begin(), v.end(), [](Pixel& p1, Pixel& p2) {
							return p1.getBlue() < p2.getBlue();
						});
						pixel.setBlue(v[n].getBlue());
					}
				});
			});
		}
	});
}

void Power::processPixel(Pixel& pixel) {

	pixel.setRed(pow(pixel.getRed(), this->num));
	pixel.setGreen(pow(pixel.getGreen(), this->num));
	pixel.setBlue(pow(pixel.getBlue(), this->num));
}

void Log::processPixel(Pixel& pixel) {

	pixel.setRed((int)log2(pixel.getRed()));
	pixel.setGreen((int)log2(pixel.getGreen()));
	pixel.setBlue((int)log2(pixel.getBlue()));
}

void Abs::processPixel(Pixel& pixel) {

	pixel.setRed(abs(pixel.getRed()));
	pixel.setGreen(abs(pixel.getGreen()));
	pixel.setBlue(abs(pixel.getBlue()));
}

void Min::processPixel(Pixel& pixel) {

	if (pixel.getRed() > this->num) pixel.setRed(this->num);
	if (pixel.getGreen() > this->num) pixel.setGreen(this->num);
	if (pixel.getBlue() > this->num) pixel.setBlue(this->num);
}

void Max::processPixel(Pixel& pixel) {

	if (pixel.getRed() < this->num) pixel.setRed(this->num);
	if (pixel.getGreen() < this->num) pixel.setGreen(this->num);
	if (pixel.getBlue() < this->num) pixel.setBlue(this->num);
}