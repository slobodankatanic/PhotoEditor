#include "Rectangle.h"

std::ostream& operator<<(std::ostream& os, Rectangle& r) {

	os << "(" << r.position.first << ", " << r.position.second << ", " <<
		r.dimensions.second << ", " << r.dimensions.first << ")";

	return os;
}