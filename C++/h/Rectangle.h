#pragma once
#include <utility>
#include <iostream>
#include "Exceptions.h"

class Rectangle {
	std::pair<int, int> position;
	std::pair<int, int> dimensions;
public:
	Rectangle(int x, int y, int w, int h) {
		if (x < 0 || y < 0 || w <= 0 || h <= 0) throw InputException();
		position.first = x; position.second = y;
		dimensions.first = h; dimensions.second = w;
	}
	std::pair<int, int> getPosition() const { return position; }
	std::pair<int, int> getDimensions() const { return dimensions; }
	void setPosition(int x, int y) { position.first = x; position.second = y; }
	friend std::ostream& operator<<(std::ostream&, Rectangle&);
};

