#include "CompositeOperation.h"
#include <algorithm>
#include <iostream>
#include <string>

CompositeOperation::~CompositeOperation() {

	for (Operation* o : operations) {
		delete o;
	}
	operations.erase(operations.begin(), operations.end());
}

/*void CompositeOperation::processPixel(Pixel& pixel, std::vector<Pixel> v) {

	for_each(operations.begin(), operations.end(), [&pixel, &v](Operation* op) {
		op->processPixel(pixel, v);
	});
}*/

void CompositeOperation::write() {

	std::cout << getName() << " (";

	auto it = operations.begin();
	std::cout << *(*it);
	for_each(operations.begin() + 1, operations.end(), [](Operation* op) {
		std::cout << ", " << *op;
	});
	std::cout << ")";
}

void CompositeOperation::processImage(Image* img) {

	for_each(operations.begin(), operations.end(), [img](Operation* o) {
		o->processImage(img);
	});
}